package jus.poc.prodcons.v4;

import java.util.logging.Logger;

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

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

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
	public Message get(_Consommateur conso) throws Exception,
			InterruptedException {

		// this.sCons.attendre();

		// On vérifie qu'un exemplaire soit disponible
		this.sExemp.attendre();

		// On récupère le message dans le buffer
		MessageX r;

		synchronized (this) {
			r = (MessageX) buffer[out];
			LOGGER.info("CONSO " + conso.identification() + " : "
					+ r.toString());

			r.consommeEx(); // enlever un exemplaire

			// Si plus aucun msg on exécute le bloc synchronized +
			// réveil du prod qui a été bloqué
			if (r.nbExempNul()) {
				out = (out + 1) % taille();
				nbplein--;
				observateur.retraitMessage(conso, r);
				LOGGER.info("DESTRUCTION : " + r.getMess() + " du producteur "
						+ r.getIdProd() + "\n");
				this.sProd.reveiller(1);
			} else {
				this.sCons.reveiller(1);
			}
		}
		return r;
	}

	@Override
	public void put(_Producteur prod, Message mess) throws Exception,
			InterruptedException {
		this.sProd.attendre();
		synchronized (this) {
			buffer[in] = mess;
			in = (in + 1) % taille();
			nbplein++;
			observateur.depotMessage(prod, mess);
			LOGGER.info("PROD : " + mess.toString());
		}
		this.sExemp.reveiller(((MessageX) mess).getNbExemp());
		this.sCons.attendre();
	}

	@Override
	public int taille() {
		return buffer.length;
	}

}
