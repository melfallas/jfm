package com.journaldev.servlet;

import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    //Guatemala
    /*
    public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliance";
    public static String USER = "sa";
    public static String PASSWORD = "Datasys123";
    public static String LOGIN_TABLE = "[compliance].[dbo].[jc_users]";
	*/
    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }
    //TODO: KEVIN
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
             CallableStatement cStmt = connObj.prepareCall("{call jabber.SP_JWU_GetWebChatUserByCredentials(?,?)}");
              cStmt.setString(1, webChatUser);
          	  cStmt.setString(2, password);
          	  cStmt.execute();
          	// Process all returned result sets  
                 final ResultSet rs = cStmt.getResultSet();
                 if (rs.next()) {
                	 UserParameterDB = rs.getString("JWU_WebChatUser");
                	 passParameterDB= rs.getString("JWU_WebChatPassword");
                	 if(webChatUser.equals(UserParameterDB)  && password.equals(passParameterDB)){
                         //success
                		 obj.put("result", "success");
                     }else{
                    	 //error
                    	 obj.put("result", "error");
                     }
                      }else{ 
                    	  //validate
                    	  obj.put("result", "error");
                	 }
				}
              //obj.put("result", "success");
              //System.out.println("Este es el doGet"); 
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
    }
    
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}
 
}
