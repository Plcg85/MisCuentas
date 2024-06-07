package com.plcg.miscuentas;

import java.util.Locale;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

public class EstadisticaMensual2 {
	int anio = 0;
	int mes = 0;
	String[] meses={"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre",
			"Octubre","Noviembre","Diciembre"};
	String[] mesesIngles={"January","February","March","April","May","June","July","August","September",
			"October","November","December"};
	
	//constructor
	public EstadisticaMensual2(int anioRecibido,int mesRecibido){
		anio = anioRecibido;
		mes = mesRecibido;
	}//final constructor
	
	public Intent getIntent(Context context){
				
		double cantidadProvisional;
		
		//data 2
		//////////////////////////
		///////////////////////////
		double[] gastos = new double[31];
		
		DBAdapterMisCuentas dbMC1 = new DBAdapterMisCuentas(context);
		dbMC1.open();
		for (int i=0;i<31;i++){
			cantidadProvisional = dbMC1.devuelveTotalGastosCorrientesHoy(i+1,mes-1,anio);
			cantidadProvisional = Math.rint(cantidadProvisional*100)/100;
			gastos[i]=cantidadProvisional;
		}
		dbMC1.close();
		
		String idioma = Locale.getDefault().getDisplayLanguage();
		String category="";
		
		if (idioma.equalsIgnoreCase("English")){
	    	category="Expenses";
		}
		if (idioma.equalsIgnoreCase("Español")){
			category="Gastos";
		}
		CategorySeries seriesGastos = new CategorySeries(category);
		
		for (int i = 0; i< gastos.length; i++){
			seriesGastos.add("Bar " + (i+1), gastos[i]);
		}		
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(seriesGastos.toXYSeries());
		
		//customize bar 2
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		renderer2.setDisplayChartValues(true);
		renderer2.setChartValuesSpacing((float) 1.5);//espacio de los numeros con la barra
		renderer2.setColor(Color.RED);
		
		//customize for the graph
				
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer2);
		
		mRenderer.setChartTitle("Gastos " + meses[mes-1] + "/"+anio);
		mRenderer.setXTitle("Dias");
		mRenderer.setYTitle("Cantidad en €");
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBarSpacing(0);
		mRenderer.setBarWidth(10);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setShowLegend(true);
	
		mRenderer.setXLabels(20);
		mRenderer.setYLabels(20);
		mRenderer.setTextTypeface("sans_serif", Typeface.BOLD);
		mRenderer.setLabelsTextSize(20f);
		mRenderer.setAxisTitleTextSize(20);
		mRenderer.setLegendTextSize(20);
		
		if (idioma.equalsIgnoreCase("English")){
			mRenderer.setChartTitle("Expenses " + mesesIngles[mes-1] + "/"+anio);
			mRenderer.setXTitle("Days");
			mRenderer.setYTitle("Amount €");
		}
		  
		Intent intent = ChartFactory.getBarChartIntent(context, dataset, mRenderer, Type.DEFAULT);
		return intent;
		
	}

}
