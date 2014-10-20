package tad

class PTVProductionMin (  var _id: Int
                        , var title: String
                        , var year: Int
                        , var thumbnail: String) extends ReturnTrait{


  def this(prod: PTVProduction) = {
    this(prod._id,prod.title,prod.year,prod.image)
  }
}
