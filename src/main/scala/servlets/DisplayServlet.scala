package servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}

import model.{Production, User}
import net.liftweb.common.Full
import net.liftweb.json.Serialization.write

@WebServlet(urlPatterns = Array("/display"))
class DisplayServlet extends HttpServlet{

  // json formats
  implicit val formats = net.liftweb.json.DefaultFormats

  def fetchDB(typeSearch: String, i: Long) = {
    typeSearch match {
      case "p" =>
//        dbQuery = "Select * FROM production " +
//          "WHERE _id = " + i
//
//        executeQuery(dbQuery
//        , (rs: ResultSet) => {
//            new PTVProduction(rs.getInt(1)
//              , rs.getString(2)
//              , rs.getInt(3)
//              , rs.getString(4)
//              , rs.getString(5)
//              , rs.getString(6)
//              , rs.getString(7)
//              , rs.getString(8)
//              , rs.getString(9)
//              , rs.getString(10)
//              , rs.getString(11)
//              , rs.getString(12)
//              , rs.getString(13)
//              , rs.getString(14))
//          })

        Production.findByKey(i) match {
          case Full(prod) =>
            write(prod.asPTVProduction)
          case _ =>
            null
        }

      case "u" =>
//        dbQuery = "Select * FROM user " +
//          "WHERE _id = " + i
//        executeQuery(dbQuery
//          , (rs: ResultSet) => {
//            new PTVUser(rs.getLong(1)
//              , rs.getString(2)
//              , rs.getString(3)
//              , rs.getString(4)
//              , rs.getString(5)
//              , rs.getString(6)
//              , rs.getInt(7)
//              , null)
//          })
        User.findByKey(i) match {
          case Full(user) =>
            write(user.asPTVUser)
          case _ =>
            null
        }
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
    var id: Long = 0
    try {
      id = req.getParameter("id").toLong
    }
    catch {
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
