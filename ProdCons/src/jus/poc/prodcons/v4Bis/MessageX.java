package jus.poc.prodcons.v4Bis;

import jus.poc.prodcons.Message;

public class MessageX implements Message {
	/*
	 * Attributs
	 */
	private int idProd;
	private String mess;
	private int NbExemplaire;

	MessageX(int idprod, String m, int ex) {
		this.idProd = idprod;
		this.mess = m;
		this.NbExemplaire = ex;
	}

	public int getIdProd() {
		return this.idProd;
	}

	public int getNbEx() {
		return this.NbExemplaire;
	}

	public String getMess() {
		return this.mess;
	}

	@Override
	public String toString() {
		return "Je suis le message de " + idProd + " " + mess + "\n";
	}
}
