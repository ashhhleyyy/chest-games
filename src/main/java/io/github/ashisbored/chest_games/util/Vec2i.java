package io.github.ashisbored.chest_games.util;

public record Vec2i(int x, int y) {
    public Vec2i add(int xOffset, int yOffset) {
        return new Vec2i(this.x + xOffset, this.y + yOffset);
    }

    public int pack(int width) {
        return (y * width) + x;
    }

    public static Vec2i unpackAndOffset(int index, int width, int xOffset, int yOffset) {
        return new Vec2i(
                (index % width) + xOffset,
                (index / width) + yOffset
        );
    }
}
