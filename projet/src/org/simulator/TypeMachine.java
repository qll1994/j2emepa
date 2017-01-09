package org.simulator;

public enum TypeMachine 
{
	SERVEUR ("serveur"),
	PARE_FEUX ("pare-feux"),
	ROUTEUR ("routeur");
	
	private String state;
	
	private TypeMachine(String state)
	{
		this.state = state;
	}
	
	public String getState()
	{
		return this.state;
	}
}
