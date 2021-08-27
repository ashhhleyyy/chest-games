package io.github.ashisbored.chest_games.games.minesweeper;

import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.layered.Layer;
import eu.pb4.sgui.api.gui.layered.LayeredGui;
import io.github.ashisbored.chest_games.util.Vec2i;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Formatting;

import java.util.Random;

public class MinesweeperGui extends LayeredGui {
    private static final int WIDTH = 9;
    private static final int HEIGHT = 6;
    private static final int MINE_COUNT = 10;

    private static ItemStack create(String nbt) {
        ItemStack stack = Items.GRAY_BANNER.getDefaultStack();
        try {
            stack.setNbt(StringNbtReader.parse(nbt));
            stack.addHideFlag(ItemStack.TooltipSection.ADDITIONAL);
        } catch (Exception ignored) {}

        return stack;
    }

    // From: https://github.com/Patbox/sgui/blob/1.17.1/src/testmod/java/eu/pb4/sgui/testmod/SnakeGui.java#L29-L40
    private static final ItemStack[] NUMBERS = new ItemStack[] {
            create("{BlockEntityTag:{Patterns:[{Pattern:bs,Color:0},{Pattern:ls,Color:0},{Pattern:ts,Color:0},{Pattern:rs,Color:0},{Pattern:dls,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:cs,Color:0},{Pattern:tl,Color:0},{Pattern:cbo,Color:7},{Pattern:bs,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:ts,Color:0},{Pattern:mr,Color:7},{Pattern:bs,Color:0},{Pattern:dls,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:bs,Color:0},{Pattern:ms,Color:0},{Pattern:ts,Color:0},{Pattern:cbo,Color:7},{Pattern:rs,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:ls,Color:0},{Pattern:hhb,Color:7},{Pattern:rs,Color:0},{Pattern:ms,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:bs,Color:0},{Pattern:mr,Color:7},{Pattern:ts,Color:0},{Pattern:drs,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:bs,Color:0},{Pattern:rs,Color:0},{Pattern:hh,Color:7},{Pattern:ms,Color:0},{Pattern:ts,Color:0},{Pattern:ls,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:dls,Color:0},{Pattern:ts,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:dls,Color:0},{Pattern:ts,Color:0},{Pattern:bo,Color:7}]}}"),
            create("{BlockEntityTag:{Patterns:[{Pattern:ls,Color:0},{Pattern:hhb,Color:7},{Pattern:ms,Color:0},{Pattern:ts,Color:0},{Pattern:rs,Color:0},{Pattern:bs,Color:0},{Pattern:bo,Color:7}]}}")
    };

    private static final ItemStack[] MINE_NUMBERS = new ItemStack[] {
            new ItemStack(Items.LIGHT_GRAY_STAINED_GLASS_PANE).setCustomName(new LiteralText("0")),
            new ItemStack(Items.LIGHT_BLUE_STAINED_GLASS_PANE).setCustomName(new LiteralText("1")),
            new ItemStack(Items.LIME_STAINED_GLASS_PANE)      .setCustomName(new LiteralText("2")),
            new ItemStack(Items.RED_STAINED_GLASS_PANE)       .setCustomName(new LiteralText("3")),
            new ItemStack(Items.BLUE_STAINED_GLASS_PANE)      .setCustomName(new LiteralText("4")),
            new ItemStack(Items.BROWN_STAINED_GLASS_PANE)     .setCustomName(new LiteralText("5")),
            new ItemStack(Items.CYAN_STAINED_GLASS_PANE)      .setCustomName(new LiteralText("6")),
            new ItemStack(Items.BLACK_STAINED_GLASS_PANE)     .setCustomName(new LiteralText("7")),
            new ItemStack(Items.GRAY_STAINED_GLASS_PANE)      .setCustomName(new LiteralText("8")),
    };

    private final Layer numbersLayer;
    private final Layer flagsLayer;
    private final Layer statsLayer;

    private final boolean[] mines = new boolean[WIDTH * HEIGHT];
    @SuppressWarnings("MismatchedReadAndWriteOfArray") // hmm, intellij seems to think I'm not reading it, when I clearly do.
    private final boolean[] flags = new boolean[WIDTH * HEIGHT];
    private final int[] nearbyMinesCount = new int[WIDTH * HEIGHT];

    private int minesRemaining = MINE_COUNT;
    private int revealedCount = 0;

    private boolean locked = false;

    public MinesweeperGui(ServerPlayerEntity player) {
        super(ScreenHandlerType.GENERIC_9X6, player, true);
        this.setTitle(new LiteralText("Minesweeper"));
        this.populateMines();
        this.numbersLayer = new Layer(HEIGHT, WIDTH);

        // Fill the board with test markings
        for (int i = 0; i < this.mines.length; i++) {
            this.numbersLayer.setSlot(i, new GuiElementBuilder(Items.GRAY_STAINED_GLASS_PANE)
                    .setName(new LiteralText(""))
                    .setCallback((index, type, action) -> {
                        if (this.locked) return;

                        if (type.isLeft) {
                            this.revealSquare(index);
                        } else if (type.isRight) {
                            if (this.minesRemaining == 0) return;
                            this.flagSquare(index);
                        }
                    })
            );
            int bombCount = this.countSurroundingMines(i);
            this.nearbyMinesCount[i] = bombCount;
        }

        this.flagsLayer = new Layer(HEIGHT, WIDTH);

        this.statsLayer = new Layer(1, 2);
        this.updateStats();

        this.addLayer(this.numbersLayer, 0, 0).setZIndex(0);
        this.addLayer(this.flagsLayer, 0, 0).setZIndex(5);
        this.addLayer(this.statsLayer, 7, 6).setZIndex(0);
    }

    private void failed() {
        this.getPlayer().playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 1.0f, 1.0f);
        for (int i = 0; i < this.mines.length; i++) {
            boolean mine = this.mines[i];
            if (mine) {
                this.flagsLayer.setSlot(i, new ItemStack(Items.TNT));
            }
        }
        this.locked = true;
    }

    private void complete() {
        this.getPlayer().playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.MASTER, 1.0f, 1.0f);
        this.locked = true;
    }

    private void checkComplete() {
        if (this.revealedCount >= (WIDTH * HEIGHT) - MINE_COUNT) {
            this.complete();
        }
    }

    private void flagSquare(int index) {
        boolean flagged = this.flags[index] = !this.flags[index];
        this.getPlayer().playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, SoundCategory.MASTER, 1.0f, 1.0f);
        if (flagged) {
            this.minesRemaining--;
            this.updateStats();
            this.flagsLayer.setSlot(index, new GuiElementBuilder(Items.RED_BANNER)
                    .setName(new LiteralText("Flag").formatted(Formatting.RED))
                    .setCallback((index1, type, action) -> {
                        if (this.locked) return;

                        if (type.isRight) {
                            this.flagSquare(index1);
                        }
                    })
            );
        } else {
            this.minesRemaining++;
            this.updateStats();
            this.flagsLayer.clearSlot(index);
        }
    }

    private void revealSquare(int index) {
        if (this.mines[index]) {
            // Failed
            this.failed();
            return;
        }

        int nearbyMines = this.nearbyMinesCount[index];
        this.numbersLayer.setSlot(index, itemForCount(nearbyMines));
        this.revealedCount++;
        this.getPlayer().playSound(SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1.0f, 1.0f);
        this.checkComplete();
    }

    private int countSurroundingMines(int pos) {
        int mineCount = 0;

        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int yOffset = -1; yOffset <= 1; yOffset++) {
                if (xOffset == 0 && yOffset == 0) continue;

                Vec2i testPos = Vec2i.unpackAndOffset(pos, WIDTH, xOffset, yOffset);

                if (testPos.x() < 0 || testPos.x() >= WIDTH
                        || testPos.y() < 0 || testPos.y() >= HEIGHT) {
                    // Skip positions that are out-of-bounds
                    continue;
                }

                if (this.mines[testPos.pack(WIDTH)]) {
                    mineCount++;
                }
            }
        }

        return mineCount;
    }

    private void populateMines() {
        Random rand = new Random();
        for (int m = 0; m < MINE_COUNT; m++) {
            int i = rand.nextInt(this.mines.length);
            while (this.mines[i]) {
                i = rand.nextInt(this.mines.length);
            }
            this.mines[i] = true;
        }
    }

    private void updateStats() {
        int firstDigit = (this.minesRemaining / 10) % 10;
        int secondDigit = this.minesRemaining % 10;
        if (firstDigit != 0) {
            this.setNumber(0, this.minesRemaining, firstDigit);
        } else {
            this.statsLayer.clearSlot(0);
        }
        this.setNumber(1, this.minesRemaining, secondDigit);
    }

    private void setNumber(int index, int value, int digit) {
        ItemStack stack = NUMBERS[digit].copy();
        stack.setCustomName(new LiteralText("Flags remaining: " + value));
        this.statsLayer.setSlot(index, stack);
    }

    private static ItemStack itemForCount(int count) {
        ItemStack stack = MINE_NUMBERS[count].copy();
        if (count > 1) {
            stack.setCount(count);
        }
        return stack;
    }
}
