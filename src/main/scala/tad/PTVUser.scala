package tad

import java.util.Date

import model.User

class PTVUser ( var _id: Long
              , var name: String
              , var surname: String
              , var birthday: String
              , var address: String
              , var email: String
              , var phone: Int
              , var password: String){

  def this(user: User) = {
    this(user._id.get
      , user.name.get
      , user.surname.get
      , user.birthday.get
      , user.address.get
      , user.email.get
      , user.phone.get
      , user.password.get)
  }
}
