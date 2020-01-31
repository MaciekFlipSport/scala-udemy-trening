package part4FaultTolerance

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props}

object ActorLifeCycle extends App {

    object StartChild
    class LifeCycleActor extends Actor with ActorLogging {

      override def preStart(): Unit = log.info("I'm starting")
      override def postStop(): Unit = log.info("I have stopped")

      override def receive: Receive = {
        case StartChild =>
          context.actorOf(Props[LifeCycleActor], "child")
      }
    }

  val system = ActorSystem("LifeDemo")
//  val parent = system.actorOf(Props[LifeCycleActor], "parent")
//  parent ! StartChild
//    parent ! PoisonPill

  /**
   *  REstart
   */
  object Fail
  object FailChild
  object CheckChild
  object Check

  class Parent extends Actor {
    private val child = context.actorOf(Props[Child], "supervisedChild")

    override def receive: Receive = {
      case FailChild => child ! Fail
      case CheckChild => child ! Check
    }
  }
  class Child extends Actor with ActorLogging {

    override def preStart(): Unit = log.info("supervised child starting")
    override def postStop(): Unit = log.info(s"supervised child stopped")

    override def preRestart(reason: Throwable, message: Option[Any]): Unit =
      log.info(s"supervised actor restarting becaouse of ${reason.getMessage}")

    override def postRestart(reason: Throwable): Unit =
      log.info("supervised actor restarted")

    override def receive: Receive = {
      case Fail =>
        log.warning("child will fail now")
        throw new RuntimeException("I failed")
      case Check =>
        log.info("alive and kicking")
    }
  }

  val supervisor = system.actorOf(Props[Parent], "supervisor")
  supervisor ! FailChild
  supervisor ! CheckChild

  //supervision strategy
}
