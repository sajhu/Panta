package co.panta.android.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import co.panta.android.pojos.Viaje;

public class Panta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<Viaje> viajes;

	public Date fechaActualización;
	
	public int actualizaciones = 0;

	public Panta() {
		viajes = new ArrayList<Viaje>();
		fechaActualización = new Date();
	}

	/**
	 * Agregar el viaje dado por parametro a la lista
	 * @param viaje - el viaje a ser agregado
	 */
	public void agregarViaje(Viaje viaje)
	{
		viajes.add(viaje);
	}

	/**
	 * actualiza la lista de viajes con un nuevo ArrayList, eliminando los viejos
	 * @param nuevosViajes
	 */
	public void actualizar(ArrayList<Viaje> nuevosViajes)
	{
		
		viajes = nuevosViajes;
		
		fechaActualización = new Date();
		
		actualizaciones++;
		
		//return "A" + actualizaciones +": " + viajes.size() + " nuevos viajes"; 
	}

	/**
	 * da el viaje según su identificador
	 * @param id int con el identificador
	 * @return Viaje el viaje
	 */
	public Viaje darViaje(int id)
	{
		for (int i = 0; i < viajes.size(); i++) {
			Viaje viaje = viajes.get(i);

			if(viaje.id == id)
				return viaje;
		}

		return null;
	}

	@SuppressLint("SimpleDateFormat")
	public boolean necesitoActualizarme()
	{
		if(viajes.size() == 0)
			return true;
		
		Date hoy = new Date();

		//TODO DEBERÍA USAR LOCALE PARA USAR HORAS DEL USUARIO
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

		boolean esHoy = fmt.format(hoy).equals(fmt.format(fechaActualización));


		Date ayer = new Date((long)System.currentTimeMillis()*1000 - (24 * 60 * 60 * 1000));

		boolean esAyer = fmt.format(ayer).equals(fmt.format(fechaActualización));

		return !esHoy && !esAyer;
	}
}
