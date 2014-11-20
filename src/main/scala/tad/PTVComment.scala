package tad

import java.util.Date

import model.Comment
import net.liftweb.common.Full

class PTVComment (var _id: Long
                   , var userId: Long
                   , var pId: Long
                   , var userMail: String
                   , var created_date: Date
                   , var modified_date: Date
                   , var title: String
                   , var text: String){

  def this(comment: Comment) = {
    this(comment._id.get
    , comment.idUser.get
    , comment.idProd.get
    , comment.idUser.foreign match{
        case Full(user) => user.email.get
        case _ => null
      }
    , comment.creation_date.get
    , comment.modified_date.get
    , comment.title.get
    , comment.text.get)
  }
}
