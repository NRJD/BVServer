<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<body>
    <form action="mobileReq" method="post">
 
        Name:<input type="text" name="name" maxlength="50"/><br/>
        Password:<input type="password" name="password" maxlength="16"/><br/>
        Email Id:<input type="text" name="email" maxlength="50"/><br/>
        Phone Number:<input type="text" name="phoneNum" maxlength="12"/><br/>
        Language: <select name="language">
            <option>Hindi</option>
            <option>English</option>
            <option>French</option>
        </select> <br/>
        <input type="submit" value="Submit"/>
 
    </form>
</body>
</html>
