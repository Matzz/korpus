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
class FileListSpec extends Specification {
  "File list" should {
    val baseDir = new File(getClass.getResource("/docs").toURI())
    val list = FileList.list(Seq(baseDir))
    "Find all files in dir" in {
      val result = list.groupBy {
        case (np, file) => np.newspaper
      }
      result(NewspaperType.F).toList.size === 25
      result(NewspaperType.SE).toList.size === 25
    }
  }
}