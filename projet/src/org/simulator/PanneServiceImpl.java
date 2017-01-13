package org.simulator;

import java.util.ArrayList;
import java.util.List;

public class PanneServiceImpl implements PanneService
{
	
	private PanneDAO panneDao = new PanneDAOImpl();

	/**
	 * ajoute une panne à la base de données,
	 *la machine qui subit la panne et le type de la panne sont passés en paramètre
	 */
	public String ajoutPanne(String machine, TypePanne typepanne) 
	{
		return panneDao.add(machine,typepanne);		
	}

	/**
	 * ajout de pannes de manière aléatoire
	 */
	public String ajoutPanneAlea()
	{
		String machine = this.randomMachine();
		return panneDao.add(machine, this.randomTypePanne(machine));
	}
	
	/**
	 * ajout de nbPannes pannes de manière aléatoire
	 */
	public String ajoutPannes(int nbPannes)
	{
		int cpt = nbPannes;	
		int i=0;
		boolean stop = false;
		
		while(!stop && i<nbPannes)
		{
			String tmp = this.ajoutPanneAlea();
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
	
	
	/**
	 * Ces méthodes 
	 * comptent le nombre de pannes survenues lors :
	 * de la dernière minute, de la dernière heure, du dernier jour,
	 * du dernier mois et le total
	 */
	public int nbMinute() {
		return panneDao.nbMinute();
	}

	public int nbHour() {
		return panneDao.nbHour();
	}

	public int nbDay() {
		return panneDao.nbDay();
	}

	public int nbMonth() {
		return panneDao.nbMonth();
	}

	public int nbEver() {
		return panneDao.nbEver();
	}
	
	
	/**
	 * Renvoie une liste avec le nombre de pannes survenues chaque 
	 *heure du jour passé en paramètre (sert à tracer l'histograme)
	 */
	public ListWithErr<Integer> nbPannesJour(int jour, int mois, int annee)
	{
		ListWithErr<Integer> finalList = new ListWithErr<Integer>();	
		List<Integer> list = new ArrayList<Integer>();
		boolean valide = true;
		
		int i=0;
		
		while((i<24)&& valide)
		{
			int tmp = panneDao.nbPanne(i,1, jour, mois, annee);
			if(tmp>=0)
			{
				list.add(i, tmp);
			}
			else
			{
				valide = false;
			}
			i++;
		}
		finalList.setValide(valide);
		finalList.setList(list);		
		return finalList;
	}

	
	/**
	 * Renvoie le nombre de pannes survenues a la date et l'heure passées en paramètres
	 */
		
	public ListWithErr<Panne> lastMinute() {
		return panneDao.lastMinute();
	}

	public ListWithErr<Panne> lastHour() {
		return panneDao.lastHour();
	}

	public ListWithErr<Panne> lastDay() {
		return panneDao.lastDay();
	}

	public ListWithErr<Panne> lastMonth() {
		return panneDao.lastMonth();
	}

	public ListWithErr<Panne> ever() {
		return panneDao.ever();
	}

	public ListWithErr<Panne> lastMinutePerType() {
		return panneDao.lastMinutePerType();
	}

	public ListWithErr<Panne> lastHourPerType() {
		return panneDao.lastHourPerType();
	}

	public ListWithErr<Panne> lastDayPerType() {
		return panneDao.lastDayPerType();
	}

	public ListWithErr<Panne> lastMonthPerType() {
		return panneDao.lastMonthPerType();
	}

	public ListWithErr<Panne> everPerType() {
		return panneDao.everPerType();
	}

	
	/**
	 * indique qu'une panne a été réparée
	 */
	public String fix(int id, boolean fixed) {
		return panneDao.fix(id,fixed);
	}
	
	
	/**
	 * Choisit une machine au hasard (sert à la génération aléatoire de panne)
	 * @return
	 */
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
	
	/**
	 * Choisit une panne au hasard
	 * @param machine
	 * @return
	 */
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
}

