package org.example.codellamacopilot.chatwindow.ui.customcomponents;

import org.example.codellamacopilot.settings.modelsettings.ModelSpecificSettingsIdentifier;

import javax.swing.*;
import java.awt.*;

public class ParametrizedJPanel<T extends ModelSpecificSettingsIdentifier> extends JPanel {
    public ParametrizedJPanel(LayoutManager layout) {
        super(layout);
    }

    public T getSettingComponent(int n) {
        return (T) super.getComponent(n);
    }
}
