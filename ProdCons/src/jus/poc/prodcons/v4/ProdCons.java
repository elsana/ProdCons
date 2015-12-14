package jus.poc.prodcons.v4;

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
	Semaphore sExemp = null;

	Observateur observateur = null;

	public ProdCons(int Taille, Observateur obs) {
		buffer = new Message[Taille];
		this.sProd = new Semaphore(Taille);
		this.sCons = new Semaphore(0);
		this.sExemp = new Semaphore(0);
		this.observateur = obs;
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception,
			InterruptedException {
		this.sCons.attendre();
		MessageX r = (MessageX) buffer[out]; // r ne peut etre déclaré dans le
												// bloc synchronisé et
		// retourné à la fin, on le déclare donc avant
		// enlever un exemplaire
		// Si le nb d'exemplaire du msg =0 on exécute le bloc synchronized +
		// réveil le prod qui a été bloqué
		if (r.nbExempNul()) {
			synchronized (this) {
				out = (out + 1) % taille();
				nbplein--;
				observateur.retraitMessage(arg0, r);
			}
			this.sProd.reveiller(1);
		}
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
			observateur.depotMessage(arg0, arg1);
		}
		this.sCons.reveiller(1);
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
