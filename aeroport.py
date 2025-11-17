"""
Module Aeroport - Gère l'ensemble du système de contrôle aérien

LOGIQUE:
L'aéroport coordonne:
1. La gestion des pistes (disponibilité, attribution)
2. La file d'attente des avions en approche
3. Les avions au sol prêts à décoller
4. L'attribution intelligente des pistes aux avions

Algorithme d'attribution:
- Priorité aux atterrissages (sécurité)
- Attribution de la première piste disponible
- Gestion de la file d'attente si toutes les pistes sont occupées
"""

from avion import Avion, StatutAvion
from piste import Piste


class Aeroport:
    """
    Classe représentant un aéroport avec son système de contrôle aérien
    
    Attributes:
        nom (str): Nom de l'aéroport
        pistes (list): Liste des pistes disponibles
        avions_en_approche (list): File d'attente des avions en approche
        avions_au_sol (list): Liste des avions au sol
        historique (list): Historique des événements
    """
    
    def __init__(self, nom, nombre_pistes=3):
        """
        Initialise un nouvel aéroport
        
        Args:
            nom (str): Nom de l'aéroport
            nombre_pistes (int): Nombre de pistes à créer (défaut: 3)
        """
        self.nom = nom
        self.pistes = [Piste(i + 1) for i in range(nombre_pistes)]
        self.avions_en_approche = []
        self.avions_au_sol = []
        self.historique = []
    
    def ajouter_avion_en_approche(self, avion):
        """
        Ajoute un avion à la file d'attente des approches
        
        Args:
            avion (Avion): L'avion en approche
        """
        avion.changer_statut(StatutAvion.EN_APPROCHE)
        self.avions_en_approche.append(avion)
        message = f"✈️  Vol {avion.code_vol} signale son approche de l'aéroport"
        self.historique.append(message)
        print(message)
    
    def trouver_piste_disponible(self):
        """
        Trouve la première piste disponible
        
        Returns:
            Piste: Une piste disponible, ou None si aucune n'est disponible
        """
        for piste in self.pistes:
            if piste.est_disponible():
                return piste
        return None
    
    def autoriser_atterrissage(self):
        """
        Autorise l'atterrissage du prochain avion en approche
        
        LOGIQUE:
        1. Vérifie s'il y a des avions en attente
        2. Cherche une piste disponible
        3. Assigne la piste et change le statut de l'avion
        4. Simule l'atterrissage
        
        Returns:
            bool: True si un atterrissage a été autorisé, False sinon
        """
        if not self.avions_en_approche:
            print("⚠️  Aucun avion en approche")
            return False
        
        piste = self.trouver_piste_disponible()
        if not piste:
            print("⚠️  Toutes les pistes sont occupées, l'avion doit patienter")
            return False
        
        # Traiter le premier avion en attente
        avion = self.avions_en_approche.pop(0)
        avion.changer_statut(StatutAvion.ATTERRISSAGE)
        avion.assigner_piste(piste.numero)
        piste.occuper(avion)
        
        message = f"🛬 Vol {avion.code_vol} autorisé à atterrir sur piste {piste.numero}"
        self.historique.append(message)
        print(message)
        
        return True
    
    def finaliser_atterrissage(self, code_vol):
        """
        Finalise l'atterrissage d'un avion (libère la piste, avion au sol)
        
        Args:
            code_vol (str): Code du vol à finaliser
            
        Returns:
            bool: True si la finalisation a réussi, False sinon
        """
        # Chercher l'avion sur une piste
        for piste in self.pistes:
            if (piste.avion_actuel and 
                piste.avion_actuel.code_vol == code_vol and
                piste.avion_actuel.statut == StatutAvion.ATTERRISSAGE):
                
                avion = piste.liberer()
                avion.changer_statut(StatutAvion.AU_SOL)
                avion.liberer_piste()
                self.avions_au_sol.append(avion)
                
                message = f"✅ Vol {code_vol} a atterri et libéré la piste {piste.numero}"
                self.historique.append(message)
                print(message)
                return True
        
        print(f"⚠️  Vol {code_vol} non trouvé en phase d'atterrissage")
        return False
    
    def autoriser_decollage(self, code_vol):
        """
        Autorise le décollage d'un avion au sol
        
        Args:
            code_vol (str): Code du vol à faire décoller
            
        Returns:
            bool: True si le décollage a été autorisé, False sinon
        """
        # Chercher l'avion au sol
        avion = None
        for a in self.avions_au_sol:
            if a.code_vol == code_vol:
                avion = a
                break
        
        if not avion:
            print(f"⚠️  Vol {code_vol} non trouvé au sol")
            return False
        
        # Chercher une piste disponible
        piste = self.trouver_piste_disponible()
        if not piste:
            print("⚠️  Toutes les pistes sont occupées, impossible de décoller maintenant")
            return False
        
        # Autoriser le décollage
        self.avions_au_sol.remove(avion)
        avion.changer_statut(StatutAvion.DECOLLAGE)
        avion.assigner_piste(piste.numero)
        piste.occuper(avion)
        
        message = f"🛫 Vol {code_vol} autorisé à décoller de la piste {piste.numero}"
        self.historique.append(message)
        print(message)
        
        return True
    
    def finaliser_decollage(self, code_vol):
        """
        Finalise le décollage d'un avion (libère la piste, avion en vol)
        
        Args:
            code_vol (str): Code du vol à finaliser
            
        Returns:
            bool: True si la finalisation a réussi, False sinon
        """
        # Chercher l'avion sur une piste
        for piste in self.pistes:
            if (piste.avion_actuel and 
                piste.avion_actuel.code_vol == code_vol and
                piste.avion_actuel.statut == StatutAvion.DECOLLAGE):
                
                avion = piste.liberer()
                avion.changer_statut(StatutAvion.EN_VOL)
                avion.liberer_piste()
                
                message = f"✅ Vol {code_vol} a décollé et libéré la piste {piste.numero}"
                self.historique.append(message)
                print(message)
                return True
        
        print(f"⚠️  Vol {code_vol} non trouvé en phase de décollage")
        return False
    
    def afficher_etat(self):
        """Affiche l'état complet de l'aéroport"""
        print("\n" + "="*60)
        print(f"📍 AÉROPORT: {self.nom}")
        print("="*60)
        
        print("\n🛫 PISTES:")
        for piste in self.pistes:
            print(f"  {piste}")
        
        print(f"\n✈️  AVIONS EN APPROCHE ({len(self.avions_en_approche)}):")
        if self.avions_en_approche:
            for avion in self.avions_en_approche:
                print(f"  - {avion}")
        else:
            print("  Aucun")
        
        print(f"\n🅿️  AVIONS AU SOL ({len(self.avions_au_sol)}):")
        if self.avions_au_sol:
            for avion in self.avions_au_sol:
                print(f"  - {avion}")
        else:
            print("  Aucun")
        
        print("="*60 + "\n")
    
    def afficher_historique(self):
        """Affiche l'historique des événements"""
        print("\n" + "="*60)
        print("📋 HISTORIQUE DES ÉVÉNEMENTS")
        print("="*60)
        for i, event in enumerate(self.historique, 1):
            print(f"{i}. {event}")
        print("="*60 + "\n")
