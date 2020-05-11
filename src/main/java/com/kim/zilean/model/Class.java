package com.kim.zilean.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author duanledexianxianxian
 * @date 2020/5/10 10:01
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class Class {
    /**
     * 包名
     */
    private String packagePath;
    /**
     * 类名（首字母大写，如 MobilePhone）
     */
    private String name;
    /**
     * 类作为属性名（首字母小写，如 mobilePhone）
     */
    private String propsName;
    /**
     * 类全名（如 com.xxx.MobilePhone）
     */
    private String className;
    /**
     * 类文件的目标目录
     */
    private String filePath;
    /**
     * 类文件的绝对路径
     */
    private String fileName;
    /**
     * 类需要 import 的信息
     */
    private List<String> imports;
}
