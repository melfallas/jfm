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
    private static Connection connObj;    
    private static String JDBC_URL = CommonConstants.JDBC_URL;
    private static String USER = CommonConstants.DB_USER;
    private static String PASSWORD = CommonConstants.DB_PASSWORD;
    private static String MESSAGE_TABLE = CommonConstants.MESSAGE_TABLE;

    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String session = request.getParameter("session");
        System.out.println(session);
        JSONObject obj;
        JSONArray array = new JSONArray();
        String from_jid;
        String to_jid;
        
        if(session.split("/").length > 1){
             from_jid = session.split("/")[0];
             to_jid = session.split("/")[1];
            
            if ( from_jid.toLowerCase().indexOf("@".toLowerCase()) != -1){
          	  String fromUser_jid = from_jid;
                int firstUser = fromUser_jid.indexOf('@');
                fromUser_jid = fromUser_jid.substring(0,firstUser);
                from_jid =fromUser_jid;
          }
          	 if(to_jid.toLowerCase().indexOf("@".toLowerCase()) != -1) {
          		 
          		 String fromTo_jid = to_jid;
                   int secondUser = fromTo_jid.indexOf('@');
                   fromTo_jid = fromTo_jid.substring(0,secondUser);
                   
                   to_jid = fromTo_jid;
          	 }
          	 
            try {
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
                if(connObj != null) {

                	Statement statement = connObj.createStatement();
                    /*
                    String query = 
                    		"SELECT  sent_date, to_jid, from_jid, body_string " +
                            "FROM "+ MESSAGE_TABLE +
                            " WHERE "
                            + "((to_jid LIKE '%" + to_jid + "%' AND from_jid LIKE '%" + from_jid + "%') " 
                            + " OR (to_jid LIKE '%" + from_jid + "%' AND from_jid LIKE '%" + to_jid + "%')) "
                    		+ "AND direction = 'O'"
                            + " ORDER BY  CONVERT (char(10), sent_date, 103) ASC";
                    */
                	
        
                    // ESTA VERSION ES PARA SQL 2017 TRIM
                	/*
                    String query =   "  SELECT sent_date, to_jid, from_jid, body_string FROM( "
                    + " SELECT sent_date, to_jid, from_jid, body_string FROM [compliancedb].[dbo].[jm] (NOLOCK) "
                    + "  WHERE (TRIM (SUBSTRING(to_jid,0,CHARINDEX('@',to_jid))) LIKE '"+to_jid+"' AND TRIM(SUBSTRING(from_jid,0,CHARINDEX('@',from_jid))) LIKE '"+from_jid+"') "
                    + " AND direction = 'O' "
                    + " UNION "
                    + " SELECT sent_date, to_jid, from_jid, body_string FROM [compliancedb].[dbo].[jm] (NOLOCK)"
                    + " WHERE (TRIM(SUBSTRING(to_jid,0,CHARINDEX('@',to_jid))) LIKE '"+to_jid+"' "
                    + " AND TRIM(SUBSTRING(from_jid,0,CHARINDEX('@',from_jid))) LIKE ''"+from_jid+"') "
                    + " AND direction = 'O' "
                    + ") t ORDER BY t.sent_date desc";
                	
                	*/
                	
                    // ESTA VERSION ES PARA SQL 2012 LTRIM
    
                    String query =
                    
                    "  SELECT sent_date, to_jid, from_jid, body_string FROM( "
                    + " SELECT sent_date, to_jid, from_jid, body_string FROM [compliancedb].[dbo].[jm] (NOLOCK) "
                    + "  WHERE (LTRIM (SUBSTRING(to_jid,0,CHARINDEX('@',to_jid))) LIKE '"+to_jid+"' AND LTRIM(SUBSTRING(from_jid,0,CHARINDEX('@',from_jid))) LIKE '"+from_jid+"') "
                    + " AND direction = 'O' "
                    + " UNION "
                    + " SELECT sent_date, to_jid, from_jid, body_string FROM [compliancedb].[dbo].[jm] (NOLOCK)"
                    + " WHERE (LTRIM(SUBSTRING(to_jid,0,CHARINDEX('@',to_jid))) LIKE '"+to_jid+"' "
                    + " AND LTRIM(SUBSTRING(from_jid,0,CHARINDEX('@',from_jid))) LIKE '"+from_jid+"') "
                    + " AND direction = 'O' "
                    + ") t ORDER BY t.sent_date ASC";

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
        String responseVal = "";
        if(updateMessage(request)){
            responseVal = "update successful";
        }else{
            responseVal = "update failed";

        };
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.print(responseVal);
        out.flush();
    }

    private boolean updateMessage(HttpServletRequest request){
        String messageInfo = request.getParameter("messageInfo");
        boolean result = false;
        try {
            String from = messageInfo.split("/")[0];
            String to = messageInfo.split("/")[1];
            String threadId = messageInfo.split("/")[2];
            String fileTransfer = messageInfo.replace(from + "/" + to + "/" + threadId + "/","");
            fileTransfer = fileTransfer.replaceAll("<mime-type>.*","");
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL, USER, PASSWORD);
            if (connObj != null) {
                Statement statement = connObj.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
                        ResultSet.CONCUR_UPDATABLE);
                String query = 
            		"SELECT  message_string, body_string " +
                    "FROM " + MESSAGE_TABLE 
                    + " WHERE to_jid LIKE '%" 
                    + to + "' AND from_jid LIKE '%" 
                    + from + "%' AND thread_id = '" 
                    + threadId + "'"
                    + " ORDER BY [sent_date] DESC"
                ;
                //System.out.println(query);
                ResultSet results = statement.executeQuery(query);
                while(results.next()) {
                    if(results.getString("message_string").contains(fileTransfer)){
                        results.updateString( "body_string", fileTransfer.replace("<size-bytes>","<size-bytes>#"));
                        results.updateRow();
                    };
                }
            }
            result  = true ;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return result;
    }

}
