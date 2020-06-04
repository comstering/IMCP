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
	
	String info = new String("{\"lati\":" + gps[0] + ",\"longi\":" + gps[1] + "}");
	
	out.clear();
	out.println(info);
	out.flush();
%>