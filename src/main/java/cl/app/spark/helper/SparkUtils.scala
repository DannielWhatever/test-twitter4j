package cl.app.spark.helper

import cl.app.spark.custom.MapCounting
import org.apache.spark.SparkContext

/**
  * Created by Daniel on 03-03-2016.
  */
object SparkUtils{

  def buildMapCounting[T](enum: Enumeration,sc:SparkContext): MapCounting[T] = {
    val mapCounting = new MapCounting[T]
    enum.values.foreach(value => mapCounting.add(value.asInstanceOf[T],sc))
    return mapCounting
  }

}
