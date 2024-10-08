package org.example.codellamacopilot.icons;

import com.intellij.openapi.util.IconLoader;

import javax.swing.*;

/**
 * Class that contains the icons for the plugin.
 */
public interface LLMCopilotIcons {
    Icon SendIconLight = IconLoader.getIcon("/icons/sendIcons/send.svg", LLMCopilotIcons.class);
    Icon SendIconDark = IconLoader.getIcon("/icons/sendIcons/send_dark.svg", LLMCopilotIcons.class);
}
