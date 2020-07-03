package Missing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import DBConnect.DBConnector;

public class MissingDAO {
	//  DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 결과 변수
	private ResultSet rs;
	
	public MissingDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	public String getMissingList() {    //  실종 아동 목록 획득
		ArrayList<MissingDTO> list = new ArrayList<MissingDTO>();
		String sql = "select ChildKey, Image, Name, Birth, Phone from MISSING_INFO";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				MissingDTO missingDTO = new MissingDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
				list.add(missingDTO);
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("MissingDAO getMissingList SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("MissingDAO getMissingList close SQLException error");
			}
		}
		
		ArrayList<JSONObject> missingArray = new ArrayList<JSONObject>();    //  JSON 데이터들을 담을 List
		for(int i = 0; i < list.size(); i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();    //  key와 value로 묶기
			hashMap.put("key", list.get(i).getChildKey());
			hashMap.put("name", list.get(i).getName());
			hashMap.put("birth", list.get(i).getBirth());
			hashMap.put("image", list.get(i).getImg());
			hashMap.put("parentPhone", list.get(i).getPrentPhone());
			JSONObject childObject = new JSONObject(hashMap);    //  JSON 데이터로 만들기
			missingArray.add(childObject);    //  JSON List에 추가
		}
		
		return missingArray.toString();
	}
	
	public MissingDTO getMissingInfo(String childKey) {    //  실종아동 정보 획득
		String sql = "select Image, Name, Birth, Phone from MISSING_INFO where ChildKey = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			rs = pstmt.executeQuery();
			if(rs.next()) {
				MissingDTO missingDTO = new MissingDTO(childKey, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				return missingDTO;
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("MissingDAO getMissingInfo SQLExceptoin error");
		} finally {    //  자원해제
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
	
	public double[] getMissingGPS(String childKey) {    //  실종아동 위치 획득
		double[] gps = new double[2];
		String sql = "select Latitude, Longitude from CHILD_GPS where ChildKey = ? order by Time desc limit 1 ";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			rs = pstmt.executeQuery();
			if(rs.next()) {
				gps[0] = rs.getDouble(1);    //  위도
				gps[1] = rs.getDouble(2);    //  경도
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("MissingDAO getMissingGPS SQLExceptoin error");
		} finally {    //  자원해제
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
