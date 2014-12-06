package com.jhonny.infocar;

import java.util.Timer;
import java.util.TimerTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;


public class InicioActivity extends Activity {
	
	private long splashDelay = 6000; //segundos
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.activity_inicio);
		
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Intent mainIntent = new Intent().setClass(InicioActivity.this, PrincipalActivity.class);
				startActivity(mainIntent);
				
				//Destruimos esta activity para prevenir que el usuario retorne aqui presionando el boton Atras.
				finish();
			}
		};
		
		Timer timer = new Timer();
		//Pasado los 6 segundos dispara la tarea
	    timer.schedule(task, splashDelay);
	    
	    // Animacion del texto
	    TextView texto = (TextView)findViewById(R.id.inicio_cargando);
	    Animation transparente = AnimationUtils.loadAnimation(this, R.anim.transparencia);
		transparente.reset();
		transparente.setRepeatCount(99);
		texto.startAnimation(transparente);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.inicio, menu);
		return true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		
		try {
			SharedPreferences prop = getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
			if(prop != null) {
				SharedPreferences.Editor editor = prop.edit();
				
				if(!prop.contains(Constantes.PRIMERA_VEZ)) {
					editor.putBoolean(Constantes.PRIMERA_VEZ, true);
					editor.putBoolean(Constantes.INTRO_PERSONALES, false);
					editor.putBoolean(Constantes.INTRO_VEHICULO, false);
					editor.commit();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
