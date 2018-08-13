<html>
<head>
    <script src="https://code.jquery.com/jquery-2.2.4.min.js" integrity="sha256-BbhdlvQf/xTY9gja0Dq3HiwQF8LaCRTXxZKRutelT44=" crossorigin="anonymous"></script>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
</head>
<body>
    <div class="container-fluid">
        <div class="row">
            <div class="col-md-6  center-block">
                <h1>Ingrese sus credenciales para descargar el archivo:</h1>
                <input type="hidden" name="filename" id="filename" value='${fileName}'/>

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
    
    //  TODO: KEVIN
        $("#submit").click(function() {
            var filename = $("#filename").val();
            var username = $("#username").val();
            var password = $("#password").val();
			if(username.trim() != "" && password.trim() != "") {	
            	//Datasys
                var url = "http://172.31.251.128:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";
            	var userAuthenticationServiceURL =  "http://172.31.251.11:8080/JabberFileManager/UserAuthenticationServlet?webuser="+username+"&pass="+password+"";
            	
                //Guatemala
                //var url = "http://172.18.142.15:8085/api/UsuarioADObtenerPorCredenciales/"+username+"/"+password+"/";
            	//var userAuthenticationServiceURL =  "http://172.128.142.16:8080/JabberFileManager/UserAuthenticationServlet?webuser="+username+"&pass="+password+"";
            	
            	$.ajax({
	                method: "GET",
	                url: userAuthenticationServiceURL,
	                dataType: "json"
	            }).done(function( data ) {
	                console.log( data );
	               if( data.result == "success"){
	            	   
	            		$('#submit').css('background-color','green');
	                	$( "#submit" ).prop( "disabled", true );
	                	$("#submit").html('Descargando...');
	            	   console.log("success");
	                  window.location.replace("/JabberFileManager/UploadDownloadFileServlet?filename="+filename);
	                  
	                  setTimeout(
	            			  function() 
	            			  {
	            				$( "#message" ).empty();
	            				$("#submit").html('Descargado.');
	            			  }, 2000);
	              
	               }else if(data.result == "error"){
	                	//$('<p>Ocurrió un error durante el proceso; por favor, intente de nuevo</p>').appendTo('#message').css('color','red');
	                	$('<p>Credenciales incorrectas. Por favor intente de nuevo.</p>').appendTo('#message').css('color','red');
	                	setTimeout(
	              			  function() 
	              			  {
	              				 $('#submit').css('background-color','grey');
	     	                	 $( "#submit" ).prop( "disabled", true );
	     	                     $( "#message" ).empty();
	     		               	 $("#submit").html('Login');
	     						 $('#submit').css('background-color','#337ab7');
	     						 $('#submit').css('color','#fff');
	     						 $('#submit').css('border-color','#2e6da4');
	     						 $( "#submit" ).prop( "disabled", false );
	     						 $('#username').val('');
	     						 $('#password').val('');
	     	   
	              			  }, 3000);
	
	               }else if(data.result == "validate"){
	            	   console.log("validate AD");
	            	   //servicio de andres validate el ajax antiguo va aqui con la url 
	            	   //aqui puedo validar el sql exeption o AD
	                       var filename = $("#filename").val();
	                       var username = $("#username").val();
	                       var password = $("#password").val();
	                       $.ajax({
	                           method: "GET",
	                           url: url,
	                           dataType: "json"
	                       }).done(function( data ) {
	                           console.log( data );
	                           if(data.username != null){
	                               //console.log("dercarga");
	                               window.location.replace("/JabberFileManager/UploadDownloadFileServlet?filename="+filename);
	                           }else{
	                               $("#error").show();
	                           }
	                       });
	               }else{
	            	   // si no esta en el sql y tampoco en el AD que me tire este mensaje
	            	   $('<p>Ocurrió un error durante el proceso; por favor, intente de nuevo</p>').appendTo('#message').css('color','red');
	               }
	            });
            }else{
            	$('<p>Por favor ingresar los datos requeridos.</p>').appendTo('#message').css('color','red');;	
            	setTimeout(
        			  function() 
        			  {
        				$( "#message" ).empty();
        			  }, 2000);
            }
        });
    	
    </script>
</body>
</html>