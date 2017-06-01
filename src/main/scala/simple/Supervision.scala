package simple

import akka.actor.{Actor, ActorRef, ActorSystem, OneForOneStrategy, Props, SupervisorStrategy}
import akka.actor.Actor.Receive
import akka.actor.SupervisorStrategy.{Escalate, Restart, Resume, Stop}
import simple.Aphrodite.{RestartException, ResumeException, StopException}

import scala.concurrent.duration._

/**
  * Hera
  * |
  * |
  * |
  * Aphrodite
  */

class Aphrodite extends Actor {

  override def preStart(): Unit = {
    println("Aphrodite preStart hook....")
  }

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
    println("Aphrodite preRestart hook...")
    super.preRestart(reason, message)
  }

  override def postRestart(reason: Throwable): Unit = {
    println("Aphrodite postRestart hook...")
    super.postRestart(reason)
  }

  override def postStop(): Unit = {
    println("Aphrodite postStop...")
  }

  override def receive: Receive = {
    case "Resume" =>
      throw ResumeException
    case "Stop" =>
      throw StopException
    case "Restart" =>
      throw RestartException
    case _ =>
      throw new Exception
  }
}
object Aphrodite {
  case object ResumeException extends Exception
  case object StopException extends Exception
  case object RestartException extends Exception
}

class Hera extends Actor {

  var childRef: ActorRef = _

  override def supervisorStrategy: SupervisorStrategy = {
    OneForOneStrategy(maxNrOfRetries = 1, withinTimeRange = 1 second) {
      case ResumeException =>
        Resume
      case RestartException =>
        Restart
      case StopException =>
        Stop
      case _: Exception =>
        Escalate
    }
  }



  override def preStart(): Unit = {
    // Create Aphrodite Actor
    childRef = context.actorOf(Props[Aphrodite], "Aphrodite")
    Thread.sleep(100)
  }

  override def receive: Receive = {
    case msg =>
      println(s"Hera received ${msg}")
      childRef ! msg
  }
}

object Supervision extends App {
  val system = ActorSystem("supervision")
  val hera = system.actorOf(Props[Hera], "hera")

  hera ! "fail"
  Thread.sleep(1000)
  println()

  //  system.terminate
}
