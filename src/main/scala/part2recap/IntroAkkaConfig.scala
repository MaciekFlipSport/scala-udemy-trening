package part2recap

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object IntroAkkaConfig extends App{

  class SimpleLoggingActor extends Actor with ActorLogging {
    override def receive: Receive = {
      case message => log.info(message.toString)
    }

  }
  /**
   * 1 - inline configuration
   */
val configString =
  """
    |akka {
    |  loglevel = "ERROR"
    |}
    |""".stripMargin
  val config = ConfigFactory.parseString((configString))
  val system = ActorSystem("ConfigurationDemo", ConfigFactory.load(config))
  val actor = system.actorOf(Props[SimpleLoggingActor])

  actor ! "A message to remember"

  /**
   * 2 - configuration file
   */

  val defaultConfigFilesystem = ActorSystem("DefaultConfigDemo")
  val defaultConfigActor = defaultConfigFilesystem.actorOf(Props[SimpleLoggingActor])
  defaultConfigActor ! "Remember me"

  /**
   * 3 - separate configuration
   */
  val specialConfig = ConfigFactory.load().getConfig("mySpecialConfig")
  val specialConfigSystem = ActorSystem("SpecialConfigDemo", specialConfig)
  val specialConfigActor = specialConfigSystem.actorOf(Props[SimpleLoggingActor])
  specialConfigActor ! "Hey babe"

  /**
   * 4 - separate config in other file
   * */
  val separateConfig = ConfigFactory.load("secretFolder/secretConfiguration.conf")
  println(s"separate config log level: ${separateConfig.getString("akka.loglevel")}")

  /**
   * 5 - different file formats
   * JSON, Properities
   */

  val jsonConfig = ConfigFactory.load("json/jsonConfig.json")
  println((s"json config: ${jsonConfig.getString("aJsonProperty")}"))
  println((s"json config: ${jsonConfig.getString("akka.loglevel")}"))
}
