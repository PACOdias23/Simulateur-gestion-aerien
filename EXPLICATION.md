# 📚 Guide d'Explication - Logique du Simulateur

## Introduction

Ce document explique en détail comment fonctionne le simulateur de gestion aérienne, étape par étape, pour que vous puissiez comprendre la logique complète du système.

## 🧩 Les Composants de Base

### 1. L'Avion (avion.py)

Un avion est comme une entité qui se déplace dans le système. Pensez à un avion réel:

**Qu'est-ce qui caractérise un avion?**
- Un numéro de vol unique (ex: "AF123")
- Le nom de la compagnie aérienne
- Son état actuel (où il est et ce qu'il fait)
- La piste qu'il utilise (si applicable)

**Les différents états d'un avion:**

```
EN_APPROCHE → ATTERRISSAGE → AU_SOL → DECOLLAGE → EN_VOL
```

1. **EN_APPROCHE**: L'avion se rapproche de l'aéroport
2. **ATTERRISSAGE**: L'avion est en train d'atterrir sur une piste
3. **AU_SOL**: L'avion est garé à l'aéroport
4. **DECOLLAGE**: L'avion est en train de décoller d'une piste
5. **EN_VOL**: L'avion a quitté l'aéroport

**Pourquoi ces états sont importants?**
- Ils permettent de savoir exactement où est chaque avion
- Ils empêchent les erreurs (ex: on ne peut pas faire décoller un avion qui est déjà en vol)
- Ils facilitent la coordination entre les avions

### 2. La Piste (piste.py)

Une piste est une ressource partagée que les avions doivent utiliser.

**Analogie**: Pensez à une piste comme à une caisse de supermarché:
- Elle peut être libre (disponible)
- Elle peut être occupée par un client (un avion)
- Elle peut être fermée pour maintenance

**Les états d'une piste:**

1. **DISPONIBLE**: La piste est libre, prête à accueillir un avion
2. **OCCUPEE**: Un avion utilise actuellement la piste
3. **MAINTENANCE**: La piste est temporairement hors service

**Règle importante**: Une seule piste ne peut être utilisée que par un seul avion à la fois!

### 3. L'Aéroport (aeroport.py)

L'aéroport est le "chef d'orchestre" qui coordonne tout. C'est le système de contrôle aérien.

**Responsabilités de l'aéroport:**

1. **Gérer les pistes**: Savoir quelles pistes sont disponibles
2. **Gérer la file d'attente**: Organiser les avions qui attendent d'atterrir
3. **Autoriser les atterrissages**: Décider quand un avion peut atterrir
4. **Autoriser les décollages**: Décider quand un avion peut décoller
5. **Tenir l'historique**: Enregistrer tous les événements

## 🔄 Comment Ça Marche: Les Flux

### Flux 1: Un Avion Arrive et Atterrit

Imaginons que le vol AF123 d'Air France arrive:

**Étape 1: Signaler l'approche**
```python
avion = Avion("AF123", "Air France")
aeroport.ajouter_avion_en_approche(avion)
```
- L'avion informe l'aéroport qu'il arrive
- Il est ajouté à la file d'attente
- Son statut devient "EN_APPROCHE"

**Étape 2: Attendre son tour**
- L'avion reste dans la file d'attente
- Le contrôle aérien traite les avions dans l'ordre (premier arrivé, premier servi)

**Étape 3: Autorisation d'atterrissage**
```python
aeroport.autoriser_atterrissage()
```

Le système fait plusieurs vérifications:
1. Y a-t-il des avions en attente? → Oui, AF123
2. Y a-t-il une piste disponible? → Oui, piste 1
3. Attribution: AF123 → Piste 1
4. Changement de statut: AF123 → ATTERRISSAGE

**Étape 4: Atterrissage**
- L'avion occupe la piste pendant qu'il atterrit
- Pendant ce temps, aucun autre avion ne peut utiliser cette piste

**Étape 5: Finalisation**
```python
aeroport.finaliser_atterrissage("AF123")
```
1. L'avion libère la piste
2. La piste redevient disponible
3. L'avion change de statut: AU_SOL
4. L'avion est ajouté à la liste des avions au sol

### Flux 2: Un Avion Décolle

Maintenant, AF123 veut repartir:

**Étape 1: Demande de décollage**
```python
aeroport.autoriser_decollage("AF123")
```

Vérifications:
1. L'avion est-il bien au sol? → Oui
2. Y a-t-il une piste disponible? → Oui, piste 2
3. Attribution: AF123 → Piste 2
4. Changement de statut: AF123 → DECOLLAGE

**Étape 2: Décollage**
- L'avion occupe la piste pour décoller
- La piste est marquée comme occupée

**Étape 3: Finalisation**
```python
aeroport.finaliser_decollage("AF123")
```
1. L'avion libère la piste
2. La piste redevient disponible
3. L'avion change de statut: EN_VOL
4. L'avion quitte le système

## 🚦 Gestion des Contraintes

### Problème 1: Plus d'Avions Que de Pistes

**Situation**: 3 avions veulent atterrir, mais il n'y a que 2 pistes

**Solution du système:**

```
File d'attente: [AF123, LH456, BA789]
Pistes disponibles: [1, 2]

1. AF123 → Piste 1 (autorisé)
2. LH456 → Piste 2 (autorisé)
3. BA789 → Aucune piste! (en attente)

Quand AF123 libère la piste 1:
4. BA789 → Piste 1 (autorisé)
```

**Principe**: File d'attente FIFO (First In, First Out = Premier entré, premier sorti)

### Problème 2: Atterrissage vs Décollage

**Question**: Si un avion veut atterrir ET un avion veut décoller, qui a la priorité?

**Réponse**: L'atterrissage a toujours la priorité!

**Raison**: Pour des raisons de sécurité, un avion en vol qui doit atterrir est prioritaire sur un avion au sol qui peut attendre.

## 💻 Concepts de Programmation Expliqués

### 1. Les Classes (Programmation Orientée Objet)

**Qu'est-ce qu'une classe?**
C'est comme un modèle pour créer des objets.

**Analogie**: Une classe est comme un plan de construction:
- Le plan de maison (classe) → Les maisons construites (objets)
- La classe "Avion" → Les avions AF123, LH456, etc.

```python
class Avion:
    def __init__(self, code_vol, compagnie):
        self.code_vol = code_vol
        self.compagnie = compagnie
```

### 2. Les Énumérations (Enum)

**Pourquoi utiliser des énumérations?**

Au lieu de faire:
```python
statut = "en approche"  # Risque de faute de frappe!
```

On fait:
```python
statut = StatutAvion.EN_APPROCHE  # Pas d'erreur possible!
```

**Avantages**:
- Pas de fautes de frappe
- L'éditeur suggère les options
- Le code est plus clair

### 3. Les Listes et Files d'Attente

**Liste simple**:
```python
avions_au_sol = []  # Liste vide
avions_au_sol.append(avion)  # Ajouter à la fin
```

**File d'attente (FIFO)**:
```python
avions_en_approche = []
avions_en_approche.append(avion)  # Ajouter à la fin
premier = avions_en_approche.pop(0)  # Retirer du début
```

**Visualisation**:
```
Ajouter:        [AF123] ← LH456 ← BA789
Retirer: AF123 → [LH456, BA789]
```

### 4. Méthodes et Encapsulation

**Encapsulation** = Cacher la complexité à l'intérieur

```python
# Au lieu de manipuler directement:
avion.statut = "atterrissage"  # Risqué!

# On utilise une méthode:
avion.changer_statut(StatutAvion.ATTERRISSAGE)  # Sûr!
```

## 🎯 Algorithme Principal: Attribution de Piste

Voici l'algorithme détaillé pour autoriser un atterrissage:

```
FONCTION autoriser_atterrissage():
    
    1. Vérifier s'il y a des avions en attente
       SI file_attente est vide ALORS
           Retourner "Aucun avion"
       
    2. Chercher une piste disponible
       POUR CHAQUE piste FAIRE
           SI piste.est_disponible() ALORS
               piste_trouvee = cette piste
               SORTIR de la boucle
       
    3. Si aucune piste disponible
       SI piste_trouvee est None ALORS
           Retourner "Toutes les pistes occupées"
    
    4. Attribuer la piste
       avion = retirer le premier de la file
       avion.statut = ATTERRISSAGE
       avion.piste_assignee = piste_trouvee.numero
       piste_trouvee.occuper(avion)
       
    5. Enregistrer dans l'historique
       Ajouter "Vol X autorisé sur piste Y"
       
    6. Retourner Succès
```

## 🔍 Exemple Complet Pas à Pas

Simulons un scénario complet:

### État Initial
```
Aéroport: Charles de Gaulle
Pistes: [Piste 1: Disponible, Piste 2: Disponible]
Avions en approche: []
Avions au sol: []
```

### Action 1: Trois avions arrivent
```python
aeroport.ajouter_avion_en_approche(Avion("AF123", "Air France"))
aeroport.ajouter_avion_en_approche(Avion("LH456", "Lufthansa"))
aeroport.ajouter_avion_en_approche(Avion("BA789", "British Airways"))
```

**État après:**
```
Avions en approche: [AF123, LH456, BA789]
```

### Action 2: Premier atterrissage
```python
aeroport.autoriser_atterrissage()  # AF123
```

**Processus:**
1. File d'attente: [AF123, LH456, BA789]
2. Retirer AF123 → File: [LH456, BA789]
3. Trouver piste disponible → Piste 1
4. AF123 → Piste 1

**État après:**
```
Piste 1: Occupée par AF123
Avions en approche: [LH456, BA789]
```

### Action 3: Deuxième atterrissage
```python
aeroport.autoriser_atterrissage()  # LH456
```

**État après:**
```
Piste 1: Occupée par AF123
Piste 2: Occupée par LH456
Avions en approche: [BA789]
```

### Action 4: Tentative de troisième atterrissage
```python
aeroport.autoriser_atterrissage()  # BA789
```

**Résultat:** Échec! Toutes les pistes sont occupées
**État:** BA789 reste en attente

### Action 5: AF123 termine son atterrissage
```python
aeroport.finaliser_atterrissage("AF123")
```

**État après:**
```
Piste 1: Disponible
Piste 2: Occupée par LH456
Avions au sol: [AF123]
```

### Action 6: Maintenant BA789 peut atterrir
```python
aeroport.autoriser_atterrissage()  # BA789
```

**État final:**
```
Piste 1: Occupée par BA789
Piste 2: Occupée par LH456
Avions en approche: []
Avions au sol: [AF123]
```

## 📊 Diagramme de Décision

```
┌─────────────────────────────┐
│ Avion demande à atterrir    │
└──────────┬──────────────────┘
           │
           ▼
    ┌──────────────┐
    │ Y a-t-il une │      Non     ┌──────────────────┐
    │ piste libre? ├─────────────→│ Mise en attente  │
    └──────┬───────┘              └──────────────────┘
           │ Oui
           ▼
    ┌──────────────┐
    │ Attribution  │
    │ de piste     │
    └──────┬───────┘
           │
           ▼
    ┌──────────────┐
    │ Atterrissage │
    └──────┬───────┘
           │
           ▼
    ┌──────────────┐
    │ Libération   │
    │ de piste     │
    └──────┬───────┘
           │
           ▼
    ┌──────────────┐
    │ Avion au sol │
    └──────────────┘
```

## 🎓 Ce Que Vous Apprenez

En étudiant ce projet, vous comprenez:

1. **Architecture logicielle**: Comment organiser du code en modules cohérents
2. **Gestion d'états**: Comment suivre et changer l'état des objets
3. **Files d'attente**: Comment gérer l'ordre de traitement
4. **Allocation de ressources**: Comment partager des ressources limitées (pistes)
5. **POO**: Comment utiliser les classes pour modéliser la réalité
6. **Algorithmes**: Comment prendre des décisions logiques

## 🚀 Pour Aller Plus Loin

### Extensions Possibles

1. **Ajouter des priorités**: Les urgences médicales passent en premier
2. **Ajouter la météo**: Certaines conditions empêchent les atterrissages
3. **Ajouter des statistiques**: Temps d'attente moyen, taux d'utilisation des pistes
4. **Interface graphique**: Visualiser les avions et pistes
5. **Simulation temporelle**: Ajouter une horloge qui avance automatiquement

### Défis à Relever

1. Modifier le code pour avoir des pistes spécialisées (certaines pour atterrir, d'autres pour décoller)
2. Ajouter un système de carburant où les avions avec peu de carburant ont la priorité
3. Créer un système de zones de parking pour les avions au sol
4. Implémenter un système de réservation de créneaux horaires

## 📝 Conclusion

Ce simulateur démontre comment, avec des concepts simples (classes, listes, états), on peut créer un système complexe qui modélise un vrai processus du monde réel. La clé est de:

1. **Décomposer le problème** en petits composants
2. **Définir clairement** les états et transitions
3. **Gérer les contraintes** (pistes limitées)
4. **Maintenir la cohérence** de l'état du système

En comprenant cette logique, vous pouvez appliquer les mêmes principes à d'autres systèmes: gestion de stock, système de réservation, contrôle de trafic routier, etc.
