package co.panta.android.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import android.annotation.SuppressLint;
import co.panta.android.pojos.Usuario;
import co.panta.android.pojos.Viaje;

public class Panta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	public Date fechaActualizacion;
	
	public int actualizaciones = 0;
	
	public ArrayList<Viaje> viajes;
	
	private String usuario;
	
	private String userSecret;
	
	
	private HashMap<Integer, Boolean> usuariosBloqueados;

	public Panta() {
		viajes = new ArrayList<Viaje>();
		fechaActualizacion = new Date();
		usuariosBloqueados = new HashMap<Integer, Boolean>();
	}

	/**
	 * Agrega el usuario dado por parametro a la lista de bloqueos, impididendo así
	 * que aparezca en las listas de viajes
	 * @param aBloquear el usuario a bloquear
	 */
	public void bloquearUsuario(Usuario aBloquear)
	{
		usuariosBloqueados.put(aBloquear.id, true);
		
		revisarBloqueados();
	}
	
	/**
	 * Revisa si el usuario indicado está bloqueado
	 * @param usuario el usuario a revisar
	 * @return true si el usuario ha sido bloqueado
	 */
	public boolean estaBloqueado(Usuario usuario)
	{
		return usuariosBloqueados.get(usuario.id) != null ? true : false;
	}
	
	/**
	 * Contrasta los viajes disponibles con la lista de viajes bloqueados
	 * si el viaje es de un conductor bloqueado es eliminado de la lista.
	 */
	public void revisarBloqueados()
	{
		for (int i = 0; i < usuariosBloqueados.size(); i++) {
			Usuario usuario = viajes.get(i).conductor;
			
			if(usuariosBloqueados.get(usuario.id))
				viajes.remove(i);
			
		}
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
		
		fechaActualizacion = new Date();
		
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
	
	public void almacenarCredenciales(String usuario, String userSecret)
	{
		this.usuario = usuario;
		this.userSecret = userSecret;
	}
	
	public boolean tengoCredenciales()
	{
		return usuario != null && userSecret != null;
	}

	@SuppressLint("SimpleDateFormat")
	public boolean necesitoActualizarme()
	{
		if(viajes.size() == 0)
			return true;
		
		Date hoy = new Date();

		//TODO DEBERÍA USAR LOCALE PARA USAR HORAS DEL USUARIO
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

		boolean esHoy = fmt.format(hoy).equals(fmt.format(fechaActualizacion));


		Date ayer = new Date((long)System.currentTimeMillis()*1000 - (24 * 60 * 60 * 1000));

		boolean esAyer = fmt.format(ayer).equals(fmt.format(fechaActualizacion));

		return !esHoy && !esAyer;
	}
}
