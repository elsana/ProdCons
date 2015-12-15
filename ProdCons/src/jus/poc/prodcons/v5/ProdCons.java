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

	/* Index d'écriture dans le buffer, pour les écritures. */
	int in = 0;
	/* Index de sortie du buffer, pour les récupérations. */
	int out = 0;
	/* Nombre de message actuellement dans le buffer */
	int nbplein = 0;

	/* Le buffer contenant les messages. */
	Message[] buffer = null;

	private final Lock myLock = new ReentrantLock();

	private final Condition bFull = myLock.newCondition();
	private final Condition bEmpty = myLock.newCondition();

	Observateur observateur = null;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	/*
	 * @param Taille Taille du buffer pour stocker les messages.
	 * 
	 * @param obs L'observateur de la classe
	 */
	public ProdCons(int Taille, Observateur obs) {
		buffer = new Message[Taille];
		this.observateur = obs;
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	/*
	 * @param conso Le consommateur récupérant le message.
	 * 
	 * @return Le message récupéré.
	 */
	@Override
	public Message get(_Consommateur conso) throws Exception,
			InterruptedException {
		this.myLock.lock();
		try {
			Message r; // r ne peut etre déclaré dans le bloc synchronisé et
						// retourné à la fin, on le déclare donc avant
			while (nbplein <= 0) {
				this.bEmpty.await();
			}

			r = buffer[out];
			LOGGER.info("CONSO " + conso.identification() + " : "
					+ r.toString());

			out = (out + 1) % taille();
			nbplein--;
			observateur.retraitMessage(conso, r);

			this.bFull.signal();
			return r;
		} finally {
			this.myLock.unlock();
		}
	}

	/*
	 * @param prod Le producteur déposant le message.
	 * 
	 * @param messs Le message déposé.
	 */
	@Override
	public void put(_Producteur prod, Message mess) throws Exception,
			InterruptedException {
		this.myLock.lock();
		try {
			while (nbplein >= taille()) {
				this.bFull.await();
			}
			buffer[in] = mess;
			in = (in + 1) % taille();
			nbplein++;
			observateur.depotMessage(prod, mess);
			LOGGER.info("PROD : " + mess.toString());

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
