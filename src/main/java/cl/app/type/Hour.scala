package cl.app.`type`

import java.time.{LocalDateTime, ZoneId}
import java.util.Date

/**
  * Created by Daniel on 02-03-2016.
  */
object Hour extends Enumeration{

  type Hour = Value

  val MORNING = Value("06~12")
  val MEDLEY = Value("12~16")
  val EVENING = Value("16~21")
  val NIGHT = Value("22~06")

  def of(date: Date): Hour.Value = LocalDateTime.ofInstant(date.toInstant, ZoneId.systemDefault).getHour match {
      case 6|7|8|9|10|11 => Hour.MORNING
      case 12|13|14|15|16 => Hour.MEDLEY
      case 16|17|18|19|20|21 => Hour.EVENING
      case 22|23|0|1|2|3|4|5 => Hour.NIGHT
  }

}
