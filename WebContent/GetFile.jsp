<html>
<head>
	<!--
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" >
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" ></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" ></script>
    -->
    <link rel="stylesheet" href="bootstrap/bootstrap.min.css" >
    <script src="bootstrap/jquery-2.2.4.min.js" ></script>
    <script src="bootstrap/bootstrap.min.js" ></script>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6  center-block">
                <h1>Ingrese sus credenciales para descargar el archivo:</h1>
                <input type="hidden" name="filename" id="filename" value='${fileName}'/>
                <input type="hidden" name="urluser" id="urluser" value='${username}'/>

                <div class="form-group">
                    <label for="username">Usuario</label>
                    <input type="text" class="form-control" id="username" placeholder="Usuario">
                </div>

                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" class="form-control" id="password" placeholder="Password">
                </div>
                
                <p id="message"></p>

                <label id="error" style="color: red; display: none;">Credenciales incorrectas</label>

                <br>

                <button id="submit" class="btn btn-primary">Login</button>
            </div>
        </div>
    </div>
    
    <script>
    /*
    $("#submit").click(function() {
        var filename = $("#filename").val();
        var username = $("#username").val();
        var password = $("#password").val();
        //Datasys
        var url = "http://172.31.251.128:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";
        //Guatemala
        //var url = "http://172.18.142.15:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";

        $.ajax({
            method: "GET",
            url: url,
            dataType: "json"
        }).done(function( data ) {
            console.log( data );
            if(data.username != null){
                console.log("dercarga");
                window.location.replace("/JabberFileManager/UploadDownloadFileServlet?filename="+filename);
            }else{
                $("#error").show();
            }
        });
    });
    */
    // KEVIN SANDI
  var errorFailedMessage = "Credenciales incorrectas. Por favor intente de nuevo";
  var accessDenied = "EL usuario no tiene permiso para descargar el archivo";
  var emptyFields = "Se requiere datos para procesar la solicitud";
  var validateAD = "Sus datos est�n siendo procesados";
  var errorMessage = "Ocurri� un error durante el proceso; por favor, intente de nuevo";
 $("#submit").click(function() {
	 $( "#message" ).empty();
	  //var getuserurl = urlParams.user[0];
      var filename = $("#filename").val();
      var username = $("#username").val();
      var urluser =  $("#urluser").val();
      console.log($("#urluser").val());
      console.log(username);
      var password = $("#password").val();
      if(username.trim() != "" && password.trim() != "") {
    	  getFileAuhentication(username,password);
      }else{
    	  $('<p>'+emptyFields+'</p>').appendTo('#message').css('color','red');
      }// fin if 1
 });//fin submit  
 function getFileAuhentication(username,password){	
		 	//Servicios Datasys
		    var filename = $("#filename").val();
			var url = "http://172.31.251.128:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";
			
			 var userAuthenticationServiceURL = "http://localhost:8080/JabberFileManager/UserAuthenticationServlet?webuser="+username+"&pass="+password+"&filename="+filename+"";//var userAuthenticationServiceURL =  "http://172.31.251.11:8080/JabberFileManager/UserAuthenticationServlet?webuser="+username+"&pass="+password+"";
			//Servicios Guatemala
			//var url = "http://172.18.142.15:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";
			//var userAuthenticationServiceURL =  "http://mp-fsapp01.mp.gob.gt:8080/JabberFileManager/UserAuthenticationServlet?webuser="+username+"&pass="+password+"";
        	$.ajax({
             method: "GET",
             url: userAuthenticationServiceURL,
             dataType: "json"
         }).done(function( data ) {
             console.log( data );
            if( data.result == "success"){
             	$( "#submit" ).prop( "disabled", true );
             	$("#submit").html('Descargando....');
         	   console.log("success");
               window.location.replace("/JabberFileManager/UploadDownloadFileServlet?filename=" + $("#filename").val());
               setTimeout(
         			  function() 
         			  {
         				$('#submit').css('background-color','green');
         				$( "#message" ).empty();
         				$("#submit").html('Descargado');
         			  }, 2000);
            }else if(data.result == "error"){
            	 $( "#message" ).empty();
        	     $("#username,#password").prop("disabled",false);
        	     $( "#submit" ).prop( "disabled", false );	
	 		     $('#submit').css('background-color','grey');
	         	 $( "#submit" ).prop( "disabled", true );
	         	 $("#submit").html('Login');
				 $('#submit').css('background-color','#337ab7');
				 $('#submit').css('color','#fff');
				 $('#submit').css('border-color','#2e6da4');
				 $( "#submit" ).prop( "disabled", false );
				 $('#password').val('');
				 $('<p>'+errorFailedMessage+'</p>').appendTo('#message').css('color','red');
            }else if(data.result == "validate"){
            	 $( "#message" ).empty();
         	   $("#username,#password").prop("disabled",true);
         	   $( "#submit" ).prop( "disabled", true );
         	   $("#submit").html('Cargando....');
         	   $( "#message" ).empty();
         	   $('<p>'+ validateAD +'</p>').appendTo('#message').css('color','black');
         	   console.log("validate AD");
                    $.ajax({
                        method: "GET",
                        url: url,
                        dataType: "json"
                    }).done(function( data ) {
                        console.log( data );
                        if(data.username != null){
                            console.log("dercarga");
                            window.location.replace("/JabberFileManager/UploadDownloadFileServlet?filename="+filename);
                        }else{
                            $("#error").show();
                        }
                    });
            }else{
            	 $( "#message" ).empty();
         	   $('<p>'+ errorMessage +'</p>').appendTo('#message').css('color','red');
            }
         });
            	}
       
    </script>
</body>
</html>