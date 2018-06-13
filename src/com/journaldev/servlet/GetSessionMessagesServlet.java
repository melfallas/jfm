package com.journaldev.servlet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet("/GetSessionMessagesServlet")
public class GetSessionMessagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static Connection connObj;

    //Datasys
    public static String JDBC_URL = "jdbc:sqlserver://172.31.251.128:1433;databaseName=compliancedb";
    public static String USER = "compliancelogin";
    public static String PASSWORD = "Datasys123";
    public static String MESSAGE_TABLE = "[compliancedb].[dbo].[jm]";

    //Guatemala
//    public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliance";
//    public static String USER = "sa";
//    public static String PASSWORD = "Datasys123";
//    public static String MESSAGE_TABLE = "[compliance].[dbo].[jm]";

    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String session = request.getParameter("session");
        System.out.println(session);
        JSONObject obj;
        JSONArray array = new JSONArray();

        if(session.split("/").length > 1){
            String from_jid = session.split("/")[0];
            String to_jid = session.split("/")[1];

            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
                if(connObj != null) {

                	Statement statement = connObj.createStatement();
                    
                    String query = 
                    		"SELECT  sent_date, to_jid, from_jid, body_string " +
                            "FROM "+ MESSAGE_TABLE +
                            " WHERE "
                            + "((to_jid LIKE '%" + to_jid + "%' AND from_jid LIKE '%" + from_jid + "%') " 
                            + " OR (to_jid LIKE '%" + from_jid + "%' AND from_jid LIKE '%" + to_jid + "%')) "
                    		+ "AND direction = 'O'"
                            + " ORDER BY [sent_date] ASC"
                    ;
                    
                    ResultSet results = statement.executeQuery(query);
                    /*
                    ResultSet results = statement.executeQuery("SELECT  sent_date, to_jid, from_jid, body_string " +
                            "FROM "+ MESSAGE_TABLE +
                            " WHERE ((to_jid = '"+to_jid+"' and from_jid LIKE '" + from_jid + "%') or (to_jid = '" + from_jid + "' and from_jid LIKE '" + to_jid + "%')) and direction = 'O'" +
                            "ORDER BY sent_date asc");
                    */

                    while(results.next()) {
                        obj = new JSONObject();
                        if(results.getString("body_string").isEmpty()){
                            continue;
                        }
                        obj.put("content", results.getString("body_string"));
                        obj.put("displayName", results.getString("from_jid").substring(0,results.getString("from_jid").indexOf("@")));
                        java.util.Date date = null;
                        Timestamp timestamp = results.getTimestamp("sent_date");
                        if (timestamp != null)
                            date = new java.util.Date(timestamp.getTime());
                        obj.put("timestamp", date.toString());
                        array.add(obj);
                    }
                }
            } catch(Exception sqlException) {
                sqlException.printStackTrace();
            }
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(array);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

}
