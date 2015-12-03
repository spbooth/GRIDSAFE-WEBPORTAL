// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ed.epcc.safe.accounting.AccountingService;
import uk.ac.ed.epcc.safe.accounting.UsageManager;
import uk.ac.ed.epcc.safe.accounting.UsageProducer;
import uk.ac.ed.epcc.safe.accounting.charts.MapperEntry;
import uk.ac.ed.epcc.safe.accounting.charts.PlotEntry;
import uk.ac.ed.epcc.safe.accounting.properties.PropertyFinder;
import uk.ac.ed.epcc.safe.accounting.properties.PropertyTag;
import uk.ac.ed.epcc.safe.accounting.selector.AndRecordSelector;
import uk.ac.ed.epcc.safe.accounting.selector.SelectClause;
import uk.ac.ed.epcc.webapp.AppContext;
import uk.ac.ed.epcc.webapp.charts.Chart;
import uk.ac.ed.epcc.webapp.charts.PeriodChart;
import uk.ac.ed.epcc.webapp.charts.PieTimeChart;
import uk.ac.ed.epcc.webapp.charts.TimeChart;
import uk.ac.ed.epcc.webapp.content.HtmlBuilder;
import uk.ac.ed.epcc.webapp.content.HtmlPrinter;
import uk.ac.ed.epcc.webapp.content.Table;
import uk.ac.ed.epcc.webapp.content.TableXMLFormatter;
import uk.ac.ed.epcc.webapp.logging.Logger;
import uk.ac.ed.epcc.webapp.logging.LoggerService;
import uk.ac.ed.epcc.webapp.servlet.ServletService;
import uk.ac.ed.epcc.webapp.servlet.SessionServlet;
import uk.ac.ed.epcc.webapp.session.SessionService;

public class PlotServlet extends SessionServlet {

	@Override
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest req, HttpServletResponse res,
			AppContext conn, SessionService person) throws Exception {
		String path=req.getPathInfo();
		Logger log=conn.getService(LoggerService.class).getLogger(conn.getClass());

		log.debug("path is "+path);

		Map<String,Object> params = conn.getService(ServletService.class).getParams();
		AccountingService acc_serv = conn.getService(AccountingService.class);


		String type = (String) params.get("type");
		if( type == null ){
			type = "T";
		}
		String display = (String) params.get("display");
		if( display == null ){
			display = "G";
		}
		String table = (String) params.get("table");
		String map = (String) params.get("map");

		if( map == null )
		{
			map = "Project";
		}
		Calendar start = parseCalendar((String) params.get("start"));
		Calendar end = parseCalendar((String) params.get("end"));

		int field =parseInt((String) params.get("field"),Calendar.MONTH);

		int count = parseInt((String) params.get("count"),1);
		int major= parseInt((String) params.get("major"),5);
		int minor= parseInt((String) params.get("minor"),10);

		int max = parseInt((String) params.get("max"),10);
		boolean overlap = parseBoolean((String) params.get("overlap"),true);

		if( start == null ){
			// no start time step back from current time
			start = Calendar.getInstance();
			start.add(field, -(count*major));
		}
		if( end == null || end.before(start)){
			// bad end time add to start
			end = Calendar.getInstance();
			end.setTimeInMillis(start.getTimeInMillis());
			end.add(field,count*major);
		}
		UsageManager um = acc_serv.getUsageManager();
		UsageProducer ap = um.parseProducer(table);
		PropertyFinder finder = ap.getFinder();

		AndRecordSelector sel = new AndRecordSelector(); 
		for(String p : params.keySet()){
			try{
				PropertyTag t = finder.find(p);
				if( t != null ){
					sel.add(parseProp(conn,t,(String) params.get(p)));
				}
			}catch(Exception e){

			}
		}


		if( ! acc_serv.allow(person, sel)){
			res.sendError(HttpServletResponse.SC_FORBIDDEN,"User not allowed access to selected records");
			return;
		}


		MapperEntry entry=MapperEntry.parseMapSelector(map,MapperEntry.getMappers(conn,ap));

		PlotEntry plot_entry=PlotEntry.parseMapSelector(map,PlotEntry.getMappers(conn,ap));



		boolean graphical = display.equals("G");
		if( type.equals("P")){
			PieTimeChart tc = PieTimeChart.getInstance(conn,start,end);

			if(entry.plot(plot_entry,tc,ap,sel,max,overlap)){
				if( graphical){

					writePNG(res, tc);
				}else{
					outputTable(res, tc);
				}
			}else{
				res.sendError(HttpServletResponse.SC_NO_CONTENT, "Plot failed");
			}
		}else{
			if(major < 2  ){
				// timechart limitation
				major=2;
			}
			TimeChart tc = TimeChart.getInstance(conn,start,field,count,major,minor);

			if(entry.plot(graphical,plot_entry,tc,ap,sel,max,overlap)){
				if( graphical){
					writePNG(res,tc);
				}else{
					outputTable(res,tc);
				}
			}else{
				res.sendError(HttpServletResponse.SC_NO_CONTENT, "Plot failed");
			}

		}


		return;

	}
	protected void outputTable(HttpServletResponse res, PeriodChart tc)
			throws IOException {
		PrintWriter out = res.getWriter();
		Table t = tc.getTable();
		HtmlPrinter hb = new HtmlBuilder();
		TableXMLFormatter fmt = new TableXMLFormatter(hb, null,"auto");
		fmt.add(t);
		out.print(hb.toString());
	}
	protected void writePNG(HttpServletResponse res, Chart tc)
	throws Exception {
		res.setContentType("image/png");
		tc.getChartData().createPNG(res.getOutputStream());
	}
	@SuppressWarnings("unchecked")
	private <T> SelectClause<T> parseProp(AppContext c,PropertyTag<T> t, String value) throws Exception {
		//TODO could be more generic
		if( t.getTarget() == String.class){
			return new SelectClause<T>(t,(T)value);
		}
		if( t.getTarget().isAssignableFrom(Double.class) ){
			return new SelectClause<T>(t,(T) Double.valueOf(value));
		}
		if( t.getTarget().isAssignableFrom(Float.class) ){
			return new SelectClause<T>(t,(T) Float.valueOf(value));
		}
		if( t.getTarget().isAssignableFrom(Long.class) ){
			return new SelectClause<T>(t,(T) Long.valueOf(value));
		}
		if( t.getTarget().isAssignableFrom(Integer.class) ){
			return new SelectClause<T>(t,(T) Integer.valueOf(value));
		}
		return null;
	}
	/**
	 * @param string
	 * @param def 
	 * @return double
	 */
	private double parseDouble(String string, double def) {
		if( string == null ){
			return def;
		}
		return Double.parseDouble(string.trim());
	}

	/**
	 * @param string
	 * @return Calendar
	 */
	private Calendar parseCalendar(String string) {
		if( string == null ){
			return null;
		}
		Calendar res = Calendar.getInstance();
		res.setTimeInMillis(Long.parseLong(string.trim()));
		return res;
	}

	/**
	 * @param string
	 * @param def 
	 * @return int
	 */
	private int parseInt(String string, int def) {
		if( string == null ){
			return def;
		}
		return Integer.parseInt(string.trim());
	}
	private boolean parseBoolean(String string, boolean def){
		if( string == null ){
			return def;
		}
		return Boolean.parseBoolean(string);
	}
}