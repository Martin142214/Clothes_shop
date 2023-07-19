let purchaseBtn = document.getElementById("purchase");
let totalPrice = document.getElementById("price-summary");

window.addEventListener("load", function () {
    if (parseInt(totalPrice.innerText) > 0) {
      //const fileSize = fileInput.files.item(0).size;
      //const fileMb = fileSize / 1024 ** 2;
        purchaseBtn.disabled = false;
        purchaseBtn.style.cursor = "pointer";
    } else {
        purchaseBtn.disabled = true;
        purchaseBtn.style.cursor = "not-allowed";
    }
  });
