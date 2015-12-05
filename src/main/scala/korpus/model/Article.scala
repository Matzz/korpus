package korpus.model

object ArticleType extends Enumeration {
  val Message, Comment = Value
}
case class Article(artType:ArticleType.Value, header:String, content:Option[String], captions:Seq[String])