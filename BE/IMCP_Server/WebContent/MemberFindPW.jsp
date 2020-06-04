<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.MemberDAO" %>
<%@ page import="User.MemberDTO" %>
<%
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");	//	부모 id
 		String name = request.getParameter("name");	//	부모 이름
 		String email = request.getParameter("email");	//	부모 이메일 주소
		String returns = "";
 		
		MemberDTO memDTO = new MemberDTO();
		memDTO.setId(id);
 		memDTO.setName(name);
 		memDTO.setEmail(email);
 		
		MemberDAO memDAO = new MemberDAO(memDTO);
		returns = memDAO.memberFindPW();
		
		out.clear();
		out.print(returns);
		out.flush();
%>