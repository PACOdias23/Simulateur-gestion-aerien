"""
Module Avion - Représente un avion dans le système de gestion aérienne

LOGIQUE:
Un avion est caractérisé par:
- Un identifiant unique (code de vol)
- Une compagnie aérienne
- Un statut (EN_APPROCHE, ATTERRISSAGE, AU_SOL, DECOLLAGE, EN_VOL)
- Une piste assignée (si applicable)
"""

from enum import Enum


class StatutAvion(Enum):
    """Énumération des différents statuts possibles d'un avion"""
    EN_APPROCHE = "En approche"
    ATTERRISSAGE = "Atterrissage en cours"
    AU_SOL = "Au sol"
    DECOLLAGE = "Décollage en cours"
    EN_VOL = "En vol"


class Avion:
    """
    Classe représentant un avion
    
    Attributes:
        code_vol (str): Identifiant unique du vol (ex: "AF123")
        compagnie (str): Nom de la compagnie aérienne
        statut (StatutAvion): Statut actuel de l'avion
        piste_assignee (int): Numéro de piste assignée (None si aucune)
    """
    
    def __init__(self, code_vol, compagnie):
        """
        Initialise un nouvel avion
        
        Args:
            code_vol (str): Code de vol unique
            compagnie (str): Nom de la compagnie aérienne
        """
        self.code_vol = code_vol
        self.compagnie = compagnie
        self.statut = StatutAvion.EN_APPROCHE
        self.piste_assignee = None
    
    def changer_statut(self, nouveau_statut):
        """
        Change le statut de l'avion
        
        Args:
            nouveau_statut (StatutAvion): Nouveau statut à assigner
        """
        self.statut = nouveau_statut
    
    def assigner_piste(self, numero_piste):
        """
        Assigne une piste à l'avion
        
        Args:
            numero_piste (int): Numéro de la piste assignée
        """
        self.piste_assignee = numero_piste
    
    def liberer_piste(self):
        """Libère la piste assignée à l'avion"""
        self.piste_assignee = None
    
    def __str__(self):
        """Représentation textuelle de l'avion"""
        piste_info = f"Piste {self.piste_assignee}" if self.piste_assignee else "Aucune piste"
        return f"Vol {self.code_vol} ({self.compagnie}) - {self.statut.value} - {piste_info}"
    
    def __repr__(self):
        return f"Avion('{self.code_vol}', '{self.compagnie}')"
