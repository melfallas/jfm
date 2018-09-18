package com.journaldev.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

@WebServlet("/UploadDownloadFileServlet")
public class UploadDownloadFileServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private static Connection connObj;    
    private static String JDBC_URL = CommonConstants.JDBC_URL;
    private static String USER = CommonConstants.DB_USER;
    private static String PASSWORD = CommonConstants.DB_PASSWORD;
    private static String FOLDER_PATH = CommonConstants.FOLDER_PATH;
    private ServletFileUpload uploader = null;

	@Override
	public void init() throws ServletException{
		DiskFileItemFactory fileFactory = new DiskFileItemFactory();
		File filesDir = (File) getServletContext().getAttribute("FILES_DIR_FILE");
		fileFactory.setRepository(filesDir);
		this.uploader = new ServletFileUpload(fileFactory);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String fileResult = request.getParameter("filename");
		String fileName = java.net.URLDecoder.decode(fileResult, "UTF-8");
		
		if(fileName == null || fileName.equals("")){
			throw new ServletException("File Name can't be null or empty");
		}

		String [] params = {};
		String lastPath = "";
		if(fileName.contains("custom")) {
			lastPath = fileName;
		}else {
			params = getFilePath(fileName);
			lastPath = params[0].replace(FOLDER_PATH, "");
		}

		System.out.println("real_path: " + request.getServletContext().getAttribute("FILES_DIR")+File.separator+lastPath);
		File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+lastPath);
		if(!file.exists()){
			throw new ServletException("File doesn't exists on server.");
		}
		
		System.out.println("File location on server::"+file.getAbsolutePath());
		ServletContext ctx = getServletContext();
		InputStream fis = new FileInputStream(file);
		String mimeType = ctx.getMimeType(file.getAbsolutePath());
		response.setContentType(mimeType != null? mimeType:"application/octet-stream");
		response.setContentLength((int) file.length());
		if(fileName.contains("custom")) {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
		}else {
			response.setHeader("Content-Disposition", "attachment; filename=\"" + params[1] + "\"");
		}
		
		ServletOutputStream os = response.getOutputStream();
		byte[] bufferData = new byte[1024];
		int read=0;
		while((read = fis.read(bufferData))!= -1){
			os.write(bufferData, 0, read);
		}
		os.flush();
		os.close();
		fis.close();
		System.out.println("File downloaded at client successfully");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(!ServletFileUpload.isMultipartContent(request)){
			throw new ServletException("Content type is not multipart/form-data");
		}
		
		response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
		String ajaxUpdateResult = "";
		try {
			List<FileItem> fileItemsList = uploader.parseRequest(request);
			Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();
			String lastPath = "";
			while(fileItemsIterator.hasNext()){
				FileItem fileItem = fileItemsIterator.next();
				if (fileItem.isFormField()) {
					lastPath = fileItem.getString().trim();
					File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+"custom/"+lastPath);
					if(!file.exists()) file.mkdirs();
				}else {
					File file = new File(request.getServletContext().getAttribute("FILES_DIR")+File.separator+"custom/"+lastPath+"/"+fileItem.getName().replace(" ",""));
					System.out.println("Absolute Path at server="+file.getAbsolutePath());
					fileItem.write(file);
					ajaxUpdateResult += fileItem.getName();
				}
			}
		} catch (FileUploadException e) {
			response.getWriter().print("Exception in uploading file.");
			e.printStackTrace();
		} catch (Exception e) {
			response.getWriter().print("Exception in uploading file.");
			e.printStackTrace();
		}
		response.getWriter().print(ajaxUpdateResult);
	}
	
	private String[] getFilePath(String filename) {
		String filePath = "";
		String realFileName = "";
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
			if(connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery("SELECT filename, real_filename, file_path FROM aft_log "
                		+ " WHERE filename='" + filename + "'"
                		+ " AND file_path <> ''"
                		+ " ORDER BY file_path desc"
                );

                if(results.next()) {
                    filePath = results.getString("file_path");
                    realFileName = results.getString("real_filename");
                }
			}
			connObj.close();
		} catch(Exception sqlException) {
			sqlException.printStackTrace();
		}
		
		String [] params = {filePath, realFileName};
		return params;
	}

}
