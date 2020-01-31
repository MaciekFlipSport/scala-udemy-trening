package part2recap

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2recap.ActorCapabilities.Person.LiveTheLife

object ActorCapabilities extends App{

  class SimpleActor extends Actor {
    override def receive: Receive = {
      case "Hi!" => context.sender() ! "Hello there!"  // replaying to a message
      case message: String => println(s"[$self] I have received: $message")
      case number: Int  => println(s"[simple actor] I have received NUMBER: $number")
      case SpecialObject(content) => println(s"[simple actor] I have received something SPECIAL: $content")
      case SendMessageToYourself(content) =>
        self ! content
      case SayHiTo(ref) => ref ! "Hi!"
      case WirlessPhoneMessage(content, ref) => ref forward (content + "s") // I keep the original sender of WPM
    }
  }

  val system = ActorSystem("actorCapabilitiesDemo")
  val simpleActor = system.actorOf(Props[SimpleActor], "simpleActor")

  //1 - messages can be of any type
  //a)) messages must be IMMUTABLE
  //b) messages must be SERIALIZABLE
  // in practice use case classes and case objects

  simpleActor ! "Hello, actor"
  simpleActor ! 42

  case class SpecialObject(content: String)
  simpleActor ! SpecialObject("This is SPECIAL")

  //2 - actors have information about their context and about themselves
  // context.self === `this` in OOP
  case class SendMessageToYourself(content: String)
  simpleActor ! SendMessageToYourself("I'm an actor and I'm proud of it")


  //3 - actor can REPLY to messages
  val alice = system.actorOf(Props[SimpleActor], "alice")
  val bob = system.actorOf(Props[SimpleActor], "bob")

  case class SayHiTo(ref: ActorRef)
  alice ! SayHiTo(bob)

  //4 - dead letters
  alice ! "Hi!" // replay to me

  //5 - forwarding messages
  case class WirlessPhoneMessage(content: String, ref: ActorRef)
  alice ! WirlessPhoneMessage("Hi", bob)

  /**
   *Exercises
   *
   * 1. a Counter actor
   * -Increment
   * -Decrement
   * -Print
   */
  //BEST PRACTICE
  //DOMAIN of the Counter
  object CounterActor{
    case object Increment
    case object Decrement
    case object Print
  }

  class CounterActor extends Actor{
    import CounterActor._
    var totalCounter = 0
    override def receive: Receive = {
      case Increment => totalCounter += 1
      case Decrement => totalCounter -= 1
      case Print => println(s"[counter] value of counter is: $totalCounter")
    }
  }
  var handleCounter = system.actorOf(Props[CounterActor], "handleCounter")
//   handleCounter ! "Increment"
  import CounterActor._
  (1 to 5).foreach(_ => handleCounter ! Increment)
//  handleCounter ! "Decrement"
  (1 to 5).foreach(_ => handleCounter ! Decrement)
  handleCounter ! Print

  /**
   * 2. Bank account as an actor
   * receives
   *  -Deposit an amount
   *  -Withdraw an amount
   *  -Statement
   *  replies with
   *  -Success
   *  -Failure
   */
  object BankAccount {
    case class Deposit(amount: Int)
    case class Withdraw(amount: Int)
    case object Statment

    case class TransactionSuccess(message: String)
    case class TransactionFailure(reason: String)
  }
  class BankAccount extends Actor{
    import BankAccount._
    var funds = 0

    override def receive: Receive = {
      case Deposit(amount) =>
        if (amount < 0) sender() ! TransactionFailure("invalid deposit amount")
        else {
          funds += amount
          sender() ! TransactionSuccess(s"sucessfully deposited $amount")
        }
      case Withdraw(amount) =>
        if (amount < 0) sender() ! TransactionFailure("invalid deposit amount")
        else if (amount > funds) sender() ! TransactionFailure("not enough funds in account")
        else {
          funds -= amount
          sender() ! TransactionSuccess(s"successfully withdraw $amount")
        }
      case Statment => sender ! s"[bank account] The fund on account is: $funds"
    }
    }
  object Person {
    case class LiveTheLife(account: ActorRef)
  }
  class Person extends Actor{
    import Person._
    import BankAccount._

    override def receive: Receive = {
      case LiveTheLife(account) =>
        account ! Deposit(1000)
        account ! Withdraw(9000)
        account ! Withdraw(500)
        account ! Statment
      case message => println(message.toString)
    }
  }
  val account = system.actorOf(Props[BankAccount], "bankAccount")
  val person = system.actorOf(Props[Person], "billionare")

  person ! LiveTheLife(account)
}
