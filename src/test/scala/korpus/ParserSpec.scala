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
class ParserSpec extends Specification {
  "Parse" should {
    val a1 = Article(ArticleType.Message, "H1\nH2", Some("C1\nC2\nC3"), Seq("P1","P2"))
    val a2 = Article(ArticleType.Comment, "H1", Some("C1"), Nil)
    val s1 = Seq(
          MessageToken,
          LineToken("H1"),
          LineToken("H2"),
          EmptyLineToken,
          LineToken("C1"),
          LineToken("C2"),
          LineToken("C3"),
          BoldedToken("P1"),
          BoldedToken("P2")
          )
      val s2 =  Seq(
          CommentToken,
          LineToken("H1"),
          EmptyLineToken,
          LineToken("C1")
          )
    "convert stream of single article" in {
        val s = (s1 :+ EndOfDocumentToken).toStream
        Parser.parse(s) === Stream(a1)
    }
    "convert stream of multiple article" in {
        val s = (s1 ++ s2 :+ EndOfDocumentToken).toStream
        Parser.parse(s) === Stream(a1, a2)
    }
  }
}