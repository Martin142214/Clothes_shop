function openShoeEditModal(id, brand, model, price, releaseDate, mainImageUrl) {
    document.getElementById('id02').style.display='block';
    const text = brand.toString();
    var brandSelect = document.getElementById('brandOptions');
    var options = Array.from(brandSelect.options);
    var optionToSelect = options.find(item => item.text === text);
    //brandSelect.selectedIndex = optionToSelect.index;
    brandSelect.selectedOptions[0] = optionToSelect;
    document.getElementById('model').value = model;
    document.getElementById('price').value = price;
    document.getElementById('releaseDate').value = releaseDate;
    /*document.getElementById('shiftOptions').value = shift;
    document.getElementById('numberOfDoors').value = numberOfDoors;
    var colorDropdown = document.getElementById('colorOptions');
    for (let i = 0; i < colorDropdown.length; i++) {
        if(colorDropdown[i].value == color){
            colorDropdown[i].setAttribute('selected', true);
        }
    }
    color.value = color;
    color.innerText = color; */
    pathToImages = mainImageUrl.split("/");
    var currentImage = pathToImages.pop();
    document.getElementById('imageUrl').innerHTML = currentImage;
    document.getElementById('modalShoeEditForm').setAttribute("action", "/shoes/" + id + "/edit");
}

function openShoeDeleteModal(id, brand, model, releaseDate, colorSpecification, mainImageUrl){
    document.getElementById('id03').style.display='block';
    document.getElementById('description').innerHTML = brand + " " + model + "<br> Release date: " + releaseDate + "<br> Color specification: " + colorSpecification;
    document.getElementById('shoeImage').src = "/images/" + mainImageUrl;
    document.getElementById('modalShoeDeleteForm').setAttribute("action", "/shoes/" + id + "/delete");
}