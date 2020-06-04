<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.MemberDAO" %>
<%@ page import="User.MemberDTO" %>
<%
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");	//	부모 id
 		String password = request.getParameter("password");	//	부모 이름
		String returns = "";
 		
		MemberDTO memDTO = new MemberDTO();
 		memDTO.setId(id);
 		memDTO.setPassword(password);
		
 		MemberDAO memDAO = new MemberDAO(memDTO);
		returns = memDAO.memberChangePW();
		
		out.clear();
		out.print(returns);
		out.flush();
%>