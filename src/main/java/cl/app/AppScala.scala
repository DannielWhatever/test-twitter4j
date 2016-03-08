package cl.app

import cl.app.spark.Spark
import cl.app.twitter.TwitterCli

object AppScala {

  def main(args: Array[String]): Unit = {

    val statuses = TwitterCli.getTweets("dannielwhatever")
    statuses.foreach{println}

    val sc = Spark.getCtx()


    val rddX = sc.parallelize(statuses, 2).cache()

    new Process(sc).process(rddX).printResults


  }



}