let fileInput = document.getElementById("file-input");
let fileResult = document.getElementById("files-submitted");
let fileSubmit = document.getElementById("file-submit");

fileInput.addEventListener("change", function () {
    if (fileInput.files.length > 0) {
      //const fileSize = fileInput.files.item(0).size;
      //const fileMb = fileSize / 1024 ** 2;
      if (fileInput.files.length > 3) {
        fileResult.innerHTML = "Please select less than 3 image files.";
        fileSubmit.disabled = true;
      } else {
        fileResult.innerHTML = "Success, you have " + fileInput.files.length + " images selected.";
        fileSubmit.disabled = false;
      }
    }
  });

