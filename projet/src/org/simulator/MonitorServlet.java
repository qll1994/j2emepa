package org.simulator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MonitorServlet
 */
@WebServlet("/Monitor")
public class MonitorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
      
	private PanneService panneService = new PanneServiceImpl();
	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MonitorServlet() {
        super();
    }

    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pageName = "/WEB-INF/monitor.jsp";
		RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
		try {
			rd.forward(request, response);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String param=request.getParameter("param");
		if((param == null) || param.equals("undefined"))
		{
			String pageName = "/WEB-INF/monitor.jsp";
			RequestDispatcher rd = getServletContext().getRequestDispatcher(pageName);
			try {
				rd.forward(request, response);
			} catch (ServletException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			doProcess(request,response,param);
		}
	}
	
	private void doProcess(HttpServletRequest request, HttpServletResponse response, String param) 
	{	
		String json = "";
		if(param!=null)
		{
			if(param.equals("number"))
			{			
			    int minute;
				int hour;
				int day;
				int month;
				int ever;
				
				if( ((minute = panneService.nbMinute())<0) || ((hour = panneService.nbHour())<0) || ((day = panneService.nbDay())<0) || ((month = panneService.nbMonth())<0) || ((ever = panneService.nbEver())<0) )
				{
				 
					json = convertToJson("Error : Unable to reach database.");
				}
				else
				{
					json = convertToJson(minute, hour, day, month, ever);
				}				
			}
			else if(param.equals("fix"))
			{
				String id = request.getParameter("id");
				String checked = request.getParameter("checked");
				if(id==null || checked==null)
				{
					json = convertToJson("Error : Unexpected ending.");
				}
				else
				{
					String m = panneService.fix(Integer.valueOf(id),Boolean.valueOf(checked));
					json = convertToJson(m);
				}
			}
			else if(param.equals("graph"))
			{				
				if((request.getParameter("day")!=null) && (request.getParameter("month")!=null) && (request.getParameter("year")!=null) && !request.getParameter("day").equals("") && !request.getParameter("month").equals("") && !request.getParameter("year").equals(""))
				{
					try
					{
						int jour = Integer.valueOf(request.getParameter("day"));
						int mois = Integer.valueOf(request.getParameter("month"));
						int annee = Integer.valueOf(request.getParameter("year"));	
						
						ListWithErr<Integer> list = panneService.nbPannesJour(jour, mois, annee);
						
						if(list.isValide() && (list.getList().size()==24))
						{
							Iterator<Integer> it = list.getList().iterator();
							int i=0;
							int[] tab = new int[24];
							while(it.hasNext())
							{
								tab[i] = it.next();
								i++;
							}
							json = convertToJson(tab);
						}
						else
						{
							json = convertToJson("Error : Unable to search in database.");
						}
						
					}
					catch(Exception e)
					{
						json = convertToJson("Error : Bad parameter.");
					}
				}
				else
				{
					json = convertToJson("Error : Missing parameter.");
				}
			}
			else 
			{
				ListWithErr<Panne> listPannes = null;
				if(param.equals("minute"))
				{
					listPannes = panneService.lastMinute();
				}
				else if(param.equals("hour"))
				{
					listPannes = panneService.lastHour();
				}
				else if(param.equals("day"))
				{
					listPannes = panneService.lastDay();
				}
				else if(param.equals("month"))
				{
					listPannes = panneService.lastMonth();
				}
				else if(param.equals("ever"))
				{
					listPannes = panneService.ever();
				}
				else if(param.equals("minutetype"))
				{
					listPannes = panneService.lastMinutePerType();
				}
				else if(param.equals("hourtype"))
				{
					listPannes = panneService.lastHourPerType();
				}
				else if(param.equals("daytype"))
				{
					listPannes = panneService.lastDayPerType();
				}
				else if(param.equals("monthtype"))
				{
					listPannes = panneService.lastMonthPerType();
				}
				else if(param.equals("evertype"))
				{
					listPannes = panneService.everPerType();
				}
				
				if(listPannes==null)
				{
					json = convertToJson("Error : Bad parameter.");
				}
				else if(!listPannes.isValide())
				{
					json = convertToJson("Error : Unable to search in database.");
				}
				else if(listPannes.getList().isEmpty())
				{
					json = convertToJson("No breakdown found.");
				}
				else
				{
					json = convertToJson(listPannes.getList());
				}
			}
			
		}
		
		response.setContentType("application/json");
		PrintWriter out;
		try 
		{
			out = response.getWriter();
			out.print(json);
			out.flush();
		} 
		catch (IOException e1) 
		{
			e1.printStackTrace();
		}
	}
	/**
	 *  Méthode qui convertit les messages en Json		
	 * @param message
	 * @return Chaîne de caractère en json
	 */
	private static String convertToJson(String message)
	{
		return "{ \"message\": \" "+message+" \"}";
	}
	/**
	 * Méthode qui convertit les dates en Json
	 * @param minute
	 * @param hour
	 * @param day
	 * @param month
	 * @param ever
	 * @return Chaîne de caractère en json
	 */
	private static String convertToJson(int minute, int hour, int day, int month, int ever)
	{
		return "{ \"minute\": \" "+minute+" \", \"hour\": \" "+hour+" \" , \"day\": \" "+day+" \" , \"month\": \" "+month+" \" , \"ever\": \" "+ever+" \" }";
	}
	/**
	 *  Méthode qui convertit les tableaux en Json
	 * @param tab
	 * @return Chaîne de caractère  en json
	 */
	private static String convertToJson(int[] tab)
	{
		String json = "{ \"graph\" : {";
		for(int i=0; i<tab.length; i++)
		{
			if(i!=0)
			{
				json+= ", ";
			}
			json += "\""+i+"-"+(i+1)+"\" : \""+tab[i]+"\"";
		}
		json += "}}";
		return json;
	}	
	/**
	 * Méthode qui convertit les listes de pannes en Json
	 * @param listPannes
	 * @return Chaîne de caractère en json
	 */
	private static String convertToJson(List<Panne> listPannes)
	{
		boolean first = true;		
		Iterator<Panne> it = listPannes.iterator();
		String json = "{ \"breakdown\" : [";
		
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
				json += ",";
			}
			json += "{ \"id\": \""+ id + "\", \"hour\" : \"" + hour + "\", \"typepanne\" : \"" + typepanne + "\", \"machine\" : \"" + machine + "\", \"typemachine\" : \""+typemachine+"\", \"reparee\" : \"" + reparee + "\"}";
			
			first = false;
		}
		json += "]}";
		return json;
	}	
}
