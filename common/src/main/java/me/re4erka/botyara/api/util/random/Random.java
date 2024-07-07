package me.re4erka.botyara.api.util.random;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class Random {
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public <T> T nextElement(@NotNull T[] array) {
        return array[RANDOM.nextInt(array.length)];
    }

    public <T> T nextElement(@NotNull List<T> list) {
        return list.get(
                RANDOM.nextInt(list.size())
        );
    }

    public <T extends Enum<T>> T nextEnum(@NotNull Class<T> enumType) {
        final T[] constants = enumType.getEnumConstants();

        return constants[RANDOM.nextInt(constants.length)];
    }

    public boolean chance(@Range(from = 1, to = 100) int chance) {
        final int randomChance = RANDOM.nextInt(1, 100);

        return chance >= randomChance;
    }

    public int range(int origin, int bound) {
        return RANDOM.nextInt(origin, bound);
    }

    public int next(int bound) {
        return RANDOM.nextInt(bound);
    }
}
