<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form action="<%=request.getContextPath()%>/Search/list">
		<input type="text" name="keyWord" value="${keyWord }"> <input
			type="submit">
	</form>
	<div style="float:left">
		<ul>
			<c:forEach items="${map}" var="mymap">
				<li><a href="<%=request.getContextPath()%>/Search/catalog?url=${mymap.value}" >${mymap.key}</a></li>
			</c:forEach>
		</ul>
	</div>
	<div style="float:left">
		<ul>
			<c:forEach items="${lists}" var="list">
				<li><a href="${list.value}" >${list.key}</a></li>
			</c:forEach>
		</ul>
	</div>
</body>
<script type="text/javascript">
</script>
</html>