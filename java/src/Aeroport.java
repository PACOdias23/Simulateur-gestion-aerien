import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Aeroport - Cœur du système de contrôle aérien.
 *
 * LOGIQUE GÉNÉRALE:
 * L'aéroport coordonne trois ressources :
 *   1. {@code pistes}           – liste des pistes physiques (nombre fixé à la création)
 *   2. {@code avionsEnApproche} – file d'attente FIFO des avions qui veulent atterrir
 *   3. {@code avionsAuSol}      – liste des avions garés, prêts à décoller
 *
 * ALGORITHME D'ATTRIBUTION DE PISTE:
 *   - On parcourt les pistes dans l'ordre et on prend la première disponible.
 *   - Si toutes les pistes sont occupées, l'opération est refusée (retour false).
 *
 * PRIORITÉS:
 *   - Les atterrissages sont traités en FIFO (premier arrivé = premier servi).
 *   - Les décollages sont initiés à la demande explicite (code vol fourni).
 *   - Atterrissage prioritaire sur décollage en cas de conflit de ressource.
 *
 * FLUX D'ATTERRISSAGE:
 *   ajouter → autoriserAtterrissage → finaliserAtterrissage
 *
 * FLUX DE DÉCOLLAGE:
 *   autoriserDecollage(codeVol) → finaliserDecollage(codeVol)
 */
public class Aeroport {

    private final String nom;
    private final List<Piste> pistes;
    private final Queue<Avion> avionsEnApproche;  // FIFO
    private final List<Avion> avionsAuSol;
    private final List<String> historique;

    /**
     * Crée un aéroport avec le nombre de pistes indiqué.
     *
     * @param nom          nom de l'aéroport
     * @param nombrePistes nombre de pistes à créer (doit être >= 1)
     */
    public Aeroport(String nom, int nombrePistes) {
        this.nom = nom;
        this.pistes = new ArrayList<>();
        for (int i = 1; i <= nombrePistes; i++) {
            pistes.add(new Piste(i));
        }
        this.avionsEnApproche = new LinkedList<>();
        this.avionsAuSol = new ArrayList<>();
        this.historique = new ArrayList<>();
    }

    /** Constructeur avec 3 pistes par défaut. */
    public Aeroport(String nom) {
        this(nom, 3);
    }

    // ------------------------------------------------------------------ //
    //  Getters (utiles pour les tests)
    // ------------------------------------------------------------------ //

    public String getNom() { return nom; }
    public List<Piste> getPistes() { return pistes; }
    public Queue<Avion> getAvionsEnApproche() { return avionsEnApproche; }
    public List<Avion> getAvionsAuSol() { return avionsAuSol; }
    public List<String> getHistorique() { return historique; }

    // ------------------------------------------------------------------ //
    //  Logique métier
    // ------------------------------------------------------------------ //

    /**
     * Enregistre un avion en approche et l'ajoute à la file d'attente.
     *
     * @param avion l'avion en approche
     */
    public void ajouterAvionEnApproche(Avion avion) {
        avion.changerStatut(StatutAvion.EN_APPROCHE);
        avionsEnApproche.add(avion);
        String msg = "✈  Vol " + avion.getCodeVol() + " signale son approche de l'aéroport";
        historique.add(msg);
        System.out.println(msg);
    }

    /**
     * Cherche la première piste disponible parmi toutes les pistes.
     *
     * @return une piste disponible, ou null si toutes sont occupées/en maintenance
     */
    public Piste trouverPisteDisponible() {
        for (Piste piste : pistes) {
            if (piste.estDisponible()) {
                return piste;
            }
        }
        return null;
    }

    /**
     * Autorise l'atterrissage du prochain avion en file d'attente.
     *
     * LOGIQUE:
     *  1. S'il n'y a aucun avion en approche → refus.
     *  2. Si aucune piste n'est libre       → refus (avion reste en tête de file).
     *  3. Sinon : défile le premier avion, lui assigne une piste,
     *     change son statut en ATTERRISSAGE et occupe la piste.
     *
     * @return true si un atterrissage a été autorisé, false sinon
     */
    public boolean autoriserAtterrissage() {
        if (avionsEnApproche.isEmpty()) {
            System.out.println("⚠  Aucun avion en approche");
            return false;
        }

        Piste piste = trouverPisteDisponible();
        if (piste == null) {
            System.out.println("⚠  Toutes les pistes sont occupées, l'avion doit patienter");
            return false;
        }

        // Défile le premier avion (FIFO)
        Avion avion = avionsEnApproche.poll();
        avion.changerStatut(StatutAvion.ATTERRISSAGE);
        avion.assignerPiste(piste.getNumero());
        piste.occuper(avion);

        String msg = ">> Vol " + avion.getCodeVol() + " autorisé à atterrir sur piste " + piste.getNumero();
        historique.add(msg);
        System.out.println(msg);
        return true;
    }

    /**
     * Finalise l'atterrissage d'un avion : libère la piste, pose l'avion au sol.
     *
     * @param codeVol code du vol dont l'atterrissage est terminé
     * @return true si la finalisation a réussi, false si le vol n'est pas trouvé
     */
    public boolean finaliserAtterrissage(String codeVol) {
        for (Piste piste : pistes) {
            Avion avion = piste.getAvionActuel();
            if (avion != null
                    && avion.getCodeVol().equals(codeVol)
                    && avion.getStatut() == StatutAvion.ATTERRISSAGE) {

                piste.liberer();
                avion.changerStatut(StatutAvion.AU_SOL);
                avion.libererPiste();
                avionsAuSol.add(avion);

                String msg = "OK Vol " + codeVol + " a atterri et libéré la piste " + piste.getNumero();
                historique.add(msg);
                System.out.println(msg);
                return true;
            }
        }
        System.out.println("⚠  Vol " + codeVol + " non trouvé en phase d'atterrissage");
        return false;
    }

    /**
     * Autorise le décollage d'un avion identifié par son code de vol.
     *
     * LOGIQUE:
     *  1. Cherche l'avion dans la liste des avions au sol.
     *  2. Cherche une piste disponible.
     *  3. Si les deux conditions sont remplies, change le statut et occupe la piste.
     *
     * @param codeVol code du vol à faire décoller
     * @return true si le décollage a été autorisé, false sinon
     */
    public boolean autoriserDecollage(String codeVol) {
        Avion avion = trouverAvionAuSol(codeVol);
        if (avion == null) {
            System.out.println("⚠  Vol " + codeVol + " non trouvé au sol");
            return false;
        }

        Piste piste = trouverPisteDisponible();
        if (piste == null) {
            System.out.println("⚠  Toutes les pistes sont occupées, impossible de décoller maintenant");
            return false;
        }

        avionsAuSol.remove(avion);
        avion.changerStatut(StatutAvion.DECOLLAGE);
        avion.assignerPiste(piste.getNumero());
        piste.occuper(avion);

        String msg = ">> Vol " + codeVol + " autorisé à décoller de la piste " + piste.getNumero();
        historique.add(msg);
        System.out.println(msg);
        return true;
    }

    /**
     * Finalise le décollage d'un avion : libère la piste, l'avion quitte l'espace aérien.
     *
     * @param codeVol code du vol dont le décollage est terminé
     * @return true si la finalisation a réussi, false sinon
     */
    public boolean finaliserDecollage(String codeVol) {
        for (Piste piste : pistes) {
            Avion avion = piste.getAvionActuel();
            if (avion != null
                    && avion.getCodeVol().equals(codeVol)
                    && avion.getStatut() == StatutAvion.DECOLLAGE) {

                piste.liberer();
                avion.changerStatut(StatutAvion.EN_VOL);
                avion.libererPiste();

                String msg = "OK Vol " + codeVol + " a décollé et libéré la piste " + piste.getNumero();
                historique.add(msg);
                System.out.println(msg);
                return true;
            }
        }
        System.out.println("⚠  Vol " + codeVol + " non trouvé en phase de décollage");
        return false;
    }

    // ------------------------------------------------------------------ //
    //  Affichage
    // ------------------------------------------------------------------ //

    /** Affiche l'état complet de l'aéroport dans la console. */
    public void afficherEtat() {
        String sep = "=".repeat(60);
        System.out.println();
        System.out.println(sep);
        System.out.println("AEROPORT: " + nom);
        System.out.println(sep);

        System.out.println("\nPISTES:");
        for (Piste p : pistes) {
            System.out.println("  " + p);
        }

        System.out.println("\nAVIONS EN APPROCHE (" + avionsEnApproche.size() + "):");
        if (avionsEnApproche.isEmpty()) {
            System.out.println("  Aucun");
        } else {
            for (Avion a : avionsEnApproche) {
                System.out.println("  - " + a);
            }
        }

        System.out.println("\nAVIONS AU SOL (" + avionsAuSol.size() + "):");
        if (avionsAuSol.isEmpty()) {
            System.out.println("  Aucun");
        } else {
            for (Avion a : avionsAuSol) {
                System.out.println("  - " + a);
            }
        }

        System.out.println(sep);
        System.out.println();
    }

    /** Affiche le journal chronologique de tous les événements. */
    public void afficherHistorique() {
        String sep = "=".repeat(60);
        System.out.println();
        System.out.println(sep);
        System.out.println("HISTORIQUE DES EVENEMENTS");
        System.out.println(sep);
        int i = 1;
        for (String event : historique) {
            System.out.println(i++ + ". " + event);
        }
        System.out.println(sep);
        System.out.println();
    }

    // ------------------------------------------------------------------ //
    //  Méthodes utilitaires privées
    // ------------------------------------------------------------------ //

    /**
     * Recherche un avion dans la liste des avions au sol.
     *
     * @param codeVol code du vol recherché
     * @return l'avion trouvé, ou null
     */
    private Avion trouverAvionAuSol(String codeVol) {
        for (Avion a : avionsAuSol) {
            if (a.getCodeVol().equals(codeVol)) {
                return a;
            }
        }
        return null;
    }
}
