<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%@ page import="java.util.Enumeration" %>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%
	request.setCharacterEncoding("UTF-8");
	
	String directory = application.getRealPath("/upload/");    //  파일 저장 경로(서버/IMCP_Server/upload/)
	int maxSize = 5 * 1024 * 1024;    //  최대용량 5MB
	
	//  MultipartRequest 사용즉시  서버에 파일을 받는다
	MultipartRequest multipartRequest = new MultipartRequest(request, directory, maxSize, "utf-8", new DefaultFileRenamePolicy());
	
	//  시스템에 저장된 파일명
	String img = multipartRequest.getFilesystemName("image");
	
	//  파일을 제외한 파라미터들
	Enumeration<?> params = multipartRequest.getParameterNames();
	
	String[] values = new String[4];
	int i = 0;
	//  모든 파라미터 값들 저장
	while(params.hasMoreElements()) {
		String param = (String)params.nextElement();
		values[i++] = multipartRequest.getParameter(param);
	}
	
	String str = values[0] + values[1] + values[2] + values[3] + img;
	
	/*
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.addChild(values[0], values[1], values[2], values[3], img);
	*/
	String returns = "";
	/*
	if(result == 1) {
		returns = "AddSuccess";
	} else if(result == 0) {
		returns = "NoPrivateKey";
	} else if(result == -1) {
		returns = "DBError";
	}
	*/
	returns = str;
	out.clear();
	out.println(returns);
	out.flush();
%>