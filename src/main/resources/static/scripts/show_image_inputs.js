function showOneImageInput(){
    var manyImagesLabel = document.getElementById("manyImagesLabel");
    manyImagesLabel.style.display = "none";
    var manyImagesNoteLabel = document.getElementById("noteLabel");
    manyImagesNoteLabel.style.display = "none";
    var manyImagesInput = document.getElementById("manyImagesInput");
    manyImagesInput.style.display = "none";
    manyImagesInput.value = "";

    //var oneImageInput = document.getElementById("oneImageInput");
    //oneImageInput.style.display = "block";
    //var oneImageLabel = document.getElementById("oneImageLabel");
    //oneImageLabel.style.display = "block";

}


function showManyImagesInput(){
    var manyImagesNoteLabel = document.getElementById("noteLabel");
    manyImagesNoteLabel.style.display = "block";
    //var oneImageLabel = document.getElementById("oneImageLabel");
    //oneImageLabel.style.display = "none";
    //oneImageInput.value = "";

    var manyImagesLabel = document.getElementById("manyImagesLabel");
    manyImagesLabel.style.display = "block";
    var manyImagesInput = document.getElementById("manyImagesInput");
    manyImagesInput.style.display = "block";
}