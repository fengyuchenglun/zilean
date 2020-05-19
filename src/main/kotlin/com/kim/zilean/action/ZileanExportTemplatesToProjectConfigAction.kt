package com.kim.zilean.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.kim.zilean.util.TemplateUtils

class ZileanExportTemplatesToProjectConfigAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        TemplateUtils.exportTemplates(e.project, "/.idea/zilean")
    }

    override fun update(e: AnActionEvent) {
        this.templatePresentation.isEnabledAndVisible = e.project != null
    }
}