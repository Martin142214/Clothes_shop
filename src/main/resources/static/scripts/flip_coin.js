function flipCoin() {
    let coin = document.getElementById("coin");
        var flipResult = Math.random();
        //coin.className = "";
        if(coin.classList.contains("tails")){
            coin.classList.remove("tails");
            coin.classList.add('heads');
            setTimeout(function(){
                showShoesContainer();
            }, 1000);  
        }
        else{
            coin.classList.remove("heads");
            coin.classList.add('tails');
            setTimeout(function(){
                showClothesContainer();       
            }, 1000);
        }
}

function showShoesContainer() {
    var shoesContainer = document.getElementById("shoesContainer");
    var clothesContainer = document.getElementById("clothesContainer");
    //var clothesText = document.getElementsByClassName("side-b")[0];
    //clothesText.innerHTML = "";
    shoesContainer.style.display = "block";
    clothesContainer.style.display = "none";
}

function showClothesContainer() {
    var shoesContainer = document.getElementById("shoesContainer");
    var clothesContainer = document.getElementById("clothesContainer");
    //var shoesText = document.getElementsByClassName("side-a")[0];
    //shoesText.innerHTML = "";
    shoesContainer.style.display = "none";
    clothesContainer.style.display = "block";
}