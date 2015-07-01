package net.mayoct.hcp.littlebits;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.mayoct.hcp.littlebits.persistence.Cloudbit;
import net.mayoct.hcp.littlebits.persistence.CloudbitEvent;
import net.mayoct.hcp.littlebits.persistence.CloudbitEventDAO;

import org.json.JSONObject;

import com.sap.security.core.server.csi.IXSSEncoder;
import com.sap.security.core.server.csi.XSSEncoder;

@WebServlet("/Cloudbit")
public class CloudbitSubscriptionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CloudbitEventDAO cloudbitEventDAO;
      
    public CloudbitSubscriptionServlet() {
        super();
    }

	public void init() throws ServletException {
		super.init();
		try {
			InitialContext ctx = new InitialContext();
			DataSource dataSource = (DataSource) ctx
					.lookup("java:comp/env/jdbc/DefaultDB");
			cloudbitEventDAO = new CloudbitEventDAO(dataSource);
		} catch (SQLException e) {
			throw new ServletException(e);
		} catch (NamingException e) {
			throw new ServletException(e);
		}
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.println("<html><head>");
		writer.println("<title>Cloudbit Sensor Data stored in HCP</title>");
		writer.println("</head>");
		writer.println("<body>");
		writer.println("<h1>Cloudbit Sensor Data stored in HCP.</h1>");

		try {
			appendTableData(request, response);
		} catch (Exception e) {
			writer.println(
					"Database operation(GET) failed with reason: " +
							e.getMessage());
		}
		
		writer.println("</body>");
		writer.println("</html>");
	}

	void appendTableData(HttpServletRequest request, HttpServletResponse response) 
			throws SQLException, IOException {
		PrintWriter writer = response.getWriter();
		
		List<CloudbitEvent> resultList = cloudbitEventDAO.selectAllRows();
		writer.println(
				"<table><tbody><tr><th colspan=\"6\">" +
				(resultList.isEmpty() ? "No" : resultList.size()) +
				" Record(s) in HCP</th></tr>");
		writer.println("<tr><th>Timestamp</th>" +
				"<th>Label</th><th>Absolute</th><th>Percent</th><th>Level</th><th>Delta</th></tr>");
		IXSSEncoder xssEncoder = XSSEncoder.getInstance();
		for (CloudbitEvent p : resultList) {
			Date date = new Date(p.getEventTimestamp());
			String eventBitId = p.getEventBitId();
			// Timestamp
			writer.println("<tr><td>" +
					xssEncoder.encodeHTML(date.toString()) +
					"</td>");
			// Label
			String label = "(" + eventBitId + ")";
			writer.println("<td>" + xssEncoder.encodeHTML(label) + "</td>");
			// Absolute
			writer.println("<td>" +
					p.getPayloadAbsolute() +
					"</td>");
			// Percent
			writer.println("<td>" +
					p.getPayloadPercent() +
					"</td>");
			// Level
			writer.println("<td>" + 
					xssEncoder.encodeHTML(p.getPayloadLevel()) +
					"</td>");
			// Delta
			writer.println("<td>" + 
					xssEncoder.encodeHTML(p.getPayloadDelta()) + 
					"</td></tr>");
		}
		writer.println("</tbody></table>");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			doAdd(request);
		} catch (Exception e) {
			response.getWriter().println(
					"Database operation(POST) failed with reason: " +
					e.getMessage());
		}
	}

	private static final String JSON_TYPE = "type";
	private static final String JSON_TIMESTAMP = "timestamp";
	private static final String JSON_USERID = "user_id";
	private static final String JSON_BITID = "bit_id";
	private static final String JSON_PAYLOAD = "payload";
	private static final String JSON_ABSOLUTE = "absolute";
	private static final String JSON_PERCENT = "percent";
	private static final String JSON_LEVEL = "level";
	private static final String JSON_DELTA = "delta";
	
	private void doAdd(HttpServletRequest request) throws ServletException, IOException, SQLException {
		BufferedReader reader = new BufferedReader(request.getReader());
		StringBuffer body = new StringBuffer();
		String line;
		
		while ((line = reader.readLine()) != null) {
			body.append(line);
		}
		
		CloudbitEvent eventData = new CloudbitEvent();
		try {
			JSONObject json = new JSONObject(body.toString());
			if (json.has(JSON_TYPE)) {
				eventData.setEventType(json.getString(JSON_TYPE));
			}
			if (json.has(JSON_TIMESTAMP)) {
				eventData.setEventTimestamp(json.getInt(JSON_TIMESTAMP));
			}
			if (json.has(JSON_USERID)) {
				eventData.setEventUserId(json.getInt(JSON_USERID));
			}
			if (json.has(JSON_BITID)) {
				eventData.setEventBitId(json.getString(JSON_BITID));
			}
			if (json.has(JSON_PAYLOAD)) {
				JSONObject json2 = json.getJSONObject(JSON_PAYLOAD);
				if (json2.has(JSON_ABSOLUTE)) {
					eventData.setPayloadAbsolute(json2.getInt(JSON_ABSOLUTE));
				}
				if (json2.has(JSON_PERCENT)) {
					eventData.setPayloadPercent(json2.getInt(JSON_PERCENT));
				}
				if (json2.has(JSON_LEVEL)) {
					eventData.setPayloadLevel(json2.getString(JSON_LEVEL));
				}
				if (json2.has(JSON_DELTA)) {
					eventData.setPayloadDelta(json2.getString(JSON_DELTA));
				}
			}
		} catch (Exception e) {
			new IOException("JSON parse exception: " + e.getMessage());
		}
		cloudbitEventDAO.addRow(eventData);
	}
	
}
