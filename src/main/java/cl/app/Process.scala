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
    rdd.foreach(process(_))

    val words = rdd.flatMap(_.getText.split(" "))

    val top10 = rdd
      .flatMap(_.getText.split(" ")) //obtain words
      .map((_,1)) //parto con 1
      .reduceByKey(_+_) //wtf, pero la magia, onda, no sé , suma las palabras
      .map(x=>(x._2,x._1)) //ahora dejo la cantidad como key
      .sortByKey(false) //y ordeno, desc
      .take(10) //saco los 10 primeros

    top10.foreach(println(_))

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



}
