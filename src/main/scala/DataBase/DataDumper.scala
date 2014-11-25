package DataBase

import java.io.File

import model.{Production, User}
import net.liftweb.common.Full
import net.liftweb.util.Props
import org.apache.lucene.analysis.es.SpanishAnalyzer
import org.apache.lucene.document._
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.store.{Directory, FSDirectory}
import org.apache.lucene.util.Version
import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.{read => readJson}
import tad.PTVProduction

import scala.io.Source

/**
 * Dumps the raw data into the bd
 */
object DataDumper {

  private def indexDocs(writer: IndexWriter) = {
    val productions = Production.findAll()
    val field = Props.get("lucene.field") openOr "context"

    for(prod <- productions){
      println("indexing: " + prod.title)
      val doc = new Document

      doc.add(new StringField("_id", prod._id.get+"", Field.Store.YES))

      doc.add(new TextField(field, prod.title.get, Field.Store.YES))

      if(prod.plot != "N/A"){
        doc.add(new TextField(field, prod.title.get, Field.Store.YES))
      }

      writer.addDocument(doc)
    }
  }

  /**
   * Generates whole lucene index
   */
  def generateLuceneIndex() = {
    val dir = FSDirectory.open(new File(Props.get("lucene.indexPath") openOr "index"))

    val analyzer = new SpanishAnalyzer(Version.LATEST)
    val iwc = new IndexWriterConfig(Version.LATEST, analyzer)

    // Always create a new index
    iwc.setOpenMode(OpenMode.CREATE)

    val writer = new IndexWriter(dir, iwc)

    indexDocs(writer)

    writer.close()
  }


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
