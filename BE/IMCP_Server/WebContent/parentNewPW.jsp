<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  부모 아이디
	String newPassword = request.getParameter("newPassword");    //  새로운 비밀번호
	String email = request.getParameter("email");    //  이메일
	
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.newPW(id, newPassword, email);
	
	String returns = "";
	if(result == 1) {
		returns = "NewPWSuccess";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>
