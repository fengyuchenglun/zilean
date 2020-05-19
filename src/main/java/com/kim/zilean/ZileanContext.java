package com.kim.zilean;

import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.kim.zilean.form.ConfigForm;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;


/**
 * The type Zilean context.
 */
@Slf4j
public class ZileanContext {
    /**
     * 当前项目
     */
    private Project project;
    /**
     * 当前页面实例
     */
    private ConfigForm configForm;

    /**
     * xml路径
     */
    private String baseXmlPath;
    private AnActionEvent action;
    /**
     * 所有的表结构
     */
    private List<String> tableList;
    /**
     * 表名称与表对象map结构
     */
    private Map<String, DbTable> tableMap;
    /**
     * 当前选中的表
     */
    private List<String> selectedTableList;

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

    /**
     * Gets config form.
     *
     * @return the config form
     */
    public ConfigForm getConfigForm() {
        return configForm;
    }

    /**
     * Sets config form.
     *
     * @param configForm the config form
     */
    public void setConfigForm(ConfigForm configForm) {
        this.configForm = configForm;
    }

    /**
     * Gets project.
     *
     * @return the project
     */
    public Project getProject() {
        return project;
    }

    /**
     * Sets project.
     *
     * @param project the project
     */
    public void setProject(Project project) {
        this.project = project;
    }


    /**
     * Gets data cache file.
     *
     * @return the data cache file
     */
    public String getDataCacheFile() {
        return this.project == null ? null : this.project.getBasePath() + "/.idea/zilean/dataCache.json";
    }


    /**
     * Gets table list.
     *
     * @return the table list
     */
    public List<String> getTableList() {
        return tableList;
    }

    /**
     * Sets table list.
     *
     * @param tableList the table list
     */
    public void setTableList(List<String> tableList) {
        this.tableList = tableList;
    }

    /**
     * Gets table map.
     *
     * @return the table map
     */
    public Map<String, DbTable> getTableMap() {
        return tableMap;
    }

    /**
     * Sets table map.
     *
     * @param tableMap the table map
     */
    public void setTableMap(Map<String, DbTable> tableMap) {
        this.tableMap = tableMap;
    }

    /**
     * Gets selected table list.
     *
     * @return the selected table list
     */
    public List<String> getSelectedTableList() {
        return selectedTableList;
    }

    /**
     * Sets selected table list.
     *
     * @param selectedTableList the selected table list
     */
    public void setSelectedTableList(List<String> selectedTableList) {
        this.selectedTableList = selectedTableList;
    }

    public String getBaseXmlPath() {
        return baseXmlPath;
    }

    public void setBaseXmlPath(String baseXmlPath) {
        this.baseXmlPath = baseXmlPath;
    }


    private static class ZileanContextInstance {
        /**
         * The constant INSTANCE.
         */
        private static final ZileanContext INSTANCE = new ZileanContext();
    }


}
