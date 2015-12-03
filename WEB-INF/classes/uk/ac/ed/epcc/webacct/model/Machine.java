// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.model;

import uk.ac.ed.epcc.safe.accounting.db.AccountingClassification;
import uk.ac.ed.epcc.safe.accounting.db.ConfigUsageRecordFactory;
import uk.ac.ed.epcc.safe.accounting.db.ParseUsageRecordFactory;
import uk.ac.ed.epcc.webapp.model.data.Repository.Record;

public class Machine extends AccountingClassification {

	public static final String ACCOUNTING_TABLE = "Table";

	protected Machine(MachineFactory fac,Record r) {
		super(fac,r);
	}
	
	public ParseUsageRecordFactory getUsageFactory(){
		String table = record.getStringProperty(ACCOUNTING_TABLE);
		if( table == null ){
			return null;
		}
		ParseUsageRecordFactory res =  new ConfigUsageRecordFactory(getContext(),table);
		if( res.isValid()){
			return res;
		}
		return null;
	}

}