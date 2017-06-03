package ref

import akka.actor.{Actor, ActorIdentity, ActorRef, ActorSystem, Identify, PoisonPill, Props}
import ref.Counter.{Dec, Inc}


class Watcher extends Actor {

  var counterRef: ActorRef = _

  val selection = context.actorSelection("/user/counter")

  selection ! Identify(None)

  //  use "Identify" message to to retrieve the ActorRef in a receive method
  override def receive: Receive = {
    case ActorIdentity(_, Some(ref)) =>
      println(s"Actor Reference for counter is ${ref}")
    case ActorIdentity(_, None) =>
      println("Actor selection for actor does not live :( ")
  }
}

/** *
  * Conclusion: you can use 'ActorRef' or 'ActorPath' for referencing to Actors. But the ActorPath cannot use as the reference in the watcher, so
  * you can use "Identify" message to to retrieve the ActorRef in a receive method
  */
object Watcher extends App {

  val system = ActorSystem("watch-actor-selection")

  //  Actor ref
  val counter: ActorRef = system.actorOf(Props[Counter], "counter")
  val watcher = system.actorOf(Props[Watcher], "watcher")

  println(s"Counter : ${counter}")

  counter ! Inc(10)

  // Actor path
  val counter2 = system.actorSelection("/user/counter")

  println(s"Counter2 : ${counter2.pathString}")

  counter2 ! Inc(20)

  // The result will display 30 since it is the same actor that contain a count value.

  system.terminate()
}


