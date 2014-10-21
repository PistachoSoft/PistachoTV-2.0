package servlets

import java.sql.ResultSet
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}
import javax.servlet.annotation.WebServlet

import org.json4s.jackson.Serialization.{write => writeJson}
import org.json4s.DefaultFormats
import tad.{ReturnTrait, PTVProductionMin}



@WebServlet(urlPatterns = Array("/search"))
class SearchServlet extends HttpServlet{

  // json formats
  implicit val formats = DefaultFormats

  val itemsPage = 20

  /**
   * Generates de SQL for the LIMIT condition
   * @param page page number
   * @return
   */
  private def limitPage(page: Int) = {
    "LIMIT " + ((page-1)*itemsPage) + "," + (page*itemsPage)
  }

  /**
   * Generates a PTVProductionMin from the ResultSet row
   * @param rs the result set
   * @return
   */
  private def getMinProductionRS(rs: ResultSet) = {
    new PTVProductionMin(rs.getInt(1),rs.getString(2),rs.getInt(3),rs.getString(4))
  }

  /**
   * Executes a SQL query and returns the data in json format
   * @param query the query
   * @return
   */
  private def excecuteQuery(query: String) = {

    var list = List[ReturnTrait]()
    ConnectionPool.getConnection match{
      case Some(connection) =>
        try {
          val statement = connection.createStatement()
          val rs = statement.executeQuery(query)
          while(rs.next()){
            list = list.+:(getMinProductionRS(rs))
          }
        } catch {
          case exception: Exception =>
        } finally {
          if (!connection.isClosed) connection.close()
        }
      case None =>
        println("Not geting connection from connection pooling")
    }

    writeJson(list.reverse)
  }

  /**
   * Based on it's parameters, performs a search in the database return the result as json
   * @param typeSearch type search
   * @param query the query
   * @param page the page number
   * @param resp HSResp in case of error
   * @return
   */
  private def fetchDB(typeSearch: String, query: String, page: Int, resp: HSResp) = {
    var dbQuery = ""

    typeSearch match {
      case "p" =>
        dbQuery = "SELECT _id, title, year, image " +
                  "FROM production " +
                  "WHERE title LIKE '%" + query.replace("'","\\'") + "%' " +
                  "ORDER BY title ASC " +
                  limitPage(page)
        excecuteQuery(dbQuery)
      case "u" =>
        resp.sendError(HSResp.SC_NOT_IMPLEMENTED)
        null
      case _ =>
        resp.sendError(HSResp.SC_BAD_REQUEST)
        null
    }
  }

  /**
   * Based on it's parameters, performs a search in the database return the result as json
   * @param typeSearch type search
   * @param page the page number
   * @param resp HSResp in case of error
   * @return
   */
  private def fetchDB(typeSearch: String, page: Int, resp: HSResp): String = {
    var dbQuery = ""

    typeSearch match {
      case "p" =>
        dbQuery = "SELECT _id, title, year, image FROM production ORDER BY title ASC " +
                      limitPage(page)
        excecuteQuery(dbQuery)
      case "u" =>
        resp.sendError(HSResp.SC_NOT_IMPLEMENTED)
        null
      case _ =>
        resp.sendError(HSResp.SC_BAD_REQUEST)
        null
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
    val typeFetch = req.getParameter("t")

    if(typeFetch == null){
      resp.sendError(HSResp.SC_BAD_REQUEST)
      error = true
    }

    if(!error){
      val query = req.getParameter("q")
      var json = ""
      if(query == null){
        json = fetchDB(typeFetch,page,resp)
      }
      else{
        json = fetchDB(typeFetch,query,page,resp)
      }

      if(json != null){
        resp.setContentType("application/json")
        resp.getWriter.print(json)
      }
    }
  }
}
