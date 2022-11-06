package voxelengine.gui;

import java.util.ArrayList;

/**
 * @author gabed
 * @Date 10/23/2022
 */
public class UIMenu {
    private final ArrayList<UIElement> uiElements = new ArrayList<>();

    public void addElement(UIElement element) {
        uiElements.add(element);
    }

    public void render() {
        uiElements.forEach(UIElement::Render);
    }

    public void resize() {
        uiElements.forEach(UIElement::resetVAO);
    }

    public ArrayList<UIElement> getUIElements() {
        return uiElements;
    }
}
