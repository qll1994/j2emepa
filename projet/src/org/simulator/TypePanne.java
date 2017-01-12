package org.simulator;

//Classe de type énumération relative aux types de pannes 
public enum TypePanne 
{
	RESEAU ("reseau"),
	CRASH_DISQUE ("crash_disque"),
	PROBLEME_MEMOIRE ("probleme_memoire");
	
	private String state;
	
	private TypePanne(String state)
	{
		this.state = state;
	}
	
	public String getState()
	{
		return this.state;
	}
}
