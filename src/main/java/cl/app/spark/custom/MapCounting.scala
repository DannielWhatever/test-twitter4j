package cl.app.spark.custom

import org.apache.spark.{Accumulator, SparkContext}

import scala.collection.mutable

/**
  * Created by Daniel on 03-03-2016.
  */
class MapCounting[T](val sc: SparkContext) extends Serializable{

  private val mapCounting = mutable.Map.empty[T,Accumulator[Int]]


  def add(t:T) = mapCounting.put(t,sc.accumulator(0))

  def foreach(fn:((T,Accumulator[Int]))=>Unit):Unit = mapCounting.foreach(fn)

  def get(t:T) = mapCounting.get(t)

  def increment(t:T) = get(t).get.add(1)

}
