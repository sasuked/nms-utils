package me.saiintbrisson.nms.sdk.util;

import lombok.Getter;

import java.util.Iterator;

@Getter
public class StringSplitter implements Iterable<String> {

    private int splitMaxSize;
    private String[] parts;

    public StringSplitter(int splitCount, int splitMaxSize, String text) {
        this.splitMaxSize = splitMaxSize;
        this.parts = new String[splitCount];

        int cursor = 0;
        for(int i = 0; i < splitCount; i++) {
            int charCursor = cursor * splitMaxSize;
            if(charCursor >= text.length()) break;

            int nextCharCursor = charCursor + splitMaxSize;
            if(nextCharCursor >= text.length()) {
                parts[cursor] = text.substring(charCursor);
                break;
            } else {
                parts[cursor] = text.substring(charCursor, nextCharCursor);
            }

            cursor++;
        }
    }

    public String get(int index) {
        try {
            return parts[index];
        } catch (ArrayIndexOutOfBoundsException ignore) {
            return null;
        }
    }

    public int partsSize() {
        return parts.length;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {

            private int current = 0;

            @Override
            public boolean hasNext() {
                return current < parts.length;
            }

            @Override
            public String next() {
                current++;
                return parts[current - 1];
            }

        };
    }
}
