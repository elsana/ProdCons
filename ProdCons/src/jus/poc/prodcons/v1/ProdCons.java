package jus.poc.prodcons.v1;

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

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	public ProdCons(int Taille) {
		buffer = new Message[Taille];
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

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
