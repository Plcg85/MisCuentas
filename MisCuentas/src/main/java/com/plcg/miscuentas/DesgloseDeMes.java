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

public class DesgloseDeMes extends Activity {
	
	private ListView listaDesglose;
	DatoDesglose[] datos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.desglosedemes);
		
		//aqui obtengo  la fecha de hoy
		Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el año
		//int dia = hoy.get(Calendar.DAY_OF_MONTH);
		int mes = hoy.get(Calendar.MONTH);
		int anio = hoy.get(Calendar.YEAR);
		
		double gastosDelDia;
		double ingresosDelDia;
		
		//controles
		listaDesglose = (ListView)findViewById(R.id.lista_Desglose);
		
		//abrir la base de datos
		DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
		dbMC.open();
		
		datos = new DatoDesglose[31];
		for(int i=0;i<=30;i++){
			gastosDelDia = dbMC.devuelveTotalGastosCorrientesHoy(i+1,mes,anio);
			ingresosDelDia = dbMC.devuelveTotalIngresosPuntualesDelDia(i+1, mes, anio);
			datos[i] = new DatoDesglose(String.valueOf(gastosDelDia),String.valueOf(ingresosDelDia),String.valueOf(i+1));
		}//final for
		//cerrar la base de datos
		dbMC.close();
		
		AdaptadorDesglose adaptadorDesglose = new AdaptadorDesglose(DesgloseDeMes.this,datos);
		listaDesglose.setAdapter(adaptadorDesglose);
		
		///Evento lanzado al pulsar un elemento de la lista en la pantalla desglose
		listaDesglose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Sonidos.sonar(DesgloseDeMes.this,"botoncomun");
				
				//en esta variable se guarda la opcion elegida
				int elegido = arg2+1;
				Intent intent = new Intent(DesgloseDeMes.this,DesgloseDeDia.class);
				intent.putExtra("diaelegido", elegido);
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
			Sonidos.sonar(DesgloseDeMes.this,"botoncasa");
			
			Intent intent = new Intent(this,MainActivity.class);
			startActivity(intent);
			return true;
		}//final switch
		return false;
	}//final menuchoice
}//final DesgloseDeMes

