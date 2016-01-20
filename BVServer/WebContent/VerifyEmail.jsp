<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bhakthi Vriksha app - Account Activation</title>
</head>
<body>

<form action="verifyEmail" method="get">
	<h4>Hare Krishna!</h4>
	Welcome to Bhakthi Vriksha app.
	<br>
	<br/>All Glories To Srila Prabhupada!
	<br/>All Glories To Sri Guru and Gauranga!
	<br/>
	<br/>Thanks for showing the interest in Bhakti Vriksha app to experience the spiritual bliss and gain the ultimate knowledge.
	<br/>
	<br/>Please press the Activate button below to complete the activation.
		<input type=hidden name="vCode" value="<%= request.getParameter("vCode")%>">
		<input type=hidden name="email" value="<%= request.getParameter("email")%>">
		<br/>
		<input type="submit" value="Activate"/>	
	<br/>
	<br/>
	<br/>Please Chant <b>Hare Krishna Maha Mantra</b> and Be Happy!
	<br/>
	<br/>Bhakti Vriksha Team
	</form>
</body>
</html>