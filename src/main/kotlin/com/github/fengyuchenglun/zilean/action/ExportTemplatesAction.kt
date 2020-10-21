package com.github.fengyuchenglun.zilean.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.github.fengyuchenglun.zilean.util.TemplateUtils

class ExportTemplatesAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        TemplateUtils.exportTemplates(e.project, "/.zilean")
    }

    override fun update(e: AnActionEvent) {
        this.templatePresentation.isEnabledAndVisible = e.project != null
    }
}