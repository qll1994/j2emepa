package org.simulator;
/**
 * Classe de type énumération relative aux caractéristiques des machines
 */
public enum TypeMachine 
{
	SERVEUR ("serveur"),
	PARE_FEUX ("pare-feux"),
	ROUTEUR ("routeur");
	
	private String state;
	/**
	 * Constructeur privé 
	 * @param state
	 */
	private TypeMachine(String state)
	{
		this.state = state;
	}
	/**
	 * Getteur d'état
	 * @return état de machine
	 */
	public String getState()
	{
		return this.state;
	}
}
