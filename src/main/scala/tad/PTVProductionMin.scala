package tad

import model.Production

class PTVProductionMin (  var _id: Long
                        , var title: String
                        , var year: Int
                        , var thumbnail: String){


  def this(prod: PTVProduction) = {
    this(prod._id,prod.title,prod.year,prod.image)
  }

  def this(prod: Production) = {
    this(prod._id.get
      ,prod.title.get
      ,prod.year.get
      ,prod.image.get)
  }
}
