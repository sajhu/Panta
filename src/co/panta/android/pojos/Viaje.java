package co.panta.android.pojos;

import java.io.Serializable;

public class Viaje implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String descripcion;
	public String fecha;
	public String hora;
	public String latitud;
	public String longitud;
	public int sillas;
	public int destino;
	public Usuario conductor;

	public Viaje(int id, String descripcion, String string, String hora, int sillas, int destino)
	{
		this.id = id;
		this.descripcion = descripcion;
		this.fecha = string;
		this.hora = hora ;
		this.sillas = sillas;
		this.destino = destino;
		
	}
	
	public void setLatLng(String lat, String lon)
	{
		this.latitud = lat;
		this.longitud = lon;
	}
	
	public void setDriver(Usuario driver)
	{
		this.conductor = driver;
	}
	
	public String toString(){
		return descripcion;
	}
}
