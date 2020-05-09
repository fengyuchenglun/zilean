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
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.kim.zilean.form.component.PackageChooseTextField;

import javax.swing.*;
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
    private JButton 加载上次配置Button;
    private JCheckBox lombokCheckBox1;
    private JCheckBox 覆盖原文件CheckBox;


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
    private JTextField textField1;

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

        this.entityPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.dtoPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.voPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.queryPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.formPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);

        this.daoPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.servicePackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.serviceImplPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);
        this.controllerPackageField.setText(DEFAULT_LOGIC_DELETE_FIELD);

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

        this.entityPackageField.setActionListener("选择Entity包", project, this);
        this.dtoPackageField.setActionListener("选择DTO包", project, this);
        this.voPackageField.setActionListener("选择VO包", project, this);
        this.queryPackageField.setActionListener("选择Query包", project, this);
        this.formPackageField.setActionListener("选择Form包", project, this);

        this.daoPackageField.setActionListener("选择Dao包", project, this);
        this.servicePackageField.setActionListener("选择Service包", project, this);
        this.serviceImplPackageField.setActionListener("选择ServiceImpl包", project, this);
        this.controllerPackageField.setActionListener("选择Controller包", project, this);


    }


}
