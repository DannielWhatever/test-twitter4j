package cl.app.rule

import cl.app.`type`.Category

/**
  * Created by Daniel on 02-03-2016.
  */
object CategoryRules extends Rules[Category.Value]
  with Serializable{

  override protected def initRules(): Unit = {


    RuleConstructor(Category.JAVA)
      .add(_.getText.toLowerCase.contains("java"))
      .apply

    RuleConstructor(Category.NET)
      .add(_.getText.toLowerCase.contains(".net"))
      .apply

    RuleConstructor(Category.JAVASCRIPT)
      .add(_.getText.toLowerCase.contains("javascript"))
      .apply

    RuleConstructor(Category.OTHER)
      .add(status=>{
        val text = status.getText.toLowerCase
        return !(text.contains("java") || text.contains(".net") || text.contains("javascipt"))
      })
      .apply

  }


}
