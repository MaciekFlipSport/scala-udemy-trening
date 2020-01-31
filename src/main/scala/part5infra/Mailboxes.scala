package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.dispatch.{ControlMessage, PriorityGenerator, UnboundedPriorityMailbox}
import com.typesafe.config.{Config, ConfigFactory}

object Mailboxes extends App{
  val system = ActorSystem("MailboxDemo", ConfigFactory.load().getConfig("mailboxesDemo"))

  class SimpleActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  /**
   * Itersting case #1 - custom priority mailbox
   * P0 - most important
   * P1
   * P2
   * P3 - less important
   */

  //step 1 - mailbox definition
  class SupportTicketMailbox(setting: ActorSystem.Settings, config: Config)
    extends UnboundedPriorityMailbox(
      PriorityGenerator {
        case message: String if message.startsWith("[P0]") => 0
        case message: String if message.startsWith("[P1]") => 1
        case message: String if message.startsWith("[P2]") => 2
        case message: String if message.startsWith("[P3]") => 3
        case _ => 4
      })

  //step 2 - make it know in the config
  //step 3 - attach the dispatcher to an actor
  val supportTicketLogger = system.actorOf(Props[SimpleActor].withDispatcher("support-ticket-dispatcher"))
//  supportTicketLogger ! "[P3] this thing would be nice to have"
//  supportTicketLogger ! "[P0] this need to be solve now"
//  supportTicketLogger ! "[P1] this when you have time"

  // after which time can I send another message and be prioritized accordingly

  /**
   * Interesting case #2 - control-aware mailbox
   * we'll use UnboundedControlAwareMailbox
   */
  //step 1 - mark important messages as coontrol messages
  case object ManagmentTicket extends ControlMessage

  /**
  * step 2 - configure who gets the mialbox
  * -make the actor attach to mailbox
   */
    //method #1
  val controlAwareActor = system.actorOf(Props[SimpleActor].withMailbox("control-mailbox"))
//  controlAwareActor ! "[P0] this need to be solve now"
//  controlAwareActor ! "[P1] this when you have time"
//  controlAwareActor ! ManagmentTicket

  //method #2 - using deployment config
  val alternativeAwareActor = system.actorOf(Props[SimpleActor], "altControlAwareActor")
  alternativeAwareActor ! "[P0] this need to be solve now"
  alternativeAwareActor ! "[P1] this when you have time"
  alternativeAwareActor ! ManagmentTicket


}
