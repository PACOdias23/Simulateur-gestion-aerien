/**
 * StatutAvion - Énumération des états possibles d'un avion
 *
 * LOGIQUE:
 * Un avion traverse ces états dans cet ordre typique:
 *   EN_APPROCHE → ATTERRISSAGE → AU_SOL → DECOLLAGE → EN_VOL
 *
 * Chaque état correspond à une phase précise du traitement aérien.
 */
public enum StatutAvion {
    EN_APPROCHE("En approche"),
    ATTERRISSAGE("Atterrissage en cours"),
    AU_SOL("Au sol"),
    DECOLLAGE("Décollage en cours"),
    EN_VOL("En vol");

    /** Libellé lisible affiché dans les messages console */
    private final String libelle;

    StatutAvion(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
