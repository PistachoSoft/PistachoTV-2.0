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
        Production.findByKey(i) match {
          case Full(prod) =>
            write(prod.asPTVProduction)
          case _ =>
            null
        }

      case "u" =>
        User.findByKey(i) match {
          case Full(user) =>
            write(user.password(null).asPTVUser)
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
