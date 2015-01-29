import java.io.File

import models.{Production, Productions}
import org.apache.lucene.analysis.es.SpanishAnalyzer
import org.apache.lucene.document.{Document, Field, StringField, TextField}
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.index.{IndexWriter, IndexWriterConfig}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import play.api.Logger

object DataDumper {

  private def indexDocs(writer: IndexWriter,productions: List[Production]) = {
    val field = "context"

    var i = 0

    for(prod <- productions){
      i += 1
      if(i%50 == 0)
        Logger.info(s"Generating lucene index ... ${i.toDouble/productions.size*100}%")
      val doc = new Document

      doc.add(new StringField("_id", prod._id.get+"", Field.Store.YES))

      doc.add(new TextField(field, prod.title, Field.Store.YES))

      if(prod.plot != "N/A"){
        doc.add(new TextField(field, prod.title, Field.Store.YES))
      }

      writer.addDocument(doc)
    }
  }

  /**
   * Generates whole lucene index
   */
  def generateLuceneIndex(prods: List[Production]) = {
    val dir = FSDirectory.open(new File("index"))

    val analyzer = new SpanishAnalyzer(Version.LATEST)
    val iwc = new IndexWriterConfig(Version.LATEST, analyzer)

    // Always create a new index
    iwc.setOpenMode(OpenMode.CREATE)

    val writer = new IndexWriter(dir, iwc)

    indexDocs(writer,prods)

    writer.close()
  }
}
