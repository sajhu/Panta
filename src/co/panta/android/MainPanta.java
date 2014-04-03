package co.panta.android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import co.panta.android.model.Panta;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.DownloadJsonTask;

public class MainPanta extends FragmentActivity {

	private static final String PERSISTENCIA_ARCHIVO_VIAJES = "viajes.data";

	public static final String INTENT_DAR_VIAJE = "co.panta.android.pojos.Viaje";
	public static final String INTENT_REFERENCIA_MUNDO = "co.panta.android.model.Panta";

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	private Panta panta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_panta);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		
		
		
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		
		
		// CONTROL APLICACIÓN
		if(!estaLogueado())
		{
	        Intent intentLogin = new Intent(this, LoginActivity.class);
	        intentLogin.putExtra(INTENT_REFERENCIA_MUNDO, panta);
	        
	        this.startActivity(intentLogin);
		}

        
		panta = new Panta();
		
		
		boolean seCargo = cargarViajesMemoria();
		
		echo(panta.actualizaciones + " Actualizaciones");
		if(!seCargo || panta.necesitoActualizarme())
			cargarViajesServidor();

		
	
	}
	

	@Override
	protected void onResume() {
		super.onResume();
		
		//echo("Volvimos ...");

		//cargarViajesServidor();

		
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		guardarViajesMemoria();
	}

	
	private void guardarViajesMemoria() {

		Context context = getApplicationContext();
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(PERSISTENCIA_ARCHIVO_VIAJES, Context.MODE_PRIVATE);
			
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(panta);
			os.close();	
			//echo("exito persistiendo");

			
		} catch (FileNotFoundException e) {
			
			Log.d(MainPanta.class.toString(), e.getLocalizedMessage());
			echo("ERROR: el archivo de persistencia no existe");
			
		} catch (IOException e) {
			
			Log.d(MainPanta.class.toString(), e.getLocalizedMessage());
			echo("ERROR: no se pudo guardar los viajes");
		} finally {
			
			try {
				fos.close();
			} catch (IOException e) {
				Log.d(MainPanta.class.toString(), e.getLocalizedMessage());
				echo("ERROR: cerrando el archivo de persistencia");

			}
		}
		
	}


	private void cargarViajesServidor() {
		
		
		
		//TODO poner el codigo de carga.. debe generar un ArrayList
		// de Viaje (ArrayList<Viaje>) y llamar a panta
		
		ArrayList<Viaje> nuevosViajes = new ArrayList<Viaje>();
		
		
		new DownloadJsonTask(nuevosViajes).execute(DownloadJsonTask.URL_BASICA);
//		Viaje viajePrueba = new Viaje(0, "Salgo por la #7 luego la #140 hasta #Cedritos. PRE", "2014-03-12", "2100", 3);
//		
//		Usuario usuarioPrueba = new Usuario(1, "Santiago", "Rojas", "http://wheels.comoj.com/fotos/1.png", "3016957229");
//		
//		viajePrueba.setDriver(usuarioPrueba);
//		
//		nuevosViajes.add(viajePrueba);
		
		
		//NO OLVIDAR REVISAR CASOS DE RESPONSE != 0, en especial response 1
		panta.actualizar(nuevosViajes);
		
		echo(nuevosViajes.size() + " nuevos viajes descargados.");
		
	}

	/**
	 * Carga la lista de viajes desde la memoria interna privada al modelo del mundo
	 * @return true si se lograron cargar viajes
	 */
	private boolean cargarViajesMemoria() {
		
		boolean cargado = false;
		
		Context context = getApplicationContext();
		
		FileInputStream fis;
		try {
			
			fis = context.openFileInput(PERSISTENCIA_ARCHIVO_VIAJES);
			ObjectInputStream is = new ObjectInputStream(fis);
			panta = (Panta) is.readObject();
			is.close();
			
			if(panta.viajes.size() > 0)
				cargado = true;

		} catch (FileNotFoundException e) {
			Log.d(MainPanta.class.toString(), "Primera ejecución, no hay caché");
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		return cargado;
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_panta, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
			    
	    
	    switch(item.getItemId()) {
	    
	    case R.id.action_settings:
	        Intent intentSettings = new Intent(this, SettingsActivity.class);
	        this.startActivity(intentSettings);
	        break;
	        
	    case R.id.action_demo_viaje:
	        Intent intentDemo = new Intent(this, DetalleViajeActivity.class);
	        if(panta.viajes.get(0) != null)
	        	intentDemo.putExtra(INTENT_DAR_VIAJE, panta.viajes.get(0));
	        else
	        	echo("no tengo viajes");
	        this.startActivity(intentDemo);
	        break;
	        
	    case R.id.action_login:
	        Intent intentLogin = new Intent(this, LoginActivity.class);
	        this.startActivity(intentLogin);
	        break;
	        
	    case R.id.action_refresh:
	        cargarViajesServidor();
	        break;
	        
	    default:
	        return super.onOptionsItemSelected(item);
	        
	    }

	    return true;
	}
	


	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			Fragment fragment = null;
			
			echo ("pido " + position);
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			
			
			// Aqui se dice que fragmento mostrar según la posición
			switch (position) {
			case 0:
				fragment = new PasajeroFragment();
				
			case 1:
				fragment = new DummySectionFragment();
				
			case 2:
				fragment = new DummySectionFragment();
			}
			

			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section_pasajero).toUpperCase(l);
			case 1:
				return getString(R.string.title_section_conductor).toUpperCase(l);
			case 2:
				return getString(R.string.title_section_perfil).toUpperCase(l);
			}
			return null;
		}
	}

	public void echo(String text)
	{
		Context context = getApplicationContext();
		int duration = Toast.LENGTH_SHORT;

		
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	private boolean estaLogueado()
	{
		if(!panta.tengoCredenciales())
			return false;
		
		
		
		return true;
	}
	
	
}
