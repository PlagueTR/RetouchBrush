package space.plague.retouchbrush.fabric;

import net.fabricmc.api.ModInitializer;

import space.plague.retouchbrush.Main;

public final class MainFabric implements ModInitializer {
    @Override
    public void onInitialize() {

        Main.init();

    }
}