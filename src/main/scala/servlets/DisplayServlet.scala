package servlets

import java.sql.ResultSet
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}

import org.json4s.jackson.Serialization.{write => writeJson}
import org.json4s.DefaultFormats
import tad.{PTVUser, PTVProduction, ReturnTrait}

@WebServlet(urlPatterns = Array("/display"))
class DisplayServlet extends HttpServlet{

  // json formats
  implicit val formats = DefaultFormats

  private def executeQuery(query: String
                           , getData: (ResultSet) => ReturnTrait) = {
    var data: ReturnTrait = null
    ConnectionPool.getConnection match{
      case Some(connection) =>
        try{
          val rs = connection.createStatement().executeQuery(query)
          if(rs.next())
            data = getData(rs)
        }
        catch {
          case exception: Exception =>
        } finally {
          if (!connection.isClosed) connection.close()
        }
      case None =>
        println("Not getting connection from connection pooling")
    }

    writeJson(data)
  }

  def fetchDB(typeSearch: String, i: Int) = {
    var dbQuery = ""
    typeSearch match {
      case "p" =>
        dbQuery = "Select * FROM production " +
          "WHERE _id = " + i

        executeQuery(dbQuery
        , (rs: ResultSet) => {
            new PTVProduction(rs.getInt(1)
              , rs.getString(2)
              , rs.getInt(3)
              , rs.getString(4)
              , rs.getString(5)
              , rs.getString(6)
              , rs.getString(7)
              , rs.getString(8)
              , rs.getString(9)
              , rs.getString(10)
              , rs.getString(11)
              , rs.getString(12)
              , rs.getString(13)
              , rs.getString(14))
          })
      case "u" =>
        dbQuery = "Select * FROM production " +
          "WHERE _id = " + i
        executeQuery(dbQuery
          , (rs: ResultSet) => {
            new PTVUser(rs.getInt(1)
              , rs.getString(2)
              , rs.getString(3)
              , rs.getString(4)
              , rs.getString(5)
              , rs.getString(6)
              , rs.getInt(7)
              , rs.getString(8))
          })
    }
  }

  override def doGet(req: HSReq, resp: HSResp) = {
    /*
    Parameters:
      t: <p|u> ->type
      id: Int -> id
     */
    var error = false

    // parameter [id] treatment
    var id = 0
    try{
      id = req.getParameter("id").toInt
    } catch {
      case nfe: NumberFormatException =>
        resp.sendError(HSResp.SC_BAD_REQUEST)
        error = true
    }

    // parameter [t] treatment
    var typeSearch = req.getParameter("t")

    if(typeSearch == null && (typeSearch != "p" || typeSearch != "u")){
      resp.sendError(HSResp.SC_BAD_REQUEST)
      error = true
    }

    if(!error){
      val json = fetchDB(typeSearch,id)

      if(json != null){
        resp.setContentType("application/json")
        val writer = resp.getWriter
        writer.print(json)
        writer.close()
      }
    }

  }

}
