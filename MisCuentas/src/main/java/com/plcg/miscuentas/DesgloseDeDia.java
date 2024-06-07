package com.plcg.miscuentas;

import java.util.Calendar;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class DesgloseDeDia extends Activity{
	
	private ListView lstGastosPuntuales;
	private ListView lstIngresosPuntuales;
	private ElementoGastoCompuesto[] datosGastosCorrientes;
	private ElementoGastoCompuesto[] datosIngresosPuntuales;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.desglosededia);
		
		int diaElegido = getIntent().getIntExtra("diaelegido",0);
		
		//aqui obtengo  la fecha de hoy
		Calendar hoy = Calendar.getInstance();//primero obtengo el mes en el que estamos y el añ
		int mesElegido = hoy.get(Calendar.MONTH)+1;
		int anioElegido = hoy.get(Calendar.YEAR);
		
		//referenciar los controles
		lstGastosPuntuales = (ListView)findViewById(R.id.listGastosPuntualesDesgloseDia);
		lstIngresosPuntuales = (ListView)findViewById(R.id.listIngresosPuntualesDesgloseDia);
		
		/////gastos////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////
		//solicito el array de elementoGastoCompuesto;
		
		DBAdapterMisCuentas dbGC = new DBAdapterMisCuentas(this);
		dbGC.open();
		datosGastosCorrientes = dbGC.devuelveArrayElementoGastoCompuestoDeUnDia(diaElegido,mesElegido,anioElegido);
		dbGC.close();
			
		//relleno el listViewGastosPuntuales
		Adaptador adaptador = new Adaptador(this,datosGastosCorrientes,"gastos");
		lstGastosPuntuales.setAdapter(adaptador);
		
		//////ingresos//////////////////////////////////////////////////////////
		////////////////////////////////////////////////////////////////////////
		//solicito el array de elementoGastoCompuesto para ingresos puntuales
		DBAdapterMisCuentas dbIP = new DBAdapterMisCuentas(this);
		dbIP.open();
		datosIngresosPuntuales = dbIP.devuelveArrayElementoGastoCompuestoIngresosPuntualesDeUnDia(diaElegido,mesElegido,anioElegido);
		dbIP.close();
			
		//relleno el listViewIngresosPuntuales
		Adaptador adaptador1 = new Adaptador(this,datosIngresosPuntuales,"ingresos");
		lstIngresosPuntuales.setAdapter(adaptador1);
		///////////////////////////////////////////////////////////////////////////////////
		///////////////////////////////////////////////////////////////////////////////////
	}//onCreate final
}//final DesgloseDeDia
