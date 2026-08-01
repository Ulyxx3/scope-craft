package net.scopecraft.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ScopeConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static File configFile;

    // Config options
    public boolean enabled = true;
    public boolean scopeOnlyWhenAiming = true; // Si vrai: uniquement en visant à l'arc. Si faux: dès qu'un arc est tenu.
    public float zoomFactor = 3.0f; // Multiplicateur de zoom FOV (ex: 3.0x zoom)
    public float scopeOpacity = 1.0f; // Opacité (0.1 à 1.0)
    public float scopeScale = 1.0f; // Échelle de l'image (0.5 à 2.0)

    public static ScopeConfig INSTANCE = new ScopeConfig();

    public static void init(File configDir) {
        configFile = new File(configDir, "scopecraft.json");
        load();
    }

    public static void load() {
        if (configFile != null && configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                ScopeConfig config = GSON.fromJson(reader, ScopeConfig.class);
                if (config != null) {
                    INSTANCE = config;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        if (configFile == null) return;
        try (FileWriter writer = new FileWriter(configFile)) {
            GSON.toJson(INSTANCE, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
