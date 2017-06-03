package simple

import akka.actor._

class Ares(athena: ActorRef) extends Actor {

  override def preStart(): Unit = {
    context.watch(athena)
  }

  override def postStop(): Unit = {
    println("Ares postStop...")
  }

  override def receive: Receive = {
    case Terminated =>
      context.stop(self)
    case DeadLetter =>
      println("DeadLetter.")
  }
}

class Athena extends Actor {

  override def postStop(): Unit = {
    println("Athena postStop...")
  }

  override def receive: Receive = {
    case msg =>
      println(s"Athena received ${msg}")
      context.stop(self);

  }
}


object Monitor extends App {

  val system = ActorSystem("monitor")

  val athena = system.actorOf(Props[Athena], "athena")

  val Ares = system.actorOf(Props(classOf[Ares], athena), "ares")

  athena ! "Hi"
}