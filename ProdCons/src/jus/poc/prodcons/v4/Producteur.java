package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	private int nbMess = 0;
	// private Aleatoire nbExemp;
	private ProdCons pc;

	protected Producteur(int type, Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			int nombreMoyenNbExemplaire, int deviationNombreMoyenNbExemplaire,
			ProdCons pc) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement,
				deviationTempsDeTraitement);

		/*
		 * this.nbMess = Aleatoire.valeur(moyenneNbMessages,
		 * deviationNbMessages); this.nbExemp = new Aleatoire(nbExemplaire,
		 * deviationNbExemplaire);
		 */

		// Creation d'un message
		this.nbMess = new Aleatoire(nombreMoyenNbExemplaire,
				deviationNombreMoyenNbExemplaire).next();
		this.pc = pc;
	}

	@Override
	public void run() {
		int messNum = 1;
		int tAlea;
		while (nbMess > 0) {
			tAlea = Aleatoire.valeur(moyenneTempsDeTraitement(),
					deviationTempsDeTraitement());
			try {
				Thread.sleep(tAlea);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String contenu = "le message numéro " + messNum;
			Message m = new MessageX(super.identification(), contenu, nbMess);

			try { // Message déposé dans le tampon
				this.pc.put(this, m);
				observateur.productionMessage(this, m, tAlea);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.nbMess--;
			messNum++;
		}
	}

	@Override
	public int nombreDeMessages() {
		return this.nbMess;
	}
}
