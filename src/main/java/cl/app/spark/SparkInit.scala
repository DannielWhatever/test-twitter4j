package cl.app.spark

import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Daniel on 02-03-2016.
  */
object SparkInit {

  private val conf = new SparkConf().setMaster("local[4]").setAppName("TwitterAnalytics")
  private val sc = new SparkContext(conf)

  def getCtx: SparkContext = {
    return sc
  }

}
