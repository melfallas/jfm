package com.journaldev.servlet;

public class CommonConstants {
	
	// Constantes de los ambientes local y producción
	
	//--------------------------- Constantes para Ambiente de Datasys -------------------------//
  	// Constantes Servidores
	public static String BD_SERVER = "172.31.251.128:1433";
	public static String AD_SERVER = "http://172.31.251.128:8085/";
  	public static String FILE_SERVER = "http://172.31.251.11:8080/";
  	// Parámetros Generales
	public static String JDBC_URL = "jdbc:sqlserver://" + BD_SERVER + ";databaseName=compliancedb";
	public static String DB_USER = "compliancelogin";
	public static String DB_PASSWORD = "Datasys123";
 	public static String LOGIN_TABLE = "[compliancedb].[dbo].[jc_users]";
 	public static String MESSAGE_TABLE = "[compliancedb].[dbo].[jm]";
 	public static String CHATS_TABLE = "[compliancedb].[dbo].[jc_chats]";
 	public static String CHATROOMS_TABLE = "[compliancedb].[dbo].[jc_chatrooms]";
  	public static String ADMIN_CHATROOMS_TABLE = "[compliancedb].[dbo].[tc_rooms]";
	public static String ROOT_PATH = "/home/soporte/fileserver";
	public static String FOLDER_PATH = "/home/soporte/fileserver/files/";
	public static String FILE_SERVER_DOWNLOAD = FILE_SERVER + "JabberFileManager/GetFile?filename=";
	//Servicios para peticiones
  	public static String AD_CREDENTIAL_SERVICE_URL = AD_SERVER + "api/UsuarioADObtenerPorCredenciales/";
  	public static String USER_AUTHENTICATION_SERVICE_URL = FILE_SERVER + "JabberFileManager/UserAuthenticationServlet";
  	public static String USER_LOGIN_SERVICE_URL = FILE_SERVER + "JabberFileManager/LoginServlet";
	
	
	//--------------------------- Constantes para Ambiente de Guatemala -------------------------//
//  	// Constantes Servidores
//	public static String BD_SERVER = "172.18.142.15:1433";
//	public static String AD_SERVER = "http://172.18.142.15:8085/";
//  	public static String FILE_SERVER = "http://mp-fsapp01.mp.gob.gt:8080/";
//	// Parámetros Generales
//	public static String JDBC_URL = "jdbc:sqlserver://" + BD_SERVER + ";databaseName=compliancedb";
//	public static String DB_USER = "datasysUser";
//	public static String DB_PASSWORD = "Datasys123";
//	public static String LOGIN_TABLE = "compliancedb.jabber.jc_users";
//    public static String MESSAGE_TABLE = "[compliancedb].[dbo].[jm]";
//    public static String CHATS_TABLE = "[compliancedb].[jabber].[jc_chats]";
//    public static String CHATROOMS_TABLE = "[compliancedb].[jabber].[jc_chatrooms]";
//	public static String ADMIN_CHATROOMS_TABLE = "[compliancedb].[dbo].[tc_rooms]";
//	public static String ROOT_PATH = "/fileserver/archivos/server1";
//	public static String FOLDER_PATH = "/fileserver/archivos/server1/files/";
//  	public static String FILE_SERVER_DOWNLOAD = FILE_SERVER + "JabberFileManager/GetFile?filename=";	
//  	//Servicios para peticiones
//  	public static String AD_CREDENTIAL_SERVICE_URL = AD_SERVER + "api/UsuarioADObtenerPorCredenciales/";
//  	public static String USER_AUTHENTICATION_SERVICE_URL = FILE_SERVER + "JabberFileManager/UserAuthenticationServlet";
//  	public static String USER_LOGIN_SERVICE_URL = FILE_SERVER + "JabberFileManager/LoginServlet";

	
	
	// Constantes Comunes
	public static String JWU_GetWebChatUser = "[jabber].[SP_JWU_GetWebChatUser]";
    public static String JWU_SaveWebChatUser = "[jabber].[SP_JWU_SaveWebChatUser]";
    public static String SP_JWU_CountWebChatUserRegister = "[jabber].[SP_JWU_CountWebChatUserRegister]";
    public static String SP_JWU_GetWebChatUserByCredentials= "[jabber].[SP_JWU_GetWebChatUserByCredentials]";

}
