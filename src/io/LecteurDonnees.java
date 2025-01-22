package io;


import java.io.*;
import java.util.*;
import java.util.zip.DataFormatException;

import donneesSimulation.DonneesSimulation;
import geographie.Incendie;
import geographie.Carte;
import geographie.Case;
import geographie.Case.NatureTerrain;
import robot.Robot;
import robot.Drone;
import robot.RobotAPattes;
import robot.RobotAChenille;
import robot.RobotARoue;


/**
 * Lecteur de cartes au format spectifié dans le sujet.
 * Les données sur les cases, robots puis incendies sont lues dans le fichier,
 * puis simplement affichées.
 * A noter: pas de vérification sémantique sur les valeurs numériques lues.
 *
 * IMPORTANT:
 *
 * Cette classe ne fait que LIRE les infos et les afficher.
 * A vous de modifier ou d'ajouter des méthodes, inspirées de celles présentes
 * (ou non), qui CREENT les objets au moment adéquat pour construire une
 * instance de la classe DonneesSimulation à partir d'un fichier.
 *
 * Vous pouvez par exemple ajouter une méthode qui crée et retourne un objet
 * contenant toutes les données lues:
 *    public static DonneesSimulation creeDonnees(String fichierDonnees);
 * Et faire des méthode creeCase(), creeRobot(), ... qui lisent les données,
 * créent les objets adéquats et les ajoutent ds l'instance de
 * DonneesSimulation.
 */
public class LecteurDonnees {


    /**
     * Lit et affiche le contenu d'un fichier de donnees (cases,
     * robots et incendies).
     * Ceci est méthode de classe; utilisation:
     * LecteurDonnees.lire(fichierDonnees)
     * @param fichierDonnees nom du fichier à lire
     */
    public static DonneesSimulation lire(String fichierDonnees)
        throws FileNotFoundException, DataFormatException {
        System.out.println("\n == Lecture du fichier" + fichierDonnees);
        LecteurDonnees lecteur = new LecteurDonnees(fichierDonnees);
        DonneesSimulation tableau = new DonneesSimulation();
        Carte map = lecteur.lireCarte(tableau);
        tableau.associerCarte(map);
        lecteur.lireIncendies(tableau);
        lecteur.lireRobots(tableau);
        scanner.close();
        System.out.println("\n == Lecture terminee");
        return tableau;
    }




    // Tout le reste de la classe est prive!

    private static Scanner scanner;

    /**
     * Constructeur prive; impossible d'instancier la classe depuis l'exterieur
     * @param fichierDonnees nom du fichier a lire
     */
    private LecteurDonnees(String fichierDonnees)
        throws FileNotFoundException {
        scanner = new Scanner(new File(fichierDonnees));
        scanner.useLocale(Locale.US);
    }

    /**
     * Lit et affiche les donnees de la carte.
     * @throws ExceptionFormatDonnees
     */
    private Carte lireCarte(DonneesSimulation tableau) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbLignes = scanner.nextInt();
            int nbColonnes = scanner.nextInt();
            int tailleCases = scanner.nextInt();	// en m
            Carte map = new Carte(nbLignes, nbColonnes, tailleCases);
            System.out.println("Carte " + nbLignes + "x" + nbColonnes
                    + "; taille des cases = " + tailleCases);

            for (int lig = 0; lig < nbLignes; lig++) {
                for (int col = 0; col < nbColonnes; col++) {
                    NatureTerrain nature = lireCase(lig, col);
                    map.setNatureCase(lig, col, nature);
                }
            }
            return map;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbLignes nbColonnes tailleCases");
        }
        
        // une ExceptionFormat levee depuis lireCase est remontee telle quelle
    }




    /**
     * Lit et affiche les donnees d'une case.
     */
    private NatureTerrain lireCase(int lig, int col) throws DataFormatException {
        ignorerCommentaires();
        String chaineNature = new String();
        NatureTerrain nature;

        try {
            chaineNature = scanner.next();
            // si NatureTerrain est un Enum, vous pouvez recuperer la valeur
            // de l'enum a partir d'une String avec:
            nature = NatureTerrain.valueOf(chaineNature);
            verifieLigneTerminee();

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de case invalide. "
                    + "Attendu: nature altitude [valeur_specifique]");
        }
        
        return nature;

    }


    /**
     * Lit et affiche les donnees des incendies.
     */
    private void lireIncendies(DonneesSimulation tableau) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbIncendies = scanner.nextInt();
            System.out.println("Nb d'incendies = " + nbIncendies);
            for (int i = 0; i < nbIncendies; i++) {
                Incendie fire = lireIncendie(i, tableau);
                tableau.ajouterIncendie(fire);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbIncendies");
        }
        
    }


    /**
     * Lit et affiche les donnees du i-eme incendie.
     * @param i
     */
    private Incendie lireIncendie(int i, DonneesSimulation tableau) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Incendie " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            int intensite = scanner.nextInt();
            if (intensite <= 0) {
                throw new DataFormatException("incendie " + i
                        + "nb litres pour eteindre doit etre > 0");
            }
            verifieLigneTerminee();

            System.out.println("position = (" + lig + "," + col
                    + ");\t intensite = " + intensite);
            Incendie fire = new Incendie(tableau.getMap().getCase(lig, col), intensite);
            return fire;
        } catch (NoSuchElementException e) {
            throw new DataFormatException("format d'incendie invalide. "
                    + "Attendu: ligne colonne intensite");
        }
        
    }


    /**
     * Lit et affiche les donnees des robots.
     */
    private void lireRobots(DonneesSimulation tableau) throws DataFormatException {
        ignorerCommentaires();
        try {
            int nbRobots = scanner.nextInt();
            System.out.println("Nb de robots = " + nbRobots);
            for (int i = 0; i < nbRobots; i++) {
                Robot robot = lireRobot(i, tableau);
                tableau.ajouterRobot(robot);
            }

        } catch (NoSuchElementException e) {
            throw new DataFormatException("Format invalide. "
                    + "Attendu: nbRobots");
        }
    }


    /**
     * Lit et affiche les donnees du i-eme robot.
     * @param i
     */
    private Robot lireRobot(int i, DonneesSimulation tableau) throws DataFormatException {
        ignorerCommentaires();
        System.out.print("Robot " + i + ": ");

        try {
            int lig = scanner.nextInt();
            int col = scanner.nextInt();
            System.out.print("position = (" + lig + "," + col + ");");
            Case position = tableau.getMap().getCase(lig, col);
            String type = scanner.next();

            System.out.print("\t type = " + type);


            // lecture eventuelle d'une vitesse du robot (entier)
            System.out.print("; \t vitesse = ");
            String s = scanner.findInLine("(\\d+)");	// 1 or more digit(s) ?
            // pour lire un flottant:    ("(\\d+(\\.\\d+)?)");

            int vitesse = -1;
            if (s == null) {
                System.out.print("valeur par defaut");
            } else {
                vitesse = Integer.parseInt(s);
                System.out.print(vitesse);
            }
            verifieLigneTerminee();

            System.out.println();

            Robot robot;
            switch(type){
                case "DRONE":
                    if (vitesse<0){
                        robot = new Drone(position, 100, 1.0);
                    } else {
                        if (vitesse > 150){
                            throw new DataFormatException("Vitesse trop grande\tmax = 150km/h");
                        }
                        robot = new Drone(position, vitesse, 1.0);
                    }
                    break;
                case "ROUES":
                    if (vitesse<0){
                        robot = new RobotARoue(position, 80, 1.0);
                    } else {
                        robot = new RobotARoue(position, vitesse, 1.0);
                    }
                    break;
                case "CHENILLES":
                    if (vitesse<0){
                        robot = new RobotAChenille(position, 60, 1.0);
                    } else {
                        if (vitesse > 80){
                            throw new DataFormatException("Vitesse trop grande\tmax = 80km/h");
                        }
                        robot = new RobotAChenille(position, vitesse, 1.0);
                    }
                    break;
                case "PATTES":
                    if (vitesse<0){
                        robot = new RobotAPattes(position);
                    } else {
                        robot = new RobotAPattes(position);
                    }
                    break;
                default :
                    throw new DataFormatException("Format de robot invalide; \n\tattendu : DRONE ROUES CHENILLES PATTES");
            }

            return robot;

        } catch (NoSuchElementException e) {
            throw new DataFormatException("format de robot invalide. "
                    + "Attendu: ligne colonne type [valeur_specifique]");
        }
    }




    /** Ignore toute (fin de) ligne commencant par '#' */
    private void ignorerCommentaires() {
        while(scanner.hasNext("#.*")) {
            scanner.nextLine();
        }
    }

    /**
     * Verifie qu'il n'y a plus rien a lire sur cette ligne (int ou float).
     * @throws ExceptionFormatDonnees
     */
    private void verifieLigneTerminee() throws DataFormatException {
        if (scanner.findInLine("(\\d+)") != null) {
            throw new DataFormatException("format invalide, donnees en trop.");
        }
    }
}
