/**
 * Piste - Représente une piste d'atterrissage/décollage.
 *
 * LOGIQUE:
 * Une piste est une ressource limitée partagée entre les avions.
 * À tout instant, une piste ne peut être utilisée que par UN SEUL avion.
 *
 * Transitions d'état :
 *   DISPONIBLE → occuper(avion)  → OCCUPEE
 *   OCCUPEE    → liberer()       → DISPONIBLE
 *   DISPONIBLE → mettreEnMaintenance() → MAINTENANCE
 *   MAINTENANCE → sortirDeMaintenance() → DISPONIBLE
 */
public class Piste {

    /** Identifiant unique de la piste */
    private final int numero;

    /** État courant de la piste */
    private StatutPiste statut;

    /** Avion utilisant actuellement la piste (null si disponible) */
    private Avion avionActuel;

    /**
     * Crée une nouvelle piste, initialement disponible.
     *
     * @param numero numéro unique de la piste (commence à 1)
     */
    public Piste(int numero) {
        this.numero = numero;
        this.statut = StatutPiste.DISPONIBLE;
        this.avionActuel = null;
    }

    // ------------------------------------------------------------------ //
    //  Getters
    // ------------------------------------------------------------------ //

    public int getNumero() {
        return numero;
    }

    public StatutPiste getStatut() {
        return statut;
    }

    /** @return l'avion qui occupe la piste, ou null si la piste est libre */
    public Avion getAvionActuel() {
        return avionActuel;
    }

    // ------------------------------------------------------------------ //
    //  Logique métier
    // ------------------------------------------------------------------ //

    /**
     * Vérifie si la piste est disponible.
     *
     * @return true si et seulement si le statut est DISPONIBLE
     */
    public boolean estDisponible() {
        return statut == StatutPiste.DISPONIBLE;
    }

    /**
     * Marque la piste comme occupée par un avion.
     *
     * LOGIQUE:
     *  1. Vérifie que la piste est disponible.
     *  2. Change le statut en OCCUPEE.
     *  3. Mémorise la référence à l'avion.
     *
     * @param avion l'avion qui prend la piste
     * @return true si l'occupation a réussi, false si la piste n'était pas libre
     */
    public boolean occuper(Avion avion) {
        if (!estDisponible()) {
            return false;
        }
        this.statut = StatutPiste.OCCUPEE;
        this.avionActuel = avion;
        return true;
    }

    /**
     * Libère la piste et la rend disponible.
     *
     * @return l'avion qui occupait la piste (peut être null si la piste était déjà libre)
     */
    public Avion liberer() {
        Avion avionPrecedent = this.avionActuel;
        this.statut = StatutPiste.DISPONIBLE;
        this.avionActuel = null;
        return avionPrecedent;
    }

    /**
     * Met la piste en maintenance (seulement si elle est disponible).
     *
     * @return true si la transition a réussi
     */
    public boolean mettreEnMaintenance() {
        if (estDisponible()) {
            this.statut = StatutPiste.MAINTENANCE;
            return true;
        }
        return false;
    }

    /**
     * Sort la piste de maintenance et la rend disponible.
     *
     * @return true si la transition a réussi
     */
    public boolean sortirDeMaintenance() {
        if (statut == StatutPiste.MAINTENANCE) {
            this.statut = StatutPiste.DISPONIBLE;
            return true;
        }
        return false;
    }

    // ------------------------------------------------------------------ //
    //  Affichage
    // ------------------------------------------------------------------ //

    @Override
    public String toString() {
        if (avionActuel != null) {
            return "Piste " + numero + ": occupée par " + avionActuel.getCodeVol();
        }
        return "Piste " + numero + ": " + statut;
    }
}
