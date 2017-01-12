package org.simulator;
/**
 * Classe Panne permettant de créer une panne avec tous ses attributs 
 *
 */
public class Panne 
{
	private int id;
	private String heure;
	private TypePanne typepanne;
	private String machine;
	private TypeMachine typemachine;
	private boolean reparee;
	
	public Panne(int id, String heure, TypePanne typepanne, String machine, TypeMachine typemachine,boolean reparee) 
	{
		this.id = id;
		this.heure = heure;
		this.typepanne = typepanne;
		this.machine = machine;
		this.typemachine = typemachine;
		this.reparee = reparee;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getHeure() {
		return heure;
	}
	public void setHeure(String heure) {
		this.heure = heure;
	}
	public TypePanne getTypepanne() {
		return typepanne;
	}
	public void setTypepanne(TypePanne typepanne) {
		this.typepanne = typepanne;
	}
	public String getMachine() {
		return machine;
	}
	public void setMachine(String machine) {
		this.machine = machine;
	}
	public TypeMachine getTypemachine() {
		return typemachine;
	}
	public void setTypemachine(TypeMachine typemachinne) {
		this.typemachine = typemachinne;
	}
	public boolean isReparee() {
		return reparee;
	}
	public void setReparee(boolean reparee) {
		this.reparee = reparee;
	}	
	
}
