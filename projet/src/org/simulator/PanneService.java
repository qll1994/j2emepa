package org.simulator;
/**
 * Interface 
 */
public interface PanneService 
{
	String ajoutPanne(String machine, TypePanne typepanne);
	
	String ajoutPanneAlea();
	String ajoutPannes(int nbPannes);
	
	int nbMinute();
	int nbHour();
	int nbDay();
	int nbMonth();
	int nbEver();
	
	ListWithErr<Integer> nbPannesJour(int jour, int mois, int annee);
	
	ListWithErr<Panne> lastMinute();
	ListWithErr<Panne> lastHour();
	ListWithErr<Panne> lastDay();
	ListWithErr<Panne> lastMonth();
	ListWithErr<Panne> ever();
	
	ListWithErr<Panne> lastMinutePerType();
	ListWithErr<Panne> lastHourPerType();
	ListWithErr<Panne> lastDayPerType();
	ListWithErr<Panne> lastMonthPerType();
	ListWithErr<Panne> everPerType();
	
	String fix(int id, boolean fixed);
	
}
