package cl.app.spark

import cl.app.`type`.Hour.of
import cl.app.`type`.{Category, Hour}
import cl.app.rule.CategoryRules
import cl.app.spark.helper.SparkUtils._
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import twitter4j.Status

/**
  * Created by Daniel on 03-03-2016.
  */
class Process(sc: SparkContext) extends Serializable {

  //MapCountings
  val categories = buildMapCounting[Category.Value](Category,sc)
  val hours = buildMapCounting[Hour.Value](Hour,sc)


  def printResults: Process = {
    categories.foreach(println)
    hours.foreach(println)
    return this
  }

  //process!
  def process(rdd: RDD[Status]): Process = {
    doProcess(rdd)
    return this
  }


  private def doProcess(rdd: RDD[Status]) ={
    rdd.collect.foreach(applyRules)

    val rddAllWords = rdd.flatMap(_.getText.split(" ")).cache
    val rddUserMentions = rdd.flatMap(_.getUserMentionEntities.map("@"+_.getName))

    val topWords = wordCounts(rddAllWords)
    val topMentions = wordCounts(rddUserMentions)

    //TODO: no tengo datos, por ahora
    rdd.filter(_.getGeoLocation.ne(null)).map(_.getGeoLocation.toString).collect.foreach(println)
    //--

    topStatus(rdd, _.getFavoriteCount).foreach(println)
    topStatus(rdd, _.getRetweetCount).foreach(println)

    val topLang = wordCounts(rdd.map(_.getLang)).take(1)(0)

    topWords.foreach(println)
    topMentions.foreach(println)
    println("Top Language: "+topLang._2.toString.toUpperCase)
  }

  private def applyRules(status: Status) = {

    CategoryRules.apply(status,categories.increment)

    //max hour
    hours.increment(of(status.getCreatedAt))
  }

  private def wordCounts(rdd:RDD[String], cant:Int=10):Array[(Int,String)] = {
    return rdd
      .map((_,1)) //parto con 1
      .reduceByKey(_+_) //wtf, pero la magia, onda, no sé , suma el segundo parametro cuando el key coincide
      .map(x=>(x._2,x._1)) //ahora dejo la cantidad como key
      .sortByKey(ascending = false) //y ordeno, desc
      .take(cant) //saco los $cant primeros
  }


  private def topStatus(rdd:RDD[Status], fx:Status=>Int, allowRetweet:Boolean=false, cant:Int=3):Array[(Int,Status)] = {
    //noinspection ComparingUnrelatedTypes
    return rdd
      .filter(_.isRetweet.equals(allowRetweet))
      .map(status => (fx(status),status))
      .sortByKey(ascending = false)
      .take(cant)
  }

}
