package korpus

import java.io.File
import scala.swing._
import java.io.File
import scala.concurrent._
import korpus.output.CsvOutput
import korpus.output.PoliqarpOutput

class GUI extends MainFrame {
  implicit val ec = ExecutionContext.global

  title = "Korpus parser"
  preferredSize = new Dimension(600, 300)

  val textArea = new TextArea {
    enabled = false
  }
  val inputFileChoorser = new FileChooser(new File("./")) {
    multiSelectionEnabled = true
    fileSelectionMode = FileChooser.SelectionMode.FilesAndDirectories
  }
  val outputFileChoorser = new FileChooser(new File("./")) {
    fileSelectionMode = FileChooser.SelectionMode.FilesAndDirectories
  }
  val inputFileButton = new Button("Input")
  val outputFileButton = new Button("Output")
  val startCSVButton = new Button("Start CSV")
  val startPoliqarpButton = new Button("Start Poliqarp")
  val inputFileLabel = new Label("No path...")
  val outputFileLabel = new Label("No path...")

  val mainPanel = new BoxPanel(Orientation.Vertical) {
    contents += new BoxPanel(Orientation.Horizontal) {
      contents += textArea
    }
    contents += new GridPanel(2, 2) {
      contents += inputFileButton
      contents += inputFileLabel
      contents += outputFileButton
      contents += outputFileLabel
    }
    contents += startCSVButton
    contents += startPoliqarpButton
  }
  contents = mainPanel

  listenTo(inputFileButton, outputFileButton, startCSVButton, startPoliqarpButton)

  def validate(i:Seq[File],o:File)(action:(Seq[File], File)=>Unit) = {
      (i, o) match {
        case (i, _) if i == null || i.size==0 =>
          textArea.text = "Input does not exists"
        case (_, null) =>
          textArea.text = "Output not selected"
        case (_, o) if o.exists =>
          textArea.text = "Output already exists"
        case (i, o) =>
          textArea.text = "Processing..."
          toggleButtons(false)
          Future {
            action(i, o)
            toggleButtons(true)
          }
      }
  }

  reactions += {
    case swing.event.ButtonClicked(`inputFileButton`) =>
      if (inputFileChoorser.showSaveDialog(mainPanel) == FileChooser.Result.Approve) {
        inputFileLabel.text = inputFileChoorser.selectedFiles.map(_.getAbsolutePath).mkString("; ")
      }
    case swing.event.ButtonClicked(`outputFileButton`) =>
      if (outputFileChoorser.showSaveDialog(mainPanel) == FileChooser.Result.Approve) {
        outputFileLabel.text = outputFileChoorser.selectedFile.getAbsolutePath
      }
    case swing.event.ButtonClicked(`startCSVButton`) =>
      validate(inputFileChoorser.selectedFiles, outputFileChoorser.selectedFile) {
        case (i,o) => textArea.text = processCsv(i, o)
      }
    case swing.event.ButtonClicked(`startPoliqarpButton`) =>
      validate(inputFileChoorser.selectedFiles, outputFileChoorser.selectedFile) {
        case (i,o) => textArea.text = processPoliqarp(i, o)
      }
  }

  def toggleButtons(isEnabled: Boolean) = {
    inputFileButton.enabled = isEnabled
    outputFileButton.enabled = isEnabled
    startCSVButton.enabled = isEnabled
    startPoliqarpButton.enabled = isEnabled
  }

  def processCsv(input: Seq[File], output: File): String = {
    try {
      val stream = Converter.convert(input)
      CsvOutput.writer(output, stream)
      "CSV data extracted"
    } catch {
      case e => e.getMessage
    }
  }

  def processPoliqarp(input: Seq[File], output: File): String = {
    try {
      val stream = Converter.convert(input)
      PoliqarpOutput.writer(output, stream)
      "Poliqarp data extracted"
    } catch {
      case e => e.getMessage
    }
  }
}

object GUI {
  def main(args: Array[String]) {
    val ui = new GUI
    ui.visible = true
  }
}