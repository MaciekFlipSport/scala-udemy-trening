package part1recap

object MultiTreadingObject extends App{

//  val aThread = new Thread(new Runnable {
//    override def run(): Unit = println("I'm running in pararell")
//  })
  //syntactic sugar, better readability, use this
  val aThread = new Thread(() => println("I'm running in pararell"))
  aThread.start()
  aThread.join()

  val threadHello = new Thread(() => (1 to 1000).foreach(_ -> println("Hello")))
  val threadGoodbye = new Thread(() => (1 to 1000).foreach(_ -> println("Goodbye")))

  threadHello.start()
  threadGoodbye.start()

  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount

    //not THREAD safe method
    def withdraw(money: Int): Unit = this.amount -= money

    def safeWithdraw(money: Int): Unit = this.synchronized {
      this.amount -= money
    }
  }
}
