<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%@ page import="Child.ChildDTO" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%
	request.setCharacterEncoding("UTF-8");
	
	String directory = application.getRealPath("/upload/");    //  파일 저장 경로(서버/IMCP_Server/upload/)
	int maxSize = 5 * 1024 * 1024; // 최대용량 5MB
	
	//	MultipartRequest 사용즉시  서버에 파일을 받는다
	MultipartRequest multipartRequest = new MultipartRequest(request, directory, maxSize, "utf-8", new DefaultFileRenamePolicy());
	
	//  시스템에 저장된 파일명
	String img = multipartRequest.getFilesystemName("image");

	//  파일을 제외한 파라미터들
	Enumeration<?> params = multipartRequest.getParameterNames();
	
	String[] values = new String[5];
	int i = 0;
	//  모든 파라미터 값들 저장
	while(params.hasMoreElements()) {
		String param = (String)params.nextElement();
		values[i++] = multipartRequest.getParameter(param);
	}
	//  values[0]: new key
	//  values[1]: password
	//  values[2]: name
	//  values[3]: old key
	//  values[4]: birth
	
	ChildDTO childDTO = new ChildDTO(values[0], values[2], values[4], img);
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.modifyChildInfo(values[3], values[1], childDTO);
	
	String returns = "";
	
	if(result == 1) {
		returns = "ModifySuccess";
	} else if(result == 0) {
		returns = "NoPrivateKey";
	} else if(result == -1) {
		returns = "DBError";
	} else if(result == -2) {
		returns = "NoChildInfo";
	}
	
	out.clear();
	out.print(returns);
	out.flush();
%>