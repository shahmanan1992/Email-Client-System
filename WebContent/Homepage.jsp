<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Login</title>
<link rel="stylesheet" type="text/css" href="css/home.css">
</head>


<body style="background-image:url('img/loginback.png')">

<%-- Login system --%>
<div id="login">
    <div class="fixed_width">
        <p>Login to your account</p>
        <% HttpSession sees=request.getSession(true); %>
        <% if(sees.getAttribute("Error") != null){ %>
        <span id="errorMessage"><%= sees.getAttribute("Error") %></span>
        <% } 
        	sees.removeAttribute("Error");%>
        <div class="information">
            <form action="ClientSide" method="POST">
                <p><input type="text" name="username"  placeholder="Username"/></p>
                <p><input type="password" name="pass"  placeholder="Password" /></p>
                <div id="submit">
                    <input type="submit" value="Submit" name="submit" />
                </div>
            </form>
        </div>

    </div>
</div>

</body>
</html>