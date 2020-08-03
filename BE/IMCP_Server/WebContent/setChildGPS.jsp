<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Child.ChildDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("childKey");    //  아이 식별값
	double lati = Double.parseDouble(request.getParameter("lati"));    //  위도
	double longi = Double.parseDouble(request.getParameter("longi"));    //  경도
	
	ChildDAO childDAO = new ChildDAO();
	int result = childDAO.setChildGPS(childKey, lati, longi);
	
	String returns = "";
	
	if(result == 1) {
		childDAO.checkChildDanger(childKey);
		returns = "SetGPSSuccess";
	} else if(result == 0) {
		returns = "NoChildInfo";
	} else if(result == -1) {
		returns = "DBError";
	}

	out.clear();
	out.print(returns);
	out.flush();
%>