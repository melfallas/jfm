package com.journaldev.servlet;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/UserAuthenticationServlet")
public class UserAuthenticationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Connection connObj;    
    private static String JDBC_URL = CommonConstants.JDBC_URL;
    private static String USER = CommonConstants.DB_USER;
    private static String PASSWORD = CommonConstants.DB_PASSWORD;
    private static String SP_COUNT_VALUE = CommonConstants.SP_COUNT_VALUE;
    private static String SP_USER_CREDENTIALS = CommonConstants.SP_USER_CREDENTIALS;
	
    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }
    //TODO: KEVSAN
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String webChatUser  = request.getParameter("webuser");
        String password = request.getParameter("pass");
        String UserParameterDB;
        String passParameterDB;
        String filename = request.getParameter("filename");
        JSONObject obj = new JSONObject();
        Set<String> userList = new HashSet<String>();
        String url = "http://mp-fsapp01.mp.gob.gt:8080/JabberFileManager/GetFile?filename="+filename;
        String query = "SELECT "+
				"LOWER(SUBSTRING(to_jid, 0, CHARINDEX('@', to_jid))) AS toUser"+
				",LOWER(SUBSTRING(from_jid, 0, CHARINDEX('@', from_jid))) AS fromUser" +
				" FROM [dbo].[jm]"+
				" WHERE "+
				" body_string ='"+url+"' and direction = 'O'"+ 
				" ORDER BY [sent_date] DESC";
        
        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            if(connObj != null) {
            	
            	// mandar nuevo codigo 
           	 	Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery(query);
                
                while(results.next()) {
                	 obj = new JSONObject();
                	 userList.add(results.getString("toUser"));
                	 userList.add(results.getString("fromUser"));
                }
                if(!userList.contains(webChatUser)){
               	 
               	 obj.put("result", "denied");
                	 System.out.println("denied");
                }else{
               	 
               	 obj.put("result", "granted");
               	 System.out.println("granted");
					 
                
            	CallableStatement cstmt = connObj.prepareCall("exec "+SP_COUNT_VALUE+" ?");
                cstmt.setString(1, webChatUser);
                cstmt.execute();
                final ResultSet rs = cstmt.getResultSet();
                 if (rs.next()) {
                	 int  countValue = rs.getInt(1);
                	 //System.out.println("Value from first result set = " + countValue); 
                	 if(countValue > 0){
                	     CallableStatement cStmt2 = connObj.prepareCall("{call "+SP_USER_CREDENTIALS+"(?,?)}");
                	     cStmt2.setString(1, webChatUser);
                	     cStmt2.setString(2, password);
                	     cStmt2.execute();
                	     // Process all returned result sets 
                         final ResultSet rs2 = cStmt2.getResultSet();
                         if (rs2.next()) {
                        	 UserParameterDB = rs2.getString("JWU_WebChatUser");
                        	 passParameterDB= rs2.getString("JWU_WebChatPassword");
                        	 if(webChatUser.equals(UserParameterDB)  && password.equals(passParameterDB)){
	                    		 obj.put("result", "success");
                        	 }
                         }
                         else{ 
                        	 obj.put("result", "error");
                    	 }
                	 }else{
                		 // Si el count es 0 se envía result = validate para autenticar con AD
                		 obj.put("result", "validate");
                	 }
                 }
                }
            }   	 
        } catch(Exception sqlException) {
   		 	obj.put("result", "success");
            sqlException.printStackTrace();
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    }
}
