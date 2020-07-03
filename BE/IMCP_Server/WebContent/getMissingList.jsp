<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Missing.MissingDAO" %>

<%
	request.setCharacterEncoding("UTF-8");
	
	MissingDAO missingDAO = new MissingDAO();
	
	out.clear();
	out.println(missingDAO.getMissingList());
	out.flush();
%>