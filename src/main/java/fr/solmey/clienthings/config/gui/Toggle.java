package fr.solmey.clienthings.config.gui;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

import java.util.function.Consumer;

public class Toggle extends CheckboxWidget {
    private final Consumer<Boolean> onChange;

    public Toggle(int x, int y, int width, int height, Text message, boolean checked, Consumer<Boolean> onChange) {
        super(x, y, width, height, message, checked);
        this.onChange = onChange;
    }

    @Override
    public void onPress() {
        super.onPress();
        if (this.onChange != null) {
            this.onChange.accept(this.isChecked());
        }
    }
}
