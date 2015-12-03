<%-- Copyright - The University of Edinburgh 2015 --%>
<%-- 
    password_update.jsp - Page used to change password.


   
--%>
<%@ page	
   import="uk.ac.ed.epcc.webapp.*, uk.ac.ed.epcc.webapp.model.*"
%>
<%@ include file="/basic_session.jsf" %>
<%	
	String page_title = service_name+" Change Password";
%>
<%@ include file="/std_header.jsf" %>
<% if("bad_password".equals(request.getParameter("error"))) { %>
<div class="block">
<p class="warn">
Please enter your current valid password.
</p>
</div>
<% } %>
<% if("password_mismatch".equals(request.getParameter("error"))) { %>
<div class="block">
<p class="warn">
New passwords do not match.<br/><br/>
	  Please type your new password properly in both boxes.
</p>
</div>
<% } %>
<% if("short_password".equals(request.getParameter("error"))) { %>
<div class="block">
<p class="warn">
Your new password is too short.<br/><br/>
	  Please enter a new password longer than 6 characters.
	  </p>
	  </div>
<% } %>

<div class="block">
<h2>Change Web Password</h2>

  <form method="post" action="<%= response.encodeURL(web_path+"/UserServlet") %>">
	<input type="hidden" name="action" value="CHANGE_PASSWORD"/>
    <table class="form">
	<tr>
	  <th>Current Password:</th>
	  <td>
		<input type="password" name="password" class="input" maxlength="32">
	  </td>
	</tr>

	<tr>
	
	  <th>New Password:</th>
	  <td>
		<input type="password" name="password1" class="input" maxlength="32">
	  </td>
	</tr>
	<tr>
	  
	  <th>New Password (again):</th>
	 <td>
		<input type="password" name="password2" class="input" maxlength="32">
	  </td>
	  
	</tr>
	</table>
    <div class="action_buttons">
	<input class="input_button" type="submit" value=" Change " name="submit"/>
	</div>
  </form>
</div>


<%@ include file="/std_footer.jsf" %>