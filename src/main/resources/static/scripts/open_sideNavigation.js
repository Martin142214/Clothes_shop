
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

function makeSecondaryNavResponsive(){
  var x = document.getElementsByClassName("secondaryNav")[0];
  if (x.className === "secondaryNav") {
    x.className += " responsive";
  } else {
    x.className = "secondaryNav";
  }
}

function changeLanguage(){
  var languageSelect = document.getElementById("language");
  var lang = languageSelect.value;
  var path = defineRedirectUrl(lang);
  window.location.assign(path);
}

function defineRedirectUrl(language){
  var uri = "";
  var fullPath = "";
  var protocolName = window.location.protocol;
  var pageHostname = window.location.hostname;
  var port = window.location.port;
  var pagePathname = window.location.pathname;
  const pathWords = pagePathname.split("/");
  if (language == "english") {
    if (pathWords.includes("bg") || pathWords.includes("fr")) {
      // pattern: http: // localhost : 8080 /shoes
      uri = protocolName.concat("//", pageHostname, ":", port);
      fullPath = uri;
      for (let i = 2; i < pathWords.length; i++) {
        fullPath = fullPath.concat("/", pathWords[i]);      
      }
    }
  }
  
  else if (language == "bulgarian") {
    if (pathWords.includes("fr")) {
      uri = protocolName.concat("//", pageHostname, ":", port, "/bg");
      fullPath = uri;
      for (let i = 2; i < pathWords.length; i++) {
        fullPath = fullPath.concat("/", pathWords[i]);
      }
    }
    else if(pathWords.includes("bg")){
      fullPath = window.location.href;
    }
    else{
      fullPath = protocolName.concat("//", pageHostname, ":", port, "/bg", pagePathname);
    }
  }

  return fullPath;
}

function setLanguage(){
  var lang = "";
  var langDropdown = document.getElementById('language');
  var pagePathname = window.location.pathname;
  const pathWords = pagePathname.split("/");
  if (pathWords.includes("bg")) {
    lang = "bulgarian";
  }
  else{
    lang = "english";
  }
  for (let i = 0; i < langDropdown.length; i++) {
      if(langDropdown[i].value == lang){
          langDropdown[i].setAttribute('selected', true);
      }
  }
}