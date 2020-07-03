<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  부모 아이디
	String name = request.getParameter("name");    //  이름
	String email = request.getParameter("email");    //  이메일
	
	ParentDAO parentDAO = new ParentDAO();
	String returns = parentDAO.findPW(id, name, email);

	out.clear();
	out.print(returns);
	out.flush();
%>
