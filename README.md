# ScopeCraft - Minecraft 1.8.9 Scope & Zoom Mod

**ScopeCraft** est un mod Minecraft 1.8.9 Forge qui affiche un viseur/lunette (`scope.png`) personnalisable lorsque vous visiez à l'arc, accompagné d'un système de zoom (FOV) ajustable et d'un menu de configuration intégré.

![Scope Overlay](assets/scope.png)

---

## 🌟 Fonctionnalités

- 🎯 **Superposition d'écran Scope (PNG)** : Affiche automatiquement votre image de lunette personnalisée en transparence plein écran lorsque vous maintenez le clic droit avec un arc.
- 🔍 **Zoom FOV Ajustable** : Réglage du multiplicateur de zoom (de 1.5x jusqu'à 8.0x) pour ajuster votre précision de tir.
- ⚙️ **Menu de Paramètres In-Game** : Appuyez sur la touche **`O`** en jeu pour ouvrir l'interface de configuration :
  - Activer / Désactiver le mod.
  - Définir le mode d'activation (Visée uniquement vs Arc tenu en main).
  - Modifier la puissance du Zoom FOV.
  - Régler l'opacité de l'image de scope.
- 💾 **Sauvegarde automatique** : Les préférences sont enregistrées dans le fichier `.minecraft/config/scopecraft.json`.

---

## 🎮 Installation & Utilisation

1. Installez **Minecraft 1.8.9 Forge**.
2. Compilez le projet avec Gradle (`./gradlew build`) ou téléchargez le fichier `.jar` compilé dans l'onglet *Releases*.
3. Placez le fichier `.jar` dans votre dossier `.minecraft/mods`.
4. Lancez le jeu et profitez de la lunette en visant avec un arc !
5. Appuyez sur **`O`** pour personnaliser le mod à tout moment.

---

## 🛠️ Structure du Projet

```text
scope-craft/
├── assets/
│   └── scope.png                   # Image originale de la lunette
├── src/main/java/net/scopecraft/
│   ├── ScopeCraft.java             # Main Mod Class & Keybinding
│   ├── config/ScopeConfig.java     # Sauvegarde & chargement JSON
│   ├── event/ScopeRenderHandler.java # Overlay Scope & FOV Zoom
│   └── gui/ScopeConfigGui.java     # Interface graphique (Touche O)
└── src/main/resources/
    ├── mcmod.info                  # Metadata du Mod Forge
    └── assets/scopecraft/textures/gui/scope.png # Texture scope
```

---

## 📄 Licence

Ce projet est sous licence MIT. Libre à vous de modifier le scope ou le code selon vos besoins !
