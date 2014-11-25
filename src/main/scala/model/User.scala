package model

import net.liftweb.mapper._
import tad.{PTVUserMin, PTVUser}

class User extends LongKeyedMapper[User]{
  def getSingleton = User

  def primaryKeyField = _id

  object _id extends MappedLongIndex(this)

  object name extends MappedString(this, 255)

  object surname extends MappedString(this, 255)
  object birthday extends MappedString(this,255)
  object address extends MappedString(this, 255)
  object email extends MappedString(this, 255)
  object phone extends MappedInt(this)
  object password extends MappedString(this, 255)

  /**
   * Parse this as a PTVUser
   * @return a PTVUser
   */
  def asPTVUser = new PTVUser(this)

  /**
   * Parse this as a PTVUserMin
   * @return a PTVUserMin
   */
  def asPTVUserMin = new PTVUserMin(this)

}
object User extends User with LongKeyedMetaMapper[User]{

  override def dbTableName = "user"

  override def dbIndexes = UniqueIndex(email) :: super.dbIndexes

  override def fieldOrder = List(_id,name,surname,birthday,address,email,phone,password)
}