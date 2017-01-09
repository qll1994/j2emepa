package org.simulator;

public class PannesDuree 
{
	private int nbPannesTotal;
	private int nbPannesCurrent;
	private int dureeRestante;
	private int duree;
	private int inter;
	
	public PannesDuree(int nbPannesTotal, int duree, int inter)
	{
		this.nbPannesCurrent = 0;
		this.nbPannesTotal = nbPannesTotal;
		this.duree = duree;
		this.dureeRestante = duree;
		this.inter = inter;
	}

	public int getNbPannesCurrent() {
		return nbPannesCurrent;
	}

	public void setNbPannesCurrent(int nbPannesCurrent) {
		this.nbPannesCurrent = nbPannesCurrent;
	}

	public int getNbPannesTotal() {
		return nbPannesTotal;
	}

	public int getDuree() {
		return duree;
	}

	public int getDureeRestante() {
		return dureeRestante;
	}

	public void setDureeRestante(int dureeRestante) {
		this.dureeRestante = dureeRestante;
	}

	public int getInter() {
		return inter;
	}
	
	
}
