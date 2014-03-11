package co.panta.android.pojos;

import java.io.Serializable;

public class Usuario implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1900485448636327210L;
	
	public String apellido;
	public String nombre;
	public String telefono;
	public String foto;
	public int id;
	public String correo;
	public int viajes;
	public int pasajeros;


	public Usuario (int id, String nombre, String apellido, String foto, String telefono)
	{

		this(id, nombre, apellido, foto, telefono, null, 0, 0);
	}
	
	public Usuario(int id, String nombre, String apellido, String foto, String telefono, String correo, int viajes, int pasajeros)
	{
		this.id = id;
		this.nombre = nombre;
		this.apellido = apellido;
		this.foto = foto;
		this.telefono = telefono;
		this.correo = correo;
		this.viajes = viajes;
		this.pasajeros = pasajeros;
	}
	
	
	public String toString()
	{
		return nombre + " " + apellido;
	}
	
	public String prettyName()
	{
		return nombre;
	}

}


