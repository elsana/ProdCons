package jus.poc.prodcons.v1;

import java.util.logging.Logger;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v4.TestProdCons;

public class ProdCons implements Tampon {

	/** Index d'écriture dans le buffer, pour les écritures. */
	int in = 0;
	/** Index de sortie du buffer, pour les récupérations. */
	int out = 0;
	/** Nombre de message actuellement dans le buffer */
	int nbplein = 0;

	/** Le buffer contenant les messages. */
	Message[] buffer = null;

	/** Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	/**
	 * @param Taille
	 *            Taille du buffer pour stocker les messages.
	 */
	public ProdCons(int Taille) {
		buffer = new Message[Taille];
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
	public synchronized Message get(_Consommateur conso) throws Exception,
			InterruptedException {
		while (nbplein <= 0) {
			wait();
		}
		Message r = buffer[out];
		LOGGER.info("CONSO " + conso.identification() + " : " + r.toString());

		out = (out + 1) % taille();
		nbplein--;
		notifyAll();
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
	public synchronized void put(_Producteur prod, Message mess)
			throws Exception, InterruptedException {
		while (nbplein >= taille()) {
			wait();
		}
		buffer[in] = mess;
		in = (in + 1) % taille();
		nbplein++;
		LOGGER.info("PROD : " + mess.toString());

		notifyAll();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
