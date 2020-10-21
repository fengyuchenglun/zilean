package com.github.fengyuchenglun.zilean.form.component;

import com.intellij.ide.util.PackageChooserDialog;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiPackage;
import com.intellij.ui.ReferenceEditorComboWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The type Package name reference editor combo.
 *
 * @author duanledexianxianxian
 * @date 2020 /5/18 23:41
 * @since 1.0.0
 */
public class PackageNameReferenceEditorCombo extends ReferenceEditorComboWithBrowseButton {
    /**
     * Instantiates a new Package name reference editor combo.
     *
     * @param text         the text
     * @param project      the project
     * @param recentsKey   the recents key
     * @param chooserTitle the chooser title
     */
    public PackageNameReferenceEditorCombo(final String text, @NotNull final Project project,
                                           final String recentsKey, final String chooserTitle) {
        super(null, text, project, false, recentsKey);
        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final PackageChooserDialog chooser = new PackageChooserDialog(chooserTitle, project);
                chooser.selectPackage(getText());
                chooser.show();
                if (chooser.isOK()) {
                    final PsiPackage aPackage = chooser.getSelectedPackage();
                    if (aPackage != null) {
                        setText(aPackage.getQualifiedName());
                    }
                }
            }
        });
    }
}
