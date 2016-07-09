<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<link href='http://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald|Pontano+Sans' rel='stylesheet' type='text/css'>
<link href="css/style.css" rel="stylesheet" type="text/css" />
</head>
<body>
<script type="text/javascript">
function changeContent()
{
	document.getElementById("material").innerHTML="Hello world";
	var a=document.getElementById("inboxMessage").innerHTML;
	document.getElementById("inboxMessage").innerHTML=a+"<button onclick=\"login.jsp\">Back</button>";
}

</script>


<div class="header-wrap">
  <div class="header">
    <h1>Web Client</h1>
  </div>

</div>

<div class="clearing"></div>
<!-- end of panel wrapper -->
<div class="page-wrapper ">

  <div class="sidebar floatLeft">
    <div class="panel">
      <div class="title">
        <ul>
          <li><button class="click" onclick="">Compose Email</button></li>
          <li>Inbox</li>
          <li>Sent</li>
        </ul>
      </div>
    </div>
  </div>

  <div class="primary-content floatLeft">
    <div class="panel">
      <div class="title">
        <input type="text" class="search-text-field" placeholder="Search"/>
      </div>
    </div>

    <div class="panel clearing">
      <div class="content" id="inboxMessage">
        <h2>Inbox messages</h2>
        
      </div>
      <div class="content" id="material">
      <% for(int j=0;j<10;j++) {%>
     <div class="contentMail">
   
      <%= (Integer)request.getAttribute("I") %>
      
      </div> 	
        <% } %>
      </div>
    </div>																																																								
  </div>
  <!-- end of Primary content -->

  <!-- end of Sidebar -->
</div>
</body>
</html>
