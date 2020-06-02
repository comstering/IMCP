<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ChildrenDAO"%>
<%@ page import="User.ChildrenDTO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String key = request.getParameter("key");	//	아이 고유키(식별키)
 		String password = request.getParameter("password");	//	고유키 확인 password
		String returns = "";
 		
		ChildrenDTO childDTO = new ChildrenDTO();
		childDTO.setKey(key);
		childDTO.setPassword(password);
		
		ChildrenDAO childDAO = new ChildrenDAO(childDTO);
		returns = childDAO.childrenLogin();
		
		out.clear();
		out.print(returns);
		out.flush();
%>
