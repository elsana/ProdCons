package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;
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
		}
		this.sCons.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
