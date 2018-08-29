package com.journaldev.servlet;

public class CommonConstants {
	
	// Constantes de los ambientes local y producción
	
    // Local - Datasys
	public static String JDBC_URL = "jdbc:sqlserver://172.31.251.128:1433;databaseName=compliancedb";
	public static String DB_USER = "compliancelogin";
	public static String DB_PASSWORD = "Datasys123";
  	public static String LOGIN_TABLE = "[compliancedb].[dbo].[jc_users]";
  	public static String MESSAGE_TABLE = "[compliancedb].[dbo].[jm]";
  	public static String CHATS_TABLE = "[compliancedb].[dbo].[jc_chats]";
  	public static String CHATROOMS_TABLE = "[compliancedb].[dbo].[tc_rooms]";
  	public static String ROOT_PATH = "/home/soporte/fileserver";
  	
  	/*
	// Produccion - Guatemala
	public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliancedb";
	public static String DB_USER = "datasysUser";
	public static String DB_PASSWORD = "Datasys123";
    public static String LOGIN_TABLE = "compliancedb.jabber.jc_users";
    public static String MESSAGE_TABLE = "[compliancedb].[dbo].[jm]";
    public static String CHATS_TABLE = "[compliancedb].[jabber].[jc_chats]";
	public static String CHATROOMS_TABLE = "[compliancedb].[dbo].[tc_rooms]";
	public static String ROOT_PATH = "/fileserver/archivos/server1";
	*/
	
	// Comunes
	public static String SP_DOGET = "jabber.SP_k_selectUser";
    public static String SP_DOPOST = "jabber.SP_K_insertUser";
    public static String SP_COUNT_VALUE = "[jabber].[SP_JWU_CountWebChatUserRegister]";
    public static String SP_USER_CREDENTIALS = "[jabber].[SP_JWU_GetWebChatUserByCredentials]";

}
