package jus.poc.prodcons.v1;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	int in = 0;
	int out = 0;
	int nbplein = 0;

	Message[] buffer = null;

	public ProdCons(int Taille) {
		buffer = new Message[Taille];
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public synchronized Message get(_Consommateur arg0) throws Exception,
			InterruptedException {
		while (nbplein <= 0) {
			wait();
		}
		Message r = buffer[out];
		out = (out + 1) % taille();
		nbplein--;
		notifyAll();
		return r;
	}

	@Override
	public synchronized void put(_Producteur arg0, Message arg1)
			throws Exception, InterruptedException {
		while (nbplein >= taille()) {
			wait();
		}
		buffer[in] = arg1;
		in = (in + 1) % taille();
		nbplein++;
		notifyAll();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
