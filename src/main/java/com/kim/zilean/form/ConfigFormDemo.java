package com.kim.zilean.form;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.collect.Lists;
import com.intellij.database.model.DasObject;
import com.intellij.database.model.ObjectKind;
import com.intellij.database.psi.DbElement;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.kim.zilean.KimPlusAutoGenerator;
import com.kim.zilean.ZileanContext;
import com.kim.zilean.form.component.PackageChooseTextField;
import com.kim.zilean.model.ConfigModel;
import com.kim.zilean.util.ZileanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.kim.zilean.constant.Constants.*;

/**
 * The type Config form demo.
 */
public class ConfigFormDemo extends JFrame {
    //-------- 其他变量-------------
    private final AnActionEvent action;
    private final Project project;
    private JPanel basePane;
    /**
     * 表列表
     */
    private JBList<String> tableList;
    private JButton selectBtn;
    private JButton okBtn;
    private JButton cancelBtn;
    private JButton previousConfigBtn;
    private JCheckBox lombokCheckBox;


    //---------基本配置-----------
    /**
     * 生成代码基础路径
     */
    private TextFieldWithBrowseButton outputDirField;
    /**
     * 包路径
     */
    private PackageChooseTextField parentField;
    /**
     * 模块名称
     */
    private JTextField moduleNameField;

    /**
     * 表前缀
     */
    private JTextField tablePrefixField;
    /**
     * 开发人员
     */
    private JTextField authorField;
    //---------------domain---------------
    /**
     * 逻辑字段
     */
    private JTextField logicDeleteField;
    /**
     * entity
     */
    private JCheckBox entityCheckbox;
    private PackageChooseTextField entityPackageField;
    private JTextField entitySuffixField;
    /**
     * dto
     */
    private JCheckBox dtoCheckbox;
    private PackageChooseTextField dtoPackageField;
    private JTextField dtoSuffixField;
    /**
     * vo
     */
    private JCheckBox voCheckbox;
    private PackageChooseTextField voPackageField;
    private JTextField voSuffixField;
    /**
     * query
     */
    private JCheckBox queryCheckbox;
    private PackageChooseTextField queryPackageField;
    private JTextField querySuffixField;
    /**
     * form
     */
    private JCheckBox formCheckbox;

    //---------------工程结构---------------
    private PackageChooseTextField formPackageField;
    private JTextField formSuffixField;
    /**
     * dao
     */
    private JCheckBox daoCheckbox;
    private PackageChooseTextField daoPackageField;
    private JTextField daoSuffixField;
    /**
     * mapper xml
     */
    private JCheckBox xmlCheckbox;
    private PackageChooseTextField xmlPathField;
    private JTextField xmlSuffixField;
    /**
     * service
     */
    private JCheckBox serviceCheckbox;
    private PackageChooseTextField servicePackageField;
    private JTextField serviceSuffixField;
    /**
     * serviceImpl
     */
    private JCheckBox serviceImplCheckbox;
    private PackageChooseTextField serviceImplPackageField;
    private JTextField serviceImplSuffixField;
    /**
     * controller
     */
    private JCheckBox controllerCheckbox;
    private PackageChooseTextField controllerPackageField;
    private JTextField controllerSuffixField;
    private JTextField controllerUrlPrefixField;

    //------------基本配置-------------
    private JCheckBox entityLombokModelField;
    private JCheckBox logicDeleteFieldNameField;

    /**
     * 表名与表对象map
     */
    private Map<String, DbTable> tables;

    /**
     * Instantiates a new Config form.
     *
     * @param name   the name
     * @param action the action
     * @throws HeadlessException the headless exception
     */
    public ConfigFormDemo(String name, AnActionEvent action) throws HeadlessException {
        this.action = action;
        this.project = action.getProject();
        ZileanContext.getInstance().setConfigForm(this);
        ZileanContext.getInstance().setProject(this.project);

        this.setTitle(name);
        this.setPreferredSize(new Dimension(1000, 800));
        this.setContentPane(basePane);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.rootPane.setDefaultButton(okBtn);
        this.setVisible(true);


        // 初始化数据
        this.initData();
        this.bindListeners();
    }


    /**
     * 初始化数据
     */
    private void initData() {
        PsiElement[] elements = action.getData(LangDataKeys.PSI_ELEMENT_ARRAY);
        if (elements == null || elements.length == 0) {
            this.dispose();
            return;
        }
        DbTable table = (DbTable) elements[0];
        // 选择的表列表
        java.util.List<DbTable> selectedTables = Arrays.stream(elements).filter(i -> i instanceof DbTable).map(i -> (DbTable) i).collect(Collectors.toList());
        DbElement parent = table.getDasParent();
        // tables表示所有的表
        List<DbTable> tables = parent == null ? selectedTables : parent.getDasChildren(ObjectKind.TABLE).filter(i -> i instanceof DbTable).map(i -> (DbTable) i).toList();
        this.tables = tables.parallelStream().collect(Collectors.toMap(DasObject::getName, i -> i));
        Vector<String> tableNames = tables.stream().map(DasObject::getName).collect(Collectors.toCollection(Vector::new));
        // 设置表列表
        this.tableList.setListData(tableNames);
        // 选择的表
        this.tableList.setSelectedIndices(selectedTables.stream().mapToInt(i -> tableNames.indexOf(i.getName())).toArray());

        if (project != null) {
            this.outputDirField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/java"));
            this.xmlPathField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/resources/mappers"));
//            if (dataCacheFile != null && new File(dataCacheFile).isFile()) {
//                this.cacheBtn.setEnabled(true);
//            }
        }
//        this.commonColumnsField.setText("id,create_time,update_time");
        this.logicDeleteField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.controllerUrlPrefixField.setText(DEFAULT_CONTROLLER_URL_PREFIX);
        this.authorField.setText(StringUtils.isBlank(System.getProperty("user.name")) ? System.getProperty("user.name") : DEFAULT_AUTHOR);

        this.entitySuffixField.setText(DEFAULT_ENTITY_SUFFIX);
        this.dtoSuffixField.setText(DEFAULT_DTO_SUFFIX);
        this.voSuffixField.setText(DEFAULT_VO_SUFFIX);
        this.querySuffixField.setText(DEFAULT_QUERY_SUFFIX);
        this.formSuffixField.setText(DEFAULT_FORM_SUFFIX);


        this.daoSuffixField.setText(DEFAULT_DAO_SUFFIX);
        this.xmlSuffixField.setText(DEFAULT_MAPPER_SUFFIX);
        this.serviceSuffixField.setText(DEFAULT_SERVICE_SUFFIX);
        this.serviceImplSuffixField.setText(DEFAULT_SERVICE_IMPL_SUFFIX);
        this.controllerSuffixField.setText(DEFAULT_CONTROLLER_SUFFIX);


    }


    /**
     * 绑定事件
     */
    private void bindListeners() {
        FileChooserDescriptor folderChooser = new FileChooserDescriptor(false, true, false, false, false, false);
        this.outputDirField.addActionListener(e -> {
            FileChooser.chooseFile(folderChooser, project, null, x -> {
                this.outputDirField.setText(x.getPath());
                this.setAlwaysOnTop(true);
            });
        });

        this.xmlPathField.addActionListener(e -> {
            FileChooser.chooseFile(folderChooser, project, null, x -> {
                this.xmlPathField.setText(x.getPath());
                this.setAlwaysOnTop(true);
            });
        });


        // 包路径
        this.parentField.setActionListener("选择包路径", project, this, x -> {
            this.updatePackagePath();
        });

        this.moduleNameField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                ZileanContext.getInstance().getConfigForm().updatePackagePath();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                ZileanContext.getInstance().getConfigForm().updatePackagePath();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                System.out.println("changedUpdate");
            }
        });


        this.entityPackageField.setActionListener("选择Entity包", project, this);
        this.dtoPackageField.setActionListener("选择DTO包", project, this);
        this.voPackageField.setActionListener("选择VO包", project, this);
        this.queryPackageField.setActionListener("选择Query包", project, this);
        this.formPackageField.setActionListener("选择Form包", project, this);

        this.daoPackageField.setActionListener("选择Dao包", project, this);
        this.servicePackageField.setActionListener("选择Service包", project, this);
        this.serviceImplPackageField.setActionListener("选择ServiceImpl包", project, this);
        this.controllerPackageField.setActionListener("选择Controller包", project, this);


        // ok按钮
        this.okBtn.addActionListener(e -> this.generateCode());
        // 销毁窗口
        this.cancelBtn.addActionListener(e -> this.dispose());
        this.previousConfigBtn.addActionListener(e -> this.initDataFromCache());
    }

    private void updatePackagePath() {
        String parent = this.parentField.getText();
        String moduleName = this.moduleNameField.getText();
        String basePackage = parent;
        if (StringUtils.isNotBlank(moduleName)) {
            basePackage = ZileanUtils.joinPackage(parent, moduleName + DOT);
        }
        this.entityPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.entity"));
        this.dtoPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.dto"));
        this.voPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.vo"));
        this.queryPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.query"));
        this.formPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.form"));

        this.daoPackageField.setText(ZileanUtils.joinPackage(basePackage, "dao"));
        this.servicePackageField.setText(ZileanUtils.joinPackage(basePackage, "service"));
        this.serviceImplPackageField.setText(ZileanUtils.joinPackage(basePackage, "service.impl"));
        this.controllerPackageField.setText(ZileanUtils.joinPackage(basePackage, "controller"));
    }


    private ConfigModel buildConfigModel() {
        ConfigModel data = new ConfigModel();
        data.setOutputDir(this.outputDirField.getText());
        data.setTablePrefix(this.tablePrefixField.getText());
        data.setCommonColumn(this.commonColumnsField.getText());
        data.setLogicColumn(this.logicColumnField.getText());
        data.setEntitySuperClass(this.entitySuperClassField.getText());
        data.setColumnConst(this.columnConstCheckbox.isSelected());
        data.setLombok(this.lombokCheckBox.isSelected());
        data.setTinyint2boolean(this.tinyintCheckbox.isSelected());
        data.setSwagger(this.swaggerCheckbox.isSelected());

        GenerateData.Types types = new GenerateData.Types();
        types.setEntity(new GenerateData.Type(this.entityPkgField.getText(), this.entitySuffixField.getText(), this.entityCheckbox.isSelected()));
        types.setDao(new GenerateData.Type(this.daoPkgField.getText(), this.daoSuffixField.getText(), this.daoCheckbox.isSelected()));
        types.setMapper(new GenerateData.Type(this.mapperPathField.getText(), this.mapperSuffixField.getText(), this.mapperCheckbox.isSelected()));
        types.setService(new GenerateData.Type(this.servicePkgField.getText(), this.serviceSuffixField.getText(), this.serviceCheckbox.isSelected()));
        types.setServiceImpl(new GenerateData.Type(this.serviceImplPkgField.getText(), this.serviceImplSuffixField.getText(), this.serviceImplCheckbox.isSelected()));
        data.setTypes(types);

        data.setTables(this.tablesList.getSelectedValuesList().parallelStream().map(i -> buildTableData(data, tables.get(i))).collect(Collectors.toList()));
        return data;
    }


    /**
     * 生成代码
     */
    private void generateCode() {
        if (this.tableList.getSelectedIndices().length == 0) {
            Messages.showMessageDialog(project, "请至少选择一个表", "提示", Messages.getInformationIcon());
            return;
        }


        ConfigModel configModel = null;
        GlobalConfig config = new GlobalConfig()
                .setActiveRecord(true)
                .setAuthor(configModel.getAuthor())
                .setOutputDir(configModel.getOutputDir())
                .setIdType(IdType.AUTO)
                .setFileOverride(true);

        StrategyConfig strategyConfig = new StrategyConfig();
        String tableNames = Lists.transform(KvnPluginContext.instance().getDbTableList(), new Function<DbTable, String>() {
            public String apply(DbTable dbTable) {
                return dbTable.getName();
            }
        }).stream().collect(Collectors.joining(","));
        strategyConfig
                .setCapitalMode(true)
                .setEntityLombokModel(true) // lombok支持
                .setDbColumnUnderline(true)
                .setNaming(NamingStrategy.underline_to_camel)
//                .setTablePrefix("ts_")
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组

        PackageConfig packageConfig = new PackageConfig()
                .setParent(KvnPluginContext.instance().getPackageName())
                .setModuleName(null)
                .setController("controller")
                .setEntity("entity");

        // 当前使用的代码生成模板
        TemplateConfig template = new TemplateConfig(PersistentConfig.instance().getCurrTemplateGroupName(), selectTemplateList);
//        Debugger.debug(template);
        new KimPlusAutoGenerator().setTemplate(template).setGlobalConfig(config).setStrategy(strategyConfig).setPackageInfo(packageConfig).setDbTables(KvnPluginContext.instance().getDbTableList()).execute();
    }

    private void initDataFromCache() {

    }

}
