package part2recap

import akka.actor.{Actor, ActorSystem, Props}

object ActorsIntro extends App{

  //part1 - actor system
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  //part2 - create actors
  //word count actor
  class WordCountActor extends Actor {
    //internal data
    var totalWords = 0

    //behaviour
    def receive: PartialFunction[Any, Unit] = {
        case message: String =>
          println(s"[word counter] I have recived: $message")
          totalWords += message.split(" ").length
        case msg => println(s"[word counter] I cannot understand ${msg.toString}.")
    }
  }
  //part3 - instantiate our actor
  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter = actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")

  //part4 - communicate!
  // ! - exclamation method or tell
  wordCounter ! "I'm learning Akka and it is pretty damn cool!"
  wordCounter ! "This is bullshit"

  //best practice to creating Actors with constructor arguments
  object Person {
    def props(name: String): Props = Props(new Person(name))
  }
  class Person(name: String) extends Actor {
    override def receive: Receive = {
        case "hi" => println(s"Hi my name is $name")
        case _ =>
    }
  }
  //Not best practice
  val person1 = actorSystem.actorOf(Props(new Person("Bob")))

  //Best practice
  val person = actorSystem.actorOf(Person.props("Bob"))
  person ! "hi"



}
