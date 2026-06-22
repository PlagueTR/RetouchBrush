package space.plague.retouchbrush.forge;

import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.gui.GeneralOptionsScreen;

@Mod(Main.MOD_ID)
public final class MainForge {
    public MainForge(FMLJavaModLoadingContext context) {

        if (ModList.get().isLoaded("cloth_config")) {
            context.registerExtensionPoint(
                    ConfigScreenHandler.ConfigScreenFactory.class,
                    () -> new ConfigScreenHandler.ConfigScreenFactory((mc, parentScreen) ->
                            GeneralOptionsScreen.getConfigBuilder().build()
                    )
            );
        }

        Main.init();
    }
}