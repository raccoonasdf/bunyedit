package fun.raccoon.bunyedit;

import net.fabricmc.api.ModInitializer;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BunyEdit implements ModInitializer {
    public static final String MOD_ID = "bunyedit";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final TomlConfigHandler CONFIG;
    public static final boolean ALLOWED_CREATIVE;
    public static final boolean ALLOWED_SURVIVAL;
    static {
        Toml toml = new Toml();
        toml.addCategory("Let non-operators use bunyedit", "allowNonOperators");
        toml.addEntry("allowNonOperators.inCreative", false);
        toml.addEntry("allowNonOperators.inSurvival", false);

        CONFIG = new TomlConfigHandler(MOD_ID, toml);
        ALLOWED_CREATIVE = CONFIG.getBoolean("allowNonOperators.inCreative");
        ALLOWED_SURVIVAL = CONFIG.getBoolean("allowNonOperators.inSurvival");
    }

    @Override
    public void onInitialize() {
  		LOGGER.info("bunyedit initialized babey :)");
    }
}
