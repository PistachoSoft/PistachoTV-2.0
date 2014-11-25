package tad

import model.User

class PTVUserMin ( var _id: Long
                 , var name: String
                 , var surname: String
                 , var email: String){


  /**
   * Parses the User, which is a lift's Mapper, into an object that can be parsed
   * into json with its correct format
   * @param user
   */
  def this(user: User) = {
    this(user._id.get
      , user.name.get
      , user.surname.get
      , user.email.get)
  }
}
