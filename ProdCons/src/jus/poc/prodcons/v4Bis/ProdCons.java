package jus.poc.prodcons.v4Bis;

import java.util.HashMap;
import java.util.Map;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	int in = 0;
	int out = 0;
	int nbplein = 0;

	Message[] buffer = null;

	Semaphore sProd = null;
	Semaphore sCons = null;

	Observateur observateur = null;

	private Map<Integer, Semaphore> ProdAtt = null;

	private Map<Integer, Integer> ExempRestants = null;

	public ProdCons(int Taille, Observateur obs) {
		buffer = new Message[Taille];
		this.sProd = new Semaphore(Taille);
		this.sCons = new Semaphore(0);
		this.observateur = obs;

		this.ProdAtt = new HashMap<Integer, Semaphore>();
		this.ExempRestants = new HashMap<Integer, Integer>();
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception,
			InterruptedException {
		this.sCons.attendre();
		MessageX r; // r ne peut etre déclaré dans le bloc synchronisé et
					// retourné à la fin, on le déclare donc avant
		synchronized (this) {
			r = (MessageX) buffer[out];

			int exRestants = this.ExempRestants.get(r.getIdProd());
			this.ExempRestants.put(r.getIdProd(), --exRestants);

			if (exRestants == 0) {
				out = (out + 1) % taille();
				nbplein--;

				ProdAtt.get(r.getIdProd()).reveiller();
				this.sProd.reveiller();
			} else {
				this.sCons.reveiller();
			}
			observateur.retraitMessage(arg0, r);
		}
		return r;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,
			InterruptedException {
		this.sProd.attendre();

		MessageX myMessage = ((MessageX) arg1);

		synchronized (this) {
			buffer[in] = arg1;
			in = (in + 1) % taille();
			nbplein++;

			int exRestants = myMessage.getNbEx();
			this.ExempRestants.put(arg0.identification(), exRestants);

			observateur.depotMessage(arg0, arg1);
		}
		this.sCons.reveiller();

		Semaphore mySemaphore = new Semaphore(0);
		this.ProdAtt.put(myMessage.getIdProd(), mySemaphore);
		mySemaphore.attendre();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
