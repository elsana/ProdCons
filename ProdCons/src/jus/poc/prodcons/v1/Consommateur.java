package jus.poc.prodcons.v1;

import java.util.logging.Logger;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	private int nbMessConso = 0;
	private ProdCons pc;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	protected Consommateur(Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons pc) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
		this.pc = pc;
	}

	@Override
	public void run() {
		int tAlea;
		Message m;
		while (true) {
			try {
				m = this.pc.get(this);
				this.nbMessConso++;
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
		}
	}

	@Override
	public int nombreDeMessages() {
		return this.nbMessConso;
	}

}
