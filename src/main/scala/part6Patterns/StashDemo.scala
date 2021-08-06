package part6Patterns

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Stash}

object StashDemo extends App{

  /*
  * ResourceActor
    - open => it can receive read/write requestes to the resource
    - otherwise it will postpone all read/write requests until the state is open

    ResourceActor is closed
      - Open => switch to open state
      - Read Write messages are POSTPOND

    ResourceActor is open
      - Read Write messages are handled
      - Close => switch to closed state

     [Open, Read, Read, Write]
      - switch to the open state
      - read data
      - read data
      - write the data

      [Read, Open, Write]
        - stash Read
          Stash: [Read]
        - open => switch to open state
          Mailbox: [Read, Write]
        - read and write are handled
  */


  case object Open
  case object Close
  case object Read
  case class Write(data: String)

  //step 1 - mixin the Stash trait
  class ResourceActor extends Actor with ActorLogging with Stash {

    private var innerData: String = ""
    override def receive: Receive = closed

    def closed: Receive = {
    case Open =>
        log.info("Opening resource")
        //step 3 - unstashAll when you switch the message handle
        unstashAll()
        context.become(open)
    case message =>
        log.info(s"Stashing [$message] because I can't handle it in the closed state")
        //step 2 - stash away what you can't handle
        stash()
    }

    def open: Receive = {
      case Read =>
        //do some actual computation
        log.info(s"I have read: $innerData")
      case Write(data) =>
        log.info(s"I'm writing: $data")
        innerData = data
      case Close =>
        log.info("Closing resource")
        unstashAll()
        context.become(closed)
      case message =>
        log.info(s"Stashing [$message] because I can't handle it in the open state")
        stash()
    }
  }

  val  system = ActorSystem("StashDemo")
  val resourceSystem = system.actorOf(Props[ResourceActor])

//  resourceSystem ! Write("I love stash")
//  resourceSystem ! Read
//  resourceSystem ! Open

  resourceSystem ! Read
  resourceSystem ! Open
  resourceSystem ! Open
  resourceSystem ! Write("I love stash")
  resourceSystem ! Close
  resourceSystem ! Read
}
