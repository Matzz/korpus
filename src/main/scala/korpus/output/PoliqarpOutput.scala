package korpus.output

import java.io.File
import org.joda.time.LocalDate
import korpus.model.ArticleType
import korpus.model.NewspaperType
import korpus.Converter
import com.github.tototoshi.csv.CSVWriter

object PoliqarpOutput {
  import korpus.output.excelCsvFormat
  
  object ContentType extends Enumeration {
    val header = Value("h")
    val content = Value("t")
    val caption = Value("c")
  }
  case class Entry(
    newspaper:NewspaperType.Value,
    from:LocalDate,
    artType:ArticleType.Value,
    contentType:ContentType.Value)

  def writer(outputDir: File, stream: Converter.ArticlesStream) {
    val grouped = group(stream)
    print(outputDir, grouped)
  }
  
  private def print(outputDir: File, groups:Stream[(Entry, Seq[String])]) {
    outputDir.mkdirs()
    val legendFile = new File(outputDir, "legend.csv")
    val legendWriter = CSVWriter.open(legendFile)
    legendWriter.writeRow(Seq("Newspaper", "From", "Art type", "Content type", "File name"))
    
    val filesList = groups.foreach {
      case (entry, content) =>
        val outputName = outputFileName(entry)
        val file = new File(outputDir, outputName)
        val writer = CSVWriter.open(file)
        writer.writeAll(content.map(Seq(_)))
        writer.close()
        
        val Entry(newspaper, from, artType, contentType) = entry
        legendWriter.writeRow(Seq(newspaper, from, artType, contentType, outputName))
    }
    legendWriter.close()
  }
  
  
  private def group(stream: Converter.ArticlesStream) = {
    stream.flatMap {
      case (newspaper, arts) =>
        def getEntry(at:ArticleType.Value, ct:ContentType.Value) =
          Entry(newspaper.newspaper, newspaper.from, at, ct)

        val (msgs, cmts) = arts.partition(_.artType==ArticleType.Message)
        Seq(
          (getEntry(ArticleType.Message, ContentType.header), msgs.map(_.header)),
          (getEntry(ArticleType.Message, ContentType.content), msgs.flatMap(_.content)),
          (getEntry(ArticleType.Message, ContentType.caption), msgs.flatMap(_.captions)),
          (getEntry(ArticleType.Comment, ContentType.header), cmts.map(_.header)),
          (getEntry(ArticleType.Comment, ContentType.content), cmts.flatMap(_.content)),
          (getEntry(ArticleType.Comment, ContentType.caption), cmts.flatMap(_.captions))
        )
    }
  }
  
  def outputFileName(e:Entry) = s"${e.newspaper}_${dateFormat(e.from)}_${e.artType}_${e.contentType}.csv"

}