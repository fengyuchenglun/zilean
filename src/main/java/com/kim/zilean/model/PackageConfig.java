package com.kim.zilean.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 包配置路径
 *
 * @author duanledexianxianxian
 * @date 2020 /5/10 9:54
 * @since 1.0.0
 */
@Data
@AllArgsConstructor
public class PackageConfig implements Serializable {
    private static final long serialVersionUID = -8542560138912382329L;
    /**
     * 包路径
     * 如com.kim.zilean
     */
    private String pkg;
    /**
     * 类型后缀名
     * 如 User 加后缀 Service 为 UserService
     */
    private String suffix;
    /**
     * 是否需要生成
     */
    private boolean isNeed;
}
