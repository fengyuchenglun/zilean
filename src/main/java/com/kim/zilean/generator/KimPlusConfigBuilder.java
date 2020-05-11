package com.kim.zilean.generator;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.po.TableField;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.intellij.database.model.DasColumn;
import com.intellij.database.psi.DbTable;
import com.intellij.database.util.DasUtil;
import com.intellij.util.containers.JBIterable;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 配置汇总 传递给文件生成工具
 */
public class KimPlusConfigBuilder extends ConfigBuilder {
    /**
     * 过滤正则
     */
    private static final Pattern REGX = Pattern.compile("[~!/@#$%^&*()-_=+\\\\|[{}];:\\'\\\",<.>/?]+");
    /**
     * 模板路径配置信息
     */
    private final TemplateConfig template;
    private final Collection<DbTable> dbTables;
    /**
     * SQL语句类型
     */
    private IDbQuery dbQuery;
    private String superEntityClass;
    private String superMapperClass;
    /**
     * service超类定义
     */
    private String superServiceClass;
    private String superServiceImplClass;
    private String superControllerClass;
    /**
     * 数据库表信息
     */
    private List<TableInfo> tableInfoList;
    /**
     * 包配置详情
     */
    private Map<String, String> packageInfo;
    /**
     * 路径配置信息
     */
    private Map<String, String> pathInfo;
    /**
     * 策略配置
     */
    private StrategyConfig strategyConfig;
    /**
     * 全局配置信息
     */
    private GlobalConfig globalConfig;
    /**
     * 注入配置信息
     */
    private InjectionConfig injectionConfig;
    /**
     * 是否支持注释
     */
    private boolean commentSupported;
    // FIXME 根据DB来选择
    private ITypeConvert typeConvert = new MySqlTypeConvert();

    /**
     * 在构造器中处理配置
     *  @param packageConfig  包配置
     * @param strategyConfig 表配置
     * @param template       模板配置
     * @param globalConfig   全局配置
     * @param dbTables       the db tables
     */
    public KimPlusConfigBuilder(PackageConfig packageConfig,
                                StrategyConfig strategyConfig,
                                TemplateConfig template,
                                GlobalConfig globalConfig,
                                Collection<DbTable> dbTables) {
        super(packageConfig,null,strategyConfig,template,globalConfig);
        this.dbTables = dbTables;
        // 全局配置
        if (null == globalConfig) {
            this.globalConfig = new GlobalConfig();
        } else {
            this.globalConfig = globalConfig;
        }
        // 模板配置
        if (null == template) {
            this.template = new TemplateConfig();
        } else {
            this.template = template;
        }
        // 包配置
        if (null == packageConfig) {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), new PackageConfig());
        } else {
            handlerPackage(this.template, this.globalConfig.getOutputDir(), packageConfig);
        }
        // 策略配置
        if (null == strategyConfig) {
            this.strategyConfig = new StrategyConfig();
        } else {
            this.strategyConfig = strategyConfig;
        }
        //SQLITE 数据库不支持注释获取
        //commentSupported = !dataSourceConfig.getDbType().equals(DbType.SQLITE);

        handlerStrategy(dbTables);
    }

    // ************************ 曝露方法 BEGIN*****************************

    /**
     * 所有包配置信息
     *
     * @return 包配置 package info
     */
    public Map<String, String> getPackageInfo() {
        return packageInfo;
    }


    /**
     * 所有路径配置
     *
     * @return 路径配置 path info
     */
    public Map<String, String> getPathInfo() {
        return pathInfo;
    }


    /**
     * Gets super entity class.
     *
     * @return the super entity class
     */
    public String getSuperEntityClass() {
        return superEntityClass;
    }


    /**
     * Gets super mapper class.
     *
     * @return the super mapper class
     */
    public String getSuperMapperClass() {
        return superMapperClass;
    }


    /**
     * 获取超类定义
     *
     * @return 完整超类名称 super service class
     */
    public String getSuperServiceClass() {
        return superServiceClass;
    }


    /**
     * Gets super service impl class.
     *
     * @return the super service impl class
     */
    public String getSuperServiceImplClass() {
        return superServiceImplClass;
    }


    /**
     * Gets super controller class.
     *
     * @return the super controller class
     */
    public String getSuperControllerClass() {
        return superControllerClass;
    }


    /**
     * 表信息
     *
     * @return 所有表信息 table info list
     */
    public List<TableInfo> getTableInfoList() {
        return tableInfoList;
    }

    /**
     * Sets table info list.
     *
     * @param tableInfoList the table info list
     * @return the table info list
     */
    public KimPlusConfigBuilder setTableInfoList(List<TableInfo> tableInfoList) {
        this.tableInfoList = tableInfoList;
        return this;
    }


    /**
     * 模板路径配置信息
     *
     * @return 所以模板路径配置信息 template
     */
    public TemplateConfig getTemplate() {
        return template == null ? new TemplateConfig() : template;
    }

    // ****************************** 曝露方法 END**********************************

    /**
     * 处理包配置
     *
     * @param template  TemplateConfig
     * @param outputDir
     * @param config    PackageConfig
     */
    private void handlerPackage(TemplateConfig template, String outputDir, PackageConfig config) {
        // 包信息
        packageInfo = new HashMap<>(8);
        packageInfo.put(ConstVal.MODULE_NAME, config.getModuleName());
        packageInfo.put(ConstVal.ENTITY, joinPackage(config.getParent(), config.getEntity()));
        packageInfo.put(ConstVal.MAPPER, joinPackage(config.getParent(), config.getMapper()));
        packageInfo.put(ConstVal.XML, joinPackage(config.getParent(), config.getXml()));
        packageInfo.put(ConstVal.SERVICE, joinPackage(config.getParent(), config.getService()));
        packageInfo.put(ConstVal.SERVICE_IMPL, joinPackage(config.getParent(), config.getServiceImpl()));
        packageInfo.put(ConstVal.CONTROLLER, joinPackage(config.getParent(), config.getController()));

        // 自定义路径
        Map<String, String> configPathInfo = config.getPathInfo();
        if (null != configPathInfo) {
            pathInfo = configPathInfo;
        } else {
            // 生成路径信息
            pathInfo = new HashMap<>(6);
            setPathInfo(pathInfo, template.getEntity(getGlobalConfig().isKotlin()), outputDir, ConstVal.ENTITY_PATH, ConstVal.ENTITY);
            setPathInfo(pathInfo, template.getMapper(), outputDir, ConstVal.MAPPER_PATH, ConstVal.MAPPER);
            setPathInfo(pathInfo, template.getXml(), outputDir, ConstVal.XML_PATH, ConstVal.XML);
            setPathInfo(pathInfo, template.getService(), outputDir, ConstVal.SERVICE_PATH, ConstVal.SERVICE);
            setPathInfo(pathInfo, template.getServiceImpl(), outputDir, ConstVal.SERVICE_IMPL_PATH, ConstVal.SERVICE_IMPL);
            setPathInfo(pathInfo, template.getController(), outputDir, ConstVal.CONTROLLER_PATH, ConstVal.CONTROLLER);
        }
    }

    private void setPathInfo(Map<String, String> pathInfo, String template, String outputDir, String path, String module) {
        if (StringUtils.isNotBlank(template)) {
            pathInfo.put(path, joinPath(outputDir, packageInfo.get(module)));
        }
    }



    /**
     * 处理数据库表 加载数据库表、列、注释相关数据集
     *
     */
    private void handlerStrategy(Collection<DbTable> dbTables) {
        processTypes(strategyConfig);
        tableInfoList = getTablesInfo(dbTables);
    }


    /**
     * 处理superClassName,IdClassType,IdStrategy配置
     *
     * @param config 策略配置
     */
    private void processTypes(StrategyConfig config) {
        if (StringUtils.isBlank(config.getSuperServiceClass())) {
            superServiceClass = ConstVal.SUPER_SERVICE_CLASS;
        } else {
            superServiceClass = config.getSuperServiceClass();
        }
        if (StringUtils.isBlank(config.getSuperServiceImplClass())) {
            superServiceImplClass = ConstVal.SUPER_SERVICE_IMPL_CLASS;
        } else {
            superServiceImplClass = config.getSuperServiceImplClass();
        }
        if (StringUtils.isBlank(config.getSuperMapperClass())) {
            superMapperClass = ConstVal.SUPER_MAPPER_CLASS;
        } else {
            superMapperClass = config.getSuperMapperClass();
        }
        superEntityClass = config.getSuperEntityClass();
        superControllerClass = config.getSuperControllerClass();
    }


    /**
     * 处理表对应的类名称
     *
     * @param tableList 表名称
     * @param strategy  命名策略
     * @param config    策略配置项
     * @return 补充完整信息后的表
     */
    private List<TableInfo> processTable(List<TableInfo> tableList, NamingStrategy strategy, StrategyConfig config) {
        String[] tablePrefix = config.getTablePrefix();
        for (TableInfo tableInfo : tableList) {
            String entityName;
            INameConvert nameConvert = strategyConfig.getNameConvert();
            if (null != nameConvert) {
                // 自定义处理实体名称
                entityName = nameConvert.entityNameConvert(tableInfo);
            } else {
                entityName = NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategy, tablePrefix));
            }
            if (StringUtils.isNotBlank(globalConfig.getEntityName())) {
                tableInfo.setConvert(true);
                tableInfo.setEntityName(String.format(globalConfig.getEntityName(), entityName));
            } else {
                tableInfo.setEntityName(strategyConfig, entityName);
            }
            if (StringUtils.isNotBlank(globalConfig.getMapperName())) {
                tableInfo.setMapperName(String.format(globalConfig.getMapperName(), entityName));
            } else {
                tableInfo.setMapperName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotBlank(globalConfig.getXmlName())) {
                tableInfo.setXmlName(String.format(globalConfig.getXmlName(), entityName));
            } else {
                tableInfo.setXmlName(entityName + ConstVal.MAPPER);
            }
            if (StringUtils.isNotBlank(globalConfig.getServiceName())) {
                tableInfo.setServiceName(String.format(globalConfig.getServiceName(), entityName));
            } else {
                tableInfo.setServiceName("I" + entityName + ConstVal.SERVICE);
            }
            if (StringUtils.isNotBlank(globalConfig.getServiceImplName())) {
                tableInfo.setServiceImplName(String.format(globalConfig.getServiceImplName(), entityName));
            } else {
                tableInfo.setServiceImplName(entityName + ConstVal.SERVICE_IMPL);
            }
            if (StringUtils.isNotBlank(globalConfig.getControllerName())) {
                tableInfo.setControllerName(String.format(globalConfig.getControllerName(), entityName));
            } else {
                tableInfo.setControllerName(entityName + ConstVal.CONTROLLER);
            }
            // 检测导入包
            checkImportPackages(tableInfo);
        }
        return tableList;
    }

    /**
     * 检测导入包
     *
     * @param tableInfo ignore
     */
    private void checkImportPackages(TableInfo tableInfo) {
        if (StringUtils.isNotBlank(strategyConfig.getSuperEntityClass())) {
            // 自定义父类
            tableInfo.getImportPackages().add(strategyConfig.getSuperEntityClass());
        } else if (globalConfig.isActiveRecord()) {
            // 无父类开启 AR 模式
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.extension.activerecord.Model.class.getCanonicalName());
        }
        if (null != globalConfig.getIdType()) {
            // 指定需要 IdType 场景
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotation.IdType.class.getCanonicalName());
            tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotation.TableId.class.getCanonicalName());
        }
        if (StringUtils.isNotBlank(strategyConfig.getVersionFieldName())
                && CollectionUtils.isNotEmpty(tableInfo.getFields())) {
            tableInfo.getFields().forEach(f -> {
                if (strategyConfig.getVersionFieldName().equals(f.getName())) {
                    tableInfo.getImportPackages().add(com.baomidou.mybatisplus.annotation.Version.class.getCanonicalName());
                }
            });
        }
    }


    /**
     * 数据库表处理器
     *
     * @param dbTables   数据库表
     * @return 处理结果
     */
    private List<TableInfo> getTablesInfo(Collection<DbTable> dbTables) {
        String[] tableNames = Collections2.transform(dbTables, new Function<DbTable, String>() {
            @Override
            public String apply(DbTable dbTable) {
                return dbTable.getName();
            }
        }).toArray(new String[]{});
        // FIXME 设置StrategyConfig，后面可以做与配置页面
        strategyConfig = new StrategyConfig()
                .setCapitalMode(true)
                .setEntityLombokModel(true) // lombok支持
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组


        List<TableInfo> result = new ArrayList<>();
        dbTables.forEach(dbTable -> {
            result.add(convertTableFields(dbTable));
        });

        return processTable(result, strategyConfig.getNaming(), strategyConfig);
    }


    /**
     * <p>
     * 将字段信息与表信息关联
     * </p>
     *
     * @param dbTable 表信息
     * @return
     */
    private TableInfo convertTableFields(DbTable dbTable) {
        TableInfo tableInfo = new TableInfo();
        tableInfo.setName(dbTable.getName());
        tableInfo.setComment(dbTable.getComment());
        tableInfo.setEntityName(strategyConfig, NamingStrategy.capitalFirst(processName(tableInfo.getName(), strategyConfig.getNaming(), null)));


        List<TableField> fieldList = new ArrayList<>();
        List<TableField> commonFieldList = new ArrayList<>();


        // 处理所有列
        JBIterable<? extends DasColumn> columns = DasUtil.getColumns(dbTable);
        for (DasColumn column : columns) {
            TableField field = new TableField();
            field.setKeyFlag(DasUtil.isPrimary(column)); // 是否为主键
            field.setKeyIdentityFlag(DasUtil.isAutoGenerated(column)); // 主键是否为自增类型
            // 处理其它信息
            field.setName(column.getName());
            field.setType(column.getDataType().typeName);
            field.setPropertyName(strategyConfig, processName(field.getName(), strategyConfig.getNaming()));
            field.setColumnType(typeConvert.processTypeConvert(globalConfig,field.getType()));
            field.setComment(column.getComment());
            if (strategyConfig.includeSuperEntityColumns(field.getName())) {
                // 跳过公共字段
                commonFieldList.add(field);
                continue;
            }
            // 填充逻辑判断
            List<TableFill> tableFillList = strategyConfig.getTableFillList();
            if (null != tableFillList) {
                for (TableFill tableFill : tableFillList) {
                    if (tableFill.getFieldName().equals(field.getName())) {
                        field.setFill(tableFill.getFieldFill().name());
                        break;
                    }
                }
            }
            fieldList.add(field);
        }
        tableInfo.setFields(fieldList);
        tableInfo.setCommonFields(commonFieldList);
        return tableInfo;
    }



    /**
     * 连接路径字符串
     *
     * @param parentDir   路径常量字符串
     * @param packageName 包名
     * @return 连接后的路径
     */
    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isBlank(parentDir)) {
            parentDir = System.getProperty(ConstVal.JAVA_TMPDIR);
        }
        if (!StringUtils.endsWith(parentDir, File.separator)) {
            parentDir += File.separator;
        }
        packageName = packageName.replaceAll("\\.", StringPool.BACK_SLASH + File.separator);
        return parentDir + packageName;
    }


    /**
     * 连接父子包名
     *
     * @param parent     父包名
     * @param subPackage 子包名
     * @return 连接后的包名
     */
    private String joinPackage(String parent, String subPackage) {
        if (StringUtils.isBlank(parent)) {
            return subPackage;
        }
        return parent + StringPool.DOT + subPackage;
    }


    /**
     * 处理字段名称
     *
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy) {
        return processName(name, strategy, strategyConfig.getFieldPrefix());
    }


    /**
     * 处理表/字段名称
     *
     * @param name     ignore
     * @param strategy ignore
     * @param prefix   ignore
     * @return 根据策略返回处理后的名称
     */
    private String processName(String name, NamingStrategy strategy, String[] prefix) {
        boolean removePrefix = false;
        if (prefix != null && prefix.length != 0) {
            removePrefix = true;
        }
        String propertyName;
        if (removePrefix) {
            if (strategy == NamingStrategy.underline_to_camel) {
                // 删除前缀、下划线转驼峰
                propertyName = NamingStrategy.removePrefixAndCamel(name, prefix);
            } else {
                // 删除前缀
                propertyName = NamingStrategy.removePrefix(name, prefix);
            }
        } else if (strategy == NamingStrategy.underline_to_camel) {
            // 下划线转驼峰
            propertyName = NamingStrategy.underlineToCamel(name);
        } else {
            // 不处理
            propertyName = name;
        }
        return propertyName;
    }


    /**
     * Gets strategy config.
     *
     * @return the strategy config
     */
    public StrategyConfig getStrategyConfig() {
        return strategyConfig;
    }


    /**
     * Sets strategy config.
     *
     * @param strategyConfig the strategy config
     * @return the strategy config
     */
    public KimPlusConfigBuilder setStrategyConfig(StrategyConfig strategyConfig) {
        this.strategyConfig = strategyConfig;
        return this;
    }


    /**
     * Gets global config.
     *
     * @return the global config
     */
    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }


    /**
     * Sets global config.
     *
     * @param globalConfig the global config
     * @return the global config
     */
    public KimPlusConfigBuilder setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
        return this;
    }


    /**
     * Gets injection config.
     *
     * @return the injection config
     */
    public InjectionConfig getInjectionConfig() {
        return injectionConfig;
    }


    /**
     * Sets injection config.
     *
     * @param injectionConfig the injection config
     * @return the injection config
     */
    public KimPlusConfigBuilder setInjectionConfig(InjectionConfig injectionConfig) {
        this.injectionConfig = injectionConfig;
        return this;
    }
}
