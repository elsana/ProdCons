package jus.poc.prodcons.v4Bis;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	/** Producteur du message */
	private int idProd;
	/** Corps du message */
	private String mess;
	/** Contient le nbExemplaire du message. */
	private int NbExemplaire;

	/**
	 * @param idprod
	 *            L'id du producteur du message.
	 * 
	 * @param m
	 *            Le contenu du message
	 * 
	 * @param nbE
	 *            Le nombre d'exemplaire du message.
	 */
	MessageX(int idprod, String m, int ex) {
		this.idProd = idprod;
		this.mess = m;
		this.NbExemplaire = ex;
	}

	/** @return L'id du producteur du message. */
	public synchronized int getIdProd() {
		return this.idProd;
	}

	/** @return Le nombre d'exemplaire restants du message */
	public synchronized int getNbEx() {
		return this.NbExemplaire;
	}

	/** Décrémente le nombre d'exemplaires du message */
	public synchronized void decrementerNbEx() {
		this.NbExemplaire--;
	}

	/** @return Le contenu du message */
	public String getMess() {
		return this.mess;
	}

	@Override
	public String toString() {
		return "Je suis le message de " + idProd + " " + mess + "\n";
	}
}
