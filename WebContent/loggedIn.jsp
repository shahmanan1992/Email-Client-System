<%-- Importing required classes and packages --%>
<%@page import="Content.EmailClient"%>
<%@page import="java.util.*"%>
<%@page import="Content.Mail"%>
<%@page import="Content.Header"%>
<%@page import="Content.Attachment"%>
<%@page import="Content.MailServer"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
    
    
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Mail Client</title>
<link href='http://fonts.googleapis.com/css?family=Oswald' rel='stylesheet' type='text/css'>
<link href='http://fonts.googleapis.com/css?family=Oswald|Pontano+Sans' rel='stylesheet' type='text/css'>
<link href="css/style.css" rel="stylesheet" type="text/css" />
<link href="css/email.css" rel="stylesheet" type="text/css" />
</head>
<body>

<%-- Loads new Data on each inbox or sent message refresh --%>
<% HttpSession current_js_session=request.getSession(true);
	   EmailClient ec= (EmailClient)current_js_session.getAttribute("client");
	   ec.refresh(); 
	   current_js_session.setAttribute("Received", ec.getInbox());
	   current_js_session.setAttribute("Sent", ec.getSentMails());	
	%>

<script type="text/javascript">
<%-- Function to reflect the contents according to choice (inbox/sent messages) --%>
function changeContent(from,date,subject,message)
{
	document.getElementById("material").innerHTML="<div class=\"contentMail\">" + "<b>From:</b>"+from+"<br/>" + "<b>Date:</b>" +date+"<br/>"+ "<b>Subject:</b>"+subject+"<br/>"+ "<b>Message:</b>"+message+"</div>";
	
}

<%-- Function to relfect changes for attachment files --%>
function changeContentWithAttachment(from,date,subject,message,fileContent,title,type)
{
	
	document.getElementById("material").innerHTML="<div class=\"contentMail\">" + "<b>From:</b> "+from+"<br/>  <b>Date:</b> "+date+"<br/> <b>Subject:</b> "+subject+"<br/> <b>Message:</b> "+message+"<br/>"+"<b>Attachment: </b><input type=\"hidden\" name=\"fileTitle\"><a href=\"#\" onclick=\"javacsript:downloadContent()\">"+title+"</a></div>";
	document.getElementById("file_data").innerHTML=fileContent;
	document.getElementById("file_title").innerHTML=title;
}

<%-- Function to download content on receiver or sender's end --%>
function downloadContent()
{	
	window.alert("File downloaded successfully");
	document.location.href = 'downloadFileData?file_title='+document.getElementById("file_title").innerHTML+'&file_data='+document.getElementById("file_data");

}

function sentContent()
{
	var m='';
	<% 	
		ArrayList<Mail> values1=(ArrayList<Mail>)current_js_session.getAttribute("Sent"); 
	for(int i=values1.size()-1;i>=0;i--){
		Mail v=values1.get(i);
		Header h=v.getHeader();
		 if(!v.isIfAttachment()) { %>	
 		m+="<div class=\"contentMail\"><a href=\"#\" onclick=\"changeContent('<%= h.getFrom()%>','<%= h.getDate() %>','<%=h.getSubject()%>','<%=new String(v.getMessage())%>')\">"+ '<%= h.getSubject()%>'+"</a>"+"<span style=\"float:right\">"+'<%= h.getDate() %>'+"</span></div>";
		<% }else { %>
		m+="<div class=\"contentMail\"><a href=\"#\" onclick=\"changeContentWithAttachment('<%= h.getFrom()%>','<%= h.getDate() %>','<%=h.getSubject()%>','<%=new String(v.getMessage())%>','<%=v.getAttachment().getAttachment()%>','<%=v.getAttachment().getTitle()%>','<%=v.getAttachment().getType()%>')\">"+ '<%= h.getSubject()%>'+"</a>"+"<span style=\"float:right\">"+'<%= h.getDate() %>'+"</span></div>";
		<% } %>
	<% } %>	
	
	// Displaying content of sent Mails
	document.getElementById("inboxMessage").innerHTML="<h2>Sent Messages</h2>";
	document.getElementById("material").innerHTML=m;
}


function currentPage()
{
	
	   document.getElementById("inboxMessage").innerHTML="<h2>Inbox Messages</h2>";
	   location.href='loggedIn.jsp';
}

<%-- Custom page displayed for composing message --%>
function customalert()
{
	var w=window.innerWidth;
	var h=window.innerHeight;
	var dialogoverlay=document.getElementById("login");
	var dialog=document.getElementById("dialogBox");
	dialogoverlay.style.display="block";
	dialog.style.left=(w/2)-(550*0.5)+"px";
	dialog.style.display="block";
	dialog.style.top="120px";
	headerMessage.style.left=(w/2)-(550*0.5)+"px";
	headerMessage.style.display="block";
	headerMessage.style.top="80px";
	
}


function display()
{
	document.getElementById("login").style.display="none";
}
</script>


<div id="login">
<div id="headerMessage">
New Message<button id="closeButton" onclick="javascript:display()"><img src="img/close.PNG" width="18" height="15px"></button>
</div>
<div id="dialogBox">
	 <form action="emailResponse" method="POST">
                <input type="text" name="toSend"  placeholder="To: "/>
                <input type="text" name="cc"  placeholder="CC:" />
                <input type="text" name="subject"  placeholder="Subject:" />
                <textarea rows="5" name="message" placeholder="Enter Message Here"></textarea>
                <input type="file" maxlength="50" name="attachment" />
                <input type="submit" style="width:535px;" value="Submit" name="submit" />
     </form>   
</div>    
</div>


 
<div class="header-wrap">
<% 	String user=ec.getUsername().split("@")[0];
	String split_user[]=user.split("\\.");%>
<span id="welcome">Welcome <%= split_user[0].toUpperCase() %></span>
<span id="file_data" style="display:none;"></span>
<span id="file_title" style="display:none;"></span>
  <div class="header">
    <h1>Web Client</h1>
  </div>
  <span id="logout"><a href="Homepage.jsp" style="color:black;">Logout</a></span>
</div>

<div class="clearing"></div>
<!-- end of panel wrapper -->
<div class="page-wrapper ">

  <div class="sidebar floatLeft">
    <div class="panel">
      <div class="title">
        <ul>
          <li><button class="click" onclick="javascript:customalert()">Compose Email</button></li>
          <li><a href="javascript:currentPage()">Inbox</a></li>
          <li><a href="javascript:sentContent()">Sent</a></li>
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
        <h2>Inbox Messages</h2>      
      </div>
      <div class="content" id="material">
      <%  HttpSession sess=request.getSession(true); 
 			ArrayList<Mail> values=(ArrayList<Mail>)sess.getAttribute("Received"); 
			for(int i=values.size()-1;i>=0;i--)
			{ 
				Mail v=values.get(i);
				Header h=v.getHeader();
			%>
     <div class="contentMail">	
     <% if(!v.isIfAttachment()) { %>
    <a href="#" onclick="javascript:changeContent('<%= h.getFrom()%>','<%= h.getDate()%>','<%= h.getSubject()%>','<%= new String(v.getMessage())%>')"> <%= h.getSubject() %></a>  
      <span style="float:right"><%= h.getDate() %></span>
      <% }else {%>
     	<a href="#" onclick="javascript:changeContentWithAttachment('<%= h.getFrom()%>','<%= h.getDate()%>','<%= h.getSubject()%>','<%= new String(v.getMessage())%>','<%=v.getAttachment().getAttachment()%>','<%=v.getAttachment().getTitle()%>','<%=v.getAttachment().getType()%>')"> <%= h.getSubject() %></a>  
      <span style="float:right"><%= h.getDate() %></span>
      <% } %>
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
