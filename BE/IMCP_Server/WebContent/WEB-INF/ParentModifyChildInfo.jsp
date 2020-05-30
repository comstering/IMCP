<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ParentDAO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String img_file = request.getParameter("img_file"); //	파일경로
		String img_realfile = request.getParameter("img_realfile"); //	변경할 파일이름
		String name = request.getParameter("name"); //	변경된 아이이름		
		String birth = request.getParameter("birth"); //	변경된 아이 생년월일
		String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
		String returns = "";
	
		ParentDAO parentDAO = new ParentDAO();
		returns = parentDAO.modifyChildInfo(childKey, img_file, img_realfile, name, birth);
	
		out.clear();
		out.print(returns);
		out.flush();
%>
