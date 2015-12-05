package korpus.model

sealed trait Token {
  val text:String
}
trait NoTextToken extends Token { val text="" }
trait HeaderToken extends NoTextToken
case class LineToken(text: String) extends Token
case class BoldedToken(text: String) extends Token
case object MessageToken extends HeaderToken
case object CommentToken extends HeaderToken
case object EmptyLineToken extends NoTextToken
case object EndOfDocumentToken extends NoTextToken