package Parent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import Child.*;
import DBConnect.DBConnector;

public class ParentDAO {
	// DB 연결 변수
	private DBConnector dbConnector;
	private Connection conn;
	
	//  SQL 질의 결과 변수
	private ResultSet rs;
	
	public ParentDAO() {
		dbConnector = DBConnector.getInstance();
	}
	
	private boolean checkID(String id) {    //  아이디 중복확인
		String sql = "select ID from PARENT_INFO where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  아이디
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return false;	//  아이디 중복
			} else {
				return true;    //  아이디 사용가능
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO checkID SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO checkID close SQLException error");
			}
		}
		return false;    //  DB 오류
	}
	
	public int join(String id, String password, String name, String phone, String email) {    //  회원가입
		if(checkID(id)) {
			String sql = "insert into PARENT_INFO values(?, ?, ?, ?, ?)";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, id);    //  아이디
				pstmt.setString(2, password);    //  비밀번호
				pstmt.setString(3, name);    //  이름
				pstmt.setString(4, phone);    //  핸드폰번호
				pstmt.setString(5, email);    //  이메일
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO join SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("PatentDAO join close SQLException error");
				}
			}
			return -1;    //  DB 오류
		} else {
			return 0;    //  아이디 중복
		}
	}
	
	public String login(String id, String password) {    //  로그인
		String sql = "select Password from PARENT_INFO where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  부모 아이디
			rs = pstmt.executeQuery();
			if(rs.next()) {
				if(rs.getString(1).equals(password)) {
					return "LoginSuccess";    //  로그인 성공
				} else {
					return "LoginFail";    //  비밀번호 오류
				}
			}
			return "NoID";    //  아이디 없음
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO login SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO login close SQLException error");
			}
		}
		return "DBError";    //  DB 오류
	}
	
	public String findID(String name, String email) {    //  아이디 찾기
		String sql = "select ID from PARENT_INFO where Name = ? and Email = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, name);    //  부모 이름
			pstmt.setString(2, email);    //  부모 이메일
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return rs.getString(1);    //  찾은 아이디 반환
			}
			return "NoID";    //  해당하는 아니디 없음
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO findID SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO findID close SQLException error");
			}
		}
		return "DBError";    //  DB 오류
	}
	
	public String findPW(String id, String name, String email) {    //  패스워드 찾기
		String sql = "select ID from PARNET_INFO where ID = ? and Name = ? and Email = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  아이디
			pstmt.setString(2, name);    //  이름
			pstmt.setString(3, email);    //  핸드폰번호
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return "FindPWSuccess";    //  해당아이디 확인 성공, 비밀번호 변경 가능
			}
			return "FindPWFail";    //  해당아이디 확인 실패, 비밀번호 변경 불가
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO findPW SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO findPW close SQLException error");
			}
		}
		return "DBError";    //  DB 오류
	}
	
	public int newPW(String id, String newPassword, String email) {    //  새로운 비밀번호
		String sql = "update PARENT_INFO set password = ? where ID = ? and Email = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newPassword);    //  새로운 비밀번호
			pstmt.setString(2, id);    //  아이디
			pstmt.setString(3, email);    //  핸드폰번호
			return pstmt.executeUpdate();    //  비밀번호 변경 완료
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO newPW SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO newPW close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int changePW(String id, String password, String newPassword) {    //  패스워드 바꾸기
		String sql = "update PARENT_INFO set Password = ? where ID = ? and Password = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newPassword);    //  새로운 비밀번호
			pstmt.setString(2, id);    //  아이디
			pstmt.setString(3, password);    //  이전번호
			return pstmt.executeUpdate();    //  비밀번호 변경 완료
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO changePW SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO changePW close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public String getParentInfo(String parentID) {    //  부모 정보 획득
		ParentDTO parentDTO = null;    //  부모 정보 저장 변수
		
		String sql = "select ID, Name, Phone, Email from PARENT_INFO where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parentID);    //  부모 아이디
			rs = pstmt.executeQuery();
			if(rs.next()) {
				parentDTO = new ParentDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO getParentInfo SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO getParentInfo close SQLException error");
			}
		}
		
		HashMap<String, Object> hashMap = new HashMap<String, Object>();    //  key와 value로 묶기
		hashMap.put("id", parentDTO.getId());    //  부모 아이디
		hashMap.put("name", parentDTO.getName());    //  부모 이름
		hashMap.put("phone", parentDTO.getPhone());    //  부모 핸드폰번호
		hashMap.put("email", parentDTO.getEmail());    //  부모 이메일
		JSONObject parentObject = new JSONObject(hashMap);    //  JSON데이터로 만들기
		
		return parentObject.toJSONString();    //  JSON데이터 String으로 변환하여 반환
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
			System.err.println("ParentDAO getTime SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ParentDAO getTime close SQLException error");
			}
		}
		return "";    //  DB 오류
	}
	
	private int checkParentGPS(String id) {    //  부모의 위치 정보가 저장되어 있는지 확인
		String sql = "select * from PARENT_GPS where ID = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);    //  부모 아이디
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 1;    //  저장된 위치 정보가 있을 경우
			}
			return 0;    //  저장된  위치 정보가 없을 경우
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO checkParentGPS SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO checkParentGPS close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int setParentGPS(String id, double lati, double longi) {    //  부모 현재 위치 저장(누적 x, 갱신 o)
		int check = checkParentGPS(id);
		String sql = "";
		if(check == 1) {    //  부모 위치 저장 정보가 있을 경우
			sql = "update PARENT_GPS set Time = ?, Latitude = ?, Longitude = ? where ID = ?";
		} else if(check == 0){    //  부모 위치 저장 정보가 없을 경우
			sql = "insert into PARENT_GPS values(?, ?, ?, ?)";
		} else {
			return 0;    //  부모 위치 저장 정보 존재 확인 오류
		}
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			if(check == 1) {    //  부모 위치 저장 정보가 있을 경우
				pstmt.setString(1, getTime());    //  서버 시간
				pstmt.setDouble(2, lati);    //  위도
				pstmt.setDouble(3, longi);    //  경도
				pstmt.setString(4, id);    //  부모 아이디
			} else {    //  부모 위치 저장 정보가 없을 경우
				pstmt.setString(1, id);    //  부모 아이디
				pstmt.setString(1, getTime());    //  서버 시간
				pstmt.setDouble(1, lati);    //  위도
				pstmt.setDouble(4, longi);    //  경도
			}
			return pstmt.executeUpdate();
		}  catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO setParentGPS SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO setParentGPS close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	private int checkChildKey(String childKey, String password) {    //  아이 식별값 존재 확인
		String sql = "select ChildKey from PRIVATE_KEY where ChildKey = ? and Password = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			pstmt.setString(2, password);    //  아이 식별값 확인 비밀번호
			rs = pstmt.executeQuery();
			if(rs.next()) {
				return 1;    //  아이 고유키 정보 있음, 비밀번호도 일치
			}
			return 0;    //  아이 고유키 정보 없음 또는 비밀번호 불일치
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO checkChildKey SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("PatentDAO checkChildKey close SQLException error");
			}
		}
		return -1;    //  DB 오류
	}
	
	public int addChild(String childKey, String password, String name, String birth, String img) {    //  아이 추가
		if(checkChildKey(childKey, password) == 1) {
			String sql = "insert into CHILD_INFO values(?, ?, ?, ?)";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childKey);    //  아이 식별값
				pstmt.setString(2, name);    //  아이 이름
				pstmt.setString(3, birth);    //  아이 생일
				pstmt.setString(4, img);    //  아이 사진파일 경로
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO addChild SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("PatentDAO addChild close SQLException error");
				}
			}
			return -1;    //  DB 오류
		} else if(checkChildKey(childKey, password) == 0){
			return 0;    //  아이 식별값 오류
		} else {
			return -1;    //  아이 식별값 check DB 오류
		}
	}
	
	public String getChildList(String parentID) {    //  해당 부모의 아이들 리스트 획득
		ArrayList<ChildDTO> list = new ArrayList<ChildDTO>();    //  아이들 리스트를 저장할 리스트 변수
		
		String sql = "select ChildKey, Name, Birth, Image from CHILD_INFO where ChildKey in (select ChildKey from PtoC where ID = ?)";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, parentID);    //  부모 아이디
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ChildDTO childDTO = new ChildDTO(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4));
				list.add(childDTO);
			}
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO getChildList SQLExceptoin error");
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
				if(rs != null) {rs.close();}
			} catch(SQLException e) {
				System.err.println("ParentDAO getChildList close SQLException error");
			}
		}
		
		ArrayList<JSONObject> childArray = new ArrayList<JSONObject>();    //  JSON데이터들을 담을 List
		for(int i = 0; i < list.size(); i++) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();    //  key와 value로 묶기
			hashMap.put("key", list.get(i).getKey());    //  아이 식별값
			hashMap.put("name", list.get(i).getName());    //  아이 이름
			hashMap.put("birth", list.get(i).getBirth());    //  아이 생년월일
			hashMap.put("image", list.get(i).getImg());    //  아이 사진 경로
			JSONObject childObject = new JSONObject(hashMap);    //  JSON데이터로 만들기
			childArray.add(childObject);    //  JSON List에 추가
		}
		
		return childArray.toString();
	}
	
	private boolean checkChildInfo(String childKey) {    //  아이 정보 유무 확인
		ChildDAO childDAO = new ChildDAO();
		return childDAO.checkChild(childKey);
	}
	
	public ChildDTO getChildInfo(String childKey) {    //  아이 정보 획득
		ChildDTO childDTO = null;
		if(checkChildInfo(childKey)) {
			String sql = "select Name, Birth, Image from CHILD_INFO where ChildKey = ?";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childKey);    //  아이 식별값
				rs = pstmt.executeQuery();
				if(rs.next()) {
					childDTO = new ChildDTO(childKey, rs.getString(1), rs.getString(2), rs.getString(3));
				}
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO getChildInfo SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
					if(rs != null) {rs.close();}
				} catch(SQLException e) {
					System.err.println("ParentDAO getChildInfo close SQLException error");
				}
			}
		}
		return childDTO;
	}
	
	public String getChildGPS(String childKey) {    //  아이 현재 위치 획득
		JSONObject gpsObject = new JSONObject();
		if(checkChildInfo(childKey)) {
			String sql = "select Latitude, Longitude from CHILD_GPS where ChildKey = ? order by Time desc limit 1";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childKey);    //  아이 식별값
				rs = pstmt.executeQuery();
				if(rs.next()) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();    //  key와 value로 묶기
					hashMap.put("lati", rs.getDouble(1));    //  위도
					hashMap.put("longi", rs.getDouble(2));    //  경도
					gpsObject = new JSONObject(hashMap);    //  JSON데이터로 만들기
				}
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO getChildGPS SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
					if(rs != null) {rs.close();}
				} catch(SQLException e) {
					System.err.println("ParentDAO getChildGPS close SQLException error");
				}
			}	
		}
		return gpsObject.toJSONString();    //  JSON데이터 String으로 변환하여 반환
	}
	
	public int modifyChildInfo(String childKey, String childPassword, ChildDTO childDTO) {    //  아이 정보 수정
		if(checkChildKey(childKey, childPassword) == 1) {
			String sql = "update CHILD_INFO set ChildKey = ?, Name = ?, Birth = ?, Image = ? where ChildKey = ?";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childDTO.getKey());    //  아이 식별값
				pstmt.setString(2, childDTO.getName());    //  아이 이름
				pstmt.setString(3, childDTO.getBirth());    //  아이 생년월일
				pstmt.setString(4, childDTO.getImg());    //  아이 사진 저장 경로
				pstmt.setString(5, childKey);    //  이전 아이 식별값(아이 핸드폰이나 기기가 변경되었을 경우)
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO modifyChildInfo SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("PatentDAO modifyChildInfo close SQLException error");
				}
			}
			return -1;    //  DB 오류
		} else {
			return 0;    //  아이 식별값 오류
		}
	}
	
	private boolean deleteInitial(String childKey) {    //  초기 안전 위치 등록시 이전에 저장된 Initial 삭제
		int result = 0;
		String sql = "delete from CHILD_GPS_INITIAL where ChildKey = ?";
		conn = dbConnector.getConnection();
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, childKey);    //  아이 식별값
			result = pstmt.executeUpdate();
		} catch (SQLException e) {    //  예외처리
			System.err.println("ParentDAO deleteInitial SQLException error");
			result = -1;    //  DB 오류
		} finally {    //  자원해제
			try {
				if(conn != null) {conn.close();}
				if(pstmt != null) {pstmt.close();}
			} catch(SQLException e) {
				System.err.println("ParentDAO deleteInitial SQLException error");
			}
		}
		if(result == 1) {
			return true;    //  delete 성공시
		} else {
			return false;    //  delete 실패시
		}
	}
	
	public int setChildIntial(String childKey, String gpsData) {    //  아이 초기 안전 위치 세팅
		int result = 0;
		if(checkChildInfo(childKey)) {
			if(deleteInitial(childKey)) {
				JSONParser jsonParser = new JSONParser();
				try {
					JSONArray gpsArray = (JSONArray) jsonParser.parse(gpsData);
					for(int i = 0; i < gpsArray.size(); i++) {
						JSONObject gpsJSON = (JSONObject) gpsArray.get(i);
						double lati = Double.parseDouble(gpsJSON.get("lati").toString());
						double longi = Double.parseDouble(gpsJSON.get("longi").toString());
						String sql = "insert into CHILD_GPS_INITIAL values(?, ?, ?)";
						conn = dbConnector.getConnection();
						PreparedStatement pstmt = null;
						try {
							pstmt = conn.prepareStatement(sql);
							pstmt.setString(1, childKey);    //  아이 식별값
							pstmt.setDouble(2, lati);    //  위도
							pstmt.setDouble(3, longi);    //  경도
							result = pstmt.executeUpdate();
						} catch (SQLException e) {    //  예외처리
							System.err.println("ParentDAO setChildInitial SQLException error");
							result = -1;    //  DB 오류
						} finally {    //  자원해제
							try {
								if(conn != null) {conn.close();}
								if(pstmt != null) {pstmt.close();}
							} catch(SQLException e) {
								System.err.println("ParentDAO setChildInitial SQLException error");
							}
						}
						if(result == -1) {
							break;
						}
					}
				} catch (ParseException e) {    //  JSON 예외처리
					System.err.println("ParentDAO setChildInitial ParseException error");
					result = -2;    //  JSON 오류
				}
			} else {
				result = -3;    //  초기 위치 삭제 실패
			}
			
		}
		/*
		 * result = 0: 저장된 아이 정보 없음
		 * result = 1: 위치설정 성공
		 * result = -1: DB 오류
		 * result = -2: JSON 오류
		 * result = -3: 원래 있던 위치 삭제 오류
		 */
		return result;
	}
	
	public String getChildInitial(String childKey) {    //  아이 초기 안전 위치 세팅값 획득
		ArrayList<JSONObject> initialArray = new ArrayList<JSONObject>();
		if(checkChildInfo(childKey)) {
			String sql = "select Latitude, Longitude from CHILD_GPS_INITIAL where ChildKey = ?";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, childKey);    //  아이 식별값
				rs = pstmt.executeQuery();
				while(rs.next()) {
					HashMap<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("lati", rs.getDouble(1));    //  위도
					hashMap.put("longi", rs.getDouble(2));    //  경도
					JSONObject initialObject = new JSONObject(hashMap);
					initialArray.add(initialObject);    //  JSON List에 추가
				}
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO getChildInitial SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
					if(rs != null) {rs.close();}
				} catch(SQLException e) {
					System.err.println("ParentDAO getChildInitial close SQLException error");
				}
			}
		}
		return initialArray.toString();
	}
	
	public int childMissing(String childKey, boolean type) {    //  아이 실종여부 등록
		if(checkChildInfo(childKey)) {
			String sql = "update CHILD_INFO set Missing = ? where ChildKey = ?";
			conn = dbConnector.getConnection();
			PreparedStatement pstmt = null;
			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setBoolean(1, type);    //  아이 실종 여부
				pstmt.setString(2, childKey);    //  아이 식별값
				return pstmt.executeUpdate();
			} catch (SQLException e) {    //  예외처리
				System.err.println("ParentDAO childMissing SQLExceptoin error");
			} finally {    //  자원해제
				try {
					if(conn != null) {conn.close();}
					if(pstmt != null) {pstmt.close();}
				} catch(SQLException e) {
					System.err.println("ParentDAO childMissing close SQLException error");
				}
			}
			return -1;    //  DB 오류
		}
		return 0;    //  아이 정보가 저장되어 있지 않음
	}
}
