package jus.poc.prodcons.v1;

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
		int tAlea;
		Message m = null;
		while (nombreDeMessages() > 0) {
			try {
				m = this.pc.get(this);
				System.out.println("Message consommé par " + identification()
						+ ": " + m);
			} catch (InterruptedException e) {

				e.printStackTrace();
			} catch (Exception e) {

				e.printStackTrace();
			}

			tAlea = new Aleatoire(moyenneTempsDeTraitement,
					deviationTempsDeTraitement).next();
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
