package simple

import akka.actor.{Actor, ActorSystem, Props}


// =========== Items Enumeration ===========
sealed trait Item

case object Pizza extends Item

case object Omelet extends Item


// =========== Actor ===========
class ChefActor extends Actor {
  override def receive: Receive = {
    case Pizza => println("Pizza is delivered!")
    case Omelet => println("Omelet is delivered")
    case _ => println("This item does not exist.")
  }
}

object ChefActor extends App {

  // Define Actor System
  val system = ActorSystem("chef-system")

  // Create a Chef's actor
  val chefActor = system.actorOf(Props[ChefActor], "chef-ped")

  // Get items from waitress
  val items = Vector(Pizza, Omelet, "Sushi")

  // Send the items to chef actor
  items foreach (chefActor ! _)

  // terminate actor system
  system.terminate

}






