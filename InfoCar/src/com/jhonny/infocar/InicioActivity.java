package com.jhonny.infocar;

import java.util.Timer;
import java.util.TimerTask;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
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
					editor.putInt(Constantes.FONDO_PANTALLA, 1);
					editor.putBoolean(Constantes.NOTIFICACIONES, false);
					editor.commit();
				}
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		cargaFondoDePantalla();
	}

	private synchronized void cargaFondoDePantalla() {
		try {
			SharedPreferences prop = getSharedPreferences(Constantes.CONFIGURACION, Context.MODE_PRIVATE);
			int fondoSeleccionado = 1;
			if(prop != null) {
				SharedPreferences.Editor editor = prop.edit();
				if(editor != null) {
					if(prop.contains(Constantes.FONDO_PANTALLA)) {
						fondoSeleccionado = prop.getInt(Constantes.FONDO_PANTALLA, 1);
					}
				}
			}

			String imagen = Constantes.FONDO_1;
			switch(fondoSeleccionado) {
				case 1:
					imagen = Constantes.FONDO_1;
					break;
				case 2:
					imagen = Constantes.FONDO_2;
					break;
				case 3:
					imagen = Constantes.FONDO_3;
					break;
			}
			int imageResource1 = getApplicationContext().getResources().getIdentifier(imagen, "drawable", getApplicationContext().getPackageName());
			Drawable image = getResources().getDrawable(imageResource1);
			ImageView imageView = (ImageView)findViewById(R.id.fondo_principal);
			if(imageView != null)
				imageView.setImageDrawable(image);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
