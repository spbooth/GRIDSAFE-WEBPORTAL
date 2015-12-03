<%-- Copyright - The University of Edinburgh 2015 --%>
<%@ page
	import="uk.ac.ed.epcc.webapp.*, uk.ac.ed.epcc.webapp.model.*,uk.ac.ed.epcc.webacct.model.AdminFormRegistry"%>
<%@ include file="/session.jsf"%>
<%
	String page_title = service_name+" Main";
%>
<%@ include file="/std_header.jsf"%>
<!-- $Id: main.jsp,v 1.32 2015/11/27 17:17:35 spb Exp $ -->
<div class="block">
	<h2>Main page</h2>
</div>
<%@ include file="/logged_in.jsf" %>
<%@ include file="/scripts/toggle_roles.jsf" %>

<div class="block">
	<h2>Introduction</h2>
	<p>This is the <%=service_name%> accounting portal. This system allows you to generate reports 
	from the accounting database.


</div>


<div class="block">
	<h2>Reports</h2>
	<p>These are the reports that you may current run on the accounting database.</p>
	<%@ include file="accounting/reports.jsf"%>
	
</div>

<%
	if ( session_service.hasRole(SessionService.ADMIN_ROLE) ){
%>
<div class="block">
<h2>Admin links</h2>
<p> the following links are only available to  administrators</p>
	<P>Here is a list of <A HREF="<%=response.encodeURL(web_path+"/accounting/accounting_properties.jsp")%>">Accounting 
	Properties</A> that can be used when writing reports.</P>
	<p>Here are the forms for modifying <A HREF="<%=response.encodeURL(web_path+"/tables/index.jsp")%>">Database tables</A></p>	
</div>
<%
	}
%>
<%
if( session_service.canSU(null)){
%>
<div class="block">
<h2>Become a different user</h2>
<p>You have permission to <A HREF="<%=response.encodeURL(web_path+"/scripts/become_user.jsp")%>">become a different user</A>.</p>
</div>
<%
}
%>

<%
	if ( session_service.hasRole("AllocationAdmin") ){
%>
<div class="block">
<h2>Manage Allocations</h2>
<p> Allocations can be managed from the <A HREF="<%= response.encodeURL(web_path+"/allocations/index.jsp") %>">allocation page</A></p>
</div>
<%} %>
<%@ include file="/scripts/registry_list.jsf"%>


<%@ include file="/std_footer.jsf"%>