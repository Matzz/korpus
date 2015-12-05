package korpus.model

import org.joda.time.LocalDate

object NewspaperType extends Enumeration {
  val SE, F = Value
}
object Newspaper {
  private val newspeperTypePatt = """(F|SE)\."""
  private val suffixPatt = """\.(\d{4})\.docx"""
  private val singleDayPattern = (newspeperTypePatt+"""(\d{1,2})\.(\d{1,2})"""+suffixPatt).r
  private val weekendPattern   = (newspeperTypePatt+"""(\d{1,2})-(\d{1,2})\.(\d{1,2})"""+suffixPatt).r
  private val crossMonthWeekendPattern
     = (newspeperTypePatt+"""(\d{1,2})\.(\d{1,2})\.?-(\d{1,2})\.(\d{1,2})"""+suffixPatt).r
  
  def nameToNewspaper(name:String):Newspaper = {
    name match {
      case singleDayPattern(n, d, m, y) =>
        val date = new LocalDate(y.toInt, m.toInt, d.toInt)
        Newspaper(NewspaperType.withName(n), date, date)
      case weekendPattern(n, dayFrom, dayTo, m, y) =>
        val from = new LocalDate(y.toInt, m.toInt, dayFrom.toInt)
        val to = new LocalDate(y.toInt, m.toInt, dayTo.toInt)
        Newspaper(NewspaperType.withName(n), from, to)
      case crossMonthWeekendPattern(n, dayFrom, monthFrom, dayTo, monthTo, y) =>
        val from = new LocalDate(y.toInt, monthFrom.toInt, dayFrom.toInt)
        val to = new LocalDate(y.toInt, monthTo.toInt, dayTo.toInt)
        Newspaper(NewspaperType.withName(n), from, to)
      case _ => throw new RuntimeException(s"Invalid name $name")
    }
  }
}
case class Newspaper(newspaper:NewspaperType.Value, from:LocalDate, to:LocalDate)