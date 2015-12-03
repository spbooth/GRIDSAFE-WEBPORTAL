// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.model;

import java.util.Map;

import uk.ac.ed.epcc.safe.accounting.db.AccountingUpdater;
import uk.ac.ed.epcc.safe.accounting.db.UsageRecordParseTarget;
import uk.ac.ed.epcc.safe.accounting.properties.PropertyFinder;
import uk.ac.ed.epcc.safe.accounting.properties.PropertyMap;
import uk.ac.ed.epcc.safe.accounting.properties.PropertyTag;
import uk.ac.ed.epcc.safe.accounting.upload.UploadException;
import uk.ac.ed.epcc.safe.accounting.upload.UploadParser;
import uk.ac.ed.epcc.webapp.AppContext;
import uk.ac.ed.epcc.webapp.Contexed;

public class ReceiveAccountingUploadParser implements UploadParser, Contexed {
	private AppContext conn;
	public ReceiveAccountingUploadParser(AppContext c){
		this.conn=c;
	}
	@SuppressWarnings("unchecked")
	public String upload(Map<String, Object> parameters) throws UploadException {

		
        String machine = (String) parameters.get("machine");
        String update = (String) parameters.get("update");
       
        PropertyMap defaults = new PropertyMap();       
        
        MachineFactory mach_fac = new MachineFactory(conn);
        
        if( ! mach_fac.isValid() ){	
    		throw new UploadException("Machine table not valid");
        }
        Machine mach = mach_fac.findFromString(machine);
        if( mach == null ){
        	throw new UploadException("Machine "+machine+" not valid");
        }    	
		if( update == null ){
			throw new UploadException("No upload data");
		}
        UsageRecordParseTarget fac = mach.getUsageFactory();
        if( fac == null ){
        	throw new UploadException("Machine "+machine+" does not have an accounting table configured");
        }
        PropertyFinder finder = fac.getFinder();
        PropertyTag<?> machine_tag = finder.find("MachineName");
        if( machine_tag != null && machine_tag.allow(machine)){
        	 defaults.setProperty((PropertyTag<String>)machine_tag, machine);
        }
        boolean replace = (parameters.get("replace") != null);
        String result = new AccountingUpdater(conn,defaults,fac).receiveAccountingData( update, replace,false,false);
		
		return result;
	}

	public AppContext getContext() {
		return conn;
	}

}