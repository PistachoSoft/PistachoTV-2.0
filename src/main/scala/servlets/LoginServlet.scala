package servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}

@WebServlet(urlPatterns = Array("/login"))
class LoginServlet extends HttpServlet{

  def verifyUser(email: String, pass: String) = {
    var resp: String = null
    ConnectionPool.getConnection match {
      case Some(connection) =>
        try{
          val pst = connection.prepareStatement("SELECT password FROM user WHERE mail = ?")
          pst.setString(1,email)
          val rs = pst.executeQuery()
          if(rs.next() && rs.getString(1) == pass)
            resp = email
        }
        catch {
          case exception: Exception =>
        } finally {
          if (!connection.isClosed) connection.close()
        }

        // Return
        resp
      case None =>
        println("Not getting connection from connection pooling")
        null
    }
  }

  override def doGet(req: HSReq, resp: HSResp) = {
    val email = req.getParameter("email")
    val pass = req.getParameter("pass")

    println(email)
    println(pass)

    if(email == null || pass == null){
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else {
      val verifiedEmail = verifyUser(email,pass)
      if(verifiedEmail == null){
        resp.sendError(HSResp.SC_BAD_REQUEST)
      } else {
        resp.setContentType("text/plain")
        val writer = resp.getWriter
        writer.print(verifiedEmail)
        writer.close()
      }
    }
  }

}
