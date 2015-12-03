<%-- Copyright - The University of Edinburgh 2015 --%>
<%@ page
	import="uk.ac.ed.epcc.webapp.*, uk.ac.ed.epcc.webapp.model.*,uk.ac.ed.epcc.safe.accounting.*"%>
<%@ include file="/session.jsf"%>
<%
	String page_title = service_name+" Property Information";
    String producer = request.getParameter("producer");
    AccountingService accounting_service = conn.getService(AccountingService.class);
    UsageProducer usage_producer;
    if( producer == null ){
    	usage_producer = accounting_service.getUsageManager();
    }else{
    	usage_producer = accounting_service.getUsageManager(producer);
    }
%>
<%@ include file="/std_header.jsf"%>
<%@ include file="/back.jsf"%>
<%
  if( usage_producer == null ){
%>
  <div class="block">
  <h2>No Usage Producer</h2>
  <p>
  no accounting tables are defined for the requested accounting set.
  </p>
  </div>
<% }else{ 
  PropertyInfoMaker info = new PropertyInfoMaker(usage_producer);
  HtmlBuilder hb = new HtmlBuilder();
  hb.addTable(conn, info.getTable());
%>
 <div class="block">
 <h2>Property Information</h2>
 <%=hb.toString() %>
 </div>
<% 

} %>
<%@ include file="/std_footer.jsf"%>