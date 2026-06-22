package space.plague.retouchbrush.config;

import space.plague.retouchbrush.util.PaintingCycleUtil;

public class ModConfig {

    private boolean enableMod;

    private boolean enableUseBehavior;

    private boolean enableDispenserBehavior;

    private String cycleToUse;

    private String cycleToDispenser;

    private boolean keepSizeUse;

    private boolean keepSizeDispenser;

    private boolean enableUseDamage;

    private boolean enableDispenserDamage;

    public ModConfig() {
        this.enableMod = true;
        this.cycleToUse = PaintingCycleUtil.CycleTo.SEQUENTIAL.getCode();
        this.cycleToDispenser = PaintingCycleUtil.CycleTo.SEQUENTIAL.getCode();
        this.keepSizeUse = true;
        this.keepSizeDispenser = true;
        this.enableUseBehavior = true;
        this.enableDispenserBehavior = true;
        this.enableUseDamage = false;
        this.enableDispenserDamage = false;
    }

    public boolean isEnableMod() {
        return enableMod;
    }

    public void setEnableMod(boolean enableMod) {
        this.enableMod = enableMod;
    }


    public boolean isEnableUseBehavior() {
        return enableUseBehavior;
    }

    public void setEnableUseBehavior(boolean enableUseBehavior) {
        this.enableUseBehavior = enableUseBehavior;
    }

    public boolean isEnableDispenserBehavior() {
        return enableDispenserBehavior;
    }

    public void setEnableDispenserBehavior(boolean enableDispenserBehavior) {
        this.enableDispenserBehavior = enableDispenserBehavior;
    }

    public String getCycleToUse() {
        return cycleToUse;
    }

    public void setCycleToUse(String cycleToUse) {
        this.cycleToUse = cycleToUse;
    }

    public String getCycleToDispenser() {
        return cycleToDispenser;
    }

    public void setCycleToDispenser(String cycleToDispenser) {
        this.cycleToDispenser = cycleToDispenser;
    }

    public boolean isKeepSizeUse() {
        return keepSizeUse;
    }

    public void setKeepSizeUse(boolean keepSizeUse) {
        this.keepSizeUse = keepSizeUse;
    }

    public boolean isKeepSizeDispenser() {
        return keepSizeDispenser;
    }

    public void setKeepSizeDispenser(boolean keepSizeDispenser) {
        this.keepSizeDispenser = keepSizeDispenser;
    }

    public boolean isEnableUseDamage() {
        return enableUseDamage;
    }

    public void setEnableUseDamage(boolean enableUseDamage) {
        this.enableUseDamage = enableUseDamage;
    }

    public boolean isEnableDispenserDamage() {
        return enableDispenserDamage;
    }

    public void setEnableDispenserDamage(boolean enableDispenserDamage) {
        this.enableDispenserDamage = enableDispenserDamage;
    }
}