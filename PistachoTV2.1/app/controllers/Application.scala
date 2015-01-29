package controllers

import models._

import play.api._
import play.api.db.slick._
import play.api.libs.json.Json
import play.api.mvc._
import scala.Some

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

}