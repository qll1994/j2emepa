package org.simulator;

public interface PanneDAO 
{
	String add(String machine, TypePanne typepanne);
	String addRandom();
	String addMany(int nbPannes);
	String fix(int id, boolean fixed);
	
	int nbMinute();
	int nbHour();
	int nbDay();
	int nbMonth();
	int nbEver();
	int nbPanne(int heureDebut, int duree, int jour, int mois, int annee);
	
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
}
