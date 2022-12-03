import java.time.{LocalDateTime, ZoneOffset}

object Main {
	def main(args: Array[String]): Unit = {
		val packages: Array[Pac] = Pac.getPackages()
		val db = new Sql()
		db.init()
		db.executeBatch("./scripts/init.sql")
		var queryString = ""
		// Insert license
		val licenses: Array[String] = Pac.getLicenses(packages)
		queryString =
			licenses
				.foldLeft(
					"""
						INSERT INTO core.license(name) VALUES
					"""
				) {(s, l) => s + s"""("${l}"),"""}
				.dropRight(1)
		db.execute(queryString)
		// Insert archs
		val archs: Array[String] = Pac.getArchitectures(packages)
		queryString =
			archs
				.foldLeft(
					"""
						INSERT INTO core.arch(name) VALUES
					"""
				) {(s, l) => s + s"""("${l}"),"""}
				.dropRight(1)
		db.execute(queryString)
		// Insert packagers
		val packagers: Array[(String, String)] = Pac.getPackagers(packages)
		queryString =
			packagers
				.foldLeft(
					"""
						INSERT INTO core.packager(name, email) VALUES
					"""
				) {(s, l) => s + s"""("${l._1}","${l._2}"),"""}
				.dropRight(1)
		db.execute(queryString)
		// Insert packages
		db.executeBatch("./scripts/addTmpCol.sql")
		queryString =
			packages
				.foldLeft(
					"""
						INSERT INTO core.package(
							name, version, description, csize, isize,
							md5sum, sha256sum, pgpSig, url, buildDate,
							licenseName, archName, packagerName
						) VALUES
					"""
				) {(s, p) => s + s"""(
					"${p.name}","${p.version}","${p.desc.replaceAll("\"", "\"\"").replaceAll("'", "''")}",
					${p.packageSize},${p.installSize},
					"${p.md5sum}","${p.sha256sum}","${p.pgpSig}",
					"${p.url}","${LocalDateTime.ofEpochSecond(p.date, 0, ZoneOffset.UTC)}",
					"${p.license}","${p.arch}","${p.packagerName}"
				),"""}
				.dropRight(1)
		db.execute(queryString)
		// Update package info
		db.executeBatch("./scripts/update.sql")
		db.executeBatch("./scripts/removeTmpCol.sql")
		// Insert dependency
		packages.foreach { p => p.depends match {
			case s: String => {
				val dependString: String =
					p.depends
						.split("\\n")
						.foldLeft("") {(x, y) => s"""${x}"${y}","""}
						.dropRight(1)
				queryString = s"""
					SET @id = (SELECT id FROM core.package WHERE name = "${p.name}")
				"""
				db.execute(queryString)
				queryString = s"""
					INSERT INTO core.dependency(packageId, dependId)
					SELECT @id, id
					FROM core.package
					WHERE name IN (${dependString})
				"""
				db.execute(queryString)
			}
			case null => ()
		}}
		db.close()
	}
}