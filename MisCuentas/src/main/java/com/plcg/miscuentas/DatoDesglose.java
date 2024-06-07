package com.plcg.miscuentas;

public class DatoDesglose {

	private String gastos;
	private String ingresos;
	private String dia;
	
	//constructor
	public DatoDesglose(String gast,String ingres,String di){
		gastos = gast;
		ingresos = ingres;
		dia = di;
	}//fin constructor
	public String getGastos() {
		return gastos;
	}
	public String getIngresos() {
		return ingresos;
	}
	public String getDia(){
		return dia;
	}
	
}//final datoDesglose
