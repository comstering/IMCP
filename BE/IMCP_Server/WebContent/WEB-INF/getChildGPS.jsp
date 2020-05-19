<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="User.ChildDTO" %>
<%@ page import="User.ChildDAO" %>
<%@ page import="java.util.ArrayList" %>

<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("Key");
	ChildDAO childDAO = new ChildDAO();
	double[] gps = childDAO.getChildGPS(childKey);
	StringBuilder gpsInfo = new StringBuilder("{" + gps[0] + "," + gps[1] + "}");
	
	out.println(gpsInfo);
%>