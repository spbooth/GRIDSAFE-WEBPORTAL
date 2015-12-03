// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.model;



import java.util.LinkedHashSet;
import java.util.Set;

import uk.ac.ed.epcc.safe.accounting.reports.ReportBuilder;
import uk.ac.ed.epcc.webapp.AppContext;
import uk.ac.ed.epcc.webapp.session.AppUser;


public  class RoleUpdate<T extends AppUser> extends uk.ac.ed.epcc.webapp.session.RoleUpdate<T>{
   
	public RoleUpdate(AppContext conn){
    	super(conn);
    }
	@Override
	public Set<String> getRoles() {
		Set<String> roles = new LinkedHashSet<String>();
		for(String role : getContext().getInitParameter("role_list", "Admin,"+ReportBuilder.REPORT_DEVELOPER).split(",")){
			roles.add(role);
		}
		return roles;
	}

	
}