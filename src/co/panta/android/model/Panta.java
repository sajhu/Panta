package co.panta.android.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import android.annotation.SuppressLint;
import co.panta.android.pojos.Usuario;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.BooleanTable;

public class Panta implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final boolean soloHoy = true;

//	private static Panta instancia;
	
	
	public Date fechaActualizacion;
	
	public int actualizaciones = 0;
	
	public ArrayList<Viaje> viajes;
	
	public String usuario;
	
	public String userSecret;
	
	
	private BooleanTable usuariosBloqueados;

	private Date fechaCreacion;

//	public static Panta getInstance()
//	{
//	    if (instancia == null)
//	    {
//	    	instancia = new Panta();
//	    }
//	    return instancia;
//	}
	
	public Panta() {
		viajes = new ArrayList<Viaje>();
		fechaCreacion = new Date();
		fechaActualizacion = new Date();
		usuariosBloqueados = new BooleanTable();
	}

	/**
	 * Agrega el usuario dado por parametro a la lista de bloqueos, impididendo as�
	 * que aparezca en las listas de viajes
	 * @param aBloquear el usuario a bloquear
	 */
	public void bloquearUsuario(Usuario aBloquear)
	{
		usuariosBloqueados.put(aBloquear.id, true);
		
		revisarBloqueados();
	}
	
	/**
	 * Revisa si el usuario indicado est� bloqueado
	 * @param usuario el usuario a revisar
	 * @return true si el usuario ha sido bloqueado
	 */
	public boolean estaBloqueado(Usuario usuario)
	{
		return usuariosBloqueados.get(usuario.id);
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
		if(usuariosBloqueados.get(viaje.conductor.id))
			return;
		
		viajes.add(viaje);
	}

	/**
	 * actualiza la lista de viajes con un nuevo ArrayList, eliminando los viejos
	 * @param nuevosViajes
	 */
	public void actualizar(ArrayList<Viaje> nuevosViajes)
	{
		
		viajes = nuevosViajes;
		
		ordenarPorHora();
		
		revisarBloqueados();
		
		fechaActualizacion = new Date();
		
		actualizaciones++;
		
		//return "A" + actualizaciones +": " + viajes.size() + " nuevos viajes"; 
	}

	public void ordenarPorHora() {
		ArrayList<Viaje> arreglo = (ArrayList<Viaje>) viajes.clone();
        for( int i = 1; i < arreglo.size(); i++ )
        {
            for( int j = i; j > 0 && compararHoras(viajes.get(j - 1), viajes.get(j)); j-- )
            {
            	Viaje j0 = viajes.get(j);
            	Viaje j1 = viajes.get(j - 1);
            	
                Viaje temp = j0;
                j0 = j1;
                j1 = temp;
            }
        }
       
		
	}
	
	private boolean compararHoras(Viaje uno, Viaje dos)
	{
		int horaUno = Integer.parseInt(uno.hora);
		int horaDos = Integer.parseInt(dos.hora);
		return horaUno > horaDos;
	}

	/**
	 * da el viaje seg�n su identificador
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

	@SuppressWarnings("unused")
	@SuppressLint("SimpleDateFormat")
	public boolean necesitoActualizarme()
	{
		if(viajes.size() == 0)
			return true;
		
		Date hoy = new Date();

		//TODO DEBER�A USAR LOCALE PARA USAR HORAS DEL USUARIO
		SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");

		boolean esHoy = fmt.format(hoy).equals(fmt.format(fechaActualizacion));


		Date ayer = new Date((long)System.currentTimeMillis()*1000 - (24 * 60 * 60 * 1000));

		boolean esAyer = fmt.format(ayer).equals(fmt.format(fechaActualizacion));

		if(!soloHoy && esAyer)
			return false;
		
		return !esHoy;
	}
	
	/**
	 * Retorna el número de viajes actuales
	 * @return
	 */
	public int darNumeroViajes()
	{
		return viajes.size();
	}
	
	public String toString()
	{
		return " (" + usuario + " : " + fechaCreacion +")";
		
	}
}
