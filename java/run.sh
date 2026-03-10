#!/bin/bash
# run.sh - Compile et exécute le simulateur Java
# Usage :
#   ./java/run.sh          → lance le simulateur
#   ./java/run.sh tests    → exécute les tests unitaires

set -e

SCRIPT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$SCRIPT_DIR"

echo "Compilation des sources Java..."
mkdir -p out
javac -d out src/*.java
echo "Compilation réussie."
echo

if [ "$1" = "tests" ]; then
    java -cp out TestsSimulateur
else
    java -cp out Simulateur
fi
