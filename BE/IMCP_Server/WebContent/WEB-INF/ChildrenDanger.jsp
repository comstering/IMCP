<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ChildrenDAO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
		String returns = "";
	
		ChildrenDAO childDAO = new ChildrenDAO();
		returns = childDAO.childrenDanger(childKey);
	
		out.clear();
		out.print(returns);
		out.flush();
%>
