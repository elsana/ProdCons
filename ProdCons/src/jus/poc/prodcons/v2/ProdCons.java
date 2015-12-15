package jus.poc.prodcons.v2;

import java.util.logging.Logger;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v4.TestProdCons;

public class ProdCons implements Tampon {

	int in = 0;
	int out = 0;
	int nbplein = 0;

	Message[] buffer = null;

	Semaphore sProd = null;
	Semaphore sCons = null;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	public ProdCons(int Taille) {
		buffer = new Message[Taille];
		this.sProd = new Semaphore(1);
		this.sCons = new Semaphore(0);
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception,
			InterruptedException {
		this.sCons.attendre();
		Message r; // r ne peut etre déclaré dans le bloc synchronisé et
					// retourné à la fin, on le déclare donc avant
		synchronized (this) {
			r = buffer[out];
			LOGGER.info("CONSO " + arg0.identification() + " : " + r.toString());

			out = (out + 1) % taille();
			nbplein--;
		}
		this.sProd.reveiller();
		return r;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,
			InterruptedException {
		this.sProd.attendre();
		synchronized (this) {
			buffer[in] = arg1;
			in = (in + 1) % taille();
			nbplein++;
			LOGGER.info("PROD : " + arg1.toString());
		}
		this.sCons.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
