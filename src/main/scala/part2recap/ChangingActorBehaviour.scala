package part2recap

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import part2recap.ChangingActorBehaviour.Mom.{MomHappy, MomStart}

object ChangingActorBehaviour extends App{

  object FussyKid{
    case object KidAccept
    case object KidReject
    val HAPPY = "happy"
    val SAD = "sad"
  }

  class FussyKid extends Actor {
    import FussyKid._
    import Mom._
    //internal sate of the kid
    var state = HAPPY
    override def receive: Receive = {
      case Food(WEGETABLES) => state = SAD
      case Food(CHOCOLATE) => state = HAPPY
      case Ask(_) =>
        if (state == HAPPY) sender() ! KidAccept
        else {
          sender() ! KidReject
        }
    }
  }

  //stateless FussyKid ! ! ! BEST PRACTICE BEST PRACTICE BEST PRACTICE BEST PRACTICE  ! ! ! ! ! ! ! !
  class StatelessFussyKid extends Actor {
    import FussyKid._
    import Mom._
    override def receive: Receive = happyReceive

    def happyReceive: Receive = {
      case Food(WEGETABLES) => context.become(sadReceive)
        // Method with stack, be aware of it!
//      case Food(WEGETABLES) => context.become(sadReceive, false)
      case Food(CHOCOLATE) =>
      case Ask(_) => sender() ! KidAccept
    }
    def sadReceive: Receive = {
      case Food(WEGETABLES) =>
      // Method with stack, be aware of it!
//      case Food(WEGETABLES) => context.become(sadReceive, false)
//      case Food(CHOCOLATE) =>  context.unbecome()
      case Food(CHOCOLATE) => context.become(happyReceive)
      case Ask(_) => sender() ! KidReject
    }
  }

  object Mom{
    case class MomStart(kidRef: ActorRef)
    case class MomHappy(kidRef: ActorRef)
    case class Food(food: String)
    case class Ask(message: String) //do you want to play

    val WEGETABLES = "veggies"
    val CHOCOLATE = "chocolate"
  }

  class Mom extends Actor{
    import Mom._
    import FussyKid._
    override def receive: Receive = {
      case MomStart(kidRef) =>
        // test our interaction
        kidRef ! Food(WEGETABLES)
        kidRef ! Ask("do you want to play")
      case MomHappy(kidRef) =>
        kidRef ! Food(CHOCOLATE)
        kidRef ! Ask("do you want to play")
      case KidAccept => println("yes my kid is happy")
      case KidReject => println("My kid is sad, but at least he is healthy!")
    }
  }
  val system = ActorSystem("changingActorBehavoiur")
  val fussyKid = system.actorOf(Props[FussyKid])
  val statelessFussyKid = system.actorOf(Props[StatelessFussyKid])
  val mom = system.actorOf(Props[Mom])

//  mom ! MomStart(fussyKid)
  mom ! MomStart(statelessFussyKid)
  mom ! MomStart(statelessFussyKid)
  mom ! MomHappy(statelessFussyKid)
  mom ! MomHappy(statelessFussyKid)

  /**
   * Exercise 1 - Counter with actors
   */
  //BEST PRACTICE
  //DOMAIN of the Counter
  object Counter{
    case object Increment
    case object Decrement
    case object Print
  }

  class Counter extends Actor{
    import Counter._
    override def receive: Receive = countReceive(0)

    def countReceive(currentCount: Int): Receive = {
      case Increment =>
        println(s"[$currentCount] incrementing")
        context.become(countReceive(currentCount + 1))
      case Decrement =>
        println(s"[$currentCount] decrementing")
        context.become(countReceive(currentCount - 1))
      case Print => println(s"[counter] $currentCount")
    }
  }
  import Counter._
  val counter = system.actorOf(Props[Counter], "COUNTER")
  (1 to 100 ).foreach(_ => counter ! Increment)
  (1 to 37 ).foreach(_ => counter ! Decrement)
  counter ! Print

  /**
   *  Exercise 2 - a simplified voting system
   */

  case class Vote(candidate: String)
  case object VoteStatusRequest
  case class VoteStatusReplay(candidate: Option[String])

  class Citizen extends Actor {
    var candidate: Option[String] = None
    override def receive: Receive = {
      case Vote(c) => context.become(voted(c))
      case VoteStatusRequest => sender() ! VoteStatusReplay(None)
    }
    def voted(candidate: String): Receive = {
      case VoteStatusRequest => sender() ! VoteStatusReplay(Some(candidate))
    }
  }

  case class AggregateVotes(citizens: Set[ActorRef])
  class VoteAggregator extends Actor {
    //BAD PRACTICE
//    var stillWaiting: Set[ActorRef] = Set()
//    var currentStats: Map[String, Int] = Map()
//    override def receive: Receive = {
//      case AggregateVotes(citizens) =>
//        stillWaiting = citizens
//        citizens.foreach(citizenRef => citizenRef ! VoteStatusRequest)
//      case VoteStatusReplay(None) =>
//        //a citizen hasn't voted yet
//        sender() ! VoteStatusRequest
//      case VoteStatusReplay(Some(candidate)) =>
//        val newStillWaiting = stillWaiting - sender()
//        val currentVotesOfCandidate = currentStats.getOrElse(candidate, 0)
//        currentStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
//        if (newStillWaiting.isEmpty) {
//          println(s"[aggregator] vote stats: $currentStats")
//        } else {
//          println("Waiting for everyone to vote")
//          stillWaiting = newStillWaiting
//        }
//    }
    //GOOD PRACTICE
      override def receive: Receive = awaitingCommand
      def awaitingCommand: Receive = {
            case AggregateVotes(citizens) =>
              citizens.foreach(citizenRef => citizenRef ! VoteStatusRequest)
              context.become(awaitingStatuses(citizens, Map()))
          }
      def awaitingStatuses(stillWaiting: Set[ActorRef], currentStats: Map[String, Int]): Receive = {
        case VoteStatusReplay(None) =>
          //a citizen hasn't voted yet
          sender() ! VoteStatusRequest
        case VoteStatusReplay(Some(candidate)) =>
          val newStillWaiting = stillWaiting - sender()
          val currentVotesOfCandidate = currentStats.getOrElse(candidate, 0)
          val newStats = currentStats + (candidate -> (currentVotesOfCandidate + 1))
          if (newStillWaiting.isEmpty) {
            println(s"[aggregator] vote stats: $newStats")
          } else {
            println("Waiting for everyone to vote")
            context.become(awaitingStatuses(newStillWaiting, newStats))
          }
      }
    }

  val alice = system.actorOf(Props[Citizen])
  val bob = system.actorOf(Props[Citizen])
  val charlie = system.actorOf(Props[Citizen])
  val daniel = system.actorOf(Props[Citizen])

  alice ! Vote("Martin")
  bob ! Vote("Jonas")
  charlie ! Vote("Roland")
  daniel ! Vote("Roland")

  val voteAggregator = system.actorOf(Props[VoteAggregator])
  voteAggregator ! AggregateVotes(Set(alice, bob, charlie, daniel))

}
