package com.informatics.payload;

import com.github.javafaker.Faker;
import com.google.gson.Gson;
import com.informatics.endpoints.APIConstants;
import com.informatics.pojos.crm.*;
import com.informatics.pojos.admin.*;
import com.informatics.utils.DataUtils;
import com.informatics.utils.PropertiesUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class RequestPayloads {

	protected Gson gson = new Gson();
	Faker faker = new Faker();
	Random random = new Random();

	public String getEmployeePayload() {
		EmployeeRecord employeeRecord = new EmployeeRecord();

		employeeRecord.setFirstName("kushan");
		employeeRecord.setLastName("amarasiri");
		employeeRecord.setEmail("lkkushan@email.com");
		
		return gson.toJson(employeeRecord);
	}

	public String login(){
		String username = DataUtils.getTestData("TestData", "Username", "Value");
		String password = DataUtils.getTestData("TestData", "Password", "Value");
		User user = new User();
		user.setUserName(username);
		user.setPassword(password);

		return gson.toJson(user);
	}

	public String logout(){
		Logout logout = new Logout();
		logout.setLogoutStatus("true");
		logout.setRefreshToken(System.getProperty("refresh_token"));
		return gson.toJson(logout);
	}

	public CustomerGroups createNewCustomerGroup() {
		CustomerGroups newCustomerGroups = new CustomerGroups();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));
		newCustomerGroups.setCode(code + ((int)(Math.random() * 9999)));
		newCustomerGroups.setName("AUT_GROUP_" + ((int)(Math.random() * 9999999)));
		newCustomerGroups.setIsActive("true");

		return newCustomerGroups;
	}

	public UnderwritingCategories createUnderwritingCategory() {
		UnderwritingCategories underwritingCategories = new UnderwritingCategories();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));
		underwritingCategories.setCode(code + ((int)(Math.random() * 9999)));
		underwritingCategories.setName("AUT_Category_" + ((int)(Math.random() * 9999999)));

		return underwritingCategories;
	}

	public CustomerBasicInfo createTestCustomer(String customerType, Integer categoryId, Integer groupId, String salutation, String genderCode, String nationality, Integer civilStatusId, String preferLang, String idType, String idNumber) {
		Map<String, Object>  mapId = new HashMap<>();
		mapId.put("idType", idType);
		mapId.put("idNumber", idNumber);
		mapId.put("isPrimary", true);

		List<Integer> uwCategories = new ArrayList<Integer>();
		uwCategories.add(categoryId);
		List<Integer> customerGroups = new ArrayList<Integer>();
		customerGroups.add(groupId);
		List<Map> idNumbers = new ArrayList<>();
		idNumbers.add(mapId);

		CustomerBasicInfo createTestCustomer = new CustomerBasicInfo();
		createTestCustomer.setCustomerTypeCode(customerType);
		createTestCustomer.setFirstName("AUT_" + faker.name().firstName());
		createTestCustomer.setMiddleName("AUT_" + faker.name().firstName());
		createTestCustomer.setLastName("AUT_" + faker.name().lastName());
		createTestCustomer.setCallingName(createTestCustomer.getFirstName());
		createTestCustomer.setSurnameWithInitials(createTestCustomer.getFirstName().substring(0,1) + "." + createTestCustomer.getMiddleName().substring(0,1) + "." + createTestCustomer.getLastName());
		createTestCustomer.setSalutationCode(salutation);
		createTestCustomer.setSalutationOther(null);
		createTestCustomer.setGender(genderCode);
		createTestCustomer.setDob("1994-10-15");
		createTestCustomer.setNationality(nationality);
		createTestCustomer.setCivilStatusId(civilStatusId);
		createTestCustomer.setPolicyholderPreferredLanguage(preferLang);
		createTestCustomer.setUwCategories(uwCategories);
		createTestCustomer.setCustomerGroups(customerGroups);
		createTestCustomer.setCustomerNatureId(1);
		createTestCustomer.setCustomerPortalAccess(true);
		createTestCustomer.setIdNumbers(idNumbers);

		return createTestCustomer;
	}

	public CustomerBasicInfo createCustomerBasicInfo(String customerType, Integer categoryId, Integer groupId, String salutation, String genderCode, String nationality, Integer civilStatusId, String preferLang, String idType, Integer relationshipTypeId, String idNumber, Integer customerId) {
		Map<String, Object>  mapId = new HashMap<>();
		mapId.put("idType", idType);
		mapId.put("idNumber", idNumber);
		mapId.put("isPrimary", true);

		Map<String, Object>  mapCus = new HashMap<>();
		mapCus.put("relationshipTypeId", relationshipTypeId);
		mapCus.put("customerId", customerId);

		List<Integer> uwCategories = new ArrayList<Integer>();
		uwCategories.add(categoryId);
		List<Integer> customerGroups = new ArrayList<Integer>();
		customerGroups.add(groupId);
		List<Map> idNumbers = new ArrayList<>();
		idNumbers.add(mapId);
		List<Map> relatedCustomers = new ArrayList<>();
		relatedCustomers.add(mapCus);

		CustomerBasicInfo createCustomerBasicInfo = new CustomerBasicInfo();
		createCustomerBasicInfo.setCustomerTypeCode(customerType);
		createCustomerBasicInfo.setFirstName("AUT_" + faker.name().firstName());
		createCustomerBasicInfo.setMiddleName("AUT_" + faker.name().firstName());
		createCustomerBasicInfo.setLastName("AUT_" + faker.name().lastName());
		createCustomerBasicInfo.setCallingName(createCustomerBasicInfo.getFirstName());
		createCustomerBasicInfo.setSurnameWithInitials(createCustomerBasicInfo.getFirstName().substring(0,1) + "." + createCustomerBasicInfo.getMiddleName().substring(0,1) + "." + createCustomerBasicInfo.getLastName());
		createCustomerBasicInfo.setSalutationCode(salutation);
		createCustomerBasicInfo.setSalutationOther(null);
		createCustomerBasicInfo.setGender(genderCode);
		createCustomerBasicInfo.setDob("1994-10-15");
		createCustomerBasicInfo.setNationality(nationality);
		createCustomerBasicInfo.setCivilStatusId(civilStatusId);
		createCustomerBasicInfo.setPolicyholderPreferredLanguage(preferLang);
		createCustomerBasicInfo.setUwCategories(uwCategories);
		createCustomerBasicInfo.setCustomerGroups(customerGroups);
		createCustomerBasicInfo.setCustomerNatureId(1);
		createCustomerBasicInfo.setCustomerPortalAccess(true);
		createCustomerBasicInfo.setIdNumbers(idNumbers);
		createCustomerBasicInfo.setRelatedCustomers(relatedCustomers);

		return createCustomerBasicInfo;
	}

	public CustomerBasicInfo updateCustomerBasicInfo(Integer customerId, String customerType, String fName, String lName, String mName, String cName, String surname, String salutation, String salutationOther, String gender, String dob, String nationality, Integer civilStatus, String lang, List<Integer> uwCategories, List<Integer> custGrps, Integer custNature, Boolean portalAccess, List<Map> idNumbers, List<Map> relatedCusts  ) {
		CustomerBasicInfo updateCustomerBasicInfo = new CustomerBasicInfo();

		updateCustomerBasicInfo.setCustomerId(customerId);
		updateCustomerBasicInfo.setCustomerTypeCode(customerType);
		updateCustomerBasicInfo.setFirstName(fName);
		updateCustomerBasicInfo.setMiddleName(mName);
		updateCustomerBasicInfo.setLastName(lName);
		updateCustomerBasicInfo.setCallingName(cName);
		updateCustomerBasicInfo.setSurnameWithInitials(surname);
		updateCustomerBasicInfo.setSalutationCode(salutation);
		updateCustomerBasicInfo.setSalutationOther(salutationOther);
		updateCustomerBasicInfo.setGender(gender);
		updateCustomerBasicInfo.setDob(dob);
		updateCustomerBasicInfo.setNationality(nationality);
		updateCustomerBasicInfo.setCivilStatusId(civilStatus);
		updateCustomerBasicInfo.setPolicyholderPreferredLanguage(lang);
		updateCustomerBasicInfo.setUwCategories(uwCategories);
		updateCustomerBasicInfo.setCustomerGroups(custGrps);
		updateCustomerBasicInfo.setCustomerNatureId(custNature);
		updateCustomerBasicInfo.setCustomerPortalAccess(portalAccess);
		updateCustomerBasicInfo.setIdNumbers(idNumbers);
		updateCustomerBasicInfo.setRelatedCustomers(relatedCusts);

		return updateCustomerBasicInfo;
	}

	public RelationshipTypes createRelationshipType() {
		RelationshipTypes newRelationshipType = new RelationshipTypes();
		newRelationshipType.setName("AUT_REL_" + ((int)(Math.random() * 99999)));
		newRelationshipType.setIsActive("true");

		return newRelationshipType;
	}

	public ValidationEvents updateValidationEvents(Integer fieldId, Boolean isUsed, Boolean isEditAllowed, Boolean isApprovalRequired) {
		ValidationEvents validationEvents = new ValidationEvents();
		validationEvents.setFieldId(fieldId);
		validationEvents.setIsUsed(isUsed);
		validationEvents.setIsEditAllowed(isEditAllowed);
		validationEvents.setIsApprovalRequired(isApprovalRequired);

		return validationEvents;
	}

	public Tax createTaxType(Integer countryId, String countryName) {
		Tax tax = new Tax();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));
		tax.setTaxTypeCode(code + ((int)(Math.random() * 9999)));
		tax.setTaxType("AUT_TAX_" + ((int)(Math.random() * 99999)));
		tax.setIsActive(true);
		tax.setCountryId(countryId);
		tax.setCountryName(countryName);

		return tax;
	}

	public TaxRegistration newTaxRegistration(Integer taxTypeId, Integer customerId) {
		TaxRegistration taxRegistration = new TaxRegistration();
		taxRegistration.setReferenceNumber("AUT_VAT_" + ((int)(Math.random() * 99999)));
		taxRegistration.setTaxTypeId(taxTypeId);
		taxRegistration.setIsActive(true);
		taxRegistration.setCustomerId(customerId);

		return taxRegistration;
	}

	LocalDate dateObj = LocalDate.now();
	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("YYYY-MM-dd");

	public TaxesExcepted taxesExcepted(Integer customerId, Integer taxTypeId)
	{
		TaxesExcepted taxesExcepted = new TaxesExcepted();
		taxesExcepted.setCustomerId(customerId);
		taxesExcepted.setTaxTypeId(taxTypeId);
		taxesExcepted.setReferenceNumber("AUT_VAT_" + ((int)(Math.random() * 99999)));
		taxesExcepted.setFrom(dateObj.format(formatter));
		taxesExcepted.setTo(dateObj.format(formatter));
		taxesExcepted.setIsApprovalRequired(true);
		taxesExcepted.setIsActive(true);

		return taxesExcepted;
	}

	public CustomerNotes newCustomerNotes(Integer customerId){
		CustomerNotes customerNotes = new CustomerNotes();

		customerNotes.setCustomerId(customerId);
		customerNotes.setCustomerNote("AUT");
		customerNotes.setIsActive(true);

		return customerNotes;
	}

	public JointParticipation createJointParticipation(Integer jointPartnershipId, Integer customerId, Integer percentage)
	{
		Map<String, Object>  mapId = new HashMap<>();
		mapId.put("jointPartnershipId", jointPartnershipId);
		mapId.put("customerId", customerId);
		mapId.put("percentage", percentage);
		mapId.put("isDefault", Boolean.valueOf(PropertiesUtils.isDefault));

		List<Map> participation = new ArrayList<>();
		participation.add(mapId);

		JointParticipation createJointParticipation = new JointParticipation();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));
		createJointParticipation.setCode(code + ((int)(Math.random() * 9999)));
		createJointParticipation.setIsActive(true);
		createJointParticipation.setPartnershipName("AUT_Mr & Mrs " + faker.name().lastName());
		createJointParticipation.setParticipation(participation);

		return createJointParticipation;
	}

	public BusinessType CreateBusinessType(String businessType)
	{
		BusinessType CreateBusinessType = new BusinessType();
		CreateBusinessType.setBusinessType(businessType);
		return CreateBusinessType;
	}

	public Bank newBank(String country) {
		Bank bank = new Bank();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));
		bank.setCode(code + ((int)(Math.random() * 9999)));
		bank.setName("AUT_BANK_" + ((int)(Math.random() * 99999)));
		bank.setCountry(country);

		return bank;
	}

	public BankBranch newBankBranch(Integer bankId, String bankName) {
		BankBranch bankBranch = new BankBranch();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));

		bankBranch.setBankId(bankId);
		bankBranch.setBankName(bankName);
		bankBranch.setCode(code + ((int)(Math.random() * 9999)));
		bankBranch.setName("AUT_BRANCH_" + ((int)(Math.random() * 99999)));
		bankBranch.setSwiftCode("SWI" + ((int)(Math.random() * 99999)));
		bankBranch.setIsActive(true);

		return  bankBranch;
	}

	public Occupation newOccupation() {
		Occupation occupation = new Occupation();
		String code = String.valueOf((char) (random.nextInt(26) + 'A'));
		occupation.setOccupation(code + ((int)(Math.random() * 9999)));
		occupation.setName("AUT_INFO_" + ((int)(Math.random() * 99999)));
		occupation.setIsActive(true);

		return occupation;
	}

	public BankInfo newBankInfo(Integer bankId, Integer branchId, String fName, String lName) {
		BankInfo bankInfo = new BankInfo();
		bankInfo.setBankAccountName(fName + " " + lName);
		bankInfo.setBankAccountNo(String.valueOf((int)(Math.random() * 999999999)));
		bankInfo.setBankId(bankId);
		bankInfo.setBranchId(branchId);
		bankInfo.setIsPrimary(true);
		bankInfo.setIsActive(true);

		return  bankInfo;
	}

	public EmploymentInfo newEmploymentIno(Integer occupationId) {
		EmploymentInfo employmentInfo = new EmploymentInfo();
		employmentInfo.setEmploymentName("AUT_EMP_" + ((int)(Math.random() * 99999)));
		employmentInfo.setStartDate(dateObj.format(formatter));
		employmentInfo.setEndDate("2026-12-25");
		employmentInfo.setOccupationId(occupationId);
		employmentInfo.setIsActive(true);

		return employmentInfo;
	}

	public CustomerAdditionalInfo customerAdditionalInfo() {
		CustomerAdditionalInfo customerAdditionalInfo = new CustomerAdditionalInfo();
		customerAdditionalInfo.setAdditionalInfoValue("B+");
		customerAdditionalInfo.setIsActive(true);
		customerAdditionalInfo.setCmRAdditionalInfoId(Integer.parseInt(PropertiesUtils.fieldId0));

		return customerAdditionalInfo;
	}

	public StatusChange statusChange(Integer id0, Integer id1) {
		List<Integer> transId = new ArrayList<Integer>();
		transId.add(id0);
		transId.add(id1);

		StatusChange statusChange = new StatusChange();
		statusChange.setStatusId(Integer.parseInt(PropertiesUtils.activeStatusId0));
		statusChange.setRemark("AUT_REMARK_" + ((int)(Math.random() * 99999)));
		statusChange.setRestrictedTransIds(transId);

		return statusChange;
	}

	public ContactInfo contactInfo(Integer emailTypeId, String fName){
		ContactInfo contactInfo = new ContactInfo();
		contactInfo.setEmailTypeId(emailTypeId);
		contactInfo.setEmail(fName + "@gmail.com");
		contactInfo.setIsReceiveNotification(Boolean.valueOf(PropertiesUtils.cusNoteReceivedNotification));
		contactInfo.setIsPrimary(Boolean.valueOf(PropertiesUtils.cusNoteIsPrimary));

		return contactInfo;
	}

	public CustomerDocuments customerDocuments(Integer documentTypeId){
		CustomerDocuments customerDocuments = new CustomerDocuments();
		customerDocuments.setDocumentPath(PropertiesUtils.cusDocsDocumentPath);
		customerDocuments.setRemark("AUT_TEST_" + ((int)(Math.random() * 99999)));
		customerDocuments.setReceivedDate(dateObj.format(formatter));
		customerDocuments.setFunctionDocumentId(Integer.parseInt(PropertiesUtils.cusDocsFunctionId));
		customerDocuments.setDocumentTypeId(documentTypeId);
		customerDocuments.setIsReceived(Boolean.valueOf(PropertiesUtils.cusDocsIsReceived));

		return customerDocuments;
	}

	public CoporateCutomerBasicInfo coporateCutomerBasicInfo(String customerType, Integer businessTypeId, Integer categoryId,Integer parentCompanyId) {
		List<Integer> uwCategories = new ArrayList<Integer>();
		uwCategories.add(categoryId);

		List<Integer> deleteUwCategories = new ArrayList<Integer>();

		CoporateCutomerBasicInfo coporateCutomerBasicInfo = new CoporateCutomerBasicInfo();
		coporateCutomerBasicInfo.setCustomerTypeCode(customerType);
		coporateCutomerBasicInfo.setName(faker.company().name() + " Group");
		coporateCutomerBasicInfo.setRegistrationNumber("AUTO" + ((int)(Math.random() * 9999999)));
		coporateCutomerBasicInfo.setParentCompanyId(parentCompanyId);
		coporateCutomerBasicInfo.setBusinessTypeId(businessTypeId);
		coporateCutomerBasicInfo.setUwCategories(uwCategories);
		coporateCutomerBasicInfo.setDeletingUwCategories(deleteUwCategories);

		return  coporateCutomerBasicInfo;
	}

	public CivilStatus newCivilStatus() {
		CivilStatus civilStatus = new CivilStatus();
		civilStatus.setCivilStatus("AUT_Test_CivilStatus");
		civilStatus.setIsActive(true);

		return civilStatus;
	}
}
