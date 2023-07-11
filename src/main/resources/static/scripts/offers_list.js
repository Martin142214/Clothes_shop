function setPathAlias(){
    var aliasDiv = document.getElementsByClassName("path-alias");
    var uri = "";
    var protocolName = window.location.protocol;
    var pageHostname = window.location.hostname;
    var port = window.location.port;
    var pagePathname = window.location.pathname;
    uri = protocolName.concat("//", pageHostname, ":", port);
    var fullUrl = getCurrentURL();

    const pathWords = pagePathname.split("/");
    pathWords.shift();

    if (pathWords.includes("bg")){
        pathWords.shift();
    }

    if (fullUrl == uri) {
        aliasDiv.children.forEach(child => {
            aliasDiv[0].removeChild(child);
        });
    }
    else {
        if (pathWords.length != 0) {
            var li_first = createChild("home", uri.concat("/"));
            aliasDiv[0].appendChild(li_first);
            var currentUrl = uri;
            for (let i = 0; i < pathWords.length; i++) {
                /*if (i + 1 == pathWords.length) {
                    var li_last = document.createElement("li");
                    var a = document.createElement("a");
                    a.innerText = "/ " + pathWords[i];
                    li_last.appendChild(a);
                    //li_last.style.textDecoration = "underline";
                    li_last.style.cursor = "default";
                    aliasDiv[0].appendChild(li_last);*/
                    currentUrl = concatUrl(currentUrl, pathWords[i]);
                    var li = createChild(pathWords[i], currentUrl);
                    li.style.cursor = "alias";
                    aliasDiv[0].appendChild(li);
                }             
            }          
        }
    }

function applyProperLangUrl(shoeId){
    let currentUrl = window.location.pathname;
    let parts = currentUrl.split("/");
    if (parts[1] === "bg") {
        let aTag = document.getElementById("shoeProductLink");
        aTag.href = currentUrl.replace("get", shoeId);
    }
}


function getCurrentURL () {
    return window.location.href
}

function createChild(word, url){
    var li = document.createElement("li");
    var a = document.createElement("a");
    a.innerText = "/  " + word;
    if (url != "") {
        a.href = url;   
    }
    li.appendChild(a);
    return li;
}

function concatUrl(currentUrl, word) {
    return currentUrl.concat("/", word);
}

function applyFilterForBrand() {
    var selectedBrand, filter, cards, cardBody, i, txtValue, results, noResultsTitle;
    selectedBrand = document.getElementById("adidas_brand");
    selectedBrand.classList.add("active");
    filter = selectedBrand.innerText.toLowerCase();
    cards = document.getElementsByClassName("card");
    results = 0;
    noResultsTitle = document.getElementById("noResults");
    for (i = 0; i < cards.length; i++) {
        cardBody = cards[i].children[1];
        if(cardBody){
            txtValue = cardBody.children[0].innerText;
               if (txtValue.toLowerCase().indexOf(filter) > -1) {
                   cards[i].parentElement.style.display = "";
                   results++;
               } else {
                   cards[i].parentElement.style.display = "none";
               }
        }
    }

    var aliasDiv = document.getElementsByClassName("path-alias");
    var li = document.createElement("li");
    var a = document.createElement("a");
    a.innerText = "/ " + selectedBrand.innerText;
    li.appendChild(a);
    li.style.cursor = "text";
    aliasDiv[0].appendChild(li);

    if (results == 0) {
        noResultsTitle.style.display = "block";
    }
    else{
        noResultsTitle.style.display = "none";
    }
}

function changeImage(imageUrl) {
    var mainImageTag = document.getElementById("mainImage");
    mainImageTag.src = "/images/" + imageUrl;
}

function openFilters(){
    var filtersColumn = document.getElementById("leftColumn");
    if (filtersColumn.style.display === "none") {
        filtersColumn.style.display = "block";
    }
    else {
        filtersColumn.style.display = "none";
    }
}

/*if (window.innerWidth > 1000) {
    document.getElementById("leftColumn").style.display = "block";
}*/
  