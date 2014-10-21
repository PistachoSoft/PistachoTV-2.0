package servlets

import com.jolbox.bonecp.BoneCP
import com.jolbox.bonecp.BoneCPConfig
import org.slf4j.LoggerFactory
import java.sql.Connection

/**
 * @author Satendra Kumar
 * http://blog.knoldus.com/2014/03/24/connection-pooling-in-scala/
 * @[2014-10-20]
 */
object ConnectionPool {
  val logger = LoggerFactory.getLogger(this.getClass)

  private val connectionPool = {
    try {
      Class.forName("com.mysql.jdbc.Driver")
      val config = new BoneCPConfig()
      config.setJdbcUrl("jdbc:mysql://localhost:3306/pistachotv")
      config.setUsername("pistachoroot")
      config.setPassword("toor")
      config.setMinConnectionsPerPartition(2)
      config.setMaxConnectionsPerPartition(5)
      config.setPartitionCount(3)
      config.setCloseConnectionWatch(true)// if connection is not closed throw exception
      config.setLogStatementsEnabled(true) // for debugging purpose
      Some(new BoneCP(config))
    } catch {
      case exception: Exception =>;
        logger.warn("Error in creation of connection pool"+exception.printStackTrace())
        None
    }
  }

  def getConnection: Option[Connection] = {
    connectionPool match {
      case Some(connPool) => Some(connPool.getConnection)
      case None => None
    }
  }
}