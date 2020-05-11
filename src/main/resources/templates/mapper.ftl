<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${table.dao.className}">

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${table.entity.className}">
        <#list table.columns as column>
        <#if column.primaryKey>
        <id column="${column.name}" property="${column.fieldName}"/>
        <#else>
        <result column="${column.name}" property="${column.fieldName}"/>
        </#if>
        </#list>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="BaseColumnList">
        <#list table.columns as column>
        `${column.name}`<#if column_has_next>,</#if>
        </#list>
    </sql>

</mapper>