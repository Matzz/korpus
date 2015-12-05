package korpus

import org.specs2.mutable._
import org.specs2.runner.JUnitRunner
import org.junit.runner._
import org.junit.runner.RunWith
import korpus.model.NewspaperType
import org.joda.time.LocalDate
import korpus.model.Newspaper

@RunWith(classOf[JUnitRunner])
class NewspaperSpec extends Specification {
  "Newspaper name parser should pare file names" >> {
    "with single date title" >> {
      val from1 = new LocalDate(2012, 1, 2)
      Newspaper.nameToNewspaper("F.2.01.2012.docx") ===
        Newspaper(NewspaperType.F, from1, from1)
            
      val from2 = new LocalDate(2014, 11, 15)
      Newspaper.nameToNewspaper("SE.15.11.2014.docx") ===
        Newspaper(NewspaperType.SE, from2, from2) 
    }
  }
}