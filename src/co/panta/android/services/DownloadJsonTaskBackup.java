package co.panta.android.services;

import java.util.ArrayList;
import android.os.AsyncTask;
import co.panta.android.pojos.Usuario;
import co.panta.android.pojos.Viaje;

public class DownloadJsonTaskBackup extends AsyncTask<String, Void, ArrayList<Viaje>>{

	
	
	private ArrayList<Viaje> array;
	
	
	public DownloadJsonTaskBackup(ArrayList<Viaje> array) {
		
		this.array = array;
		
	}

	@Override
	protected ArrayList<Viaje> doInBackground(String... urls) {
		String JsonURL = urls[0];
		
		//bajar el json de JsonURL
		
		//EJEMPLO

			Viaje viajePrueba = new Viaje(0, "Salgo por la #7 hasta la #140. #Cedritos", "2014-03-12", "2100", 3);
		        
		        Usuario usuarioPrueba = new Usuario(1, "Santiago", "Rojas", "http://wheels.comoj.com/fotos/1.png", "3016957229");
		       
		    viajePrueba.setDriver(usuarioPrueba);
	       
	        array.add(viajePrueba);
		
		return array;
	}

}
