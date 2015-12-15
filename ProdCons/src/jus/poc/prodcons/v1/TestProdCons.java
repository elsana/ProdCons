package jus.poc.prodcons.v1;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {

	static String fichierTest;

	int nbProd;
	int nbCons;
	int nbBuffer;
	int tempsMoyenProduction;
	int deviationTempsMoyenProduction;
	int tempsMoyenConsommation;
	int deviationTempsMoyenConsommation;
	int nombreMoyenDeProduction;
	int deviationNombreMoyenDeProduction;
	int nombreMoyenNbExemplaire;
	int deviationNombreMoyenNbExemplaire;

	/* Logger utilise pour l'affichage de debug */
	private final static Logger LOGGER = Logger.getLogger(TestProdCons.class
			.getName());

	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	@Override
	protected void run() throws Exception {
		// Corps du programme principal
		this.init("jus/poc/prodcons/options/" + fichierTest);// On lit un xml
		if ((nbProd > 0) && (nbCons > 0)) {
			ProdCons pc = new ProdCons(nbBuffer);
			Producteur[] prods = new Producteur[nbProd];// Tableau des
														// producteurs
			Consommateur[] cons = new Consommateur[nbCons];// Tableau des
															// consommateurs
			/* On crée les producteurs */
			LOGGER.info("Je crée les producteurs");
			LOGGER.info("Nombre: " + nbProd);

			for (int i = 0; i < prods.length; i++) {
				prods[i] = new Producteur(1, observateur, tempsMoyenProduction,
						deviationTempsMoyenProduction, nombreMoyenDeProduction,
						deviationNombreMoyenDeProduction, pc);// Creation prods
				prods[i].start();
			}

			LOGGER.info("Je crée les consommateurs");
			LOGGER.info("Nombre: " + nbCons);

			for (int i = 0; i < cons.length; i++) {
				cons[i] = new Consommateur(2, observateur,
						tempsMoyenConsommation,
						deviationTempsMoyenConsommation, pc,
						nombreMoyenDeProduction,
						deviationNombreMoyenDeProduction); // Creation
				// Cons
				cons[i].start();
			}
			/* Vérification fin exécution pour terminer appli */
			for (int i = 0; i < prods.length; i++) {
				prods[i].join();
			}
			do {
				Thread.sleep(250);
			} while (pc.enAttente() > 0);
			LOGGER.info("Simulation terminée.");
		} else {
			LOGGER.info("Simulation annulée : abscence de producteur ou de consommateur.");
		}
		System.exit(0);
	}

	public static void main(String[] args) {
		fichierTest = args[0];
		new TestProdCons(new Observateur()).start();
	}

	protected void init(String file) throws InvalidPropertiesFormatException,
			IOException, IllegalArgumentException, IllegalAccessException,
			NoSuchFieldException, SecurityException {
		Properties properties = new Properties();
		properties.loadFromXML(ClassLoader.getSystemResourceAsStream(file));
		String key;
		int value;
		Class<?> thisOne = getClass();
		for (Map.Entry<Object, Object> entry : properties.entrySet()) {
			key = (String) entry.getKey();
			value = Integer.parseInt((String) entry.getValue());
			thisOne.getDeclaredField(key).set(this, value);
		}
	}
}
