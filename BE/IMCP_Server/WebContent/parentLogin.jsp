<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Parent.ParentDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
	String id = request.getParameter("ID");    //  부모 아이디
	String password = request.getParameter("password");    //  부모 비밀번호
	
	ParentDAO parentDAO = new ParentDAO();
	String returns = parentDAO.login(id, password);

	out.clear();
	out.print(returns);
	out.flush();
%>