package jus.poc.prodcons.v3;

import java.util.logging.Logger;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;
import jus.poc.prodcons.v1.TestProdCons;

public class Producteur extends Acteur implements _Producteur {

	private int nbMess = 0;
	private ProdCons pc;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	protected Producteur(Observateur observateur, int moyenneTempsDeTraitement,
			int deviationTempsDeTraitement, int nombreMoyenNbExemplaire,
			int deviationNombreMoyenNbExemplaire, ProdCons pc)
			throws ControlException {
		super(Acteur.typeProducteur, observateur, moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
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
			Message m = new MessageX(super.identification(),
					", je suis le message numéro " + messNum);

			try { // Message déposé dans le tampon
				this.pc.put(this, m);
				observateur.productionMessage(this, m, tAlea);
				LOGGER.info(m.toString());
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
