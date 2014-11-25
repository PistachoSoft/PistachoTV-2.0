package tad

import model.Production

class PTVProductionMin (  var _id: Long
                        , var title: String
                        , var title_tooltip: String
                        , var year: Int
                        , var thumbnail: String){

  def this(prod: PTVProduction) = {
    this(prod._id,prod.title,PTVProductionMin.genToolTip(prod.title),prod.year,prod.image)
  }

  /**
   * Parses the Production, which is a lift's Mapper, into an object that can be parsed
   * into json with its correct format
   * @param prod
   */
  def this(prod: Production) = {
    this(prod._id.get
      ,PTVProductionMin.genToolTip(prod.title.get)
      ,prod.title.get
      ,prod.year.get
      ,prod.image.get)
  }
}

object PTVProductionMin{
  def genToolTip(s: String): String = {
    if(s.length > 20) s.substring(0,20) + "..."
    else s
  }
}
