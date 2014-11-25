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

  /**
   * Call: /comment/{id} -> deletes the comment which {id} matches
   * @param req
   * @param resp
   */
  override def doDelete(req: HSReq, resp: HSResp) = {
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
      Comment.findByKey(id) match {
        case Full(comment) =>
          if(comment.idUser.get != 1 && ! comment.delete_!) {
            resp.sendError(HSResp.SC_BAD_REQUEST)
          }
        case _ =>
          resp.sendError(HSResp.SC_BAD_REQUEST)
      }
    }
  }

  /**
   * Returns the comment which id is requested as json
   * @param id
   * @return a String if the comment exists otherwise returns null
   */
  def getComment(id: Long) = {
    Comment.findByKey(id) match {
      case Full(comment) =>
        write(comment.asPTVComment)
      case _ => null
    }
  }

  /**
   * Call: /comment/{id} gets the comment which {id} matches
   * @param req
   * @param resp
   */
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

  /**
   * Saves a new comment with all the data from it's parameters
   * @param pid production's id
   * @param userEmail user's email
   * @param title comment's title
   * @param text comment's body
   * @param created comment's creation date
   * @param modified comment's modification date
   * @return the comment as json if created otherwise null
   */
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

  /**
   * Saves a new comment into the DB
   * @param req
   * @param resp
   */
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

  /**
   * Overrides an existing comment
   * @param id comment's id
   * @param text comment's new body
   * @param title comment's new title
   * @return true if everything goes properly otherwise false
   */
  def overrideComment(id: Long, text: String, title: String) = {
    Comment.findByKey(id) match {
      case Full(comment) =>
        comment.text(text)
        comment.title(title)
        comment.modified_date(new Date)
        comment.idUser.get != 1 && comment.save()
      case _ => false
    }
  }

  /**
   * Overrides a comment
   * @param req
   * @param resp
   */
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

    error = req.getPathInfo != null ||
      text == null || title == null || error

    println(error)

    if(error){
      resp.sendError(HSResp.SC_BAD_REQUEST)
    } else{
      if(!overrideComment(id,text,title))
        resp.sendError(HSResp.SC_BAD_REQUEST)
    }
  }
}
