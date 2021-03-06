package com.github.fengyuchenglun.zilean.action

import com.intellij.database.psi.DbTable
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.ui.Messages
import com.github.fengyuchenglun.zilean.form.ConfigForm

class GenerationAction : AnAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val data = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY)
        // 表节点与数据源节点
//        if (data == null || (data.filterIsInstance<DbTable>().isEmpty() && data.filterIsInstance<DasNamespace>().isEmpty())) {
        if (data == null || data.filterIsInstance<DbTable>().isEmpty()) {
            Messages.showMessageDialog("请至少选择一张数据表", "提示", Messages.getInformationIcon())
            return
        }
        ConfigForm("MyBatis-Plus 代码生产工具", e)
    }

    override fun update(e: AnActionEvent) {
        val data = e.getData(LangDataKeys.PSI_ELEMENT_ARRAY)
        // 表节点与数据源节点
//        e.presentation.isEnabledAndVisible = data != null && (data.filterIsInstance<DbTable>().isNotEmpty() || data.filterIsInstance<DasNamespace>().isNotEmpty())
        e.presentation.isEnabledAndVisible = data != null && (data.filterIsInstance<DbTable>().isNotEmpty())
    }
}