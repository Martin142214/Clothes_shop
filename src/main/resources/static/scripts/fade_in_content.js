/*function Utils() {}
Utils.prototype = {
  constructor: Utils,
  isElementInView: function (element, fullyInView) {
    var pageTop = window.scrollY;
    var pageBottom = pageTop + window.innerHeight;
    var elementTop = element.offsetTop;
    var elementBottom = elementTop + element.innerHeight;

    if (fullyInView === true) {
      return pageTop < elementTop && pageBottom > elementBottom;
    } else {
      return elementTop <= pageBottom && elementBottom >= pageTop;
    }
  }
};*/
//window.addEventListener("load", addFadeIn());

//window.onscroll = function() {addFadeIn()};

function addFadeIn(className) {
  var classToFadeIn = document.querySelectorAll(`${className}`);

  classToFadeIn.forEach(function (value, index) {
    var isElementInV = isElementInView(value, false);
    if (isElementInV) {
        if (!value.classList.contains("fadeInRight") || !value.classList.contains("fadeInLeft")) {
          if (index % 2 == 0) {
            if (index > 1) {
              value.classList.add("fadeInRight");
            }
            else {
              value.classList.add("fadeInLeft");
            }
          } 
          else {
            if (index > 1) {
              value.classList.add("fadeInLeft");
            }
            else {
              value.classList.add("fadeInRight");
            }
          } 
        }
    }
  });
}

function isElementInView(element, fullyInView) {
  var pageTop = window.scrollY;
  var pageBottom = pageTop + window.innerHeight;
  var elementTop = element.offsetTop;
  var elementBottom = elementTop + element.offsetHeight;

  if (fullyInView === true) {
    return pageTop < elementTop && pageBottom > elementBottom;
  } else {
    return elementTop <= pageBottom && elementBottom >= pageTop;
  }
}

//window.scroll(function () {
 // addFadeIn();
//});

