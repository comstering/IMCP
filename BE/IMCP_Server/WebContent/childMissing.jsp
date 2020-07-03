<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
	String missing = request.getParameter("missing");    //  실종 여부
	
	boolean type = false;
	
	if(missing.equals("missed")) {
		type = true;
	} else if(missing.equals("finded")) {
		type = false;
	}
	
	String returns = "";
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.childMissing(childKey, type);
	
	if(result == 1) {
		returns = "MissingSetSuccess";
	} else if(result == 0) {
		returns = "NoChildInfo";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>