<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  아이 식별값
	double lati = Double.parseDouble(request.getParameter("lati"));    //  위도
	double longi = Double.parseDouble(request.getParameter("longi"));    //  경도
	
	ParentDAO parentDAO = new ParentDAO();
	int result = parentDAO.setParentGPS(id, lati, longi);
	
	String returns = "";
	
	if(result == 1) {
		returns = "SetGPSSuccess";
	} else if(result == 0) {
		returns = "NoID";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>