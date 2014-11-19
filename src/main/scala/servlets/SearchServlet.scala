package servlets

import javax.servlet.annotation.WebServlet
import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}

import model.{Production, User}
import net.liftweb.json.Serialization.write
import net.liftweb.mapper._


@WebServlet(urlPatterns = Array("/search"))
class SearchServlet extends HttpServlet{

  // json formats
  implicit val formats = net.liftweb.json.DefaultFormats

  val itemsPerPage = 20

  def getStartAt(page: Int) = (page-1)*itemsPerPage
  /**
   * Based on it's parameters, performs a search in the database return the result as json
   * @param typeSearch type search
   * @param query the query
   * @param page the page number
   * @return
   */
  private def fetchDB(typeSearch: String, query: String, page: Int) = {
    typeSearch match {
      case "p" =>
        val prodList = Production.findAllFields(
          Seq(Production._id,Production.title,Production.year,Production.image)
        , OrderBy(Production.title,Ascending)
        , Like(Production.title,"%" + query + "%")
        , MaxRows(itemsPerPage)
        , StartAt(getStartAt(page)))

        write(prodList.map(x => x.asPTVProductionMin))

      case "u" =>
        val userList = User.findAllFields(
          Seq(User._id, User.name, User.surname, User.email)
          , Like(User.email, "%" + query + "%")
          , OrderBy(User.surname,Ascending)
          , OrderBy(User.name,Ascending)
          , OrderBy(User.email,Ascending)
          , MaxRows(itemsPerPage)
          , StartAt(getStartAt(page))
        )

        write(userList.map(x => x.asPTVUserMin))
    }
  }


  /**
   * Based on it's parameters, performs a search in the database return the result as json
   * @param typeSearch type search
   * @param page the page number
   * @return
   */
  private def fetchDB(typeSearch: String, page: Int): String = {
    typeSearch match {
      case "p" =>
        val prodList = Production.findAllFields(
          Seq(Production._id,Production.title,Production.year,Production.image)
          , OrderBy(Production.title,Ascending)
          , MaxRows(itemsPerPage)
          , StartAt(getStartAt(page)))

        write(prodList.map(x => x.asPTVProductionMin))

      case "u" =>
        val userList = User.findAllFields(
          Seq(User._id, User.name, User.surname, User.email)
          , OrderBy(User.surname,Ascending)
          , OrderBy(User.name,Ascending)
          , OrderBy(User.email,Ascending)
          , MaxRows(itemsPerPage)
          , StartAt(getStartAt(page)))

        write(userList.map(x => x.asPTVUserMin))
    }
  }

  override def doGet(req: HSReq, resp: HSResp) = {
    /*
    Parameters:
      t: <p|u> -> type
      [q: String] -> query to match
      p: Int -> page
     */

    var error = false

    // parameter [p] treatment
    var page = 0
    try{
       page = req.getParameter("p").toInt
    } catch {
      case nfe: NumberFormatException =>
        resp.sendError(HSResp.SC_BAD_REQUEST)
        error = true
    }

    // parameter [t] treatment
    val typeSearch = req.getParameter("t")

    if(typeSearch == null && (typeSearch != "p" || typeSearch != "u")){
      resp.sendError(HSResp.SC_BAD_REQUEST)
      error = true
    }

    if(!error){
      val query = req.getParameter("q")
      var json = ""
      if(query == null){
        json = fetchDB(typeSearch,page)
      }
      else{
        json = fetchDB(typeSearch,query,page)
      }

      if(json != null){
        resp.setContentType("application/json")
        resp.getWriter.print(json)
      }
    }
  }
}
