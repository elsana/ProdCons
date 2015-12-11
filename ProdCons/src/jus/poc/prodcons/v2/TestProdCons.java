package jus.poc.prodcons.v2;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;

import jus.poc.prodcons.Observateur;
import jus.poc.prodcons.Simulateur;

public class TestProdCons extends Simulateur {

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

	public TestProdCons(Observateur observateur) {
		super(observateur);
	}

	@Override
	protected void run() throws Exception {
		// Corps du programme principal
		this.init("jus/poc/prodcons/options/v1.xml");// On lit un xml
		System.out.println("marque 1");
		ProdCons pc = new ProdCons(nbBuffer);
		System.out.println("marque 3");
		Producteur[] prods = new Producteur[nbProd];// Tableau des producteurs
		System.out.println("marque 4");
		Consommateur[] cons = new Consommateur[nbCons];// Tableau des
														// consommateurs
		/* On crée les producteurs */
		System.out.println("Je crée les producteurs");
		System.out.println("Nombre: " + nbProd);
		for (int i = 0; i < prods.length; i++) {
			prods[i] = new Producteur(1, observateur, tempsMoyenProduction,
					deviationTempsMoyenProduction, nombreMoyenNbExemplaire,
					deviationNombreMoyenNbExemplaire, pc);// Creation prods
			prods[i].start();
		}

		System.out.println("Je crée les consommateurs");
		System.out.println("Nombre: " + nbCons);
		for (int i = 0; i < cons.length; i++) {
			cons[i] = new Consommateur(2, observateur, tempsMoyenConsommation,
					deviationTempsMoyenConsommation, pc,
					nombreMoyenNbExemplaire, deviationNombreMoyenNbExemplaire); // Creation
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
		System.out.println("Simulation terminée.");
		System.exit(0);
	}

	public static void main(String[] args) {
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
