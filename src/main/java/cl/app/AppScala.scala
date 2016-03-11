package cl.app

import cl.app.spark.{Process, SparkInit}
import cl.app.twitter.TwitterCli

object AppScala {

  def main(args: Array[String]): Unit = {

    val statuses = TwitterCli.getTweets("dannielwhatever")
    statuses.foreach{println}

    val sc = SparkInit.getCtx()


    val rddX = sc.parallelize(statuses, 2).cache()

    new Process(sc).process(rddX).printResults


  }



}