<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8" 
import="java.util.*,Model.Bean.ProductBean,Model.CartModel"%>

<!DOCTYPE html>
<html>

<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link href="./style/HomeST.css" rel="stylesheet" type="text/css">
	<link href="./style/menu-style.css" rel="stylesheet" type="text/css">
	<!-- Footer -->
	<link rel="stylesheet" type="text/css" href="./style/footer.css">
	
	<title>Beauty Blind</title>
</head>

<body>

        <%@ include file="./html/menu.html" %>

        <div id="cards-container">
		<h1><a href="/admin/prova.html"></a></h1>
            <%
                Collection<?> products = (Collection<?>) request.getAttribute("products");
				int i = 0;
				
	            if(products == null) {
	        		System.out.println("NIENTE PRODOTTI");
	        		
	        		// si potrebbe aggiungere error page
	        		response.sendRedirect("./");	
	        		return;
	        	}

                if (products != null && !products.isEmpty()) {

                    Iterator<?> it = products.iterator();
                    
					//System.out.println("ciao 1384636");
					
                    while (it.hasNext()) {
                        //System.out.println("ciao nel while");
                        ProductBean bean = (ProductBean) it.next();
            %>
                        <div class="card" id="c<%=i%>">

                            <div class="card-container-up" onclick="window.location.href='?action=read&ProductID=<%=bean.getID()%>';">

								<div class="new-banner">NEW</div>
								<div class="BackBanner"></div>

                                <div class="image">
                                    <img class="immagine-principale" src="./getPicture?IDpic=<%=bean.getID()%>&ruolo=principale" alt="Immagine di: '<%=bean.getDescrizione()%>'">
		   							<img class="immagine-secondaria" src="./getPicture?IDpic=<%=bean.getID()%>&ruolo=secondaria"  onerror="this.src='./img/beauty.png'"  alt="Immagine di: '<%=bean.getDescrizione()%>'">        
                                </div>

                                <div class="name">
                                    <h2><%=bean.getNome()%></h2>
                                </div>
                                
                                <div class = "stars" id="s<%=i%>">
                           
                                	<svg width="20" height="20" viewBox="0 0 50 50">
									    <polygon points="25,1 32,18 50,18 36,29 40,46 25,36 10,46 14,29 1,18 18,18" fill="white" stroke="black"/>
									</svg>
									
									<svg width="20" height="20" viewBox="0 0 50 50">
									    <polygon points="25,1 32,18 50,18 36,29 40,46 25,36 10,46 14,29 1,18 18,18" fill="white" stroke="black"/>
									</svg>
									
									<svg width="20" height="20" viewBox="0 0 50 50">
									    <polygon points="25,1 32,18 50,18 36,29 40,46 25,36 10,46 14,29 1,18 18,18" fill="white" stroke="black"/>
									</svg>
									
									<svg width="20" height="20" viewBox="0 0 50 50">
									    <polygon points="25,1 32,18 50,18 36,29 40,46 25,36 10,46 14,29 1,18 18,18" fill="white" stroke="black"/>
									</svg>
									
									<svg width="20" height="20" viewBox="0 0 50 50">
									    <polygon points="25,1 32,18 50,18 36,29 40,46 25,36 10,46 14,29 1,18 18,18" fill="white" stroke="black"/>
									</svg>
									
									<!--come su amazon questo link potrebbe portare alle recensioni del prodotto -->
									<span class="Nrecensioni"><strong><a>(<%=bean.getNrecensioni()%> voti)</a></strong></span>
									
									<div class="info">
								        <h3><%= String.format("%.1f", bean.getVotazione()) %> su 5 stelle</h3>
								    </div>
									
                                </div>
                                
                                <div class="grammatura">
                                    <h6><%=bean.getGrammatura()%> ml</h6>
                                </div>
                            </div>

                            <div class="card-container-low">

                                <div class="price">
					    <% if(bean.getPercentualeSconto() != 0) { %>
					        <span class="prezzo-scontato">
					            <%= bean.getPrezzo() - ((bean.getPrezzo() * bean.getPercentualeSconto()) / 100) %> &euro;
					        </span>
					        <mark>-<%= bean.getPercentualeSconto() %> %</mark>
					    <% } else { %>
					        <span class="prezzo-normale">
					            <%= bean.getPrezzo() %> &euro;
					        </span>
					    <% } %>   	                                
                                </div>

                                <div class="visualizza">
                                    <a href="/Cart?action=aggiungi&id=<%=bean.getID()%>">ADD TO CART</a>
                                </div>
                            </div>

                        </div>
<%						i++;
                    }
                }
%>
        </div>
        
       	<script type="text/javascript" src="./javascript/HomeAnimations.js"></script>
       	<script type="text/javascript" href="./javascript/barraDiRicerca.js"></script> 
	    
	<%@ include file="html/footer.html" %>     
    </body>
</html>

