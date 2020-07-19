<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Child.ChildDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
	String sos = request.getParameter("sos");    //  sos 여부
	
	boolean type = false;
	
	if(sos.equals("helped")) {
		type = true;
	} else if(sos.equals("solution")) {
		type = false;
	}
	
	ChildDAO childDAO = new ChildDAO();
	int result = childDAO.childSOS(childKey, type);
	
	String returns = "";		
	if(result == 1) {
		returns = "SOSSetSuccess";
	} else if(result == 0) {
		returns = "NoChildInfo";
	} else if(result == -1) {
		returns = "DBError";
	} else {
		returns = "HttpError" + result;
	}

	out.clear();
	out.print(returns);
	out.flush();
%>