package com.kim.zilean.form;

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
import com.kim.zilean.ZileanContext;
import com.kim.zilean.form.component.PackageChooseTextField;
import com.kim.zilean.generator.KimPlusGeneratorHelper;
import com.kim.zilean.model.Config;
import com.kim.zilean.model.PackageConfig;
import com.kim.zilean.model.PackageConfigs;
import com.kim.zilean.util.ZileanUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.kim.zilean.constant.Constants.*;

/**
 * The type Config form demo.
 */
public class ConfigForm extends JFrame {
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
    private JButton resetBtn;
    private JCheckBox lombokCheckBox;


    //---------基本配置-----------
    /**
     * 生成代码基础路径
     */
    private TextFieldWithBrowseButton basePathField;
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
     * entity
     */
    private JCheckBox entityCheckbox;
    private PackageChooseTextField entityPackageField;
    private JTextField entitySuffixField;
    private PackageChooseTextField dtoPackageField;
    private JTextField dtoSuffixField;
    private PackageChooseTextField voPackageField;
    private JTextField voSuffixField;
    private PackageChooseTextField queryPackageField;
    private JTextField querySuffixField;

    //---------------工程结构---------------
    private PackageChooseTextField formPackageField;
    private JTextField formSuffixField;
    /**
     * dao
     */
    private JCheckBox daoCheckbox;
    private PackageChooseTextField daoPackageField;
    private JTextField daoSuffixField;
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
    private PackageChooseTextField controllerPackageField;
    private JTextField controllerSuffixField;
    private JTextField controllerUrlPrefixField;

    //------------基本配置-------------
    private JTextField commonColumnField;
    private JTextField logicColumnField;
    private JCheckBox isOpenCheckBox;
    private JCheckBox dtoCheckbox;
    private JCheckBox voCheckbox;
    private JCheckBox queryCheckbox;
    private JCheckBox formCheckbox;
    private JCheckBox xmlCheckbox;
    private JCheckBox controllerCheckbox;
    private JCheckBox fileOverrideCheckBox;
    private JCheckBox kotlinCheckBox;
    private JCheckBox swaggerCheckBox;
    private JCheckBox kimCheckBox;
    private JButton previousConfigButton;


    /**
     * Instantiates a new Config form.
     *
     * @param name   the name
     * @param action the action
     * @throws HeadlessException the headless exception
     */
    public ConfigForm(String name, AnActionEvent action) throws HeadlessException {
        this.action = action;
        this.project = action.getProject();
        ZileanContext.getInstance().setConfigForm(this);
        ZileanContext.getInstance().setProject(this.project);

        this.setTitle(name);
        this.setPreferredSize(new Dimension(1000, 900));
        this.setContentPane(basePane);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.rootPane.setDefaultButton(okBtn);
        this.setVisible(true);

        // 初始化数据
        this.initData();
        // 绑定事件
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
        Vector<String> tableNames = tables.stream().map(DasObject::getName).collect(Collectors.toCollection(Vector::new));
        ZileanContext.getInstance().setTableMap(tables.parallelStream().collect(Collectors.toMap(DasObject::getName, i -> i)));
        ZileanContext.getInstance().setTableList(Collections.list(tableNames.elements()));
        // 设置表列表
        this.tableList.setListData(tableNames);
        // 选择的表
        this.tableList.setSelectedIndices(selectedTables.stream().mapToInt(i -> tableNames.indexOf(i.getName())).toArray());

        // 重置按钮是否显示
        if (project != null) {
            String dataCacheFile = ZileanContext.getInstance().getDataCacheFile();
            if (dataCacheFile != null && new File(dataCacheFile).isFile()) {
                this.resetBtn.setEnabled(true);
                this.previousConfigButton.setEnabled(true);
            }
        }

        // 重配置文件中读取
        this.loadConfigCache();
    }


    /**
     * 重置配置
     */
    private void reset() {
        if (project != null) {
            this.basePathField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/java"));
            this.xmlPathField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/resources/mapper"));
        }
        this.parentField.setText(null);
        this.moduleNameField.setText(null);
        // TODO: 2020/5/18 自动推测表前缀
        this.tablePrefixField.setText(null);
        this.authorField.setText(StringUtils.isBlank(System.getProperty("user.name")) ? System.getProperty("user.name") : DEFAULT_AUTHOR);
        this.controllerUrlPrefixField.setText(DEFAULT_CONTROLLER_URL_PREFIX);
        this.logicColumnField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.commonColumnField.setText(DEFAULT_COMMON_COLUMN_FIELD);

        this.entityPackageField.setText(null);
        this.dtoPackageField.setText(null);
        this.voPackageField.setText(null);
        this.formPackageField.setText(null);
        this.queryPackageField.setText(null);

        this.entitySuffixField.setText(DEFAULT_ENTITY_SUFFIX);
        this.dtoSuffixField.setText(DEFAULT_DTO_SUFFIX);
        this.voSuffixField.setText(DEFAULT_VO_SUFFIX);
        this.querySuffixField.setText(DEFAULT_QUERY_SUFFIX);
        this.formSuffixField.setText(DEFAULT_FORM_SUFFIX);

        this.daoPackageField.setText(null);
        this.xmlPathField.setText(null);
        this.serviceImplPackageField.setText(null);
        this.servicePackageField.setText(null);
        this.controllerPackageField.setText(null);

        this.daoSuffixField.setText(DEFAULT_DAO_SUFFIX);
        this.xmlSuffixField.setText(DEFAULT_MAPPER_SUFFIX);
        this.serviceSuffixField.setText(DEFAULT_SERVICE_SUFFIX);
        this.serviceImplSuffixField.setText(DEFAULT_SERVICE_IMPL_SUFFIX);
        this.controllerSuffixField.setText(DEFAULT_CONTROLLER_SUFFIX);

        this.lombokCheckBox.setSelected(true);
        this.fileOverrideCheckBox.setSelected(true);
        this.isOpenCheckBox.setSelected(false);
        this.kotlinCheckBox.setSelected(false);
        this.swaggerCheckBox.setSelected(false);
        this.kimCheckBox.setSelected(false);

    }


    /**
     * 绑定事件
     */
    private void bindListeners() {
        FileChooserDescriptor folderChooser = new FileChooserDescriptor(false, true, false, false, false, false);
        // 选择根路径
        this.basePathField.addActionListener(e -> {
            FileChooser.chooseFile(folderChooser, project, this, null, x -> {
                String basePath = x.getPath();
                this.basePathField.setText(basePath.concat("/src/main/java"));
                String baseXmlPath = basePath + "/src/main/resources/mapper";
                if (StringUtils.isNotBlank(this.moduleNameField.getText())) {
                    baseXmlPath = baseXmlPath + File.separator + this.moduleNameField.getText();
                }
                this.xmlPathField.setText(baseXmlPath);
            });
        });

        this.xmlPathField.addActionListener(e -> {
            FileChooser.chooseFile(folderChooser, project, this, null, x -> {
                this.xmlPathField.setText(x.getPath());
//                this.setAlwaysOnTop(true);
            });
        });

        this.selectBtn.addActionListener(e -> {
            List<String> tableList = ZileanContext.getInstance().getTableList();
            if (this.tableList.getSelectedIndices().length < tableList.size()) {
                this.tableList.setSelectionInterval(0, tableList.size() - 1);
            } else {
                this.tableList.clearSelection();
            }
        });

        //this.tableList.addListSelectionListener(x -> {
        //    System.out.println(x);
        //});


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
        // 重置表单
        this.resetBtn.addActionListener(e -> this.reset());
        // 加载上次配置
        this.previousConfigButton.addActionListener(e -> this.loadConfigCache());
    }

    //private void buildTablePrefix() {
    //    List<String> selectTableList = this.tableList.getSelectedValuesList();
    //    if (CollectionUtils.isNotEmpty(selectTableList)) {
    //        this.tablePrefixField.setText(StringUtils.join(Sets.newHashSet(selectTableList.stream().map(x-> {return StringUtils.lastIndexOf()})), ","));
    //    }
    //}

    /**
     * 更新包路径
     */
    private void updatePackagePath() {
        String parent = this.parentField.getText();
        String moduleName = this.moduleNameField.getText();
        String basePackage = parent;
        String baseXmlPath = this.xmlPathField.getText();
        if (StringUtils.isNotBlank(moduleName)) {
            basePackage = ZileanUtils.joinPackage(parent, moduleName + DOT);
            baseXmlPath = baseXmlPath + File.separator + moduleName;
        }
        this.entityPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.entity"));
        this.dtoPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.dto"));
        this.voPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.model.vo"));
        this.queryPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.model.query"));
        this.formPackageField.setText(ZileanUtils.joinPackage(basePackage, "domain.model.query"));

        this.daoPackageField.setText(ZileanUtils.joinPackage(basePackage, "dao"));
        this.servicePackageField.setText(ZileanUtils.joinPackage(basePackage, "service"));
        this.serviceImplPackageField.setText(ZileanUtils.joinPackage(basePackage, "service.impl"));
        this.controllerPackageField.setText(ZileanUtils.joinPackage(basePackage, "controller"));
        this.xmlPathField.setText(baseXmlPath);
    }


    /**
     * 从页面配置构建新建配置参数
     *
     * @return
     */
    private Config buildConfig() {
        Config config = new Config();
        config.setBasePath(this.basePathField.getText());
        config.setTablePrefix(this.tablePrefixField.getText());
        config.setParent(this.parentField.getText());
        config.setLogicColumn(this.logicColumnField.getText());
        config.setCommonColumn(this.commonColumnField.getText());
        config.setModuleName(this.moduleNameField.getText());
        config.setAuthor(this.authorField.getText());
        config.setControllerUrlPrefix(this.controllerUrlPrefixField.getText());

        PackageConfigs packageConfigs = new PackageConfigs();
        packageConfigs.setEntity(new PackageConfig(this.entityPackageField.getText(), this.entitySuffixField.getText(), this.entityCheckbox.isSelected()));
        packageConfigs.setDto(new PackageConfig(this.dtoPackageField.getText(), this.dtoSuffixField.getText(), this.dtoCheckbox.isSelected()));
        packageConfigs.setVo(new PackageConfig(this.voPackageField.getText(), this.voSuffixField.getText(), this.voCheckbox.isSelected()));
        packageConfigs.setForm(new PackageConfig(this.formPackageField.getText(), this.formSuffixField.getText(), this.formCheckbox.isSelected()));
        packageConfigs.setQuery(new PackageConfig(this.queryPackageField.getText(), this.querySuffixField.getText(), this.queryCheckbox.isSelected()));
        packageConfigs.setDao(new PackageConfig(this.daoPackageField.getText(), this.daoSuffixField.getText(), this.daoCheckbox.isSelected()));
        packageConfigs.setXml(new PackageConfig(this.xmlPathField.getText(), this.xmlSuffixField.getText(), this.xmlCheckbox.isSelected()));
        packageConfigs.setService(new PackageConfig(this.servicePackageField.getText(), this.serviceSuffixField.getText(), this.serviceCheckbox.isSelected()));
        packageConfigs.setServiceImpl(new PackageConfig(this.serviceImplPackageField.getText(), this.serviceImplSuffixField.getText(), this.serviceImplCheckbox.isSelected()));
        packageConfigs.setController(new PackageConfig(this.controllerPackageField.getText(), this.controllerSuffixField.getText(), this.controllerCheckbox.isSelected()));
        config.setPackageConfigs(packageConfigs);

        config.setLombok(this.fileOverrideCheckBox.isSelected());
        config.setOpen(this.isOpenCheckBox.isSelected());
        config.setFileOverride(this.fileOverrideCheckBox.isSelected());
        config.setKim(this.kimCheckBox.isSelected());
        config.setKotlin(this.kotlinCheckBox.isSelected());
        config.setSwagger(this.swaggerCheckBox.isSelected());


        ZileanContext.getInstance().setSelectedTableList(tableList.getSelectedValuesList());
        config.setTables(KimPlusGeneratorHelper.getInstance().getTableInfoList(config));
        return config;
    }


    /**
     * 生成代码
     */
    private void generateCode() {
        if (this.tableList.getSelectedIndices().length == 0) {
            Messages.showMessageDialog(this, "请至少选择一个表", "提示", Messages.getInformationIcon());
            return;
        }

        if (StringUtils.isBlank(this.parentField.getText())) {
            Messages.showMessageDialog(this, "包路径必填", "提示", Messages.getInformationIcon());
            return;
        }

        Config config = this.buildConfig();
        KimPlusGeneratorHelper.getInstance().cacheGeneratorData(config);
        KimPlusGeneratorHelper.getInstance().generate(config);
        Messages.showMessageDialog(this, "代码生成完毕！", "提示", Messages.getInformationIcon());
        this.dispose();
    }

    private void loadConfigCache() {
        Config config = KimPlusGeneratorHelper.getInstance().readCacheData();
        if (config == null) {
            this.reset();
            return;
        }
        PackageConfigs packageConfigs = config.getPackageConfigs();
        this.basePathField.setText(config.getBasePath());
        this.parentField.setText(config.getParent());
        this.moduleNameField.setText(config.getModuleName());
        this.tablePrefixField.setText(config.getTablePrefix());
        this.authorField.setText(config.getAuthor());
        this.controllerUrlPrefixField.setText(config.getControllerUrlPrefix());
        this.logicColumnField.setText(config.getLogicColumn());
        this.commonColumnField.setText(config.getLogicColumn());

        this.entityPackageField.setText(packageConfigs.getEntity().getPkg());
        this.dtoPackageField.setText(packageConfigs.getDto().getPkg());
        this.voPackageField.setText(packageConfigs.getVo().getPkg());
        this.formPackageField.setText(packageConfigs.getForm().getPkg());
        this.queryPackageField.setText(packageConfigs.getQuery().getPkg());

        this.entitySuffixField.setText(packageConfigs.getEntity().getSuffix());
        this.dtoSuffixField.setText(packageConfigs.getDto().getSuffix());
        this.voSuffixField.setText(packageConfigs.getVo().getSuffix());
        this.formSuffixField.setText(packageConfigs.getForm().getSuffix());
        this.querySuffixField.setText(packageConfigs.getQuery().getSuffix());

        this.daoPackageField.setText(packageConfigs.getDao().getPkg());
        this.xmlPathField.setText(packageConfigs.getXml().getPkg());
        this.serviceImplPackageField.setText(packageConfigs.getServiceImpl().getPkg());
        this.servicePackageField.setText(packageConfigs.getService().getPkg());
        this.controllerPackageField.setText(packageConfigs.getController().getPkg());

        this.daoSuffixField.setText(packageConfigs.getDao().getSuffix());
        this.xmlSuffixField.setText(packageConfigs.getXml().getSuffix());
        this.serviceImplSuffixField.setText(packageConfigs.getServiceImpl().getSuffix());
        this.serviceSuffixField.setText(packageConfigs.getService().getSuffix());
        this.controllerSuffixField.setText(packageConfigs.getController().getSuffix());

        this.entityCheckbox.setSelected(packageConfigs.getEntity().isNeed());
        this.dtoCheckbox.setSelected(packageConfigs.getDto().isNeed());
        this.voCheckbox.setSelected(packageConfigs.getVo().isNeed());
        this.queryCheckbox.setSelected(packageConfigs.getQuery().isNeed());
        this.formCheckbox.setSelected(packageConfigs.getForm().isNeed());


        this.daoCheckbox.setSelected(packageConfigs.getDao().isNeed());
        this.xmlCheckbox.setSelected(packageConfigs.getXml().isNeed());
        this.serviceCheckbox.setSelected(packageConfigs.getService().isNeed());
        this.serviceImplCheckbox.setSelected(packageConfigs.getServiceImpl().isNeed());
        this.controllerCheckbox.setSelected(packageConfigs.getController().isNeed());

        this.lombokCheckBox.setSelected(config.isLombok());
        this.kotlinCheckBox.setSelected(config.isKotlin());
        this.swaggerCheckBox.setSelected(config.isSwagger());
        this.fileOverrideCheckBox.setSelected(config.isFileOverride());
        this.isOpenCheckBox.setSelected(config.isOpen());
        this.kimCheckBox.setSelected(config.isKim());
    }


}
