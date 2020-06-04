package User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBConnect.DBConnector;

public class ParentDAO {
	private DBConnector dbConnector;
	private Connection conn;
	private ParentDTO paDTO;
	
	private String sql = "";
	private String sql2 = "";
	private PreparedStatement pstmt;
	private PreparedStatement pstmt2;
	private ResultSet rs;
	private String results = "";
	private String error = "non";

	public ParentDAO(ParentDTO paDTO){
		this.paDTO = paDTO;
		dbConnector = new DBConnector();
	}
	
	public String addChild() {	//	내 아이 추가하기
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paDTO.getChildKey());
			rs = pstmt.executeQuery();
			if(rs.next()) {	//	등록된 아이일때
				return "childAddFail";
			}
			else {	//	아이를 등록
				sql2 = "insert into CHILD_INFO (img_realfile, name, childkey, birth) values (?, ?, ?, ?)";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, paDTO.getImg_realfile());
				pstmt2.setString(2, paDTO.getName());
				pstmt2.setString(3, paDTO.getChildKey());
				pstmt2.setString(4, paDTO.getBirth());
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
	
	public String modifyChildInfo() {	//	아이 정보 수정(사진, 이름, 생년월일, GPS-자주 방문하는 곳)
		try {
			conn = dbConnector.getConnection();
			sql = "select * from CHILD_INFO where childkey=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, paDTO.getChildKey());
			rs = pstmt.executeQuery();
			if(rs.next()) {	//	아이 정보가 존재할 떄
				sql2 = "update CHILD_INFO set img_realfile=?, name=?, birth=? where childkey=?";
				pstmt2 = conn.prepareStatement(sql2);
				pstmt2.setString(1, paDTO.getImg_realfile());
				pstmt2.setString(2, paDTO.getName());
				pstmt2.setString(3, paDTO.getBirth());
				pstmt2.setString(4, paDTO.getChildKey());
				pstmt2.executeUpdate();
				
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
