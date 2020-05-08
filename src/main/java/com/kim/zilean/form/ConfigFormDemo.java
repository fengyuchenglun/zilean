package com.kim.zilean.form;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.intellij.database.model.DasObject;
import com.intellij.database.model.ObjectKind;
import com.intellij.database.psi.DbElement;
import com.intellij.database.psi.DbTable;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.psi.PsiElement;
import com.intellij.ui.components.JBList;
import com.intellij.util.ui.JBUI;
import com.kim.zilean.form.component.PackageChooseTextField;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.kim.zilean.constant.Constants.DEFAULT_LOGIC_DELETE_FIELD;

/**
 * The type Config form demo.
 */
public class ConfigFormDemo extends JFrame {
    private JPanel basePane;

    /**
     * 表列表
     */
    private JBList<String> tableList;
    private JButton selectBtn;
    private JButton okBtn;
    private JButton cancelBtn;


    private JButton 加载上次配置Button;
    private JTextField textField4;
    private JCheckBox entity列常量CheckBox;
    private JCheckBox tinyint1转BooleanCheckBox;
    private JCheckBox lombokCheckBox;
    private JCheckBox swaggerCheckBox;


    //---------基本配置-----------
    /**
     * 生成代码基础路径
     */
    private TextFieldWithBrowseButton outputDirField;
    /**
     * 表前缀
     */
    private JTextField tablePrefixField;
    /**
     * 逻辑字段
     */
    private JTextField logicDeleteField;
    /**
     * 作者
     */
    private JTextField authorField;


    //---------------domain---------------
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
    private PackageChooseTextField formPackageField;
    private JTextField formSuffixField;

    //---------------工程结构---------------
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
    private PackageChooseTextField xmlPackageField;
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

    //-------- 其他变量-------------
    private final AnActionEvent action;
    private final Project project;
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
        this.setPreferredSize(new Dimension(1000, 1000));
        this.setContentPane(basePane);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.rootPane.setDefaultButton(okBtn);
        this.setVisible(true);


        // 初始化数据
        this.initData();
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
            this.xmlPackageField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/resources/mappers"));
//            if (dataCacheFile != null && new File(dataCacheFile).isFile()) {
//                this.cacheBtn.setEnabled(true);
//            }
        }
//        this.commonColumnsField.setText("id,create_time,update_time");
        this.logicDeleteField.setText(DEFAULT_LOGIC_DELETE_FIELD);
    }

    private void bindListeners() {
        FileChooserDescriptor folderChooser = new FileChooserDescriptor(false, true, false, false, false, false);
        this.outputDirField.addBrowseFolderListener("选择目录", "选择文件生成的目标文件夹", project, folderChooser);
        this.outputDirField.addBrowseFolderListener();

    }
}
