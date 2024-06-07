package com.plcg.miscuentas;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private int diaSeleccionado,mesSeleccionado,anioSeleccionado;
	private String fechaSeleccionada;
	private Button btnAnterior,btnSiguiente;
	private TextView txtFecha;
	
	private Button btnEstadisticas,btnGastoCorriente,btnIngresoPuntual,btnIngresoFijo,btnGastoFijo,btnEliminarFijos,btnEliminarPuntuales;
	private TextView textQueda,textGastoDeHoy;
	private ListView listGastosHoy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		String idioma = Locale.getDefault().getDisplayLanguage();
		
		//new CopiarBaseDatosMisCuentas(this);
		
		//inicializar preferencias
		iniciarPreferencias();
		
		//obtener controles
		textGastoDeHoy = (TextView)findViewById(R.id.textGastoDeHoy);//gasto de hoy
		textQueda = (TextView)findViewById(R.id.textDineroRestante);//dinero restante
		btnGastoCorriente = (Button)findViewById(R.id.btnGastoCorriente);
		btnIngresoPuntual = (Button)findViewById(R.id.btnIngresoPuntual);
		btnIngresoFijo = (Button)findViewById(R.id.btnIngresoFijo);
		btnGastoFijo = (Button)findViewById(R.id.btnGastoFijo);
		listGastosHoy = (ListView)findViewById(R.id.listGastosHoy);
		btnEliminarFijos = (Button)findViewById(R.id.btnEliminarFijos);
		btnEliminarPuntuales = (Button)findViewById(R.id.btnEliminarPuntuales);
		btnEstadisticas = (Button)findViewById(R.id.btnEstadisticas);
		
		btnAnterior = (Button)findViewById(R.id.btnAnterior);
		btnSiguiente = (Button)findViewById(R.id.btnSiguiente);
		txtFecha = (TextView)findViewById(R.id.textFecha);
		
		
		///AHORA VOY A CALCULAR EL DINERO RESTANTE PARA ESTE MES
		///LA FORMULA ES: (INGRESOS FIJOS + INGRESOS PUNTUALES) - 
		///((GASTOS FIJOS/DIAS QUE VAN DE MES) + GASTOS CORRIENTES)
		double ingresosFijos=0;
		double ingresosPuntuales=0;
		double gastosFijos=0;
		double gastosCorrientes=0;
		double dineroQueQueda=0;
		
		//aqui obtengo  la fecha de hoy
		Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el año
		int dia = hoy.get(Calendar.DAY_OF_MONTH);
		int mes = hoy.get(Calendar.MONTH);
		int anio = hoy.get(Calendar.YEAR);
		
		//aqui comprueba si los ingresos fijos estan añadidos en la tabla ingresos puntuales el dia 1
		//para que la cuenta de dinero restante sea ingresos puntuales - gastos puntuales
		DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(this);
		dbMC.open();
		String[] conceptosIngresosFijos = dbMC.devuelveArrayDeIngresosFijos();
		
		///////////////////////////////////
		///////////////////////////////////
		boolean hayCoincidenciasEnIngresos=false;
		for(int i=0;i<conceptosIngresosFijos.length;i++){
			boolean estaEnIngresos = dbMC.compruebaSiEstaEnIngresosPuntuales(conceptosIngresosFijos[i],1,mes+1,anio);
			if (estaEnIngresos==true){
				hayCoincidenciasEnIngresos=true;//quiere decir que hay almenos 1 coincidencia
			}
			if (estaEnIngresos==false){//si este concepto no esta entonces lo busco en gastos fijos y lo a�ado a gastos corrientes el dia 1

				double cuantia =dbMC.devuelveCantidadIngresosFijos(conceptosIngresosFijos[i]);//busco el importe de dicho concepto
		
				dbMC.nuevoIngresoPuntual(cuantia, conceptosIngresosFijos[i], 1, mes+1, anio);
				Toast.makeText(this, "Guardado "+ conceptosIngresosFijos[i]+" "+cuantia, Toast.LENGTH_LONG).show();
			}//final si esta enIngresos puntjuales
		}//final de recorrer los conceptos
		dbMC.close();
		
		//aqui compruebo si los gastos fijos estan añadidos en la base de datos de gastos corrientes
		//el dia 1 y si no estan, los añado
		//pido string[] de conceptos
		DBAdapterMisCuentas dbGF1 = new DBAdapterMisCuentas(this);
		dbGF1.open();
		String[] conceptos = dbGF1.devuelveArrayDeGastosFijos();//en esta variable van todos los conceptos gastos fijos
		dbGF1.close();
				
		DBAdapterMisCuentas dbGC1 = new DBAdapterMisCuentas(this);
		dbGC1.open();
				
		///ya de paso seteo el gasto de hoy
		double gastoDeHoy = dbGC1.devuelveTotalGastosCorrientesHoy(dia,mes,anio);
		gastoDeHoy = Math.rint(gastoDeHoy*100)/100;
		textGastoDeHoy.setText("Gasto De Hoy = "+gastoDeHoy+" €");
		if (idioma.equalsIgnoreCase("English")){
			textGastoDeHoy.setText("Spending today = "+gastoDeHoy+" €");
		}
		//estas variables controlan el dia seleccionado para localizar los gastos de ese dia y luego
		//asi rellenar el listview
		diaSeleccionado = dia;
		mesSeleccionado = mes+1;
		anioSeleccionado = anio;
		
		fechaSeleccionada = ""+diaSeleccionado+"/"+mesSeleccionado+"/"+anioSeleccionado;
		//txtFecha.setText(fechaSeleccionada);
		txtFecha.setText(R.string.hoy);
		///////////////////////////////////
		///////////////////////////////////
		boolean hayCoincidencias=false;
		for(int i=0;i<conceptos.length;i++){
			boolean esta = dbGC1.compruebaSiEsta(conceptos[i],1,mes+1,anio);
			if (esta==true){
				hayCoincidencias=true;//quiere decir que hay almenos 1 coincidencia
			}
			if (esta==false){//si este concepto no esta entonces lo busco en gastos fijos y lo a�ado a gastos corrientes el dia 1
				
				DBAdapterMisCuentas dbGF2 = new DBAdapterMisCuentas(this);
				dbGF2.open();
				double cuantia =dbGF2.devuelveCantidadGastosFijos(conceptos[i]);//busco el importe de dicho concepto
								
				String tipoGasto = dbGF2.devuelveTipoGastoFijo(conceptos[i]);//busco el tipo de gasto de dicho gasto
				dbGF2.close();
				
				//lo guardo en gastos corrientes
				DBAdapterMisCuentas dbGC2 = new DBAdapterMisCuentas(this);
				dbGC2.open();
				dbGC2.nuevoGasto(cuantia, conceptos[i], 1, mes+1, anio, tipoGasto);
				dbGC2.close();
				Toast.makeText(this, "Guardado "+ conceptos[i]+" "+cuantia, Toast.LENGTH_LONG).show();
			}
		}
		dbGC1.close();
		
//		//aqui obtengo el total de ingresos fijos (Mensuales)
//		DBAdapterMisCuentas dbIngresosFijos = new DBAdapterMisCuentas(this);
//		dbIngresosFijos.open();
//		//dbIngresosFijos.nuevoIngresoFijo(1400, "Sueldo");
//		ingresosFijos = dbIngresosFijos.devuelveTotalIngresosFijos();
//		dbIngresosFijos.close();
		
		//aqui obtengo el total de ingresos puntuales
		DBAdapterMisCuentas dbIngresosPuntuales = new DBAdapterMisCuentas(this);
		dbIngresosPuntuales.open();
		ingresosPuntuales=dbIngresosPuntuales.devuelveTotalIngresosPuntualesDelMes(mes, anio);//obtengo los ingresos de ese mes/año
		dbIngresosPuntuales.close();
		
//		//ahora voy a calcular la suma de todos los gastos fijos
//		DBAdapterMisCuentas dbGastosFijos = new DBAdapterMisCuentas(this);
//		dbGastosFijos.open();
//		gastosFijos = dbGastosFijos.devuelveTotalGastosFijos();
//		dbGastosFijos.close();
		
		//ahora voy a calcular la suma de todos los gastos corrientes de este mes
		DBAdapterMisCuentas dbGastosCorrientes = new DBAdapterMisCuentas(this);
		dbGastosCorrientes.open();
		gastosCorrientes = dbGastosCorrientes.devuelveTotalGastosCorrientesMes(mes,anio);
		dbGastosCorrientes.close();
				
		//calculando el dinero que queda...
		dineroQueQueda = (ingresosPuntuales)-(gastosCorrientes);
		dineroQueQueda = Math.rint(dineroQueQueda*100)/100;//redondeo decimales a solo 2 cifras
		textQueda.setText(" "+dineroQueQueda+" €");
		
		if(dineroQueQueda<0){
			textQueda.setTextColor(Color.RED);
		}
//		}else{
//			textQueda.setTextColor(Color.BLACK);
//		}//fin si quedan menos de 0 euros
		
		
		////////////////////////////////////////////////////////////////////////////////////////
		//Rellenar el listView de gastos del dia
		DBAdapterMisCuentas dbGC2 = new DBAdapterMisCuentas(this);
		dbGC2.open();
		DatoGastoHoy[] datos = dbGC2.listaGastoHoy(dia,mes+1,anio);
		dbGC2.close();
		AdaptadorGastosHoy adaptador = new AdaptadorGastosHoy(MainActivity.this,datos);
		listGastosHoy.setAdapter(adaptador);
		
		////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////////////////////
		
		///animaciones/////
		Animation rotar = AnimationUtils.loadAnimation(this, R.anim.animar);
        rotar.reset();
		btnGastoCorriente.startAnimation(rotar);
		btnEstadisticas.startAnimation(rotar);
		btnGastoCorriente.startAnimation(rotar);
		btnIngresoPuntual.startAnimation(rotar);
		btnIngresoFijo.startAnimation(rotar);
		btnGastoFijo.startAnimation(rotar);
		btnEliminarFijos.startAnimation(rotar);
		btnEliminarPuntuales.startAnimation(rotar);
		btnAnterior.startAnimation(rotar);
		btnSiguiente.startAnimation(rotar);
		Animation escalar = AnimationUtils.loadAnimation(this, R.anim.escalar);
		escalar.reset();
		textQueda.startAnimation(escalar);

		////////////////////////////////////////////////////////////////////////////////////////
		
		///////////evento del textview dinero restante
		textQueda.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,DesgloseDeMes.class);
				startActivity(intent);
			}//final onClick
		});
		
		//evento del boton gasto Corriente
		btnGastoCorriente.setOnClickListener(new View.OnClickListener() {
									
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,NuevoGastoCorriente.class);
				startActivity(intent);
				//startActivity(new Intent("com.example.NuevoGastoCorriente"));
			}//finalonClick
		});//final clase interna
		
		//evento del boton ingreso Puntual
		btnIngresoPuntual.setOnClickListener(new View.OnClickListener() {
											
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,NuevoIngresoPuntual.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
		
		//evento del boton ingreso Fijo
		btnIngresoFijo.setOnClickListener(new View.OnClickListener() {
													
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,NuevoIngresoFijo.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
		
		//evento del boton gasto Fijo
		btnGastoFijo.setOnClickListener(new View.OnClickListener() {
															
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,NuevoGastoFijo.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
		
		//evento del boton Eliminar
		btnEliminarFijos.setOnClickListener(new View.OnClickListener() {
																	
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,EliminarFijos.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
		
		//evento boton eliminar gastos puntuales
		btnEliminarPuntuales.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				Intent intent = new Intent(MainActivity.this,EliminarPuntuales.class);
				startActivity(intent);
			}//finalonClick
		});//final clase interna
		
		//evento boton estadisticas
		btnEstadisticas.setOnClickListener(new View.OnClickListener() {
					
			@Override
			public void onClick(View v) {
				Sonidos.sonar(MainActivity.this,"botoncomun");
				
				DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(MainActivity.this);
				dbMC.open();
				int contador = dbMC.contadorDeRegistros();
				dbMC.close();
				if (contador>0){
					//Intent intent = new Intent(MainActivity.this,GraficaDelMes.class);
					Intent intent = new Intent(MainActivity.this,EstadisticaOpciones.class); 
					startActivity(intent);
				}else{
					Toast.makeText(MainActivity.this, "No hay gastos todavia",Toast.LENGTH_LONG).show();
				}
				
			}//finalonClick
		});//final clase interna
		
		//evento del boton Anterior
		btnAnterior.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Sonidos.sonar(MainActivity.this,"botonatras");
				
				diaSeleccionado = diaSeleccionado-1;
				
				if (diaSeleccionado == 0){
					diaSeleccionado = 31;
					mesSeleccionado = mesSeleccionado-1;
				}
				if (mesSeleccionado==0){
					mesSeleccionado = 12;
					anioSeleccionado = anioSeleccionado-1;
				}
				fechaSeleccionada = ""+diaSeleccionado+"/"+mesSeleccionado+"/"+anioSeleccionado;
				txtFecha.setText(fechaSeleccionada);
				
				//aqui obtengo  la fecha de hoy
				Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el año
				int dia = hoy.get(Calendar.DAY_OF_MONTH);
				int mes = hoy.get(Calendar.MONTH);
				int anio = hoy.get(Calendar.YEAR);
				if(diaSeleccionado==dia && (mesSeleccionado==mes+1) && anioSeleccionado==anio){
					txtFecha.setText(R.string.hoy);
					
				}
				//Poner el gasto del dia
				DBAdapterMisCuentas dbGC1 = new DBAdapterMisCuentas(MainActivity.this);
				dbGC1.open();
				double gastoDeHoy = dbGC1.devuelveTotalGastosCorrientesHoy(diaSeleccionado,mesSeleccionado-1,anioSeleccionado);
				gastoDeHoy = Math.rint(gastoDeHoy*100)/100;
				textGastoDeHoy.setText("Gasto Del Dia = "+gastoDeHoy+" �");
				dbGC1.close();
				
				//Rellenar el listView de gastos del dia
				DBAdapterMisCuentas dbGC2 = new DBAdapterMisCuentas(MainActivity.this);
				dbGC2.open();
				DatoGastoHoy[] datos = dbGC2.listaGastoHoy(diaSeleccionado,mesSeleccionado,anioSeleccionado);
				dbGC2.close();
				AdaptadorGastosHoy adaptador = new AdaptadorGastosHoy(MainActivity.this,datos);
				listGastosHoy.setAdapter(adaptador);
			}//finalonClick
		});//final clase interna
		
		//evento boton siguiente
		btnSiguiente.setOnClickListener(new View.OnClickListener() {
							
			@Override
			public void onClick(View v) {
				
				Sonidos.sonar(MainActivity.this,"botonadelante");
				
				diaSeleccionado = diaSeleccionado+1;
						
				if (diaSeleccionado == 32){
					diaSeleccionado = 1;
					mesSeleccionado = mesSeleccionado+1;
				}
				if (mesSeleccionado == 13){
					mesSeleccionado = 1;
					anioSeleccionado = anioSeleccionado+1;
				}
				fechaSeleccionada = ""+diaSeleccionado+"/"+mesSeleccionado+"/"+anioSeleccionado;
				txtFecha.setText(fechaSeleccionada);
				
				//aqui obtengo  la fecha de hoy
				Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el año
				int dia = hoy.get(Calendar.DAY_OF_MONTH);
				int mes = hoy.get(Calendar.MONTH);
				int anio = hoy.get(Calendar.YEAR);
				if(diaSeleccionado==dia && (mesSeleccionado==mes+1) && anioSeleccionado==anio){
					txtFecha.setText(R.string.hoy);
				}
				
				//Poner el gasto del dia
				DBAdapterMisCuentas dbGC1 = new DBAdapterMisCuentas(MainActivity.this);
				dbGC1.open();
				double gastodia = dbGC1.devuelveTotalGastosCorrientesHoy(diaSeleccionado,mesSeleccionado-1,anioSeleccionado);
				gastodia = Math.rint(gastodia*100)/100;
				textGastoDeHoy.setText("Gasto Del Dia = "+gastodia+" �");
				dbGC1.close();
				
				//Rellenar el listView de gastos del dia
				DBAdapterMisCuentas dbGC2 = new DBAdapterMisCuentas(MainActivity.this);
				dbGC2.open();
				DatoGastoHoy[] datos = dbGC2.listaGastoHoy(diaSeleccionado,mesSeleccionado,anioSeleccionado);
				dbGC2.close();
				AdaptadorGastosHoy adaptador = new AdaptadorGastosHoy(MainActivity.this,datos);
				listGastosHoy.setAdapter(adaptador);
			}//finalonClick
		});//final clase interna
		
		
	}//final oncreate
	
	private void iniciarPreferencias() {
		
		DBAdapterPreferencias dbP = new DBAdapterPreferencias(this);
		dbP.open();
		
		int numPref = dbP.devuelveNumeroPreferencias();

		//si el numero de preferencias es 0 se inician las preferencias
		if (numPref == 0){
			dbP.inicializarPreferencias();
		}//final si no hay preferencias
		
		dbP.close();
	}//final iniciarPreferencias

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
		MenuItem mnu1 = menu.add(0,0,0,"Configuracion");
		{
			mnu1.setIcon(R.drawable.configuracion);
			mnu1.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		}
	}//final createMenu
	
	private boolean MenuChoice(MenuItem item){
		switch(item.getItemId()){
		case 0 :
			Sonidos.sonar(MainActivity.this,"botoncomun");
			
			Intent i = new Intent("com.plcg.miscuentas.Preferencias");
			startActivity(i);
			return true;
		}//final switch
		return false;
	}//final menuchoice
}//final clase mainActivity
