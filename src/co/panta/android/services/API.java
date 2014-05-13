package co.panta.android.services;

import java.net.URLEncoder;
import java.util.ArrayList;

import android.util.Log;


public class API {
	
	public static final String BASE_API_URL = "http://dev.santiagorojas.co/panta/api/";
	
	
	/**
	 * P�GINAS DISPONIBLES || SERVICIOS
	 */
	
	public static final String AUTHENTICATE = BASE_API_URL + "auth.php";
	
	public static final String GET_TRIPS = BASE_API_URL + "trips.php";
	
	public static final String PUBLISH = BASE_API_URL + "send.php";
	
	public static final String SUSCRIBE = BASE_API_URL + "favorite.php";
	
	

	/**
	 * CONSTANTES DE PETICIONES
	 */
	
	// GLOBALES
	
	public static final String USER_ID_PARAM = "userId";
	
	public static final String USER_SECRET_PARAM = "userSecret";
	
	// AUTENTICACI�N
	
	public static final String REQUEST_AUTH = "auth=true";
	
	// DESCARGAR LISTA DE VIAJES
	
	public static final String TIME_PARAM = "time";
	
	public static final String HASHTAG_PARAM = "hashtag";
	
	// PUBLICAR UN NUEVO VIAJE

	public static final String DESCRIPTION_PARAM = "description";
	
	public static final String DATE_PARAM = "date";
	
	public static final String DESTINATION_PARAM = "to";

	// PUBLICAR UN VIAJE VALORES
	public static final String DATE_TODAY = "today";
	
	public static final String DATE_TOMORROW = "tomorrow";
	
	public static final int DESTINATION_UNIANDES = 0;
	
	public static final int DESTINATION_HOME = 1;
	
	
	// SERVICIO DE SUSCRIPCI�N
	
	public static final String TYPE_PARAM = "type";
	
	public static final String TYPE_HASHTAG = "hashtag";
	
	public static final String TYPE_USER = "user";
	
	
	
	/**
	 *  CONSTANTES DE RESPUESTAS
	 */
	
	
	public static final String TAG_RESPONSE = "response";
	
	/**
	 * CONSTANTES DE LISTA DE VIAJES
	 */
	
	public static final String TAG_TRIPS = "trips";
	public static final String TAG_ID = "id";
	public static final String TAG_DESCRIPTION = "description";
	public static final String TAG_DATE = "date";
	public static final String TAG_TIME = "time";
	public static final String TAG_SEATS = "seats";
	public static final String TAG_DRIVER = "driver";
	public static final String TAG_DRIVER_ID = "id";
	public static final String TAG_DRIVER_NAME = "name";
	public static final String TAG_DRIVER_SURNAME = "surname";
	public static final String TAG_DRIVER_PICTURE = "picture";
	public static final String TAG_DRIVER_PHONE = "phone";
	
	
	/**
	 * Método constructor de urls de peticiones al API
	 * @param service tipo de servicio a realizar
	 * @param userId correo del usuario
	 * @param userSecret contrase�a con sal haseada seg�n define el API
	 * @param params parametros adicionales a agregar. Puede ser nulo, para agergar uno poner un
	 * 			ArrayList<String> con los parametros formateados de la manera {"key=value"}
	 * @return String  - la url completa para realizar el pedido REST	
	 */
	@SuppressWarnings("deprecation")
	public static String buildURL(String service, String userId, String userSecret, ArrayList<String> params)
	{
		String url =  service + "?";
		
		// Se agregan par�metros obligatorios de autenticaci�n
		url += USER_ID_PARAM + "=" + userId + "&" + USER_SECRET_PARAM + "=" + userSecret;
		
		if(params != null && params.size() > 0)
			for (int i = 0; i < params.size(); i++) {
				String param = (String) params.get(i);

				String key = param.split("=")[0];
				String value = param.split("=")[1];
				
				url += "&" + key + "=" + URLEncoder.encode(value);
			}
	
		Log.i("panta API", url);
		return url;
	}
	
	/**
	 * Método constructor de urls de peticiones al API
	 * Acepta 1 sólo parametro formateado "key=value"
	 * @param service tipo de servicio a realizar
	 * @param userId correo del usuario
	 * @param userSecret contrase�a con sal hasheada según define el API
	 * @param param parametro formateado "key=value"
	 * @return String  - la url completa para realizar el pedido REST	
	 */
	public static String buildURL(String service, String userId, String userSecret, String param)
	{
		ArrayList<String> temp = new ArrayList<String>();
		temp.add(param);
		return buildURL(service, userId, userSecret, temp);
	}
	
	public static String buildURL(String service, String userId, String userSecret)
	{
		ArrayList<String> temp = new ArrayList<String>();

		return buildURL(service, userId, userSecret, temp);
	}

	/**
	 * Clase con la definici�n de los errores
	 * los errores se acceden mediante API.Error.RESPONSE_OK
	 * @author Santiago Rojas
	 *
	 */
	public class Error {
		
		// Respuestas Correctas
		
		public static final int RESPONSE_OK = 0;
		
		public static final int RESPONSE_EMPTY = 1;
		
		// Errores de Protocolo
		
		public static final int AUTH_SUCCESS = 100;
		
		public static final int AUTH_REQUIRED = 101;
		
		public static final int AUTH_FAILED = 102;
		
		public static final int MISSING_PARAMS = 103;
		
		public static final int NO_UTF8 = 104;
		
		public static final int NOT_SUPPORTED = 105;
		
		public static final int SERVER_ERROR = 106;

		// Errores Publicaci�n Viajes
		
		public static final int PROBLEM_PUBLISHING = 200;
		
		public static final int ERROR_VALIDATING = 201;
		
		
	}


	
}
