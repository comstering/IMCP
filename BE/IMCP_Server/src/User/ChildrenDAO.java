package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBConnect.DBConnector;

public class ChildrenDAO {
	private DBConnector dbConnector;
	private Connection conn;
	
	private String sql = "";
	private String sql2 = "";
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;
	private String results = "";
	private String error = "non";

	public ChildrenDAO() {
		dbConnector = new DBConnector();
	}
	
	public String childrenRegister(String key, String password) {	//	아이 고유키 생성
		try {
			
			conn = dbConnector.getConnection();
			sql = "select key from CHILD where key=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, key);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "keyAlreadyExist";	//	키가 존재하여 저장하지 못할 때
			}
			else {
				sql2 = "insert into CHILD (key, password) values (?, ?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, key);
				pstmt2.setString(2, password);
				pstmt2.executeUpdate();
				return "childRegisterSuccess";	//	키 저장 성공
			}
			
		} catch (SQLException e) {
			System.err.println("ChildrenDAO childrenRegister SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ChildrenDAO childrenRegister close SQLExceptoin error");
			}
		}
		return error;
	}
	
	public String childrenLogin(String key, String password) {	//	아이 고유키 확인을 위한 로그인
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD where key=? and password=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, key);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "childLoginSuccess";	//	로그인 성공
			}
			else {
				return "childLoginFail";	//	로그인 실패
			}
		} catch (SQLException e) {
			System.err.println("ChildrenDAO childrenLogin SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ChildrenDAO childrenLogin close SQLExceptoin error");
			}
		}
		return error;
	}
	
	public String childrenGPSSet(String childKey, double latitude, double longtitude) {
		try {
			conn = dbConnector.getConnection();
			sql = "select * from GPS_INITIALIZATION where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				sql2 = "insert into GPS_INITIALIZATION (latitude, longtitude) values (?, ?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setDouble(1, latitude);
				pstmt2.setDouble(2, longtitude);
				pstmt2.executeUpdate();
				return "childGPSSetSuccess";
			}
			else {	//	아이고유키(아이식별값)이 잘못된 경우
				return "childGPSSetFail";
			}
			
		} catch (SQLException e) {
			System.err.println("ChildrenDAO childGPSSet close SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ChildrenDAO childGPSSet close SQLExceptoin error");
			}
		}
		return error;
	}
	
	public String childrenSOS(String childKey) {
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			pstmt.executeQuery();
			if(rs.next()) {
				boolean temp = rs.getBoolean("help");	//	데이터베이스에 저장된 help(boolean)값을 가져온다
				sql2 = "update CHILD_INFO set help=? where childkey=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setBoolean(1, !temp);
				pstmt2.setString(2, childKey);
				pstmt2.executeUpdate();
				return "childSOSUpdateSuccess";
			}
			else {
				return "childSOSUpdateFail";
			}
		} catch (SQLException e) {
			System.err.println("ChildrenDAO childSOS close SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ChildrenDAO childSOS close SQLExceptoin error");
			}
		}
		return error;
	}
	
	public String childrenDanger(String childKey) {
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			pstmt.executeQuery();
			if(rs.next()) {
				boolean temp = rs.getBoolean("dangerous");	//	데이터베이스에 저장된 dangerous(boolean)값을 가져온다
				sql2 = "update CHILD_INFO set dangerous=? where childkey=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setBoolean(1, !temp);
				pstmt2.setString(2, childKey);
				pstmt2.executeUpdate();
				return "childDangerUpdateSuccess";
			}
			else {
				return "childDangerUpdateFail";
			}
		} catch (SQLException e) {
			System.err.println("ChildrenDAO childDanger close SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ChildrenDAO childDanger close SQLExceptoin error");
			}
		}
		return error;
	}
	
	public String childrenMissing(String childKey) {
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			pstmt.executeQuery();
			if(rs.next()) {
				boolean temp = rs.getBoolean("missing");	//	데이터베이스에 저장된 missing(boolean)값을 가져온다
				sql2 = "update CHILD_INFO set missing=? where childkey=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setBoolean(1, !temp);
				pstmt2.setString(2, childKey);
				pstmt2.executeUpdate();
				return "childMissingUpdateSuccess";
			}
			else {
				return "childMissingUpdateFail";
			}
		} catch (SQLException e) {
			System.err.println("ChildrenDAO childMissing close SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ChildrenDAO childMissing close SQLExceptoin error");
			}
		}
		return error;
	}
}
