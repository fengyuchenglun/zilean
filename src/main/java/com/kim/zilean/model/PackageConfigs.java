package com.kim.zilean.model;

import lombok.Data;

/**
 * The type Class packages.
 *
 * @author duanledexianxianxian
 * @date 2020 /5/10 9:57
 * @since 1.0.0
 */
@Data
public class PackageConfigs {
    private PackageConfig entity;
    private PackageConfig dto;
    private PackageConfig vo;
    private PackageConfig form;
    private PackageConfig query;
    private PackageConfig dao;
    private PackageConfig xml;
    private PackageConfig service;
    private PackageConfig serviceImpl;
    private PackageConfig controller;
}