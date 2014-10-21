package tad

class PTVProduction (  var _id: Int
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
                     , var image: String) extends ReturnTrait{

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
        ,omdbproduction.Poster)
  }

}
