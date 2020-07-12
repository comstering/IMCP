<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String name = request.getParameter("name");    //  부모 이름
	String email = request.getParameter("email");    //  이메일
	
	ParentDAO parentDAO = new ParentDAO();

	out.clear();
	out.print(parentDAO.findID(name, email));
	out.flush();
%>