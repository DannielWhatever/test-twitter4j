package cl.app

object AppScala {

  def main(args: Array[String]): Unit = {

    println("Hello, world!")

    val statuses = TwitterCli.getTweets("cht_informatica")
    statuses.foreach{println}

  }

}