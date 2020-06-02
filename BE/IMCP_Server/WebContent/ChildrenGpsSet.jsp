<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="DBConnect.*"%>
<%@ page import="User.ChildrenDAO"%>
<%@ page import="User.ChildrenDTO"%>
<%
		request.setCharacterEncoding("UTF-8");
		double latitude = Double.parseDouble(request.getParameter("latitude")); //	위도
		double longtitude = Double.parseDouble(request.getParameter("longtitude")); // 경도
		String childKey = request.getParameter("childKey"); //	아이 고유키(식별키)
		String returns = "";
	
		ChildrenDTO childDTO = new ChildrenDTO();
		childDTO.setLatitude(latitude);
		childDTO.setLongtitude(longtitude);
		childDTO.setChildKey(childKey);
		
		ChildrenDAO childDAO = new ChildrenDAO(childDTO);
		returns = childDAO.childrenGPSSet();
	
		out.clear();
		out.print(returns);
		out.flush();
%>
