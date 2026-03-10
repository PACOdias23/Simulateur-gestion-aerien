/**
 * Avion - Représente un avion dans le système de gestion aérienne.
 *
 * LOGIQUE:
 * Un avion est caractérisé par :
 *  - un identifiant unique (codeVol, ex. "AF123")
 *  - une compagnie aérienne
 *  - un statut courant (machine à états via StatutAvion)
 *  - la piste qui lui est assignée (null si aucune)
 *
 * Cycle de vie normal :
 *   EN_APPROCHE → ATTERRISSAGE → AU_SOL → DECOLLAGE → EN_VOL
 */
public class Avion {

    /** Identifiant unique du vol (ex. "AF123") */
    private final String codeVol;

    /** Nom de la compagnie aérienne */
    private final String compagnie;

    /** État courant de l'avion */
    private StatutAvion statut;

    /**
     * Numéro de piste assignée (0 = aucune piste).
     * On utilise 0 comme valeur sentinelle pour rester simple (pas de Integer/Optional).
     */
    private int pisteAssignee;

    /**
     * Crée un nouvel avion.
     * L'avion démarre en état EN_APPROCHE car il vient de signaler son arrivée.
     *
     * @param codeVol   code de vol unique (ex. "AF123")
     * @param compagnie nom de la compagnie aérienne
     */
    public Avion(String codeVol, String compagnie) {
        this.codeVol = codeVol;
        this.compagnie = compagnie;
        this.statut = StatutAvion.EN_APPROCHE;
        this.pisteAssignee = 0;
    }

    // ------------------------------------------------------------------ //
    //  Getters
    // ------------------------------------------------------------------ //

    public String getCodeVol() {
        return codeVol;
    }

    public String getCompagnie() {
        return compagnie;
    }

    public StatutAvion getStatut() {
        return statut;
    }

    /**
     * Retourne le numéro de piste assignée, ou 0 si aucune piste.
     */
    public int getPisteAssignee() {
        return pisteAssignee;
    }

    /** @return true si l'avion a une piste assignée */
    public boolean aPisteAssignee() {
        return pisteAssignee != 0;
    }

    // ------------------------------------------------------------------ //
    //  Mutateurs
    // ------------------------------------------------------------------ //

    /**
     * Change l'état courant de l'avion.
     *
     * @param nouveauStatut le nouvel état à assigner
     */
    public void changerStatut(StatutAvion nouveauStatut) {
        this.statut = nouveauStatut;
    }

    /**
     * Assigne une piste à l'avion.
     *
     * @param numeroPiste numéro de la piste (doit être > 0)
     */
    public void assignerPiste(int numeroPiste) {
        this.pisteAssignee = numeroPiste;
    }

    /** Libère la piste assignée (remet à 0). */
    public void libererPiste() {
        this.pisteAssignee = 0;
    }

    // ------------------------------------------------------------------ //
    //  Affichage
    // ------------------------------------------------------------------ //

    @Override
    public String toString() {
        String pisteInfo = aPisteAssignee() ? "Piste " + pisteAssignee : "Aucune piste";
        return "Vol " + codeVol + " (" + compagnie + ") - " + statut + " - " + pisteInfo;
    }
}
