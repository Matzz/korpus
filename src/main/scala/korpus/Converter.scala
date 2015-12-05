package korpus

import java.io.File
import korpus.model.Newspaper
import korpus.model.Article

object Converter {
  type ArticlesStream = Stream[(Newspaper, List[Article])]
  
  def convert(input:Seq[File]):ArticlesStream = {
      val list = FileList.list(input)
      val parsed = list.par.map {
        case (newspaper, file) =>
          val tokens = Lexer.tokenize(file)
          val articles = Parser.parse(tokens.toStream)
          (newspaper, articles.toList)
      }
      parsed.toStream
  }
}