package models

import play.api.db.slick.Config.driver.simple._
import java.util.{Date => jDate}
import java.sql.Date

case class Comment(_id: Option[Long] = None,
                   idProd: Long,
                   idUser: Long,
                   var title: String,
                   var text: String,
                   creation_date: Option[Date] = Some(new Date(new jDate().getTime)),
                   var modified_date: Option[Date] = Some(new Date(new jDate().getTime)))

case class PostComment(user: String,
                       title: String,
                       text: String,
                       pid: Long)

case class PutComment(id: Long,
                      text: String,
                      title: String)

class Comments(tag: Tag) extends Table[Comment](tag, "COMMENT") {

  def _id = column[Long]("_id", O.PrimaryKey, O.AutoInc)

  def idProd = column[Long]("idProd", O.NotNull)

  def idUser = column[Long]("idUser", O.NotNull)

  def title = column[String]("title", O.NotNull)

  def text = column[String]("text", O.DBType("text"), O.NotNull)

  def creation_date = column[Date]("creation_date", O.NotNull)

  def modified_date = column[Date]("modified_date", O.NotNull)

  def * = (_id.?, idProd, idUser, title, text, creation_date.?, modified_date.?) <>
    (Comment.tupled, Comment.unapply)

  def foreign_prod = foreignKey("FK_Prod", idProd, TableQuery[Productions])(_._id)

  def foreign_user = foreignKey("FK_User", idUser, TableQuery[Users])(_._id)
}

object Comments {
  val comments = TableQuery[Comments]
  val users = TableQuery[Users]
  val productions = TableQuery[Productions]

  def findById(id: Long)(implicit s: Session) = comments.filter(_._id === id).firstOption

  def insert(comment: Comment)(implicit s: Session) = comments insert comment

  def post(comment: PostComment)(implicit s: Session): Option[Comment] = {
    // Verify the user exists
    users.filter(_.email === comment.user).firstOption match {
      case Some(user) =>
        // Verify the production exists
        Productions.findById(comment.pid) match {
          case Some(_) =>
            // Insert new comment
            val dummy = Comment(idProd = comment.pid, idUser = user._id.get,
              title = comment.title, text = comment.text)
            insert(dummy)
            Some(dummy)
          case None =>
            None
        }
      case None =>
        None
    }
  }

  def update(putComment: PutComment)(implicit s: Session) = {
    findById(putComment.id) match {
      case Some(comment) =>
        comment.title = putComment.title
        comment.modified_date = Some(new Date(new jDate().getTime))
        comments.filter(_._id === comment._id).update(comment)
        Some(comment)
      case None =>
        None
    }
  }

  def findComments(id: Long)(implicit s: Session) = {
      (for {
        (comment, _) <- comments leftJoin productions on (_.idProd === _._id)
      } yield comment).list
  }
}