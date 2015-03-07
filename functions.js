var currentWord = "sauropod";
function getCanvas() {
	return document.getElementById("webCanvas");	
}
function getCanvasContext() {
	return getCanvas().getContext("2d");
}
function loadWordWeb(wordName) {
	var test = words[wordName];
	if (test == null) {
		wordName = currentWord;
	}
	currentWord = wordName;
	var layoutName = getBestLayoutName(wordName);
	loadLayout(layoutName, wordName);
}
function getBestLayoutName(wordName) {
	
	var centralWord = words[wordName];
	var rootsCount = 0;
	for (var rootName in centralWord.roots) {
		rootsCount++;
	}
	if (rootsCount == 3) {
		return "three-roots-six-words-each";
	} else {
		return "two-roots-ten-words-each";
	}
}
function loadLayout(layoutName, wordName) {
	
	var loc = document.location.toString();
	var pos = loc.indexOf("#");
	if (pos > 0) {
		loc = loc.substring(0, pos);
	}
	loc = loc + "#" + wordName;
	document.location = loc;
	
	
	// clear all content from canvas and spans
	getCanvasContext().fillStyle = "white";
	getCanvasContext().fillRect(0, 0, getCanvas().width, getCanvas().height);
	var webSpan = document.getElementById("spanPanel");
	var searchSpan = document.getElementById("search-span");
	while (webSpan.firstChild) {
		webSpan.removeChild(webSpan.firstChild);
	}
	webSpan.appendChild(searchSpan);
	
	var parentElement = document.getElementById("webCanvas");
	var layout = layouts[layoutName];

	var centralWord = words[wordName];
	var centralWordSpan = configureSearchBox(wordName, layout);

	// add all roots for the central word
	var rootLayouts = layout.roots;
	var i = 0;
	for (var rootName in centralWord.roots) {
		var root = centralWord.roots[rootName];
		var rootLayout = rootLayouts[i];
		if (rootLayout == null) {
			break;
		}
		var theme = rootLayout.theme;
		var rootSpan = addBubble(layoutName + "-root root", theme, rootLayout);
		drawCenterLine(parentElement, rootSpan, centralWordSpan, rootSpan);
		setBubbleText(rootSpan, rootName, root.definition);
		
		var wordLayouts = rootLayout.words;
		var j = 0;
		for (var wordName in root.words) {
			var word = root.words[wordName];
			if (word != centralWord) {
				var wordLayout = wordLayouts[j];
				if (wordLayout == null) {
					break;
				}
				var wordSpan = addBubble(layoutName + "-word word", theme, wordLayout);
				drawCenterLine(parentElement, rootSpan, wordSpan, rootSpan);
				setBubbleLink(wordSpan, wordName, word.definition);
				
				j++;
			}
		}
		i++;
	}
}
function configureSearchBox(wordName, layout) {
	
	var layout = layout.centralWord;
	var centralWordSpan = document.getElementById("search-span");
	centralWordSpan.style.left = layout.position.left;
	centralWordSpan.style.top = layout.position.top;

	var word = words[wordName];
	
	var defSpan = document.getElementById("search-definition");
	defSpan.innerHTML = word.definition;

	var searchInput = document.getElementById("search");
	searchInput.value = wordName;

	return centralWordSpan;
}
function setBubbleText(bubble, word, definition) {
	bubble.innerHTML = word + "<br/><span class='definition'>" + definition + "</span>";
}
function setBubbleLink(bubble, word, definition) {
	bubble.addEventListener("click", function() { loadWordWeb(word); });
	setBubbleText(bubble, word, definition);
}
function initSearchBox() {
	var box = document.getElementById("search");
    var availableTags = [];
    var count = 0;
	for (var rootName in words) {
		availableTags[count++] = rootName;
	}
    $( "#search" ).autocomplete(
    		{
    			source: availableTags,
    			select: function( event, ui ) { loadWordWeb(ui.item.value); }
    		}
    	);
    $("#search").focus(function() {
        $(this).select();
    });
    $("#search").blur(function() {
        $(this).val(currentWord);
    });
}
function drawCenterLine(parent, fromSpan, toSpan, colorHintSpan) {
	var ctx = getCanvasContext();
	var cstyle = getComputedStyle(colorHintSpan, null);
	ctx.strokeStyle = cstyle.borderColor;
	
	var px = parent.getBoundingClientRect().left;
	var py = parent.getBoundingClientRect().top;
	
	var p1 = fromSpan.getBoundingClientRect();
	var p2 = toSpan.getBoundingClientRect();
	
	var x1 = p1.left + (p1.width / 2) - px;
	var y1 = p1.top + (p1.height / 2) - py;

	var x2 = p2.left + (p2.width / 2) - px;
	var y2 = p2.top + (p2.height / 2) - py;
	
	ctx.beginPath();
	ctx.moveTo(x1, y1);
	ctx.lineTo(x2, y2);
	ctx.stroke();
	ctx.closePath();
}
function addBubble(spanClass, theme, layout) {
	var span = document.createElement("span");
	
	span.className = spanClass + " " + theme + "-theme";
	
	span.style.left = layout.position.left;
	span.style.top = layout.position.top;
	
	var webSpan = document.getElementById("spanPanel");
    webSpan.appendChild(span);
    
    return span;
}
function initWords() {
	for (var wordName in words) {
		var word = words[wordName];
		for (i = 0; i < word.rootNames.length; i++) {
			var root = roots[word.rootNames[i]];
			if (root == null) {
				alert("couldn't find: " + word.rootNames[i]);
			}
			word.roots[word.rootNames[i]] = root;
			root.words[wordName] = word;
		}
	}
}
function initPage() {

	// select the starting word, either from default or URL#anchor
	var loc = document.location.toString();
	var pos = loc.indexOf("#");
	var wordName = currentWord;
	if (pos > 0) {
		var temp = loc.substring(pos + 1);
		if (temp.trim().length > 0) {
			wordName = temp.trim();
		}
	}
	
	initWords();
	initSearchBox();
	
	// setup the sizes
	var c = getCanvas();
	c.width = 850;
	c.height = 650;
	
	var s = document.getElementById("canvasPanel");
	s.style.width = (c.width + 12) + "px";
	s.style.height = (c.height + 12) + "px";
	
	loadWordWeb(wordName);
}
