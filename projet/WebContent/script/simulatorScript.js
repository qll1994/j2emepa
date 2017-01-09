var PATH = document.location.href.substring( 0 ,document.location.href.lastIndexOf( "/" ) );
var SUBPATH = PATH.substring(5,PATH.length);	// on enleve le 'http:'

var beforeConnect ;
var BEFORE_CONNECT = 5;

var webSocket;
var messages = document.getElementById("infoContent");

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

function generate()
{
	var machine = document.getElementById("machine").value;
	var typepanne = document.getElementById("typepanne").value;	
	request(updateMessage,"machine="+machine+"&typepanne="+typepanne);
}

function randomGenerate()
{
	request(updateMessage,"param=random");
}

function manyGenerate()
{
	var nombre = document.getElementById("nombre").value;
	request(updateMessage,"nombre="+nombre);
}

function generateOverTime()
{
	var nombre = document.getElementById("nbOnTime").value;
	var duree = document.getElementById("dureeOnTime").value;	
	request(updateMessage,"nombreDuree="+nombre+"&duree="+duree);
}

function stopGeneration()
{
	request(updateMessage,"param=stop");
}

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

(function() {
	openSocket();
})();

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
	webSocket.send("simulator");
}

function closeSocket(){
    webSocket.close();
}

function writeResponse(text){
    messages.innerHTML = text;
}