package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem, AllForOneStrategy, Cancellable, OneForOneStrategy, Props, SupervisorStrategy, Timers}
import akka.actor.SupervisorStrategy.Stop

import scala.concurrent.duration._
object TimersSchedulers extends  App {

    class SimpleActor extends Actor with ActorLogging {

      override def receive: Receive = {
        case message => log.info(message.toString)
      }
    }

  val system = ActorSystem("SchecdulersTimersDemo")
  val simpleActor = system.actorOf(Props[SimpleActor])

  system.log.info("Scheduling reminder for simpleActor")

  import system.dispatcher

  /**
   *  Once scheduler
   */
//  system.scheduler.scheduleOnce(1 second){
//    simpleActor ! "reminder"
//  } // no need for this when system.dispatcher is imported ! (system.dispatcher)

  /**
   *  Repeated scheduler
   */
//  val routine: Cancellable = system.scheduler.schedule(1 second, 2 seconds) {
//    simpleActor ! "heartBeat"
//  }
//  system.scheduler.scheduleOnce(5 seconds) {
//    routine.cancel()
//  }

  /**
   * Exercise: implement a self-closing actor
   *
   * - if the actor receives the message (anything), you have one second to send it another message
   * - if the time window expires, the actor will stop itself
   * - if you send antoher message, the time window is reset
   */
  class SelfClosingActor extends Actor with ActorLogging {
    private var schedule = createTimeoutWindow()

    def createTimeoutWindow(): Cancellable = {
      context.system.scheduler.scheduleOnce(1 second) {
        self ! "timeout"
      }
    }

    override def receive: Receive = {
      case "timeout" =>
        log.info("Stopping myself")
        // Stopping ACTOR !!!
        context.stop(self)
      case message =>
        log.info(s"Received $message, staying alive")
        schedule.cancel()
        schedule = createTimeoutWindow()

    }
  }

//  val selfCosingActor = system.actorOf(Props[SelfClosingActor], "selfClosingActor")
//  system.scheduler.scheduleOnce(250 millis) {
//    selfCosingActor ! "ping"
//  }
//  system.scheduler.scheduleOnce(2 seconds) {
//    selfCosingActor ! "pong"
//  }

  /**
   * Timer
   **/
  case object TimerKey
  case object Start
  case object Reminder
  case object Stop

  class TimerBasedHeartBeatActor extends Actor with ActorLogging with Timers {
    timers.startSingleTimer(TimerKey, Start, 500 millis)

    override def receive: Receive = {
      case Start =>
        log.info("Bootsraping")
        timers.startPeriodicTimer(TimerKey, Reminder, 1 second)
      case Reminder =>
        log.info("I am alive")
      case Stop =>
        log.warning("Stopping!")
        timers.cancel(TimerKey)
        context.stop(self)
    }
  }

  val timerHBActor = system.actorOf(Props[TimerBasedHeartBeatActor], "TimerActor")
  system.scheduler.scheduleOnce(5 second) {
    timerHBActor ! Stop
  }
}
