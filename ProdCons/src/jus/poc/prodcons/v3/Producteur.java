package jus.poc.prodcons.v3;

import java.util.logging.Logger;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Producteur;

public class Producteur extends Acteur implements _Producteur {

	/** Nombre de message que le producteur doit produire. */
	private int nbMess = 0;
	/** ProdCons utilisé pour le dépot des messages */
	private ProdCons pc;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	/**
	 * @param observateur
	 *            L'observateur de la classe.
	 * 
	 * @param moyenneTempsDeTraitement
	 *            Temps moyen pour produire le message.
	 * 
	 * @param deviationTempsDeTraitement
	 *            Déviation de temps pour produire le message.
	 * 
	 * @param pc
	 *            Le ProdCons utilisé pour poser les messages.
	 */
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

	/**
	 * Execution d'un Thread Producteur. Un producteur va produire NbMessages
	 * dans ProdCons après avoir effectuer un traitement sur ces messages. Le
	 * traitement est simulé par un appel à Thread.sleep() avec une durée
	 * aléatoire générée par moyenneTempsDeTraitement et
	 * deviationTempsDeTraitement.
	 */
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
					"le message numéro " + messNum);

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
