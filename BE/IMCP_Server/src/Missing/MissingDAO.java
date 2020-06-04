package Missing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import DBConnect.DBConnector;
import User.ChildDTO;

public class MissingDAO {
	private DBConnector dbConnector;
	private Connection conn;
	
	private ResultSet rs;
	
	public MissingDAO() {
		dbConnector = new DBConnector();
	}
	
	public ArrayList<MissingDTO> getMissingList() {
		ArrayList<MissingDTO> list = new ArrayList<MissingDTO>();
		String sql = "select CHILDKEY, IMG_REALFILE, NAME, BIRTH from MISSING_DATA where MISSING = TRUE";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MissingDTO missingDTO = new MissingDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				list.add(missingDTO);
			}
		} catch (SQLException e) {
			System.err.println("MissingDAO getMissingList SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("MissingDAO getMissingList close SQLException error");
			}
		}
		return list;
	}
	
	public MissingDTO getMissingInfo(String childKey) {
		String sql = "select IMG_REALFILE, NAME, BIRTH, PHONE from MISSING_DATA where CHILDKEY = ? and MISSING = TRUE";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				MissingDTO missingDTO = new MissingDTO(childKey, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				return missingDTO;
			}
		} catch (SQLException e) {
			System.err.println("MissingDAO getMissingInfo SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("MissingDAO getMissingInfo close SQLException error");
			}
		}
		return null;
	}
	
	public double[] getMissingGPS(String childKey) {
		double[] gps = new double[2];
		String sql = "select LATITUDE, LONGITUDE from CHILD_GPS order by DATETIME desc limit 1 where CHILDKEY = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				gps[0] = rs.getDouble(1);
				gps[1] = rs.getDouble(2);
			}
		} catch (SQLException e) {
			System.err.println("MissingDAO getMissingGPS SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("MissingDAO getMissingGPS close SQLException error");
			}
		}
		
		return gps;
	}
}
