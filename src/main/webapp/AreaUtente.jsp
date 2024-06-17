<%@ page contentType="text/html;charset=UTF-8" language="java" import="java.util.*,Model.Bean.*"%>
<html>
    <head>
        <title>Title</title>
        <link href="./style/menu-style.css" rel="stylesheet" type="text/css">
        <link href="./style/AreaUtente.css" rel="stylesheet" type="text/css">
        <!-- Footer -->
		<link rel="stylesheet" type="text/css" href="./style/footer.css">	
    </head>
    <body>
        <%@ include file="./html/menu.html" %>
        <%
        	UserBean user;
        	boolean isAdmin = (boolean) request.getSession().getAttribute("adminRoles");
        	System.out.println("admin : " + isAdmin);
        	
        	user = (UserBean) request.getAttribute("datiUtente");
            if (user != null) {
            	System.out.println(user.toString());
        %>
        <main>
            <div class="data-container">
                <section class="user-profile">
                    <h2>Bentornato <%= request.getSession().getAttribute("username")%></h2>
                    <div class="user-info">
                        <p><strong>Nome:</strong> <%= user.getNome() %> </p>
                        <p><strong>Cognome:</strong> <%= user.getCognome() %> </p>
                        <p><strong>Username:</strong> <%= user.getUserName() %> </p>
                        <p><strong>Email:</strong> <%= user.getEmail() %> </p>
                        <p><strong>Indirizzo:</strong> <%= user.getIndirizzoBase() %> </p>
                        <p><strong>Numero di telefono:</strong> <%= user.getNumeroDiTelefono() %> </p>
                    </div>
                </section>
                <section class="user-actions">
                    <h2>Azioni</h2>
                    <div class="actions">
                        <button onclick="location.href='#'">Storico Ordini</button>
                        <button onclick="location.href='/Cart?action=visualizza'">Carrello</button>
                        <button onclick="location.href='/User?action=logout'">Logout</button>
                        <%
                        	if(isAdmin){
                        %>
                        <button onclick="location.href='#'">Modifica Prodotto</button>
                        <button onclick="location.href='./admin/InserisciProdotto.jsp'">Inserisci Prodotto</button>
                        <button onclick="location.href='./Admin?action=visualizzaUtenti'">Visualizza Utenti</button>                        
                        <%
                        	}
                        %>
                    </div>
                    
                </section>
            </div>
        </main>
        <%}else{%>
              <p>Errore recupero dati dell'utente</p>                                         
        <%} %>
        
        <%@ include file="html/footer.html" %>
    </body>
</html>
