package co.panta.android.pojos;

import java.io.Serializable;
import java.util.Date;

public class Viaje implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int id;
	public String descripcion;
	public Date fecha;
	public String hora;
	public int sillas;
	public Usuario conductor;

	public Viaje(int id, String descripcion, Date fecha, String hora, int sillas)
	{
		this.id = id;
		this.descripcion = descripcion;
		this.fecha = fecha;
		this.hora = hora ;
		this.sillas = sillas;
		
	}
	
	public void setDriver(Usuario driver)
	{
		this.conductor = driver;
	}
	
}
