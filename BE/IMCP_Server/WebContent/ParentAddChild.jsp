<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ParentDAO"%>
<%@ page import="User.ParentDTO"%>
<%@ page import="com.oreilly.servlet.MultipartRequest" %>
<%@ page import="com.oreilly.servlet.multipart.DefaultFileRenamePolicy" %>
<%
		request.setCharacterEncoding("UTF-8");
		String name = request.getParameter("name"); //	아이이름		
		String birth = request.getParameter("birth"); //	아이 생년월일
		String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
		String filePath = getServletContext().getRealPath("/") + "WebContent\\upload";	//	이미지 저장경로
		String returns = "";
		int sizeLimit = 5 * 1024 * 1024; // 최대용량 5MB
		
		//	MultipartRequest 사용즉시  서버에 파일을 받는다
		MultipartRequest multi = new MultipartRequest(request, filePath, sizeLimit,
														"utf-8", new DefaultFileRenamePolicy());
														
		ParentDTO parentDTO = new ParentDTO();
		parentDTO.setImg_realfile(multi.getFilesystemName("upfile"));
		parentDTO.setFilePath(filePath);
		parentDTO.setName(name);
		parentDTO.setBirth(birth);
		parentDTO.setChildKey(childKey);
		
		ParentDAO parentDAO = new ParentDAO(parentDTO);
		returns = parentDAO.addChild();
	
		out.clear();
		out.print(returns);
		out.flush();
%>
