package models

import models.Data._
import play.api.db.slick.DBSessionRequest
import play.api.libs.json.Json

import play.api.db.slick.Config.driver.simple._

case class Production(_id: Option[Long] = None,
                      title: String,
                      year: Int,
                      rated: String,
                      released: String,
                      runtime: String,
                      genre: String,
                      director: String,
                      writer: String,
                      actors: String,
                      plot: String,
                      language: String,
                      typeProd: String,
                      image: String)

case class ProductionMin(_id: Option[Long] = None,
                         title: String,
                         title_tooltip: String,
                         year: Int,
                         thumbnail: String){
  def this(prod: Production) ={
    this(prod._id,
      if(prod.title.size > 20) prod.title.substring(0,20) + "..."
      else prod.title,
      prod.title,
      prod.year,
      prod.image)
  }
}

case class DumpProduction(dump: List[Production])

class Productions(tag: Tag) extends Table[Production](tag, "PRODUCTION") {

  def _id = column[Long]("_id", O.PrimaryKey, O.AutoInc)
  def title = column[String]("title", O.NotNull)
  def year = column[Int]("year", O.NotNull)
  def rated = column[String]("rated", O.NotNull)
  def released = column[String]("released", O.NotNull)
  def runtime = column[String]("runtime", O.NotNull)
  def genre = column[String]("genre", O.NotNull)
  def director = column[String]("director", O.NotNull)
  def writer = column[String]("writer", O.DBType("text"), O.NotNull)
  def actors = column[String]("actors", O.NotNull)
  def plot = column[String]("plot", O.DBType("text"), O.NotNull)
  def language = column[String]("language", O.NotNull)
  def typeProd = column[String]("typeProd", O.NotNull)
  def image = column[String]("image", O.NotNull)

  def * = (_id.?, title, year, rated, released, runtime, genre,
    director, writer, actors, plot, language, typeProd, image) <>
    (Production.tupled, Production.unapply)
}

/**
 * Wraps its TableQuery usage
 */
object Productions{
  val productions = TableQuery[Productions]

  /**
   * count all productions
   */
  def count(implicit s: Session) = Query(productions.length).first

  /**
   * Insert a production
   */
  def insert(prod: Production)(implicit s: Session) = productions insert prod

  def findAll(implicit s: Session) = productions.list

  def list(page: Int = 0, pageSize: Int = defaultPageSize, filter: String = "%")(implicit s: Session) ={
    val offset = pageSize * page
    val query =
      (for {
        user <- productions
        if user.title.toLowerCase like filter.toLowerCase
      }yield user).sortBy(_.title.asc).drop(offset).take(pageSize)

    query.list.map(new ProductionMin(_))
  }

  def findById(id: Long)(implicit s: Session) = productions.filter(_._id === id).firstOption
}