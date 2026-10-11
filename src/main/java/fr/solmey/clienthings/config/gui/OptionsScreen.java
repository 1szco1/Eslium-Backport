package fr.solmey.clienthings.config.gui;

import fr.solmey.clienthings.config.JsonConfig;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class OptionsScreen extends Screen {
    private static final int ROW_H = 24;

    public interface Row {
        void build(OptionsScreen screen, int x, int y, int w);
    }

    private final Screen parent;
    private final List<Row> rows = new ArrayList<>();
    private double scroll;

    public OptionsScreen(Text title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    public OptionsScreen addToggle(String label, Supplier<Boolean> get, Consumer<Boolean> set) {
        this.rows.add((screen, x, y, w) -> screen.addButton(new Toggle(x, y, w, 20, Text.of(label), Boolean.TRUE.equals(get.get()), value -> {
            set.accept(value);
            screen.changed();
        })));
        return this;
    }

    public OptionsScreen addInt(String label, Supplier<Integer> get, Consumer<Integer> set, int min, int max) {
        this.rows.add((screen, x, y, w) -> {
            Integer value = get.get();
            int current = value == null ? min : value;
            screen.addButton(new IntSlider(x, y, w, 20, label, current, min, max, next -> {
                set.accept(next);
                screen.changed();
            }));
        });
        return this;
    }

    public OptionsScreen addAction(String label, Runnable action) {
        this.rows.add((screen, x, y, w) -> screen.addButton(new ButtonWidget(x, y, w, 20, Text.of(label), button -> action.run())));
        return this;
    }

    public OptionsScreen addNav(String label, Supplier<Boolean> enabledGet, Consumer<Boolean> enabledSet, Supplier<Screen> open) {
        this.rows.add((screen, x, y, w) -> {
            int buttonWidth = enabledGet == null ? w : w - 26;
            screen.addButton(new ButtonWidget(x, y, buttonWidth, 20, Text.of(label), button -> screen.client.openScreen(open.get())));
            if (enabledGet != null) {
                screen.addButton(new Toggle(x + w - 22, y, 20, 20, Text.of(""), Boolean.TRUE.equals(enabledGet.get()), value -> {
                    enabledSet.accept(value);
                    screen.changed();
                }));
            }
        });
        return this;
    }

    private void changed() {
        JsonConfig.saveConfig();
    }

    public void openScreen(Screen screen) {
        if (this.client != null) {
            this.client.openScreen(screen);
        }
    }

    private int listTop() {
        return 32;
    }

    private int listBottom() {
        return this.height - 36;
    }

    private int panelWidth() {
        return Math.min(320, this.width - 40);
    }

    @Override
    protected void init() {
        rebuild();
    }

    private void rebuild() {
        this.buttons.clear();
        this.children.clear();
        addButton(new ButtonWidget(this.width / 2 - 60, this.height - 28, 120, 20, Text.of("Done"), button -> this.onClose()));

        int top = listTop();
        int bottom = listBottom();
        int x = this.width / 2 - panelWidth() / 2;
        int w = panelWidth();

        for (int i = 0; i < this.rows.size(); i++) {
            int y = (int) Math.round(top + i * ROW_H - this.scroll);
            if (y + 20 <= top || y >= bottom) {
                continue;
            }
            this.rows.get(i).build(this, x, y, w);
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        int listHeight = listBottom() - listTop();
        int content = this.rows.size() * ROW_H;
        int maxScroll = Math.max(0, content - listHeight);
        this.scroll -= amount * ROW_H;
        if (this.scroll < 0) {
            this.scroll = 0;
        }
        if (this.scroll > maxScroll) {
            this.scroll = maxScroll;
        }
        rebuild();
        return true;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        drawCenteredText(matrices, this.textRenderer, this.title, this.width / 2, 12, 0xFFFFFF);

        int top = listTop();
        int bottom = listBottom();
        int content = this.rows.size() * ROW_H;
        if (content > bottom - top) {
            int trackHeight = bottom - top;
            int thumbHeight = Math.max(20, trackHeight * trackHeight / content);
            int maxScroll = content - trackHeight;
            int thumbY = top + (maxScroll == 0 ? 0 : (int) ((trackHeight - thumbHeight) * (this.scroll / maxScroll)));
            int barX = this.width / 2 + panelWidth() / 2 + 4;
            fill(matrices, barX, top, barX + 4, bottom, 0x40000000);
            fill(matrices, barX, thumbY, barX + 4, thumbY + thumbHeight, 0xFFAAAAAA);
        }
    }

    @Override
    public void onClose() {
        if (this.client != null) {
            this.client.openScreen(this.parent);
        }
    }
}
