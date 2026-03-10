import java.util.Scanner;

/**
 * Simulateur - Point d'entrée du programme Java.
 *
 * LOGIQUE DU PROGRAMME:
 *  Le programme propose trois modes :
 *   1. Démonstration simple   – scénario prédéfini avec 2 pistes
 *   2. Démonstration avancée  – gestion de la saturation (1 piste)
 *   3. Mode interactif        – l'utilisateur contrôle tout via un menu
 *
 * Lancement :
 *   javac -d out src/*.java
 *   java -cp out Simulateur
 */
public class Simulateur {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println();
        printRepeat("✈ ", 30);
        System.out.println("          SIMULATEUR DE GESTION AERIENNE (Java)");
        printRepeat("✈ ", 30);
        System.out.println();

        System.out.println("Choisissez un mode:");
        System.out.println("1. Démonstration simple");
        System.out.println("2. Démonstration avancée (saturation)");
        System.out.println("3. Mode interactif");
        System.out.print("\nVotre choix (1-3): ");

        String choix = sc.nextLine().trim();

        switch (choix) {
            case "1" -> demonstrationSimple();
            case "2" -> demonstrationComplexe();
            case "3" -> menuInteractif(sc);
            default  -> {
                System.out.println("Choix invalide, lancement de la démonstration simple...");
                demonstrationSimple();
            }
        }

        sc.close();
    }

    // ------------------------------------------------------------------ //
    //  Démonstrations prédéfinies
    // ------------------------------------------------------------------ //

    /**
     * Démonstration simple : 3 avions, 2 pistes, atterrissages puis décollages.
     *
     * OBJECTIF PÉDAGOGIQUE:
     *  Montre le cycle complet et l'effet de la file d'attente quand
     *  le nombre d'avions dépasse le nombre de pistes disponibles.
     */
    static void demonstrationSimple() {
        printStar("DÉMONSTRATION SIMPLE DU SIMULATEUR DE GESTION AÉRIENNE");

        Aeroport aeroport = new Aeroport("Charles de Gaulle", 2);
        System.out.println("Aéroport '" + aeroport.getNom() + "' créé avec "
                + aeroport.getPistes().size() + " pistes\n");

        Avion avion1 = new Avion("AF123", "Air France");
        Avion avion2 = new Avion("LH456", "Lufthansa");
        Avion avion3 = new Avion("BA789", "British Airways");

        // --- Scénario 1 : arrivée ---
        printScenario("SCÉNARIO 1 : Arrivée de plusieurs avions");
        aeroport.ajouterAvionEnApproche(avion1);
        aeroport.ajouterAvionEnApproche(avion2);
        aeroport.ajouterAvionEnApproche(avion3);
        aeroport.afficherEtat();

        // --- Scénario 2 : atterrissages ---
        printScenario("SCÉNARIO 2 : Atterrissages");
        aeroport.autoriserAtterrissage();   // AF123
        aeroport.autoriserAtterrissage();   // LH456
        aeroport.afficherEtat();

        // --- Scénario 3 : finalisation ---
        printScenario("SCÉNARIO 3 : Finalisation des atterrissages");
        aeroport.finaliserAtterrissage("AF123");
        aeroport.finaliserAtterrissage("LH456");
        aeroport.afficherEtat();

        // --- Scénario 4 : troisième avion ---
        printScenario("SCÉNARIO 4 : Troisième atterrissage");
        aeroport.autoriserAtterrissage();   // BA789
        aeroport.finaliserAtterrissage("BA789");
        aeroport.afficherEtat();

        // --- Scénario 5 : décollages ---
        printScenario("SCÉNARIO 5 : Décollages");
        aeroport.autoriserDecollage("AF123");
        aeroport.finaliserDecollage("AF123");
        aeroport.autoriserDecollage("LH456");
        aeroport.finaliserDecollage("LH456");
        aeroport.afficherEtat();

        aeroport.afficherHistorique();
    }

    /**
     * Démonstration avancée : 3 avions arrivent sur un aéroport n'ayant qu'une seule piste.
     *
     * OBJECTIF PÉDAGOGIQUE:
     *  Montre clairement que la file d'attente prend en charge la saturation :
     *  le troisième avion doit attendre la libération de la piste unique.
     */
    static void demonstrationComplexe() {
        printStar("DÉMONSTRATION AVANCÉE : GESTION DE LA SATURATION");

        Aeroport aeroport = new Aeroport("Orly", 1);
        System.out.println("Aéroport '" + aeroport.getNom() + "' créé avec "
                + aeroport.getPistes().size() + " piste\n");

        Avion[] avions = {
            new Avion("EZ101", "EasyJet"),
            new Avion("RY202", "Ryanair"),
            new Avion("VY303", "Vueling")
        };

        printScenario("Saturation avec une seule piste");
        for (Avion a : avions) {
            aeroport.ajouterAvionEnApproche(a);
        }
        aeroport.afficherEtat();

        printScenario("Premier avion atterrit");
        aeroport.autoriserAtterrissage();
        aeroport.afficherEtat();

        printScenario("Tentative du deuxième (piste occupée)");
        aeroport.autoriserAtterrissage();   // doit échouer
        aeroport.afficherEtat();

        printScenario("Libération de la piste");
        aeroport.finaliserAtterrissage("EZ101");
        aeroport.afficherEtat();

        printScenario("Maintenant le deuxième peut atterrir");
        aeroport.autoriserAtterrissage();
        aeroport.finaliserAtterrissage("RY202");
        aeroport.afficherEtat();

        printScenario("Et le troisième");
        aeroport.autoriserAtterrissage();
        aeroport.finaliserAtterrissage("VY303");
        aeroport.afficherEtat();

        aeroport.afficherHistorique();
    }

    // ------------------------------------------------------------------ //
    //  Mode interactif
    // ------------------------------------------------------------------ //

    /**
     * Menu interactif permettant de contrôler l'aéroport opération par opération.
     *
     * @param sc Scanner partagé avec main()
     */
    static void menuInteractif(Scanner sc) {
        printStar("MODE INTERACTIF - CONTRÔLE AÉRIEN");

        System.out.print("Nom de l'aéroport: ");
        String nom = sc.nextLine().trim();
        if (nom.isEmpty()) nom = "Mon Aéroport";

        int nbPistes = 3;
        System.out.print("Nombre de pistes (1-5): ");
        try {
            nbPistes = Integer.parseInt(sc.nextLine().trim());
            nbPistes = Math.max(1, Math.min(5, nbPistes));
        } catch (NumberFormatException ignored) { }

        Aeroport aeroport = new Aeroport(nom, nbPistes);
        System.out.println("\nAéroport '" + aeroport.getNom() + "' créé avec "
                + aeroport.getPistes().size() + " piste(s)\n");

        boolean continuer = true;
        while (continuer) {
            String sep = "=".repeat(60);
            System.out.println("\n" + sep);
            System.out.println("MENU PRINCIPAL");
            System.out.println(sep);
            System.out.println("1. Ajouter un avion en approche");
            System.out.println("2. Autoriser un atterrissage");
            System.out.println("3. Finaliser un atterrissage");
            System.out.println("4. Autoriser un décollage");
            System.out.println("5. Finaliser un décollage");
            System.out.println("6. Afficher l'état de l'aéroport");
            System.out.println("7. Afficher l'historique");
            System.out.println("8. Quitter");
            System.out.println(sep);
            System.out.print("\nVotre choix (1-8): ");

            String choix = sc.nextLine().trim();

            switch (choix) {
                case "1" -> {
                    System.out.print("Code du vol (ex: AF123): ");
                    String codeVol = sc.nextLine().trim().toUpperCase();
                    System.out.print("Compagnie aérienne: ");
                    String compagnie = sc.nextLine().trim();
                    if (!codeVol.isEmpty() && !compagnie.isEmpty()) {
                        aeroport.ajouterAvionEnApproche(new Avion(codeVol, compagnie));
                    } else {
                        System.out.println("⚠  Code de vol et compagnie requis");
                    }
                }
                case "2" -> aeroport.autoriserAtterrissage();
                case "3" -> {
                    System.out.print("Code du vol à finaliser: ");
                    String codeVol = sc.nextLine().trim().toUpperCase();
                    if (!codeVol.isEmpty()) {
                        aeroport.finaliserAtterrissage(codeVol);
                    } else {
                        System.out.println("⚠  Code de vol requis");
                    }
                }
                case "4" -> {
                    System.out.print("Code du vol à faire décoller: ");
                    String codeVol = sc.nextLine().trim().toUpperCase();
                    if (!codeVol.isEmpty()) {
                        aeroport.autoriserDecollage(codeVol);
                    } else {
                        System.out.println("⚠  Code de vol requis");
                    }
                }
                case "5" -> {
                    System.out.print("Code du vol à finaliser: ");
                    String codeVol = sc.nextLine().trim().toUpperCase();
                    if (!codeVol.isEmpty()) {
                        aeroport.finaliserDecollage(codeVol);
                    } else {
                        System.out.println("⚠  Code de vol requis");
                    }
                }
                case "6" -> aeroport.afficherEtat();
                case "7" -> aeroport.afficherHistorique();
                case "8" -> {
                    System.out.println("\nAu revoir! Merci d'avoir utilisé le simulateur.\n");
                    continuer = false;
                }
                default -> System.out.println("⚠  Choix invalide, veuillez réessayer");
            }
        }
    }

    // ------------------------------------------------------------------ //
    //  Utilitaires d'affichage
    // ------------------------------------------------------------------ //

    private static void printRepeat(String s, int n) {
        for (int i = 0; i < n; i++) System.out.print(s);
        System.out.println();
    }

    private static void printStar(String titre) {
        System.out.println();
        printRepeat("* ", 30);
        System.out.println(titre);
        printRepeat("* ", 30);
        System.out.println();
    }

    static void printScenario(String titre) {
        System.out.println("\n>>> " + titre);
        System.out.println("-".repeat(50));
    }
}
