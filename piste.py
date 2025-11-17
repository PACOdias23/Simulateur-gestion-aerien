"""
Module Piste - Représente une piste d'atterrissage/décollage

LOGIQUE:
Une piste peut être:
- Disponible: Prête à accueillir un avion
- Occupée: Un avion est en train d'atterrir ou de décoller
- En maintenance: Temporairement indisponible

Chaque piste a un numéro unique et peut traiter les atterrissages et décollages
"""

from enum import Enum


class StatutPiste(Enum):
    """Énumération des différents statuts possibles d'une piste"""
    DISPONIBLE = "Disponible"
    OCCUPEE = "Occupée"
    MAINTENANCE = "En maintenance"


class Piste:
    """
    Classe représentant une piste d'atterrissage/décollage
    
    Attributes:
        numero (int): Numéro unique de la piste
        statut (StatutPiste): Statut actuel de la piste
        avion_actuel (Avion): Avion utilisant actuellement la piste (None si disponible)
    """
    
    def __init__(self, numero):
        """
        Initialise une nouvelle piste
        
        Args:
            numero (int): Numéro unique de la piste
        """
        self.numero = numero
        self.statut = StatutPiste.DISPONIBLE
        self.avion_actuel = None
    
    def est_disponible(self):
        """
        Vérifie si la piste est disponible
        
        Returns:
            bool: True si la piste est disponible, False sinon
        """
        return self.statut == StatutPiste.DISPONIBLE
    
    def occuper(self, avion):
        """
        Marque la piste comme occupée par un avion
        
        Args:
            avion (Avion): L'avion qui occupe la piste
            
        Returns:
            bool: True si l'occupation a réussi, False si la piste n'était pas disponible
        """
        if not self.est_disponible():
            return False
        
        self.statut = StatutPiste.OCCUPEE
        self.avion_actuel = avion
        return True
    
    def liberer(self):
        """
        Libère la piste (la rend disponible)
        
        Returns:
            Avion: L'avion qui occupait la piste, ou None
        """
        avion_precedent = self.avion_actuel
        self.statut = StatutPiste.DISPONIBLE
        self.avion_actuel = None
        return avion_precedent
    
    def mettre_en_maintenance(self):
        """Met la piste en maintenance"""
        if self.avion_actuel is None:
            self.statut = StatutPiste.MAINTENANCE
            return True
        return False
    
    def sortir_de_maintenance(self):
        """Sort la piste de maintenance"""
        if self.statut == StatutPiste.MAINTENANCE:
            self.statut = StatutPiste.DISPONIBLE
            return True
        return False
    
    def __str__(self):
        """Représentation textuelle de la piste"""
        avion_info = f"occupée par {self.avion_actuel.code_vol}" if self.avion_actuel else self.statut.value
        return f"Piste {self.numero}: {avion_info}"
    
    def __repr__(self):
        return f"Piste({self.numero})"
