package com.atoz.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.sun.xml.internal.txw2.annotation.XmlElement;

@XmlRootElement(name="student")
public class student {
    private Integer sId;

    private String sName;

    private Integer age;

    private Boolean sex;

    @XmlElement("sId")
    public Integer getsId() {
        return sId;
    }

    public void setSId(Integer sId) {
        this.sId = sId;
    }
    @XmlElement("sName")
    public String getsName() {
        return sName;
    }

    public void setSName(String sName) {
        this.sName = sName == null ? null : sName.trim();
    }
    @XmlElement("age")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
	public String toString() {
		return "student [sId=" + sId + ", sName=" + sName + ", age=" + age
				+ ", sex=" + sex + "]";
	}
    @XmlElement("sex")
	public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }
}