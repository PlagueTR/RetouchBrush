package space.plague.retouchbrush.fabric;

import net.fabricmc.api.ModInitializer;

import space.plague.retouchbrush.Main;
import space.plague.retouchbrush.fabric.registry.RightClickBehavior;

public final class MainFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        RightClickBehavior.register();

        Main.init();

    }
}