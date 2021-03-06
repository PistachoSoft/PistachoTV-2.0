package tad

import java.util.Date

import model.Comment
import net.liftweb.common.Full

class PTVComment (var _id: Long
                   , var userId: Long
                   , var pId: Long
                   , var userMail: String
                   , var prodTitle: String
                   , var created_date: Date
                   , var modified_date: Date
                   , var title: String
                   , var text: String){

  /**
   * Parses a Comment, which is a lift's Mapper, into an object that can be parsed
   * into json with its correct format
   * @param comment
   */
  def this(comment: Comment) = {
    this(comment._id.get
    , comment.idUser.get
    , comment.idProd.get
    , comment.idUser.foreign match {
        case Full(user) => user.email.get
        case _ => null
      }
    , comment.idProd.foreign match {
        case Full(prod) => prod.title.get
        case _ => null
      }
    , comment.creation_date.get
    , comment.modified_date.get
    , comment.title.get
    , comment.text.get)
  }
}
