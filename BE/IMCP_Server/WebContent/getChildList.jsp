<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="User.ChildDTO" %>
<%@ page import="User.ChildDAO" %>
<%@ page import="java.util.ArrayList" %>

<%
	request.setCharacterEncoding("UTF-8");
	String parentID = request.getParameter("ID");
	ChildDAO childDAO = new ChildDAO();
	ArrayList<ChildDTO> childList = childDAO.getChildList(parentID);
	StringBuilder child = new StringBuilder("[");
	for(int i = 0; i < childList.size(); i++) {
		child.append("{\"childKey\":"+ childList.get(i).getChildKey() + ",\"name\":"+ childList.get(i).getName()
			+ ",\"img\":" + childList.get(i).getImgRealname() + "},");
	}
	child.deleteCharAt(child.length() - 1);
	child.append("]");
	
	out.println(child);
%>