package com.plcg.miscuentas;

public class DatoGastoHoy {
	private String tipoGasto;
	private String concepto;
	private String cantidad;
	
	//constructor
	public DatoGastoHoy(String con,String cant,String tipoG){
		tipoGasto = tipoG;
		concepto = con;
		cantidad = cant;
	}//fin constructor
	
	public String getTipoGasto(){
		return tipoGasto;
	}
	public String getConcepto() {
		return concepto;
	}

	public String getCantidad() {
		return cantidad;
	}
}
