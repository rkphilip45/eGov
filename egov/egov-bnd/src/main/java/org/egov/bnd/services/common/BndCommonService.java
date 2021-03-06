/*******************************************************************************
 * eGov suite of products aim to improve the internal efficiency,transparency,
 *    accountability and the service delivery of the government  organizations.
 *
 *     Copyright (C) <2015>  eGovernments Foundation
 *
 *     The updated version of eGov suite of products as by eGovernments Foundation
 *     is available at http://www.egovernments.org
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program. If not, see http://www.gnu.org/licenses/ or
 *     http://www.gnu.org/licenses/gpl.html .
 *
 *     In addition to the terms of the GPL license to be adhered to in using this
 *     program, the following additional terms are to be complied with:
 *
 * 	1) All versions of this program, verbatim or modified must carry this
 * 	   Legal Notice.
 *
 * 	2) Any misrepresentation of the origin of the material is prohibited. It
 * 	   is required that all modified versions of this material be marked in
 * 	   reasonable ways as different from the original version.
 *
 * 	3) This license does not grant any rights to any user of the program
 * 	   with regards to rights under trademark law for use of the trade names
 * 	   or trademarks of eGovernments Foundation.
 *
 *   In case of any queries, you can reach eGovernments Foundation at contact@egovernments.org.
 ******************************************************************************/
package org.egov.bnd.services.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.egov.bnd.model.AdoptionInstitute;
import org.egov.bnd.model.BnDCitizen;
import org.egov.bnd.model.BndFeeTypes;
import org.egov.bnd.model.CRelation;
import org.egov.bnd.model.CitizenRelation;
import org.egov.bnd.model.City;
import org.egov.bnd.model.Disease;
import org.egov.bnd.model.Establishment;
import org.egov.bnd.model.EstablishmentType;
//import org.egov.bnd.model.FeeCollection;
import org.egov.bnd.model.PlaceType;
import org.egov.bnd.model.Registrar;
import org.egov.bnd.model.Registration;
import org.egov.bnd.model.RegistrationUnit;
import org.egov.bnd.utils.BndConstants;
import org.egov.commons.EgwStatus;
import org.egov.commons.ObjectHistory;
import org.egov.infra.admin.master.entity.Address;
import org.egov.infra.admin.master.entity.CityWebsite;
import org.egov.infra.admin.master.entity.Role;
import org.egov.infra.admin.master.entity.User;
import org.egov.infra.admin.master.service.CityWebsiteService;
import org.egov.infra.workflow.entity.State;
import org.egov.infra.workflow.entity.StateAware;
import org.egov.infstr.client.filter.EGOVThreadLocals;
import org.egov.infstr.commons.Module;
import org.egov.infstr.config.AppConfigValues;
import org.egov.infstr.config.dao.AppConfigValuesHibernateDAO;
import org.egov.infstr.services.EISServeable;
import org.egov.infstr.services.PersistenceService;
import org.egov.infstr.utils.EGovConfig;
import org.egov.pims.commons.Position;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * This is a common service class defined for bnd module. It is used to interact
 * with different modules All the common api's required by bnd module is defined
 * here.
 *
 * @author pritiranjan
 */

@SuppressWarnings({ "unchecked" })
public class BndCommonService {

    private static final String STATEQUERYID = "from org.egov.mdm.masters.administration.State where id=?";
    private static final String GETALLSTATESQUERY = "from org.egov.mdm.masters.administration.State order by id";
    private static final String ADDRESSQUERYID = "from Address addr where addr.addressID=?";
    private static final String REGISTRARQUERY = "from Registrar where userId.id=?";
    private static final String STATEQUERYCONS = "from org.egov.mdm.masters.administration.State where stateConst=?";
    private static final String ADDRESSQUERYTYPE = "from Address addr where addr.streetAddress1=? and addr.addTypeMaster.addressTypeName=?";
    private static final String STATUSQUERY = "from EgwStatus where moduleType=? and code=?";
    private static final String PLACETYPEQUERY = "from PlaceType p where p.desc=?";
    private static final String ESTABLISHMENTQUERY = "from Establishment  where id=?";
    private static final String RELATIONQUERY = "from CRelation where relatedAsConst=?";
    private static final String DISEASEQUERY = "from Disease where parent.id=?";
    private static final String BNDFEETYPEQUERYFORGENERALTYPE = "from BndFeeTypes where isActive=1 and feeType in (?)";
    private static final String GETBNDFEETYPEBYCODE = "from BndFeeTypes where code=?";
    private static final String BNDFEETYPEQUERYFORBIRTH = "from BndFeeTypes where isActive=1 and feeType in (?,?)";
    private static final String USERQUERY = "from User where id=?";
    private static final String REGUNITQUERY = "from RegistrationUnit where id=?";
    private static final String CITIZENQUERY = "from BndCitizen where citizenID=?";
    private static final String ADOPTIONINSTITUTEQUERY = "from AdoptionInstitute where id=?";
    private static final String CITYQUERY = "from org.egov.bnd.model.City where talukId=? ";
    private static final String HOSPITALTYPEQUERY = "from org.egov.bnd.model.EstablishmentType";
    private static final String REGISTRATIONUNITQUERY = "from org.egov.bnd.model.RegistrationUnit";
    // private static final String USERNAMEQUERY =
    // " from User where id in(select user.id from UserRole where role.id in(select id from Role  where  roleName in('Registrar','HospitalRegistrar','Accountant','HospitalUser','Operator')))";
    private static final String ROLE = "from Role where id in(select role.id  from UserRole where user.id=?)";
    private static final String ESTABLISHMENTNAMEQUERY = "from Establishment";
    private static final String CITYBYID = "from org.egov.bnd.model.City where id=?";
    private static final String USERNAMEQUERY = " from User where id in (select userId from Registrar)";
    private Map<String, Object> registrationTypeServiceMap;

    private static final String GETUSERBYPASSINGIDQUERY = "from User  where id=?";
    private static final String MODULEQUERY = "from org.egov.infstr.commons.Module where moduleName=?";
    private static final String GENERALFEETYPES = "GENERAL";
    private static final String BIRTHFEETYPES = "BIRTH";
    private PersistenceService persistenceService;
    private ObjectHistoryService objectHistoryService;
    private ObjectTypeService objectTypeService;
    private AppConfigValuesHibernateDAO appConfigValuesDAO;
    private EISServeable eisService;

    @Autowired
    private CityWebsiteService cityWebsiteService;

    public Map<Long, String> ADDRESSMAP = new HashMap<Long, String>();

    public void setEisService(final EISServeable eisService) {
        this.eisService = eisService;
    }

    public void setAppConfigValuesDAO(final AppConfigValuesHibernateDAO appConfigValuesDAO) {
        this.appConfigValuesDAO = appConfigValuesDAO;
    }

    public void setObjectHistoryService(final ObjectHistoryService objectHistoryService) {
        this.objectHistoryService = objectHistoryService;
    }

    public void setObjectTypeService(final ObjectTypeService objectTypeService) {
        this.objectTypeService = objectTypeService;
    }

    public void setPersistenceService(final PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    public State getStateById(final Integer id) {
        return (State) persistenceService.find(STATEQUERYID, id);
    }

    public void setRegistrationTypeServiceMap(final Map<String, Object> registrationTypeServiceMap) {
        this.registrationTypeServiceMap = registrationTypeServiceMap;
    }

    public List findAll() {
        return persistenceService.findAll();
    }

    public Map<String, Object> getRegistrationTypeServiceMap() {
        return registrationTypeServiceMap;
    }

    /**
     * This ApI returns the Registrar for Logged in user.
     *
     * @return
     */

    public Registrar getRegistrarByLoggedInUser() {
        final Integer userId = Integer.valueOf(EGOVThreadLocals.getUserId());
        return (Registrar) persistenceService.find(REGISTRARQUERY, userId);
    }

    public Registrar getRegistrarByUserId(final Long userId) {
        return (Registrar) persistenceService.find(REGISTRARQUERY, userId);
    }

    public Address getDefaultAddress(final String addressType) {
        return (Address) persistenceService.find(ADDRESSQUERYTYPE, BndConstants.NOTAVAILABLE, addressType);
    }

    /**
     * This Api is to return address by passing id
     *
     * @param addressId
     * @return
     */

    public Address getAddressById(final Integer addressId) {
        return (Address) persistenceService.find(ADDRESSQUERYID, addressId);
    }

    public State getStateByStateConstant(final String stateConst) {
        return (State) persistenceService.find(STATEQUERYCONS, stateConst);
    }

    /**
     * This API is to get CRelation By Passing related Constant
     *
     * @param relatedConst
     * @return
     */

    public CRelation getCRelationByRelatedConstant(final String relatedConst) {
        return (CRelation) persistenceService.find(RELATIONQUERY, relatedConst);
    }

    /**
     * This API is to get PlaceType object by passing placetype description
     */

    public PlaceType getPlaceType(final String placeType) {
        return (PlaceType) persistenceService.find(PLACETYPEQUERY, placeType);
    }

    public boolean getAppconfigValue(final String module, final String key, final String defaultValue) {
        final String appConfigValue = EGovConfig.getAppConfigValue(module, key, defaultValue);
        return appConfigValue != null
                && (appConfigValue.equals("1") || appConfigValue.equals("Y") || appConfigValue.equals("y")
                        || appConfigValue.equals("YES") || appConfigValue.equals("yes"));
    }

    public List<String> getAppconfigActualValue(final String module, final String key) {
        final List<AppConfigValues> configValues = appConfigValuesDAO.getConfigValuesByModuleAndKey(module, key);
        List<String> configValueList = Collections.EMPTY_LIST;
        if (!configValues.isEmpty()) {
            configValueList = new ArrayList();
            for (final AppConfigValues configValue : configValues)
                configValueList.add(configValue.getValue());
        }
        return configValueList;
    }

    public State getStateByName(final String name) {
        return (State) persistenceService.find("from org.egov.mdm.masters.administration.State where name=?", name);
    }

    public BigDecimal getCertFeeConfigValueforNAForm(final String module, final String key, final String defaultValue) {
        final String appConfigValue = EGovConfig.getAppConfigValue(module, key, defaultValue);
        return new BigDecimal(appConfigValue);

    }

    public String getAppconfigValueResult(final String module, final String key, final String defaultValue) {
        return EGovConfig.getAppConfigValue(module, key, defaultValue);

    }

    /**
     * This method returns hospital list for different hospital type and
     * registration unit. This method considers registration unit based on app
     * config value.
     *
     * @param hospitalType
     * @param regUnit
     * @return list of hospital names
     */

    public List<Establishment> getHospitalByTypeAndUnit(final Integer hospitalType, final Long regUnit) {
        final Criteria estCriteria = persistenceService.getSession().createCriteria(Establishment.class);
        if (hospitalType != null && hospitalType != -1)
            estCriteria.add(Restrictions.eq("type.id", hospitalType));
        if (getAppconfigValue(BndConstants.BNDMODULE, BndConstants.REGUNITKEYFORHOSPITAL, "1") && regUnit != null
                && regUnit != -1)
            estCriteria.add(Restrictions.eq("regUnit.id", regUnit));
        return estCriteria.list();
    }

    public List<Establishment> getHospitalByRoleAndUnit(final String role, final Long regUnit) {
        final Criteria estCriteria = persistenceService.getSession().createCriteria(Establishment.class);
        if (role != "" && role != null && role.equalsIgnoreCase("HospitalRegistrar"))
            estCriteria.add(Restrictions.eq("isAuth", Boolean.TRUE));
        if (role != "" && role != null && role.equalsIgnoreCase("HospitalUser"))
            estCriteria.add(Restrictions.eq("isAuth", Boolean.FALSE));
        if (getAppconfigValue(BndConstants.BNDMODULE, BndConstants.REGUNITKEYFORHOSPITAL, "1") && regUnit != null
                && regUnit != -1)
            estCriteria.add(Restrictions.eq("regUnit.id", regUnit));
        return estCriteria.list();
    }

    public List<Establishment> getHospitalName() {
        return persistenceService.findAllBy(ESTABLISHMENTNAMEQUERY);

    }

    public Establishment getEstablishmentById(final Integer id) {
        return (Establishment) persistenceService.find(ESTABLISHMENTQUERY, Long.valueOf(id));
    }

    /**
     * This Api returns status object
     *
     * @param module
     * @param code
     * @return
     */

    public EgwStatus getStatusByModuleAndCode(final String module, final String code) {
        return (EgwStatus) persistenceService.find(STATUSQUERY, module, code);
    }

    public Module getModuleByPassingCode(final String code) {
        return (Module) persistenceService.find(MODULEQUERY, code);
    }

    public User getUserByPassingUserId(final Integer userId) {
        return (User) persistenceService.find(GETUSERBYPASSINGIDQUERY, userId);

    }

    public List<Disease> getCauseofDeathbyParentId(final Long id) {
        return persistenceService.findAllBy(DISEASEQUERY, id);
    }

    public List<BndFeeTypes> getBndFeeTypes(final String bndFeeType) {
        String typeOfFees = GENERALFEETYPES;
        if (bndFeeType != null)
            if (bndFeeType.equals(BndConstants.SEARCHBIRTH))
                return persistenceService.findAllBy(BNDFEETYPEQUERYFORBIRTH, BIRTHFEETYPES, GENERALFEETYPES);
            else if (bndFeeType.equals(BndConstants.SEARCHDEATH))
                typeOfFees = GENERALFEETYPES;
        return persistenceService.findAllBy(BNDFEETYPEQUERYFORGENERALTYPE, typeOfFees);
    }

    public BndFeeTypes getBndFeeTypesByCode(final String bndFeeTypeCode) {
        if (bndFeeTypeCode != null)
            return (BndFeeTypes) persistenceService.find(GETBNDFEETYPEBYCODE, bndFeeTypeCode);
        return null;
    }

    public CitizenRelation populateCitizenRelation(final BnDCitizen citizen, final BnDCitizen person,
            final String relatedConst) {
        final CitizenRelation relation = new CitizenRelation();
        relation.setCit(citizen);
        relation.setPerson(person);
        relation.setRelatedAs(getCRelationByRelatedConstant(relatedConst));
        return relation;
    }

    public CitizenRelation populateCitizenRelation(final BnDCitizen citizen, final BnDCitizen person,
            final CRelation relation) {
        final CitizenRelation cRelation = new CitizenRelation();
        cRelation.setCit(citizen);
        cRelation.setPerson(person);
        cRelation.setRelatedAs(relation);
        return cRelation;
    }

    /**
     * @param currUserid
     *            : id of the user
     * @return list of role names assigned to the user
     */

    public List<String> getRoleNamesByPassingUserId(final Long currUserid) {
        final User user = (User) persistenceService.find(USERQUERY, currUserid);
        final List<String> roleList = new ArrayList<String>();
        for (final Role role : user.getRoles())
            roleList.add(role.getName().toUpperCase());
        return roleList;
    }

    public List<Role> getRoleNamesByUserId(final Long currUserid, final List<String> roleList) {
        return persistenceService.findAllByNamedQuery(BndConstants.QUERY_GETROLES, currUserid, roleList);

    }

    /**
     * @param id
     * @return registration unit object
     */

    public RegistrationUnit getRegistrationUnitById(final Long id) {
        return (RegistrationUnit) persistenceService.find(REGUNITQUERY, id);
    }

    public BnDCitizen getCitizenById(final Integer id) {
        return (BnDCitizen) persistenceService.find(CITIZENQUERY, id);
    }

    public List<CitizenRelation> getCitizenRelationsByCitizen(final Integer citizenId) {
        return persistenceService.findAllBy("from CitizenRelation rel where rel.cit.citizenID=?", citizenId);
    }

    public ObjectHistory saveHistory(final Registration registration, final String objectType, final String remarks) {
        final User user = getUserByPassingUserId(Integer.valueOf(EGOVThreadLocals.getUserId()));
        return objectHistoryService.save(objectTypeService.getObjectTypebyType(objectType), registration.getId()
                .intValue(), remarks, user);
    }

    public AdoptionInstitute getAdoptionInstituteById(final Long id) {
        return (AdoptionInstitute) persistenceService.find(ADOPTIONINSTITUTEQUERY, id);
    }

    public List<City> getCityByTalukId(final Integer talukId) {
        return persistenceService.findAllBy(CITYQUERY, talukId);
    }

    // TODO
    /*
     * public List<Taluk> getTalukByDistrictId(final Integer districtId) {
     * return persistenceService.findAllBy(TALUKQUERY, districtId); } public
     * List<District> getDistrictByStateId(final Integer stateId) { return
     * persistenceService.findAllBy(DISTRICTQUERY, stateId); }
     */

    /**
     * This method is to get District by passing district id
     *
     * @param id
     * @return District
     */

    /*
     * public District getDistrictById(final Integer id) { return (District)
     * persistenceService.find(DISTRICTBYID, id); } public Taluk
     * getTalukById(final Integer talukId) { return (Taluk)
     * persistenceService.find(TALUKBYID, talukId); }
     */

    public City getCityById(final Integer cityId) {
        return (City) persistenceService.find(CITYBYID, cityId);
    }

    public List<EstablishmentType> getHospitalType() {
        return persistenceService.findAllBy(HOSPITALTYPEQUERY);
    }

    public List<RegistrationUnit> getRegistrationUnit() {
        return persistenceService.findAllBy(REGISTRATIONUNITQUERY);
    }

    public List<User> getUserName(final List<String> roleList) {
        return persistenceService.findAllByNamedQuery(BndConstants.QUERY_GETUSERS, roleList);
    }

    public List<User> getUserName() {
        return persistenceService.findAllBy(USERNAMEQUERY);
    }

    public List<Role> getRole() {
        return persistenceService.findAllBy(ROLE);
    }

    public List findAllBy(final String query) {
        return persistenceService.findAllBy(query);
    }

    public Object findById(final String query, final Object params) {
        return persistenceService.find(query, params);
    }

    /**
     * This method is to check whether the work flow object is created by logged
     * in user or not
     *
     * @param wfObj
     * @return Boolean TRUE - If it is created by logged in user Boolean FALSE -
     *         If it is not created by logged in user
     */

    public Boolean isCreatedByLoggedInUser(final StateAware wfObj) {
        if (wfObj.getCreatedBy() != null
                && wfObj.getCreatedBy().getId().equals(Integer.valueOf(EGOVThreadLocals.getUserId())))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public List<CRelation> getRelationTypesbyConstant(final String[] relation) {
        final Criteria relationCriteria = persistenceService.getSession().createCriteria(CRelation.class,
                "citizenRelations");
        relationCriteria.add(Restrictions.in("relatedAsConst", relation));
        return relationCriteria.list();
    }

    public List<Position> getPositionsForUser(final Integer userId, final Date date) {
        return eisService.getPositionsForUser(Long.valueOf(userId), date);
    }

    public String validateReceiptNumber(final Long registrationid) {
        final Criteria criteria = null;
        criteria.add(Restrictions.eq("feetypeobj.code", "NAMEINCLUSIONFEE"));
        criteria.add(Restrictions.ilike("feecollection.type", BndConstants.BIRTH));
        criteria.add(Restrictions.isNotNull("feecollection.egBills"));
        criteria.add(Restrictions.eq("feecollection.reportId", registrationid));
        if (criteria.list().size() > 0)
            return "yes";
        return "no";

    }

    public void getAllStatesOfCountry() {
        final List<State> stateList = findAllBy(GETALLSTATESQUERY);
        for (final State state : stateList)
            ADDRESSMAP.put(state.getId(), state.getSenderName());
    }

    /*
     * This Api returns logoname. Here the path is appended with city logoname.
     * It refers eg_city_website table, LOGO column. If the data is missing in
     * this column, then india.png image name will be return back as default
     * value.
     */
    public String getCityLogoName(final HttpServletRequest request) {
        // HttpServletRequest request=ServletActionContext.getRequest();
        final HttpSession session = request.getSession();
        final ServletContext servletContext = session.getServletContext();

        final String cityurl = (String) request.getSession().getAttribute("cityurl");
        CityWebsite cityWebsite = null;
        String logoname = servletContext.getRealPath("/") + "images/" + BndConstants.DEFAULTLOGONAME;

        if (cityurl != null) {
            cityWebsite = cityWebsiteService.getCityWebSiteByURL(cityurl);
            logoname = cityWebsite == null ? BndConstants.DEFAULTLOGONAME : cityWebsite.getLogo();
            logoname = servletContext.getRealPath("/") + "images/" + logoname;
        }
        return logoname;

    }

    public String getIndiaImage(final HttpServletRequest request) {
        final HttpSession session = request.getSession();

        final ServletContext servletContext = session.getServletContext();
        final String logoname = servletContext.getRealPath("/") + "images/" + BndConstants.DEFAULTLOGONAME;
        return logoname;

    }

    public List findAll(final String query, final String param) {
        return persistenceService.findAllBy(query, param);
    }
}
