package controllers

import java.io.File

import org.apache.lucene.analysis.es.SpanishAnalyzer
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{IndexSearcher, Query}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version

import scala.Some
import models.{Production, ProductionMin, Productions}
import models.Data._
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.api.db.slick._

import scala.collection.mutable

object ProdCont extends Controller{
  implicit val prodFormat = Json.format[Production]
  implicit val prodMinFormat = Json.format[ProductionMin]


  def findProduction(id: Long) = DBAction{
    implicit rs =>
      Productions.findById(id) match {
        case Some(prod) =>
          Ok(Json.toJson(prod))
        case None =>
          NotFound("Resource not found")
      }
  }

  def listProductions(page: Int) = DBAction{
    implicit rs =>
      Ok(Json.toJson(Productions.list(page = page)))
  }

  def listProductionsF(page: Int, filter: String) = DBAction{
    implicit rs =>
      Ok(Json.toJson(Productions.list(page = page, filter = s"%$filter%")))
  }

  def listProductionsL(page: Int, filter: String) = DBAction{ implicit rs =>
    val parser = new QueryParser(Version.LATEST
      , "context"
      , new SpanishAnalyzer(Version.LATEST))
    Ok(Json.toJson(doQuerySearch(parser.parse(filter),page*defaultPageSize)))
  }

  private def doQuerySearch(query: Query, start: Int)(implicit s: Session) = {
    val searcher = new IndexSearcher(
      DirectoryReader.open(
        FSDirectory.open(
          new File("index")
        )
      )
    )

    val r = new mutable.MutableList[Production]

    val results = searcher.search(query,start+defaultPageSize)
    val hits = results.scoreDocs

    val endLoop = math.min(results.totalHits,start+defaultPageSize)

    for(i <- start to endLoop-1){
      val doc = searcher.doc(hits(i).doc)

      Productions.findById(doc.get("_id").toLong) match {
        case Some(production) =>
          r += production
        case None =>
      }
    }

    r.toList.map(new ProductionMin(_))
  }
}
