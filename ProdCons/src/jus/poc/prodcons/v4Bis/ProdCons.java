package jus.poc.prodcons.v4Bis;

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

	private Semaphore[] tProdAtt = null;

	public ProdCons(int Taille, Observateur obs, int nbProd) {
		buffer = new Message[Taille];
		this.sProd = new Semaphore(Taille);
		this.sCons = new Semaphore(0);
		this.observateur = obs;

		this.tProdAtt = new Semaphore[nbProd];
		for (int i = 0; i < tProdAtt.length; i++) {
			tProdAtt[i] = new Semaphore(0);
		}
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public Message get(_Consommateur conso) throws Exception,
			InterruptedException {
		this.sCons.attendre();
		MessageX r; // r ne peut etre déclaré dans le bloc synchronisé et
					// retourné à la fin, on le déclare donc avant
		synchronized (this) {
			r = (MessageX) buffer[out];

			r.decrementerNbEx();

			if (r.getNbEx() == 0) {
				out = (out + 1) % taille();
				nbplein--;

				tProdAtt[r.getIdProd() - 1].reveiller();
				this.sProd.reveiller();
			} else {
				this.sCons.reveiller();
			}
			observateur.retraitMessage(conso, r);
		}
		return r;
	}

	@Override
	public void put(_Producteur prod, Message mess) throws Exception,
			InterruptedException {
		this.sProd.attendre();

		MessageX myMessage = ((MessageX) mess);

		synchronized (this) {
			buffer[in] = mess;
			in = (in + 1) % taille();
			nbplein++;

			observateur.depotMessage(prod, mess);
		}
		this.sCons.reveiller();
		this.tProdAtt[prod.identification() - 1].attendre();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
