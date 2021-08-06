package scalaCourse.part3fp

object TuplesAndMaps extends App {

  //tuples = finite ordered "lists"
//  val aTuple = new Tuple2(2, "hello scala")  // Tuple[Int, String] = (Int, String)
  val aTuple = (2, "hello scala")

  println(aTuple._1)  //2
  println(aTuple.copy(_2 = "goodbye Java"))
  println(aTuple.swap)  // ("hello scala", 2)

  // Maps - keys -> values
  val aMap: Map[String, Int] = Map()

  val phonebook = Map(("Jim", 900), "Daniel"-> 789).withDefaultValue(-1)
  // a -> b is sugar for (a,b)
  println(phonebook)

  // map ops
  println(phonebook.contains("Jim"))
  println(phonebook("Jim"))
//  println(phonebook("Marry")) -> throw NoSuchElementException need to add withDefaultValue
  println(phonebook("Mary"))

  // add pairing
  val newPairing = "Mary" -> 678
  val newPhonebook = phonebook + newPairing
  println(newPhonebook)

  // functionals on maps
  // map, flatMap, filter
  println(phonebook.map(pair => pair._1.toLowerCase() -> pair._2))

  //filterKeys
  println(phonebook.filterKeys(x => x.startsWith("J")))

  //mapValues
  println(phonebook.mapValues(number => number + 10))
  println(phonebook.mapValues(number => "046-" + number))

  // conversion to other colections
  println(phonebook.toList)
  println(List(("Daniel", 555)))
  println(List(("Daniel", 555)).toMap)
  val names = List("Bob", "James", "Angela", "Mary", "Daniel", "Jim")
  println(names.groupBy(name => name.charAt(0)))


  /*
    1. What would happen if I had two original entries "Jim" -> 555 and "JIM" -> 900?
    ! ! ! careful with mapping keys

    2. Overly simplified social network based on maps
      Person = String
      - add a person to the network
      - remove
      - friend
      - unfriend

      - number of friend of a person
      - person with most friends
      - how many people have NO friends
      - if there is a social connection between two people (direct or not)
   */

  // 1.
  val BigJim = "JIM" -> 555
  val exercise1Phonebook = phonebook + BigJim
  println(exercise1Phonebook)
  println(exercise1Phonebook.map(pair => pair._1.toLowerCase() -> pair._2))

  // 2.
  def add(network: Map[String, Set[String]], person: String): Map[String, Set[String]] =
    network + (person -> Set())

  def friend(network: Map[String, Set[String]], personA: String, personB: String): Map[String, Set[String]] ={
    val friendsA = network(personA)
    val friendsB = network(personB)

    network + (personA -> (friendsA + personB)) + (personB -> (friendsB + personA))
  }

  def unfriending(network: Map[String, Set[String]], personA: String, personB: String): Map[String, Set[String]] = {
    val friendsA = network(personA)
    val friendsB = network(personB)

    network + (personA -> (friendsA - personB)) + (personB -> (friendsB - personA))
  }

  def remove(network: Map[String, Set[String]], person: String): Map[String, Set[String]] ={
    def removeAux(friends: Set[String], networkAcc: Map[String, Set[String]]): Map[String, Set[String]] =
      if (friends.isEmpty) networkAcc
      else removeAux(friends.tail, unfriending(networkAcc, person, friends.head))

    val unfriended = removeAux(network(person), network)
    unfriended - person
  }

  val empty: Map[String, Set[String]] = Map()
  val network = add(add(empty, "Bob"), "Maciek")
  println(network)
  println(friend(network, "Bob", "Maciek"))
  println(unfriending(friend(network, "Bob", "Maciek"), "Bob", "Maciek"))
  println(remove(friend(network, "Bob", "Maciek"),"Bob"))

  // Jim, Bob, Marry
  val people = add(add(add(empty,"Bob"), "Mary"), "Jim")
  val jimBob = friend(people, "Bob", "Jim")
  val testNet = friend(jimBob, "Bob", "Mary")

  println(testNet)

  


}
