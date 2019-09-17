package com.lifelight.api.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.common.pageUtil.PageInfo;

public class XlPersonContractVO extends PageInfo{
    private Integer id;

    private Integer documentId;
    
    private String isPoor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date signDate;

    private String mobile;
    
    private String variable;

    public String getVariable() {
		return variable;
	}
	public void setVariable(String variable) {
		this.variable = variable;
	}
	private String familyCount;

    private String signMode;

    private String captainName;

    private String doctorMobile;
    
    private String inUse;
    
    public String getInUse() {
		return inUse;
	}
	public void setInUse(String inUse) {
		this.inUse = inUse;
	}
	private String health;

    private String year;

    private String signDoctorId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date signStartDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date signEndDate;

    private String serviceContent;

    private String healthAssessment;

    private String arrangement;

    private String serviceMode;

    private String medicalType;

    private Double signPayment;

    private Double selfPayment;

    private Double compensationPayment;

    private String signPersonType;

    private String servicePackage;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date updateTime;

    private String signStatus;

    private String finishStatus;

    private String auditStatus;

    private String provinceCode;
    private String cityCode;
    private String townCode;
    private String name;
    private String cardNum;
    private String sex;
    private String age;
    private String familyRelations;
    private String degreeEducation;
    private String documentNum;
    private String occupation;
    private String residentialAddress;
    private String lowInsuranceCar;
    private String signOrgName;
    private String signDoctorName;
    private String signOrgId;
    private String managerId;
    private String packageName;
    
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getManagerId() {
		return managerId;
	}
	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}
	public String getSignOrgId() {
		return signOrgId;
	}
	public void setSignOrgId(String signOrgId) {
		this.signOrgId = signOrgId;
	}
	public String getSignDoctorName() {
		return signDoctorName;
	}
	public void setSignDoctorName(String signDoctorName) {
		this.signDoctorName = signDoctorName;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getCityCode() {
		return cityCode;
	}
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	public String getTownCode() {
		return townCode;
	}
	public void setTownCode(String townCode) {
		this.townCode = townCode;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getDocumentId() {
		return documentId;
	}
	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
	public String getIsPoor() {
		return isPoor;
	}
	public void setIsPoor(String isPoor) {
		this.isPoor = isPoor;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFamilyCount() {
		return familyCount;
	}
	public void setFamilyCount(String familyCount) {
		this.familyCount = familyCount;
	}
	public String getSignMode() {
		return signMode;
	}
	public void setSignMode(String signMode) {
		this.signMode = signMode;
	}
	public String getCaptainName() {
		return captainName;
	}
	public void setCaptainName(String captainName) {
		this.captainName = captainName;
	}
	public String getDoctorMobile() {
		return doctorMobile;
	}
	public void setDoctorMobile(String doctorMobile) {
		this.doctorMobile = doctorMobile;
	}
	public String getHealth() {
		return health;
	}
	public void setHealth(String health) {
		this.health = health;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getSignDoctorId() {
		return signDoctorId;
	}
	public void setSignDoctorId(String signDoctorId) {
		this.signDoctorId = signDoctorId;
	}
	public Date getSignStartDate() {
		return signStartDate;
	}
	public void setSignStartDate(Date signStartDate) {
		this.signStartDate = signStartDate;
	}
	public Date getSignEndDate() {
		return signEndDate;
	}
	public void setSignEndDate(Date signEndDate) {
		this.signEndDate = signEndDate;
	}
	public String getServiceContent() {
		return serviceContent;
	}
	public void setServiceContent(String serviceContent) {
		this.serviceContent = serviceContent;
	}
	public String getHealthAssessment() {
		return healthAssessment;
	}
	public void setHealthAssessment(String healthAssessment) {
		this.healthAssessment = healthAssessment;
	}
	public String getArrangement() {
		return arrangement;
	}
	public void setArrangement(String arrangement) {
		this.arrangement = arrangement;
	}
	public String getServiceMode() {
		return serviceMode;
	}
	public void setServiceMode(String serviceMode) {
		this.serviceMode = serviceMode;
	}
	public String getMedicalType() {
		return medicalType;
	}
	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}
	public Double getSignPayment() {
		return signPayment;
	}
	public void setSignPayment(Double signPayment) {
		this.signPayment = signPayment;
	}
	public Double getSelfPayment() {
		return selfPayment;
	}
	public void setSelfPayment(Double selfPayment) {
		this.selfPayment = selfPayment;
	}
	public Double getCompensationPayment() {
		return compensationPayment;
	}
	public void setCompensationPayment(Double compensationPayment) {
		this.compensationPayment = compensationPayment;
	}
	public String getSignPersonType() {
		return signPersonType;
	}
	public void setSignPersonType(String signPersonType) {
		this.signPersonType = signPersonType;
	}
	public String getServicePackage() {
		return servicePackage;
	}
	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	public String getSignStatus() {
		return signStatus;
	}
	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}
	public String getFinishStatus() {
		return finishStatus;
	}
	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}
	public String getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCardNum() {
		return cardNum;
	}
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getFamilyRelations() {
		return familyRelations;
	}
	public void setFamilyRelations(String familyRelations) {
		this.familyRelations = familyRelations;
	}
	public String getDegreeEducation() {
		return degreeEducation;
	}
	public void setDegreeEducation(String degreeEducation) {
		this.degreeEducation = degreeEducation;
	}
	public String getDocumentNum() {
		return documentNum;
	}
	public void setDocumentNum(String documentNum) {
		this.documentNum = documentNum;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public String getResidentialAddress() {
		return residentialAddress;
	}
	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}
	public String getLowInsuranceCar() {
		return lowInsuranceCar;
	}
	public void setLowInsuranceCar(String lowInsuranceCar) {
		this.lowInsuranceCar = lowInsuranceCar;
	}
	public String getSignOrgName() {
		return signOrgName;
	}
	public void setSignOrgName(String signOrgName) {
		this.signOrgName = signOrgName;
	}
   
    
}