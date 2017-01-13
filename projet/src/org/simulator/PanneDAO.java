package org.simulator;

public interface PanneDAO 
{
	/**
	 * Renvoi le résultat d'ajout d'une panne
	 * @param machine
	 * @param typepanne
	 * @return message de résultat
	 */
	String add(String machine, TypePanne typepanne);
	/**
	 * Renvoi le résultat de réparer d'une panne
	 * @param id
	 * @param fixed
	 * @return le résultat d'activité
	 */
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
