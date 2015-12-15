package jus.poc.prodcons.v5;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	/*
	 * Attributs
	 */
	private int idProd;
	private String mess;

	MessageX(int idprod, String m) {
		this.idProd = idprod;
		this.mess = m;
	}

	public int getIdProd() {
		return this.idProd;
	}

	public String getMess() {
		return this.mess;
	}

	@Override
	public String toString() {
		return "Je suis " + mess + " du producteur " + idProd + "\n";
	}
}
