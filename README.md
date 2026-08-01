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

## 📦 Téléchargement & Compilation du Fichier `.jar`

Le fichier prêt à l'emploi est compilé dans le dossier `build/libs/` :

```text
build/libs/ScopeCraft-1.0.0.jar
```

### Installation :
1. Assurez-vous d'avoir **Minecraft 1.8.9 Forge** installé.
2. Copiez le fichier `ScopeCraft-1.0.0.jar` dans votre dossier `.minecraft/mods`.
3. Lancez Minecraft et appuyez sur **`O`** en jeu pour ajuster la configuration !

### Recompilation avec PowerShell :
Pour re-générer le paquet `.jar` après des modifications :
```powershell
powershell -ExecutionPolicy Bypass -File build_jar.ps1
```

---

## 🛠️ Structure du Projet

```text
scope-craft/
├── assets/
│   └── scope.png                      # Image originale de la lunette
├── build/libs/
│   └── ScopeCraft-1.0.0.jar           # Fichier .jar exécutable pour Minecraft
├── src/main/java/net/scopecraft/
│   ├── ScopeCraft.java                # Main Mod Class
│   ├── config/ScopeConfig.java        # Sauvegarde & chargement JSON
│   ├── event/ScopeRenderHandler.java    # Overlay Scope & FOV Zoom
│   └── gui/ScopeConfigGui.java        # Interface graphique (Touche O)
└── src/main/resources/
    ├── mcmod.info                     # Metadata du Mod Forge
    └── assets/scopecraft/textures/gui/scope.png # Texture scope
```

---

## 📄 Licence

Ce projet est sous licence MIT. Libre à vous de modifier le scope ou le code selon vos besoins !
