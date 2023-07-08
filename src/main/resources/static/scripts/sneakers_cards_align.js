window.addEventListener("resize", () => {
    alignCards();
})

function alignCards(){
    var cards = document.getElementsByClassName("card");
    for (i = 0; i < cards.length; i++) {
        var isFirefox = typeof InstallTrigger !== 'undefined';
        if (isFirefox) {
            cards[i].style.height = "-moz-available";
        }
        else {
            cards[i].style.height = "-webkit-fill-available";
        }
    }

    var maxHeight = 0;
    for (i = 0; i < cards.length; i++) {
        var compStyles = window.getComputedStyle(cards[i]);
        var height = compStyles.getPropertyValue("height");
        if (i == 0) {
            maxHeight = height;
        }
        if (height > maxHeight) {
            maxHeight = height;
        }
    }

    for (i = 0; i < cards.length; i++) {
        cards[i].style.height = maxHeight;
    }
}