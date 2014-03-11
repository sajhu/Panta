package co.panta.android.model;

import java.io.Serializable;
import java.util.ArrayList;

import co.panta.android.pojos.Viaje;

public class Panta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public ArrayList<Viaje> viajes;
	
	public Panta() {
		viajes = new ArrayList<Viaje>();
	}
	
	public void agregarViaje(Viaje viaje)
	{
		viajes.add(viaje);
	}
	
	public void eliminarTodos()
	{
		viajes = new ArrayList<Viaje>();
	}
	
	public Viaje darViaje(int id)
	{
		for (int i = 0; i < viajes.size(); i++) {
			Viaje viaje = viajes.get(i);
			
			if(viaje.id == id)
				return viaje;
		}
		
		return null;
	}
}
