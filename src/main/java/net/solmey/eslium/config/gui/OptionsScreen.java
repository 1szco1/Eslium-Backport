package net.solmey.eslium.config.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.solmey.eslium.config.ConfigManager;

public class OptionsScreen extends Screen {

    private static final int ROW_H = 24;

    public interface Row {
        void build(OptionsScreen screen, int x, int y, int w);
    }

    private final Screen parent;
    private final List<Row> rows = new ArrayList<>();
    private double scroll;

    public OptionsScreen(Component title, Screen parent) {
        super(title);
        this.parent = parent;
    }

    public OptionsScreen addToggle(String label, Supplier<Boolean> get, Consumer<Boolean> set) {
        this.rows.add((screen, x, y, w) -> screen.addRenderableWidget(
            Button.builder(toggleMessage(label, Boolean.TRUE.equals(get.get())), b -> {
                boolean next = !Boolean.TRUE.equals(get.get());
                set.accept(next);
                b.setMessage(toggleMessage(label, next));
                screen.changed();
            }).bounds(x, y, w, 20).build()));
        return this;
    }

    public OptionsScreen addInt(String label, Supplier<Integer> get, Consumer<Integer> set, int min, int max) {
        this.rows.add((screen, x, y, w) -> {
            Integer value = get.get();
            int current = value == null ? min : value;
            screen.addRenderableWidget(new IntSlider(x, y, w, 20, label, current, min, max, next -> {
                set.accept(next);
                screen.changed();
            }));
        });
        return this;
    }

    public OptionsScreen addAction(String label, Runnable action) {
        this.rows.add((screen, x, y, w) -> screen.addRenderableWidget(
            Button.builder(Component.literal(label), b -> action.run()).bounds(x, y, w, 20).build()));
        return this;
    }

    public OptionsScreen addNav(String label, Supplier<Boolean> enabledGet, Consumer<Boolean> enabledSet, Supplier<Screen> open) {
        this.rows.add((screen, x, y, w) -> {
            int buttonWidth = enabledGet == null ? w : w - 26;
            screen.addRenderableWidget(
                Button.builder(Component.literal(label), b -> screen.openScreen(open.get())).bounds(x, y, buttonWidth, 20).build());
            if (enabledGet != null) {
                screen.addRenderableWidget(
                    Button.builder(onOff(Boolean.TRUE.equals(enabledGet.get())), b -> {
                        boolean next = !Boolean.TRUE.equals(enabledGet.get());
                        enabledSet.accept(next);
                        b.setMessage(onOff(next));
                        screen.changed();
                    }).bounds(x + w - 22, y, 20, 20).build());
            }
        });
        return this;
    }

    private static Component toggleMessage(String label, boolean value) {
        return Component.literal(label + ": " + (value ? "ON" : "OFF"));
    }

    private static Component onOff(boolean value) {
        return Component.literal(value ? "ON" : "OFF");
    }

    private void changed() {
        ConfigManager.saveConfig(ConfigManager.getConfig());
    }

    public void openScreen(Screen screen) {
        if (this.minecraft != null) {
            this.minecraft.setScreen(screen);
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
        this.clearWidgets();
        this.addRenderableWidget(Button.builder(Component.literal("Done"), b -> this.onClose())
            .bounds(this.width / 2 - 60, this.height - 28, 120, 20).build());

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
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        int listHeight = listBottom() - listTop();
        int content = this.rows.size() * ROW_H;
        int maxScroll = Math.max(0, content - listHeight);
        this.scroll -= scrollY * ROW_H;
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
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.renderBackground(graphics, mouseX, mouseY, delta);
        super.render(graphics, mouseX, mouseY, delta);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 12, 0xFFFFFF);

        int top = listTop();
        int bottom = listBottom();
        int content = this.rows.size() * ROW_H;
        if (content > bottom - top) {
            int trackHeight = bottom - top;
            int thumbHeight = Math.max(20, trackHeight * trackHeight / content);
            int maxScroll = content - trackHeight;
            int thumbY = top + (maxScroll == 0 ? 0 : (int) ((trackHeight - thumbHeight) * (this.scroll / maxScroll)));
            int barX = this.width / 2 + panelWidth() / 2 + 4;
            graphics.fill(barX, top, barX + 4, bottom, 0x40000000);
            graphics.fill(barX, thumbY, barX + 4, thumbY + thumbHeight, 0xFFAAAAAA);
        }
    }

    @Override
    public void onClose() {
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.parent);
        }
    }
}
