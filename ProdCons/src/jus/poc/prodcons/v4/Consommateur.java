package jus.poc.prodcons.v4;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private int nbMess = 0;
	private ProdCons pc;

	protected Consommateur(int type, Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons pc, int nombreMoyenNbExemplaire,
			int deviationNombreMoyenNbExemplaire) throws ControlException {
		super(type, observateur, moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
		this.nbMess = Aleatoire.valeur(nombreMoyenNbExemplaire,
				deviationNombreMoyenNbExemplaire);
		this.pc = pc;
	}

	@Override
	public void run() {
		int tAlea = new Aleatoire(moyenneTempsDeTraitement,
				deviationTempsDeTraitement).next();
		Message m = null;
		while (nombreDeMessages() > 0) {

			// Msg retiré du tampon
			try {
				m = this.pc.get(this);
				observateur.consommationMessage(this, m, tAlea);
				System.out.println("Message consommé par " + identification()
						+ ": " + m);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

			try {
				Thread.sleep(tAlea);
			} catch (Exception e) {
				e.printStackTrace();
			}

			this.nbMess--;
		}
	}

	@Override
	public int nombreDeMessages() {
		return this.nbMess;
	}

}
