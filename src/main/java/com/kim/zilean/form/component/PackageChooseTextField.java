package com.kim.zilean.form.component;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.application.Experiments;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.TextAccessor;
import com.intellij.ui.components.JBTextField;
import com.intellij.ui.components.fields.ExtendableTextField;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

/**
 * The type Package choose text field.
 *
 * @author duanledexianxianxian
 */
public class PackageChooseTextField extends ComponentWithBrowseButton<JTextField> implements TextAccessor {
    private ActionListener actionListener;

    /**
     * Instantiates a new Package choose text field.
     */
    public PackageChooseTextField() {
        super(Experiments.getInstance().isFeatureEnabled("inline.browse.button") ? new ExtendableTextField(10) : new JBTextField(10), null);
    }

    /**
     * Gets text field.
     *
     * @return the text field
     */
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

    /**
     * Is editable boolean.
     *
     * @return the boolean
     */
    public boolean isEditable() {
        return getTextField().isEditable();
    }

    /**
     * Sets editable.
     *
     * @param b the b
     */
    public void setEditable(boolean b) {
        getTextField().setEditable(b);
        getButton().setFocusable(!b);
    }

    /**
     * Sets action listener.
     *
     * @param title   the title
     * @param project the project
     * @param frame   the frame
     */
    public void setActionListener(String title, Project project, @Nullable JFrame frame) {
        this.setActionListener(title, project, frame, null);
    }

    /**
     * Sets action listener.
     *
     * @param title    the title
     * @param project  the project
     * @param frame    the frame
     * @param callback the callback
     */
    public void setActionListener(String title, Project project, @Nullable JFrame frame, @Nullable Consumer<String> callback) {
        if (super.listenerList.getListenerCount() > 0) {
            super.removeActionListener(this.actionListener);
        }
        this.actionListener = e -> {
            PackageChooserDialog chooser = new PackageChooserDialog(title, project);
            chooser.selectPackage(this.getText());
            //chooser.show();
            final PsiPackage aPackage = chooser.getSelectedPackage();
            if (aPackage != null) {
                this.setText(aPackage.getQualifiedName());
                if (callback != null) {
                    callback.accept(aPackage.getQualifiedName());
                }
            }
            if (frame != null) {
                frame.toFront();
            }
        };
        super.addActionListener(this.actionListener);
    }
}
