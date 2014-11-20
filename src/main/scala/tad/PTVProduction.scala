package tad

import model.{Comment, Production}
import net.liftweb.mapper.By

class PTVProduction (  var _id: Long
                     , var title: String
                     , var year: Int
                     , var rated: String
                     , var released: String
                     , var runtime: String
                     , var genre: String
                     , var director: String
                     , var writer: String
                     , var actors: String
                     , var plot: String
                     , var language: String
                     , var typeProd: String
                     , var image: String
                     , var comments: List[PTVComment]){


  def this(prod: Production) = {
    this(prod._id.get
    ,prod.title.get
    ,prod.year.get
    ,prod.rated.get
    ,prod.released.get
    ,prod.runtime.get
    ,prod.genre.get
    ,prod.director.get
    ,prod.writer.get
    ,prod.actors.get
    ,prod.plot.get
    ,prod.language.get
    ,prod.typeProd.get
    ,prod.image.get
    ,Comment.findAll(By(Comment.idProd,prod._id.get))
            .map(x => new PTVComment(x)))
  }

  def this(omdbproduction: OMDBProduction) = {
    this(0
        ,omdbproduction.Title
        ,0
        ,omdbproduction.Rated
        ,omdbproduction.Released
        ,omdbproduction.Runtime
        ,omdbproduction.Genre
        ,omdbproduction.Director
        ,omdbproduction.Writer
        ,omdbproduction.Actors
        ,omdbproduction.Plot
        ,omdbproduction.Language
        ,omdbproduction.Type
        ,omdbproduction.Poster
        ,null)
  }

  def getRawProductionMapped = {
    val prod = Production.create

    prod.title(title)
        .year(year)
        .rated(rated)
        .released(released)
        .runtime(runtime)
        .genre(genre)
        .director(director)
        .writer(writer)
        .actors(actors)
        .plot(plot)
        .language(language)
        .typeProd(typeProd)
        .image(image)

    prod
  }

}
