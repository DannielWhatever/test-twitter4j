package cl.app

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
  val categories = buildMapCounting[Category.Value](Category, sc)
  val hours = buildMapCounting[Hour.Value](Hour, sc)

  val rules = CategoryRules.get

  def printResults(): Process = {
    categories.foreach {println}
    hours.foreach {println}
    return this
  }

  //process!
  def process(rdd: RDD[Status]): Process = {
    rdd.foreach(status=>process(status))
    return this
  }

  private def process(status: Status) = {

    rules.foreach(rule=>{
      if(rule.predicate(status))
        increment(categories.get(rule.group))
    })

    //max hour
    increment(hours.get(Hour.of(status.getCreatedAt)))

  }



}
