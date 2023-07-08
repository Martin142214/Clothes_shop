function searchFunction() {
    var input, filter, cards, cardBody, i, txtValue, results, noResultsTitle;
    input = document.getElementById("myInput");
    filter = input.value.toLowerCase();
    cards = document.getElementsByClassName("card");
    results = 0;
    noResultsTitle = document.getElementById("noResults");
    for (i = 0; i < cards.length; i++) {
        cardBody = cards[i].children[1];
        if(cardBody){
            txtValue = cardBody.children[0].innerText;
               if (txtValue.toLowerCase().indexOf(filter) > -1) {
                   var aLinkParentElement = cards[i].parentElement;
                   aLinkParentElement.parentElement.style.display = "block";
                   results++;
               } else {
                var aLinkParentElement = cards[i].parentElement;
                aLinkParentElement.parentElement.style.display = "none";
               }
        }
    }
    if (results == 0) {
        noResultsTitle.style.display = "block";
    }
    else{
        noResultsTitle.style.display = "none";
    }
}