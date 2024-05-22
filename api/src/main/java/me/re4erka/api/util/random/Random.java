package me.re4erka.api.util.random;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Range;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@UtilityClass
public class Random {
    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    public <T> T nextElement(T[] array) {
        return array[RANDOM.nextInt(0, array.length)];
    }

    public <T> T nextElement(List<T> list) {
        return list.get(
                RANDOM.nextInt(0, list.size())
        );
    }

    public <T extends Enum<T>> T nextEnum(Class<T> enumType) {
        final T[] constants = enumType.getEnumConstants();

        return constants[RANDOM.nextInt(0, constants.length)];
    }

    public boolean chance(@Range(from = 1, to = 100) int chance) {
        final int randomChance = RANDOM.nextInt(1, 100);

        return chance >= randomChance;
    }

    public int range(int origin, int bound) {
        return RANDOM.nextInt(origin, bound);
    }
}
