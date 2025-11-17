"""
Test simple du simulateur de gestion aérienne
Ce fichier teste les fonctionnalités de base du système
"""

from avion import Avion, StatutAvion
from piste import Piste, StatutPiste
from aeroport import Aeroport


def test_avion():
    """Test de la classe Avion"""
    print("Test 1: Création d'un avion")
    avion = Avion("AF123", "Air France")
    assert avion.code_vol == "AF123"
    assert avion.compagnie == "Air France"
    assert avion.statut == StatutAvion.EN_APPROCHE
    assert avion.piste_assignee is None
    print("✅ Test avion réussi\n")


def test_piste():
    """Test de la classe Piste"""
    print("Test 2: Création et gestion d'une piste")
    piste = Piste(1)
    assert piste.numero == 1
    assert piste.est_disponible() == True
    assert piste.statut == StatutPiste.DISPONIBLE
    
    avion = Avion("AF123", "Air France")
    piste.occuper(avion)
    assert piste.est_disponible() == False
    assert piste.avion_actuel == avion
    
    piste.liberer()
    assert piste.est_disponible() == True
    assert piste.avion_actuel is None
    print("✅ Test piste réussi\n")


def test_aeroport_atterrissage():
    """Test du flux d'atterrissage"""
    print("Test 3: Flux d'atterrissage complet")
    aeroport = Aeroport("Test Airport", nombre_pistes=2)
    avion = Avion("AF123", "Air France")
    
    # Ajouter l'avion en approche
    aeroport.ajouter_avion_en_approche(avion)
    assert len(aeroport.avions_en_approche) == 1
    assert avion.statut == StatutAvion.EN_APPROCHE
    
    # Autoriser l'atterrissage
    result = aeroport.autoriser_atterrissage()
    assert result == True
    assert len(aeroport.avions_en_approche) == 0
    assert avion.statut == StatutAvion.ATTERRISSAGE
    assert avion.piste_assignee is not None
    
    # Finaliser l'atterrissage
    result = aeroport.finaliser_atterrissage("AF123")
    assert result == True
    assert len(aeroport.avions_au_sol) == 1
    assert avion.statut == StatutAvion.AU_SOL
    assert avion.piste_assignee is None
    print("✅ Test atterrissage réussi\n")


def test_aeroport_decollage():
    """Test du flux de décollage"""
    print("Test 4: Flux de décollage complet")
    aeroport = Aeroport("Test Airport", nombre_pistes=2)
    avion = Avion("AF123", "Air France")
    
    # Simuler un avion au sol
    avion.changer_statut(StatutAvion.AU_SOL)
    aeroport.avions_au_sol.append(avion)
    
    # Autoriser le décollage
    result = aeroport.autoriser_decollage("AF123")
    assert result == True
    assert len(aeroport.avions_au_sol) == 0
    assert avion.statut == StatutAvion.DECOLLAGE
    assert avion.piste_assignee is not None
    
    # Finaliser le décollage
    result = aeroport.finaliser_decollage("AF123")
    assert result == True
    assert avion.statut == StatutAvion.EN_VOL
    assert avion.piste_assignee is None
    print("✅ Test décollage réussi\n")


def test_saturation():
    """Test de la gestion de saturation"""
    print("Test 5: Gestion de la saturation (1 piste, 2 avions)")
    aeroport = Aeroport("Test Airport", nombre_pistes=1)
    
    avion1 = Avion("AF123", "Air France")
    avion2 = Avion("LH456", "Lufthansa")
    
    aeroport.ajouter_avion_en_approche(avion1)
    aeroport.ajouter_avion_en_approche(avion2)
    
    # Premier atterrissage réussit
    result = aeroport.autoriser_atterrissage()
    assert result == True
    assert avion1.statut == StatutAvion.ATTERRISSAGE
    
    # Deuxième atterrissage échoue (piste occupée)
    result = aeroport.autoriser_atterrissage()
    assert result == False
    assert avion2.statut == StatutAvion.EN_APPROCHE
    assert len(aeroport.avions_en_approche) == 1
    
    # Libérer la piste
    aeroport.finaliser_atterrissage("AF123")
    
    # Maintenant le deuxième peut atterrir
    result = aeroport.autoriser_atterrissage()
    assert result == True
    assert avion2.statut == StatutAvion.ATTERRISSAGE
    print("✅ Test saturation réussi\n")


def run_all_tests():
    """Exécute tous les tests"""
    print("="*60)
    print("TESTS DU SIMULATEUR DE GESTION AÉRIENNE")
    print("="*60 + "\n")
    
    test_avion()
    test_piste()
    test_aeroport_atterrissage()
    test_aeroport_decollage()
    test_saturation()
    
    print("="*60)
    print("✅ TOUS LES TESTS ONT RÉUSSI!")
    print("="*60)


if __name__ == "__main__":
    run_all_tests()
