package tad

import model.User

class PTVUserMin ( var _id: Long
                 , var name: String
                 , var surname: String
                 , var email: String){


  def this(user: User) = {
    this(user._id.get
      , user.name.get
      , user.surname.get
      , user.email.get)
  }
}
