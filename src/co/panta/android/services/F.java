package co.panta.android.services;

import java.security.MessageDigest;
import java.util.UUID;

import co.panta.android.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Vibrator;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

public class F {
	
	
	public static void escribirPreferencia(Activity activity, String llave, String valor)
	{
		SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(llave, valor);
		editor.commit();
	}

	public static void escribirPreferencia(Activity activity, String llave, int valor)
	{
		SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putInt(llave, valor);
		editor.commit();
	}
	
	public static String leerPreferenciaString(Activity activity, String llave)
	{
		SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		
		return sharedPref.getString(llave, "");
	}
	
	public static int leerPreferenciaInt(Activity activity, String llave)
	{
		SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		
		return sharedPref.getInt(llave, 0);
	}
	
	/**
	 * Atajo para descargar informaci�n JSON.
	 * Debe llamarse dentro de un entorno AsyncTask, asume conectividad
	 * @param URL la direcci�n completa a descargar
	 * @return retorna el JSON obtenido de la URL indicada
	 */
	public String downloadJson(String URL)
	{
		ServiceHandler sh = new ServiceHandler();

		return sh.makeServiceCall(URL, ServiceHandler.GET);
	}
	 
	/**
	 * Hace que el dispositivo vibre por 100ms
	 * @param context el contexto actual
	 */
	public static void vibrar(Context context)
	{
		vibrar(context, 100);
	}
	
	/**
	 * Hace que el dispositivo vibre por un tiempo determinado
	 * @param context el contexto actual
	 * @param duracion la duración de la vibración en ms
	 */
	public static void vibrar(Context context, int duracion)
	{
		Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		 v.vibrate(duracion);
	}
	
	/**
	 * Imprime un Toast en el contexto dado por un tiempo corto
	 * @param context Activity actual
	 * @param text texto a desplegar
	 */
	public static void echo(Context context, String text)
	{
		echo(context, text, Toast.LENGTH_SHORT);
	}
	
	public static String string(Context context, int resId)
	{
		return context.getString(resId);
	}
	
	/**
	 * Imprime un Toast en el contexto dado por la duraci�n dada
	 * @param context Activity actual
	 * @param text texto a desplegar
	 * @param duration una constante de Toast.
	 */
	public static void echo(Context context, String text, int duration)
	{
		Toast toast = Toast.makeText(context, text, duration);
		toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER, 0, 0);
		
		TextView v = (TextView) toast.getView().findViewById(android.R.id.message);
		toast.show();
		
		toast.show();
	}
	
	/**
	 * Creates a secret out of a clear text password, adding a random salt and hashing with md5
	 * @param password password to obscure
	 * @return obscured userSecret from password
	 */
	public static String createSecret(String password)
	{
		String salt = UUID.randomUUID().toString();
		
		String hash = md5(password + ":" + salt);
		
		String secret = hash + ":" + salt;
		
//		Log.i("panta auth", secret);
		
		return secret;
		
	}
	
	/**
	 * Returns de MD5 hash of any string in a String form
	 * @param md5 clear text
	 * @return md5 hash
	 */
	public static String md5(String md5) {
		   try {
		       	MessageDigest md = MessageDigest.getInstance("MD5");
		        byte[] array = md.digest(md5.getBytes());
		        StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < array.length; ++i) {
		          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
		       }
		        return sb.toString();
		    } catch (java.security.NoSuchAlgorithmException e) {
		    }
		    return null;
		}
	
	/**
	 * Returns the url to pool for user authentication
	 * @param user
	 * @param password in clean text, will be obscured in userSecret
	 * @return URL to poll for JSON response
	 */
	public static String buildLoginURL(String user, String password)
	{
	
		return API.buildURL(API.AUTHENTICATE, user, createSecret(password), "auth=true");
	}
	

	
}
