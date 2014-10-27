package DataBase

import java.sql.{Connection, DriverManager, Statement}
import javax.naming.CommunicationException

import org.json4s.DefaultFormats
import org.json4s.jackson.Serialization.{read => readJson, write => writeJson}
import tad.PTVProduction

import scala.io.Source

object LoadData {

  var st: Statement = null
  var connection: Connection = null
  var user: String = null
  var pass: String = null
  var driver: String = null

  def checkParams(args: Array[String]) = {
    if(args.length != 3){
      System.exit(1)
    }

    driver = "jdbc:mysql://localhost:3306/" + args(0)
    user = args(1)
    pass = args(2)
  }

  def escapeChars(s: String)= {
    s.replace("'","\\'")
  }

  def addProductionDB(production: PTVProduction) = {
    st.executeUpdate("INSERT INTO production" +
      "(title,year,rated,released,runtime,genre,diretor,writer,actors,plot,language,type,image)" +
      "VALUES" +
      "('" + escapeChars(production.title) + "'" +
      ",'" + production.year + "'" +
      ",'" + production.rated.replace("'","\\'") + "'" +
      ",'" + production.released.replace("'","\\'") + "'" +
      ",'" + production.runtime.replace("'","\\'") + "'" +
      ",'" + production.genre.replace("'","\\'") + "'" +
      ",'" + production.director.replace("'","\\'") + "'" +
      ",'" + production.writer.replace("'","\\'") + "'" +
      ",'" + production.actors.replace("'","\\'") + "'" +
      ",'" + production.plot.replace("'","\\'") + "'" +
      ",'" + production.language.replace("'","\\'") + "'" +
      ",'" + production.typeProd.replace("'","\\'") + "'" +
      ",'" + production.image.replace("'","\\'") + "');")
  }

  def loadDataJson() = {
    //json formats
    implicit val formats = DefaultFormats

    val productionList = readJson[List[PTVProduction]](Source.fromURL(getClass.getResource("/rawData.json")).mkString)

    for(prod <- productionList) {
      //println(writeJson(prod))
      addProductionDB(prod)
    }
  }

  def initializeDB() = {
    val script = Source.fromURL(getClass.getResource("/initDB.sql")).mkString
    for(s <- script.split(";")){
      st.executeUpdate(s)
    }
  }

  def main(args: Array[String]) = {
    checkParams(args)

    Class.forName("com.mysql.jdbc.Driver")

    println("driver: " + driver)
    println("user: " +user)
    println("pass: " + pass)
    try{
      connection = DriverManager.getConnection(driver,user,pass)
    }
    catch{
      case ce: CommunicationException =>
        Console.err.println("Database not found")
        System.exit(1)
    }

    st = connection.createStatement()

    if(!st.executeQuery("show tables").next()){
      // initialize database
      println("Initialazing DB...")
      initializeDB()
      println("Done")
    }

    println("Dumping json...")
    loadDataJson()
    println("Done")

  }
}
