package model

import net.liftweb.mapper._
import tad.{PTVProductionMin, PTVProduction}

class Production extends LongKeyedMapper[Production]{
  def getSingleton = Production

  def primaryKeyField = _id

  object _id extends MappedLongIndex(this)

  object title extends MappedString(this, 255)
  object year extends MappedInt(this)
  object rated extends MappedString(this,255)
  object released extends MappedString(this, 255)
  object runtime extends MappedString(this, 255)
  object genre extends MappedString(this, 255)
  object director extends MappedString(this, 255)
  object writer extends MappedText(this)
  object actors extends MappedString(this, 255)
  object plot extends MappedText(this)
  object language extends MappedString(this, 255)
  object typeProd extends MappedString(this, 255)
  object image extends MappedString(this, 255)

  def asPTVProduction = new PTVProduction(this)

  def asPTVProductionMin = new PTVProductionMin(this)
}

object Production extends Production with LongKeyedMetaMapper[Production]{

  override def dbTableName = "production"

  override def fieldOrder = List(_id,title,year,rated,released,runtime,genre
    ,director,writer,actors,plot,language,typeProd,image)
}
