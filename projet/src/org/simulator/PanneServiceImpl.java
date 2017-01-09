package org.simulator;

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
