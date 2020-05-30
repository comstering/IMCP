<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ParentDAO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String img_file = request.getParameter("img_file"); //	파일경로
		String img_realfile = request.getParameter("img_realfile"); //	파일이름
		String name = request.getParameter("name"); //	아이이름		
		String birth = request.getParameter("birth"); //	아이 생년월일
		String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
		String returns = "";
	
		ParentDAO parentDAO = new ParentDAO();
		returns = parentDAO.addChild(img_file, img_realfile, name, childKey, birth);
	
		out.clear();
		out.print(returns);
		out.flush();
%>
