# Documentation Technique - Simulateur de Gestion Aérienne

## 📋 Vue d'ensemble

Ce logiciel simule le système d'aiguillage aérien d'un aéroport. Il gère l'arrivée, l'atterrissage, le stationnement et le décollage des avions en respectant les contraintes de disponibilité des pistes.

## 🏗️ Architecture du Système

### 1. Module `avion.py` - Classe Avion

**Rôle**: Représente un avion dans le système

**Attributs**:
- `code_vol`: Identifiant unique (ex: "AF123")
- `compagnie`: Nom de la compagnie aérienne
- `statut`: État actuel de l'avion (énumération StatutAvion)
- `piste_assignee`: Numéro de piste attribuée ou None

**États possibles** (StatutAvion):
1. `EN_APPROCHE`: L'avion approche de l'aéroport
2. `ATTERRISSAGE`: L'avion est en train d'atterrir
3. `AU_SOL`: L'avion est garé au sol
4. `DECOLLAGE`: L'avion est en train de décoller
5. `EN_VOL`: L'avion a quitté l'espace aérien

**Méthodes principales**:
- `changer_statut()`: Change l'état de l'avion
- `assigner_piste()`: Attribue une piste à l'avion
- `liberer_piste()`: Libère la piste utilisée

### 2. Module `piste.py` - Classe Piste

**Rôle**: Gère une piste d'atterrissage/décollage

**Attributs**:
- `numero`: Identifiant unique de la piste
- `statut`: État de la piste (énumération StatutPiste)
- `avion_actuel`: Référence à l'avion qui occupe la piste

**États possibles** (StatutPiste):
1. `DISPONIBLE`: Prête à être utilisée
2. `OCCUPEE`: Un avion l'utilise actuellement
3. `MAINTENANCE`: Temporairement hors service

**Méthodes principales**:
- `est_disponible()`: Vérifie la disponibilité
- `occuper()`: Marque la piste comme occupée
- `liberer()`: Libère la piste pour un autre avion
- `mettre_en_maintenance()`: Désactive la piste
- `sortir_de_maintenance()`: Réactive la piste

### 3. Module `aeroport.py` - Classe Aeroport

**Rôle**: Cœur du système, coordonne tout le trafic aérien

**Attributs**:
- `nom`: Nom de l'aéroport
- `pistes`: Liste des pistes disponibles
- `avions_en_approche`: File d'attente FIFO des avions en approche
- `avions_au_sol`: Liste des avions garés
- `historique`: Journal de tous les événements

**Méthodes principales**:
- `ajouter_avion_en_approche()`: Enregistre un nouvel avion
- `autoriser_atterrissage()`: Attribue une piste et autorise l'atterrissage
- `finaliser_atterrissage()`: Libère la piste après atterrissage
- `autoriser_decollage()`: Attribue une piste et autorise le décollage
- `finaliser_decollage()`: Libère la piste après décollage
- `afficher_etat()`: Montre l'état en temps réel
- `afficher_historique()`: Affiche le journal des événements

## 🔄 Logique des Flux

### Flux d'Atterrissage

```
1. Avion en approche
   ↓
2. Ajout à la file d'attente (avions_en_approche)
   ↓
3. [Attente si nécessaire]
   ↓
4. Piste disponible trouvée
   ↓
5. Attribution de la piste + changement statut → ATTERRISSAGE
   ↓
6. Occupation de la piste
   ↓
7. Finalisation: libération piste + changement statut → AU_SOL
   ↓
8. Ajout à la liste des avions au sol
```

### Flux de Décollage

```
1. Avion au sol prêt à décoller
   ↓
2. Recherche de piste disponible
   ↓
3. [Attente si nécessaire]
   ↓
4. Attribution de la piste + changement statut → DECOLLAGE
   ↓
5. Occupation de la piste
   ↓
6. Finalisation: libération piste + changement statut → EN_VOL
   ↓
7. Avion quitte le système
```

## 🎯 Algorithmes Clés

### 1. Attribution de Piste

```python
def trouver_piste_disponible():
    # Parcourt toutes les pistes dans l'ordre
    for piste in self.pistes:
        if piste.est_disponible():
            return piste  # Retourne la première disponible
    return None  # Aucune piste disponible
```

**Logique**: 
- Recherche séquentielle simple
- Première piste disponible est attribuée
- Si aucune piste disponible → mise en file d'attente

### 2. Gestion de la File d'Attente

**Principe FIFO** (First In, First Out):
```python
avion = self.avions_en_approche.pop(0)  # Premier arrivé, premier servi
```

**Priorités**:
1. Les avions en approche ont priorité sur les décollages (sécurité)
2. Traitement dans l'ordre d'arrivée
3. Blocage si toutes les pistes sont occupées

### 3. Gestion de la Saturation

**Cas 1: Plus d'avions que de pistes**
- Les avions supplémentaires restent en file d'attente
- Traitement séquentiel au fur et à mesure que les pistes se libèrent

**Cas 2: Piste occupée**
- Tentative d'atterrissage → échec avec message d'attente
- L'avion reste en tête de file pour le prochain cycle

## 💡 Concepts de Programmation Utilisés

### 1. Programmation Orientée Objet (POO)

**Encapsulation**: Chaque classe gère ses propres données
```python
class Avion:
    def __init__(self, code_vol, compagnie):
        self.code_vol = code_vol  # Données privées
        self.compagnie = compagnie
```

**Héritage**: Utilisation d'énumérations pour les états
```python
class StatutAvion(Enum):
    EN_APPROCHE = "En approche"
    ATTERRISSAGE = "Atterrissage en cours"
```

### 2. Structures de Données

**Listes**: Pour les collections d'objets
```python
self.pistes = [Piste(i + 1) for i in range(nombre_pistes)]  # Compréhension de liste
self.avions_en_approche = []  # File d'attente
```

**Files FIFO**: Avec les opérations de liste Python
```python
self.avions_en_approche.append(avion)  # Ajout en fin
avion = self.avions_en_approche.pop(0)  # Retrait en début
```

### 3. Gestion d'État

**Machine à états**: Chaque avion et piste a un état
```python
avion.changer_statut(StatutAvion.ATTERRISSAGE)
piste.statut = StatutPiste.OCCUPEE
```

### 4. Association d'Objets

**Relations entre objets**:
- Un avion → a une piste (association)
- Une piste → peut avoir un avion (composition)
- Un aéroport → a plusieurs pistes et avions (agrégation)

## 🚀 Utilisation

### Exécution du programme

```bash
python simulateur.py
```

### Modes disponibles

1. **Démonstration simple**: Scénario prédéfini avec commentaires
2. **Démonstration avancée**: Montre la gestion de saturation
3. **Mode interactif**: Contrôle manuel complet
4. **Toutes les démonstrations**: Exécute tout en séquence

### Exemple d'utilisation interactive

```
Menu Principal:
1. Ajouter un avion en approche
   → Saisir: code vol (AF123), compagnie (Air France)

2. Autoriser un atterrissage
   → Le système assigne automatiquement une piste

3. Finaliser un atterrissage
   → Libère la piste, avion passe au sol

4. Autoriser un décollage
   → Nécessite un avion au sol

5. Finaliser un décollage
   → Libère la piste, avion quitte le système
```

## 🎓 Concepts Pédagogiques

### Pour les débutants

Ce projet illustre:
1. **Variables et types**: Utilisation de chaînes, entiers, booléens
2. **Conditions**: `if/else` pour la logique de décision
3. **Boucles**: `for` pour parcourir les listes
4. **Fonctions**: Organisation du code en blocs réutilisables
5. **Classes**: Regroupement de données et comportements

### Pour les intermédiaires

1. **Énumérations**: Gestion d'états typés et sûrs
2. **Listes et files**: Structures de données adaptées
3. **Gestion d'erreurs**: Retours booléens et messages
4. **Design patterns**: État, File d'attente
5. **Documentation**: Docstrings et commentaires explicatifs

## 🔧 Extensions Possibles

1. **Système de notation de priorité**: Urgences médicales, retards
2. **Météo**: Impact sur les atterrissages/décollages
3. **Temps simulé**: Horloge pour planification avancée
4. **Interface graphique**: Visualisation des pistes et avions
5. **Statistiques**: Temps d'attente, utilisation des pistes
6. **Sauvegarde/Chargement**: Persistance des données
7. **Multi-aéroports**: Transferts entre aéroports
8. **Zones de parking**: Gestion des aires de stationnement

## 📊 Diagramme de Flux Simplifié

```
                    SYSTÈME D'AIGUILLAGE AÉRIEN
                              
    [Avion en approche] ──┐
                          │
    [Avion en approche] ──┼──→ [File d'attente]
                          │            │
    [Avion en approche] ──┘            ↓
                                 [Recherche piste]
                                       │
                        ┌──────────────┴──────────────┐
                        ↓                             ↓
                  [Piste libre]                [Piste occupée]
                        │                             │
                        ↓                             ↓
              [Attribution piste]              [Rester en file]
                        │
                        ↓
                 [Atterrissage] ──→ [Libération] ──→ [Au sol]
                                                          │
                                                          ↓
                                                   [Demande décollage]
                                                          │
                                                          ↓
                                                   [Attribution piste]
                                                          │
                                                          ↓
                                                      [Décollage]
                                                          │
                                                          ↓
                                                   [Libération piste]
                                                          │
                                                          ↓
                                                       [En vol]
```

## 📝 Bonnes Pratiques Implémentées

1. **Code lisible**: Noms de variables explicites en français
2. **Documentation**: Docstrings pour toutes les classes et méthodes
3. **Modularité**: Séparation en modules logiques
4. **Feedback utilisateur**: Messages clairs avec émojis
5. **Gestion d'erreurs**: Vérifications et retours appropriés
6. **Principe DRY**: Pas de répétition de code
7. **Responsabilité unique**: Chaque classe a un rôle clair

## 🐛 Gestion des Cas Limites

1. **Aucun avion en approche**: Message informatif
2. **Toutes pistes occupées**: Mise en file d'attente
3. **Vol inexistant**: Message d'erreur approprié
4. **Piste déjà occupée**: Vérification avant attribution
5. **Entrées utilisateur invalides**: Valeurs par défaut et validation
