package cl.app

import cl.app.`type`.Hour.of
import cl.app.`type`.{Category, Hour}
import cl.app.rule.CategoryRules
import cl.app.spark.SparkUtils._
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

  val rules = CategoryRules.get

  def printResults(): Process = {
    categories.foreach {println}
    hours.foreach {println}
    return this
  }

  //process!
  def process(rdd: RDD[Status]): Process = {
    rdd.foreach(process)

    val topWords = wordCounts(rdd.flatMap(_.getText.split(" ")))
    val topMentions = wordCounts(rdd.flatMap(_.getUserMentionEntities.map("@"+_.getName)))

    //TODO: no tengo datos, por ahora
    rdd.filter(_.getGeoLocation.ne(null)).map(_.getGeoLocation.toString).foreach(println)
    //--

    topStatus(rdd, _.getFavoriteCount).foreach(println)
    topStatus(rdd, _.getRetweetCount).foreach(println)

    topWords.foreach(println(_))
    topMentions.foreach(println(_))

    return this
  }

  private def process(status: Status) = {

    status.getText.split(" ")

    rules.foreach(rule=>{
      if(rule.predicate(status))
        categories.increment(rule.group)
    })

    //max hour
    hours.increment(of(status.getCreatedAt))

  }

  private def wordCounts(rdd:RDD[String], cant:Int=10):Array[(Int,String)] = {
    return rdd
      .map((_,1)) //parto con 1
      .reduceByKey(_+_) //wtf, pero la magia, onda, no sé , suma el segundo parametro cuando el key coincide
      .map(x=>(x._2,x._1)) //ahora dejo la cantidad como key
      .sortByKey(false) //y ordeno, desc
      .take(cant) //saco los $cant primeros
  }


  private def topStatus(rdd:RDD[Status], fx:Status=>Int, allowRetweet:Boolean=false, cant:Int=3):Array[(Int,Status)] = {
    return rdd
      .filter(_.isRetweet.equals(allowRetweet))
      .map(status => (fx(status),status))
      .sortByKey(false)
      .take(cant)
  }

}
