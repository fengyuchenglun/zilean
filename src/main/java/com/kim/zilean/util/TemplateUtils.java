package com.kim.zilean.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * The type Template utils.
 */
public class TemplateUtils {
    private static void copyResourceFile(String name, String toDir) {
        InputStream in = TemplateUtils.class.getClassLoader().getResourceAsStream("/templates/" + name);
        if (in != null) {
            try {
                byte[] bytes = new byte[in.available()];
                IOUtils.read(in, bytes);
                in.close();
                String data = new String(bytes, StandardCharsets.UTF_8);
                FileUtils.writeStringToFile(new File(toDir + File.separator + name), data, StandardCharsets.UTF_8);
            } catch (IOException e) {
                Messages.showMessageDialog("导出文件 " + name + " 发生错误: " + e.getMessage(), "提示", Messages.getErrorIcon());
            }
        }
    }

    /**
     * Export templates.
     *
     * @param project the project
     * @param path    the path
     */
    public static void exportTemplates(Project project, String path) {
        if (project == null) {
            return;
        }
        String toDir = project.getBasePath() + path + "/templates";
        File dir = new File(toDir);
        if (dir.isDirectory() || dir.mkdirs()) {
            copyResourceFile("controller.ftl", toDir);
            copyResourceFile("dao.ftl", toDir);
            copyResourceFile("dto.ftl", toDir);
            copyResourceFile("entity.ftl", toDir);
            copyResourceFile("form.ftl", toDir);
            copyResourceFile("query.ftl", toDir);
            copyResourceFile("service.ftl", toDir);
            copyResourceFile("service-impl.ftl", toDir);
            copyResourceFile("vo.ftl", toDir);
            copyResourceFile("xml.ftl", toDir);
        }
        Messages.showMessageDialog("默认模版已导出到：" + dir.getAbsolutePath(), "导出成功", Messages.getInformationIcon());
    }
}
