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
 	public static String CHATROOMS_TABLE = "[compliancedb].[dbo].[jc_chatrooms]";
  	public static String ADMIN_CHATROOMS_TABLE = "[compliancedb].[dbo].[tc_rooms]";
	public static String ROOT_PATH = "/home/soporte/fileserver";
	public static String FOLDER_PATH = "/home/soporte/fileserver/files/";
	public static String FILE_SERVER_DOWNLOAD = "http://172.31.251.11:8080/JabberFileManager/GetFile?filename=";
//  	public static String urlADcredentialService = "http://172.31.251.128:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";
//  	public static String userAuthenticationServiceURL = "http://172.31.251.11:8080/JabberFileManager/UserAuthenticationServlet?webuser="+username+"&pass="+password+"&filename="+encoded+"";
//  	public static String urlUserLogin = "http://172.31.251.11:8080/JabberFileManager/LoginServlet?system=temp&username=temp&jusername="+$("#username").val()+"&jpassword="+$("#password").val()+"";

  	
	// Produccion - Guatemala
//	public static String JDBC_URL = "jdbc:sqlserver://172.18.142.15:1433;databaseName=compliancedb";
//	public static String DB_USER = "datasysUser";
//	public static String DB_PASSWORD = "Datasys123";
//	public static String LOGIN_TABLE = "compliancedb.jabber.jc_users";
//    public static String MESSAGE_TABLE = "[compliancedb].[dbo].[jm]";
//    public static String CHATS_TABLE = "[compliancedb].[jabber].[jc_chats]";
//    public static String CHATROOMS_TABLE = "[compliancedb].[jabber].[jc_chatrooms]";
//	public static String ADMIN_CHATROOMS_TABLE = "[compliancedb].[dbo].[tc_rooms]";
//	public static String ROOT_PATH = "/fileserver/archivos/server1";
//	public static String FOLDER_PATH = "/fileserver/archivos/server1/files/";
//  	public static String FILE_SERVER_DOWNLOAD = "http://mp-fsapp01.mp.gob.gt:8080/JabberFileManager/GetFile?filename=";	
//
//	//Servicios Guatemala
//  	public static String AD_CREDENTIAL_SERVICE_URL = "http://172.18.142.15:8085/api/UsuarioADObtenerPorCredenciales/";
//  	public static String USER_AUTHENTICATION_SERVICE_URL = "http://mp-fsapp01.mp.gob.gt:8080/JabberFileManager/UserAuthenticationServlet";
//  	public static String USER_LOGIN_SERVICE_URL = "http://mp-fsapp01.mp.gob.gt:8080/JabberFileManager/LoginServlet";

	
	
	// Comunes
	public static String JWU_GetWebChatUser = "[jabber].[SP_JWU_GetWebChatUser]";
    public static String JWU_SaveWebChatUser = "[jabber].[SP_JWU_SaveWebChatUser]";
    public static String SP_JWU_CountWebChatUserRegister = "[jabber].[SP_JWU_CountWebChatUserRegister]";
    public static String SP_JWU_GetWebChatUserByCredentials= "[jabber].[SP_JWU_GetWebChatUserByCredentials]";

}
