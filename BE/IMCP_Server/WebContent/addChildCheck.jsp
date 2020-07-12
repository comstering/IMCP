<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String parentID = request.getParameter("ID");    //  부모 아이디
	String childKey = request.getParameter("childKey");    //  아이 식별값
	String password = request.getParameter("password");    //  고유키 비밀번호
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.addChildCheck(parentID, childKey, password);
	
	String returns = "";
	
	if(result == 1) {
		returns = "AlreadyInfo";
	} else if(result == 0) {
		returns = "NoInfo";
	} else if(result == -1) {
		returns = "DBError";
	} else if(result == -2) {
		returns = "PtoCError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>