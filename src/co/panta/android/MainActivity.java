package co.panta.android;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import co.panta.android.adapter.NavDrawerListAdapter;
import co.panta.android.model.NavDrawerItem;
import co.panta.android.model.Panta;
import co.panta.android.pojos.Usuario;
import co.panta.android.pojos.Viaje;
import co.panta.android.services.API;
import co.panta.android.services.DownloadJsonTask;
import co.panta.android.services.F;
import co.panta.android.services.ShakeDetectActivity;
import co.panta.android.services.ShakeDetectActivityListener;

public class MainActivity extends Activity {

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			displayView(position);
		}
	}

	private static final String PERSISTENCIA_ARCHIVO_VIAJES = "viajes.data";

	/**
	 * SERVICIOS DE LA CLASE
	 */
	public static final String INTENT_BLOQUEAR_USUARIO = "co.panta.android.bloquear_usuario";

	/**
	 * SERVICIOS QUE PIDE LA CLASE
	 */
	public static final String INTENT_DAR_VIAJE = "co.panta.android.pojos.Viaje";
	public static final String INTENT_REQUEST_LOGIN_DETAILS = "co.panta.android.model.Panta";

	public static final int REQUEST_LOGIN_DETAILS = 100;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;

	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;
	// slide menu items
	private String[] navMenuTitles;

	private TypedArray navMenuIcons;
	private ArrayList<NavDrawerItem> navDrawerItems;

	private NavDrawerListAdapter adapter;

	private Panta panta;

	private ShakeDetectActivity shakeDetectActivity;

	public void actualizarBurbujaViajes(int numero) {
		navDrawerItems.get(0).setCount(numero + "");
	}

	public void actualizarBurbujaViajes(String numero) {
		navDrawerItems.get(0).setCount(numero);
	}

	/**
	 * Carga la lista de viajes desde la memoria interna privada al modelo del
	 * mundo
	 * 
	 * @return true si se lograron cargar viajes
	 */
	private boolean cargarPantaMemoria() {

		boolean cargado = false;

		Context context = getApplicationContext();

		FileInputStream fis;
		try {

			fis = context.openFileInput(PERSISTENCIA_ARCHIVO_VIAJES);
			ObjectInputStream is = new ObjectInputStream(fis);
			Panta mundo = (Panta) is.readObject();
			is.close();

			if (mundo != null && mundo.fechaActualizacion != null
					&& mundo.viajes.size() > 0) {
				Log.d("panta", "mundo desde memoria " + mundo);
				panta = mundo;
				cargado = true;
			}

		} catch (FileNotFoundException e) {
			Log.d("panta", "Primera ejecución, no hay caché");
		} catch (OptionalDataException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		actualizarBurbujaViajes(panta.darNumeroViajes());
		panta.ordenarPorHora();

		return cargado;

	}

	public void actualizarListaViajes()
	{
		
		actualizarBurbujaViajes(panta.darNumeroViajes());
		panta.ordenarPorHora();
		displayView(0);
	}
	
	private void cargarViajesServidor() {

		ArrayList<Viaje> nuevosViajes = new ArrayList<Viaje>();

		new DownloadJsonTask(this, nuevosViajes).execute(API.buildURL(API.GET_TRIPS,
				panta.usuario, panta.userSecret));

//		displayView(0);

		
		panta.actualizar(nuevosViajes);
		

		F.echo(this, "Actualizando los viajes ...");

	}

	public void cerrarSesion() {

		panta.usuario = null;
		panta.userSecret = null;

		Intent intentLogin = new Intent(this, Login.class);
		intentLogin.putExtra(INTENT_REQUEST_LOGIN_DETAILS, panta);

		this.startActivityForResult(intentLogin,
				MainActivity.REQUEST_LOGIN_DETAILS);

	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragmento fragment = null;
		switch (position) {
		case 0:
			fragment = new ListaViajesFragment();
			break;
		case 1:
			fragment = new PublicarViajeFragment();
			break;
		case 2:
			fragment = new PhotosFragment();
			break;
		case 3:
			fragment = new SobrePantaFragment();
			break;
		case 4:
			fragment = new PagesFragment();
			break;
		case 5:
			cerrarSesion();
			break;

		default:
			break;
		}

		if (fragment != null) {
			// Log.d("panta frags", "voy a poner el fragment " + panta);
			fragment.setMundo(panta);

			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("panta", "Error in creating fragment");
		}
	}

	private boolean estaLogueado() {
		return panta.tengoCredenciales();
	}

	private void guardarViajesMemoria() {

		Context context = getApplicationContext();
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(PERSISTENCIA_ARCHIVO_VIAJES,
					Context.MODE_PRIVATE);

			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(panta);
			os.close();
			// echo("exito persistiendo");

		} catch (FileNotFoundException e) {

			Log.d("panta", e.getLocalizedMessage());
			F.echo(this, "ERROR: el archivo de persistencia no existe");

		} catch (IOException e) {

			Log.d("panta", e.getLocalizedMessage());
			F.echo(this, "ERROR: no se pudo guardar los viajes");
		} finally {

			try {
				fos.close();
			} catch (IOException e) {
				Log.d("panta", e.getLocalizedMessage());
				F.echo(this, "ERROR: cerrando el archivo de persistencia");

			}
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_LOGIN_DETAILS) {
			String[] datos = data.getStringExtra(
					MainActivity.INTENT_REQUEST_LOGIN_DETAILS).split("::");

			Log.d("panta", "volvi de login " + panta);

			panta.almacenarCredenciales(datos[0], datos[1]);

			F.echo(this, "Bienvenido " + panta.usuario);

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Log.d("panta", "Comienza onCreate()");

		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		// mRightDragger or mLeftDragger based on Drawer Gravity

		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons
				.getResourceId(0, -1), true, "22"));
		// Find People
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons
				.getResourceId(1, -1)));
		// Photos
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons
				.getResourceId(2, -1)));
		// Communities, Will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons
				.getResourceId(3, -1)));
		// Pages
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons
				.getResourceId(4, -1)));
		// What's hot, We will add a counter here
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons
				.getResourceId(5, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		panta = new Panta();

		cargarPantaMemoria();

		revisarPeticionBloqueo();

		actualizarBurbujaViajes(panta.darNumeroViajes());


		// CONTROL APLICACI�N
		if (!estaLogueado()) {
			Intent intentLogin = new Intent(this, Login.class);
			intentLogin.putExtra(INTENT_REQUEST_LOGIN_DETAILS, panta);

			this.startActivityForResult(intentLogin,
					MainActivity.REQUEST_LOGIN_DETAILS);
		}

		// F.echo(this, panta.actualizaciones + " Actualizaciones");

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
		
		
		shakeDetectActivity = new ShakeDetectActivity(this);
		shakeDetectActivity.addListener(new ShakeDetectActivityListener() {
			
			@Override
			public void shakeDetected() {
				// TODO Auto-generated method stub
				hayShake();
			}
		});
		
	}

	protected void hayShake() {
		F.vibrar(this, 500);
		cargarViajesServidor();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			F.echo(this, "Opción no disponible", Toast.LENGTH_LONG);
			return true;
//		case R.id.action_logout:
//			cerrarSesion();
//			return true;
//		case R.id.action_filtrar:
//			F.echo(this, "Hay " + panta.viajes.size() + " viajes");
//			return true;
			
		case R.id.action_refrescar:
			cargarViajesServidor();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_refrescar).setVisible(!drawerOpen);
//		menu.findItem(R.id.action_filtrar).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		actualizarBurbujaViajes(panta.darNumeroViajes() + "");
		shakeDetectActivity.onResume();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		shakeDetectActivity.onPause();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// F.echo(this, panta.usuario + " logueado: " + estaLogueado() + ". " +
		// panta.actualizaciones + " actualizaciones");
		actualizarBurbujaViajes(panta.darNumeroViajes() + "");

		if (panta.necesitoActualizarme())
			cargarViajesServidor();

	}

	@Override
	protected void onStop() {
		super.onStop();

		guardarViajesMemoria();
	}

	private void revisarPeticionBloqueo() {

		Usuario aBloquear = (Usuario) getIntent().getSerializableExtra(
				MainActivity.INTENT_BLOQUEAR_USUARIO);

		if (aBloquear != null)
			panta.bloquearUsuario(aBloquear);

		actualizarBurbujaViajes(panta.darNumeroViajes() + "");
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	public void ventana(String mensaje) {
		// 1. Instantiate an AlertDialog.Builder with its constructor
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	               // User clicked OK button
	           }
	       });

		// 2. Chain together various setter methods to set the dialog characteristics
		builder.setMessage(mensaje)
		       .setTitle(getString(R.string.app_name));

		// 3. Get the AlertDialog from create()
		AlertDialog dialog = builder.create();
		
	}

}
