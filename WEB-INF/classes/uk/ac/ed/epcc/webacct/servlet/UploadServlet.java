// Copyright - The University of Edinburgh 2015
/*******************************************************************************
 * Copyright (c) - The University of Edinburgh 2010
 *******************************************************************************/
package uk.ac.ed.epcc.webacct.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.ac.ed.epcc.safe.accounting.upload.UploadException;
import uk.ac.ed.epcc.safe.accounting.upload.UploadParser;
import uk.ac.ed.epcc.webacct.model.ReceiveAccountingUploadParser;
import uk.ac.ed.epcc.webapp.AppContext;
import uk.ac.ed.epcc.webapp.Feature;
import uk.ac.ed.epcc.webapp.logging.Logger;
import uk.ac.ed.epcc.webapp.logging.LoggerService;
import uk.ac.ed.epcc.webapp.servlet.ContainerAuthServlet;
import uk.ac.ed.epcc.webapp.servlet.ServletService;

public class UploadServlet extends ContainerAuthServlet {

	public static final Feature SERVLET_UPLOAD_FEATURE = new Feature("servlet.upload",false,"accounting data may be uploaded to a servlet by scripts");
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res,
			AppContext conn, String user) throws ServletException,
			IOException {

		if( ! conn.isFeatureOn(SERVLET_UPLOAD_FEATURE)){
			message(conn, req, res, "upload_servlet_disabled");
			return;
		}
		synchronized(this){

		Logger log = conn.getService(LoggerService.class).getLogger(getClass());	
		
		try {
			Map<String,Object> params = conn.getService(ServletService.class).getParams();
			String type = (String) params.get("mode");
			UploadParser parser = conn.makeObjectWithDefault(UploadParser.class, ReceiveAccountingUploadParser.class, type);
			String result = parser.upload(params);
			res.setContentType("text/plain");
			PrintWriter out = res.getWriter();
			out.println(result);
		} catch(UploadException ue){
			log.debug("Upload error",ue);
			doError(req, res, conn, ue.getMessage());
			
		}catch (Throwable e) {
			log.debug("Error in upload",e);
			conn.error(e,"Error in upload");
			doError(req, res, conn, e.getMessage());
		}
       
		}
	}
    protected void doError(HttpServletRequest req, HttpServletResponse res,
			AppContext conn, String error) throws ServletException, IOException{
    	message(conn, req, res, "upload_error",error);
    }
}