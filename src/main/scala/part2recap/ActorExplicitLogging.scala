package part2recap

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.Logging

object ActorExplicitLogging extends  App {
  class SimpleActorWithExplicitLogger extends Actor {
    // #1 - explicit logging
    val logger = Logging(context.system, this)

    override def receive: Receive = {
      case message => logger.info(message.toString) //Log it
    }
  }

  val system = ActorSystem("LoggingDemo")
  val actor = system.actorOf(Props[SimpleActorWithExplicitLogger], "SimpleActorWithExplicitLogger")

  actor ! "Logging a simple message"
  // #2 - Actor Logging
  class ActorWithLogging extends Actor with ActorLogging {
    override def receive: Receive = {
//      case (a, b) => log.info(s"Two things: {} and {}", a, b) //same as the above
      case (a, b) => log.info(s"Two things: $a and $b")
      case message => log.info(message.toString)
    }
  }
  val simplerActor = system.actorOf(Props[ActorWithLogging], "ActorWithLogging")

  simplerActor ! "Logging a simple message by extending a trait"
  simplerActor ! (2, 3)
}
