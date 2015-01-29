package controllers

import scala.Some
import models.{Login, User, UserMin, Users}
import play.api.libs.json.Json
import play.api.mvc.Controller
import play.api.db.slick._

object UserCont extends Controller {

  implicit val userFormat = Json.format[User]
  implicit val userMinFormat = Json.format[UserMin]
  implicit val loginFormat = Json.format[Login]

  def findUser(id: Long) = DBAction { implicit rs =>
    Users.findById(id) match {
      case Some(user) =>
        user.password = None
        Ok(Json.toJson(user))
      case None =>
        NotFound("Resource not found")
    }
  }

  def listUsers(page: Int) = DBAction { implicit rs =>
    Ok(Json.toJson(Users.list(page = page)))
  }

  def listUsersF(page: Int, filter: String) = DBAction { implicit rs =>
    Ok(Json.toJson(Users.list(page = page, filter = s"%$filter%")))
  }

  def login = DBAction(parse.json) { implicit rs =>
    rs.body.validate[Login].map {
      login =>
        if(Users.login(login))
          Ok("Some authentication token")
        else
          BadRequest("authentication failed")
    }.getOrElse(BadRequest("wrong body format"))
  }

  def register = DBAction(parse.json) { implicit rs =>
    rs.body.validate[User].map {
      user =>
        Users.insert(user)
        Users.findByEmail(user.email) match {
          case Some(u) =>
            u.password = None
            Ok(Json.toJson(u))
        }
    }.getOrElse(BadRequest("registration failed"))
  }
}
