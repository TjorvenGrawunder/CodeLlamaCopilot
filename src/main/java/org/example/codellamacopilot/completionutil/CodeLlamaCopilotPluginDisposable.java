package org.example.codellamacopilot.completionutil;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 * The service is intended to be used instead of a project/application as a parent disposable.
 */
@Service({Service.Level.APP, Service.Level.PROJECT})
public final class CodeLlamaCopilotPluginDisposable implements Disposable {

    private static Disposable shortDisposable = null;

    public static @NotNull Disposable getInstance() {
        return ApplicationManager.getApplication().getService(CodeLlamaCopilotPluginDisposable.class);
    }

    public static @NotNull Disposable getInstance(@NotNull Project project) {
        return project.getService(CodeLlamaCopilotPluginDisposable.class);
    }

    @Override
    public void dispose() {

    }

    public static void setShortDisposable(Disposable disposable) {
        shortDisposable = disposable;
    }

    public static Disposable getShortDisposable() {
        return shortDisposable;
    }
}
