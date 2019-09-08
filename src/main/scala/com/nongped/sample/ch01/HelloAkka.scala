package com.nongped.sample.ch01

import akka.actor.{Actor, ActorSystem, Props}

// Define Actor Messages
case class WhoToGreet(who: String)

// Define Actor
class Greeter extends Actor {
  override def receive: Receive = {
    case WhoToGreet(who) => println(s"Hello $who")
    case _ => println("Invalid Message")
  }
}


object HelloAkka extends App {
  val actorSystem = ActorSystem("Hello-Akka")
  val greeter = actorSystem.actorOf(Props[Greeter], "greeter-01")

  greeter ! WhoToGreet("Worasit")

}
