package simple

import akka.actor.{Actor, ActorSystem, Props}
import simple.MusicController.{Play, Stop}
import simple.MusicPlayer.{StartMusic, StopMusic}


// Music Controller Messages
object MusicController {

  // (Best Practice) => Should have its Message on it companion object
  sealed trait ControllerMsg

  case object Play extends ControllerMsg

  case object Stop extends ControllerMsg

  // (Best Practice) => Should have its prop on it companion object
  def props = Props[MusicController]

}

// Music Controller Actor
class MusicController extends Actor {
  override def receive: Receive = {
    case Play =>
      println("Music Started ... ")
    case Stop =>
      println("Music Stopped ... ")
  }
}


object MusicPlayer {

  // (Best Practice) => Should have its Message on it companion object
  sealed trait PlayMsg

  case object StartMusic extends PlayMsg

  case object StopMusic extends PlayMsg

  // (Best Practice) => Should have its prop on it companion object
  def props = Props[MusicPlayer]
}

class MusicPlayer extends Actor {
  override def receive: Receive = {
    case StartMusic =>
      val controller = context.actorOf(MusicController.props, "controller")
      controller ! MusicController.Play
    case StopMusic =>
      println("I dont want to stop music ")
    case _ =>
      println("Unknown Message")
  }
}


object Creation extends App {

  // Create the creation actor system
  val system = ActorSystem("creation")

  // Create the Music Player
  val player = system.actorOf(MusicPlayer.props, "player")

  // Send start music
  player ! StartMusic

  // Send stop music
  //  player ! StopMusic

  // Shutdown system
  system.terminate
}
