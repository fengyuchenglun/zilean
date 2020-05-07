package com.kim.zilean.form.component;

import com.intellij.ide.util.TreeJavaClassChooserDialog;
import com.intellij.openapi.application.Experiments;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.ClassUtil;
import com.intellij.ui.TextAccessor;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.ExtendableTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

public class ClassChooseTextField extends ComponentWithBrowseButton<JTextField> implements TextAccessor {
    private ActionListener actionListener;

    public ClassChooseTextField() {
        super(Experiments.getInstance().isFeatureEnabled("inline.browse.button") ? new ExtendableTextField(10) : new JBTextField(10), null);
    }

    @NotNull
    public JTextField getTextField() {
        return getChildComponent();
    }

    @NotNull
    @Override
    public String getText() {
        return StringUtil.notNullize(getTextField().getText());
    }

    @Override
    public void setText(@Nullable String text) {
        getTextField().setText(text);
    }

    public boolean isEditable() {
        return getTextField().isEditable();
    }

    public void setEditable(boolean b) {
        getTextField().setEditable(b);
        getButton().setFocusable(!b);
    }

    public void setActionListener(String title, Project project, @Nullable JFrame frame) {
        this.setActionListener(title, project, frame, null);
    }

    public void setActionListener(String title, Project project, @Nullable JFrame frame, @Nullable Consumer<String> callback) {
        if (super.listenerList.getListenerCount() > 0) {
            super.removeActionListener(this.actionListener);
        }
        this.actionListener = e -> {
            TreeJavaClassChooserDialog chooser = new TreeJavaClassChooserDialog(title, project);
            if (!this.getText().isEmpty()) {
                PsiClass aClass = ClassUtil.findPsiClass(PsiManager.getInstance(project), this.getText());
                if (aClass != null) {
                    chooser.select(aClass);
                }
            }
            chooser.showDialog();
            PsiClass clz = chooser.getSelected();
            if (clz != null) {
                this.setText(clz.getQualifiedName());
                if (callback != null) {
                    callback.accept(clz.getQualifiedName());
                }
            }
            if (frame != null) {
                frame.toFront();
            }
        };
        super.addActionListener(this.actionListener);
    }
}
