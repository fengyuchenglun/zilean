package com.kim.zilean.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.kim.zilean.util.TemplateUtils

class ZileanExportTemplatesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        TemplateUtils.exportTemplates(e.project, "/.zilean")
    }

    override fun update(e: AnActionEvent) {
        this.templatePresentation.isEnabledAndVisible = e.project != null
    }
}