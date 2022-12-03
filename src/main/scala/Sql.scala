import scalikejdbc._
import scalikejdbc.config.DBs

class Sql {

	def init(): Unit = {
		DBs.setupAll()
	}

	def close(): Unit = {
		DBs.closeAll()
	}

	def query(queryString: String): List[Any] = {
		NamedDB("umrw") readOnly { implicit session =>
			SQL(queryString).map(_.toMap()).list.apply()
		}
	}

	def execute(queryString: String): Boolean = {
		NamedDB("umrw") autoCommit { implicit session =>
			SQL(queryString).execute.apply()
		}
	}

	def executeBatch(fileName: String): Boolean = {
		val batch: Array[String] = this.filterComment(io.readFile(fileName)).split(";")
		var success = true
		NamedDB("umrw") autoCommit { implicit session =>
			batch.foreach(queryString => {
				success = success & SQL(queryString).execute.apply()
			})
		}
		success
	}

	def filterComment(s: String): String = {
		s.replaceAll("(\\/\\*(.|\\n)*?\\*\\/)|(-- .*?\\n)", "")
	}

}