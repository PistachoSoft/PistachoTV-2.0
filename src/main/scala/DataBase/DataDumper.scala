package DataBase

import model.User
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.{read => readJson}
import tad.PTVProduction

import scala.io.Source

object DataDumper {

  def dumpRawData() = {
    //json formats
    implicit val formats = DefaultFormats

    val productionList = readJson[List[PTVProduction]](
                    Source.fromURL(getClass.getResource("/rawData.json")).mkString
                    )

    for(prod <- productionList) {
      println(prod)
      prod.getRawProductionMapped.save()
    }

    val anon = User.create

    anon.email("anon@not.need")
    anon.password("I got no password")

    anon.save()

  }

}
