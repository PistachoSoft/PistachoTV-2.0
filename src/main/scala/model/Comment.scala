package model

import net.liftweb.mapper._
import tad.PTVComment

class Comment extends LongKeyedMapper[Comment]{
  def getSingleton = Comment

  def primaryKeyField = _id

  object _id extends MappedLongIndex(this)

  object idProd extends MappedLongForeignKey(this,Production)
  object idUser extends MappedLongForeignKey(this,User)

  object title extends MappedString(this,255)
  object text extends MappedText(this)
  object creation_date extends MappedDateTime(this)
  object modified_date extends MappedDateTime(this)

  /**
   * parse this as a PTVComment
   * @return PTVComment
   */
  def asPTVComment = new PTVComment(this)
}

object Comment extends Comment with LongKeyedMetaMapper[Comment]{

  override def dbTableName = "comment"

  override def fieldOrder = List(_id,idProd,idUser,text,creation_date,modified_date)
}