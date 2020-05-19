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
package com.kim.zilean.generator.engine;

import com.intellij.openapi.project.Project;
import com.kim.zilean.ZileanContext;
import com.kim.zilean.form.ConfigForm;
import com.kim.zilean.model.Config;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;

import static com.kim.zilean.constant.Constants.UTF8;

/**
 * Freemarker 模板引擎实现文件输出
 *
 * @author duanledexianxian
 * @since 2018-01-11
 */
@Slf4j
public class FreemarkerTemplateEngine extends AbstractTemplateEngine {

    private Configuration configuration;

    @Override
    public FreemarkerTemplateEngine init(Config config) {
        super.init(config);
        initConfiguration();
        return this;
    }

    /**
     * 初始化Configuration配置
     * <p>
     * todo进行抽象优化
     */
    public void initConfiguration() {
        boolean hasCustomTemp = false;
        configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding(UTF8);
        Project project = ZileanContext.getInstance().getProject();
        if (project != null) {
            File tempDir = new File(project.getBasePath() + "/.zilean/templates");
            try {
                if (tempDir.isDirectory() && tempDir.canRead()) {
                    configuration.setDirectoryForTemplateLoading(tempDir);
                    hasCustomTemp = true;
                } else {
                    tempDir = new File(project.getBasePath() + "/.idea/zilean/templates");
                    if (tempDir.isDirectory() && tempDir.canRead()) {
                        configuration.setDirectoryForTemplateLoading(tempDir);
                        hasCustomTemp = true;
                    }
                }
            } catch (IOException e) {
                log.info("Load Template Fail.");
            }
        }
        if (!hasCustomTemp) {
            configuration.setClassLoaderForTemplateLoading(ConfigForm.class.getClassLoader(), "templates");
        }
    }


    @Override
    public void writer(Map<String, Object> objectMap, String templatePath, String outputFile) throws Exception {
        Template template = configuration.getTemplate(templatePath);
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFile)) {
            template.process(objectMap, new OutputStreamWriter(fileOutputStream, UTF8));
        }
        log.debug("模板:" + templatePath + ";  文件:" + outputFile);
    }

    @Override
    public String templateFilePath(String filePath) {
        return filePath + ".ftl";
    }
}
