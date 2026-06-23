package space.plague.retouchbrush.forge;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.config.gui.GeneralOptionsScreen;

@Mod(Main.MOD_ID)
public final class MainForge {
    public MainForge() {

        if (ModList.get().isLoaded("cloth_config")) {
            MinecraftForge.registerConfigScreen(
                (mc, parentScreen) -> GeneralOptionsScreen.getConfigBuilder().build()
            );
        }

        Main.init();
    }
}