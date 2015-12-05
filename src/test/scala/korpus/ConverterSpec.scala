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

@RunWith(classOf[JUnitRunner])
class ConverterSpec extends Specification {
  "Files" should {
    "parsed to articles" in {
      val baseDir = new File(getClass.getResource("/docs").toURI())
      val parsed = Converter.convert(Seq(baseDir))
      parsed.size === 50
      parsed.flatMap(_._2).size === 402
    }
  }
}