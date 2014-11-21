package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/displayasdf" })
public class display extends HttpServlet{
    /**
     * @see HttpServlet#HttpServlet()
     */
    public display() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        String id = request.getParameter("id");
        String t = request.getParameter("t");

/*        System.out.println("<html><head><title>display</title></head>"
                + "<body><h1>You are searching for:</h1>"
                + "<h2>"+id+" in "+t+"</h2></body></html>");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if(t.equals("p")) {
            out.println("{\n" +
                    "            _id: 1,\n" +
                    "\t\t\ttitle: 'Braveheart',\n" +
                    "\t\t\tyear: 1995,\n" +
                    "\t\t\trated: 'R',\n" +
                    "\t\t\treleased: '24 May 1995',\n" +
                    "\t\t\truntime: '177 min',\n" +
                    "\t\t\tgenre: 'Action, Biography, Drama',\n" +
                    "\t\t\tdirector: 'Mel Gibson',\n" +
                    "\t\t\twriter: 'Randall Wallace',\n" +
                    "\t\t\tactors: 'James Robinson, Sean Lawlor, Sandy Nelson, James Cosmo',\n" +
                    "\t\t\tplot: 'When his secret bride is executed for assaulting an English soldier who tried to rape her, a commoner begins a revolt and leads Scottish warriors against the cruel English tyrant who rules Scotland with an iron fist.',\n" +
                    "\t\t\tlanguage: 'English, French, Latin, Scottish Gaelic',\n" +
                    "\t\t\ttype: 'movie',\n" +
                    "\t\t\tposter: './img/braveheart_thumb.jpg',\n" +
                    "            comments: [\n" +
                    "                {\n" +
                    "                    user: 'David Recuenco',\n" +
                    "                    date: '06/08/2000',\n" +
                    "                    text: 'Esta película es la puta hostia, cómo mola!'\n" +
                    "                },\n" +
                    "                {\n" +
                    "                    user: 'Rammus',\n" +
                    "                    date: '19/10/2014',\n" +
                    "                    text: 'Ok'\n" +
                    "                }\n" +
                    "            ]\n" +
                    "\t\t}");
        }else if(t.equals("u")){
            out.println("{\n" +
                    "        _id: 1,\n" +
                    "        name: 'David',\n" +
                    "        surname: 'Recuenco',\n" +
                    "        email: 'david.recuencogadea@pistachosoft.com',\n" +
                    "        avatar: '',\n" +
                    "        year: '06/08/1993',\n" +
                    "        address: 'Void',\n" +
                    "        phone: '555-666-777',\n" +
                    "        comments: [\n" +
                    "            {\n" +
                    "                production: 'Braveheart',\n" +
                    "                date: '06/08/2000',\n" +
                    "                text: 'Esta película es la puta hostia, cómo mola!'\n" +
                    "            },\n" +
                    "            {\n" +
                    "                production: 'Brave',\n" +
                    "                date: '06/08/2011',\n" +
                    "                text: 'OP'\n" +
                    "            }\n" +
                    "        ]\n" +
                    "\n" +
                    "    }");
        }*/
    }
}