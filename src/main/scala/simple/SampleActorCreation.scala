package simple

import akka.actor.{Actor, ActorSystem, Props}


object VideoController {

  sealed trait Controller

  case object Play extends Controller

  case object Stop extends Controller

  case object Rewind extends Controller

  def prop = Props[VideoController]
}

class VideoController extends Actor {
  override def receive: Receive = {
    case VideoController.Play =>
      println("The video is playing ....... ")
    case VideoController.Stop =>
      println("The video stopped ! ")
    case VideoController.Rewind =>
      println("The video is rewinding to ....... ")
    case _ =>
      println("The command does not exist")
  }
}

object VideoPlayer {

  sealed trait Player

  case object Play extends Player

  case object Stop extends Player

  case object Rewind extends Player

  def prop = Props[VideoPlayer]
}

class VideoPlayer extends Actor {

  def videoController = this.context.actorOf(VideoController.prop)

  override def receive: Receive = {
    case VideoPlayer.Play =>
      println("Played the video!")
      this.videoController ! VideoController.Play
    case VideoPlayer.Stop =>
      println("Stopped the video")
      this.videoController ! VideoController.Stop
    case VideoPlayer.Rewind =>
      println("Rewinding video")
      this.videoController ! VideoController.Rewind
    case _ =>
      println("Unknown command!!")
  }
}


object SampleActorCreation extends App {

  val system = ActorSystem("video-system")
  val videoPlayer = system.actorOf(Props[VideoPlayer], "video-player")

  videoPlayer ! VideoPlayer.Play
  Thread.sleep(5000)
  videoPlayer ! VideoPlayer.Rewind
  Thread.sleep(2000)
  videoPlayer ! VideoPlayer.Stop

  system.terminate

}
