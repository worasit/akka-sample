package simple

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import simple.Recorder.NewUser
import simple.Storage.AddUser
import scala.concurrent.duration._


case class User(username: String, email: String)

object Recorder {

  sealed trait RecoderMessage

  case class NewUser(user: User) extends RecoderMessage

  def props(checker: ActorRef, storage: ActorRef): Props = Props(new Recorder(checker, storage))

}

object Checker {

  sealed trait CheckerMessage

  case class CheckUser(user: User) extends CheckerMessage


  sealed trait CheckerResponse

  case class BlackUser(user: User) extends CheckerResponse

  case class WhiteUser(user: User) extends CheckerResponse

  def props: Props = Props[Checker]

}


object Storage {

  sealed trait StorageMessage

  case class AddUser(user: User) extends StorageMessage

  def props: Props = Props[Storage]

}

class Storage extends Actor {

  var users = List.empty[User]

  override def receive: Receive = {
    case AddUser(user) =>
      println(s"Storage: ${user} added")
      users = user :: users
  }
}


class Checker extends Actor {

  val blackList = List(
    User("Adam", "adam@gmail.com")
  )

  override def receive: Receive = {
    case Checker.CheckUser(user) =>
      if (blackList.contains(user)) {
        println(s"Checker: ${user} is in blacklist.")
        sender ! Checker.BlackUser(user)
      } else {
        println(s"Checker: ${user} is in white list.")
        sender ! Checker.WhiteUser(user)
      }
  }
}

class Recorder(checker: ActorRef, storage: ActorRef) extends Actor {

  import scala.concurrent.ExecutionContext.Implicits.global

  implicit val timeout = Timeout(5 seconds)

  override def receive: Receive = {
    case Recorder.NewUser(user) =>
      println(s"Recorder recieves a new user for ${user}")
      checker ? Checker.CheckUser(user) map {
        case Checker.WhiteUser(user) =>
          storage ! Storage.AddUser(user)
        case Checker.BlackUser(user) =>
          println(s"Recorder: ${user} is a black user.")
      }
  }

}


object TalkToActor extends App {

  // Create the 'talk-to-actor' actor system
  val system = ActorSystem("talk-to-actor")

  // Create the 'checker' actor
  val checker = system.actorOf(Checker.props, "checker")

  // Create the 'storage' actor
  val storage = system.actorOf(Storage.props, "storage")

  // Create the 'recorder' actor
  val recorder = system.actorOf(Recorder.props(checker, storage), "recorder")

  // send NewUser Message to Recorder
  recorder ! NewUser(User("Adam", "adam@gmail.com"))

  // shutdown the system
  //  system.terminate


}























