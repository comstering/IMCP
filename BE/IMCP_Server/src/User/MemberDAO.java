package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBConnect.DBConnector;

public class MemberDAO {
	private DBConnector dbConnector;
	private Connection conn;
	
	private String sql = "";
	private String sql2 = "";
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;
	private String results = "";
	private String error = "non";
	public MemberDAO() {
		dbConnector = new DBConnector();
	}

	public String memberJoin(String id, String password, String name, String phone, String email) {	//	회원가입
		try {
			conn = dbConnector.getConnection();
			sql = "select * from PARENT_INFO where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "memberAlreadyExist";
			}
			else {
				sql2 = "insert into PARENT_INFO(id, password, name, phone, email) values (?, ?, ?, ?, ?)";
				pstmt.setString(1, id);
				pstmt.setString(2, password);
				pstmt.setString(3, name);
				pstmt.setString(4, phone);
				pstmt.setString(5, email);
				pstmt.executeUpdate();
				
				return "memberAddSuccess";
			}			
		} catch (SQLException e) {
			System.err.println("MemberDAO memberJoin SQLExceptoin error");
		} finally {
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
					if(rs != null) {rs.close();}
				} catch (SQLException e) {
					System.err.println("MemberDAO memberJoin close SQLExceptoin error");
				}
		}
		//	오류 발생시
		return error;
	}
	
	public String memberLogin(String id, String password) {	//	로그인
		try {
			conn = dbConnector.getConnection();
			sql = "select id, password from PARENT_INFO where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getString("password").equals(password)) {
					return "memberLoginSuccess";
				}
			}
		} catch (SQLException e) {
			System.err.println("MemberDAO memberLogin SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("MemberDAO memberLogin close SQLExceptoin error");
			}
		}
		//	오류 혹은 id가 틀렸거나 비밀번호가 맞지 않을때
		return error;
	}
	
	public String memberFindID(String name, String email) {	//	비밀번호 찾기
		try {
			conn = dbConnector.getConnection();
			sql = "select name, email, id from PARENT_INFO where name=? and email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);
			pstmt.setString(2, email);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				//	찾는 ID를 같이 보내준다 언더바 '_'로 구분
				return rs.getString("id")+"_memberFindSuccess";
			}
		} catch (SQLException e) {
			System.err.println("MemberDAO memberFindID SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("MemberDAO memberFindID close SQLExceptoin error");
			}
		}
		//	오류 혹은 이름과 email이 일치하는 부모가 데이터베이스에 없을 때
		return error;
	}
	
	public String memberFindPW(String id, String name, String email) {	//	비밀번호 찾기
		try {
			conn = dbConnector.getConnection();
			sql = "select id, name, email from PARENT_INFO where id=? and name=? and email=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, name);
			pstmt.setString(3, email);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "memberFindPasswordSuccess";
			}
			
		} catch (SQLException e) {
			System.err.println("MemberDAO memberFindPW SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("MemberDAO memberFindPW close SQLExceptoin error");
			}
		}
		//	오류 혹은 id, 이름, 이메일이 일치하는 정보가 없을 때
		return error;
	}
	
	public String memberChangePW(String id, String password) {	//	비밀번호 변경
		try {
			conn = dbConnector.getConnection();
			sql = "select password from PARENT_INFO where id=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				sql2 = "update PARENT_INFO set password=? where id=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, password);
				pstmt2.setString(2,  id);
			}
		} catch (SQLException e) {
			System.err.println("MemberDAO memberChangePW SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("MemberDAO memberChangePW close SQLExceptoin error");
			}
		}
		//	오류 혹은 id가 없을때
		return error;
	}
}
