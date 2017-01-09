package org.simulator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PanneDAOImpl implements PanneDAO
{
	public String add(String machine, TypePanne typepanne) 
	{
		//nomenclature : machine commençant par 0 à 5 -> serveurs, de 6 à A -> pare-feux, de B à F -> routeur
		TypeMachine typemachine;
		Character premCar = machine.toLowerCase().charAt(0);
		
		if(Character.isDigit(premCar))
		{
			if( (Integer.valueOf(premCar.toString())>=0) && (Integer.valueOf(premCar.toString())<6))
			{
				typemachine = TypeMachine.SERVEUR;
			}
			else
			{
				typemachine = TypeMachine.PARE_FEUX;
			}
		}
		else
		{
			if(premCar == 'a')
			{
				typemachine = TypeMachine.PARE_FEUX;
			}
			else if( (premCar>'a') && (premCar<='f') )
			{
				typemachine = TypeMachine.ROUTEUR;
			}
			else
			{
				typemachine = TypeMachine.SERVEUR;	//tous les cas ont ete traite, celui-ci n'arrivera pas
			}
		}
		
		String update = "insert into pannes values (null,NOW(),\""+typepanne.getState()+"\",\""+machine+"\",\""+typemachine.getState()+"\",false);";
		Connection conn = null;
		Statement stat = null;
		String message ;
		
		try
		{
			conn = DBManager.getInstance().getConnection();
			if( conn != null)
			{
				stat = conn.createStatement();
				stat.executeUpdate(update);
				message = "Breakdown properly added.";
			}
			else
			{
				message = "Error : Unable to reach database.";
			}
		}
		catch(Exception e)
		{
			message =  "Error : " + e.getMessage();
		}
		finally
		{
			if(conn!=null)
			{
				DBManager.getInstance().cleanup(conn, stat, null);
			}
		}		
		return message;
	}
	
	public String fix(int id,boolean fixed)
	{
		String update = "UPDATE pannes SET reparee="+fixed+" WHERE id="+id+";";
		Connection conn = null;
		Statement stat = null;
		String message ;
		
		try
		{
			conn = DBManager.getInstance().getConnection();
			if( conn != null)
			{
				stat = conn.createStatement();
				stat.executeUpdate(update);
				message = "Breakdown properly update.";
			}
			else
			{
				message = "Error : Unable to reach database.";
			}
		}
		catch(Exception e)
		{
			message =  "Error : " + e.getMessage();
		}
		finally
		{
			if(conn!=null)
			{
				DBManager.getInstance().cleanup(conn, stat, null);
			}
		}		
		return message;
	}
	
	public String addMany(int nbPannes)
	{
		int cpt = nbPannes;	
		int i=0;
		boolean stop = false;
		
		while(!stop && i<nbPannes)
		{
			String tmp = this.addRandom();
			if(tmp.equals("Error : Unable to reach database."))
			{
				stop = true;
				cpt = 0;
			}
			else if(!tmp.equals("Breakdown properly added."))
			{
				cpt--;
			}
			i++;
		}
		
		if(stop)
		{
			return "Error : Unable to reach database.";
		}
		else
		{
			return cpt+" out of "+ nbPannes +" breakdown(s) properly added.";
		}
	}
	public String addRandom()
	{
		String machine = this.randomMachine();
		return this.add(machine, this.randomTypePanne(machine));
	}
	
	private String randomMachine()
	{
		String machine = "";
		
		for(int i=0;i<16;i++)
		{
			Integer n = new Integer((int)(Math.random()*16));
			machine += Integer.toHexString(n);
		}
		
		return machine;
	}
	
	private TypePanne randomTypePanne(String machine)
	{
		TypePanne typepanne;
		Character premCar = machine.toLowerCase().charAt(0);
		
		if(Character.isDigit(premCar) &&  (Integer.valueOf(premCar.toString())>=0) && (Integer.valueOf(premCar.toString())<6))
		{
			int n = (int)(Math.random()*3);
			
			switch(n)
			{
			case 0:
				typepanne = TypePanne.CRASH_DISQUE;
				break;
			case 1:
				typepanne = TypePanne.PROBLEME_MEMOIRE;
				break;
			default:
				typepanne = TypePanne.RESEAU;
				break;
			}
		}
		else
		{
			typepanne = TypePanne.RESEAU;
		}		
		return typepanne;
	}

	public int nbMinute() {
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<60;");
	}

	public int nbHour() {
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<3600;");
	}

	public int nbDay() {
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<86400;");
	}

	public int nbMonth() {
		// 1 mois de 30 jours
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<2592000;");
	}

	public int nbEver() {
		return count("select count(id) as n from pannes;");
	}
	
	private int count(String query) {
		Connection conn = null;
		int n=0;		
		Statement stat = null;
		ResultSet rs = null;
		
		try {
			conn = DBManager.getInstance().getConnection();
			if (conn != null) {
				stat = conn.createStatement();
				rs = stat.executeQuery(query);
				while(rs.next())
				{
					n = rs.getInt("n");
				}
			}
			else
			{
				n = -1;
			}
		} catch (Exception e) {
			n = -1;

		} finally {
			if (conn != null) {
				DBManager.getInstance().cleanup(conn, stat, rs);
			}
		}
		return n;
	}
	
	private ListWithErr<Panne> find(String query) 
	{		
		ListWithErr<Panne> listWithErr = new ListWithErr<>();		
		List<Panne> listPannes = new ArrayList<Panne>();
		
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			conn = DBManager.getInstance().getConnection();
			if (conn != null) 
			{
				stat = conn.createStatement();
				rs = stat.executeQuery(query);
				while (rs.next()) 
				{
					int id = rs.getInt("id");
					String hour = rs.getString("heure");
					String typepanneName = rs.getString("typepanne");
					String machine = rs.getString("machine");
					String typemachineName = rs.getString("typemachine");
					Boolean reparee = rs.getBoolean("reparee");
					listPannes.add(new Panne(id,hour,toTypePanne(typepanneName),machine,toTypeMachine(typemachineName),reparee));				
				}
				listWithErr = new ListWithErr<Panne>(listPannes,true);
			}
			else
			{
				listWithErr.setValide(false);
			}
		} 
		catch (Exception e) 
		{
			listWithErr.setValide(false);

		} finally {
			if (conn != null) {
				DBManager.getInstance().cleanup(conn, stat, rs);
			}
		}
		return listWithErr;
	}
	
	private TypePanne toTypePanne(String n)
	{
		TypePanne typepanne;
		if(n.equals("reseau"))
		{
			typepanne = TypePanne.RESEAU;
		}
		else if(n.equals("crash_disque"))
		{
			typepanne = TypePanne.CRASH_DISQUE;
		}
		else
		{
			typepanne = TypePanne.PROBLEME_MEMOIRE;
		}
		return typepanne;
	}
	private TypeMachine toTypeMachine(String n)
	{
		TypeMachine typemachine;
		if(n.equals("serveur"))
		{
			typemachine = TypeMachine.SERVEUR;
		}
		else if(n.equals("routeur"))
		{
			typemachine = TypeMachine.ROUTEUR;
		}
		else
		{
			typemachine = TypeMachine.PARE_FEUX;
		}
		return typemachine;
	}
	
	public ListWithErr<Panne> lastMinute()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<60 ORDER BY heure DESC;");
	}
	public ListWithErr<Panne> lastHour()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<3600 ORDER BY heure DESC;");
	}
	public ListWithErr<Panne> lastDay()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<86400 ORDER BY heure DESC;");
	}
	public ListWithErr<Panne> lastMonth()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<2592000 ORDER BY heure DESC;");
	}
	public ListWithErr<Panne> ever()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes ORDER BY heure DESC;");
	}
	public ListWithErr<Panne> lastMinutePerType()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<60 ORDER BY typemachine;");
	}
	public ListWithErr<Panne> lastHourPerType()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<3600 ORDER BY typemachine");
	}
	public ListWithErr<Panne> lastDayPerType()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<86400 ORDER BY typemachine;");
	}
	public ListWithErr<Panne> lastMonthPerType()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes WHERE TIME_TO_SEC(TIMEDIFF(NOW(),heure))<2592000 ORDER BY typemachine;");
	}
	public ListWithErr<Panne> everPerType()
	{
		return find("SELECT id,heure,typepanne,machine,typemachine,reparee FROM pannes ORDER BY typemachine;");
	}
}
