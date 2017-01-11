var PATH = document.location.href.substring( 0 ,document.location.href.lastIndexOf( "/" ) );
var SUBPATH = PATH.substring(5,PATH.length);	// on enleve le 'http:'

var beforeConnect ;
var BEFORE_CONNECT = 5;

var webSocket;
var messages = document.getElementById("infoContent");

(function() {
	openSocket();
})();

//Fonction qui permet de mettre à jour le message de infoContent. 
function updateMessage(responseText)
{
	var response = JSON.parse(responseText);
	var message;
	if(response["message"]!=null)
	{
		message = response["message"];
	}
	else
	{
		message = "None";
	}
	document.getElementById("infoContent").innerHTML = message;
}

//Fonction qui permet de prendre la valeur de machine et typepanne et transmet les valeurs au serveur.
function generate()
{
	var machine = document.getElementById("machine").value;
	var typepanne = document.getElementById("typepanne").value;	
	request(updateMessage,"machine="+machine+"&typepanne="+typepanne);
}
 //Fonction qui informe le serveur que l'on génère aléatoirement des pannes 
function randomGenerate()
{
	request(updateMessage,"param=random");
}
//Fonction qui informe le serveur que l'on génère plusieurs pannes 
function manyGenerate()
{
	var nombre = document.getElementById("nombre").value;
	request(updateMessage,"nombre="+nombre);
}

//Fonction qui informe le serveur que l'on génère plusieurs pannes sur une durée précise
function generateOverTime()
{
	var nombre = document.getElementById("nbOnTime").value;
	var duree = document.getElementById("dureeOnTime").value;	
	request(updateMessage,"nombreDuree="+nombre+"&duree="+duree);
}
//Fonction qui informe le serveur que l'on ne génère plus de pannes 
function stopGeneration()
{
	request(updateMessage,"param=stop");
}

//Fonction qui communiquee avec le serveur 
function request(callback, param)	//callback est une fonction, param une String
{
	var xhr = getXMLHttpRequest();
	xhr.open('POST', PATH+"/Simulator");	
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

//
function openSocket(){
    // Vérifie s'il y a déjà une connection établie
    if(webSocket !== undefined && webSocket.readyState !== WebSocket.CLOSED){
       writeResponse("WebSocket is already opened.");
        return;
    }
    // Créer une nouvelle instance de websocket
    webSocket = new WebSocket("ws:"+SUBPATH+"/Simulator");
     
   
    webSocket.onopen = function(event){
	    
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
    	else
        {
    		writeResponse(event.data);
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

//Fonction qui assure les mises à jours de l'infoContent pendant la tentative de reconnection 
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
	webSocket.send("simulator");
}

// Fonction qui ferme le Websocket
function closeSocket(){
	alert("closing");
    webSocket.close();
}

//Focntion qui permet  d'écrire du texte au format HTML
function writeResponse(text){
    messages.innerHTML = text;
}
