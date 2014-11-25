package servlets

import java.io.File
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}

import model.{Production, User}
import net.liftweb.common.Full
import net.liftweb.json.Serialization.write
import net.liftweb.mapper._
import net.liftweb.util.Props
import org.apache.lucene.index.DirectoryReader
import org.apache.lucene.queryparser.classic.QueryParser
import org.apache.lucene.search.{Query, IndexSearcher}
import org.apache.lucene.store.FSDirectory
import org.apache.lucene.util.Version
import org.apache.lucene.analysis.es.SpanishAnalyzer
import tad.PTVProduction

import scala.collection.mutable


@WebServlet(urlPatterns = Array("/search"))
class SearchServlet extends HttpServlet{

  var searcher: IndexSearcher = null

  // json formats
  implicit val formats = net.liftweb.json.DefaultFormats

  // Items shown per page
  val itemsPerPage = 20


  override def init() = {
    searcher = new IndexSearcher(
      DirectoryReader.open(
        FSDirectory.open(
          new File(
            Props.get("lucene.indexPath") openOr "index"
          )
        )
      )
    )

  }

  // Method that gets the DB's start row given a page
  def getStartAt(page: Int) = (page-1)*itemsPerPage

  private def fetchQueryProds(query: String, page: Int) = {
    val prodList = Production.findAllFields(
      Seq(Production._id,Production.title,Production.year,Production.image)
      , OrderBy(Production.title,Ascending)
      , Like(Production.title,"%" + query + "%")
      , MaxRows(itemsPerPage)
      , StartAt(getStartAt(page)))

    write(prodList.map(x => x.asPTVProductionMin))
  }

  def doQuerySearch(query: Query, start: Int) = {
    val r = new mutable.MutableList[PTVProduction]

    val results = searcher.search(query,start+itemsPerPage)
    val hits = results.scoreDocs

    val endLoop = math.min(results.totalHits,start+itemsPerPage)

    for(i <- start to endLoop-1){
      val doc = searcher.doc(hits(i).doc)

      Production.findByKey(doc.get("_id").toLong) match {
        case Full(production) =>
          r += production.asPTVProduction
        case _ =>
      }
    }

    r
  }

  /**
   * Based on it's parameters, performs a search in the database return the result as json
   * @param typeSearch type search
   * @param query the query
   * @param page the page number
   * @return
   */
  private def fetchDB(typeSearch: String, query: String, page: Int) = {
    typeSearch match {
      case "p" =>
        fetchQueryProds(query,page)

      case "l" =>
        if(query == ""){
          fetchQueryProds(query,page)
        } else {
          val parser = new QueryParser(Version.LATEST
                                      , Props.get("lucene.field") openOr "context"
                                      , new SpanishAnalyzer(Version.LATEST))

          write(doQuerySearch(parser.parse(query),getStartAt(page)))
        }

      case "u" =>
        val userList = User.findAllFields(
          Seq(User._id, User.name, User.surname, User.email)
          , By_>(User._id,1)
          , Like(User.email, "%" + query + "%")
          , OrderBy(User.surname,Ascending)
          , OrderBy(User.name,Ascending)
          , OrderBy(User.email,Ascending)
          , MaxRows(itemsPerPage)
          , StartAt(getStartAt(page))
        )

        write(userList.map(x => x.asPTVUserMin))
    }
  }


  /**
   * Based on it's parameters, performs a search in the database return the result as json
   * @param typeSearch type search
   * @param page the page number
   * @return
   */
  private def fetchDB(typeSearch: String, page: Int): String = {
    typeSearch match {
      case "u" =>
        val userList = User.findAllFields(
          Seq(User._id, User.name, User.surname, User.email)
          , By_>(User._id,1)
          , OrderBy(User.surname,Ascending)
          , OrderBy(User.name,Ascending)
          , OrderBy(User.email,Ascending)
          , MaxRows(itemsPerPage)
          , StartAt(getStartAt(page)))

        write(userList.map(x => x.asPTVUserMin))

      case _ =>
        fetchQueryProds("",page)
    }
  }

  override def doGet(req: HSReq, resp: HSResp) = {
    /*
    Parameters:
      t: <p|u> -> type
      [q: String] -> query to match
      p: Int -> page
     */

    var error = false

    // parameter [p] treatment
    var page = 0
    try{
       page = req.getParameter("p").toInt
    } catch {
      case nfe: NumberFormatException =>
        resp.sendError(HSResp.SC_BAD_REQUEST)
        error = true
    }

    // parameter [t] treatment
    val typeSearch = req.getParameter("t")

    if(typeSearch == null && (typeSearch != "p" || typeSearch != "u" ||typeSearch != "l")){
      resp.sendError(HSResp.SC_BAD_REQUEST)
      error = true
    }

    if(!error){
      val query = req.getParameter("q")
      var json = ""
      if(query == null){
        json = fetchDB(typeSearch,page)
      }
      else{
        json = fetchDB(typeSearch,query,page)
      }

      if(json != null){
        resp.setContentType("application/json")
        resp.getWriter.print(json)
      }
    }
  }
}
