package korpus

import org.apache.poi.xwpf.extractor.XWPFWordExtractor
import java.io.File
import org.apache.poi.POIXMLDocument
import org.apache.poi.xwpf.usermodel.XWPFDocument
import scala.collection.JavaConversions._
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import korpus.model._

object Lexer {
  def tokenize(file: File): Seq[Token] = tokenize(file.getPath)
  def tokenize(filePath: String): Seq[Token] = {
    val poiPackage = POIXMLDocument.openPackage(filePath)
    val document = new XWPFDocument(poiPackage);

    extractDocumentTokens(document)
  }

  private def extractDocumentTokens(document: XWPFDocument): Seq[Token] = {
    val documentTokens = documentParagraphs(document)
      .map { paragraph =>
        val runs = paragraph.getRuns

        paragraph.getText.trim match {
          case l if l.isEmpty                 => EmptyLineToken
          case l if runs(0).isBold            => BoldedToken(l)
          case l if l.startsWith("Wiadomość") => MessageToken
          case l if l.startsWith("Komentarz") => CommentToken
          case l                              => LineToken(l)
        }
      }

    documentTokens :+ EndOfDocumentToken
  }

  private def documentParagraphs(document: XWPFDocument) = document
    .getBodyElements
    .map {
      case p: XWPFParagraph => p
      case x                => throw new Exception("Invalid type " + x.getClass)
    }

}