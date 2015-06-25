package net.mayoct.hcp.littlebits;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Cloudbit")
public class CloudbitSubscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public CloudbitSubscriptionServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head>");
		writer.println("<title>Cloudbit Sensor Data stored in HCP</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<h1>Cloudbit Sensor Data stored in HCP.</h1>");
		writer.println("</body>");
		writer.println("</html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}
}
