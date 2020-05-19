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
    <sql id="Base_Column_List">
        <#list table.columns as column>
        `${column.name}`<#if column_has_next>,</#if>
        </#list>
    </sql>


    <!--
    note:
     like语句example:
         <if test=" query.fieldName != null and query.fieldName != '' ">
             and field_name LIKE concat(concat('%',${r'#'}{query.fieldName}),'%')
         </if>
     时间日期比较:
         <if test=" query.startTime != null " >
           <![CDATA[
                   and start_time >= ${r'#'}{query.startTime}
                   ]]>
         </if>
    -->
    <select id="${table.dao.className}.queryList"
            parameterType="${table.query.className}"
            resultMap="BaseResultMap">
        select
        <include refid="Base_Column_List"/>
        FROM
        ${table.name}
        <where>
            <#-- ----------  BEGIN 字段循环遍历  ---------->
        <#list table.columns as column>
        <#if column.simpleJavaType == "String">
            <if test=" query.${column.fieldName} != null and query.${column.fieldName}!=''">
                and ${column.name} = ${r'#'}{query.${column.fieldName}}
            </if>
        <#else>
            <if test=" query.${column.fieldName} != null ">
                and ${column.name} = ${r'#'}{query.${column.fieldName}}
            </if>
        </#if>
        </#list>
            <#-- ----------  END 字段循环遍历  ---------->
        </where>
        <choose>
            <when test=" query.sortField != null and query.sort != sortOrder and query.sortField != '' and query.sort != ''">
                ORDER BY  ${r'$'}{query.sortField} ${r'$'}{query.sortOrder}
            </when>
            <otherwise>
                <#if config.extConfig.keyTableField??>
                    ORDER BY ${config.extConfig.keyTableField.name} desc
                </#if>
            </otherwise>
        </choose>
    </select>
</mapper>