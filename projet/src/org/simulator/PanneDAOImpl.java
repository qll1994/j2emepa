package org.simulator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PanneDAOImpl implements PanneDAO
{
	/**
	 * Ajoute une panne à la base de données
	 * La machine sur laquelle a lieu la panne et le type de la panne sont passés en paramètres.
	 * La méthode renvoie un message indiquant que la date a été ajoutée proprement en cas de succes,
	 * et renvoie une erreur en cas d'échec. 
	 */
	public String add(String machine, TypePanne typepanne) 
	{
		//nomenclature : machine commençant par 0 à 5 -> serveurs, de 6 à A -> pare-feux, de B à F -> routeur
		TypeMachine typemachine;
		
		/*Récupération du premier caractère et teste
		 * pour connaître le type de la machine 
		 */
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
		
		//"update" contient la requète sql pour insérer la panne
		String update = "insert into pannes values (null,NOW(),\""+typepanne.getState()+"\",\""+machine+"\",\""+typemachine.getState()+"\",false);";
		Connection conn = null;
		Statement stat = null;
		String message ;
		
		try
		{
			//Connexion à la base de données
			conn = DBManager.getInstance().getConnection();
			if( conn != null)
			{
				stat = conn.createStatement();
				stat.executeUpdate(update); //exécusion de la requète sql
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
	
	
	/**
	 * Indique que la panne dont l'id est passé en paramètre a été réparée
	 * La méthode renvoie un message :
	 * - indiquant que la mis à jour a été faite
	 * - indiquant une erreur en cas d'échec 
	 */
	public String fix(int id,boolean fixed)
	{
		
		//"update" contient la requète sql pour enregistrer la réparation d'une panne
		String update = "UPDATE pannes SET reparee="+fixed+" WHERE id="+id+";";
		Connection conn = null;
		Statement stat = null;
		String message ;
		
		//connexion à la base de données
		try
		{
			conn = DBManager.getInstance().getConnection();
			if( conn != null)
			{
				stat = conn.createStatement();
				stat.executeUpdate(update);	//exécution de la requète sql
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
	
	
	/**
	 *  Méthode générique pour exécuter une requête sql passée en paramètre  
	 * Cette requète comporte le mot clé "count".
	 * La méthode renvoie l'entier résultat de la requète.
	 */
	private int count(String query) {
		Connection conn = null;
		int n=0;		
		Statement stat = null;
		ResultSet rs = null;
		
		
		//connexion à la base de données
		try {
			conn = DBManager.getInstance().getConnection();
			if (conn != null) {
				stat = conn.createStatement();
				rs = stat.executeQuery(query); //exécution de la commande sql
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
	
	/**
	 * Méthode générique pour exécuter une requête sql passée en paramètre
	 *La méthode une liste des pannes résultats de la requète 
	 */ 
	private ListWithErr<Panne> find(String query) 
	{		
		
		ListWithErr<Panne> listWithErr = new ListWithErr<>();		
		List<Panne> listPannes = new ArrayList<Panne>();
		
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		
		//connexion à la base de donnée
		try {
			conn = DBManager.getInstance().getConnection();
			if (conn != null) 
			{
				stat = conn.createStatement();
				
				//rs contient le résultat de la requète
				rs = stat.executeQuery(query); 
				while (rs.next()) 
				{
					int id = rs.getInt("id");
					String hour = rs.getString("heure");
					String typepanneName = rs.getString("typepanne");
					String machine = rs.getString("machine");
					String typemachineName = rs.getString("typemachine");
					Boolean reparee = rs.getBoolean("reparee");
					
					//ajout de la panne au résultat 
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
	
	/**
	 * compte le nombre de pannes survenues lors de la dernière minute
	 */
	public int nbMinute() {
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<60;");
	}

	/**
	 * compte le nombre de pannes survenues lors de la dernière heure
	 */
	public int nbHour() {
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<3600;");
	}

	/**
	 * compte le nombre de pannes survenues lors du dernier jour
	 */
	public int nbDay() {
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<86400;");
	}

	/**
	 * compte le nombre de pannes survenues lors du dernier mois
	 */
	public int nbMonth() {
		// 1 mois de 30 jours
		return count("select count(id) as n from pannes where TIME_TO_SEC(TIMEDIFF(NOW(),heure))<2592000;");
	}

	/**
	 * compte le nombre total de pannes
	 */
	public int nbEver() {
		return count("select count(id) as n from pannes;");
	}
	
	/**
	 * Renvoie le nombre de pannes survenues à la date et l'heure passé en paramètres
	 *La méthode évalue la méthode count() définie précédement 
	 *avec les arguments passés en paramètre	
	 */
	public int nbPanne(int heureDebut, int duree, int jour, int mois, int annee)
	{
		String heure = "\""+annee+"-"+mois+"-"+jour+" "+heureDebut+":00:00\"";
		return count("select count(*) as n from pannes where heure>="+heure+" AND (heure<addtime("+heure+",\""+duree+":00:00\"));");
	}
	
	/**
	 * renvoie le type de la panne en fonction 
	 *	de son nom (chaîne de caractère) passé en paramètre
	 */ 
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
	
	
	/**
	 * renvoie le type de la machine en fonction 
	 * de son nom (chaîne de caractère) passé en paramètre
	 */
	
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
	
	
	/**
	 *  Les méthodes suivantes renvoient le nombre de pannes pendant une
	 * période donnée éventuelement classé par type (pour les méthodes avec le suffixe "PerType")
	 * en évaluant la méthode find() définie précédement 
	 */
	
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

