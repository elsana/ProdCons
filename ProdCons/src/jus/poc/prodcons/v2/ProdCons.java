package jus.poc.prodcons.v2;

import java.util.logging.Logger;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	/** Index d'écriture dans le buffer, pour les écritures. */
	int in = 0;
	/** Index de sortie du buffer, pour les récupérations. */
	int out = 0;
	/** Nombre de message actuellement dans le buffer */
	int nbplein = 0;

	/** Le buffer contenant les messages. */
	Message[] buffer = null;

	Semaphore sProd = null;
	Semaphore sCons = null;

	/** Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	/**
	 * @param Taille
	 *            Taille du buffer pour stocker les messages.
	 */
	public ProdCons(int Taille) {
		buffer = new Message[Taille];
		this.sProd = new Semaphore(1);
		this.sCons = new Semaphore(0);
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	/**
	 * @param conso
	 *            Le consommateur récupérant le message.
	 * 
	 * @return Le message récupéré.
	 */
	@Override
	public Message get(_Consommateur conso) throws Exception,
			InterruptedException {
		this.sCons.attendre();
		Message r; // r ne peut etre déclaré dans le bloc synchronisé et
					// retourné à la fin, on le déclare donc avant
		synchronized (this) {
			r = buffer[out];
			LOGGER.info("CONSO " + conso.identification() + " : "
					+ r.toString());

			out = (out + 1) % taille();
			nbplein--;
		}
		this.sProd.reveiller();
		return r;
	}

	/**
	 * @param prod
	 *            Le producteur déposant le message.
	 * 
	 * @param messs
	 *            Le message déposé.
	 */
	@Override
	public void put(_Producteur prod, Message mess) throws Exception,
			InterruptedException {
		this.sProd.attendre();
		synchronized (this) {
			buffer[in] = mess;
			in = (in + 1) % taille();
			nbplein++;
			LOGGER.info("PROD : " + mess.toString());
		}
		this.sCons.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
