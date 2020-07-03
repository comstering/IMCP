<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  부모 아이디
	String password = request.getParameter("password");    //  비밀번호
	String name = request.getParameter("name");    //  이름
	String phone = request.getParameter("phone");    //  핸드폰 번호
	String email = request.getParameter("email");    //  이메일
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.join(id, password, name, phone, email);
	
	String returns = "";
	if(result == 1) {
		returns = "JoinSuccess";
	} else if(result == 0) {
		returns = "SameID";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>