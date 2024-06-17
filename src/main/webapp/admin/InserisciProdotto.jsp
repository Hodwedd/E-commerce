<html>
    <head>
        <title>Title</title>
        <link href="../style/menu-style.css" rel="stylesheet" type="text/css">
        <link href="../style/Inserimento-prodotti.css" rel="stylesheet" type="text/css">
        <!-- Footer -->
        <link rel="stylesheet" type="text/css" href="../style/footer.css">    
    </head>
    <body>
       <%@ include file="../html/menu.html" %>
       <div class="container">
            <h2>Inserimento Prodotto Makeup</h2>
            <form class="form-inserimento-prodotti" action="<%=request.getContextPath()%>/Admin" method="post" enctype="multipart/form-data">
                <label for="ID">ID</label>
                <input type="text" id="ID" name="ID" required>
                
                <label for="nome">Nome</label>
                <input type="text" id="nome" name="nome" required>
                
                <label for="descrizione">Descrizione</label>
                <textarea id="descrizione" name="descrizione" rows="4" required></textarea>
                
                <label for="prezzo">Prezzo</label>
                <input type="number" id="prezzo" name="prezzo" step="0.01" required>
                
                <label for="grammatura">Grammatura</label>
                <input type="text" id="grammatura" name="grammatura" required>
                
                <label for="categoria">Categoria</label>
                <input type="text" id="categoria" name="categoria" required>
                
                <label for="QTAmagazzino">QTA Magazzino</label>
                <input type="number" id="QTAmagazzino" name="QTAmagazzino" required>
                
                <label for="percentualeSconto">Percentuale Sconto</label>
                <input type="number" id="percentualeSconto" name="percentualeSconto" step="0.01">
                
                <label for="colorazione">Colorazione</label>
                <input type="text" id="colorazione" name="colorazione" required>

                <label for="immagine1">Immagine Principale [png,jpg]</label>
                <input type="file" id="immagine1" name="immagine1" required>

                <label for="immagine2">Immagine Secondaria [png,jpg]</label>
                <input type="file" id="immagine2" name="immagine2" required>
                
                <input type="hidden" id="action" name="action" value="inserisciProdotto" required>
                
                <input type="submit" value="Inserisci Prodotto">
            </form>
            
        </div>
        <%@ include file="../html/footer.html" %>
        <script type="text/javascript" src="../javascript/barraDiRicerca.js"></script> 
                
    </body>
</html>
