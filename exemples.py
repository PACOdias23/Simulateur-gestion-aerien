"""
Exemple d'utilisation de l'API du simulateur
Ce fichier montre comment utiliser le simulateur de manière programmatique
"""

from aeroport import Aeroport
from avion import Avion


def exemple_simple():
    """Exemple simple d'utilisation"""
    print("="*60)
    print("EXEMPLE 1: Utilisation Simple")
    print("="*60 + "\n")
    
    # Créer un aéroport
    aeroport = Aeroport("Orly", nombre_pistes=2)
    
    # Créer des avions
    vol1 = Avion("AF100", "Air France")
    vol2 = Avion("EZ200", "EasyJet")
    
    # Ajouter les avions en approche
    aeroport.ajouter_avion_en_approche(vol1)
    aeroport.ajouter_avion_en_approche(vol2)
    
    # Autoriser les atterrissages
    aeroport.autoriser_atterrissage()
    aeroport.autoriser_atterrissage()
    
    # Finaliser les atterrissages
    aeroport.finaliser_atterrissage("AF100")
    aeroport.finaliser_atterrissage("EZ200")
    
    # Afficher l'état final
    aeroport.afficher_etat()


def exemple_cycle_complet():
    """Exemple d'un cycle complet: arrivée → atterrissage → décollage"""
    print("\n" + "="*60)
    print("EXEMPLE 2: Cycle Complet")
    print("="*60 + "\n")
    
    aeroport = Aeroport("Roissy", nombre_pistes=1)
    
    # Avion arrive
    avion = Avion("LH500", "Lufthansa")
    print(f"1. Création de l'avion: {avion}\n")
    
    # Approche
    aeroport.ajouter_avion_en_approche(avion)
    print(f"2. Après approche: {avion}\n")
    
    # Atterrissage
    aeroport.autoriser_atterrissage()
    print(f"3. Pendant atterrissage: {avion}\n")
    
    # Finalisation atterrissage
    aeroport.finaliser_atterrissage("LH500")
    print(f"4. Après atterrissage: {avion}\n")
    
    # Décollage
    aeroport.autoriser_decollage("LH500")
    print(f"5. Pendant décollage: {avion}\n")
    
    # Finalisation décollage
    aeroport.finaliser_decollage("LH500")
    print(f"6. Après décollage: {avion}\n")
    
    aeroport.afficher_historique()


def exemple_gestion_file():
    """Exemple de gestion de file d'attente"""
    print("\n" + "="*60)
    print("EXEMPLE 3: Gestion de File d'Attente")
    print("="*60 + "\n")
    
    # Aéroport avec deux pistes pour éviter les blocages
    aeroport = Aeroport("Marseille", nombre_pistes=2)
    
    # Plusieurs avions arrivent
    avions = [
        Avion("AF300", "Air France"),
        Avion("RY400", "Ryanair"),
        Avion("VY500", "Vueling"),
    ]
    
    # Tous les avions signalent leur approche
    print("📥 Ajout de 3 avions en approche sur 2 pistes:")
    for avion in avions:
        aeroport.ajouter_avion_en_approche(avion)
    
    print(f"\n📊 File d'attente: {len(aeroport.avions_en_approche)} avions\n")
    
    # Traiter les deux premiers avions
    print("🔄 Traitement des deux premiers:\n")
    print("--- Cycle 1 ---")
    aeroport.autoriser_atterrissage()
    aeroport.autoriser_atterrissage()
    print()
    
    # Finaliser un atterrissage pour libérer une piste
    print("--- Cycle 2: Libération d'une piste ---")
    aeroport.finaliser_atterrissage("AF300")
    print()
    
    # Le troisième peut maintenant atterrir
    print("--- Cycle 3: Troisième atterrissage ---")
    aeroport.autoriser_atterrissage()
    print()
    
    # Finaliser tous les atterrissages
    aeroport.finaliser_atterrissage("RY400")
    aeroport.finaliser_atterrissage("VY500")
    
    print(f"✅ Tous les avions ont atterri!")
    print(f"📊 Avions au sol: {len(aeroport.avions_au_sol)}\n")


def exemple_avec_verification():
    """Exemple avec vérifications d'état"""
    print("\n" + "="*60)
    print("EXEMPLE 4: Vérifications et Gestion d'Erreurs")
    print("="*60 + "\n")
    
    aeroport = Aeroport("Nice", nombre_pistes=2)
    
    # Tentative d'atterrissage sans avion
    print("1. Tentative d'atterrissage sans avion:")
    resultat = aeroport.autoriser_atterrissage()
    print(f"   Résultat: {resultat}\n")
    
    # Ajouter un avion
    avion = Avion("AF700", "Air France")
    aeroport.ajouter_avion_en_approche(avion)
    
    # Atterrissage réussi
    print("2. Atterrissage avec un avion:")
    resultat = aeroport.autoriser_atterrissage()
    print(f"   Résultat: {resultat}\n")
    
    # Tentative de décollage d'un avion qui n'est pas au sol
    print("3. Tentative de décollage d'un avion inexistant:")
    resultat = aeroport.autoriser_decollage("XX999")
    print(f"   Résultat: {resultat}\n")
    
    # Finaliser l'atterrissage d'abord
    aeroport.finaliser_atterrissage("AF700")
    
    # Maintenant le décollage fonctionne
    print("4. Décollage après atterrissage:")
    resultat = aeroport.autoriser_decollage("AF700")
    print(f"   Résultat: {resultat}\n")


def exemple_statistiques():
    """Exemple avec statistiques"""
    print("\n" + "="*60)
    print("EXEMPLE 5: Statistiques de l'Aéroport")
    print("="*60 + "\n")
    
    aeroport = Aeroport("Lyon", nombre_pistes=3)
    
    # Simuler une journée chargée
    vols_arrivee = [
        ("AF100", "Air France"),
        ("LH200", "Lufthansa"),
        ("BA300", "British Airways"),
        ("EZ400", "EasyJet"),
        ("RY500", "Ryanair")
    ]
    
    # Tous arrivent
    for code, compagnie in vols_arrivee:
        aeroport.ajouter_avion_en_approche(Avion(code, compagnie))
    
    # Atterrissages des 3 premiers
    aeroport.autoriser_atterrissage()
    aeroport.autoriser_atterrissage()
    aeroport.autoriser_atterrissage()
    
    # Libérer toutes les pistes
    for piste in aeroport.pistes:
        if piste.avion_actuel:
            aeroport.finaliser_atterrissage(piste.avion_actuel.code_vol)
    
    # Atterrissages des 2 derniers
    aeroport.autoriser_atterrissage()
    aeroport.autoriser_atterrissage()
    
    # Libérer les pistes à nouveau
    for piste in aeroport.pistes:
        if piste.avion_actuel:
            aeroport.finaliser_atterrissage(piste.avion_actuel.code_vol)
    
    # Afficher les statistiques
    print(f"\n📊 STATISTIQUES DE LA JOURNÉE")
    print(f"   Nombre de pistes: {len(aeroport.pistes)}")
    print(f"   Avions au sol: {len(aeroport.avions_au_sol)}")
    print(f"   Événements enregistrés: {len(aeroport.historique)}")
    print(f"   Compagnies présentes: {len(set(a.compagnie for a in aeroport.avions_au_sol))}")
    
    # Détail par compagnie
    print("\n   Avions par compagnie:")
    compagnies = {}
    for avion in aeroport.avions_au_sol:
        compagnies[avion.compagnie] = compagnies.get(avion.compagnie, 0) + 1
    for compagnie, count in sorted(compagnies.items()):
        print(f"   - {compagnie}: {count} avion(s)")
    
    print()


def main():
    """Exécute tous les exemples"""
    exemple_simple()
    exemple_cycle_complet()
    exemple_gestion_file()
    exemple_avec_verification()
    exemple_statistiques()
    
    print("\n" + "="*60)
    print("✅ TOUS LES EXEMPLES ONT ÉTÉ EXÉCUTÉS")
    print("="*60)
    print("\nConsultez ces exemples pour comprendre comment utiliser l'API")
    print("dans vos propres programmes!\n")


if __name__ == "__main__":
    main()
