package jus.poc.prodcons.v4;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	/** Producteur du message */
	private int idProd;
	/** Corps du message */
	private String mess;
	/** Contient le nbExemplaire du message. */
	private int nbExemp;

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
	MessageX(int idprod, String m, int nbE) {// Param du nb exemplaire
		this.idProd = idprod;
		this.mess = m;
		this.nbExemp = nbE;
	}

	/** @return L'id du producteur du message. */
	public int getIdProd() {
		return this.idProd;
	}

	/** @return Le contenu du message */
	public String getMess() {
		return this.mess;
	}

	/** @return Le nombre d'exemplaire restants du message */
	public synchronized int getNbExemp() {
		return this.nbExemp;
	}

	@Override
	public String toString() {
		return "Je suis " + mess + " du producteur " + idProd + " present en "
				+ nbExemp + " exemplaires\n";
	}

	/** Décrémente le nombre d'exemplaires du message */
	public synchronized void consommeEx() {
		nbExemp--;
	}

	/** @return Le booleen correspondant au test nombre exemplaire est nulle. */
	public synchronized boolean nbExempNul() {
		return (nbExemp == 0);
	}
}
