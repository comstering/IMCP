<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="User.ChildDTO" %>
<%@ page import="User.ChildDAO" %>
<%@ page import="java.util.ArrayList" %>

<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("Key");
	double lati = Double.parseDouble(request.getParameter("lati"));
	double longi = Double.parseDouble(request.getParameter("longi"));
	ChildDAO childDAO = new ChildDAO();
	
	int result = childDAO.setChildGPS(childKey, lati, longi);
	
	out.clear();
	out.println(result);
	out.flush();
%>