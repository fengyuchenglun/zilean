package com.kim.zilean.form;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.kim.zilean.form.component.ClassChooseTextField;
import com.kim.zilean.form.component.PackageChooseTextField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

import static com.kim.zilean.constant.Constants.DEFAULT_LOGIC_DELETE_FIELD;

/**
 * The type Config form.
 *
 * @author duanledexianxianxian
 */
public class ConfigForm extends JFrame {
    private JPanel basePane;
    /**
     * 表列表
     */
    private JBList<String> tableList;
    /**
     * 全选/反选按钮
     */
    private JButton selectBtn;
    /**
     * 生成按钮
     */
    private JButton okBtn;
    /**
     * 取消按钮
     */
    private JButton cancelBtn;
    /**
     * 生成代码基础路径
     */
    private TextFieldWithBrowseButton basePathField;
    /**
     * 公共字段
     */
    private JTextField commonColumnsField;
    /**
     * 逻辑字段
     */
    private JTextField logicDeleteField;
    //---------------domain---------------
    /**
     * entity
     */
    private JCheckBox entityCheckbox;
    private PackageChooseTextField entityPkgField;
    private JTextField entitySuffixField;
    /**
     * dto
     */
    private JCheckBox dtoCheckbox;
    private PackageChooseTextField dtoPkgField;
    private JTextField dtoSuffixField;
    /**
     * vo
     */
    private JCheckBox voCheckbox;
    private PackageChooseTextField voPkgField;
    private JTextField voSuffixField;
    /**
     * query
     */
    private JCheckBox queryCheckbox;
    private PackageChooseTextField queryPkgField;
    private JTextField querySuffixField;
    /**
     * form
     */
    private JCheckBox formCheckbox;
    private PackageChooseTextField formPkgField;
    private JTextField formSuffixField;

    /**
     * dao
     */
    private JCheckBox daoCheckbox;
    private PackageChooseTextField daoPkgField;
    private JTextField daoSuffixField;
    private PackageChooseTextField servicePkgField;
    private ClassChooseTextField entitySuperClassField;

    private TextFieldWithBrowseButton xmlPathField;


    private PackageChooseTextField serviceImplPkgField;


    private JTextField tablePrefixField;
    private JCheckBox columnConstCheckbox;
    private JCheckBox lombokCheckBox;
    private JCheckBox tinyintCheckbox;
    private JButton cacheBtn;
    private JCheckBox swaggerCheckbox;
    private JTextField textField1;

    private final Project project;
    private final String dataCacheFile;
    private final AnActionEvent action;
    private final ObjectMapper objectMapper = new ObjectMapper();
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
    public ConfigForm(String name, AnActionEvent action) throws HeadlessException {
        this.action = action;
        this.project = action.getProject();
        this.setTitle(name);
        this.setPreferredSize(new Dimension(1000, 600));
        this.setContentPane(basePane);
        this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.pack();
        this.setLocationRelativeTo(null);
        this.rootPane.setDefaultButton(okBtn);
        this.setVisible(true);
        this.dataCacheFile = this.project == null ? null : this.project.getBasePath() + "/.idea/batiso/dataCache.json";
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        this.tableList.setBorder(JBUI.Borders.empty(5));

        // 初始化数据
        this.initData();
        // 绑定监听器
        this.bindListeners();
    }

    private void bindListeners() {
        this.rootPane.registerKeyboardAction(e -> this.dispose(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);
//        bindCheckboxChange(entityCheckbox, entityPkgField, entitySuffixField, entitySuperClassField);
//        bindCheckboxChange(daoCheckbox, daoPkgField, daoSuffixField);
//        bindCheckboxChange(mapperCheckbox, mapperPathField, mapperSuffixField);
//        bindCheckboxChange(serviceCheckbox, servicePkgField, serviceSuffixField);
//        bindCheckboxChange(serviceImplCheckbox, serviceImplPkgField, serviceImplSuffixField);
        this.selectBtn.addActionListener(e -> {
            if (this.tableList.getSelectedIndices().length < this.tables.size()) {
                this.tableList.setSelectionInterval(0, this.tables.size() - 1);
            } else {
                this.tableList.clearSelection();
            }
        });

        FileChooserDescriptor folderChooser = new FileChooserDescriptor(false, true, false, false, false, false);
        this.xmlPathField.addBrowseFolderListener("选择目录", "选择Mapper XML文件生成目标文件夹", project, folderChooser);

        this.entityPkgField.setActionListener("选择Entity包", project, this);

        this.entitySuperClassField.setActionListener("选择Entity父类", project, this);

        this.daoPkgField.setActionListener("选择Dao包", project, this);

        this.servicePkgField.setActionListener("选择Service包", project, this, pkg -> {
            if (this.serviceImplPkgField.getText().isEmpty()) {
                this.serviceImplPkgField.setText(pkg.concat(".impl"));
            }
        });

        this.serviceImplPkgField.setActionListener("选择ServiceImpl包", project, this);

//        this.okBtn.addActionListener(e -> this.generator());

//        this.cancelBtn.addActionListener(e -> this.dispose());

//        this.cacheBtn.addActionListener(e -> this.initDataFromCache());
    }

    private void bindCheckboxChange(JCheckBox checkBox, Component... components) {
        if (components != null && components.length > 0) {
            checkBox.addChangeListener(e -> {
                boolean selected = ((JCheckBox) e.getSource()).isSelected();
                Arrays.stream(components).forEach(i -> i.setEnabled(selected));
            });
        }
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
        List<DbTable> selectedTables = Arrays.stream(elements).filter(i -> i instanceof DbTable).map(i -> (DbTable) i).collect(Collectors.toList());
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
            this.basePathField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/java"));
            this.xmlPathField.setText(Objects.requireNonNull(project.getBasePath()).concat("/src/main/resources/mappers"));
            if (dataCacheFile != null && new File(dataCacheFile).isFile()) {
                this.cacheBtn.setEnabled(true);
            }
        }
        this.commonColumnsField.setText("id,create_time,update_time");
        this.logicDeleteField.setText(DEFAULT_LOGIC_DELETE_FIELD);
    }

//    private void initDataFromCache() {
//        GenerateData data = this.readCacheData();
//        if (data == null) {
//            return;
//        }
//
//        this.basePathField.setText(data.getBasePath());
//        this.commonColumnsField.setText(data.getCommonColumn());
//        this.tablePrefixField.setText(data.getTablePrefix());
//        this.logicDeleteField.setText(data.getLogicColumn());
//
//        this.entityCheckbox.setSelected(data.getTypes().getEntity().isGen());
//        this.entityPkgField.setText(data.getTypes().getEntity().getPkg());
//        this.entitySuffixField.setText(data.getTypes().getEntity().getSuffix());
//        this.entitySuperClassField.setText(data.getEntitySuperClass());
//
//        this.daoCheckbox.setSelected(data.getTypes().getDao().isGen());
//        this.daoPkgField.setText(data.getTypes().getDao().getPkg());
//        this.daoSuffixField.setText(data.getTypes().getDao().getSuffix());
//
//        this.mapperCheckbox.setSelected(data.getTypes().getMapper().isGen());
//        this.mapperPathField.setText(data.getTypes().getMapper().getPkg());
//        this.mapperSuffixField.setText(data.getTypes().getMapper().getSuffix());
//
//        this.serviceCheckbox.setSelected(data.getTypes().getService().isGen());
//        this.servicePkgField.setText(data.getTypes().getService().getPkg());
//        this.serviceSuffixField.setText(data.getTypes().getService().getSuffix());
//
//        this.serviceImplCheckbox.setSelected(data.getTypes().getServiceImpl().isGen());
//        this.serviceImplPkgField.setText(data.getTypes().getServiceImpl().getPkg());
//        this.serviceImplSuffixField.setText(data.getTypes().getServiceImpl().getSuffix());
//
//        this.columnConstCheckbox.setSelected(data.isColumnConst());
//        this.lombokCheckBox.setSelected(data.isLombok());
//        this.tinyintCheckbox.setSelected(data.isTinyint2boolean());
//        this.swaggerCheckbox.setSelected(data.isSwagger());
//    }
//
//    private GenerateData.Class buildClass(GenerateData g, GenerateData.Type type, String singleName, String beanName) {
//        GenerateData.Class clz = new GenerateData.Class();
//        clz.setPkg(type.getPkg());
//        clz.setName(singleName + type.getSuffix());
//        clz.setBeanName(beanName + type.getSuffix());
//        clz.setClassName(clz.getPkg() + "." + clz.getName());
//        clz.setFilePath(g.getBasePath() + "/" + clz.getPkg().replaceAll("\\.", "/"));
//        clz.setFileName(clz.getFilePath() + "/" + clz.getName() + ".java");
//        return clz;
//    }
//
//    private GenerateData.Table buildTableData(GenerateData g, DbTable table) {
//        GenerateData.Table data = new GenerateData.Table();
//        data.setName(table.getName());
//        data.setComment(table.getComment());
//
//        final List<String> imports = new ArrayList<>();
//
//        List<? extends DasColumn> columns = DasUtil.getColumns(table).toList();
//        List<GenerateData.Column> list = columns.parallelStream().map(c -> {
//            GenerateData.Column cd = new GenerateData.Column();
//            cd.setName(c.getName());
//            cd.setFieldName(BatisoUtils.underLineToHump(c.getName()));
//            cd.setDataType(c.getDataType());
//            ColumnDataType type = ColumnDataType.nameOf(c.getDataType().typeName.split(" ")[0]);
//            cd.setJdbcType(type.jdbcType);
//            if (type == ColumnDataType.TINYINT && c.getDataType().size == 1 && g.isTinyint2boolean()) {
//                cd.setJavaType(ColumnDataType.BOOLEAN.javaType);
//            } else {
//                cd.setJavaType(type.javaType);
//            }
//            cd.setComment(c.getComment());
//            cd.setNotNull(c.isNotNull());
//            cd.setPrimaryKey(DasUtil.isPrimary(c));
//            cd.setAutoGenerate(DasUtil.isAutoGenerated(c));
//            cd.setInCommon(g.isCommonColumn(c.getName()));
//            cd.setTableLogic(g.isLogicColumn(c.getName()));
//            cd.setMysqlKeyword(BatisoUtils.MYSQL_KEYWORDS.contains(c.getName().toUpperCase()));
//            if (cd.getJavaType().contains(".")) {
//                imports.add(cd.getJavaType());
//                cd.setSimpleJavaType(cd.getJavaType().substring(cd.getJavaType().lastIndexOf('.') + 1));
//            } else {
//                cd.setSimpleJavaType(cd.getJavaType());
//            }
//            return cd;
//        }).collect(Collectors.toList());
//        data.setColumns(list);
//
//        String noPrefixName = g.getTablePrefix().isEmpty() ? table.getName() : table.getName().replaceFirst("^" + g.getTablePrefix(), "");
//        String singleName = BatisoUtils.toHumpAndUpperCaseFirstLetter(noPrefixName);
//        String beanName = BatisoUtils.underLineToHump(noPrefixName);
//
//        data.setEntity(this.buildClass(g, g.getTypes().getEntity(), singleName, beanName));
//        data.getEntity().setImports(imports.stream().distinct().sorted().collect(Collectors.toList()));
//        data.setDao(this.buildClass(g, g.getTypes().getDao(), singleName, beanName));
//        data.setService(this.buildClass(g, g.getTypes().getService(), singleName, beanName));
//        data.setServiceImpl(this.buildClass(g, g.getTypes().getServiceImpl(), singleName, beanName));
//        data.setMapperPath(g.getTypes().getMapper().getPkg() + "/" + data.getEntity().getName() + g.getTypes().getMapper().getSuffix() + ".xml");
//
//        return data;
//    }
//
//    private GenerateData buildGenerateData() {
//        GenerateData data = new GenerateData();
//        data.setBasePath(this.basePathField.getText());
//        data.setTablePrefix(this.tablePrefixField.getText());
//        data.setCommonColumn(this.commonColumnsField.getText());
//        data.setLogicColumn(this.logicDeleteField.getText());
//        data.setEntitySuperClass(this.entitySuperClassField.getText());
//        data.setColumnConst(this.columnConstCheckbox.isSelected());
//        data.setLombok(this.lombokCheckBox.isSelected());
//        data.setTinyint2boolean(this.tinyintCheckbox.isSelected());
//        data.setSwagger(this.swaggerCheckbox.isSelected());
//
//        GenerateData.Types types = new GenerateData.Types();
//        types.setEntity(new GenerateData.Type(this.entityPkgField.getText(), this.entitySuffixField.getText(), this.entityCheckbox.isSelected()));
//        types.setDao(new GenerateData.Type(this.daoPkgField.getText(), this.daoSuffixField.getText(), this.daoCheckbox.isSelected()));
//        types.setMapper(new GenerateData.Type(this.mapperPathField.getText(), this.mapperSuffixField.getText(), this.mapperCheckbox.isSelected()));
//        types.setService(new GenerateData.Type(this.servicePkgField.getText(), this.serviceSuffixField.getText(), this.serviceCheckbox.isSelected()));
//        types.setServiceImpl(new GenerateData.Type(this.serviceImplPkgField.getText(), this.serviceImplSuffixField.getText(), this.serviceImplCheckbox.isSelected()));
//        data.setTypes(types);
//
//        data.setTables(this.tableList.getSelectedValuesList().parallelStream().map(i -> buildTableData(data, tables.get(i))).collect(Collectors.toList()));
//        return data;
//    }
//
//    private GenerateData readCacheData() {
//        try {
//            if (dataCacheFile != null) {
//                File cacheFile = new File(dataCacheFile);
//                FileUtils.forceMkdirParent(cacheFile);
//                if (cacheFile.isFile()) {
//                    String cache = FileUtils.readFileToString(cacheFile, StandardCharsets.UTF_8);
//                    return this.objectMapper.readValue(cache, GenerateData.class);
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void cacheGeneratorData(GenerateData data) {
//        try {
//            if (dataCacheFile != null) {
//                String cache = this.objectMapper.writeValueAsString(data);
//                FileUtils.writeStringToFile(new File(dataCacheFile), cache, StandardCharsets.UTF_8);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void generator() {
//        if (this.tableList.getSelectedIndices().length == 0) {
//            Messages.showMessageDialog(project, "请至少选择一个表", "提示", Messages.getInformationIcon());
//            return;
//        }
//        GenerateData data = this.buildGenerateData();
//        this.cacheGeneratorData(data);
//        Configuration gt = new Configuration(Configuration.getVersion());
//        boolean hasCustomTemp = false;
//        if (project != null) {
//            File tempDir = new File(project.getBasePath() + "/.batiso/templates");
//            try {
//                if (tempDir.isDirectory() && tempDir.canRead()) {
//                    gt.setDirectoryForTemplateLoading(tempDir);
//                    hasCustomTemp = true;
//                } else {
//                    tempDir = new File(project.getBasePath() + "/.idea/batiso/templates");
//                    if (tempDir.isDirectory() && tempDir.canRead()) {
//                        gt.setDirectoryForTemplateLoading(tempDir);
//                        hasCustomTemp = true;
//                    }
//                }
//            } catch (IOException ignored) {
//            }
//        }
//        if (!hasCustomTemp) {
//            gt.setClassLoaderForTemplateLoading(ConfigForm.class.getClassLoader(), "templates");
//        }
//        gt.setDefaultEncoding("utf-8");
//        data.getTables().forEach(t -> {
//            doGenTemp(t, data.getTypes().getEntity(), gt, "/entity.ftl", t.getEntity().getFileName(), data);
//            doGenTemp(t, data.getTypes().getDao(), gt, "/dao.ftl", t.getDao().getFileName(), data);
//            doGenTemp(t, data.getTypes().getService(), gt, "/service.ftl", t.getService().getFileName(), data);
//            doGenTemp(t, data.getTypes().getServiceImpl(), gt, "/service-impl.ftl", t.getServiceImpl().getFileName(), data);
//            doGenTemp(t, data.getTypes().getMapper(), gt, "/mapper.ftl", t.getMapperPath(), data);
//        });
//        Messages.showMessageDialog(project, "代码生成完毕！", "提示", Messages.getInformationIcon());
//        this.dispose();
//    }
//
//    private void doGenTemp(GenerateData.Table table, GenerateData.Type type, Configuration gt, String tpl, String distFile, GenerateData data) {
//        if (type.isGen()) {
//            try {
//                Map<String, Object> dataModel = new HashMap<>(4);
//                dataModel.put("data", data);
//                dataModel.put("table", table);
//                dataModel.put("type", type);
//                dataModel.put("date", new Date());
//                Template template = gt.getTemplate(tpl, StandardCharsets.UTF_8.name());
//                FileUtils.forceMkdirParent(new File(distFile));
//                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(distFile), StandardCharsets.UTF_8);
//                template.process(dataModel, osw);
//                osw.close();
//            } catch (IOException | TemplateException e) {
//                Messages.showMessageDialog(String.format("%s >>> 生成文件【%s】失败: %s", table.getName(), distFile, e.getMessage()), "提示", Messages.getErrorIcon());
//            }
//        }
//    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
