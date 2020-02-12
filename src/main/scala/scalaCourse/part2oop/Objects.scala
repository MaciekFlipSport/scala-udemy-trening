package scalaCourse.part2oop

object Objects extends App{

  // SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY ("static")
  object Person {  // type + its only instance
    // "static"/"class" - level functionality
    val N_EYES = 2
    def canFly: Boolean = false

    // factory method
//    def from(mother: Person, father: Person): Person = new Person("Bobbie")
    def apply(mother: Person, father: Person): Person = new Person("Bobbie")
  }
  class Person(val name: String) {
    // instance-level functionality
  }
  // COMPANIONS

  println(Person.N_EYES)
  println(Person.canFly)

  // Scala object = SINGLETON INSTANCE
  val mary = Person
  val john = Person
  println(mary == john) // true

  val mary1 = new Person("Mary")
  val john1 = new Person("John")
  println(mary == john) // false

//  val bobbie = Person.from(mary1, john1)
  val bobbie = Person(mary1, john1)

  // Scala Applications = Scala object with
  //def main(args: Array[String]): Unit    == extends App  ! ! ! !



}
