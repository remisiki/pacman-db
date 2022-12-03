import scala.io.Source

object io {

	def readFile(fileName: String): String = {
		Source.fromFile(fileName).mkString
	}

}