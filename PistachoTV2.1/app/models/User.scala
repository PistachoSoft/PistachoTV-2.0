package models

import Data._
import play.api.db.slick.Config.driver.simple._

case class User(_id: Option[Long] = None,
                name: String,
                surname: String,
                birthday: String,
                address: String,
                email: String,
                phone: Int,
                var password: Option[String])

case class UserMin(_id: Option[Long] = None,
                   name: String,
                   surname: String,
                   email: String){
  def this(user: User) = {
    this(user._id,user.name,user.surname,user.email)
  }
}

case class Login(email: String, password: String)

class Users(tag: Tag) extends Table[User](tag, "USER") {

  def _id = column[Long]("_id", O.PrimaryKey, O.AutoInc)
  def name = column[String]("name", O.NotNull)
  def surname = column[String]("surname", O.NotNull)
  def birthday = column[String]("birthday", O.NotNull)
  def address = column[String]("address", O.NotNull)
  def email = column[String]("email", O.NotNull)
  def phone = column[Int]("phone", O.NotNull)
  def password = column[String]("password", O.NotNull)

  def * = (_id.?, name, surname, birthday, address, email, phone, password.?) <>
    (User.tupled, User.unapply)

  def unique_email = index("U_email", email, unique = true)
}

/**
 * wraps its TableQuery usage
 */
object Users{

  val users = TableQuery[Users]

  /**
   * Insert a new user
   * @return
   */
  def insert(user: User)(implicit s: Session) = users insert user

  def list(page: Int = 0, pageSize: Int = defaultPageSize, filter: String = "%")(implicit s: Session) ={
    val offset = pageSize * page
    val query =
      (for {
        user <- users
        if user.email.toLowerCase like filter.toLowerCase
        if user._id =!= 1L
      }yield user).sortBy(_.email.asc).drop(offset).take(pageSize)

    query.list.map(new UserMin(_))
  }

  def findById(id: Long)(implicit s: Session) = {
    if(id == 1) None
    else users.filter(_._id === id).firstOption
  }

  def findByEmail(email: String)(implicit s: Session) = users.filter(_.email === email).firstOption

  def login(log: Login)(implicit s: Session) = {
    findByEmail(log.email) match {
      case Some(user) =>
        user.password == log.password
      case None =>
        false
    }
  }
}