package com.ruoyi.fx67ll.dortmund.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 球队编辑面板与检索表单使用的主场、地区/联赛和标签选项。
 * <p>
 * 选项由内置目录和球队表实际数据合并生成；主场与地区/联赛额外携带关联球队名称，
 * 供前端按球队名称模糊检索。前端在接口失败或返回空列表时使用本地兜底目录。
 *
 * @author fx67ll
 * @date 2026-11-20
 */
public class Fx67llDortmundTeamOptions {
    /** 球队主场单选选项。 */
    private List<Option> teamVenueOptions = new ArrayList<>();

    /** 球队地区/联赛单选选项。 */
    private List<Option> teamCountryOptions = new ArrayList<>();

    /** 球队标签多选选项；type 用于 Element UI 标签配色。 */
    private List<Option> teamTagOptions = new ArrayList<>();

    public List<Option> getTeamVenueOptions() {
        return teamVenueOptions;
    }

    public void setTeamVenueOptions(List<Option> teamVenueOptions) {
        this.teamVenueOptions = teamVenueOptions;
    }

    public List<Option> getTeamCountryOptions() {
        return teamCountryOptions;
    }

    public void setTeamCountryOptions(List<Option> teamCountryOptions) {
        this.teamCountryOptions = teamCountryOptions;
    }

    public List<Option> getTeamTagOptions() {
        return teamTagOptions;
    }

    public void setTeamTagOptions(List<Option> teamTagOptions) {
        this.teamTagOptions = teamTagOptions;
    }

    /** 选择器兼容 Element UI 的 label/value/type 结构。 */
    public static class Option {
        private String label;
        private String value;
        private String type;
        /** 使用该主场或地区/联赛的球队名称，仅用于前端模糊检索与展示。 */
        private List<String> teamNames = new ArrayList<>();

        public Option() {
        }

        public Option(String label, String value, String type) {
            this.label = label;
            this.value = value;
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getTeamNames() {
            return teamNames;
        }

        public void setTeamNames(List<String> teamNames) {
            this.teamNames = teamNames;
        }
    }
}
