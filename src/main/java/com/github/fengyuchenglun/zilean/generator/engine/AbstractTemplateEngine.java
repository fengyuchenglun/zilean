
package com.github.fengyuchenglun.zilean.generator.engine;


import com.github.fengyuchenglun.zilean.model.Config;
import com.github.fengyuchenglun.zilean.model.PackageConfig;
import com.github.fengyuchenglun.zilean.model.PackageConfigs;
import com.github.fengyuchenglun.zilean.model.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 模板引擎抽象类
 * @author duanledexianxian
 */
@Slf4j
public abstract class AbstractTemplateEngine {

    /**
     * 配置信息
     */
    private Config config;


    /**
     * 模板引擎初始化
     *
     * @param config the config
     * @return the abstract template engine
     */
    public AbstractTemplateEngine init(Config config) {
        this.config = config;
        return this;
    }


    private Map<String, Object> getObjectMap(TableInfo tableInfo, Config config, PackageConfig packageConfig) {
        Map<String, Object> dataMap = new HashMap<>(4);
        if (packageConfig.isNeed()) {
            dataMap.put("config", config);
            dataMap.put("table", tableInfo);
            dataMap.put("packageConfig", packageConfig);
            dataMap.put("date", new Date());
        }
        return dataMap;
    }

    /**
     * 输出 java xml 文件
     *
     * @return the abstract template engine
     */
    public AbstractTemplateEngine batchOutput() {
        try {
            PackageConfigs packageConfigs = getConfig().getPackageConfigs();
            List<TableInfo> tableInfoList = getConfig().getTables();
            for (TableInfo tableInfo : tableInfoList) {
                mkdirs(tableInfo.getEntity().getFilePath());
                if (isCreate(tableInfo.getEntity().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getEntity()), templateFilePath("entity"), tableInfo.getEntity().getFileName());
                }
                if (isCreate(tableInfo.getDto().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getDto()), templateFilePath("dto"), tableInfo.getDto().getFileName());
                }
                if (isCreate(tableInfo.getVo().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getVo()), templateFilePath("vo"), tableInfo.getVo().getFileName());
                }
                if (isCreate(tableInfo.getQuery().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getQuery()), templateFilePath("query"), tableInfo.getQuery().getFileName());
                }
                if (isCreate(tableInfo.getForm().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getForm()), templateFilePath("form"), tableInfo.getForm().getFileName());
                }
                if (isCreate(tableInfo.getDao().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getDao()), templateFilePath("dao"), tableInfo.getDao().getFileName());
                }
                if (isCreate(tableInfo.getXmlPath())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getXml()), templateFilePath("xml"), tableInfo.getXmlPath());
                }
                if (isCreate(tableInfo.getService().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getService()), templateFilePath("service"), tableInfo.getService().getFileName());
                }
                if (isCreate(tableInfo.getServiceImpl().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getServiceImpl()), templateFilePath("service-impl"), tableInfo.getServiceImpl().getFileName());
                }
                if (isCreate(tableInfo.getController().getFileName())) {
                    writer(getObjectMap(tableInfo, config, packageConfigs.getController()), templateFilePath("controller"), tableInfo.getController().getFileName());
                }
            }
        } catch (Exception e) {
            log.error("无法创建文件，请检查配置信息！", e);
        }
        return this;
    }


    /**
     * 将模板转化成为文件
     *
     * @param objectMap    渲染对象 MAP 信息
     * @param templatePath 模板文件
     * @param outputFile   文件生成的目录
     * @throws Exception the exception
     */
    public abstract void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception;

    /**
     * 处理输出目录
     *
     * @return the abstract template engine
     */
    public AbstractTemplateEngine mkdirs(String path) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean result = dir.mkdirs();
            if (result) {
                log.debug("创建目录： [" + path + "]");
            }
        }
        return this;
    }


    /**
     * 打开输出目录
     */
    public void open() {
        String outDir = getConfig().getBasePath();
        if (getConfig().isOpen()
                && StringUtils.isNotBlank(outDir)) {
            try {
                String osName = System.getProperty("os.name");
                if (osName != null) {
                    if (osName.contains("Mac")) {
                        Runtime.getRuntime().exec("open " + outDir);
                    } else if (osName.contains("Windows")) {
                        Runtime.getRuntime().exec("cmd /c start " + outDir);
                    } else {
                        log.debug("文件输出目录:" + outDir);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 模板真实文件路径
     *
     * @param filePath 文件路径
     * @return ignore string
     */
    public abstract String templateFilePath(String filePath);


    /**
     * 检测文件是否存在
     *
     * @param filePath the file path
     * @return 文件是否存在 boolean
     */
    protected boolean isCreate(String filePath) {
        // 全局判断【默认】
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist) {
            file.getParentFile().mkdirs();
        }
        return !exist || getConfig().isFileOverride();
    }


    /**
     * Gets config builder.
     *
     * @return the config builder
     */
    public Config getConfig() {
        return config;
    }

    /**
     * Sets config builder.
     *
     * @param config the config
     * @return the config builder
     */
    public AbstractTemplateEngine setConfig(Config config) {
        this.config = config;
        return this;
    }
}
