package com.journaldev.servlet;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class FileLocationContextListener implements ServletContextListener {

	//Datasys
	public static String ROOT_PATH = "/home/soporte/fileserver";

	//Guatemala
//	public static String ROOT_PATH = "/fileserver/archivos/server1";

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    	
//    	String rootPath = System.getProperty("catalina.home");
//		String relativePath = ctx.getInitParameter("tempfile.dir");
    	
    	ServletContext ctx = servletContextEvent.getServletContext();
    	String relativePath = "files";
    	File file = new File(ROOT_PATH + File.separator + relativePath);
    	if(!file.exists()) file.mkdirs();
    	System.out.println("File Directory created to be used for storing files");
    	ctx.setAttribute("FILES_DIR_FILE", file);
    	System.out.println(ROOT_PATH);
    	ctx.setAttribute("FILES_DIR", ROOT_PATH + File.separator + relativePath);
    }

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		//do cleanup if needed
	}
	
}
