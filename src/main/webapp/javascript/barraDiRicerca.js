function riempiRisultati(prodotti) {
    let contenuto = "";
    for (let i = 0; i < prodotti.length; i++) {
        contenuto += '<a id="singolo-risultato" href="/?action=read&ProductID=' + prodotti[i].Id + '">'
                    + '<img src="../getPicture?IDpic=' + prodotti[i].Id + '&ruolo=principale" onerror="this.src=\'./img/beauty.png\'">'
                    + '<span>' + prodotti[i].nome + '</span>'
                    + '</a>';
    }

    console.log(contenuto);
    document.getElementById("risultatiRicerca").innerHTML = contenuto;
    document.getElementById("risultatiRicerca").style.display = "block";
}

function attendiECerca() {
    setTimeout(getSpecificheProdotti, 200);
}

function getSpecificheProdotti() {
    let request = new XMLHttpRequest();
    let input = document.getElementById("barraRicerca").value;

    if (input === "") {
        nascondiRisultatiRicerca();
        return;
    }

    request.onreadystatechange = function() {
        console.log("READYSTATE: " + request.readyState);

        if (request.readyState === XMLHttpRequest.DONE) {
            if (request.status !== 200) {
                console.error("Errore nella richiesta: " + request.status);
                return;
            }

            try {
                let prodotti = JSON.parse(request.responseText);
                riempiRisultati(prodotti);
            } catch (e) {
                console.error("Errore nel parsing della risposta JSON: " + e);
            }
        }
    };

    request.open("GET", "../?action=dettagliBarraRicercaJSON&input=" + encodeURIComponent(input), true);
    request.send();
}

function nascondiRisultatiRicerca() {
    setTimeout(() => {
        document.getElementById("risultatiRicerca").innerHTML = "";
        document.getElementById("risultatiRicerca").style.display = "none";
    }, 200);
}
