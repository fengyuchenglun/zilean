package com.kim.zilean.action

//import cn.cnscoo.batiso.utils.TemplateUtils
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ZileanExportTemplatesToProjectConfigAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
//        TemplateUtils.exportTemplates(e.project, "/.idea/batiso")
    }

    override fun update(e: AnActionEvent) {
        this.templatePresentation.isEnabledAndVisible = e.project != null
    }
}