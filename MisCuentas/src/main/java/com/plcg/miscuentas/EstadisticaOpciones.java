package com.plcg.miscuentas;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class EstadisticaOpciones extends Activity{
	
	ListView listaAnios;
	ListView listaMeses;
	String[] anios;
	DatoAnioMesGasto[] datosMeses;//aqui van los datos de todos los meses que rellenan el listview
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.estadisticaopciones);
		
		//obtengo controles 
		listaAnios = (ListView)findViewById(R.id.listOpcionesAnios);
		listaMeses = (ListView)findViewById(R.id.listOpcionesMeses);
		
		//rellenar el listview de anios
		//////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////
		//obtengo un array con los a�os disponibles
		DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
		dbMC.open();
		anios = dbMC.devuelveListaAnios();
		
		dbMC.close();
		//adaptador de los meses
		AdaptadorAnios adaptador = new AdaptadorAnios(EstadisticaOpciones.this,anios);
		listaAnios.setAdapter(adaptador);
		
		//rellenar el listview de meses/////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////
		
		//primero hay que encontrar el a�o menor del array de anios
		int menor = Integer.parseInt(anios[0]);
		
		for(int i = 0; i<anios.length; i++){
			if(Integer.parseInt(anios[i])<menor){
				menor = Integer.parseInt(anios[i]);
			}//final if
		}//final bucle
		
		//ahora la variable menor contiene el primer a�o 
		//ahora hay que recorrer desde ese momento hasta la actualidad los mesess
		//y meterlos en el array meses
		Calendar hoy = Calendar.getInstance();//primero obtengo el anio en el que estamos
		int esteAnio = hoy.get(Calendar.YEAR);
		
		//la longitud del array que contiene los datos de los meses tiene que ser 12 meses
		//multiplicado por el numero de a�os que contienen datos
		datosMeses = new DatoAnioMesGasto[anios.length*12];
		
		int contadorIteraciones = 0;
		double gasto = 0;
		double ingreso = 0;
		
		//hay que abrir la base de datos para solicitar el gasto de cada mes
		DBAdapterMisCuentas dbMC1 = new DBAdapterMisCuentas(this);
		dbMC1.open();
		
		for (int anio = menor; anio<=esteAnio; anio++){
			
			for(int mes =1; mes<=12; mes++){
				//Toast.makeText(this, "a�o"+ i + " mes "+ j, Toast.LENGTH_LONG).show();
				//System.out.println("a�o"+ anio + " mes "+ mes);
				gasto = dbMC1.devuelveTotalGastosCorrientesMes(mes-1, anio);
				ingreso = dbMC1.devuelveTotalIngresosPuntualesDelMes(mes-1, anio);
				
				datosMeses[contadorIteraciones] = new DatoAnioMesGasto(String.valueOf(anio),
						String.valueOf(mes),String.valueOf(gasto),String.valueOf(ingreso));
				
				contadorIteraciones = contadorIteraciones + 1;
				
			}
		}//final bucle que recorre los a�os
		dbMC1.close();
		
		///invertir el array para que los ultimos meses salgan los primeros
		DatoAnioMesGasto[] aux = new DatoAnioMesGasto[datosMeses.length];
		for (int i=0; i<datosMeses.length/2;i++){
			aux[i] = datosMeses[i];
			datosMeses[i] = datosMeses[datosMeses.length -1 -i];
			datosMeses[datosMeses.length -1 -i] = aux[i];
		}
		//adaptador de los meses
		AdaptadorMeses adaptadorM = new AdaptadorMeses(EstadisticaOpciones.this,datosMeses);
		listaMeses.setAdapter(adaptadorM);
		/////////////////EVENTOS//////////////////////////EVENTOS///////////////////
		////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////////		
		///Evento lanzado al pulsar un elemento de la lista en la pantalla desglose
		listaAnios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Sonidos.sonar(EstadisticaOpciones.this,"botoncomun");
				
				//en esta variable se guarda la opcion elegida
				int elegido = Integer.parseInt(anios[arg2]);
				
				Intent intent = new Intent(EstadisticaOpciones.this,EstadisticaAnual.class);
				intent.putExtra("anioelegido", elegido);
				startActivityForResult(intent,1);
			}//fin onItemClick
											
		});//fin clase interna
		
		///Evento lanzado al pulsar un elemento de la lista en la pantalla desglose
		listaMeses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Sonidos.sonar(EstadisticaOpciones.this,"botoncomun");
				
				int anioElegido = Integer.parseInt(datosMeses[arg2].getAnio().toString());
				int mesElegido = Integer.parseInt(datosMeses[arg2].getMes().toString());
									
				Intent intent = new Intent(EstadisticaOpciones.this,EstadisticaMensual.class);
				intent.putExtra("anioelegido", anioElegido);
				intent.putExtra("meselegido",mesElegido);
				startActivityForResult(intent,1);
			}//fin onItemClick
													
		});//fin clase interna 
	}//final onCreate
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		super.onCreateOptionsMenu(menu);
		CreateMenu(menu);
		return true;
	}//final onCreateOptionsMenu
	
	public boolean onOptionsItemSelected(MenuItem item){
		return MenuChoice(item);	
	}//final onOptionsItemSelected

	private void CreateMenu(Menu menu){
		MenuItem mnu1 = menu.add(0,0,0,"Home");
		{
			mnu1.setIcon(R.drawable.home);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}//final createMenu
	
	private boolean MenuChoice(MenuItem item){
		switch(item.getItemId()){
		case 0 :
			Sonidos.sonar(EstadisticaOpciones.this,"botoncasa");
			
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}//final switch
		return false;
	}//final menuchoice
}//final clase EstadisticaOpciones
