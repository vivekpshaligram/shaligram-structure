function scrollToPosition(searchText, selectedIndex) {
    var searchTextLower = searchText.toLowerCase(); // Convert search text to lowercase
    var elements = document.evaluate('//*[text()[contains(translate(., "ABCDEFGHIJKLMNOPQRSTUVWXYZ", "abcdefghijklmnopqrstuvwxyz"), "' + searchTextLower + '")]]', document, null, XPathResult.ORDERED_NODE_SNAPSHOT_TYPE, null);
    var count = elements.snapshotLength;
    if (count > 0) {
        var element = elements.snapshotItem(selectedIndex);
        var textNode = element.firstChild;
        var text = textNode.nodeValue;
        var index = text.toLowerCase().indexOf(searchTextLower); // Convert element text to lowercase for comparison

        var range = document.createRange();
        range.setStart(element.firstChild, index);
        range.setEnd(element.firstChild, index + searchTextLower.length);

        window.getSelection().removeAllRanges();
        window.getSelection().addRange(range);
        scrollToNodeOrRange(textNode);
//        var rect = element.getBoundingClientRect();
//        window.scrollTo(0, rect.top - 100);
        console.log("Scrolled to text position of one element containing '" + searchText + "' and highlighted it");
    } else {
        console.error("Text '" + searchText + "' not found");
    }
}

function findOccurrencesInDivs(searchText) {
    var occurrences = [];
    var divs = document.getElementsByTagName('div');
    var searchTextLower = searchText.toLowerCase(); // Convert searchText to lowercase
    var scrollTop = window.pageYOffset || document.documentElement.scrollTop; // Get the current scroll position
    for (var i = 0; i < divs.length; i++) {
        var div = divs[i];
        var text = (div.innerText || div.textContent).toLowerCase(); // Convert text within the div to lowercase
        var index = text.indexOf(searchTextLower);
        while (index !== -1) {
            var prefixStart = Math.max(0, index - 20); // Adjust prefix length as needed
            var postfixEnd = Math.min(text.length, index + searchTextLower.length + 20); // Adjust postfix length as needed
            var prefix = text.substring(prefixStart, index);
            var postfix = text.substring(index + searchTextLower.length, postfixEnd);
            var chapterPageNumber = 1
            occurrences.push({ divId: div.id, prefix: prefix, highlight: searchTextLower, postfix: postfix, chapterPageNumber: chapterPageNumber });
            index = text.indexOf(searchTextLower, index + 1);
        }
    }
    console.log("occurrences::"+ JSON.stringify(occurrences));

    return occurrences;
}
