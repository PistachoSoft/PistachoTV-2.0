package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/display" })
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

        String q = request.getParameter("q");
        String t = request.getParameter("t");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><head><title>display</title></head>"
                + "<body><h1>You are searching for:</h1>"
                + "<h2>"+q+" in "+t+"</h2></body></html>");
    }
}