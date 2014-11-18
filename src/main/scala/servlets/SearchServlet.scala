package servlets

import java.sql.{PreparedStatement, ResultSet}
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}
import javax.servlet.annotation.WebServlet

import org.json4s.jackson.Serialization.{write => writeJson}
import org.json4s.DefaultFormats
import tad.{PTVUserMin, ReturnTrait, PTVProductionMin}

import scala.collection.mutable


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

  private def getMinUserRS(rs: ResultSet) = {
    new PTVUserMin(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getString(4))
  }

  /**
   * Executes a mysql query
   * @param query a mysql query
   * @param getRS a function that given a PreparedStatement generated from the query returns a ResultSet
   * @return the query result as a json
   */
  private def executeQuery(query: String
                           , getRS: (PreparedStatement) => ResultSet
                           , getData: (ResultSet) => ReturnTrait) = {
    var list = mutable.MutableList[ReturnTrait]()
    ConnectionPool.getConnection match{
      case Some(connection) =>
        try {
          val rs = getRS(connection.prepareStatement(query))
          while(rs.next()){
            list += getData(rs)
          }
        } catch {
          case exception: Exception =>
        } finally {
          if (!connection.isClosed) connection.close()
        }
      case None =>
        println("Not getting connection from connection pooling")
    }

    writeJson(list)
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
                  "WHERE title LIKE ? " +
                  "ORDER BY title ASC " +
                  limitPage(page)
        executeQuery(dbQuery
          , (st: PreparedStatement) => {
            st.setString(1, "%" + query + "%")
            st.executeQuery()
          }
          , (rs: ResultSet) => getMinProductionRS(rs)
        )
      case "u" =>
        dbQuery = "SELECT _id, name, surname, email " +
          "FROM user " +
          "WHERE email LIKE ? " +
          "ORDER BY surname,name,email ASC " +
          limitPage(page)
        executeQuery(dbQuery
          , (st: PreparedStatement) => {
            st.setString(1,"%" + query + "%")
            st.executeQuery()
          }
          , (rs: ResultSet) => getMinUserRS(rs)
        )
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

        executeQuery(dbQuery
          , (st: PreparedStatement) => st.executeQuery()
          , (rs: ResultSet) => getMinProductionRS(rs)
        )
      case "u" =>
        dbQuery = "SELECT _id, name, surname, email " +
          "FROM user " +
          "ORDER BY surname,name,email ASC " +
          limitPage(page)

        executeQuery(dbQuery
          , (st: PreparedStatement) => st.executeQuery()
          , (rs: ResultSet) => getMinUserRS(rs)
        )
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

    if(typeSearch == null && (typeSearch != "p" || typeSearch != "u")){
      resp.sendError(HSResp.SC_BAD_REQUEST)
      error = true
    }

    if(!error){
      val query = req.getParameter("q")
      var json = ""
      if(query == null){
        json = fetchDB(typeSearch,page,resp)
      }
      else{
        json = fetchDB(typeSearch,query,page,resp)
      }

      if(json != null){
        resp.setContentType("application/json")
        resp.getWriter.print(json)
      }
    }
  }
}
