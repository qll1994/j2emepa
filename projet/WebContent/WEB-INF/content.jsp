<%@ page language="java" contentType="charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.Enumeration,java.util.List,java.util.Iterator,org.simulator.Panne"
%>

<%	
	String message = new String();
	if(request.getAttribute("message") !=null )
	{
		message = (String)request.getAttribute("message");
	
		if(!message.isEmpty())
		{
%>
{
"message" :
"<%=message%>"
}
<%
		}
	}
	else if((request.getAttribute("nbminute")!=null)&&(request.getAttribute("nbhour")!=null)&&(request.getAttribute("nbday")!=null)&&(request.getAttribute("nbmonth")!=null)&&(request.getAttribute("nbever")!=null))
	{
		int p;
		String m;
		
		m = (String)request.getAttribute("nbminute");
		if(!m.isEmpty())
		{
			p = Integer.valueOf(m);
%>
{
"minute" :
"<%=p%>",
<%
		}
		m = (String)request.getAttribute("nbhour");
		if(!m.isEmpty())
		{
			p = Integer.valueOf(m);
%>
"hour" :
"<%=p%>",
<%
		}
		m = (String)request.getAttribute("nbday");
		if(!m.isEmpty())
		{
			p = Integer.valueOf(m);
%>
"day" :
"<%=p%>",
<%
		}
		m = (String)request.getAttribute("nbmonth");
		if(!m.isEmpty())
		{
		p = Integer.valueOf(m);
%>
"month" :
"<%=p%>",
<%		}
		m = (String)request.getAttribute("nbever");
		if(!m.isEmpty())
		{
			p = Integer.valueOf(m);
%>
"ever" :
"<%=p%>"
}
<%
		}
	}
	else if(request.getAttribute("listPannes")!=null)
	{
		boolean first = true;
		
		List<Panne> list = (List<Panne>)request.getAttribute("listPannes");		
		Iterator<Panne> it = list.iterator();
%>
{
"breakdown":[
<%
		while(it.hasNext())
		{
			Panne tmp = it.next();
			int id = tmp.getId();
			String hour = tmp.getHeure();
			String typepanne;
			switch(tmp.getTypepanne())
			{
			case CRASH_DISQUE:
				typepanne = "Disk Crash";
				break;
			case PROBLEME_MEMOIRE:
				typepanne = "Memory Problem";
				break;
			case RESEAU:
				typepanne = "Network";
				break;
			default:
				typepanne = "Network";
				break;
			}
			String machine = tmp.getMachine();
			String typemachine;
			switch(tmp.getTypemachine())
			{
			case PARE_FEUX:
				typemachine = "Firewall";
				break;
			case ROUTEUR:
				typemachine = "Router";
				break;
			case SERVEUR:
				typemachine = "Server";
				break;
			default:
				typemachine = "Server";
				break;
			}
			boolean reparee = tmp.isReparee();
			
			if(!first)
			{
%>
,
<%
			}
%>
{
	"id":"<%=id %>","hour":"<%=hour %>","typepanne":"<%=typepanne %>","machine":"<%=machine %>","typemachine":"<%=typemachine %>","reparee":"<%=reparee %>"
}
<%
			first = false;
		}
%>
]
}
<%
	}
%>
	
