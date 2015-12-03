// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.model;

import java.util.HashMap;
import java.util.Map;

import uk.ac.ed.epcc.safe.accounting.allocations.AllocationTableCreator;
import uk.ac.ed.epcc.safe.accounting.db.transitions.AccountingTableCreator;
import uk.ac.ed.epcc.safe.accounting.db.transitions.ClassificationTableCreator;
import uk.ac.ed.epcc.safe.accounting.model.PropertyPersonFactory;
import uk.ac.ed.epcc.safe.accounting.model.ReportTemplate;
import uk.ac.ed.epcc.safe.accounting.model.ReportTemplateFactory;
import uk.ac.ed.epcc.webapp.AppContext;
import uk.ac.ed.epcc.webapp.Contexed;
import uk.ac.ed.epcc.webapp.Indexed;
import uk.ac.ed.epcc.webapp.forms.registry.FormEntry;
import uk.ac.ed.epcc.webapp.forms.registry.FormFactoryProvider;
import uk.ac.ed.epcc.webapp.forms.registry.FormPolicy;
import uk.ac.ed.epcc.webapp.forms.registry.FormRegistry;
import uk.ac.ed.epcc.webapp.model.Property;
import uk.ac.ed.epcc.webapp.model.PropertyFactory;
import uk.ac.ed.epcc.webapp.model.TextFileOverlay;
import uk.ac.ed.epcc.webapp.model.data.DataObject;
import uk.ac.ed.epcc.webapp.model.data.DataObjectFactory;
import uk.ac.ed.epcc.webapp.model.data.forms.registry.DataObjectFormEntry;
import uk.ac.ed.epcc.webapp.model.data.forms.registry.DynamicFormEntry;
import uk.ac.ed.epcc.webapp.model.data.forms.registry.IndexedFormEntry;
import uk.ac.ed.epcc.webapp.model.data.reference.IndexedProducer;
import uk.ac.ed.epcc.webapp.model.relationship.RelationshipTableCreator;
import uk.ac.ed.epcc.webapp.session.SessionService;

@SuppressWarnings("unchecked")
public class AdminFormRegistry extends FormRegistry {
	 public AdminFormRegistry(AppContext conn) {
		super(conn);
	}
	public static final String TAG="Form";
	private static final Map<String,FormFactoryProvider> map = new HashMap<String,FormFactoryProvider>();
	@Override
	public String getGroup() {
		return TAG;
	}

	@Override
	protected Map<String, FormFactoryProvider> getMap() {
		return map;
	}

	@Override
	public String getTitle() {
		return "Admin";
	}
	public static class FormType<F extends IndexedProducer<T>&Contexed,T extends Indexed> extends IndexedFormEntry<F,T>{

		protected FormType(String name, Class<? extends F> c,
				FormPolicy policy) {
			super(name, c, policy);
		}
        protected FormType(String name, Class<? extends F> c,
        		boolean create, boolean update){
        	super(name,c,new AdminPolicy(create,update));
        }
		@Override
		protected void register(String tag) {
			map.put(tag,this);
		}
    	
    }
	public static class CreateType<F extends Contexed,T> extends FormEntry<F,T>{

		protected CreateType(String name, Class<? extends F> c) {
			super(name, c, new AdminPolicy(true,false));
        }
		@Override
		protected void register(String tag) {
			map.put(tag,this);
		}
		public String getID(T target) {
			// Not needed for just create
			return null;
		}
		public T getTarget(AppContext c, String id) {
			// not needed for just crate
			return null;
		}
    	
    }
	public static class DataObjectType<T extends DataObject> extends DataObjectFormEntry<T>{

		protected DataObjectType(String name, Class<? extends DataObjectFactory<T>> c,
				FormPolicy policy) {
			super(name, c, policy);
		}
        protected DataObjectType(String name, Class<? extends DataObjectFactory<T>> c,
        		boolean create, boolean update){
        	super(name,c,new AdminPolicy(create,update));
        }
		@Override
		protected void register(String tag) {
			map.put(tag,this);
		}
    	
    }
	public static class DynamicType<T extends DataObject> extends DynamicFormEntry<T>{

		protected DynamicType(String name, String tag, FormPolicy policy) {
			super(name, tag, policy);
		
		}
		protected DynamicType(String name,String tag,boolean create, boolean update){
			super(name,tag,new AdminPolicy(create, update));
		}

		@Override
		protected void register(String tag) {
			map.put(tag,this);
		}
		
	}
	public static class AdminPolicy implements FormPolicy{
        final boolean can_create;
        final boolean can_update;
        public AdminPolicy(boolean create, boolean update){
        	can_create=create;
        	can_update=update;
        }
		public boolean canCreate(SessionService p) {
			return can_create && p.hasRole(SessionService.ADMIN_ROLE);
		}

		public boolean canUpdate(SessionService p) {
			return can_update && p.hasRole(SessionService.ADMIN_ROLE);
		}
		
	}
	// if used machine should turn up in the Classification heirarchy.
	//public static final DataObjectType<Machine> MACHINE_TYPE = new DataObjectType<Machine>("Machine",MachineFactory.class,true,true);
	public static final DataObjectType<Property> PROPERTY_TYPE = new DataObjectType<Property>("DataBase Property",PropertyFactory.class,true,true);
	public static final DataObjectType REPORT_TEMPLATE_TYPE = new DataObjectType("Report Listing",ReportTemplateFactory.class,true,true);
    public static final DynamicType TEMPLATE_OVERLAY_TYPE = new DynamicType("Report template", "ReportTemplateOverlay", true, true);
	public static final DataObjectType OVERLAY_TYPE = new DataObjectType("Schema/Stylesheet override",TextFileOverlay.class,true,true);
    public static final DataObjectType PERSON_TYPE = new DataObjectType("Person", PropertyPersonFactory.class,false,true);

	public static final FormType ROLE_TYPE = new FormType("Persons Roles", RoleUpdate.class,false,true);
    public static final CreateType ACCOUNTING_TABLE_TYPE = new CreateType("Accounting Table", AccountingTableCreator.class);
    public static final CreateType CLASSISIER_TABLE_TYPE = new CreateType("Classifier Table", ClassificationTableCreator.class);
    public static final CreateType ALLOCATION_TABLE_TYPE = new CreateType("Allocation Table", AllocationTableCreator.class);
    public static final CreateType RELATIONSHIP_TABLE_TYPE = new CreateType("Relationship Table", RelationshipTableCreator.class);
}