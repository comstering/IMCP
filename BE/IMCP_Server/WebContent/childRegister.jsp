<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Child.ChildDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("childKey");    //  아이 고유키(식별키)
	String password = request.getParameter("password");    //  고유키 비밀번호
	
	ChildDAO childDAO = new ChildDAO();
	int result = childDAO.childRegister(childKey, password);
	
	String returns = "";
	
	if(result == 1) {
		returns = "ChildRegisterSuccess";
	} else if(result == 0) {
		returns = "SamePrivateKey";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>