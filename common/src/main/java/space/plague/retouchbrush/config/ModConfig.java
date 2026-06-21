package space.plague.retouchbrush.config;

import space.plague.retouchbrush.util.PaintingCycleUtil;

public class ModConfig {

    private boolean enableMod;

    private String cycleTo;

    private boolean keepSize;

    private boolean enableDispenserBehavior;

    private boolean enableRightClickBehavior;

    private boolean enableUseDamage;

    private boolean enableDispenserUseDamage;

    public ModConfig() {
        this.enableMod = true;
        this.cycleTo = PaintingCycleUtil.CycleTo.NEXT.getCode();
        this.keepSize = true;
        this.enableDispenserBehavior = true;
        this.enableRightClickBehavior = true;
        this.enableUseDamage = false;
        this.enableDispenserUseDamage = false;
    }

    public boolean isEnableMod() {
        return enableMod;
    }

    public void setEnableMod(boolean enableMod) {
        this.enableMod = enableMod;
    }

    public String getCycleTo() {
        return cycleTo;
    }

    public void setCycleTo(String cycleTo) {
        this.cycleTo = cycleTo;
    }

    public boolean isKeepSize() {
        return keepSize;
    }

    public void setKeepSize(boolean keepSize) {
        this.keepSize = keepSize;
    }

    public boolean isEnableDispenserBehavior() {
        return enableDispenserBehavior;
    }

    public void setEnableDispenserBehavior(boolean enableDispenserBehavior) {
        this.enableDispenserBehavior = enableDispenserBehavior;
    }

    public boolean isEnableRightClickBehavior() {
        return enableRightClickBehavior;
    }

    public void setEnableRightClickBehavior(boolean enableRightClickBehavior) {
        this.enableRightClickBehavior = enableRightClickBehavior;
    }

    public boolean isEnableUseDamage() {
        return enableUseDamage;
    }

    public void setEnableUseDamage(boolean enableUseDamage) {
        this.enableUseDamage = enableUseDamage;
    }

    public boolean isEnableDispenserUseDamage() {
        return enableDispenserUseDamage;
    }

    public void setEnableDispenserUseDamage(boolean enableDispenserUseDamage) {
        this.enableDispenserUseDamage = enableDispenserUseDamage;
    }
}