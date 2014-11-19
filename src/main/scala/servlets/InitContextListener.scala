package servlets

import javax.servlet.annotation.WebListener
import javax.servlet.{ServletContextEvent, ServletContextListener}

import DataBase.DataDumper
import bootstrap.liftweb.Boot
import model.Production

@WebListener
class InitContextListener extends ServletContextListener{

  override def contextInitialized(sce: ServletContextEvent): Unit = {
    val boot = new Boot
    boot.boot

    if(Production.count == 0){
      println("No data found, dumping raw data")
      DataDumper.dumpRawData()
    }
  }

  override def contextDestroyed(sce: ServletContextEvent): Unit = {}

}
