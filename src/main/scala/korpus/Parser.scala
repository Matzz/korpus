package korpus

import scala.collection.mutable.ListBuffer
import korpus.model._

class InvalidTokenException(msg: String) extends RuntimeException(msg)

object Parser {

   class ArticleBuffer(
        var artType:ArticleType.Value = ArticleType.Message,
        val header: ListBuffer[Token] = ListBuffer(),
        val content: ListBuffer[Token] = ListBuffer(),
        val picturesDescriptions: ListBuffer[BoldedToken] = ListBuffer()) {
    def isHeaderEnded = header.size>0 && header.takeRight(1).forall(_ == EmptyLineToken)
  }

  def parse(tokens: Stream[Token]) = {
    def isSeparator(t: Token) = t==EndOfDocumentToken || t.isInstanceOf[HeaderToken]

    groupStream(tokens, isSeparator, (list: Seq[Token]) => {
      val buffer = list.foldLeft(new ArticleBuffer()) {
        case (buffer, token) =>
          token match {
            case CommentToken =>
              buffer.artType = ArticleType.Comment
            case MessageToken =>
              buffer.artType = ArticleType.Message
            case LineToken(_) if buffer.isHeaderEnded =>
              buffer.content += token
            case LineToken(_) => //First token
              buffer.header += token
            case EmptyLineToken if buffer.isHeaderEnded =>
              buffer.content += token
            case EmptyLineToken =>
              buffer.header += token
            case boldToken @ BoldedToken(_) =>
              buffer.picturesDescriptions += boldToken
          }
          buffer
      }
      bufferToArticle(buffer)
    });
  }

  private def groupStream[A, B](s: Stream[A], isSeparator: A => Boolean, group: Seq[A] => B): Stream[B] = {
    def isNotSeparator(i: A) = !isSeparator(i)
    val header = s.takeWhile(isSeparator)
    val tail = s.dropWhile(isSeparator)
    val artTokens = tail.takeWhile(isNotSeparator)
    artTokens match {
      case at if at.length > 0 => group(header ++ at) #:: groupStream(tail.dropWhile(isNotSeparator), isSeparator, group)
      case _                 => Stream.empty[B]
    }
  }

  private def bufferToArticle(articleBuffer: ArticleBuffer) = {
    val header = tokensToString(skipEmptyLinesAtTheEdges(articleBuffer.header))
    val content = skipEmptyLinesAtTheEdges(articleBuffer.content) match {
      case l if l.isEmpty => None
      case l              => Some(tokensToString(l))
    }
    val picturesDescriptions = articleBuffer
      .picturesDescriptions
      .map(_.text)
      .toList
    Article(articleBuffer.artType, header, content, picturesDescriptions)
  }

  private def skipEmptyLinesAtTheEdges(tokens: Seq[Token]) = {
    val cleanHead = tokens.dropWhile(_ == EmptyLineToken)
    cleanHead.reverse.dropWhile(_ == EmptyLineToken).reverse
  }
  private def tokensToString(tokens: Seq[Token]) = {
    tokens.map(_.text).mkString("\n")
  }

}