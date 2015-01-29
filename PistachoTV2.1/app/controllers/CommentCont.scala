package controllers

import scala.Some
import models.{PutComment, PostComment, Comments, Comment}
import play.api.libs.json.Json
import play.api.mvc.Controller
import play.api.db.slick._

object CommentCont extends Controller {

  implicit val commentFormat = Json.format[Comment]
  implicit val postCommentFormat = Json.format[PostComment]
  implicit val putCommentFormat = Json.format[PutComment]

  def findComment(id: Long) = DBAction { implicit rs =>
    Comments.findById(id) match {
      case Some(comment) =>
        Ok(Json.toJson(comment))
      case None =>
        NotFound("Resource not found")
    }
  }

  def findComments(id: Long) = DBAction { implicit rs =>
    Ok(Json.toJson(Comments.findComments(id)))
  }

  def newComment = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[PostComment].map {
      comment =>
        Comments.post(comment) match {
          case Some(posted) =>
            Ok(Json.toJson(posted))
          case None =>
            BadRequest("user or production invalid")
        }
    }.getOrElse(BadRequest("Invalid comment format"))
  }

  def edit = DBAction(parse.json) { implicit rs =>
    rs.request.body.validate[PutComment].map {
      comment =>
        Comments.update(comment) match {
          case Some(updated) =>
            Ok(Json.toJson(updated))
          case None =>
            NotFound("not found")
        }
    }.getOrElse(BadRequest("Invalid comment format"))
  }
}
