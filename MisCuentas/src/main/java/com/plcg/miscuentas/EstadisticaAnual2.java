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

//esta clase sera la encargada de mostras los graficos de barras 
//sobre ingresos y gastos de el anio elegido
public class EstadisticaAnual2 {
	
	int anio = 0;
	
	//constructor
	public EstadisticaAnual2(int anioRecibido){
		anio = anioRecibido;
	}//final constructor
	
	public Intent getIntent(Context context){
				
		//data 1
		/////////////////////////////////
		/////////////////////////////////
		double[] ingresos = new double[12];
		double cantidadProvisional;
		
		DBAdapterMisCuentas dbMC = new DBAdapterMisCuentas(context);
		dbMC.open();
		for (int i=0;i<ingresos.length;i++){
			cantidadProvisional = dbMC.devuelveTotalIngresosPuntualesDelMes(i, anio);
			//ahora se redondea
			cantidadProvisional = Math.rint(cantidadProvisional*100)/100;
			ingresos[i]= cantidadProvisional;
					
		}
		dbMC.close();
		
		String idioma = Locale.getDefault().getDisplayLanguage();
		String category="";
		
		if (idioma.equalsIgnoreCase("English")){
	    	category="Income";
		}
		if (idioma.equalsIgnoreCase("Espa�ol")){
			category="Ingresos";
		}
		
		CategorySeries seriesIngresos = new CategorySeries(category);
		for (int i = 0; i< ingresos.length; i++){
			seriesIngresos.add("Bar " + (i+1), ingresos[i]);
		}
		
		//data 2
		//////////////////////////
		///////////////////////////
		double[] gastos = new double[12];
		
		DBAdapterMisCuentas dbMC1 = new DBAdapterMisCuentas(context);
		dbMC1.open();
		for (int i=0;i<gastos.length;i++){
			cantidadProvisional = dbMC1.devuelveTotalGastosCorrientesMes(i, anio);
			cantidadProvisional = Math.rint(cantidadProvisional*100)/100;
			gastos[i]=cantidadProvisional;
		}
		dbMC1.close();
		
		
		String category2="";
		
		if (idioma.equalsIgnoreCase("English")){
	    	category2="Expenses";
		}
		if (idioma.equalsIgnoreCase("Espa�ol")){
			category2="Gastos";
		}
		
		CategorySeries seriesGastos = new CategorySeries(category2);
		for (int i = 0; i< gastos.length; i++){
			seriesGastos.add("Bar " + (i+1), gastos[i]);
		}		
		
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		dataset.addSeries(seriesIngresos.toXYSeries());
		dataset.addSeries(seriesGastos.toXYSeries());
		
		//customize bar 1
		XYSeriesRenderer renderer = new XYSeriesRenderer();
		renderer.setDisplayChartValues(true);
		renderer.setChartValuesSpacing((float) 1.5);//espacio de los numeros con la barra
		renderer.setColor(Color.GREEN);
		
		//customize bar 2
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();
		renderer2.setDisplayChartValues(true);
		renderer2.setChartValuesSpacing((float) 1.5);//espacio de los numeros con la barra
		renderer2.setColor(Color.RED);
		
		//customize for the graph
		XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
		mRenderer.addSeriesRenderer(renderer);
		mRenderer.addSeriesRenderer(renderer2);
		
		mRenderer.setChartTitle("Ingresos / Gastos " + anio);
		mRenderer.setXTitle("Meses");
		mRenderer.setYTitle("Cantidad en �");
		mRenderer.setBackgroundColor(Color.BLACK);
		mRenderer.setApplyBackgroundColor(true);
		mRenderer.setBarSpacing(10);
		mRenderer.setBarWidth(15);
		mRenderer.setChartTitleTextSize(20);
		mRenderer.setShowLegend(true);
	
		mRenderer.setXLabels(20);
		mRenderer.setYLabels(20);
		mRenderer.setTextTypeface("sans_serif", Typeface.BOLD);
		mRenderer.setLabelsTextSize(20f);
		mRenderer.setAxisTitleTextSize(20);
		mRenderer.setLegendTextSize(20);
		
	
		if (idioma.equalsIgnoreCase("English")){
			mRenderer.setChartTitle("Income / Expenses " + anio);
			mRenderer.setXTitle("Month");
			mRenderer.setYTitle("Amount �");
		}
		  
		Intent intent = ChartFactory.getBarChartIntent(context, dataset, mRenderer, Type.DEFAULT);
		return intent;
	}
}//final clase
