var direction = "VERTICAL";
var content;
var pageHeight;
var pageWidth;
var totalPages = 0;
var currentPageNumber = 0;

document.addEventListener('DOMContentLoaded', function() {
  console.log("DOMContentLoaded");
  direction = FolioWebView.getDirection();
});

function loadVerticalData() {
    direction = FolioWebView.getDirection();
    content = document.getElementById('content');
    pageHeight = content.clientHeight;
    pageWidth = content.clientWidth;

    document.addEventListener('scroll', function() {
        var scrollTop = content.scrollTop;
        currentPageNumber = Math.ceil(scrollTop / pageHeight);
        EpubFragment.setCurrentPageNumber(currentPageNumber);
    });

    function findVerticalTotalPages() {
        var totalContentHeight = content.scrollHeight;
        totalPages = Math.ceil(totalContentHeight / pageHeight);
        EpubFragment.setTotalPages(totalPages);
    }

    function findVerticalPageNumber(divId) {
        var targetDiv = document.getElementById(divId);
        if (targetDiv) {
            var targetDivTop = targetDiv.offsetTop;
            var pageNumber = Math.ceil(targetDivTop / pageHeight);
            return pageNumber;
        } else {
            return -1;
        }
    }

    findVerticalTotalPages();
    findVerticalPageNumber('Untitled-1-6');
}

function loadHorizontalData() {

     console.log("loadHorizontalData");

     direction = FolioWebView.getDirection();
     content = document.getElementById('content');
     pageHeight = content.clientHeight;
     pageWidth = content.clientWidth;

     document.addEventListener('scroll', function() {
         var containerWidth = content.clientWidth;
         var totalWidth = content.scrollWidth;
         var maxScroll = totalWidth - containerWidth;
         var scrollLeft = maxScroll - content.scrollLeft;
         currentPageNumber = Math.floor(scrollLeft / containerWidth) + 1;
         currentPageNumber = currentPageNumber - totalPages;
         EpubFragment.setCurrentPageNumber(currentPageNumber);
     });

     function findHorizontalTotalPages() {
         var totalContentWidth = content.scrollWidth;
         totalPages = Math.ceil(totalContentWidth / pageWidth);
         EpubFragment.setTotalPages(totalPages);
     }

     function findHorizontalPageNumber(divId) {
       var targetDiv = document.getElementById(divId);
       if (targetDiv) {
         var targetDivLeft = targetDiv.offsetLeft;
         var pageNumber = Math.floor(targetDivLeft / pageWidth) + 1;
         pageNumber = totalPages - pageNumber;
         console.log('Div with id', divId, 'is on page number:', pageNumber);
       }
     }

     findHorizontalTotalPages();
     findHorizontalPageNumber('Untitled-1-6');
}


function redirectToPage(pageNumber) {
  if (direction == "VERTICAL") {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
      var targetScrollPosition = (pageNumber - 1) * pageHeight;
      content.scrollTo({
        top: targetScrollPosition
      });
    } else {
      console.error('Page number out of range.');
    }
  } else {
    if (pageNumber >= 1 && pageNumber <= totalPages) {
        const scrollDistance = (pageNumber - 1) * pageWidth;
        content.scrollTo({
            top: 0,
            left: -scrollDistance
          });
    } else {
       console.error('Page number out of range.');
    }
  }
}