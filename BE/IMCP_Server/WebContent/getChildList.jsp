<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>

<%
	request.setCharacterEncoding("UTF-8");
	String parentID = request.getParameter("ID");    //  부모 아이디
	
	ParentDAO parentDAO = new ParentDAO();
	
	out.clear();
	out.println(parentDAO.getChildList(parentID));
	out.flush();
%>