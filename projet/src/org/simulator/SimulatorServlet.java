package org.simulator;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Servlet implementation class SimulatorServlet
 */
@WebServlet("/Simulator")
@ServerEndpoint("/Simulator") 
public class SimulatorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;	
	private static final int DUREE_LIMIT = 10;

	private static PannesDuree currentPannes = null;
	private static Timer minuteur;
	private static TimerTask tache;
	
	private static Map<Session, String> sessions = new HashMap<Session, String>();
	
	private PanneService panneService = new PanneServiceImpl();
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SimulatorServlet() {
		super();
	}

	 @OnOpen
	 public void onOpen(Session session){
        System.out.println(session.getId() + " has opened a connection");
        try {
            session.getBasicRemote().sendText("connected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	 }
	 
    @OnMessage
    public void onMessage(String message, Session session){
    	System.out.println(session.getId() + " just sent : " + message);
    	if(message.equals("monitor") || message.equals("simulator"))
    	{
    		sessions.put(session,message);
    	}
    } 

    @OnClose
    public void onClose(Session session){
    	System.out.println("Session " +session.getId()+" ("+ sessions.get(session) +") has ended");
    	sessions.remove(session);	        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{		
		String pageName = "/WEB-INF/simulator.jsp";
		
		request.setAttribute("message", null);
		
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
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)	throws ServletException, IOException
	{
		doProcess(request, response);
	}

	private void doProcess(HttpServletRequest request, HttpServletResponse response) 
	{		
		String json ="";
				
		if( (request.getParameter("machine")!=null) && (request.getParameter("typepanne")!=null) )
		{
			if(request.getParameter("machine").equals(""))
			{
				json = convertToJson("Error : missing parameter.");
			}
			else
			{
				String machine = request.getParameter("machine").toLowerCase();
				String typepanneName = request.getParameter("typepanne");
				TypePanne typepanne = null;			
				
				boolean ok = true;
				//verification des saisies
				if(machine.length() != 16)
				{
					ok = false;
				}
				else
				{
					int i=0;
					while((i<machine.length()) && ok )
					{
						Character c= machine.charAt(i);
						if( !((Character.isDigit(c)) || ( (c>='a') && (c<='f'))) )
						{
							ok = false;
						}
						i++;
					}
				}			
				if(typepanneName.equals("reseau"))
				{
					typepanne = TypePanne.RESEAU;
				}
				else if(typepanneName.equals("crashdisque"))
				{
					typepanne = TypePanne.CRASH_DISQUE;
				}
				else if(typepanneName.equals("memoire"))
				{
					typepanne = TypePanne.PROBLEME_MEMOIRE;
				}
				
				if( (typepanne != null) && ok)
				{
					String message = panneService.ajoutPanne(machine, typepanne);
					
					if(message!=null)
					{
						json = convertToJson(message);
					}
					updateMonitors();
				}
				else
				{
					json = convertToJson("Error : Bad format for the 'Targeted machine' field. ");
				}
			}				
		}
		else if( request.getParameter("nombre")!=null)
		{
			if(request.getParameter("nombre").equals(""))
			{
				json = convertToJson("Error : missing parameter.");
			}
			else
			{
				try
				{
					int nbPannes = Integer.valueOf(request.getParameter("nombre"));
					String message;
					if(nbPannes < 2)
					{
						message = "The number of breakdowns must be greater than or equal to 2.";
					}
					else
					{
						message = panneService.ajoutPannes(nbPannes);
					}
					if(message!=null)
					{
						json = convertToJson(message);
					}
					updateMonitors();
				}
				catch(Exception e)
				{
					json = convertToJson("Error : "+e.getMessage().replaceAll("\"","\'"));
				}
			}
		}
		else if( (request.getParameter("nombreDuree")!=null) && (request.getParameter("duree")!=null) )
		{
			if(request.getParameter("nombreDuree").equals("") || request.getParameter("duree").equals(""))
			{
				json = convertToJson("Error : missing parameter.");
			}
			else
			{
				try
				{
					int nbPannes = Integer.valueOf(request.getParameter("nombreDuree"));
					int duree = Integer.valueOf(request.getParameter("duree"));
					String message;
					
					if( currentPannes == null )
					{
						if( (nbPannes<2) || (duree<20) )
						{
							message = "The number of breakdowns must be greater than or equal to 2. The duration must be greater than or equal to 20 secondes.";
						}
						else
						{
							int inter = duree / nbPannes;
							if(inter < DUREE_LIMIT)
							{
								message = "There are too many breakdowns for this duration. Minimum " + DUREE_LIMIT + " secondes per breakdown. ";
							}
							else
							{
								currentPannes = new PannesDuree(nbPannes, duree, inter);
								
								 minuteur = new Timer();
							     tache = new TimerTask() {
							            public void run() {
							            	String message;
							            	currentPannes.setDureeRestante(currentPannes.getDureeRestante() - currentPannes.getInter());
							            		
						            		message = panneService.ajoutPanneAlea();
						            		if(message.equals("Breakdown properly added."))
						            		{
						            			currentPannes.setNbPannesCurrent(currentPannes.getNbPannesCurrent()+1);
						            			message = currentPannes.getNbPannesCurrent()+" breakdown(s) on "+currentPannes.getNbPannesTotal()+" already generated. Remaining time : " + currentPannes.getDureeRestante() + " secondes. Total time : " + currentPannes.getDuree() + " secondes .";
						            		}
						            		else
						            		{
						            			message = "Error : a breakdown could not be generated.";

						            		}
						            		if( (currentPannes.getDureeRestante() <= 0) || (currentPannes.getNbPannesCurrent()>=currentPannes.getNbPannesTotal()) )
							            	{
							            		message = "Generation over time just ended : " + currentPannes.getNbPannesCurrent()+" breakdown(s) on "+currentPannes.getNbPannesTotal()+" have been generated over "+ currentPannes.getDuree() + " secondes .";
							            		this.cancel();		
							            		currentPannes = null;
							            	}
						            		updateMonitors();
							            	sendToSimulators(message);				
							            }
							        };
							        minuteur.schedule(tache, inter*1000, inter*1000);
								
								message = nbPannes+ " breakdowns will be generated over a period of " + duree + " secondes. ";
							}
						}
					}
					else
					{
						message = "Breakdowns generation over time is already set : "+currentPannes.getNbPannesCurrent()+" breakdown(s) on "+currentPannes.getNbPannesTotal()+" already generated. Remaining time : " + currentPannes.getDureeRestante() + "secondes (+/- " + currentPannes.getInter() + " secondes). Total time : " + currentPannes.getDuree() + "secondes .";
					}
					if(message!=null)
					{
						json = convertToJson(message);
					}				
				}
				catch(Exception e)
				{
					json = convertToJson("Error : "+e.getMessage().replaceAll("\"","\'"));
				}
			}
			
		}
		else if( (request.getParameter("param")!=null) && request.getParameter("param").equals("random")  )
		{
			String message = panneService.ajoutPanneAlea();
			if(message!=null)
			{
				json = convertToJson(message);
			}
			updateMonitors();
		}
		else if( (request.getParameter("param")!=null) && request.getParameter("param").equals("stop")  )
		{
			if(currentPannes != null)
			{
				tache.cancel();
				currentPannes = null;
				json = convertToJson("Generation has been stopped. ");
			}
			else
			{
				json = convertToJson("There is no generation in progress. ");
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
	
	private static void updateMonitors()
	{
    	List <Session> monitors = new ArrayList<Session>();
    	Iterator<Session> it = sessions.keySet().iterator();
    	
    	while(it.hasNext())
    	{
    		Session session = it.next();
    		if(sessions.get(session).equals("monitor"))
    		{
    			monitors.add(session);
    		}
    	}
		
		Iterator<Session> it1 = monitors.iterator();
		while(it1.hasNext())
		{
			Session session = it1.next();
			String toSend = "update";
			try {
	            session.getBasicRemote().sendText(toSend);					    			  
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		}
	}
	
	private static void sendToSimulators(String message)
	{
		List <Session> simulators = new ArrayList<Session>();
    	Iterator<Session> it = sessions.keySet().iterator();
    	
    	while(it.hasNext())
    	{
    		Session session = it.next();
    		if(sessions.get(session).equals("simulator"))
    		{
    			simulators.add(session);
    		}
    	}
		Iterator<Session> it1 = simulators.iterator();
		while(it1.hasNext())
		{
			Session session = it1.next();
			try {
	            session.getBasicRemote().sendText(message);					    			  
	        } catch (IOException ex) {
	            ex.printStackTrace();
	        }
		}
	}	
	private static String convertToJson(String message)
	{
		return "{ \"message\": \" "+message+" \"}";
	}
}
