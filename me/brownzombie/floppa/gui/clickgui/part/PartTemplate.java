package me.brownzombie.floppa.gui.clickgui.part;

public class PartTemplate {

    private boolean shown;

    public void renderComponent() {}

    public void updateComponent(int mouseX, int mouseY) {}

    public void mouseClicked(int mouseX, int mouseY, int button) {}

    public void mouseReleased(int mouseX, int mouseY, int mouseButton) {}

    public int getParentHeight() {
        return 0;
    }

    public void keyTyped(char typedChar, int key) {}

    public void setOff(int newOff) {}

    public boolean isShown() {
        return !this.shown;
    }

    public boolean setShown(boolean shown) {
        this.shown = shown;
        return this.shown;
    }

    public int getHeight() {
        return 0;
    }
}
