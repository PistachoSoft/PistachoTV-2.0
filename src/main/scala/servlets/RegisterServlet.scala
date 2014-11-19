package servlets

import java.sql.SQLException
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServletRequest => HSReq, HttpServletResponse => HSResp, HttpServlet}

import model.User
import org.json4s.jackson.Serialization.{write => writeJson}
import org.json4s.DefaultFormats
import tad.PTVUser

@WebServlet(urlPatterns = Array("/register"))
class RegisterServlet extends HttpServlet{

  // json formats
  implicit val formats = DefaultFormats

  def registerUser(name: String, surname: String, birthday: String, address: String, email: String
                   , password: String, phone: Int) = {
    val user = User.create
    user.name(name)
    user.surname(surname)
    user.birthday(birthday)
    user.address(address)
    user.email(email)
    user.phone(phone)
    user.password(password)

    try{
      if(user.save()){
        email
      }
      else{
        null
      }
    }
    catch{
      case e: Exception =>
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
