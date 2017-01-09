package org.simulator;

import java.util.List;

public class ListWithErr<E>
{
	private List<E> list;
	private boolean valide;
	
	public ListWithErr()
	{
		
	}
	
	public ListWithErr(List<E> list, boolean valide)
	{
		this.list = list;
		this.valide = valide;
	}

	public List<E> getList() {
		return list;
	}

	public void setList(List<E> list) {
		this.list = list;
	}

	public boolean isValide() {
		return valide;
	}

	public void setValide(boolean valide) {
		this.valide = valide;
	}
	
	
}
