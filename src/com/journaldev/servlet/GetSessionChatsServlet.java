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

@WebServlet("/GetSessionChatsServlet")
public class GetSessionChatsServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static Connection connObj;    
    private static String JDBC_URL = CommonConstants.JDBC_URL;
    private static String USER = CommonConstants.DB_USER;
    private static String PASSWORD = CommonConstants.DB_PASSWORD;
    private static String CHATS_TABLE = CommonConstants.CHATS_TABLE;

    @Override
    public void init() throws ServletException {
        //Initialize Servlet
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        JSONObject obj;
        JSONArray array = new JSONArray();

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            if(connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery("SELECT  chat " +
                        "FROM " + CHATS_TABLE +
                        " WHERE username = '"+ username + "'");

                while(results.next()) {
                    obj = new JSONObject();
                    obj.put("chat", results.getString("chat"));
                    array.add(obj);
                }
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(array);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String chat = request.getParameter("chat");
        String username = request.getParameter("username");
        String action = request.getParameter("action");
        JSONObject obj = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connObj = DriverManager.getConnection(JDBC_URL,USER,PASSWORD);
            if(connObj != null) {
                Statement statement = connObj.createStatement();
                ResultSet results = statement.executeQuery("SELECT  chat " +
                        "FROM " + CHATS_TABLE +
                        " WHERE username = '"+ username + "' AND chat = '" + chat + "'");

                while(results.next()) {
                    obj = new JSONObject();
                    obj.put("chat", results.getString("chat"));
                }

                if(obj == null){
                    PreparedStatement stmt = connObj.prepareStatement("INSERT INTO " + CHATS_TABLE + " (username, chat) VALUES (?, ?)");
                    stmt.setString(1, username);
                    stmt.setString(2, chat);
                    stmt.executeUpdate();
                }else{
                    if(action.equals("delete")){
                        PreparedStatement stmt = connObj.prepareStatement("DELETE FROM " + CHATS_TABLE + " WHERE username = ? AND chat = ?");
                        stmt.setString(1,username);
                        stmt.setString(2,chat);
                        stmt.executeUpdate();
                    }
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
