package com.journaldev.servlet;

import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

@WebServlet("/GetSessionMessagesServlet")
public class GetSessionMessagesServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    public static Connection connObj;

    //Datasys
    //public static String JDBC_URL = "jdbc:sqlserver://172.31.251.128:1433;databaseName=compliancedb";
    //public static String user = "compliancelogin";
    //public static String pass = "Datasys123";

    //Guatemala
    public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliance";
    public static String user = "sa";
    public static String pass = "Datasys123";

    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String session = request.getParameter("session");
        System.out.println(session);
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONObject obj = new JSONObject();
        JSONArray array = new JSONArray();
        obj.put("content", "test");
        obj.put("displayName", "test");
        obj.put("timestamp", "test");
        array.add(obj);
        out.print(array);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private String[] getFilePath(String filename) {
        String filePath = "";
        String realFileName = "";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,user,pass);
            if(connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery("SELECT filename, real_filename, file_path FROM aft_log "
                        + "WHERE filename='" + filename + "' ORDER BY file_path desc");

                while(results.next()) {
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
