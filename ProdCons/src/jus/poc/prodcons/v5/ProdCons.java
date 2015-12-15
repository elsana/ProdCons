package jus.poc.prodcons.v5;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v4.TestProdCons;

public class ProdCons implements Tampon {

	int in = 0;
	int out = 0;
	int nbplein = 0;

	Message[] buffer = null;

	private final Lock myLock = new ReentrantLock();

	private final Condition bFull = myLock.newCondition();
	private final Condition bEmpty = myLock.newCondition();

	Observateur observateur = null;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	public ProdCons(int Taille, Observateur obs) {
		buffer = new Message[Taille];
		this.observateur = obs;
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception,
			InterruptedException {
		this.myLock.lock();
		try {
			Message r; // r ne peut etre déclaré dans le bloc synchronisé et
						// retourné à la fin, on le déclare donc avant
			while (nbplein <= 0) {
				this.bEmpty.await();
			}

			r = buffer[out];
			LOGGER.info("CONSO " + arg0.identification() + " : " + r.toString());

			out = (out + 1) % taille();
			nbplein--;
			observateur.retraitMessage(arg0, r);

			this.bFull.signal();
			return r;
		} finally {
			this.myLock.unlock();
		}
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,
			InterruptedException {
		this.myLock.lock();
		try {
			while (nbplein >= taille()) {
				this.bFull.await();
			}
			buffer[in] = arg1;
			in = (in + 1) % taille();
			nbplein++;
			observateur.depotMessage(arg0, arg1);
			LOGGER.info("PROD : " + arg1.toString());

			this.bEmpty.signal();
		} finally {
			this.myLock.unlock();
		}
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
