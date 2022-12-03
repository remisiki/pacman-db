import java.io.File
import java.nio.file.Paths

class Pac(
	var name: String = "",
	var version: String = "",
	var desc: String = "",
	var packageSize: Int = 0,
	var installSize: Int = 0,
	var md5sum: String = "",
	var sha256sum: String = "",
	var pgpSig: String = "",
	var url: String = "",
	var license: String = "",
	var arch: String = "",
	var date: Long = 0,
	var packagerName: String = "",
	var packagerEmail: String = "",
	var depends: String = "",
	var provides: String = ""
	) {

	var config: String = null

	def this(file: File) = {
		this()
		this.config = io.readFile(file.getPath())
		this.name = this.getConfigField("NAME")
		this.version = this.getConfigField("VERSION")
		this.desc = this.getConfigField("DESC")
		this.packageSize = this.getConfigField("CSIZE").toInt
		this.installSize = this.getConfigField("ISIZE").toInt
		this.md5sum = this.getConfigField("MD5SUM").trim()
		this.sha256sum = this.getConfigField("SHA256SUM").trim()
		this.pgpSig = this.getConfigField("PGPSIG").trim()
		this.url = this.getConfigField("URL").trim()
		this.license = this.getConfigField("LICENSE").split("\n")(0).toUpperCase().replaceAll("(\\s)|(CUSTOM:)", "")
		this.arch = this.getConfigField("ARCH")
		this.date = this.getConfigField("BUILDDATE").toLong
		val packager = this.getConfigField("PACKAGER")
		this.packagerName = packager.substring(0, packager.indexOf("<")).trim()
		this.packagerEmail = packager.substring(packager.indexOf("<") + 1, packager.indexOf(">"))
		this.depends = this.getConfigField("DEPENDS")
		this.provides = this.getConfigField("PROVIDES")
	}

	override def toString: String = {
		s"${this.name} ${this.version}"
	}

	def getConfigField(name: String): String = {
		val regex = s"%${name}%(.|\\n)*?(%|$$)".r
		regex.findFirstIn(this.config) match {
			case Some(s: String) => {
				s.replaceAll("(%.*?%)|%", "").trim()
			}
			case None => null
		}
	}

}

object Pac {

	def getPackages(): Array[Pac] = {
		val dataDir = "./data"
		val packageDirs = (new File(dataDir))
			.listFiles()
			.filter(_.isDirectory)
		packageDirs.map { s =>
			val descFile = new File(Paths.get(dataDir, s.getName, "desc").toString)
			new Pac(descFile)
		}
	}

	def getLicenses(packages: Array[Pac]): Array[String] = {
		packages
			.map { p => p.license }
			.toSet
			.toArray
	}

	def getArchitectures(packages: Array[Pac]): Array[String] = {
		packages
			.map { p => p.arch }
			.toSet
			.toArray
	}

	def getPackagers(packages: Array[Pac]): Array[(String, String)] = {
		packages
			.map { p => (p.packagerName, p.packagerEmail) }
			.toSet
			.toArray
	}

}