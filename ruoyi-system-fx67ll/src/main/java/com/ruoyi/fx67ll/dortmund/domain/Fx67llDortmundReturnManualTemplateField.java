package com.ruoyi.fx67ll.dortmund.domain;

/**
 * 回报组合人工AI交换字段说明。
 * <p>
 * 字段说明用于前端向用户解释导出任务与导入结果的每个JSON字段，不参与业务校验，
 * 也不包含任何批次的真实业务数据。
 *
 * @author fx67ll
 * @date 2026-11-18
 */
public class Fx67llDortmundReturnManualTemplateField {
    /** 字段分组 */
    private String group;

    /** JSON路径 */
    private String path;

    /** 中文字段名 */
    private String label;

    /** 字段类型 */
    private String type;

    /** 是否必填 */
    private Boolean required;

    /** 字段说明 */
    private String description;

    /** 示例值 */
    private String example;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return required;
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExample() {
        return example;
    }

    public void setExample(String example) {
        this.example = example;
    }
}
