<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html><head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="./style/User-login-registrazione.css" rel="stylesheet" type="text/css">
	<link href="./style/menu-style.css" rel="stylesheet" type="text/css">
	<!-- Footer -->
	<link rel="stylesheet" type="text/css" href="./style/footer.css">
	
	<title>Beauty Blind - Gestione Utente</title>
	<!-- To rework the favicon: https://www.favicon.cc/ -->
	<link rel="icon" type="image/x-icon" href="./img/favicon_bb_ns.ico">
</head><body>
	<%@ include file="html/menu.html" %>
	<div class="contenitore-generale">
	    <%
	    	if(!(request.getSession().getAttribute("userID") == null)){
	    		System.out.println("utente già loggato : " + request.getSession().getAttribute("userID"));
	    %>
	   		<h2> Bentornato <%= request.getSession().getAttribute("username")%></h2>
	   		
	   		<a href="/User?action=logout">Logout</a>
	   
	    <%
	    	}else{
	    %>   
			<div class="user_container">				
			  <div class="tab">
			    <button class="tablinks" onclick="openForm(event, 'Login')" id="defaultOpen">Login</button>
			    <button class="tablinks" onclick="openForm(event, 'Registrazione')">Registrazione</button>
			  </div>
			
			  <div id="Login" class="tabcontent">
			    <form name="login" action="User" method="post">
				    <label for="username">Username: <strong>*</strong> </label><br>
		            <input type="text" id="username" name="username" required><br>
		
		            <label for="password">Password: <strong>*</strong> </label><br>
		            <input type="password" id="password" name="password" required><br>
		
		            <!-- parametro che serve a vedere se l'utente vuole loggarsi o registrarsi -->
		            <input type="hidden" name="action" value="login">
		
		            <input type="submit" value="Login">
			    </form>
			    <div class="link">
			      <a href="#">Hai dimenticato la password?</a>
			    </div>
			  </div>
			
			  <div id="Registrazione" class="tabcontent">
			  
		    	<p id="errorMessage" class="hiddenError">
		    	
		    		<img src="./img/errorFormValidation.png" alt="error">
		    		<span id="errorTxt"></span>
		    	</p>
		    	
		    	<!-- action="User" method="post" -->
			    	
			    <form name="register" onsubmit="validateRegister(event)">
				    <div>
					    <label for="nome">Nome: <strong>*</strong> </label>
			            <input type="text" id="nome" name="nome" required
			            oninput="isAllLetter(this.value)">
			
			            <label for="cognome">Cognome: <strong>*</strong> </label>
			            <input type="text" id="cognome" name="cognome" required
			            oninput="isAllLetter(this.value)">
			
			            <label for="new_username">Username: <strong>*</strong> </label>
			            <input type="text" id="new_username" name="new_username" required
			            oninput="isAlphanumeric(this.value)">
			            
			            <label for="phoneNumber">Numero di telefono: <strong>*</strong> </label>
			            <input type="tel" id="phoneNumber" name="phoneNumber" required
			            oninput="phoneNumberIsValid(this.value)">
			            
			            <ul id="passRequirements">
			            	<li>Almeno 8 caratteri</li>
			            	<li>Massimo 20 caratteri</li>
			            	<li>Almeno 1 minuscola</li>
			            	<li>Almeno 1 maiuscola</li>
			            	<li>Almeno 1 numero</li>
			            	<li>Almeno 1 carattere speciale</li>
			            </ul>
					</div>
					<div>
			            <label for="dataNascita">Data di nascita: <strong>*</strong> </label>
			            <input type="date" id="dataNascita" name="dataNascita" required
			            oninput="birthDateIsValid(this.value)">
			
			            <label for="email">E-mail: <strong>*</strong> </label>
			            <input type="email" id="email" name="email" required
			            oninput="emailIsValid(this.value)">
			            
			            <label for="email">Indirizzo: <strong>*</strong> </label>
			            <input type="text" id="indirizzo" name="indirizzo" required disabled
			            oninput="indirizzoIsValid(this.value)">
			
			            <label for="new_password">Password: <strong>*</strong> </label>
			            <input type="password" id="new_password" name="new_password" required
			            oninput="passwordIsValid(this.value)" onfocus="passwordIsValid(this.value)"
			            onblur="hideRequirements()">
						
			            <!-- parametro che serve a vedere se l'utente vuole loggarsi o registrarsi 
			            	<input type="hidden" name="action" value="register">
			            -->
			            <input type="submit" value="Register">
			            
		            </div>
		
			    </form>
			  </div>
			
			  <div class="link">
			    <a href="/home.jsp">Torna alla home</a>
			  </div>
			</div>
			
			<script>
			  function openForm(evt, formName) {
			    var i, tabcontent, tablinks;
			    tabcontent = document.getElementsByClassName("tabcontent");
			    for (i = 0; i < tabcontent.length; i++) {
			      tabcontent[i].style.display = "none";
			    }
			    tablinks = document.getElementsByClassName("tablinks");
			    for (i = 0; i < tablinks.length; i++) {
			      tablinks[i].className = tablinks[i].className.replace(" active", "");
			    }
			    document.getElementById(formName).style.display = "block";
			    evt.currentTarget.className += " active";
			  }
			
			  // Get the element with id="defaultOpen" and click on it
			  document.getElementById("defaultOpen").click();
			</script>
				        
	    <%}%>
	</div>

    <%@ include file="html/footer.html" %>
    <script type="text/javascript" src="./javascript/barraDiRicerca.js"></script> 
    <script type="text/javascript" src="./javascript/FormValidation.js"></script>
</body>

</html>
