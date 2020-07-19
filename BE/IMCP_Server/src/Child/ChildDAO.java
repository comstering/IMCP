package Child;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONObject;

import DBConnect.DBConnector;

public class ChildDAO {
	// DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 결과 변수
	private ResultSet rs;
	
	public ChildDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	private boolean checkKey(String childKey) {    //  저장할 고유키가 중복되는지 확인
		String sql = "select ChildKey from PRIVATE_KEY where ChildKey=?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return false;	//	키가 존재하여 저장이 불가능 할 때
			} else {
				return true;    //  키가 존재하지 않아 저장이 가능할 때
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ChildDAO checkKey SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO checkKey close SQLException error");
			}
		}
		return false;    //  DB 오류
	}
	
	public int childRegister(String childKey, String password) {    //  아이 고유키 저장
		if(checkKey(childKey)) {    //  키가 존재하지 않아 저장이 가능할 때
			String sql = "insert into PRIVATE_KEY values(?, ?)";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childKey);    //  아이 식별값
				pstmt.setString(2, password);    //  고유키 비밀번호
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ChildDAO childRegister SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("ChildDAO childRegister close SQLException error");
				}
			}
			return -1;    //  DB 오류
		} else {
			return 0;    //  키가 존재하여 저장이 불가능할 때
		}
	}
	
	public String childLogin(String childKey, String password) {    //  아이 고유키 확인을 위한 로그인
		String sql = "select ChildKey from PRIVATE_KEY where ChildKey = ? and Password = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			pstmt.setString(2, password);    //  고유키 비밀번호
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "ChildLoginSuccess";    //  로그인 성공
			} else {
				return "ChildLoginFail";    //  로그인 실패
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ChildDAO childLogin SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO childLogin close SQLException error");
			}
		}
		return "DBError";    //  DB 오류
	}
	
	public boolean checkChild(String childKey) {    //  아이정보가 저장되었는지 확인
		String sql = "select ChildKey from CHILD_INFO where ChildKey = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return true;    //  아이 정보가 저장이 되어 있음
			} else {
				return false;    //  아이 정보가 저장되어 있지 않음
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ChildDAO checkChild SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO checkChild close SQLException error");
			}
		}
		return false;    //  DB 오류
	}
	
	private String getTime() {    //  서버 현재 시간 구하기
		String sql = "select now()";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ChildDAO getTime SQLExceptoin error");
		} finally {    //  자원해제
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
	
	public int setChildGPS(String childKey, double lati, double longi) {    //  아이 현재 위치 등록
		if(checkChild(childKey)) {
			String sql = "insert into CHILD_GPS values(?, ?, ?, ?);";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childKey);    //  아이 식별값
				pstmt.setString(2, getTime());    //  서버 현재시간
				pstmt.setDouble(3, lati);    //  위도
				pstmt.setDouble(4, longi);    //  경도
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ChildDAO setChildGPS SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("ChildDAO setChildGPS close SQLException error");
				}
			}
			return -1;    //  DB 오류	
		}
		else {
			return 0;    //  아이 정보가 저장되어 있지 않음
		}
	}
	
	private ArrayList<String> getParentsToken(String childKey) {    //  부모 fcm 토큰 값 획득
		ArrayList<String> list = new ArrayList<String>();
		String sql = "select Token from PARENT_TOKEN where ID in (select ID from PtoC where ChildKey = ?)";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			rs = pstmt.executeQuery();
			while(rs.next()) {
				list.add(rs.getString(1));
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ChildDAO getParentsToken SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ChildDAO getParentsToken close SQLException error");
			}
		}
		return list;
	}
	
	private int sendFCMSOS(ArrayList<String> list, boolean type) {    //  FCM 데이터 전송(SOS)
		int result = -200;
		
	    //  데이터를 보낼 URL
		String fcmURL = "https://fcm.googleapis.com/fcm/send";
	    //  FCM Setting -> Cloud Messaging
		String fcmApiKey = "AAAAK-E7ezg:APA91bHMDCXaStMIhwELOcDylGkg-W8EuUPfl8Jt3d2T5B0kdp_o8-IxLvuf9zeCu_vV8KEbLn9Lu6C9XrAwE8ezlJR2kgcrgAz2G7LSgtYY8Gn24r85qR1zE3zno-xdJlhvdYAwY9sK";
		
		DataOutputStream dos = null;
		OutputStreamWriter osw = null;
		BufferedWriter writer = null;
		
		try {
			URL url = new URL(fcmURL);
			HttpURLConnection httpUrlConnection = (HttpURLConnection) url.openConnection();
			httpUrlConnection.setUseCaches(false);
			httpUrlConnection.setDoInput(true);
			httpUrlConnection.setDoOutput(true);
			httpUrlConnection.setRequestMethod("POST");
			httpUrlConnection.setRequestProperty("Authorization", "key=" + fcmApiKey);
			httpUrlConnection.setRequestProperty("Content-Type", "application/json; UTF-8");

			//  FCM으로 보낼 JSONObject
			HashMap<String, Object> hashData = new HashMap<String, Object>();
			hashData.put("title", "SOS");    //  Notification Title
			hashData.put("body", "아이가 위험합니다.");    //  Notification Body
			if(type) {
				hashData.put("SOS", "on");    //  아이가 도움요청 상태
			} else {
				hashData.put("SOS", "off");    //  아이 도움 요청 상태 해결
			}
			JSONObject dataObject = new JSONObject(hashData);    //  HashMap을 JSONObject로 변환

			//  FCM으로 알림 받을 대상 JSONObject
			HashMap<String, Object> hashFCM = new HashMap<String, Object>();
			hashFCM.put("registration_ids", list);    //  보낼 기기들의 토큰
			hashFCM.put("data", dataObject);    //  기기에 보낼 데이터
			JSONObject sendObject = new JSONObject(hashFCM);    //  HashMap을 JSONObject로 변환

			dos = new DataOutputStream(httpUrlConnection.getOutputStream());
			osw = new OutputStreamWriter(dos);
			writer = new BufferedWriter(osw);
			writer.write(sendObject.toJSONString());
			writer.flush();
			httpUrlConnection.connect();
			result = httpUrlConnection.getResponseCode();
		} catch (MalformedURLException e) {
			System.err.println("ChildDAO sendFCMSOS MalformedURLException error");
		} catch (IOException e) {
			System.err.println("ChildDAO sendFCMSOS IOException error");
		} finally {
			try {
				if(dos != null) {dos.close();}
				if(osw != null) {osw.close();}
				if(writer != null) {writer.close();}
			} catch (IOException e) {
				System.err.println("ChildDAO sendFCMSOS close IOException error");
			}
			
		}
		return result;
	}
	
	public int childSOS(String childKey, boolean type) {    //  아이 SOS 여부 등록
		if(checkChild(childKey)) {
			ArrayList<String> list = getParentsToken(childKey);
			int httpResult = sendFCMSOS(list, type);
			if(httpResult != 200) {
				return httpResult;
			}
			String sql = "update CHILD_INFO set Help = ? where ChildKey = ?";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setBoolean(1, type);    //  SOS 여부
				pstmt.setString(2, childKey);    //  아이 식별값
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ChildDAO childSOS SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("ChildDAO childSOS close SQLException error");
				}
			}
			return -1;    //  DB 오류
		}
		return 0;    //  아이 정보가 저장되어 있지 않음
	}
}
