package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	/*
	 * Attributs
	 */
	private int idProd;
	private String mess;
	private int nbExemp;

	MessageX(int idprod, String m, int nbE) {// Param du nb exemplaire
		this.idProd = idprod;
		this.mess = m;
		this.nbExemp = nbE;
	}

	public int getIdProd() {
		return this.idProd;
	}

	public String getMess() {
		return this.mess;
	}

	@Override
	public String toString() {
		return "Je suis le message de " + idProd + " " + mess + "\n";
	}

	public void consommeEx() {
		nbExemp--;
	}

	// Verifie que nbExemp est nulle
	public boolean nbExempNul() {
		return (nbExemp == 0);
	}
}
