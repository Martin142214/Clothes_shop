function setCardsData(entities) {
    const shoesObject = [];

    entities.forEach(entity => {
        shoesObject.push({
           "id": entity.id,
           "brand": entity.brand,
           "model": entity.model,
           "releaseDate": entity.releaseDate,
           "price": entity.price,
           "image": entity.mainImageUrl
        });
        while (shoesObject.length < entities.length) {
            return;
        }
    });

    const cardContainer = document.getElementById("card-container");
    const loadMoreButton = document.getElementById("load-more");
    //const cardCountElem = document.getElementById("card-count");
    //const cardTotalElem = document.getElementById("card-total");
    
    const cardLimit = shoesObject.length;
    const cardIncrease = 12;
    const pageCount = Math.ceil(cardLimit / cardIncrease);
    let currentPage = 1;

    addCards(currentPage, cardLimit, cardIncrease, pageCount, loadMoreButton, cardContainer, shoesObject);

    loadMoreButton.addEventListener("click", () => {
        addCards(currentPage + 1, cardLimit, cardIncrease, pageCount, loadMoreButton, cardContainer, shoesObject);
        alignCards();
    });
}

const addCards = (pageIndex, cardLimit, cardIncrease, pageCount, loadMoreButton, cardContainer, shoesObject) => {
  currentPage = pageIndex;

  handleButtonStatus(pageCount, currentPage, loadMoreButton);

  const startRange = (pageIndex - 1) * cardIncrease;
  const endRange =
    pageIndex * cardIncrease > cardLimit ? cardLimit : pageIndex * cardIncrease;
  
  //cardCountElem.innerHTML = endRange;
  let cardContainerInnerHtml = "";

  for (let i = startRange + 1; i <= endRange; i++) {
    createCard(cardContainer, shoesObject[i - 1]);
  }
  
};

//cardTotalElem.innerHTML = cardLimit;

const getRandomColor = () => {
  const h = Math.floor(Math.random() * 360);

  return `hsl(${h}deg, 90%, 85%)`;
};

const handleButtonStatus = (pageCount, currentPage, loadMoreButton) => {
  if (pageCount === currentPage || pageCount == 0) {
    //loadMoreButton.classList.add("disabled");
    //loadMoreButton.setAttribute("disabled", true);
    loadMoreButton.style.display = "none";
  }
};

const createCard = (cardContainer, shoe) => {
  
  /*cardContainerInnerHtml += 
  `<div class="col-12 col-md-6 col-lg-4 mt-5 mb-4" style="align-items: stretch;">
    <a href="/shoes/${shoe.id}">
        <div class="card">
            <div class="card-header">
                <img src="/images/${shoe.mainImageUrl}" alt="image for shoe" style="float: right">
            </div>
            <div class="card-body" style="background-color:white">
                <p>${shoe.brand + ' ' + shoe.model}</p>
                <p class="card-text" >ReleaseDate: ${shoe.releaseDate}</p>
                <p class="card-text">Price: ${shoe.price}$</p>
            </div>
        </div>
    </a>
  </div>`*/
  const cardSection = document.createElement("div");
  cardSection.className = "col-12 col-md-6 col-lg-4 mt-5 mb-4";
  cardSection.style.alignItems = "stretch";
  
  const aHref = document.createElement("a");
  aHref.href = "/shoes/" + shoe.id;
  
  const card = document.createElement("div");  
  card.className = "card";
  
  const cardHeader = document.createElement("div"); 
  cardHeader.className = "card-header";
  
  var imgForHeader = document.createElement("img"); 
  imgForHeader.src = "/images/" + shoe.image;

  const cardBody = document.createElement("div"); 
  cardBody.className = "card-body";

  const cardBodyTextForBrand = document.createElement("p"); 
  cardBodyTextForBrand.className = "card-text";
  cardBodyTextForBrand.innerText = shoe.brand + " " + shoe.model;

  const cardBodyTextForReleaseDate = document.createElement("p"); 
  cardBodyTextForReleaseDate.className = "card-text";
  cardBodyTextForReleaseDate.innerText = "Release date: " + shoe.releaseDate;

  const cardBodyTextForPrice = document.createElement("p"); 
  cardBodyTextForPrice.className = "card-text";
  cardBodyTextForPrice.innerText = "Price: " + shoe.price + "$";

  cardHeader.appendChild(imgForHeader);
  cardBody.appendChild(cardBodyTextForBrand);
  cardBody.appendChild(cardBodyTextForReleaseDate);
  cardBody.appendChild(cardBodyTextForPrice);
  card.appendChild(cardHeader);
  card.appendChild(cardBody);
  aHref.appendChild(card);
  cardSection.appendChild(aHref);
  cardContainer.appendChild(cardSection);
};

window.addEventListener("resize", () => {
  alignCards();
})

function alignCards(){
  var cards = document.getElementsByClassName("card");
  for (i = 0; i < cards.length; i++) {
      cards[i].style.height = "-webkit-fill-available";
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


/*window.onload = function () {
  addCards(currentPage);
  loadMoreButton.style.backgroundColor = getRandomColor();
  loadMoreButton.addEventListener("click", () => {
    addCards(currentPage + 1);
  });
}; */