<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="User.ChildDTO" %>
<%@ page import="User.ChildDAO" %>
<%@ page import="java.util.ArrayList" %>

<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("Key");
	ChildDAO childDAO = new ChildDAO();
	ChildDTO child = childDAO.getChildInfo(childKey);
	StringBuilder info = new StringBuilder("{" + child.getImgRealname() + "," + child.getName() + "," + child.getBirth() + "}");
	
	out.println(info);
%>