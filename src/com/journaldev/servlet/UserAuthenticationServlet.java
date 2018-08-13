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

@WebServlet("/UserAuthenticationServlet")
public class UserAuthenticationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static Connection connObj;

    //Datasys
    public static String JDBC_URL = "jdbc:sqlserver://172.31.251.128:1433;databaseName=compliancedb";
    public static String USER = "compliancelogin";
    public static String PASSWORD = "Datasys123";
    public static String LOGIN_TABLE = "[compliancedb].[dbo].[jc_users]";
    public static String SP_COUNT_VALUE = "[jabber].[SP_JWU_CountWebChatUserRegister]";
    public static String SP_USER_CREDENTIALS = "[jabber].[SP_JWU_GetWebChatUserByCredentials]";

    //Guatemala
    /*
    public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliance";
    public static String USER = "sa";
    public static String PASSWORD = "Datasys123";
    public static String LOGIN_TABLE = "[compliance].[dbo].[jc_users]";
    public static String SP_COUNT_VALUE = "[jabber].[SP_JWU_CountWebChatUserRegister]";
    public static String SP_USER_CREDENTIALS = "[jabber].[SP_JWU_GetWebChatUserByCredentials]";
	*/
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
        JSONObject obj = new JSONObject();
        try {
        	Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            if(connObj != null) {
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
