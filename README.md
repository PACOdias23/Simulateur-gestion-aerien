# ✈️ Simulateur de Gestion Aérienne

Un logiciel de simulation d'aiguillage aérien d'un aéroport, développé en Python avec une architecture orientée objet claire et pédagogique.

## 🎯 Objectif

Ce projet simule le contrôle du trafic aérien d'un aéroport en gérant:
- L'arrivée et l'atterrissage des avions
- L'attribution intelligente des pistes
- Le décollage et le départ des avions
- La gestion des files d'attente et de la saturation

## 🚀 Démarrage Rapide

### Prérequis
- Python 3.6+ **ou** Java 11+

### Version Python

```bash
# Cloner le dépôt
git clone https://github.com/PACOdias23/Simulateur-gestion-aerien.git
cd Simulateur-gestion-aerien

# Exécuter le simulateur
python3 simulateur.py

# Tests
python3 tests.py
```

### Version Java

```bash
# Depuis la racine du projet
cd java
./run.sh           # Lance le simulateur
./run.sh tests     # Exécute les tests unitaires

# Ou manuellement :
javac -d out src/*.java
java -cp out Simulateur
java -cp out TestsSimulateur
```

### Utilisation

Le programme propose 3 modes:

1. **Démonstration simple**: Un scénario prédéfini qui montre le fonctionnement de base
2. **Démonstration avancée**: Gestion de la saturation avec une seule piste
3. **Mode interactif**: Contrôlez manuellement tous les aspects de l'aéroport

## 📁 Structure du Projet

```
Simulateur-gestion-aerien/
├── avion.py           # Classe Avion et états possibles (Python)
├── piste.py           # Classe Piste et gestion de disponibilité (Python)
├── aeroport.py        # Classe Aéroport - cœur du système (Python)
├── simulateur.py      # Programme principal avec démonstrations (Python)
├── tests.py           # Tests automatisés (Python)
├── exemples.py        # Exemples d'utilisation de l'API (Python)
├── java/              # Version Java du simulateur
│   ├── src/
│   │   ├── StatutAvion.java       # Enum états avion
│   │   ├── Avion.java             # Classe Avion
│   │   ├── StatutPiste.java       # Enum états piste
│   │   ├── Piste.java             # Classe Piste
│   │   ├── Aeroport.java          # Contrôle aérien
│   │   ├── Simulateur.java        # Programme principal + CLI
│   │   └── TestsSimulateur.java   # Tests unitaires
│   └── run.sh         # Script de compilation/lancement
├── DOCUMENTATION.md   # Documentation technique détaillée
└── README.md          # Ce fichier
```

## 🏗️ Architecture

### Classes Principales

**Avion** (`avion.py`)
- Représente un avion avec son code de vol et sa compagnie
- Gère les états: EN_APPROCHE → ATTERRISSAGE → AU_SOL → DECOLLAGE → EN_VOL

**Piste** (`piste.py`)
- Représente une piste d'atterrissage/décollage
- Gère la disponibilité: DISPONIBLE, OCCUPEE, MAINTENANCE

**Aeroport** (`aeroport.py`)
- Coordonne tout le trafic aérien
- Gère les files d'attente et l'attribution des pistes
- Maintient l'historique des événements

## 💡 Logique du Système

### Flux d'Atterrissage
1. Un avion signale son approche
2. Il est ajouté à la file d'attente
3. Quand une piste est disponible, l'atterrissage est autorisé
4. L'avion atterrit et libère la piste
5. L'avion se positionne au sol

### Flux de Décollage
1. Un avion au sol demande à décoller
2. Une piste disponible lui est attribuée
3. L'avion décolle et libère la piste
4. L'avion quitte l'espace aérien

### Priorités
- Les atterrissages sont prioritaires (sécurité)
- File d'attente FIFO (First In, First Out)
- Gestion automatique de la saturation

## 📖 Documentation

Pour une explication détaillée de la logique et des concepts, consultez [DOCUMENTATION.md](DOCUMENTATION.md) qui couvre:
- Architecture détaillée de chaque module
- Algorithmes et flux de données
- Concepts de programmation utilisés
- Exemples d'utilisation
- Extensions possibles

## 🎓 Aspects Pédagogiques

Ce projet est idéal pour apprendre:
- **Programmation Orientée Objet**: Classes, encapsulation, états
- **Structures de données**: Listes, files FIFO
- **Algorithmes**: Gestion de files d'attente, attribution de ressources
- **Gestion d'état**: Machines à états finis
- **Bonnes pratiques**: Code lisible, documentation, modularité

## 🔧 Exemple d'Utilisation

### Python

```python
from aeroport import Aeroport
from avion import Avion

aeroport = Aeroport("Charles de Gaulle", nombre_pistes=3)

avion = Avion("AF123", "Air France")
aeroport.ajouter_avion_en_approche(avion)
aeroport.autoriser_atterrissage()
aeroport.finaliser_atterrissage("AF123")

aeroport.afficher_etat()
```

### Java

```java
Aeroport aeroport = new Aeroport("Charles de Gaulle", 3);

Avion avion = new Avion("AF123", "Air France");
aeroport.ajouterAvionEnApproche(avion);
aeroport.autoriserAtterrissage();
aeroport.finaliserAtterrissage("AF123");

aeroport.afficherEtat();
```

## 🌟 Fonctionnalités

- ✅ Gestion multi-pistes
- ✅ File d'attente automatique
- ✅ Attribution intelligente des pistes
- ✅ Historique complet des événements
- ✅ Interface console interactive
- ✅ Démonstrations pédagogiques
- ✅ Messages clairs avec émojis
- ✅ Gestion de la saturation

## 🔮 Extensions Futures Possibles

- Interface graphique (GUI)
- Système de priorités (urgences médicales)
- Simulation météorologique
- Statistiques et rapports
- Multi-aéroports connectés
- Persistance des données

## 📝 Licence

Ce projet est sous licence MIT - voir le fichier LICENSE pour plus de détails.

## 👤 Auteur

PACOdias23

## 🤝 Contribution

Les contributions sont les bienvenues! N'hésitez pas à:
1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commit vos changements
4. Push vers la branche
5. Ouvrir une Pull Request
