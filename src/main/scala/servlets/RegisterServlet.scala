package servlets

import java.sql.SQLException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}

import org.json4s.jackson.Serialization.{write => writeJson}
import org.json4s.DefaultFormats
import tad.PTVUser

@WebServlet(urlPatterns = Array("/register"))
class RegisterServlet extends HttpServlet{

  // json formats
  implicit val formats = DefaultFormats

  def registerUser(name: String, surname: String, birthday: String, address: String, email: String
                   , password: String, phone: Int): String = {
    ConnectionPool.getConnection match {
      case Some(connection) =>
        try{
          val st = connection.prepareStatement("INSERT INTO user " +
            "(name,surname,birthday,address,mail,phone,password) " +
            " VALUES(?,?,?,?,?,?,?)")
          st.setString(1,name)
          st.setString(2,surname)
          st.setString(3,birthday)
          st.setString(4,address)
          st.setString(5,email)
          st.setInt(6,phone)
          st.setString(7,password)
          val rst = st.executeUpdate()
          if(rst > 0){
            email
          } else{
            null
          }
        } catch {
          case se: SQLException =>
            se.printStackTrace()
            null
        }
      case None =>
        println("Not getting connection from connection pooling")
        null
    }
  }

  override def doPost(req: HSReq, resp: HSResp) = {
    var error = false
    var name = req.getParameter("name")
    var surname = req.getParameter("surname")
    var birthday = req.getParameter("birthday")
    var address = req.getParameter("address")
    var email = req.getParameter("email")
    var password = req.getParameter("password")
    var phone = 0

    error = name == null || surname == null || birthday == null || address == null ||
      email == null || password == null

    try {
      phone = req.getParameter("phone").toInt
    } catch {
      case nfe: NumberFormatException =>
        error = true
    }

    if(error){
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else{
      val user = registerUser(name,surname,birthday,address,email,password,phone)
      if(user != null){
        resp.setContentType("text/plain")
        val writer = resp.getWriter
        writer.print(user)
        writer.close()
      } else {
        resp.sendError(HSResp.SC_BAD_REQUEST)
      }
    }
  }

}
