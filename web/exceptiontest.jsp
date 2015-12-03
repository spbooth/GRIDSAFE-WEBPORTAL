<%-- Copyright - The University of Edinburgh 2015 --%>
<%-- 
	Throws various exceptions to test how well we report
	runtime errors

--%>
<%@ page 
%>
<%--    buffer="100kb" autoFlush="true" --%>
<%@ include file="/session.jsf" %>
<%
String page_title = "Exception testing";

String text = (String) request.getParameter("Exception");
String where = (String) request.getParameter("Where");

if( text != null && "top".equals(where) ){
  throw new Exception(text);
}

%>
<%@ include file="std_header.jsf" %>

<%@ include file="main__logged_in.jsf"%>


<div class="block">
<h2>Exception testing</h2>
<p>
	      This form is intended to throw test exceptions to allow 
	      testing of error detection and reporting.
	    </p>
	    <form>
	    <table class="form">
	
	 
		<tr>
		  <td> Exception text </td>
		  <td> 
		   <input type="text" name="Exception" maxlength="64" size="64" class="input" >
		  </td>		  
		</tr>
		<tr>
		  <td> Exception 'timing' </td>
		  <td>
		   <select name="Where">
		    <option value="top">At the top of the page.</option>
		    <option value="bottom">Near the bottom of the page.</option>
		   </select>
		  </td>		  
		</tr>
		</table>
	   <div class="action_buttons">
	    <input class="input_button" type="submit" value=" Throw ">
		</div>
 
      </form>
  </div>
<%
if( text != null && "bottom".equals(where) ){
  throw new Exception(text);
}
%>

<%@ include file="back.jsf" %>

<%@ include file="std_footer.jsf" %>