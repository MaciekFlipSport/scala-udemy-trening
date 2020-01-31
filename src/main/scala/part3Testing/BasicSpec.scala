package part3Testing

import akka.actor.{Actor, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfter, BeforeAndAfterAll, WordSpecLike}

import scala.concurrent.duration._
import scala.util.Random

class BasicSpec extends TestKit(ActorSystem("BasicSpec"))
  with ImplicitSender
  with WordSpecLike
  with BeforeAndAfterAll {

  // setup
  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
  import BasicSpec._
  "A simple actor" should{
    "send back the same message" in {
      val echoActor = system.actorOf(Props[SimpleActor])
      val message = "hello text"
      echoActor ! message  //aka.test.signle-expect-defaut

      expectMsg(message)
    }
  }

  "A blackhole actor" should{
    "send back the same message" in {
      val blackhole = system.actorOf(Props[BlackHole])
      val message = "hello text"
      blackhole ! message

      expectNoMessage(1 second)
    }
  }

  "A lab test actor" should {
    val labTestActor = system.actorOf(Props[LabTestActor])

    "turn a string into uppercase" in {
      labTestActor ! "I love Akka"
      val replay = expectMsgType[String]

      assert(replay == "I LOVE AKKA")
//      expectMsg("I LOVE AKKA")

    }
    "replay to a greeting" in {
      labTestActor ! "greeting"
      val replay = expectMsgAnyOf("hi", "hello")
      println(replay)
    }

    "replay with favoriteTech tech" in {
      labTestActor ! "favoriteTech"
     expectMsgAllOf("Scala", "Akka")
    }

    "replay with cool tech in a diffrent way" in {
      labTestActor ! "favoriteTech"
      val messages = receiveN(2) //Seq[Any]
      // free to do more comlicated assertions
      println(messages)
    }

    "replay with cool tech in a fancy way" in {
      labTestActor ! "favoriteTech"
//      receiveN(2)
      expectMsgPF() {
        case "Scala" => println("No jestem Scala") //only care the PF is define ??
        case "Akka" => println("No jestem Akka")
    }
    }
  }


}

object BasicSpec {
  class SimpleActor extends Actor {
    override def receive: Receive = {
      case message => sender() ! message
    }
  }
  class BlackHole extends Actor {
    override def receive: Receive = Actor.emptyBehavior
  }

  class LabTestActor extends Actor {
    val random = new Random()
    override def receive: Receive = {
      case "greeting" =>
        println(random)
        println(random.nextBoolean())
        if (random.nextBoolean()) sender() ! "hi" else sender() ! "hello"
      case "favoriteTech" =>
        sender() ! "Scala"
        sender() ! "Akka"
      case message: String => sender() ! message.toUpperCase()
    }
  }
}