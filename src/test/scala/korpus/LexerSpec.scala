package korpus

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner._
import org.junit.runner.RunWith
import korpus.model.NewspaperType
import org.joda.time.LocalDate
import korpus.model.Newspaper
import scala.io.Source
import java.io.File
import korpus.model._

@RunWith(classOf[JUnitRunner])
class LexerSpec extends Specification {
  "Lexer" should {
    "find tokens in document" in {
      val file = resourceFile("/docs/Fakt/01/F.10.01.2012.docx")
      val grupped = Lexer
        .tokenize(file)
        .groupBy { _.getClass }
      grupped(classOf[LineToken]).size === 17
      grupped(classOf[BoldedToken]).size === 3
      grupped(MessageToken.getClass).size === 8
      grupped(CommentToken.getClass).size === 1
      grupped(EmptyLineToken.getClass).size === 23
      grupped(EndOfDocumentToken.getClass).size === 1
    }
  }
}