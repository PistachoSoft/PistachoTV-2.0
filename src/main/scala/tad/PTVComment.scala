package tad

import java.util.Date

import model.Comment
import net.liftweb.common.Full

class PTVComment (_id: Long
                   , userId: Long
                   , pId: Long
                   , userMail: String
                   , created_date: Date
                   , modified_date: Date
                   , title: String
                   , text: String){

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
