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
        $("#submit").click(function() {
            var filename = $("#filename").val();
            var username = $("#username").val();
            var password = $("#password").val();
			if(username.trim() != "" && password.trim() != ""){	
            	$('#submit').css('background-color','green');
            	$( "#submit" ).prop( "disabled", true );
            	$("#submit").html('Descargar');
            }else{
            	$('<p>Por favor ingresar los datos requeridos !</p>').appendTo('#message').css('color','red');;	
            	setTimeout(
            			  function() 
            			  {
            				$( "#message" ).empty();
            			  }, 2000);
            	
            }
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
                	
                	$("#submit").html('Descargando...');
                
                    console.log("dercarga");
                    window.location.replace("/JabberFileManager/UploadDownloadFileServlet?filename="+filename);
                	$("#submit").html('Descargado');
                }else{
 
                    setTimeout(
              			  function() 
              			  {
              				  $("#error").show();
                              $('#submit').css('background-color','gray');
              			
              			  }, 4000);
    
                }
                 $("#submit").html('Login');
				 $('#submit').css('background-color','#337ab7');
				 $('#submit').css('color','#fff');
				 $('#submit').css('border-color','#2e6da4');
				 $( "#submit" ).prop( "disabled", false );
				 $('#username').val('');
				 $('#password').val('');
            });
        });
    </script>
</body>
</html>