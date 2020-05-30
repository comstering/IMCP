<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.MemberDAO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");	//	부모 id
 		String password = request.getParameter("password");	//	부모 password
 		String name = request.getParameter("name");	//	부모 이름
 		String phone = request.getParameter("phone");	//	부모 핸드폰 번호
 		String email = request.getParameter("email");	//	부모 이메일 주소
		String returns = "";
 		
		MemberDAO memDAO = new MemberDAO();
		returns = memDAO.memberJoin(id, password, name, phone, email);
		
		out.clear();
		out.print(returns);
		out.flush();
%>