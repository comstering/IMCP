package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBConnect.DBConnector;

public class ParentDAO {
	private DBConnector dbConnector;
	private Connection conn;
	
	private String sql = "";
	private String sql2 = "";
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;
	private String results = "";
	private String error = "non";

	public ParentDAO(){
		dbConnector = new DBConnector();
	}
	
	public String addChild(String img_file, String img_realfile,
							String name, String childKey, String birth) {	//	내 아이 추가하기
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			rs = pstmt.executeQuery();
			if(rs.next()) {	//	등록된 아이일때
				return "childAddFail";
			}
			else {	//	아이를 등록
				sql2 = "insert into CHILD_INFO (img_file, img_realfile, name, childkey, birth) values (?, ?, ?, ?, ?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, img_file);
				pstmt2.setString(2, img_realfile);
				pstmt2.setString(3, name);
				pstmt2.setString(4, childKey);
				pstmt2.setString(5, birth);
				/*
				 * 이미지 파일을 서버에 저장하는 코드 필요
				 */
				return "childAddSuccess";
			}
		} catch (SQLException e) {
			System.err.println("ParentDAO addChild SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ParentDAO addChild close SQLExceptoin error");
			}
		}
		return error;
	}
	
	public String modifyChildInfo(String childKey, String img_file, String img_realfile, 
									String name, String birth) {	//	아이 정보 수정(사진, 이름, 생년월일, GPS-자주 방문하는 곳)
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);
			rs = pstmt.executeQuery();
			if(rs.next()) {	//	아이 정보가 존재할 떄
				sql2 = "update CHILD_INFO set img_file=?, img_realfile=?, name=?, birth=? where childkey=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, img_file);
				pstmt2.setString(2, img_realfile);
				pstmt2.setString(3, name);
				pstmt2.setString(4, birth);
				pstmt2.setString(5, childKey);
				pstmt2.executeUpdate();
				/*
				 * 이미지 파일을 서버에 저장하는 코드 필요
				 */
				
				return "childInfoUpdateSuccess";
			}
			else {
				return "childInfoUpdateFail";
			}
			
		} catch (SQLException e) {
			System.err.println("ParentDAO modifyChildInfo SQLExceptoin error");
		} finally {
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch (SQLException e) {
				System.err.println("ParentDAO modifyChildInfo close SQLExceptoin error");
			}
		}
		return error;
	}
}
