package net.mayoct.hcp.littlebits.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.sql.DataSource;

public class CloudbitEventDAO {
	protected DataSource dataSource;

	public CloudbitEventDAO(DataSource newDataSource) throws SQLException {
		setDataSource(newDataSource);
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource newDataSource) throws SQLException {
		this.dataSource = newDataSource;
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement pstmt = connection.prepareCall(
					"set schema NEO_08ZQOKUJHBH5PMI6WAUGN0GDU");
			pstmt.execute();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
		checkTable();
	}

	void checkTable() throws SQLException {
		Connection connection = null;
		try {
			connection = dataSource.getConnection();
			if (!existsTable(connection)) {
				createTable(connection);	// wouldn't be called
			}
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	protected boolean existsTable(Connection conn) throws SQLException {
		String tableName = "EVENTS";
		DatabaseMetaData meta = conn.getMetaData();
		ResultSet rs = meta.getTables(null, null, tableName, null);
		while (rs.next()) {
			String name = rs.getString("TABLE_NAME");
			if (name.equals(tableName)) {
				return true;
			}
		}
		return false;
	}

	void createTable(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(
				"CREATE COLUMN TABLE EVENTS (" +
				"EVENT_ID   varchar(255)," +
				"EVENT_TYPE varchar(255)," +
				"EVENT_TIMESTAMP bigint," +
				"EVENT_USERID integer," +
				"EVENT_BITID  varchar(255)," +
				"PAYLOAD_ABSOLUTE integer," +
				"PAYLOAD_PERCENT integer," +
				"PAYLOAD_LEVEL varchar(255)," +
				"PAYLOAD_DELTA varchar(255)," +
				"PRIMARY KEY (EVENT_ID))");
		pstmt.execute();
		pstmt.close();
	}
	
	public void addRow(CloudbitEvent data) throws SQLException {
		CloudbitEvent eventData = (CloudbitEvent) data;
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement pstmt = connection.prepareCall(
					"INSERT INTO EVENTS " + 
					"(EVENT_ID,EVENT_TYPE,EVENT_TIMESTAMP,EVENT_USERID,EVENT_BITID," +
					"PAYLOAD_ABSOLUTE,PAYLOAD_PERCENT,PAYLOAD_LEVEL,PAYLOAD_DELTA) " +
					"VALUES(?,?,?,?,?,?,?,?,?)");
			pstmt.setString(1, UUID.randomUUID().toString());
			pstmt.setString(2, eventData.getEventType());
			pstmt.setLong(3, eventData.getEventTimestamp());
			pstmt.setInt(4, eventData.getEventUserId());
			pstmt.setString(5, eventData.getEventBitId());
			pstmt.setInt(6,  eventData.getPayloadAbsolute());
			pstmt.setInt(7, eventData.getPayloadPercent());
			pstmt.setString(8,  eventData.getPayloadLevel());
			pstmt.setString(9, eventData.getPayloadDelta());
			pstmt.executeUpdate();
			pstmt.close();
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	public List<CloudbitEvent> selectAllRows() throws SQLException {
		Connection connection = dataSource.getConnection();
		try {
			PreparedStatement pstmt = connection.prepareStatement(
					"SELECT EVENT_ID,EVENT_TYPE,EVENT_TIMESTAMP,EVENT_USERID,EVENT_BITID," +
					"PAYLOAD_ABSOLUTE,PAYLOAD_PERCENT,PAYLOAD_LEVEL,PAYLOAD_DELTA " +
					"FROM EVENTS ORDER BY EVENT_TIMESTAMP DESC");
			ResultSet rs = pstmt.executeQuery();
			ArrayList<CloudbitEvent> list = new ArrayList<CloudbitEvent>();
			int count = 0;
			while (rs.next() && count++ < 100) {
				CloudbitEvent p = new CloudbitEvent();
				p.setEventId(rs.getString(1));
				p.setEventType(rs.getString(2));
				p.setEventTimestamp(rs.getInt(3));
				p.setEventUserId(rs.getInt(4));
				p.setEventBitId(rs.getString(5));
				p.setPayloadAbsolute(rs.getInt(6));
				p.setPayloadPercent(rs.getInt(7));
				p.setPayloadLevel(rs.getString(8));
				p.setPayloadDelta(rs.getString(9));
				list.add(p);
			}
			pstmt.close();
			return list;
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

}
