package part4FaultTolerance

import akka.actor.{Actor, ActorLogging, ActorSystem, OneForOneStrategy, Props}

import scala.io.Source
import java.io.File

import akka.actor.SupervisorStrategy.Stop

import scala.concurrent.duration._
import akka.pattern.{Backoff, BackoffSupervisor}

object BackOffSupervisorPattern extends App {

  case object ReadFile
  class FileBasedPersistentActor extends Actor with ActorLogging {
    var dataSource: Source = null

    override def preStart(): Unit = {
      log.info("Persistent actor starting")
    }
    override def postStop(): Unit = {
      log.warning("Persistent actor has stopped")
    }
    override def preRestart(reason: Throwable, message: Option[Any]): Unit = {
      log.warning("Persistent actor restarting")
    }

    override def receive: Receive = {
      case ReadFile =>
        if (dataSource == null)
          dataSource = Source.fromFile(new File("src/main/resources/testfiles/important_data1.txt")) //not existing path !! be aware !!!!
        log.info("I've just read some important data:" + dataSource.getLines().toList)
    }
  }
  val system = ActorSystem("BackOffSupervisorDemo")
//  val simpleActor = system.actorOf(Props[FileBasedPersistentActor], "simpleActor")
//  simpleActor ! ReadFile
  /**
   * BackOffSupervisor Pattern !!!!!!
   */
  val simpleSupervisorProps = BackoffSupervisor.props(
    Backoff.onFailure(
      Props[FileBasedPersistentActor],
      "simpleBackoffActor",
      3 seconds, // then 6, 12s, 24s, <=== 30 MAX!
      30 seconds,
      0.2
    )
  )

//  val simpleBackoffSupervisor = system.actorOf(simpleSupervisorProps, "simpleSupervisor")
//  simpleBackoffSupervisor ! ReadFile
  /*
  simpleSupervisor
    -child called simpleBackoffActor (props of type FileBasedPersistentActor)
    -supervision strategy is the default one (restarting everything)
      -first attempt after 3 seconds
      -next attempt is 2x previous attempt
   */

  val stopSupervisorProps = BackoffSupervisor.props(
    Backoff.onStop(
      Props[FileBasedPersistentActor],
      "stopBackoffActor",
      3 seconds,
      30 seconds,
      0.2
    ).withSupervisorStrategy(
      OneForOneStrategy() {
        case _ => Stop
      }
    )
  )

//  val stopSpervisor = system.actorOf(stopSupervisorProps, "stopSupervisor")
//  stopSpervisor ! ReadFile

  class EagerFBPA extends FileBasedPersistentActor {

    override def preStart(): Unit = {
      log.info("Eager actor starting")
      dataSource = Source.fromFile(new File("src/main/resources/testfiles/important_data1.txt"))
    }
  }

//  val eagerActor = system.actorOf(Props[EagerFBPA])
  //ActorInitializationException => STOP
  val repeatedSupervisorProps = BackoffSupervisor.props(
    Backoff.onStop(
      Props[EagerFBPA],
      "eagerActor",
      1 second,
      30 seconds,
      0.1
    )
  )
  val repatedSupervisor = system.actorOf(repeatedSupervisorProps, "eagerSupervisor")
  /*
  eagerSupervisor
    -child eagerActor
      -will die on start with ActorInitializationException
        -trigger the supervision strategy in eagerSupervisor => STOP eagerActor
       -backoff will kick in after 1 second, 2s, 4, 8, 16,

   */


}
