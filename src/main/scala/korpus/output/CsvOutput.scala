package korpus.output

import com.github.tototoshi.csv.CSVWriter
import java.io.File
import korpus.model.Newspaper
import korpus.model.Article
import korpus.Converter

object CsvOutput {
  import korpus.output.excelCsvFormat

  def writer(outputFile: File, stream: Converter.ArticlesStream) {
    val writer = CSVWriter.open(outputFile)
    writer.writeRow(header)
    for (
      (newspaper, arts) <- stream;
      art <- arts
    ) {
      writer.writeRow(createRow(newspaper, art))
    }
    writer.close()
  }
  
  private val header = List(
      "Gazeta",
      "Typ wiadomości",
      "Od",
      "Do",
      "Nagłówek",
      "Treść",
      "Podpisy pod zdjęciami")

  private def createRow(nw: Newspaper, art: Article) = {
    List(
      newspaperTypeFormat(nw.newspaper),
      articleTypeFormat(art.artType),
      dateFormat(nw.from),
      dateFormat(nw.to),
      normalizeNewLines(art.header),
      normalizeNewLines(art.content.getOrElse("")),
      art.captions.map(normalizeNewLines(_)).mkString("\n"))
  }
  
  private def normalizeNewLines(text:String) = text.replace("\r", "")
}