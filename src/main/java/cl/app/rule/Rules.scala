package cl.app.rule

import cl.app.spark.custom.Rule
import twitter4j.Status

import scala.collection.mutable

/**
  * Created by Daniel on 02-03-2016.
  */
trait Rules[Q] {

  protected def initRules()

  private val rules = mutable.ArrayBuffer.empty[Rule[Q]]
  initRules

  def get:List[Rule[Q]] = rules.toList


  protected def forRule(q:Q):RuleConstructor =  RuleConstructor(q)

  protected case class RuleConstructor(q: Q){

    private val tempRules = mutable.ArrayBuffer.empty[Rule[Q]]

    def add(predicate: Status=>Boolean): RuleConstructor = {
      tempRules.+=(new Rule[Q](q, predicate))
      return this
    }

    def apply = rules.++=(tempRules)

  }






}
