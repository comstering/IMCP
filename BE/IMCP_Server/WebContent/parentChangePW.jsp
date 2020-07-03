<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  부모 아이디
	String password = request.getParameter("password");    //  현재 비밀번호
	String newPassword = request.getParameter("newPassword");    //  새로 바꿀 비밀번호
	
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.changePW(id, password, newPassword);
	
	String returns = "";
	if(result == 1) {
		returns = "PWChangeSuccess";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>
