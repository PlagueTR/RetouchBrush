package space.plague.retouchbrush.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.gui.GeneralOptionsScreen;

@Mod(Main.MOD_ID)
public final class MainForge {
    public MainForge() {

        if (ModList.get().isLoaded("cloth_config")) {
            ModLoadingContext.get().registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parentScreen) ->
                            GeneralOptionsScreen.getConfigBuilder().build()
                    )
            );
        }

        Main.init();
    }
}