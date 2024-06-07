package com.plcg.miscuentas;

public class ElementoGastoCompuesto {
	private String tipoGastoIngreso;
	private String concepto;
	private String cantidad;
	private String dia;
	private String mes;
	private String anio;
	
	public ElementoGastoCompuesto(String concep,String cant,String di,String me,String ani,String tipo){
		tipoGastoIngreso = tipo;
		concepto = concep;
		cantidad = cant;
		dia = di;
		mes = me;
		anio = ani;
	}//constructor titular
	
	public String getTipoGastoIngreso(){
		return tipoGastoIngreso;
	}

	public String getConcepto() {
		return concepto;
	}

	public String getCantidad() {
		return cantidad;
	}

	public String getDia() {
		return dia;
	}

	public String getMes() {
		return mes;
	}

	public String getAnio() {
		return anio;
	}
}
