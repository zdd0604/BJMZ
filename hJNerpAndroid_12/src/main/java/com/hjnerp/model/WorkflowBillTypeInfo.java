package com.hjnerp.model;

public class WorkflowBillTypeInfo implements Comparable<WorkflowBillTypeInfo> {
    private String id_table = "";    // 工单类型id
    private String name_table = "";    // 工单类型名称
    private Boolean isSelected = true;//用户是否想要展示此数据
    private Boolean isChecked = false;//界面展示时此RadioButton是否选中状态
    private int frequence = 0;//根据点击次数排序

    public String getId() {
        return id_table;
    }

    public void setId(String id) {
        this.id_table = id;
    }

    public String getName() {
        return name_table;
    }

    public void setName(String name) {
        this.name_table = name;
    }

    public Boolean getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getFrequence() {
        return frequence;
    }

    public void setFrequence(int frequence) {
        this.frequence = frequence;
    }


    @Override
    public int compareTo(WorkflowBillTypeInfo workflowBillTypeInfo) {
        return workflowBillTypeInfo.getFrequence() - this.getFrequence();
    }
}
