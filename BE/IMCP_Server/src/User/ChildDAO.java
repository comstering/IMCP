package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DBConnect.DBConnector;

public class ChildDAO {
	private DBConnector dbConnector;
	private Connection conn;
	
	private ResultSet rs;
	
	public ChildDAO() {
		dbConnector = new DBConnector();
	}
	
	public ArrayList<ChildDTO> getChildList(String parentID) {
		ArrayList<ChildDTO> list = new ArrayList<ChildDTO>();
		String sql = "select CHILDKEY, NAME, IMG_REALFILE from CHILD_INFO where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parentID);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ChildDTO childDTO = new ChildDTO(rs.getString(1), rs.getString(2), rs.getString(3));
				list.add(childDTO);
			}
		} catch (SQLException e) {
			System.err.println("ChildDAO getChildList SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO getChildList close SQLException error");
			}
		}
		return list;
	}
	
	public int getAge(String date) {
		String sql = "select truncate(datediff((select curdate()), ?) /365, 0)";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, date);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			System.err.println("ChildDAO getAge SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO getAge close SQLException error");
			}
		}
		return -1;
	}
	
	public ChildDTO getChildInfo(String childKey) {
		ChildDTO childDTO = null;
		String sql = "select NAME, BIRTH, IMG_REALFILE from CHILD_INFO where CHILDKEY = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				childDTO = new ChildDTO(childKey, rs.getString(1), rs.getString(2), rs.getString(3));
			}
		} catch (SQLException e) {
			System.err.println("ChildDAO getChildInfo SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO getChildInfo close SQLException error");
			}
		}
		return childDTO;
	}
	
	public double[] getChildGPS(String childKey) {
		double[] gps = new double[2];
		String sql = "select LATITUDE, LONGITUDE from CHILD_GPS_DATA where CHILDKEY = ? order by DATETIME desc limit 1";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				gps[0] = rs.getDouble(1);
				gps[1] = rs.getDouble(2);
			}
		} catch (SQLException e) {
			System.err.println("ChildDAO getChildInfo SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO getChildGPS close SQLException error");
			}
		}
		return gps;
	}
	
	public String getTime() {
		String sql = "select now()";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			System.err.println("PostDAO getDate SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO getTime close SQLException error");
			}
		}
		return "";    //  DB 오류
	}
	
	public int setChildGPS(String childKey, double lati, double longi) {
		String sql = "insert into CHILD_GPS_DATA values(?, ?, ?, ?);";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			pstmt.setDouble(2, lati);
			pstmt.setDouble(3, longi);
			pstmt.setString(4, getTime());
			return pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("PostDAO getDate SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO setChildGPS close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
}
