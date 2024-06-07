package com.plcg.miscuentas;

import android.content.Context;

/*esta clase gestiona los sonidos
	es llamada para saber si los sonidos estan activados y recibe como parametro
	el tipo de sonido que debe reproducir y el contexto*/

public class Sonidos {

	public static void sonar(Context contexto, String tipoSonido) {

		//obtengo el valor de la variable sonido en la bd
		DBAdapterPreferencias dbP = new DBAdapterPreferencias(contexto);
		dbP.open();
		Boolean sonido = dbP.valorSonido();
		dbP.close();
				
		if (sonido==true){
			//se llama a la clase sonidoasincrono pasandole el tipo de sonido a sonar;
			SonidoAsincrono sonido1 = new SonidoAsincrono(contexto,tipoSonido);
			sonido1.execute();
		}//final si hay sonido
	}//final sonar
	
}//final clase sonidos
