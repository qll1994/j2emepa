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
		
	private static String convertToJson(String message)
	{
		return "{ \"message\": \" "+message+" \"}";
	}
	
	private static String convertToJson(int minute, int hour, int day, int month, int ever)
	{
		return "{ \"minute\": \" "+minute+" \", \"hour\": \" "+hour+" \" , \"day\": \" "+day+" \" , \"month\": \" "+month+" \" , \"ever\": \" "+ever+" \" }";
	}
	
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
