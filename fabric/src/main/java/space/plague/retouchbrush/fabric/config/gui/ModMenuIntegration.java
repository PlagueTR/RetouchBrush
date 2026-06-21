package space.plague.retouchbrush.fabric.config.gui;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import space.plague.retouchbrush.config.gui.GeneralOptionsScreen;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> GeneralOptionsScreen.getConfigBuilder().build();
    }

}