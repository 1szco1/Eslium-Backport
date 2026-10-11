package fr.solmey.clienthings.config.gui;

import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

import java.util.function.IntConsumer;

public class IntSlider extends SliderWidget {
    private final IntConsumer setter;
    private final int min;
    private final int max;
    private final String label;

    public IntSlider(int x, int y, int width, int height, String label, int value, int min, int max, IntConsumer setter) {
        super(x, y, width, height, Text.of(label + ": " + value), normalize(value, min, max));
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
        setMessage(Text.of(this.label + ": " + current()));
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
