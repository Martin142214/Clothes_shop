function searchFunction() {
    var input, filter, cards, cardBody, i, txtValue, results, shoesDeals, clothesDeals, noResultsTitle;
    input = document.getElementById("myInput");
    filter = input.value.toLowerCase();
    cards = document.getElementsByClassName("card");
    results = 0;
    //shoesDeals = document.getElementById("shoesDeals");
    //clothesDeals = document.getElementById("clothesDeals");
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
    if (results == 0) {
        //shoesDeals.style.display = "none";
        //clothesDeals.style.display = "none";
        noResultsTitle.style.display = "block";
    }
    else{
        //shoesDeals.style.display = "block";
        //clothesDeals.style.display = "block";
        noResultsTitle.style.display = "none";
    }
}