package com.kim.zilean.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
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
public class TableInfo implements Serializable {
    private static final long serialVersionUID = 2331066848350455886L;
    /**
     * 表名
     * 比如classic_read_article
     */
    private String name;
    /**
     * 属性名称
     * 比如classicReadArticle
     */
    private String propsName;
    /**
     * 类名称
     * 比如ClassicReadArticle
     */
    private String simpleClassName;

    /**
     * 表注释
     */
    private String comment;
    /**
     * 表的实体类信息
     */
    private Classes entity;
    /**
     * vo
     */
    private Classes vo;
    /**
     * dto
     */
    private Classes dto;
    /**
     * form
     */
    private Classes form;
    /**
     * query
     */
    private Classes query;
    /**
     * 表的 Dao 类信息
     */
    private Classes dao;
    /**
     * 表的 Service 接口信息
     */
    private Classes service;
    /**
     * 表的 ServiceImpl 类信息
     */
    private Classes serviceImpl;
    /**
     * 表的 ServiceImpl 类信息
     */
    private Classes controller;
    /**
     * 表的 Mapper 文件路径
     */
    private String xmlPath;
    /**
     * 表的数据列信息
     */
    private List<TableField> columns;
}
