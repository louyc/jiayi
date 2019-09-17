package com.lifelight.api.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lifelight.common.pageUtil.PageInfo;

public class XlPersonDocumentVO extends PageInfo{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5032968746449657266L;

	private Integer id;

    private String managerId;
    private String examinationInfo;
    
    public String getExaminationInfo() {
		return examinationInfo;
	}

	public void setExaminationInfo(String examinationInfo) {
		this.examinationInfo = examinationInfo;
	}


	private String cityName;
    
    public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}


	private Integer platformId;
    
	private String variable;
    
    public String getVariable() {
		return variable;
	}

	public void setVariable(String variable) {
		this.variable = variable;
	}


	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId(Integer platformId) {
		this.platformId = platformId;
	}


	private String name; //姓名

    private String sex;

    private String cardType;
    private String cardNum;  //身份证
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date birthday;

    private String mobile;  

    private String healthCard;  //健康卡号

    private String documentId;

    private String newFarmersCard;
    private String medicalInsuranceCard;

    private String residentialAddress;

    private String permanentAddress;

    private String workUnit;

    private String familyRelations;

    private String contactsName;

    private String contactsMobile;

    private String livingState;

    private String livingType;

    private String registeredResidence;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date arriveDate;

    private String country;

    private String nation;

    private String bloodType;

    private String rh;

    private String degreeEducation;

    private String occupation;

    private String marriageStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date dateMarriage;

    private String documentDoctor;
    private String documentOrgName;
    
    public String getDocumentOrgName() {
		return documentOrgName;
	}

	public void setDocumentOrgName(String documentOrgName) {
		this.documentOrgName = documentOrgName;
	}

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date documentDate;

    private Double medicalCosts;

    private String medicalCardNumber;

    private String lowInsuranceCar;

    private String drugAllergyHistory;

    private String pastHistory;

    private String motherMedicalHistory;

    private String fatherMedicalHistory;

    private String childrenMedicalHistory;

    private String brotherMedicalHistory;

    private String heredityHistory;

    private String isDisability;

    private String disabilityCard;

    private String exposeHistory;

    private String operationHistory;

    private String traumaHistory;

    private String bloodTransfusionHistory;

    private String kitchenExhaust;

    private String fuelType;

    private String water;

    private String toilet;

    private String livestock;

    private String remarks;

    private String keyCrowdType;

    private String provinceCode;

    private String cityCode;

    private String townCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date createTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "PRC")
    private Date updateTime;

    private String isSign; //
    
    private String signStatus;//签约状态
    
    public String getSignStatus() {
		return signStatus;
	}

	public void setSignStatus(String signStatus) {
		this.signStatus = signStatus;
	}

	private String auditStatus;//审核状态
    
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date signStartTime; //签约日期起
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
    private Date signEndTime; //签约日期至
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date signStartDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date signEndDate;
	
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

	private String inputPerson;//录入人
	private String servicePackage;//服务包类型
	private String projectType;//检查项id
	public String getProjectType() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}


	private String year; //年度
	private String isPayment;//是否有偿  1:是  2：否
	private String medicalType;//医保类型
	private String medicalTypeDesc;
	private String finishStatus;//完成状态 1:完成  2：未完成
	private String signPersonType;//签约人群
	private String signPersonTypeDesc;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "PRC")
	private Date signDate;//签约日期
	private String signOrgId;
	private String signOrgName;
	private String documentDoctorName;//档案医生姓名
	private String systemHints;//系统提示
	private String signPayment;//签约金额
	private String selfPayment;
	private String compensationPayment;
	private String servicePackageDesc;
	private String signDoctorId;
	private String signDoctorName;
	
	public String getDocumentDoctorName() {
		return documentDoctorName;
	}

	public void setDocumentDoctorName(String documentDoctorName) {
		this.documentDoctorName = documentDoctorName;
	}

	public String getMedicalTypeDesc() {
		return medicalTypeDesc;
	}

	public void setMedicalTypeDesc(String medicalTypeDesc) {
		this.medicalTypeDesc = medicalTypeDesc;
	}

	public String getSignPersonTypeDesc() {
		return signPersonTypeDesc;
	}

	public void setSignPersonTypeDesc(String signPersonTypeDesc) {
		this.signPersonTypeDesc = signPersonTypeDesc;
	}

	public String getSignDoctorId() {
		return signDoctorId;
	}

	public void setSignDoctorId(String signDoctorId) {
		this.signDoctorId = signDoctorId;
	}

	public String getSignDoctorName() {
		return signDoctorName;
	}

	public void setSignDoctorName(String signDoctorName) {
		this.signDoctorName = signDoctorName;
	}

	public String getServicePackageDesc() {
		return servicePackageDesc;
	}

	public void setServicePackageDesc(String servicePackageDesc) {
		this.servicePackageDesc = servicePackageDesc;
	}

	public String getSignPayment() {
		return signPayment;
	}

	public void setSignPayment(String signPayment) {
		this.signPayment = signPayment;
	}

	public String getSelfPayment() {
		return selfPayment;
	}

	public void setSelfPayment(String selfPayment) {
		this.selfPayment = selfPayment;
	}

	public String getCompensationPayment() {
		return compensationPayment;
	}

	public void setCompensationPayment(String compensationPayment) {
		this.compensationPayment = compensationPayment;
	}

	public String getSystemHints() {
		return systemHints;
	}

	public void setSystemHints(String systemHints) {
		this.systemHints = systemHints;
	}

	public Date getSignDate() {
		return signDate;
	}

	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}

	public String getSignOrgId() {
		return signOrgId;
	}

	public void setSignOrgId(String signOrgId) {
		this.signOrgId = signOrgId;
	}

	public String getSignOrgName() {
		return signOrgName;
	}

	public void setSignOrgName(String signOrgName) {
		this.signOrgName = signOrgName;
	}

	public String getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(String auditStatus) {
		this.auditStatus = auditStatus;
	}

	public String getInputPerson() {
		return inputPerson;
	}

	public void setInputPerson(String inputPerson) {
		this.inputPerson = inputPerson;
	}

	public String getServicePackage() {
		return servicePackage;
	}

	public void setServicePackage(String servicePackage) {
		this.servicePackage = servicePackage;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getIsPayment() {
		return isPayment;
	}

	public void setIsPayment(String isPayment) {
		this.isPayment = isPayment;
	}

	public String getMedicalType() {
		return medicalType;
	}

	public void setMedicalType(String medicalType) {
		this.medicalType = medicalType;
	}

	public String getFinishStatus() {
		return finishStatus;
	}

	public void setFinishStatus(String finishStatus) {
		this.finishStatus = finishStatus;
	}

	public String getSignPersonType() {
		return signPersonType;
	}

	public void setSignPersonType(String signPersonType) {
		this.signPersonType = signPersonType;
	}

	public Date getSignStartTime() {
		return signStartTime;
	}

	public void setSignStartTime(Date signStartTime) {
		this.signStartTime = signStartTime;
	}

	public Date getSignEndTime() {
		return signEndTime;
	}

	public void setSignEndTime(Date signEndTime) {
		this.signEndTime = signEndTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public String getCardNum() {
		return cardNum;
	}

	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHealthCard() {
		return healthCard;
	}

	public void setHealthCard(String healthCard) {
		this.healthCard = healthCard;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getNewFarmersCard() {
		return newFarmersCard;
	}

	public void setNewFarmersCard(String newFarmersCard) {
		this.newFarmersCard = newFarmersCard;
	}

	public String getMedicalInsuranceCard() {
		return medicalInsuranceCard;
	}

	public void setMedicalInsuranceCard(String medicalInsuranceCard) {
		this.medicalInsuranceCard = medicalInsuranceCard;
	}

	public String getResidentialAddress() {
		return residentialAddress;
	}

	public void setResidentialAddress(String residentialAddress) {
		this.residentialAddress = residentialAddress;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getWorkUnit() {
		return workUnit;
	}

	public void setWorkUnit(String workUnit) {
		this.workUnit = workUnit;
	}

	public String getFamilyRelations() {
		return familyRelations;
	}

	public void setFamilyRelations(String familyRelations) {
		this.familyRelations = familyRelations;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}

	public String getContactsMobile() {
		return contactsMobile;
	}

	public void setContactsMobile(String contactsMobile) {
		this.contactsMobile = contactsMobile;
	}

	public String getLivingState() {
		return livingState;
	}

	public void setLivingState(String livingState) {
		this.livingState = livingState;
	}

	public String getLivingType() {
		return livingType;
	}

	public void setLivingType(String livingType) {
		this.livingType = livingType;
	}

	public String getRegisteredResidence() {
		return registeredResidence;
	}

	public void setRegisteredResidence(String registeredResidence) {
		this.registeredResidence = registeredResidence;
	}

	public Date getArriveDate() {
		return arriveDate;
	}

	public void setArriveDate(Date arriveDate) {
		this.arriveDate = arriveDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getRh() {
		return rh;
	}

	public void setRh(String rh) {
		this.rh = rh;
	}

	public String getDegreeEducation() {
		return degreeEducation;
	}

	public void setDegreeEducation(String degreeEducation) {
		this.degreeEducation = degreeEducation;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getMarriageStatus() {
		return marriageStatus;
	}

	public void setMarriageStatus(String marriageStatus) {
		this.marriageStatus = marriageStatus;
	}

	public Date getDateMarriage() {
		return dateMarriage;
	}

	public void setDateMarriage(Date dateMarriage) {
		this.dateMarriage = dateMarriage;
	}

	public String getDocumentDoctor() {
		return documentDoctor;
	}

	public void setDocumentDoctor(String documentDoctor) {
		this.documentDoctor = documentDoctor;
	}

	public Date getDocumentDate() {
		return documentDate;
	}

	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}

	public Double getMedicalCosts() {
		return medicalCosts;
	}

	public void setMedicalCosts(Double medicalCosts) {
		this.medicalCosts = medicalCosts;
	}

	public String getMedicalCardNumber() {
		return medicalCardNumber;
	}

	public void setMedicalCardNumber(String medicalCardNumber) {
		this.medicalCardNumber = medicalCardNumber;
	}

	public String getLowInsuranceCar() {
		return lowInsuranceCar;
	}

	public void setLowInsuranceCar(String lowInsuranceCar) {
		this.lowInsuranceCar = lowInsuranceCar;
	}

	public String getDrugAllergyHistory() {
		return drugAllergyHistory;
	}

	public void setDrugAllergyHistory(String drugAllergyHistory) {
		this.drugAllergyHistory = drugAllergyHistory;
	}

	public String getPastHistory() {
		return pastHistory;
	}

	public void setPastHistory(String pastHistory) {
		this.pastHistory = pastHistory;
	}

	public String getMotherMedicalHistory() {
		return motherMedicalHistory;
	}

	public void setMotherMedicalHistory(String motherMedicalHistory) {
		this.motherMedicalHistory = motherMedicalHistory;
	}

	public String getFatherMedicalHistory() {
		return fatherMedicalHistory;
	}

	public void setFatherMedicalHistory(String fatherMedicalHistory) {
		this.fatherMedicalHistory = fatherMedicalHistory;
	}

	public String getChildrenMedicalHistory() {
		return childrenMedicalHistory;
	}

	public void setChildrenMedicalHistory(String childrenMedicalHistory) {
		this.childrenMedicalHistory = childrenMedicalHistory;
	}

	public String getBrotherMedicalHistory() {
		return brotherMedicalHistory;
	}

	public void setBrotherMedicalHistory(String brotherMedicalHistory) {
		this.brotherMedicalHistory = brotherMedicalHistory;
	}

	public String getHeredityHistory() {
		return heredityHistory;
	}

	public void setHeredityHistory(String heredityHistory) {
		this.heredityHistory = heredityHistory;
	}

	public String getIsDisability() {
		return isDisability;
	}

	public void setIsDisability(String isDisability) {
		this.isDisability = isDisability;
	}

	public String getDisabilityCard() {
		return disabilityCard;
	}

	public void setDisabilityCard(String disabilityCard) {
		this.disabilityCard = disabilityCard;
	}

	public String getExposeHistory() {
		return exposeHistory;
	}

	public void setExposeHistory(String exposeHistory) {
		this.exposeHistory = exposeHistory;
	}

	public String getOperationHistory() {
		return operationHistory;
	}

	public void setOperationHistory(String operationHistory) {
		this.operationHistory = operationHistory;
	}

	public String getTraumaHistory() {
		return traumaHistory;
	}

	public void setTraumaHistory(String traumaHistory) {
		this.traumaHistory = traumaHistory;
	}

	public String getBloodTransfusionHistory() {
		return bloodTransfusionHistory;
	}

	public void setBloodTransfusionHistory(String bloodTransfusionHistory) {
		this.bloodTransfusionHistory = bloodTransfusionHistory;
	}

	public String getKitchenExhaust() {
		return kitchenExhaust;
	}

	public void setKitchenExhaust(String kitchenExhaust) {
		this.kitchenExhaust = kitchenExhaust;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}

	public String getWater() {
		return water;
	}

	public void setWater(String water) {
		this.water = water;
	}

	public String getToilet() {
		return toilet;
	}

	public void setToilet(String toilet) {
		this.toilet = toilet;
	}

	public String getLivestock() {
		return livestock;
	}

	public void setLivestock(String livestock) {
		this.livestock = livestock;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getKeyCrowdType() {
		return keyCrowdType;
	}

	public void setKeyCrowdType(String keyCrowdType) {
		this.keyCrowdType = keyCrowdType;
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

	public String getIsSign() {
		return isSign;
	}

	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}
    
    
}