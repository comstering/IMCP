<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="Child.ChildDAO"%>
<%
	request.setCharacterEncoding("UTF-8");
	String childKey = request.getParameter("childKey");    //  아이 고유키(식별키)
 	String password = request.getParameter("password");    //  고유키 확인 password
 	
 	ChildDAO childDAO = new ChildDAO();
	String returns = childDAO.childLogin(childKey, password);
	
	out.clear();
	out.print(returns);
	out.flush();
%>