<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.MemberDAO"%>
<%@ page import="User.MemberDTO"%>
<%
		request.setCharacterEncoding("UTF-8");
		String id = request.getParameter("id");	//	부모 id
 		String password = request.getParameter("password");	//	부모 password
 		String name = request.getParameter("name");	//	부모 이름
 		String phone = request.getParameter("phone");	//	부모 핸드폰 번호
 		String email = request.getParameter("email");	//	부모 이메일 주소
		String returns = "";
 		
		MemberDTO memDTO = new MemberDTO();
		memDTO.setId(id);
		memDTO.setPassword(password);
 		memDTO.setName(name);
 		memDTO.setPhone(phone);
 		memDTO.setEmail(email);
 		
		MemberDAO memDAO = new MemberDAO(memDTO);
		returns = memDAO.memberJoin();
		out.clear();
		out.print(returns);
		out.flush();
%>