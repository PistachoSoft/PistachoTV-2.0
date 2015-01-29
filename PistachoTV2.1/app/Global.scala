import models._
import play.api.db.DB
import play.api.libs.json.Json
import play.api.{Logger, Application, GlobalSettings}

import scala.slick.driver.MySQLDriver.simple._
import scala.io.Source

import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application) = {

    implicit val prodFormat = Json.format[Production]
    implicit val dumpFormat = Json.format[DumpProduction]

    val database = Database.forDataSource(DB.getDataSource())
    database withSession {
      implicit rs =>
        if(Productions.count == 0){
          val list = Json.parse(Source.fromFile("conf/data/rawData.json").map(_.toByte).toArray)
            .as[DumpProduction]

          var i = 0
          for(prod <- list.dump){
            i += 1
            if(i%50 == 0)
              Logger.info(s"Dumping data ... ${i.toDouble/list.dump.size*100}%")
            Productions.insert(prod)
          }
          Logger.info("Done")
          Logger.info("Inserting anon ...")

          Users.insert(User(
            name = "/a/non",
            surname = "who?",
            birthday = "2015",
            address = "no-where",
            email = "anon@nit.need",
            phone = 666666666,
            password = Some("I got no password")))

          Logger.info("Done")

          DataDumper.generateLuceneIndex(Productions.findAll)
          Logger.info("Done")
        }
    }
  }
}
