package com.kim.zilean.action

import com.intellij.database.model.DasNamespace
import com.intellij.database.psi.DbDataSource
import com.intellij.database.psi.DbTable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.ui.Messages
import com.kim.zilean.form.ConfigForm

class ZileanGenerationAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY)
        // 表节点与数据源节点
        if (data == null || (data.filterIsInstance<DbTable>().isEmpty() && data.filterIsInstance<DbDataSource>().isEmpty())) {
            Messages.showMessageDialog("请至少选择一张数据表", "提示", Messages.getInformationIcon())
            return
        }
        ConfigForm("MyBatis-Plus 代码生产工具", e)
    }

    override fun update(e: AnActionEvent) {
        val data = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY)
        // 表节点与数据源节点
        e.presentation.isEnabledAndVisible = data != null && (data.filterIsInstance<DbTable>().isNotEmpty() || data.filterIsInstance<DasNamespace>().isNotEmpty())
    }
}