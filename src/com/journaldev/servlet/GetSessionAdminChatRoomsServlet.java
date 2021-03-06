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

@WebServlet("/GetSessionAdminChatRoomsServlet")
public class GetSessionAdminChatRoomsServlet extends HttpServlet {
	
    private static final long serialVersionUID = 1L;
    private static Connection connObj;
    private static String JDBC_URL = CommonConstants.JDBC_URL;
    private static String USER = CommonConstants.DB_USER;
    private static String PASSWORD = CommonConstants.DB_PASSWORD;
    private static String CHATROOMS_TABLE = CommonConstants.ADMIN_CHATROOMS_TABLE;

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
                String query = 
                				"SELECT room_jid " +
                                "FROM "
                                + "" + CHATROOMS_TABLE +
                                " WHERE creator_jid LIKE '"+ username + "%'"
                                ;
                ResultSet results = statement.executeQuery(query);

                while(results.next()) {
                    obj = new JSONObject();
                    obj.put("chatroom", results.getString("room_jid"));
                    array.add(obj);
                }
            }
        } catch(Exception sqlException) {
            sqlException.printStackTrace();
        }finally {
            try {
                System.out.println("Closing connection");
                connObj.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(array);
        out.flush();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // todo: doPost to be implemented
    }
}
