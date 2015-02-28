var roots = {
	"aero": { "definition": "air", "words": {} },
	"aerostatic": { "definition": "air not changing", "words": {} },
	"anthrop": { "definition": "human", "words": {} },
	"anti": { "definition": "against; preventing", "words": {} },
	"aqua": { "definition": "water", "words": {} },
	"arthr": { "definition": "jointed", "words": {} },
	"bi": { "definition": "two", "words": {} },
	"bio": { "definition": "life", "words": {} },
	"blenn": { "definition": "slime", "words": {} },
	"brachio": { "definition": "arm", "words": {} },
	"bronch": { "definition": "windpipe", "words": {} },
	"bronto": { "definition": "thunder", "words": {} },
	"cephal": { "definition": "head", "words": {} },
	"cerat": { "definition": "horn", "words": {} },
	"chir": { "definition": "hand", "words": {} },
	"chlor": { "definition": "green", "words": {} },
	"chrom": { "definition": "color", "words": {} },
	"cide": { "definition": "act of killing; killer", "words": {} },
	"claus": { "definition": "shut; closed", "words": {} },
	"con": { "definition": "with)  (together", "words": {} },
	"crypt": { "definition": "hidden; vault", "words": {} },
	"cycl": { "definition": "circular", "words": {} },
	"dactyl": { "definition": "finger; toe", "words": {} },
	"dec": { "definition": "ten", "words": {} },
	"derm": { "definition": "skin", "words": {} },
	"dino": { "definition": "terrifying", "words": {} },
	"dyna": { "definition": "power; motion; energy", "words": {} },
	"dys": { "definition": "ill; bad", "words": {} },
	"en": { "definition": "in; with", "words": {} },
	"entero": { "definition": "intestine", "words": {} },
	"ex": { "definition": "out; from", "words": {} },
	"frater": { "definition": "brother", "words": {} },
	"funct": { "definition": "do", "words": {} },
	"gamy": { "definition": "marriage", "words": {} },
	"gastr": { "definition": "stomach", "words": {} },
	"geno": { "definition": "culture; race", "words": {} },
	"gon": { "definition": "angle", "words": {} },
	"graph": { "definition": "draw; write", "words": {} },
	"hal": { "definition": "to breath", "words": {} },
	"hedron": { "definition": "face", "words": {} },
	"helico": { "definition": "spiral", "words": {} },
	"hex": { "definition": "six", "words": {} },
	"hom": { "definition": "same", "words": {} },
	"homin": { "definition": "human", "words": {} },
	"hydrochloric": { "definition": "green acid water", "words": {} },
	"hypn": { "definition": "sleep", "words": {} },
	"iatry": { "definition": "healing", "words": {} },
	"itis": { "definition": "inflammation", "words": {} },
	"logy": { "definition": "any branch of science", "words": {} },
	"mancy": { "definition": "divination", "words": {} },
	"mar": { "definition": "sea", "words": {} },
	"merg": { "definition": "plunge", "words": {} },
	"meta": { "definition": "change; alteration", "words": {} },
	"mon": { "definition": "one", "words": {} },
	"morph": { "definition": "shape", "words": {} },
	"necr": { "definition": "dead", "words": {} },
	"nex": { "definition": "nect (join; tie", "words": {} },
	"oct": { "definition": "eight", "words": {} },
	"ops": { "definition": "face", "words": {} },
	"ornith": { "definition": "bird", "words": {} },
	"osis": { "definition": "state of being; abnormal condition; action", "words": {} },
	"pach": { "definition": "thick", "words": {} },
	"penta": { "definition": "five", "words": {} },
	"phag": { "definition": "eat", "words": {} },
	"phob": { "definition": "irrational fear", "words": {} },
	"phon": { "definition": "sound", "words": {} },
	"photo": { "definition": "light", "words": {} },
	"pod": { "definition": "foot", "words": {} },
	"polien": { "definition": "to sell", "words": {} },
	"poly": { "definition": "many", "words": {} },
	"psych": { "definition": "mind", "words": {} },
	"pter": { "definition": "wing", "words": {} },
	"rex": { "definition": "king", "words": {} },
	"saurus": { "definition": "lizard", "words": {} },
	"sinus": { "definition": "hollow", "words": {} },
	"soror": { "definition": "sister", "words": {} },
	"stasis": { "definition": "standing; still; stopped", "words": {} },
	"steg": { "definition": "covering; roof", "words": {} },
	"sub": { "definition": "below", "words": {} },
	"syn": { "definition": "together; with", "words": {} },
	"tend": { "definition": "strech; strain", "words": {} },
	"the": { "definition": "put", "words": {} },
	"ther": { "definition": "animal; beast", "words": {} },
	"tom": { "definition": "cut; insicion", "words": {} },
	"trema": { "definition": "hole", "words": {} },
	"tri": { "definition": "three", "words": {} },
	"un": { "definition": "one", "words": {} },
	"verse": { "definition": "turn", "words": {} },
	"zo": { "definition": "animal; living being", "words": {} },
};

var words = {
	"aerobiosis": { "rootNames": [ "aero", "bio", ], "definition": "life in an environment with air", "roots": {} },
	"aerodynamic": { "rootNames": [ "aero", "dyna", ], "definition": "air in motion", "roots": {} },
	"aerosinusitis": { "rootNames": [ "aero", "itis", "sinus", ], "definition": "air causes inflammation to sinuses", "roots": {} },
	"aerostatic": { "rootNames": [ "aero", ], "definition": "air not changing", "roots": {} },
	"anthropomancy": { "rootNames": [ "anthrop", "mancy", ], "definition": "human sacrifice divintation", "roots": {} },
	"anthropomorphic": { "rootNames": [ "anthrop", "morph", ], "definition": "resembling human", "roots": {} },
	"anthropophagy": { "rootNames": [ "anthrop", "phag", ], "definition": "eating humans", "roots": {} },
	"antibiotic": { "rootNames": [ "anti", "bio", ], "definition": "preventing life", "roots": {} },
	"aquamarine": { "rootNames": [ "aqua", "mar", ], "definition": "sea water", "roots": {} },
	"aquaphobia": { "rootNames": [ "aqua", "phob", ], "definition": "fear of water", "roots": {} },
	"arthritis": { "rootNames": [ "arthr", "itis", ], "definition": "joint inflammation", "roots": {} },
	"arthropod": { "rootNames": [ "arthr", "pod", ], "definition": "jointed foot", "roots": {} },
	"bicycle": { "rootNames": [ "bi", "cycl", ], "definition": "two circles", "roots": {} },
	"biocide": { "rootNames": [ "bio", "cide", ], "definition": "killing life", "roots": {} },
	"biography": { "rootNames": [ "bio", "graph", ], "definition": "writing of your life", "roots": {} },
	"biology": { "rootNames": [ "bio", "logy", ], "definition": "study of life", "roots": {} },
	"bipod": { "rootNames": [ "bi", "pod", ], "definition": "two feet", "roots": {} },
	"blennadenitis": { "rootNames": [ "blenn", "itis", ], "definition": "inflammation of mucas glands", "roots": {} },
	"blennophobia": { "rootNames": [ "blenn", "phob", ], "definition": "fear of slime", "roots": {} },
	"blennostasis": { "rootNames": [ "blenn", "stasis", ], "definition": "suppression of mucas", "roots": {} },
	"brachiosaurus": { "rootNames": [ "brachio", "saurus", ], "definition": "armed lizard", "roots": {} },
	"bronchitis": { "rootNames": [ "bronch", "itis", ], "definition": "windpipe inflammation", "roots": {} },
	"bronchotomy": { "rootNames": [ "bronch", "tom", ], "definition": "windpipe insicion", "roots": {} },
	"brontology": { "rootNames": [ "bronto", ], "definition": "study of thunder", "roots": {} },
	"brontophobia": { "rootNames": [ "bronto", "phob", ], "definition": "fear of thunder", "roots": {} },
	"brontosaurus": { "rootNames": [ "bronto", "saurus", ], "definition": "thunder lizard", "roots": {} },
	"cephalomancy": { "rootNames": [ "cephal", "mancy", ], "definition": "head divination", "roots": {} },
	"cephalopod": { "rootNames": [ "cephal", "pod", ], "definition": "head foot", "roots": {} },
	"ceratosaurus": { "rootNames": [ "cerat", "saurus", ], "definition": "horned lizard", "roots": {} },
	"chiromancy": { "rootNames": [ "chir", "mancy", ], "definition": "hand divination", "roots": {} },
	"chiropter": { "rootNames": [ "chir", "pter", ], "definition": "hand wing", "roots": {} },
	"chlorosis": { "rootNames": [ "chlor", "osis", ], "definition": "having green skin", "roots": {} },
	"claustrophobia": { "rootNames": [ "claus", "phob", ], "definition": "fear of closed spaces", "roots": {} },
	"connect": { "rootNames": [ "con", "nex", ], "definition": "join together", "roots": {} },
	"contend": { "rootNames": [ "con", "tend", ], "definition": "with strain", "roots": {} },
	"cryptography": { "rootNames": [ "crypt", "graph", ], "definition": "hidden writing", "roots": {} },
	"cyclops": { "rootNames": [ "cycl", "ops", ], "definition": "circular face", "roots": {} },
	"dactylology": { "rootNames": [ "dactyl", "logy", ], "definition": "study of fingers; sign language", "roots": {} },
	"decahedron": { "rootNames": [ "dec", "hedron", ], "definition": "ten face", "roots": {} },
	"dermatology": { "rootNames": [ "derm", "logy", ], "definition": "study of skin", "roots": {} },
	"dermatosis": { "rootNames": [ "derm", "osis", ], "definition": "having any skin disease", "roots": {} },
	"dinosaur": { "rootNames": [ "dino", "saurus", ], "definition": "terrifying lizard", "roots": {} },
	"dysfunction": { "rootNames": [ "dys", "funct", ], "definition": "functioning badly", "roots": {} },
	"dysmorphic": { "rootNames": [ "dys", "morph", ], "definition": "badly shaped", "roots": {} },
	"dysphonia": { "rootNames": [ "dys", "phon", ], "definition": "bad sound", "roots": {} },
	"enterobacteria": { "rootNames": [ "entero", ], "definition": "intestine bacteria", "roots": {} },
	"enterology": { "rootNames": [ "entero", "logy", ], "definition": "study of intestine", "roots": {} },
	"entomophagous": { "rootNames": [ "en", "phag", "tom", ], "definition": "eating insects", "roots": {} },
	"exclude": { "rootNames": [ "claus", "ex", ], "definition": "shut out", "roots": {} },
	"extend": { "rootNames": [ "ex", "tend", ], "definition": "stretchout", "roots": {} },
	"extrusion": { "rootNames": [ "ex", ], "definition": "thrust out", "roots": {} },
	"fraticide": { "rootNames": [ "cide", "frater", ], "definition": "killing brother", "roots": {} },
	"gastrectomy": { "rootNames": [ "gastr", "tom", ], "definition": "surgical stomach removal", "roots": {} },
	"gastritis": { "rootNames": [ "gastr", "itis", ], "definition": "stomach inflammation", "roots": {} },
	"gastroenterology": { "rootNames": [ "entero", "gastr", "logy", ], "definition": "stomach intestine study", "roots": {} },
	"gastropod": { "rootNames": [ "gastr", "pod", ], "definition": "stomach foot", "roots": {} },
	"genocide": { "rootNames": [ "cide", "geno", ], "definition": "killing a race", "roots": {} },
	"halitosis": { "rootNames": [ "hal", "osis", ], "definition": "state of having bad breath", "roots": {} },
	"helicograph": { "rootNames": [ "graph", "helico", ], "definition": "instument to draw spirals", "roots": {} },
	"helicopter": { "rootNames": [ "helico", "pter", ], "definition": "spiral wing", "roots": {} },
	"hexagon": { "rootNames": [ "gon", "hex", ], "definition": "six angle", "roots": {} },
	"hexahedron": { "rootNames": [ "hedron", "hex", ], "definition": "six face", "roots": {} },
	"homicide": { "rootNames": [ "cide", "homin", ], "definition": "human killing", "roots": {} },
	"homophone": { "rootNames": [ "hom", "phon", ], "definition": "same sound", "roots": {} },
	"hydro": { "rootNames": [ "aqua", "hydrochloric", ], "definition": "water", "roots": {} },
	"hydrochloric": { "rootNames": [ "chlor", ], "definition": "green acid water", "roots": {} },
	"hypnosis": { "rootNames": [ "hypn", "osis", ], "definition": "the state of sleep", "roots": {} },
	"keratosis": { "rootNames": [ "cerat", "osis", ], "definition": "having horny-like growth", "roots": {} },
	"metamorphosis": { "rootNames": [ "meta", "morph", "osis", ], "definition": "action of changing shape", "roots": {} },
	"mon": { "rootNames": [ "un", ], "definition": "one", "roots": {} },
	"monochrome": { "rootNames": [ "chrom", "mon", ], "definition": "one color", "roots": {} },
	"monograph": { "rootNames": [ "graph", "mon", ], "definition": "one writer", "roots": {} },
	"monologue": { "rootNames": [ "logy", "mon", ], "definition": "one speaker", "roots": {} },
	"monophonic": { "rootNames": [ "mon", "phon", ], "definition": "one sound", "roots": {} },
	"monopoly": { "rootNames": [ "mon", "polien", ], "definition": "one seller", "roots": {} },
	"monotreme": { "rootNames": [ "mon", "trema", ], "definition": "one hole", "roots": {} },
	"necromancy": { "rootNames": [ "mancy", "necr", ], "definition": "dead divination", "roots": {} },
	"necrophobia": { "rootNames": [ "necr", "phob", ], "definition": "fear of dead", "roots": {} },
	"necrosis": { "rootNames": [ "necr", "osis", ], "definition": "the state of death", "roots": {} },
	"necrotomy": { "rootNames": [ "necr", "tom", ], "definition": "cutting dead bodies", "roots": {} },
	"octogon": { "rootNames": [ "gon", "oct", ], "definition": "eight angle", "roots": {} },
	"octopus": { "rootNames": [ "oct", "pod", ], "definition": "eight foot", "roots": {} },
	"ornithology": { "rootNames": [ "logy", "ornith", ], "definition": "study of birds", "roots": {} },
	"ornithophobia": { "rootNames": [ "ornith", "phob", ], "definition": "fear of birds", "roots": {} },
	"pachycephalosaurus": { "rootNames": [ "cephal", "pach", "saurus", ], "definition": "thick headed lizard", "roots": {} },
	"pachyderm": { "rootNames": [ "derm", "pach", ], "definition": "thick skin", "roots": {} },
	"pentagamy": { "rootNames": [ "gamy", "penta", ], "definition": "five spouses", "roots": {} },
	"pentagon": { "rootNames": [ "gon", "penta", ], "definition": "five angle", "roots": {} },
	"phonograph": { "rootNames": [ "graph", "phon", ], "definition": "writing to sound", "roots": {} },
	"photograph": { "rootNames": [ "graph", "photo", ], "definition": "light drawing", "roots": {} },
	"photosythensis": { "rootNames": [ "photo", "syn", "the", ], "definition": "putting light together", "roots": {} },
	"podiatry": { "rootNames": [ "iatry", "pod", ], "definition": "foot healing", "roots": {} },
	"polychrome": { "rootNames": [ "chrom", "poly", ], "definition": "many colors", "roots": {} },
	"polydactyl": { "rootNames": [ "dactyl", "poly", ], "definition": "many fingered", "roots": {} },
	"polygamy": { "rootNames": [ "gamy", "poly", ], "definition": "many spouses", "roots": {} },
	"polygon": { "rootNames": [ "gon", "poly", ], "definition": "many corners", "roots": {} },
	"polypod": { "rootNames": [ "pod", "poly", ], "definition": "many foot", "roots": {} },
	"psychiatry": { "rootNames": [ "iatry", "psych", ], "definition": "mind healing", "roots": {} },
	"psychology": { "rootNames": [ "logy", "psych", ], "definition": "study of the mind", "roots": {} },
	"psychosis": { "rootNames": [ "osis", "psych", ], "definition": "severe mental disorder", "roots": {} },
	"pteradactyl": { "rootNames": [ "dactyl", "pter", ], "definition": "winged toes/fingers", "roots": {} },
	"pteropod": { "rootNames": [ "pod", "pter", ], "definition": "winged foot", "roots": {} },
	"sauropod": { "rootNames": [ "pod", "saurus", ], "definition": "lizard foot", "roots": {} },
	"sororicide": { "rootNames": [ "cide", "soror", ], "definition": "killing sister", "roots": {} },
	"stasiphobia": { "rootNames": [ "phob", "stasis", ], "definition": "fear of standing", "roots": {} },
	"stasis dermatitis": { "rootNames": [ "derm", "itis", "stasis", ], "definition": "impaired circulation in legs", "roots": {} },
	"static": { "rootNames": [ "aerostatic", "stasis", ], "definition": "stopped", "roots": {} },
	"steganography": { "rootNames": [ "graph", "steg", ], "definition": "writing covering", "roots": {} },
	"stegosaurus": { "rootNames": [ "saurus", "steg", ], "definition": "covered lizard", "roots": {} },
	"submarine": { "rootNames": [ "mar", "sub", ], "definition": "below sea", "roots": {} },
	"submerge": { "rootNames": [ "merg", "sub", ], "definition": "plunge below", "roots": {} },
	"theromorpha": { "rootNames": [ "morph", "ther", ], "definition": "animal shape", "roots": {} },
	"theropod": { "rootNames": [ "pod", "ther", ], "definition": "beast foot", "roots": {} },
	"therosaurus": { "rootNames": [ "saurus", "ther", ], "definition": "beast lizard", "roots": {} },
	"triceratops": { "rootNames": [ "cerat", "ops", "tri", ], "definition": "three horned face", "roots": {} },
	"trigon": { "rootNames": [ "gon", "tri", ], "definition": "three angle", "roots": {} },
	"tripod": { "rootNames": [ "pod", "tri", ], "definition": "three foot", "roots": {} },
	"tyrannosaurus rex": { "rootNames": [ "rex", "saurus", ], "definition": "tyrant lizard king", "roots": {} },
	"unicorn": { "rootNames": [ "cerat", "un", ], "definition": "one horn", "roots": {} },
	"unicycle": { "rootNames": [ "cycl", "un", ], "definition": "one circle", "roots": {} },
	"universe": { "rootNames": [ "un", "verse", ], "definition": "turned to one", "roots": {} },
	"zoology": { "rootNames": [ "logy", "zo", ], "definition": "study of animals", "roots": {} },
	"zoophobia": { "rootNames": [ "phob", "zo", ], "definition": "fear of animals", "roots": {} },
};






/*
var wwww = [

	"pachy:pach,cephalo:cephal,saurus,thick headed lizard",
	"octo:oct,pus:pod,eight footed",
	"or",
	"octopus,oct,pod,eight footed"
	

];

var rrrr = [

	"octo:eight",
	"pach:thick",
	"cephal:head",
	"saurus:lizard"

];
*/
/*
perhaps, instead of parsing this client-side, create a small util to take this data, and convert it to the json

then, we could have two really simple files - words.txt and roots.txt
	
pachy:pach,cephalo:cephal,saurus,thick headed lizard
octo:oct,pus:pod,eight footed
or
octopus,oct,pod,eight footed

octo:eight
pach:thick
cephal:head
saurus:lizard

*/
/*
var words = {
	"octopus": { "rootNames": ["octo", "pod", ], "definition": "eight-footed", "roots": {} },
	"gastropod": { "rootNames": ["gastr", "pod"], "definition": "stomach-foot", "roots": {} },
	"pachycephalosaurus": { "rootNames": ["pach", "cephal", "saurus", ], "definition": "thick headed lizard", "roots": {} },
};
var roots = {
	"octo": { "definition": "eight", "words": {} },
	"pod": { "otherRoots": "pus", "definition": "foot", "words": {} },
	"pach": { "definition": "thick", "words": {} },
	"gastr": { "definition": "stomach", "words": {} },
	"cephal": { "definition": "head", "words": {} },
	"saurus": { "definition": "lizard", "words": {} },
};
*/