package servlets

import javax.servlet.annotation.WebListener
import javax.servlet.{ServletContextEvent, ServletContextListener}

import DataBase.DataDumper
import bootstrap.liftweb.Boot
import model.Production

/**
 * Context Initializer, loads lift's boot when the app is deployed if the model
 * is empty it's loaded with resources/rawData.json saved by FetchData.scala
 */
@WebListener
class InitContextListener extends ServletContextListener{

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    val boot = new Boot
    boot.boot()

    if(Production.count == 0){
      println("No data found, dumping raw data")
      DataDumper.dumpRawData()

      DataDumper.generateLuceneIndex()
    }
  }

  override def contextDestroyed(sce: ServletContextEvent): Unit = {}

}
