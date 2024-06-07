package com.plcg.miscuentas;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;

public class SonidoAsincrono extends AsyncTask<Void,Void,Boolean>{

	MediaPlayer sonido;
	Context contexto;
	String tipoSonido;
	boolean suenaSonido=false;	
	
	//constructor
	public SonidoAsincrono(Context contextoRecibido,String sonidoRecibido){
		tipoSonido = sonidoRecibido;
		contexto = contextoRecibido;
	}//final contructor
	
	@Override
	protected Boolean doInBackground(Void... params) {
		if (tipoSonido == "botoncomun"){
			sonido = MediaPlayer.create(contexto, R.raw.botoncomun);
			suenaSonido=true;
		}
		if (tipoSonido == "botonadelante"){
			sonido = MediaPlayer.create(contexto, R.raw.botonadelante);
			suenaSonido=true;
		}
		if (tipoSonido == "botonatras"){
			sonido = MediaPlayer.create(contexto, R.raw.botonatras);
			suenaSonido=true;
		}
		if (tipoSonido == "botoncasa"){
			sonido = MediaPlayer.create(contexto, R.raw.botoncasa);
			suenaSonido=true;
		}
		if (tipoSonido == "botonguardar"){
			sonido = MediaPlayer.create(contexto, R.raw.botonguardar);
			suenaSonido=true;
		}
		if (suenaSonido ==true){
			sonido.start();
		}
		return true;
	}
			
	@Override
	protected void onProgressUpdate(Void... values) {
		//int progreso = values[0].intValue();
				
		//pbarProgreso.setProgress(progreso);
	}
			
	@Override
	protected void onPreExecute() {
		//pbarProgreso.setMax(100);
		//pbarProgreso.setProgress(0);
	}
			
	@Override
	protected void onPostExecute(Boolean result) {
		//if(result)
		//Toast.makeText(MainActivity.this, "Tarea finalizada!", Toast.LENGTH_SHORT).show();
	}
			
	@Override
	protected void onCancelled() {
		//Toast.makeText(MainActivity.this, "Tarea cancelada!", Toast.LENGTH_SHORT).show();
	}

}//final clase sonidoAsincrono
