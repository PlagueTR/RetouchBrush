package space.plague.retouchbrush.util;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.level.Level;

import java.util.List;

public class PaintingCycleUtil {

    public enum CycleTo {
        SEQUENTIAL("SEQUENTIAL"),
        RANDOM("RANDOM");

        private final String code;

        CycleTo(String code) {
            this.code = code;
        }

        public String getCode() { return code; }
    }

    public static boolean cyclePainting(Painting painting, Level level, CycleTo cycleTo, boolean keepSize) {

        Registry<PaintingVariant> registry = level.registryAccess().registryOrThrow(Registries.PAINTING_VARIANT);
        Holder<PaintingVariant> currentVariant = painting.getVariant();

        List<Holder.Reference<PaintingVariant>> variants = registry.holders()
                .filter(holder -> {
                    if(!holder.is(PaintingVariantTags.PLACEABLE)) {
                        return false;
                    }

                    PaintingVariant variant = holder.value();

                    if (keepSize) {
                        return variant.width() == currentVariant.value().width() && variant.height() == currentVariant.value().height();
                    }

                    painting.setVariant(holder);
                    boolean fits = painting.survives();
                    painting.setVariant(currentVariant);

                    return fits;
                })
                .toList();

        if (variants.isEmpty()) {
            return false;
        }

        Holder<PaintingVariant> nextVariant;

        if (cycleTo == CycleTo.SEQUENTIAL) {
            int currentIndex = -1;
            for (int i = 0; i < variants.size(); i++) {
                if (variants.get(i).key().equals(currentVariant.unwrapKey().orElse(null))) {
                    currentIndex = i;
                    break;
                }
            }
            int nextIndex = (currentIndex + 1) % variants.size();
            nextVariant = variants.get(nextIndex);
        }
        else if (cycleTo == CycleTo.RANDOM) {
            List<Holder.Reference<PaintingVariant>> otherVariants = variants.stream()
                    .filter(holder -> !holder.value().equals(currentVariant.value()))
                    .toList();

            if (otherVariants.isEmpty()) {
                return false;
            }

            RandomSource rand = level.getRandom();
            int randomIndex = rand.nextInt(otherVariants.size());
            nextVariant = otherVariants.get(randomIndex);
        }
        else {
            return false;
        }

        painting.setVariant(nextVariant);

        return true;
    }

}
