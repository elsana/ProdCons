package jus.poc.prodcons.v2;

import jus.poc.prodcons.Message;
import jus.poc.prodcons.Tampon;
import jus.poc.prodcons._Consommateur;
import jus.poc.prodcons._Producteur;

public class ProdCons implements Tampon {

	int in = 0;
	int out = 0;
	int nbplein = 0;
	int nbvide = taille();

	Message[] buffer = null;

	Semaphore fp = new Semaphore();
	Semaphore fc = new Semaphore();

	public ProdCons(int Taille) {
		System.out.println("marque 2.1");
		buffer = new Message[Taille];
		System.out.println("marque 2.2");
	}

	@Override
	public synchronized int enAttente() {
		return nbplein;
	}

	@Override
	public Message get(_Consommateur arg0) throws Exception,
			InterruptedException {
		while (nbplein == 0) {
			fc.attendre();
		}
		Message r; // r ne peut etre déclaré dans le bloc synchronisé et
					// retourné à la fin, on le déclare donc avant
		nbplein--;
		synchronized (this) {
			r = buffer[out];
			out = (out + 1) % taille();
		}
		nbvide++;
		fp.reveiller();
		return r;
	}

	@Override
	public void put(_Producteur arg0, Message arg1) throws Exception,
			InterruptedException {
		while (nbvide <= 0) {
			fp.attendre();
		}
		nbplein--;
		synchronized (this) {
			buffer[in] = arg1;
			in = (in + 1) % taille();
		}
		fc.reveiller();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
