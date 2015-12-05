package korpus

import java.io.File
import scala.annotation.tailrec
import korpus.model.Newspaper
import korpus.model.Newspaper
import korpus.model.NewspaperType
import org.joda.time.LocalDate

object FileList {
  def list(input: Seq[File]): Stream[(Newspaper, File)] = {
    val (dirs, files) = input.partition(_.isDirectory)
    val list = dirs.toStream.flatMap(filesRecursively(_)) ++ files.toStream
    list.map {
      case f:File => 
        val newspaper = Newspaper.nameToNewspaper(f.getName)
        (newspaper, f)
    }
  }

  private def filesRecursively(dir: File): Stream[File] = {
    val these = dir.listFiles
    val files = these.filter(_.isFile())
    val dirs =  these.filter(_.isDirectory).flatMap(filesRecursively)
    (files ++ dirs).toStream
  }
}