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

	public synchronized int getNbExemp() {
		return this.nbExemp;
	}

	@Override
	public String toString() {
		return "Je suis " + mess + " du producteur " + idProd + " present en "
				+ nbExemp + " exemplaires\n";
	}

	public synchronized void consommeEx() {
		nbExemp--;
	}

	// Verifie que nbExemp est nulle
	public synchronized boolean nbExempNul() {
		return (nbExemp == 0);
	}
}
