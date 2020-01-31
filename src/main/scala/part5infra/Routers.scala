package part5infra

import akka.actor.{Actor, ActorLogging, ActorSystem, PoisonPill, Props, Terminated}
import akka.routing.{ActorRefRoutee, Broadcast, FromConfig, RoundRobinGroup, RoundRobinPool, RoundRobinRoutingLogic, Router}
import com.typesafe.config.ConfigFactory

object Routers extends App {

  /**
   *  #1 Manual router == worse case
   */
  class Master extends  Actor {
    // step 1 - create routees
    // 5 actor routees based off Slave actors
    private val slaves = for (i <- 1 to 5) yield {
      val slave = context.actorOf(Props[Slave], s"slave_$i")
      context.watch(slave)
      ActorRefRoutee(slave)
    }
    // step 2 - define router
    private var router = Router(RoundRobinRoutingLogic(), slaves)

    override def receive: Receive = {
      // step 4 - handle the termination/lifecycle of the routers
      case Terminated(ref) =>
        router = router.removeRoutee(ref)
        val newSlave = context.actorOf(Props[Slave])
        router = router.addRoutee(newSlave)
      // step 3 - route the message
      case message =>
        router.route(message, sender())
    }
  }

  class Slave extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }
  }

  val system = ActorSystem("ActorsDemo", ConfigFactory.load().getConfig("routersDemo"))
  val master = system.actorOf(Props[Master])
//  for (i <- 1 to 10) {
//    master ! s"[$i]Hello from the world"
//  }

  /**
   * Method #2 - a router with its onw children
   *  POOL router
   */
    //2.1 programmaticlly (in code)
  val poolMaster = system.actorOf(RoundRobinPool(5).props(Props[Slave]), "simplePoolMaster")

//  for (i <- 1 to 10) {
//    poolMaster ! s"[$i]Hello from the world"
//  }

  // from configuration
  val poolMaster2 = system.actorOf(FromConfig.props(Props[Slave]), "poolMaster2")
//  for (i <- 1 to 10) {
//    poolMaster2 ! s"[$i]Hello from the world"
//  }

  /**
   * Method #3 - router with actors created elsewhere
   * GROUP router
   */
    //.. in another part of my app I created:
  val slaveList = (1 to 5).map(i => system.actorOf(Props[Slave], s"slave_$i")).toList

  //need actor paths
  val slavePaths = slaveList.map(slaveRef => slaveRef.path.toString)

  //3.1 in the code
  val groupMaster = system.actorOf(RoundRobinGroup(slavePaths).props())
//  for (i <- 1 to 10) {
//    groupMaster ! s"[$i]Hello from the world"
//  }

  //3.2 from configuration
  val groupMaster2 = system.actorOf(FromConfig.props(), "groupMaster2")
  for (i <- 1 to 10) {
    groupMaster2 ! s"[$i]Hello from the world"
  }

  /**
   * Special messages
   */
  groupMaster2 ! Broadcast("hello everyone")

  // PoisonPill and Kill are NOT routed
  //AddRoutee, Remove, Get handled only by the routing actor

}
