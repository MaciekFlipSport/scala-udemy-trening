package part3Testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import org.scalatest.{BeforeAndAfterAll, WordSpec, WordSpecLike}

import scala.concurrent.duration._
import scala.util.Random

class TimeAssertionSpec extends TestKit(ActorSystem("TimmedAssertionsSpec")) //, ConfigFactory.load().getConfig("specialTimedAssertionsConfig")))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  import TimeAssertionSpec._
  "a worker actor " should {
    val workerActor = system.actorOf(Props[WorkerActor])

    "replay with the meaninf life in a timley manner" in {
      within(500 millis, 1 second){
        workerActor ! "work"
        expectMsg(WorkResult(43))
      }
    }

    "repplay wihjt valid work at a reasonable cadence" in {
      within(1 second) {
        workerActor ! "workSequence"

        val results: Seq[Int] =receiveWhile[Int](max=2 seconds, idle = 500 millis, messages = 10) {
          case WorkResult(result) => result
        }
        assert(results.sum > 5 )
        println(results.sum)
      }
    }

   "replay to a test probe in a timley maner" in {
     within(1 second) {
       val probe = TestProbe()
       probe.send(workerActor, "work")
       probe.expectMsg(WorkResult(43))  ///timeout of 0.3 seconds Becafeful ! ! !

     }
   }

  }
}

object TimeAssertionSpec {
  // testing scenario
  case class WorkResult(result: Int)

  class WorkerActor extends Actor {
    override def receive: Receive = {
      case "work" =>
        //long computation
        Thread.sleep(500)
        sender() ! WorkResult(43)
      case "workSequence" =>
      val r = new Random()
      for (i <- 1 to 10) {
        println(s"thread number: $i")
        Thread.sleep(r.nextInt(50))
        sender() ! WorkResult(1)
      }
    }
  }
}
