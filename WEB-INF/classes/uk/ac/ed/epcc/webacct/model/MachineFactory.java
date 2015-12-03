// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.model;

import uk.ac.ed.epcc.safe.accounting.db.AccountingClassificationFactory;
import uk.ac.ed.epcc.webapp.AppContext;
import uk.ac.ed.epcc.webapp.jdbc.table.StringFieldType;
import uk.ac.ed.epcc.webapp.jdbc.table.TableSpecification;
import uk.ac.ed.epcc.webapp.model.data.DataObject;
import uk.ac.ed.epcc.webapp.model.data.Exceptions.DataFault;
import uk.ac.ed.epcc.webapp.model.data.Repository.Record;

public class MachineFactory extends AccountingClassificationFactory<Machine>  {

	public MachineFactory(AppContext conn){
    	super(conn,"Machine");
    }
	public MachineFactory(AppContext conn, String table){
		super(conn,table);
	}
	@Override
	public TableSpecification getDefaultTableSpecification(AppContext c,String table) {
		TableSpecification s = super.getDefaultTableSpecification(c,table);
		s.setField(Machine.ACCOUNTING_TABLE, new StringFieldType(true, null, c.getIntegerParameter("machine.table.length", 64)));
		return s;
	}
	@Override
	protected DataObject makeBDO(Record res) throws DataFault {
		return new Machine(this,res);
	}
	
}