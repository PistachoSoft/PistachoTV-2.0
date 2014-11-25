package DataBase

import java.io._
import java.sql.{DriverManager, Statement}
import java.util.concurrent.TimeoutException

import akka.actor.{Props, ActorSystem, Actor}
import org.apache.commons.httpclient.util.URIUtil
import org.json4s.DefaultFormats
import tad.{PTVProduction, OMDBProduction}
import org.json4s.jackson.Serialization.{read => readJson, write => writeJson}
import org.json4s.MappingException

import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.io.Source

case object TaskDone

/**
 * Single actor that looks for the data into OMDB API
 */
class FetchDataActor extends Actor {

  def receive = {
    case (title: String, year: Int) =>
      val production = FetchData.getProductionOMDB(title)
      if(production != null){
        production.year = year
        sender ! production
      }

      sender ! TaskDone
  }

}

/**
 * Controls the actor system
 * @param toDo the number of actors the system will have
 */
class FetchDataController(var toDo: Int) extends Actor{

  def receive = {
    case TaskDone =>
      toDo -= 1
      if(toDo == 0) context.system.shutdown()
    case production: PTVProduction =>
      FetchData.productionList = FetchData.productionList.+:(production)
  }
}

/**
 * Main object
 */
object FetchData {

  //json formats
  implicit val formats = DefaultFormats

  var productionList = List[PTVProduction]()

  val user_hendrix = "a642535"
  val pass_hendrix = "path1837"
  val driver_imdb = "jdbc:mysql://hendrix-mysql.cps.unizar.es:3306/miniIMDB"
  var st_hendrix: Statement = null

  var skippedCount = 0
  var functionCount = 0
  val filename = "rawData.json"

  def getProductionOMDB(title: String) = {
    functionCount += 1

    val url = "http://www.omdbapi.com/?i=&plot=full&t=" + title

    // Fetch data
    val response = Source.fromURL(URIUtil.encodeQuery(url),"UTF-8").mkString

    try{
      // Get json from response
      val omdbProduction = readJson[OMDBProduction](response)

      // Parse to custom production
      val production = new PTVProduction(omdbProduction)

      production
    }
    catch{
      case mexception: MappingException =>
        if(mexception.getMessage.contains("No usable value for Title")){
          skippedCount += 1
          println("("+ functionCount + ") Skipped: " + title)
        }
        else{
          mexception.printStackTrace()
        }
        null
    }

  }


  /**
   * main method
   * @param args arguments
   */
  def main(args: Array[String]) = {
    val system = ActorSystem("FetchSystem")

    Class.forName("com.mysql.jdbc.Driver")

    val conn = DriverManager.getConnection(driver_imdb, user_hendrix, pass_hendrix)
    st_hendrix = conn.createStatement()

    // get titles movies & year
    val rs = st_hendrix.executeQuery("SELECT title, production_year " +
      "FROM title ORDER BY title DESC")

    // get result set size
    rs.last()

    val rows = rs.getRow

    rs.beforeFirst()

    val actorsController = system.actorOf(Props(new FetchDataController(rows)))

    // Looping through the set
    println("Loading data ...")
    while (rs.next) {
      val fetchActor = system.actorOf(Props[FetchDataActor])
      fetchActor.tell((rs.getString(1),rs.getInt(2)), actorsController)
    }
    
    // Wait for the actors to finish their task
    system.awaitTermination()

    println("Done | Total skipped: " + skippedCount)

    println("Saving data ...")
    try{
      new FileReader(filename)
    }
    catch{
      case fnfe: FileNotFoundException =>
        println(filename + ": File not found.  Creating a new file.")
    }

    val output = new FileWriter(filename)
        output.write(writeJson(productionList))
        output.close()
    println("Done")
  }
}
