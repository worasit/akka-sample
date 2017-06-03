package ref

import akka.actor.Actor
import ref.Counter.{Dec, Inc}


class Counter extends Actor {

  var count = 0

  override def receive: Receive = {
    case Inc(num) =>
      count += num
      println(s"Current count Inc: ${count}")
    case Dec(num) =>
      count -= num
      println(s"Current count Dec: ${count}")
  }
}
object Counter {
  final case class Inc(num: Int)
  final case class Dec(num: Int)
}
