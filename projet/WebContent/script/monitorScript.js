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
	
	
	/*recharger la page 
	le contenu de "information est réinitialisé"
	*/
	var reloadbutton = document.getElementById("reload");
	reloadbutton.onclick = function() {
		document.getElementById("infoContent").innerHTML = "None";
		reload();
	}
	
	var drawGraph = document.getElementById("drawGraph");
	drawGraph.onclick = function(){
		getDataGraph();
	}	
	request(loadNumber,"param=number");
	setReload();
	openSocket(); 
})();

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

function fix(responseText)
{
	request(loadContent,"param="+current);	
	var response = JSON.parse(responseText);	
	
	if(response["message"]!=null)
	{
		document.getElementById('infoContent').innerHTML = response["message"]; 
	}
}

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

function setReload()
{
	currentTime = delai*60;
	setInterval(updateTime,1000);
}

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

function getDataGraph()
{
	var param;
	var day = document.getElementById("dayGraph").value; 
	var month = document.getElementById("monthGraph").value; 
	var year = document.getElementById("yearGraph").value; 
	
	param="param=graph&day="+day+"&month="+month+"&year="+year;
	request(drawGraph,param);
}


/*recherche la valeur max d'un tableau
 * utile pour le tracé du graphe
 */
function max(tab){
	var max=tab[0];
	for(i=1;i<tab.length; i++){
		if(max<tab[i]){
			max=tab[i];
		}
	}
	return max;
}

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
		
		var vMax=max(arrayValue);
		
		//dimension du graphe
		var longueur=600;
		var hauteur=300;
	
		/*définition des valeurs extrèmes des axes 
		(décalage par rapport au dimensions du canvas)*/
		x0=40;
		y0=hauteur-40;
		yMax=40;
		xMax= longueur-10;
		
		//Récupération du canvas
	    var canvas = document.getElementById('graphContent');
	   
	        if(!canvas)

	        {
	        	//message d'erreur si la récupération échoue
	            alert("Impossible de récupérer le canvas");
	            return;
	        }

	    //Récupération du context
	    var context = canvas.getContext('2d');
	    
	        if(!context)

	        {
	        	//message d'erreur si la récupération échoue
	            alert("Impossible de récupérer le context du canvas");
	            return;
	        }
	    
	    /*si l'on effectue plusieurs requêtes, 
	     le graphe précédent doit être effacé*/
	    context.clearRect(0, 0, canvas.width, canvas.height);
		
	    context.beginPath();

	    //tracé des axes
		context.moveTo(x0, yMax);
		context.lineTo(x0,y0);
		context.lineTo(xMax, y0);
		
		
		//titre
		context.font = "15px Helvetica";
		context.fillText("Number of breakdowns per hour",longueur/3,20); 
	
		//label des axes
		context.font = "13px Helvetica";
		context.fillText("Nb of breakdowns",0,30); 
		context.font = "13px Helvetica";
		context.fillText("hour",(xMax-x0)/2,hauteur);
		
		//tracé du graphe
		
		var x=x0;	//mémorise l'absisse du contexte
		context.moveTo(x0, y0);	//le tracé commence au point (0,0)
		
		
		context.font = "10px Helvetica";
		context.fillText(0, x,hauteur-25); 
		
		for(var i=0; i<24; i++){	
			context.lineTo(x,y0+ arrayValue[i]*(yMax-y0)/vMax);
			x=x+(xMax-x0)/24;
			context.lineTo(x,y0+ arrayValue[i]*(yMax-y0)/vMax);
			context.lineTo(x,y0);
			context.font = "10px Helvetica";
			
			//graduations des axes
			context.fillText(i+1, x-5, hauteur-25);
			
			//valeur de l'histogramme
			if (arrayValue[i]!=0){
				context.fillText(arrayValue[i], x-20, y0+ arrayValue[i]*(yMax-y0)/vMax-5);
			}
		}

		
		
		context.stroke();

		

		context.closePath();
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
    // Ensures only one connection is open at a time
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
       writeResponse("WebSocket is already opened.");
        return;
    }
    // Create a new instance of the websocket
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

function tryConnect()
{
	var x = setInterval(updateBeforeConnect,1000);
	setTimeout(function(){clearInterval(x);},(BEFORE_CONNECT+1)*1000);
}

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

function sendType()
{
	webSocket.send("monitor");
}

function closeSocket(){
    webSocket.close();
}

function writeResponse(text){
    messages.innerHTML = text;
}