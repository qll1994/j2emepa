package org.simulator;

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