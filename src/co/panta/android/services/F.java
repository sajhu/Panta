package co.panta.android.services;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
	
	public static void echo(Context context, String text)
	{
		Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		toast.show();
	}
	
	public static void echo(Context context, String text, int duration)
	{
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
}
