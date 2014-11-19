package DataBase

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
      prod.getRawProductionMapped.save()
    }
  }

}
