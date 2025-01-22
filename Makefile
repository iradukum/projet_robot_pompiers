# Ensimag 2A POO - TP 2022/23
# ============================
#
# Ce Makefile permet de compiler le test de l'ihm en ligne de commande.
# Alternative (recommandee?): utiliser un IDE (eclipse, netbeans, ...)
# Le but est ici d'illustrer les notions de "classpath", a vous de l'adapter
# a votre projet.
#
# Organisation:
#  1) Les sources (*.java) se trouvent dans le repertoire src
#     Les classes d'un package toto sont dans src/toto
#     Les classes du package par defaut sont dans src
#
#  2) Les bytecodes (*.class) se trouvent dans le repertoire bin
#     La hierarchie des sources (par package) est conservee.
#     L'archive bin/gui.jar contient les classes de l'interface graphique
#
# Compilation:
#  Options de javac:
#   -d : repertoire dans lequel sont places les .class compiles
#   -classpath : repertoire dans lequel sont cherches les .class deja compiles
#   -sourcepath : repertoire dans lequel sont cherches les .java (dependances)

all: testAffichage testInvader testLecture testio testRobot testGeographie testDonnees testEvenement testChemin testStrategie

testStrategie:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/strategie/*.java

testChemin:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/itineraires/*.java

testEvenement:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/scenario/*.java

testAffichage:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/affichage/*.java

testDonnees:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/donneesSimulation/*.java

testInvader:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/TestInvader.java

testLecture:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/TestLecteurDonnees.java

testRobot:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/robot/*.java 

testGeographie:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/geographie/*.java

testio:
	javac -d bin -classpath bin/gui.jar -sourcepath src src/io/*.java

# Execution:
# on peut taper directement la ligne de commande :
#   > java -classpath bin:bin/gui.jar TestInvader
# ou bien lancer l'execution en passant par ce Makefile:
#   > make exeInvader

exeChemin:
	java -classpath bin:bin/gui.jar itineraires/TestDijkstra

exeEvenement: 
	java -classpath bin:bin/gui.jar scenario/TestEvenement

exeInvader: 
	java -classpath bin:bin/gui.jar TestInvader

exeLecture: 
	java -classpath bin:bin/gui.jar TestLecteurDonnees cartes/carteSujet.map

exeResolution1:
	java -classpath bin:bin/gui.jar strategie.TestDebutant cartes/carteSujet.map

exeResolution2:
	java -classpath bin:bin/gui.jar strategie.TestInitie cartes/carteSujet.map

clean:
	rm -rf bin/*.class
	rm -rf bin/robot/*.class
	rm -rf bin/geographie/*.class
	rm -rf bin/io/*.class
	rm -rf bin/donneesSimulation/*.class
	rm -rf bin/affichage/*.class
	rm -rf bin/scenario/*.class
	rm -rf bin/itineraires/*.class
	rm -rf bin/strategie/*.class
