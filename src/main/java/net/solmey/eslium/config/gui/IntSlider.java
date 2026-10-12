package net.solmey.eslium.config.gui;

import java.util.function.IntConsumer;

import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;

public class IntSlider extends AbstractSliderButton {

    private final IntConsumer setter;
    private final int min;
    private final int max;
    private final String label;

    public IntSlider(int x, int y, int width, int height, String label, int value, int min, int max, IntConsumer setter) {
        super(x, y, width, height, Component.literal(label + ": " + value), normalize(value, min, max));
        this.label = label;
        this.setter = setter;
        this.min = min;
        this.max = max;
    }

    private static double normalize(int value, int min, int max) {
        if (max <= min) {
            return 0.0D;
        }
        return (value - min) / (double) (max - min);
    }

    @Override
    protected void updateMessage() {
        setMessage(Component.literal(this.label + ": " + current()));
    }

    @Override
    protected void applyValue() {
        if (this.setter != null) {
            this.setter.accept(current());
        }
    }

    private int current() {
        int range = this.max - this.min;
        return this.min + (int) Math.round(this.value * range);
    }
}
