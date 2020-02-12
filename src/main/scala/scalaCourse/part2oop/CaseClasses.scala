package scalaCourse.part2oop

object CaseClasses extends App{

  /*
  equals, hashCode, toString
   */

  case class Person(name: String, age: Int)

  // 1. class parameters are fields
  val jim = new Person("Jim", 34)
  println(jim.name)

  // 2. sensible toString
  // println(instance) = println(instance.toSting)  // syntactic sugar
  // println(jim.toString)
  println(jim)

  // 3. equals and hashCode implemented OOTB
  val jim2 =  new Person("Jim",34)
  println(jim2 == jim)

  // 4. CCs have handy copy method
  val jim3 = jim.copy(age=45)

  // 5. CCs have companion objects
  val thePerson = Person
  val mary = Person("Mary", 23)

  // 6. CCs are serializable
  // Akka

  //  7. CCs have extractor patterns = CCs can be used in PATTERN MATCHING

  case object UnitedKingdom {
    def name: String = "The UK of GB an NI"
  }

  /*
  Expand MyList - use case classes and case objectd
   */



}
