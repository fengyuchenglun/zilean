package com.kim.zilean.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ZileanExportTemplatesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
//        TemplateUtils.exportTemplates(e.project, "/.batiso")
    }

    override fun update(e: AnActionEvent) {
        this.templatePresentation.isEnabledAndVisible = e.project != null
    }
}