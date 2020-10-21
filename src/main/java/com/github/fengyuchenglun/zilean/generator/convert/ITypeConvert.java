/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.github.fengyuchenglun.zilean.generator.convert;


import com.github.fengyuchenglun.zilean.model.Config;
import com.github.fengyuchenglun.zilean.model.TableField;

/**
 * 数据库字段类型转换
 *
 * @author hubin
 * @since 2017 -01-20
 */
public interface ITypeConvert {


    /**
     * 执行类型转换
     *
     * @param config     the config
     * @param tableField 字段列信息
     * @return ignore column type
     */
    default IColumnType processTypeConvert(Config config, TableField tableField) {
        // 该方法提供重写
        return processTypeConvert(config, tableField.getType());
    }


    /**
     * 执行类型转换
     *
     * @param config    the config
     * @param fieldType 字段类型
     * @return ignore column type
     */
    IColumnType processTypeConvert(Config config, String fieldType);
}
