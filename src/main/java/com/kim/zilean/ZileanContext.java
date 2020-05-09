package com.kim.zilean;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.kim.zilean.form.ConfigForm;
import com.kim.zilean.form.ConfigFormDemo;

import javax.swing.*;

/**
 * The type Zilean context.
 */
public class ZileanContext {
    /**
     * 当前项目
     */
    private Project project;
    /**
     * 当前页面实例
     */
    private ConfigFormDemo configForm;

    private  AnActionEvent action;

    /**
     * Instantiates a new Zilean context.
     */
    ZileanContext() {

    }


    /**
     * 获取OperationLogHelper实例
     *
     * @return instance instance
     */
    public static ZileanContext getInstance() {
        return ZileanContextInstance.INSTANCE;
    }

    public ConfigFormDemo getConfigForm() {
        return configForm;
    }

    public void setConfigForm(ConfigFormDemo configForm) {
        this.configForm = configForm;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }


    private static class ZileanContextInstance {
        /**
         * The constant INSTANCE.
         */
        private static final ZileanContext INSTANCE = new ZileanContext();
    }


}
