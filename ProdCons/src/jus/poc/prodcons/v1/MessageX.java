package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;

public class MessageX implements Message {

	/** Producteur du message */
	private int idProd;
	/** Corps du message */
	private String mess;

	/**
	 * @param idprod
	 *            L'id du producteur du message.
	 * 
	 * @param m
	 *            Le contenu du message
	 */
	MessageX(int idprod, String m) {
		this.idProd = idprod;
		this.mess = m;
	}

	/** @return L'id du producteur du message. */
	public int getIdProd() {
		return this.idProd;
	}

	/** @return Le contenu du message */
	public String getMess() {
		return this.mess;
	}

	@Override
	public String toString() {
		return "Je suis " + mess + " du producteur " + idProd + "\n";
	}
}
