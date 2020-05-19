<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.MemberDAO" %>
<%
	request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");	//	부모 id
 		String password = request.getParameter("password");	//	부모 password
		String returns = "";
 		
		MemberDAO memDAO = new MemberDAO();
		returns = memDAO.memberLogin(id, password);
		
		out.clear();
		out.print(returns);
		out.flush();
%>
