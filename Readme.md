#Compte-Rendu TPSE Programmation concurrente
Realisé par Antoine REVEL et Elsa NAVARRO.

##Principe du programme
Simulation du comportement de processus producteurs et de processus
consommateurs avec tampon intermédiaire borné, réalisé en binôme dans le cadre du cours de Système d'exploitation de RICM4. L'objectif principal était de simuler une programmation concurrente entre producteurs et consommateurs au moyen d'une applicaiton java.

###Exécution

L'exécution du programme demande en paramètre un fichier de test au format xml se trouvant dans le dossier jus.poc.prodcons.options, et une option -DEBUG=1 permet d'afficher le déroulement de l'exxécution lors du test, -DEBUG=0 désactive cette option.
'''
java -classpath ProdCons.jar:$CLASSPATH TestProdCons "test.xml" -DEBUG=1
java -classpath ProdCons.jar:$CLASSPATH TestProdCons "test.xml" -DEBUG=0
'''

###Contenu du fichier test.xml
Le fichier test.xml regroupe les paramètres passés au programme pour l'exécution des tests. 
Les temps de production et de consommation sont différents pour chaque message, de même que le nombre moyen de message produit par un producteur, et le nombre d'exemplaire déposés pour les versions plus avancées. Des variables de deviation autour de cette moyenne sont donc nécessaire pour contrôler cet aleatoire.
Le fichier xml a donc la forme suivante :
'''
<entry key="nbProd">1</entry>		//le nombre de producteurs
<entry key="nbCons">5</entry>		//le nombre de consommateurs
<entry key="nbBuffer">1</entry>		//le nombre buffers utilisés pour le dépôt des messages
<entry key="tempsMoyenProduction">15</entry>			
<entry key="deviationTempsMoyenProduction">4</entry>	
<entry key="tempsMoyenConsommation">5</entry>
<entry key="deviationTempsMoyenConsommation">2</entry>
<entry key="nombreMoyenDeProduction">5</entry>
<entry key="deviationNombreMoyenDeProduction">2</entry>
<entry key="nombreMoyenNbExemplaire">30</entry>
<entry key="deviationNombreMoyenNbExemplaire">3</entry>
'''

###Resultats
Exemple : extrait de messages obtenus lors d'une exécution.
'''
[INFOS]: {jus.poc.prodcons.v4.ProdCons put} PROD : Je suis le message numéro 30 du producteur 2 present en 2 exemplaires
[INFOS]: {jus.poc.prodcons.v4.ProdCons get} CONSO 4 : Je suis le message numéro 30 du producteur 2 present en 2 exemplaires
[INFOS]: {jus.poc.prodcons.v4.ProdCons get} CONSO 2 : Je suis le message numéro 30 du producteur 2 present en 1 exemplaires
[INFOS]: {jus.poc.prodcons.v4.ProdCons get} DESTRUCTION : le message numéro 30 du producteur 2
'''
Le mot-clé PROD suivit de l'identification du message (détaillant son numéro, son producteur et le nombre d'exemplaires lorsque c'est nécessaire) indique qu'un producteeur vient de déposer un message.

Le mot-clé CONSO est suivit d'un entier qui indique quel est le consommateur qui a récupéré le message dans le buffer. On voit ensuite l'identification du message qui a été récupéré, et son nombre d'exemplaires décrémente pour la prochaine consommation.

Le mot-clé DESTRUCTION apparaît après la dernière consommation d'un message. Ceci s'affiche uniquement lorsque les messages sont créés en plusieurs exemplaires, sinon une consommation correspond à une destruction.

####Terminaison
Lorsque la simulation termine sans échec, le message suivant clôt le suivit du déroulement :
'''
[INFOS]: {jus.poc.prodcons.v4.TestProdCons run} Simulation terminée avec succès.
'''

##Objectif 1 (v1)
Temps passé : 4h

Dans cette partie, nous avons implémenté une solution naïve au problème de producteurs consommateurs, au moyen du système de garde/action.

###Tests :


##Objectif 2 (v2)
Temps passé : 3h
 
###Tests :


##Objectif 3 (v3)
Temps passé : 4h

###Tests :


##Objectif 4 (v4)
Temps passé : 5h

###Tests :


##Objectif 5 (v5)
Temps passé :

###Tests :


##Objectif 6
Temps passé :
 
###Tests :
