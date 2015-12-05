package korpus

import org.joda.time.LocalDate
import com.github.tototoshi.csv.CSVFormat
import org.joda.time.format.DateTimeFormat
import com.github.tototoshi.csv.QUOTE_ALL

import korpus.model.ArticleType
import korpus.model.NewspaperType

package object output {
  private val df = DateTimeFormat.forPattern("y-MM-d");
  def dateFormat(date: LocalDate): String = df.print(date)
  def articleTypeFormat(n:ArticleType.Value) = n match {
    case ArticleType.Message => "Wiadomość"
    case ArticleType.Comment => "Komentarz"
  }
  def newspaperTypeFormat(n:NewspaperType.Value) = n match {
    case NewspaperType.SE => "Super Express"
    case NewspaperType.F => "Fakt"
  }

  implicit val excelCsvFormat = new CSVFormat {
    val delimiter = ';'
    val quoteChar = '"'
    val escapeChar = '\\'
    val lineTerminator = "\r\n"
    val quoting = QUOTE_ALL
    val treatEmptyLineAsNil = false
  }
}