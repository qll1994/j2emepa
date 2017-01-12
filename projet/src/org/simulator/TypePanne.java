package org.simulator;
/**
 * Classe de type énumération relative aux types de pannes 
 */
public enum TypePanne 
{
	RESEAU ("reseau"),
	CRASH_DISQUE ("crash_disque"),
	PROBLEME_MEMOIRE ("probleme_memoire");
	
	private String state;
	/**
	 * Constructeur privé
	 * @param state
	 */
	private TypePanne(String state)
	{
		this.state = state;
	}
	/**
	 * Getteur d'état de panne
	 * @return état de panne
	 */
	public String getState()
	{
		return this.state;
	}
}
