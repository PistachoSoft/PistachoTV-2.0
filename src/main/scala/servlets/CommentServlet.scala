package servlets

import java.util.Date
import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}

import model.{User, Comment}
import net.liftweb.common.Full
import net.liftweb.mapper.Like
import net.liftweb.json.Serialization.write

@WebServlet(urlPatterns = Array("/comment/*"))
class CommentServlet extends HttpServlet{

  // json formats
  implicit val formats = net.liftweb.json.DefaultFormats

  override def doDelete(req: HSReq, resp: HSResp) = {
    var id: Long = 0
    var error = false

    try{
      id = req.getPathInfo.substring(1).toLong
    } catch {
      case e: Exception => error = true;
    }

    error |= id == 1

    if(error) {
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else {
      Comment.findByKey(id) match {
        case Full(comment) =>
          if(! comment.delete_!) {
            resp.sendError(HSResp.SC_BAD_REQUEST)
          }
        case _ =>
          resp.sendError(HSResp.SC_BAD_REQUEST)
      }
    }
  }

  def getComment(id: Long) = {
    Comment.findByKey(id) match {
      case Full(comment) =>
        write(comment.asPTVComment)
      case _ => null
    }
  }

  override def doGet(req: HSReq, resp: HSResp) = {
    var id: Long = 0
    var error = false

    try{
      id = req.getPathInfo.substring(1).toLong
    } catch {
      case e: Exception => error = true;
    }

    if(error) {
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else {
      val commentR = getComment(id)
      if(commentR == null) {
        resp.sendError(HSResp.SC_BAD_REQUEST)
      } else {
        resp.setContentType("application/json")
        val writer = resp.getWriter
        writer.print(commentR)
        writer.close()
      }
    }
  }

  def postComment(pid: Long, userEmail: String, title: String
                  , text: String, created: Date, modified: Date) = {
    val comment = Comment.create
    comment.idProd(pid)
    comment.title(title)
    comment.text(text)
    comment.creation_date(created)
    comment.modified_date(modified)
    User.find(Like(User.email,userEmail)) match {
      case Full(user) =>
        comment.idUser(user._id.get)
        if(comment.save()){
		  write(comment.asPTVComment)
        } else { null }
      case _ =>
        null
    }
  }

  override def doPost(req: HSReq, resp: HSResp) = {
    var pid: Long = 0
    val user = req.getParameter("user")
    val title = req.getParameter("title")
    val text = req.getParameter("text")
    val created = new Date()
    val modified = created
    var error = false

    try {
      pid = req.getParameter("pid").toLong
    } catch {
      case e: Exception => error = true
    }


    error = req.getPathInfo != null ||
      user == null ||  text == null || title == null || error

    if(error){
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else {
      val userR = postComment(pid,user,title,text,created,modified)
      if(userR == null){
        resp.sendError(HSResp.SC_BAD_REQUEST)
      } else {
        resp.setContentType("application/json")
        val writer = resp.getWriter
		writer.println(userR)
        writer.close()
      }
    }
  }

  def overrideComment(id: Long, text: String, title: String) = {
    Comment.findByKey(id) match {
      case Full(comment) =>
        comment.text(text)
        comment.title(title)
        comment.modified_date(new Date)
        comment.save()
      case _ =>
    }
  }

  override def doPut(req: HSReq, resp:HSResp) = {
    var id: Long = 0
    val text = req.getParameter("text")
    val title = req.getParameter("title")
    var error = false

    try{
      id = req.getParameter("id").toLong
    } catch {
      case e: Exception => error = true
    }

    error = req.getPathInfo != null || id == 1 ||
      text == null || title == null || error

    if(error){
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else{
      overrideComment(id,text,title)
    }
  }
}
