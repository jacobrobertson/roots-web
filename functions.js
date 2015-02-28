function getCanvas() {
	return document.getElementById("webCanvas");	
}
function getCanvasContext() {
	return getCanvas().getContext("2d");
}
function loadWordWeb(wordName) {
	var layoutName = getBestLayoutName(name);
	loadLayout(layoutName, wordName);
}
function getBestLayoutName(wordName) {
	// TODO ...
	return "two-roots-ten-words-each";
}
function loadLayout(layoutName, wordName) {
	getCanvasContext().clearRect(0, 0, getCanvas().width, getCanvas().height);

	var parentElement = document.getElementById("webCanvas");
	var layout = layouts[layoutName];

	// create the center word
	// TODO get the actual text
	var centralWordLayout = layout.centralWord;
	var centralWordSpan = addBubble(layoutName + "-word word", centralWordLayout.theme, centralWordLayout);
	
	var centralWord = words[wordName];
	setBubbleText(centralWordSpan, wordName, centralWord.definition);
	
	// add all roots for the central word
	var rootLayouts = layout.roots;
	var i = 0;
	for (var rootName in centralWord.roots) {
		var root = centralWord.roots[rootName];

		var theme = rootLayouts[i].theme;
		var rootSpan = addBubble(layoutName + "-root root", theme, rootLayouts[i]);
		// drawLine(roots[i].connectLine, roots[i].connectLineColor);
		drawCenterLine(parentElement, rootSpan, centralWordSpan, rootSpan);
		setBubbleText(rootSpan, rootName, root.definition);
		
		var wordLayouts = rootLayouts[i].words;
		var j = 0;
		for (var wordName in root.words) {
			var word = root.words[wordName];
			if (word != centralWord) {
				var wordSpan = addBubble(layoutName + "-word word", theme, wordLayouts[j]);
				drawCenterLine(parentElement, rootSpan, wordSpan, rootSpan);
				setBubbleLink(wordSpan, wordName, word.definition);
				
				j++;
			}
		}
		
		i++;
	}
}
function setBubbleText(bubble, word, definition) {
	bubble.innerHTML = word + " <br/> " + definition;
}
function setBubbleLink(bubble, word, definition) {
	bubble.innerHTML = "<a href='#' onClick=\"loadWordWeb(\'" + word + "\');\">" + word + " <br/> " + definition + "</a>";
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
	
//	alert(x1 + "," + y1 + " > " + x2 + "," + y2);
	
	ctx.moveTo(x1, y1);
	ctx.lineTo(x2, y2);
	ctx.stroke();
}
function drawLine(connectLine, color) {
	var ctx = getCanvasContext();
	ctx.strokeStyle = color;
	ctx.moveTo(connectLine.x1, connectLine.y1);
	ctx.lineTo(connectLine.x2, connectLine.y2);
	ctx.stroke();
}
function addBubble(spanClass, theme, layout) {
	
	// TODO get relative x/y
	
	var span = document.createElement("span");
	
	span.className = spanClass + " " + theme + "-theme";
	
	span.style.left = layout.position.left;
	span.style.top = layout.position.top;
	
	var webSpan = document.getElementById("canvasPanel");
    webSpan.appendChild(span);
    
    return span;
}
function initWords() {
	
	/*
	var words = {
			"octopus": { "rootNames": ["octo", "pod"], "definition": "eight-footed", "roots": {} },
			"pachycephalosaurus": { "rootNames": ["pach", "cephal", "saurus"], "definition": "thick headed lizard", "roots": {} },
		};
	var roots = {
		"octo": { "definition": "eight", "words": {} },
		"pod": { "otherRoots": "pus", "definition": "foot", "words": {} },
	};
	*/
		
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
	var wordName = "octopus";
	if (pos > 0) {
		var temp = loc.substring(pos + 1);
		if (temp.trim().length > 0) {
			wordName = temp.trim();
		}
	}
	
	initWords();
	
	// setup the sizes
	var c = getCanvas();
	c.width = 850;
	c.height = 750;
	
	var s = document.getElementById("canvasPanel");
	s.style.width = (c.width + 12) + "px";
	s.style.height = (c.height + 12) + "px";
	
	loadWordWeb(wordName);
//	loadLayout("two-roots-ten-words-each");

	/*
	var ctx = getCanvasContext();
	ctx.moveTo(5,5);
	ctx.lineTo(270,176);
	ctx.stroke();
	*/

}
