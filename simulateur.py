"""
Simulateur de Gestion Aérienne - Programme Principal

LOGIQUE DU SYSTÈME:

1. ARCHITECTURE:
   - Avion: Représente un avion avec son état (en approche, atterrissage, au sol, décollage)
   - Piste: Gère la disponibilité et l'occupation des pistes
   - Aéroport: Coordonne l'ensemble du trafic aérien

2. FLUX D'ATTERRISSAGE:
   a) Un avion signale son approche → ajouté à la file d'attente
   b) Le contrôle aérien assigne une piste disponible
   c) L'avion atterrit → occupe temporairement la piste
   d) L'avion libère la piste → se positionne au sol

3. FLUX DE DÉCOLLAGE:
   a) Un avion au sol demande à décoller
   b) Le contrôle aérien assigne une piste disponible
   c) L'avion décolle → occupe temporairement la piste
   d) L'avion libère la piste → quitte l'espace aérien

4. PRIORITÉS:
   - Les atterrissages sont prioritaires (sécurité)
   - File d'attente FIFO (First In, First Out)
   - Attribution automatique des pistes disponibles

Ce programme démontre le fonctionnement du système avec plusieurs scénarios.
"""

from avion import Avion
from aeroport import Aeroport
import time


def pause(secondes=1):
    """Pause pour simuler le temps réel"""
    time.sleep(secondes)


def demonstration_simple():
    """Démonstration simple du système"""
    print("\n" + "🌟"*30)
    print("DÉMONSTRATION SIMPLE DU SIMULATEUR DE GESTION AÉRIENNE")
    print("🌟"*30 + "\n")
    
    # Créer l'aéroport
    aeroport = Aeroport("Charles de Gaulle", nombre_pistes=2)
    print(f"✅ Aéroport '{aeroport.nom}' créé avec {len(aeroport.pistes)} pistes\n")
    pause()
    
    # Créer des avions
    avion1 = Avion("AF123", "Air France")
    avion2 = Avion("LH456", "Lufthansa")
    avion3 = Avion("BA789", "British Airways")
    
    print("📝 SCÉNARIO 1: Arrivée de plusieurs avions")
    print("-" * 50)
    aeroport.ajouter_avion_en_approche(avion1)
    pause()
    aeroport.ajouter_avion_en_approche(avion2)
    pause()
    aeroport.ajouter_avion_en_approche(avion3)
    pause()
    
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 SCÉNARIO 2: Atterrissages")
    print("-" * 50)
    aeroport.autoriser_atterrissage()  # AF123
    pause()
    aeroport.autoriser_atterrissage()  # LH456
    pause()
    
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 SCÉNARIO 3: Finalisation des atterrissages")
    print("-" * 50)
    aeroport.finaliser_atterrissage("AF123")
    pause()
    aeroport.finaliser_atterrissage("LH456")
    pause()
    
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 SCÉNARIO 4: Troisième atterrissage")
    print("-" * 50)
    aeroport.autoriser_atterrissage()  # BA789
    pause()
    aeroport.finaliser_atterrissage("BA789")
    pause()
    
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 SCÉNARIO 5: Décollages")
    print("-" * 50)
    aeroport.autoriser_decollage("AF123")
    pause()
    aeroport.finaliser_decollage("AF123")
    pause()
    
    aeroport.autoriser_decollage("LH456")
    pause()
    aeroport.finaliser_decollage("LH456")
    pause()
    
    aeroport.afficher_etat()
    pause(2)
    
    # Afficher l'historique complet
    aeroport.afficher_historique()


def demonstration_complexe():
    """Démonstration avec gestion de la saturation des pistes"""
    print("\n" + "🌟"*30)
    print("DÉMONSTRATION AVANCÉE: GESTION DE LA SATURATION")
    print("🌟"*30 + "\n")
    
    # Aéroport avec une seule piste
    aeroport = Aeroport("Orly", nombre_pistes=1)
    print(f"✅ Aéroport '{aeroport.nom}' créé avec {len(aeroport.pistes)} piste\n")
    pause()
    
    # Plusieurs avions arrivent simultanément
    avions = [
        Avion("EZ101", "EasyJet"),
        Avion("RY202", "Ryanair"),
        Avion("VY303", "Vueling")
    ]
    
    print("📝 SCÉNARIO: Saturation avec une seule piste")
    print("-" * 50)
    
    for avion in avions:
        aeroport.ajouter_avion_en_approche(avion)
        pause()
    
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 Premier avion atterrit (piste occupée)")
    print("-" * 50)
    aeroport.autoriser_atterrissage()
    pause()
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 Tentative d'atterrissage du deuxième (piste occupée)")
    print("-" * 50)
    aeroport.autoriser_atterrissage()  # Va échouer car piste occupée
    pause()
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 Libération de la piste")
    print("-" * 50)
    aeroport.finaliser_atterrissage("EZ101")
    pause()
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 Maintenant le deuxième peut atterrir")
    print("-" * 50)
    aeroport.autoriser_atterrissage()
    pause()
    aeroport.finaliser_atterrissage("RY202")
    pause()
    aeroport.afficher_etat()
    pause(2)
    
    print("\n📝 Et le troisième")
    print("-" * 50)
    aeroport.autoriser_atterrissage()
    pause()
    aeroport.finaliser_atterrissage("VY303")
    pause()
    
    aeroport.afficher_etat()
    aeroport.afficher_historique()


def menu_interactif():
    """Menu interactif pour contrôler manuellement l'aéroport"""
    print("\n" + "🌟"*30)
    print("MODE INTERACTIF - CONTRÔLE AÉRIEN")
    print("🌟"*30 + "\n")
    
    nom_aeroport = input("Nom de l'aéroport: ").strip() or "Mon Aéroport"
    try:
        nb_pistes = int(input("Nombre de pistes (1-5): ").strip() or "3")
        nb_pistes = max(1, min(5, nb_pistes))
    except ValueError:
        nb_pistes = 3
    
    aeroport = Aeroport(nom_aeroport, nb_pistes)
    print(f"\n✅ Aéroport '{aeroport.nom}' créé avec {len(aeroport.pistes)} piste(s)\n")
    
    while True:
        print("\n" + "="*60)
        print("MENU PRINCIPAL")
        print("="*60)
        print("1. Ajouter un avion en approche")
        print("2. Autoriser un atterrissage")
        print("3. Finaliser un atterrissage")
        print("4. Autoriser un décollage")
        print("5. Finaliser un décollage")
        print("6. Afficher l'état de l'aéroport")
        print("7. Afficher l'historique")
        print("8. Quitter")
        print("="*60)
        
        choix = input("\nVotre choix (1-8): ").strip()
        
        if choix == "1":
            code_vol = input("Code du vol (ex: AF123): ").strip().upper()
            compagnie = input("Compagnie aérienne: ").strip()
            if code_vol and compagnie:
                avion = Avion(code_vol, compagnie)
                aeroport.ajouter_avion_en_approche(avion)
            else:
                print("⚠️  Code de vol et compagnie requis")
        
        elif choix == "2":
            aeroport.autoriser_atterrissage()
        
        elif choix == "3":
            code_vol = input("Code du vol à finaliser: ").strip().upper()
            if code_vol:
                aeroport.finaliser_atterrissage(code_vol)
            else:
                print("⚠️  Code de vol requis")
        
        elif choix == "4":
            code_vol = input("Code du vol à faire décoller: ").strip().upper()
            if code_vol:
                aeroport.autoriser_decollage(code_vol)
            else:
                print("⚠️  Code de vol requis")
        
        elif choix == "5":
            code_vol = input("Code du vol à finaliser: ").strip().upper()
            if code_vol:
                aeroport.finaliser_decollage(code_vol)
            else:
                print("⚠️  Code de vol requis")
        
        elif choix == "6":
            aeroport.afficher_etat()
        
        elif choix == "7":
            aeroport.afficher_historique()
        
        elif choix == "8":
            print("\n👋 Au revoir! Merci d'avoir utilisé le simulateur.\n")
            break
        
        else:
            print("⚠️  Choix invalide, veuillez réessayer")


def main():
    """Programme principal"""
    print("\n" + "✈️ "*30)
    print(" "*20 + "SIMULATEUR DE GESTION AÉRIENNE")
    print("✈️ "*30 + "\n")
    
    print("Choisissez un mode:")
    print("1. Démonstration simple")
    print("2. Démonstration avancée (saturation)")
    print("3. Mode interactif")
    print("4. Toutes les démonstrations")
    
    choix = input("\nVotre choix (1-4): ").strip()
    
    if choix == "1":
        demonstration_simple()
    elif choix == "2":
        demonstration_complexe()
    elif choix == "3":
        menu_interactif()
    elif choix == "4":
        demonstration_simple()
        pause(3)
        demonstration_complexe()
        pause(3)
        reponse = input("\nVoulez-vous essayer le mode interactif? (o/n): ").strip().lower()
        if reponse == "o":
            menu_interactif()
    else:
        print("Choix invalide, lancement de la démonstration simple...")
        demonstration_simple()


if __name__ == "__main__":
    main()
