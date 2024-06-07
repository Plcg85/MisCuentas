package com.plcg.miscuentas;

public class DatoAnioMesGasto {
	private String anio;
	private String mes;
	private String gasto;
	private String ingreso;
	
	//constructor
	public DatoAnioMesGasto(String ani,String me,String gast,String ingres){
		anio = ani;
		mes = me;
		gasto = gast;
		ingreso = ingres;
	}//fin constructor

	public String getAnio() {
		return anio;
	}

	public String getMes() {
		return mes;
	}

	public String getGasto() {
		return gasto;
	}
	
	public String getIngreso() {
		return ingreso;
	}
}//final datoaniomesgasto
