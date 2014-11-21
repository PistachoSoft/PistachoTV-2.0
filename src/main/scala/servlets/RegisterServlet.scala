package servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}

import model.User

@WebServlet(urlPatterns = Array("/register"))
class RegisterServlet extends HttpServlet{

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
    val name = req.getParameter("name")
    val surname = req.getParameter("surname")
    val birthday = req.getParameter("birthday")
    val address = req.getParameter("address")
    val email = req.getParameter("email")
    val password = req.getParameter("password")
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
