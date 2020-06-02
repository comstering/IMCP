<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ParentDAO"%>
<%@ page import="User.ParentDTO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String img_file = request.getParameter("img_file"); //	파일경로
		String img_realfile = request.getParameter("img_realfile"); //	파일이름
		String name = request.getParameter("name"); //	아이이름		
		String birth = request.getParameter("birth"); //	아이 생년월일
		String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
		String downPath = getServletContext().getRealPath("/") + "WebContent\\WEB-INF\\upload";	//	이미지 저장경로
		String returns = "";
		
		ParentDTO parentDTO = new ParentDTO();
		parentDTO.setImg_file(img_file);
		parentDTO.setImg_realfile(img_realfile);
		parentDTO.setName(name);
		parentDTO.setBirth(birth);
		parentDTO.setChildKey(childKey);
		
		ParentDAO parentDAO = new ParentDAO(parentDTO);
		returns = parentDAO.addChild();
	
		out.clear();
		out.print(returns);
		out.flush();
%>
