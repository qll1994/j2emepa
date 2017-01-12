var delai = 5; //intervalle de raffraichissement des données (en minutes)
var currentTime;

var PATH = document.location.href.substring( 0 ,document.location.href.lastIndexOf( "/" ) );
var SUBPATH = PATH.substring(5,PATH.length);	// on enleve le 'http:'

var current;

var beforeConnect ;
var BEFORE_CONNECT = 5;

var webSocket;
var messages = document.getElementById("infoContent");

(function() {

	var inputs = document.getElementsByClassName('buttonNumber'), inputsLen = inputs.length;

	for (var i = 0; i < inputsLen; i++) {
		inputs[i].onclick = function() {
			current = this.name;
			reload();
			document.getElementById("infoContent").innerHTML = "None";
		};
	}		
	//Recharge la page en cliquant sur le boutton reload
	var reloadbutton = document.getElementById("reload");
	reloadbutton.onclick = function() {
		document.getElementById("infoContent").innerHTML = "None";
		reload();
	}
	//Dessine le graphe en cliqaunt sur le bouton draw
	var drawGraph = document.getElementById("drawGraph");
	drawGraph.onclick = function(){
		getDataGraph();
	}	
	request(loadNumber,"param=number");
	setReload();
	openSocket(); 
})();

//Fonction de rechargement de la page. Envoie de la requête au serveur
function reload()
{
	if(current != null)
	{
		request(loadContent,"param="+current);
	}
	else
	{
		document.getElementById("infoContent").innerHTML = "None";
	}
	request(loadNumber,"param=number");
}

//Fonction permettant de réparer une panne
function fix(responseText)
{
	request(loadContent,"param="+current);	
	var response = JSON.parse(responseText);	
	
	if(response["message"]!=null)
	{
		document.getElementById('infoContent').innerHTML = response["message"]; 
	}
}

//Fonction qui permet de charger les nombres des les cases minute, hour, day, month and ever
function loadNumber(responseText)
{
	var response = JSON.parse(responseText);					
	Object.getOwnPropertyNames(response).forEach(function(key){
		  var res = response[key];
			if(res != null && key != "message")
			{
				document.getElementById(key).innerHTML = res;
			}
		});
	if(response["message"]!=null)
	{
		document.getElementById('infoContent').innerHTML = response["message"];
		var tab = ["minute","hour","day","month","ever"];
		tab.forEach( function(element){
			if(document.getElementById(element).innerHTML == "")
			{
				document.getElementById(element).innerHTML = "ø";
			}
		});
		
	}
}
//Fonction qui recharge le contenu du tableau en fonction des pannes reçues ou des messages reçus
function loadContent(responseText) 
{
	var response = JSON.parse(responseText);
	if(response["message"]!=null)
	{
		document.getElementById('infoContent').innerHTML = response["message"]; // Et on affiche !
		current = null;
		document.getElementById('content').innerHTML = "";
	}
	else if(response["breakdown"] != null)
	{
		var toAdd = "<table border=\"1\" cellpadding=\"2\" cellspacing=\"1\"><tr><th>Id</th><th>Hour</th><th>Type of breakdown</th><th>Machine</th><th>Type of machine</th><th>Fixed</th></tr>";
		var breakdowns = response["breakdown"];
		Object.getOwnPropertyNames(breakdowns).forEach(function(key){
			  var bd = breakdowns[key];
			  
			  var id = bd.id;
			  var hour = bd.hour;
			  var typepanne = bd.typepanne;
			  var machine = bd.machine;
			  var typemachine = bd.typemachine;
			  var reparee = bd.reparee;
			  
			  var tmp = "";
			  if(reparee=="true")
		   	  {
				  tmp="checked";
			  }
			  
			  if(id != undefined)
			 	 toAdd += "<tr><td>"+id+"</td><td>"+hour+"</td><td>"+typepanne+"</td><td>"+machine+"</td><td>"+typemachine+"</td><td><input type=\"checkbox\" value=\"Fixed\" "+tmp + " class=\"toFix\" id=\""+id+"\"/></td></tr>";
		});
		document.getElementById('content').innerHTML = toAdd;
		
		var inputs = document.getElementsByClassName('toFix'), inputsLen = inputs.length;

		for (var i = 0; i < inputsLen; i++) {

			inputs[i].onclick = function() {
				request(fix,"param=fix&id="+this.id+"&checked="+this.checked);
			};
		}
	}
}

//Fonction permettant  de demander un rechargement de la page
function setReload()
{
	currentTime = delai*60;
	setInterval(updateTime,1000);
}

//Fonction permettant de mettre à jour le temps
function updateTime()
{
	if(currentTime <= 0)
	{
		reload();
		currentTime = delai*60;
	}
	else
	{
		currentTime--;
	}
	var minute = Math.floor(currentTime/60);
	var seconde = currentTime%60;
	var content ="";
	
	if(minute > 1)
	{
		content += minute + " minutes "; 
	}
	else
	{
		content += minute + " minute "; 
	}
	if(seconde > 1)
	{
		content += seconde + " secondes";
	}
	else
	{
		content += seconde + " seconde";
	}
	document.getElementById("beforeUpdateContent").innerHTML = content;	
}

//Fonction permettant d'acquérir les données du graphe grâce au serveur.
function getDataGraph()
{
	var param;
	
	var day = document.getElementById("dayGraph").value; 
	var month = document.getElementById("monthGraph").value; 
	var year = document.getElementById("yearGraph").value; 
	
	param="param=graph&day="+day+"&month="+month+"&year="+year;
	request(drawGraph,param);
}

//Fonction permettant de tracer le graphe
function drawGraph(responseText)
{
	var response = JSON.parse(responseText);
	var arrayName = new Array();
	var arrayValue = new Array();
	
	if(response["message"]!=null)
	{
		document.getElementById('infoContent').innerHTML = response["message"]; // Et on affiche !
		current = null;
		document.getElementById('content').innerHTML = "";
	}
	else if(response["graph"] != null)
	{
		var numbers = response["graph"];
		Object.getOwnPropertyNames(numbers).forEach(function(key){
			  var num = numbers[key];
			  arrayName.push(key);
			  arrayValue.push(num);
		});
		
		/*
		 * ###################################################################################
		 * Tracer le graphe ici :
		 * 		- arrayName est un tableau contenant les créneaux ["0-1","1-2",.....,"22-23","23-24"]   <-- facultatif
		 * 		- arrayValue est un tableau contenant les valeurs. Ex : [0,0,0,0,0,0,0,0,0,2,6,3,0,9,4,1,3,5,3,4,0,0,0,0]
		 * 
		 * L'element ou tracer le graphe est dans docuement.getElementById('graphContent'); (c'est une balise div)
		 */
	}
}
 //Focntion permettant de communiquer avec le serveur
function request(callback, param)	//callback est une fonction, param une String
{
	var xhr = getXMLHttpRequest();
	xhr.open('POST', PATH+"/Monitor");	
	xhr.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
	
	xhr.onreadystatechange = function()	{
		if (xhr.readyState == 4 && (xhr.status == 200 || xhr.status == 0))
		{
			callback(xhr.responseText);
		}
		else if (xhr.readyState === XMLHttpRequest.DONE	&& xhr.status != 200)
		{
			// En cas d'erreur !
			alert('An error occurs !\n\nCode :' + xhr.status + '\nText : ' + xhr.statusText);
		}
	};

	xhr.send(param);
}

//Fonction qui teste si le navigateur supporte le XMLHTTPRequest object
function getXMLHttpRequest()
{
	var xhr = null;
	
	if (window.XMLHttpRequest || window.ActiveXObject)
	{
		if (window.ActiveXObject) 
		{
			try 
			{
				xhr = new ActiveXObject("Msxml2.XMLHTTP");
			} 
			catch(e) 
			{
				xhr = new ActiveXObject("Microsoft.XMLHTTP");
			}
		} 
		else 
		{
			xhr = new XMLHttpRequest(); 
		}
	} 
	else 
	{
		alert("Your browser does not support the XMLHTTPRequest object...");
		return null;
	}	
	return xhr;
}


function openSocket(){
    // Vérifie s'il y a déjà une connection établie
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
       writeResponse("WebSocket is already opened.");
        return;
    }
     // Créer une nouvelle instance de websocket
    webSocket = new WebSocket("ws:"+SUBPATH+"/Simulator");
     
    /**
     * Binds functions to the listeners for the websocket.
     */
    webSocket.onopen = function(event){
        // For reasons I can't determine, onopen gets called twice
        // and the first time event.data is undefined.
        if(event.data === undefined)
            return;

        writeResponse(event.data);
    };

    webSocket.onmessage = function(event){
    	if(event.data == "connected")
    	{
    		writeResponse("Connected to the server.");
    		sendType();
    	}
    	else if(event.data == "update")
    	{
    		document.getElementById("infoContent").innerHTML = "None";
    		reload();
    	}
    	else
    	{
    		 writeResponse("Error : '" +event.data+ "' received from server. ");
    	}       
    };

    webSocket.onclose = function(event){
        writeResponse("Connection with server closed.");
        beforeConnect = BEFORE_CONNECT;
        tryConnect();
    };
}

//Fonction qui teste la connection 
function tryConnect()
{
	var x = setInterval(updateBeforeConnect,1000);
	setTimeout(function(){clearInterval(x);},(BEFORE_CONNECT+1)*1000);
}

//Fonction qui assure les mises à jours de l'infoContent pendant la tentative de reconnection//*
function updateBeforeConnect()
{
	var accord;
	if( (beforeConnect==0) || (beforeConnect==1) )
	{
		accord = "seconde";
	}
	else
	{
		accord = "secondes";
	}
	var message = "Reconnection in " + beforeConnect + " " + accord + ".";
	document.getElementById("infoContent").innerHTML = message;
	if(beforeConnect <= 0)
	{
		openSocket();
	}	
	beforeConnect--;
}


//Fonction qui envoie le type au Websocket
function sendType()
{
	webSocket.send("monitor");
}

// Fonction qui ferme le Websocket
function closeSocket(){
    webSocket.close();
}

//Fonction qui permet  d'écrire du texte au format HTML
function writeResponse(text){
    messages.innerHTML = text;
}
