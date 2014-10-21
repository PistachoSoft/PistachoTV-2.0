package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/searchasdf" })
public class search extends HttpServlet{
    /**
     * @see HttpServlet#HttpServlet()
     */
    public search() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see javax.servlet.http.HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub

        String q = request.getParameter("q");
        String t = request.getParameter("t");

        System.out.println("<html><head><title>search</title></head>"
                + "<body><h1>You are searching for:</h1>"
                + "<h2>"+q+" in "+t+"</h2></body></html>");
/*        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if(t.equals("p")) {
            out.println("[\n" +
                    "\t\t{\n" +
                    "            _id: 1,\n" +
                    "\t\t\ttitle: 'Braveheart',\n" +
                    "\t\t\tyear: 1995,\n" +
                    "\t\t\tthumbnail: './img/braveheart_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 2,\n" +
                    "\t\t\ttitle: 'Brave',\n" +
                    "\t\t\tyear: 2012,\n" +
                    "\t\t\tthumbnail: './img/brave_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 3,\n" +
                    "\t\t\ttitle: 'Como entrenar a tu dragon',\n" +
                    "\t\t\tyear: 2010,\n" +
                    "\t\t\tthumbnail: './img/httyd_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 4,\n" +
                    "\t\t\ttitle: 'Como entrenar a tu dragon 2',\n" +
                    "\t\t\tyear: 2014,\n" +
                    "\t\t\tthumbnail: './img/httyd_2_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 5,\n" +
                    "\t\t\ttitle: 'Paranormal Activity',\n" +
                    "\t\t\tyear: 2007,\n" +
                    "\t\t\tthumbnail: './img/paranormal_activity_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 6,\n" +
                    "\t\t\ttitle: 'Paranormal Activity 2',\n" +
                    "\t\t\tyear: 2010,\n" +
                    "\t\t\tthumbnail: './img/paranormal_activity_2_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 7,\n" +
                    "\t\t\ttitle: 'Paranormal Activity 3',\n" +
                    "\t\t\tyear: 2011,\n" +
                    "\t\t\tthumbnail: './img/paranormal_activity_3_thumb.jpg'\n" +
                    "\t\t},\n" +
                    "\t\t{\n" +
                    "            _id: 8,\n" +
                    "\t\t\ttitle: 'Paranormal Activity 4',\n" +
                    "\t\t\tyear: 2012,\n" +
                    "\t\t\tthumbnail: './img/paranormal_activity_4_thumb.jpg'\n" +
                    "\t\t}\n" +
                    "\t]");
        }else if(t.equals("u")){
            out.println("[\n" +
                    "        {\n" +
                    "            _id: 1,\n" +
                    "        \tname: 'David Recuenco',\n" +
                    "        \temail: 'david.recuencogadea@pistachosoft.com',\n" +
                    "        \tthumbnail: './img/ajax-loader.gif'\n" +
                    "        },\n" +
                    "        {\n" +
                    "            _id: 2,\n" +
                    "        \tname: 'Adrian Reyes',\n" +
                    "        \temail: 'adrian@pistachosoft.com',\n" +
                    "        \tthumbnail: './img/ajax-loader.gif'\n" +
                    "        },\n" +
                    "        {\n" +
                    "            _id: 3,\n" +
                    "        \tname: 'Pistacho Anon',\n" +
                    "        \temail: 'anon@pistachosoft.com',\n" +
                    "        \tthumbnail: './img/ajax-loader.gif'\n" +
                    "        }\n" +
                    "       ]");
        }*/
    }
}