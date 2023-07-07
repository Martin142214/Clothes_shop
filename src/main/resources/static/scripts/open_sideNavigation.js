
function openNav() {
  var sidenav = document.getElementById("mySidenav");
  sidenav.style.width = "250px";
  //section
  var sections = document.getElementsByTagName("section");
  for (let i = 0; i < sections.length; i++) {
    sections[i].style.filter = "blur(3px)";
    sections[i].style.pointerEvents = "none";
  }
  //header
  var headers = document.getElementsByTagName("header");
  for (let i = 0; i < headers.length; i++) {
    headers[i].style.filter = "blur(3px)";
    headers[i].style.pointerEvents = "none";
  }
  var footer = document.getElementsByTagName("footer")[0];
  footer.style.filter = "blur(3px)";
  footer.style.pointerEvents = "none";
}
  
function closeNav() {
  var sidenav = document.getElementById("mySidenav");
  sidenav.style.width = "0";
  //section
  var sections = document.getElementsByTagName("section");
  for (let i = 0; i < sections.length; i++) {
    sections[i].style.filter = "blur(0px)";
    sections[i].style.pointerEvents = "all";
  }
  //header
  var headers = document.getElementsByTagName("header");
  for (let i = 0; i < headers.length; i++) {
    headers[i].style.filter = "blur(0px)";
    headers[i].style.pointerEvents = "all";
  }
  var footer = document.getElementsByTagName("footer")[0];
  footer.style.filter = "blur(0px)";
  footer.style.pointerEvents = "all";
}