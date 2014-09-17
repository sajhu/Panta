package co.panta.android;

import org.json.JSONException;
import org.json.JSONObject;

import co.panta.android.model.Panta;
import co.panta.android.services.API;
import co.panta.android.services.F;
import co.panta.android.services.Parameters;
import co.panta.android.services.ServiceHandler;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.view.MenuItem;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class Registro extends Activity {


	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserRegisterTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mNombre;
	private String mTelefono;
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mNombreView;
	private EditText mEmailView;
	private EditText mTelefonoView;
	private EditText mPasswordView;
	
	
	private View mRegisterFormView;
	private View mRegisterStatusView;
	private TextView mRegisterStatusMessageView;

	private Panta panta;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_registro);
		setupActionBar();
		Intent intent = getIntent();


		// Set up the login form.
		

		mEmailView = (EditText) findViewById(R.id.campo_registro_email);
		mNombreView = (EditText) findViewById(R.id.campo_registro_nombre);
		mTelefonoView = (EditText) findViewById(R.id.campo_registro_telefono);
		//mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.campo_registro_clave);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptRegister();
							return true;
						}
						return false;
					}
				});

		mRegisterFormView = findViewById(R.id.register_form);
		mRegisterStatusView = findViewById(R.id.register_status);
		mRegisterStatusMessageView = (TextView) findViewById(R.id.register_status_message);

		findViewById(R.id.sign_up_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptRegister();
					}
				});
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(false);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			// 
			// that hierarchy.
			//NavUtils.navigateUpFromSameTask(this);
			return false;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
//		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mTelefonoView.setError(null);
		mNombreView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mNombre = mNombreView.getText().toString();
		mTelefono = mTelefonoView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (TextUtils.isEmpty(mEmail)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (TextUtils.isEmpty(mNombre)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mNombreView;
			cancel = true;
		} else if (TextUtils.isEmpty(mTelefono)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mTelefonoView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.

//		else if (!mEmail.contains("@")) {
//			mEmailView.setError(getString(R.string.error_invalid_email));
//			focusView = mEmailView;
//			cancel = true;
//		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mRegisterStatusMessageView.setText(R.string.register_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserRegisterTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			
			
		try{
			
			Parameters<String> tags = new Parameters<String>();
			
			
			tags.add(API.TAG_DRIVER_NAME, mNombre);
			tags.add(API.TAG_DRIVER_SURNAME, "Fuentes");
			tags.add(API.TAG_DRIVER_PHONE, mTelefono);
			tags.add(API.TAG_DRIVER_PASSWORD, mPassword);
			tags.add(API.USER_ID_PARAM, mEmail);
			
			String JsonURL = API.buildURL(API.REGISTER, null, null, tags);

			ServiceHandler sh = new ServiceHandler();


			// Making a request to url and getting response
			String jsonStr = sh.makeServiceCall(JsonURL, ServiceHandler.GET);
			
			
			Log.d("Response: ", ">>" + jsonStr);

			if (jsonStr != null) {

				JSONObject jsonObj = new JSONObject(jsonStr);

				// Getting JSON Array node
				int responseCode = jsonObj.getInt(API.TAG_RESPONSE);

				return Integer.valueOf(responseCode);
//				if(responseCode == API.Error.RESPONSE_OK)
//				{
//					Log.d("panta", "Cuenta registrada");
//					return true;
//				}
//				else {
//				}
//				Log.d("panta", "No se pudo crear " + responseCode);

			} else {
				Log.e("panta", "Couldn't get any data from the url");
			}
			
		} catch (JSONException e) {
			Log.e("panta", e.getMessage());
			return 200;
		}


			// TODO: register the new account here.
			return 200;
		}

		@Override
		protected void onPostExecute( Integer result) {
			mAuthTask = null;
			showProgress(false);
			int response = result.intValue();
			
			if (response == API.Error.RESPONSE_OK) {
				
							
				
				finish();
			} else {
				mPasswordView
						.setError("Oops " + response );
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
