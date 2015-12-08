<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>BV - Email Verification</title>
</head>
<body>

<form action="verifyEmail" method="get">
	<h4>Hare Krishna Devotee,</h4>
	<br> Welcome to Bhakthi Vriksha
	<br></br>All Glories To Srila Prabhupada.
	</br>All Glories To Sri Guru and Gauranga.
	</br>
	</br>Thanks for showing interest to experience spiritual bliss and gain the
	<b>Ultimate Knowledge.</b>
	</br>
	</br>
		Please press the Verify button below to complete the activation.
		<input type=hidden name="vCode" value="<%= request.getParameter("vCode")%>">
		<input type=hidden name="email" value="<%= request.getParameter("email")%>">
		</br>
		<input type="submit" value="Verify"/>
		
	 </br></br>
	<b>Please Chant Hare Krsna Maha Mantra and Be Happy,</b>
	<br>Bhakti Vriksha Team
	</form>
</body>
</html>