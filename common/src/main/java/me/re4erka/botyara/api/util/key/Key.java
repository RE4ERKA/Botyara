package me.re4erka.botyara.api.util.key;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public final class Key implements CharSequence, Comparable<Key> {
    private final byte[] characters;
    private final int hash; // Кэшируем хэш-код для производительности

    public Key(final byte[] input) {
        this.characters = input;
        this.hash = computeHashCode();
    }

    public static Key of(final @NotNull String input) {
        return new Key(input.getBytes(StandardCharsets.US_ASCII));
    }

    public static Key create(final String input, final boolean checkAscii, final boolean checkUpperCase) {
        if (input == null) {
            throw new IllegalArgumentException("Input cannot be null");
        }

        if (checkAscii && !isAscii(input)) {
            throw new IllegalArgumentException("Input contains non-ASCII characters");
        }

        return new Key(checkUpperCase ? toUpperCaseAscii(input) : input.getBytes(StandardCharsets.US_ASCII));
    }

    // Метод для проверки, содержит ли строка только ASCII символы
    private static boolean isAscii(final String input) {
        for (char c : input.toCharArray()) {
            if (c > 127) {
                return false;
            }
        }

        return true;
    }

    // Метод для преобразования строки в массив байтов в верхнем регистре ASCII
    private static byte[] toUpperCaseAscii(final String input) {
        final byte[] bytes = input.getBytes(StandardCharsets.US_ASCII);

        for (int i = 0; i < bytes.length; i++) {
            if (bytes[i] >= 'a' && bytes[i] <= 'z') {
                bytes[i] = (byte) (bytes[i] - ('a' - 'A'));
            }
        }

        return bytes;
    }

    @Override
    public int length() {
        return characters.length;
    }

    @Override
    public char charAt(int index) {
        if (index < 0 || index >= characters.length) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Length: " + characters.length);
        }

        return (char) characters[index];
    }

    @Override
    public @NotNull CharSequence subSequence(int start, int end) {
        if (start < 0 || end > characters.length || start > end) {
            throw new IndexOutOfBoundsException("start: " + start + ", end: " + end + ", length: " + characters.length);
        }

        return new String(characters, start, end - start, StandardCharsets.US_ASCII);
    }

    @Override
    public @NotNull String toString() {
        return new String(characters, StandardCharsets.US_ASCII);
    }

    public @NotNull String toLowerCase() {
        final byte[] lowerCaseBytes = new byte[characters.length];

        for (int i = 0; i < characters.length; i++) {
            final byte currentByte = characters[i];

            if (currentByte >= 'A' && currentByte <= 'Z') {
                lowerCaseBytes[i] = (byte) (currentByte + ('a' - 'A'));
            } else {
                lowerCaseBytes[i] = currentByte;
            }
        }

        return new String(lowerCaseBytes, StandardCharsets.US_ASCII);
    }

    @Override
    public int compareTo(final Key other) {
        final int len1 = this.characters.length;
        final int len2 = other.characters.length;

        final int lim = Math.min(len1, len2);

        for (int i = 0; i < lim; i++) {
            final int b1 = this.characters[i] & 0xFF;
            final int b2 = other.characters[i] & 0xFF;

            if (b1 != b2) {
                return b1 - b2;
            }
        }

        return len1 - len2;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Key other = (Key) obj;
        return Arrays.equals(this.characters, other.characters);
    }

    public boolean equals(CharSequence charSequence) {
        if (charSequence == null) {
            return false;
        }

        if (charSequence.length() != characters.length) {
            return false;
        }

        if (charSequence instanceof String) {
            return charSequence.equals(
                    toString()
            );
        }

        final int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if ((char) characters[i] != charSequence.charAt(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    private int computeHashCode() {
        int result = 1;
        for (byte b : characters) {
            result = 31 * result + (b & 0xFF);
        }
        return result;
    }
}
