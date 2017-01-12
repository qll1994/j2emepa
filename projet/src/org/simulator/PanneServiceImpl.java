package org.simulator;

import java.util.ArrayList;
import java.util.List;
/**
 *
 */
public class PanneServiceImpl implements PanneService
{
	private PanneDAO panneDao = new PanneDAOImpl();

	public String ajoutPanne(String machine, TypePanne typepanne) 
	{
		return panneDao.add(machine,typepanne);		
	}

	public String ajoutPanneAlea()
	{
		return panneDao.addRandom();
	}
	/**
	 * Ajout de panne
	 */
	public String ajoutPannes(int nbPannes)
	{
		return panneDao.addMany(nbPannes);
	}
	
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

	public String fix(int id, boolean fixed) {
		return panneDao.fix(id,fixed);
	}
}
