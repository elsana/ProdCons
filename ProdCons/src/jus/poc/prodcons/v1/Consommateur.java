package jus.poc.prodcons.v1;

import java.util.logging.Logger;

import jus.poc.prodcons.Acteur;
import jus.poc.prodcons.Aleatoire;
import jus.poc.prodcons.ControlException;
import jus.poc.prodcons.Message;
import jus.poc.prodcons.Observateur;
import jus.poc.prodcons._Consommateur;

public class Consommateur extends Acteur implements _Consommateur {

	/** Nombre de message lu par le consommateur */
	private int nbMessConso = 0;
	/** ProdCons utilisé par le consommateur pour lire les messages */
	private ProdCons pc;

	/** Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	/**
	 * @param observateur
	 *            L'obsevateur de la classe
	 * 
	 * @param moyenneTempsDeTraitement
	 *            Durée moyenne que le consommateur va mettre à consommer le
	 *            message.
	 * 
	 * @param deviationTempsDeTraitement
	 *            Déviation du temps moyen du temps de consommation
	 * 
	 * @param pc
	 *            Le ProdCons dans lequel le consommateur va lire les messages.
	 * 
	 * @throws ControlException
	 */
	protected Consommateur(Observateur observateur,
			int moyenneTempsDeTraitement, int deviationTempsDeTraitement,
			ProdCons pc) throws ControlException {
		super(Acteur.typeConsommateur, observateur, moyenneTempsDeTraitement,
				deviationTempsDeTraitement);
		this.pc = pc;
	}

	/**
	 * Execution d'un Thread consommateur. Un consommateur récupère des messages
	 * dans ProdCons tant qu'il le peut et simule un traitement sur ceux-ci. Le
	 * traitement est simulé par un appel à sleep() avec un temps tiré
	 * aléatoirement à l'aide de moyenneTempsDeTraitement et
	 * deviationTempsDeTraitement.
	 */
	@Override
	public void run() {
		int tAlea;
		Message m;
		while (true) {
			try {
				/* On récupère un message dans ProdCons */
				m = this.pc.get(this);
				this.nbMessConso++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			/* On génére un temps de traitement aléatoire */
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
