package servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}

import model.User
import net.liftweb.common.Full
import net.liftweb.mapper.Like

@WebServlet(urlPatterns = Array("/login"))
class LoginServlet extends HttpServlet{

  def verifyUser(email: String, pass: String) = {
    User.find(Like(User.email,email), Like(User.password,pass)) match {
      case Full(user) =>
        email
      case _ =>
        null
    }
  }

  override def doGet(req: HSReq, resp: HSResp) = {
    val email = req.getParameter("email")
    val pass = req.getParameter("pass")

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
