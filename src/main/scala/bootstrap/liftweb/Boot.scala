package bootstrap.liftweb

import net.liftweb.mapper._
import java.sql.{Connection, DriverManager}
import net.liftweb.common.{Box, Empty, Full}
import net.liftweb.util.Props
import model._

class Boot {

  def boot(): Unit = {

    DB.defineConnectionManager(DefaultConnectionIdentifier, myDBVendor)

    MapperRules.createForeignKeys_? = _ => true
    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User, Production, Comment)

  }

}

object myDBVendor extends ConnectionManager {
  private var pool: List[Connection] = Nil
  private var poolSize = 0
  private val maxPoolSize = 4

  private lazy val chooseDriver = Props.mode match {
    case _ => "com.mysql.jdbc.Driver"
  }

  private lazy val chooseURL = Props.mode match {
    case _ => "jdbc:mysql://localhost:3306/testlift"
  }

  private def createOne: Box[Connection] = try {
    val driverName: String = Props.get("db.driver") openOr chooseDriver
    val dbUrl: String = Props.get("db.url") openOr chooseURL

    Class.forName(driverName)

//    val dm = DriverManager.getConnection(dbUrl,"pistachoroot","toor")
    val dm = (Props.get("db.user"), Props.get("db.pass")) match {
      case (Full(user), Full(pwd)) =>
        DriverManager.getConnection(dbUrl, user, pwd)

      case _ => DriverManager.getConnection(dbUrl)
    }

    Full(dm)
  } catch {
    case e: Exception => e.printStackTrace(); Empty
  }

  def newConnection(name: ConnectionIdentifier): Box[Connection] =
    synchronized {
      pool match {
        case Nil if poolSize < maxPoolSize =>
          val ret = createOne
          poolSize = poolSize + 1
          ret.foreach(c => pool = c :: pool)
          ret

        case Nil => wait(1000L); newConnection(name)
        case x :: xs => try {
          x.setAutoCommit(false)
          Full(x)
        } catch {
          case e: Throwable => try {
            pool = xs
            poolSize = poolSize - 1
            x.close()
            newConnection(name)
          } catch {
            case e: Throwable => newConnection(name)
          }
        }
      }
    }

  def releaseConnection(conn: Connection): Unit = synchronized {
    pool = conn :: pool
    notify()
  }

}