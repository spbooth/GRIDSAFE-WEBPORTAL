<%-- Copyright - The University of Edinburgh 2015 --%>
<%--
Update personal details

--%>
<%@ page	
   import="java.util.*, 
   uk.ac.ed.epcc.webapp.*, 
   uk.ac.ed.epcc.webapp.forms.*,
   uk.ac.ed.epcc.webapp.model.*,
   uk.ac.ed.epcc.webapp.model.data.forms.*,
   uk.ac.ed.epcc.webapp.forms.factory.*,
   uk.ac.ed.epcc.webapp.forms.html.*"
%>
<%@ include file="/session.jsf" %>
<%	
	String page_title = service_name+" Update Personal Details";

%>
<%@ include file="/std_header.jsf" %>
<%@ include file="/back.jsf"%>
<%
   AppUserFactory fac =  session_service.getLoginFactory();
   StandAloneFormUpdate<AppUser> u = (StandAloneFormUpdate<AppUser>) fac.getFormUpdate(conn);
   HTMLForm form = new HTMLForm(conn);
   u.buildUpdateForm("User",form,session_service.getCurrentPerson());
   
%>
<%@ include file="/scripts/form_context.jsf" %>

<div class="block">
<h2>This page is to allow you to update your contact details.</h2>

<h3>Your current details:</h3>
  <form method="post" action="<%= response.encodeURL(web_path+"/UserServlet") %>">
	<input type="hidden" name="form_url" value="/personal_update.jsp"/>
	<input type="hidden" name="action" value="MODIFY_PERSON"/>
          <%= form.getHtmlFieldTable(request) %>
     <div class="action_buttons">
	    <input class="input_button" type="submit" value=" Commit Update "/>
	 </div>
  </form>
</div>


<%@ include file="/std_footer.jsf" %>