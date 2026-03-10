/**
 * StatutPiste - Énumération des états possibles d'une piste
 *
 * LOGIQUE:
 * Une piste est une ressource partagée. Elle ne peut être utilisée
 * que par un seul avion à la fois.
 *
 *   DISPONIBLE ←→ OCCUPEE
 *   DISPONIBLE ←→ MAINTENANCE
 */
public enum StatutPiste {
    DISPONIBLE("Disponible"),
    OCCUPEE("Occupée"),
    MAINTENANCE("En maintenance");

    private final String libelle;

    StatutPiste(String libelle) {
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
