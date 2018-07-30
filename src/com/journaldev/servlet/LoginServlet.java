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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static Connection connObj;

    //Datasys
    public static String JDBC_URL = "jdbc:sqlserver://172.31.251.128:1433;databaseName=compliancedb";
    public static String USER = "compliancelogin";
    public static String PASSWORD = "Datasys123";
    public static String LOGIN_TABLE = "[compliancedb].[dbo].[jc_users]";

    //Guatemala
//    public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliance";
//    public static String USER = "sa";
//    public static String PASSWORD = "Datasys123";
//    public static String LOGIN_TABLE = "[compliance].[dbo].[jc_users]";

    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }
    /*
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String system = request.getParameter("system");
        String sysUsername = request.getParameter("username");
        JSONObject obj = new JSONObject();
        try {
        	  Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
              connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
              if(connObj != null) { 
            	  CallableStatement cStmt = connObj.prepareCall("{call jabber.SP_VerifyWebChatUser(?)}");  
            	  cStmt.setString(1, sysUsername); 
            	// Process all returned result sets  
                  cStmt.execute();    
                  final ResultSet rs = cStmt.getResultSet();
                  while (rs.next()) {
                	  System.out.println("Cadena de caracteres pasada como parametro de entrada="+rs.getString("NombreUsuario"));
                	  obj = new JSONObject();
                      obj.put("username", rs.getString("Sistema"));
                      obj.put("password", rs.getString("contrasena"));
				}
              	//System.out.println("Este es el doGet"); 
              }
        	
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
    }
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String systemName = request.getParameter("system");
        String systemUsername = request.getParameter("username");
        String username = request.getParameter("jusername");
        String password = request.getParameter("jpassword");
        String modificador = "NA";
        JSONObject obj = null;
        try {
        	   Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
               connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
               if(connObj != null) {   
                   CallableStatement cStmt = connObj.prepareCall("{call [jabber].[UsuarioAgregarModificar](?, ?, ?, ?, ?)}");  
                   cStmt.setString(1, systemUsername); 
             	   cStmt.setString(2, systemName);
             	   cStmt.setString(3, username);
             	   cStmt.setString(4, password);
             	   cStmt.setString(5, modificador);
             	   cStmt.execute();       
               }
        	//System.out.println("Este es el doPost");
        	
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
    }
    */

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String system = request.getParameter("system");
        String sysUsername = request.getParameter("username");
        JSONObject obj = new JSONObject();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            if(connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery("SELECT  username, password " +
                        "FROM " + LOGIN_TABLE +
                        " WHERE system = '"+ system + "' AND sys_username = '" + sysUsername + "'");

                while(results.next()) {
                    obj = new JSONObject();
                    obj.put("username", results.getString("username"));
                    obj.put("password", results.getString("password"));
                }
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
    }
    

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String systemName = request.getParameter("system");
        String systemUsername = request.getParameter("username");
        String username = request.getParameter("jusername");
        String password = request.getParameter("jpassword");
        JSONObject obj = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            if(connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery("SELECT  username, password " +
                        "FROM " + LOGIN_TABLE +
                        " WHERE system = '"+ systemName + "' AND sys_username = '" + systemUsername + "'");

                while(results.next()) {
                    obj = new JSONObject();
                    obj.put("username", results.getString("username"));
                    obj.put("password", results.getString("password"));
                }

                if(obj == null){
                    PreparedStatement stmt = connObj.prepareStatement("INSERT INTO " + LOGIN_TABLE + " (system, sys_username, username, password) VALUES (?, ?, ?, ?)");
                    stmt.setString(1, systemName);
                    stmt.setString(2, systemUsername);
                    stmt.setString(3, username);
                    stmt.setString(4, password);
                    stmt.executeUpdate();
                }else{
                    PreparedStatement stmt = connObj.prepareStatement("UPDATE " + LOGIN_TABLE + " SET username = ?, password = ? WHERE system = ? AND sys_username = ?" );
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    stmt.setString(3, systemName);
                    stmt.setString(4, systemUsername);
                    stmt.executeUpdate();
                }
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(obj);
        out.flush();
    }
    
    

}
