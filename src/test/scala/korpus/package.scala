
package object korpus {
  def resourceFile(name: String) = new java.io.File(getClass.getResource(name).toURI())
}