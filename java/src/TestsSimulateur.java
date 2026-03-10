/**
 * TestsSimulateur - Tests unitaires du simulateur de gestion aérienne (Java).
 *
 * LOGIQUE:
 * Chaque méthode test*() vérifie une fonctionnalité précise.
 * En cas d'échec, une AssertionError est levée avec un message clair.
 * Tous les tests passent si le programme se termine avec le message
 * "TOUS LES TESTS ONT RÉUSSI".
 *
 * Lancement :
 *   javac -d out src/*.java
 *   java -cp out TestsSimulateur
 */
public class TestsSimulateur {

    // ------------------------------------------------------------------ //
    //  Point d'entrée
    // ------------------------------------------------------------------ //

    public static void main(String[] args) {
        String sep = "=".repeat(60);
        System.out.println(sep);
        System.out.println("TESTS DU SIMULATEUR DE GESTION AERIENNE (Java)");
        System.out.println(sep);
        System.out.println();

        testAvion();
        testPiste();
        testAeroportAtterrissage();
        testAeroportDecollage();
        testSaturation();

        System.out.println(sep);
        System.out.println("TOUS LES TESTS ONT RÉUSSI !");
        System.out.println(sep);
    }

    // ------------------------------------------------------------------ //
    //  Tests unitaires
    // ------------------------------------------------------------------ //

    /** Test 1 : création et état initial d'un avion. */
    static void testAvion() {
        System.out.println("Test 1 : Création d'un avion");

        Avion avion = new Avion("AF123", "Air France");

        assertEquals("codeVol",    "AF123",                avion.getCodeVol());
        assertEquals("compagnie",  "Air France",           avion.getCompagnie());
        assertEquals("statut",     StatutAvion.EN_APPROCHE, avion.getStatut());
        assertFalse("pisteAssignee", avion.aPisteAssignee());

        // Changement de statut
        avion.changerStatut(StatutAvion.AU_SOL);
        assertEquals("statut après changement", StatutAvion.AU_SOL, avion.getStatut());

        // Assignation de piste
        avion.assignerPiste(2);
        assertTrue("pisteAssignee après assignation", avion.aPisteAssignee());
        assertEquals("numéro piste", 2, avion.getPisteAssignee());

        avion.libererPiste();
        assertFalse("pisteAssignee après libération", avion.aPisteAssignee());

        System.out.println("OK Test avion réussi\n");
    }

    /** Test 2 : état initial et transitions d'une piste. */
    static void testPiste() {
        System.out.println("Test 2 : Création et gestion d'une piste");

        Piste piste = new Piste(1);
        assertEquals("numero", 1, piste.getNumero());
        assertTrue("disponible au départ", piste.estDisponible());
        assertEquals("statut initial", StatutPiste.DISPONIBLE, piste.getStatut());

        // Occuper avec un avion
        Avion avion = new Avion("AF123", "Air France");
        boolean ok = piste.occuper(avion);
        assertTrue("occuper() doit réussir", ok);
        assertFalse("piste non disponible après occuper", piste.estDisponible());
        assertEquals("avionActuel", avion, piste.getAvionActuel());

        // Tentative de double occupation
        assertFalse("double occupation doit échouer", piste.occuper(new Avion("LH456", "Lufthansa")));

        // Libérer
        Avion retour = piste.liberer();
        assertEquals("avion retourné par liberer", avion, retour);
        assertTrue("piste disponible après liberer", piste.estDisponible());
        assertNull("avionActuel null après liberer", piste.getAvionActuel());

        // Maintenance
        assertTrue("mettreEnMaintenance doit réussir", piste.mettreEnMaintenance());
        assertEquals("statut MAINTENANCE", StatutPiste.MAINTENANCE, piste.getStatut());
        assertFalse("non disponible en maintenance", piste.estDisponible());
        assertTrue("sortirDeMaintenance", piste.sortirDeMaintenance());
        assertTrue("disponible après maintenance", piste.estDisponible());

        System.out.println("OK Test piste réussi\n");
    }

    /** Test 3 : flux complet d'atterrissage. */
    static void testAeroportAtterrissage() {
        System.out.println("Test 3 : Flux d'atterrissage complet");

        Aeroport aeroport = new Aeroport("Test Airport", 2);
        Avion avion = new Avion("AF123", "Air France");

        // Ajout en approche
        aeroport.ajouterAvionEnApproche(avion);
        assertEquals("1 avion en approche", 1, aeroport.getAvionsEnApproche().size());
        assertEquals("statut EN_APPROCHE", StatutAvion.EN_APPROCHE, avion.getStatut());

        // Autoriser atterrissage
        assertTrue("autoriserAtterrissage doit réussir", aeroport.autoriserAtterrissage());
        assertEquals("file vide après autorisation", 0, aeroport.getAvionsEnApproche().size());
        assertEquals("statut ATTERRISSAGE", StatutAvion.ATTERRISSAGE, avion.getStatut());
        assertTrue("piste assignée", avion.aPisteAssignee());

        // Finaliser atterrissage
        assertTrue("finaliserAtterrissage doit réussir", aeroport.finaliserAtterrissage("AF123"));
        assertEquals("1 avion au sol", 1, aeroport.getAvionsAuSol().size());
        assertEquals("statut AU_SOL", StatutAvion.AU_SOL, avion.getStatut());
        assertFalse("piste libérée", avion.aPisteAssignee());

        System.out.println("OK Test atterrissage réussi\n");
    }

    /** Test 4 : flux complet de décollage. */
    static void testAeroportDecollage() {
        System.out.println("Test 4 : Flux de décollage complet");

        Aeroport aeroport = new Aeroport("Test Airport", 2);
        Avion avion = new Avion("AF123", "Air France");

        // Placer l'avion directement au sol
        avion.changerStatut(StatutAvion.AU_SOL);
        aeroport.getAvionsAuSol().add(avion);

        // Autoriser décollage
        assertTrue("autoriserDecollage doit réussir", aeroport.autoriserDecollage("AF123"));
        assertEquals("liste au sol vide", 0, aeroport.getAvionsAuSol().size());
        assertEquals("statut DECOLLAGE", StatutAvion.DECOLLAGE, avion.getStatut());
        assertTrue("piste assignée", avion.aPisteAssignee());

        // Finaliser décollage
        assertTrue("finaliserDecollage doit réussir", aeroport.finaliserDecollage("AF123"));
        assertEquals("statut EN_VOL", StatutAvion.EN_VOL, avion.getStatut());
        assertFalse("piste libérée", avion.aPisteAssignee());

        System.out.println("OK Test décollage réussi\n");
    }

    /** Test 5 : gestion de la saturation (1 piste, 2 avions simultanés). */
    static void testSaturation() {
        System.out.println("Test 5 : Gestion de la saturation (1 piste, 2 avions)");

        Aeroport aeroport = new Aeroport("Test Airport", 1);
        Avion avion1 = new Avion("AF123", "Air France");
        Avion avion2 = new Avion("LH456", "Lufthansa");

        aeroport.ajouterAvionEnApproche(avion1);
        aeroport.ajouterAvionEnApproche(avion2);

        // Premier atterrissage réussit
        assertTrue("premier atterrissage doit réussir", aeroport.autoriserAtterrissage());
        assertEquals("statut avion1 ATTERRISSAGE", StatutAvion.ATTERRISSAGE, avion1.getStatut());

        // Deuxième atterrissage échoue (piste occupée)
        assertFalse("deuxième atterrissage doit échouer", aeroport.autoriserAtterrissage());
        assertEquals("statut avion2 toujours EN_APPROCHE", StatutAvion.EN_APPROCHE, avion2.getStatut());
        assertEquals("avion2 toujours en file", 1, aeroport.getAvionsEnApproche().size());

        // Libérer la piste
        aeroport.finaliserAtterrissage("AF123");

        // Maintenant le deuxième peut atterrir
        assertTrue("atterrissage avion2 doit réussir", aeroport.autoriserAtterrissage());
        assertEquals("statut avion2 ATTERRISSAGE", StatutAvion.ATTERRISSAGE, avion2.getStatut());

        System.out.println("OK Test saturation réussi\n");
    }

    // ------------------------------------------------------------------ //
    //  Méthodes d'assertion minimalistes (pas de dépendance externe)
    // ------------------------------------------------------------------ //

    private static void assertEquals(String label, Object expected, Object actual) {
        if (!expected.equals(actual)) {
            throw new AssertionError("ECHEC [" + label + "]: attendu=" + expected + ", obtenu=" + actual);
        }
    }

    private static void assertEquals(String label, int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("ECHEC [" + label + "]: attendu=" + expected + ", obtenu=" + actual);
        }
    }

    private static void assertTrue(String label, boolean condition) {
        if (!condition) {
            throw new AssertionError("ECHEC [" + label + "]: la condition devrait être true");
        }
    }

    private static void assertFalse(String label, boolean condition) {
        if (condition) {
            throw new AssertionError("ECHEC [" + label + "]: la condition devrait être false");
        }
    }

    private static void assertNull(String label, Object obj) {
        if (obj != null) {
            throw new AssertionError("ECHEC [" + label + "]: devrait être null, obtenu=" + obj);
        }
    }
}
