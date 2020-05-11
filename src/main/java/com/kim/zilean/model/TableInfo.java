package com.kim.zilean.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * The type Table info.
 *
 * @author duanledexianxianxian
 * @date 2020 /5/10 10:05
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class TableInfo {
    /**
     * 表名
     */
    private String name;
    /**
     * 表注释
     */
    private String comment;
    /**
     * 表的实体类信息
     */
    private Class entity;
    /**
     * 表的 Dao 类信息
     */
    private Class dao;
    /**
     * 表的 Service 接口信息
     */
    private Class service;
    /**
     * 表的 ServiceImpl 类信息
     */
    private Class serviceImpl;
    /**
     * 表的 Mapper 文件路径
     */
    private String xmlPath;
    /**
     * 表的数据列信息
     */
    private List<TableField> columns;
}