package tad

import model.{Comment, User}
import net.liftweb.mapper.By

class PTVUser ( var _id: Long
              , var name: String
              , var surname: String
              , var birthday: String
              , var address: String
              , var email: String
              , var phone: Int
              , var password: String
              , var comments: List[PTVComment]){

  /**
   * Parses the User, which is a lift's Mapper, into an object that can be parsed
   * into json with its correct format
   * @param user
   */
  def this(user: User) = {
    this(user._id.get
      , user.name.get
      , user.surname.get
      , user.birthday.get
      , user.address.get
      , user.email.get
      , user.phone.get
      , user.password.get
      , Comment.findAll(By(Comment.idUser,user._id.get))
               .map(x => new PTVComment(x)))
  }
}
