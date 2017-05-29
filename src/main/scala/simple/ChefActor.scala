package simple

import akka.actor.Actor


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




