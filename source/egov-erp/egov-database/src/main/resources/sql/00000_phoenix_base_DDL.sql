
CREATE TABLE accountdetailkey (
    id bigint NOT NULL,
    groupid bigint NOT NULL,
    glcodeid bigint,
    detailtypeid bigint NOT NULL,
    detailname character varying(50) NOT NULL,
    detailkey bigint NOT NULL
);




CREATE TABLE accountdetailtype (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(50) NOT NULL,
    tablename character varying(50),
    columnname character varying(50),
    attributename character varying(50) NOT NULL,
    nbroflevels bigint NOT NULL,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone,
    modifiedby bigint,
    full_qualified_name character varying(250)
);




CREATE TABLE accountentitymaster (
    id bigint NOT NULL,
    name character varying(350),
    code character varying(25),
    detailtypeid bigint,
    narration character varying(250),
    isactive smallint,
    lastmodified timestamp without time zone,
    modifiedby bigint,
    created timestamp without time zone
);




CREATE TABLE accountgroup (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    nbroflevels bigint NOT NULL
);




CREATE TABLE bank (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    narration character varying(250),
    isactive smallint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    created timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    type character varying(50)
);




CREATE TABLE bankaccount (
    id bigint NOT NULL,
    branchid bigint NOT NULL,
    accountnumber character varying(20) NOT NULL,
    accounttype character varying(150) NOT NULL,
    narration character varying(250),
    isactive smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    glcodeid bigint,
    currentbalance double precision NOT NULL,
    fundid bigint,
    payto character varying(100),
    type character varying(50)
);




CREATE TABLE bankbranch (
    id bigint NOT NULL,
    branchcode character varying(50) NOT NULL,
    branchname character varying(50) NOT NULL,
    branchaddress1 character varying(50) NOT NULL,
    branchaddress2 character varying(50),
    branchcity character varying(50),
    branchstate character varying(50),
    branchpin character varying(50),
    branchphone character varying(15),
    branchfax character varying(15),
    bankid bigint,
    contactperson character varying(50),
    isactive smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    narration character varying(250),
    micr bigint
);




CREATE TABLE bankentries (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    refno character varying(20) NOT NULL,
    type character(1) NOT NULL,
    txndate timestamp without time zone NOT NULL,
    txnamount bigint NOT NULL,
    glcodeid bigint,
    voucherheaderid bigint,
    remarks character varying(100),
    isreversed smallint,
    instrumentheaderid bigint
);




CREATE TABLE bankentries_mis (
    id bigint NOT NULL,
    bankentries_id bigint,
    function_id bigint
);




CREATE TABLE bankreconciliation (
    id bigint NOT NULL,
    bankaccountid bigint,
    amount double precision,
    transactiontype character varying(2) NOT NULL,
    instrumentheaderid bigint
);




CREATE TABLE calendaryear (
    id bigint NOT NULL,
    calendaryear character varying(50),
    startingdate timestamp without time zone,
    endingdate timestamp without time zone,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone,
    modifiedby smallint
);




CREATE TABLE chartofaccountdetail (
    id bigint NOT NULL,
    glcodeid bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone
);




CREATE TABLE chartofaccounts (
    id bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    name character varying(150) NOT NULL,
    description character varying(250),
    isactiveforposting smallint NOT NULL,
    parentid bigint,
    lastmodified timestamp without time zone,
    modifiedby smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    purposeid bigint,
    operation character(1),
    type character(1) NOT NULL,
    class smallint,
    classification smallint,
    functionreqd bigint,
    budgetcheckreq smallint,
    scheduleid bigint,
    receiptscheduleid bigint,
    receiptoperation character(1),
    paymentscheduleid bigint,
    paymentoperation character(1),
    majorcode character varying(255),
    createdby bigint,
    fiescheduleid bigint,
    fieoperation character varying(1)
);




CREATE TABLE cheque_dept_mapping (
    id bigint NOT NULL,
    allotedto bigint NOT NULL,
    accountchequeid bigint NOT NULL
);




CREATE TABLE closedperiods (
    id bigint NOT NULL,
    startingdate timestamp without time zone NOT NULL,
    endingdate timestamp without time zone NOT NULL,
    isclosed bigint NOT NULL
);




CREATE TABLE codemapping (
    id bigint NOT NULL,
    eg_boundaryid bigint,
    cashinhand bigint NOT NULL,
    chequeinhand bigint NOT NULL
);




CREATE TABLE codeservicemap (
    id bigint NOT NULL,
    serviceid smallint,
    glcodeid integer
);




CREATE TABLE companydetail (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    address1 character varying(50) NOT NULL,
    address2 character varying(50),
    city character varying(50) NOT NULL,
    pin character varying(10) NOT NULL,
    state character varying(25),
    phone character varying(25) NOT NULL,
    contactperson character varying(50),
    mobile character varying(25),
    fax character varying(25),
    email character varying(25),
    isactive smallint NOT NULL,
    modifiedby bigint,
    created timestamp without time zone NOT NULL,
    lastmodified timestamp without time zone,
    narration character varying(250),
    dbname character varying(50)
);




CREATE TABLE contrajournalvoucher (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    frombankaccountid bigint,
    tobankaccountid bigint,
    instrumentheaderid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint
);








CREATE SEQUENCE dtproperties_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE ebpaextnd_dochist_detail (
    id bigint NOT NULL,
    documentnumber character varying(256),
    document_date timestamp without time zone,
    doc_enclosed_extent_areasqmt bigint,
    doc_layout_extent_areasqmt bigint,
    registrationid bigint,
    wheather_plot_developedby character varying(256),
    wheather_document_enclosedby character(1),
    wheather_part_of_layout character(1),
    wheather_fmssketch_copyofreg character(1)
);




CREATE TABLE eg_abstract_estimate_status (
    abstractestimate_id bigint NOT NULL,
    status_id bigint NOT NULL,
    ae_index bigint
);




CREATE TABLE eg_accountservicemapping (
    serviceid bigint NOT NULL,
    accountsid bigint NOT NULL
);




CREATE TABLE eg_action (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    entityid bigint,
    taskid bigint,
    updatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    url character varying(150),
    queryparams character varying(150),
    urlorderid bigint,
    module_id bigint,
    order_number bigint,
    display_name character varying(80),
    is_enabled smallint,
    action_help_url character varying(255),
    context_root character varying(32)
);




CREATE TABLE eg_actiondetails (
    id bigint NOT NULL,
    moduletype character varying(30) NOT NULL,
    moduleid bigint NOT NULL,
    actiondoneby bigint NOT NULL,
    actiondoneon timestamp without time zone NOT NULL,
    comments character varying(500),
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    actiontype character varying(25)
);




CREATE TABLE eg_actionrg_map (
    actionid bigint NOT NULL,
    rulegroupid bigint NOT NULL
);




CREATE TABLE eg_address (
    addressid bigint NOT NULL,
    streetaddress1 character varying(512),
    streetaddress2 character varying(512),
    block character varying(512),
    locality character varying(512),
    citytownvillage character varying(512),
    district character varying(512),
    state character varying(512),
    pincode bigint,
    streetaddress1local character varying(512),
    streeraddress2local character varying(512),
    blocklocal character varying(512),
    localitylocal character varying(512),
    citytownvillagelocal character varying(512),
    districtlocal character varying(512),
    statelocal character varying(512),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_addresstypemaster bigint,
    talukname character varying(256),
    taluklocal character varying(256),
    houseno character varying(32)
);




CREATE TABLE eg_address_type_master (
    id_address_type bigint NOT NULL,
    name_address_type character varying(256) NOT NULL,
    name_address_type_local character varying(256),
    updatedtimestamp timestamp without time zone,
    narration character varying(256)
);




CREATE TABLE eg_adm_bndry (
    id_bndry_master bigint NOT NULL,
    id_adm_bndry bigint NOT NULL,
    bndry_num bigint NOT NULL,
    bndry_name character varying(128) NOT NULL,
    id_parent bigint,
    bndry_name_local character varying(128),
    bndry_name_old character varying(128),
    bndry_name_old_local character varying(128)
);




CREATE TABLE eg_adm_bndry_master (
    id_bndry_master bigint NOT NULL,
    bndry_master_name character varying(128) NOT NULL,
    bndry_heirarchy bigint NOT NULL,
    is_leaf character(1) NOT NULL,
    bndry_master_name_local character varying(128)
);




CREATE TABLE eg_advancereqpayeedetails (
    id bigint NOT NULL,
    advancerequisitiondetailid bigint NOT NULL,
    accountdetailtypeid bigint NOT NULL,
    accountdetailkeyid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    tdsid bigint,
    narration character varying(250)
);




CREATE TABLE eg_advancerequisition (
    id bigint NOT NULL,
    advancerequisitionnumber character varying(100) NOT NULL,
    advancerequisitiondate timestamp without time zone NOT NULL,
    advancerequisitionamount double precision NOT NULL,
    narration character varying(512),
    arftype character varying(50) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone NOT NULL,
    statusid bigint NOT NULL,
    state_id bigint
);




CREATE TABLE eg_advancerequisitiondetails (
    id bigint NOT NULL,
    advancerequisitionid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    creditamount double precision,
    debitamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    narration character varying(256),
    functionid bigint
);




CREATE TABLE eg_advancerequisitionmis (
    id bigint NOT NULL,
    advancerequisitionid bigint NOT NULL,
    fundid bigint,
    fieldid bigint,
    subfieldid bigint,
    functionaryid bigint,
    lastupdatedtime timestamp without time zone NOT NULL,
    departmentid bigint,
    fundsourceid bigint,
    payto character varying(250),
    paybydate timestamp without time zone,
    schemeid bigint,
    subschemeid bigint,
    voucherheaderid bigint,
    sourcepath character varying(256),
    partybillnumber character varying(50),
    partybilldate timestamp without time zone,
    referencenumber character varying(50),
    id_function bigint
);




CREATE TABLE eg_ageing_list (
    id bigint NOT NULL,
    type character varying(20) NOT NULL,
    colheading character varying(50) NOT NULL,
    rangefrom bigint NOT NULL,
    rangeto bigint,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE eg_app_data (
    id bigint NOT NULL,
    key character varying(250) NOT NULL,
    value character varying(250) NOT NULL,
    module character varying(250) NOT NULL
);




CREATE TABLE eg_appconfig (
    id bigint NOT NULL,
    key_name character varying(250) NOT NULL,
    description character varying(250) NOT NULL,
    module character varying(250) NOT NULL
);




CREATE TABLE eg_appconfig_values (
    id bigint NOT NULL,
    key_id bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    value character varying(4000) NOT NULL
);




CREATE TABLE eg_appl_domain (
    id bigint NOT NULL,
    name character varying(128) NOT NULL,
    description character varying(50)
);




CREATE TABLE eg_approvaldetails (
    id bigint,
    moduletype character varying(30),
    moduleid bigint,
    approvedby bigint,
    approveddate timestamp without time zone,
    comments character varying(500),
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    approvaltype character varying(20)
);




CREATE TABLE eg_asset (
    id bigint NOT NULL,
    code character varying(256) NOT NULL,
    statusid bigint NOT NULL,
    description character varying(256),
    departmentid bigint,
    categoryid bigint NOT NULL,
    modeofacquisition character varying(1024),
    name character varying(256) NOT NULL,
    area_id bigint,
    location_id bigint,
    street_id bigint,
    asset_details character varying(4000),
    ward_id bigint,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    date_of_creation timestamp without time zone,
    remark character varying(1024),
    preparedby bigint,
    length bigint,
    width bigint,
    total_area bigint,
    sourcepath character varying(150)
);




CREATE TABLE eg_asset_bills (
    assetid bigint NOT NULL,
    pobillid bigint,
    wobillid bigint,
    billid bigint NOT NULL
);




CREATE TABLE eg_asset_po (
    id bigint NOT NULL,
    assetid bigint NOT NULL,
    workorderid bigint NOT NULL,
    lastupdateddate timestamp without time zone NOT NULL
);




CREATE TABLE eg_asset_sale (
    asset_sale_id bigint NOT NULL,
    assetid bigint NOT NULL,
    date_of_sale timestamp without time zone,
    wdv bigint,
    sale_value bigint,
    reason_for_sale character varying(1025),
    authorized_by character varying(256),
    voucherheaderid bigint NOT NULL,
    as_a_index bigint
);




CREATE TABLE eg_asset_type (
    id bigint NOT NULL,
    name character varying(20)
);




CREATE TABLE eg_assetcategory (
    id bigint NOT NULL,
    code character varying(256) NOT NULL,
    name character varying(256) NOT NULL,
    parentid bigint,
    maxlife bigint,
    assetcode bigint NOT NULL,
    accdepcode bigint,
    depexpcode bigint,
    revcode bigint NOT NULL,
    depmethord bigint,
    depid bigint,
    assettype_id bigint NOT NULL,
    uom_id bigint NOT NULL,
    ac_at_index bigint,
    cat_attr_template character varying(4000),
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL
);




CREATE TABLE eg_assetvaluechange (
    id bigint NOT NULL,
    changetype character(1) NOT NULL,
    changedate timestamp without time zone NOT NULL,
    assetid bigint NOT NULL,
    changereason character varying(250),
    changeamount bigint,
    voucherheaderid bigint NOT NULL,
    isvalueadded character(1),
    approvedby character varying(30),
    avc_a_index bigint
);




CREATE SEQUENCE eg_assetvaluechange_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_attributelist (
    id bigint NOT NULL,
    att_typeid bigint NOT NULL,
    att_key character varying(25) NOT NULL,
    att_value character varying(75) NOT NULL
);




CREATE TABLE eg_attributetype (
    id bigint NOT NULL,
    appl_domainid bigint NOT NULL,
    att_name character varying(50) NOT NULL,
    att_datatype character varying(25) NOT NULL,
    default_value character varying(100),
    isrequired bigint NOT NULL
);




CREATE TABLE eg_attributevalues (
    id bigint NOT NULL,
    appl_domainid bigint NOT NULL,
    att_typeid bigint NOT NULL,
    att_value character varying(200) NOT NULL,
    domaintxnid bigint NOT NULL
);




CREATE TABLE eg_audit_event (
    id bigint NOT NULL,
    pkid bigint,
    module character varying(100) NOT NULL,
    action character varying(100) NOT NULL,
    entity character varying(100) NOT NULL,
    fqcn character varying(100) NOT NULL,
    bizid character varying(100) NOT NULL,
    username character varying(100) NOT NULL,
    details1 character varying(4000) NOT NULL,
    details2 character varying(4000),
    eventdate timestamp without time zone NOT NULL
);




CREATE SEQUENCE eg_audit_event_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE eg_authorization_rule (
    id bigint NOT NULL,
    actionid bigint,
    object_type character varying(256),
    scriptid bigint
);




CREATE TABLE eg_bankaccountservicemapping (
    serviceid bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    id_department bigint NOT NULL,
    id bigint NOT NULL,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    from_date timestamp without time zone,
    to_date timestamp without time zone,
    ecstype bigint
);




CREATE TABLE eg_bankcodes (
    bankcode character varying(255) NOT NULL,
    bankname character varying(255),
    banktype character varying(255),
    bankaddress character varying(255),
    phonenumber character varying(255),
    faxnumber character varying(255)
);




CREATE TABLE eg_bill (
    id bigint NOT NULL,
    id_demand bigint,
    citizen_name character varying(1024) NOT NULL,
    citizen_address character varying(1024) NOT NULL,
    bill_no character varying(20) NOT NULL,
    id_bill_type bigint NOT NULL,
    issue_date timestamp without time zone NOT NULL,
    last_date timestamp without time zone,
    module_id bigint NOT NULL,
    user_id bigint NOT NULL,
    create_time_stamp timestamp without time zone NOT NULL,
    last_updated_timestamp timestamp without time zone NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL,
    is_cancelled character(1) DEFAULT 'N'::bpchar NOT NULL,
    fundcode character varying(32),
    functionary_code double precision,
    fundsource_code character varying(32),
    department_code character varying(32),
    coll_modes_not_allowed character varying(512),
    boundary_num bigint,
    boundary_type character varying(512),
    total_amount double precision,
    total_collected_amount double precision,
    service_code character varying(50),
    part_payment_allowed character(1),
    override_accountheads_allowed character(1),
    description character varying(250),
    min_amt_payable double precision,
    consumer_id character varying(64),
    dspl_message character varying(256),
    callback_for_apportion character(1) DEFAULT 0 NOT NULL,
    coll_type character varying(8),
    pg_type character varying(16),
    receiptno_forcancellation character varying(50),
    reason_cancellation character varying(30)
);




COMMENT ON COLUMN eg_bill.coll_type IS 'Type of Collection, Eg: Counter, Field etc';



COMMENT ON COLUMN eg_bill.pg_type IS 'Type of payment gateway used for online payment, Eg: eBiz, etc';



CREATE TABLE eg_bill_details (
    id bigint NOT NULL,
    id_demand_reason bigint,
    create_time_stamp timestamp without time zone,
    last_updated_timestamp timestamp without time zone NOT NULL,
    id_bill bigint,
    collected_amount double precision,
    order_no bigint,
    glcode character varying(64),
    function_code character varying(32),
    cr_amount double precision,
    dr_amount double precision,
    description character varying(128),
    id_installment bigint,
    additional_flag bigint
);




CREATE TABLE eg_bill_subtype (
    id bigint NOT NULL,
    name character varying(120) NOT NULL,
    expenditure_type character varying(50) NOT NULL
);




CREATE TABLE eg_bill_type (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    code character varying(10) NOT NULL,
    create_time_stamp timestamp without time zone NOT NULL,
    updated_time_stamp timestamp without time zone NOT NULL
);




CREATE TABLE eg_billdetails (
    id bigint NOT NULL,
    billid bigint NOT NULL,
    functionid bigint,
    glcodeid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    narration character varying(250)
);




CREATE TABLE eg_billpayeedetails (
    id bigint NOT NULL,
    billdetailid bigint NOT NULL,
    accountdetailtypeid bigint NOT NULL,
    accountdetailkeyid bigint NOT NULL,
    debitamount double precision,
    creditamount double precision,
    lastupdatedtime timestamp without time zone NOT NULL,
    tdsid bigint,
    narration character varying(250),
    pc_department bigint
);




CREATE TABLE eg_billreceipt (
    id bigint NOT NULL,
    billid bigint NOT NULL,
    receipt_number character varying(50),
    receipt_date timestamp without time zone,
    receipt_amount double precision NOT NULL,
    collection_status character varying(20),
    createtimestamp timestamp without time zone NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    createdby bigint,
    lastmodifiedby bigint,
    is_cancelled character(1) DEFAULT 'N'::bpchar NOT NULL
);




CREATE TABLE eg_billregister (
    id bigint NOT NULL,
    billnumber character varying(50) NOT NULL,
    billdate timestamp without time zone NOT NULL,
    billamount double precision NOT NULL,
    fieldid bigint,
    worksdetailid bigint,
    billstatus character varying(50) NOT NULL,
    narration character varying(1024),
    passedamount double precision,
    billtype character varying(50),
    expendituretype character varying(20) NOT NULL,
    advanceadjusted double precision,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    statusid bigint,
    workorderdate timestamp without time zone,
    zone character varying(20),
    division character varying(50),
    workordernumber character varying(50),
    billpasseddate timestamp without time zone,
    isactive smallint,
    billapprovalstatus character varying(50),
    po character varying(50),
    state_id bigint
);




CREATE TABLE eg_billregistermis (
    id bigint,
    billid bigint NOT NULL,
    fundid bigint,
    segmentid bigint,
    subsegmentid bigint,
    fieldid bigint,
    subfieldid bigint,
    functionaryid bigint,
    sanctionedby character varying(30),
    sanctiondate timestamp without time zone,
    sanctiondetail character varying(200),
    narration character varying(300),
    lastupdatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    disbursementtype character varying(30),
    escalation bigint,
    advancepayments bigint,
    securedadvances bigint,
    deductamountwitheld bigint,
    departmentid bigint,
    month bigint,
    financialyearid bigint,
    fundsourceid bigint,
    rebate real,
    billtype character varying(50),
    payto character varying(250),
    paybydate timestamp without time zone,
    mbrefno character varying(200),
    schemeid bigint,
    subschemeid bigint,
    voucherheaderid bigint,
    sourcepath character varying(150),
    partybillnumber character varying(50),
    partybilldate timestamp without time zone,
    inwardserialnumber character varying(50),
    billsubtype bigint,
    budgetary_appnumber character varying(30),
    budgetcheckreq smallint,
    functionid bigint
);




CREATE TABLE eg_bins (
    id bigint NOT NULL,
    bin_code character varying(30) NOT NULL,
    bin_name character varying(150),
    status bigint NOT NULL,
    storeid bigint
);




CREATE TABLE eg_boundary (
    id_bndry bigint NOT NULL,
    bndry_num bigint,
    parent bigint,
    name character varying(512) NOT NULL,
    id_bndry_type bigint NOT NULL,
    updatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    bndry_name_local character varying(256),
    bndry_name_old character varying(256),
    bndry_name_old_local character varying(256),
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL,
    bndryid bigint,
    lng double precision,
    lat double precision,
    materialized_path character varying(32)
);




CREATE TABLE eg_boundary_type (
    id_bndry_type bigint NOT NULL,
    hierarchy bigint NOT NULL,
    parent bigint,
    name character varying(64) NOT NULL,
    updatedtime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    id_heirarchy_type bigint NOT NULL,
    bndryname_local character varying(64)
);




CREATE TABLE eg_branchcodes (
    branchcode character varying(255) NOT NULL,
    branchaddress character varying(255),
    phonenumber character varying(255),
    faxnumber character varying(255),
    branchname character varying(255),
    bankcode character varying(255)
);




CREATE TABLE eg_catalogue (
    id bigint NOT NULL,
    relationid bigint NOT NULL,
    itemid bigint NOT NULL,
    activefrom timestamp without time zone NOT NULL,
    activeto timestamp without time zone,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE eg_checklists (
    id bigint NOT NULL,
    appconfig_values_id bigint NOT NULL,
    checklistvalue character varying(5) NOT NULL,
    object_id bigint NOT NULL,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE eg_citizen (
    citizenid bigint NOT NULL,
    ssn character varying(512),
    pannumber character varying(256),
    passportnumber character varying(256),
    drivinglicencenumber character varying(256),
    rationcardnumber character varying(256),
    voterregistrationnumber character varying(256),
    firstname character varying(512) NOT NULL,
    middlename character varying(512),
    lastname character varying(512),
    birthdate timestamp without time zone,
    homephone character varying(32),
    officephone character varying(32),
    mobilephone character varying(32),
    fax character varying(32),
    emailaddress character varying(64),
    occupation character varying(256),
    jobstatus character varying(128),
    locale character varying(256),
    firstnamelocal character varying(512),
    lastnamelocal character varying(512),
    ownertitle character varying(64),
    ownertitlelocal character varying(64),
    sex character varying(16),
    middlenamelocal character varying(512),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    fathername character varying(256),
    creationtimestamp timestamp without time zone,
    mnicnumber character varying(32),
    updateuserid integer,
    userid integer
);




CREATE TABLE eg_city_website (
    url character varying(128) NOT NULL,
    bndryid integer NOT NULL,
    cityname character varying(256) NOT NULL,
    citynamelocal character varying(256),
    isactive bigint DEFAULT 1,
    id bigint NOT NULL,
    logo character varying(100)
);




CREATE TABLE eg_complaints_reply (
    complaintid bigint NOT NULL,
    reply_status character varying(3) NOT NULL,
    no_of_days bigint,
    extrafield1 character varying(100),
    extrafield2 character varying(100)
);




CREATE TABLE eg_controller (
    cid bigint NOT NULL,
    featureid bigint,
    jid bigint
);




CREATE TABLE eg_crossheirarchy_linkage (
    id bigint NOT NULL,
    parent bigint NOT NULL,
    child bigint NOT NULL
);




CREATE TABLE eg_deduction_details (
    id bigint NOT NULL,
    tdsid bigint NOT NULL,
    partytypeid bigint,
    doctypeid bigint,
    docsubtypeid bigint,
    datefrom timestamp without time zone,
    dateto timestamp without time zone,
    lowlimit double precision NOT NULL,
    highlimit double precision,
    incometax real,
    surcharge real,
    education real,
    lastmodifieddate timestamp without time zone,
    amount double precision,
    cumulativehighlimit double precision,
    cumulativelowlimit double precision
);




CREATE TABLE eg_default (
    roleid bigint,
    defaultid bigint,
    toplevelbndryid bigint
);




CREATE TABLE eg_demand (
    id bigint NOT NULL,
    id_installment bigint NOT NULL,
    base_demand bigint,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL,
    create_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    last_updated_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    amt_collected double precision,
    status character(1),
    min_amt_payable double precision,
    amt_rebate double precision
);




CREATE TABLE eg_demand_details (
    id bigint NOT NULL,
    id_demand bigint NOT NULL,
    id_demand_reason bigint NOT NULL,
    id_status bigint,
    file_reference_no character varying(32),
    remarks character varying(512),
    amount bigint NOT NULL,
    last_updated_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    create_time_stamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    amt_collected double precision,
    amt_rebate double precision
);




CREATE TABLE eg_demand_reason (
    id bigint NOT NULL,
    id_demand_reason_master bigint NOT NULL,
    id_installment bigint NOT NULL,
    percentage_basis real,
    id_base_reason bigint,
    create_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    last_updated_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    id_account_head bigint,
    purposeid bigint,
    glcodeid bigint
);




CREATE TABLE eg_demand_reason_details (
    id bigint NOT NULL,
    id_demand_reason bigint NOT NULL,
    percentage real,
    from_date timestamp without time zone NOT NULL,
    to_date timestamp without time zone,
    low_limit double precision,
    high_limit double precision,
    create_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    last_updated_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    flat_amount double precision
);




CREATE TABLE eg_demand_reason_master (
    id bigint NOT NULL,
    reason_master character varying(64) NOT NULL,
    id_category bigint NOT NULL,
    is_debit character(1) DEFAULT NULL::bpchar NOT NULL,
    module_id bigint NOT NULL,
    code character varying(16) NOT NULL,
    order_id bigint NOT NULL,
    create_time_stamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    last_updated_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE eg_department (
    id_dept numeric NOT NULL,
    dept_name character varying(64) NOT NULL,
    dept_details character varying(128),
    updatetime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    dept_code character varying(520),
    dept_addr character varying(250),
    isbillinglocation bigint,
    parentid bigint,
    isleaf bigint
);




CREATE TABLE eg_department_address (
    departmentid bigint NOT NULL,
    addressid bigint NOT NULL
);




CREATE TABLE eg_depreciation (
    id_depreciation bigint NOT NULL,
    depreciatedvalue bigint,
    dateofdepreciation timestamp without time zone,
    assetid bigint,
    financialyearid bigint,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    voucherheaderid bigint,
    dep_a_index bigint
);




CREATE SEQUENCE eg_depreciation_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_depreciationmetadata (
    id_depreciationmetadata bigint NOT NULL,
    depreciation_rate bigint,
    finyearid bigint,
    assetcategoryid bigint,
    depmd_ac_index bigint
);




CREATE SEQUENCE eg_depreciationmetadata_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_dept_do_mapping (
    id bigint NOT NULL,
    department_id bigint NOT NULL,
    drawingofficer_id bigint NOT NULL
);




CREATE TABLE eg_dept_functionmap (
    id bigint NOT NULL,
    departmentid bigint,
    functionid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);




CREATE TABLE eg_designation (
    designationid bigint NOT NULL,
    deptid bigint,
    designation_name character varying(256) NOT NULL,
    designation_local character varying(256),
    officer_level bigint,
    designation_description character varying(1024),
    sanctioned_posts bigint,
    outsourced_posts bigint,
    basic_from bigint,
    basic_to bigint,
    ann_increment bigint,
    reportsto bigint,
    grade_id bigint,
    glcodeid bigint
);




CREATE TABLE eg_digital_signed_docs (
    id bigint NOT NULL,
    id_module bigint,
    objecttype character varying(64) NOT NULL,
    objectid bigint NOT NULL,
    objectno character varying(100),
    document bytea NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL
);




CREATE TABLE eg_disbursement_mode (
    id bigint NOT NULL,
    type character varying(64)
);




CREATE TABLE eg_drawingofficer (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(150),
    id_bank bigint,
    id_branch bigint,
    account_number character varying(20),
    tan character varying(10)
);




CREATE TABLE eg_emp_assignment (
    id bigint NOT NULL,
    id_fund bigint,
    id_function bigint,
    designationid bigint,
    id_functionary bigint,
    pct_allocation character varying(256),
    reports_to bigint,
    id_emp_assign_prd bigint,
    field bigint,
    main_dept bigint,
    position_id bigint,
    govt_order_no character varying(256),
    grade_id bigint,
    is_primary character varying(1) DEFAULT 'Y'::character varying NOT NULL
);




CREATE TABLE eg_emp_assignment_prd (
    id bigint NOT NULL,
    from_date timestamp without time zone,
    to_date timestamp without time zone,
    id_employee bigint
);




CREATE TABLE eg_employee (
    id bigint NOT NULL,
    date_of_birth timestamp without time zone,
    blood_group bigint,
    mother_tonuge character varying(256),
    religion_id bigint,
    community_id bigint,
    gender character(1),
    is_handicapped character(1),
    is_med_report_available character(1),
    date_of_first_appointment timestamp without time zone,
    identification_marks1 character varying(1024),
    languages_known_id bigint,
    mode_of_recruiment_id bigint,
    recruitment_type_id bigint,
    employment_status bigint DEFAULT 1,
    category_id bigint,
    qulified_id bigint,
    salary_bank bigint,
    pay_fixed_in_id bigint,
    grade_id bigint,
    present_designation integer,
    scale_of_pay character varying(1024),
    basic_pay bigint,
    spl_pay bigint,
    pp_sgpp_pay bigint,
    annual_increment_id bigint,
    gpf_ac_number character varying(1024),
    retirement_age smallint,
    present_department integer,
    if_on_duty_arrangment_duty_dep character varying(256),
    location character varying(256),
    cost_center character varying(256),
    id_dept bigint,
    id_user bigint,
    isactive smallint,
    empfather_firstname character varying(256),
    empfather_lastname character varying(256),
    empfather_middlename character varying(256),
    emp_firstname character varying(256) NOT NULL,
    emp_lastname character varying(256),
    emp_middlename character varying(256),
    identification_marks2 character varying(1024),
    pan_number character varying(256),
    name character varying(256),
    maturity_date timestamp without time zone,
    bank character varying(256),
    createdtime timestamp without time zone,
    created_by bigint,
    status bigint,
    death_date timestamp without time zone,
    lastmodified_date timestamp without time zone,
    deputation_date timestamp without time zone,
    govt_order_no character varying(256),
    retirement_date timestamp without time zone,
    payment_type character varying(32),
    posting_type_id bigint,
    code character varying(32) NOT NULL,
    modified_by bigint,
    is_avail_quarters smallint DEFAULT 2
);




CREATE VIEW eg_eis_employeeinfo AS
 SELECT eea.id AS ass_id,
    eap.id AS prd_id,
    ee.id,
    ee.code,
    (((((ee.emp_firstname)::text || ' '::text) || (ee.emp_middlename)::text) || ' '::text) || (ee.emp_lastname)::text) AS name,
    eea.designationid,
    eap.from_date,
    eap.to_date,
    eea.reports_to,
    ee.date_of_first_appointment AS date_of_fa,
    ee.isactive,
    eea.main_dept AS dept_id,
    eea.id_functionary AS functionary_id,
    eea.position_id AS pos_id,
    ee.id_user AS user_id,
    ee.status,
    ee.employment_status AS employee_type,
    eea.is_primary,
    eea.id AS ass_id_unique,
    eea.id_function AS function_id
   FROM eg_emp_assignment_prd eap,
    eg_emp_assignment eea,
    eg_employee ee
  WHERE ((ee.id = eap.id_employee) AND (eap.id = eea.id_emp_assign_prd));




CREATE TABLE eg_employee_dept (
    deptid smallint,
    id bigint NOT NULL,
    assignment_id bigint,
    hod bigint
);




CREATE TABLE eg_entity (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone
);




CREATE TABLE eg_entity_history (
    id_history bigint NOT NULL,
    entity_name character varying(128) NOT NULL,
    history_number bigint NOT NULL,
    refpk bigint NOT NULL,
    updated_username_role character varying(256) NOT NULL,
    action character varying(32) NOT NULL,
    entered_date timestamp without time zone NOT NULL,
    lastupdatedtime timestamp without time zone NOT NULL,
    parent bigint
);




CREATE TABLE eg_event (
    eventid bigint,
    eventname character varying(32) NOT NULL
);




CREATE TABLE eg_event_action_citizen (
    actionid bigint NOT NULL,
    complaintid bigint NOT NULL
);




CREATE TABLE eg_event_action_notification (
    userid bigint NOT NULL,
    actionid bigint NOT NULL
);




CREATE TABLE eg_event_actions (
    pkid bigint NOT NULL,
    eventid bigint NOT NULL,
    actioncode character varying(32) NOT NULL
);




CREATE SEQUENCE eg_exemption
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_favourites (
    id bigint,
    user_id bigint,
    action_id bigint,
    fav_name character varying(100),
    ctx_name character varying(50)
);




CREATE SEQUENCE eg_favourites_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE eg_financialyear (
    id bigint NOT NULL,
    financialyear character varying(50),
    startingdate timestamp without time zone,
    endingdate timestamp without time zone,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone,
    modifiedby bigint,
    isactiveforposting smallint NOT NULL,
    isclosed smallint,
    transferclosingbalance smallint
);




CREATE TABLE eg_heirarchy_type (
    id_heirarchy_type bigint NOT NULL,
    type_name character varying(128) NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    type_code character varying(50) NOT NULL
);




CREATE TABLE eg_history_attribs (
    property_name character varying(256) NOT NULL,
    old_value character varying(256),
    id_entity_history bigint,
    new_value character varying(256),
    id_hist_attr bigint NOT NULL,
    lastupdatedtime timestamp without time zone NOT NULL
);




CREATE TABLE eg_ielist (
    id bigint NOT NULL,
    ruleid bigint NOT NULL,
    value character varying(40) NOT NULL,
    type character(1) NOT NULL,
    updatedtime timestamp without time zone
);




CREATE TABLE eg_installment_master (
    id_installment bigint NOT NULL,
    installment_num bigint NOT NULL,
    installment_year timestamp without time zone NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    id_module bigint,
    lastupdatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    description character varying(25),
    installment_type character varying(50)
);




CREATE TABLE eg_integration_data (
    id bigint NOT NULL,
    type character varying(30) NOT NULL,
    aliasname character varying(30) NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    functionaryid bigint,
    functionid bigint,
    accountcodeid bigint,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    voucher_name character varying(40)
);




CREATE TABLE eg_intg_agg_accthd (
    id bigint NOT NULL,
    aggregate_id bigint NOT NULL,
    id_account_head bigint NOT NULL,
    amount double precision NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL
);




CREATE TABLE eg_intg_aggr_tx (
    id bigint NOT NULL,
    aggregate_id bigint,
    transaction_id bigint,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_account_head bigint,
    is_suspense_act_bal character(1) NOT NULL,
    last_tx_read_time timestamp without time zone NOT NULL,
    amount double precision NOT NULL,
    old_vouchernum character varying(256),
    is_old character(1)
);




CREATE TABLE eg_intg_fin_aggregate (
    id bigint NOT NULL,
    total_amount double precision,
    glcodeid_for_mode bigint,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    is_posted character(1),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    integrated_vouchernum character varying(256),
    exception_message character varying(2048),
    mode_of_collection bigint,
    id_module bigint,
    voucher_type character varying(60)
);




CREATE TABLE eg_intg_misdata (
    id bigint NOT NULL,
    aggregate_id bigint NOT NULL,
    name character varying(256) NOT NULL,
    value character varying(512) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL
);




CREATE TABLE eg_invstatus (
    id bigint NOT NULL,
    status character varying(20) NOT NULL,
    moduletype character varying(10) NOT NULL
);




CREATE TABLE eg_item (
    id bigint NOT NULL,
    itemno character varying(30) NOT NULL,
    itemtypeid bigint NOT NULL,
    uomid bigint NOT NULL,
    narration character varying(512),
    lastmodified timestamp without time zone NOT NULL,
    isinventoryasset bigint NOT NULL,
    itemstatusid bigint NOT NULL,
    lastmodifiedby bigint,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    stockinguomid bigint,
    abcclass character(1),
    minsoh bigint,
    islotcontrolled bigint NOT NULL,
    taxable bigint,
    techspecification character varying(250),
    termsofdelivery character varying(250),
    model character varying(250),
    expenseglcodeid bigint,
    contractreqt character(1) NOT NULL,
    puchuomid bigint NOT NULL,
    canpurchase bigint NOT NULL,
    isshelflifecontrolled bigint NOT NULL,
    isindentrequired smallint DEFAULT 0,
    manufacturerpartno character varying(250),
    allownegetiveqty bigint DEFAULT 0,
    binlevelstorage bigint DEFAULT 0,
    isscrap bigint DEFAULT 0
);




CREATE TABLE eg_itemtype (
    id bigint NOT NULL,
    itemtype character varying(30) NOT NULL,
    narration character varying(512),
    lastmodified timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    stockinhandcodeid bigint,
    taxcodeid bigint,
    parentitemtype bigint
);




CREATE TABLE eg_location (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(100),
    locationid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    isactive smallint,
    islocation smallint
);




CREATE TABLE eg_location_ipmap (
    id bigint NOT NULL,
    locationid bigint NOT NULL,
    ipaddress character varying(150) NOT NULL
);




CREATE TABLE eg_login_log (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    logintime timestamp without time zone,
    logouttime timestamp without time zone,
    locationid bigint,
    ipaddress character varying(15)
);




CREATE TABLE eg_master_default (
    defaultname character varying(64),
    defaultid bigint NOT NULL
);




CREATE TABLE eg_module (
    id_module bigint NOT NULL,
    module_name character varying(100) NOT NULL,
    lastupdatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    isenabled smallint,
    module_namelocal character varying(128),
    baseurl character varying(256),
    parentid bigint,
    module_desc character varying(256),
    order_num bigint
);




CREATE TABLE eg_modules (
    id bigint NOT NULL,
    name character varying(30) NOT NULL,
    description character varying(250)
);




CREATE TABLE eg_number_generic (
    id bigint NOT NULL,
    objecttype character varying(50) NOT NULL,
    value bigint NOT NULL,
    updatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE eg_numbers (
    id bigint NOT NULL,
    vouchertype character varying(50) NOT NULL,
    vouchernumber bigint NOT NULL,
    fiscialperiodid bigint NOT NULL,
    month bigint
);




CREATE TABLE eg_object_history (
    id bigint NOT NULL,
    object_type_id bigint,
    modifed_by bigint,
    object_id bigint,
    remarks character varying(4000),
    modifieddate timestamp without time zone
);




CREATE TABLE eg_object_type (
    id bigint NOT NULL,
    type character varying(20) NOT NULL,
    description character varying(50),
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE eg_partytype (
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    parentid bigint,
    description character varying(100) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE eg_payment (
    paymentid bigint,
    paymentvalue character varying(10),
    paymentamount double precision
);




CREATE TABLE eg_portal_organization (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    parentid bigint,
    id_ward bigint
);




CREATE SEQUENCE eg_portal_organization_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE eg_portal_user (
    id bigint NOT NULL,
    username character varying(64) NOT NULL,
    email character varying(64) NOT NULL,
    pwd character varying(64) NOT NULL,
    name character varying(255),
    organization_id bigint,
    mob_number bigint NOT NULL,
    alternate_number bigint,
    address_id bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    is_active smallint NOT NULL,
    service_dept_id bigint,
    CONSTRAINT portal_name_orgn_check CHECK (((((name IS NOT NULL) AND ((name)::text <> ''::text)) OR ((organization_id IS NOT NULL) AND ((organization_id)::text <> ''::text))) AND (NOT (((name IS NOT NULL) AND ((name)::text <> ''::text)) AND ((organization_id IS NOT NULL) AND ((organization_id)::text <> ''::text))))))
);




CREATE SEQUENCE eg_portal_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE eg_position (
    position_name character varying(256) NOT NULL,
    id bigint NOT NULL,
    sanctioned_posts bigint,
    outsourced_posts bigint,
    desig_id bigint,
    effective_date timestamp without time zone,
    id_drawing_officer bigint,
    id_deptdesig bigint,
    ispost_outsourced smallint
);




CREATE TABLE eg_position_hir (
    id bigint NOT NULL,
    position_from bigint,
    position_to bigint,
    object_type_id bigint
);




CREATE TABLE eg_ptbill_agent (
    id_billagent bigint NOT NULL,
    id_ptdemand bigint,
    create_timestamp timestamp without time zone,
    updt_timestamp timestamp without time zone
);




CREATE TABLE eg_reason_category (
    id_type bigint NOT NULL,
    name character varying(64) NOT NULL,
    code character varying(64) NOT NULL,
    order_id bigint NOT NULL,
    last_updated_timestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE eg_remittance (
    id bigint NOT NULL,
    tdsid bigint NOT NULL,
    fundid bigint NOT NULL,
    fyid bigint NOT NULL,
    month bigint NOT NULL,
    paymentvhid bigint NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    asondate timestamp without time zone
);




CREATE TABLE eg_remittance_detail (
    id bigint NOT NULL,
    remittanceid bigint NOT NULL,
    remittancegldtlid bigint,
    lastmodifieddate timestamp without time zone,
    remittedamt double precision,
    generalledgerid bigint
);




CREATE TABLE eg_remittance_gldtl (
    id bigint NOT NULL,
    gldtlid bigint NOT NULL,
    gldtlamt double precision,
    lastmodifieddate timestamp without time zone,
    remittedamt double precision,
    tdsid bigint
);




CREATE TABLE eg_revaluations (
    revaluationid bigint NOT NULL,
    revaluationdate timestamp without time zone,
    revaluationamount bigint,
    isvalueadded character(1),
    revaluationreason character varying(1024),
    assetid bigint,
    approvedby character varying(256)
);




CREATE TABLE eg_rgrule_map (
    rulegroupid bigint NOT NULL,
    ruleid bigint NOT NULL
);




CREATE TABLE eg_roleaction_map (
    roleid bigint,
    actionid bigint
);




CREATE TABLE eg_roles (
    id_role bigint NOT NULL,
    role_name character varying(32) NOT NULL,
    role_desc character varying(128),
    role_name_local character varying(64),
    role_desc_local character varying(128),
    updatetime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    updateuserid bigint
);




CREATE TABLE eg_router (
    id bigint NOT NULL,
    pgrflag bigint NOT NULL,
    userid bigint,
    bndryid bigint NOT NULL,
    deptid bigint,
    complainttypeid bigint NOT NULL
);




CREATE TABLE eg_rulegroup (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone,
    roleid bigint NOT NULL
);




CREATE TABLE eg_rules (
    id bigint NOT NULL,
    name character varying(50),
    defaultvalue character varying(50),
    minrange bigint,
    maxrange bigint,
    updatedtime timestamp without time zone,
    type character varying(30),
    active bigint,
    included smallint,
    excluded smallint
);




CREATE TABLE eg_ruletype (
    id bigint NOT NULL,
    name character varying(50),
    updatedtime timestamp without time zone
);




CREATE TABLE eg_script (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    script_type character varying(256) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    script text,
    start_date timestamp without time zone DEFAULT '1900-01-01 00:00:00'::timestamp without time zone NOT NULL,
    end_date timestamp without time zone DEFAULT '2100-01-01 00:00:00'::timestamp without time zone NOT NULL,
    CONSTRAINT check_start_end CHECK ((start_date <= end_date))
);




CREATE SEQUENCE eg_script_seq
    START WITH 301
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE eg_securitydeposit (
    securitydepositid bigint NOT NULL,
    retainedamount bigint,
    releasedate timestamp without time zone,
    releasedamount bigint
);




CREATE SEQUENCE eg_securitydeposit_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_service_accountdetails (
    id bigint NOT NULL,
    id_servicedetails bigint NOT NULL,
    glcodeid bigint NOT NULL,
    amount double precision,
    functionid bigint
);




CREATE TABLE eg_service_dept_mapping (
    id_department bigint NOT NULL,
    id_servicedetails bigint NOT NULL
);




CREATE TABLE eg_service_subledgerinfo (
    id bigint NOT NULL,
    id_accountdetailtype bigint NOT NULL,
    id_accountdetailkey bigint,
    amount double precision,
    id_serviceaccountdetail bigint NOT NULL
);




CREATE TABLE eg_servicecategory (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    code character varying(50) NOT NULL,
    isactive character(1) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL
);




CREATE TABLE eg_servicedetails (
    id bigint NOT NULL,
    servicename character varying(100) NOT NULL,
    serviceurl character varying(256),
    isenabled smallint,
    callbackurl character varying(256),
    servicetype character(1),
    code character varying(12) NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    functionaryid bigint,
    voucher_creation smallint,
    schemeid bigint,
    subschemeid bigint,
    id_service_category bigint,
    isvoucherapproved smallint,
    vouchercutoffdate timestamp without time zone
);




CREATE TABLE eg_stores (
    id bigint NOT NULL,
    storeno character varying(30) NOT NULL,
    storename character varying(30) NOT NULL,
    narration character varying(250),
    deptid bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    deliveryaddr character varying(300),
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    billingaddr character varying(300) NOT NULL,
    phoneno bigint
);




CREATE TABLE eg_streetaddress (
    streetid bigint NOT NULL,
    bndryid bigint,
    streetaddress character varying(255),
    numberofproperties bigint,
    streetno bigint
);




CREATE TABLE eg_surrendered_cheques (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    chequenumber character varying(20) NOT NULL,
    chequedate timestamp without time zone NOT NULL,
    vhid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE eg_tasks (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    updatedtime timestamp without time zone
);




CREATE TABLE eg_terminal (
    id bigint NOT NULL,
    terminal_name character varying(16),
    ip_address character varying(16),
    terminal_desc character varying(64)
);




CREATE TABLE eg_token (
    id bigint NOT NULL,
    token_number character varying(128) NOT NULL,
    ttl_secs bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    token_identity character varying(100)
);




CREATE TABLE eg_uom (
    id bigint NOT NULL,
    uomcategoryid bigint NOT NULL,
    uom character varying(30) NOT NULL,
    narration character varying(250),
    conv_factor bigint NOT NULL,
    baseuom smallint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);




CREATE TABLE eg_uomcategory (
    id bigint NOT NULL,
    category character varying(30) NOT NULL,
    narration character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);




CREATE TABLE eg_user (
    id_user bigint NOT NULL,
    title character varying(8),
    salutation character varying(5),
    first_name character varying(32) NOT NULL,
    middle_name character varying(32),
    last_name character varying(32),
    dob timestamp without time zone,
    id_department bigint,
    locale character varying(16),
    user_name character varying(64) NOT NULL,
    pwd character varying(64) NOT NULL,
    pwd_reminder character varying(64),
    updatetime timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    updateuserid bigint,
    extrafield1 character varying(64),
    extrafield2 character varying(64),
    extrafield3 character varying(64),
    extrafield4 character varying(64),
    is_suspended character(1) DEFAULT 'N'::bpchar NOT NULL,
    id_top_bndry bigint DEFAULT 1,
    reportsto bigint,
    isactive bigint DEFAULT 1 NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    user_sign bigint,
    pwd_updated_date timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    organizationid bigint,
    mobilenumber character varying(50),
    alternatenumber character varying(50),
    addressid bigint,
    email character varying(128),
    isportaluser smallint DEFAULT 0
);




CREATE TABLE eg_user_jurlevel (
    id_user_jurlevel bigint NOT NULL,
    id_user bigint NOT NULL,
    id_bndry_type bigint NOT NULL,
    updatetime timestamp without time zone NOT NULL
);




CREATE TABLE eg_user_jurvalues (
    id_user_jurlevel bigint NOT NULL,
    id_bndry bigint NOT NULL,
    id bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL
);




CREATE TABLE eg_user_sign (
    id bigint NOT NULL,
    user_signature bytea,
    digi_signature bytea
);




CREATE TABLE eg_usercounter_map (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    counterid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE eg_userdetails (
    id_userdet bigint NOT NULL,
    id_user bigint NOT NULL,
    id_bankbranch bigint,
    extrafield1 character varying(32),
    extrafield2 character varying(32),
    extrafield3 character varying(32),
    dob timestamp without time zone,
    locale character varying(16),
    id_emp character varying(16)
);




CREATE TABLE eg_userrole (
    id_role bigint NOT NULL,
    id_user bigint NOT NULL,
    id bigint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar NOT NULL
);




CREATE TABLE eg_view (
    complaintnumber character varying(32),
    userid bigint,
    dateofview timestamp without time zone
);




CREATE TABLE eg_wf_actions (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(1024) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE SEQUENCE eg_wf_actions_seq
    START WITH 155
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_wf_amountrule (
    id bigint NOT NULL,
    fromqty bigint,
    toqty bigint,
    ruledesc character varying(30)
);




CREATE SEQUENCE eg_wf_amountrule_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 9999999
    CACHE 1;




CREATE TABLE eg_wf_matrix (
    id bigint NOT NULL,
    department character varying(30),
    objecttype character varying(30) NOT NULL,
    currentstate character varying(30),
    currentstatus character varying(30),
    pendingactions character varying(512),
    currentdesignation character varying(512),
    additionalrule character varying(50),
    nextstate character varying(30),
    nextaction character varying(100),
    nextdesignation character varying(512),
    nextstatus character varying(30),
    validactions character varying(512) NOT NULL,
    fromqty bigint,
    toqty bigint
);




CREATE SEQUENCE eg_wf_matrix_seq
    START WITH 262
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 9999999
    CACHE 1;




CREATE TABLE eg_wf_states (
    id bigint NOT NULL,
    type character varying(255) NOT NULL,
    value character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    owner bigint,
    date1 timestamp without time zone,
    date2 timestamp without time zone,
    text1 character varying(1024),
    text2 character varying(1024),
    previous bigint,
    next bigint,
    next_action character varying(255)
);




CREATE SEQUENCE eg_wf_states_seq
    START WITH 16
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE eg_wf_types (
    id_type bigint NOT NULL,
    module_id bigint NOT NULL,
    wf_type character varying(100) NOT NULL,
    wf_link character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    render_yn character varying(1),
    group_yn character varying(1),
    full_qualified_name character varying(255) NOT NULL,
    display_name character varying(100) NOT NULL
);




CREATE SEQUENCE eg_wf_types_seq
    START WITH 70
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egasset_activities (
    id bigint NOT NULL,
    assetid bigint NOT NULL,
    additionamount double precision,
    deductionamount double precision,
    activitydate timestamp without time zone NOT NULL,
    voucherheaderid bigint,
    identifier character(1) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    description character varying(256),
    state_id bigint,
    statusid bigint
);




CREATE TABLE egasset_depreciation (
    id bigint NOT NULL,
    assetid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone NOT NULL,
    amount double precision,
    voucherheaderid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    isbatchprocessed smallint
);




CREATE TABLE egasset_disposalsalemis (
    id bigint NOT NULL,
    assetid bigint NOT NULL,
    disposal_sale_date timestamp without time zone NOT NULL,
    disposal_sale_partyname character varying(256),
    disposal_sale_partyaddress character varying(256),
    disposal_sale_reason character varying(256),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egasset_openingbalance (
    id bigint NOT NULL,
    assetid bigint NOT NULL,
    financialyearid bigint NOT NULL,
    grossopeningbalance double precision,
    deductionopeningbalance double precision,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egasset_revaluation (
    id bigint
);




CREATE TABLE egasset_subledger (
    id bigint NOT NULL,
    voucherdetailid bigint NOT NULL,
    detailkey bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    amount double precision NOT NULL
);




CREATE TABLE egasset_voucherdetail (
    id bigint NOT NULL,
    assetvoucherid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL
);




CREATE TABLE egasset_voucherheader (
    id bigint NOT NULL,
    assetrefid bigint NOT NULL,
    fundid bigint,
    departmentid bigint,
    schemeid bigint,
    subschemeid bigint,
    fundsourceid bigint,
    fieldid bigint,
    functionaryid bigint,
    functionid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    voucherdate timestamp without time zone NOT NULL,
    description character varying(256)
);




CREATE TABLE egbpa_address (
    id bigint NOT NULL,
    addresstypeid bigint NOT NULL,
    registrationid bigint NOT NULL,
    plotdoornumber character varying(32),
    plotlandmark character varying(128),
    plotnumber character varying(32),
    plotsurveynumber character varying(256),
    plotblocknumber character varying(512),
    streetaddress1 character varying(512),
    streetaddress2 character varying(512),
    citytown character varying(512),
    villageid bigint,
    blocknumber character varying(512),
    stateid bigint,
    pincode bigint
);




CREATE TABLE egbpa_apprd_buildingdetails (
    id bigint NOT NULL,
    registrationid bigint,
    unit_classification character varying(64),
    unit_count bigint,
    floor_count bigint,
    isbasementunit character(1) DEFAULT 0,
    building_height bigint,
    total_floorarea bigint
);




CREATE TABLE egbpa_apprd_buildingfloordtls (
    id bigint NOT NULL,
    approvedbldgid bigint,
    exist_bldg_area bigint,
    proposed_bldg_area bigint,
    exist_bldg_usage bigint,
    proposed_bldg_usage bigint,
    floor_num bigint
);




CREATE TABLE egbpa_autodcr (
    id bigint NOT NULL,
    autodcr_num character varying(64) NOT NULL,
    applicant_name character varying(64),
    address character varying(256),
    email_id character varying(64),
    mobile_no bigint,
    zone character varying(64),
    ward character varying(64),
    door_no character varying(512),
    plotnumber character varying(32),
    survey_no character varying(256),
    village character varying(512),
    blocknumber character varying(512),
    plotarea bigint,
    floor_count bigint
);




CREATE TABLE egbpa_autodcr_floordetails (
    id bigint NOT NULL,
    autodcr_id bigint NOT NULL,
    existing_bldg_area bigint,
    existing_bldg_usage bigint,
    proposed_bldg_area bigint,
    proposed_bldg_usage bigint,
    floor_num bigint
);




CREATE TABLE egbpa_ddfee_details (
    id bigint NOT NULL,
    ddamount bigint,
    ddno character varying(64),
    dddate timestamp without time zone,
    ddtype character varying(126),
    registrationid bigint,
    ddbank bigint
);




CREATE TABLE egbpa_inspect_measurementdtls (
    id bigint NOT NULL,
    inspectiondtlsid bigint,
    inspectionsourceid bigint,
    fsb bigint,
    rsb bigint,
    ssb1 bigint,
    ssb2 bigint,
    pass_width bigint,
    passage_length bigint,
    surroundedbynorth bigint,
    surroundedbysouth bigint,
    surroundedbyeast bigint,
    surroundedbywest bigint
);




CREATE TABLE egbpa_inspection (
    id bigint NOT NULL,
    inspection_num character varying(64),
    inspection_date timestamp without time zone NOT NULL,
    inspectedby bigint NOT NULL,
    registrationid bigint,
    parent bigint,
    isinspected character(1) DEFAULT 0,
    ispostponeddate character(1) DEFAULT 0,
    postponementreason character varying(256),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_inspection_checklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1) DEFAULT 0,
    inspectiondtlsid bigint
);




CREATE TABLE egbpa_inspection_details (
    id bigint NOT NULL,
    conststagesid bigint,
    building_extent character varying(256),
    num_of_plots bigint,
    remarks character varying(1064)
);




CREATE TABLE egbpa_lettertoparty (
    id bigint NOT NULL,
    lp_num character varying(128),
    acknowledgementnumber character varying(128),
    inspectionid bigint,
    registrationid bigint,
    letterdate timestamp without time zone NOT NULL,
    lp_reasonid bigint NOT NULL,
    sent_date timestamp without time zone,
    reply_date timestamp without time zone,
    lpremarks character varying(1024),
    lpreplyremarks character varying(1024),
    lpdesc character varying(1024),
    lpreplydesc character varying(1024),
    is_history character(1),
    documentid character varying(256)
);




CREATE TABLE egbpa_lpchecklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1),
    lp_checklist_type character varying(64),
    lpid bigint,
    remarks character varying(300)
);




CREATE TABLE egbpa_mstr_bpafee (
    id bigint NOT NULL,
    servicetypeid bigint NOT NULL,
    fundid bigint NOT NULL,
    functionid bigint NOT NULL,
    fee_type character varying(64) NOT NULL,
    fee_code character varying(3) NOT NULL,
    fee_description character varying(64) NOT NULL,
    glcodeid bigint NOT NULL,
    isfixedamount character(1) DEFAULT 0,
    isactive character(1) DEFAULT 1 NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    ismandatory smallint DEFAULT 0 NOT NULL,
    fee_description_local character varying(256),
    order_num bigint,
    isplanningpermitfee character(1) DEFAULT 1 NOT NULL,
    feegroup character varying(128)
);




CREATE TABLE egbpa_mstr_bpafeedetail (
    id bigint NOT NULL,
    feeid bigint NOT NULL,
    from_areasqmt bigint,
    to_areasqmt bigint,
    amount bigint NOT NULL
);




CREATE TABLE egbpa_mstr_buildingcategory (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpa_mstr_buildingusage (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpa_mstr_changeofusage (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpa_mstr_checklist (
    id bigint NOT NULL,
    checklisttype character varying(128) NOT NULL,
    servicetypeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_mstr_checklistdetail (
    id bigint NOT NULL,
    checklistid bigint NOT NULL,
    code character varying(3) NOT NULL,
    description character varying(256) NOT NULL,
    ismandatory character(1) DEFAULT 0,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpa_mstr_const_stages (
    id bigint NOT NULL,
    const_stage character varying(256) NOT NULL,
    description character varying(64),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_mstr_inspectionsource (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_mstr_lpreason (
    id bigint NOT NULL,
    reason character varying(1024) NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_mstr_servicetype (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    iscmdatype character(1) DEFAULT 0,
    isinspectionfeerequired character(1) DEFAULT 0 NOT NULL,
    isscrutinyfeerequired character(1) DEFAULT 0 NOT NULL,
    isptisnumberrequired character(1) DEFAULT 0 NOT NULL,
    isautodcrnumberrequired character(1) DEFAULT 0 NOT NULL,
    servicenumberprefix character varying(10) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL,
    description_local character varying(256)
);




CREATE TABLE egbpa_mstr_surroundedbldgdtls (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpa_mstr_surveyorname (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    regnnum character varying(64),
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpa_mstr_village (
    id bigint NOT NULL,
    villagename character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpa_registration (
    id bigint NOT NULL,
    ba_num character varying(128),
    ba_order_date timestamp without time zone,
    psn_num character varying(128) NOT NULL,
    psn_date timestamp without time zone NOT NULL,
    cmda_num character varying(128),
    cmda_ref_date timestamp without time zone,
    app_type character varying(128) NOT NULL,
    propertyid character varying(128),
    parent bigint,
    servicetypeid bigint NOT NULL,
    demandid bigint,
    stateid bigint,
    statusid bigint NOT NULL,
    ownerid bigint NOT NULL,
    surveyorid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    adminboundaryid bigint NOT NULL,
    locboundaryid bigint,
    documentid character varying(256),
    rejectionid bigint,
    approverid bigint,
    ppa_num character varying(256),
    issanctionfeeraised bigint DEFAULT 0,
    feeremarks character varying(1024),
    externalfeecollecteddate timestamp without time zone,
    securitykey character varying(64),
    exist_ppanum character varying(128),
    exist_banum character varying(128),
    app_mode character varying(128) DEFAULT 'General'::character varying,
    request_number character varying(50),
    servicereqregistryid bigint,
    serviceregistryid bigint
);




CREATE TABLE egbpa_registration_checklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1) DEFAULT 0,
    registrationid bigint
);




CREATE TABLE egbpa_registrationfee (
    id bigint NOT NULL,
    feedate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    feeremarks character varying(1024),
    stateid bigint,
    statusid bigint NOT NULL,
    isrevised bigint DEFAULT 0,
    registrationid bigint NOT NULL,
    challannumber character varying(128),
    createdby bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_registrationfeedetail (
    id bigint NOT NULL,
    registrationfeeid bigint NOT NULL,
    bpafeeid bigint NOT NULL,
    amount bigint DEFAULT 0
);




CREATE TABLE egbpa_regn_approvalinfo (
    id bigint NOT NULL,
    approval_type bigint NOT NULL,
    comm_approved_date timestamp without time zone NOT NULL,
    usage_from bigint NOT NULL,
    registrationid bigint NOT NULL,
    usage_to bigint NOT NULL,
    isforward_to_cmda character(1) NOT NULL,
    date_of_forward timestamp without time zone,
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpa_regn_autodcr (
    id bigint NOT NULL,
    lpid bigint,
    registrationid bigint NOT NULL,
    autodcr_num character varying(64) NOT NULL,
    isactive character(1) DEFAULT 0
);




CREATE TABLE egbpa_regn_details (
    id bigint NOT NULL,
    registrationid bigint NOT NULL,
    exist_bldgcatgid bigint,
    proposed_bldgcatgid bigint,
    sital_areasqmt bigint,
    sital_areasqft bigint,
    remarks character varying(256)
);




CREATE TABLE egbpa_regnstatus_details (
    id bigint NOT NULL,
    registrationid bigint NOT NULL,
    statusdate timestamp without time zone,
    remarks character varying(1064),
    statusid bigint NOT NULL
);




CREATE TABLE egbpa_rejection (
    id bigint NOT NULL,
    rejectiondate timestamp without time zone NOT NULL,
    remarks character varying(256),
    rejectionnumber character varying(32)
);




CREATE TABLE egbpa_rejection_checklist (
    id bigint NOT NULL,
    rejectionid bigint,
    checklistdetailid bigint,
    ischecked character(1),
    remarks character varying(300)
);




CREATE TABLE egbpaextnd_address (
    id bigint NOT NULL,
    addresstypeid bigint NOT NULL,
    registrationid bigint NOT NULL,
    plotdoornumber character varying(32),
    plotlandmark character varying(128),
    plotnumber character varying(32),
    plotsurveynumber character varying(256),
    plotblocknumber character varying(512),
    streetaddress1 character varying(512),
    streetaddress2 character varying(512),
    citytown character varying(512),
    villageid bigint,
    blocknumber character varying(512),
    stateid bigint,
    pincode bigint
);




CREATE TABLE egbpaextnd_apprd_bldgdetails (
    id bigint NOT NULL,
    registrationid bigint,
    unit_classification character varying(64),
    unit_count bigint,
    floor_count bigint,
    isbasementunit character(1) DEFAULT 0,
    building_height bigint,
    total_floorarea bigint
);




CREATE TABLE egbpaextnd_apprd_bldgfloordtls (
    id bigint NOT NULL,
    approvedbldgid bigint,
    exist_bldg_area bigint,
    proposed_bldg_area bigint,
    exist_bldg_usage bigint,
    proposed_bldg_usage bigint,
    floor_num bigint
);




CREATE TABLE egbpaextnd_autodcr (
    id bigint NOT NULL,
    autodcr_num character varying(64) NOT NULL,
    applicant_name character varying(64),
    address character varying(256),
    email_id character varying(64),
    mobile_no bigint,
    zone character varying(64),
    ward character varying(64),
    door_no character varying(512),
    plotnumber character varying(32),
    survey_no character varying(256),
    village character varying(512),
    blocknumber character varying(512),
    plotarea bigint,
    floor_count bigint
);




CREATE TABLE egbpaextnd_autodcr_floordetail (
    id bigint NOT NULL,
    autodcr_id bigint NOT NULL,
    existing_bldg_area bigint,
    existing_bldg_usage bigint,
    proposed_bldg_area bigint,
    proposed_bldg_usage bigint,
    floor_num bigint
);




CREATE TABLE egbpaextnd_ddfee_details (
    id bigint NOT NULL,
    ddamount bigint,
    ddno character varying(64),
    dddate timestamp without time zone,
    ddtype character varying(126),
    registrationid bigint,
    ddbank bigint
);




CREATE TABLE egbpaextnd_docket (
    id bigint NOT NULL,
    statusofapplicant character varying(20),
    existingusage character varying(52),
    proposedactivitypermissible character varying(52),
    old_proptax_paidrecpt_enclosd character varying(5),
    abuttingroad_width bigint,
    abuttingroad_publicorprivate character varying(7),
    abuttingroad_takenupforimpmnt character varying(5),
    abuttingroad_gainaccessthrpsg character varying(5),
    abuttingroad_gainwidth bigint,
    abuttingroad_gainpuborprivate character varying(7),
    plancomplieswithsidecondition character varying(5),
    remarks character varying(512),
    aeeinspectionreport character varying(512),
    createdby bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    totalfloor_count bigint,
    length_of_compoundwall bigint,
    diameter_of_well bigint,
    seperatelat_tank bigint,
    terraced bigint,
    tiled_roof bigint,
    plotwidth_rear bigint DEFAULT 0,
    constructionwidth_rear bigint DEFAULT 0,
    constructionheight_rear bigint DEFAULT 0
);




CREATE TABLE egbpaextnd_docket_constrnstage (
    id bigint NOT NULL,
    checklistdetailid bigint NOT NULL,
    docketid bigint NOT NULL,
    value character varying(5),
    remarks character varying(256)
);




CREATE TABLE egbpaextnd_docket_documentdtl (
    id bigint NOT NULL,
    checklistdetailid bigint NOT NULL,
    docketid bigint NOT NULL,
    value character varying(256),
    remarks character varying(256)
);




CREATE TABLE egbpaextnd_docket_floordetails (
    id bigint NOT NULL,
    docketid bigint NOT NULL,
    floor_num bigint,
    floor_value bigint
);




CREATE TABLE egbpaextnd_docket_violationdtl (
    id bigint NOT NULL,
    checklistdetailid bigint NOT NULL,
    docketid bigint NOT NULL,
    value character varying(7),
    required character varying(20),
    provided character varying(20),
    extentofviolation character varying(20),
    percentageofviolation character varying(20),
    remarks character varying(256)
);




CREATE TABLE egbpaextnd_documenthistory (
    id bigint NOT NULL,
    registrationid bigint,
    extent_areasqmt bigint,
    surveynumber character varying(256),
    vendor character varying(256),
    purchaser character varying(256),
    nature_of_deed character varying(256),
    registartion_date timestamp without time zone,
    reference_number character varying(256),
    north_boundary character varying(256),
    south_boundary character varying(256),
    east_boundary character varying(256),
    west_boundary character varying(256),
    doc_remarks character varying(256)
);




CREATE TABLE egbpaextnd_insp_checklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1) DEFAULT 0,
    inspectiondtlsid bigint
);




CREATE TABLE egbpaextnd_inspect_measuredtls (
    id bigint NOT NULL,
    inspectiondtlsid bigint,
    inspectionsourceid bigint,
    fsb bigint,
    rsb bigint,
    ssb1 bigint,
    ssb2 bigint,
    pass_width bigint,
    passage_length bigint,
    surroundedbynorth bigint,
    surroundedbysouth bigint,
    surroundedbyeast bigint,
    surroundedbywest bigint
);




CREATE TABLE egbpaextnd_inspection (
    id bigint NOT NULL,
    inspection_num character varying(64),
    inspection_date timestamp without time zone NOT NULL,
    inspectedby bigint NOT NULL,
    registrationid bigint,
    parent bigint,
    isinspected character(1) DEFAULT 0,
    ispostponeddate character(1) DEFAULT 0,
    postponementreason character varying(256),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    inspection_remarks character varying(256),
    land_zoning character varying(400),
    lnd_layout_type bigint,
    lnd_min_plotextent bigint,
    lnd_proposed_plotextent bigint,
    lnd_osr_landextent bigint,
    lnd_guidelinevalue bigint,
    building_zoning character varying(400),
    bldg_builduparea bigint,
    bldg_proposed_plotfrontage bigint,
    bldg_roadwidth bigint,
    bldg_proposed_buildingarea bigint,
    bldg_gfloor_othertypes bigint,
    bldg_firstfloor_totalarea bigint,
    bldg_compoundwall bigint,
    bldg_welloht_sumptankarea bigint,
    lnd_usage bigint,
    building_type bigint,
    bldg_commercial bigint,
    bldg_residential bigint,
    bldg_gfloor_tiledfloor bigint,
    bldg_stormwaterdrain bigint,
    lnd_regularizationarea bigint,
    lnd_penaltyperiod_halfyears bigint,
    bldg_isimprovementcharges character(1) DEFAULT 1,
    bldg_isregularisationcharges character(1) DEFAULT 1,
    land_isregularisationcharges character(1) DEFAULT 1,
    docketid bigint,
    dwellingunit bigint
);




CREATE TABLE egbpaextnd_inspection_details (
    id bigint NOT NULL,
    conststagesid bigint,
    building_extent character varying(256),
    num_of_plots bigint,
    remarks character varying(1064)
);




CREATE TABLE egbpaextnd_land_buildingtypes (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    description character varying(126),
    type character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpaextnd_lettertoparty (
    id bigint NOT NULL,
    lp_num character varying(128),
    acknowledgementnumber character varying(128),
    inspectionid bigint,
    registrationid bigint,
    letterdate timestamp without time zone NOT NULL,
    lp_reasonid bigint NOT NULL,
    sent_date timestamp without time zone,
    reply_date timestamp without time zone,
    lpremarks character varying(1024),
    lpreplyremarks character varying(1024),
    lpdesc character varying(1024),
    lpreplydesc character varying(1024),
    is_history character(1),
    documentid character varying(256),
    createdby bigint
);




CREATE TABLE egbpaextnd_lpchecklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1),
    lp_checklist_type character varying(64),
    lpid bigint,
    remarks character varying(300)
);




CREATE TABLE egbpaextnd_mstr_bldgcategory (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpaextnd_mstr_bpafee (
    id bigint NOT NULL,
    servicetypeid bigint NOT NULL,
    fundid bigint NOT NULL,
    functionid bigint NOT NULL,
    fee_type character varying(64) NOT NULL,
    fee_code character varying(3) NOT NULL,
    fee_description character varying(64) NOT NULL,
    glcodeid bigint NOT NULL,
    isfixedamount character(1) DEFAULT 0,
    isactive character(1) DEFAULT 1 NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    ismandatory smallint DEFAULT 0 NOT NULL,
    fee_description_local character varying(256),
    order_num bigint,
    isplanningpermitfee character(1) DEFAULT 1 NOT NULL,
    feegroup character varying(128)
);




CREATE TABLE egbpaextnd_mstr_bpafeedetail (
    id bigint NOT NULL,
    feeid bigint NOT NULL,
    from_areasqmt bigint,
    to_areasqmt bigint,
    amount bigint NOT NULL,
    subtype character varying(25),
    landusezone character varying(25),
    floornumber bigint,
    usagetypeid bigint,
    startdate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    enddate timestamp without time zone,
    additionaltype character varying(25)
);




CREATE TABLE egbpaextnd_mstr_buildingusage (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpaextnd_mstr_changeofusage (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpaextnd_mstr_checklist (
    id bigint NOT NULL,
    checklisttype character varying(128) NOT NULL,
    servicetypeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpaextnd_mstr_chklistdetail (
    id bigint NOT NULL,
    checklistid bigint NOT NULL,
    code character varying(10) NOT NULL,
    description character varying(256) NOT NULL,
    ismandatory character(1) DEFAULT 0,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpaextnd_mstr_const_stages (
    id bigint NOT NULL,
    const_stage character varying(256) NOT NULL,
    description character varying(64),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpaextnd_mstr_inspsource (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpaextnd_mstr_layout (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    description character varying(126),
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpaextnd_mstr_lpreason (
    id bigint NOT NULL,
    reason character varying(1024) NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpaextnd_mstr_roadwidth (
    id bigint NOT NULL,
    range character varying(126) NOT NULL,
    rate bigint DEFAULT 0,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpaextnd_mstr_servicetype (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    description character varying(256) NOT NULL,
    iscmdatype character(1) DEFAULT 0,
    isinspectionfeerequired character(1) DEFAULT 0 NOT NULL,
    isscrutinyfeerequired character(1) DEFAULT 0 NOT NULL,
    isptisnumberrequired character(1) DEFAULT 0 NOT NULL,
    isautodcrnumberrequired character(1) DEFAULT 0 NOT NULL,
    servicenumberprefix character varying(10) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL,
    description_local character varying(256),
    isdocuploadrequiredforcitizen character(1) DEFAULT 0
);




CREATE TABLE egbpaextnd_mstr_surnbldgdtls (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    isactive bigint DEFAULT 1,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egbpaextnd_mstr_surveyorname (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    regnnum character varying(64),
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpaextnd_mstr_village (
    id bigint NOT NULL,
    villagename character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    isactive character(1) DEFAULT 1 NOT NULL
);




CREATE TABLE egbpaextnd_registration (
    id bigint NOT NULL,
    ba_num character varying(128),
    ba_order_date timestamp without time zone,
    psn_num character varying(128) NOT NULL,
    psn_date timestamp without time zone NOT NULL,
    cmda_num character varying(128),
    cmda_ref_date timestamp without time zone,
    app_type character varying(128) NOT NULL,
    propertyid character varying(128),
    parent bigint,
    servicetypeid bigint NOT NULL,
    demandid bigint,
    stateid bigint,
    statusid bigint NOT NULL,
    ownerid bigint NOT NULL,
    surveyorid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    adminboundaryid bigint NOT NULL,
    locboundaryid bigint,
    documentid character varying(256),
    rejectionid bigint,
    approverid bigint,
    ppa_num character varying(256),
    issanctionfeeraised bigint DEFAULT 0,
    feeremarks character varying(1024),
    externalfeecollecteddate timestamp without time zone,
    securitykey character varying(64),
    exist_ppanum character varying(128),
    exist_banum character varying(128),
    app_mode character varying(128) DEFAULT 'General'::character varying,
    request_number character varying(50),
    servicereqregistryid bigint,
    serviceregistryid bigint
);




CREATE TABLE egbpaextnd_registrationfee (
    id bigint NOT NULL,
    feedate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    feeremarks character varying(1024),
    stateid bigint,
    statusid bigint NOT NULL,
    isrevised bigint DEFAULT 0,
    registrationid bigint NOT NULL,
    challannumber character varying(128),
    createdby bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpaextnd_regn_approvalinfo (
    id bigint NOT NULL,
    approval_type bigint NOT NULL,
    comm_approved_date timestamp without time zone NOT NULL,
    usage_from bigint NOT NULL,
    registrationid bigint NOT NULL,
    usage_to bigint NOT NULL,
    isforward_to_cmda character(1) NOT NULL,
    date_of_forward timestamp without time zone,
    createdby bigint NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE egbpaextnd_regn_autodcr (
    id bigint NOT NULL,
    lpid bigint,
    registrationid bigint NOT NULL,
    autodcr_num character varying(64) NOT NULL,
    isactive character(1) DEFAULT 0
);




CREATE TABLE egbpaextnd_regn_checklist (
    id bigint NOT NULL,
    checklistdetailid bigint,
    ischecked character(1) DEFAULT 0,
    registrationid bigint,
    checklistremarks character varying(256)
);




CREATE TABLE egbpaextnd_regn_details (
    id bigint NOT NULL,
    registrationid bigint NOT NULL,
    exist_bldgcatgid bigint,
    proposed_bldgcatgid bigint,
    sital_areasqmt bigint,
    sital_areasqft bigint,
    remarks character varying(256)
);




CREATE TABLE egbpaextnd_regnfeedetail (
    id bigint NOT NULL,
    registrationfeeid bigint NOT NULL,
    bpafeeid bigint NOT NULL,
    amount bigint DEFAULT 0
);




CREATE TABLE egbpaextnd_regnstatus_details (
    id bigint NOT NULL,
    registrationid bigint NOT NULL,
    statusdate timestamp without time zone,
    remarks character varying(1064),
    statusid bigint NOT NULL
);




CREATE TABLE egbpaextnd_rejection (
    id bigint NOT NULL,
    rejectiondate timestamp without time zone NOT NULL,
    remarks character varying(256),
    rejectionnumber character varying(32)
);




CREATE TABLE egbpaextnd_rejection_checklist (
    id bigint NOT NULL,
    rejectionid bigint,
    checklistdetailid bigint,
    ischecked character(1),
    remarks character varying(300)
);




CREATE TABLE egbpaextnd_stormwaterdrain (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    dimension character varying(126) NOT NULL,
    description character varying(126),
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egcc_conservancy_application (
    id bigint NOT NULL,
    id_status bigint,
    id_property bigint,
    application_date timestamp without time zone NOT NULL,
    application_number character varying(50) NOT NULL,
    application_type character(1),
    owner_name character varying(200),
    occupier_name character varying(200),
    conservancy_place character varying(200),
    email character varying(100),
    mobile character varying(20),
    payee_details character varying(200),
    bulk_waste_generator character varying(100),
    remarks character varying(500),
    no_of_seats bigint,
    approx_garbage double precision,
    garbage_compactor bigint,
    garbage_rotomould bigint,
    annual_charges double precision,
    app_approx_garbage double precision,
    app_garbage_compactor bigint,
    app_garbage_rotomould bigint,
    app_annual_charges double precision,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    app_no_of_seats bigint,
    assessment_number character varying(50),
    cons_notice_number character varying(50),
    state_id bigint
);




CREATE TABLE egcc_conservancy_demand (
    id_demand bigint,
    id_conservancy bigint,
    id_installment bigint
);




CREATE TABLE egcl_accountpayeedetails (
    id bigint NOT NULL,
    id_collectiondetails bigint NOT NULL,
    id_accountdetailstype bigint NOT NULL,
    id_accountdetailskey bigint NOT NULL,
    amount bigint
);




CREATE TABLE egcl_bank_remittance (
    id bigint NOT NULL,
    id_depositedinbank bigint NOT NULL,
    id_service bigint NOT NULL,
    id_bankaccounttoremit bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL
);




CREATE TABLE egcl_challanheader (
    id bigint NOT NULL,
    challannumber character varying(25) NOT NULL,
    receiptid bigint NOT NULL,
    validupto timestamp without time zone NOT NULL,
    statusid bigint,
    wfstateid bigint,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    challandate timestamp without time zone NOT NULL,
    service_id bigint,
    reasonforcancellation character varying(256),
    voucherheaderid bigint,
    old_challannumber character varying(25)
);




CREATE TABLE egcl_collection_mode_details (
    id_coll_mode bigint NOT NULL,
    mode_of_collection character varying(64) NOT NULL,
    account_number character varying(64),
    branch_name character varying(64),
    instrument_num character varying(32),
    instrument_date timestamp without time zone,
    is_cleared character(32),
    amount double precision,
    id_trans bigint,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    bank_name character varying(64),
    challan_number character varying(32),
    is_bounced character(32),
    glcode bigint,
    bank_id bigint
);




CREATE TABLE egcl_collectiondetails (
    id bigint NOT NULL,
    id_accounthead bigint NOT NULL,
    dramount double precision,
    cramount double precision,
    ordernumber bigint,
    id_collectionheader bigint,
    id_function bigint,
    actualcramounttobepaid double precision,
    description character varying(500),
    id_financialyear bigint,
    isactualdemand bigint
);




CREATE TABLE egcl_collectionheader (
    id bigint NOT NULL,
    referencenumber character varying(50),
    referencedate timestamp without time zone,
    receipttype character(1) NOT NULL,
    receiptnumber character varying(50),
    fieldcollectorname character varying(100),
    manualreceiptnumber character varying(50),
    manualreceiptdate timestamp without time zone,
    is_modifiable smallint,
    id_service bigint NOT NULL,
    id_collectionpayeedetails bigint,
    collectiontype character(1),
    ip character varying(20),
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    state_id bigint,
    referencedesc character varying(250),
    id_location bigint,
    is_reconciled bigint NOT NULL,
    id_status bigint NOT NULL,
    reasonforcancellation character varying(250),
    paidby character varying(1024),
    reference_ch_id bigint,
    overrideaccountheads smallint,
    partpaymentallowed smallint,
    displaymsg character varying(256),
    minimumamount double precision,
    totalamount double precision,
    collmodesnotallwd character varying(256),
    consumercode character varying(256),
    callbackforapportioning smallint,
    version bigint DEFAULT 1 NOT NULL
);




COMMENT ON COLUMN egcl_collectionheader.paidby IS 'To capture Payee Name at Counter';



COMMENT ON COLUMN egcl_collectionheader.reference_ch_id IS 'To  link cancelled receipt in case of interday cancellation';



COMMENT ON COLUMN egcl_collectionheader.overrideaccountheads IS 'Flag to check if override account head allowed';



COMMENT ON COLUMN egcl_collectionheader.partpaymentallowed IS 'Flag to check if part payment allowed';



CREATE TABLE egcl_collectioninstrument (
    collectionheaderid bigint NOT NULL,
    instrumentmasterid bigint NOT NULL
);




CREATE TABLE egcl_collectionmis (
    id bigint NOT NULL,
    id_fund bigint NOT NULL,
    id_fundsource bigint,
    id_boundary bigint,
    id_department bigint NOT NULL,
    id_scheme bigint,
    id_subscheme bigint,
    id_collectionheader bigint NOT NULL,
    id_functionary bigint,
    id_depositedinbank bigint
);




CREATE TABLE egcl_collectionpayeedetails (
    id bigint NOT NULL,
    payeename character varying(1024),
    payeeaddress character varying(1024)
);




CREATE TABLE egcl_collectionvoucher (
    id bigint NOT NULL,
    collectionheaderid bigint NOT NULL,
    voucherheaderid bigint,
    internalrefno character varying(50)
);




CREATE TABLE egcl_collstg_achead_amount (
    id bigint NOT NULL,
    id_collstg_rcpt bigint,
    account_description character varying(20),
    tax_amount double precision,
    penalty double precision,
    advance double precision,
    glcode character varying(20),
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    functioncode character varying(20)
);




CREATE TABLE egcl_collstg_instrument (
    id bigint NOT NULL,
    id_collstg_rcpt bigint,
    coll_mode character varying(12),
    amount double precision,
    instr_no character varying(50),
    instr_date timestamp without time zone,
    bank character varying(128),
    branch character varying(128),
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    instr_status character(1),
    instr_bounce_date timestamp without time zone,
    bank_account character varying(128)
);




CREATE TABLE egcl_collstg_receipt (
    id bigint NOT NULL,
    billing_sys_id character varying(32),
    module character varying(20),
    ward_no bigint,
    rcpt_no character varying(32),
    rcpt_date timestamp without time zone,
    rcpt_amount double precision,
    coll_type character varying(1),
    is_coll_sys_updated character varying(1),
    is_bill_sys_updated character varying(1),
    paid_by character varying(512),
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    additional_info1 character varying(32),
    additional_info2 character varying(32),
    additional_info3 character varying(32),
    additional_info4 character varying(32),
    coll_receipt_no character varying(50),
    coll_message character varying(250),
    bill_message character varying(250),
    receipt_status character(1)
);




CREATE TABLE egcl_onlinepayments (
    id bigint NOT NULL,
    receiptid bigint NOT NULL,
    serviceid bigint NOT NULL,
    transactionnumber character varying(50),
    transactionamount double precision,
    transactiondate timestamp without time zone,
    statusid bigint,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    authorisation_statuscode character varying(50),
    remarks character varying(256)
);




CREATE TABLE egcsr_mstr_service_category (
    id bigint NOT NULL,
    category_name character varying(128) NOT NULL,
    code character varying(32) NOT NULL,
    orderno bigint,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone
);




CREATE TABLE egcsr_mstr_service_type (
    id bigint NOT NULL,
    service_type character varying(128) NOT NULL,
    code character varying(32) NOT NULL,
    orderno bigint,
    id_service_cat bigint,
    full_qualified_name character varying(256) NOT NULL,
    search_url character varying(256) NOT NULL,
    is_refno_mandatory character(1) NOT NULL,
    checklist_key character varying(256) NOT NULL,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone
);




CREATE TABLE egcsr_service_request (
    id bigint NOT NULL,
    zone bigint NOT NULL,
    ward bigint NOT NULL,
    area bigint,
    location bigint,
    street bigint,
    id_service_cat bigint NOT NULL,
    id_service_type bigint NOT NULL,
    reference_no character varying(64),
    applicant_name character varying(512) NOT NULL,
    father_name character varying(512),
    applicant_address character varying(1024),
    applicant_date timestamp without time zone,
    pincode character varying(512),
    email character varying(512),
    mobileno character varying(10) NOT NULL,
    service_req_no character varying(512) NOT NULL,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    state_aware_pk_id bigint
);




CREATE TABLE egdcb_depreciationmaster (
    id_depreciationmaster bigint NOT NULL,
    year bigint NOT NULL,
    depreciation_pct double precision NOT NULL,
    id_module bigint,
    lastupdatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    id_installment bigint NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar,
    userid bigint
);




CREATE TABLE egdcb_rebatemaster (
    id_rebatemaster bigint NOT NULL,
    rebate_pct double precision NOT NULL,
    rebate_reason character varying(64) NOT NULL,
    id_module bigint NOT NULL,
    lastupdatedtimestamp timestamp without time zone,
    id_installment bigint NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar,
    userid bigint
);




CREATE TABLE egdiary_activity_type (
    id bigint NOT NULL,
    attribute_name character varying(512) NOT NULL,
    has_details character(1) NOT NULL,
    description character varying(512),
    parent bigint,
    levelno bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint NOT NULL,
    modified_by bigint
);




CREATE TABLE egdiary_attribute_type_column (
    id bigint NOT NULL,
    column_name character varying(64) NOT NULL,
    datatype character varying(10) NOT NULL,
    id_diary_activity_type bigint NOT NULL,
    order_no bigint,
    is_required character(1),
    is_active character(1),
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint NOT NULL,
    modified_by bigint
);




CREATE TABLE egdiary_attribute_values (
    id bigint NOT NULL,
    value character varying(512),
    id_diary_attr_type_column bigint,
    document_no bigint,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint NOT NULL,
    modified_by bigint
);




CREATE TABLE egdiary_details (
    id bigint NOT NULL,
    id_diary_header bigint NOT NULL,
    id_diary_attr_values bigint NOT NULL,
    id_diary_activity_type bigint NOT NULL,
    sno bigint,
    text character varying(512),
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint NOT NULL,
    modified_by bigint
);




CREATE TABLE egdiary_header (
    id bigint NOT NULL,
    id_employee bigint NOT NULL,
    date_of_diary timestamp without time zone NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint NOT NULL,
    modified_by bigint
);




CREATE TABLE egdiary_images (
    id bigint NOT NULL,
    id_diary_header bigint NOT NULL,
    id_diary_activity_type bigint NOT NULL,
    sno bigint NOT NULL,
    image bytea NOT NULL,
    latitude bigint,
    longitude bigint,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    id_diary_attribute_values bigint
);




CREATE TABLE egdm_collected_receipts (
    id bigint NOT NULL,
    id_demand_detail bigint NOT NULL,
    receipt_number character varying(50) NOT NULL,
    receipt_date timestamp without time zone NOT NULL,
    receipt_amount double precision NOT NULL,
    status character varying(1) NOT NULL,
    updated_time timestamp without time zone NOT NULL,
    reason_amount double precision DEFAULT 0 NOT NULL
);




CREATE TABLE egdms_extnl_user (
    id bigint NOT NULL,
    user_type bigint,
    user_source bigint,
    user_name character varying(100) NOT NULL,
    user_address character varying(300),
    addressed_to character varying(150),
    user_phnum character varying(50),
    user_mailid character varying(150),
    outbound_filenum character varying(150)
);




CREATE TABLE egdms_extnl_user_type (
    id bigint NOT NULL,
    type_code character varying(10) NOT NULL,
    type_name character varying(100) NOT NULL,
    type_desc character varying(200)
);




CREATE TABLE egdms_file_assignment (
    id bigint NOT NULL,
    file_id bigint NOT NULL,
    user_type character varying(100) NOT NULL,
    internal_user bigint,
    external_user bigint,
    assignment_index bigint NOT NULL,
    outbound_filenum character varying(150)
);




CREATE TABLE egdms_file_category (
    id bigint NOT NULL,
    parent_id bigint
);




CREATE TABLE egdms_file_priority (
    id bigint NOT NULL
);




CREATE TABLE egdms_file_property (
    id bigint NOT NULL,
    code character varying(100) NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(200),
    property_type character varying(100) NOT NULL
);




CREATE TABLE egdms_file_source (
    id bigint NOT NULL
);




CREATE TABLE egdms_generic_file (
    id bigint NOT NULL,
    file_number character varying(100) NOT NULL,
    file_summary character varying(500),
    file_searchtag character varying(500),
    file_heading character varying(200) NOT NULL,
    file_type character varying(50),
    doc_number character varying(100),
    state_id bigint,
    category bigint NOT NULL,
    status bigint,
    source bigint,
    priority bigint,
    dept bigint,
    zone bigint,
    ward bigint,
    sub_category bigint,
    file_rcvd_sent_dt timestamp without time zone NOT NULL,
    file_date timestamp without time zone NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL
);




CREATE TABLE egdms_inbound_file (
    id bigint NOT NULL,
    receiver bigint,
    sender bigint
);




CREATE TABLE egdms_internal_file (
    id bigint NOT NULL,
    receiver bigint,
    sender bigint
);




CREATE TABLE egdms_intnl_user (
    id bigint NOT NULL,
    desig bigint NOT NULL,
    dept bigint NOT NULL,
    "position" bigint NOT NULL,
    empinfo bigint NOT NULL
);




CREATE TABLE egdms_notification (
    id bigint NOT NULL,
    file_id bigint NOT NULL,
    position_id bigint NOT NULL
);




CREATE TABLE egdms_notification_file (
    id bigint NOT NULL,
    sender bigint NOT NULL
);




CREATE TABLE egdms_notification_group (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    description character varying(250),
    eff_date timestamp without time zone NOT NULL,
    active character(1) NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL
);




CREATE TABLE egdms_notification_user (
    position_id bigint NOT NULL,
    group_id bigint NOT NULL
);




CREATE TABLE egdms_notify_file_grp_mapping (
    file_id bigint NOT NULL,
    group_id bigint NOT NULL
);




CREATE TABLE egdms_outbound_file (
    id bigint NOT NULL,
    receiver bigint,
    sender bigint
);




CREATE SEQUENCE egeis_approval_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_att_type (
    id bigint,
    type_value character varying(256)
);




CREATE TABLE egeis_attendence (
    id bigint NOT NULL,
    att_date timestamp without time zone,
    emp_id bigint,
    month bigint,
    fin_year_id bigint,
    type_id bigint
);




CREATE TABLE egeis_bank_det (
    id_bank bigint NOT NULL,
    id bigint NOT NULL,
    salary_bank smallint,
    bank bigint,
    account_number character varying(256),
    branch bigint
);




CREATE SEQUENCE egeis_blood_seq
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_bloodgroup (
    id bigint NOT NULL,
    value character varying(10),
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_category_mstr (
    category_id bigint NOT NULL,
    category_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_cert_details (
    id bigint NOT NULL,
    id_emp bigint NOT NULL,
    id_nominee bigint,
    id_cert_type bigint NOT NULL,
    present_date timestamp without time zone,
    status bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egeis_cert_details_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_community_mstr (
    community_id bigint NOT NULL,
    community_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_compoff (
    id bigint NOT NULL,
    approved_by character varying(256),
    status_id bigint,
    compoff_date timestamp without time zone,
    created_by bigint,
    att_id bigint,
    createddate timestamp without time zone,
    state_id bigint
);




CREATE TABLE egeis_dept_tests (
    dept_tests_id bigint NOT NULL,
    name_of_test_id bigint,
    date_of_pass timestamp without time zone,
    id bigint
);




CREATE TABLE egeis_deptdesig (
    id bigint NOT NULL,
    desig_id integer NOT NULL,
    dept_id integer NOT NULL,
    outsourced_posts integer,
    sanctioned_posts integer
);




CREATE SEQUENCE egeis_dis_approval_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_edu_details (
    education_details_id bigint NOT NULL,
    id bigint NOT NULL,
    edu_qul character varying(256),
    major_subject character varying(256),
    month_year_pass timestamp without time zone,
    university_board character varying(256)
);




CREATE TABLE egeis_elig_cert_type (
    id bigint NOT NULL,
    type character varying(64),
    description character varying(64)
);




CREATE SEQUENCE egeis_elig_cert_type_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_employee_grade (
    id bigint NOT NULL,
    emp_id bigint,
    grade_id bigint,
    retirement_date timestamp without time zone
);




CREATE TABLE egeis_grade_mstr (
    grade_id bigint NOT NULL,
    grade_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone,
    age bigint
);




CREATE TABLE egeis_holidays (
    holiday_date timestamp without time zone,
    fin_year_id bigint,
    id bigint NOT NULL,
    holiday_name character varying(256),
    month bigint,
    cal_year_id bigint
);




CREATE TABLE egeis_how_acquired_mstr (
    how_acquired_id bigint NOT NULL,
    how_acquired_value character varying(256) NOT NULL,
    end_date timestamp without time zone,
    start_date timestamp without time zone
);




CREATE TABLE egeis_immovable_prop_details (
    imm_property_details_id bigint NOT NULL,
    property_description character varying(256),
    place character varying(256),
    how_acquired_id bigint,
    present_value bigint,
    permission_obtained character(1),
    order_no character varying(256),
    order_date timestamp without time zone,
    id bigint
);




CREATE TABLE egeis_lang_known (
    id_lang bigint NOT NULL,
    id bigint,
    languages_known_id bigint
);




CREATE TABLE egeis_languages_known_mstr (
    languages_known_id bigint NOT NULL,
    languages_known_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_leave_application (
    id bigint NOT NULL,
    from_date timestamp without time zone,
    to_date timestamp without time zone,
    workingdays bigint,
    reason character varying(256),
    emp_id bigint,
    leave_type_id bigint,
    desig_id bigint,
    status smallint,
    application_number character varying(256),
    leaves_available integer,
    created_by bigint,
    fin_year smallint,
    sanction_no character varying(512),
    two_halfleaves_in_day character(1),
    is_leave_encashment smallint,
    cal_year smallint,
    createddate timestamp without time zone,
    state_id bigint
);




CREATE TABLE egeis_leave_approval (
    id bigint NOT NULL,
    pay_eligible character(1),
    approved_by bigint,
    application_id bigint
);




CREATE TABLE egeis_leave_mstr (
    id bigint NOT NULL,
    desig_id bigint,
    leave_type_id bigint,
    no_of_days bigint
);




CREATE SEQUENCE egeis_leave_mstr_seq
    START WITH 10
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_leave_opening_balance (
    id bigint NOT NULL,
    emp_id bigint,
    leave_type bigint,
    leaves_available bigint,
    financialyear bigint,
    calendaryear bigint
);




CREATE TABLE egeis_leave_status (
    status character varying(256),
    id smallint NOT NULL
);




CREATE TABLE egeis_local_lang_qul_mstr (
    qulified_id bigint NOT NULL,
    qulified_name character varying(256) NOT NULL,
    end_date timestamp without time zone,
    start_date timestamp without time zone
);




CREATE TABLE egeis_ltc_pirticulars (
    ltc_details_id bigint NOT NULL,
    block_year character varying(256),
    claimed_way_fare character varying(256),
    order_no character varying(256),
    order_date timestamp without time zone,
    id bigint,
    leave_type character varying(256)
);




CREATE TABLE egeis_mode_of_recruiment_mstr (
    mode_of_recruiment_id bigint NOT NULL,
    mode_of_recruiment_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_movable_prop_details (
    mov_property_details_id bigint NOT NULL,
    property_discription character varying(256),
    val_at_purchase bigint,
    how_acquired_id bigint,
    permission_obtained character(1),
    order_no character varying(256),
    order_date timestamp without time zone,
    id bigint
);




CREATE TABLE egeis_name_of_test_mstr (
    name_of_test_id bigint NOT NULL,
    name_of_test_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE SEQUENCE egeis_nom_cert_req_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_nomimation_pirticulars (
    nom_details_id bigint NOT NULL,
    name_of_nominee character varying(256),
    age bigint,
    marital_status character varying(256),
    relation character varying(256),
    gpf bigint,
    spfgs bigint,
    fbf bigint,
    dcrg bigint,
    pension bigint,
    id bigint
);




CREATE SEQUENCE egeis_nominee_details_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_nominee_master (
    id bigint NOT NULL,
    emp_id bigint,
    name character varying(256),
    age bigint,
    id_relation_type bigint NOT NULL,
    dob timestamp without time zone,
    marital_status bigint,
    isactive bigint,
    isworking bigint,
    branch_id bigint,
    account_number character varying(256),
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    guardian_name character varying(256),
    guardian_relationship character varying(128),
    nominee_address character varying(512),
    disbursement_type bigint
);




CREATE SEQUENCE egeis_nominee_type_seq
    START WITH 7
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_pay_fixed_in_mstr (
    pay_fixed_in_id bigint NOT NULL,
    pay_fixed_in_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE SEQUENCE egeis_pension_header_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_person_address (
    id_person_address bigint NOT NULL,
    id bigint NOT NULL,
    id_address bigint NOT NULL
);




CREATE TABLE egeis_posting_type_mstr (
    posting_type_id bigint NOT NULL,
    posting_type_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE SEQUENCE egeis_posting_type_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egeis_probation (
    id_probation bigint NOT NULL,
    id bigint NOT NULL,
    probation_declared_date timestamp without time zone,
    order_no character varying(256),
    order_date timestamp without time zone,
    id_post bigint
);




CREATE TABLE egeis_recruitment_type_mstr (
    recruitment_type_id bigint NOT NULL,
    recruitment_type_name character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_regularisation (
    regularisation_id bigint NOT NULL,
    regularisation_date timestamp without time zone,
    order_no character varying(256),
    order_date timestamp without time zone,
    id bigint NOT NULL,
    id_post bigint
);




CREATE TABLE egeis_rel_nom_cert_reqd (
    id bigint NOT NULL,
    id_relation_type bigint NOT NULL,
    id_cert_type bigint NOT NULL,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL
);




CREATE TABLE egeis_relation_type (
    id bigint NOT NULL,
    relation_type character varying(64),
    full_benefit_eligible smallint,
    gender character varying(1),
    eligible_age bigint,
    elig_status_if_married character varying(1),
    elig_status_if_employed character varying(1),
    narration character varying(256)
);




CREATE TABLE egeis_religion_mstr (
    religion_id bigint NOT NULL,
    religion_value character varying(256) NOT NULL,
    start_date timestamp without time zone,
    end_date timestamp without time zone
);




CREATE TABLE egeis_service_history (
    id bigint NOT NULL,
    emp_id bigint,
    comment_date timestamp without time zone,
    comments character varying(256),
    reason character varying(256),
    order_no character varying(256),
    doc_no bigint
);




CREATE TABLE egeis_skill_mstr (
    skill_id bigint NOT NULL,
    skill_value character varying(256),
    end_date timestamp without time zone,
    start_date timestamp without time zone
);




CREATE TABLE egeis_technical_grades_mstr (
    teck_grades_id bigint NOT NULL,
    teck_grades_value character varying(256) NOT NULL,
    skill_id bigint,
    end_date timestamp without time zone,
    start_date timestamp without time zone
);




CREATE TABLE egeis_tecnical_qualification (
    tecnical_qulification_id bigint NOT NULL,
    passed_date timestamp without time zone,
    id bigint NOT NULL,
    teck_grades_id bigint,
    skillid bigint,
    remarks character varying(256)
);




CREATE TABLE egeis_training_pirticulars (
    training_details_id bigint NOT NULL,
    course character varying(256),
    institution character varying(256),
    city character varying(256),
    pot_from timestamp without time zone,
    pot_to timestamp without time zone,
    id bigint
);




CREATE TABLE egeis_type_master (
    status_id bigint NOT NULL,
    name character varying(256),
    end_date timestamp without time zone,
    start_date timestamp without time zone,
    id_chartofaccount bigint
);




CREATE TABLE egeis_type_of_leave_mstr (
    type_of_leave_id bigint NOT NULL,
    type_of_leave_value character varying(256) NOT NULL,
    accumulate character(1),
    pay_eligible character(1),
    is_half_day character(1),
    is_encashable character(1) DEFAULT 0
);




CREATE TABLE egeis_working_constants (
    id bigint NOT NULL,
    value character varying(256),
    isactive character(1)
);




CREATE TABLE egf_account_cheques (
    id bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    fromchequenumber character varying(50) NOT NULL,
    tochequenumber character varying(50) NOT NULL,
    receiveddate timestamp without time zone NOT NULL,
    isexhausted bigint,
    nextchqno character varying(50),
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    serialno character varying(16)
);




CREATE TABLE egf_accountcode_purpose (
    id bigint NOT NULL,
    name character varying(250),
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint
);




CREATE TABLE egf_asset_history (
    id bigint,
    assetid bigint,
    transactiontype character varying(1),
    prevstatusid bigint,
    newstatusid bigint,
    statuschangedate timestamp without time zone,
    amount real,
    approvedby bigint,
    reasonforchange character varying(1),
    isincreased bigint
);




CREATE TABLE egf_brs_bankstatements (
    id bigint NOT NULL,
    accountnumber character varying(20),
    accountid bigint,
    txdate timestamp without time zone,
    type character varying(64),
    instrumentno character varying(8),
    debit double precision,
    credit double precision,
    balance double precision,
    narration character varying(126),
    cslno character varying(32),
    createddate timestamp without time zone,
    action character varying(32),
    reconciliationdate timestamp without time zone,
    errorcode character varying(8),
    errormessage character varying(256)
);




CREATE TABLE egf_budget (
    id bigint NOT NULL,
    name character varying(50),
    description character varying(250),
    financialyearid bigint NOT NULL,
    state_id bigint,
    parent bigint,
    isactivebudget bigint,
    updatedtimestamp timestamp without time zone NOT NULL,
    isprimarybudget bigint NOT NULL,
    createdby bigint,
    lastmodifiedby bigint,
    isbere character varying(20),
    as_on_date timestamp without time zone,
    materializedpath character varying(10),
    reference_budget bigint,
    document_number bigint
);




CREATE TABLE egf_budget_reappropriation (
    id bigint NOT NULL,
    budgetdetail bigint NOT NULL,
    anticipatory_amount bigint,
    addition_amount bigint,
    deduction_amount bigint,
    state_id bigint,
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint,
    reappropriation_misc bigint,
    original_addition_amount bigint,
    original_deduction_amount bigint,
    status bigint
);




CREATE TABLE egf_budget_usage (
    id bigint NOT NULL,
    financialyearid bigint NOT NULL,
    moduleid bigint,
    referencenumber character varying(50) NOT NULL,
    createdby bigint,
    consumedamt double precision,
    releasedamt double precision,
    updatedtime timestamp without time zone NOT NULL,
    budgetdetailid bigint NOT NULL,
    appropriationnumber character varying(32)
);




CREATE TABLE egf_budgetdetail (
    id bigint NOT NULL,
    using_department bigint,
    executing_department bigint,
    function bigint,
    budget bigint NOT NULL,
    budgetgroup bigint NOT NULL,
    originalamount double precision,
    approvedamount double precision,
    anticipatory_amount double precision,
    budgetavailable double precision,
    scheme bigint,
    subscheme bigint,
    functionary bigint,
    boundary bigint,
    modifieddate timestamp without time zone,
    modifiedby bigint,
    createddate timestamp without time zone,
    createdby bigint,
    state_id bigint,
    fund bigint,
    materializedpath character varying(10),
    document_number bigint,
    uniqueno character varying(32),
    planningpercent real
);




CREATE TABLE egf_budgetgroup (
    id bigint NOT NULL,
    majorcode bigint,
    maxcode bigint,
    mincode bigint,
    name character varying(250),
    description character varying(250),
    budgetingtype character varying(250),
    accounttype character varying(250),
    isactive smallint,
    updatedtimestamp timestamp without time zone NOT NULL
);




CREATE TABLE egf_cbill (
    id bigint
);




CREATE TABLE egf_cess_master (
    id bigint NOT NULL,
    taxcodeid bigint,
    glcodeid bigint,
    percentage double precision,
    financialyearid character varying(50),
    isactive smallint
);




CREATE TABLE egf_deprformula (
    id bigint NOT NULL,
    name character varying(64)
);




CREATE TABLE egf_deptissue (
    id bigint NOT NULL,
    mrinheaderid bigint NOT NULL,
    deptid bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL
);




CREATE TABLE egf_dishonorcheque (
    id bigint NOT NULL,
    instrumentheaderid bigint NOT NULL,
    originalvhid bigint NOT NULL,
    statusid bigint NOT NULL,
    bankcharges bigint,
    transactiondate timestamp without time zone NOT NULL,
    bankchargeglcodeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    bankreferencenumber character varying(20),
    reversalvhid bigint,
    bankchargesvhid bigint,
    stateid bigint NOT NULL,
    bankreason character varying(50),
    instrumentdishonorreason character varying(50)
);




CREATE TABLE egf_dishonorcheque_detail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    glcodeid bigint NOT NULL,
    debitamt bigint,
    creditamt bigint,
    detailkey bigint,
    detailtype bigint,
    functionid bigint
);




CREATE TABLE egf_ebconsumer (
    id bigint NOT NULL,
    code character varying(64) NOT NULL,
    name character varying(96) NOT NULL,
    region character varying(64) NOT NULL,
    oddorevenbilling character varying(32) NOT NULL,
    wardid bigint,
    location character varying(64),
    address character varying(256),
    isactive bigint DEFAULT 0,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE TABLE egf_ebdetails (
    id bigint NOT NULL,
    consumerno bigint NOT NULL,
    billno character varying(32),
    billamount double precision NOT NULL,
    month bigint NOT NULL,
    prevbillamount double precision,
    comments character varying(1024),
    billid bigint,
    billdate timestamp without time zone,
    status bigint NOT NULL,
    receiptno character varying(32),
    recieptdate timestamp without time zone,
    duedate timestamp without time zone,
    stateid bigint,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    wardid bigint,
    target_area_id bigint,
    position_id bigint,
    region character varying(64),
    financialyearid bigint,
    variance bigint
);




CREATE TABLE egf_ebschedulerlog (
    id bigint NOT NULL,
    starttime timestamp without time zone,
    endtime timestamp without time zone,
    noofpendingbills bigint,
    noofbillsprocessed bigint,
    noofbillscreated bigint,
    schedulerstatus character varying(16),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    oddorevenbilling character varying(16),
    noofbillsfailed bigint
);




CREATE TABLE egf_ebschedulerlogdetails (
    id bigint NOT NULL,
    schedulerlogid bigint,
    consumer bigint,
    consumerno character varying(16),
    duedate character varying(16),
    amount character varying(16),
    message character varying(128),
    status character varying(16)
);




CREATE TABLE egf_ecstype (
    id bigint NOT NULL,
    type character varying(30) NOT NULL,
    isactive bigint NOT NULL
);




CREATE TABLE egf_fixeddeposit (
    id bigint NOT NULL,
    fileno character varying(48),
    amount double precision,
    depositdate timestamp without time zone NOT NULL,
    bankbranchid bigint NOT NULL,
    bankaccountid bigint NOT NULL,
    interestrate double precision NOT NULL,
    period character varying(64),
    serialnumber character varying(32) NOT NULL,
    outflowgjvid bigint,
    gjvamount double precision,
    maturityamount double precision,
    maturitydate timestamp without time zone,
    withdrawaldate timestamp without time zone,
    inflowgjvid bigint,
    challanreceiptvhid bigint,
    instrumentheaderid bigint,
    receiptamount double precision,
    remarks character varying(512),
    parentid bigint,
    extend smallint DEFAULT 0,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egf_function_generalledger (
    id bigint NOT NULL,
    generalledgerid bigint NOT NULL,
    functionid bigint,
    lastmodifieddate timestamp without time zone NOT NULL,
    amount double precision
);




CREATE TABLE egf_fundflow (
    id bigint NOT NULL,
    reportdate timestamp without time zone NOT NULL,
    bankaccountid bigint,
    openingbalance double precision,
    currentreceipt double precision,
    createdby bigint,
    lastmodifiedby bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egf_fundingagency (
    id bigint NOT NULL,
    code character varying(16) NOT NULL,
    address character varying(256),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    remarks character varying(200),
    name character varying(100),
    isactive smallint
);




CREATE TABLE egf_grant (
    id bigint NOT NULL,
    deptid bigint,
    financialyearid bigint NOT NULL,
    period character varying(10) NOT NULL,
    proceedingsno character varying(48) NOT NULL,
    proceedingsdate timestamp without time zone NOT NULL,
    accrualvoucherid bigint NOT NULL,
    accrualamount double precision,
    grantvoucherid bigint,
    receiptvoucherid bigint,
    grantamount double precision,
    instrumentheaderid bigint,
    remarks character varying(512),
    granttype character varying(48),
    commtaxofficer character varying(48),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egf_instrument_dishonor_reason (
    reason character varying(96) NOT NULL
);




CREATE TABLE egf_instrumentaccountcodes (
    id bigint NOT NULL,
    typeid bigint,
    glcodeid bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egf_instrumentheader (
    id bigint NOT NULL,
    instrumentnumber character varying(20),
    instrumentdate timestamp without time zone,
    instrumentamount double precision NOT NULL,
    id_status bigint NOT NULL,
    bankaccountid bigint,
    payto character varying(250),
    ispaycheque character(1),
    instrumenttype character varying(20),
    bankid bigint,
    detailkeyid bigint,
    detailtypeid bigint,
    transactionnumber character varying(50),
    transactiondate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    payee character varying(50),
    bankbranchname character varying(50),
    surrendarreason character varying(100),
    serialno character varying(16),
    ecstype bigint
);




CREATE TABLE egf_instrumentotherdetails (
    id bigint,
    instrumentheaderid bigint,
    payinslipid bigint,
    instrumentstatusdate timestamp without time zone,
    reconciledamount double precision,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    reconciledon timestamp without time zone,
    dishonorbankrefno character varying(20)
);




CREATE TABLE egf_instrumenttype (
    id bigint NOT NULL,
    type character varying(50),
    isactive character varying(1),
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egf_instrumentvoucher (
    id bigint,
    instrumentheaderid bigint,
    voucherheaderid bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egf_interbintransfer (
    id bigint NOT NULL,
    storeid bigint NOT NULL,
    frombinid bigint NOT NULL,
    tobinid bigint NOT NULL,
    qty bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    status bigint NOT NULL,
    itemid bigint NOT NULL,
    uomid bigint NOT NULL,
    createdby bigint NOT NULL
);




CREATE TABLE egf_interbintransferdetail (
    id bigint NOT NULL,
    interbintransferid bigint NOT NULL,
    mrinheaderid bigint,
    mrnheaderid bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egf_interstoretransfer (
    id bigint NOT NULL,
    mrinlineid bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    mrnlineid bigint NOT NULL
);




CREATE TABLE egf_issuedfrommrn (
    id bigint NOT NULL,
    mrinlineid bigint NOT NULL,
    mrnheaderid bigint NOT NULL,
    quantity bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    status bigint,
    mrnlineid bigint,
    mrnlineotherid bigint
);




CREATE TABLE egf_issuerepair (
    id bigint NOT NULL,
    mrinheaderid bigint NOT NULL,
    assetid bigint,
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egf_loan_paid (
    schemeid bigint,
    agencyid bigint,
    amount double precision
);




CREATE TABLE egf_loangrantdetail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    agencyid bigint NOT NULL,
    loanamount double precision,
    grantamount double precision,
    percentage real,
    agencyschemeno character varying(48),
    councilresno character varying(48),
    loansanctionno character varying(48),
    agreementdate timestamp without time zone,
    commorderno character varying(48),
    docid bigint,
    type character varying(16) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egf_loangrantheader (
    id bigint NOT NULL,
    subschemeid bigint,
    councilresno character varying(48),
    govtorderno character varying(48),
    amendmentno character varying(48),
    projectcost double precision,
    sanctionedcost double precision,
    revisedcost double precision,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    amendmentdate timestamp without time zone,
    councilresdate timestamp without time zone,
    govtorderdate timestamp without time zone
);




CREATE TABLE egf_loangrantreceiptdetail (
    id bigint NOT NULL,
    headerid bigint NOT NULL,
    agencyid bigint,
    amount double precision,
    description character varying(1024),
    voucherheaderid bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    instrumentheaderid bigint,
    bankaccountid bigint
);




CREATE TABLE egf_mrheader (
    id bigint NOT NULL,
    mrno character varying(100) NOT NULL,
    mrdate timestamp without time zone NOT NULL,
    mrtype character(1) NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    requestedby character varying(100) NOT NULL,
    statusid bigint NOT NULL,
    narration character varying(250),
    approvedby bigint,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    mrname character varying(50),
    fromstoreid bigint,
    tostoreid bigint,
    fieldid bigint,
    functionid bigint,
    functionaryid bigint,
    departmentid bigint,
    assetid bigint,
    accountcode bigint,
    projectcodeid bigint,
    purpose bigint,
    drawnon character varying(100),
    gatepassno character varying(25),
    state_id bigint,
    sanctioned bigint DEFAULT 0
);




CREATE TABLE egf_mrinheader (
    id bigint NOT NULL,
    mrinno character varying(100) NOT NULL,
    deptid bigint,
    tostoreid bigint NOT NULL,
    purpose bigint NOT NULL,
    mrindate timestamp without time zone NOT NULL,
    created timestamp without time zone NOT NULL,
    createdby bigint DEFAULT 0::bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    modifiedby bigint,
    statusid bigint NOT NULL,
    fromstoreid bigint,
    fundid bigint NOT NULL,
    fundsourceid bigint,
    fieldid bigint,
    functionid bigint,
    functionaryid bigint,
    accountcode bigint,
    mrheaderid bigint,
    drawnon character varying(100),
    gatepassno character varying(25),
    state_id bigint
);




CREATE TABLE egf_mrinline (
    id bigint NOT NULL,
    mrinheaderid bigint NOT NULL,
    lineno bigint NOT NULL,
    itemid bigint NOT NULL,
    qty bigint NOT NULL,
    uomid bigint NOT NULL,
    value bigint NOT NULL,
    remarks character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    voucherheaderid bigint,
    linkedassetid bigint,
    relationid bigint,
    mrlineid bigint,
    mileage bigint
);




CREATE TABLE egf_mrinpurpose (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    isindenttype smallint DEFAULT 0
);




CREATE TABLE egf_mrline (
    id bigint NOT NULL,
    srlno bigint NOT NULL,
    mrheaderid bigint NOT NULL,
    itemid bigint,
    mruom bigint NOT NULL,
    contractid bigint,
    mrqty bigint NOT NULL,
    needby timestamp without time zone,
    relationid bigint,
    deliveryterms character varying(250),
    notetoreceiver character varying(250),
    processedqty bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    techspecification character varying(100),
    assetcategoryid bigint,
    sanctionedqty bigint DEFAULT 0
);




CREATE TABLE egf_mrnheader (
    id bigint NOT NULL,
    bookno character varying(32),
    mrnno character varying(100) NOT NULL,
    challno character varying(30),
    fromstoreid bigint,
    recvstoreid bigint NOT NULL,
    relationid bigint,
    deptid bigint,
    purpose character varying(30),
    receiveddate timestamp without time zone NOT NULL,
    inspectedby character varying(30),
    created timestamp without time zone NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    modifiedby bigint,
    createdby bigint DEFAULT 0::bigint NOT NULL,
    status character varying(30) NOT NULL,
    billreceived bigint,
    fundid bigint,
    fundsourceid bigint,
    schemeid bigint,
    subschemeid bigint,
    fieldid bigint,
    functionid bigint,
    billid bigint,
    functionaryid bigint,
    accountcode bigint,
    deliverychallan character varying(20),
    state_id bigint,
    projectcodeid bigint,
    purchasetype bigint
);




CREATE TABLE egf_mrnline (
    id bigint NOT NULL,
    mrnheaderid bigint NOT NULL,
    lineno bigint NOT NULL,
    itemid bigint,
    polineid bigint,
    uomid bigint NOT NULL,
    billid bigint,
    received_qty bigint NOT NULL,
    unitprice bigint NOT NULL,
    balance bigint,
    remarks character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    voucherheaderid bigint,
    accepted_qty bigint,
    othercharges bigint,
    assetid bigint
);




CREATE TABLE egf_mrnlineothers (
    id bigint NOT NULL,
    mrnlineid bigint NOT NULL,
    lineno bigint NOT NULL,
    breakqty bigint NOT NULL,
    lotno character varying(30),
    expirydate timestamp without time zone,
    lastmodifieddate timestamp without time zone NOT NULL,
    balance bigint,
    binid bigint
);




CREATE TABLE egf_openingbalance_jobdetail (
    id bigint NOT NULL,
    financialyearid bigint NOT NULL,
    storeid bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    status bigint NOT NULL
);




CREATE TABLE egf_poline (
    id bigint NOT NULL,
    worksdetailid bigint NOT NULL,
    lineno bigint NOT NULL,
    itemid bigint NOT NULL,
    qty bigint NOT NULL,
    uomid bigint NOT NULL,
    price bigint NOT NULL,
    received_qty bigint,
    remarks character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    termsofdelivery character varying(300),
    mrheaderid bigint,
    mrlineid bigint,
    contractid bigint,
    techinfo character varying(300)
);




CREATE TABLE egf_reappropriation_misc (
    id bigint NOT NULL,
    sequence_number character varying(1024),
    reappropriation_date timestamp without time zone,
    remarks character varying(1024),
    state_id bigint,
    modifiedby bigint,
    modifieddate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone
);




CREATE TABLE egf_receipt_mis (
    id_trans_header bigint,
    fieldid bigint,
    fundid bigint,
    segmentid bigint,
    sub_segmentid bigint,
    functionid bigint,
    bill_no character varying(30),
    narration character varying(2000),
    id bigint,
    address character varying(200),
    receiptno character varying(25),
    voucherheaderid bigint
);




CREATE TABLE egf_receipt_voucher (
    id bigint NOT NULL,
    voucherheaderid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint
);




CREATE TABLE egf_record_status (
    id bigint NOT NULL,
    record_type character varying(50) NOT NULL,
    status bigint NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    userid bigint NOT NULL,
    voucherheaderid bigint NOT NULL
);




CREATE TABLE egf_recovery_bankdetails (
    id bigint NOT NULL,
    tds_id bigint NOT NULL,
    fund_id bigint NOT NULL,
    bank_id bigint NOT NULL,
    branch_id bigint NOT NULL,
    bankaccount_id bigint NOT NULL
);




CREATE TABLE egf_remittance_schd_payment (
    id bigint NOT NULL,
    schd_id bigint NOT NULL,
    vhid bigint NOT NULL
);




CREATE TABLE egf_remittance_scheduler (
    id bigint NOT NULL,
    sch_job_name character varying(200) NOT NULL,
    lastrundate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    sch_type character(1) DEFAULT 'S'::bpchar NOT NULL,
    remarks character varying(4000),
    status character varying(50) NOT NULL,
    glcode character varying(16)
);




CREATE TABLE egf_rom (
    id bigint NOT NULL,
    relationid bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    mrinlineid bigint,
    mrnlineid bigint,
    mrinheaderid bigint NOT NULL
);




CREATE TABLE egf_rpreport_schedulemapping (
    id bigint NOT NULL,
    rpscheduleid bigint,
    glcode character varying(16),
    fund_code character varying(16),
    is_consolidated smallint,
    subschedule_id bigint
);




CREATE TABLE egf_rpreport_schedulemaster (
    id bigint NOT NULL,
    transaction_type character(1),
    schedule_name character varying(256),
    schedule_no character varying(16),
    is_subschedule smallint
);




CREATE TABLE egf_sale (
    id bigint NOT NULL,
    mrinheaderid bigint NOT NULL,
    buyer character varying(30),
    lastmodified timestamp without time zone NOT NULL,
    saleprice bigint NOT NULL,
    mrinlineid bigint NOT NULL
);




CREATE TABLE egf_scheme_bankaccount (
    id bigint NOT NULL,
    schemeid bigint,
    subschemeid bigint,
    bankaccountid bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egf_storesopeningbalance (
    id bigint NOT NULL,
    financialyearid bigint NOT NULL,
    storeid bigint NOT NULL,
    binid bigint,
    itemid bigint NOT NULL,
    uomid bigint NOT NULL,
    qty bigint NOT NULL,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egf_subschedule (
    id bigint,
    reporttype character varying(5),
    subschedulename character varying(50),
    subschname character varying(10)
);




CREATE TABLE egf_subscheme_project (
    id bigint NOT NULL,
    subschemeid bigint NOT NULL,
    projectcodeid bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egf_supplierbillpoadjustment (
    id bigint NOT NULL,
    billid bigint,
    worksdetailid bigint,
    adjustmentamount double precision
);




CREATE TABLE egf_target_area (
    id bigint NOT NULL,
    name character varying(32) NOT NULL,
    code character varying(32) NOT NULL,
    isactive bigint DEFAULT 0,
    positionid bigint,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE TABLE egf_tax_account_mapping (
    id bigint NOT NULL,
    taxcodeid bigint,
    glcodeid bigint,
    isactive smallint,
    financialyear character varying(50) NOT NULL,
    financialyearid bigint,
    isold bigint
);




CREATE TABLE egf_tax_code (
    id bigint NOT NULL,
    code character varying(25),
    name character varying(150),
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone,
    modifiedby bigint,
    accrued smallint
);




CREATE TABLE egf_wardtargetarea_mapping (
    id bigint NOT NULL,
    boundaryid bigint,
    targetareaid bigint NOT NULL
);




CREATE TABLE egfms_accident_details (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    location character varying(500) NOT NULL,
    driverid bigint NOT NULL,
    damage_nature character varying(1000) NOT NULL,
    operated_deptid bigint NOT NULL,
    accident_date timestamp without time zone NOT NULL,
    remarks character varying(1000),
    attached_rto character varying(300) NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    accident_index bigint NOT NULL
);




CREATE SEQUENCE egfms_accident_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_fitness_details (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    fitness_due timestamp without time zone NOT NULL,
    fiteness_passed timestamp without time zone,
    attached_rto character varying(300),
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    fitness_index bigint NOT NULL
);




CREATE SEQUENCE egfms_fitness_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_greentax_details (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    tax_amt double precision NOT NULL,
    per_tax double precision NOT NULL,
    total_tax double precision NOT NULL,
    rto_challan_no character varying(20),
    valid_from timestamp without time zone NOT NULL,
    valid_to timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    greentax_index bigint NOT NULL
);




CREATE SEQUENCE egfms_greentax_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_marchout_details (
    id bigint NOT NULL,
    marchout_headerid bigint NOT NULL,
    departmentid bigint NOT NULL,
    complement bigint NOT NULL,
    marchout bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    marchout_details_index bigint NOT NULL,
    veh_category bigint NOT NULL
);




CREATE SEQUENCE egfms_marchout_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_marchout_header (
    id bigint NOT NULL,
    marchout_date timestamp without time zone NOT NULL,
    total_complement bigint NOT NULL,
    total_marchout bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint
);




CREATE SEQUENCE egfms_marchout_header_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE egfms_marchout_othdetails_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_marchout_otherdetails (
    id bigint NOT NULL,
    marchout_headerid bigint NOT NULL,
    vehicleid bigint NOT NULL,
    reason character varying(1000) NOT NULL,
    possible_marchout timestamp without time zone,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    marchout_otherdetails_index bigint NOT NULL
);




CREATE TABLE egfms_marchout_vehicles (
    id bigint NOT NULL,
    marchout_detailid bigint NOT NULL,
    vehicleid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    marchout_vehicles_index bigint NOT NULL
);




CREATE SEQUENCE egfms_marchout_vehicles_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_motortax_details (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    payment_frequency character varying(20) NOT NULL,
    tax_amt double precision NOT NULL,
    per_mwgwf double precision NOT NULL,
    service_charge double precision NOT NULL,
    rto_challan_no character varying(20),
    valid_from timestamp without time zone NOT NULL,
    valid_to timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    motortax_index bigint NOT NULL,
    total_tax double precision NOT NULL
);




CREATE SEQUENCE egfms_motortax_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_vehicle_assignment (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    departmentid bigint NOT NULL,
    storeid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone NOT NULL,
    storekeeperid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    assignment_index bigint NOT NULL
);




CREATE SEQUENCE egfms_vehicle_assignment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_vehicle_category (
    id bigint NOT NULL,
    description character varying(100) NOT NULL,
    asset_categoryid bigint NOT NULL
);




CREATE SEQUENCE egfms_vehicle_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_vehicle_header (
    id bigint NOT NULL,
    assetid bigint NOT NULL,
    veh_number character varying(100) NOT NULL,
    manuf_name character varying(200) NOT NULL,
    veh_type character varying(10) NOT NULL,
    body_fab_comp character varying(200),
    engine_no character varying(200) NOT NULL,
    veh_class character varying(50),
    wheel_base double precision,
    gross_veh_wt double precision,
    purchase_date timestamp without time zone NOT NULL,
    veh_cost double precision NOT NULL,
    warranty_period double precision NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone,
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    fuel_type character varying(30) NOT NULL,
    chassis_number character varying(200) NOT NULL,
    unladen_wt double precision,
    veh_category bigint NOT NULL,
    latest_insurance_valid_to timestamp without time zone,
    latest_fitness_due_date timestamp without time zone,
    latest_motor_valid_to timestamp without time zone,
    latest_green_valid_to timestamp without time zone,
    latest_next_service_due_date timestamp without time zone
);




CREATE SEQUENCE egfms_vehicle_header_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_vehicle_insurance (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    company character varying(200) NOT NULL,
    premium_amt double precision NOT NULL,
    valid_from timestamp without time zone NOT NULL,
    valid_to timestamp without time zone NOT NULL,
    cheque_number character varying(6),
    cheque_date timestamp without time zone,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    insurance_index bigint NOT NULL
);




CREATE SEQUENCE egfms_vehicle_insurance_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egfms_vehicle_service (
    id bigint NOT NULL,
    vehicleid bigint NOT NULL,
    servicedate timestamp without time zone NOT NULL,
    remarks character varying(1000) NOT NULL,
    nextservicedate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    service_index bigint NOT NULL
);




CREATE SEQUENCE egfms_vehicle_service_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE eggis_bndry_dim (
    dimid bigint NOT NULL,
    bndryid bigint NOT NULL,
    width bigint NOT NULL,
    height bigint NOT NULL
);




CREATE TABLE eggr_communication (
    id bigint NOT NULL,
    message character varying(1024) NOT NULL,
    commdate timestamp without time zone NOT NULL,
    complaintid bigint NOT NULL,
    touserid bigint,
    fromuserid bigint NOT NULL,
    communicationtype bigint NOT NULL,
    message_local character varying(1024),
    modifiedby bigint
);




CREATE TABLE eggr_comp_receiving_modes (
    comp_id bigint NOT NULL,
    comp_mode character varying(64) NOT NULL
);




CREATE TABLE eggr_complaintdetails (
    complaintid bigint NOT NULL,
    complaintnumber character varying(64),
    complaintdate timestamp without time zone,
    complainantfirstname character varying(32),
    complainaninitials character varying(32),
    complainantlastname character varying(32),
    complainantaddress character varying(256),
    complainantcity character varying(64),
    complainanttelephone character varying(32),
    complainantemail character varying(64),
    complainttype bigint,
    complaintdetails character varying(1000),
    complainttitle character varying(1000),
    refcomplaintid bigint,
    priority bigint,
    receivingcenterid bigint,
    complaintlocation character varying(256),
    deptid bigint,
    complaintlandmark character varying(256),
    severity bigint,
    complainantfirstname_local character varying(32),
    complainaninitials_local character varying(32),
    complainantlastname_local character varying(32),
    complainantaddress_local character varying(256),
    complainantcity_local character varying(64),
    complaintdetails_local character varying(1000),
    complaintlocation_local character varying(256),
    complaintlandmark_local character varying(256),
    complainttitle_local character varying(32),
    mobilenumber character varying(32),
    complaintmode character varying(32),
    pincode character varying(32),
    othervalue character varying(256),
    id_mode bigint DEFAULT 1 NOT NULL,
    bndry bigint,
    toplevelbndry bigint,
    extrafield1 character varying(1024),
    isescalated bigint DEFAULT 0 NOT NULL,
    escalatedtime timestamp without time zone,
    expirydate timestamp without time zone,
    locbndryid bigint,
    latitude bigint,
    longitude bigint,
    portal_reqno character varying(64)
);




CREATE TABLE eggr_complaintgroup (
    id_complaintgroup bigint NOT NULL,
    complaintgroupname character varying(64) NOT NULL,
    is_active character(1)
);




CREATE TABLE eggr_complaintreceivingcenter (
    centerid bigint NOT NULL,
    centertype bigint,
    centername character varying(64),
    centeraddress character varying(128),
    centermanagerid bigint,
    centername_local character varying(64),
    centeraddress_local character varying(128),
    bndryid bigint,
    is_active character(1)
);




CREATE TABLE eggr_complaintstatus (
    complaintstatusid bigint NOT NULL,
    statusname character varying(64) NOT NULL
);




CREATE TABLE eggr_complainttypes (
    complainttypeid bigint NOT NULL,
    deptid bigint,
    complainttypename character varying(128),
    complainttypename_local character varying(128),
    complaint_type_code character varying(16),
    noofdays bigint,
    category bigint,
    complaintgroup_id bigint NOT NULL,
    is_active character(1),
    is_region character(1)
);




CREATE TABLE eggr_forward_tracker (
    fid bigint NOT NULL,
    complaintid bigint NOT NULL,
    fromofficerid bigint NOT NULL,
    toofficerid bigint,
    dateforwarded timestamp without time zone,
    comments character varying(256),
    notes character varying(256),
    modifiedby bigint
);




CREATE TABLE eggr_imagevideo_upload (
    uploadid bigint NOT NULL,
    complaintid bigint NOT NULL,
    image bytea,
    uploadtype character varying(256),
    contenttype character varying(256)
);




CREATE TABLE eggr_priority (
    priorityid bigint NOT NULL,
    priorityname character varying(32) NOT NULL
);




CREATE TABLE eggr_qams_store (
    id bigint NOT NULL,
    fdbk_obj_ref bigint NOT NULL,
    operator_id bigint NOT NULL,
    overallrating bigint,
    firstqtnrating bigint,
    secondqtnrating bigint,
    thirdqtnrating bigint,
    fourthqtnrating bigint,
    fifthqtnrating bigint,
    status character varying(50),
    qcdate timestamp without time zone,
    enquirydate timestamp without time zone,
    qchistory character varying(2000)
);




CREATE TABLE eggr_redressaldetails (
    complaintid bigint NOT NULL,
    redressalid bigint NOT NULL,
    responsedate timestamp without time zone,
    redressalofficerid bigint NOT NULL,
    response character varying(1000),
    extrafield1 character varying(32),
    extrafield2 character varying(32),
    extrafield3 character varying(32),
    complaintstatusid bigint,
    actiontype bigint,
    response_local character(10)
);




CREATE TABLE eggr_region_dept_mapping (
    id bigint NOT NULL,
    region_name character varying(50) NOT NULL,
    dept_id bigint NOT NULL,
    dept_name character varying(50) NOT NULL
);




CREATE TABLE eggr_roles_department (
    roleid bigint NOT NULL,
    departmentid bigint NOT NULL
);




CREATE TABLE eggr_status_roles (
    role bigint NOT NULL,
    status bigint NOT NULL,
    state_order bigint NOT NULL
);




CREATE TABLE eggr_status_tracker (
    pkid bigint NOT NULL,
    orderid bigint NOT NULL,
    statusid bigint NOT NULL,
    communicationid bigint,
    "timestamp" timestamp without time zone,
    fromuserid bigint NOT NULL,
    redressalid bigint NOT NULL,
    touserid bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE eglc_advcourttype_mapping (
    id_advocate bigint,
    id_courttype bigint
);




CREATE TABLE eglc_advocate_bill (
    id bigint NOT NULL,
    billnumber character varying(64) NOT NULL,
    billdate timestamp without time zone NOT NULL,
    id_advocate bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    fin_bill_id bigint,
    total_invoiced_amount double precision DEFAULT 0 NOT NULL,
    total_passed_amount double precision DEFAULT 0 NOT NULL,
    state_id bigint
);




CREATE TABLE eglc_advocate_master (
    id bigint NOT NULL,
    advocate_name character varying(128) NOT NULL,
    advocate_addr character varying(256),
    contact_phone character varying(256),
    advocate_specialty character varying(256),
    mobile_number character varying(256),
    email character varying(128),
    monthly_renumeration double precision,
    is_retaineradvocate bigint DEFAULT 1 NOT NULL,
    firmname character varying(128),
    pannumber character varying(20),
    is_active bigint DEFAULT 1 NOT NULL,
    is_senioradvocate bigint DEFAULT 0 NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    salutation character varying(10),
    paymentmode character varying(25),
    bankname character varying(150),
    bankaccount character varying(150),
    ifsccode character varying(150),
    tinumber character varying(150),
    fee bigint,
    bankbranch character varying(200)
);




CREATE TABLE eglc_advocatedept_mapping (
    id_department bigint NOT NULL,
    id_advocate bigint NOT NULL
);




CREATE TABLE eglc_appeal (
    id bigint NOT NULL,
    srnumber character varying(50) NOT NULL,
    appealfiledon timestamp without time zone,
    appealfiledby character varying(100),
    id_status bigint NOT NULL,
    id_judgmentimpl bigint NOT NULL
);




CREATE TABLE eglc_bill (
    id bigint NOT NULL,
    serialnumber bigint NOT NULL,
    id_advocate_bill bigint NOT NULL,
    id_stage bigint NOT NULL,
    id_account_head bigint NOT NULL,
    invoiced_amount double precision NOT NULL,
    passed_amount double precision NOT NULL,
    remarks character varying(256),
    id_legalcase bigint NOT NULL,
    previous_amountpaid_for_stage double precision,
    senior_present character varying(32)
);




CREATE TABLE eglc_bipartisandetails (
    id bigint NOT NULL,
    name character varying(128),
    address character varying(256),
    contact_number bigint,
    id_legalcase bigint NOT NULL,
    is_respondent bigint DEFAULT 0 NOT NULL,
    serial_number bigint NOT NULL,
    is_respondentgovernment bigint,
    id_respondentgovtdept bigint
);




CREATE TABLE eglc_case_stage (
    id bigint NOT NULL,
    stage character varying(128) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_casetype_master (
    id bigint NOT NULL,
    case_type character varying(100) NOT NULL,
    notes character varying(256),
    ordernumber bigint,
    code character varying(12),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_contempt (
    id bigint NOT NULL,
    canumber character varying(50) NOT NULL,
    receivingdate timestamp without time zone,
    iscommappr_required bigint DEFAULT 0,
    commapp_date timestamp without time zone,
    id_judgmentimpl bigint NOT NULL
);




CREATE TABLE eglc_court_master (
    id bigint NOT NULL,
    court_name character varying(256) NOT NULL,
    court_address character varying(256),
    id_courttype bigint NOT NULL,
    ordernumber bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_courttype_master (
    id bigint NOT NULL,
    court_type character varying(256) NOT NULL,
    notes character varying(256),
    ordernumber bigint,
    code character varying(12),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_employeehearing (
    id_employee bigint,
    id_hearing bigint
);




CREATE TABLE eglc_governmentdepartment (
    id bigint NOT NULL,
    code character varying(50),
    name character varying(256),
    description character varying(256),
    ordernumber bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_hearings (
    id bigint NOT NULL,
    hearing_date timestamp without time zone NOT NULL,
    id_legalcase bigint NOT NULL,
    is_standingcounselpresent bigint DEFAULT 1,
    additional_lawyers character varying(256),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    hearingoutcome character varying(1024),
    isseniorstandingcounselpresent bigint DEFAULT 0,
    purposeofhearing character varying(1024)
);




CREATE TABLE eglc_interimtype_master (
    id bigint NOT NULL,
    interimordertype character varying(100) NOT NULL,
    description character varying(256),
    code character varying(12) NOT NULL,
    ordernumber bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_judgment (
    id bigint NOT NULL,
    order_date timestamp without time zone NOT NULL,
    sent_to_dept_on timestamp without time zone,
    implement_by_date timestamp without time zone,
    cost_awarded double precision,
    compensation_awarded double precision,
    judgment_details character varying(1024) NOT NULL,
    judgment_document bytea,
    advisor_fee double precision,
    arbitrator_fee double precision,
    enquirydetails character varying(1024),
    enquirydate timestamp without time zone,
    setaside_petition_date timestamp without time zone,
    set_aside_petition_details character varying(1024),
    id_legalcase bigint NOT NULL,
    id_judgmenttype bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    saphearingdate timestamp without time zone,
    issapaccepted bigint,
    parent bigint,
    documentnum bigint
);




CREATE TABLE eglc_judgmentimpl (
    id bigint NOT NULL,
    is_compiled bigint NOT NULL,
    dateofcompliance timestamp without time zone,
    compliancereport character varying(1024),
    id_judgment bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    reason character varying(10),
    implementationdetails character varying(128)
);




CREATE TABLE eglc_judgmenttype_master (
    id bigint NOT NULL,
    judgmenttype character varying(50) NOT NULL,
    description character varying(256),
    code character varying(12) NOT NULL,
    ordernumber bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_lcinterimorder (
    id bigint NOT NULL,
    iodate timestamp without time zone NOT NULL,
    mpnumber character varying(50) NOT NULL,
    notes character varying(1024) NOT NULL,
    id_intordertypeid bigint,
    id_legalcase bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    sendtostandingcounsel timestamp without time zone,
    petitionfiledon timestamp without time zone,
    reportfilingdue timestamp without time zone,
    senttodepartment timestamp without time zone,
    revdfromhod timestamp without time zone,
    reportsendtostandingcounsel timestamp without time zone,
    reportfilingdate timestamp without time zone,
    documentnum bigint
);




CREATE TABLE eglc_legalcase (
    id bigint NOT NULL,
    casenumber character varying(50) NOT NULL,
    casedate timestamp without time zone,
    casetitle character varying(1024) NOT NULL,
    appealnum character varying(50),
    id_court bigint NOT NULL,
    id_casetype bigint NOT NULL,
    remarks character varying(1024),
    id_status bigint NOT NULL,
    case_receiving_date timestamp without time zone,
    isfiledbycorporation bigint,
    lcnumber character varying(50) NOT NULL,
    is_respondentgovernment bigint,
    id_respondentgovtdept bigint,
    prayer character varying(1024) NOT NULL,
    is_senioradvrequired bigint,
    id_petitiontype bigint NOT NULL,
    assignto_idboundary bigint,
    id_functionary bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    opp_party_advocate character varying(128),
    representedby character varying(256),
    lcnumber_type character varying(32) NOT NULL,
    documentnum bigint,
    wpyear bigint,
    id_reportstatus bigint,
    next_date timestamp without time zone
);




CREATE TABLE eglc_legalcase_advocate (
    id bigint NOT NULL,
    id_advocatemaster bigint NOT NULL,
    assignedtodate timestamp without time zone,
    vakalatdate timestamp without time zone,
    id_legalcase bigint NOT NULL,
    is_active bigint DEFAULT 1 NOT NULL,
    ordernumber character varying(50),
    orderdate timestamp without time zone,
    id_senioradvocate bigint,
    senior_assignedtodate timestamp without time zone,
    ordernumber_junior character varying(50),
    orderdate_junior timestamp without time zone,
    id_juniorstage bigint,
    reassignmentreason_junior character varying(1024),
    id_seniorstage bigint,
    reassignmentreason_senior character varying(1024),
    changeadvocate bigint DEFAULT 0,
    changesenioradvocate bigint DEFAULT 0
);




CREATE TABLE eglc_legalcase_batchcase (
    id bigint NOT NULL,
    id_legalcase bigint NOT NULL,
    casedate timestamp without time zone NOT NULL,
    casenumber character varying(50) NOT NULL,
    petitioner_name character varying(1024) NOT NULL
);




CREATE TABLE eglc_legalcase_dept (
    id_legalcase bigint NOT NULL,
    id_dept bigint NOT NULL,
    id bigint NOT NULL,
    date_of_receipt_of_pwr timestamp without time zone,
    id_position bigint NOT NULL,
    is_primarydepartment bigint DEFAULT 0 NOT NULL
);




CREATE TABLE eglc_legalcasedisposal (
    id bigint NOT NULL,
    disposal_date timestamp without time zone,
    disposal_details character varying(1024),
    consignmenttorecordroom_date timestamp without time zone,
    id_legalcase bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_legalcasefee (
    id bigint NOT NULL,
    installmentnumber bigint NOT NULL,
    additionalfees double precision,
    reason character varying(256),
    seniodadvfee double precision,
    arbitratorfee double precision,
    id_legalcase bigint NOT NULL,
    id_lcadvocate bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_petitiontype_master (
    id bigint NOT NULL,
    petition_code character varying(50) NOT NULL,
    petition_type character varying(100) NOT NULL,
    id_courttype bigint,
    ordernumber bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_pwr (
    id bigint NOT NULL,
    upload_pwr bytea,
    ca_filingdate timestamp without time zone,
    upload_ca bytea,
    id_legalcase bigint NOT NULL,
    ca_duedate timestamp without time zone,
    pwr_due_date timestamp without time zone
);




CREATE TABLE eglc_reportstatus (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE TABLE eglc_retainership_fee (
    id bigint NOT NULL,
    month bigint,
    year bigint
);




CREATE TABLE eglc_vacatestay_petition (
    id bigint NOT NULL,
    id_lcinterimorder bigint NOT NULL,
    vsreceivedfromstandingcounsel timestamp without time zone,
    vssendtostandingcounsel timestamp without time zone,
    vspetitionfiledon timestamp without time zone NOT NULL,
    remarks character varying(1024),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglc_vakalat (
    id bigint NOT NULL,
    vakalat_due_date timestamp without time zone,
    vakalat_compl_date timestamp without time zone,
    vakalat_assigned_to character varying(50),
    id_case bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglems_agreementdetail (
    id bigint NOT NULL,
    agreementnumber character varying(126),
    unitid bigint NOT NULL,
    dateofsanction timestamp without time zone,
    sanctionedby character varying(256),
    commensementdate timestamp without time zone,
    terminationdate timestamp without time zone,
    rateperunit bigint,
    totalarea bigint NOT NULL,
    totalamount bigint,
    resolutionno character varying(256),
    paymentstartingmonth timestamp without time zone,
    paymentendingmonth timestamp without time zone,
    isdeedexecuted character(1),
    deednumber character varying(32),
    documentid character varying(256),
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    remarks character varying(512),
    statusid bigint NOT NULL,
    tendernumber character varying(32),
    tenderauctionnumber character varying(32),
    ordernumber character varying(32),
    orderdate timestamp without time zone,
    agreementdate timestamp without time zone,
    casenumber character varying(32),
    orderdetail character varying(256),
    rrreadingnumber character varying(32),
    isuserotherthanallotee character(1),
    userid bigint,
    useraddressid bigint,
    paymentcycle bigint,
    uomid bigint,
    natureofallotment bigint,
    state_id bigint,
    previousagreement bigint,
    reasonforrenewal character varying(512),
    rateofincrease bigint,
    tccid bigint,
    demandid bigint,
    advanceamount bigint,
    totalarrears bigint,
    dateofhandover timestamp without time zone,
    tccflag bigint,
    natureofbusiness bigint,
    cancelleddate timestamp without time zone,
    cancelledorderdate timestamp without time zone,
    cancellationremarks character varying(1024),
    cancelledordernumber character varying(256),
    cancellationreason bigint,
    evictionreason bigint,
    proceedingnumber character varying(32),
    proceedingdate timestamp without time zone,
    courtreferencenumber character varying(32),
    evictiondocumentid character varying(256),
    islegacy bigint DEFAULT 0,
    demandgenerationdate timestamp without time zone
);




CREATE TABLE eglems_allottee (
    id bigint NOT NULL,
    ownerid bigint NOT NULL,
    addressid bigint NOT NULL,
    advanceamountpaid bigint,
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    agreementid bigint NOT NULL
);




CREATE TABLE eglems_baserent (
    id bigint NOT NULL,
    adminboundaryid bigint NOT NULL,
    locboundaryid bigint,
    baserent bigint NOT NULL,
    orderdate timestamp without time zone,
    ordernumber character varying(256),
    effectivefrom timestamp without time zone,
    usagetypeid bigint,
    uomid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone
);




CREATE TABLE eglems_estate (
    id bigint NOT NULL,
    estatetype character varying(32) NOT NULL,
    name character varying(256) NOT NULL,
    estatenumber character varying(126) NOT NULL,
    adminboundaryid bigint,
    locboundaryid bigint,
    landid bigint,
    assetid bigint,
    pincode bigint,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    statusid bigint,
    remarks character varying(512),
    nooffloors bigint,
    noofunits bigint,
    doorno character varying(32)
);




CREATE TABLE eglems_land (
    id bigint NOT NULL,
    assetid bigint,
    adminboundaryid bigint,
    locboundaryid bigint,
    oldsurveynumber character varying(32),
    newsurveynumber character varying(75) NOT NULL,
    townsurveynumber character varying(32) NOT NULL,
    villagename character varying(100) NOT NULL,
    possessiondate timestamp without time zone,
    landtypeid bigint NOT NULL,
    modeofacquisitionid bigint NOT NULL,
    landacquiredfrom character varying(256),
    acquiredpurpose character varying(512),
    usagereferenceno character varying(32),
    deeddate timestamp without time zone NOT NULL,
    compensationpaid bigint,
    specialconditiondetails character varying(3500),
    refnumber character varying(32),
    documentid character varying(256),
    indexnumber character varying(32),
    layoutnumber character varying(32),
    layoutpromoterawardnumber character varying(32),
    giftedpersonname character varying(256),
    landacqgovtordernumber character varying(32),
    landacqcollectororder character varying(32),
    landacqcouncilnumber character varying(32),
    remarks character varying(1000),
    landacqawardno character varying(32),
    alienationlandgovtordernumber character varying(32),
    alienationlandcollectorder character varying(32),
    alienationlandcouncilnumber character varying(32),
    alienationlandcost bigint,
    osrland character(1),
    talukid bigint,
    landuomid bigint,
    landarea character varying(32),
    blocknumber character varying(32),
    subdivisionnumber character varying(32),
    cawne character varying(32),
    ground character varying(32)
);




CREATE TABLE eglems_landtype (
    id bigint NOT NULL,
    type character varying(256) NOT NULL,
    description character varying(256),
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    code character varying(8) NOT NULL
);




CREATE TABLE eglems_modeofacquisition (
    id bigint NOT NULL,
    name character varying(256) NOT NULL,
    landtype bigint,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    code character varying(126) NOT NULL
);




CREATE TABLE eglems_natureofbusiness (
    id bigint NOT NULL,
    business_type character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE eglems_paymentcycle (
    id bigint NOT NULL,
    code character varying(256) NOT NULL,
    name character varying(36) NOT NULL,
    description character varying(256),
    value bigint,
    isactive bigint DEFAULT 1
);




CREATE TABLE eglems_policychange (
    id bigint NOT NULL,
    financialyearid bigint NOT NULL,
    damageamount bigint NOT NULL,
    agreementid bigint NOT NULL,
    councilresolution character varying(126),
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE eglems_quarter (
    id bigint NOT NULL,
    tmtfloordetailid bigint,
    rrnumber bigint,
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    tenementid bigint,
    remarks character varying(512)
);




CREATE TABLE eglems_rentincrement (
    id bigint NOT NULL,
    percentage bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone
);




CREATE TABLE eglems_scfloordetail (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    shoppingcomplexid bigint NOT NULL,
    maximumnoofshops bigint NOT NULL,
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    status bigint
);




CREATE TABLE eglems_shop (
    id bigint NOT NULL,
    scfloordetailid bigint NOT NULL,
    rrnumber bigint,
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE eglems_shoppingcomplex (
    id bigint NOT NULL
);




CREATE TABLE eglems_taxrate (
    id bigint NOT NULL,
    from_date timestamp without time zone NOT NULL,
    to_date timestamp without time zone NOT NULL,
    tax_percentage bigint NOT NULL,
    is_active character(1) DEFAULT 1
);




CREATE TABLE eglems_tcc (
    id bigint NOT NULL,
    remarks character varying(1024),
    handbackremarks character varying(1024),
    tccnumber character varying(256) NOT NULL,
    tccpossessiondate timestamp without time zone,
    tcchandedbackdate timestamp without time zone,
    documentnumber character varying(256) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    state_id bigint,
    statusid bigint NOT NULL
);




CREATE TABLE eglems_tender (
    id bigint NOT NULL,
    tendernotificationnumber character varying(126) NOT NULL,
    tenderdate timestamp without time zone NOT NULL,
    tendertime character varying(20),
    subject character varying(1024),
    statusid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    remarks character varying(1024),
    state_id bigint
);




CREATE TABLE eglems_tenderdetail (
    id bigint NOT NULL,
    tenderid bigint NOT NULL,
    tendernumber character varying(126) NOT NULL,
    marketrate bigint,
    modifieddate timestamp without time zone,
    unitid bigint,
    reason character varying(512),
    statusshop character varying(512)
);




CREATE TABLE eglems_tenement (
    id bigint NOT NULL,
    isindependent character(1) NOT NULL
);




CREATE TABLE eglems_tmtfloordetail (
    id bigint NOT NULL,
    name character varying(126) NOT NULL,
    tenementid bigint NOT NULL,
    maximumnoofquarters bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    status bigint
);




CREATE TABLE eglems_unit (
    id bigint NOT NULL,
    unittype character varying(32) NOT NULL,
    unitnumber character varying(256) NOT NULL,
    totalarea bigint NOT NULL,
    statusid bigint NOT NULL,
    usagetype bigint,
    disposeid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone,
    oldunitnumber character varying(256),
    uomid bigint NOT NULL,
    totalplotarea bigint,
    plotuomid bigint
);




CREATE TABLE eglems_unitdetails (
    id bigint NOT NULL,
    unitid bigint NOT NULL,
    inventoryname character varying(256) NOT NULL,
    quantity bigint NOT NULL,
    uom character varying(32),
    remarks character varying(512),
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL
);




CREATE TABLE eglems_usagetype (
    id bigint NOT NULL,
    type character varying(256) NOT NULL,
    description character varying(256),
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    assetcategoryid bigint
);




CREATE TABLE egovintegrated_schema_versions (
    version character varying(32) NOT NULL
);




CREATE TABLE egovsampledata_schema_versions (
    version character varying(32) NOT NULL
);




CREATE TABLE egp_actions (
    id bigint NOT NULL,
    requestid bigint NOT NULL,
    actionname character varying(64) NOT NULL,
    actionurl character varying(256) NOT NULL
);




CREATE SEQUENCE egp_actions_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egp_citizen (
    id bigint NOT NULL,
    pan character varying(20),
    auid character varying(100),
    passportno character varying(50),
    gender character varying(1)
);




CREATE TABLE egp_citizenactiveservcreg (
    id bigint NOT NULL,
    referenceno character varying(50),
    description character varying(500) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_citizennotification (
    id bigint NOT NULL,
    subject character varying(100) NOT NULL,
    message character varying(500) NOT NULL,
    readstatus smallint NOT NULL,
    priorityflag character varying(1) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_citizenserviceregistry (
    id bigint NOT NULL,
    parentid bigint,
    actionid bigint NOT NULL,
    moduleid bigint NOT NULL,
    actiontype character varying(1) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    isenabled smallint DEFAULT 1 NOT NULL
);




CREATE TABLE egp_citizenservicereqregistry (
    id bigint NOT NULL,
    requestid character varying(50) NOT NULL,
    entityrefno character varying(50),
    applrefno character varying(50),
    requeststatus character varying(50) NOT NULL,
    message character varying(500) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_firm (
    id bigint NOT NULL,
    name character varying(150) NOT NULL,
    pan character varying(20),
    tin character varying(50),
    designation character varying(100)
);




CREATE TABLE egp_firmactiveservcreg (
    id bigint NOT NULL,
    referenceno character varying(50),
    description character varying(500) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_firmnotification (
    id bigint NOT NULL,
    subject character varying(100) NOT NULL,
    message character varying(500) NOT NULL,
    readstatus smallint NOT NULL,
    priorityflag character varying(1) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_firmserviceregistry (
    id bigint NOT NULL,
    parentid bigint,
    actionid bigint NOT NULL,
    moduleid bigint NOT NULL,
    actiontype character varying(1) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    isenabled smallint DEFAULT 1 NOT NULL
);




CREATE TABLE egp_firmservicereqregistry (
    id bigint NOT NULL,
    requestid character varying(50) NOT NULL,
    entityrefno character varying(50),
    applrefno character varying(50),
    requeststatus character varying(50) NOT NULL,
    message character varying(500) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_notifications (
    id bigint NOT NULL,
    requestid bigint NOT NULL,
    heading character varying(256),
    summary character varying(1024),
    status smallint DEFAULT 1 NOT NULL,
    createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    createdby bigint NOT NULL
);




CREATE SEQUENCE egp_notifications_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE egp_organisation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egp_organization (
    id bigint NOT NULL,
    name character varying(256) NOT NULL
);




CREATE TABLE egp_portaluser (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    usertype character varying(20),
    landphno character varying(20),
    activationcode character varying(8),
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_publicnotification (
    id bigint NOT NULL,
    subject character varying(100) NOT NULL,
    message character varying(500) NOT NULL,
    cutoffdate timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE SEQUENCE egp_requeset_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egp_request (
    id bigint NOT NULL,
    userid bigint NOT NULL,
    modulename character varying(128) NOT NULL,
    requesttype character varying(64) NOT NULL,
    applicationdate timestamp without time zone NOT NULL,
    businesskey character varying(128) NOT NULL,
    description character varying(256),
    message character varying(256),
    actionurl character varying(256),
    status character varying(64) NOT NULL,
    portalitemstatus character varying(64),
    createddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    createdby bigint NOT NULL,
    modifiedby bigint
);




CREATE TABLE egp_salary_budget (
    id bigint,
    billid bigint,
    budgetcodeid bigint,
    functionid bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egp_surveyor (
    id bigint NOT NULL,
    code character varying(20),
    name character varying(100),
    corr_address character varying(200),
    narration character varying(200),
    id_bank bigint,
    ifsccode character varying(20),
    bankaccountnumber character varying(20),
    pannumber character varying(20),
    tin character varying(50),
    isenabled smallint NOT NULL,
    off_address character varying(200)
);




CREATE TABLE egp_surveyoractiveservcreg (
    id bigint NOT NULL,
    referenceno character varying(50),
    description character varying(500) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_surveyordetails (
    id bigint NOT NULL,
    id_surveyor bigint NOT NULL,
    surveyor_class character varying(50),
    status character varying(50),
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    id_boundary bigint NOT NULL
);




CREATE TABLE egp_surveyornotification (
    id bigint NOT NULL,
    subject character varying(100) NOT NULL,
    message character varying(500) NOT NULL,
    readstatus smallint NOT NULL,
    priorityflag character varying(1) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egp_surveyorserviceregistry (
    id bigint NOT NULL,
    parentid bigint,
    actionid bigint NOT NULL,
    moduleid bigint NOT NULL,
    actiontype character varying(1) NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL,
    isenabled smallint DEFAULT 1 NOT NULL
);




CREATE TABLE egp_surveyorservicereqregistry (
    id bigint NOT NULL,
    requestid character varying(50) NOT NULL,
    entityrefno character varying(50),
    applrefno character varying(50),
    requeststatus character varying(50) NOT NULL,
    message character varying(500) NOT NULL,
    portaluserid bigint NOT NULL,
    serviceregid bigint NOT NULL,
    createdby bigint NOT NULL,
    modifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egpay_advance_schedule (
    id bigint NOT NULL,
    id_saladvance bigint,
    installment_no bigint NOT NULL,
    principal_amt double precision,
    interest_amt double precision,
    recover character varying(1)
);




CREATE TABLE egpay_batchfailuredetails (
    id bigint NOT NULL,
    empid bigint NOT NULL,
    month bigint NOT NULL,
    financialyearid bigint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    paytype bigint NOT NULL,
    remarks character varying(2000),
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    status bigint NOT NULL,
    ishistory character varying(2),
    deptid bigint NOT NULL,
    id_functionary bigint,
    id_function bigint
);




CREATE TABLE egpay_batchgendetails (
    id bigint NOT NULL,
    id_dept bigint,
    financialyearid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    remarks character varying(300),
    month bigint NOT NULL,
    succcount bigint NOT NULL,
    failcount bigint NOT NULL,
    status smallint NOT NULL,
    id_functionary bigint,
    sch_job_group_name character varying(100),
    sch_job_name character varying(100),
    modifieddate timestamp without time zone,
    modifiedby bigint,
    id_function bigint,
    approverpos bigint
);




CREATE TABLE egpay_category_master (
    id bigint NOT NULL,
    name character varying(64) NOT NULL,
    description character varying(64),
    cat_type character varying(1) NOT NULL,
    createdby bigint NOT NULL,
    createdtimestamp timestamp without time zone NOT NULL
);




CREATE TABLE egpay_deductions (
    id bigint NOT NULL,
    id_emppayroll bigint NOT NULL,
    id_salcode bigint,
    amount double precision NOT NULL,
    id_sal_advance bigint,
    id_accountcode bigint,
    reference_no character varying(100),
    id_advance_scheduler bigint
);




CREATE TABLE egpay_earnings (
    id bigint NOT NULL,
    id_emppayroll bigint NOT NULL,
    id_salcode bigint NOT NULL,
    pct double precision,
    amount double precision NOT NULL
);




CREATE TABLE egpay_emppayroll (
    id bigint NOT NULL,
    id_employee bigint NOT NULL,
    id_emp_assignment bigint NOT NULL,
    gross_pay double precision NOT NULL,
    net_pay double precision NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    id_billregister bigint,
    financialyearid bigint NOT NULL,
    month bigint NOT NULL,
    exception_comments character varying(512),
    status bigint NOT NULL,
    basic_pay bigint NOT NULL,
    paytype smallint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone NOT NULL,
    workingdays bigint NOT NULL,
    modify_remarks character varying(512),
    numdays double precision NOT NULL,
    lastmodifieddate timestamp without time zone,
    state_id bigint,
    first_approver_pos bigint,
    id_payscale_employee bigint
);




CREATE TABLE egpay_emppayrolltypes (
    id bigint NOT NULL,
    paytype character varying(512) NOT NULL,
    narration character varying(800) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint,
    bill_type character varying(512) NOT NULL
);




CREATE TABLE egpay_exception (
    id bigint NOT NULL,
    id_exceptionmstr bigint NOT NULL,
    id_employee bigint NOT NULL,
    userid bigint NOT NULL,
    month bigint NOT NULL,
    financilayear_id bigint NOT NULL,
    comments character varying(512),
    status bigint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    sr_entry bigint,
    expdate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone,
    state_id bigint,
    disciplinary_id bigint
);




CREATE TABLE egpay_exception_mstr (
    id bigint NOT NULL,
    type character varying(64) NOT NULL,
    reason character varying(512)
);




CREATE TABLE egpay_incrementdetails (
    id bigint NOT NULL,
    month smallint NOT NULL,
    financialyearid bigint NOT NULL,
    incrementdate timestamp without time zone NOT NULL,
    amount bigint NOT NULL,
    empid bigint NOT NULL,
    remarks character varying(200),
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    status bigint NOT NULL,
    id_payscale_employee bigint
);




CREATE TABLE egpay_paygenrulessetup_mstr (
    id bigint NOT NULL,
    salarycode_id bigint,
    percentage double precision,
    monthlyflat_amount double precision,
    month bigint,
    id_financialyear bigint,
    lastmodified_date timestamp without time zone
);




CREATE TABLE egpay_payhead_rule (
    id bigint NOT NULL,
    id_salarycode bigint,
    effective_from timestamp without time zone,
    description character varying(64),
    id_wf_action bigint
);




CREATE TABLE egpay_payscale_details (
    id bigint NOT NULL,
    id_payheader bigint NOT NULL,
    id_salarycodes bigint NOT NULL,
    pct double precision,
    amount double precision NOT NULL
);




CREATE TABLE egpay_payscale_employee (
    id bigint NOT NULL,
    id_payheader bigint NOT NULL,
    id_employee bigint NOT NULL,
    effectivefrom timestamp without time zone NOT NULL,
    annual_increment timestamp without time zone,
    monthly_pay bigint,
    stagnant_pay character varying(1) DEFAULT 'N'::character varying NOT NULL,
    daily_pay bigint,
    createdby bigint,
    createddate timestamp without time zone,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egpay_payscale_header (
    id bigint NOT NULL,
    name character varying(512) NOT NULL,
    paycommision character varying(512),
    effectivefrom timestamp without time zone NOT NULL,
    amountfrom double precision NOT NULL,
    amountto double precision NOT NULL,
    type character varying(512),
    id_wf_action bigint,
    pay_fixed_id bigint,
    createdby bigint,
    createddate timestamp without time zone,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egpay_payscale_incrdetails (
    id bigint NOT NULL,
    incslabamt double precision NOT NULL,
    incslabtoamt double precision NOT NULL,
    incslabfrmamt double precision NOT NULL,
    id_payheader bigint NOT NULL
);




CREATE TABLE egpay_pfdetails (
    id bigint NOT NULL,
    pfheaderid bigint NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    todate timestamp without time zone,
    annualrateofinterest real NOT NULL
);




CREATE TABLE egpay_pfheader (
    id bigint NOT NULL,
    pfaccountid bigint,
    pfintexpaccountid bigint,
    frequency character varying(8),
    tds_id bigint,
    id_wf_action bigint,
    pf_type character varying(10)
);




CREATE TABLE egpay_pftrigger_detail (
    id bigint,
    pf_type character varying(10),
    month bigint,
    financialyearid bigint,
    createdby bigint,
    createddate timestamp without time zone
);




CREATE TABLE egpay_saladvances (
    id bigint NOT NULL,
    requested_amt double precision,
    advance_amt double precision NOT NULL,
    interest_pct double precision,
    interest_type character varying(32),
    num_of_inst double precision NOT NULL,
    interest_amt double precision,
    inst_amt double precision NOT NULL,
    pending_amt double precision NOT NULL,
    sanction_num character varying(32),
    sanctioned_by bigint,
    sanctioned_date timestamp without time zone,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    id_salcode bigint NOT NULL,
    id_employee bigint NOT NULL,
    status bigint,
    payment_type character varying(32),
    advance_type character varying(32),
    bankaccount_id bigint,
    maintain_schedule character varying(1),
    state_id bigint,
    is_legacy_advance character varying(1) DEFAULT 'N'::character varying,
    doc_no bigint,
    previous_pending_amt bigint
);




COMMENT ON COLUMN egpay_saladvances.doc_no IS 'Document Reference Number';



CREATE TABLE egpay_saladvances_arf (
    id bigint NOT NULL,
    advance_id bigint NOT NULL
);




CREATE TABLE egpay_salary_billregister (
    id bigint NOT NULL,
    first_approver_pos bigint
);




CREATE TABLE egpay_salarycodes (
    id bigint NOT NULL,
    head character varying(64) NOT NULL,
    categoryid bigint NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    description character varying(1000) NOT NULL,
    is_taxable character(1) DEFAULT 'N'::bpchar NOT NULL,
    cal_type character varying(32) NOT NULL,
    glcodeid bigint,
    pct_basis bigint,
    order_id bigint,
    tds_id bigint,
    local_lang_desc character varying(1000),
    interest_glcodeid bigint,
    isattendancebased character varying(1) NOT NULL,
    isrecomputed character varying(1) NOT NULL,
    isrecurring character varying(1) NOT NULL,
    capture_rate character varying(1) DEFAULT 'N'::character varying NOT NULL,
    id_wf_action bigint
);




CREATE TABLE egpen_additionbasic_valmap (
    id bigint NOT NULL,
    minimumage smallint NOT NULL,
    maximumage smallint NOT NULL,
    percentage smallint NOT NULL,
    description character varying(50) NOT NULL
);




CREATE TABLE egpen_advance (
    id bigint NOT NULL,
    id_pensionerheader bigint NOT NULL,
    id_pensionbill bigint,
    id_payhead smallint NOT NULL,
    advance_amt double precision NOT NULL,
    inst_amt double precision,
    pending_amt double precision,
    status smallint,
    month smallint NOT NULL,
    year smallint NOT NULL,
    num_of_inst smallint NOT NULL,
    createdby smallint NOT NULL,
    lastmodifiedby smallint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    interest_pct real,
    interest_type character varying(50),
    interest_amt real,
    islegacy character(1) DEFAULT 'N'::bpchar
);




CREATE TABLE egpen_advance_schedule (
    id bigint NOT NULL,
    id_advance bigint NOT NULL,
    installment_no smallint,
    principal_amt double precision,
    createdby smallint NOT NULL,
    lastmodifiedby smallint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    interest_amt real,
    isrecovered character(1) DEFAULT 'N'::bpchar
);




CREATE TABLE egpen_arrears_detail (
    id bigint NOT NULL,
    arrears_header_id bigint NOT NULL,
    status bigint NOT NULL,
    id_pensioner_header bigint,
    id_family_pensioner bigint,
    amount bigint NOT NULL
);




CREATE TABLE egpen_arrears_header (
    id bigint NOT NULL,
    payheadrates_id bigint NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    pensionerscount bigint,
    familypensionerscount bigint,
    pensionersamount bigint,
    familypensionersamount bigint
);




CREATE TABLE egpen_basic_components (
    id bigint NOT NULL,
    head1_amt double precision,
    head2_amt double precision,
    head3_amt double precision,
    head4_amt double precision,
    head5_amt double precision,
    is_active character varying(1)
);




CREATE TABLE egpen_batchfailuredetails (
    id bigint NOT NULL,
    pensionerid bigint,
    month bigint NOT NULL,
    financialyearid bigint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    pensiontype bigint NOT NULL,
    remarks character varying(2000),
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    status bigint NOT NULL,
    ishistory character varying(2),
    deptid bigint,
    familypensionerid bigint,
    functionary_id bigint,
    fund_id bigint
);




CREATE TABLE egpen_batchgendetails (
    id bigint NOT NULL,
    id_dept bigint,
    financialyearid bigint NOT NULL,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    createdby bigint,
    createddate timestamp without time zone,
    remarks character varying(300),
    month bigint NOT NULL,
    succcount bigint NOT NULL,
    failcount bigint NOT NULL,
    status smallint NOT NULL,
    sch_job_group_name character varying(100),
    sch_job_name character varying(100),
    modifieddate timestamp without time zone,
    modifiedby bigint,
    approverpos bigint,
    functionary_id bigint,
    fund_id bigint
);




CREATE SEQUENCE egpen_batchgendetails_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egpen_billmisinfo_mapping (
    id bigint NOT NULL,
    objecttype character varying(50),
    pensionerdept character varying(50),
    billdept character varying(50),
    billfunction character varying(50),
    billfund character varying(50)
);




CREATE TABLE egpen_category_master (
    id bigint NOT NULL,
    name character varying(64) NOT NULL,
    description character varying(64),
    cat_type character varying(1) NOT NULL,
    createdby bigint NOT NULL,
    createdtimestamp timestamp without time zone NOT NULL
);




CREATE TABLE egpen_cert_master (
    id bigint NOT NULL,
    code character varying(64),
    name character varying(64),
    description character varying(64),
    applicable_to character varying(64),
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone
);




CREATE TABLE egpen_cert_submission_details (
    id bigint NOT NULL,
    id_certsubmit_header bigint NOT NULL,
    id_cert_master bigint NOT NULL
);




CREATE TABLE egpen_cert_submission_header (
    id bigint NOT NULL,
    id_pensioner_header bigint,
    id_family_pensioner_header bigint,
    year bigint NOT NULL,
    submission_date timestamp without time zone,
    valid_upto timestamp without time zone,
    grace_period timestamp without time zone,
    doc_id bigint,
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone
);




CREATE TABLE egpen_commutation (
    id bigint NOT NULL,
    id_pensioner_header bigint NOT NULL,
    commutation_amt double precision,
    commutationdate timestamp without time zone NOT NULL,
    status_id bigint NOT NULL,
    state_id bigint,
    created_by bigint NOT NULL,
    last_modified_by bigint,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone,
    type character varying(10) NOT NULL,
    monthlydeduction bigint NOT NULL,
    pending_number_of_months bigint NOT NULL,
    id_bill bigint,
    auto_commutation_amt bigint,
    pension_amt bigint
);




CREATE TABLE egpen_commutationvalue_mapping (
    id bigint NOT NULL,
    age bigint NOT NULL,
    commutationvalue bigint NOT NULL
);




CREATE TABLE egpen_deathgratuity_details (
    id bigint NOT NULL,
    id_gratuity bigint,
    id_empnominee bigint,
    amount double precision,
    percentage double precision
);




CREATE TABLE egpen_deductions (
    id bigint NOT NULL,
    id_monthly_pension bigint NOT NULL,
    id_payhead bigint,
    amount double precision NOT NULL,
    id_advance bigint,
    id_accountcode bigint,
    reference_no character varying(32),
    id_advance_scheduler bigint,
    CONSTRAINT payheadorglcodenotnull CHECK ((((id_payhead IS NOT NULL) AND ((id_payhead)::text <> ''::text)) OR ((id_accountcode IS NOT NULL) AND ((id_accountcode)::text <> ''::text))))
);




CREATE TABLE egpen_earnings (
    id bigint NOT NULL,
    id_monthly_pension bigint NOT NULL,
    id_payhead bigint NOT NULL,
    pct double precision,
    amount double precision NOT NULL,
    id_arrears_detail bigint
);




CREATE TABLE egpen_family_pensioner_details (
    id bigint NOT NULL,
    id_family_pensioner_header bigint NOT NULL,
    pension_amt double precision,
    family_pension_amt double precision,
    death_gratuity_amt double precision,
    pension_commencement_date timestamp without time zone,
    last_date_for_enh_family_pen timestamp without time zone,
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    enh_family_pen_amt bigint
);




CREATE TABLE egpen_family_pensioner_header (
    id bigint NOT NULL,
    emp_id bigint,
    emp_code character varying(20) NOT NULL,
    pensionerheader_id bigint,
    nominee_id bigint,
    family_pensioner_number character varying(60),
    family_pensioner_name character varying(256),
    emp_date_of_birth timestamp without time zone,
    emp_date_of_death timestamp without time zone,
    emp_date_of_appointment timestamp without time zone,
    emp_date_of_retirement timestamp without time zone,
    emp_net_qualifying_service bigint,
    emp_department_id bigint,
    emp_designation_id bigint,
    emp_payscale_id bigint,
    emp_grade_id bigint,
    status_id bigint,
    disbursement_type_id bigint,
    pen_bank_details_id bigint,
    emp_component_id bigint,
    emp_lastincrement timestamp without time zone,
    emp_payscale character varying(60),
    emp_fund_id bigint,
    address_id bigint,
    date_of_birth timestamp without time zone,
    date_of_death timestamp without time zone,
    document_number bigint,
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    last_date_for_enh_family_pen timestamp without time zone,
    old_pensioner_number character varying(60),
    functionary_id bigint,
    drawingoffice_id bigint,
    pay_commission_id bigint,
    lfaudit_number character varying(40),
    proceeding_number character varying(40),
    familypensioner_type bigint,
    additionbasic_valmap_id bigint
);




CREATE TABLE egpen_func_do_mapping (
    id bigint NOT NULL,
    functionary_id bigint NOT NULL,
    drawingofficer_id bigint NOT NULL
);




CREATE TABLE egpen_gratuity (
    id bigint NOT NULL,
    id_pensioner_header bigint,
    gratuity_amt double precision,
    type bigint,
    status_id bigint,
    id_bill_register bigint,
    id_wf_state bigint,
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    auto_gratuity_amt bigint,
    id_family_pensioner_header bigint
);




CREATE TABLE egpen_monthly_pension (
    id bigint NOT NULL,
    id_pension_header bigint,
    gross_pay double precision NOT NULL,
    net_pay double precision NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    id_billregister bigint,
    financialyearid bigint NOT NULL,
    month bigint NOT NULL,
    exception_comments character varying(512),
    status bigint NOT NULL,
    basic_pay bigint NOT NULL,
    pensiontype smallint NOT NULL,
    modify_remarks character varying(512),
    lastmodifieddate timestamp without time zone,
    state_id bigint,
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    id_family_pensioner_header bigint,
    comments character varying(512),
    functionary_id bigint,
    drawingoffice_id bigint,
    department_id bigint,
    fund_id bigint,
    first_approver_pos bigint,
    id_bank bigint,
    id_bankbranch bigint
);




CREATE TABLE egpen_nominee (
    emp_nominee_id bigint NOT NULL,
    pensioner_header_id bigint
);




CREATE TABLE egpen_pay_commission (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    code character varying(20) NOT NULL,
    effective_from timestamp without time zone NOT NULL
);




CREATE TABLE egpen_payhead_rates (
    id bigint NOT NULL,
    payhead_id bigint NOT NULL,
    refpayhead_id bigint,
    percentage real,
    prev_percentage real,
    flat_amount bigint,
    rule_effectivefrom timestamp without time zone NOT NULL,
    rule_implmonth bigint,
    rule_implyear bigint,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    paycommision_id bigint,
    prev_flat_amount bigint
);




CREATE TABLE egpen_payhead_rule (
    id bigint NOT NULL,
    id_payhead bigint,
    effective_from timestamp without time zone,
    description character varying(64),
    id_wf_action bigint,
    created_by bigint,
    created_date timestamp without time zone,
    last_modified_by bigint,
    last_modified_date timestamp without time zone
);




CREATE TABLE egpen_payheads (
    id bigint NOT NULL,
    head character varying(64) NOT NULL,
    categoryid bigint NOT NULL,
    description character varying(1000),
    cal_type character varying(32) NOT NULL,
    glcodeid bigint NOT NULL,
    pct_basis bigint,
    order_id bigint,
    tds_id bigint,
    local_lang_desc character varying(1000),
    interest_glcodeid bigint,
    isrecomputed character varying(1) NOT NULL,
    isrecurring character varying(1) NOT NULL,
    capture_rate character varying(1) DEFAULT 'N'::character varying NOT NULL,
    id_wf_action bigint,
    ref_head bigint,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    ref_arrear_head bigint
);




CREATE TABLE egpen_pension (
    id bigint NOT NULL,
    id_pensioner_header bigint,
    type bigint NOT NULL,
    pension_amt bigint,
    status_id bigint NOT NULL,
    state_id bigint,
    created_by bigint NOT NULL,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    auto_pension_amt bigint,
    id_family_pensioner_header bigint,
    normal_pension_amt bigint,
    auto_normal_pension_amt bigint,
    enh_family_pen_amt bigint,
    auto_enh_family_pen_amt bigint
);




CREATE TABLE egpen_pension_banks (
    id bigint NOT NULL,
    id_bank bigint NOT NULL,
    modifieddate timestamp without time zone,
    modifiedby bigint
);




CREATE TABLE egpen_pension_branches (
    id bigint NOT NULL,
    id_branch bigint NOT NULL,
    modifieddate timestamp without time zone,
    modifiedby bigint
);




CREATE TABLE egpen_pension_change_history (
    id bigint NOT NULL,
    id_pensioner_header bigint,
    effectivedate timestamp without time zone NOT NULL,
    amount double precision NOT NULL,
    remarks character varying(1024),
    created_by bigint NOT NULL,
    last_modified_by bigint,
    created_date timestamp without time zone NOT NULL,
    last_modified_date timestamp without time zone,
    id_family_pensioner_header bigint
);




CREATE TABLE egpen_pension_types (
    id bigint NOT NULL,
    pensiontype character varying(512) NOT NULL,
    narration character varying(800) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint NOT NULL,
    bill_type character varying(512) NOT NULL
);




CREATE TABLE egpen_pensionbill (
    id bigint NOT NULL,
    first_approver_pos bigint
);




CREATE TABLE egpen_pensioner_bank_details (
    id bigint NOT NULL,
    id_bank bigint NOT NULL,
    id_branch bigint NOT NULL,
    account_number character varying(20) NOT NULL,
    is_active character(1),
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone
);




CREATE TABLE egpen_pensioner_details (
    id bigint NOT NULL,
    id_pensioner_header bigint,
    pension_amt double precision,
    family_pension_amt double precision,
    gratuity_amt double precision,
    death_gratuity_amt double precision,
    commutation1_amt double precision,
    commutation2_amt double precision,
    commutation3_amt double precision,
    pension_commencement_date timestamp without time zone,
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    enh_family_pen_amt bigint
);




CREATE TABLE egpen_pensioner_header (
    id bigint NOT NULL,
    emp_id bigint,
    emp_code character varying(20) NOT NULL,
    pensioner_number character varying(60),
    pensioner_name character varying(256),
    date_of_birth timestamp without time zone,
    date_of_appointment timestamp without time zone,
    date_of_retirement timestamp without time zone,
    net_qualifying_service bigint,
    department_id bigint,
    designation_id bigint,
    payscale_id bigint,
    grade_id bigint,
    status_id bigint,
    commutation_opted character(1),
    disbursement_type_id bigint,
    pen_bank_details_id bigint,
    component_id bigint,
    created_by bigint,
    last_modified_by bigint,
    created_date timestamp without time zone,
    last_modified_date timestamp without time zone,
    lastincrement timestamp without time zone,
    payscale character varying(60),
    date_of_death timestamp without time zone,
    fund_id bigint,
    salarycodes_id bigint,
    address_id bigint,
    document_number bigint,
    lfaudit_number character varying(256),
    proceeding_number character varying(256),
    pensioner_typedata character varying(256),
    commisioner_order_number character varying(60),
    old_pensioner_number character varying(60),
    medically_fit character(1),
    functionary_id bigint,
    drawingoffice_id bigint,
    pay_commission_id bigint,
    additionbasic_valmap_id bigint
);




CREATE TABLE egpen_pensioner_nominee (
    id bigint NOT NULL,
    id_pensioner_header bigint,
    id_nominee bigint,
    is_eligible character(1),
    pct double precision,
    active_from timestamp without time zone,
    active_to timestamp without time zone
);




CREATE TABLE egpen_recovery_details (
    id bigint NOT NULL,
    glcodeid bigint,
    id_gratuity bigint,
    recovery_amt double precision
);




CREATE TABLE egpen_type_master (
    id bigint NOT NULL,
    type character varying(64)
);




CREATE SEQUENCE egpims_add_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_annual_increment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_bank_det_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_cat_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_community_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_dept_tests_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_designation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_det_of_enquiry_off_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_disciplinary_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_edu_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_edu_qul_mstr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_edu_qulification_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_eo_designation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_grade_mstr_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_how_acquired_seq
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_immovable_prop_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_lang_known_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_languages_known_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_languages_qulified_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_languages_skills_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_leave_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_leavetype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_ltc_pirticulars_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_mode_of_recruiment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_movable_prop_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_name_of_test_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_nat_of_allig_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_nat_of_dis_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_nat_of_pun_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_nomimation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_pay_fixed_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_personal_info_seq
    START WITH 69
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_post_mstr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_probation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_recruitment_type_seq
    START WITH 12
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_regularisation_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_relation_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_religion_seq
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_scale_of_pay_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_skill_mstr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_skill_mstr_seq1
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_tec_grade_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_tec_qu_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_teck_grades_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_teck_grades_seq1
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_training_course_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_training_pir_se
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_type_of_leave_seq
    START WITH 6
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egpims_uni_board_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egpt_address (
    subnumber character varying(32),
    doornumold character varying(32),
    emailaddress character varying(64),
    mobileno character varying(10),
    id_address bigint
);




CREATE TABLE egpt_basic_property (
    id_basic_property bigint NOT NULL,
    userid bigint NOT NULL,
    propertyid character varying(64),
    is_active character(1) DEFAULT 'Y'::bpchar NOT NULL,
    addressid bigint NOT NULL,
    crt_timestamp timestamp without time zone NOT NULL,
    updt_timestamp timestamp without time zone,
    reg_num character varying(64),
    reg_date timestamp without time zone,
    id_prop_doc bigint,
    id_propertyid bigint NOT NULL,
    municiapl_no_old character varying(64),
    id_adm_bndry bigint,
    dc_reg_no bigint,
    dc_serial_no character varying(32),
    extra_field1 character varying(30),
    extra_field2 character varying(30),
    mcno character varying(64),
    extra_field3 character varying(30),
    id_prop_create_reason smallint,
    prop_create_date timestamp without time zone,
    status bigint,
    old_propertyid character varying(64),
    id_property_subgroup bigint,
    is_bill_created character(1),
    bill_error character varying(4000)
);




COMMENT ON COLUMN egpt_basic_property.old_propertyid IS 'This holds the old Property Id.';



CREATE TABLE egpt_bills (
    id bigint NOT NULL,
    id_basic_property bigint NOT NULL,
    pidno character varying(64),
    paybydate timestamp without time zone,
    currentdate timestamp without time zone,
    year character varying(10),
    dcno character varying(24),
    demandno character varying(24),
    billparticulars character varying(512),
    propertyno character varying(64),
    nameandaddress character varying(1024),
    addldemand bigint,
    transfertax bigint,
    arrpttax bigint,
    propertytax bigint,
    arrpenaltytax bigint,
    penaltytax bigint,
    pttotal bigint,
    penaltyaddldemand bigint,
    penaltytotal bigint,
    transferaddldemand bigint,
    transfertotal bigint,
    rateablevalue bigint,
    arrtransfertax bigint,
    billno bigint NOT NULL,
    noticebillno bigint,
    create_timestamp timestamp without time zone NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar,
    arv_date timestamp without time zone,
    userid bigint NOT NULL,
    ownername character varying(1024),
    address character varying(1024),
    govtpenaltytax bigint,
    govtarrpenaltytax bigint,
    govtpenaltytotal bigint,
    govtnoticefee bigint,
    govtarrnoticefee bigint,
    govtnoticefeetotal bigint,
    pendingnotice72no character varying(128),
    totaldemand bigint,
    pronoticefee bigint,
    bankcharges bigint,
    arrearpronoticefee bigint,
    arrearbankcharges bigint,
    grandtotal bigint,
    seatno character varying(15)
);




CREATE TABLE egpt_bndry_category (
    id_bndry_category bigint NOT NULL,
    id_bndry bigint NOT NULL,
    id_category bigint NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    from_date timestamp without time zone,
    to_date timestamp without time zone
);




CREATE TABLE egpt_category (
    id_category bigint NOT NULL,
    category_name character varying(32) NOT NULL,
    category_amnt double precision NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_installment bigint,
    is_history character(1),
    userid bigint,
    id_usage bigint,
    from_date timestamp without time zone,
    to_date timestamp without time zone
);




CREATE TABLE egpt_citizen_address (
    id_address bigint NOT NULL,
    id_citizen bigint NOT NULL
);




CREATE TABLE egpt_collection_report (
    id bigint NOT NULL,
    boundary_id bigint NOT NULL,
    month bigint NOT NULL,
    target_amount_month double precision DEFAULT 0 NOT NULL,
    target_amount_year double precision DEFAULT 0 NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    report_type character varying(10) NOT NULL,
    period character varying(7)
);




CREATE TABLE egpt_constr_type (
    id_constr_type bigint,
    constr_code character varying(256),
    constr_type character varying(256),
    constr_name character varying(256),
    lastupdatedtimestamp timestamp without time zone NOT NULL
);




CREATE TABLE egpt_demandcalculations (
    id bigint NOT NULL,
    id_demand bigint NOT NULL,
    propertytax double precision,
    rate_of_tax double precision,
    current_interest double precision,
    arrear_interest double precision,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    createtimestamp timestamp without time zone
);




CREATE TABLE egpt_demandreason (
    id bigint NOT NULL,
    pct double precision NOT NULL,
    reason character varying(64) NOT NULL,
    id_module bigint NOT NULL,
    ishistory character(1) NOT NULL,
    userid bigint,
    reasonmaster character varying(64) NOT NULL,
    todate timestamp without time zone NOT NULL,
    fromdate timestamp without time zone NOT NULL,
    id_acchead bigint NOT NULL,
    iscredit character(1) NOT NULL,
    flatorbasis character varying(32),
    pct_basis bigint,
    amount bigint,
    id_installment bigint NOT NULL
);




CREATE TABLE egpt_depreciation (
    id_depreciation bigint NOT NULL,
    depreciation_rate bigint NOT NULL,
    no_of_years bigint NOT NULL
);




CREATE TABLE egpt_floor_detail (
    id_property_detail bigint NOT NULL,
    id_floor_detail bigint NOT NULL,
    extra_field1 character varying(256),
    floor_no bigint NOT NULL,
    crt_timestamp timestamp without time zone NOT NULL,
    year_of_constr bigint,
    floor_area double precision,
    builtup_area double precision,
    id_featurelist bigint,
    id_struc_cl bigint,
    id_occpn_mstr bigint,
    id_usg_mstr bigint,
    water_meter_num character varying(32),
    elec_meter_num character varying(32),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_rebatemaster bigint,
    id_constr_attr bigint,
    rentper_month character varying(256),
    extra_field2 character varying(256),
    extra_field3 character varying(256)
);




CREATE TABLE egpt_floordemandcalc (
    id bigint NOT NULL,
    id_floordet bigint,
    id_dmdcalc bigint,
    categoryamt double precision,
    occupancyrebate double precision,
    constructionrebate double precision,
    depreciation_value double precision,
    usagerebate double precision,
    createtimestamp timestamp without time zone,
    lastupdatedtimestamp timestamp without time zone
);




CREATE TABLE egpt_mstr_property_group (
    id bigint NOT NULL,
    group_name character varying(255) NOT NULL,
    code character varying(255),
    order_no bigint,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint,
    modified_by bigint
);




CREATE TABLE egpt_mstr_property_subgroup (
    id bigint NOT NULL,
    id_mstr_property_group bigint NOT NULL,
    subgroup_name character varying(255) NOT NULL,
    code character varying(255),
    order_no bigint,
    address1 character varying(255),
    address2 character varying(255),
    pincode integer,
    email character varying(50),
    contact_no bigint,
    mobile_no bigint,
    fax bigint,
    contact_person_name character varying(255),
    contact_person_desg character varying(255),
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone,
    created_by bigint,
    modified_by bigint
);




CREATE TABLE egpt_mutation_master (
    id_mutation bigint NOT NULL,
    mutation_name character varying(256),
    mutation_desc character varying(256),
    type character varying(16),
    code character varying(16),
    order_id bigint
);




CREATE TABLE egpt_mutation_owner (
    id_mutation_owner bigint NOT NULL,
    id_prop_mutation bigint NOT NULL,
    ownerid bigint NOT NULL
);




CREATE TABLE egpt_property (
    id_property bigint NOT NULL,
    crt_timestamp timestamp without time zone NOT NULL,
    updt_timestamp timestamp without time zone NOT NULL,
    userid bigint NOT NULL,
    id_source bigint NOT NULL,
    extra_field1 character varying(256),
    extra_field2 character varying(256),
    extra_field3 character varying(256),
    id_basic_property bigint NOT NULL,
    is_default_property character(1) DEFAULT 'N'::bpchar NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar,
    latest_node bigint,
    effective_date timestamp without time zone NOT NULL,
    ischecked character(1) DEFAULT 'N'::bpchar,
    remarks character varying(1048),
    id_modify_reason bigint,
    id_installment bigint
);




CREATE MATERIALIZED VIEW egpt_mv_current_property AS
 SELECT bp.id_basic_property,
    prop.id_property
   FROM egpt_basic_property bp,
    egpt_property prop
  WHERE ((((prop.id_basic_property = bp.id_basic_property) AND (prop.id_installment = ( SELECT eg_installment_master.id_installment
           FROM eg_installment_master
          WHERE (((eg_installment_master.start_date <= ('now'::text)::timestamp without time zone) AND (eg_installment_master.end_date >= ('now'::text)::timestamp without time zone)) AND (eg_installment_master.id_module = ( SELECT eg_module.id_module
                   FROM eg_module
                  WHERE ((eg_module.module_name)::text = 'Property Tax'::text))))))) AND (prop.is_default_property = 'Y'::bpchar)) AND (prop.is_history = 'N'::bpchar))
  WITH NO DATA;




CREATE TABLE egpt_ptdemand (
    id_demand bigint NOT NULL,
    id_property bigint NOT NULL
);




CREATE MATERIALIZED VIEW egpt_mv_bp_curr_demand AS
 SELECT bp.id_basic_property,
    dem.id
   FROM egpt_basic_property bp,
    egpt_mv_current_property prop,
    eg_demand dem,
    egpt_ptdemand ptdem
  WHERE (((((prop.id_basic_property = bp.id_basic_property) AND (dem.is_history = 'N'::bpchar)) AND (dem.id_installment = ( SELECT eg_installment_master.id_installment
           FROM eg_installment_master
          WHERE (((eg_installment_master.start_date <= ('now'::text)::timestamp without time zone) AND (eg_installment_master.end_date >= ('now'::text)::timestamp without time zone)) AND (eg_installment_master.id_module = ( SELECT eg_module.id_module
                   FROM eg_module
                  WHERE ((eg_module.module_name)::text = 'Property Tax'::text))))))) AND (ptdem.id_demand = dem.id)) AND (ptdem.id_property = prop.id_property))
  WITH NO DATA;




CREATE MATERIALIZED VIEW egpt_mv_arrear_dem_coll AS
 SELECT dem.id_basic_property,
    sum(det.amount) AS amt_demand,
    sum(det.amt_collected) AS amt_collected
   FROM egpt_mv_bp_curr_demand dem,
    eg_demand_details det
  WHERE ((det.id_demand = dem.id) AND (det.id_demand_reason IN ( SELECT demand_reason.id
           FROM eg_demand_reason demand_reason
          WHERE (demand_reason.id_installment <> ( SELECT eg_installment_master.id_installment
                   FROM eg_installment_master
                  WHERE (((eg_installment_master.start_date <= ('now'::text)::timestamp without time zone) AND (eg_installment_master.end_date >= ('now'::text)::timestamp without time zone)) AND (eg_installment_master.id_module = ( SELECT eg_module.id_module
                           FROM eg_module
                          WHERE ((eg_module.module_name)::text = 'Property Tax'::text)))))))))
  GROUP BY dem.id_basic_property
  WITH NO DATA;




CREATE MATERIALIZED VIEW egpt_mv_bp_address AS
 SELECT bp.id_basic_property,
    addr.addressid,
    addr.houseno,
    propadd.subnumber,
    (((((((((
        CASE
            WHEN ((addr.houseno IS NOT NULL) AND ((addr.houseno)::text <> ''::text)) THEN addr.houseno
            ELSE ''::character varying
        END)::text ||
        CASE
            WHEN ((propadd.subnumber IS NOT NULL) AND ((propadd.subnumber)::text <> ''::text)) THEN ('/'::text || (propadd.subnumber)::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((propadd.doornumold IS NOT NULL) AND ((propadd.doornumold)::text <> ''::text)) THEN (('('::text || (propadd.doornumold)::text) || ')'::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((addr.streetaddress1 IS NOT NULL) AND ((addr.streetaddress1)::text <> ''::text)) THEN (', '::text || (addr.streetaddress1)::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((addr.streetaddress2 IS NOT NULL) AND ((addr.streetaddress2)::text <> ''::text)) THEN (', '::text || (addr.streetaddress2)::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((addr.locality IS NOT NULL) AND ((addr.locality)::text <> ''::text)) THEN (', '::text || (addr.locality)::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((addr.block IS NOT NULL) AND ((addr.block)::text <> ''::text)) THEN (', '::text || (addr.block)::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((addr.citytownvillage IS NOT NULL) AND ((addr.citytownvillage)::text <> ''::text)) THEN (', '::text || (addr.citytownvillage)::text)
            ELSE ''::text
        END) ||
        CASE
            WHEN ((addr.pincode IS NOT NULL) AND ((addr.pincode)::text <> ''::text)) THEN (' '::text || addr.pincode)
            ELSE ''::text
        END) AS address
   FROM egpt_basic_property bp,
    eg_address addr,
    egpt_address propadd
  WHERE ((addr.addressid = bp.addressid) AND (addr.addressid = propadd.id_address))
  WITH NO DATA;




CREATE MATERIALIZED VIEW egpt_mv_bp_curr_demand_av AS
 SELECT dem.id_basic_property,
    dem.id,
    attrv.att_value
   FROM egpt_mv_bp_curr_demand dem,
    eg_attributevalues attrv,
    eg_attributetype attrtype,
    egpt_demandcalculations demandcalc
  WHERE ((((dem.id = demandcalc.id_demand) AND (demandcalc.id = attrv.domaintxnid)) AND (attrv.att_typeid = attrtype.id)) AND ((attrtype.att_name)::text = 'ANNUALVALUE'::text))
  WITH NO DATA;




CREATE MATERIALIZED VIEW egpt_mv_current_dem_coll AS
 SELECT dem.id_basic_property,
    sum(det.amount) AS amt_demand,
    sum(det.amt_collected) AS amt_collected
   FROM egpt_mv_bp_curr_demand dem,
    eg_demand_details det
  WHERE ((det.id_demand = dem.id) AND (det.id_demand_reason IN ( SELECT demand_reason.id
           FROM eg_demand_reason demand_reason
          WHERE (demand_reason.id_installment = ( SELECT eg_installment_master.id_installment
                   FROM eg_installment_master
                  WHERE (((eg_installment_master.start_date <= ('now'::text)::timestamp without time zone) AND (eg_installment_master.end_date >= ('now'::text)::timestamp without time zone)) AND (eg_installment_master.id_module = ( SELECT eg_module.id_module
                           FROM eg_module
                          WHERE ((eg_module.module_name)::text = 'Property Tax'::text)))))))))
  GROUP BY dem.id_basic_property
  WITH NO DATA;




CREATE TABLE egpt_property_detail (
    id_property_detail bigint NOT NULL,
    id_property bigint NOT NULL,
    property_length double precision,
    property_breadth double precision,
    sital_area double precision,
    is_irregular character(1) DEFAULT 'N'::bpchar,
    plinth_area double precision,
    total_builtup_area double precision,
    commn_builtup_area double precision,
    commn_vcnt_land double precision,
    num_floors bigint,
    id_usg_mstr bigint,
    water_meter_num character varying(32),
    elec_meter_num character varying(32),
    survey_num character varying(64),
    is_field_verified character(1) DEFAULT 'N'::bpchar NOT NULL,
    field_verif_date timestamp without time zone,
    property_type character varying(64) NOT NULL,
    id_installment bigint,
    completion_year timestamp without time zone,
    effective_date timestamp without time zone,
    id_propertytypemaster bigint,
    id_mutation bigint NOT NULL,
    updt_timestamp timestamp without time zone NOT NULL,
    refnum character varying(16),
    refdate timestamp without time zone,
    remarks character varying(32),
    extra_field1 character varying(256),
    extra_field2 character varying(256),
    extra_field3 character varying(256),
    is_comzone character(1),
    is_cornerplot character(1),
    extra_field4 character varying(4000),
    extra_field5 character varying(256),
    extra_field6 character varying(256),
    dateofcompletion timestamp without time zone
);




CREATE MATERIALIZED VIEW egpt_mv_current_prop_det AS
 SELECT prop.id_property,
    propdet.id_propertytypemaster,
    propdet.id_usg_mstr,
    propdet.sital_area,
    propdet.total_builtup_area
   FROM egpt_mv_current_property prop,
    egpt_property_detail propdet
  WHERE (prop.id_property = propdet.id_property)
  WITH NO DATA;




CREATE TABLE egpt_notice100 (
    id bigint NOT NULL,
    notice100_nuber character varying(16),
    id_basic_property bigint NOT NULL,
    pid character varying(16),
    seat_no character varying(16),
    dcr character varying(32),
    property_address character varying(512),
    owner_address character varying(512),
    owner_name character varying(256),
    status character(1),
    userid bigint,
    updated_timestamp timestamp without time zone,
    created_timestamp timestamp without time zone,
    notice100_iss_date timestamp without time zone,
    id_installment bigint NOT NULL,
    balance bigint
);




CREATE TABLE egpt_notice72 (
    id_notice72 bigint NOT NULL,
    por_no character varying(16),
    id_basic_property bigint,
    notice_iss_date timestamp without time zone,
    owner character varying(256),
    address character varying(256),
    year bigint,
    curr_arv bigint,
    currarv_remarks character varying(512),
    proposed_arv bigint,
    proparv_remarks character varying(512),
    status character(1) DEFAULT 'N'::bpchar,
    userid bigint NOT NULL,
    purch_name_addr character varying(256),
    createtimestamp timestamp without time zone,
    lastupdatedtimestamp timestamp without time zone,
    modified_by bigint
);




CREATE TABLE egpt_notice_generation (
    id bigint,
    notice_type character varying(16),
    id_bndry bigint,
    id_installment bigint,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone
);




CREATE TABLE egpt_occupation_type_master (
    id_occpn_mstr bigint NOT NULL,
    occupation character varying(256) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    occupany_factor double precision,
    occupation_local character varying(768),
    code character varying(10),
    from_date timestamp without time zone,
    to_date timestamp without time zone,
    id_usg_mstr bigint
);




CREATE SEQUENCE egpt_private_prop
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egpt_prop_corr_details (
    id bigint NOT NULL,
    id_prop_correction bigint,
    id_installment bigint,
    tax_type character varying(32),
    old_tax double precision,
    new_tax double precision,
    old_coll double precision,
    new_coll double precision,
    old_value character varying(128),
    new_value character varying(128)
);




CREATE TABLE egpt_prop_modify_reason (
    id_reason bigint NOT NULL,
    reason_name character varying(64) NOT NULL,
    lastupdatedtimestamp timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone
);




CREATE TABLE egpt_prop_ref (
    id_propref bigint NOT NULL,
    file_no character varying(256),
    file_date timestamp without time zone,
    survey_no character varying(256),
    id_basic_property bigint,
    is_history character(1) DEFAULT 'N'::bpchar
);




CREATE TABLE egpt_property_corrections (
    id bigint NOT NULL,
    id_basic_property bigint,
    corr_type character varying(64),
    create_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    reason character varying(128),
    remarks character varying(1024)
);




CREATE TABLE egpt_property_effective_period (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    created_by bigint,
    modified_by bigint,
    id_basic_property bigint NOT NULL,
    old_effective_date bigint NOT NULL,
    new_effective_date bigint NOT NULL,
    remarks character varying(256) NOT NULL,
    is_active character(1)
);




CREATE TABLE egpt_property_integration (
    id bigint NOT NULL,
    bill_no character varying(20) NOT NULL,
    operation character varying(20) NOT NULL,
    is_sync character(1) DEFAULT 'N'::bpchar NOT NULL,
    create_timestamp timestamp without time zone NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    error character varying(256)
);




CREATE TABLE egpt_property_master_docs (
    id_prop_doc bigint NOT NULL,
    crt_timestamp timestamp without time zone NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    doc_number bigint,
    status_id bigint,
    id_basic_property bigint
);




CREATE TABLE egpt_property_mutation (
    id_prop_mutation bigint NOT NULL,
    id_mutation bigint NOT NULL,
    mutation_no character varying(60),
    mutation_date timestamp without time zone,
    ref_pid character varying(64),
    id_basic_property bigint NOT NULL,
    extra_field1 character varying(256),
    extra_field2 character varying(256),
    createtimestamp timestamp without time zone,
    lastupdatedtimestamp timestamp without time zone,
    userid bigint,
    consfortransfee bigint,
    is_tfpayable character(1),
    notice_date timestamp without time zone,
    extra_field3 character varying(256),
    application_no character varying(60),
    id_appln_status bigint,
    deed_no character varying(64),
    deed_date timestamp without time zone
);




CREATE TABLE egpt_property_mutation_docs (
    id_prop_doc bigint NOT NULL,
    id_prop_mutation bigint NOT NULL
);




CREATE TABLE egpt_property_owner (
    ownerid bigint NOT NULL,
    id_property bigint NOT NULL,
    id_source bigint
);




CREATE TABLE egpt_property_status_values (
    id_status bigint NOT NULL,
    id_basic_property bigint NOT NULL,
    ref_date timestamp without time zone,
    ref_num character varying(64),
    remarks character varying(1024),
    is_active character(1),
    id_status_values bigint NOT NULL,
    lastupdatedtimestamp timestamp without time zone,
    createdtimestamp timestamp without time zone,
    extra_field1 character varying(64),
    extra_field2 character varying(128),
    extra_field3 character varying(64),
    user_id bigint NOT NULL,
    ref_id_basic bigint,
    commitee_appr_no character varying(256),
    commitee_appr_date timestamp without time zone,
    bpa_no character varying(32),
    bpa_date timestamp without time zone
);




CREATE TABLE egpt_property_tenants (
    id_property bigint NOT NULL,
    citizenid bigint NOT NULL
);




CREATE TABLE egpt_property_type_master (
    id_propertytypemaster bigint NOT NULL,
    property_type character varying(256) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    type_factor bigint NOT NULL,
    property_type_local character varying(768),
    code character varying(32)
);




CREATE TABLE egpt_property_usage_master (
    id_usg_mstr bigint NOT NULL,
    usg_name character varying(256) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    usage_factor bigint NOT NULL,
    usg_name_local character varying(768),
    code character varying(16) NOT NULL,
    order_id bigint,
    from_date timestamp without time zone NOT NULL,
    to_date timestamp without time zone,
    is_enabled bigint DEFAULT 1 NOT NULL
);




CREATE TABLE egpt_propertyid (
    zone_num bigint,
    ward_adm_id bigint,
    colony_num bigint,
    block_adm_id bigint,
    street_adm_id bigint,
    door_num character varying(50),
    assmt_unit bigint,
    adm1 bigint,
    adm2 bigint,
    adm3 bigint,
    crt_timestamp timestamp without time zone NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id bigint NOT NULL,
    front_bndry_street bigint,
    back_bndry_street bigint,
    left_bndry_street bigint,
    right_bndry_street bigint,
    bndry_street_dmdcalc bigint,
    north_bounded character varying(256),
    south_bounded character varying(256),
    east_bounded character varying(256),
    west_bounded character varying(256)
);




CREATE TABLE egpt_ptdemand_arv (
    id bigint,
    id_property bigint,
    arv bigint,
    arv_type character varying(45),
    createtimestamp timestamp without time zone,
    lastupdatedtimestamp timestamp without time zone,
    reasonid bigint,
    section72no character varying(128),
    aonumber character varying(256),
    aodate timestamp without time zone,
    amlgamation_pid character varying(64),
    fromdate timestamp without time zone,
    todate timestamp without time zone,
    is_history character(1) DEFAULT 'N'::bpchar,
    netrateoftax character varying(16),
    userid bigint NOT NULL,
    notice72date timestamp without time zone
);




CREATE TABLE egpt_ptdemanddetails (
    id bigint NOT NULL,
    id_ptdemand bigint NOT NULL,
    account_head_id bigint,
    amount bigint NOT NULL,
    remarks character varying(512),
    extrafield1 timestamp without time zone,
    reason_id bigint,
    createtimestamp timestamp without time zone,
    lastupdatedtimestamp timestamp without time zone,
    docrefno character varying(256),
    docrefdate timestamp without time zone,
    isapproved character(1),
    extrafield2 timestamp without time zone,
    extrafield3 character varying(34),
    totaldemand bigint,
    pendingnotice72no character varying(32)
);




CREATE TABLE egpt_rcpt_rectification (
    id bigint NOT NULL,
    propertyid character varying(20),
    adjustment_pid character varying(20),
    receipt_no character varying(50),
    rectification_rsn character varying(30),
    approval_no bigint,
    approval_date timestamp without time zone,
    remarks character varying(256),
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint,
    state_id bigint
);




CREATE TABLE egpt_rcpt_rectify_details (
    id bigint NOT NULL,
    id_rcpt_rectify bigint,
    id_installment bigint,
    tax_coll double precision,
    penalty_coll double precision
);




CREATE TABLE egpt_register (
    id_register bigint NOT NULL,
    register_number character varying(32),
    register_name character varying(32),
    is_active character varying(1),
    type bigint
);




CREATE TABLE egpt_src_of_info (
    id_source bigint NOT NULL,
    source_name character varying(256) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    source_name_local character varying(768),
    src_sht_name character varying(32)
);




CREATE TABLE egpt_status (
    id_status bigint NOT NULL,
    status_name character varying(256) NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    is_active character(1),
    code character varying(32)
);




CREATE TABLE egpt_struc_cl (
    id_struc_cl bigint NOT NULL,
    constr_num bigint NOT NULL,
    constr_type character varying(128) NOT NULL,
    constr_descr character varying(128),
    constr_factor double precision,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_installment bigint NOT NULL,
    is_history character(1) DEFAULT 'N'::bpchar,
    userid bigint,
    floor_num bigint,
    code character varying(16) NOT NULL,
    order_id bigint,
    from_date timestamp without time zone NOT NULL,
    to_date timestamp without time zone
);




CREATE TABLE egpt_tax_perc (
    id bigint NOT NULL,
    id_category bigint NOT NULL,
    id_usg_mstr bigint NOT NULL,
    tax_perc double precision NOT NULL,
    from_amt bigint,
    to_amt bigint,
    from_date timestamp without time zone,
    to_date timestamp without time zone
);




CREATE TABLE egpt_taxrates (
    id bigint NOT NULL,
    from_date timestamp without time zone NOT NULL,
    to_date timestamp without time zone NOT NULL,
    type bigint NOT NULL,
    tax bigint NOT NULL,
    from_amount bigint NOT NULL,
    to_amount bigint NOT NULL
);




CREATE TABLE egpt_tx_agent (
    id bigint NOT NULL,
    updatedtime timestamp without time zone NOT NULL,
    id_basicproperty bigint NOT NULL
);




CREATE TABLE egpt_usage_rate (
    id_usage_rate bigint NOT NULL,
    percentage double precision NOT NULL
);




CREATE TABLE egpt_wfactivateprop (
    id bigint NOT NULL,
    state_id bigint,
    propertyid character varying(64),
    order_num character varying(64),
    order_date character varying(10),
    comments character varying(512),
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone
);




CREATE TABLE egpt_wfcitizen (
    id bigint NOT NULL,
    ssn character varying(512),
    pannumber character varying(256),
    passportnumber character varying(256),
    drivinglicencenumber character varying(256),
    rationcardnumber character varying(256),
    voterregistrationnumber character varying(256),
    firstname character varying(512) NOT NULL,
    middlename character varying(512),
    lastname character varying(512),
    birthdate timestamp without time zone,
    homephone character varying(32),
    officephone character varying(32),
    mobilephone character varying(32),
    fax character varying(32),
    emailaddress character varying(64),
    occupation character varying(256),
    jobstatus character varying(128),
    locale character varying(256),
    firstnamelocal character varying(512),
    lastnamelocal character varying(512),
    ownertitle character varying(64),
    ownertitlelocal character varying(64),
    sex character varying(16),
    middlenamelocal character varying(512),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    fathername character varying(256),
    creationtimestamp timestamp without time zone,
    mnicnumber character varying(32)
);




CREATE TABLE egpt_wfcorrectprop_particular (
    id bigint NOT NULL,
    state_id bigint,
    propertyid character varying(64),
    order_num character varying(64),
    order_date character varying(10),
    doornum_new character varying(32),
    doornum_old character varying(32),
    door_subnum character varying(32),
    street_address1 character varying(100),
    pincode bigint,
    corr_address1 character varying(150),
    corr_address2 character varying(150),
    corr_pincode bigint,
    new_mobileno character varying(10),
    new_email_addr character varying(64),
    comments character varying(512),
    modify_readon character varying(15) NOT NULL,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    ser_req_ackno character varying(128),
    creation_type character varying(25)
);




CREATE TABLE egpt_wfmodfloor_detail (
    id bigint NOT NULL,
    id_wfmodpropid bigint NOT NULL,
    floor_no bigint NOT NULL,
    crt_timestamp timestamp without time zone NOT NULL,
    year_of_constr bigint,
    floor_area double precision,
    builtup_area double precision,
    id_featurelist bigint,
    id_struc_cl bigint,
    id_occpn_mstr bigint,
    id_usg_mstr bigint,
    water_meter_num character varying(32),
    elec_meter_num character varying(32),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_rebatemaster bigint,
    id_constr_attr bigint,
    rentper_month character varying(256),
    extra_field1 character varying(256),
    extra_field2 character varying(256),
    extra_field3 character varying(256)
);




CREATE TABLE egpt_wfmodifyprop (
    id bigint NOT NULL,
    propertyid character varying(32),
    modifyreason character varying(16),
    mod_effective_date timestamp without time zone,
    notice_type character varying(16),
    court_order_no character varying(32),
    court_order_date timestamp without time zone,
    av_amount character varying(16),
    is_manual_override character(1),
    typeofbuilding character varying(32),
    areaofplot double precision,
    percoflandownership double precision,
    ownerofland character varying(32),
    land_owner_name character varying(256),
    state_id bigint,
    adm_bndry bigint,
    createdby bigint,
    lastmodifiedby bigint,
    createtimestamp timestamp without time zone,
    lastupdatedtimestamp timestamp without time zone,
    ramarks character varying(512),
    total_amount character varying(16),
    dateofcompletion timestamp without time zone,
    completerate character varying(4000),
    manual_av double precision,
    manual_tax double precision,
    half_year_tax double precision,
    is_submit_tocommitee character(1) DEFAULT 'N'::bpchar NOT NULL,
    commitee_appr_no character varying(256),
    commitee_appr_date timestamp without time zone,
    is_committee_apprd character(1),
    bpa_no character varying(32),
    bpa_date timestamp without time zone,
    id_property_group bigint,
    id_property_subgroup bigint,
    ser_req_ackno character varying(128),
    applicant_name character varying(512),
    application_mode character varying(16),
    status_id bigint
);




CREATE TABLE egpt_wfmutation (
    id bigint NOT NULL,
    propertyid character varying(32),
    application_no character varying(64),
    application_date timestamp without time zone,
    deed_no character varying(64),
    deed_date timestamp without time zone,
    id_mutation bigint,
    mobileno character varying(12),
    email_id character varying(128),
    remarks character varying(512),
    created_date timestamp without time zone,
    updatedtimestamp timestamp without time zone,
    state_id bigint,
    created_by bigint,
    adm_bndry bigint,
    modified_by bigint,
    modified_date timestamp without time zone,
    ser_req_ackno character varying(128),
    applicant_name character varying(512),
    application_mode character varying(16)
);




CREATE TABLE egpt_wfmutation_owner (
    ownerid bigint,
    id_wfmutation bigint
);




CREATE TABLE egpt_wfproperty (
    id bigint NOT NULL,
    creationreason character varying(10) NOT NULL,
    oldpropbillnos character varying(256),
    zone bigint NOT NULL,
    divnum bigint NOT NULL,
    mobileno character varying(10),
    email character varying(64),
    area bigint NOT NULL,
    locality bigint NOT NULL,
    streetname bigint NOT NULL,
    doornumnew character varying(32) NOT NULL,
    subnum character varying(32),
    doornumold character varying(32),
    address1 character varying(100),
    pincode bigint NOT NULL,
    corraddress1 character varying(150),
    corraddress2 character varying(150),
    corrpincode bigint,
    typeofbuilding character varying(50) NOT NULL,
    areaofplot double precision,
    percoflandownership double precision,
    ownerofland character varying(25),
    dateofcompletion timestamp without time zone NOT NULL,
    createtimestamp timestamp without time zone NOT NULL,
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint,
    remarks character varying(512),
    north_boundary character varying(256),
    south_boundary character varying(256),
    east_boundary character varying(256),
    west_boundary character varying(256),
    bndry_street_dmdcalc character varying(256),
    right_bndry_street bigint,
    back_bndry_street bigint,
    left_bndry_street bigint,
    street_ward bigint,
    right_street_ward bigint,
    back_street_ward bigint,
    left_street_ward bigint,
    propertyid character varying(64),
    is_corradd_diff character varying(10) DEFAULT 0,
    land_owner_name character varying(255),
    completerate character varying(4000),
    manual_av double precision,
    manual_tax double precision,
    old_propertyid character varying(24),
    half_year_tax double precision,
    is_submit_tocommitee character(1) DEFAULT 'N'::bpchar NOT NULL,
    commitee_appr_no character varying(256),
    commitee_appr_date timestamp without time zone,
    is_committee_apprd character(1),
    bpa_no character varying(32),
    bpa_date timestamp without time zone,
    id_property_group bigint,
    id_property_subgroup bigint,
    ser_req_ackno character varying(128),
    applicant_name character varying(512),
    document_number bigint,
    status_id bigint,
    creation_type character varying(25),
    calc_av double precision,
    assessment_no character varying(20),
    cancel_reason character varying(512),
    portal_reqno character varying(64)
);




CREATE TABLE egpt_wfproperty_owner (
    ownerid bigint,
    id_wfproperty bigint
);




CREATE TABLE egptwf_floor_detail (
    id bigint NOT NULL,
    id_property_detail bigint NOT NULL,
    extra_field1 character varying(256),
    floor_no bigint NOT NULL,
    crt_timestamp timestamp without time zone NOT NULL,
    year_of_constr bigint,
    floor_area double precision,
    builtup_area double precision,
    id_featurelist bigint,
    id_struc_cl bigint,
    id_occpn_mstr bigint,
    id_usg_mstr bigint,
    water_meter_num character varying(32),
    elec_meter_num character varying(32),
    lastupdatedtimestamp timestamp without time zone NOT NULL,
    id_rebatemaster bigint,
    id_constr_attr bigint,
    rentper_month character varying(256),
    extra_field2 character varying(256),
    extra_field3 character varying(256)
);




CREATE TABLE egstores_advancerequisition (
    id bigint NOT NULL,
    purchaseorderid bigint NOT NULL,
    advancesettlement bigint
);




CREATE TABLE egstores_capitalissue (
    id bigint NOT NULL,
    mrinheaderid bigint NOT NULL,
    projectcodeid bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    assetid bigint
);




CREATE TABLE egstores_indent_quotation (
    id bigint NOT NULL,
    processindentlineid bigint NOT NULL,
    supplierid bigint NOT NULL,
    unitprice bigint DEFAULT 0 NOT NULL
);




CREATE TABLE egstores_issuewriteoffscrap (
    id bigint NOT NULL,
    mrinlineid bigint NOT NULL,
    scrapvalue bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    scrapitem bigint NOT NULL
);




CREATE TABLE egstores_itemdeptdetails (
    id bigint NOT NULL,
    itemid bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    deptid bigint NOT NULL
);




CREATE TABLE egstores_itemtypedetails (
    id bigint NOT NULL,
    deptid bigint NOT NULL,
    storeid bigint,
    itemtypeid bigint NOT NULL,
    stockinhandcode bigint NOT NULL,
    purchaseaccount bigint,
    expenseaccount bigint,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL
);




CREATE TABLE egstores_mrnpodetails (
    id bigint NOT NULL,
    polineid bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    mrnlineid bigint NOT NULL
);




CREATE TABLE egstores_poheader (
    id bigint NOT NULL,
    orderdate timestamp without time zone NOT NULL,
    pono character varying(100) NOT NULL,
    totalamount bigint,
    quotationrefno character varying(120),
    quotationdate timestamp without time zone,
    created timestamp without time zone NOT NULL,
    lastmodified timestamp without time zone NOT NULL,
    modifiedby bigint,
    createdby bigint NOT NULL,
    authorizedby bigint,
    relationid bigint NOT NULL,
    statusid bigint NOT NULL,
    potype character(1) NOT NULL,
    inventorytype character(1) NOT NULL,
    purchasedept bigint NOT NULL,
    storeid bigint,
    deliverydept bigint NOT NULL,
    deliverydate timestamp without time zone,
    narration character varying(250),
    fundid bigint NOT NULL,
    functionid bigint,
    functionaryid bigint,
    fundsourceid bigint,
    advanceamount bigint,
    retentionamount bigint,
    securitydeposit bigint,
    passedamount bigint,
    advadjustamount bigint,
    state_id bigint,
    advance_percentage bigint,
    approvedon timestamp without time zone,
    budgetapprnumber character varying(100)
);




CREATE TABLE egstores_poindentdetail (
    id bigint NOT NULL,
    polineid bigint NOT NULL,
    lastmodifieddate timestamp without time zone,
    mrlineid bigint NOT NULL
);




CREATE TABLE egstores_poline (
    id bigint NOT NULL,
    poheaderid bigint NOT NULL,
    lineno bigint NOT NULL,
    itemid bigint,
    qty bigint NOT NULL,
    uomid bigint NOT NULL,
    unitprice bigint NOT NULL,
    received_qty bigint,
    remarks character varying(250),
    lastmodified timestamp without time zone NOT NULL,
    termsofdelivery character varying(300),
    mrlineid bigint,
    taxamount bigint,
    assetcategoryid bigint,
    techspecification character varying(255),
    status bigint,
    ratecontractflag bigint
);




CREATE TABLE egstores_polineothers (
    id bigint NOT NULL,
    polineid bigint NOT NULL,
    lineno bigint NOT NULL,
    breakqty double precision NOT NULL,
    price bigint,
    balance bigint,
    lastmodifieddate timestamp without time zone NOT NULL
);




CREATE TABLE egstores_processindent (
    id bigint NOT NULL,
    departmentid bigint NOT NULL,
    storeid bigint NOT NULL,
    stateid bigint,
    statusid bigint NOT NULL,
    processindentnumber character varying(100) NOT NULL,
    processindentdate timestamp without time zone NOT NULL,
    totalamount bigint NOT NULL,
    fundid bigint,
    functionid bigint,
    remarks character varying(512),
    budgetusageid bigint,
    createdby bigint NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    budgetgroup_id bigint,
    tenderid bigint,
    quotedsupplierused bigint
);




CREATE TABLE egstores_processindent_mrline (
    id bigint NOT NULL,
    processindentlineid bigint NOT NULL,
    mrlineid bigint NOT NULL,
    splitquantity bigint
);




CREATE TABLE egstores_processindentline (
    id bigint NOT NULL,
    processindentid bigint NOT NULL,
    itemid bigint NOT NULL,
    uom bigint NOT NULL,
    baseunitprice bigint NOT NULL,
    orderquantity bigint NOT NULL
);




CREATE TABLE egstores_processindenttender (
    id bigint NOT NULL,
    tendernumber character varying(128),
    tenderdate timestamp without time zone NOT NULL,
    invitingdate timestamp without time zone,
    documentreleasedate timestamp without time zone,
    tenderopendate timestamp without time zone,
    evaluationdate timestamp without time zone,
    commercialevaluationdate timestamp without time zone,
    tenderfinalizeddate timestamp without time zone,
    modifiedby bigint
);




CREATE TABLE egstores_ratecontract (
    id bigint NOT NULL,
    ratecontractno character varying(100) NOT NULL,
    ratecontractdate timestamp without time zone NOT NULL,
    relationid bigint NOT NULL,
    narration character varying(512),
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    statusid bigint NOT NULL,
    state_id bigint
);




CREATE TABLE egstores_ratecontractdetails (
    id bigint NOT NULL,
    ratecontractid bigint NOT NULL,
    itemid bigint NOT NULL,
    uomid bigint NOT NULL,
    rate bigint NOT NULL,
    startdate timestamp without time zone NOT NULL,
    enddate timestamp without time zone NOT NULL,
    lastupdatedtime timestamp without time zone NOT NULL
);




CREATE TABLE egstores_rcontractslabdetails (
    id bigint NOT NULL,
    ratecontractdetailid bigint NOT NULL,
    fromquantity bigint NOT NULL,
    toquantity bigint,
    rate bigint NOT NULL,
    lastupdatedtime timestamp without time zone NOT NULL
);




CREATE TABLE egstores_rcreq_slabdetails (
    id bigint NOT NULL,
    rcrequisitiondetailid bigint NOT NULL,
    fromquantity bigint NOT NULL,
    toquantity bigint,
    rate bigint NOT NULL,
    lastupdatedtime timestamp without time zone NOT NULL
);




CREATE TABLE egstores_rcrequisition (
    id bigint NOT NULL,
    requisitionno character varying(100) NOT NULL,
    requisitiondate timestamp without time zone NOT NULL,
    narration character varying(512),
    createdby bigint NOT NULL,
    lastmodifiedby bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    department bigint NOT NULL,
    statusid bigint NOT NULL,
    stateid bigint
);




CREATE TABLE egstores_rcrequisition_details (
    id bigint NOT NULL,
    rcrequisitionid bigint NOT NULL,
    itemid bigint NOT NULL,
    uomid bigint NOT NULL,
    rate bigint NOT NULL,
    startdate timestamp without time zone NOT NULL,
    enddate timestamp without time zone NOT NULL,
    lastupdatedtime timestamp without time zone NOT NULL
);




CREATE TABLE egstores_txntypes (
    id bigint NOT NULL,
    type character varying(50) NOT NULL,
    code character varying(50) NOT NULL,
    description character varying(256) NOT NULL,
    module character varying(50),
    acccodepurposeid bigint,
    isactive bigint DEFAULT 1,
    budgetcheckreq bigint DEFAULT 1
);




CREATE TABLE egtl_demand (
    license_id bigint NOT NULL,
    demand_id bigint NOT NULL,
    area_weight_fee bigint DEFAULT 0,
    erection_fee bigint DEFAULT 0,
    using_fee bigint DEFAULT 0,
    conservancy_fee bigint DEFAULT 0,
    pfa_fee bigint DEFAULT 0
);




CREATE TABLE egtl_department (
    id bigint NOT NULL,
    dept_name character varying(1024) NOT NULL
);




CREATE TABLE egtl_duplicate_license_info (
    id bigint NOT NULL,
    receipt_number character varying(1024) NOT NULL,
    receipt_date timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    license_id bigint
);




CREATE TABLE egtl_history_trade_area (
    id bigint NOT NULL,
    weight_or_area bigint,
    trade_id bigint,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    area_weight_fee bigint,
    using_fee bigint,
    conservancy_fee bigint
);




CREATE TABLE egtl_installed_motor (
    id bigint NOT NULL,
    trade_id bigint NOT NULL,
    motor_hp bigint NOT NULL,
    history smallint DEFAULT 0,
    no_of_machines bigint NOT NULL
);




CREATE TABLE egtl_license (
    id bigint NOT NULL,
    id_installment bigint NOT NULL,
    trade_id bigint NOT NULL,
    status smallint NOT NULL,
    license_number character varying(40),
    temp_license_number character varying(40),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    total_fee bigint DEFAULT 0,
    transfer_fee bigint DEFAULT 0,
    active smallint DEFAULT 0,
    old_lic_no character varying(40),
    is_manual_renewal character(1)
);




CREATE TABLE egtl_license_cancel_info (
    id bigint NOT NULL,
    reason_for_cancellation bigint NOT NULL,
    reference_number character varying(40),
    reference_date timestamp without time zone,
    reference_remarks character varying(1024),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    license_id bigint NOT NULL
);




CREATE TABLE egtl_license_objection_info (
    id bigint NOT NULL,
    reason_for_objection bigint NOT NULL,
    objection_details character varying(1024) NOT NULL,
    response_expected_date timestamp without time zone NOT NULL,
    aho_remarks character varying(1024) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    license_id bigint NOT NULL,
    notice_number character varying(1024),
    notice_date timestamp without time zone
);




CREATE TABLE egtl_license_revoke_info (
    id bigint NOT NULL,
    order_number character varying(40),
    order_date timestamp without time zone,
    revoke_remarks character varying(1024),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    license_id bigint NOT NULL
);




CREATE TABLE egtl_mstr_app_type (
    id bigint NOT NULL,
    license_application_type character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_mstr_area_licfee (
    id bigint NOT NULL,
    id_schedule bigint NOT NULL,
    id_trade_subctgr bigint NOT NULL,
    from_area bigint NOT NULL,
    to_area bigint NOT NULL,
    license_fee bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    effective_to timestamp without time zone,
    order_number character varying(256) NOT NULL,
    order_date timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_mstr_conserfee (
    id bigint NOT NULL,
    id_trade_subctgr bigint NOT NULL,
    id_license_type bigint NOT NULL,
    fee_charged character varying(40) NOT NULL,
    conservancy_fee double precision NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    effective_to timestamp without time zone,
    order_date timestamp without time zone NOT NULL,
    order_number character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_mstr_erection_fee (
    id bigint NOT NULL,
    motor_hp_from double precision NOT NULL,
    motor_hp_to double precision NOT NULL,
    erection_fee double precision NOT NULL,
    using_fee double precision NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    effective_to timestamp without time zone,
    order_number character varying(256),
    order_date timestamp without time zone,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    using_or_erection_flag character varying(100)
);




CREATE TABLE egtl_mstr_pfa_fee (
    id bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    id_application_type bigint NOT NULL,
    pfa_fee bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    effective_to timestamp without time zone,
    order_number character varying(256) NOT NULL,
    order_date timestamp without time zone NOT NULL
);




CREATE TABLE egtl_mstr_schedule (
    id bigint NOT NULL,
    schedule_code character varying(16) NOT NULL,
    schedule_name character varying(256),
    order_date timestamp without time zone,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_mstr_trade_category (
    id bigint NOT NULL,
    trade_category character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_mstr_trade_nature (
    id bigint NOT NULL,
    trade_nature character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_mstr_trade_sub_ctgr (
    id bigint NOT NULL,
    trade_name character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    id_schedule bigint,
    section_applicable character varying(256),
    pfa_applicable smallint DEFAULT 0,
    fee_based_on character varying(40) NOT NULL,
    trade_number character varying(32),
    id_trade_nature bigint,
    id_trade_department bigint,
    id_trade_category bigint,
    approval_required smallint DEFAULT 0
);




CREATE TABLE egtl_mstr_weight_licfee (
    id bigint NOT NULL,
    id_schedule bigint NOT NULL,
    id_trade_subctgr bigint NOT NULL,
    weight_from bigint NOT NULL,
    weight_to bigint NOT NULL,
    license_fee bigint NOT NULL,
    effective_from timestamp without time zone NOT NULL,
    effective_to timestamp without time zone,
    order_number character varying(256) NOT NULL,
    order_date timestamp without time zone NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egtl_name_transfer (
    id bigint NOT NULL,
    previous_owner_name character varying(256) NOT NULL,
    trade_id bigint NOT NULL,
    transfer_date timestamp without time zone NOT NULL,
    previous_owner_address character varying(1024) NOT NULL
);




CREATE TABLE egtl_renewal (
    id bigint NOT NULL,
    id_installment bigint NOT NULL,
    license_id bigint NOT NULL,
    paid_status smallint,
    total_fee bigint DEFAULT 0,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    renewed_date timestamp without time zone
);




CREATE TABLE egtl_trade (
    id bigint NOT NULL,
    applicant_name character varying(256) NOT NULL,
    father_name character varying(256) NOT NULL,
    establishment_date timestamp without time zone,
    applicant_address character varying(1024) NOT NULL,
    zone_id bigint NOT NULL,
    division_id bigint NOT NULL,
    area_id bigint NOT NULL,
    location_id bigint NOT NULL,
    street_id bigint NOT NULL,
    weight_or_area bigint NOT NULL,
    rent_paid bigint,
    remarks character varying(1024),
    field_inspection character varying(2048),
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL,
    trade_subctgr_id bigint NOT NULL,
    active smallint,
    id_parent bigint,
    name_transfer smallint,
    edit_detail smallint,
    state_id bigint,
    building_type character varying(128),
    building_name character varying(128) NOT NULL,
    property_no character varying(128),
    pincode bigint NOT NULL,
    establishment_name character varying(256),
    ptnan character varying(40),
    motor_installed smallint DEFAULT 0 NOT NULL,
    rental_agreemant smallint DEFAULT 0,
    application_date timestamp without time zone,
    motor_total_hp bigint DEFAULT 0,
    deactivate_reason character varying(1024),
    latest_name_transfer_id bigint,
    doc_no bigint,
    old_door_no character varying(128),
    mob_number bigint,
    alternate_number bigint,
    email character varying(64),
    aho_remarks character varying(2048),
    aee_remarks character varying(2048)
);




CREATE TABLE egtl_trade_nature (
    id bigint NOT NULL,
    trade_nature character varying(256) NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    createdby bigint NOT NULL,
    lastmodifiedby bigint NOT NULL
);




CREATE TABLE egw_abstractestimate (
    id bigint NOT NULL,
    estimatedate timestamp without time zone NOT NULL,
    name character varying(1024) NOT NULL,
    ward bigint,
    type bigint,
    description character varying(1024) NOT NULL,
    location character varying(250),
    category bigint,
    userdepartment bigint NOT NULL,
    executingdepartment bigint NOT NULL,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    value double precision,
    preparedby bigint NOT NULL,
    estimate_number character varying(256),
    parent_category bigint,
    projectcode_id bigint,
    budgetapprno character varying(256),
    state_id bigint,
    document_number bigint,
    resolution_number character varying(256),
    resolution_date timestamp without time zone,
    fundsourceid bigint NOT NULL,
    document text,
    budgetavailable double precision,
    budgetrejectionno character varying(256),
    despositcode_id bigint,
    status_id bigint,
    old_estimate_number character varying(64),
    old_ward bigint,
    old_exec_department bigint,
    parentid bigint,
    approveddate timestamp without time zone,
    is_copied_est character varying(1) DEFAULT 'N'::character varying,
    old_user_department bigint,
    lon bigint,
    lat bigint,
    old_user_dept_id bigint,
    budget_apprn_date timestamp without time zone,
    rate_contract_id bigint,
    application_request_id bigint
);




CREATE SEQUENCE egw_abstractestimate_seq
    START WITH 5
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_activity (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    scheduleofrate_id bigint,
    value double precision NOT NULL,
    quantity double precision NOT NULL,
    servicetaxperc double precision,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    nonsor_id bigint,
    activity_index bigint NOT NULL,
    uom_id bigint,
    revision_type character varying(50),
    parentid bigint,
    CONSTRAINT sys_c009097 CHECK ((quantity >= (0)::double precision)),
    CONSTRAINT sys_c009098 CHECK ((servicetaxperc >= (0)::double precision))
);




CREATE SEQUENCE egw_activity_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_assetforbill (
    id bigint NOT NULL,
    id_asset bigint NOT NULL,
    id_bill bigint NOT NULL,
    amount bigint NOT NULL,
    asset_index bigint,
    narration character varying(256),
    id_coa bigint NOT NULL,
    workorder_estimate_id bigint NOT NULL,
    isprocessed smallint
);




CREATE SEQUENCE egw_assetforbill_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_assetsforestimate (
    id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    abstractestimate_id bigint NOT NULL,
    asset_id bigint NOT NULL,
    ass_index bigint
);




CREATE SEQUENCE egw_assetsforestimate_seq
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_assetsforworkorder (
    id bigint NOT NULL,
    asset_id bigint NOT NULL,
    ass_index bigint,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    workorder_estimate_id bigint NOT NULL
);




CREATE SEQUENCE egw_assetsforworkorder_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_bill_deductions (
    id bigint,
    contractorbilldetailid bigint,
    tdsid bigint,
    glcodeid bigint,
    amount double precision,
    dedtype character(1),
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egw_cancelled_bill (
    id bigint NOT NULL,
    mbheader_id bigint NOT NULL,
    billregister_id bigint NOT NULL
);




CREATE TABLE egw_change_fd_estimate (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    change_fd_id bigint NOT NULL,
    work_desc character varying(1024),
    change_fd_index bigint,
    old_fund bigint,
    old_function bigint,
    old_budget_group bigint,
    old_scheme bigint,
    old_sub_scheme bigint,
    old_dep_coa bigint,
    old_dep_code character varying(256),
    old_work_desc character varying(1024)
);




CREATE SEQUENCE egw_change_fd_estimate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_change_fd_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;




CREATE TABLE egw_change_financialdetails (
    id bigint NOT NULL,
    fund_id bigint,
    budgetgroup_id bigint,
    function_id bigint,
    scheme_id bigint,
    subscheme_id bigint,
    deposit_coa bigint,
    deposit_code character varying(250),
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    state_id bigint,
    status_id bigint NOT NULL,
    is_desc_change character(1),
    is_deposit_works character(1),
    is_scheme_works character(1),
    is_budget_head character(1)
);




CREATE TABLE egw_contractor (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    correspondence_address character varying(250),
    payment_address character varying(250),
    contact_person character varying(100),
    email character varying(100),
    narration character varying(250),
    pan_number character varying(14),
    tin_number character varying(14),
    bank_id bigint,
    ifsc_code character varying(15),
    bank_account character varying(22),
    pwd_approval_code character varying(50),
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    edit_enabled character(1) DEFAULT '1'::bpchar
);




CREATE TABLE egw_contractor_advance (
    id bigint NOT NULL,
    workorderestimate_id bigint NOT NULL,
    drawing_officer bigint NOT NULL
);




CREATE SEQUENCE egw_contractor_advance_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_contractor_detail (
    id bigint NOT NULL,
    contractor_id bigint NOT NULL,
    department_id bigint,
    registration_number character varying(50),
    contractor_grade_id bigint,
    status_id bigint,
    startdate timestamp without time zone,
    enddate timestamp without time zone,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    my_contractor_index bigint
);




CREATE SEQUENCE egw_contractor_detail_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_contractor_grade (
    id bigint NOT NULL,
    grade character varying(20) NOT NULL,
    description character varying(100) NOT NULL,
    min_amount double precision NOT NULL,
    max_amount double precision NOT NULL,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    CONSTRAINT sys_c0010480 CHECK ((min_amount >= (0)::double precision)),
    CONSTRAINT sys_c0010481 CHECK ((max_amount >= (0)::double precision))
);




CREATE SEQUENCE egw_contractor_grade_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_contractor_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_contractorbill (
    id bigint NOT NULL,
    part_billnumber bigint,
    document_number bigint
);




CREATE TABLE egw_deductiontype_bill (
    id bigint NOT NULL,
    deductiontype character varying(256) NOT NULL,
    id_bill bigint NOT NULL,
    id_workorder bigint NOT NULL,
    amount bigint NOT NULL,
    deductionbill_index bigint,
    narration character varying(256),
    id_coa bigint NOT NULL
);




CREATE SEQUENCE egw_deductiontype_bill_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_depositcode (
    id bigint NOT NULL,
    code character varying(256) NOT NULL,
    description character varying(1024),
    workstype_id bigint,
    deposit_workname character varying(256) NOT NULL,
    fund_id bigint,
    functionary_id bigint,
    function_id bigint,
    scheme_id bigint,
    subscheme_id bigint,
    department_id bigint,
    ward_id bigint,
    zone_id bigint,
    financialyear_id bigint NOT NULL,
    fundsource_id bigint,
    isactive smallint,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    typeofwork_id bigint,
    subtypeofwork_id bigint
);




CREATE SEQUENCE egw_depositcode_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_depositworks_usage (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    total_deposit_amount double precision,
    consumed_amt double precision,
    released_amt double precision,
    appropriation_number character varying(256) NOT NULL,
    appropriation_date timestamp without time zone NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    financialyearid bigint,
    depositcode_id bigint,
    coa_id bigint
);




CREATE SEQUENCE egw_depositworks_usage_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_document_template (
    id bigint NOT NULL,
    department_name character varying(75),
    template text,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone
);




CREATE SEQUENCE egw_document_template_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_dw_application_details (
    id bigint NOT NULL,
    applicationrequest_id bigint NOT NULL,
    depositcode_id bigint,
    status_id bigint,
    state_id bigint,
    estimated_cost double precision,
    preparedby bigint,
    rejection_type character varying(50),
    rejection_remarks character varying(1024),
    document_number bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    recommendation character varying(1024),
    road_lastlaid_date timestamp without time zone,
    technical_details_date timestamp without time zone
);




CREATE SEQUENCE egw_dw_application_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egw_dw_applicationrequest (
    id bigint NOT NULL,
    deposit_type_id bigint NOT NULL,
    application_no character varying(100) NOT NULL,
    document_number bigint,
    application_date timestamp without time zone NOT NULL,
    work_startdate timestamp without time zone,
    work_enddate timestamp without time zone,
    status_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    is_scheme_based smallint,
    scheme_name character varying(255),
    scheme_details character varying(1024),
    applicant_name character varying(255) NOT NULL,
    restoration_work_startdate timestamp without time zone,
    restoration_work_enddate timestamp without time zone,
    portaluser_id bigint,
    rate_contract_id bigint,
    organization_id bigint,
    service_dept_id bigint,
    deposit_works_category character varying(50) NOT NULL,
    purpose_of_roadcut character varying(50),
    reference_number character varying(255)
);




CREATE SEQUENCE egw_dw_applicationrequest_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE egw_dw_citizen_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egw_dw_deposit_type (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(255) NOT NULL,
    organization_id bigint,
    is_service_connection character(1)
);




CREATE SEQUENCE egw_dw_deposit_type_seq
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egw_dw_roadcut_approval_letter (
    id bigint NOT NULL,
    application_details_id bigint NOT NULL,
    status_id bigint,
    state_id bigint,
    document_number bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE SEQUENCE egw_dw_roadcut_aprvl_ltr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egw_dw_roadcut_details (
    id bigint NOT NULL,
    location_name character varying(255),
    applicationrequest_id bigint NOT NULL,
    zone_id bigint NOT NULL,
    ward_id bigint,
    area_id bigint,
    locality_id bigint,
    street_id bigint,
    road_length bigint,
    road_breadth bigint,
    road_depth bigint,
    remarks character varying(1024),
    details_index bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    bpa_number character varying(50)
);




CREATE SEQUENCE egw_dw_roadcut_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE egw_dw_service_deptartment_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE egw_dw_uc_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE TABLE egw_dw_utilization_certificate (
    id bigint NOT NULL,
    application_details_id bigint NOT NULL,
    status_id bigint,
    state_id bigint,
    document_number bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE TABLE egw_est_apprn_history (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    budgetusage_id bigint,
    balanceavailable bigint NOT NULL
);




CREATE SEQUENCE egw_est_apprn_history_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_est_temp_activity (
    id bigint NOT NULL,
    estimate_template_id bigint NOT NULL,
    scheduleofrate_id bigint,
    value double precision NOT NULL,
    nonsor_id bigint,
    uom_id bigint,
    activity_index bigint NOT NULL,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egw_est_temp_activity_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_estimate_appropriation (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    budgetusage_id bigint,
    balanceavailable bigint NOT NULL,
    depositworksusage_id bigint
);




CREATE SEQUENCE egw_estimate_appropriation_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_estimate_photo (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    col_index bigint,
    image bytea NOT NULL,
    latitude bigint NOT NULL,
    longitude bigint NOT NULL,
    capture_date timestamp without time zone NOT NULL,
    description character varying(1024)
);




CREATE TABLE egw_estimate_reappr_details (
    id bigint NOT NULL,
    estimate_reappr bigint,
    estimate bigint,
    estimate_no character varying(64),
    appropriation_no character varying(64),
    estimate_reappr_index bigint NOT NULL,
    created_by bigint,
    createddate timestamp without time zone,
    modified_by bigint,
    modifieddate timestamp without time zone
);




CREATE SEQUENCE egw_estimate_reappr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_estimate_reapprdetail_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_estimate_reappropriation (
    id bigint NOT NULL,
    department bigint NOT NULL,
    ward bigint,
    state_id bigint,
    status_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    user_department bigint
);




CREATE TABLE egw_estimate_template (
    id bigint NOT NULL,
    code character varying(25) NOT NULL,
    name character varying(50) NOT NULL,
    description character varying(250),
    status bigint NOT NULL,
    worktype_id bigint NOT NULL,
    subtype_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egw_estimate_template_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_financialdetail (
    id bigint NOT NULL,
    fund_id bigint NOT NULL,
    budgetgroup_id bigint,
    functionary_id bigint,
    function_id bigint,
    scheme_id bigint,
    subscheme_id bigint,
    abstractestimate_id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    fin_index bigint NOT NULL,
    coa_id bigint
);




CREATE SEQUENCE egw_financialdetail_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_financingsource (
    id bigint NOT NULL,
    percentage double precision,
    financialdetailid bigint,
    fundsourceid bigint,
    lastmodifieddate timestamp without time zone,
    finsource_index bigint
);




CREATE TABLE egw_indent (
    id bigint NOT NULL,
    indent_date timestamp without time zone NOT NULL,
    indent_number character varying(32) NOT NULL,
    indent_category character varying(50) NOT NULL,
    indent_type character varying(50) NOT NULL,
    department_id bigint NOT NULL,
    start_date timestamp without time zone NOT NULL,
    enddate timestamp without time zone NOT NULL,
    fund_id bigint,
    work_type_id bigint NOT NULL,
    work_subtype_id bigint,
    indent_amount bigint NOT NULL,
    remarks character varying(1024) NOT NULL,
    status_id bigint NOT NULL,
    state_id bigint,
    doc_number bigint,
    approved_date timestamp without time zone,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE TABLE egw_indent_jurisdiction (
    id bigint NOT NULL,
    indent_id bigint NOT NULL,
    zone_id bigint NOT NULL,
    division_id bigint,
    col_index bigint
);




CREATE SEQUENCE egw_indent_jurisdiction_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_indent_maintenance_detail (
    id bigint NOT NULL,
    indent_id bigint NOT NULL,
    works_type character varying(256) NOT NULL,
    amount bigint NOT NULL,
    col_index bigint
);




CREATE SEQUENCE egw_indent_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_indent_tender (
    id bigint NOT NULL,
    rate_contract_id bigint NOT NULL,
    tender_file_number character varying(32) NOT NULL,
    tender_type character varying(32) NOT NULL,
    quoted_percentage bigint NOT NULL,
    negotiated_percentage bigint NOT NULL,
    quoted_value bigint NOT NULL,
    negotiated_value bigint NOT NULL,
    status_id bigint NOT NULL,
    contractor_id bigint NOT NULL,
    latest_offline_status_id bigint,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE SEQUENCE egw_indent_tender_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE egw_indt_maintenance_dtl_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_marketrate (
    id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    value double precision,
    schedule bigint,
    startdate timestamp without time zone,
    enddate timestamp without time zone,
    scheduleofrate_id bigint,
    market_sor_index bigint
);




CREATE SEQUENCE egw_marketrate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_mb_details (
    id bigint NOT NULL,
    mbheader_id bigint NOT NULL,
    wo_activity_id bigint NOT NULL,
    quantity bigint NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    mbd_mbh_index bigint,
    remark character varying(400),
    order_numer character varying(20),
    mbdetail_date timestamp without time zone
);




CREATE SEQUENCE egw_mb_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_mb_head_cancel_bill_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_mb_header (
    id bigint NOT NULL,
    workorder_id bigint NOT NULL,
    mb_refno character varying(100) NOT NULL,
    prepared_by bigint NOT NULL,
    con_comments character varying(400),
    mb_date timestamp without time zone NOT NULL,
    abstract character varying(400) NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    from_page_no bigint NOT NULL,
    to_page_no bigint,
    state_id bigint,
    billregister_id bigint,
    document_number bigint,
    workorder_est_id bigint,
    status_id bigint,
    is_legacy_mb smallint,
    mb_amount bigint
);




CREATE SEQUENCE egw_mb_header_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_milestone (
    id bigint NOT NULL,
    workorder_est_id bigint NOT NULL,
    state_id bigint,
    status_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    document_number bigint
);




CREATE TABLE egw_milestone_activity (
    id bigint NOT NULL,
    stage_order_no bigint NOT NULL,
    milestone_id bigint,
    description character varying(1024) NOT NULL,
    percentage bigint NOT NULL,
    activity_index bigint NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE SEQUENCE egw_milestone_activity_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_milestone_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_milestone_tem_activity_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_milestone_temp_activity (
    id bigint NOT NULL,
    stage_order_no bigint NOT NULL,
    templateid bigint,
    description character varying(1024) NOT NULL,
    percentage bigint NOT NULL,
    activity_index bigint NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE TABLE egw_milestone_template (
    id bigint NOT NULL,
    code character varying(25) NOT NULL,
    name character varying(256) NOT NULL,
    description character varying(1024),
    status bigint NOT NULL,
    worktype_id bigint NOT NULL,
    subtype_id bigint,
    state_id bigint,
    status_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egw_milestone_template_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_multiyearestapprdtls_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_multiyearestimate (
    id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    financialyear_id bigint NOT NULL,
    percentage double precision NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    my_index bigint NOT NULL,
    CONSTRAINT sys_c009024 CHECK ((percentage <= (100)::double precision))
);




CREATE SEQUENCE egw_multiyearestimate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_multiyearestimateappr (
    id bigint NOT NULL,
    department_id bigint NOT NULL,
    financialyear_id bigint NOT NULL,
    state_id bigint,
    status_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egw_multiyearestimateappr_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_multiyearestimateapprdtls (
    id bigint NOT NULL,
    multiyearestimateappr_id bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    appramount bigint NOT NULL,
    budgetgroup_id bigint NOT NULL,
    fund_id bigint NOT NULL,
    function_id bigint NOT NULL,
    multiyearestimateappr_index bigint NOT NULL,
    is_spillover_estimate smallint,
    created_by bigint,
    createddate timestamp without time zone,
    modified_by bigint,
    modifieddate timestamp without time zone
);




CREATE TABLE egw_negotiation_status (
    id bigint NOT NULL,
    approved_date timestamp without time zone NOT NULL,
    tender_response_id bigint,
    status_id bigint,
    approved_by bigint,
    state_id bigint,
    ns_index bigint NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone
);




CREATE SEQUENCE egw_negotiation_status_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_nonsor (
    id bigint NOT NULL,
    description character varying(4000) NOT NULL,
    uom bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE SEQUENCE egw_nonsor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_overhead (
    id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    description character varying(255) NOT NULL,
    expenditure_type character varying(255) NOT NULL,
    account bigint NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    name character varying(255) NOT NULL
);




CREATE TABLE egw_overhead_rate (
    id bigint NOT NULL,
    overhead_id bigint NOT NULL,
    lumpsum_amount double precision,
    percentage double precision,
    startdate timestamp without time zone NOT NULL,
    enddate timestamp without time zone,
    my_ohr_index bigint NOT NULL
);




CREATE SEQUENCE egw_overhead_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_overheadvalues (
    id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    overhead_id bigint NOT NULL,
    oh_index bigint NOT NULL,
    abstractestimate_id bigint NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    value double precision
);




CREATE SEQUENCE egw_overheadvalues_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_projectcode (
    id bigint NOT NULL,
    code character varying(256) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    description character varying(1024),
    isactive smallint,
    name character varying(1024),
    status_id bigint,
    completion_date timestamp without time zone,
    project_value double precision DEFAULT 0,
    is_final_bill smallint DEFAULT 0
);




CREATE SEQUENCE egw_projectcode_seq
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_projectcodemis (
    id bigint NOT NULL,
    workorder_number character varying(256),
    estimate_number character varying(256),
    defect_liability_period bigint NOT NULL,
    work_completion_date timestamp without time zone NOT NULL,
    projectcode_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE SEQUENCE egw_projectcodemis_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999999
    CACHE 1;




CREATE TABLE egw_rate (
    id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    modified_by bigint,
    modified_date timestamp without time zone,
    value double precision,
    schedule bigint,
    startdate timestamp without time zone,
    enddate timestamp without time zone,
    scheduleofrate_id bigint,
    my_sor_index bigint
);




CREATE TABLE egw_rate_contract (
    id bigint NOT NULL,
    indent_id bigint NOT NULL,
    rate_contract_number character varying(32) NOT NULL,
    rate_contract_date timestamp without time zone NOT NULL,
    contractor_id bigint NOT NULL,
    status_id bigint NOT NULL,
    state_id bigint,
    doc_number bigint,
    approved_date timestamp without time zone,
    amount bigint NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE SEQUENCE egw_rate_contract_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE egw_rate_seq
    START WITH 2198
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_report2_dashboard_data (
    id bigint NOT NULL,
    sl_no bigint NOT NULL,
    department_name character varying(50) NOT NULL,
    budget_allocated double precision,
    estimate_count bigint,
    estimate_value double precision,
    as_estimate_count bigint,
    as_estimate_value double precision,
    tendered_est_count bigint,
    tendered_est_value double precision,
    tender_finalized_est_count bigint,
    tender_finalized_est_value double precision,
    wo_awarded_est_count bigint,
    wo_awarded_est_value double precision,
    work_started_est_count bigint,
    work_started_est_value double precision,
    work_completed_est_count bigint,
    work_completed_est_value double precision,
    payment_released_value double precision,
    prev_month_target double precision,
    current_month_target double precision,
    last_ranking bigint,
    created_date timestamp without time zone NOT NULL,
    payment_released_currentmonth double precision
);




CREATE SEQUENCE egw_report2_dashboard_data_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_retender (
    id bigint NOT NULL,
    workspackage_id bigint NOT NULL,
    reason character varying(1024) NOT NULL,
    retender_date timestamp without time zone NOT NULL,
    iteration_no bigint NOT NULL,
    retender_index bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE TABLE egw_retender_history (
    id bigint NOT NULL,
    workspackage_id bigint NOT NULL,
    retender_id bigint,
    status_date timestamp without time zone NOT NULL,
    status_id bigint NOT NULL,
    retender_history_index bigint,
    retender_his_dtls_index bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone
);




CREATE SEQUENCE egw_retender_history_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE egw_retender_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_revision_estimate (
    id bigint
);




CREATE TABLE egw_revision_workorder (
    id bigint
);




CREATE TABLE egw_satuschange (
    id bigint NOT NULL,
    moduletype character varying(20) NOT NULL,
    moduleid bigint NOT NULL,
    fromstatus bigint,
    tostatus bigint NOT NULL,
    createdby bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    remarks character varying(256)
);




CREATE TABLE egw_schedulecategory (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    description character varying(4000),
    modified_by bigint,
    modified_date timestamp without time zone,
    parent bigint
);




CREATE SEQUENCE egw_schedulecategory_seq
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_scheduleofrate (
    id bigint NOT NULL,
    code character varying(255) NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    description character varying(4000),
    modified_by bigint,
    modified_date timestamp without time zone,
    type bigint,
    uom bigint NOT NULL,
    category bigint NOT NULL,
    is_depositworks_sor smallint DEFAULT 0
);




CREATE SEQUENCE egw_scheduleofrate_seq
    START WITH 2191
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_scheduleofratetype (
    id bigint NOT NULL,
    created_by bigint,
    created_date timestamp without time zone,
    description character varying(255),
    modified_by bigint,
    modified_date timestamp without time zone,
    name character varying(255)
);




CREATE SEQUENCE egw_scheduleofratetype_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_status (
    id bigint NOT NULL,
    moduletype character varying(50) NOT NULL,
    description character varying(50),
    lastmodifieddate timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    code character varying(30),
    order_id bigint
);




CREATE TABLE egw_statutorydeductions_bill (
    id bigint NOT NULL,
    subpartytype_id bigint,
    typeofwork_id bigint,
    statutorydeductionbill_index bigint,
    id_bill bigint NOT NULL,
    billpayeedetails_id bigint
);




CREATE SEQUENCE egw_statutorydeductions_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_tend_resp_act_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_tend_resp_con_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_tend_resp_quo_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_tender_estimate (
    id bigint NOT NULL,
    abstractestimate_id bigint,
    tender_header_id bigint NOT NULL,
    tender_type character varying(100) NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone NOT NULL,
    te_index bigint,
    works_package_id bigint
);




CREATE SEQUENCE egw_tender_estimate_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_tender_header (
    id bigint NOT NULL,
    tender_no character varying(50) NOT NULL,
    tender_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egw_tender_header_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_tender_resp_contractors (
    id bigint NOT NULL,
    tender_response_id bigint NOT NULL,
    contractor_id bigint NOT NULL,
    tr_index bigint,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone NOT NULL
);




CREATE TABLE egw_tender_response (
    id bigint NOT NULL,
    tender_estimate_id bigint NOT NULL,
    rank character varying(10),
    type character varying(25),
    perc_quoted_rate double precision,
    perc_negotiated_rate double precision,
    negotiation_date timestamp without time zone,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone NOT NULL,
    negotiation_number character varying(256),
    state_id bigint,
    narration character varying(500),
    prepared_by bigint NOT NULL,
    document_number bigint,
    resolution_number character varying(256),
    resolution_date timestamp without time zone,
    status_id bigint,
    tender_negotiated_value bigint DEFAULT 0
);




CREATE TABLE egw_tender_response_activity (
    id bigint NOT NULL,
    tender_response_id bigint NOT NULL,
    activity_id bigint NOT NULL,
    negotiated_rate double precision,
    negotiated_quantity double precision,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone NOT NULL,
    tr_index bigint
);




CREATE TABLE egw_tender_response_quotes (
    id bigint NOT NULL,
    tender_response_activity_id bigint NOT NULL,
    contractor_id bigint NOT NULL,
    quoted_rate double precision NOT NULL,
    quoted_quantity double precision,
    tra_index bigint,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone,
    modified_date timestamp without time zone NOT NULL
);




CREATE SEQUENCE egw_tender_response_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_tender_scrut_abst_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE TABLE egw_tender_scrutinizing_abst (
    id bigint NOT NULL,
    tender_invitation_type character varying(64) NOT NULL,
    tender_response_id bigint NOT NULL,
    no_of_bidders bigint NOT NULL,
    no_of_eligible_bidders bigint NOT NULL,
    bid_capacity_required character varying(128) NOT NULL,
    bid_capacity_available character varying(128) NOT NULL,
    justification_details character varying(1024) NOT NULL,
    current_market_rate bigint NOT NULL,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint,
    modified_date timestamp without time zone,
    opening_financial_bid_date timestamp without time zone NOT NULL,
    opening_technical_bid_date timestamp without time zone NOT NULL,
    agenda_number character varying(64)
);




CREATE TABLE egw_track_milestone (
    id bigint NOT NULL,
    milestone_id bigint NOT NULL,
    total_percentage bigint,
    state_id bigint,
    status_id bigint,
    created_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_by bigint NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    is_project_completed smallint
);




CREATE SEQUENCE egw_track_milestone_act_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_track_milestone_activity (
    id bigint NOT NULL,
    trackmilestone_id bigint,
    milestone_activity_id bigint,
    status character varying(50),
    completed_percentage bigint,
    remarks character varying(1024),
    completetion_date timestamp without time zone,
    activity_index bigint NOT NULL,
    createdby bigint,
    createddate timestamp without time zone,
    modifiedby bigint,
    modifieddate timestamp without time zone
);




CREATE SEQUENCE egw_track_milestone_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_typeofwork (
    id bigint NOT NULL,
    code character varying(20) NOT NULL,
    parentid bigint,
    description character varying(1000) NOT NULL,
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    partytypeid bigint
);




CREATE TABLE egw_work_order (
    id bigint NOT NULL,
    workorder_number character varying(256) NOT NULL,
    workorder_date timestamp without time zone NOT NULL,
    contractor_id bigint,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    emd_amount_deposited double precision,
    security_deposit double precision,
    labour_welfare_fund double precision,
    engineer_incharge bigint NOT NULL,
    work_completion_date timestamp without time zone,
    tender_no character varying(64),
    negotiation_no character varying(64),
    package_no character varying(64),
    work_order_details character varying(1024),
    agreement_details character varying(1024),
    payment_terms character varying(1024),
    contract_period character varying(128),
    engineer_incharge2 bigint,
    preparedby bigint NOT NULL,
    document_number bigint,
    state_id bigint,
    workorder_amount bigint NOT NULL,
    status_id bigint,
    parentid bigint,
    approveddate timestamp without time zone,
    defect_liability_period bigint,
    rate_contract_id bigint
);




CREATE TABLE egw_work_order_activity (
    id bigint NOT NULL,
    activity_id bigint NOT NULL,
    approved_quantity bigint NOT NULL,
    approved_rate double precision,
    approved_amount double precision,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    workorder_index bigint,
    workorder_estimate_id bigint NOT NULL,
    remarks character varying(256)
);




CREATE SEQUENCE egw_work_order_activity_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_work_order_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_workorder_estimate (
    id bigint NOT NULL,
    workorder_id bigint NOT NULL,
    estimate_id bigint NOT NULL,
    workorder_estimate_index bigint,
    createdby bigint,
    modifiedby bigint,
    createddate timestamp without time zone,
    modifieddate timestamp without time zone,
    work_completion_date timestamp without time zone
);




CREATE TABLE egw_workprogress_project_code (
    id character varying(256) NOT NULL,
    pc_id bigint,
    est_id bigint,
    uuid character varying(256)
);




CREATE TABLE egw_works_deductions (
    id bigint,
    worksdetailid bigint,
    glcodeid bigint,
    amount bigint,
    perc real,
    dedtype character(1),
    tdsid bigint,
    lastmodifieddate timestamp without time zone
);




CREATE TABLE egw_works_mis (
    id bigint NOT NULL,
    worksdetailid bigint NOT NULL,
    fundid bigint,
    emdreceived double precision,
    cheque_refno character varying(20),
    cheque_refno_date timestamp without time zone,
    performance_security double precision,
    councilres character varying(30),
    councilresdate timestamp without time zone,
    tenderno character varying(50),
    tenderamt double precision,
    tenderdate timestamp without time zone,
    agreement_no character varying(50),
    estimatedcost double precision,
    thirdpary bigint,
    years bigint,
    months bigint,
    days bigint,
    estimatepreparedon timestamp without time zone,
    liabilityuntil timestamp without time zone,
    esimatepreparedby bigint,
    wardid bigint,
    lastmodifieddate timestamp without time zone NOT NULL,
    otherdetails character varying(500),
    fieldid bigint,
    schemeid bigint,
    subschemeid bigint,
    type character varying(30),
    contractrate real,
    mob_perc real,
    rebatetype character varying(30),
    rebate double precision,
    agreement_type character varying(30),
    weightage character varying(30),
    tender_details character varying(300),
    mbadvinterest real,
    security_deposit double precision,
    notes character varying(300),
    xvalue real,
    yvalue real,
    contractor_grade bigint,
    isfixedasset smallint DEFAULT 0
);




CREATE SEQUENCE egw_works_overheadrate_seq
    START WITH 9
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_works_status (
    object_type character varying(128) NOT NULL,
    object_id bigint NOT NULL,
    id bigint NOT NULL,
    status_date timestamp without time zone NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    status_id bigint NOT NULL
);




CREATE SEQUENCE egw_works_status_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_workspackage (
    name character varying(1024) NOT NULL,
    id bigint NOT NULL,
    description character varying(1024),
    packagedate timestamp without time zone NOT NULL,
    department bigint NOT NULL,
    preparedby bigint NOT NULL,
    created_by bigint NOT NULL,
    modified_by bigint NOT NULL,
    created_date timestamp without time zone NOT NULL,
    modified_date timestamp without time zone NOT NULL,
    wpnumber character varying(128) NOT NULL,
    state_id bigint,
    tender_file_number character varying(50) NOT NULL,
    document_number bigint,
    status_id bigint,
    manual_wp_number character varying(64),
    latest_offline_status bigint
);




CREATE TABLE egw_workspackage_details (
    id bigint NOT NULL,
    id_workspackage bigint,
    id_estimate bigint,
    created_by bigint,
    modified_by bigint,
    created_date timestamp without time zone,
    modified_date timestamp without time zone,
    wp_index bigint
);




CREATE SEQUENCE egw_workspackage_details_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE egw_workspackage_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE egw_workstype (
    id bigint NOT NULL,
    name character varying(50) NOT NULL,
    expenditure_type character varying(255) NOT NULL,
    code character varying(128) NOT NULL
);




CREATE TABLE generalledger (
    id bigint NOT NULL,
    voucherlineid bigint NOT NULL,
    effectivedate timestamp without time zone NOT NULL,
    glcodeid bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    description character varying(250),
    voucherheaderid bigint,
    functionid bigint,
    remittancedate timestamp without time zone
);




CREATE TABLE generalledgerdetail (
    id bigint NOT NULL,
    generalledgerid bigint NOT NULL,
    detailkeyid bigint NOT NULL,
    detailtypeid bigint NOT NULL,
    amount double precision
);




CREATE TABLE miscbilldetail (
    id bigint NOT NULL,
    billnumber character varying(50),
    billdate timestamp without time zone,
    amount double precision NOT NULL,
    passedamount double precision NOT NULL,
    paidto character varying(250) NOT NULL,
    paidbyid bigint,
    billvhid bigint,
    payvhid bigint,
    paidamount double precision
);




CREATE TABLE paymentheader (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    type character varying(50) NOT NULL,
    bankaccountnumberid bigint,
    state_id bigint,
    createdby bigint,
    lastmodifiedby bigint,
    paymentamount double precision,
    concurrencedate timestamp without time zone,
    drawingofficer_id bigint
);




CREATE TABLE voucherheader (
    id bigint NOT NULL,
    cgn character varying(50) NOT NULL,
    cgdate timestamp without time zone NOT NULL,
    name character varying(50) NOT NULL,
    type character varying(100) NOT NULL,
    description character varying(1024),
    effectivedate timestamp without time zone NOT NULL,
    vouchernumber character varying(30),
    voucherdate timestamp without time zone NOT NULL,
    departmentid bigint,
    fundid bigint,
    fiscalperiodid bigint NOT NULL,
    status smallint,
    originalvcid bigint,
    fundsourceid bigint,
    isconfirmed smallint DEFAULT 0,
    createdby bigint,
    functionid smallint,
    refcgno character varying(10),
    cgvn character varying(50) NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    moduleid bigint,
    state_id bigint
);




CREATE MATERIALIZED VIEW egw_wpreport_est_bills_mview AS
 SELECT to_char(('now'::text)::timestamp without time zone, 'hh:mi:ss AM DD-MM-YYYY'::text) AS view_timestamp,
    abs_est.id AS abs_est_id,
    work_order.id AS work_order_id,
    work_order.contractor_id AS work_order_contractor_id,
    mb.id AS mb_id,
    mb.mb_refno AS mb_mb_refno,
    mb.prepared_by AS mb_prepared_by,
    mb.mb_date AS mb_mb_date,
    mb.created_date AS mb_created_date,
    mb.state_id AS mb_state_id,
    mb.status_id AS mb_status_id,
    mb.is_legacy_mb AS mb_is_legacy_mb,
    mb.mb_amount AS mb_mb_amount,
    bill_reg.id AS bill_reg_id,
    bill_reg.billnumber AS bill_reg_billnumber,
    bill_reg.billdate AS bill_reg_billdate,
    bill_reg.billamount AS bill_reg_billamount,
    bill_reg.billstatus AS bill_reg_billstatus,
    bill_reg.passedamount AS bill_reg_passedamount,
    bill_reg.billtype AS bill_reg_billtype,
    bill_reg.expendituretype AS bill_reg_expendituretype,
    bill_reg.createddate AS bill_reg_createddate,
    bill_reg.statusid AS bill_reg_statusid,
    bill_reg.state_id AS bill_reg_state_id,
    bill_dtls.functionid AS bill_dtls_functionid,
    bill_dtls.glcodeid AS bill_dtls_glcodeid,
    bill_dtls.debitamount AS bill_dtls_debitamount,
    bill_dtls.creditamount AS bill_dtls_creditamount,
    bill_pay_dtl.accountdetailtypeid AS bill_pay_dtl_acc_detailtypeid,
    bill_pay_dtl.accountdetailkeyid AS bill_pay_dtl_acc_detailkeyid,
    bill_pay_dtl.debitamount AS bill_pay_dtl_debitamount,
    bill_pay_dtl.creditamount AS bill_pay_dtl_creditamount,
    bill_pay_dtl.pc_department AS bill_pay_dtl_pc_department,
    bill_reg_mis.id AS bill_reg_mis_id,
    bill_reg_mis.billid AS bill_reg_mis_billid,
    bill_reg_mis.fundid AS bill_reg_mis_fundid,
    bill_reg_mis.departmentid AS bill_reg_mis_departmentid,
    bill_reg_mis.payto AS bill_reg_mis_payto,
    bill_reg_mis.partybillnumber AS bill_reg_mis_partybillnumber,
    bill_reg_mis.partybilldate AS bill_reg_mis_partybilldate,
    bill_reg_mis.functionid AS bill_reg_mis_functionid,
    bill_reg_mis.financialyearid AS bill_reg_mis_financialyearid,
    bill_voucher_hdr.id AS bill_voucher_hdr_id,
    bill_voucher_hdr.vouchernumber AS bill_voucher_hdr_vouchernumber,
    bill_voucher_hdr.voucherdate AS bill_voucher_hdr_voucherdate,
    bill_voucher_hdr.status AS bill_voucher_hdr_status,
    payment_voucher_hdr.id AS pmt_voucher_hdr_id,
    payment_voucher_hdr.vouchernumber AS pmt_voucher_hdr_vouchernumber,
    payment_voucher_hdr.voucherdate AS pmt_voucher_hdr_voucherdate,
    payment_voucher_hdr.status AS pmt_voucher_hdr_status,
    misc_bill_dtl.amount AS misc_bill_dtl_amount,
    misc_bill_dtl.passedamount AS misc_bill_dtl_passedamount,
    misc_bill_dtl.paidto AS misc_bill_dtl_paidto,
    misc_bill_dtl.paidamount AS misc_bill_dtl_paidamount,
    payment_hdr.paymentamount AS payment_hdr_paymentamount,
    payment_hdr.concurrencedate AS payment_hdr_concurrencedate,
    COALESCE(( SELECT sum(gld.amount) AS sum
           FROM generalledger gd,
            generalledgerdetail gld
          WHERE ((((((gd.id = gld.generalledgerid) AND (gd.debitamount > (0)::double precision)) AND (gd.voucherheaderid = payment_voucher_hdr.id)) AND (payment_voucher_hdr.status = 0)) AND (gld.detailkeyid = work_order.contractor_id)) AND (gld.detailtypeid = ( SELECT accountdetailtype.id
                   FROM accountdetailtype
                  WHERE (lower((accountdetailtype.name)::text) = 'contractor'::text))))), (0)::double precision) AS pmt_rlsd_to_contractor_for_wo
   FROM (((((((((((egw_abstractestimate abs_est
     LEFT JOIN egw_workorder_estimate work_order_est ON ((work_order_est.estimate_id = abs_est.id)))
     LEFT JOIN egw_work_order work_order ON ((work_order.id = work_order_est.workorder_id)))
     LEFT JOIN egw_mb_header mb ON ((mb.workorder_est_id = work_order_est.id)))
     LEFT JOIN eg_billregister bill_reg ON ((bill_reg.id = mb.billregister_id)))
     LEFT JOIN eg_billdetails bill_dtls ON ((bill_dtls.billid = bill_reg.id)))
     LEFT JOIN eg_billpayeedetails bill_pay_dtl ON ((bill_pay_dtl.billdetailid = bill_dtls.id)))
     LEFT JOIN eg_billregistermis bill_reg_mis ON ((bill_reg_mis.billid = bill_reg.id)))
     LEFT JOIN voucherheader bill_voucher_hdr ON ((bill_reg_mis.voucherheaderid = bill_voucher_hdr.id)))
     LEFT JOIN miscbilldetail misc_bill_dtl ON ((misc_bill_dtl.billvhid = bill_voucher_hdr.id)))
     LEFT JOIN voucherheader payment_voucher_hdr ON ((misc_bill_dtl.payvhid = payment_voucher_hdr.id)))
     LEFT JOIN paymentheader payment_hdr ON ((payment_hdr.voucherheaderid = payment_voucher_hdr.id)))
  WHERE ((((bill_pay_dtl.accountdetailtypeid = ( SELECT accountdetailtype.id
           FROM accountdetailtype
          WHERE (((accountdetailtype.name)::text = 'PROJECTCODE'::text) AND ((accountdetailtype.description)::text = 'PROJECTCODE'::text)))) AND ((bill_reg.id IS NOT NULL) AND ((bill_reg.id)::text <> ''::text))) OR ((COALESCE((bill_pay_dtl.accountdetailtypeid)::text, ''::text) = ''::text) AND (COALESCE((bill_reg.id)::text, ''::text) = ''::text))) AND ((mb.id IS NOT NULL) AND ((mb.id)::text <> ''::text)))
  WITH NO DATA;




CREATE MATERIALIZED VIEW egw_wpreport_est_wo_mview AS
 SELECT to_char(('now'::text)::timestamp without time zone, 'hh:mi:ss AM DD-MM-YYYY'::text) AS view_timestamp,
    abs_est.approveddate AS abs_est_approveddate,
    abs_est.budgetapprno AS abs_est_budgetapprno,
    abs_est.category AS abs_est_category,
    abs_est.createddate AS abs_est_createddate,
    abs_est.despositcode_id AS abs_est_despositcode_id,
    abs_est.estimate_number AS abs_est_estimate_number,
    abs_est.estimatedate AS abs_est_estimatedate,
    abs_est.executingdepartment AS abs_est_executingdepartment,
    abs_est.id AS abs_est_id,
    abs_est.name AS abs_est_name,
    abs_est.parent_category AS abs_est_parent_category,
    abs_est.preparedby AS abs_est_preparedby,
    abs_est.projectcode_id AS abs_est_projectcode_id,
    abs_est.state_id AS abs_est_state_id,
    abs_est.status_id AS abs_est_status_id,
    abs_est.parentid AS abs_est_parentid,
    COALESCE(( SELECT egw_status.code
           FROM egw_status
          WHERE (egw_status.id = abs_est.status_id)), NULL::character varying) AS abs_est_status_code,
    abs_est.type AS abs_est_type,
    abs_est.userdepartment AS abs_est_userdepartment,
    abs_est.value AS abs_est_value,
    (COALESCE(abs_est.value, (0)::double precision) + ( SELECT COALESCE(sum(egw_overheadvalues.value), (0)::double precision) AS "coalesce"
           FROM egw_overheadvalues
          WHERE (egw_overheadvalues.abstractestimate_id = abs_est.id))) AS abs_est_value_with_oh,
    abs_est.ward AS abs_est_ward_id,
    COALESCE(( SELECT eg_boundary.name
           FROM eg_boundary
          WHERE (eg_boundary.id_bndry = abs_est.ward)), NULL::character varying) AS abs_est_ward_name,
    exec_dept.dept_name AS exec_dept_dept_name,
    fin_details.budgetgroup_id AS fin_details_budgetgroup_id,
    fin_details.coa_id AS fin_details_coa_id,
    fin_details.function_id AS fin_details_function_id,
    fin_details.fund_id AS fin_details_fund_id,
    fin_details.scheme_id AS fin_details_scheme_id,
    fin_details.subscheme_id AS fin_details_subscheme_id,
    works_package.created_date AS works_package_created_date,
    works_package.id AS works_package_id,
    works_package.name AS works_package_name,
    works_package.packagedate AS works_package_packagedate,
    works_package.preparedby AS works_package_preparedby,
    works_package.state_id AS works_package_state_id,
    works_package.status_id AS works_package_status_id,
    COALESCE(( SELECT egw_status.code
           FROM egw_status
          WHERE (egw_status.id = works_package.status_id)), NULL::character varying) AS works_package_status_code,
    works_package.tender_file_number AS works_package_tender_file_no,
    works_package.wpnumber AS works_package_wpnumber,
    COALESCE(( SELECT stat.id
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((ws.status_id = stat.id) AND (ws.id = ( SELECT max(egw_works_status.id) AS max
                   FROM egw_works_status
                  WHERE (((egw_works_status.object_type)::text = 'WorksPackage'::text) AND (egw_works_status.object_id = works_package.id)))))), NULL::bigint) AS wp_latest_offline_status_id,
    COALESCE(( SELECT stat.code
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((ws.status_id = stat.id) AND (ws.id = ( SELECT max(egw_works_status.id) AS max
                   FROM egw_works_status
                  WHERE (((egw_works_status.object_type)::text = 'WorksPackage'::text) AND (egw_works_status.object_id = works_package.id)))))), NULL::character varying) AS wp_latest_offline_status_code,
    tender_est.tender_type,
    tender.tender_date AS tender_tender_date,
    tender.tender_no AS tender_tender_no,
    tender_resp.id AS tender_resp_id,
    tender_resp.perc_quoted_rate AS tender_resp_perc_quoted_rate,
    tender_resp.perc_negotiated_rate AS tender_resp_perc_negotiated_rt,
    tender_resp.negotiation_date AS tender_resp_negotiation_date,
    tender_resp.negotiation_number AS tender_resp_negotiation_number,
    tender_resp.state_id AS tender_resp_state_id,
    tender_resp.prepared_by AS tender_resp_prepared_by,
    tender_resp.status_id AS tender_resp_status_id,
    tender_resp.tender_negotiated_value AS tender_resp_negotiated_value,
    COALESCE(( SELECT egw_status.code
           FROM egw_status
          WHERE (egw_status.id = tender_resp.status_id)), NULL::character varying) AS tender_resp_status_code,
    COALESCE(( SELECT stat.id
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((ws.status_id = stat.id) AND (ws.id = ( SELECT max(egw_works_status.id) AS max
                   FROM egw_works_status
                  WHERE (((egw_works_status.object_type)::text = 'TenderResponseContractors'::text) AND (egw_works_status.object_id = tender_resp.id)))))), NULL::bigint) AS tr_latest_offline_status_id,
    COALESCE(( SELECT stat.code
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((ws.status_id = stat.id) AND (ws.id = ( SELECT max(egw_works_status.id) AS max
                   FROM egw_works_status
                  WHERE (((egw_works_status.object_type)::text = 'TenderResponseContractors'::text) AND (egw_works_status.object_id = tender_resp.id)))))), NULL::character varying) AS tr_latest_offline_status_code,
    work_order.contract_period AS work_order_contract_period,
    work_order.contractor_id AS work_order_contractor_id,
    COALESCE(( SELECT egw_contractor.name
           FROM egw_contractor
          WHERE (egw_contractor.id = work_order.contractor_id)), NULL::character varying) AS work_order_contractor_name,
    COALESCE(( SELECT egw_contractor.code
           FROM egw_contractor
          WHERE (egw_contractor.id = work_order.contractor_id)), NULL::character varying) AS work_order_contractor_code,
    work_order.createddate AS work_order_createddate,
    work_order.approveddate AS work_order_approveddate,
    work_order.emd_amount_deposited AS work_order_emd_amt_deposited,
    work_order.id AS work_order_id,
    work_order.labour_welfare_fund AS work_order_labour_welfare_fund,
    work_order.preparedby AS work_order_preparedby,
    work_order.security_deposit AS work_order_security_deposit,
    work_order.state_id AS work_order_state_id,
    work_order.status_id AS work_order_status_id,
    COALESCE(( SELECT egw_status.code
           FROM egw_status
          WHERE (egw_status.id = work_order.status_id)), NULL::character varying) AS work_order_status_code,
    work_order.work_completion_date AS work_order_work_completion_dt,
    work_order.workorder_amount AS work_order_workorder_amount,
    work_order.workorder_date AS work_order_workorder_date,
    work_order.workorder_number AS work_order_workorder_number,
    work_order.parentid AS work_order_parentid,
    proj_code.code AS proj_code,
    proj_code.isactive AS proj_code_isactive,
    proj_code.name AS proj_code_name,
    proj_code.status_id AS proj_code_status_id,
    proj_code.completion_date AS proj_code_completion_date,
    proj_code.project_value AS proj_code_project_value,
    COALESCE(( SELECT egw_status.code
           FROM egw_status
          WHERE (egw_status.id = proj_code.status_id)), NULL::character varying) AS proj_code_status_code,
    COALESCE(( SELECT stat.id
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((ws.status_id = stat.id) AND (ws.id = ( SELECT max(egw_works_status.id) AS max
                   FROM egw_works_status
                  WHERE (((egw_works_status.object_type)::text = 'WorkOrder'::text) AND (egw_works_status.object_id = work_order.id)))))), NULL::bigint) AS wo_latest_offline_status_id,
    COALESCE(( SELECT stat.code
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((ws.status_id = stat.id) AND (ws.id = ( SELECT max(egw_works_status.id) AS max
                   FROM egw_works_status
                  WHERE (((egw_works_status.object_type)::text = 'WorkOrder'::text) AND (egw_works_status.object_id = work_order.id)))))), NULL::character varying) AS wo_latest_offline_status_code,
    COALESCE(( SELECT stat.id
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((((ws.status_id = stat.id) AND ((ws.object_type)::text = 'WorkOrder'::text)) AND (ws.object_id = work_order.id)) AND ((stat.code)::text = 'Work Order acknowledged'::text))), NULL::bigint) AS wo_acknowledged_status_date,
    COALESCE(( SELECT stat.id
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((((ws.status_id = stat.id) AND ((ws.object_type)::text = 'WorkOrder'::text)) AND (ws.object_id = work_order.id)) AND ((stat.code)::text = 'Site handed over'::text))), NULL::bigint) AS wo_site_handed_over_date,
    COALESCE(( SELECT stat.id
           FROM egw_works_status ws,
            egw_status stat
          WHERE ((((ws.status_id = stat.id) AND ((ws.object_type)::text = 'WorkOrder'::text)) AND (ws.object_id = work_order.id)) AND ((stat.code)::text = 'Work commenced'::text))), NULL::bigint) AS wo_work_commenced_date
   FROM ((((((((((egw_abstractestimate abs_est
     LEFT JOIN egw_workspackage_details works_package_dtl ON ((works_package_dtl.id_estimate = abs_est.id)))
     LEFT JOIN egw_workspackage works_package ON ((works_package.id = works_package_dtl.id_workspackage)))
     LEFT JOIN egw_tender_estimate tender_est ON ((tender_est.works_package_id = works_package.id)))
     LEFT JOIN egw_tender_response tender_resp ON ((tender_est.id = tender_resp.tender_estimate_id)))
     LEFT JOIN egw_tender_header tender ON ((tender_est.tender_header_id = tender.id)))
     LEFT JOIN egw_workorder_estimate work_order_est ON ((work_order_est.estimate_id = abs_est.id)))
     LEFT JOIN egw_work_order work_order ON ((work_order.id = work_order_est.workorder_id)))
     LEFT JOIN egw_financialdetail fin_details ON ((fin_details.abstractestimate_id = abs_est.id)))
     LEFT JOIN eg_department exec_dept ON ((exec_dept.id_dept = (abs_est.executingdepartment)::numeric)))
     LEFT JOIN egw_projectcode proj_code ON ((proj_code.id = abs_est.projectcode_id)))
  WITH NO DATA;




CREATE TABLE egw_wrkprog_projcode_spillover (
    id character varying(256) NOT NULL,
    pc_id bigint,
    est_id bigint,
    uuid character varying(256)
);




CREATE SEQUENCE eispayroll_adv_schedule_seq
    START WITH 7
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_batchfailuredts_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_batchgendetails_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_deductions_seq
    START WITH 10
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_earnings_seq
    START WITH 24
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_emppayroll_seq
    START WITH 6
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_exception_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_exceptionmstr_seq
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_incrementdet_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_paygenrules_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_payhead_rule_seq
    START WITH 7
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_payscaledetails_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    CACHE 1;




CREATE SEQUENCE eispayroll_pension_details_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_pension_payment_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_pension_recov_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_saladvances_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE eispayroll_salarycodes_seq
    START WITH 18
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE employees_seq
    START WITH 207
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE financial_institution (
    id bigint NOT NULL,
    name character varying(250) NOT NULL
);




CREATE TABLE financialyear (
    id bigint NOT NULL,
    financialyear character varying(50),
    startingdate timestamp without time zone,
    endingdate timestamp without time zone,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    modifiedby bigint,
    isactiveforposting smallint NOT NULL,
    isclosed smallint,
    transferclosingbalance smallint
);




CREATE TABLE fiscalperiod (
    id bigint NOT NULL,
    type bigint,
    name character varying(50) NOT NULL,
    startingdate timestamp without time zone NOT NULL,
    endingdate timestamp without time zone NOT NULL,
    parentid bigint,
    isactiveforposting bigint,
    isactive smallint NOT NULL,
    modifiedby bigint,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    created timestamp without time zone NOT NULL,
    financialyearid bigint,
    isclosed smallint,
    moduleid bigint
);




CREATE TABLE function (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(100) NOT NULL,
    type character varying(50),
    llevel bigint NOT NULL,
    parentid bigint,
    isactive smallint,
    created timestamp without time zone,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    modifiedby bigint,
    isnotleaf smallint NOT NULL,
    parentcode character varying(50),
    createdby bigint
);




CREATE TABLE functionary (
    id bigint NOT NULL,
    code bigint NOT NULL,
    name character varying(256) NOT NULL,
    createtimestamp timestamp without time zone,
    updatetimestamp timestamp without time zone,
    isactive smallint,
    module_name character varying(60)
);




CREATE SEQUENCE functionary_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE fund (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50),
    llevel bigint NOT NULL,
    parentid bigint,
    isactive smallint NOT NULL,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone,
    created timestamp without time zone NOT NULL,
    modifiedby bigint,
    isnotleaf smallint,
    identifier character(1),
    purpose_id smallint,
    payglcodeid bigint,
    recvglcodeid bigint,
    createdby bigint,
    openingdebitbalance double precision,
    openingcreditbalance double precision,
    transactiondebitamount double precision,
    transactioncreditamount double precision
);




CREATE TABLE fundsource (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    type character varying(50),
    parentid bigint,
    isactive smallint NOT NULL,
    created timestamp without time zone NOT NULL,
    financialinstid bigint,
    funding_type character varying(50),
    loan_percentage real,
    source_amount double precision,
    rate_of_interest real,
    loan_period real,
    moratorium_period real,
    repayment_frequency character varying(15),
    no_of_installment bigint,
    bankaccountid bigint,
    govt_order character varying(250),
    govt_date timestamp without time zone,
    dp_code_number character varying(250),
    dp_code_resg character varying(250),
    fin_inst_letter_num character varying(250),
    fin_inst_letter_date timestamp without time zone,
    fin_inst_schm_num character varying(250),
    fin_inst_schm_date timestamp without time zone,
    subschemeid bigint,
    llevel bigint,
    isnotleaf smallint,
    createdby bigint,
    lastmodifieddate timestamp without time zone,
    lastmodifiedby bigint
);




CREATE TABLE generalvouchermis (
    id bigint NOT NULL,
    costcenterid bigint,
    departmentid bigint,
    zoneid bigint,
    schemeid bigint,
    voucherheaderid bigint NOT NULL
);




CREATE TABLE gpf_empdetails (
    coc_gpf_empdetails_id bigint NOT NULL,
    seq_id bigint,
    deposit double precision,
    ref_wd_date timestamp without time zone,
    ref_wd_amt double precision,
    tot_each_mon double precision,
    widtdraw double precision,
    mon_bal_int_calc double precision,
    int_on_mon_bal double precision,
    pc_arrears double precision,
    pay_rev_arrears double precision,
    remarks character varying(1000),
    coc_gpf_openbal_id bigint,
    idx bigint DEFAULT 0,
    installment_month character varying(30),
    fifth_pay_commission_arrears double precision
);




CREATE TABLE gpf_openbal (
    coc_gpf_openbal_id bigint NOT NULL,
    empid bigint,
    financialyearid bigint,
    openbal double precision,
    created_date timestamp without time zone,
    openbalpayrev double precision,
    openbal5pay double precision,
    gpf_account_number character varying(25),
    openbal1percda double precision
);




CREATE SEQUENCE hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE jbpm_action (
    id_ bigint NOT NULL,
    class character(3) NOT NULL,
    name_ character varying(765),
    ispropagationallowed_ smallint,
    actionexpression_ character varying(765),
    isasync_ smallint,
    referencedaction_ bigint,
    actiondelegation_ bigint,
    event_ bigint,
    processdefinition_ bigint,
    timername_ character varying(765),
    duedate_ character varying(765),
    repeat_ character varying(765),
    transitionname_ character varying(765),
    timeraction_ bigint,
    expression_ character varying(4000),
    eventindex_ bigint,
    exceptionhandler_ bigint,
    exceptionhandlerindex_ bigint
);




CREATE TABLE jbpm_bytearray (
    id_ bigint NOT NULL,
    name_ character varying(765),
    filedefinition_ bigint
);




CREATE TABLE jbpm_byteblock (
    processfile_ bigint NOT NULL,
    bytes_ bytea,
    index_ bigint NOT NULL
);




CREATE TABLE jbpm_comment (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    actorid_ character varying(765),
    time_ timestamp without time zone,
    message_ character varying(4000),
    token_ bigint,
    taskinstance_ bigint,
    tokenindex_ bigint,
    taskinstanceindex_ bigint
);




CREATE TABLE jbpm_decisionconditions (
    decision_ bigint NOT NULL,
    transitionname_ character varying(765),
    expression_ character varying(765),
    index_ bigint NOT NULL
);




CREATE TABLE jbpm_delegation (
    id_ bigint NOT NULL,
    classname_ character varying(4000),
    configuration_ character varying(4000),
    configtype_ character varying(765),
    processdefinition_ bigint
);




CREATE TABLE jbpm_event (
    id_ bigint NOT NULL,
    eventtype_ character varying(765),
    type_ character(3),
    graphelement_ bigint,
    processdefinition_ bigint,
    node_ bigint,
    transition_ bigint,
    task_ bigint
);




CREATE TABLE jbpm_exceptionhandler (
    id_ bigint NOT NULL,
    exceptionclassname_ character varying(4000),
    type_ character(3),
    graphelement_ bigint,
    processdefinition_ bigint,
    graphelementindex_ bigint,
    node_ bigint,
    transition_ bigint,
    task_ bigint
);




CREATE TABLE jbpm_job (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    version_ bigint NOT NULL,
    duedate_ timestamp without time zone,
    processinstance_ bigint,
    token_ bigint,
    taskinstance_ bigint,
    issuspended_ smallint,
    isexclusive_ smallint,
    lockowner_ character varying(765),
    locktime_ timestamp without time zone,
    exception_ character varying(4000),
    retries_ bigint,
    name_ character varying(765),
    repeat_ character varying(765),
    transitionname_ character varying(765),
    action_ bigint,
    graphelementtype_ character varying(765),
    graphelement_ bigint,
    node_ bigint
);




CREATE TABLE jbpm_log (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    index_ bigint,
    date_ timestamp without time zone,
    token_ bigint,
    parent_ bigint,
    message_ character varying(4000),
    exception_ character varying(4000),
    action_ bigint,
    node_ bigint,
    enter_ timestamp without time zone,
    leave_ timestamp without time zone,
    duration_ bigint,
    newlongvalue_ bigint,
    transition_ bigint,
    child_ bigint,
    sourcenode_ bigint,
    destinationnode_ bigint,
    variableinstance_ bigint,
    oldbytearray_ bigint,
    newbytearray_ bigint,
    olddatevalue_ timestamp without time zone,
    newdatevalue_ timestamp without time zone,
    olddoublevalue_ double precision,
    newdoublevalue_ double precision,
    oldlongidclass_ character varying(765),
    oldlongidvalue_ bigint,
    newlongidclass_ character varying(765),
    newlongidvalue_ bigint,
    oldstringidclass_ character varying(765),
    oldstringidvalue_ character varying(765),
    newstringidclass_ character varying(765),
    newstringidvalue_ character varying(765),
    oldlongvalue_ bigint,
    oldstringvalue_ character varying(4000),
    newstringvalue_ character varying(4000),
    taskinstance_ bigint,
    taskactorid_ character varying(765),
    taskoldactorid_ character varying(765),
    swimlaneinstance_ bigint
);




CREATE TABLE jbpm_moduledefinition (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    name_ character varying(4000),
    processdefinition_ bigint,
    starttask_ bigint
);




CREATE TABLE jbpm_moduleinstance (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    version_ bigint NOT NULL,
    processinstance_ bigint,
    taskmgmtdefinition_ bigint,
    name_ character varying(765)
);




CREATE TABLE jbpm_node (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    name_ character varying(765),
    description_ character varying(4000),
    processdefinition_ bigint,
    isasync_ smallint,
    isasyncexcl_ smallint,
    action_ bigint,
    superstate_ bigint,
    subprocname_ character varying(765),
    subprocessdefinition_ bigint,
    decisionexpression_ character varying(765),
    decisiondelegation bigint,
    script_ bigint,
    signal_ bigint,
    createtasks_ smallint,
    endtasks_ smallint,
    nodecollectionindex_ bigint
);




CREATE TABLE jbpm_pooledactor (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    actorid_ character varying(765),
    swimlaneinstance_ bigint
);




CREATE TABLE jbpm_processdefinition (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    name_ character varying(765),
    description_ character varying(4000),
    version_ bigint,
    isterminationimplicit_ smallint,
    startstate_ bigint
);




CREATE TABLE jbpm_processinstance (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    key_ character varying(765),
    start_ timestamp without time zone,
    end_ timestamp without time zone,
    issuspended_ smallint,
    processdefinition_ bigint,
    roottoken_ bigint,
    superprocesstoken_ bigint
);




CREATE TABLE jbpm_runtimeaction (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    eventtype_ character varying(765),
    type_ character(3),
    graphelement_ bigint,
    processinstance_ bigint,
    action_ bigint,
    processinstanceindex_ bigint
);




CREATE TABLE jbpm_swimlane (
    id_ bigint NOT NULL,
    name_ character varying(765),
    actoridexpression_ character varying(765),
    pooledactorsexpression_ character varying(765),
    assignmentdelegation_ bigint,
    taskmgmtdefinition_ bigint
);




CREATE TABLE jbpm_swimlaneinstance (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    name_ character varying(765),
    actorid_ character varying(765),
    swimlane_ bigint,
    taskmgmtinstance_ bigint
);




CREATE TABLE jbpm_task (
    id_ bigint NOT NULL,
    name_ character varying(765),
    description_ character varying(4000),
    processdefinition_ bigint,
    isblocking_ smallint,
    issignalling_ smallint,
    condition_ character varying(765),
    duedate_ character varying(765),
    priority_ bigint,
    actoridexpression_ character varying(765),
    pooledactorsexpression_ character varying(765),
    taskmgmtdefinition_ bigint,
    tasknode_ bigint,
    startstate_ bigint,
    assignmentdelegation_ bigint,
    swimlane_ bigint,
    taskcontroller_ bigint
);




CREATE TABLE jbpm_taskactorpool (
    taskinstance_ bigint NOT NULL,
    pooledactor_ bigint NOT NULL
);




CREATE TABLE jbpm_taskcontroller (
    id_ bigint NOT NULL,
    taskcontrollerdelegation_ bigint
);




CREATE TABLE jbpm_taskinstance (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    version_ bigint NOT NULL,
    name_ character varying(765),
    description_ character varying(4000),
    actorid_ character varying(765),
    create_ timestamp without time zone,
    start_ timestamp without time zone,
    end_ timestamp without time zone,
    duedate_ timestamp without time zone,
    priority_ bigint,
    iscancelled_ smallint,
    issuspended_ smallint,
    isopen_ smallint,
    issignalling_ smallint,
    isblocking_ smallint,
    task_ bigint,
    token_ bigint,
    procinst_ bigint,
    swimlaninstance_ bigint,
    taskmgmtinstance_ bigint
);




CREATE TABLE jbpm_token (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    name_ character varying(765),
    start_ timestamp without time zone,
    end_ timestamp without time zone,
    nodeenter_ timestamp without time zone,
    nextlogindex_ bigint,
    isabletoreactivateparent_ smallint,
    isterminationimplicit_ smallint,
    issuspended_ smallint,
    lock_ character varying(765),
    node_ bigint,
    processinstance_ bigint,
    parent_ bigint,
    subprocessinstance_ bigint
);




CREATE TABLE jbpm_tokenvariablemap (
    id_ bigint NOT NULL,
    version_ bigint NOT NULL,
    token_ bigint,
    contextinstance_ bigint
);




CREATE TABLE jbpm_transition (
    id_ bigint NOT NULL,
    name_ character varying(765),
    description_ character varying(4000),
    processdefinition_ bigint,
    from_ bigint,
    to_ bigint,
    condition_ character varying(765),
    fromindex_ bigint
);




CREATE TABLE jbpm_variableaccess (
    id_ bigint NOT NULL,
    variablename_ character varying(765),
    access_ character varying(765),
    mappedname_ character varying(765),
    processstate_ bigint,
    taskcontroller_ bigint,
    index_ bigint,
    script_ bigint
);




CREATE TABLE jbpm_variableinstance (
    id_ bigint NOT NULL,
    class_ character(3) NOT NULL,
    version_ bigint NOT NULL,
    name_ character varying(765),
    converter_ character(3),
    token_ bigint,
    tokenvariablemap_ bigint,
    processinstance_ bigint,
    bytearrayvalue_ bigint,
    datevalue_ timestamp without time zone,
    doublevalue_ double precision,
    longidclass_ character varying(765),
    longvalue_ bigint,
    stringidclass_ character varying(765),
    stringvalue_ character varying(765),
    taskinstance_ bigint
);




CREATE TABLE jms_messages (
    messageid bigint NOT NULL,
    destination character varying(255) NOT NULL,
    txid bigint,
    txop character(1),
    messageblob bytea
);




CREATE TABLE jms_transactions (
    txid bigint NOT NULL
);




CREATE TABLE job_history (
    employee_id integer NOT NULL,
    start_date timestamp without time zone NOT NULL,
    end_date timestamp without time zone NOT NULL,
    job_id character varying(10) NOT NULL,
    department_id smallint,
    CONSTRAINT jhist_date_interval CHECK ((end_date > start_date))
);




COMMENT ON TABLE job_history IS 'Table that stores job history of the employees. If an employee
changes departments within the job or changes jobs within the department,
new rows get inserted into this table with old job information of the
employee. Contains a complex primary key: employee_id+start_date.
Contains 25 rows. References with jobs, employees, and departments tables.';



COMMENT ON COLUMN job_history.employee_id IS 'A not null column in the complex primary key employee_id+start_date.
Foreign key to employee_id column of the employee table';



COMMENT ON COLUMN job_history.start_date IS 'A not null column in the complex primary key employee_id+start_date.
Must be less than the end_date of the job_history table. (enforced by
constraint jhist_date_interval)';



COMMENT ON COLUMN job_history.end_date IS 'Last day of the employee in this job role. A not null column. Must be
greater than the start_date of the job_history table.
(enforced by constraint jhist_date_interval)';



COMMENT ON COLUMN job_history.job_id IS 'Job role in which the employee worked in the past; foreign key to
job_id column in the jobs table. A not null column.';



COMMENT ON COLUMN job_history.department_id IS 'Department id in which the employee worked in the past; foreign key to deparment_id column in the departments table';



CREATE TABLE jobs (
    job_id character varying(10) NOT NULL,
    job_title character varying(35) NOT NULL,
    min_salary integer,
    max_salary integer
);




COMMENT ON TABLE jobs IS 'jobs table with job titles and salary ranges. Contains 19 rows.
References with employees and job_history table.';



COMMENT ON COLUMN jobs.job_id IS 'Primary key of jobs table.';



COMMENT ON COLUMN jobs.job_title IS 'A not null column that shows job title, e.g. AD_VP, FI_ACCOUNTANT';



COMMENT ON COLUMN jobs.min_salary IS 'Minimum salary for a job title.';



COMMENT ON COLUMN jobs.max_salary IS 'Maximum salary for a job title';




CREATE SEQUENCE leave_app_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE leave_application_seq
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE leave_opening_balance_seq
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE leave_red_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE locations (
    location_id smallint NOT NULL,
    street_address character varying(40),
    postal_code character varying(12),
    city character varying(30) NOT NULL,
    state_province character varying(25),
    country_id character(2)
);




COMMENT ON TABLE locations IS 'Locations table that contains specific address of a specific office,
warehouse, and/or production site of a company. Does not store addresses /
locations of customers. Contains 23 rows; references with the
departments and countries tables. ';



COMMENT ON COLUMN locations.location_id IS 'Primary key of locations table';



COMMENT ON COLUMN locations.street_address IS 'Street address of an office, warehouse, or production site of a company.
Contains building number and street name';



COMMENT ON COLUMN locations.postal_code IS 'Postal code of the location of an office, warehouse, or production site
of a company. ';



COMMENT ON COLUMN locations.city IS 'A not null column that shows city where an office, warehouse, or
production site of a company is located. ';



COMMENT ON COLUMN locations.state_province IS 'State or Province where an office, warehouse, or production site of a
company is located.';



COMMENT ON COLUMN locations.country_id IS 'Country where an office, warehouse, or production site of a company is
located. Foreign key to country_id column of the countries table.';



CREATE SEQUENCE locations_seq
    START WITH 3300
    INCREMENT BY 100
    NO MINVALUE
    MAXVALUE 9900
    CACHE 1;




CREATE TABLE menutree (
    id bigint NOT NULL,
    name character varying(150),
    parentid bigint,
    actionid bigint,
    sortid bigint
);




CREATE MATERIALIZED VIEW mv2 AS
 SELECT av.id_basic_property AS basicpropertyid,
    COALESCE(currnt.amt_demand, (0)::numeric) AS aggregate_current_demand,
    COALESCE(currnt.amt_collected, (0)::double precision) AS current_collection,
    COALESCE(arrear.amt_demand, (0)::numeric) AS aggregate_arrear_demand,
    COALESCE(arrear.amt_collected, (0)::double precision) AS arrearcollection,
    av.att_value AS av_amount
   FROM egpt_mv_bp_curr_demand_av av,
    egpt_mv_current_dem_coll currnt,
    egpt_mv_arrear_dem_coll arrear
  WHERE ((av.id_basic_property = currnt.id_basic_property) AND (av.id_basic_property = arrear.id_basic_property))
  WITH NO DATA;




CREATE TABLE notice (
    id_notice bigint NOT NULL,
    id_module bigint,
    noticetype character varying(32),
    noticeno character varying(64),
    noticedate timestamp without time zone,
    objectno character varying(32),
    addressto character varying(2056),
    address character varying(256),
    id_user bigint,
    document text,
    is_blob character(1),
    document1 bytea,
    old_objectno character varying(32)
);




CREATE TABLE notice100 (
    id_notice bigint NOT NULL,
    id_basic_property bigint,
    notice_no bigint,
    owner_name character varying(256),
    owner_address character varying(256),
    arrear bigint,
    penalty bigint,
    nfa bigint,
    stay bigint,
    remand bigint,
    current_tax bigint,
    nf bigint,
    notice_iss_date timestamp without time zone,
    arr_date timestamp without time zone,
    addl_demand bigint,
    id_address bigint
);




CREATE TABLE notice72 (
    id_notice72 bigint NOT NULL,
    por_no bigint,
    id_basic_property bigint,
    seat_number bigint,
    notice_iss_date timestamp without time zone,
    owner_title character varying(8),
    owner character varying(256),
    address character varying(256),
    year bigint,
    area bigint,
    curr_arv bigint,
    currarv_remarks character varying(256),
    proposed_arv bigint,
    proparv_less bigint,
    proparv_remarks character varying(256),
    reason character varying(128)
);




CREATE TABLE otherbilldetail (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    billid bigint NOT NULL,
    payvhid bigint
);




CREATE TABLE othertaxdetail (
    id bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    interest double precision,
    penalty double precision,
    glcodeid bigint NOT NULL,
    totalamount double precision,
    isreversed smallint,
    period character varying(50)
);






CREATE TABLE qrtz_blob_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    blob_data bytea
);




CREATE TABLE qrtz_calendars (
    sched_name character varying(120) NOT NULL,
    calendar_name character varying(200) NOT NULL,
    calendar bytea NOT NULL
);




CREATE TABLE qrtz_cron_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    cron_expression character varying(120) NOT NULL,
    time_zone_id character varying(80)
);




CREATE TABLE qrtz_fired_triggers (
    sched_name character varying(120) NOT NULL,
    entry_id character varying(95) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    instance_name character varying(200) NOT NULL,
    fired_time bigint NOT NULL,
    sched_time bigint NOT NULL,
    priority integer NOT NULL,
    state character varying(16) NOT NULL,
    job_name character varying(200),
    job_group character varying(200),
    is_nonconcurrent boolean,
    requests_recovery boolean
);




CREATE TABLE qrtz_job_details (
    sched_name character varying(120) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    job_class_name character varying(250) NOT NULL,
    is_durable boolean NOT NULL,
    is_nonconcurrent boolean NOT NULL,
    is_update_data boolean NOT NULL,
    requests_recovery boolean NOT NULL,
    job_data bytea
);




CREATE TABLE qrtz_locks (
    sched_name character varying(120) NOT NULL,
    lock_name character varying(40) NOT NULL
);




CREATE TABLE qrtz_paused_trigger_grps (
    sched_name character varying(120) NOT NULL,
    trigger_group character varying(200) NOT NULL
);




CREATE TABLE qrtz_scheduler_state (
    sched_name character varying(120) NOT NULL,
    instance_name character varying(200) NOT NULL,
    last_checkin_time bigint NOT NULL,
    checkin_interval bigint NOT NULL
);




CREATE TABLE qrtz_simple_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    repeat_count bigint NOT NULL,
    repeat_interval bigint NOT NULL,
    times_triggered bigint NOT NULL
);




CREATE TABLE qrtz_simprop_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    str_prop_1 character varying(512),
    str_prop_2 character varying(512),
    str_prop_3 character varying(512),
    int_prop_1 integer,
    int_prop_2 integer,
    long_prop_1 bigint,
    long_prop_2 bigint,
    dec_prop_1 numeric(13,4),
    dec_prop_2 numeric(13,4),
    bool_prop_1 boolean,
    bool_prop_2 boolean
);




CREATE TABLE qrtz_triggers (
    sched_name character varying(120) NOT NULL,
    trigger_name character varying(200) NOT NULL,
    trigger_group character varying(200) NOT NULL,
    job_name character varying(200) NOT NULL,
    job_group character varying(200) NOT NULL,
    description character varying(250),
    next_fire_time bigint,
    prev_fire_time bigint,
    priority integer,
    trigger_state character varying(16) NOT NULL,
    trigger_type character varying(8) NOT NULL,
    start_time bigint NOT NULL,
    end_time bigint,
    calendar_name character varying(200),
    misfire_instr smallint,
    job_data bytea
);




CREATE TABLE regions (
    region_id bigint NOT NULL,
    region_name character varying(25)
);




CREATE TABLE relation (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(125) NOT NULL,
    address1 character varying(256) NOT NULL,
    address2 character varying(256),
    city character varying(50),
    pin character varying(50),
    phone character varying(50),
    fax character varying(50),
    contactperson character varying(50),
    mobile character varying(25),
    email character varying(25),
    narration character varying(250),
    isactive smallint NOT NULL,
    lastmodified timestamp without time zone DEFAULT ('now'::text)::timestamp without time zone NOT NULL,
    created timestamp without time zone NOT NULL,
    modifiedby bigint,
    relationtypeid bigint,
    tdsid bigint,
    glcodeid bigint,
    panno character varying(50),
    tinno character varying(50),
    bankaccount character varying(50),
    bankname character varying(50),
    pwdapprovalcode character varying(30),
    regno character varying(30),
    createdby bigint NOT NULL,
    statusid bigint,
    gradeid bigint,
    inactiveon timestamp without time zone,
    partytypeid bigint,
    subtypeid bigint,
    modeofpay character varying(10),
    bankaccounttype character varying(30),
    ifsccode character varying(50),
    bankid bigint,
    itexempted smallint DEFAULT 0
);




CREATE TABLE relationtype (
    id bigint NOT NULL,
    code character varying(10) NOT NULL,
    name character varying(10) NOT NULL,
    description character varying(250),
    isactive smallint,
    created timestamp without time zone NOT NULL,
    modifiedby bigint NOT NULL,
    lastmodified timestamp without time zone NOT NULL
);




CREATE SEQUENCE routerid
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_57_1_eg_address
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_58_1_eg_citizen
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_62_1_eg_entity
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_65_1_eg_user
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_66_1_egbd_addictionmaster
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_68_1_egbd_attbefrdeathma
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_69_1_egbd_attentionatdel
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_70_3_egbd_birthregistrat
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_70_3_egf_financialyear
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_72_1_egbd_citizenbddetai
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_73_1_egbd_citizendeathde
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_74_3_egbd_citizenrelatio
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_75_3_egbd_deathregistrat
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_76_1_egbd_educationmaster
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_77_1_egbd_establishmentm
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_78_1_egbd_establishmentt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_79_1_egbd_icdclassificat
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_80_1_egbd_icdmajorgroup
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_81_1_egbd_icdsubgroup
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_82_4_egbd_methodofdelive
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_83_1_egbd_occupationmast
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_87_1_egbd_relationmaster
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_88_1_egbd_religionmaster
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_89_egbd_citizenaddiction
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE s_egbd_certpayment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE schedulemap (
    id bigint,
    glcode character varying(20),
    lesscode character varying(20),
    prvcode character varying(20),
    oldcode character varying(20)
);




CREATE TABLE schedulemapping (
    id bigint NOT NULL,
    reporttype character varying(10) NOT NULL,
    schedule character varying(10) NOT NULL,
    schedulename character varying(250) NOT NULL,
    repsubtype character varying(10),
    createdby bigint NOT NULL,
    createddate timestamp without time zone NOT NULL,
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    isremission smallint
);




CREATE TABLE scheme (
    id bigint NOT NULL,
    code character varying(20),
    name character varying(50),
    validfrom timestamp without time zone,
    validto timestamp without time zone,
    isactive character varying(1),
    description character varying(255),
    fundid bigint,
    sectorid bigint,
    aaes bigint,
    fieldid bigint,
    createddate timestamp without time zone,
    lastmodifieddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);




CREATE TABLE screencontrols (
    id smallint NOT NULL,
    screenname character varying(50),
    controlname character varying(50)
);




CREATE SEQUENCE se_egf_rules
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_account_head
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_accountdetailkey
    START WITH 42
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_accountdetailtype
    START WITH 18
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_accountentitymaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_accountgroup
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_accounthead
    START WITH 75
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_address
    START WITH 9
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_address_type_master
    START WITH 10
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_ass
    START WITH 73
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_ass_dept
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_ass_prd
    START WITH 70
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_asset_sale_id
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_asset_status
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_attendence
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_auth_rule
    START WITH 17
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_banana
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bank
    START WITH 162
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bank_acc
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bankaccount
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bankbranch
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bankentries
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bankentries_mis
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bankreconciliation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_basic_prop
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bill
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_billcollector
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_billcollectordetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_billno
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_billnumber
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bills
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_birthreport_coll
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bndry_category
    START WITH 67
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_budget
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_bunch
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_calendaryear
    START WITH 9
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_category
    START WITH 11
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_cess
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_cessmaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_chartofaccountdetail
    START WITH 20
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_chartofaccounts
    START WITH 1544
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_cheque_dept_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_chequedetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_citizen
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_citizennamechange
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_closedperiods
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_code
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_codemapping
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_codeservicemap
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_collection_details
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_collection_mode
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_companydetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_complaintgroup
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;



CREATE SEQUENCE seq_compoff
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_constr_type
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_contractor
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_contractorbilldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_contrajournalvoucher
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_cregistrar
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_cregistrationunit
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_dcb
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_dcb_broker
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_deathreport_coll
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_demand
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_demandcalc
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_demandcalculations
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_demanddetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_demandreasonid
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_department
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_departmentfunctionmap
    START WITH 339
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_depreciation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_depreciationmaster
    START WITH 111
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_deptaccmapping
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_deptstoreitem_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_designation
    START WITH 40
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_dis_app
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_discountmaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_districtnamechange
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_drawing_officer
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_action
    START WITH 3654
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_actiondetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_address
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_advancereqdetails
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_advancereqpayeedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_advancerequisition
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_advancerequisitionmis
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_ageing_list
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_appconfig
    START WITH 471
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999999
    CACHE 1;




CREATE SEQUENCE seq_eg_appconfig_values
    START WITH 615
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 999999999
    CACHE 1;




CREATE SEQUENCE seq_eg_appdata
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999999
    CACHE 1;




CREATE SEQUENCE seq_eg_approvaldetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_asset
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_asset_depformula
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_asset_po
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_asset_sale
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_asset_status
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_assetcat
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_assetcategory
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_assetvaluechange
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_attributelist
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_attributetype
    START WITH 15
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_attributevalues
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bankservicemapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eg_bill
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bill_billno
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bill_details
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bill_subtype
    START WITH 26
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bill_type
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_billdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_billpayeedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_billreceipt
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_billregister
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_billregistermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bins
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bndry
    START WITH 16696
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_bndry_type
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_catalogue
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_checklists
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_city
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_crossheirarchy_linkage
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_deduction_details
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_demand
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_demand_agent
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_demand_details
    START WITH 13
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_demand_reason
    START WITH 913
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_demand_reason_details
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_demand_reason_master
    START WITH 27
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_department
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_deprecaitionmetadata
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_depreciation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_deprformula
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_dept
    START WITH 35
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_dept_do_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eg_digital_signed_docs
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eg_disbursement_mode
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_eg_district
    START WITH 101
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_domain
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_emp_assignment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_emp_assignment_prd
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999
    CACHE 1;




CREATE SEQUENCE seq_eg_employee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_eg_entity
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_heirarchy_type
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_ielist
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_improvements
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_integration_data
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_item
    START WITH 11
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_itemtype
    START WITH 8
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_location
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_location_ipmap
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_login_log
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_number_generic
    START WITH 801
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_numbers
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_partytype
    START WITH 10
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_reason_category
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_remittance
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_remittance_detail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_remittance_gldtl
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_revaluations
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_roleaction_map
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_roles
    START WITH 86
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_router
    START WITH 541
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_rulegroup
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_rules
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_ruletype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_salarycodes
    START WITH 17
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_securitydeposit
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_service_accountdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_service_subledgerinfo
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_servicecategory
    START WITH 5
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_servicedetails
    START WITH 16
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_state
    START WITH 100
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_stores
    START WITH 33
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_surrendered_cheques
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_taluk
    START WITH 205
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_tasks
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_token
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eg_uom
    START WITH 44
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_uomcategory
    START WITH 19
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_user
    START WITH 87
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_user_jurlevel
    START WITH 62
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_user_jurvalues
    START WITH 67
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_user_sign
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eg_usercounter_map
    START WITH 7
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_userrole
    START WITH 91
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eg_workstype
    START WITH 9
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_activities
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_depreciation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_disposalsalemis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_openingbalance
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_subledger
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_voucherdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egasset_voucherheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbd_diseasesmaster
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbd_nameincn_refnames
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbnd_bndcertissuereg
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpa_address
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_apprd_bldgdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_apprd_bldgflrdtls
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_autodcr
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_autodcr_floordetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_changeofusage
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpa_ddfee
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_inspect_measuredtls
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_inspection
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_inspection_checklist
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_inspection_details
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_lettertoparty
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_lpchecklist
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_bpafee
    START WITH 7
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_bpafeeline
    START WITH 10
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_buildgcategory
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_buildingusage
    START WITH 7
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_checklist
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_checklistdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_const_stages
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_inspectionsrc
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_lpreason
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_servicetype
    START WITH 8
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_surveyorname
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_mstr_village
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_regfee
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_regfeedtl
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_registration
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_regn_approvalinfo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpa_regn_autodcr
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_regn_checklist
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_regn_details
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_regnstatus_details
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_rejection
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_rejectionchklist
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egbpa_surroundedbldgdtls
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_address
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_apprd_bldgdet
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_apprd_bldgflrdt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_autodcr
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_autodcr_frdtls
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_changeofusage
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_ddfee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_dochistorydetl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_docket
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_docket_constrn
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_docket_document
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_docket_flrdetl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_docket_violtn
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_documenthistory
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_ins_checklist
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_ins_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_insmeasuredtls
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_inspection
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_layout
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_lettertoparty
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_lndbldngtypes
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_lpchecklist
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_blgusage
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_bpafee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_bpafeele
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_buildgcat
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_checklist
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_chklistdtl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_const_stg
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_inspsrc
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_lpreason
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_sertype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_surname
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_mstr_village
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regfee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regfeedtl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_registration
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regn_appinfo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regn_autodcr
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regn_chklist
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regn_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_regnstatu_dtl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_rejchklist
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_rejection
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_roadwidth
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_stormwaterdrain
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egbpaextnd_surdbldgdtls
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egcc_conservancy
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_accpayeedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_bank_remittance
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_challanheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collectiondetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collectionheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collectioninstrument
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collectionmis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collectionvoucher
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collpayeedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_collstg_achead_amount
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 999999999
    CACHE 1;




CREATE SEQUENCE seq_egcl_collstg_instrument
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 999999999
    CACHE 1;




CREATE SEQUENCE seq_egcl_collstg_receipt
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 999999999
    CACHE 1;




CREATE SEQUENCE seq_egcl_onlinepayments
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_trans_details
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_transaction_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egcl_transaction_header
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egdiary_activity_type
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdiary_attr_type_column
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdiary_attribute_values
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdiary_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdiary_header
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdiary_images
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdm_collected_receipts
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egdms_extnl_user
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_extnl_user_type
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_file_assignment
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_file_property
    START WITH 21
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_generic_file
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_intnl_user
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_notification
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_notification_group
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egdms_notification_user
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egeis_desig_hierarchy
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egeis_nominee_master
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egeis_status
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_account_cheques
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_accountcode_purpose
    START WITH 32
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_asset_status
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_brs_bankstatements
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_budget
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_budget_usage
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_budgetdetail
    START WITH 14
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 99999999999999
    CACHE 1;




CREATE SEQUENCE seq_egf_budgetgroup
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 99999999999999
    CACHE 1;




CREATE SEQUENCE seq_egf_cess_master
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_contract
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_contractheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_contractitemdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_contractpricedetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_contractpricedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_deprformula
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_deptissue
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_discount
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_dishonorchq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_dishonorchqdet
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_ebconsumer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_ebdetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_ebschedulerlog
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_ebschedulerlogdetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_ecstype
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_empdetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_empmaster
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_fixeddeposit
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_function_generalledger
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_fundflow
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_fundingagency
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_grant
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_instrumentaccountcodes
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_instrumentheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_instrumentotherdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_instrumenttype
    START WITH 11
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_instrumentvoucher
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_interbintransfer
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_interbintransferdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_interstoretransfer
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_invtxntypes
    START WITH 35
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999
    CACHE 1;




CREATE SEQUENCE seq_egf_issuedfrommrn
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_issuerepair
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_loangrantdetail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_loangrantheader
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_loangrantreceiptdetail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_mrheader
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_mrinheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_mrinline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_mrline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_mrnheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_mrnline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_mrnlineothers
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_numbers
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_poline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_reappropriation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_reappropriation_misc
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_receipt_mis
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_record_status
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_recovery_bankdetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_remit_schd_payment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_remittance_scheduler
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_rom
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_rpschedule
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_rpschedulemap
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_sale
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_scheme_bankaccount
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_subscheme_project
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_supplierbillpoadj
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_target_area
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_tax_account_mapping
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_tax_code
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_vouchermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egf_wardtargetarea_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egf_worksdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_communication
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_complaintdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_complaintgroup
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_complaintreceivingcen
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 99999999999999
    CACHE 1;




CREATE SEQUENCE seq_eggr_complainttypes
    START WITH 91
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_escalation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 9999999999999999
    CACHE 1;




CREATE SEQUENCE seq_eggr_forwardtracker
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_imagevideo_upload
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_qams_store
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eggr_redressaldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_region_dept
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_reminder
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_router
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eggr_statustracker
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_advocate_bill
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_advocate_master
    START WITH 24
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_appeal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_bill
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_bipartisandetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_case_stage
    START WITH 9
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_casetype_master
    START WITH 31
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_contempt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_court_master
    START WITH 44
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_courttype_master
    START WITH 12
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_governmentdepartment
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_hearings
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_intordertype_master
    START WITH 6
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_judgment
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_judgmentimpl
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_judgmenttype_master
    START WITH 7
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_lcinterimorder
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_legalcase
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_legalcase_advocate
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_legalcase_batchcase
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_legalcase_dept
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_legalcasedisposal
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_legalcasefee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_petitiontype_master
    START WITH 32
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_pwr
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_reportstatus
    START WITH 7
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_vacatestay_petition
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglc_vakalat
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_agrmtdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_allotte
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_baserent
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_estate
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_landtype
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_modeofacq
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_natureofbusiness
    START WITH 16
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_paymentcycle
    START WITH 5
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_policychange
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_rentinc
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_scfloordetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_taxrate
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_eglems_tcc
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_tender
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_tenderdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_tmtfloordetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_unit
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_unitdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eglems_usagetype
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egma_fieldcollection
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egp_activeservicereg
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egp_notification
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egp_portaluser
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egp_publicnotification
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egp_salary_billdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egp_salary_budget
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egp_serviceregistry
    START WITH 61
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egp_servicereqregistry
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egp_surveyordetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpay_nomination_header
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpay_nominee_payhead_det
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpay_payscale_employee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999
    CACHE 1;




CREATE SEQUENCE seq_egpay_payscale_incrdetails
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_adtnbasic_valmap
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 100
    CACHE 1;




CREATE SEQUENCE seq_egpen_advance
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_advance_schedule
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_arrears_detail
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_arrears_header
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_bank_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_basic_components
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_batchfailuredetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_billmisinfo_mapping
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_category
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_certificates
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_certsubdet
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_certsubheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_commtnvalue_mapping
    START WITH 63
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_commutation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_deathgrat_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_deductions
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_earnings
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_fpensioner_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_fpensioner_header
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_func_do_mapping
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_gratuity
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_monthly_pension
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_pay_commission
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_payhead_rates
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpen_payhead_rule
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_payheads
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_pension
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_pension_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_pension_types
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_pensionbanks
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_pensionbranches
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpen_pensioner_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_pensioner_header
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_pensioner_nominee
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_recovery_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpen_type_master
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999999999999
    CACHE 1;




CREATE SEQUENCE seq_egpt_collection_report
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpt_notice_gen
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpt_propertyid
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egpt_rcpt_rectification
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egpt_rcpt_rectify_details
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egstores_advrequisition
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_capitalissue
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_itemdeptdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_itemtypedetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egstores_mrnpodetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_poheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_poindentdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_poline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_polineother
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_ratecontract
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_rcontractdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_rcreqdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_rcreqslabdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_rcrequisition
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egstores_rcslabdetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egtl_department
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_history_trade_area
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_installed_motor
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_lic_duplic_info
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_lic_obj_info
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_license
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_license_cancel_info
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_license_revoke_info
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_app_type
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_area_fee
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_conserfee
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_erectionfee
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_pfa_fee
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_schedule
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_tradectgr
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_tradenature
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egtl_mstr_tradesubctgr
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_mstr_weight_fee
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_name_transfer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_renewal
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_trade
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egtl_tradenature
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egw_bill_deductions
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_emdrates_master
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_estimate_photo
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_egw_financing_source
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_fsdrates_master
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_satuschange
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_status
    START WITH 542
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_typeofwork
    START WITH 15
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_works_activity
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_works_budget
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_works_deductions
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_works_financingsource
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_works_mis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_workscategory
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_workssubcategory
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_egw_workstype
    START WITH 17
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_eis_deptdesig
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_emp_code
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_entityhistory
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_entityone
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_entitytwo
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_errortable
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_exemption
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_exemptionmaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_filterservices
    START WITH 13
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_financial_institution
    START WITH 6
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_financialyear
    START WITH 17
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_fiscalperiod
    START WITH 12
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_floor_detail
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_floordemandcalc
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_function
    START WITH 160
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_functionary
    START WITH 341
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_fund
    START WITH 25
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_fundsource
    START WITH 12
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_generalledger
    START WITH 6
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_generalledgerdetail
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_generalvouchermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_grade_eis
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_hib
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_historyattribs
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_holidays_ulb
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_hospital
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_hospitaltype
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_installment
    START WITH 67
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_integrationlog
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_interestmaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_intg_agg_accthd
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_intg_aggr_tx
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_intg_fin_aggregate
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_intg_misdata
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_issuewriteoffscrap_id
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_menutree
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_miscbilldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_modulemaster
    START WITH 310
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_monkey
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_mstr_prop_group
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_mstr_prop_subgroup
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_mutation_no
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_mutation_owners
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_notice100_id
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_notice100_no
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_notice72
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_notice72_id
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_notice_id
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_object_history
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_object_type
    START WITH 26
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_occupation_type_master
    START WITH 5
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_online_recptno
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_organizationstructure
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_otherbilldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_othertaxdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_payheader
    START WITH 8
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_payheader_details
    START WITH 23
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_paymentheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_payscale_employee
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_penalty
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_penaltymaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_pfdetails
    START WITH 2
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_pfheader
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_pftrigger_detail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 99999999999999
    CACHE 1;




CREATE SEQUENCE seq_pos
    START WITH 80
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_position_hir
    START WITH 18
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_create_reason
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_detail
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_effect_period
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_prop_integration
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_master_docs
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_prop_mutation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_mutation_mstr
    START WITH 27
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_ref
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_prop_type_master
    START WITH 11
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_propcorrections
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_propcorredetails
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_property_status_value
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_property_status_values
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_property_tenants
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_property_usage_master
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_propertyid
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_propertytaxintermediate
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_ptbillagent
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_ptdemand
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_rebate
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_rebatemaster
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_receiptheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_register
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_registrar
    START WITH 9
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_regkeys
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_relation
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_relationtype
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_salarybilldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_schedulemapping
    START WITH 527
    INCREMENT BY 1
    MINVALUE 0
    MAXVALUE 999999999999
    CACHE 20;




CREATE SEQUENCE seq_scheme
    START WITH 8
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_screencontrols
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_service_category
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_service_history
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_service_request
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_service_type
    START WITH 21
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_src_of_info
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_status
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_processindent
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_processindentline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_processindentmrline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_processindentquot
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_processtender
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_tenderfile
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_tenderline
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_stores_tenderstatus
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_storesopenbalancejobdetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_storesopeningbalance
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_struc_cl
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_sub_scheme
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_subledgerpaymentheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_supplier
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_supplierbilldetail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_tax_perc
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_taxes
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_tds
    START WITH 7
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_tenent
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_trans_voucher_num
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_transaction
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_transaction_header
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_transactiondetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_transactionlocation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_transactionsummary
    START WITH 3
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_transactionsummarydetails
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_trasaction_vouchernum
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_tx_agent
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_ulb
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_user_login
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_voucher_num
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_voucherdetail
    START WITH 4
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_voucherheader
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_vouchermis
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_w_dys
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_w_dys_constants
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfactivateprop
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_wfcitizen
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfcorrectprop
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE seq_wffloor_detail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfmodfloor_detail
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfmodifyprop
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfmutation
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfprop
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_wfprop_owners
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_workorder_estimate
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE seq_worksdetail
    START WITH 2
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE shared_fundsource (
    id bigint NOT NULL,
    fundsourceid bigint NOT NULL,
    subschemeid bigint NOT NULL,
    amount double precision NOT NULL
);




CREATE SEQUENCE sq_1_tjv_201213
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_1_tjv_cgvn_201213
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_4_tjv_201213
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_4_tjv_cgvn_201213
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_abstractestimate
    START WITH 5
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_abstractestimate_2001_02
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_abstractestimate_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_abstractestimate_2011_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_assessmentno
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_asset_build
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_asset_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_assetcategory_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_bdgt_apprn_2009_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_apprn_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_reapprn_be_2000_01
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_reapprn_be_2004_05
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_reapprn_be_2009_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_reapprn_be_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_reapprn_re_2009_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_bdgt_reapprn_re_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_budget_reappropriation
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_budget_reappropriation_be
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_budget_reappropriation_re
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2005
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2006
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2007
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2008
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2009
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2010
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2011
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2012
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2013
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2014
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2015
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2016
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2017
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2018
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2019
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_challan_2020
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_cj_fp6
    START WITH 3
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_cons_assessment_2012
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_assessment_2013
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_assessment_2014
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_assessment_2015
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_notice_2012
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_notice_2013
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_notice_2014
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_cons_notice_2015
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_contractorbill
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_contractorbill_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_ddiv_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_100
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_101
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_102
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_103
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_104
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_105
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_106
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_107
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_108
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_109
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_110
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_111
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_113
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_114
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_115
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_116
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_117
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_118
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_119
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_120
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_121
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_122
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_123
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_124
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_125
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_126
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_127
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_128
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_129
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_130
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_131
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_132
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_133
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_134
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_135
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_136
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_137
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_138
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_139
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_140
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_141
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_142
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_143
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_144
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_145
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_146
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_147
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_148
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_149
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_150
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_151
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_152
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_153
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_154
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_155
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_17
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_83
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_84
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_85
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_86
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_87
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_88
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_89
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_90
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_91
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_92
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_93
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_94
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_95
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_96
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_97
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_98
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ddiv_99
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_deposit
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_100
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_101
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_102
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_103
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_104
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_105
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_106
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_107
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_108
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_109
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_110
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_111
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_113
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_114
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_115
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_116
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_117
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_118
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_119
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_120
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_121
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_122
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_123
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_124
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_125
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_126
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_127
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_128
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_129
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_130
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_131
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_132
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_133
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_134
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_135
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_136
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_137
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_138
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_139
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_140
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_141
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_142
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_143
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_144
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_145
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_146
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_147
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_148
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_149
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_150
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_151
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_152
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_153
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_154
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_155
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_17
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_83
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_84
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_85
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_86
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_87
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_88
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_89
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_90
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_91
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_92
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_93
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_94
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_95
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_96
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_97
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_98
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_div_99
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_doc_num
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_eg_proftax_ack_no
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_employeecode
    START WITH 10001
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_file_num
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_legal_case
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_license_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_m__fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_m__fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_m_dbp_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_m_dbp_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_mdiv_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_100
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_101
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_102
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_103
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_104
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_105
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_106
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_107
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_108
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_109
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_110
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_111
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_113
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_114
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_115
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_116
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_117
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_118
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_119
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_120
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_121
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_122
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_123
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_124
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_125
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_126
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_127
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_128
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_129
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_130
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_131
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_132
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_133
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_134
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_135
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_136
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_137
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_138
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_139
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_140
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_141
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_142
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_143
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_144
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_145
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_146
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_147
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_148
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_149
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_150
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_151
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_152
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_153
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_154
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_155
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_17
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_83
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_84
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_85
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_86
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_87
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_88
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_89
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_90
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_91
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_92
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_93
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_94
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_95
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_96
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_97
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_98
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mdiv_99
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_mj_fp6
    START WITH 4
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_100
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_101
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_102
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_103
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_104
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_105
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_106
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_107
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_108
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_109
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_110
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_111
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_113
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_114
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_115
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_116
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_117
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_118
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_119
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_120
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_121
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_122
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_123
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_124
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_125
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_126
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_127
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_128
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_129
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_130
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_131
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_132
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_133
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_134
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_135
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_136
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_137
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_138
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_139
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_140
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_141
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_142
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_143
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_144
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_145
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_146
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_147
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_148
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_149
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_150
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_151
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_152
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_153
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_154
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_155
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_17
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_83
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_84
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_85
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_86
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_87
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_88
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_89
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_90
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_91
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_92
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_93
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_94
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_95
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_96
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_97
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_98
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_99
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_100
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_101
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_102
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_103
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_104
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_105
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_106
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_107
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_108
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_109
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_110
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_111
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_113
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_114
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_115
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_116
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_117
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_118
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_119
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_120
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_121
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_122
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_123
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_124
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_125
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_126
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_127
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_128
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_129
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_130
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_131
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_132
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_133
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_134
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_135
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_136
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_137
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_138
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_139
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_140
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_141
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_142
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_143
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_144
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_145
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_146
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_147
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_148
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_149
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_150
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_151
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_152
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_153
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_154
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_155
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_156
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_157
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_158
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_159
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_160
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_161
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_162
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_163
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_164
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_165
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_166
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_167
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_168
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_169
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_170
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_171
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_172
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_173
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_174
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_175
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_176
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_177
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_178
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_179
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_180
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_181
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_182
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_183
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_184
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_185
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_186
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_187
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_188
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_189
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_190
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_191
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_192
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_193
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_194
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_195
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_196
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_197
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_198
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_199
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_200
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_83
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_84
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_85
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_86
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_87
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_88
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_89
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_90
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_91
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_92
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_93
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_94
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_95
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_96
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_97
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_98
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_con_99
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_ndiv_tl_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_negotiation_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_negotiation_number_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_notice_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_o_454_cgvn_fp6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_454_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_454_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_51e_cgvn_fp6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_51e_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_51e_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_bpv_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_bpv_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_brv_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_brv_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_c_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_c_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_cjv_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_cjv_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_csl_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_dbp_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_dbp_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_gjv_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_gjv_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_j_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_j_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_j_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_j_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_jvg_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_jvg_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_msr_cgvn_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_msr_cgvn_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_p_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_p_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_r_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_o_r_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_po_aaaa_2009_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_projectcode_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_propertytax
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_amalg_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_amalg_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_amalg_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_amalg_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_amalg_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_amalg_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_bifur_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_bifur_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_bifur_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_bifur_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_bifur_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_bifur_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_18_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_cp_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_18_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp10_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_18_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_mp_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_18_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_nt_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_reconst_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_reconst_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_reconst_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_reconst_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_reconst_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_reconst_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_18_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_sb_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_13_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_14_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_15_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_16_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_17_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_18_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_pt_ss_19_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receipt
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2005
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2006
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2007
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2008
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2009
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2010
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2011
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2012
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2013
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2014
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2015
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2016
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2017
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2018
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2019
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptheader_2020
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2011_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2012_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2013_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2014_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2015_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2016_17
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2017_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2018_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2019_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_receiptref2020_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_revenue
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_rtgs_referencenumber
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    MAXVALUE 999999
    CACHE 2;




CREATE SEQUENCE sq_sal_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_sal_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_san_fp201112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_san_fp7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_tdiv_1
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_10
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_100
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_101
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_102
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_103
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_104
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_105
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_106
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_107
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_108
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_109
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_110
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_111
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_112
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_113
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_114
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_115
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_116
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_117
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_118
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_119
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_12
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_120
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_121
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_122
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_123
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_124
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_125
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_126
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_127
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_128
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_129
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_13
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_130
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_131
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_132
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_133
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_134
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_135
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_136
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_137
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_138
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_139
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_14
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_140
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_141
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_142
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_143
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_144
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_145
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_146
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_147
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_148
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_149
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_15
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_150
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_151
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_152
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_153
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_154
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_155
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_16
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_17
    START WITH 1000
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_18
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_19
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_2
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_20
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_21
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_22
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_23
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_24
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_25
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_26
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_27
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_28
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_29
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_3
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_30
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_31
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_32
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_33
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_34
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_35
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_36
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_37
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_38
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_39
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_4
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_40
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_41
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_42
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_43
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_44
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_45
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_46
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_47
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_48
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_49
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_5
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_50
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_51
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_52
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_53
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_54
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_55
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_56
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_57
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_58
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_59
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_6
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_60
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_61
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_62
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_63
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_64
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_65
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_66
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_67
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_68
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_69
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_7
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_70
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_71
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_72
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_73
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_74
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_75
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_76
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_77
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_78
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_79
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_8
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_80
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_81
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_82
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_83
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_84
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_85
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_86
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_87
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_88
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_89
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_9
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_90
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_91
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_92
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_93
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_94
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_95
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_96
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_97
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_98
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_tdiv_99
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_testworksdept_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_tlnt_license_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_workorder_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_workorder_number_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE SEQUENCE sq_workspackage_number
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 20;




CREATE SEQUENCE sq_workspackage_number_2010_11
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE sub_scheme (
    id bigint NOT NULL,
    code character varying(50) NOT NULL,
    name character varying(50) NOT NULL,
    validfrom timestamp without time zone NOT NULL,
    validto timestamp without time zone,
    isactive character varying(1) NOT NULL,
    schemeid bigint NOT NULL,
    lastmodifieddate timestamp without time zone NOT NULL,
    department bigint,
    initial_estimate_amount bigint,
    council_loan_proposal_number character varying(256),
    council_loan_proposal_date timestamp without time zone,
    council_admin_sanction_number character varying(256),
    council_admin_sanction_date timestamp without time zone,
    govt_loan_proposal_number character varying(256),
    govt_loan_proposal_date timestamp without time zone,
    govt_admin_sanction_number character varying(256),
    govt_admin_sanction_date timestamp without time zone,
    createddate timestamp without time zone,
    createdby bigint,
    lastmodifiedby bigint
);




CREATE SEQUENCE sys_gen_ref_num_seq
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




CREATE TABLE tds (
    id bigint NOT NULL,
    type character varying(20),
    ispaid smallint,
    glcodeid bigint,
    isactive smallint,
    lastmodified timestamp without time zone,
    created timestamp without time zone,
    modifiedby bigint,
    rate double precision,
    effectivefrom timestamp without time zone,
    createdby bigint NOT NULL,
    remitted character varying(100),
    bsrcode character varying(20),
    description character varying(200),
    partytypeid bigint,
    bankid bigint,
    caplimit double precision,
    isearning character varying(1),
    recoveryname character varying(50),
    calculationtype character varying(50),
    section character varying(50),
    recovery_mode character(1) DEFAULT 'M'::bpchar NOT NULL,
    remittance_mode character(1) DEFAULT 'M'::bpchar,
    ifsccode character varying(16),
    accountnumber character varying(32),
    CONSTRAINT tds_ma CHECK ((recovery_mode = ANY (ARRAY['M'::bpchar, 'A'::bpchar])))
);




CREATE SEQUENCE test
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;




CREATE TABLE transactionsummary (
    id bigint NOT NULL,
    glcodeid bigint NOT NULL,
    openingdebitbalance double precision NOT NULL,
    openingcreditbalance double precision NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    accountdetailtypeid bigint,
    accountdetailkey bigint,
    financialyearid bigint NOT NULL,
    fundid bigint,
    fundsourceid bigint,
    narration character varying(300),
    lastmodifiedby bigint,
    lastmodifieddate timestamp without time zone,
    departmentid bigint,
    functionaryid bigint,
    functionid smallint,
    divisionid bigint
);




CREATE VIEW v_eg_loc_boundary AS
 SELECT area.name AS area,
    location.name AS location,
    street.name AS streetname,
    street.bndry_num AS streetnumber
   FROM eg_boundary area,
    eg_boundary location,
    eg_boundary street
  WHERE (((((area.id_bndry_type = 5) AND (location.id_bndry_type = 6)) AND (street.id_bndry_type = 7)) AND (location.parent = area.id_bndry)) AND (street.parent = location.id_bndry));




CREATE VIEW v_eg_rev_boundary AS
 SELECT zone.name AS zonename,
    zone.bndry_num AS zonenumber,
    ward.name AS wardname,
    ward.bndry_num AS wardnumber
   FROM eg_boundary zone,
    eg_boundary ward
  WHERE (((zone.id_bndry_type = 2) AND (ward.id_bndry_type = 3)) AND (ward.parent = zone.id_bndry));




CREATE VIEW v_eg_role_action_module_map AS
 SELECT m.id_module AS module_id,
    m.module_desc AS module_name,
    m.parentid AS parent_id,
    NULL::bigint AS action_id,
    NULL::character varying AS action_name,
    NULL::text AS action_url,
    m.order_num AS order_number,
    'M'::text AS typeflag,
    m.isenabled AS is_enabled,
    NULL::character varying AS context_root
   FROM eg_module m
UNION
 SELECT NULL::bigint AS module_id,
    NULL::character varying AS module_name,
    a.module_id AS parent_id,
    a.id AS action_id,
    a.display_name AS action_name,
    ((a.url)::text ||
        CASE
            WHEN (a.queryparams IS NULL) THEN ''::text
            ELSE ('?'::text || (a.queryparams)::text)
        END) AS action_url,
    a.order_number,
    'A'::text AS typeflag,
    a.is_enabled,
    a.context_root
   FROM eg_action a;




CREATE VIEW v_egeis_empdetails AS
 SELECT ee.code AS empcode,
    ee.emp_firstname AS employeename,
    desig.designation_name AS designation,
    eap.from_date AS employeeassignfromdate,
    eap.to_date AS employeeassigntodate,
    dept.dept_name AS department,
    func.name AS section
   FROM eg_emp_assignment_prd eap,
    eg_emp_assignment eea,
    eg_employee ee,
    eg_designation desig,
    eg_department dept,
    functionary func
  WHERE ((((((ee.id = eap.id_employee) AND (eap.id = eea.id_emp_assign_prd)) AND (eea.designationid = desig.designationid)) AND (ee.isactive = 1)) AND ((eea.main_dept)::numeric = dept.id_dept)) AND (func.id = eea.id_functionary));




CREATE VIEW v_module_action_for_module AS
 SELECT DISTINCT v.module_id AS id,
    v.module_name AS name,
    NULL::text AS action_url
   FROM v_eg_role_action_module_map v
UNION
 SELECT DISTINCT v.action_id AS id,
    v.action_name AS name,
    v.action_url
   FROM v_eg_role_action_module_map v;




CREATE VIEW vegf_tax_cess AS
 SELECT c2.id,
    c3.name,
    c3.glcode,
    c1.percentage,
    c1.financialyearid,
    c3.purposeid,
    c1.isactive
   FROM egf_cess_master c1,
    egf_tax_code c2,
    chartofaccounts c3
  WHERE (((c1.taxcodeid = c2.id) AND (c1.glcodeid = c3.id)) AND (c1.isactive = 1));




CREATE TABLE voucherdetail (
    id bigint NOT NULL,
    lineid bigint NOT NULL,
    voucherheaderid bigint NOT NULL,
    glcode character varying(50) NOT NULL,
    accountname character varying(200) NOT NULL,
    debitamount double precision NOT NULL,
    creditamount double precision NOT NULL,
    narration character varying(250)
);




CREATE TABLE vouchermis (
    id bigint NOT NULL,
    billnumber bigint,
    divisionid bigint,
    schemename character varying(25),
    accountcode character varying(25),
    accounthead character varying(70),
    contractamt character varying(25),
    cashbook character varying(25),
    natureofwork character varying(25),
    assetdesc character varying(25),
    userdept character varying(25),
    demandno character varying(25),
    narration character varying(250),
    currentyear character varying(25),
    departmentid bigint,
    deptacchead character varying(25),
    subaccounthead character varying(25),
    projectcode bigint,
    concurrance_pn character varying(25),
    zonecode bigint,
    wardcode bigint,
    divisioncode bigint,
    month bigint,
    grossded character varying(25),
    emd_security character varying(25),
    netdeduction character varying(25),
    netamt character varying(25),
    totexpenditure character varying(25),
    voucherheaderid bigint,
    billregisterid character varying(20),
    acount_department bigint,
    projectfund bigint,
    concurrance_sn smallint,
    segmentid bigint,
    sub_segmentid bigint,
    updatedtimestamp timestamp without time zone,
    createtimestamp timestamp without time zone,
    iut_status character varying(20),
    iut_number character varying(30),
    fundsourceid bigint,
    schemeid bigint,
    subschemeid bigint,
    functionaryid bigint,
    sourcepath character varying(250),
    budgetary_appnumber character varying(30),
    budgetcheckreq smallint,
    functionid bigint
);




CREATE SEQUENCE wardid
    START WITH 1
    INCREMENT BY 1
    MINVALUE 0
    NO MAXVALUE
    CACHE 1;




ALTER TABLE ONLY accountdetailkey
    ADD CONSTRAINT accountdetailkey_pkey PRIMARY KEY (id);



ALTER TABLE ONLY accountdetailtype
    ADD CONSTRAINT accountdetailtype_attributename_key UNIQUE (attributename);



ALTER TABLE ONLY accountdetailtype
    ADD CONSTRAINT accountdetailtype_name_key UNIQUE (name);



ALTER TABLE ONLY accountdetailtype
    ADD CONSTRAINT accountdetailtype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT accountentitymaster_code_key UNIQUE (code);



ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT accountentitymaster_name_key UNIQUE (name);



ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT accountentitymaster_pkey PRIMARY KEY (id);



ALTER TABLE ONLY accountgroup
    ADD CONSTRAINT accountgroup_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_code_key UNIQUE (code);



ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_name_key UNIQUE (name);



ALTER TABLE ONLY bank
    ADD CONSTRAINT bank_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT bankaccount_branchid_accountnumber_key UNIQUE (branchid, accountnumber);



ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT bankaccount_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bankbranch
    ADD CONSTRAINT bankbranch_bankid_branchname_key UNIQUE (bankid, branchname);



ALTER TABLE ONLY bankbranch
    ADD CONSTRAINT bankbranch_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bankentries_mis
    ADD CONSTRAINT bankentries_mis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bankentries
    ADD CONSTRAINT bankentries_pkey PRIMARY KEY (id);



ALTER TABLE ONLY bankreconciliation
    ADD CONSTRAINT bankreconciliation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY calendaryear
    ADD CONSTRAINT calendaryear_calendaryear_key UNIQUE (calendaryear);



ALTER TABLE ONLY calendaryear
    ADD CONSTRAINT calendaryear_pkey PRIMARY KEY (id);



ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT chartofaccountdetail_glcodeid_detailtypeid_key UNIQUE (glcodeid, detailtypeid);



ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT chartofaccountdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT chartofaccounts_glcode_key UNIQUE (glcode);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT chartofaccounts_pkey PRIMARY KEY (id);



ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT cheque_dept_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY closedperiods
    ADD CONSTRAINT closedperiods_pkey PRIMARY KEY (id);



ALTER TABLE ONLY codemapping
    ADD CONSTRAINT codemapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY codeservicemap
    ADD CONSTRAINT codeservicemap_pkey PRIMARY KEY (id);



ALTER TABLE ONLY companydetail
    ADD CONSTRAINT companydetail_name_key UNIQUE (name);



ALTER TABLE ONLY companydetail
    ADD CONSTRAINT companydetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT contrajournalvoucher_pkey PRIMARY KEY (id);



ALTER TABLE ONLY ebpaextnd_dochist_detail
    ADD CONSTRAINT ebpaextnd_dochist_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_abstract_estimate_status
    ADD CONSTRAINT eg_abstract_estimate_status_pkey PRIMARY KEY (abstractestimate_id, status_id);



ALTER TABLE ONLY eg_action
    ADD CONSTRAINT eg_action_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_action
    ADD CONSTRAINT eg_action_url_queryparams_context_root_key UNIQUE (url, queryparams, context_root);



ALTER TABLE ONLY eg_actiondetails
    ADD CONSTRAINT eg_actiondetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_actionrg_map
    ADD CONSTRAINT eg_actionrg_map_rulegroupid_key UNIQUE (rulegroupid);



ALTER TABLE ONLY eg_address
    ADD CONSTRAINT eg_address_pkey PRIMARY KEY (addressid);



ALTER TABLE ONLY eg_address_type_master
    ADD CONSTRAINT eg_address_type_master_pkey PRIMARY KEY (id_address_type);



ALTER TABLE ONLY eg_adm_bndry_master
    ADD CONSTRAINT eg_adm_bndry_master_pkey PRIMARY KEY (id_bndry_master);



ALTER TABLE ONLY eg_adm_bndry
    ADD CONSTRAINT eg_adm_bndry_pkey PRIMARY KEY (id_adm_bndry);



ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT eg_advancereqpayeedetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT eg_advancerequisition_advancerequisitionnumber_key UNIQUE (advancerequisitionnumber);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT eg_advancerequisition_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT eg_advancerequisitiondetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT eg_advancerequisitionmis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_ageing_list
    ADD CONSTRAINT eg_ageing_list_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_appconfig
    ADD CONSTRAINT eg_appconfig_key_name_module_key UNIQUE (key_name, module);



ALTER TABLE ONLY eg_appconfig
    ADD CONSTRAINT eg_appconfig_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_appconfig_values
    ADD CONSTRAINT eg_appconfig_values_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_appl_domain
    ADD CONSTRAINT eg_appl_domain_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_asset_bills
    ADD CONSTRAINT eg_asset_bills_pkey PRIMARY KEY (billid);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT eg_asset_code_key UNIQUE (code);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT eg_asset_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_asset_po
    ADD CONSTRAINT eg_asset_po_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_asset_sale
    ADD CONSTRAINT eg_asset_sale_pkey PRIMARY KEY (asset_sale_id);



ALTER TABLE ONLY eg_asset_type
    ADD CONSTRAINT eg_asset_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT eg_assetcategory_code_key UNIQUE (code);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT eg_assetcategory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_assetvaluechange
    ADD CONSTRAINT eg_assetvaluechange_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_attributelist
    ADD CONSTRAINT eg_attributelist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_attributetype
    ADD CONSTRAINT eg_attributetype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_attributevalues
    ADD CONSTRAINT eg_attributevalues_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_audit_event
    ADD CONSTRAINT eg_audit_event_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_authorization_rule
    ADD CONSTRAINT eg_authorization_rule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_bankaccountservicemapping
    ADD CONSTRAINT eg_bankaccountservicemapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_bill_details
    ADD CONSTRAINT eg_bill_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_bill
    ADD CONSTRAINT eg_bill_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_bill_subtype
    ADD CONSTRAINT eg_bill_subtype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_bill_type
    ADD CONSTRAINT eg_bill_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT eg_billdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT eg_billpayeedetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_billreceipt
    ADD CONSTRAINT eg_billreceipt_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT eg_billregister_billnumber_key UNIQUE (billnumber);



ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT eg_billregister_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_bins
    ADD CONSTRAINT eg_bins_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_boundary
    ADD CONSTRAINT eg_boundary_pkey PRIMARY KEY (id_bndry);



ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT eg_boundary_type_id_heirarchy_type_hierarchy_key UNIQUE (id_heirarchy_type, hierarchy);



ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT eg_boundary_type_pkey PRIMARY KEY (id_bndry_type);



ALTER TABLE ONLY eg_catalogue
    ADD CONSTRAINT eg_catalogue_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_checklists
    ADD CONSTRAINT eg_checklists_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_citizen
    ADD CONSTRAINT eg_citizen_pkey PRIMARY KEY (citizenid);



ALTER TABLE ONLY eg_city_website
    ADD CONSTRAINT eg_city_website_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_controller
    ADD CONSTRAINT eg_controller_pkey PRIMARY KEY (cid);



ALTER TABLE ONLY eg_crossheirarchy_linkage
    ADD CONSTRAINT eg_crossheirarchy_linkage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT eg_deduction_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_demand_details
    ADD CONSTRAINT eg_demand_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_demand
    ADD CONSTRAINT eg_demand_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_demand_reason_details
    ADD CONSTRAINT eg_demand_reason_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_demand_reason_master
    ADD CONSTRAINT eg_demand_reason_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_demand_reason
    ADD CONSTRAINT eg_demand_reason_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_department
    ADD CONSTRAINT eg_department_dept_code_key UNIQUE (dept_code);



ALTER TABLE ONLY eg_department
    ADD CONSTRAINT eg_department_dept_name_key UNIQUE (dept_name);



ALTER TABLE ONLY eg_department
    ADD CONSTRAINT eg_department_pkey PRIMARY KEY (id_dept);



ALTER TABLE ONLY eg_depreciation
    ADD CONSTRAINT eg_depreciation_pkey PRIMARY KEY (id_depreciation);



ALTER TABLE ONLY eg_depreciationmetadata
    ADD CONSTRAINT eg_depreciationmetadata_pkey PRIMARY KEY (id_depreciationmetadata);



ALTER TABLE ONLY eg_dept_do_mapping
    ADD CONSTRAINT eg_dept_do_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_dept_functionmap
    ADD CONSTRAINT eg_dept_functionmap_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT eg_designation_designation_name_key UNIQUE (designation_name);



ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT eg_designation_pkey PRIMARY KEY (designationid);



ALTER TABLE ONLY eg_digital_signed_docs
    ADD CONSTRAINT eg_digital_signed_docs_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_disbursement_mode
    ADD CONSTRAINT eg_disbursement_mode_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_disbursement_mode
    ADD CONSTRAINT eg_disbursement_mode_type_key UNIQUE (type);



ALTER TABLE ONLY eg_drawingofficer
    ADD CONSTRAINT eg_drawingofficer_code_key UNIQUE (code);



ALTER TABLE ONLY eg_drawingofficer
    ADD CONSTRAINT eg_drawingofficer_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT eg_emp_assignment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_emp_assignment_prd
    ADD CONSTRAINT eg_emp_assignment_prd_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT eg_employee_code_key UNIQUE (code);



ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT eg_employee_dept_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT eg_employee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_entity_history
    ADD CONSTRAINT eg_entity_history_pkey PRIMARY KEY (id_history);



ALTER TABLE ONLY eg_entity
    ADD CONSTRAINT eg_entity_name_key UNIQUE (name);



ALTER TABLE ONLY eg_entity
    ADD CONSTRAINT eg_entity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_event_actions
    ADD CONSTRAINT eg_event_actions_pkey PRIMARY KEY (pkid);



ALTER TABLE ONLY eg_event
    ADD CONSTRAINT eg_event_eventid_key UNIQUE (eventid);



ALTER TABLE ONLY eg_financialyear
    ADD CONSTRAINT eg_financialyear_financialyear_key UNIQUE (financialyear);



ALTER TABLE ONLY eg_financialyear
    ADD CONSTRAINT eg_financialyear_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_heirarchy_type
    ADD CONSTRAINT eg_heirarchy_type_pkey PRIMARY KEY (id_heirarchy_type);



ALTER TABLE ONLY eg_heirarchy_type
    ADD CONSTRAINT eg_heirarchy_type_type_code_key UNIQUE (type_code);



ALTER TABLE ONLY eg_heirarchy_type
    ADD CONSTRAINT eg_heirarchy_type_type_name_key UNIQUE (type_name);



ALTER TABLE ONLY eg_history_attribs
    ADD CONSTRAINT eg_history_attribs_pkey PRIMARY KEY (id_hist_attr);



ALTER TABLE ONLY eg_ielist
    ADD CONSTRAINT eg_ielist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_installment_master
    ADD CONSTRAINT eg_installment_master_id_module_installment_num_installment_key UNIQUE (id_module, installment_num, installment_year, installment_type);



ALTER TABLE ONLY eg_installment_master
    ADD CONSTRAINT eg_installment_master_pkey PRIMARY KEY (id_installment);



ALTER TABLE ONLY eg_integration_data
    ADD CONSTRAINT eg_integration_data_aliasname_key UNIQUE (aliasname);



ALTER TABLE ONLY eg_integration_data
    ADD CONSTRAINT eg_integration_data_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_intg_agg_accthd
    ADD CONSTRAINT eg_intg_agg_accthd_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_intg_aggr_tx
    ADD CONSTRAINT eg_intg_aggr_tx_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_intg_fin_aggregate
    ADD CONSTRAINT eg_intg_fin_aggregate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_intg_misdata
    ADD CONSTRAINT eg_intg_misdata_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_invstatus
    ADD CONSTRAINT eg_invstatus_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT eg_item_itemno_key UNIQUE (itemno);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT eg_item_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_itemtype
    ADD CONSTRAINT eg_itemtype_itemtype_key UNIQUE (itemtype);



ALTER TABLE ONLY eg_itemtype
    ADD CONSTRAINT eg_itemtype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_location_ipmap
    ADD CONSTRAINT eg_location_ipmap_ipaddress_key UNIQUE (ipaddress);



ALTER TABLE ONLY eg_location_ipmap
    ADD CONSTRAINT eg_location_ipmap_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_location
    ADD CONSTRAINT eg_location_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_login_log
    ADD CONSTRAINT eg_login_log_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_master_default
    ADD CONSTRAINT eg_master_default_pkey PRIMARY KEY (defaultid);



ALTER TABLE ONLY eg_module
    ADD CONSTRAINT eg_module_module_name_key UNIQUE (module_name);



ALTER TABLE ONLY eg_module
    ADD CONSTRAINT eg_module_pkey PRIMARY KEY (id_module);



ALTER TABLE ONLY eg_modules
    ADD CONSTRAINT eg_modules_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_numbers
    ADD CONSTRAINT eg_numbers_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_numbers
    ADD CONSTRAINT eg_numbers_vouchertype_fiscialperiodid_key UNIQUE (vouchertype, fiscialperiodid);



ALTER TABLE ONLY eg_object_history
    ADD CONSTRAINT eg_object_history_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_object_type
    ADD CONSTRAINT eg_object_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_object_type
    ADD CONSTRAINT eg_object_type_type_key UNIQUE (type);



ALTER TABLE ONLY eg_partytype
    ADD CONSTRAINT eg_partytype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_portal_organization
    ADD CONSTRAINT eg_portal_organization_name_key UNIQUE (name);



ALTER TABLE ONLY eg_portal_organization
    ADD CONSTRAINT eg_portal_organization_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_portal_user
    ADD CONSTRAINT eg_portal_user_email_key UNIQUE (email);



ALTER TABLE ONLY eg_portal_user
    ADD CONSTRAINT eg_portal_user_mob_number_key UNIQUE (mob_number);



ALTER TABLE ONLY eg_portal_user
    ADD CONSTRAINT eg_portal_user_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_portal_user
    ADD CONSTRAINT eg_portal_user_username_key UNIQUE (username);



ALTER TABLE ONLY eg_position_hir
    ADD CONSTRAINT eg_position_hir_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_position_hir
    ADD CONSTRAINT eg_position_hir_position_from_position_to_object_type_id_key UNIQUE (position_from, position_to, object_type_id);



ALTER TABLE ONLY eg_position
    ADD CONSTRAINT eg_position_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_ptbill_agent
    ADD CONSTRAINT eg_ptbill_agent_pkey PRIMARY KEY (id_billagent);



ALTER TABLE ONLY eg_reason_category
    ADD CONSTRAINT eg_reason_category_pkey PRIMARY KEY (id_type);



ALTER TABLE ONLY eg_remittance_detail
    ADD CONSTRAINT eg_remittance_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_remittance_gldtl
    ADD CONSTRAINT eg_remittance_gldtl_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_remittance
    ADD CONSTRAINT eg_remittance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_revaluations
    ADD CONSTRAINT eg_revaluations_pkey PRIMARY KEY (revaluationid);



ALTER TABLE ONLY eg_rgrule_map
    ADD CONSTRAINT eg_rgrule_map_ruleid_key UNIQUE (ruleid);



ALTER TABLE ONLY eg_roles
    ADD CONSTRAINT eg_roles_pkey PRIMARY KEY (id_role);



ALTER TABLE ONLY eg_roles
    ADD CONSTRAINT eg_roles_role_name_key UNIQUE (role_name);



ALTER TABLE ONLY eg_router
    ADD CONSTRAINT eg_router_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_rulegroup
    ADD CONSTRAINT eg_rulegroup_name_key UNIQUE (name);



ALTER TABLE ONLY eg_rulegroup
    ADD CONSTRAINT eg_rulegroup_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_rules
    ADD CONSTRAINT eg_rules_name_key UNIQUE (name);



ALTER TABLE ONLY eg_rules
    ADD CONSTRAINT eg_rules_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_ruletype
    ADD CONSTRAINT eg_ruletype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_script
    ADD CONSTRAINT eg_script_name_start_date_end_date_key UNIQUE (name, start_date, end_date);



ALTER TABLE ONLY eg_script
    ADD CONSTRAINT eg_script_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_securitydeposit
    ADD CONSTRAINT eg_securitydeposit_pkey PRIMARY KEY (securitydepositid);



ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT eg_service_accountdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_service_subledgerinfo
    ADD CONSTRAINT eg_service_subledgerinfo_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_servicecategory
    ADD CONSTRAINT eg_servicecategory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_servicedetails
    ADD CONSTRAINT eg_servicedetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_stores
    ADD CONSTRAINT eg_stores_deptid_storename_key UNIQUE (deptid, storename);



ALTER TABLE ONLY eg_stores
    ADD CONSTRAINT eg_stores_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_stores
    ADD CONSTRAINT eg_stores_storeno_key UNIQUE (storeno);



ALTER TABLE ONLY eg_surrendered_cheques
    ADD CONSTRAINT eg_surrendered_cheques_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_tasks
    ADD CONSTRAINT eg_tasks_name_key UNIQUE (name);



ALTER TABLE ONLY eg_tasks
    ADD CONSTRAINT eg_tasks_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_terminal
    ADD CONSTRAINT eg_terminal_ip_address_key UNIQUE (ip_address);



ALTER TABLE ONLY eg_terminal
    ADD CONSTRAINT eg_terminal_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_terminal
    ADD CONSTRAINT eg_terminal_terminal_name_key UNIQUE (terminal_name);



ALTER TABLE ONLY eg_token
    ADD CONSTRAINT eg_token_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT eg_uom_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_uom
    ADD CONSTRAINT eg_uom_uom_key UNIQUE (uom);



ALTER TABLE ONLY eg_uomcategory
    ADD CONSTRAINT eg_uomcategory_category_key UNIQUE (category);



ALTER TABLE ONLY eg_uomcategory
    ADD CONSTRAINT eg_uomcategory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT eg_user_jurlevel_id_user_id_bndry_type_key UNIQUE (id_user, id_bndry_type);



ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT eg_user_jurlevel_pkey PRIMARY KEY (id_user_jurlevel);



ALTER TABLE ONLY eg_user_jurvalues
    ADD CONSTRAINT eg_user_jurvalues_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_user
    ADD CONSTRAINT eg_user_pkey PRIMARY KEY (id_user);



ALTER TABLE ONLY eg_user_sign
    ADD CONSTRAINT eg_user_sign_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_user
    ADD CONSTRAINT eg_user_user_name_key UNIQUE (user_name);



ALTER TABLE ONLY eg_usercounter_map
    ADD CONSTRAINT eg_usercounter_map_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT eg_userdetails_id_emp_key UNIQUE (id_emp);



ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT eg_userdetails_id_user_key UNIQUE (id_user);



ALTER TABLE ONLY eg_userdetails
    ADD CONSTRAINT eg_userdetails_pkey PRIMARY KEY (id_userdet);



ALTER TABLE ONLY eg_userrole
    ADD CONSTRAINT eg_userrole_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_view
    ADD CONSTRAINT eg_view_complaintnumber_key UNIQUE (complaintnumber);



ALTER TABLE ONLY eg_wf_actions
    ADD CONSTRAINT eg_wf_actions_name_type_key UNIQUE (name, type);



ALTER TABLE ONLY eg_wf_actions
    ADD CONSTRAINT eg_wf_actions_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_wf_amountrule
    ADD CONSTRAINT eg_wf_amountrule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_wf_matrix
    ADD CONSTRAINT eg_wf_matrix_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_wf_states
    ADD CONSTRAINT eg_wf_states_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eg_wf_types
    ADD CONSTRAINT eg_wf_types_pkey PRIMARY KEY (id_type);



ALTER TABLE ONLY eg_wf_types
    ADD CONSTRAINT eg_wf_types_wf_type_key UNIQUE (wf_type);



ALTER TABLE ONLY egasset_activities
    ADD CONSTRAINT egasset_activities_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egasset_depreciation
    ADD CONSTRAINT egasset_depreciation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egasset_disposalsalemis
    ADD CONSTRAINT egasset_disposalsalemis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egasset_openingbalance
    ADD CONSTRAINT egasset_openingbalance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egasset_subledger
    ADD CONSTRAINT egasset_subledger_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egasset_voucherdetail
    ADD CONSTRAINT egasset_voucherdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT egasset_voucherheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_address
    ADD CONSTRAINT egbpa_address_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_apprd_buildingdetails
    ADD CONSTRAINT egbpa_apprd_buildingdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_apprd_buildingfloordtls
    ADD CONSTRAINT egbpa_apprd_buildingfloordtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_autodcr_floordetails
    ADD CONSTRAINT egbpa_autodcr_floordetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_autodcr
    ADD CONSTRAINT egbpa_autodcr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_ddfee_details
    ADD CONSTRAINT egbpa_ddfee_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT egbpa_inspect_measurementdtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_inspection_checklist
    ADD CONSTRAINT egbpa_inspection_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_inspection_details
    ADD CONSTRAINT egbpa_inspection_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_inspection
    ADD CONSTRAINT egbpa_inspection_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_lettertoparty
    ADD CONSTRAINT egbpa_lettertoparty_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_lpchecklist
    ADD CONSTRAINT egbpa_lpchecklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT egbpa_mstr_bpafee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_bpafeedetail
    ADD CONSTRAINT egbpa_mstr_bpafeedetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_buildingcategory
    ADD CONSTRAINT egbpa_mstr_buildingcategory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_buildingusage
    ADD CONSTRAINT egbpa_mstr_buildingusage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_changeofusage
    ADD CONSTRAINT egbpa_mstr_changeofusage_name_key UNIQUE (name);



ALTER TABLE ONLY egbpa_mstr_changeofusage
    ADD CONSTRAINT egbpa_mstr_changeofusage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_checklist
    ADD CONSTRAINT egbpa_mstr_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_checklistdetail
    ADD CONSTRAINT egbpa_mstr_checklistdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_const_stages
    ADD CONSTRAINT egbpa_mstr_const_stages_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_inspectionsource
    ADD CONSTRAINT egbpa_mstr_inspectionsource_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_lpreason
    ADD CONSTRAINT egbpa_mstr_lpreason_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_servicetype
    ADD CONSTRAINT egbpa_mstr_servicetype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_surroundedbldgdtls
    ADD CONSTRAINT egbpa_mstr_surroundedbldgdtls_name_key UNIQUE (name);



ALTER TABLE ONLY egbpa_mstr_surroundedbldgdtls
    ADD CONSTRAINT egbpa_mstr_surroundedbldgdtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_surveyorname
    ADD CONSTRAINT egbpa_mstr_surveyorname_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_mstr_village
    ADD CONSTRAINT egbpa_mstr_village_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_registration_checklist
    ADD CONSTRAINT egbpa_registration_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT egbpa_registration_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_registrationfee
    ADD CONSTRAINT egbpa_registrationfee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_registrationfeedetail
    ADD CONSTRAINT egbpa_registrationfeedetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_regn_approvalinfo
    ADD CONSTRAINT egbpa_regn_approvalinfo_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_regn_autodcr
    ADD CONSTRAINT egbpa_regn_autodcr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_regn_details
    ADD CONSTRAINT egbpa_regn_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_regnstatus_details
    ADD CONSTRAINT egbpa_regnstatus_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_rejection_checklist
    ADD CONSTRAINT egbpa_rejection_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpa_rejection
    ADD CONSTRAINT egbpa_rejection_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_address
    ADD CONSTRAINT egbpaextnd_address_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_apprd_bldgdetails
    ADD CONSTRAINT egbpaextnd_apprd_bldgdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_apprd_bldgfloordtls
    ADD CONSTRAINT egbpaextnd_apprd_bldgfloordtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_autodcr_floordetail
    ADD CONSTRAINT egbpaextnd_autodcr_floordetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_autodcr
    ADD CONSTRAINT egbpaextnd_autodcr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_ddfee_details
    ADD CONSTRAINT egbpaextnd_ddfee_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_docket_constrnstage
    ADD CONSTRAINT egbpaextnd_docket_constrnstage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_docket_documentdtl
    ADD CONSTRAINT egbpaextnd_docket_documentdtl_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_docket_floordetails
    ADD CONSTRAINT egbpaextnd_docket_floordetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_docket
    ADD CONSTRAINT egbpaextnd_docket_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_docket_violationdtl
    ADD CONSTRAINT egbpaextnd_docket_violationdtl_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_documenthistory
    ADD CONSTRAINT egbpaextnd_documenthistory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_insp_checklist
    ADD CONSTRAINT egbpaextnd_insp_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT egbpaextnd_inspect_measuredtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_inspection_details
    ADD CONSTRAINT egbpaextnd_inspection_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT egbpaextnd_inspection_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_land_buildingtypes
    ADD CONSTRAINT egbpaextnd_land_buildingtypes_name_key UNIQUE (name);



ALTER TABLE ONLY egbpaextnd_land_buildingtypes
    ADD CONSTRAINT egbpaextnd_land_buildingtypes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_lettertoparty
    ADD CONSTRAINT egbpaextnd_lettertoparty_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_lpchecklist
    ADD CONSTRAINT egbpaextnd_lpchecklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_bldgcategory
    ADD CONSTRAINT egbpaextnd_mstr_bldgcategory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT egbpaextnd_mstr_bpafee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_bpafeedetail
    ADD CONSTRAINT egbpaextnd_mstr_bpafeedetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_buildingusage
    ADD CONSTRAINT egbpaextnd_mstr_buildingusage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_changeofusage
    ADD CONSTRAINT egbpaextnd_mstr_changeofusage_name_key UNIQUE (name);



ALTER TABLE ONLY egbpaextnd_mstr_changeofusage
    ADD CONSTRAINT egbpaextnd_mstr_changeofusage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_checklist
    ADD CONSTRAINT egbpaextnd_mstr_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_chklistdetail
    ADD CONSTRAINT egbpaextnd_mstr_chklistdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_const_stages
    ADD CONSTRAINT egbpaextnd_mstr_const_stages_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_inspsource
    ADD CONSTRAINT egbpaextnd_mstr_inspsource_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_layout
    ADD CONSTRAINT egbpaextnd_mstr_layout_name_key UNIQUE (name);



ALTER TABLE ONLY egbpaextnd_mstr_layout
    ADD CONSTRAINT egbpaextnd_mstr_layout_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_lpreason
    ADD CONSTRAINT egbpaextnd_mstr_lpreason_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_roadwidth
    ADD CONSTRAINT egbpaextnd_mstr_roadwidth_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_roadwidth
    ADD CONSTRAINT egbpaextnd_mstr_roadwidth_range_key UNIQUE (range);



ALTER TABLE ONLY egbpaextnd_mstr_servicetype
    ADD CONSTRAINT egbpaextnd_mstr_servicetype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_surnbldgdtls
    ADD CONSTRAINT egbpaextnd_mstr_surnbldgdtls_name_key UNIQUE (name);



ALTER TABLE ONLY egbpaextnd_mstr_surnbldgdtls
    ADD CONSTRAINT egbpaextnd_mstr_surnbldgdtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_surveyorname
    ADD CONSTRAINT egbpaextnd_mstr_surveyorname_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_village
    ADD CONSTRAINT egbpaextnd_mstr_village_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_mstr_village
    ADD CONSTRAINT egbpaextnd_mstr_village_villagename_key UNIQUE (villagename);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT egbpaextnd_registration_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_registrationfee
    ADD CONSTRAINT egbpaextnd_registrationfee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_regn_approvalinfo
    ADD CONSTRAINT egbpaextnd_regn_approvalinfo_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_regn_autodcr
    ADD CONSTRAINT egbpaextnd_regn_autodcr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_regn_checklist
    ADD CONSTRAINT egbpaextnd_regn_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_regn_details
    ADD CONSTRAINT egbpaextnd_regn_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_regnfeedetail
    ADD CONSTRAINT egbpaextnd_regnfeedetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_regnstatus_details
    ADD CONSTRAINT egbpaextnd_regnstatus_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_rejection_checklist
    ADD CONSTRAINT egbpaextnd_rejection_checklist_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_rejection
    ADD CONSTRAINT egbpaextnd_rejection_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egbpaextnd_stormwaterdrain
    ADD CONSTRAINT egbpaextnd_stormwaterdrain_dimension_key UNIQUE (dimension);



ALTER TABLE ONLY egbpaextnd_stormwaterdrain
    ADD CONSTRAINT egbpaextnd_stormwaterdrain_name_key UNIQUE (name);



ALTER TABLE ONLY egbpaextnd_stormwaterdrain
    ADD CONSTRAINT egbpaextnd_stormwaterdrain_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcc_conservancy_application
    ADD CONSTRAINT egcc_conservancy_application_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_accountpayeedetails
    ADD CONSTRAINT egcl_accountpayeedetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_bank_remittance
    ADD CONSTRAINT egcl_bank_remittance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_challanheader
    ADD CONSTRAINT egcl_challanheader_challannumber_key UNIQUE (challannumber);



ALTER TABLE ONLY egcl_challanheader
    ADD CONSTRAINT egcl_challanheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collection_mode_details
    ADD CONSTRAINT egcl_collection_mode_details_pkey PRIMARY KEY (id_coll_mode);



ALTER TABLE ONLY egcl_collectiondetails
    ADD CONSTRAINT egcl_collectiondetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collectionheader
    ADD CONSTRAINT egcl_collectionheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collectionheader
    ADD CONSTRAINT egcl_collectionheader_receiptnumber_key UNIQUE (receiptnumber);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT egcl_collectionmis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collectionpayeedetails
    ADD CONSTRAINT egcl_collectionpayeedetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collectionvoucher
    ADD CONSTRAINT egcl_collectionvoucher_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collstg_achead_amount
    ADD CONSTRAINT egcl_collstg_achead_amount_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collstg_instrument
    ADD CONSTRAINT egcl_collstg_instrument_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_collstg_receipt
    ADD CONSTRAINT egcl_collstg_receipt_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcl_onlinepayments
    ADD CONSTRAINT egcl_onlinepayments_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcsr_mstr_service_category
    ADD CONSTRAINT egcsr_mstr_service_category_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcsr_mstr_service_type
    ADD CONSTRAINT egcsr_mstr_service_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT egcsr_service_request_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdcb_depreciationmaster
    ADD CONSTRAINT egdcb_depreciationmaster_pkey PRIMARY KEY (id_depreciationmaster);



ALTER TABLE ONLY egdcb_rebatemaster
    ADD CONSTRAINT egdcb_rebatemaster_pkey PRIMARY KEY (id_rebatemaster);



ALTER TABLE ONLY egdiary_activity_type
    ADD CONSTRAINT egdiary_activity_type_attribute_name_key UNIQUE (attribute_name);



ALTER TABLE ONLY egdiary_activity_type
    ADD CONSTRAINT egdiary_activity_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdiary_attribute_type_column
    ADD CONSTRAINT egdiary_attribute_type_column_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdiary_attribute_values
    ADD CONSTRAINT egdiary_attribute_values_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdiary_details
    ADD CONSTRAINT egdiary_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdiary_header
    ADD CONSTRAINT egdiary_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdiary_images
    ADD CONSTRAINT egdiary_images_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdm_collected_receipts
    ADD CONSTRAINT egdm_collected_receipts_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_extnl_user
    ADD CONSTRAINT egdms_extnl_user_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_extnl_user_type
    ADD CONSTRAINT egdms_extnl_user_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_extnl_user_type
    ADD CONSTRAINT egdms_extnl_user_type_type_code_key UNIQUE (type_code);



ALTER TABLE ONLY egdms_extnl_user_type
    ADD CONSTRAINT egdms_extnl_user_type_type_name_key UNIQUE (type_name);



ALTER TABLE ONLY egdms_file_assignment
    ADD CONSTRAINT egdms_file_assignment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_file_category
    ADD CONSTRAINT egdms_file_category_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_file_priority
    ADD CONSTRAINT egdms_file_priority_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_file_property
    ADD CONSTRAINT egdms_file_property_name_property_type_key UNIQUE (name, property_type);



ALTER TABLE ONLY egdms_file_property
    ADD CONSTRAINT egdms_file_property_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_file_source
    ADD CONSTRAINT egdms_file_source_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT egdms_generic_file_file_number_key UNIQUE (file_number);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT egdms_generic_file_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_intnl_user
    ADD CONSTRAINT egdms_intnl_user_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT egdms_notification_group_name_key UNIQUE (name);



ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT egdms_notification_group_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egdms_notification
    ADD CONSTRAINT egdms_notification_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_att_type
    ADD CONSTRAINT egeis_att_type_id_key UNIQUE (id);



ALTER TABLE ONLY egeis_attendence
    ADD CONSTRAINT egeis_attendence_emp_id_att_date_key UNIQUE (emp_id, att_date);



ALTER TABLE ONLY egeis_attendence
    ADD CONSTRAINT egeis_attendence_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_bank_det
    ADD CONSTRAINT egeis_bank_det_pkey PRIMARY KEY (id_bank);



ALTER TABLE ONLY egeis_bloodgroup
    ADD CONSTRAINT egeis_bloodgroup_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_category_mstr
    ADD CONSTRAINT egeis_category_mstr_pkey PRIMARY KEY (category_id);



ALTER TABLE ONLY egeis_cert_details
    ADD CONSTRAINT egeis_cert_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_community_mstr
    ADD CONSTRAINT egeis_community_mstr_pkey PRIMARY KEY (community_id);



ALTER TABLE ONLY egeis_compoff
    ADD CONSTRAINT egeis_compoff_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_dept_tests
    ADD CONSTRAINT egeis_dept_tests_pkey PRIMARY KEY (dept_tests_id);



ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT egeis_deptdesig_desig_id_dept_id_key UNIQUE (desig_id, dept_id);



ALTER TABLE ONLY egeis_deptdesig
    ADD CONSTRAINT egeis_deptdesig_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_edu_details
    ADD CONSTRAINT egeis_edu_details_pkey PRIMARY KEY (education_details_id);



ALTER TABLE ONLY egeis_elig_cert_type
    ADD CONSTRAINT egeis_elig_cert_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_employee_grade
    ADD CONSTRAINT egeis_employee_grade_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_grade_value_key UNIQUE (grade_value);



ALTER TABLE ONLY egeis_grade_mstr
    ADD CONSTRAINT egeis_grade_mstr_pkey PRIMARY KEY (grade_id);



ALTER TABLE ONLY egeis_holidays
    ADD CONSTRAINT egeis_holidays_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_how_acquired_mstr
    ADD CONSTRAINT egeis_how_acquired_mstr_pkey PRIMARY KEY (how_acquired_id);



ALTER TABLE ONLY egeis_immovable_prop_details
    ADD CONSTRAINT egeis_immovable_prop_details_pkey PRIMARY KEY (imm_property_details_id);



ALTER TABLE ONLY egeis_lang_known
    ADD CONSTRAINT egeis_lang_known_pkey PRIMARY KEY (id_lang);



ALTER TABLE ONLY egeis_languages_known_mstr
    ADD CONSTRAINT egeis_languages_known_mstr_pkey PRIMARY KEY (languages_known_id);



ALTER TABLE ONLY egeis_leave_application
    ADD CONSTRAINT egeis_leave_application_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_leave_approval
    ADD CONSTRAINT egeis_leave_approval_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_leave_mstr
    ADD CONSTRAINT egeis_leave_mstr_desig_id_leave_type_id_key UNIQUE (desig_id, leave_type_id);



ALTER TABLE ONLY egeis_leave_mstr
    ADD CONSTRAINT egeis_leave_mstr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_leave_opening_balance
    ADD CONSTRAINT egeis_leave_opening_balance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_leave_status
    ADD CONSTRAINT egeis_leave_status_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_local_lang_qul_mstr
    ADD CONSTRAINT egeis_local_lang_qul_mstr_pkey PRIMARY KEY (qulified_id);



ALTER TABLE ONLY egeis_ltc_pirticulars
    ADD CONSTRAINT egeis_ltc_pirticulars_pkey PRIMARY KEY (ltc_details_id);



ALTER TABLE ONLY egeis_mode_of_recruiment_mstr
    ADD CONSTRAINT egeis_mode_of_recruiment_mstr_pkey PRIMARY KEY (mode_of_recruiment_id);



ALTER TABLE ONLY egeis_movable_prop_details
    ADD CONSTRAINT egeis_movable_prop_details_pkey PRIMARY KEY (mov_property_details_id);



ALTER TABLE ONLY egeis_name_of_test_mstr
    ADD CONSTRAINT egeis_name_of_test_mstr_pkey PRIMARY KEY (name_of_test_id);



ALTER TABLE ONLY egeis_nomimation_pirticulars
    ADD CONSTRAINT egeis_nomimation_pirticulars_pkey PRIMARY KEY (nom_details_id);



ALTER TABLE ONLY egeis_nominee_master
    ADD CONSTRAINT egeis_nominee_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_pay_fixed_in_mstr
    ADD CONSTRAINT egeis_pay_fixed_in_mstr_pkey PRIMARY KEY (pay_fixed_in_id);



ALTER TABLE ONLY egeis_person_address
    ADD CONSTRAINT egeis_person_address_pkey PRIMARY KEY (id_person_address);



ALTER TABLE ONLY egeis_posting_type_mstr
    ADD CONSTRAINT egeis_posting_type_mstr_pkey PRIMARY KEY (posting_type_id);



ALTER TABLE ONLY egeis_posting_type_mstr
    ADD CONSTRAINT egeis_posting_type_mstr_posting_type_name_key UNIQUE (posting_type_name);



ALTER TABLE ONLY egeis_probation
    ADD CONSTRAINT egeis_probation_pkey PRIMARY KEY (id_probation);



ALTER TABLE ONLY egeis_recruitment_type_mstr
    ADD CONSTRAINT egeis_recruitment_type_mstr_pkey PRIMARY KEY (recruitment_type_id);



ALTER TABLE ONLY egeis_regularisation
    ADD CONSTRAINT egeis_regularisation_pkey PRIMARY KEY (regularisation_id);



ALTER TABLE ONLY egeis_rel_nom_cert_reqd
    ADD CONSTRAINT egeis_rel_nom_cert_reqd_id_cert_type_id_relation_type_key UNIQUE (id_cert_type, id_relation_type);



ALTER TABLE ONLY egeis_rel_nom_cert_reqd
    ADD CONSTRAINT egeis_rel_nom_cert_reqd_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_relation_type
    ADD CONSTRAINT egeis_relation_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_religion_mstr
    ADD CONSTRAINT egeis_religion_mstr_pkey PRIMARY KEY (religion_id);



ALTER TABLE ONLY egeis_service_history
    ADD CONSTRAINT egeis_service_history_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egeis_skill_mstr
    ADD CONSTRAINT egeis_skill_mstr_pkey PRIMARY KEY (skill_id);



ALTER TABLE ONLY egeis_technical_grades_mstr
    ADD CONSTRAINT egeis_technical_grades_mstr_pkey PRIMARY KEY (teck_grades_id);



ALTER TABLE ONLY egeis_tecnical_qualification
    ADD CONSTRAINT egeis_tecnical_qualification_pkey PRIMARY KEY (tecnical_qulification_id);



ALTER TABLE ONLY egeis_training_pirticulars
    ADD CONSTRAINT egeis_training_pirticulars_pkey PRIMARY KEY (training_details_id);



ALTER TABLE ONLY egeis_type_master
    ADD CONSTRAINT egeis_type_master_pkey PRIMARY KEY (status_id);



ALTER TABLE ONLY egeis_type_of_leave_mstr
    ADD CONSTRAINT egeis_type_of_leave_mstr_pkey PRIMARY KEY (type_of_leave_id);



ALTER TABLE ONLY egeis_type_of_leave_mstr
    ADD CONSTRAINT egeis_type_of_leave_mstr_type_of_leave_value_key UNIQUE (type_of_leave_value);



ALTER TABLE ONLY egeis_working_constants
    ADD CONSTRAINT egeis_working_constants_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_account_cheques
    ADD CONSTRAINT egf_account_cheques_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_accountcode_purpose
    ADD CONSTRAINT egf_accountcode_purpose_name_key UNIQUE (name);



ALTER TABLE ONLY egf_accountcode_purpose
    ADD CONSTRAINT egf_accountcode_purpose_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_brs_bankstatements
    ADD CONSTRAINT egf_brs_bankstatements_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT egf_budget_name_key UNIQUE (name);



ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT egf_budget_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT egf_budget_reappropriation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_budget_usage
    ADD CONSTRAINT egf_budget_usage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT egf_budgetdetail_budget_budgetgroup_scheme_subscheme_functi_key UNIQUE (budget, budgetgroup, scheme, subscheme, functionary, function, executing_department, fund);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT egf_budgetdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT egf_budgetgroup_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_cess_master
    ADD CONSTRAINT egf_cess_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_deprformula
    ADD CONSTRAINT egf_deprformula_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_deptissue
    ADD CONSTRAINT egf_deptissue_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishonorcheque_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishonorcheque_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT egf_ebconsumer_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT egf_ebdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_ebschedulerlog
    ADD CONSTRAINT egf_ebschedulerlog_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_ebschedulerlogdetails
    ADD CONSTRAINT egf_ebschedulerlogdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_ecstype
    ADD CONSTRAINT egf_ecstype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_ecstype
    ADD CONSTRAINT egf_ecstype_type_key UNIQUE (type);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT egf_fixeddeposit_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_function_generalledger
    ADD CONSTRAINT egf_function_generalledger_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT egf_fundflow_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_fundingagency
    ADD CONSTRAINT egf_fundingagency_code_key UNIQUE (code);



ALTER TABLE ONLY egf_fundingagency
    ADD CONSTRAINT egf_fundingagency_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT egf_grant_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT egf_instrumentaccountcodes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT egf_instrumentheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_instrumenttype
    ADD CONSTRAINT egf_instrumenttype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_instrumenttype
    ADD CONSTRAINT egf_instrumenttype_type_key UNIQUE (type);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT egf_interbintransfer_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_interbintransferdetail
    ADD CONSTRAINT egf_interbintransferdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_interstoretransfer
    ADD CONSTRAINT egf_interstoretransfer_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_issuedfrommrn
    ADD CONSTRAINT egf_issuedfrommrn_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_issuerepair
    ADD CONSTRAINT egf_issuerepair_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT egf_loangrantdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_loangrantheader
    ADD CONSTRAINT egf_loangrantheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT egf_loangrantreceiptdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT egf_mrheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT egf_mrinheader_mrinno_key UNIQUE (mrinno);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT egf_mrinheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrinline
    ADD CONSTRAINT egf_mrinline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrinpurpose
    ADD CONSTRAINT egf_mrinpurpose_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrline
    ADD CONSTRAINT egf_mrline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT egf_mrnheader_mrnno_key UNIQUE (mrnno);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT egf_mrnheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrnline
    ADD CONSTRAINT egf_mrnline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_mrnlineothers
    ADD CONSTRAINT egf_mrnlineothers_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_openingbalance_jobdetail
    ADD CONSTRAINT egf_openingbalance_jobdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_poline
    ADD CONSTRAINT egf_poline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_reappropriation_misc
    ADD CONSTRAINT egf_reappropriation_misc_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_reappropriation_misc
    ADD CONSTRAINT egf_reappropriation_misc_sequence_number_key UNIQUE (sequence_number);



ALTER TABLE ONLY egf_record_status
    ADD CONSTRAINT egf_record_status_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_recovery_bankdetails
    ADD CONSTRAINT egf_recovery_bankdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_remittance_schd_payment
    ADD CONSTRAINT egf_remittance_schd_payment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_remittance_scheduler
    ADD CONSTRAINT egf_remittance_scheduler_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_rom
    ADD CONSTRAINT egf_rom_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_rpreport_schedulemapping
    ADD CONSTRAINT egf_rpreport_schedulemapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_rpreport_schedulemaster
    ADD CONSTRAINT egf_rpreport_schedulemaster_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_sale
    ADD CONSTRAINT egf_sale_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT egf_scheme_bankaccount_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_storesopeningbalance
    ADD CONSTRAINT egf_storesopeningbalance_financialyearid_storeid_binid_item_key UNIQUE (financialyearid, storeid, binid, itemid);



ALTER TABLE ONLY egf_storesopeningbalance
    ADD CONSTRAINT egf_storesopeningbalance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_subscheme_project
    ADD CONSTRAINT egf_subscheme_project_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_supplierbillpoadjustment
    ADD CONSTRAINT egf_supplierbillpoadjustment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_target_area
    ADD CONSTRAINT egf_target_area_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_tax_account_mapping
    ADD CONSTRAINT egf_tax_account_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_tax_code
    ADD CONSTRAINT egf_tax_code_code_key UNIQUE (code);



ALTER TABLE ONLY egf_tax_code
    ADD CONSTRAINT egf_tax_code_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egf_wardtargetarea_mapping
    ADD CONSTRAINT egf_wardtargetarea_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_accident_details
    ADD CONSTRAINT egfms_accident_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_fitness_details
    ADD CONSTRAINT egfms_fitness_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_greentax_details
    ADD CONSTRAINT egfms_greentax_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_marchout_details
    ADD CONSTRAINT egfms_marchout_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_marchout_header
    ADD CONSTRAINT egfms_marchout_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_marchout_otherdetails
    ADD CONSTRAINT egfms_marchout_otherdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_marchout_vehicles
    ADD CONSTRAINT egfms_marchout_vehicles_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_motortax_details
    ADD CONSTRAINT egfms_motortax_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_vehicle_assignment
    ADD CONSTRAINT egfms_vehicle_assignment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_vehicle_category
    ADD CONSTRAINT egfms_vehicle_category_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_vehicle_header
    ADD CONSTRAINT egfms_vehicle_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_vehicle_header
    ADD CONSTRAINT egfms_vehicle_header_veh_number_key UNIQUE (veh_number);



ALTER TABLE ONLY egfms_vehicle_insurance
    ADD CONSTRAINT egfms_vehicle_insurance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egfms_vehicle_service
    ADD CONSTRAINT egfms_vehicle_service_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eggis_bndry_dim
    ADD CONSTRAINT eggis_bndry_dim_pkey PRIMARY KEY (dimid);



ALTER TABLE ONLY eggr_communication
    ADD CONSTRAINT eggr_communication_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eggr_comp_receiving_modes
    ADD CONSTRAINT eggr_comp_receiving_modes_pkey PRIMARY KEY (comp_id);



ALTER TABLE ONLY eggr_complaintdetails
    ADD CONSTRAINT eggr_complaintdetails_complaintnumber_key UNIQUE (complaintnumber);



ALTER TABLE ONLY eggr_complaintdetails
    ADD CONSTRAINT eggr_complaintdetails_pkey PRIMARY KEY (complaintid);



ALTER TABLE ONLY eggr_complaintgroup
    ADD CONSTRAINT eggr_complaintgroup_pkey PRIMARY KEY (id_complaintgroup);



ALTER TABLE ONLY eggr_complaintreceivingcenter
    ADD CONSTRAINT eggr_complaintreceivingcenter_pkey PRIMARY KEY (centerid);



ALTER TABLE ONLY eggr_complaintstatus
    ADD CONSTRAINT eggr_complaintstatus_pkey PRIMARY KEY (complaintstatusid);



ALTER TABLE ONLY eggr_complainttypes
    ADD CONSTRAINT eggr_complainttypes_complaint_type_code_key UNIQUE (complaint_type_code);



ALTER TABLE ONLY eggr_complainttypes
    ADD CONSTRAINT eggr_complainttypes_pkey PRIMARY KEY (complainttypeid);



ALTER TABLE ONLY eggr_forward_tracker
    ADD CONSTRAINT eggr_forward_tracker_pkey PRIMARY KEY (fid);



ALTER TABLE ONLY eggr_imagevideo_upload
    ADD CONSTRAINT eggr_imagevideo_upload_pkey PRIMARY KEY (uploadid);



ALTER TABLE ONLY eggr_priority
    ADD CONSTRAINT eggr_priority_pkey PRIMARY KEY (priorityid);



ALTER TABLE ONLY eggr_qams_store
    ADD CONSTRAINT eggr_qams_store_operator_id_fdbk_obj_ref_key UNIQUE (operator_id, fdbk_obj_ref);



ALTER TABLE ONLY eggr_qams_store
    ADD CONSTRAINT eggr_qams_store_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eggr_redressaldetails
    ADD CONSTRAINT eggr_redressaldetails_pkey PRIMARY KEY (redressalid);



ALTER TABLE ONLY eggr_region_dept_mapping
    ADD CONSTRAINT eggr_region_dept_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eggr_status_tracker
    ADD CONSTRAINT eggr_status_tracker_pkey PRIMARY KEY (pkid);



ALTER TABLE ONLY eglc_advocate_bill
    ADD CONSTRAINT eglc_advocate_bill_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_advocate_master
    ADD CONSTRAINT eglc_advocate_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_appeal
    ADD CONSTRAINT eglc_appeal_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_bill
    ADD CONSTRAINT eglc_bill_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_bipartisandetails
    ADD CONSTRAINT eglc_bipartisandetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_case_stage
    ADD CONSTRAINT eglc_case_stage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_casetype_master
    ADD CONSTRAINT eglc_casetype_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_contempt
    ADD CONSTRAINT eglc_contempt_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_court_master
    ADD CONSTRAINT eglc_court_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_courttype_master
    ADD CONSTRAINT eglc_courttype_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_governmentdepartment
    ADD CONSTRAINT eglc_governmentdepartment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_hearings
    ADD CONSTRAINT eglc_hearings_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_interimtype_master
    ADD CONSTRAINT eglc_interimtype_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_judgment
    ADD CONSTRAINT eglc_judgment_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_judgmentimpl
    ADD CONSTRAINT eglc_judgmentimpl_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_judgmenttype_master
    ADD CONSTRAINT eglc_judgmenttype_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_lcinterimorder
    ADD CONSTRAINT eglc_lcinterimorder_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_legalcase_advocate
    ADD CONSTRAINT eglc_legalcase_advocate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_legalcase_batchcase
    ADD CONSTRAINT eglc_legalcase_batchcase_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_legalcase_dept
    ADD CONSTRAINT eglc_legalcase_dept_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT eglc_legalcase_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_legalcasedisposal
    ADD CONSTRAINT eglc_legalcasedisposal_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_legalcasefee
    ADD CONSTRAINT eglc_legalcasefee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_petitiontype_master
    ADD CONSTRAINT eglc_petitiontype_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_pwr
    ADD CONSTRAINT eglc_pwr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_reportstatus
    ADD CONSTRAINT eglc_reportstatus_code_key UNIQUE (code);



ALTER TABLE ONLY eglc_reportstatus
    ADD CONSTRAINT eglc_reportstatus_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_vacatestay_petition
    ADD CONSTRAINT eglc_vacatestay_petition_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglc_vakalat
    ADD CONSTRAINT eglc_vakalat_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT eglems_agreementdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_allottee
    ADD CONSTRAINT eglems_allottee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT eglems_baserent_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT eglems_estate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT eglems_land_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_landtype
    ADD CONSTRAINT eglems_landtype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_modeofacquisition
    ADD CONSTRAINT eglems_modeofacquisition_code_key UNIQUE (code);



ALTER TABLE ONLY eglems_modeofacquisition
    ADD CONSTRAINT eglems_modeofacquisition_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_natureofbusiness
    ADD CONSTRAINT eglems_natureofbusiness_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_paymentcycle
    ADD CONSTRAINT eglems_paymentcycle_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_policychange
    ADD CONSTRAINT eglems_policychange_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_quarter
    ADD CONSTRAINT eglems_quarter_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_rentincrement
    ADD CONSTRAINT eglems_rentincrement_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_scfloordetail
    ADD CONSTRAINT eglems_scfloordetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_shop
    ADD CONSTRAINT eglems_shop_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_shoppingcomplex
    ADD CONSTRAINT eglems_shoppingcomplex_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_taxrate
    ADD CONSTRAINT eglems_taxrate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_tcc
    ADD CONSTRAINT eglems_tcc_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_tcc
    ADD CONSTRAINT eglems_tcc_tccnumber_key UNIQUE (tccnumber);



ALTER TABLE ONLY eglems_tender
    ADD CONSTRAINT eglems_tender_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_tenderdetail
    ADD CONSTRAINT eglems_tenderdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_tenement
    ADD CONSTRAINT eglems_tenement_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_tmtfloordetail
    ADD CONSTRAINT eglems_tmtfloordetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_unit
    ADD CONSTRAINT eglems_unit_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_unitdetails
    ADD CONSTRAINT eglems_unitdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY eglems_usagetype
    ADD CONSTRAINT eglems_usagetype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_actions
    ADD CONSTRAINT egp_actions_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_citizenactiveservcreg
    ADD CONSTRAINT egp_citizenactiveservcreg_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_citizenactiveservcreg
    ADD CONSTRAINT egp_citizenactiveservcreg_referenceno_key UNIQUE (referenceno);



ALTER TABLE ONLY egp_citizennotification
    ADD CONSTRAINT egp_citizennotification_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_citizenserviceregistry
    ADD CONSTRAINT egp_citizenserviceregistry_actionid_actiontype_parentid_key UNIQUE (actionid, actiontype, parentid);



ALTER TABLE ONLY egp_citizenserviceregistry
    ADD CONSTRAINT egp_citizenserviceregistry_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_citizenservicereqregistry
    ADD CONSTRAINT egp_citizenservicereqregistry_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_citizenservicereqregistry
    ADD CONSTRAINT egp_citizenservicereqregistry_requestid_key UNIQUE (requestid);



ALTER TABLE ONLY egp_firm
    ADD CONSTRAINT egp_firm_name_key UNIQUE (name);



ALTER TABLE ONLY egp_firmactiveservcreg
    ADD CONSTRAINT egp_firmactiveservcreg_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_firmactiveservcreg
    ADD CONSTRAINT egp_firmactiveservcreg_referenceno_key UNIQUE (referenceno);



ALTER TABLE ONLY egp_firmnotification
    ADD CONSTRAINT egp_firmnotification_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_firmserviceregistry
    ADD CONSTRAINT egp_firmserviceregistry_actionid_actiontype_parentid_key UNIQUE (actionid, actiontype, parentid);



ALTER TABLE ONLY egp_firmserviceregistry
    ADD CONSTRAINT egp_firmserviceregistry_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_firmservicereqregistry
    ADD CONSTRAINT egp_firmservicereqregistry_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_firmservicereqregistry
    ADD CONSTRAINT egp_firmservicereqregistry_requestid_key UNIQUE (requestid);



ALTER TABLE ONLY egp_notifications
    ADD CONSTRAINT egp_notifications_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_organization
    ADD CONSTRAINT egp_organization_name_key UNIQUE (name);



ALTER TABLE ONLY egp_organization
    ADD CONSTRAINT egp_organization_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_portaluser
    ADD CONSTRAINT egp_portaluser_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_portaluser
    ADD CONSTRAINT egp_portaluser_userid_key UNIQUE (userid);



ALTER TABLE ONLY egp_publicnotification
    ADD CONSTRAINT egp_publicnotification_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_request
    ADD CONSTRAINT egp_request_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_surveyor
    ADD CONSTRAINT egp_surveyor_id_key UNIQUE (id);



ALTER TABLE ONLY egp_surveyoractiveservcreg
    ADD CONSTRAINT egp_surveyoractiveservcreg_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_surveyoractiveservcreg
    ADD CONSTRAINT egp_surveyoractiveservcreg_referenceno_key UNIQUE (referenceno);



ALTER TABLE ONLY egp_surveyordetails
    ADD CONSTRAINT egp_surveyordetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_surveyornotification
    ADD CONSTRAINT egp_surveyornotification_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_surveyorserviceregistry
    ADD CONSTRAINT egp_surveyorserviceregistry_actionid_actiontype_parentid_key UNIQUE (actionid, actiontype, parentid);



ALTER TABLE ONLY egp_surveyorserviceregistry
    ADD CONSTRAINT egp_surveyorserviceregistry_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_surveyorservicereqregistry
    ADD CONSTRAINT egp_surveyorservicereqregistry_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egp_surveyorservicereqregistry
    ADD CONSTRAINT egp_surveyorservicereqregistry_requestid_key UNIQUE (requestid);



ALTER TABLE ONLY egpay_advance_schedule
    ADD CONSTRAINT egpay_advance_schedule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_batchfailuredetails
    ADD CONSTRAINT egpay_batchfailuredetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_batchgendetails
    ADD CONSTRAINT egpay_batchgendetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_category_master
    ADD CONSTRAINT egpay_category_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_deductions
    ADD CONSTRAINT egpay_deductions_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_earnings
    ADD CONSTRAINT egpay_earnings_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT egpay_emppayroll_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_emppayrolltypes
    ADD CONSTRAINT egpay_emppayrolltypes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_exception_mstr
    ADD CONSTRAINT egpay_exception_mstr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_exception
    ADD CONSTRAINT egpay_exception_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_incrementdetails
    ADD CONSTRAINT egpay_incrementdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_paygenrulessetup_mstr
    ADD CONSTRAINT egpay_paygenrulessetup_mstr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_paygenrulessetup_mstr
    ADD CONSTRAINT egpay_paygenrulessetup_mstr_salarycode_id_month_id_financia_key UNIQUE (salarycode_id, month, id_financialyear);



ALTER TABLE ONLY egpay_payhead_rule
    ADD CONSTRAINT egpay_payhead_rule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_payscale_details
    ADD CONSTRAINT egpay_payscale_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_payscale_employee
    ADD CONSTRAINT egpay_payscale_employee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_payscale_header
    ADD CONSTRAINT egpay_payscale_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_payscale_incrdetails
    ADD CONSTRAINT egpay_payscale_incrdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_saladvances
    ADD CONSTRAINT egpay_saladvances_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT egpay_salarycodes_head_key UNIQUE (head);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT egpay_salarycodes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_additionbasic_valmap
    ADD CONSTRAINT egpen_additionbasic_valmap_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT egpen_advance_id_pensionerheader_id_payhead_month_year_stat_key UNIQUE (id_pensionerheader, id_payhead, month, year, status);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT egpen_advance_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_advance_schedule
    ADD CONSTRAINT egpen_advance_schedule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_arrears_detail
    ADD CONSTRAINT egpen_arrears_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_arrears_header
    ADD CONSTRAINT egpen_arrears_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_basic_components
    ADD CONSTRAINT egpen_basic_components_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT egpen_batchfailuredetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_batchgendetails
    ADD CONSTRAINT egpen_batchgendetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_billmisinfo_mapping
    ADD CONSTRAINT egpen_billmisinfo_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_category_master
    ADD CONSTRAINT egpen_category_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_cert_master
    ADD CONSTRAINT egpen_cert_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_cert_submission_details
    ADD CONSTRAINT egpen_cert_submission_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_cert_submission_header
    ADD CONSTRAINT egpen_cert_submission_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT egpen_commutation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_commutationvalue_mapping
    ADD CONSTRAINT egpen_commutationvalue_mapping_age_key UNIQUE (age);



ALTER TABLE ONLY egpen_commutationvalue_mapping
    ADD CONSTRAINT egpen_commutationvalue_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_deathgratuity_details
    ADD CONSTRAINT egpen_deathgratuity_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_deductions
    ADD CONSTRAINT egpen_deductions_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_earnings
    ADD CONSTRAINT egpen_earnings_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_family_pensioner_details
    ADD CONSTRAINT egpen_family_pensioner_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT egpen_family_pensioner_header_family_pensioner_number_key UNIQUE (family_pensioner_number);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT egpen_family_pensioner_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_func_do_mapping
    ADD CONSTRAINT egpen_func_do_mapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT egpen_gratuity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT egpen_monthly_pension_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pay_commission
    ADD CONSTRAINT egpen_pay_commission_code_key UNIQUE (code);



ALTER TABLE ONLY egpen_pay_commission
    ADD CONSTRAINT egpen_pay_commission_name_key UNIQUE (name);



ALTER TABLE ONLY egpen_pay_commission
    ADD CONSTRAINT egpen_pay_commission_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_payhead_rates_payhead_id_rule_effectivefrom_paycommis_key UNIQUE (payhead_id, rule_effectivefrom, paycommision_id);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_payhead_rates_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_payhead_rule
    ADD CONSTRAINT egpen_payhead_rule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT egpen_payheads_head_key UNIQUE (head);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT egpen_payheads_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pension_banks
    ADD CONSTRAINT egpen_pension_banks_id_bank_key UNIQUE (id_bank);



ALTER TABLE ONLY egpen_pension_change_history
    ADD CONSTRAINT egpen_pension_change_history_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT egpen_pension_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pension_types
    ADD CONSTRAINT egpen_pension_types_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pensioner_bank_details
    ADD CONSTRAINT egpen_pensioner_bank_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pensioner_details
    ADD CONSTRAINT egpen_pensioner_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT egpen_pensioner_header_pensioner_number_key UNIQUE (pensioner_number);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT egpen_pensioner_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_pensioner_nominee
    ADD CONSTRAINT egpen_pensioner_nominee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_recovery_details
    ADD CONSTRAINT egpen_recovery_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_type_master
    ADD CONSTRAINT egpen_type_master_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpen_type_master
    ADD CONSTRAINT egpen_type_master_type_key UNIQUE (type);



ALTER TABLE ONLY egpt_basic_property
    ADD CONSTRAINT egpt_basic_property_old_propertyid_key UNIQUE (old_propertyid);



ALTER TABLE ONLY egpt_basic_property
    ADD CONSTRAINT egpt_basic_property_pkey PRIMARY KEY (id_basic_property);



ALTER TABLE ONLY egpt_bills
    ADD CONSTRAINT egpt_bills_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_bndry_category
    ADD CONSTRAINT egpt_bndry_category_pkey PRIMARY KEY (id_bndry_category);



ALTER TABLE ONLY egpt_category
    ADD CONSTRAINT egpt_category_pkey PRIMARY KEY (id_category);



ALTER TABLE ONLY egpt_collection_report
    ADD CONSTRAINT egpt_collection_report_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_demandcalculations
    ADD CONSTRAINT egpt_demandcalculations_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_demandreason
    ADD CONSTRAINT egpt_demandreason_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_depreciation
    ADD CONSTRAINT egpt_depreciation_pkey PRIMARY KEY (id_depreciation);



ALTER TABLE ONLY egpt_floor_detail
    ADD CONSTRAINT egpt_floor_detail_pkey PRIMARY KEY (id_floor_detail);



ALTER TABLE ONLY egpt_floordemandcalc
    ADD CONSTRAINT egpt_floordemandcalc_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_mstr_property_group
    ADD CONSTRAINT egpt_mstr_property_group_group_name_key UNIQUE (group_name);



ALTER TABLE ONLY egpt_mstr_property_group
    ADD CONSTRAINT egpt_mstr_property_group_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_mstr_property_subgroup
    ADD CONSTRAINT egpt_mstr_property_subgroup_id_mstr_property_group_subgroup_key UNIQUE (id_mstr_property_group, subgroup_name);



ALTER TABLE ONLY egpt_mstr_property_subgroup
    ADD CONSTRAINT egpt_mstr_property_subgroup_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_mutation_master
    ADD CONSTRAINT egpt_mutation_master_pkey PRIMARY KEY (id_mutation);



ALTER TABLE ONLY egpt_mutation_owner
    ADD CONSTRAINT egpt_mutation_owner_pkey PRIMARY KEY (id_mutation_owner);



ALTER TABLE ONLY egpt_notice100
    ADD CONSTRAINT egpt_notice100_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_notice72
    ADD CONSTRAINT egpt_notice72_pkey PRIMARY KEY (id_notice72);



ALTER TABLE ONLY egpt_occupation_type_master
    ADD CONSTRAINT egpt_occupation_type_master_pkey PRIMARY KEY (id_occpn_mstr);



ALTER TABLE ONLY egpt_prop_corr_details
    ADD CONSTRAINT egpt_prop_corr_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_prop_modify_reason
    ADD CONSTRAINT egpt_prop_modify_reason_pkey PRIMARY KEY (id_reason);



ALTER TABLE ONLY egpt_prop_ref
    ADD CONSTRAINT egpt_prop_ref_pkey PRIMARY KEY (id_propref);



ALTER TABLE ONLY egpt_property_corrections
    ADD CONSTRAINT egpt_property_corrections_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_property_detail
    ADD CONSTRAINT egpt_property_detail_pkey PRIMARY KEY (id_property_detail);



ALTER TABLE ONLY egpt_property_effective_period
    ADD CONSTRAINT egpt_property_effective_period_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_property_integration
    ADD CONSTRAINT egpt_property_integration_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_property_master_docs
    ADD CONSTRAINT egpt_property_master_docs_pkey PRIMARY KEY (id_prop_doc);



ALTER TABLE ONLY egpt_property_mutation
    ADD CONSTRAINT egpt_property_mutation_pkey PRIMARY KEY (id_prop_mutation);



ALTER TABLE ONLY egpt_property
    ADD CONSTRAINT egpt_property_pkey PRIMARY KEY (id_property);



ALTER TABLE ONLY egpt_property_status_values
    ADD CONSTRAINT egpt_property_status_values_pkey PRIMARY KEY (id_status_values);



ALTER TABLE ONLY egpt_property_type_master
    ADD CONSTRAINT egpt_property_type_master_pkey PRIMARY KEY (id_propertytypemaster);



ALTER TABLE ONLY egpt_property_usage_master
    ADD CONSTRAINT egpt_property_usage_master_pkey PRIMARY KEY (id_usg_mstr);



ALTER TABLE ONLY egpt_propertyid
    ADD CONSTRAINT egpt_propertyid_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_ptdemanddetails
    ADD CONSTRAINT egpt_ptdemanddetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_rcpt_rectification
    ADD CONSTRAINT egpt_rcpt_rectification_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_rcpt_rectify_details
    ADD CONSTRAINT egpt_rcpt_rectify_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_register
    ADD CONSTRAINT egpt_register_pkey PRIMARY KEY (id_register);



ALTER TABLE ONLY egpt_src_of_info
    ADD CONSTRAINT egpt_src_of_info_pkey PRIMARY KEY (id_source);



ALTER TABLE ONLY egpt_status
    ADD CONSTRAINT egpt_status_pkey PRIMARY KEY (id_status);



ALTER TABLE ONLY egpt_struc_cl
    ADD CONSTRAINT egpt_struc_cl_code_floor_num_key UNIQUE (code, floor_num);



ALTER TABLE ONLY egpt_struc_cl
    ADD CONSTRAINT egpt_struc_cl_pkey PRIMARY KEY (id_struc_cl);



ALTER TABLE ONLY egpt_tax_perc
    ADD CONSTRAINT egpt_tax_perc_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_taxrates
    ADD CONSTRAINT egpt_taxrates_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_tx_agent
    ADD CONSTRAINT egpt_tx_agent_id_basicproperty_key UNIQUE (id_basicproperty);



ALTER TABLE ONLY egpt_tx_agent
    ADD CONSTRAINT egpt_tx_agent_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_usage_rate
    ADD CONSTRAINT egpt_usage_rate_pkey PRIMARY KEY (id_usage_rate);



ALTER TABLE ONLY egpt_wfactivateprop
    ADD CONSTRAINT egpt_wfactivateprop_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_wfcitizen
    ADD CONSTRAINT egpt_wfcitizen_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_wfcorrectprop_particular
    ADD CONSTRAINT egpt_wfcorrectprop_particular_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_wfmodfloor_detail
    ADD CONSTRAINT egpt_wfmodfloor_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_wfmodifyprop
    ADD CONSTRAINT egpt_wfmodifyprop_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_wfmutation
    ADD CONSTRAINT egpt_wfmutation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egpt_wfproperty
    ADD CONSTRAINT egpt_wfproperty_old_propertyid_key UNIQUE (old_propertyid);



ALTER TABLE ONLY egpt_wfproperty
    ADD CONSTRAINT egpt_wfproperty_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egptwf_floor_detail
    ADD CONSTRAINT egptwf_floor_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_capitalissue
    ADD CONSTRAINT egstores_capitalissue_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_indent_quotation
    ADD CONSTRAINT egstores_indent_quotation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_issuewriteoffscrap
    ADD CONSTRAINT egstores_issuewriteoffscrap_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_itemdeptdetails
    ADD CONSTRAINT egstores_itemdeptdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT egstores_itemtypedetails_itemtypeid_storeid_deptid_key UNIQUE (itemtypeid, storeid, deptid);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT egstores_itemtypedetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_mrnpodetails
    ADD CONSTRAINT egstores_mrnpodetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_poheader
    ADD CONSTRAINT egstores_poheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_poindentdetail
    ADD CONSTRAINT egstores_poindentdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_poline
    ADD CONSTRAINT egstores_poline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_polineothers
    ADD CONSTRAINT egstores_polineothers_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_processindent_mrline
    ADD CONSTRAINT egstores_processindent_mrline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT egstores_processindent_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_processindentline
    ADD CONSTRAINT egstores_processindentline_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_ratecontract
    ADD CONSTRAINT egstores_ratecontract_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_ratecontract
    ADD CONSTRAINT egstores_ratecontract_ratecontractno_key UNIQUE (ratecontractno);



ALTER TABLE ONLY egstores_ratecontractdetails
    ADD CONSTRAINT egstores_ratecontractdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_rcontractslabdetails
    ADD CONSTRAINT egstores_rcontractslabdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_rcreq_slabdetails
    ADD CONSTRAINT egstores_rcreq_slabdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_rcrequisition_details
    ADD CONSTRAINT egstores_rcrequisition_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_rcrequisition
    ADD CONSTRAINT egstores_rcrequisition_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egstores_rcrequisition
    ADD CONSTRAINT egstores_rcrequisition_requisitionno_key UNIQUE (requisitionno);



ALTER TABLE ONLY egstores_txntypes
    ADD CONSTRAINT egstores_txntypes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_department
    ADD CONSTRAINT egtl_department_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_duplicate_license_info
    ADD CONSTRAINT egtl_duplicate_license_info_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_history_trade_area
    ADD CONSTRAINT egtl_history_trade_area_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_installed_motor
    ADD CONSTRAINT egtl_installed_motor_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_license_cancel_info
    ADD CONSTRAINT egtl_license_cancel_info_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_license_objection_info
    ADD CONSTRAINT egtl_license_objection_info_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT egtl_license_old_lic_no_key UNIQUE (old_lic_no);



ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT egtl_license_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_license_revoke_info
    ADD CONSTRAINT egtl_license_revoke_info_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_app_type
    ADD CONSTRAINT egtl_mstr_app_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_area_licfee
    ADD CONSTRAINT egtl_mstr_area_licfee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_conserfee
    ADD CONSTRAINT egtl_mstr_conserfee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_erection_fee
    ADD CONSTRAINT egtl_mstr_erection_fee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_pfa_fee
    ADD CONSTRAINT egtl_mstr_pfa_fee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_schedule
    ADD CONSTRAINT egtl_mstr_schedule_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_trade_category
    ADD CONSTRAINT egtl_mstr_trade_category_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_trade_nature
    ADD CONSTRAINT egtl_mstr_trade_nature_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_trade_sub_ctgr
    ADD CONSTRAINT egtl_mstr_trade_sub_ctgr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_mstr_trade_sub_ctgr
    ADD CONSTRAINT egtl_mstr_trade_sub_ctgr_trade_number_key UNIQUE (trade_number);



ALTER TABLE ONLY egtl_mstr_weight_licfee
    ADD CONSTRAINT egtl_mstr_weight_licfee_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_name_transfer
    ADD CONSTRAINT egtl_name_transfer_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_renewal
    ADD CONSTRAINT egtl_renewal_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_trade_nature
    ADD CONSTRAINT egtl_trade_nature_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT egtl_trade_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT egw_abstractestimate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_activity
    ADD CONSTRAINT egw_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_assetforbill
    ADD CONSTRAINT egw_assetforbill_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_assetsforestimate
    ADD CONSTRAINT egw_assetsforestimate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_assetsforworkorder
    ADD CONSTRAINT egw_assetsforworkorder_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_cancelled_bill
    ADD CONSTRAINT egw_cancelled_bill_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT egw_change_fd_estimate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT egw_change_financialdetails_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_contractor_detail
    ADD CONSTRAINT egw_contractor_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_contractor_grade
    ADD CONSTRAINT egw_contractor_grade_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_contractor
    ADD CONSTRAINT egw_contractor_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_deductiontype_bill
    ADD CONSTRAINT egw_deductiontype_bill_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT egw_depositcode_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_depositworks_usage
    ADD CONSTRAINT egw_depositworks_usage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_document_template
    ADD CONSTRAINT egw_document_template_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT egw_dw_application_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT egw_dw_applicationrequest_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_dw_deposit_type
    ADD CONSTRAINT egw_dw_deposit_type_code_key UNIQUE (code);



ALTER TABLE ONLY egw_dw_deposit_type
    ADD CONSTRAINT egw_dw_deposit_type_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_dw_roadcut_approval_letter
    ADD CONSTRAINT egw_dw_roadcut_approval_letter_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT egw_dw_roadcut_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_dw_utilization_certificate
    ADD CONSTRAINT egw_dw_utilization_certificate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_est_apprn_history
    ADD CONSTRAINT egw_est_apprn_history_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_estimate_appropriation
    ADD CONSTRAINT egw_estimate_appropriation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_estimate_photo
    ADD CONSTRAINT egw_estimate_photo_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_estimate_reappr_details
    ADD CONSTRAINT egw_estimate_reappr_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT egw_estimate_reappropriation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_estimate_template
    ADD CONSTRAINT egw_estimate_template_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT egw_financialdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_financingsource
    ADD CONSTRAINT egw_financingsource_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT egw_indent_indent_number_key UNIQUE (indent_number);



ALTER TABLE ONLY egw_indent_jurisdiction
    ADD CONSTRAINT egw_indent_jurisdiction_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_indent_maintenance_detail
    ADD CONSTRAINT egw_indent_maintenance_detail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT egw_indent_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT egw_indent_tender_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_marketrate
    ADD CONSTRAINT egw_marketrate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_mb_details
    ADD CONSTRAINT egw_mb_details_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT egw_mb_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_milestone_activity
    ADD CONSTRAINT egw_milestone_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_milestone
    ADD CONSTRAINT egw_milestone_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_milestone_temp_activity
    ADD CONSTRAINT egw_milestone_temp_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_milestone_template
    ADD CONSTRAINT egw_milestone_template_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_multiyearestimate
    ADD CONSTRAINT egw_multiyearestimate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT egw_multiyearestimateappr_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT egw_multiyearestimateapprdtls_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT egw_negotiation_status_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_nonsor
    ADD CONSTRAINT egw_nonsor_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_overhead
    ADD CONSTRAINT egw_overhead_name_key UNIQUE (name);



ALTER TABLE ONLY egw_overhead
    ADD CONSTRAINT egw_overhead_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_overhead_rate
    ADD CONSTRAINT egw_overhead_rate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_overheadvalues
    ADD CONSTRAINT egw_overheadvalues_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_projectcode
    ADD CONSTRAINT egw_projectcode_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_projectcodemis
    ADD CONSTRAINT egw_projectcodemis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_rate_contract
    ADD CONSTRAINT egw_rate_contract_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_rate_contract
    ADD CONSTRAINT egw_rate_contract_rate_contract_number_key UNIQUE (rate_contract_number);



ALTER TABLE ONLY egw_rate
    ADD CONSTRAINT egw_rate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_report2_dashboard_data
    ADD CONSTRAINT egw_report2_dashboard_data_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_retender_history
    ADD CONSTRAINT egw_retender_history_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_retender
    ADD CONSTRAINT egw_retender_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_satuschange
    ADD CONSTRAINT egw_satuschange_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_schedulecategory
    ADD CONSTRAINT egw_schedulecategory_code_key UNIQUE (code);



ALTER TABLE ONLY egw_schedulecategory
    ADD CONSTRAINT egw_schedulecategory_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_scheduleofrate
    ADD CONSTRAINT egw_scheduleofrate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_scheduleofratetype
    ADD CONSTRAINT egw_scheduleofratetype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_status
    ADD CONSTRAINT egw_status_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_estimate
    ADD CONSTRAINT egw_tender_estimate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_header
    ADD CONSTRAINT egw_tender_header_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_resp_contractors
    ADD CONSTRAINT egw_tender_resp_contractors_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_response_activity
    ADD CONSTRAINT egw_tender_response_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_response
    ADD CONSTRAINT egw_tender_response_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_response_quotes
    ADD CONSTRAINT egw_tender_response_quotes_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_tender_scrutinizing_abst
    ADD CONSTRAINT egw_tender_scrutinizing_abst_agenda_number_key UNIQUE (agenda_number);



ALTER TABLE ONLY egw_tender_scrutinizing_abst
    ADD CONSTRAINT egw_tender_scrutinizing_abst_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_track_milestone_activity
    ADD CONSTRAINT egw_track_milestone_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_track_milestone
    ADD CONSTRAINT egw_track_milestone_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_typeofwork
    ADD CONSTRAINT egw_typeofwork_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_work_order_activity
    ADD CONSTRAINT egw_work_order_activity_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_work_order
    ADD CONSTRAINT egw_work_order_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_workorder_estimate
    ADD CONSTRAINT egw_workorder_estimate_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_workprogress_project_code
    ADD CONSTRAINT egw_workprogress_project_code_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_works_mis
    ADD CONSTRAINT egw_works_mis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_works_status
    ADD CONSTRAINT egw_works_status_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_workspackage
    ADD CONSTRAINT egw_workspackage_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_workstype
    ADD CONSTRAINT egw_workstype_pkey PRIMARY KEY (id);



ALTER TABLE ONLY egw_wrkprog_projcode_spillover
    ADD CONSTRAINT egw_wrkprog_projcode_spillover_pkey PRIMARY KEY (id);



ALTER TABLE ONLY financial_institution
    ADD CONSTRAINT financial_institution_pkey PRIMARY KEY (id);



ALTER TABLE ONLY financialyear
    ADD CONSTRAINT financialyear_financialyear_key UNIQUE (financialyear);



ALTER TABLE ONLY financialyear
    ADD CONSTRAINT financialyear_pkey PRIMARY KEY (id);



ALTER TABLE ONLY fiscalperiod
    ADD CONSTRAINT fiscalperiod_pkey PRIMARY KEY (id);



ALTER TABLE ONLY function
    ADD CONSTRAINT function_code_key UNIQUE (code);



ALTER TABLE ONLY function
    ADD CONSTRAINT function_pkey PRIMARY KEY (id);



ALTER TABLE ONLY functionary
    ADD CONSTRAINT functionary_code_key UNIQUE (code);



ALTER TABLE ONLY functionary
    ADD CONSTRAINT functionary_name_key UNIQUE (name);



ALTER TABLE ONLY functionary
    ADD CONSTRAINT functionary_pkey PRIMARY KEY (id);



ALTER TABLE ONLY fund
    ADD CONSTRAINT fund_code_key UNIQUE (code);



ALTER TABLE ONLY fund
    ADD CONSTRAINT fund_pkey PRIMARY KEY (id);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_code_key UNIQUE (code);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_name_key UNIQUE (name);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fundsource_pkey PRIMARY KEY (id);



ALTER TABLE ONLY generalledger
    ADD CONSTRAINT generalledger_pkey PRIMARY KEY (id);



ALTER TABLE ONLY generalledgerdetail
    ADD CONSTRAINT generalledgerdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY generalvouchermis
    ADD CONSTRAINT generalvouchermis_pkey PRIMARY KEY (id);



ALTER TABLE ONLY gpf_empdetails
    ADD CONSTRAINT gpf_empdetails_pkey PRIMARY KEY (coc_gpf_empdetails_id);



ALTER TABLE ONLY gpf_openbal
    ADD CONSTRAINT gpf_openbal_pkey PRIMARY KEY (coc_gpf_openbal_id);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT jbpm_action_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_bytearray
    ADD CONSTRAINT jbpm_bytearray_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_byteblock
    ADD CONSTRAINT jbpm_byteblock_pkey PRIMARY KEY (processfile_, index_);



ALTER TABLE ONLY jbpm_comment
    ADD CONSTRAINT jbpm_comment_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_decisionconditions
    ADD CONSTRAINT jbpm_decisionconditions_pkey PRIMARY KEY (decision_, index_);



ALTER TABLE ONLY jbpm_delegation
    ADD CONSTRAINT jbpm_delegation_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_event
    ADD CONSTRAINT jbpm_event_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_exceptionhandler
    ADD CONSTRAINT jbpm_exceptionhandler_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_job
    ADD CONSTRAINT jbpm_job_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT jbpm_log_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_moduledefinition
    ADD CONSTRAINT jbpm_moduledefinition_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_moduleinstance
    ADD CONSTRAINT jbpm_moduleinstance_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_node
    ADD CONSTRAINT jbpm_node_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_pooledactor
    ADD CONSTRAINT jbpm_pooledactor_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_processdefinition
    ADD CONSTRAINT jbpm_processdefinition_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_processinstance
    ADD CONSTRAINT jbpm_processinstance_key__processdefinition__key UNIQUE (key_, processdefinition_);



ALTER TABLE ONLY jbpm_processinstance
    ADD CONSTRAINT jbpm_processinstance_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_runtimeaction
    ADD CONSTRAINT jbpm_runtimeaction_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_swimlane
    ADD CONSTRAINT jbpm_swimlane_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_swimlaneinstance
    ADD CONSTRAINT jbpm_swimlaneinstance_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_task
    ADD CONSTRAINT jbpm_task_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_taskactorpool
    ADD CONSTRAINT jbpm_taskactorpool_pkey PRIMARY KEY (taskinstance_, pooledactor_);



ALTER TABLE ONLY jbpm_taskcontroller
    ADD CONSTRAINT jbpm_taskcontroller_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_taskinstance
    ADD CONSTRAINT jbpm_taskinstance_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_token
    ADD CONSTRAINT jbpm_token_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_tokenvariablemap
    ADD CONSTRAINT jbpm_tokenvariablemap_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_transition
    ADD CONSTRAINT jbpm_transition_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_variableaccess
    ADD CONSTRAINT jbpm_variableaccess_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jbpm_variableinstance
    ADD CONSTRAINT jbpm_variableinstance_pkey PRIMARY KEY (id_);



ALTER TABLE ONLY jms_messages
    ADD CONSTRAINT jms_messages_pkey PRIMARY KEY (messageid, destination);



ALTER TABLE ONLY jms_transactions
    ADD CONSTRAINT jms_transactions_pkey PRIMARY KEY (txid);



ALTER TABLE ONLY job_history
    ADD CONSTRAINT job_history_pkey PRIMARY KEY (employee_id, start_date);



ALTER TABLE ONLY jobs
    ADD CONSTRAINT jobs_pkey PRIMARY KEY (job_id);



ALTER TABLE ONLY locations
    ADD CONSTRAINT locations_pkey PRIMARY KEY (location_id);



ALTER TABLE ONLY menutree
    ADD CONSTRAINT menutree_pkey PRIMARY KEY (id);



ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT miscbilldetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY notice100
    ADD CONSTRAINT notice100_pkey PRIMARY KEY (id_notice);



ALTER TABLE ONLY notice72
    ADD CONSTRAINT notice72_pkey PRIMARY KEY (id_notice72);



ALTER TABLE ONLY notice
    ADD CONSTRAINT notice_noticeno_key UNIQUE (noticeno);



ALTER TABLE ONLY notice
    ADD CONSTRAINT notice_pkey PRIMARY KEY (id_notice);



ALTER TABLE ONLY otherbilldetail
    ADD CONSTRAINT otherbilldetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY othertaxdetail
    ADD CONSTRAINT othertaxdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT paymentheader_pkey PRIMARY KEY (id);



ALTER TABLE ONLY qrtz_blob_triggers
    ADD CONSTRAINT qrtz_blob_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);



ALTER TABLE ONLY qrtz_calendars
    ADD CONSTRAINT qrtz_calendars_pkey PRIMARY KEY (sched_name, calendar_name);



ALTER TABLE ONLY qrtz_cron_triggers
    ADD CONSTRAINT qrtz_cron_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);



ALTER TABLE ONLY qrtz_fired_triggers
    ADD CONSTRAINT qrtz_fired_triggers_pkey PRIMARY KEY (sched_name, entry_id);



ALTER TABLE ONLY qrtz_job_details
    ADD CONSTRAINT qrtz_job_details_pkey PRIMARY KEY (sched_name, job_name, job_group);



ALTER TABLE ONLY qrtz_locks
    ADD CONSTRAINT qrtz_locks_pkey PRIMARY KEY (sched_name, lock_name);



ALTER TABLE ONLY qrtz_paused_trigger_grps
    ADD CONSTRAINT qrtz_paused_trigger_grps_pkey PRIMARY KEY (sched_name, trigger_group);



ALTER TABLE ONLY qrtz_scheduler_state
    ADD CONSTRAINT qrtz_scheduler_state_pkey PRIMARY KEY (sched_name, instance_name);



ALTER TABLE ONLY qrtz_simple_triggers
    ADD CONSTRAINT qrtz_simple_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);



ALTER TABLE ONLY qrtz_simprop_triggers
    ADD CONSTRAINT qrtz_simprop_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);



ALTER TABLE ONLY qrtz_triggers
    ADD CONSTRAINT qrtz_triggers_pkey PRIMARY KEY (sched_name, trigger_name, trigger_group);



ALTER TABLE ONLY regions
    ADD CONSTRAINT regions_pkey PRIMARY KEY (region_id);



ALTER TABLE ONLY relation
    ADD CONSTRAINT relation_pkey PRIMARY KEY (id);



ALTER TABLE ONLY schedulemapping
    ADD CONSTRAINT schedulemapping_pkey PRIMARY KEY (id);



ALTER TABLE ONLY schedulemapping
    ADD CONSTRAINT schedulemapping_schedule_reporttype_key UNIQUE (schedule, reporttype);



ALTER TABLE ONLY scheme
    ADD CONSTRAINT scheme_code_fundid_key UNIQUE (code, fundid);



ALTER TABLE ONLY scheme
    ADD CONSTRAINT scheme_pkey PRIMARY KEY (id);



ALTER TABLE ONLY screencontrols
    ADD CONSTRAINT screencontrols_pkey PRIMARY KEY (id);



ALTER TABLE ONLY shared_fundsource
    ADD CONSTRAINT shared_fundsource_pkey PRIMARY KEY (id);



ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT sub_scheme_code_schemeid_key UNIQUE (code, schemeid);



ALTER TABLE ONLY sub_scheme
    ADD CONSTRAINT sub_scheme_pkey PRIMARY KEY (id);



ALTER TABLE ONLY tds
    ADD CONSTRAINT tds_pkey PRIMARY KEY (id);



ALTER TABLE ONLY tds
    ADD CONSTRAINT tds_type_key UNIQUE (type);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT transactionsummary_pkey PRIMARY KEY (id);



ALTER TABLE ONLY voucherdetail
    ADD CONSTRAINT voucherdetail_pkey PRIMARY KEY (id);



ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT voucherheader_cgvn_fiscalperiodid_key UNIQUE (cgvn, fiscalperiodid);



ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT voucherheader_pkey PRIMARY KEY (id);



CREATE UNIQUE INDEX application_num_unique ON egeis_leave_application USING btree (application_number);



CREATE UNIQUE INDEX bank_branch_code ON bankbranch USING btree (bankid, branchcode);



CREATE INDEX budget_fyear ON egf_budget USING btree (financialyearid);



CREATE INDEX budgetdetail_budget ON egf_budgetdetail USING btree (budget);



CREATE INDEX budgetdetail_budgetgroup ON egf_budgetdetail USING btree (budgetgroup);



CREATE INDEX budgetdetail_dept ON egf_budgetdetail USING btree (executing_department);



CREATE INDEX budgetdetail_function ON egf_budgetdetail USING btree (function);



CREATE INDEX budgetgroup_mincode ON egf_budgetgroup USING btree (mincode);



CREATE UNIQUE INDEX cgn_c ON voucherheader USING btree (cgn);



CREATE INDEX coa_type ON chartofaccounts USING btree (type);



CREATE UNIQUE INDEX code_unq ON egpt_mutation_master USING btree (code);



CREATE UNIQUE INDEX cont_code_unq ON egw_contractor USING btree (code);



CREATE UNIQUE INDEX cont_grade_unq ON egw_contractor_grade USING btree (grade);



CREATE INDEX digitalsign_objectid_idx ON eg_digital_signed_docs USING btree (objectid);



CREATE INDEX digitalsign_objectno_idx ON eg_digital_signed_docs USING btree (objectno);



CREATE UNIQUE INDEX eg_matrix_unique ON eg_wf_matrix USING btree (department, objecttype, currentstate, pendingactions, fromqty, toqty, additionalrule);



CREATE INDEX eg_wf_states_typ_ownr_indx ON eg_wf_states USING btree (type, owner);



CREATE INDEX egbpaextnd_indx_ltrtoparty ON egbpaextnd_lettertoparty USING btree (id, registrationid);



CREATE INDEX egdiary_images_idx ON egdiary_images USING btree (id_diary_attribute_values);



CREATE INDEX egdm_collrcts_dmddetail_index ON egdm_collected_receipts USING btree (id_demand_detail);



CREATE UNIQUE INDEX erm_id_pk ON egf_receipt_mis USING btree (id);



CREATE UNIQUE INDEX fsp_name ON fiscalperiod USING btree (name);



CREATE INDEX idx_ac_accdepcode ON eg_assetcategory USING btree (accdepcode);



CREATE INDEX idx_ac_assetcode ON eg_assetcategory USING btree (assetcode);



CREATE INDEX idx_ac_depexpcode ON eg_assetcategory USING btree (depexpcode);



CREATE INDEX idx_ac_depid ON eg_assetcategory USING btree (depid);



CREATE INDEX idx_ac_depmethord ON eg_assetcategory USING btree (depmethord);



CREATE INDEX idx_ac_parentid ON eg_assetcategory USING btree (parentid);



CREATE INDEX idx_ac_revcode ON eg_assetcategory USING btree (revcode);



CREATE INDEX idx_action_actndl ON jbpm_action USING btree (actiondelegation_);



CREATE INDEX idx_action_event ON jbpm_action USING btree (event_);



CREATE INDEX idx_action_procdf ON jbpm_action USING btree (processdefinition_);



CREATE INDEX idx_apo_assetid ON eg_asset_po USING btree (assetid);



CREATE INDEX idx_apo_workorderid ON eg_asset_po USING btree (workorderid);



CREATE INDEX idx_asset_categoryid ON eg_asset USING btree (categoryid);



CREATE INDEX idx_asset_departmentid ON eg_asset USING btree (departmentid);



CREATE INDEX idx_assetsale_assetid ON eg_asset_sale USING btree (assetid);



CREATE INDEX idx_assetsale_voucherheaderid ON eg_asset_sale USING btree (voucherheaderid);



CREATE INDEX idx_aud_eve_eventdate ON eg_audit_event USING btree (eventdate);



CREATE INDEX idx_aud_eve_module ON eg_audit_event USING btree (module);



CREATE INDEX idx_aud_eve_username ON eg_audit_event USING btree (username);



CREATE INDEX idx_avc_assetid ON eg_assetvaluechange USING btree (assetid);



CREATE INDEX idx_avc_voucherheaderid ON eg_assetvaluechange USING btree (voucherheaderid);



CREATE INDEX idx_citizenaddress_addressid ON egpt_citizen_address USING btree (id_address);



CREATE INDEX idx_citizenaddress_citizenid ON egpt_citizen_address USING btree (id_citizen);



CREATE INDEX idx_collmodedetail_transid ON egcl_collection_mode_details USING btree (id_trans);



CREATE INDEX idx_comment_token ON jbpm_comment USING btree (token_);



CREATE INDEX idx_comment_tsk ON jbpm_comment USING btree (taskinstance_);



CREATE INDEX idx_deleg_prcd ON jbpm_delegation USING btree (processdefinition_);



CREATE INDEX idx_depmd_assetcategoryid ON eg_depreciationmetadata USING btree (assetcategoryid);



CREATE INDEX idx_depmd_finyearid ON eg_depreciationmetadata USING btree (finyearid);



CREATE INDEX idx_depr_assetid ON eg_depreciation USING btree (assetid);



CREATE INDEX idx_depr_financialyearid ON eg_depreciation USING btree (financialyearid);



CREATE INDEX idx_depr_voucherheaderid ON eg_depreciation USING btree (voucherheaderid);



CREATE INDEX idx_deptissue_mrinheaderid ON egf_deptissue USING btree (mrinheaderid);



CREATE INDEX idx_egdmcoll_rcptno ON egdm_collected_receipts USING btree (receipt_number);



CREATE INDEX idx_egf_ebdetails_fyid ON egf_ebdetails USING btree (financialyearid);



CREATE INDEX idx_egf_ebdetails_pos ON egf_ebdetails USING btree (position_id);



CREATE INDEX idx_egf_ebdetails_ta ON egf_ebdetails USING btree (target_area_id);



CREATE INDEX idx_egf_ebdetails_ward ON egf_ebdetails USING btree (wardid);



CREATE UNIQUE INDEX idx_eglems_paymentcyclecode ON eglems_paymentcycle USING btree (code);



CREATE INDEX idx_egw_act_ae ON egw_activity USING btree (abstractestimate_id);



CREATE INDEX idx_egw_act_nonsor ON egw_activity USING btree (nonsor_id);



CREATE INDEX idx_egw_act_scheduleofrate ON egw_activity USING btree (scheduleofrate_id);



CREATE INDEX idx_egw_act_uom ON egw_activity USING btree (uom_id);



CREATE INDEX idx_egw_ae_despositcode ON egw_abstractestimate USING btree (despositcode_id);



CREATE INDEX idx_egw_ae_fundsource ON egw_abstractestimate USING btree (fundsourceid);



CREATE INDEX idx_egw_ae_old_exec_department ON egw_abstractestimate USING btree (old_exec_department);



CREATE INDEX idx_egw_ae_old_user_department ON egw_abstractestimate USING btree (old_user_department);



CREATE INDEX idx_egw_ae_old_ward ON egw_abstractestimate USING btree (old_ward);



CREATE INDEX idx_egw_ae_parent ON egw_abstractestimate USING btree (parentid);



CREATE INDEX idx_egw_ae_projectcode ON egw_abstractestimate USING btree (projectcode_id);



CREATE INDEX idx_egw_ae_status ON egw_abstractestimate USING btree (status_id);



CREATE INDEX idx_egw_ae_userdepartment ON egw_abstractestimate USING btree (userdepartment);



CREATE INDEX idx_egw_astforbill_id_asset ON egw_assetforbill USING btree (id_asset);



CREATE INDEX idx_egw_astforbill_id_bill ON egw_assetforbill USING btree (id_bill);



CREATE INDEX idx_egw_astsforestimate_ae ON egw_assetsforestimate USING btree (abstractestimate_id);



CREATE INDEX idx_egw_astsforestimate_asset ON egw_assetsforestimate USING btree (asset_id);



CREATE INDEX idx_egw_astsforworkorder_asset ON egw_assetsforworkorder USING btree (asset_id);



CREATE INDEX idx_egw_canc_bill_billregister ON egw_cancelled_bill USING btree (billregister_id);



CREATE INDEX idx_egw_canc_bill_mbheader ON egw_cancelled_bill USING btree (mbheader_id);



CREATE INDEX idx_egw_conc_bank ON egw_contractor USING btree (bank_id);



CREATE INDEX idx_egw_conc_detail_conc_grade ON egw_contractor_detail USING btree (contractor_grade_id);



CREATE INDEX idx_egw_conc_detail_contractor ON egw_contractor_detail USING btree (contractor_id);



CREATE INDEX idx_egw_conc_detail_department ON egw_contractor_detail USING btree (department_id);



CREATE INDEX idx_egw_conc_detail_status ON egw_contractor_detail USING btree (status_id);



CREATE INDEX idx_egw_dc_department ON egw_depositcode USING btree (department_id);



CREATE INDEX idx_egw_dc_financialyear ON egw_depositcode USING btree (financialyear_id);



CREATE INDEX idx_egw_dc_function ON egw_depositcode USING btree (function_id);



CREATE INDEX idx_egw_dc_functionary ON egw_depositcode USING btree (functionary_id);



CREATE INDEX idx_egw_dc_fund ON egw_depositcode USING btree (fund_id);



CREATE INDEX idx_egw_dc_fundsource ON egw_depositcode USING btree (fundsource_id);



CREATE INDEX idx_egw_dc_scheme ON egw_depositcode USING btree (scheme_id);



CREATE INDEX idx_egw_dc_subscheme ON egw_depositcode USING btree (subscheme_id);



CREATE INDEX idx_egw_dc_subtypeofwork ON egw_depositcode USING btree (subtypeofwork_id);



CREATE INDEX idx_egw_dc_typeofwork ON egw_depositcode USING btree (typeofwork_id);



CREATE INDEX idx_egw_dc_ward ON egw_depositcode USING btree (ward_id);



CREATE INDEX idx_egw_dc_workstype ON egw_depositcode USING btree (workstype_id);



CREATE INDEX idx_egw_dc_zone ON egw_depositcode USING btree (zone_id);



CREATE INDEX idx_egw_depositworks_usage_ae ON egw_depositworks_usage USING btree (abstractestimate_id);



CREATE INDEX idx_egw_dt_bill_id_bill ON egw_deductiontype_bill USING btree (id_bill);



CREATE INDEX idx_egw_dt_bill_id_coa ON egw_deductiontype_bill USING btree (id_coa);



CREATE INDEX idx_egw_dt_bill_id_workorder ON egw_deductiontype_bill USING btree (id_workorder);



CREATE INDEX idx_egw_dw_appdet_app_req ON egw_dw_application_details USING btree (applicationrequest_id);



CREATE INDEX idx_egw_dw_appdet_cd ON egw_dw_application_details USING btree (created_by);



CREATE INDEX idx_egw_dw_appdet_dc ON egw_dw_application_details USING btree (depositcode_id);



CREATE INDEX idx_egw_dw_appdet_md ON egw_dw_application_details USING btree (modified_by);



CREATE INDEX idx_egw_dw_appdet_prepared ON egw_dw_application_details USING btree (preparedby);



CREATE INDEX idx_egw_dw_appdet_state ON egw_dw_application_details USING btree (state_id);



CREATE INDEX idx_egw_dw_appdet_status ON egw_dw_application_details USING btree (status_id);



CREATE INDEX idx_egw_dw_appreq_cd ON egw_dw_applicationrequest USING btree (created_by);



CREATE INDEX idx_egw_dw_appreq_deposit_type ON egw_dw_applicationrequest USING btree (deposit_type_id);



CREATE INDEX idx_egw_dw_appreq_md ON egw_dw_applicationrequest USING btree (modified_by);



CREATE INDEX idx_egw_dw_appreq_status ON egw_dw_applicationrequest USING btree (status_id);



CREATE INDEX idx_egw_dw_rc_app_letter_cd ON egw_dw_roadcut_approval_letter USING btree (created_by);



CREATE INDEX idx_egw_dw_rc_app_letter_md ON egw_dw_roadcut_approval_letter USING btree (modified_by);



CREATE INDEX idx_egw_dw_rcd_app_request ON egw_dw_roadcut_details USING btree (applicationrequest_id);



CREATE INDEX idx_egw_dw_rcd_area ON egw_dw_roadcut_details USING btree (area_id);



CREATE INDEX idx_egw_dw_rcd_locality ON egw_dw_roadcut_details USING btree (locality_id);



CREATE INDEX idx_egw_dw_rcd_street ON egw_dw_roadcut_details USING btree (street_id);



CREATE INDEX idx_egw_dw_rcd_ward ON egw_dw_roadcut_details USING btree (ward_id);



CREATE INDEX idx_egw_dw_rcd_zone ON egw_dw_roadcut_details USING btree (zone_id);



CREATE INDEX idx_egw_dw_ut_certificate_cd ON egw_dw_utilization_certificate USING btree (created_by);



CREATE INDEX idx_egw_dw_ut_certificate_md ON egw_dw_utilization_certificate USING btree (modified_by);



CREATE INDEX idx_egw_est_appr_ae ON egw_estimate_appropriation USING btree (abstractestimate_id);



CREATE INDEX idx_egw_est_appr_budgetusage ON egw_estimate_appropriation USING btree (budgetusage_id);



CREATE INDEX idx_egw_est_appr_depo_usage ON egw_estimate_appropriation USING btree (depositworksusage_id);



CREATE INDEX idx_egw_est_reapp_cd ON egw_estimate_reappropriation USING btree (created_by);



CREATE INDEX idx_egw_est_reapp_department ON egw_estimate_reappropriation USING btree (department);



CREATE INDEX idx_egw_est_reapp_md ON egw_estimate_reappropriation USING btree (modified_by);



CREATE INDEX idx_egw_est_reapp_state ON egw_estimate_reappropriation USING btree (state_id);



CREATE INDEX idx_egw_est_reapp_status ON egw_estimate_reappropriation USING btree (status_id);



CREATE INDEX idx_egw_est_reapp_user_dept ON egw_estimate_reappropriation USING btree (user_department);



CREATE INDEX idx_egw_est_reapp_ward ON egw_estimate_reappropriation USING btree (ward);



CREATE INDEX idx_egw_est_reappr_details_cd ON egw_estimate_reappr_details USING btree (created_by);



CREATE INDEX idx_egw_est_reappr_details_est ON egw_estimate_reappr_details USING btree (estimate);



CREATE INDEX idx_egw_est_reappr_details_md ON egw_estimate_reappr_details USING btree (modified_by);



CREATE INDEX idx_egw_est_reapprdet_estreapr ON egw_estimate_reappr_details USING btree (estimate_reappr);



CREATE INDEX idx_egw_est_temp_act_est_temp ON egw_est_temp_activity USING btree (estimate_template_id);



CREATE INDEX idx_egw_est_temp_act_nonsor ON egw_est_temp_activity USING btree (nonsor_id);



CREATE INDEX idx_egw_est_temp_act_sor ON egw_est_temp_activity USING btree (scheduleofrate_id);



CREATE INDEX idx_egw_est_temp_act_uom ON egw_est_temp_activity USING btree (uom_id);



CREATE INDEX idx_egw_est_template_subtype ON egw_estimate_template USING btree (subtype_id);



CREATE INDEX idx_egw_est_template_worktype ON egw_estimate_template USING btree (worktype_id);



CREATE INDEX idx_egw_fis_financialdetail ON egw_financingsource USING btree (financialdetailid);



CREATE INDEX idx_egw_fis_fundsource ON egw_financingsource USING btree (fundsourceid);



CREATE INDEX idx_egw_marketrate_sor ON egw_marketrate USING btree (scheduleofrate_id);



CREATE INDEX idx_egw_mb_details_cd ON egw_mb_details USING btree (created_by);



CREATE INDEX idx_egw_mb_details_md ON egw_mb_details USING btree (modified_by);



CREATE INDEX idx_egw_mb_header_billregister ON egw_mb_header USING btree (billregister_id);



CREATE INDEX idx_egw_mb_header_cd ON egw_mb_header USING btree (created_by);



CREATE INDEX idx_egw_mb_header_md ON egw_mb_header USING btree (modified_by);



CREATE INDEX idx_egw_mb_header_status ON egw_mb_header USING btree (status_id);



CREATE INDEX idx_egw_mls_activity_milestone ON egw_milestone_activity USING btree (milestone_id);



CREATE INDEX idx_egw_mls_status ON egw_milestone USING btree (status_id);



CREATE INDEX idx_egw_mls_temp_act_template ON egw_milestone_temp_activity USING btree (templateid);



CREATE INDEX idx_egw_mls_template_state ON egw_milestone_template USING btree (state_id);



CREATE INDEX idx_egw_mls_template_status ON egw_milestone_template USING btree (status_id);



CREATE INDEX idx_egw_mls_template_subtype ON egw_milestone_template USING btree (subtype_id);



CREATE INDEX idx_egw_mls_template_worktype ON egw_milestone_template USING btree (worktype_id);



CREATE INDEX idx_egw_mul_est_ae ON egw_multiyearestimate USING btree (abstractestimate_id);



CREATE INDEX idx_egw_mul_est_financialyear ON egw_multiyearestimate USING btree (financialyear_id);



CREATE INDEX idx_egw_mult_appr_cd ON egw_multiyearestimateappr USING btree (created_by);



CREATE INDEX idx_egw_mult_appr_department ON egw_multiyearestimateappr USING btree (department_id);



CREATE INDEX idx_egw_mult_appr_fin_year ON egw_multiyearestimateappr USING btree (financialyear_id);



CREATE INDEX idx_egw_mult_appr_md ON egw_multiyearestimateappr USING btree (modified_by);



CREATE INDEX idx_egw_mult_appr_state ON egw_multiyearestimateappr USING btree (state_id);



CREATE INDEX idx_egw_mult_appr_status ON egw_multiyearestimateappr USING btree (status_id);



CREATE INDEX idx_egw_mult_yr_appr_dt ON egw_multiyearestimateapprdtls USING btree (multiyearestimateappr_id);



CREATE INDEX idx_egw_mult_yr_appr_dt_ae ON egw_multiyearestimateapprdtls USING btree (abstractestimate_id);



CREATE INDEX idx_egw_mult_yr_appr_dt_bdgt ON egw_multiyearestimateapprdtls USING btree (budgetgroup_id);



CREATE INDEX idx_egw_mult_yr_appr_dt_cd ON egw_multiyearestimateapprdtls USING btree (created_by);



CREATE INDEX idx_egw_mult_yr_appr_dt_func ON egw_multiyearestimateapprdtls USING btree (function_id);



CREATE INDEX idx_egw_mult_yr_appr_dt_fund ON egw_multiyearestimateapprdtls USING btree (fund_id);



CREATE INDEX idx_egw_mult_yr_appr_dt_md ON egw_multiyearestimateapprdtls USING btree (modified_by);



CREATE INDEX idx_egw_nego_status_state ON egw_negotiation_status USING btree (state_id);



CREATE INDEX idx_egw_negotiation_status_cd ON egw_negotiation_status USING btree (created_by);



CREATE INDEX idx_egw_negotiation_status_md ON egw_negotiation_status USING btree (modified_by);



CREATE INDEX idx_egw_nonsor_uom ON egw_nonsor USING btree (uom);



CREATE INDEX idx_egw_ohv_ae ON egw_overheadvalues USING btree (abstractestimate_id);



CREATE INDEX idx_egw_ohv_overhead ON egw_overheadvalues USING btree (overhead_id);



CREATE INDEX idx_egw_overhead_account ON egw_overhead USING btree (account);



CREATE INDEX idx_egw_overhead_rate_overhead ON egw_overhead_rate USING btree (overhead_id);



CREATE INDEX idx_egw_projectcode_status ON egw_projectcode USING btree (status_id);



CREATE INDEX idx_egw_rate_scheduleofrate ON egw_rate USING btree (scheduleofrate_id);



CREATE INDEX idx_egw_sch_category_parent ON egw_schedulecategory USING btree (parent);



CREATE INDEX idx_egw_sch_ofrate_category ON egw_scheduleofrate USING btree (category);



CREATE INDEX idx_egw_sch_ofrate_type ON egw_scheduleofrate USING btree (type);



CREATE INDEX idx_egw_sch_ofrate_uom ON egw_scheduleofrate USING btree (uom);



CREATE INDEX idx_egw_str_ded_bpd ON egw_statutorydeductions_bill USING btree (billpayeedetails_id);



CREATE INDEX idx_egw_str_ded_id_bill ON egw_statutorydeductions_bill USING btree (id_bill);



CREATE INDEX idx_egw_str_ded_subpartytype ON egw_statutorydeductions_bill USING btree (subpartytype_id);



CREATE INDEX idx_egw_str_ded_typeofwork ON egw_statutorydeductions_bill USING btree (typeofwork_id);



CREATE INDEX idx_egw_tender_estimate_wp ON egw_tender_estimate USING btree (works_package_id);



CREATE INDEX idx_egw_tm_act_mls_activity ON egw_track_milestone_activity USING btree (milestone_activity_id);



CREATE INDEX idx_egw_tm_act_trackmilestone ON egw_track_milestone_activity USING btree (trackmilestone_id);



CREATE INDEX idx_egw_tm_milestone ON egw_track_milestone USING btree (milestone_id);



CREATE INDEX idx_egw_tm_status ON egw_track_milestone USING btree (status_id);



CREATE INDEX idx_egw_tr_contractors_cd ON egw_tender_resp_contractors USING btree (created_by);



CREATE INDEX idx_egw_tr_contractors_md ON egw_tender_resp_contractors USING btree (modified_by);



CREATE INDEX idx_egw_tr_prepared ON egw_tender_response USING btree (prepared_by);



CREATE INDEX idx_egw_tr_quotes_cd ON egw_tender_response_quotes USING btree (created_by);



CREATE INDEX idx_egw_tr_quotes_md ON egw_tender_response_quotes USING btree (modified_by);



CREATE INDEX idx_egw_tr_status ON egw_tender_response USING btree (status_id);



CREATE INDEX idx_egw_typeofwork_created ON egw_typeofwork USING btree (createdby);



CREATE INDEX idx_egw_typeofwork_lastmd ON egw_typeofwork USING btree (lastmodifiedby);



CREATE INDEX idx_egw_typeofwork_parent ON egw_typeofwork USING btree (parentid);



CREATE INDEX idx_egw_wo_contractor ON egw_work_order USING btree (contractor_id);



CREATE INDEX idx_egw_wo_engineer_incharge ON egw_work_order USING btree (engineer_incharge);



CREATE INDEX idx_egw_wo_engineer_incharge2 ON egw_work_order USING btree (engineer_incharge2);



CREATE INDEX idx_egw_wo_estimate_est ON egw_workorder_estimate USING btree (estimate_id);



CREATE INDEX idx_egw_wo_estimate_workorder ON egw_workorder_estimate USING btree (workorder_id);



CREATE INDEX idx_egw_wo_parent ON egw_work_order USING btree (parentid);



CREATE INDEX idx_egw_wo_prepared ON egw_work_order USING btree (preparedby);



CREATE INDEX idx_egw_wo_status ON egw_work_order USING btree (status_id);



CREATE INDEX idx_egw_works_status_status ON egw_works_status USING btree (status_id);



CREATE INDEX idx_egw_wp_department ON egw_workspackage USING btree (department);



CREATE INDEX idx_egw_wp_det_id_wp ON egw_workspackage_details USING btree (id_workspackage);



CREATE INDEX idx_egw_wp_details_id_est ON egw_workspackage_details USING btree (id_estimate);



CREATE INDEX idx_egw_wp_prepared ON egw_workspackage USING btree (preparedby);



CREATE INDEX idx_egw_wp_status ON egw_workspackage USING btree (status_id);



CREATE UNIQUE INDEX idx_estate_estatenumber ON eglems_estate USING btree (estatenumber);



CREATE INDEX idx_fk_fpen_nominee_id ON egpen_family_pensioner_header USING btree (nominee_id);



CREATE INDEX idx_interstoretrfer_mrinlineid ON egf_interstoretransfer USING btree (mrinlineid);



CREATE INDEX idx_issuefrommrn_mrinlineid ON egf_issuedfrommrn USING btree (mrinlineid);



CREATE INDEX idx_issuefrommrn_mrnheaderid ON egf_issuedfrommrn USING btree (mrnheaderid);



CREATE INDEX idx_issuerepair_mrinheaderid ON egf_issuerepair USING btree (mrinheaderid);



CREATE INDEX idx_job_prinst ON jbpm_job USING btree (processinstance_);



CREATE INDEX idx_job_token ON jbpm_job USING btree (token_);



CREATE INDEX idx_job_tskinst ON jbpm_job USING btree (taskinstance_);



CREATE UNIQUE INDEX idx_lemsacqtype ON eglems_modeofacquisition USING btree (name);



CREATE UNIQUE INDEX idx_lemslandtype ON eglems_landtype USING btree (type);



CREATE UNIQUE INDEX idx_lemstender ON eglems_tender USING btree (tendernotificationnumber);



CREATE UNIQUE INDEX idx_lemsusagetype ON eglems_usagetype USING btree (type);



CREATE INDEX idx_moddef_procdf ON jbpm_moduledefinition USING btree (processdefinition_);



CREATE INDEX idx_modinst_prinst ON jbpm_moduleinstance USING btree (processinstance_);



CREATE INDEX idx_mrinheader_mrheaderid ON egf_mrinheader USING btree (mrheaderid);



CREATE INDEX idx_mrinline_mrinheaderid ON egf_mrinline USING btree (mrinheaderid);



CREATE INDEX idx_mrinline_voucherheaderid ON egf_mrinline USING btree (voucherheaderid);



CREATE INDEX idx_mrline_mrheaderid ON egf_mrline USING btree (mrheaderid);



CREATE INDEX idx_mrnheader_worksdetailid ON egf_mrheader USING btree (projectcodeid);



CREATE INDEX idx_mrnline_mrnheaderid ON egf_mrnline USING btree (mrnheaderid);



CREATE INDEX idx_mrnline_voucherheaderid ON egf_mrnline USING btree (voucherheaderid);



CREATE INDEX idx_mrnlineother_mrnlineid ON egf_mrnlineothers USING btree (mrnlineid);



CREATE INDEX idx_mrnpo_polineid ON egstores_mrnpodetails USING btree (polineid);



CREATE INDEX idx_node_action ON jbpm_node USING btree (action_);



CREATE INDEX idx_node_procdef ON jbpm_node USING btree (processdefinition_);



CREATE INDEX idx_node_suprstate ON jbpm_node USING btree (superstate_);



CREATE INDEX idx_pldactr_actid ON jbpm_pooledactor USING btree (actorid_);



CREATE INDEX idx_poline_mrheaderid ON egf_poline USING btree (mrheaderid);



CREATE INDEX idx_poline_mrlineid ON egf_poline USING btree (mrlineid);



CREATE INDEX idx_poline_worksdetailid ON egf_poline USING btree (worksdetailid);



CREATE INDEX idx_procdef_strtst ON jbpm_processdefinition USING btree (startstate_);



CREATE INDEX idx_procin_procdef ON jbpm_processinstance USING btree (processdefinition_);



CREATE INDEX idx_procin_roottk ON jbpm_processinstance USING btree (roottoken_);



CREATE INDEX idx_procin_sproctk ON jbpm_processinstance USING btree (superprocesstoken_);



CREATE INDEX idx_pstate_sbprcdef ON jbpm_node USING btree (subprocessdefinition_);



CREATE INDEX idx_qrtz_ft_inst_job_req_rcvry ON qrtz_fired_triggers USING btree (sched_name, instance_name, requests_recovery);



CREATE INDEX idx_qrtz_ft_j_g ON qrtz_fired_triggers USING btree (sched_name, job_name, job_group);



CREATE INDEX idx_qrtz_ft_jg ON qrtz_fired_triggers USING btree (sched_name, job_group);



CREATE INDEX idx_qrtz_ft_t_g ON qrtz_fired_triggers USING btree (sched_name, trigger_name, trigger_group);



CREATE INDEX idx_qrtz_ft_tg ON qrtz_fired_triggers USING btree (sched_name, trigger_group);



CREATE INDEX idx_qrtz_ft_trig_inst_name ON qrtz_fired_triggers USING btree (sched_name, instance_name);



CREATE INDEX idx_qrtz_j_grp ON qrtz_job_details USING btree (sched_name, job_group);



CREATE INDEX idx_qrtz_j_req_recovery ON qrtz_job_details USING btree (sched_name, requests_recovery);



CREATE INDEX idx_qrtz_t_c ON qrtz_triggers USING btree (sched_name, calendar_name);



CREATE INDEX idx_qrtz_t_g ON qrtz_triggers USING btree (sched_name, trigger_group);



CREATE INDEX idx_qrtz_t_j ON qrtz_triggers USING btree (sched_name, job_name, job_group);



CREATE INDEX idx_qrtz_t_jg ON qrtz_triggers USING btree (sched_name, job_group);



CREATE INDEX idx_qrtz_t_n_g_state ON qrtz_triggers USING btree (sched_name, trigger_group, trigger_state);



CREATE INDEX idx_qrtz_t_n_state ON qrtz_triggers USING btree (sched_name, trigger_name, trigger_group, trigger_state);



CREATE INDEX idx_qrtz_t_next_fire_time ON qrtz_triggers USING btree (sched_name, next_fire_time);



CREATE INDEX idx_qrtz_t_nft_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time);



CREATE INDEX idx_qrtz_t_nft_st ON qrtz_triggers USING btree (sched_name, trigger_state, next_fire_time);



CREATE INDEX idx_qrtz_t_nft_st_misfire ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_state);



CREATE INDEX idx_qrtz_t_nft_st_misfire_grp ON qrtz_triggers USING btree (sched_name, misfire_instr, next_fire_time, trigger_group, trigger_state);



CREATE INDEX idx_qrtz_t_state ON qrtz_triggers USING btree (sched_name, trigger_state);



CREATE INDEX idx_recordstatus_voucherhdid ON egf_record_status USING btree (voucherheaderid);



CREATE INDEX idx_remit_detail_gldtl ON eg_remittance_detail USING btree (remittancegldtlid);



CREATE INDEX idx_remit_detail_remit ON eg_remittance_detail USING btree (remittanceid);



CREATE INDEX idx_rom_mrinlineid ON egf_rom USING btree (mrinlineid);



CREATE INDEX idx_rom_mrnlineid ON egf_rom USING btree (mrnlineid);



CREATE INDEX idx_rtactn_action ON jbpm_runtimeaction USING btree (action_);



CREATE INDEX idx_rtactn_prcinst ON jbpm_runtimeaction USING btree (processinstance_);



CREATE INDEX idx_sale_mrinheaderid ON egf_sale USING btree (mrinheaderid);



CREATE INDEX idx_sale_mrinlineid ON egf_sale USING btree (mrinlineid);



CREATE INDEX idx_sc_createdby ON egw_satuschange USING btree (createdby);



CREATE INDEX idx_swimlinst_sl ON jbpm_swimlaneinstance USING btree (swimlane_);



CREATE INDEX idx_task_actorid ON jbpm_taskinstance USING btree (actorid_);



CREATE INDEX idx_task_procdef ON jbpm_task USING btree (processdefinition_);



CREATE INDEX idx_task_taskmgtdf ON jbpm_task USING btree (taskmgmtdefinition_);



CREATE INDEX idx_task_tsknode ON jbpm_task USING btree (tasknode_);



CREATE INDEX idx_taskinst_tokn ON jbpm_taskinstance USING btree (token_);



CREATE INDEX idx_taskinst_tsk ON jbpm_taskinstance USING btree (task_, procinst_);



CREATE INDEX idx_tkvarmap_ctxt ON jbpm_tokenvariablemap USING btree (contextinstance_);



CREATE INDEX idx_tkvvarmp_token ON jbpm_tokenvariablemap USING btree (token_);



CREATE INDEX idx_token_node ON jbpm_token USING btree (node_);



CREATE INDEX idx_token_parent ON jbpm_token USING btree (parent_);



CREATE INDEX idx_token_procin ON jbpm_token USING btree (processinstance_);



CREATE INDEX idx_token_subpi ON jbpm_token USING btree (subprocessinstance_);



CREATE INDEX idx_trans_procdef ON jbpm_transition USING btree (processdefinition_);



CREATE INDEX idx_transit_from ON jbpm_transition USING btree (from_);



CREATE INDEX idx_transit_to ON jbpm_transition USING btree (to_);



CREATE INDEX idx_tskinst_slinst ON jbpm_taskinstance USING btree (swimlaninstance_);



CREATE INDEX idx_tskinst_swlane ON jbpm_pooledactor USING btree (swimlaneinstance_);



CREATE INDEX idx_tskinst_tminst ON jbpm_taskinstance USING btree (taskmgmtinstance_);



CREATE INDEX idx_varinst_prcins ON jbpm_variableinstance USING btree (processinstance_);



CREATE INDEX idx_varinst_tk ON jbpm_variableinstance USING btree (token_);



CREATE INDEX idx_varinst_tkvarmp ON jbpm_variableinstance USING btree (tokenvariablemap_);



CREATE INDEX idx_vmis_dept ON vouchermis USING btree (departmentid);



CREATE INDEX idx_wf_propertydetail ON egptwf_floor_detail USING btree (id_property_detail);



CREATE INDEX idx_wfmodfloor_detail ON egpt_wfmodfloor_detail USING btree (id_wfmodpropid);



CREATE INDEX index_abs_est_categ ON egw_abstractestimate USING btree (category);



CREATE INDEX index_abs_est_date ON egw_abstractestimate USING btree (estimatedate);



CREATE INDEX index_abs_est_dept ON egw_abstractestimate USING btree (executingdepartment);



CREATE INDEX index_abs_est_parent_categ ON egw_abstractestimate USING btree (parent_category);



CREATE INDEX index_abs_est_preparedby ON egw_abstractestimate USING btree (preparedby);



CREATE INDEX index_abs_est_type ON egw_abstractestimate USING btree (type);



CREATE INDEX index_app_req_rc ON egw_dw_applicationrequest USING btree (rate_contract_id);



CREATE INDEX index_application_details_id ON egw_dw_roadcut_approval_letter USING btree (application_details_id);



CREATE INDEX index_dw_ar_portaluser_id ON egw_dw_applicationrequest USING btree (portaluser_id);



CREATE INDEX index_dw_uc_app_details_id ON egw_dw_utilization_certificate USING btree (application_details_id);



CREATE INDEX index_dw_uc_state_id ON egw_dw_utilization_certificate USING btree (state_id);



CREATE INDEX index_dw_uc_status_id ON egw_dw_utilization_certificate USING btree (status_id);



CREATE INDEX index_egbill_payd_accdetkey ON eg_billpayeedetails USING btree (accountdetailkeyid);



CREATE INDEX index_egw_ae_state_id ON egw_abstractestimate USING btree (state_id);



CREATE INDEX index_egw_assetforbill_wo_est ON egw_assetforbill USING btree (workorder_estimate_id);



CREATE INDEX index_egw_assetsforwo_wo_est ON egw_assetsforworkorder USING btree (workorder_estimate_id);



CREATE INDEX index_egw_mb_header_wo_est ON egw_mb_header USING btree (workorder_est_id);



CREATE INDEX index_egw_milestone_wo_est ON egw_milestone USING btree (workorder_est_id);



CREATE INDEX index_egw_wo_activity_act_id ON egw_work_order_activity USING btree (activity_id);



CREATE INDEX index_egw_wo_activity_wo_est ON egw_work_order_activity USING btree (workorder_estimate_id);



CREATE INDEX index_egw_wo_state_id ON egw_work_order USING btree (state_id);



CREATE INDEX index_egw_wp_state_id ON egw_workspackage USING btree (state_id);



CREATE INDEX index_eis_att_emp_id ON egeis_attendence USING btree (emp_id);



CREATE INDEX index_eis_att_fin_year_id ON egeis_attendence USING btree (fin_year_id);



CREATE INDEX index_eis_att_type_id ON egeis_attendence USING btree (type_id);



CREATE INDEX index_eis_bank_det_bank ON egeis_bank_det USING btree (bank);



CREATE INDEX index_eis_bank_det_id ON egeis_bank_det USING btree (id);



CREATE INDEX index_eis_compoff_att_id ON egeis_compoff USING btree (att_id);



CREATE INDEX index_eis_compoff_status_id ON egeis_compoff USING btree (status_id);



CREATE INDEX index_eis_lv_app_desig_id ON egeis_leave_application USING btree (desig_id);



CREATE INDEX index_eis_lv_app_emp_id ON egeis_leave_application USING btree (emp_id);



CREATE INDEX index_eis_lv_app_ltype_id ON egeis_leave_application USING btree (leave_type_id);



CREATE INDEX index_eis_lv_app_status ON egeis_leave_application USING btree (status);



CREATE INDEX index_eis_lv_appr_appl_id ON egeis_leave_approval USING btree (application_id);



CREATE INDEX index_eis_lv_appr_apprd_by ON egeis_leave_approval USING btree (approved_by);



CREATE INDEX index_emp_assgn_designationid ON eg_emp_assignment USING btree (designationid);



CREATE INDEX index_emp_assgn_id_emp ON eg_emp_assignment_prd USING btree (id_employee);



CREATE INDEX index_emp_assgn_id_function ON eg_emp_assignment USING btree (id_function);



CREATE INDEX index_emp_assgn_id_functionary ON eg_emp_assignment USING btree (id_functionary);



CREATE INDEX index_emp_assgn_id_fund ON eg_emp_assignment USING btree (id_fund);



CREATE INDEX index_emp_assgn_id_prd ON eg_emp_assignment USING btree (id_emp_assign_prd);



CREATE INDEX index_emp_assgn_main_dept ON eg_emp_assignment USING btree (main_dept);



CREATE INDEX index_emp_assgn_position_id ON eg_emp_assignment USING btree (position_id);



CREATE INDEX index_emp_assgn_reports_to ON eg_emp_assignment USING btree (reports_to);



CREATE INDEX index_emp_blood_group ON eg_employee USING btree (blood_group);



CREATE INDEX index_emp_category_id ON eg_employee USING btree (category_id);



CREATE INDEX index_emp_community_id ON eg_employee USING btree (community_id);



CREATE INDEX index_emp_date_of_birth ON eg_employee USING btree (date_of_birth);



CREATE INDEX index_emp_dept_assignment_id ON eg_employee_dept USING btree (assignment_id);



CREATE INDEX index_emp_dept_deptid ON eg_employee_dept USING btree (deptid);



CREATE INDEX index_emp_dept_hod ON eg_employee_dept USING btree (hod);



CREATE INDEX index_emp_emp_firstname ON eg_employee USING btree (emp_firstname);



CREATE INDEX index_emp_gender ON eg_employee USING btree (gender);



CREATE INDEX index_emp_grade_emp_id ON egeis_employee_grade USING btree (emp_id);



CREATE INDEX index_emp_grade_id ON eg_employee USING btree (grade_id);



CREATE INDEX index_emp_id_dept ON eg_employee USING btree (id_dept);



CREATE INDEX index_emp_id_user ON eg_employee USING btree (id_user);



CREATE INDEX index_emp_isactive ON eg_employee USING btree (isactive);



CREATE INDEX index_emp_languages_known_id ON eg_employee USING btree (languages_known_id);



CREATE INDEX index_emp_mode_of_rect_id ON eg_employee USING btree (mode_of_recruiment_id);



CREATE INDEX index_emp_mother_tonuge ON eg_employee USING btree (mother_tonuge);



CREATE INDEX index_emp_qulified_id ON eg_employee USING btree (qulified_id);



CREATE INDEX index_emp_recruitment_type_id ON eg_employee USING btree (recruitment_type_id);



CREATE INDEX index_emp_religion_id ON eg_employee USING btree (religion_id);



CREATE INDEX index_emp_status_id ON eg_employee USING btree (employment_status);



CREATE INDEX index_est_apprn_hist ON egw_est_apprn_history USING btree (abstractestimate_id);



CREATE INDEX index_est_apprn_hist_bud ON egw_est_apprn_history USING btree (budgetusage_id);



CREATE INDEX index_estimate_app_req ON egw_abstractestimate USING btree (application_request_id);



CREATE INDEX index_estimate_rc ON egw_abstractestimate USING btree (rate_contract_id);



CREATE INDEX index_fin_details_abs_est_id ON egw_financialdetail USING btree (abstractestimate_id);



CREATE INDEX index_fin_details_bdgt ON egw_financialdetail USING btree (budgetgroup_id);



CREATE INDEX index_fin_details_coa_id ON egw_financialdetail USING btree (coa_id);



CREATE INDEX index_fin_details_func ON egw_financialdetail USING btree (functionary_id);



CREATE INDEX index_fin_details_funcn ON egw_financialdetail USING btree (function_id);



CREATE INDEX index_fin_details_fund_id ON egw_financialdetail USING btree (fund_id);



CREATE INDEX index_fin_details_scheme ON egw_financialdetail USING btree (scheme_id);



CREATE INDEX index_fin_details_sub_scheme ON egw_financialdetail USING btree (subscheme_id);



CREATE INDEX index_indent_dept ON egw_indent USING btree (department_id);



CREATE INDEX index_indent_fund ON egw_indent USING btree (fund_id);



CREATE INDEX index_indent_juris ON egw_indent_jurisdiction USING btree (indent_id);



CREATE INDEX index_indent_juris_zone ON egw_indent_jurisdiction USING btree (zone_id);



CREATE INDEX index_indent_mntnce_dtl ON egw_indent_maintenance_detail USING btree (indent_id);



CREATE INDEX index_indent_status ON egw_indent USING btree (status_id);



CREATE INDEX index_indent_tender_rc ON egw_indent_tender USING btree (rate_contract_id);



CREATE INDEX index_indent_wfstate ON egw_indent USING btree (state_id);



CREATE INDEX index_indent_work_subtype ON egw_indent USING btree (work_subtype_id);



CREATE INDEX index_indent_worktype ON egw_indent USING btree (work_type_id);



CREATE INDEX index_indnt_juris_division ON egw_indent_jurisdiction USING btree (division_id);



CREATE INDEX index_indnt_tend_contractor ON egw_indent_tender USING btree (contractor_id);



CREATE INDEX index_indnt_tend_status ON egw_indent_tender USING btree (status_id);



CREATE INDEX index_it_ltst_offline_status ON egw_indent_tender USING btree (latest_offline_status_id);



CREATE INDEX index_pay_batchfail_deptid ON egpay_batchfailuredetails USING btree (deptid);



CREATE INDEX index_pay_batchfail_empid ON egpay_batchfailuredetails USING btree (empid);



CREATE INDEX index_pay_batchfail_finyrid ON egpay_batchfailuredetails USING btree (financialyearid);



CREATE INDEX index_pay_batchfail_status ON egpay_batchfailuredetails USING btree (status);



CREATE INDEX index_pay_batchgendt_finyrid ON egpay_batchgendetails USING btree (financialyearid);



CREATE INDEX index_pay_batchgendt_id_dept ON egpay_batchgendetails USING btree (id_dept);



CREATE INDEX index_pay_batchgendt_status ON egpay_batchgendetails USING btree (status);



CREATE INDEX index_pay_ded_id_accountcode ON egpay_deductions USING btree (id_accountcode);



CREATE INDEX index_pay_ded_id_emppayroll ON egpay_deductions USING btree (id_emppayroll);



CREATE INDEX index_pay_ded_id_sal_advance ON egpay_deductions USING btree (id_sal_advance);



CREATE INDEX index_pay_ded_id_salcode ON egpay_deductions USING btree (id_salcode);



CREATE INDEX index_pay_ear_id_emppayroll ON egpay_earnings USING btree (id_emppayroll);



CREATE INDEX index_pay_excp_finyrid ON egpay_exception USING btree (financilayear_id);



CREATE INDEX index_pay_excp_id_employee ON egpay_exception USING btree (id_employee);



CREATE INDEX index_pay_excp_id_exp_mst ON egpay_exception USING btree (id_exceptionmstr);



CREATE INDEX index_pay_excp_status ON egpay_exception USING btree (status);



CREATE INDEX index_pay_excp_userid ON egpay_exception USING btree (userid);



CREATE INDEX index_pay_incr_empid ON egpay_incrementdetails USING btree (empid);



CREATE INDEX index_pay_incr_finyrid ON egpay_incrementdetails USING btree (financialyearid);



CREATE INDEX index_pay_incr_status ON egpay_incrementdetails USING btree (status);



CREATE INDEX index_pay_pf_pfheaderid ON egpay_pfdetails USING btree (pfheaderid);



CREATE INDEX index_pay_proll_finyrid ON egpay_emppayroll USING btree (financialyearid);



CREATE INDEX index_pay_proll_id_billreg ON egpay_emppayroll USING btree (id_billregister);



CREATE INDEX index_pay_proll_id_emp_asgn ON egpay_emppayroll USING btree (id_emp_assignment);



CREATE INDEX index_pay_proll_status ON egpay_emppayroll USING btree (status);



CREATE INDEX index_pay_pscale_bankacc_id ON egpay_saladvances USING btree (bankaccount_id);



CREATE INDEX index_pay_pscale_id_emp ON egpay_saladvances USING btree (id_employee);



CREATE INDEX index_pay_pscale_id_payheader ON egpay_payscale_details USING btree (id_payheader);



CREATE INDEX index_pay_pscale_id_salcode ON egpay_saladvances USING btree (id_salcode);



CREATE INDEX index_pay_pscale_id_salcodes ON egpay_payscale_details USING btree (id_salarycodes);



CREATE INDEX index_pay_pscale_status ON egpay_saladvances USING btree (status);



CREATE INDEX index_pay_psemp_id_emp ON egpay_payscale_employee USING btree (id_employee);



CREATE INDEX index_pay_psemp_id_payhd ON egpay_payscale_employee USING btree (id_payheader);



CREATE INDEX index_pay_salcode_glcodeid ON egpay_salarycodes USING btree (glcodeid);



CREATE INDEX index_pay_salcode_int_glcodeid ON egpay_salarycodes USING btree (interest_glcodeid);



CREATE INDEX index_pay_salcode_order_id ON egpay_salarycodes USING btree (order_id);



CREATE INDEX index_pay_salcode_salcodes ON egpay_salarycodes USING btree (categoryid);



CREATE INDEX index_pay_salcode_tds_id ON egpay_salarycodes USING btree (tds_id);



CREATE INDEX index_rc_contractor ON egw_rate_contract USING btree (contractor_id);



CREATE INDEX index_rc_indent ON egw_rate_contract USING btree (indent_id);



CREATE INDEX index_rc_state ON egw_rate_contract USING btree (state_id);



CREATE INDEX index_rc_status ON egw_rate_contract USING btree (status_id);



CREATE INDEX index_retender_his_ret_id ON egw_retender_history USING btree (retender_id);



CREATE INDEX index_retender_his_wp_id ON egw_retender_history USING btree (workspackage_id);



CREATE INDEX index_retender_wp_id ON egw_retender USING btree (workspackage_id);



CREATE INDEX index_state_id ON egw_dw_roadcut_approval_letter USING btree (state_id);



CREATE INDEX index_status_id ON egw_dw_roadcut_approval_letter USING btree (status_id);



CREATE INDEX index_tender_scrt_abst ON egw_tender_scrutinizing_abst USING btree (tender_response_id);



CREATE INDEX indx__tradeegtl_name_transfer ON egtl_name_transfer USING btree (trade_id);



CREATE INDEX indx_acdk_acdtid ON accountdetailkey USING btree (detailtypeid);



CREATE INDEX indx_advbill_advid ON eglc_advocate_bill USING btree (id_advocate);



CREATE INDEX indx_advbill_state ON eglc_advocate_bill USING btree (state_id);



CREATE INDEX indx_advdept_deptid ON eglc_advocatedept_mapping USING btree (id_department);



CREATE INDEX indx_advreqdetail_advreqid ON eg_advancerequisitiondetails USING btree (advancerequisitionid);



CREATE INDEX indx_advreqdetail_functionid ON eg_advancerequisitiondetails USING btree (functionid);



CREATE INDEX indx_advreqdetail_glcodeid ON eg_advancerequisitiondetails USING btree (glcodeid);



CREATE INDEX indx_advreqmis_advreqid ON eg_advancerequisitionmis USING btree (advancerequisitionid);



CREATE INDEX indx_advreqmis_deptid ON eg_advancerequisitionmis USING btree (departmentid);



CREATE INDEX indx_advreqmis_fieldid ON eg_advancerequisitionmis USING btree (fieldid);



CREATE INDEX indx_advreqmis_functionaryid ON eg_advancerequisitionmis USING btree (functionaryid);



CREATE INDEX indx_advreqmis_fundid ON eg_advancerequisitionmis USING btree (fundid);



CREATE INDEX indx_advreqmis_fundsourceid ON eg_advancerequisitionmis USING btree (fundsourceid);



CREATE INDEX indx_advreqmis_schemeid ON eg_advancerequisitionmis USING btree (schemeid);



CREATE INDEX indx_advreqmis_subfieldid ON eg_advancerequisitionmis USING btree (subfieldid);



CREATE INDEX indx_advreqmis_subschemeid ON eg_advancerequisitionmis USING btree (subschemeid);



CREATE INDEX indx_advreqmis_vhid ON eg_advancerequisitionmis USING btree (voucherheaderid);



CREATE INDEX indx_aem_acdtid ON accountentitymaster USING btree (detailtypeid);



CREATE INDEX indx_apprd_buildingdetail ON egbpa_apprd_buildingdetails USING btree (id, registrationid);



CREATE INDEX indx_apprd_buildingfloordtls ON egbpa_apprd_buildingfloordtls USING btree (id, approvedbldgid);



CREATE INDEX indx_arpd_adtid ON eg_advancereqpayeedetails USING btree (accountdetailtypeid);



CREATE INDEX indx_arpd_ardid ON eg_advancereqpayeedetails USING btree (advancerequisitiondetailid);



CREATE INDEX indx_arpd_tdsid ON eg_advancereqpayeedetails USING btree (tdsid);



CREATE INDEX indx_attr_type_column ON egdiary_attribute_values USING btree (id_diary_attr_type_column);



CREATE INDEX indx_bankaccount_coaid ON bankaccount USING btree (glcodeid);



CREATE INDEX indx_bankaccount_fundid ON bankaccount USING btree (fundid);



CREATE INDEX indx_basic_idpropid ON egpt_basic_property USING btree (id_propertyid);



CREATE INDEX indx_basic_idsatus ON egpt_basic_property USING btree (status);



CREATE INDEX indx_bat_lcid ON eglc_legalcase_batchcase USING btree (id_legalcase);



CREATE INDEX indx_be_bankaccountid ON bankentries USING btree (bankaccountid);



CREATE INDEX indx_be_coaid ON bankentries USING btree (glcodeid);



CREATE INDEX indx_be_vhid ON bankentries USING btree (voucherheaderid);



CREATE INDEX indx_bg_majorcode ON egf_budgetgroup USING btree (majorcode);



CREATE INDEX indx_bg_maxcode ON egf_budgetgroup USING btree (maxcode);



CREATE INDEX indx_bill_lcid ON eglc_bill USING btree (id_legalcase);



CREATE INDEX indx_billdet_idbil ON eg_bill_details USING btree (id_bill);



CREATE INDEX indx_billmis_departmentid ON eg_billregistermis USING btree (departmentid);



CREATE INDEX indx_billmis_voucherheaderid ON eg_billregistermis USING btree (voucherheaderid);



CREATE INDEX indx_billreg_expendituretype ON eg_billregister USING btree (expendituretype);



CREATE INDEX indx_billreg_statusid ON eg_billregister USING btree (statusid);



CREATE INDEX indx_bndrycat_idcat ON egpt_bndry_category USING btree (id_category);



CREATE INDEX indx_bpaex_insp_measurentdtls ON egbpaextnd_inspect_measuredtls USING btree (id, inspectiondtlsid, inspectionsourceid);



CREATE INDEX indx_bpaex_inspection ON egbpaextnd_inspection USING btree (id, registrationid);



CREATE INDEX indx_bpaex_lpchecklist ON egbpaextnd_lpchecklist USING btree (id, lpid);



CREATE INDEX indx_bpaex_regn_details ON egbpaextnd_regn_details USING btree (id, registrationid);



CREATE INDEX indx_bpaex_rejchecklist ON egbpaextnd_rejection_checklist USING btree (id, rejectionid);



CREATE INDEX indx_bpaext_regn_autodcr ON egbpaextnd_regn_autodcr USING btree (id, registrationid, lpid, autodcr_num);



CREATE INDEX indx_bpd_lcid ON eglc_bipartisandetails USING btree (id_legalcase);



CREATE INDEX indx_bpext_bldcat_proregndtl ON egbpaextnd_regn_details USING btree (proposed_bldgcatgid);



CREATE INDEX indx_br_bankaccountid ON bankreconciliation USING btree (bankaccountid);



CREATE INDEX indx_cat_eggr_comp_types ON eggr_complainttypes USING btree (category);



CREATE INDEX indx_cat_idusage ON egpt_category USING btree (id_usage);



CREATE INDEX indx_challan_collheaderid ON egcl_challanheader USING btree (receiptid);



CREATE INDEX indx_challan_state ON egcl_challanheader USING btree (wfstateid);



CREATE INDEX indx_challan_status ON egcl_challanheader USING btree (statusid);



CREATE INDEX indx_citizenactserv_citizn_fk ON egp_citizenactiveservcreg USING btree (portaluserid);



CREATE INDEX indx_citizenactserv_servreg_fk ON egp_citizenactiveservcreg USING btree (serviceregid);



CREATE INDEX indx_citizennotif_citizn_fk ON egp_citizennotification USING btree (serviceregid);



CREATE INDEX indx_citizenservreg_action_fk ON egp_citizenserviceregistry USING btree (actionid);



CREATE INDEX indx_citizenservreg_module_fk ON egp_citizenserviceregistry USING btree (moduleid);



CREATE INDEX indx_citizenservreg_parent_fk ON egp_citizenserviceregistry USING btree (parentid);



CREATE INDEX indx_citizenservreq_citizn_fk ON egp_citizenservicereqregistry USING btree (portaluserid);



CREATE INDEX indx_citizenservreq_servreg_fk ON egp_citizenservicereqregistry USING btree (serviceregid);



CREATE INDEX indx_cjv_faccountid ON contrajournalvoucher USING btree (frombankaccountid);



CREATE INDEX indx_cjv_toaccountid ON contrajournalvoucher USING btree (tobankaccountid);



CREATE INDEX indx_cm_bndryid ON codemapping USING btree (eg_boundaryid);



CREATE INDEX indx_cm_cashinhand ON codemapping USING btree (cashinhand);



CREATE INDEX indx_cm_chequeinhand ON codemapping USING btree (chequeinhand);



CREATE INDEX indx_coa_payscheduleid ON chartofaccounts USING btree (paymentscheduleid);



CREATE INDEX indx_coa_purposeid ON chartofaccounts USING btree (purposeid);



CREATE INDEX indx_coa_receiptscheduleid ON chartofaccounts USING btree (receiptscheduleid);



CREATE INDEX indx_coa_scheduleid ON chartofaccounts USING btree (scheduleid);



CREATE INDEX indx_coad_acdtid ON chartofaccountdetail USING btree (detailtypeid);



CREATE INDEX indx_coad_coaid ON chartofaccountdetail USING btree (glcodeid);



CREATE INDEX indx_colldet_acchead ON egcl_collectiondetails USING btree (id_accounthead);



CREATE INDEX indx_collhd_locid ON egcl_collectionheader USING btree (id_location);



CREATE INDEX indx_collhd_refchid ON egcl_collectionheader USING btree (reference_ch_id);



CREATE INDEX indx_collhd_state ON egcl_collectionheader USING btree (state_id);



CREATE INDEX indx_collmis_depositedinbank ON egcl_collectionmis USING btree (id_depositedinbank);



CREATE INDEX indx_collmis_deptid ON egcl_collectionmis USING btree (id_department);



CREATE INDEX indx_collrcpts_iddmddet ON egdm_collected_receipts USING btree (id_demand_detail);



CREATE INDEX indx_column_activity_type ON egdiary_attribute_type_column USING btree (id_diary_activity_type);



CREATE INDEX indx_commu_status_t ON eggr_status_tracker USING btree (communicationid);



CREATE INDEX indx_comp_rec_mode_compdtls ON eggr_complaintdetails USING btree (id_mode);



CREATE INDEX indx_compdtls_iv_upload ON eggr_imagevideo_upload USING btree (complaintid);



CREATE INDEX indx_compgrp_comptypes ON eggr_complainttypes USING btree (complaintgroup_id);



CREATE INDEX indx_compstatus_status_roles ON eggr_status_roles USING btree (status);



CREATE INDEX indx_compstatus_tracker ON eggr_status_tracker USING btree (statusid);



CREATE INDEX indx_comptype_complaintdetail ON eggr_complaintdetails USING btree (complainttype);



CREATE INDEX indx_contadv_drawingofficer ON egw_contractor_advance USING btree (drawing_officer);



CREATE INDEX indx_contadv_workorderest ON egw_contractor_advance USING btree (workorderestimate_id);



CREATE INDEX indx_csm_glcodeid ON codeservicemap USING btree (glcodeid);



CREATE INDEX indx_csm_serviceid ON codeservicemap USING btree (serviceid);



CREATE INDEX indx_dd_activity_type ON egdiary_details USING btree (id_diary_activity_type);



CREATE INDEX indx_dd_attr_values ON egdiary_details USING btree (id_diary_attr_values);



CREATE INDEX indx_dd_header ON egdiary_details USING btree (id_diary_header);



CREATE INDEX indx_dem_reason_code ON eg_demand_reason_master USING btree (code);



CREATE INDEX indx_dem_reason_det ON eg_demand_reason_details USING btree (id_demand_reason);



CREATE INDEX indx_dem_reason_inst ON eg_demand_reason USING btree (id_demand_reason_master, id_installment);



CREATE INDEX indx_dem_reason_mstr ON eg_demand_reason_master USING btree (reason_master);



CREATE INDEX indx_demand_det_mvs ON eg_demand_details USING btree (id_demand, id_demand_reason, amount, amt_collected);



CREATE INDEX indx_dimg_act_type ON egdiary_images USING btree (id_diary_activity_type);



CREATE INDEX indx_dimg_header ON egdiary_images USING btree (id_diary_header);



CREATE INDEX indx_disp_lcid ON eglc_legalcasedisposal USING btree (id_legalcase);



CREATE INDEX indx_ead_actionbyid ON eg_actiondetails USING btree (actiondoneby);



CREATE INDEX indx_eb_bndrytypeid ON eg_boundary USING btree (id_bndry_type);



CREATE INDEX indx_ebd_billid ON eg_billdetails USING btree (billid);



CREATE INDEX indx_ebd_cbdid ON egw_bill_deductions USING btree (contractorbilldetailid);



CREATE INDEX indx_ebd_coaid ON egw_bill_deductions USING btree (glcodeid);



CREATE INDEX indx_ebd_functionid ON eg_billdetails USING btree (functionid);



CREATE INDEX indx_ebd_glcodeid ON eg_billdetails USING btree (glcodeid);



CREATE INDEX indx_ebd_tdsid ON egw_bill_deductions USING btree (tdsid);



CREATE INDEX indx_ebpd_adtid ON eg_billpayeedetails USING btree (accountdetailtypeid);



CREATE INDEX indx_ebpd_bdid ON eg_billpayeedetails USING btree (billdetailid);



CREATE INDEX indx_ebrm_billid ON eg_billregistermis USING btree (billid);



CREATE INDEX indx_ebrm_fieldid ON eg_billregistermis USING btree (fieldid);



CREATE INDEX indx_ebrm_funationaryid ON eg_billregistermis USING btree (functionaryid);



CREATE INDEX indx_ebrm_fundid ON eg_billregistermis USING btree (fundid);



CREATE INDEX indx_ebrm_segmentid ON eg_billregistermis USING btree (segmentid);



CREATE INDEX indx_ebrm_subfieldid ON eg_billregistermis USING btree (subfieldid);



CREATE INDEX indx_ebrm_subsegid ON eg_billregistermis USING btree (subsegmentid);



CREATE INDEX indx_ecm_glcodeid ON egf_cess_master USING btree (glcodeid);



CREATE INDEX indx_ecm_taxcodeid ON egf_cess_master USING btree (taxcodeid);



CREATE INDEX indx_eg_token_tokenno ON eg_token USING btree (token_number);



CREATE INDEX indx_egbill_idemand ON eg_bill USING btree (id_demand);



CREATE INDEX indx_egbpaaddress ON egbpa_address USING btree (registrationid, addresstypeid, stateid);



CREATE INDEX indx_egbpaetnd_inspdtl_chklt ON egbpaextnd_insp_checklist USING btree (inspectiondtlsid);



CREATE INDEX indx_egbpaex_cltdtl_ispchk ON egbpaextnd_insp_checklist USING btree (checklistdetailid);



CREATE INDEX indx_egbpaext_bldug_proflrdl ON egbpaextnd_autodcr_floordetail USING btree (proposed_bldg_usage);



CREATE INDEX indx_egbpaext_blgusg_exiaflr ON egbpaextnd_autodcr_floordetail USING btree (existing_bldg_usage);



CREATE INDEX indx_egbpaext_chklstdtl_lpck ON egbpaextnd_lpchecklist USING btree (checklistdetailid);



CREATE INDEX indx_egbpaextn_apprd_bngflrdtl ON egbpaextnd_apprd_bldgfloordtls USING btree (id, approvedbldgid);



CREATE INDEX indx_egbpaextnd_apprd_bldngdtl ON egbpaextnd_apprd_bldgdetails USING btree (id, registrationid);



CREATE INDEX indx_egbpaextnd_bldcat_bldndtl ON egbpaextnd_regn_details USING btree (exist_bldgcatgid);



CREATE INDEX indx_egbpaextnd_bldusgbldfldtl ON egbpaextnd_apprd_bldgfloordtls USING btree (exist_bldg_usage);



CREATE INDEX indx_egbpaextnd_bldusgprofrdtl ON egbpaextnd_apprd_bldgfloordtls USING btree (proposed_bldg_usage);



CREATE INDEX indx_egbpaextnd_bpafee_coa ON egbpaextnd_mstr_bpafee USING btree (glcodeid);



CREATE INDEX indx_egbpaextnd_chkdtl_rgnck ON egbpaextnd_regn_checklist USING btree (checklistdetailid);



CREATE INDEX indx_egbpaextnd_chkdtl_rjchk ON egbpaextnd_rejection_checklist USING btree (checklistdetailid);



CREATE INDEX indx_egbpaextnd_chklst_dtl ON egbpaextnd_mstr_chklistdetail USING btree (checklistid);



CREATE INDEX indx_egbpaextnd_constg_inspdtl ON egbpaextnd_inspection_details USING btree (conststagesid);



CREATE INDEX indx_egbpaextnd_dcrflr_adcr ON egbpaextnd_autodcr_floordetail USING btree (autodcr_id);



CREATE INDEX indx_egbpaextnd_ddfeedtl_bank ON egbpaextnd_ddfee_details USING btree (ddbank);



CREATE INDEX indx_egbpaextnd_dochstry ON egbpaextnd_documenthistory USING btree (registrationid);



CREATE INDEX indx_egbpaextnd_fee_feedtl ON egbpaextnd_mstr_bpafeedetail USING btree (feeid);



CREATE INDEX indx_egbpaextnd_fee_regnfedtl ON egbpaextnd_regnfeedetail USING btree (bpafeeid);



CREATE INDEX indx_egbpaextnd_insp_lparty ON egbpaextnd_lettertoparty USING btree (inspectionid);



CREATE INDEX indx_egbpaextnd_locbnd_bparegn ON egbpaextnd_registration USING btree (locboundaryid);



CREATE INDEX indx_egbpaextnd_lprsn_lp ON egbpaextnd_lettertoparty USING btree (lp_reasonid);



CREATE INDEX indx_egbpaextnd_regn_ddfeedtl ON egbpaextnd_ddfee_details USING btree (registrationid);



CREATE INDEX indx_egbpaextnd_regn_eg_dmd ON egbpaextnd_registration USING btree (demandid);



CREATE INDEX indx_egbpaextnd_regn_regnfee ON egbpaextnd_registrationfee USING btree (registrationid);



CREATE INDEX indx_egbpaextnd_regn_rgnlst ON egbpaextnd_regn_checklist USING btree (registrationid);



CREATE INDEX indx_egbpaextnd_regn_stsdtl ON egbpaextnd_regnstatus_details USING btree (registrationid);



CREATE INDEX indx_egbpaextnd_regn_wf_state ON egbpaextnd_registration USING btree (stateid);



CREATE INDEX indx_egbpaextnd_regnfe_fedtl ON egbpaextnd_regnfeedetail USING btree (registrationfeeid);



CREATE INDEX indx_egbpaextnd_regnfee_wf_ste ON egbpaextnd_registrationfee USING btree (stateid);



CREATE INDEX indx_egbpaextnd_srvtype_chklst ON egbpaextnd_mstr_checklist USING btree (servicetypeid);



CREATE INDEX indx_egbpaextnd_srvtype_regn ON egbpaextnd_registration USING btree (servicetypeid);



CREATE INDEX indx_egbpaextnd_survname_regn ON egbpaextnd_registration USING btree (surveyorid);



CREATE INDEX indx_egbpaextnd_village_addr ON egbpaextnd_address USING btree (villageid);



CREATE INDEX indx_egbpaextndaddress ON egbpaextnd_address USING btree (registrationid, addresstypeid, stateid);



CREATE INDEX indx_egbpatnd_adnbndy_bparegn ON egbpaextnd_registration USING btree (adminboundaryid);



CREATE UNIQUE INDEX indx_egtl_demand ON egtl_demand USING btree (demand_id, license_id);



CREATE INDEX indx_egtl_dept_mstr_sub_ctgr ON egtl_mstr_trade_sub_ctgr USING btree (id_trade_department);



CREATE INDEX indx_egtl_lic_cancel_info ON egtl_license_cancel_info USING btree (license_id);



CREATE INDEX indx_egtl_lic_dup_lic_info ON egtl_duplicate_license_info USING btree (license_id);



CREATE INDEX indx_egtl_lic_objtn_info ON egtl_license_objection_info USING btree (license_id);



CREATE INDEX indx_egtl_lic_revoke_info ON egtl_license_revoke_info USING btree (license_id);



CREATE INDEX indx_egtl_mstr_apptype_pfa_fee ON egtl_mstr_pfa_fee USING btree (id_application_type);



CREATE INDEX indx_egtl_mstr_schdl_wt_licfee ON egtl_mstr_weight_licfee USING btree (id_schedule);



CREATE INDEX indx_egtl_sub_ctgr_wt_licfee ON egtl_mstr_weight_licfee USING btree (id_trade_subctgr);



CREATE INDEX indx_egtl_trd_nature_subctgr ON egtl_mstr_trade_sub_ctgr USING btree (id_trade_nature);



CREATE INDEX indx_egw_cfd_bdgtgrp ON egw_change_financialdetails USING btree (budgetgroup_id);



CREATE INDEX indx_egw_cfd_createdby ON egw_change_financialdetails USING btree (created_by);



CREATE INDEX indx_egw_cfd_dep_coa ON egw_change_financialdetails USING btree (deposit_coa);



CREATE INDEX indx_egw_cfd_function ON egw_change_financialdetails USING btree (function_id);



CREATE INDEX indx_egw_cfd_fund ON egw_change_financialdetails USING btree (fund_id);



CREATE INDEX indx_egw_cfd_modifiedby ON egw_change_financialdetails USING btree (modified_by);



CREATE INDEX indx_egw_cfd_scheme ON egw_change_financialdetails USING btree (scheme_id);



CREATE INDEX indx_egw_cfd_state ON egw_change_financialdetails USING btree (state_id);



CREATE INDEX indx_egw_cfd_status ON egw_change_financialdetails USING btree (status_id);



CREATE INDEX indx_egw_cfd_subscheme ON egw_change_financialdetails USING btree (subscheme_id);



CREATE INDEX indx_egw_cfdest_bdgtgrp_old ON egw_change_fd_estimate USING btree (old_budget_group);



CREATE INDEX indx_egw_cfdest_changefd_id ON egw_change_fd_estimate USING btree (change_fd_id);



CREATE INDEX indx_egw_cfdest_dep_coa_old ON egw_change_fd_estimate USING btree (old_dep_coa);



CREATE INDEX indx_egw_cfdest_dep_code_old ON egw_change_fd_estimate USING btree (old_dep_code);



CREATE INDEX indx_egw_cfdest_estid ON egw_change_fd_estimate USING btree (abstractestimate_id);



CREATE INDEX indx_egw_cfdest_function_old ON egw_change_fd_estimate USING btree (old_function);



CREATE INDEX indx_egw_cfdest_fund_old ON egw_change_fd_estimate USING btree (old_fund);



CREATE INDEX indx_egw_cfdest_scheme_old ON egw_change_fd_estimate USING btree (old_scheme);



CREATE INDEX indx_egw_cfdest_subscheme_old ON egw_change_fd_estimate USING btree (old_sub_scheme);



CREATE INDEX indx_egw_projcodemis_proj_code ON egw_projectcodemis USING btree (projectcode_id);



CREATE INDEX indx_ell_locationid ON eg_login_log USING btree (locationid);



CREATE INDEX indx_ell_userid ON eg_login_log USING btree (userid);



CREATE INDEX indx_emh_activityid ON egw_mb_details USING btree (wo_activity_id);



CREATE INDEX indx_emh_mbheaderid ON egw_mb_details USING btree (mbheader_id);



CREATE INDEX indx_emh_preparedby ON egw_mb_header USING btree (prepared_by);



CREATE INDEX indx_emh_workorderid ON egw_mb_header USING btree (workorder_id);



CREATE INDEX indx_emphear_empid ON eglc_employeehearing USING btree (id_employee);



CREATE INDEX indx_emphear_hearingid ON eglc_employeehearing USING btree (id_hearing);



CREATE INDEX indx_en_fpid ON eg_numbers USING btree (fiscialperiodid);



CREATE INDEX indx_eram_actionid ON eg_roleaction_map USING btree (actionid);



CREATE INDEX indx_eram_roleid ON eg_roleaction_map USING btree (roleid);



CREATE INDEX indx_erm_fieldid ON egf_receipt_mis USING btree (fieldid);



CREATE INDEX indx_erm_functionid ON egf_receipt_mis USING btree (functionid);



CREATE INDEX indx_erm_fundid ON egf_receipt_mis USING btree (fundid);



CREATE INDEX indx_erm_segid ON egf_receipt_mis USING btree (segmentid);



CREATE INDEX indx_erm_subsegid ON egf_receipt_mis USING btree (sub_segmentid);



CREATE INDEX indx_erm_vhid ON egf_receipt_mis USING btree (voucherheaderid);



CREATE INDEX indx_esc_accountid ON eg_surrendered_cheques USING btree (bankaccountid);



CREATE INDEX indx_esc_fstatusid ON egw_satuschange USING btree (fromstatus);



CREATE INDEX indx_esc_tostatusid ON egw_satuschange USING btree (tostatus);



CREATE INDEX indx_esc_vhid ON eg_surrendered_cheques USING btree (vhid);



CREATE INDEX indx_etamapping_finyearid ON egf_tax_account_mapping USING btree (financialyearid);



CREATE INDEX indx_etamapping_glcodeid ON egf_tax_account_mapping USING btree (glcodeid);



CREATE INDEX indx_etamapping_taxcodeid ON egf_tax_account_mapping USING btree (taxcodeid);



CREATE INDEX indx_eucm_counterid ON eg_usercounter_map USING btree (counterid);



CREATE INDEX indx_eucm_userid ON eg_usercounter_map USING btree (userid);



CREATE INDEX indx_eujl_bndrytypeid ON eg_user_jurlevel USING btree (id_bndry_type);



CREATE INDEX indx_eujl_userid ON eg_user_jurlevel USING btree (id_user);



CREATE INDEX indx_eujv_bndryid ON eg_user_jurvalues USING btree (id_bndry);



CREATE INDEX indx_eujv_jurlevelid ON eg_user_jurvalues USING btree (id_user_jurlevel);



CREATE INDEX indx_euom_categoryid ON eg_uom USING btree (uomcategoryid);



CREATE INDEX indx_eur_roleid ON eg_userrole USING btree (id_role);



CREATE INDEX indx_eur_userid ON eg_userrole USING btree (id_user);



CREATE INDEX indx_ewd_coaid ON egw_works_deductions USING btree (glcodeid);



CREATE INDEX indx_ewd_wdid ON egw_works_deductions USING btree (worksdetailid);



CREATE INDEX indx_ewm_estmtdby ON egw_works_mis USING btree (esimatepreparedby);



CREATE INDEX indx_ewm_fieldid ON egw_works_mis USING btree (fieldid);



CREATE INDEX indx_ewm_fundid ON egw_works_mis USING btree (fundid);



CREATE INDEX indx_ewm_tpid ON egw_works_mis USING btree (thirdpary);



CREATE INDEX indx_ewm_wardid ON egw_works_mis USING btree (wardid);



CREATE INDEX indx_ewm_wdid ON egw_works_mis USING btree (worksdetailid);



CREATE INDEX indx_firmactserv_firm_fk ON egp_firmactiveservcreg USING btree (portaluserid);



CREATE INDEX indx_firmactserv_servreg_fk ON egp_firmactiveservcreg USING btree (serviceregid);



CREATE INDEX indx_firmnotif_firm_fk ON egp_firmnotification USING btree (portaluserid);



CREATE INDEX indx_firmservicereg_action_fk ON egp_firmserviceregistry USING btree (actionid);



CREATE INDEX indx_firmservicereg_module_fk ON egp_firmserviceregistry USING btree (moduleid);



CREATE INDEX indx_firmservicereg_parent_fk ON egp_firmserviceregistry USING btree (parentid);



CREATE INDEX indx_firmservreq_firm_fk ON egp_firmservicereqregistry USING btree (portaluserid);



CREATE INDEX indx_firmservreq_servreg_fk ON egp_firmservicereqregistry USING btree (serviceregid);



CREATE INDEX indx_floor_dmdcalc ON egpt_floordemandcalc USING btree (id_dmdcalc);



CREATE INDEX indx_floor_idocc ON egpt_floor_detail USING btree (id_occpn_mstr);



CREATE INDEX indx_floor_idstruc ON egpt_floor_detail USING btree (id_struc_cl);



CREATE INDEX indx_fp_finyearid ON fiscalperiod USING btree (financialyearid);



CREATE INDEX indx_fund_purposeid ON fund USING btree (purpose_id);



CREATE INDEX indx_fundid ON scheme USING btree (fundid);



CREATE INDEX indx_gl_cdt ON generalledger USING btree (creditamount);



CREATE INDEX indx_gl_dbt ON generalledger USING btree (debitamount);



CREATE INDEX indx_gl_functionid ON generalledger USING btree (functionid);



CREATE INDEX indx_gl_glcode ON generalledger USING btree (glcode);



CREATE INDEX indx_gl_glcodeid ON generalledger USING btree (glcodeid);



CREATE INDEX indx_gld_acdtypeid ON generalledgerdetail USING btree (detailtypeid);



CREATE INDEX indx_gld_glid ON generalledgerdetail USING btree (generalledgerid);



CREATE INDEX indx_glid_vhid ON generalledger USING btree (voucherheaderid);



CREATE INDEX indx_header_employee ON egdiary_header USING btree (id_employee);



CREATE INDEX indx_hearing_lcid ON eglc_hearings USING btree (id_legalcase);



CREATE INDEX indx_idbasic ON egpt_property_status_values USING btree (id_basic_property);



CREATE INDEX indx_ih_in ON egf_instrumentheader USING btree (instrumentnumber);



CREATE INDEX indx_ih_payto ON egf_instrumentheader USING btree (payto);



CREATE INDEX indx_ih_status ON egf_instrumentheader USING btree (id_status);



CREATE INDEX indx_inspect_measurementdtls ON egbpa_inspect_measurementdtls USING btree (id, inspectiondtlsid, inspectionsourceid);



CREATE INDEX indx_inspection ON egbpa_inspection USING btree (id, registrationid);



CREATE INDEX indx_iv_ih ON egf_instrumentvoucher USING btree (instrumentheaderid);



CREATE INDEX indx_iv_vh ON egf_instrumentvoucher USING btree (voucherheaderid);



CREATE INDEX indx_judgment_lcid ON eglc_judgment USING btree (id_legalcase);



CREATE INDEX indx_lc_casetype ON eglc_legalcase USING btree (id_casetype);



CREATE INDEX indx_lc_court ON eglc_legalcase USING btree (id_court);



CREATE INDEX indx_lc_functionaryid ON eglc_legalcase USING btree (id_functionary);



CREATE INDEX indx_lc_petid ON eglc_legalcase USING btree (id_petitiontype);



CREATE INDEX indx_lc_resgovdept ON eglc_legalcase USING btree (id_respondentgovtdept);



CREATE INDEX indx_lcadv_advid ON eglc_legalcase_advocate USING btree (id_advocatemaster);



CREATE INDEX indx_lcadv_junior ON eglc_legalcase_advocate USING btree (id_juniorstage);



CREATE INDEX indx_lcadv_lcid ON eglc_legalcase_advocate USING btree (id_legalcase);



CREATE INDEX indx_lcadv_senadvid ON eglc_legalcase_advocate USING btree (id_senioradvocate);



CREATE INDEX indx_lcadv_senior ON eglc_legalcase_advocate USING btree (id_seniorstage);



CREATE INDEX indx_lcbill_advbill ON eglc_bill USING btree (id_advocate_bill);



CREATE INDEX indx_lcbill_stage ON eglc_bill USING btree (id_stage);



CREATE INDEX indx_lcdept_deptid ON eglc_legalcase_dept USING btree (id_dept);



CREATE INDEX indx_lcdept_lcid ON eglc_legalcase_dept USING btree (id_legalcase);



CREATE INDEX indx_lcdept_posid ON eglc_legalcase_dept USING btree (id_position);



CREATE INDEX indx_lcfee_lcadvid ON eglc_legalcasefee USING btree (id_lcadvocate);



CREATE INDEX indx_lcfees_lcid ON eglc_legalcasefee USING btree (id_legalcase);



CREATE INDEX indx_lcint_iotypeid ON eglc_lcinterimorder USING btree (id_intordertypeid);



CREATE INDEX indx_lcint_lcid ON eglc_lcinterimorder USING btree (id_legalcase);



CREATE INDEX indx_lettertoparty ON egbpa_lettertoparty USING btree (id, registrationid);



CREATE INDEX indx_lpchecklist ON egbpa_lpchecklist USING btree (id, lpid);



CREATE INDEX indx_mb_paidamount ON miscbilldetail USING btree (paidamount);



CREATE INDEX indx_mb_paidto ON miscbilldetail USING btree (paidto);



CREATE INDEX indx_mbd_pvhid ON miscbilldetail USING btree (payvhid);



CREATE INDEX indx_mbd_vhid ON miscbilldetail USING btree (billvhid);



CREATE UNIQUE INDEX indx_module_name ON eg_modules USING btree (name);



CREATE INDEX indx_mstr_schdl_eg_area_licfee ON egtl_mstr_area_licfee USING btree (id_schedule);



CREATE INDEX indx_mutation_idbasic ON egpt_property_mutation USING btree (id_basic_property);



CREATE INDEX indx_mutowner_idprop ON egpt_mutation_owner USING btree (id_prop_mutation);



CREATE INDEX indx_name_state_egtl_trade ON egtl_trade USING btree (state_id);



CREATE INDEX indx_name_transferegtl_trade ON egtl_trade USING btree (latest_name_transfer_id);



CREATE INDEX indx_ns_tender_response_id ON egw_negotiation_status USING btree (tender_response_id);



CREATE INDEX indx_obd_billid ON otherbilldetail USING btree (billid);



CREATE INDEX indx_obd_billvhid ON otherbilldetail USING btree (voucherheaderid);



CREATE INDEX indx_obd_payvhid ON otherbilldetail USING btree (payvhid);



CREATE INDEX indx_online_service ON egcl_onlinepayments USING btree (serviceid);



CREATE INDEX indx_online_status ON egcl_onlinepayments USING btree (statusid);



CREATE INDEX indx_op_collheaderid ON egcl_onlinepayments USING btree (receiptid);



CREATE INDEX indx_parent_activity_type ON egdiary_activity_type USING btree (parent);



CREATE INDEX indx_ph_accountid ON paymentheader USING btree (bankaccountnumberid);



CREATE INDEX indx_ph_vhid ON paymentheader USING btree (voucherheaderid);



CREATE INDEX indx_priority_compdtls ON eggr_complaintdetails USING btree (priority);



CREATE INDEX indx_prop_subgroup ON egpt_basic_property USING btree (id_property_subgroup);



CREATE INDEX indx_propdet_idmutation ON egpt_property_detail USING btree (id_mutation);



CREATE INDEX indx_propdet_idusg ON egpt_property_detail USING btree (id_usg_mstr);



CREATE INDEX indx_propmut_idmutation ON egpt_property_mutation USING btree (id_mutation);



CREATE UNIQUE INDEX indx_ptdemand ON egpt_ptdemand USING btree (id_demand, id_property);



CREATE INDEX indx_ptdemand_propdmd ON egpt_ptdemand USING btree (id_property, id_demand);



CREATE INDEX indx_pwr_lcid ON eglc_pwr USING btree (id_legalcase);



CREATE INDEX indx_r_typeid ON relation USING btree (relationtypeid);



CREATE INDEX indx_rcptrectify_idrr ON egpt_rcpt_rectify_details USING btree (id_rcpt_rectify);



CREATE INDEX indx_redressal_status_track ON eggr_status_tracker USING btree (redressalid);



CREATE INDEX indx_refid_basic ON egpt_property_status_values USING btree (ref_id_basic);



CREATE INDEX indx_regn_autodcr ON egbpa_regn_autodcr USING btree (id, registrationid, lpid, autodcr_num);



CREATE INDEX indx_regn_details ON egbpa_regn_details USING btree (id, registrationid);



CREATE INDEX indx_rejchecklist ON egbpa_rejection_checklist USING btree (id, rejectionid);



CREATE INDEX indx_scheduleeg_trade_sub_ctgr ON egtl_mstr_trade_sub_ctgr USING btree (id_schedule);



CREATE INDEX indx_schemeid ON sub_scheme USING btree (schemeid);



CREATE INDEX indx_sectorid ON scheme USING btree (sectorid);



CREATE INDEX indx_statusval_idsatus ON egpt_property_status_values USING btree (id_status);



CREATE INDEX indx_storesar_purchaseorder ON egstores_advancerequisition USING btree (purchaseorderid);



CREATE INDEX indx_survyractserv_servreg_fk ON egp_surveyoractiveservcreg USING btree (serviceregid);



CREATE INDEX indx_survyractserv_survyr_fk ON egp_surveyoractiveservcreg USING btree (portaluserid);



CREATE INDEX indx_survyrnotif_survyr_fk ON egp_surveyornotification USING btree (serviceregid);



CREATE INDEX indx_survyrservreg_action_fk ON egp_surveyorserviceregistry USING btree (actionid);



CREATE INDEX indx_survyrservreg_module_fk ON egp_surveyorserviceregistry USING btree (moduleid);



CREATE INDEX indx_survyrservreg_parent_fk ON egp_surveyorserviceregistry USING btree (parentid);



CREATE INDEX indx_survyrservreq_servreg_fk ON egp_surveyorservicereqregistry USING btree (serviceregid);



CREATE INDEX indx_survyrservreq_survyr_fk ON egp_surveyorservicereqregistry USING btree (portaluserid);



CREATE INDEX indx_te_abstractestimate_id ON egw_tender_estimate USING btree (abstractestimate_id);



CREATE INDEX indx_te_created_by ON egw_tender_estimate USING btree (created_by);



CREATE INDEX indx_te_modified_by ON egw_tender_estimate USING btree (modified_by);



CREATE INDEX indx_te_tender_header_id ON egw_tender_estimate USING btree (tender_header_id);



CREATE INDEX indx_th_created_by ON egw_tender_header USING btree (created_by);



CREATE INDEX indx_th_modified_by ON egw_tender_header USING btree (modified_by);



CREATE INDEX indx_tr_approved_by ON egw_negotiation_status USING btree (approved_by);



CREATE INDEX indx_tr_created_by ON egw_tender_response USING btree (created_by);



CREATE INDEX indx_tr_modified_by ON egw_tender_response USING btree (modified_by);



CREATE INDEX indx_tr_status_id ON egw_negotiation_status USING btree (status_id);



CREATE INDEX indx_tr_tender_estimate_id ON egw_tender_response USING btree (tender_estimate_id);



CREATE INDEX indx_tra_activity_id ON egw_tender_response_activity USING btree (activity_id);



CREATE INDEX indx_tra_created_by ON egw_tender_response_activity USING btree (created_by);



CREATE INDEX indx_tra_modified_by ON egw_tender_response_activity USING btree (modified_by);



CREATE INDEX indx_tra_tender_response_id ON egw_tender_response_activity USING btree (tender_response_id);



CREATE INDEX indx_trade_catgry_sub_ctgr ON egtl_mstr_trade_sub_ctgr USING btree (id_trade_category);



CREATE INDEX indx_trade_installed_motor ON egtl_installed_motor USING btree (trade_id);



CREATE INDEX indx_trade_license ON egtl_license USING btree (trade_id);



CREATE INDEX indx_trade_sub_ctgr_conserfee ON egtl_mstr_conserfee USING btree (id_trade_subctgr);



CREATE INDEX indx_trade_sub_ctgr_trade ON egtl_trade USING btree (trade_subctgr_id);



CREATE INDEX indx_transaction_date ON egf_instrumentheader USING btree (transactiondate);



CREATE INDEX indx_trc_contractor_id ON egw_tender_resp_contractors USING btree (contractor_id);



CREATE INDEX indx_trc_tender_response_id ON egw_tender_resp_contractors USING btree (tender_response_id);



CREATE INDEX indx_trq_contractor_id ON egw_tender_response_quotes USING btree (contractor_id);



CREATE INDEX indx_trq_tend_res_act_id ON egw_tender_response_quotes USING btree (tender_response_activity_id);



CREATE INDEX indx_ts_acdtypeid ON transactionsummary USING btree (accountdetailtypeid);



CREATE INDEX indx_ts_coaid ON transactionsummary USING btree (glcodeid);



CREATE INDEX indx_ts_finyear ON transactionsummary USING btree (financialyearid);



CREATE INDEX indx_ts_fsourseid ON transactionsummary USING btree (fundsourceid);



CREATE INDEX indx_ts_fundid ON transactionsummary USING btree (fundid);



CREATE INDEX indx_vak_caseid ON eglc_vakalat USING btree (id_case);



CREATE INDEX indx_vd_vhid ON voucherdetail USING btree (voucherheaderid);



CREATE INDEX indx_vh_fsourcesid ON voucherheader USING btree (fundsourceid);



CREATE INDEX indx_vh_fundid ON voucherheader USING btree (fundid);



CREATE INDEX indx_vmis_schemeid ON vouchermis USING btree (schemeid);



CREATE INDEX indx_vmis_segmentid ON vouchermis USING btree (segmentid);



CREATE INDEX indx_vmis_subschemeid ON vouchermis USING btree (subschemeid);



CREATE INDEX indx_vmis_subsegmentid ON vouchermis USING btree (sub_segmentid);



CREATE INDEX indx_vmis_vhid ON vouchermis USING btree (voucherheaderid);



CREATE INDEX indx_wffloor_idocc ON egpt_wfmodfloor_detail USING btree (id_occpn_mstr);



CREATE INDEX indx_wffloor_idstruc ON egpt_wfmodfloor_detail USING btree (id_struc_cl);



CREATE INDEX indx_wffloor_idusg ON egpt_wfmodfloor_detail USING btree (id_usg_mstr);



CREATE INDEX indx_wfmodifyprop_stateid ON egpt_wfmodifyprop USING btree (state_id);



CREATE INDEX indx_wfmut_idmut ON egpt_wfmutation_owner USING btree (id_wfmutation);



CREATE INDEX indx_wfmut_idmutation ON egpt_wfmutation USING btree (id_mutation);



CREATE INDEX indx_wfmut_ownerid ON egpt_wfmutation_owner USING btree (ownerid);



CREATE INDEX indx_wfmutation_stateid ON egpt_wfmutation USING btree (state_id);



CREATE INDEX indx_wfprop_idprop ON egpt_wfproperty_owner USING btree (id_wfproperty);



CREATE INDEX indx_wfprop_ownerid ON egpt_wfproperty_owner USING btree (ownerid);



CREATE INDEX indx_wfprop_stateid ON egpt_wfproperty USING btree (state_id);



CREATE INDEX inqx_attvalue_domtxnid ON eg_attributevalues USING btree (domaintxnid);



CREATE INDEX inqx_dmdcalc_iddmd ON egpt_demandcalculations USING btree (id_demand);



CREATE INDEX inqx_dmddet_dmd_dmdrsnid ON eg_demand_details USING btree (id_demand, id_demand_reason);



CREATE INDEX inqx_propdet_propid ON egpt_property_detail USING btree (id_property);



CREATE INDEX ix_eg_adm_bndry ON eg_adm_bndry USING btree (id_bndry_master, id_parent);



CREATE INDEX jhist_department_ix ON job_history USING btree (department_id);



CREATE INDEX jhist_employee_ix ON job_history USING btree (employee_id);



CREATE INDEX jhist_job_ix ON job_history USING btree (job_id);



CREATE INDEX jms_messages_destination ON jms_messages USING btree (destination);



CREATE INDEX jms_messages_txop_txid ON jms_messages USING btree (txop, txid);



CREATE INDEX loc_city_ix ON locations USING btree (city);



CREATE INDEX loc_country_ix ON locations USING btree (country_id);



CREATE INDEX loc_state_province_ix ON locations USING btree (state_province);



CREATE UNIQUE INDEX order_type_unq ON egpt_mutation_master USING btree (order_id, type);



CREATE UNIQUE INDEX pk_code_store ON eg_bins USING btree (bin_code, storeid);



CREATE UNIQUE INDEX pk_egf_asset_history ON egf_asset_history USING btree (id);



CREATE UNIQUE INDEX pk_egpt_constr_type ON egpt_constr_type USING btree (id_constr_type);



CREATE UNIQUE INDEX pk_egpt_ptdemand_arv ON egpt_ptdemand_arv USING btree (id);



CREATE UNIQUE INDEX pk_user_jurvalues ON eg_user_jurvalues USING btree (id_user_jurlevel, id_bndry);



CREATE UNIQUE INDEX pk_userrole ON eg_userrole USING btree (id_role, id_user);



CREATE INDEX prop_inst ON egpt_property USING btree (id_basic_property, id_installment, is_history, id_source);



CREATE UNIQUE INDEX property_id_unique ON egpt_basic_property USING btree (propertyid);



CREATE UNIQUE INDEX reg_num_unique ON egpt_basic_property USING btree (reg_num);



CREATE UNIQUE INDEX rel_code ON relation USING btree (code);



CREATE UNIQUE INDEX relationtype_pk ON relationtype USING btree (id);



CREATE UNIQUE INDEX relty_code ON relationtype USING btree (code);



CREATE UNIQUE INDEX relty_name ON relationtype USING btree (name);



CREATE UNIQUE INDEX schedulemap_pk ON schedulemap USING btree (id);



CREATE UNIQUE INDEX schmap_code ON schedulemap USING btree (glcode);



CREATE UNIQUE INDEX unique_constr_name ON egpt_constr_type USING btree (constr_name);



CREATE UNIQUE INDEX unique_key_src_name ON egpt_src_of_info USING btree (source_name);



CREATE UNIQUE INDEX unique_key_status_name ON egpt_status USING btree (status_name);



CREATE UNIQUE INDEX unique_key_usagename ON egpt_property_usage_master USING btree (usg_name);



CREATE UNIQUE INDEX unq_egbpa_feemstr_code ON egbpa_mstr_bpafee USING btree (fee_code);



CREATE UNIQUE INDEX unq_egbpa_feemstr_descsertype ON egbpa_mstr_bpafee USING btree (servicetypeid, fee_description);



CREATE UNIQUE INDEX unq_egbpa_feemstr_desctype ON egbpa_mstr_bpafee USING btree (servicetypeid, fee_description, fee_type);



CREATE UNIQUE INDEX unq_egbpaextnd_feemstr_code ON egbpaextnd_mstr_bpafee USING btree (fee_code);



CREATE UNIQUE INDEX unq_egbpaextnd_fmstr_dsertp ON egbpaextnd_mstr_bpafee USING btree (servicetypeid, fee_description);



CREATE UNIQUE INDEX unq_egbpaextnd_fmstr_dtype ON egbpaextnd_mstr_bpafee USING btree (servicetypeid, fee_description, fee_type);



CREATE UNIQUE INDEX unq_egpt_cat_catname ON egpt_category USING btree (category_name);



CREATE UNIQUE INDEX unq_feelinemstr ON egbpaextnd_mstr_bpafeedetail USING btree (feeid, from_areasqmt, to_areasqmt, subtype, landusezone, floornumber, usagetypeid, startdate, additionaltype);



CREATE UNIQUE INDEX uqindx_citizen_portaluser_fk ON egp_citizen USING btree (id);



CREATE UNIQUE INDEX uqindx_firm_portaluser_fk ON egp_firm USING btree (id);



ALTER TABLE ONLY egcl_accountpayeedetails
    ADD CONSTRAINT adkid_apd_fk FOREIGN KEY (id_accountdetailskey) REFERENCES accountdetailkey(id);



ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT adt_im_pk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY egcl_accountpayeedetails
    ADD CONSTRAINT adtid_apd_fk FOREIGN KEY (id_accountdetailstype) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY eglc_advocatedept_mapping
    ADD CONSTRAINT advid_advdeptmapp_pk FOREIGN KEY (id_advocate) REFERENCES eglc_advocate_master(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT agreementfk_uom FOREIGN KEY (uomid) REFERENCES eg_uom(id);



ALTER TABLE ONLY egeis_leave_approval
    ADD CONSTRAINT app_fk FOREIGN KEY (application_id) REFERENCES egeis_leave_application(id);



ALTER TABLE ONLY egeis_leave_approval
    ADD CONSTRAINT approved_by_fk FOREIGN KEY (approved_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT ass_id FOREIGN KEY (assignment_id) REFERENCES eg_emp_assignment(id);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT asset_emp_preparedby_fk FOREIGN KEY (preparedby) REFERENCES eg_employee(id);



ALTER TABLE ONLY eg_asset_sale
    ADD CONSTRAINT asset_id_fk FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY eg_depreciationmetadata
    ADD CONSTRAINT assetcategid FOREIGN KEY (assetcategoryid) REFERENCES eg_assetcategory(id);



ALTER TABLE ONLY eg_revaluations
    ADD CONSTRAINT assetid FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egeis_compoff
    ADD CONSTRAINT attid FOREIGN KEY (att_id) REFERENCES egeis_attendence(id);



ALTER TABLE ONLY egpt_propertyid
    ADD CONSTRAINT back_bndry_street_fk FOREIGN KEY (back_bndry_street) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT baid_im_pk FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egcl_collection_mode_details
    ADD CONSTRAINT bank_fk FOREIGN KEY (bank_id) REFERENCES bank(id);



ALTER TABLE ONLY egeis_bank_det
    ADD CONSTRAINT bank_id FOREIGN KEY (bank) REFERENCES bank(id);



ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT bankid_im_pk FOREIGN KEY (bankid) REFERENCES bank(id);



ALTER TABLE ONLY eg_bins
    ADD CONSTRAINT binstatus_fk FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY codemapping
    ADD CONSTRAINT bndry_fk FOREIGN KEY (eg_boundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_boundary
    ADD CONSTRAINT bndry_parent FOREIGN KEY (parent) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egpt_propertyid
    ADD CONSTRAINT bndry_propertyidbn_fk FOREIGN KEY (block_adm_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egpt_propertyid
    ADD CONSTRAINT bndry_propertyidsn_fk FOREIGN KEY (street_adm_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egpt_propertyid
    ADD CONSTRAINT bndry_propertyidwn_fk FOREIGN KEY (ward_adm_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egpt_propertyid
    ADD CONSTRAINT bndry_street_dmdcalc_fk FOREIGN KEY (bndry_street_dmdcalc) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_boundary
    ADD CONSTRAINT bndry_type_fk FOREIGN KEY (id_bndry_type) REFERENCES eg_boundary_type(id_bndry_type);



ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT bndry_type_heirarchy_fk FOREIGN KEY (id_heirarchy_type) REFERENCES eg_heirarchy_type(id_heirarchy_type);



ALTER TABLE ONLY eg_boundary_type
    ADD CONSTRAINT bndry_type_parent FOREIGN KEY (parent) REFERENCES eg_boundary_type(id_bndry_type);



ALTER TABLE ONLY eggis_bndry_dim
    ADD CONSTRAINT bndryid_fk FOREIGN KEY (bndryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcc_conservancy_application
    ADD CONSTRAINT bp_ca_fk FOREIGN KEY (id_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egeis_holidays
    ADD CONSTRAINT cal_fk_id FOREIGN KEY (cal_year_id) REFERENCES calendaryear(id);



ALTER TABLE ONLY eglc_vakalat
    ADD CONSTRAINT case_id_vakalat_fk FOREIGN KEY (id_case) REFERENCES eglc_legalcase(id);



ALTER TABLE ONLY egcl_accountpayeedetails
    ADD CONSTRAINT cdid_apd_fk FOREIGN KEY (id_collectiondetails) REFERENCES egcl_collectiondetails(id);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT ch_cm_pk FOREIGN KEY (id_collectionheader) REFERENCES egcl_collectionheader(id);



ALTER TABLE ONLY egcl_challanheader
    ADD CONSTRAINT ch_receiptid_fk FOREIGN KEY (receiptid) REFERENCES egcl_collectionheader(id);



ALTER TABLE ONLY egcl_challanheader
    ADD CONSTRAINT ch_statusid_fk FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egcl_challanheader
    ADD CONSTRAINT ch_voucherheader_fk FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egcl_challanheader
    ADD CONSTRAINT ch_wfstateid_fk FOREIGN KEY (wfstateid) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT chequedept_cheque_fk FOREIGN KEY (accountchequeid) REFERENCES egf_account_cheques(id);



ALTER TABLE ONLY cheque_dept_mapping
    ADD CONSTRAINT chequedept_dept_fk FOREIGN KEY (allotedto) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egcl_collectiondetails
    ADD CONSTRAINT chid_cd_pk FOREIGN KEY (id_collectionheader) REFERENCES egcl_collectionheader(id);



ALTER TABLE ONLY egcl_collectionvoucher
    ADD CONSTRAINT chid_cv_pk FOREIGN KEY (collectionheaderid) REFERENCES egcl_collectionheader(id);



ALTER TABLE ONLY egcl_collectiondetails
    ADD CONSTRAINT coaid_cd_pk FOREIGN KEY (id_accounthead) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egcl_collectioninstrument
    ADD CONSTRAINT collid_ci_pk FOREIGN KEY (collectionheaderid) REFERENCES egcl_collectionheader(id);



ALTER TABLE ONLY egstores_txntypes
    ADD CONSTRAINT cons_fk_accpur FOREIGN KEY (acccodepurposeid) REFERENCES egf_accountcode_purpose(id);



ALTER TABLE ONLY egw_contractor_detail
    ADD CONSTRAINT contdtl_contractor FOREIGN KEY (contractor_id) REFERENCES egw_contractor(id);



ALTER TABLE ONLY egw_contractor_detail
    ADD CONSTRAINT contdtl_dpt FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_contractor_detail
    ADD CONSTRAINT contdtl_grade FOREIGN KEY (contractor_grade_id) REFERENCES egw_contractor_grade(id);



ALTER TABLE ONLY egw_contractor_detail
    ADD CONSTRAINT contdtl_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_contractor
    ADD CONSTRAINT contractor_bank FOREIGN KEY (bank_id) REFERENCES bank(id);



ALTER TABLE ONLY eglc_court_master
    ADD CONSTRAINT court_type_fk FOREIGN KEY (id_courttype) REFERENCES eglc_courttype_master(id);



ALTER TABLE ONLY egcl_collectionheader
    ADD CONSTRAINT cpd_ch_pk FOREIGN KEY (id_collectionpayeedetails) REFERENCES egcl_collectionpayeedetails(id);



ALTER TABLE ONLY egeis_leave_application
    ADD CONSTRAINT created_by_fk FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egeis_leave_application
    ADD CONSTRAINT de_fk FOREIGN KEY (desig_id) REFERENCES eg_designation(designationid);



ALTER TABLE ONLY egpt_demandcalculations
    ADD CONSTRAINT demandcal_id_demand_fk FOREIGN KEY (id_demand) REFERENCES eg_demand(id);



ALTER TABLE ONLY eg_depreciation
    ADD CONSTRAINT dep_asset_fk FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT depid_metadata FOREIGN KEY (depid) REFERENCES eg_depreciationmetadata(id_depreciationmetadata);



ALTER TABLE ONLY eglc_advocatedept_mapping
    ADD CONSTRAINT dept_advdeptmapp_pk FOREIGN KEY (id_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_employee_dept
    ADD CONSTRAINT dept_ids FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eglc_legalcase_dept
    ADD CONSTRAINT deptid_lcdept_fk FOREIGN KEY (id_dept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egf_deptissue
    ADD CONSTRAINT deptissuefk_dept FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egf_deptissue
    ADD CONSTRAINT deptissuefk_mrinheader FOREIGN KEY (mrinheaderid) REFERENCES egf_mrinheader(id);



ALTER TABLE ONLY eg_emp_assignment
    ADD CONSTRAINT des_fk FOREIGN KEY (designationid) REFERENCES eg_designation(designationid);



ALTER TABLE ONLY egeis_leave_mstr
    ADD CONSTRAINT desig_fk_id FOREIGN KEY (desig_id) REFERENCES eg_designation(designationid);



ALTER TABLE ONLY egeis_nominee_master
    ADD CONSTRAINT disbursement_type_fk FOREIGN KEY (disbursement_type) REFERENCES eg_disbursement_mode(id);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT eb_cm_pk FOREIGN KEY (id_boundary) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT ed_cm_pk FOREIGN KEY (id_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egpt_basic_property
    ADD CONSTRAINT eg_add_basic_prop_fk1 FOREIGN KEY (addressid) REFERENCES eg_address(addressid);



ALTER TABLE ONLY eggr_complaintdetails
    ADD CONSTRAINT eg_bndry_name FOREIGN KEY (bndry) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egpt_property_tenants
    ADD CONSTRAINT eg_cit_prop_ten_fk1 FOREIGN KEY (citizenid) REFERENCES eg_citizen(citizenid);



ALTER TABLE ONLY eg_router
    ADD CONSTRAINT eg_city_router FOREIGN KEY (bndryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcl_collstg_achead_amount
    ADD CONSTRAINT eg_collstg_acchead_amou_fk1 FOREIGN KEY (id_collstg_rcpt) REFERENCES egcl_collstg_receipt(id);



ALTER TABLE ONLY egcl_collstg_instrument
    ADD CONSTRAINT eg_collstg_instrument_fk1 FOREIGN KEY (id_collstg_rcpt) REFERENCES egcl_collstg_receipt(id);



ALTER TABLE ONLY eg_default
    ADD CONSTRAINT eg_defaultid FOREIGN KEY (defaultid) REFERENCES eg_master_default(defaultid);



ALTER TABLE ONLY egdm_collected_receipts
    ADD CONSTRAINT eg_dmd_detail_id FOREIGN KEY (id_demand_detail) REFERENCES eg_demand_details(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT eg_fpen_bandet_id FOREIGN KEY (pen_bank_details_id) REFERENCES egpen_pensioner_bank_details(id);



ALTER TABLE ONLY eg_bill_details
    ADD CONSTRAINT eg_installment_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT eg_pen_bandet_id FOREIGN KEY (pen_bank_details_id) REFERENCES egpen_pensioner_bank_details(id);



ALTER TABLE ONLY eg_default
    ADD CONSTRAINT eg_roleid FOREIGN KEY (roleid) REFERENCES eg_roles(id_role);



ALTER TABLE ONLY eg_service_dept_mapping
    ADD CONSTRAINT eg_ser_dept_map_srvcdtls_fk FOREIGN KEY (id_servicedetails) REFERENCES eg_servicedetails(id);



ALTER TABLE ONLY eg_service_dept_mapping
    ADD CONSTRAINT eg_service_deptmapping_dept_fk FOREIGN KEY (id_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT eg_srvcacc_srvdtils_fk FOREIGN KEY (id_servicedetails) REFERENCES eg_servicedetails(id);



ALTER TABLE ONLY eg_service_accountdetails
    ADD CONSTRAINT eg_srvcaccdtls_coa_fk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_service_subledgerinfo
    ADD CONSTRAINT eg_subdtls_accdtltyp_fk FOREIGN KEY (id_accountdetailtype) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY eg_service_subledgerinfo
    ADD CONSTRAINT eg_subledgerdetails_srvcacc_fk FOREIGN KEY (id_serviceaccountdetail) REFERENCES eg_service_accountdetails(id);



ALTER TABLE ONLY egasset_voucherdetail
    ADD CONSTRAINT egasset_vd_coa_fk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egasset_subledger
    ADD CONSTRAINT egast_sl_egast_adt_fk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY egasset_subledger
    ADD CONSTRAINT egast_sl_egast_vd_fk FOREIGN KEY (voucherdetailid) REFERENCES egasset_voucherdetail(id);



ALTER TABLE ONLY egasset_voucherdetail
    ADD CONSTRAINT egast_vd_egast_vh_fk FOREIGN KEY (assetvoucherid) REFERENCES egasset_voucherheader(id);



ALTER TABLE ONLY egcl_bank_remittance
    ADD CONSTRAINT egcl_bankrmtnc_bank_fk FOREIGN KEY (id_depositedinbank) REFERENCES bank(id);



ALTER TABLE ONLY egcl_bank_remittance
    ADD CONSTRAINT egcl_bankrmtnc_bankaccount_fk FOREIGN KEY (id_bankaccounttoremit) REFERENCES bankaccount(id);



ALTER TABLE ONLY egcl_bank_remittance
    ADD CONSTRAINT egcl_bankrmtnc_service_fk FOREIGN KEY (id_service) REFERENCES eg_servicedetails(id);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT egcl_collectionmis_bank_fk FOREIGN KEY (id_depositedinbank) REFERENCES bank(id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_cruse_fk FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_insthead_fk FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_mbuse_fk FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_ovh_fk FOREIGN KEY (originalvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_rvh_fk FOREIGN KEY (reversalvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_st_fk FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishchq_state_fk FOREIGN KEY (stateid) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqde_head_fk FOREIGN KEY (headerid) REFERENCES egf_dishonorcheque(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_acdet_fk FOREIGN KEY (detailtype) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_acdetky_fk FOREIGN KEY (detailkey) REFERENCES accountdetailkey(id);



ALTER TABLE ONLY egf_dishonorcheque_detail
    ADD CONSTRAINT egf_dishchqdet_glcode_fk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_dishonorcheque
    ADD CONSTRAINT egf_dishcq_bcvh_fk FOREIGN KEY (bankchargesvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY eggr_status_tracker
    ADD CONSTRAINT eggr__to_id FOREIGN KEY (touserid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_user
    ADD CONSTRAINT eggr_bndryid FOREIGN KEY (id_top_bndry) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eggr_forward_tracker
    ADD CONSTRAINT eggr_from FOREIGN KEY (fromofficerid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eggr_status_tracker
    ADD CONSTRAINT eggr_from_id FOREIGN KEY (fromuserid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eggr_complaintdetails
    ADD CONSTRAINT eggr_priorityid FOREIGN KEY (priority) REFERENCES eggr_priority(priorityid);



ALTER TABLE ONLY eggr_redressaldetails
    ADD CONSTRAINT eggr_redressal FOREIGN KEY (redressalofficerid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eggr_forward_tracker
    ADD CONSTRAINT eggr_touser FOREIGN KEY (toofficerid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eggr_communication
    ADD CONSTRAINT eggr_touserid FOREIGN KEY (touserid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglc_advocate_bill
    ADD CONSTRAINT eglc_advocate_id_fk FOREIGN KEY (id_advocate) REFERENCES eglc_advocate_master(id);



ALTER TABLE ONLY eglc_advocate_bill
    ADD CONSTRAINT eglc_advocate_state_id_fk FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY eglc_bill
    ADD CONSTRAINT eglc_advocatebill_fk FOREIGN KEY (id_advocate_bill) REFERENCES eglc_advocate_bill(id);



ALTER TABLE ONLY eglc_bill
    ADD CONSTRAINT eglc_bill_accounthead_fk FOREIGN KEY (id_account_head) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eglc_bill
    ADD CONSTRAINT eglc_bill_idlegalcase_fk FOREIGN KEY (id_legalcase) REFERENCES eglc_legalcase(id);



ALTER TABLE ONLY eglc_bill
    ADD CONSTRAINT eglc_case_stage_fk FOREIGN KEY (id_stage) REFERENCES eglc_case_stage(id);



ALTER TABLE ONLY egpen_arrears_detail
    ADD CONSTRAINT egpen_arrdet_arrhead FOREIGN KEY (arrears_header_id) REFERENCES egpen_arrears_header(id);



ALTER TABLE ONLY egpen_arrears_detail
    ADD CONSTRAINT egpen_arrdet_fph FOREIGN KEY (id_family_pensioner) REFERENCES egpen_family_pensioner_header(id);



ALTER TABLE ONLY egpen_arrears_detail
    ADD CONSTRAINT egpen_arrdet_penhead FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_arrears_detail
    ADD CONSTRAINT egpen_arrdet_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_arrears_header
    ADD CONSTRAINT egpen_arrh_cbusr FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_arrears_header
    ADD CONSTRAINT egpen_arrh_mbusr FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_arrears_header
    ADD CONSTRAINT egpen_arrh_phrates FOREIGN KEY (payheadrates_id) REFERENCES egpen_payhead_rates(id);



ALTER TABLE ONLY egpen_batchgendetails
    ADD CONSTRAINT egpen_fk_finyear_id FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egpen_batchgendetails
    ADD CONSTRAINT egpen_fk_id_dept FOREIGN KEY (id_dept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egpen_pension_banks
    ADD CONSTRAINT egpen_penbanks_fk FOREIGN KEY (id_bank) REFERENCES bank(id);



ALTER TABLE ONLY egpen_pension_branches
    ADD CONSTRAINT egpen_penbranches_fk FOREIGN KEY (id_branch) REFERENCES bankbranch(id);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_pharmstr_yr FOREIGN KEY (rule_implyear) REFERENCES financialyear(id);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_phrates_cbusr FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_phrates_mbusr FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_phrates_phid FOREIGN KEY (payhead_id) REFERENCES egpen_payheads(id);



ALTER TABLE ONLY egpen_payhead_rates
    ADD CONSTRAINT egpen_phrates_phref FOREIGN KEY (refpayhead_id) REFERENCES egpen_payheads(id);



ALTER TABLE ONLY egpt_mstr_property_subgroup
    ADD CONSTRAINT egpt_mstr_prop_subgroup_fk FOREIGN KEY (id_mstr_property_group) REFERENCES egpt_mstr_property_group(id);



ALTER TABLE ONLY egpt_property_detail
    ADD CONSTRAINT egpt_prop_usg_prop_det_fk1 FOREIGN KEY (id_usg_mstr) REFERENCES egpt_property_usage_master(id_usg_mstr);



ALTER TABLE ONLY egpt_wfmodfloor_detail
    ADD CONSTRAINT egpt_wfmod_str_cl_fk1 FOREIGN KEY (id_struc_cl) REFERENCES egpt_struc_cl(id_struc_cl);



ALTER TABLE ONLY egptwf_floor_detail
    ADD CONSTRAINT egptwf_property_detail_fk FOREIGN KEY (id_property_detail) REFERENCES egpt_wfproperty(id) ON DELETE CASCADE;



ALTER TABLE ONLY eg_default
    ADD CONSTRAINT egrr_bndry FOREIGN KEY (toplevelbndryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eggr_communication
    ADD CONSTRAINT egrr_fromuserid FOREIGN KEY (fromuserid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egtl_history_trade_area
    ADD CONSTRAINT egtl_trade_id_fk1 FOREIGN KEY (trade_id) REFERENCES egtl_trade(id);



ALTER TABLE ONLY egw_assetforbill
    ADD CONSTRAINT egw_assetforbill_ch_fk1 FOREIGN KEY (id) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egw_assetforbill
    ADD CONSTRAINT egw_assetforbill_eg_asset_fk1 FOREIGN KEY (id_asset) REFERENCES eg_asset(id);



ALTER TABLE ONLY egw_assetforbill
    ADD CONSTRAINT egw_assetforbill_eg_bill_fk1 FOREIGN KEY (id_bill) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT egw_coa_fk1 FOREIGN KEY (coa_id) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egw_deductiontype_bill
    ADD CONSTRAINT egw_dedforbill_eg_bill_fk1 FOREIGN KEY (id_bill) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egw_deductiontype_bill
    ADD CONSTRAINT egw_dedforbill_wo_fk1 FOREIGN KEY (id_workorder) REFERENCES egw_work_order(id);



ALTER TABLE ONLY egw_deductiontype_bill
    ADD CONSTRAINT egw_deductiontype_bill_ch_fk1 FOREIGN KEY (id_coa) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT egw_despositcode__fk1 FOREIGN KEY (despositcode_id) REFERENCES egw_depositcode(id);



ALTER TABLE ONLY egw_estimate_reappr_details
    ADD CONSTRAINT egw_estimate_reappr_detail_fk FOREIGN KEY (estimate_reappr) REFERENCES egw_estimate_reappropriation(id);



ALTER TABLE ONLY egw_milestone_temp_activity
    ADD CONSTRAINT egw_milestone_temp_activity_fk FOREIGN KEY (templateid) REFERENCES egw_milestone_template(id);



ALTER TABLE ONLY egw_works_status
    ADD CONSTRAINT egw_status_fk1 FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_track_milestone_activity
    ADD CONSTRAINT egw_tmilestone_activity_fk FOREIGN KEY (trackmilestone_id) REFERENCES egw_track_milestone(id);



ALTER TABLE ONLY egw_track_milestone_activity
    ADD CONSTRAINT egw_tmilestone_mactivity_fk FOREIGN KEY (milestone_activity_id) REFERENCES egw_milestone_activity(id);



ALTER TABLE ONLY egw_work_order_activity
    ADD CONSTRAINT egw_work_order_activity_e_fk1 FOREIGN KEY (workorder_estimate_id) REFERENCES egw_workorder_estimate(id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT egw_workorder_est_fk1 FOREIGN KEY (workorder_est_id) REFERENCES egw_workorder_estimate(id);



ALTER TABLE ONLY egw_workorder_estimate
    ADD CONSTRAINT egw_workorder_estimate_eg_fk1 FOREIGN KEY (workorder_id) REFERENCES egw_work_order(id);



ALTER TABLE ONLY egw_workorder_estimate
    ADD CONSTRAINT egw_workorder_estimate_eg_fk2 FOREIGN KEY (estimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_tender_estimate
    ADD CONSTRAINT egw_works_package_fk1 FOREIGN KEY (works_package_id) REFERENCES egw_workspackage(id);



ALTER TABLE ONLY egw_workspackage
    ADD CONSTRAINT egw_workspackage_eg_dept_fk1 FOREIGN KEY (department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_workspackage
    ADD CONSTRAINT egw_workspackage_eg_emp_fk1 FOREIGN KEY (preparedby) REFERENCES eg_employee(id);



ALTER TABLE ONLY egw_workspackage_details
    ADD CONSTRAINT egw_workspackage_fk1 FOREIGN KEY (id_workspackage) REFERENCES egw_workspackage(id);



ALTER TABLE ONLY egw_workspackage_details
    ADD CONSTRAINT egw_wp_details_est_fk1 FOREIGN KEY (id_estimate) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egcl_collectionheader
    ADD CONSTRAINT el_cpd_pk FOREIGN KEY (id_location) REFERENCES eg_location(id);



ALTER TABLE ONLY egeis_attendence
    ADD CONSTRAINT emp_fk FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egeis_leave_opening_balance
    ADD CONSTRAINT emp_fk_id FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egeis_leave_application
    ADD CONSTRAINT emp_id_fk FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY eglc_employeehearing
    ADD CONSTRAINT empid_lgdepemponhearing_fk FOREIGN KEY (id_employee) REFERENCES eg_employee(id);



ALTER TABLE ONLY egeis_dept_tests
    ADD CONSTRAINT empp_fk_id FOREIGN KEY (id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT f_cm_pk FOREIGN KEY (id_functionary) REFERENCES functionary(id);



ALTER TABLE ONLY egcl_collectionmis
    ADD CONSTRAINT fd_cm_pk FOREIGN KEY (id_fund) REFERENCES fund(id);



ALTER TABLE ONLY egcl_collectiondetails
    ADD CONSTRAINT fid_cd_pk FOREIGN KEY (id_function) REFERENCES function(id);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fiescheduleid_shedule_map_fk FOREIGN KEY (fiescheduleid) REFERENCES schedulemapping(id);



ALTER TABLE ONLY egeis_leave_opening_balance
    ADD CONSTRAINT fin_fk FOREIGN KEY (financialyear) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_depreciationmetadata
    ADD CONSTRAINT fin_pk FOREIGN KEY (finyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fin_source_bankaccount_fk FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fin_source_sub_scheme_fk FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fin_src_crtd_fk FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egeis_holidays
    ADD CONSTRAINT fin_year FOREIGN KEY (fin_year_id) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_depreciation
    ADD CONSTRAINT finyearid FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_roleaction_map
    ADD CONSTRAINT fk1 FOREIGN KEY (actionid) REFERENCES eg_action(id);



ALTER TABLE ONLY egw_financingsource
    ADD CONSTRAINT fk1_fundsource FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY egw_overhead_rate
    ADD CONSTRAINT fk1_overheadrate FOREIGN KEY (overhead_id) REFERENCES egw_overhead(id);



ALTER TABLE ONLY eg_roleaction_map
    ADD CONSTRAINT fk2 FOREIGN KEY (roleid) REFERENCES eg_roles(id_role);



ALTER TABLE ONLY egw_financingsource
    ADD CONSTRAINT fk2_financingsource FOREIGN KEY (financialdetailid) REFERENCES egw_financialdetail(id);



ALTER TABLE ONLY egw_activity
    ADD CONSTRAINT fk349bac1956c82594 FOREIGN KEY (scheduleofrate_id) REFERENCES egw_scheduleofrate(id);



ALTER TABLE ONLY egw_activity
    ADD CONSTRAINT fk349bac1983db03a3 FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_activity
    ADD CONSTRAINT fk349bac19f3af9e43 FOREIGN KEY (nonsor_id) REFERENCES egw_nonsor(id);



ALTER TABLE ONLY egw_nonsor
    ADD CONSTRAINT fk6f547b335ffbc3b9 FOREIGN KEY (uom) REFERENCES eg_uom(id);



ALTER TABLE ONLY eg_abstract_estimate_status
    ADD CONSTRAINT fk7b04e5c983db03a3 FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_multiyearestimate
    ADD CONSTRAINT fk7cc1ca3483db03a3 FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_multiyearestimate
    ADD CONSTRAINT fk7cc1ca34af30bcd8 FOREIGN KEY (financialyear_id) REFERENCES financialyear(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_abs_est_old_user_dept FOREIGN KEY (old_user_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_ac_ac FOREIGN KEY (parentid) REFERENCES eg_assetcategory(id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_ac_usr1 FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_ac_usr2 FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_acc_ac1 FOREIGN KEY (assetcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_acc_ac2 FOREIGN KEY (accdepcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_acc_ac4 FOREIGN KEY (depexpcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_acc_ac5 FOREIGN KEY (revcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egfms_accident_details
    ADD CONSTRAINT fk_accident_dept FOREIGN KEY (operated_deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egfms_accident_details
    ADD CONSTRAINT fk_accident_emp FOREIGN KEY (driverid) REFERENCES eg_employee(id);



ALTER TABLE ONLY egfms_accident_details
    ADD CONSTRAINT fk_accident_modified FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egfms_accident_details
    ADD CONSTRAINT fk_accident_veh FOREIGN KEY (vehicleid) REFERENCES egfms_vehicle_header(id);



ALTER TABLE ONLY egw_activity
    ADD CONSTRAINT fk_act_uom FOREIGN KEY (uom_id) REFERENCES eg_uom(id);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT fk_action_actndel FOREIGN KEY (actiondelegation_) REFERENCES jbpm_delegation(id_);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT fk_action_event FOREIGN KEY (event_) REFERENCES jbpm_event(id_);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT fk_action_expthdl FOREIGN KEY (exceptionhandler_) REFERENCES jbpm_exceptionhandler(id_);



ALTER TABLE ONLY menutree
    ADD CONSTRAINT fk_action_mt FOREIGN KEY (actionid) REFERENCES eg_action(id);



ALTER TABLE ONLY eg_event_action_notification
    ADD CONSTRAINT fk_action_notification FOREIGN KEY (actionid) REFERENCES eg_event_actions(pkid);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT fk_action_procdef FOREIGN KEY (processdefinition_) REFERENCES jbpm_processdefinition(id_);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT fk_action_refact FOREIGN KEY (referencedaction_) REFERENCES jbpm_action(id_);



ALTER TABLE ONLY egw_activity
    ADD CONSTRAINT fk_activity_parent_id FOREIGN KEY (parentid) REFERENCES egw_activity(id);



ALTER TABLE ONLY egeis_person_address
    ADD CONSTRAINT fk_add_id FOREIGN KEY (id_address) REFERENCES eg_address(addressid);



ALTER TABLE ONLY egpt_address
    ADD CONSTRAINT fk_addressid FOREIGN KEY (id_address) REFERENCES eg_address(addressid);



ALTER TABLE ONLY eg_address
    ADD CONSTRAINT fk_addtype_id FOREIGN KEY (id_addresstypemaster) REFERENCES eg_address_type_master(id_address_type);



ALTER TABLE ONLY eg_adm_bndry
    ADD CONSTRAINT fk_adm_bdr_adm_bdr_mst FOREIGN KEY (id_bndry_master) REFERENCES eg_adm_bndry_master(id_bndry_master);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereq_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereqcreator_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereqmodifier_user FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_advancerequisition
    ADD CONSTRAINT fk_advancereqstatus_egwstatus FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY eglc_advcourttype_mapping
    ADD CONSTRAINT fk_advocateid FOREIGN KEY (id_advocate) REFERENCES eglc_advocate_master(id);



ALTER TABLE ONLY eglc_legalcase_advocate
    ADD CONSTRAINT fk_advocateid_pk FOREIGN KEY (id_advocatemaster) REFERENCES eglc_advocate_master(id);



ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT fk_advreqdetail_brg FOREIGN KEY (advancerequisitionid) REFERENCES eg_advancerequisition(id);



ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT fk_advreqdetail_fun FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY eg_advancerequisitiondetails
    ADD CONSTRAINT fk_advreqdetail_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egw_assetsforestimate
    ADD CONSTRAINT fk_ae_afe FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_overheadvalues
    ADD CONSTRAINT fk_ae_ohv FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_ae_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_aggdet_usr1 FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_aggdet_usr2 FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_intg_aggr_tx
    ADD CONSTRAINT fk_aggr FOREIGN KEY (aggregate_id) REFERENCES eg_intg_fin_aggregate(id);



ALTER TABLE ONLY eg_intg_agg_accthd
    ADD CONSTRAINT fk_aggr_accthd FOREIGN KEY (aggregate_id) REFERENCES eg_intg_fin_aggregate(id);



ALTER TABLE ONLY eg_intg_misdata
    ADD CONSTRAINT fk_aggr_misdata FOREIGN KEY (aggregate_id) REFERENCES eg_intg_fin_aggregate(id);



ALTER TABLE ONLY eglems_allottee
    ADD CONSTRAINT fk_agree_allotee FOREIGN KEY (agreementid) REFERENCES eglems_agreementdetail(id);



ALTER TABLE ONLY eglems_policychange
    ADD CONSTRAINT fk_agree_finyear FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY eglems_policychange
    ADD CONSTRAINT fk_agree_policychange FOREIGN KEY (agreementid) REFERENCES eglems_agreementdetail(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreedetail_unit FOREIGN KEY (unitid) REFERENCES eglems_unit(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_addrs FOREIGN KEY (useraddressid) REFERENCES eg_address(addressid);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_demand FOREIGN KEY (demandid) REFERENCES eg_demand(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_natureofbusiness FOREIGN KEY (natureofbusiness) REFERENCES eglems_natureofbusiness(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_paymentcycle FOREIGN KEY (paymentcycle) REFERENCES eglems_paymentcycle(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_status FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_tcc FOREIGN KEY (tccid) REFERENCES eglems_tcc(id);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agreement_user FOREIGN KEY (userid) REFERENCES eg_citizen(citizenid);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_agremntdtl_egwstatus FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY eg_ageing_list
    ADD CONSTRAINT fk_al_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_ageing_list
    ADD CONSTRAINT fk_al_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglems_allottee
    ADD CONSTRAINT fk_allotee_addrs FOREIGN KEY (addressid) REFERENCES eg_address(addressid);



ALTER TABLE ONLY eg_actiondetails
    ADD CONSTRAINT fk_apd_usr FOREIGN KEY (actiondoneby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_actiondetails
    ADD CONSTRAINT fk_apd_usr1 FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_asset_po
    ADD CONSTRAINT fk_apo_ass FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY eg_appconfig_values
    ADD CONSTRAINT fk_appdata_key FOREIGN KEY (key_id) REFERENCES eg_appconfig(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_appln_req_rate_contract FOREIGN KEY (rate_contract_id) REFERENCES egw_rate_contract(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_appreq_orgzn FOREIGN KEY (organization_id) REFERENCES eg_portal_organization(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_appreq_service_dept FOREIGN KEY (service_dept_id) REFERENCES eg_portal_organization(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_ar_portaluser_id FOREIGN KEY (portaluser_id) REFERENCES eg_portal_user(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armis_dpt FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armis_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisar_ar FOREIGN KEY (advancerequisitionid) REFERENCES eg_advancerequisition(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armischeme_scheme FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisfield_bdry FOREIGN KEY (fieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisfund_fd FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisfunry_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armissubfield_bdry FOREIGN KEY (subfieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisubsm_subscheme FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY eg_advancerequisitionmis
    ADD CONSTRAINT fk_armisvh_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT fk_arpd_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT fk_arpd_ard FOREIGN KEY (advancerequisitiondetailid) REFERENCES eg_advancerequisitiondetails(id);



ALTER TABLE ONLY eg_advancereqpayeedetails
    ADD CONSTRAINT fk_arpd_tds FOREIGN KEY (tdsid) REFERENCES tds(id);



ALTER TABLE ONLY eg_assetvaluechange
    ADD CONSTRAINT fk_as_avc FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY eg_accountservicemapping
    ADD CONSTRAINT fk_asm_accountid FOREIGN KEY (accountsid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_accountservicemapping
    ADD CONSTRAINT fk_asm_serviceid FOREIGN KEY (serviceid) REFERENCES eg_servicedetails(id);



ALTER TABLE ONLY egw_assetsforestimate
    ADD CONSTRAINT fk_ass_afe FOREIGN KEY (asset_id) REFERENCES eg_asset(id);



ALTER TABLE ONLY egw_assetsforworkorder
    ADD CONSTRAINT fk_ass_afw FOREIGN KEY (asset_id) REFERENCES eg_asset(id);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_ass_usr1 FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_ass_usr2 FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_asset_ac FOREIGN KEY (categoryid) REFERENCES eg_assetcategory(id);



ALTER TABLE ONLY egasset_activities
    ADD CONSTRAINT fk_asset_act_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egasset_activities
    ADD CONSTRAINT fk_asset_act_voucher FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_mrline
    ADD CONSTRAINT fk_asset_cat FOREIGN KEY (assetcategoryid) REFERENCES eg_assetcategory(id);



ALTER TABLE ONLY egasset_depreciation
    ADD CONSTRAINT fk_asset_depr_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egasset_depreciation
    ADD CONSTRAINT fk_asset_depr_voucher FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egasset_disposalsalemis
    ADD CONSTRAINT fk_asset_dispos FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egasset_openingbalance
    ADD CONSTRAINT fk_asset_opb_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egasset_openingbalance
    ADD CONSTRAINT fk_asset_opb_finyear FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_asset_bills
    ADD CONSTRAINT fk_assetid_bill FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egasset_activities
    ADD CONSTRAINT fk_ast_act_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egasset_activities
    ADD CONSTRAINT fk_ast_act_status FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_dept FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_field FOREIGN KEY (fieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_fndsrc FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_func FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_fund FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_funry FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_schm FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY egasset_voucherheader
    ADD CONSTRAINT fk_ast_vh_subschm FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY eg_attributelist
    ADD CONSTRAINT fk_att_typeid FOREIGN KEY (att_typeid) REFERENCES eg_attributetype(id);



ALTER TABLE ONLY eg_attributevalues
    ADD CONSTRAINT fk_atttypeid FOREIGN KEY (att_typeid) REFERENCES eg_attributetype(id);



ALTER TABLE ONLY eg_authorization_rule
    ADD CONSTRAINT fk_auth_actionid FOREIGN KEY (actionid) REFERENCES eg_action(id);



ALTER TABLE ONLY egf_account_cheques
    ADD CONSTRAINT fk_ba_chq FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_ba_cjv FOREIGN KEY (frombankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_ba_cjv1 FOREIGN KEY (tobankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT fk_ba_ph FOREIGN KEY (bankaccountnumberid) REFERENCES bankaccount(id);



ALTER TABLE ONLY bankreconciliation
    ADD CONSTRAINT fk_bacc_brs FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egeis_nominee_master
    ADD CONSTRAINT fk_bank_branch FOREIGN KEY (branch_id) REFERENCES bankbranch(id);



ALTER TABLE ONLY egpay_saladvances
    ADD CONSTRAINT fk_bankaccount_id FOREIGN KEY (bankaccount_id) REFERENCES bankaccount(id);



ALTER TABLE ONLY egpt_property
    ADD CONSTRAINT fk_bas_prop FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT fk_baserent_adminbry FOREIGN KEY (adminboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT fk_baserent_locbry FOREIGN KEY (locboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT fk_baserent_uom FOREIGN KEY (uomid) REFERENCES eg_uom(id);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT fk_baserent_usgtype FOREIGN KEY (usagetypeid) REFERENCES eglems_usagetype(id);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT fk_baserent_usr1 FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglems_baserent
    ADD CONSTRAINT fk_baserent_usr2 FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpt_property_effective_period
    ADD CONSTRAINT fk_basic_property FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egpt_basic_property
    ADD CONSTRAINT fk_basic_status_id FOREIGN KEY (status) REFERENCES egpt_status(id_status);



ALTER TABLE ONLY egpt_property_status_values
    ADD CONSTRAINT fk_basicprop FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egpt_property_mutation
    ADD CONSTRAINT fk_basicpropformut FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY eg_bankaccountservicemapping
    ADD CONSTRAINT fk_basm_accountid FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY eg_bankaccountservicemapping
    ADD CONSTRAINT fk_basm_deptid FOREIGN KEY (id_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_bankaccountservicemapping
    ADD CONSTRAINT fk_basm_serviceid FOREIGN KEY (serviceid) REFERENCES eg_servicedetails(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_deptid FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_finyr_id FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_func_id FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_fund_id FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_id_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_pen_id FOREIGN KEY (pensionerid) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_pentype FOREIGN KEY (pensiontype) REFERENCES egpen_pension_types(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_batchfailure_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_batchgendetails
    ADD CONSTRAINT fk_batchgen_func_id FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egpen_batchgendetails
    ADD CONSTRAINT fk_batchgen_fund_id FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY bankaccount
    ADD CONSTRAINT fk_bb_ba FOREIGN KEY (branchid) REFERENCES bankbranch(id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_brg FOREIGN KEY (billid) REFERENCES eg_billregister(id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_fun FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY eg_billdetails
    ADD CONSTRAINT fk_bd_gl FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_billpayeedetails
    ADD CONSTRAINT fk_bdp_adt FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY eg_city_website
    ADD CONSTRAINT fk_bdr_cw FOREIGN KEY (bndryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY bankentries
    ADD CONSTRAINT fk_be_ih FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY bankbranch
    ADD CONSTRAINT fk_bk_bb FOREIGN KEY (bankid) REFERENCES bank(id);



ALTER TABLE ONLY relation
    ADD CONSTRAINT fk_bk_rel FOREIGN KEY (bankid) REFERENCES bank(id);



ALTER TABLE ONLY egpt_bndry_category
    ADD CONSTRAINT fk_bndry_bndrycategory FOREIGN KEY (id_bndry) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_user_jurvalues
    ADD CONSTRAINT fk_bndry_jurvalues FOREIGN KEY (id_bndry) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_user_jurlevel
    ADD CONSTRAINT fk_bndrytype_jurlevel FOREIGN KEY (id_bndry_type) REFERENCES eg_boundary_type(id_bndry_type);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_boundary_ass1 FOREIGN KEY (area_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_boundary_ass2 FOREIGN KEY (location_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_boundary_ass3 FOREIGN KEY (street_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_boundary_ass4 FOREIGN KEY (ward_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_boundary_ward FOREIGN KEY (ward) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_boundary_zone FOREIGN KEY (zone) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egpt_basic_property
    ADD CONSTRAINT fk_boundry FOREIGN KEY (id_adm_bndry) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT fk_bpaex_ims_surndedbywest FOREIGN KEY (surroundedbywest) REFERENCES egbpaextnd_mstr_surnbldgdtls(id);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT fk_bpaex_ims_surrdedbynorth FOREIGN KEY (surroundedbynorth) REFERENCES egbpaextnd_mstr_surnbldgdtls(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_bpaex_regn_adminbdry FOREIGN KEY (adminboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_bpaex_regn_locbdry FOREIGN KEY (locboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_fd FOREIGN KEY (fieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY bankreconciliation
    ADD CONSTRAINT fk_br_ih FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_usr FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_billregister
    ADD CONSTRAINT fk_br_usr1 FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_br FOREIGN KEY (billid) REFERENCES eg_billregister(id);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_bst FOREIGN KEY (billsubtype) REFERENCES eg_bill_subtype(id);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_dpt FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_fd FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_fy FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_billregistermis
    ADD CONSTRAINT fk_brm_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_exe_dept FOREIGN KEY (executing_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_functionary FOREIGN KEY (functionary) REFERENCES functionary(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_budgetdetail_using_dept FOREIGN KEY (using_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY jbpm_bytearray
    ADD CONSTRAINT fk_bytearr_fildef FOREIGN KEY (filedefinition_) REFERENCES jbpm_moduledefinition(id_);



ALTER TABLE ONLY jbpm_byteblock
    ADD CONSTRAINT fk_byteblock_file FOREIGN KEY (processfile_) REFERENCES jbpm_bytearray(id_);



ALTER TABLE ONLY jbpm_variableinstance
    ADD CONSTRAINT fk_byteinst_array FOREIGN KEY (bytearrayvalue_) REFERENCES jbpm_bytearray(id_);



ALTER TABLE ONLY eglems_agreementdetail
    ADD CONSTRAINT fk_cancel_type FOREIGN KEY (cancellationreason) REFERENCES eg_appconfig_values(id);



ALTER TABLE ONLY egstores_capitalissue
    ADD CONSTRAINT fk_capissue_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egstores_capitalissue
    ADD CONSTRAINT fk_capissue_projectcode FOREIGN KEY (projectcodeid) REFERENCES egw_projectcode(id);



ALTER TABLE ONLY egstores_capitalissue
    ADD CONSTRAINT fk_capital_mrinhr FOREIGN KEY (mrinheaderid) REFERENCES egf_mrinheader(id);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_case_casetype FOREIGN KEY (id_casetype) REFERENCES eglc_casetype_master(id);



ALTER TABLE ONLY eglc_hearings
    ADD CONSTRAINT fk_caseid FOREIGN KEY (id_legalcase) REFERENCES eglc_legalcase(id);



ALTER TABLE ONLY eg_catalogue
    ADD CONSTRAINT fk_cat_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_catalogue
    ADD CONSTRAINT fk_cat_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpt_bndry_category
    ADD CONSTRAINT fk_category_bndry_category FOREIGN KEY (id_category) REFERENCES egpt_category(id_category);



ALTER TABLE ONLY egpt_category
    ADD CONSTRAINT fk_categoryuid_userid FOREIGN KEY (userid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_catrgory_id FOREIGN KEY (categoryid) REFERENCES egpay_category_master(id);



ALTER TABLE ONLY egw_cancelled_bill
    ADD CONSTRAINT fk_cb_billreg FOREIGN KEY (billregister_id) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egw_cancelled_bill
    ADD CONSTRAINT fk_cb_mbheader FOREIGN KEY (mbheader_id) REFERENCES egw_mb_header(id);



ALTER TABLE ONLY egeis_cert_details
    ADD CONSTRAINT fk_cert_type_id FOREIGN KEY (id_cert_type) REFERENCES egeis_elig_cert_type(id);



ALTER TABLE ONLY egpen_cert_submission_details
    ADD CONSTRAINT fk_certsubdet_certhead FOREIGN KEY (id_certsubmit_header) REFERENCES egpen_cert_submission_header(id);



ALTER TABLE ONLY egpen_cert_submission_details
    ADD CONSTRAINT fk_certsubdet_certmstr FOREIGN KEY (id_cert_master) REFERENCES egpen_cert_master(id);



ALTER TABLE ONLY egpen_cert_submission_header
    ADD CONSTRAINT fk_certsubhead_creat_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_cert_submission_header
    ADD CONSTRAINT fk_certsubhead_fampenhead FOREIGN KEY (id_family_pensioner_header) REFERENCES egpen_family_pensioner_header(id);



ALTER TABLE ONLY egpen_cert_submission_header
    ADD CONSTRAINT fk_certsubhead_mod_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_cert_submission_header
    ADD CONSTRAINT fk_certsubhead_penhead FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_cfd_est_cfdid FOREIGN KEY (change_fd_id) REFERENCES egw_change_financialdetails(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_cfd_estid FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_budgetgrp FOREIGN KEY (budgetgroup_id) REFERENCES egf_budgetgroup(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_createdby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_dep_coa FOREIGN KEY (deposit_coa) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_function FOREIGN KEY (function_id) REFERENCES function(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_fund FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_modified_by FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_change_financialdetails
    ADD CONSTRAINT fk_change_fd_subscheme FOREIGN KEY (subscheme_id) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egbpaextnd_regn_approvalinfo
    ADD CONSTRAINT fk_changeofusage_to FOREIGN KEY (usage_to) REFERENCES egbpaextnd_mstr_changeofusage(id);



ALTER TABLE ONLY egbpa_regn_approvalinfo
    ADD CONSTRAINT fk_changeofusagefrom FOREIGN KEY (usage_from) REFERENCES egbpa_mstr_changeofusage(id);



ALTER TABLE ONLY egbpa_regn_approvalinfo
    ADD CONSTRAINT fk_changeofusageto FOREIGN KEY (usage_to) REFERENCES egbpa_mstr_changeofusage(id);



ALTER TABLE ONLY egpt_citizen_address
    ADD CONSTRAINT fk_cit_add_add FOREIGN KEY (id_address) REFERENCES eg_address(addressid);



ALTER TABLE ONLY egp_citizenactiveservcreg
    ADD CONSTRAINT fk_citizen_actvservreg_citizen FOREIGN KEY (portaluserid) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_citizenactiveservcreg
    ADD CONSTRAINT fk_citizen_actvservreg_servreg FOREIGN KEY (serviceregid) REFERENCES egp_citizenserviceregistry(id);



ALTER TABLE ONLY egp_citizennotification
    ADD CONSTRAINT fk_citizen_notifcation_citizen FOREIGN KEY (portaluserid) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_citizen
    ADD CONSTRAINT fk_citizen_portaluser FOREIGN KEY (id) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_citizenservicereqregistry
    ADD CONSTRAINT fk_citizen_servreqreg_citizen FOREIGN KEY (portaluserid) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_citizenservicereqregistry
    ADD CONSTRAINT fk_citizen_servreqreg_servreg FOREIGN KEY (serviceregid) REFERENCES egp_citizenserviceregistry(id);



ALTER TABLE ONLY egp_citizenserviceregistry
    ADD CONSTRAINT fk_citizenservicereg_action FOREIGN KEY (actionid) REFERENCES eg_action(id);



ALTER TABLE ONLY egp_citizenserviceregistry
    ADD CONSTRAINT fk_citizenservicereg_module FOREIGN KEY (moduleid) REFERENCES eg_module(id_module);



ALTER TABLE ONLY egp_citizenserviceregistry
    ADD CONSTRAINT fk_citizenservicereg_parent FOREIGN KEY (parentid) REFERENCES egp_citizenserviceregistry(id);



ALTER TABLE ONLY contrajournalvoucher
    ADD CONSTRAINT fk_cjv_ih FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY egbpaextnd_regn_approvalinfo
    ADD CONSTRAINT fk_cngofusgefrm FOREIGN KEY (usage_from) REFERENCES egbpaextnd_mstr_changeofusage(id);



ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_cess_master
    ADD CONSTRAINT fk_coa_cess FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY codemapping
    ADD CONSTRAINT fk_coa_cm FOREIGN KEY (chequeinhand) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY codemapping
    ADD CONSTRAINT fk_coa_cm1 FOREIGN KEY (cashinhand) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_coa_coa FOREIGN KEY (parentid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY codeservicemap
    ADD CONSTRAINT fk_coa_csm FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY accountdetailkey
    ADD CONSTRAINT fk_coa_dk FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_coa_gl FOREIGN KEY (glcode) REFERENCES chartofaccounts(glcode);



ALTER TABLE ONLY othertaxdetail
    ADD CONSTRAINT fk_coa_ot FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_tax_account_mapping
    ADD CONSTRAINT fk_coa_tam FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY tds
    ADD CONSTRAINT fk_coa_tds FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY voucherdetail
    ADD CONSTRAINT fk_coa_vd FOREIGN KEY (glcode) REFERENCES chartofaccounts(glcode);



ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT fk_coadt FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpt_collection_report
    ADD CONSTRAINT fk_coll_rpt_ward_id FOREIGN KEY (boundary_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcl_collectionheader
    ADD CONSTRAINT fk_collectionheader_status FOREIGN KEY (id_status) REFERENCES egw_status(id);



ALTER TABLE ONLY eggr_complainttypes
    ADD CONSTRAINT fk_comdept FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY jbpm_comment
    ADD CONSTRAINT fk_comment_token FOREIGN KEY (token_) REFERENCES jbpm_token(id_);



ALTER TABLE ONLY jbpm_comment
    ADD CONSTRAINT fk_comment_tsk FOREIGN KEY (taskinstance_) REFERENCES jbpm_taskinstance(id_);



ALTER TABLE ONLY eggr_status_tracker
    ADD CONSTRAINT fk_commid_statra FOREIGN KEY (communicationid) REFERENCES eggr_communication(id);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT fk_commutation_bill FOREIGN KEY (id_bill) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT fk_commutation_created_by FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT fk_commutation_modified_by FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT fk_commutation_pen_id FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT fk_commutation_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egpen_commutation
    ADD CONSTRAINT fk_commutation_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY eggr_complainttypes
    ADD CONSTRAINT fk_complaintgroup FOREIGN KEY (complaintgroup_id) REFERENCES eggr_complaintgroup(id_complaintgroup);



ALTER TABLE ONLY egtl_mstr_conserfee
    ADD CONSTRAINT fk_conserfee_trade FOREIGN KEY (id_trade_subctgr) REFERENCES egtl_mstr_trade_sub_ctgr(id);



ALTER TABLE ONLY egtl_mstr_conserfee
    ADD CONSTRAINT fk_conservancyfee_licensetype FOREIGN KEY (id_license_type) REFERENCES egtl_mstr_app_type(id);



ALTER TABLE ONLY egw_contractor_advance
    ADD CONSTRAINT fk_contadv_drawingofficer FOREIGN KEY (drawing_officer) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egw_contractor_advance
    ADD CONSTRAINT fk_contadv_workorderest FOREIGN KEY (workorderestimate_id) REFERENCES egw_workorder_estimate(id);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_cos_sch FOREIGN KEY (scheduleid) REFERENCES schedulemapping(id);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_cos_sch1 FOREIGN KEY (receiptscheduleid) REFERENCES schedulemapping(id);



ALTER TABLE ONLY chartofaccounts
    ADD CONSTRAINT fk_cos_sch2 FOREIGN KEY (paymentscheduleid) REFERENCES schedulemapping(id);



ALTER TABLE ONLY eglc_petitiontype_master
    ADD CONSTRAINT fk_courttype_pk FOREIGN KEY (id_courttype) REFERENCES eglc_courttype_master(id);



ALTER TABLE ONLY eglc_advcourttype_mapping
    ADD CONSTRAINT fk_courttypeid FOREIGN KEY (id_courttype) REFERENCES eglc_courttype_master(id);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egeis_cert_details
    ADD CONSTRAINT fk_createdby_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY jbpm_action
    ADD CONSTRAINT fk_crtetimeract_ta FOREIGN KEY (timeraction_) REFERENCES jbpm_action(id_);



ALTER TABLE ONLY jbpm_decisionconditions
    ADD CONSTRAINT fk_deccond_dec FOREIGN KEY (decision_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY jbpm_node
    ADD CONSTRAINT fk_decision_deleg FOREIGN KEY (decisiondelegation) REFERENCES jbpm_delegation(id_);



ALTER TABLE ONLY egpen_deductions
    ADD CONSTRAINT fk_ded_id_monthly_pen FOREIGN KEY (id_monthly_pension) REFERENCES egpen_monthly_pension(id);



ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT fk_dedd_fy FOREIGN KEY (partytypeid) REFERENCES eg_partytype(id);



ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT fk_dedd_typw FOREIGN KEY (doctypeid) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY eg_deduction_details
    ADD CONSTRAINT fk_dedd_typw1 FOREIGN KEY (docsubtypeid) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY jbpm_delegation
    ADD CONSTRAINT fk_delegation_prcd FOREIGN KEY (processdefinition_) REFERENCES jbpm_processdefinition(id_);



ALTER TABLE ONLY eg_demand_details
    ADD CONSTRAINT fk_demdndetail_status FOREIGN KEY (id_status) REFERENCES egw_status(id);



ALTER TABLE ONLY eg_asset
    ADD CONSTRAINT fk_department_ass FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_department_dept FOREIGN KEY (dept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egdms_intnl_user
    ADD CONSTRAINT fk_department_intnl_user FOREIGN KEY (dept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_department FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_financialyear FOREIGN KEY (financialyear_id) REFERENCES financialyear(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_function FOREIGN KEY (function_id) REFERENCES function(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_functionary FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_fund FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_fundsource FOREIGN KEY (fundsource_id) REFERENCES fundsource(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_subscheme FOREIGN KEY (subscheme_id) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_subtypeofwork FOREIGN KEY (subtypeofwork_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_typeofwork FOREIGN KEY (typeofwork_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_ward FOREIGN KEY (ward_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_workstype FOREIGN KEY (workstype_id) REFERENCES egw_workstype(id);



ALTER TABLE ONLY egw_depositcode
    ADD CONSTRAINT fk_depcode_zone FOREIGN KEY (zone_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_depositworks_usage
    ADD CONSTRAINT fk_depositworksusage_est FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egdcb_depreciationmaster
    ADD CONSTRAINT fk_depreciation_module FOREIGN KEY (id_module) REFERENCES eg_module(id_module);



ALTER TABLE ONLY egdcb_rebatemaster
    ADD CONSTRAINT fk_deprecmstruid_userid FOREIGN KEY (userid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdcb_depreciationmaster
    ADD CONSTRAINT fk_depremsrtuid_userid FOREIGN KEY (userid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdcb_depreciationmaster
    ADD CONSTRAINT fk_depremstr_instlmstrstr FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY eg_dept_do_mapping
    ADD CONSTRAINT fk_deptdo_deptid FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_dept_do_mapping
    ADD CONSTRAINT fk_deptdo_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egpay_batchfailuredetails
    ADD CONSTRAINT fk_deptid FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_dept FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_expense FOREIGN KEY (expenseaccount) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_item FOREIGN KEY (itemtypeid) REFERENCES eg_itemtype(id);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_purchase FOREIGN KEY (purchaseaccount) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_stockinhand FOREIGN KEY (stockinhandcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egstores_itemtypedetails
    ADD CONSTRAINT fk_deptstores_store FOREIGN KEY (storeid) REFERENCES eg_stores(id);



ALTER TABLE ONLY egw_depositworks_usage
    ADD CONSTRAINT fk_depworks_usage_coa FOREIGN KEY (coa_id) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egw_depositworks_usage
    ADD CONSTRAINT fk_depworks_usage_dep_code FOREIGN KEY (depositcode_id) REFERENCES egw_depositcode(id);



ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT fk_designation_chartofacc_id FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egdms_intnl_user
    ADD CONSTRAINT fk_designation_intnl_user FOREIGN KEY (desig) REFERENCES eg_designation(designationid);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_dettype FOREIGN KEY (accountdetailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY eg_assetcategory
    ADD CONSTRAINT fk_dfor_ac FOREIGN KEY (depmethord) REFERENCES egf_deprformula(id);



ALTER TABLE ONLY egdiary_header
    ADD CONSTRAINT fk_diary_eg_employee FOREIGN KEY (id_employee) REFERENCES eg_employee(id);



ALTER TABLE ONLY eg_digital_signed_docs
    ADD CONSTRAINT fk_digitalsign FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdiary_images
    ADD CONSTRAINT fk_dimg_activity_type FOREIGN KEY (id_diary_activity_type) REFERENCES egdiary_activity_type(id);



ALTER TABLE ONLY egdiary_images
    ADD CONSTRAINT fk_dimg_header FOREIGN KEY (id_diary_header) REFERENCES egdiary_header(id);



ALTER TABLE ONLY egpt_floordemandcalc
    ADD CONSTRAINT fk_dmdcalc FOREIGN KEY (id_dmdcalc) REFERENCES egpt_demandcalculations(id);



ALTER TABLE ONLY eg_demand_reason
    ADD CONSTRAINT fk_dreason_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY accountentitymaster
    ADD CONSTRAINT fk_dt_aem FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY chartofaccountdetail
    ADD CONSTRAINT fk_dt_coa FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY accountdetailkey
    ADD CONSTRAINT fk_dt_dk FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY generalledgerdetail
    ADD CONSTRAINT fk_dt_gld FOREIGN KEY (detailtypeid) REFERENCES accountdetailtype(id);



ALTER TABLE ONLY egw_dw_utilization_certificate
    ADD CONSTRAINT fk_dw_uc_appdetails FOREIGN KEY (application_details_id) REFERENCES egw_dw_application_details(id);



ALTER TABLE ONLY egw_dw_utilization_certificate
    ADD CONSTRAINT fk_dw_uc_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_dw_utilization_certificate
    ADD CONSTRAINT fk_dw_uc_usr_createdby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_utilization_certificate
    ADD CONSTRAINT fk_dw_uc_usr_modifiedby FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_utilization_certificate
    ADD CONSTRAINT fk_dw_uc_wfstate FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_dw_roadcut_approval_letter
    ADD CONSTRAINT fk_dwapp_letter_usr_createdby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_roadcut_approval_letter
    ADD CONSTRAINT fk_dwapp_letter_usr_modifiedby FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_appreq FOREIGN KEY (applicationrequest_id) REFERENCES egw_dw_applicationrequest(id);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_depcode FOREIGN KEY (depositcode_id) REFERENCES egw_depositcode(id);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_prpdby FOREIGN KEY (preparedby) REFERENCES eg_employee(id);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_usr_createdby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_usr_modifiedby FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_application_details
    ADD CONSTRAINT fk_dwappdtl_wfstate FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_dwappreq_dwtype FOREIGN KEY (deposit_type_id) REFERENCES egw_dw_deposit_type(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_dwappreq_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_dwappreq_usr_createdby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_applicationrequest
    ADD CONSTRAINT fk_dwappreq_usr_modifiedby FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT fk_dwrc_details_appreq FOREIGN KEY (applicationrequest_id) REFERENCES egw_dw_applicationrequest(id);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT fk_dwrc_details_area FOREIGN KEY (area_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT fk_dwrc_details_locality FOREIGN KEY (locality_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT fk_dwrc_details_street FOREIGN KEY (street_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT fk_dwrc_details_ward FOREIGN KEY (ward_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_dw_roadcut_details
    ADD CONSTRAINT fk_dwrc_details_zone FOREIGN KEY (zone_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_dw_roadcut_approval_letter
    ADD CONSTRAINT fk_dwrdappr_appdetails FOREIGN KEY (application_details_id) REFERENCES egw_dw_application_details(id);



ALTER TABLE ONLY egw_dw_roadcut_approval_letter
    ADD CONSTRAINT fk_dwrdappr_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_dw_roadcut_approval_letter
    ADD CONSTRAINT fk_dwrdappr_wfstate FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_dw_deposit_type
    ADD CONSTRAINT fk_dwtype_orgzn FOREIGN KEY (organization_id) REFERENCES eg_portal_organization(id);



ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT fk_ebconsumer_boundary FOREIGN KEY (wardid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT fk_ebconsumer_created_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_ebconsumer
    ADD CONSTRAINT fk_ebconsumer_modified_user_id FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_bill_id FOREIGN KEY (billid) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_consumer_no FOREIGN KEY (consumerno) REFERENCES egf_ebconsumer(id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_created_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_modified_user_id FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_ebdetails_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY eg_bankaccountservicemapping
    ADD CONSTRAINT fk_ecstype FOREIGN KEY (ecstype) REFERENCES egf_ecstype(id);



ALTER TABLE ONLY egw_estimate_template
    ADD CONSTRAINT fk_eet_etw FOREIGN KEY (worktype_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_estimate_template
    ADD CONSTRAINT fk_eet_etws FOREIGN KEY (subtype_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_est_temp_activity
    ADD CONSTRAINT fk_eeta_en FOREIGN KEY (nonsor_id) REFERENCES egw_nonsor(id);



ALTER TABLE ONLY egw_est_temp_activity
    ADD CONSTRAINT fk_eeta_es FOREIGN KEY (scheduleofrate_id) REFERENCES egw_scheduleofrate(id);



ALTER TABLE ONLY egw_est_temp_activity
    ADD CONSTRAINT fk_eeta_est FOREIGN KEY (estimate_template_id) REFERENCES egw_estimate_template(id);



ALTER TABLE ONLY egw_est_temp_activity
    ADD CONSTRAINT fk_eeta_eu FOREIGN KEY (uom_id) REFERENCES eg_uom(id);



ALTER TABLE ONLY eg_event_action_citizen
    ADD CONSTRAINT fk_eg_action FOREIGN KEY (actionid) REFERENCES eg_event_actions(pkid);



ALTER TABLE ONLY eg_bill_details
    ADD CONSTRAINT fk_eg_bill_det_idbill FOREIGN KEY (id_bill) REFERENCES eg_bill(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_eg_boundary_ward FOREIGN KEY (ward) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_checklists
    ADD CONSTRAINT fk_eg_checklist_appconfig FOREIGN KEY (appconfig_values_id) REFERENCES eg_appconfig_values(id);



ALTER TABLE ONLY eg_demand_details
    ADD CONSTRAINT fk_eg_dem_reason_id FOREIGN KEY (id_demand_reason) REFERENCES eg_demand_reason(id);



ALTER TABLE ONLY eg_demand_reason
    ADD CONSTRAINT fk_eg_dem_reason_id_base_dem FOREIGN KEY (id_base_reason) REFERENCES eg_demand_reason(id);



ALTER TABLE ONLY eg_demand_details
    ADD CONSTRAINT fk_eg_demand_id FOREIGN KEY (id_demand) REFERENCES eg_demand(id);



ALTER TABLE ONLY eg_demand_reason
    ADD CONSTRAINT fk_eg_demandrsn_purid FOREIGN KEY (purposeid) REFERENCES egf_accountcode_purpose(id);



ALTER TABLE ONLY egeis_nominee_master
    ADD CONSTRAINT fk_eg_employee FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY eg_demand
    ADD CONSTRAINT fk_eg_instal_master_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY eg_demand_reason
    ADD CONSTRAINT fk_eg_installment_id FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY eg_demand_reason_master
    ADD CONSTRAINT fk_eg_module_id FOREIGN KEY (module_id) REFERENCES eg_module(id_module);



ALTER TABLE ONLY eg_demand_reason_master
    ADD CONSTRAINT fk_eg_reason_category_id FOREIGN KEY (id_category) REFERENCES eg_reason_category(id_type);



ALTER TABLE ONLY egdcb_rebatemaster
    ADD CONSTRAINT fk_eg_rebate_mster_eg_mod FOREIGN KEY (id_module) REFERENCES eg_module(id_module);



ALTER TABLE ONLY eg_bill
    ADD CONSTRAINT fk_egbilltype_id FOREIGN KEY (id_bill_type) REFERENCES eg_bill_type(id);



ALTER TABLE ONLY egbpa_address
    ADD CONSTRAINT fk_egbpa_add_addtypeid FOREIGN KEY (addresstypeid) REFERENCES eg_address_type_master(id_address_type);



ALTER TABLE ONLY egbpa_address
    ADD CONSTRAINT fk_egbpa_add_regid FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_inspection
    ADD CONSTRAINT fk_egbpa_appl_chklist_regid FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_inspection_details
    ADD CONSTRAINT fk_egbpa_appl_inspdt_const FOREIGN KEY (conststagesid) REFERENCES egbpa_mstr_const_stages(id);



ALTER TABLE ONLY egbpa_apprd_buildingdetails
    ADD CONSTRAINT fk_egbpa_appr_regid FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_apprd_buildingfloordtls
    ADD CONSTRAINT fk_egbpa_apprflr_apbfdid FOREIGN KEY (approvedbldgid) REFERENCES egbpa_apprd_buildingdetails(id);



ALTER TABLE ONLY egbpa_apprd_buildingfloordtls
    ADD CONSTRAINT fk_egbpa_apprflr_exbldugid FOREIGN KEY (exist_bldg_usage) REFERENCES egbpa_mstr_buildingusage(id);



ALTER TABLE ONLY egbpa_apprd_buildingfloordtls
    ADD CONSTRAINT fk_egbpa_apprflr_prbldugid FOREIGN KEY (proposed_bldg_usage) REFERENCES egbpa_mstr_buildingusage(id);



ALTER TABLE ONLY egbpa_autodcr_floordetails
    ADD CONSTRAINT fk_egbpa_autodcrflrdautodcrid FOREIGN KEY (autodcr_id) REFERENCES egbpa_autodcr(id);



ALTER TABLE ONLY egbpa_autodcr_floordetails
    ADD CONSTRAINT fk_egbpa_autodcrflrdexistusgid FOREIGN KEY (existing_bldg_usage) REFERENCES egbpa_mstr_buildingusage(id);



ALTER TABLE ONLY egbpa_autodcr_floordetails
    ADD CONSTRAINT fk_egbpa_autodcrflrdpropusgid FOREIGN KEY (proposed_bldg_usage) REFERENCES egbpa_mstr_buildingusage(id);



ALTER TABLE ONLY egbpa_ddfee_details
    ADD CONSTRAINT fk_egbpa_bank_dd FOREIGN KEY (ddbank) REFERENCES bank(id);



ALTER TABLE ONLY egbpa_mstr_buildingcategory
    ADD CONSTRAINT fk_egbpa_cat_mstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_buildingcategory
    ADD CONSTRAINT fk_egbpa_cat_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_rejection_checklist
    ADD CONSTRAINT fk_egbpa_chk_rejid FOREIGN KEY (rejectionid) REFERENCES egbpa_rejection(id);



ALTER TABLE ONLY egbpa_mstr_checklist
    ADD CONSTRAINT fk_egbpa_chklist_mstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_checklist
    ADD CONSTRAINT fk_egbpa_chklist_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_checklist
    ADD CONSTRAINT fk_egbpa_chklist_mstr_srvtype FOREIGN KEY (servicetypeid) REFERENCES egbpa_mstr_servicetype(id);



ALTER TABLE ONLY egbpa_mstr_const_stages
    ADD CONSTRAINT fk_egbpa_conststg_mstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_const_stages
    ADD CONSTRAINT fk_egbpa_conststg_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_changeofusage
    ADD CONSTRAINT fk_egbpa_cou_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_changeofusage
    ADD CONSTRAINT fk_egbpa_cou_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_docket_constrnstage
    ADD CONSTRAINT fk_egbpa_docconstgchklst FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpaextnd_docket_constrnstage
    ADD CONSTRAINT fk_egbpa_docconststages FOREIGN KEY (docketid) REFERENCES egbpaextnd_docket(id);



ALTER TABLE ONLY egbpaextnd_docket_violationdtl
    ADD CONSTRAINT fk_egbpa_docetviolationdtl FOREIGN KEY (docketid) REFERENCES egbpaextnd_docket(id);



ALTER TABLE ONLY egbpaextnd_docket_floordetails
    ADD CONSTRAINT fk_egbpa_docfloordetails FOREIGN KEY (docketid) REFERENCES egbpaextnd_docket(id);



ALTER TABLE ONLY egbpaextnd_docket_documentdtl
    ADD CONSTRAINT fk_egbpa_docktdocchklst FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpaextnd_docket_documentdtl
    ADD CONSTRAINT fk_egbpa_docktdocdtl FOREIGN KEY (docketid) REFERENCES egbpaextnd_docket(id);



ALTER TABLE ONLY egbpaextnd_docket_violationdtl
    ADD CONSTRAINT fk_egbpa_docviolationchklst FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpa_mstr_bpafeedetail
    ADD CONSTRAINT fk_egbpa_feelinemstr_bpaid FOREIGN KEY (feeid) REFERENCES egbpa_mstr_bpafee(id);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT fk_egbpa_feemstr_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT fk_egbpa_feemstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT fk_egbpa_feemstr_functionid FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT fk_egbpa_feemstr_funid FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT fk_egbpa_feemstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_bpafee
    ADD CONSTRAINT fk_egbpa_feemstr_servtype FOREIGN KEY (servicetypeid) REFERENCES egbpa_mstr_servicetype(id);



ALTER TABLE ONLY egbpa_inspection
    ADD CONSTRAINT fk_egbpa_insp_inspby FOREIGN KEY (inspectedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_inspection
    ADD CONSTRAINT fk_egbpa_insp_mstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_inspection
    ADD CONSTRAINT fk_egbpa_insp_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_inspection
    ADD CONSTRAINT fk_egbpa_inspec_parentid FOREIGN KEY (parent) REFERENCES egbpa_inspection(id);



ALTER TABLE ONLY egbpa_inspection_checklist
    ADD CONSTRAINT fk_egbpa_inspect_clist_chklid FOREIGN KEY (checklistdetailid) REFERENCES egbpa_mstr_checklistdetail(id);



ALTER TABLE ONLY egbpa_inspection_checklist
    ADD CONSTRAINT fk_egbpa_inspect_inspdtlaid FOREIGN KEY (inspectiondtlsid) REFERENCES egbpa_inspection_details(id);



ALTER TABLE ONLY egbpa_inspection_details
    ADD CONSTRAINT fk_egbpa_inspectionid FOREIGN KEY (id) REFERENCES egbpa_inspection(id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT fk_egbpa_inspmsrdtls_insdtlsid FOREIGN KEY (inspectiondtlsid) REFERENCES egbpa_inspection_details(id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT fk_egbpa_inspmsrdtls_inspsrcid FOREIGN KEY (inspectionsourceid) REFERENCES egbpa_mstr_inspectionsource(id);



ALTER TABLE ONLY egbpa_lpchecklist
    ADD CONSTRAINT fk_egbpa_lp_chklist_chklid FOREIGN KEY (checklistdetailid) REFERENCES egbpa_mstr_checklistdetail(id);



ALTER TABLE ONLY egbpa_lpchecklist
    ADD CONSTRAINT fk_egbpa_lp_chklist_idlp FOREIGN KEY (lpid) REFERENCES egbpa_lettertoparty(id);



ALTER TABLE ONLY egbpa_lettertoparty
    ADD CONSTRAINT fk_egbpa_ltp_idreg FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_lettertoparty
    ADD CONSTRAINT fk_egbpa_ltp_inspid FOREIGN KEY (inspectionid) REFERENCES egbpa_inspection(id);



ALTER TABLE ONLY egbpa_lettertoparty
    ADD CONSTRAINT fk_egbpa_ltp_lpr FOREIGN KEY (lp_reasonid) REFERENCES egbpa_mstr_lpreason(id);



ALTER TABLE ONLY egbpa_mstr_checklistdetail
    ADD CONSTRAINT fk_egbpa_mstr_checklist_id FOREIGN KEY (checklistid) REFERENCES egbpa_mstr_checklist(id);



ALTER TABLE ONLY egbpa_mstr_inspectionsource
    ADD CONSTRAINT fk_egbpa_mstr_inspecsr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_inspectionsource
    ADD CONSTRAINT fk_egbpa_mstr_inspecsr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_lpreason
    ADD CONSTRAINT fk_egbpa_mstr_lpr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_lpreason
    ADD CONSTRAINT fk_egbpa_mstr_lpr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_buildingusage
    ADD CONSTRAINT fk_egbpa_mstr_usg_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_buildingusage
    ADD CONSTRAINT fk_egbpa_mstr_usg_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_village
    ADD CONSTRAINT fk_egbpa_mstr_village FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_address
    ADD CONSTRAINT fk_egbpa_mstr_villagename FOREIGN KEY (villageid) REFERENCES egbpa_mstr_village(id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_parent_id FOREIGN KEY (parent) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_citizenid FOREIGN KEY (ownerid) REFERENCES eg_citizen(citizenid);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_id_demand FOREIGN KEY (demandid) REFERENCES eg_demand(id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_id_servtype FOREIGN KEY (servicetypeid) REFERENCES egbpa_mstr_servicetype(id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_id_state FOREIGN KEY (stateid) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_id_status FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_mstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_reg_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_registrationfee
    ADD CONSTRAINT fk_egbpa_regfee_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_registrationfee
    ADD CONSTRAINT fk_egbpa_regfee_id_reg FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_registrationfee
    ADD CONSTRAINT fk_egbpa_regfee_id_state FOREIGN KEY (stateid) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egbpa_registrationfee
    ADD CONSTRAINT fk_egbpa_regfee_id_status FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egbpa_registrationfee
    ADD CONSTRAINT fk_egbpa_regfee_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_registrationfeedetail
    ADD CONSTRAINT fk_egbpa_regfeedtl_fee FOREIGN KEY (bpafeeid) REFERENCES egbpa_mstr_bpafee(id);



ALTER TABLE ONLY egbpa_registrationfeedetail
    ADD CONSTRAINT fk_egbpa_regfeedtl_id FOREIGN KEY (registrationfeeid) REFERENCES egbpa_registrationfee(id);



ALTER TABLE ONLY egbpa_regn_approvalinfo
    ADD CONSTRAINT fk_egbpa_regn_approvalinfo FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_regn_autodcr
    ADD CONSTRAINT fk_egbpa_regn_autodcr_idlp FOREIGN KEY (lpid) REFERENCES egbpa_lettertoparty(id);



ALTER TABLE ONLY egbpa_regn_autodcr
    ADD CONSTRAINT fk_egbpa_regn_autodcr_idreg FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_registration_checklist
    ADD CONSTRAINT fk_egbpa_regn_chklist_chklid FOREIGN KEY (checklistdetailid) REFERENCES egbpa_mstr_checklistdetail(id);



ALTER TABLE ONLY egbpa_registration_checklist
    ADD CONSTRAINT fk_egbpa_regn_chklist_idreg FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_ddfee_details
    ADD CONSTRAINT fk_egbpa_regn_dd FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_regn_details
    ADD CONSTRAINT fk_egbpa_regn_dtls_exbldgcat FOREIGN KEY (exist_bldgcatgid) REFERENCES egbpa_mstr_buildingcategory(id);



ALTER TABLE ONLY egbpa_regn_details
    ADD CONSTRAINT fk_egbpa_regn_dtls_prbldgcat FOREIGN KEY (proposed_bldgcatgid) REFERENCES egbpa_mstr_buildingcategory(id);



ALTER TABLE ONLY egbpa_regn_details
    ADD CONSTRAINT fk_egbpa_regn_dtls_regid FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_regn_approvalinfo
    ADD CONSTRAINT fk_egbpa_regn_regid FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_regnstatus_details
    ADD CONSTRAINT fk_egbpa_regnst_regid FOREIGN KEY (registrationid) REFERENCES egbpa_registration(id);



ALTER TABLE ONLY egbpa_regnstatus_details
    ADD CONSTRAINT fk_egbpa_regnst_statusid FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egbpa_rejection_checklist
    ADD CONSTRAINT fk_egbpa_rejchklist FOREIGN KEY (checklistdetailid) REFERENCES egbpa_mstr_checklistdetail(id);



ALTER TABLE ONLY egbpa_mstr_surroundedbldgdtls
    ADD CONSTRAINT fk_egbpa_sbd_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_surroundedbldgdtls
    ADD CONSTRAINT fk_egbpa_sbd_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_servicetype
    ADD CONSTRAINT fk_egbpa_serv_typ_mstr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_servicetype
    ADD CONSTRAINT fk_egbpa_serv_typ_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_mstr_surveyorname
    ADD CONSTRAINT fk_egbpa_surveyor_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpa_registration
    ADD CONSTRAINT fk_egbpa_surveyor_id FOREIGN KEY (surveyorid) REFERENCES egbpa_mstr_surveyorname(id);



ALTER TABLE ONLY egbpaextnd_apprd_bldgfloordtls
    ADD CONSTRAINT fk_egbpaex_aprflr_exbldugid FOREIGN KEY (exist_bldg_usage) REFERENCES egbpaextnd_mstr_buildingusage(id);



ALTER TABLE ONLY egbpaextnd_mstr_bldgcategory
    ADD CONSTRAINT fk_egbpaex_cat_mst_crtby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_checklist
    ADD CONSTRAINT fk_egbpaex_chklt_mst_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT fk_egbpaex_feemst_ctdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT fk_egbpaex_feemst_funid FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT fk_egbpaex_feemstr_coa FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT fk_egbpaex_inspmstls_intlsid FOREIGN KEY (inspectiondtlsid) REFERENCES egbpaextnd_inspection_details(id);



ALTER TABLE ONLY egbpaextnd_lpchecklist
    ADD CONSTRAINT fk_egbpaex_lp_chklist_idlp FOREIGN KEY (lpid) REFERENCES egbpaextnd_lettertoparty(id);



ALTER TABLE ONLY egbpaextnd_mstr_buildingusage
    ADD CONSTRAINT fk_egbpaex_mst_usg_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_lpreason
    ADD CONSTRAINT fk_egbpaex_mstr_lpr_mfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_servicetype
    ADD CONSTRAINT fk_egbpaex_serv_typ_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_surveyorname
    ADD CONSTRAINT fk_egbpaex_surv_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_address
    ADD CONSTRAINT fk_egbpaext_add_addtypid FOREIGN KEY (addresstypeid) REFERENCES eg_address_type_master(id_address_type);



ALTER TABLE ONLY egbpaextnd_inspection_details
    ADD CONSTRAINT fk_egbpaext_app_inpdt_cont FOREIGN KEY (conststagesid) REFERENCES egbpaextnd_mstr_const_stages(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaext_appl_chklt_reg FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_apprd_bldgfloordtls
    ADD CONSTRAINT fk_egbpaext_apprflr_prbldu FOREIGN KEY (proposed_bldg_usage) REFERENCES egbpaextnd_mstr_buildingusage(id);



ALTER TABLE ONLY egbpaextnd_apprd_bldgfloordtls
    ADD CONSTRAINT fk_egbpaext_aprflr_apbfid FOREIGN KEY (approvedbldgid) REFERENCES egbpaextnd_apprd_bldgdetails(id);



ALTER TABLE ONLY egbpaextnd_mstr_bldgcategory
    ADD CONSTRAINT fk_egbpaext_cat_mstr_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_checklist
    ADD CONSTRAINT fk_egbpaext_chklt_mst_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_checklist
    ADD CONSTRAINT fk_egbpaext_chklt_mst_srvtype FOREIGN KEY (servicetypeid) REFERENCES egbpaextnd_mstr_servicetype(id);



ALTER TABLE ONLY egbpaextnd_mstr_const_stages
    ADD CONSTRAINT fk_egbpaext_constg_mst_mfby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_const_stages
    ADD CONSTRAINT fk_egbpaext_contst_mst_cdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_autodcr_floordetail
    ADD CONSTRAINT fk_egbpaext_dcrflrdautodcrid FOREIGN KEY (autodcr_id) REFERENCES egbpaextnd_autodcr(id);



ALTER TABLE ONLY egbpaextnd_autodcr_floordetail
    ADD CONSTRAINT fk_egbpaext_dcrflrdextusgid FOREIGN KEY (existing_bldg_usage) REFERENCES egbpaextnd_mstr_buildingusage(id);



ALTER TABLE ONLY egbpaextnd_autodcr_floordetail
    ADD CONSTRAINT fk_egbpaext_dcrflrdpropusgid FOREIGN KEY (proposed_bldg_usage) REFERENCES egbpaextnd_mstr_buildingusage(id);



ALTER TABLE ONLY egbpaextnd_mstr_bpafeedetail
    ADD CONSTRAINT fk_egbpaext_feedtlbldusgid FOREIGN KEY (usagetypeid) REFERENCES egbpaextnd_land_buildingtypes(id);



ALTER TABLE ONLY egbpaextnd_mstr_bpafeedetail
    ADD CONSTRAINT fk_egbpaext_feelnemst_bpaid FOREIGN KEY (feeid) REFERENCES egbpaextnd_mstr_bpafee(id);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT fk_egbpaext_feemst_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT fk_egbpaext_feemst_setype FOREIGN KEY (servicetypeid) REFERENCES egbpaextnd_mstr_servicetype(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaext_insp_insby FOREIGN KEY (inspectedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_insp_checklist
    ADD CONSTRAINT fk_egbpaext_insp_inspdtlaid FOREIGN KEY (inspectiondtlsid) REFERENCES egbpaextnd_inspection_details(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaext_insp_mst_crtby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaext_insp_mt_mdfby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaext_insp_prntid FOREIGN KEY (parent) REFERENCES egbpaextnd_inspection(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaext_inspection_docket FOREIGN KEY (docketid) REFERENCES egbpaextnd_docket(id);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT fk_egbpaext_inspmsrdtl_inrcid FOREIGN KEY (inspectionsourceid) REFERENCES egbpaextnd_mstr_inspsource(id);



ALTER TABLE ONLY egbpaextnd_inspection_details
    ADD CONSTRAINT fk_egbpaext_inspnid FOREIGN KEY (id) REFERENCES egbpaextnd_inspection(id);



ALTER TABLE ONLY egbpaextnd_lpchecklist
    ADD CONSTRAINT fk_egbpaext_lp_chklst_cklid FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpaextnd_lettertoparty
    ADD CONSTRAINT fk_egbpaext_ltp_lpr FOREIGN KEY (lp_reasonid) REFERENCES egbpaextnd_mstr_lpreason(id);



ALTER TABLE ONLY egbpaextnd_mstr_chklistdetail
    ADD CONSTRAINT fk_egbpaext_mst_chklist_id FOREIGN KEY (checklistid) REFERENCES egbpaextnd_mstr_checklist(id);



ALTER TABLE ONLY egbpaextnd_mstr_buildingusage
    ADD CONSTRAINT fk_egbpaext_mst_usg_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_inspsource
    ADD CONSTRAINT fk_egbpaext_mstr_inscsr_mdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_inspsource
    ADD CONSTRAINT fk_egbpaext_mstr_insp_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_address
    ADD CONSTRAINT fk_egbpaext_mstr_vgename FOREIGN KEY (villageid) REFERENCES egbpaextnd_mstr_village(id);



ALTER TABLE ONLY egbpaextnd_regn_details
    ADD CONSTRAINT fk_egbpaext_reg_dtl_exblg FOREIGN KEY (exist_bldgcatgid) REFERENCES egbpaextnd_mstr_bldgcategory(id);



ALTER TABLE ONLY egbpaextnd_regn_details
    ADD CONSTRAINT fk_egbpaext_reg_dtl_prbldcat FOREIGN KEY (proposed_bldgcatgid) REFERENCES egbpaextnd_mstr_bldgcategory(id);



ALTER TABLE ONLY egbpaextnd_regn_details
    ADD CONSTRAINT fk_egbpaext_reg_dtl_regid FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaext_reg_id_dmd FOREIGN KEY (demandid) REFERENCES eg_demand(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaext_reg_id_sertyp FOREIGN KEY (servicetypeid) REFERENCES egbpaextnd_mstr_servicetype(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaext_reg_mst_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaext_reg_mst_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_regn_checklist
    ADD CONSTRAINT fk_egbpaext_regn_chklst_chkid FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpaextnd_regn_checklist
    ADD CONSTRAINT fk_egbpaext_regn_chklst_idrg FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_ddfee_details
    ADD CONSTRAINT fk_egbpaext_regn_dd FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_documenthistory
    ADD CONSTRAINT fk_egbpaext_regn_id FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_regn_autodcr
    ADD CONSTRAINT fk_egbpaext_regn_idreg FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_regn_approvalinfo
    ADD CONSTRAINT fk_egbpaext_regn_regid FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_rejection_checklist
    ADD CONSTRAINT fk_egbpaext_rejchklst FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpaextnd_mstr_servicetype
    ADD CONSTRAINT fk_egbpaext_serv_typ_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_registrationfee
    ADD CONSTRAINT fk_egbpaextn_regfee_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_registrationfee
    ADD CONSTRAINT fk_egbpaextn_regfee_id_state FOREIGN KEY (stateid) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egbpaextnd_regnstatus_details
    ADD CONSTRAINT fk_egbpaextn_regn_statsid FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egbpaextnd_regnstatus_details
    ADD CONSTRAINT fk_egbpaextn_regst_regid FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_registrationfee
    ADD CONSTRAINT fk_egbpaextn_rgfee_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_address
    ADD CONSTRAINT fk_egbpaextnd_add_regid FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_apprd_bldgdetails
    ADD CONSTRAINT fk_egbpaextnd_appr_regid FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_ddfee_details
    ADD CONSTRAINT fk_egbpaextnd_bank_dd FOREIGN KEY (ddbank) REFERENCES bank(id);



ALTER TABLE ONLY egbpaextnd_rejection_checklist
    ADD CONSTRAINT fk_egbpaextnd_chk_rejid FOREIGN KEY (rejectionid) REFERENCES egbpaextnd_rejection(id);



ALTER TABLE ONLY egbpaextnd_mstr_changeofusage
    ADD CONSTRAINT fk_egbpaextnd_cou_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_changeofusage
    ADD CONSTRAINT fk_egbpaextnd_cou_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_bpafee
    ADD CONSTRAINT fk_egbpaextnd_feemstr_funid FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaextnd_insp_bldngtype FOREIGN KEY (building_type) REFERENCES egbpaextnd_land_buildingtypes(id);



ALTER TABLE ONLY egbpaextnd_insp_checklist
    ADD CONSTRAINT fk_egbpaextnd_insp_clst_chklid FOREIGN KEY (checklistdetailid) REFERENCES egbpaextnd_mstr_chklistdetail(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaextnd_insp_layout FOREIGN KEY (lnd_layout_type) REFERENCES egbpaextnd_mstr_layout(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaextnd_insp_lndusage FOREIGN KEY (lnd_usage) REFERENCES egbpaextnd_land_buildingtypes(id);



ALTER TABLE ONLY egbpaextnd_inspection
    ADD CONSTRAINT fk_egbpaextnd_insp_swd FOREIGN KEY (bldg_stormwaterdrain) REFERENCES egbpaextnd_stormwaterdrain(id);



ALTER TABLE ONLY egbpaextnd_mstr_layout
    ADD CONSTRAINT fk_egbpaextnd_layout_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_layout
    ADD CONSTRAINT fk_egbpaextnd_layout_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_land_buildingtypes
    ADD CONSTRAINT fk_egbpaextnd_lbt_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_land_buildingtypes
    ADD CONSTRAINT fk_egbpaextnd_lbt_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_lettertoparty
    ADD CONSTRAINT fk_egbpaextnd_ltp_idreg FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_lettertoparty
    ADD CONSTRAINT fk_egbpaextnd_ltp_inspid FOREIGN KEY (inspectionid) REFERENCES egbpaextnd_inspection(id);



ALTER TABLE ONLY egbpaextnd_mstr_lpreason
    ADD CONSTRAINT fk_egbpaextnd_mstr_lpr_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_village
    ADD CONSTRAINT fk_egbpaextnd_mstr_village FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaextnd_parent_id FOREIGN KEY (parent) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY ebpaextnd_dochist_detail
    ADD CONSTRAINT fk_egbpaextnd_redocid FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaextnd_reg_citizenid FOREIGN KEY (ownerid) REFERENCES eg_citizen(citizenid);



ALTER TABLE ONLY egbpaextnd_regn_autodcr
    ADD CONSTRAINT fk_egbpaextnd_reg_dcr_idlp FOREIGN KEY (lpid) REFERENCES egbpaextnd_lettertoparty(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egbpaextnd_reg_id_status FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egbpaextnd_registrationfee
    ADD CONSTRAINT fk_egbpaextnd_regfee_id_reg FOREIGN KEY (registrationid) REFERENCES egbpaextnd_registration(id);



ALTER TABLE ONLY egbpaextnd_registrationfee
    ADD CONSTRAINT fk_egbpaextnd_regfee_id_status FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egbpaextnd_regnfeedetail
    ADD CONSTRAINT fk_egbpaextnd_regfeedtl_fee FOREIGN KEY (bpafeeid) REFERENCES egbpaextnd_mstr_bpafee(id);



ALTER TABLE ONLY egbpaextnd_regnfeedetail
    ADD CONSTRAINT fk_egbpaextnd_regfeedtl_id FOREIGN KEY (registrationfeeid) REFERENCES egbpaextnd_registrationfee(id);



ALTER TABLE ONLY egbpaextnd_regn_approvalinfo
    ADD CONSTRAINT fk_egbpaextnd_regn_applinfo FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_roadwidth
    ADD CONSTRAINT fk_egbpaextnd_roadwidth_crtd FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_roadwidth
    ADD CONSTRAINT fk_egbpaextnd_roadwidth_mdfd FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_surnbldgdtls
    ADD CONSTRAINT fk_egbpaextnd_sbd_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_mstr_surnbldgdtls
    ADD CONSTRAINT fk_egbpaextnd_sbd_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_stormwaterdrain
    ADD CONSTRAINT fk_egbpaextnd_swdrain_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_stormwaterdrain
    ADD CONSTRAINT fk_egbpaextnd_swdrain_modified FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egcsr_mstr_service_type
    ADD CONSTRAINT fk_egcsr_servicetype_cat FOREIGN KEY (id_service_cat) REFERENCES egcsr_mstr_service_category(id);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_area_bndry FOREIGN KEY (area) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_locatoin_bndry FOREIGN KEY (location) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_service_cat FOREIGN KEY (id_service_cat) REFERENCES egcsr_mstr_service_category(id);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_service_type FOREIGN KEY (id_service_type) REFERENCES egcsr_mstr_service_type(id);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_street_bndry FOREIGN KEY (street) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_ward_bndry FOREIGN KEY (ward) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egcsr_service_request
    ADD CONSTRAINT fk_egcsr_sr_zone_bndry FOREIGN KEY (zone) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eg_bill
    ADD CONSTRAINT fk_egdemand_id FOREIGN KEY (id_demand) REFERENCES eg_demand(id);



ALTER TABLE ONLY eg_demand_reason
    ADD CONSTRAINT fk_egdemandreasonmaster_id FOREIGN KEY (id_demand_reason_master) REFERENCES eg_demand_reason_master(id);



ALTER TABLE ONLY egdiary_attribute_type_column
    ADD CONSTRAINT fk_egdiary_activity_type FOREIGN KEY (id_diary_activity_type) REFERENCES egdiary_activity_type(id);



ALTER TABLE ONLY egdiary_attribute_values
    ADD CONSTRAINT fk_egdiary_attr_type_column FOREIGN KEY (id_diary_attr_type_column) REFERENCES egdiary_attribute_type_column(id);



ALTER TABLE ONLY egdiary_details
    ADD CONSTRAINT fk_egdiary_attribute_values FOREIGN KEY (id_diary_attr_values) REFERENCES egdiary_attribute_values(id);



ALTER TABLE ONLY egdiary_details
    ADD CONSTRAINT fk_egdiary_det_activity_type FOREIGN KEY (id_diary_activity_type) REFERENCES egdiary_activity_type(id);



ALTER TABLE ONLY egdiary_details
    ADD CONSTRAINT fk_egdiary_header FOREIGN KEY (id_diary_header) REFERENCES egdiary_header(id);



ALTER TABLE ONLY egdms_extnl_user
    ADD CONSTRAINT fk_egdms_extnl_user_source FOREIGN KEY (user_source) REFERENCES egdms_file_source(id);



ALTER TABLE ONLY egdms_file_assignment
    ADD CONSTRAINT fk_egdms_file_extnluser FOREIGN KEY (external_user) REFERENCES egdms_extnl_user(id);



ALTER TABLE ONLY egdms_file_assignment
    ADD CONSTRAINT fk_egdms_file_id FOREIGN KEY (file_id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY egdms_file_assignment
    ADD CONSTRAINT fk_egdms_file_intluser FOREIGN KEY (internal_user) REFERENCES egdms_intnl_user(id);



ALTER TABLE ONLY egf_brs_bankstatements
    ADD CONSTRAINT fk_egf_brs_accountnumber_id FOREIGN KEY (accountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_egf_budget_budget FOREIGN KEY (parent) REFERENCES egf_budget(id);



ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_egf_budget_eg_finyear1 FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egf_budget
    ADD CONSTRAINT fk_egf_budget_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_egf_budgetdetail FOREIGN KEY (budgetdetail) REFERENCES egf_budgetdetail(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_budget FOREIGN KEY (budget) REFERENCES egf_budget(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_fund FOREIGN KEY (fund) REFERENCES fund(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_funtion FOREIGN KEY (function) REFERENCES function(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_group FOREIGN KEY (budgetgroup) REFERENCES egf_budgetgroup(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_scheme FOREIGN KEY (scheme) REFERENCES scheme(id);



ALTER TABLE ONLY egf_budgetdetail
    ADD CONSTRAINT fk_egf_budgetdetail_subscheme FOREIGN KEY (subscheme) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_majorcode FOREIGN KEY (majorcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_maxcode FOREIGN KEY (maxcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_budgetgroup
    ADD CONSTRAINT fk_egf_budgetgroup_mincode FOREIGN KEY (mincode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_fyid FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_pos FOREIGN KEY (position_id) REFERENCES eg_position(id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_ta FOREIGN KEY (target_area_id) REFERENCES egf_target_area(id);



ALTER TABLE ONLY egf_ebdetails
    ADD CONSTRAINT fk_egf_ebdetails_ward FOREIGN KEY (wardid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egf_ebschedulerlogdetails
    ADD CONSTRAINT fk_egf_eblog_ebconsumer FOREIGN KEY (consumer) REFERENCES egf_ebconsumer(id);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_accrualvh FOREIGN KEY (accrualvoucherid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_deptid FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_finyearid FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_grantvh FOREIGN KEY (grantvoucherid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_instrumentheader FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY egf_grant
    ADD CONSTRAINT fk_egf_grant_receiptvh FOREIGN KEY (receiptvoucherid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT fk_egf_instracccodes_coa FOREIGN KEY (typeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_instrumentaccountcodes
    ADD CONSTRAINT fk_egf_instracccodes_instrtype FOREIGN KEY (typeid) REFERENCES egf_instrumenttype(id);



ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT fk_egf_lgdetail_agency FOREIGN KEY (agencyid) REFERENCES egf_fundingagency(id);



ALTER TABLE ONLY egf_loangrantdetail
    ADD CONSTRAINT fk_egf_lgdetail_lgheader FOREIGN KEY (headerid) REFERENCES egf_loangrantheader(id);



ALTER TABLE ONLY egf_loangrantheader
    ADD CONSTRAINT fk_egf_lgheader_subscheme FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_agency FOREIGN KEY (agencyid) REFERENCES egf_fundingagency(id);



ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_bankacc FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_instrument FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_lgheader FOREIGN KEY (headerid) REFERENCES egf_loangrantheader(id);



ALTER TABLE ONLY egf_loangrantreceiptdetail
    ADD CONSTRAINT fk_egf_lgrcptdetail_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_loan_paid
    ADD CONSTRAINT fk_egf_loan_paid_fa FOREIGN KEY (agencyid) REFERENCES egf_fundingagency(id);



ALTER TABLE ONLY egf_loan_paid
    ADD CONSTRAINT fk_egf_loan_paid_scheme FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY egf_budget_reappropriation
    ADD CONSTRAINT fk_egf_reappropriation_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_bankacc FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_sch FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY egf_scheme_bankaccount
    ADD CONSTRAINT fk_egf_scheme_ba_subsch FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egf_subscheme_project
    ADD CONSTRAINT fk_egf_subscheme_prj_subsch FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY eggr_region_dept_mapping
    ADD CONSTRAINT fk_eggr_region_dept FOREIGN KEY (dept_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_eglc_legalcase_status FOREIGN KEY (id_status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_egp_glcode FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egbpaextnd_registration
    ADD CONSTRAINT fk_egp_reg_surveyourid FOREIGN KEY (surveyorid) REFERENCES egp_surveyor(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_egpay_emppayroll FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpay_saladvances
    ADD CONSTRAINT fk_egpay_saladvances_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egpen_cert_submission_header
    ADD CONSTRAINT fk_egpen_cert_financialyear FOREIGN KEY (year) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_demand_reason_details
    ADD CONSTRAINT fk_egpt_dem_reason_id FOREIGN KEY (id_demand_reason) REFERENCES eg_demand_reason(id);



ALTER TABLE ONLY egpt_prop_corr_details
    ADD CONSTRAINT fk_egpt_prop_corr_det FOREIGN KEY (id_prop_correction) REFERENCES egpt_property_corrections(id);



ALTER TABLE ONLY egpt_property_corrections
    ADD CONSTRAINT fk_egpt_prop_corr_idbasic FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egpt_prop_corr_details
    ADD CONSTRAINT fk_egpt_propcorr_det_inst FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egpt_ptdemand
    ADD CONSTRAINT fk_egpt_property_id FOREIGN KEY (id_property) REFERENCES egpt_property(id_property);



ALTER TABLE ONLY egpt_rcpt_rectify_details
    ADD CONSTRAINT fk_egpt_rcpt_rect_det_idinst FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egpt_rcpt_rectify_details
    ADD CONSTRAINT fk_egpt_rcpt_rect_det_idrect FOREIGN KEY (id_rcpt_rectify) REFERENCES egpt_rcpt_rectification(id);



ALTER TABLE ONLY egpt_rcpt_rectification
    ADD CONSTRAINT fk_egpt_rcpt_rect_stateid FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egstores_indent_quotation
    ADD CONSTRAINT fk_egstores_indentquot_rel FOREIGN KEY (supplierid) REFERENCES relation(id);



ALTER TABLE ONLY egstores_indent_quotation
    ADD CONSTRAINT fk_egstores_indentquot_sanc FOREIGN KEY (processindentlineid) REFERENCES egstores_processindentline(id);



ALTER TABLE ONLY egstores_processindent_mrline
    ADD CONSTRAINT fk_egstores_poidtmr_item FOREIGN KEY (mrlineid) REFERENCES egf_mrline(id);



ALTER TABLE ONLY egstores_processindent_mrline
    ADD CONSTRAINT fk_egstores_poidtmr_sanc FOREIGN KEY (processindentlineid) REFERENCES egstores_processindentline(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_crtdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_dept FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_function FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_fund FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_id_bdgt FOREIGN KEY (budgetusageid) REFERENCES egf_budget_usage(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_id_state FOREIGN KEY (stateid) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_mdfdby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_status FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_poindnt_store FOREIGN KEY (storeid) REFERENCES eg_stores(id);



ALTER TABLE ONLY egstores_processindentline
    ADD CONSTRAINT fk_egstores_poindntline_item FOREIGN KEY (itemid) REFERENCES eg_item(id);



ALTER TABLE ONLY egstores_processindentline
    ADD CONSTRAINT fk_egstores_poindntline_sanc FOREIGN KEY (processindentid) REFERENCES egstores_processindent(id);



ALTER TABLE ONLY egstores_processindentline
    ADD CONSTRAINT fk_egstores_poindntline_uom FOREIGN KEY (uom) REFERENCES eg_uom(id);



ALTER TABLE ONLY egstores_processindent
    ADD CONSTRAINT fk_egstores_rel FOREIGN KEY (quotedsupplierused) REFERENCES relation(id);



ALTER TABLE ONLY egstores_processindenttender
    ADD CONSTRAINT fk_egstores_tender_mdfby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_area FOREIGN KEY (area_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egtl_mstr_area_licfee
    ADD CONSTRAINT fk_egtl_areafee_schedule FOREIGN KEY (id_schedule) REFERENCES egtl_mstr_schedule(id);



ALTER TABLE ONLY egtl_mstr_area_licfee
    ADD CONSTRAINT fk_egtl_areafee_trade FOREIGN KEY (id_trade_subctgr) REFERENCES egtl_mstr_trade_sub_ctgr(id);



ALTER TABLE ONLY egtl_demand
    ADD CONSTRAINT fk_egtl_demandjoin_demand FOREIGN KEY (demand_id) REFERENCES eg_demand(id);



ALTER TABLE ONLY egtl_demand
    ADD CONSTRAINT fk_egtl_demandjoin_license FOREIGN KEY (license_id) REFERENCES egtl_license(id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_division FOREIGN KEY (division_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egtl_license_cancel_info
    ADD CONSTRAINT fk_egtl_lic_can_info_lic FOREIGN KEY (license_id) REFERENCES egtl_license(id);



ALTER TABLE ONLY egtl_duplicate_license_info
    ADD CONSTRAINT fk_egtl_lic_duplic_info FOREIGN KEY (license_id) REFERENCES egtl_license(id);



ALTER TABLE ONLY egtl_license_objection_info
    ADD CONSTRAINT fk_egtl_lic_obj_info_lic FOREIGN KEY (license_id) REFERENCES egtl_license(id);



ALTER TABLE ONLY egtl_license_revoke_info
    ADD CONSTRAINT fk_egtl_lic_revoke_lic_info FOREIGN KEY (license_id) REFERENCES egtl_license(id);



ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_egtl_licdet_installment FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egtl_license
    ADD CONSTRAINT fk_egtl_licdet_trade FOREIGN KEY (trade_id) REFERENCES egtl_trade(id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_location FOREIGN KEY (location_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egtl_mstr_trade_sub_ctgr
    ADD CONSTRAINT fk_egtl_mstr_trade_nature FOREIGN KEY (id_trade_nature) REFERENCES egtl_mstr_trade_nature(id);



ALTER TABLE ONLY egtl_name_transfer
    ADD CONSTRAINT fk_egtl_nametran_trade FOREIGN KEY (trade_id) REFERENCES egtl_trade(id);



ALTER TABLE ONLY egtl_mstr_pfa_fee
    ADD CONSTRAINT fk_egtl_pfa_fee_type FOREIGN KEY (id_application_type) REFERENCES egtl_mstr_app_type(id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_primary_trade FOREIGN KEY (trade_subctgr_id) REFERENCES egtl_mstr_trade_sub_ctgr(id);



ALTER TABLE ONLY egtl_renewal
    ADD CONSTRAINT fk_egtl_renewal_installment FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egtl_renewal
    ADD CONSTRAINT fk_egtl_renewal_license FOREIGN KEY (license_id) REFERENCES egtl_license(id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_street FOREIGN KEY (street_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egtl_mstr_trade_sub_ctgr
    ADD CONSTRAINT fk_egtl_trade_category FOREIGN KEY (id_trade_category) REFERENCES egtl_mstr_trade_category(id);



ALTER TABLE ONLY egtl_mstr_trade_sub_ctgr
    ADD CONSTRAINT fk_egtl_trade_department FOREIGN KEY (id_trade_department) REFERENCES egtl_department(id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_trade_nametransfer FOREIGN KEY (latest_name_transfer_id) REFERENCES egtl_name_transfer(id);



ALTER TABLE ONLY egtl_mstr_trade_sub_ctgr
    ADD CONSTRAINT fk_egtl_trademas_schedule FOREIGN KEY (id_schedule) REFERENCES egtl_mstr_schedule(id);



ALTER TABLE ONLY egtl_mstr_weight_licfee
    ADD CONSTRAINT fk_egtl_weightfee_schedule FOREIGN KEY (id_schedule) REFERENCES egtl_mstr_schedule(id);



ALTER TABLE ONLY egtl_mstr_weight_licfee
    ADD CONSTRAINT fk_egtl_weightfee_trade FOREIGN KEY (id_trade_subctgr) REFERENCES egtl_mstr_trade_sub_ctgr(id);



ALTER TABLE ONLY egtl_trade
    ADD CONSTRAINT fk_egtl_zone FOREIGN KEY (zone_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_egw_absestimate_olddept FOREIGN KEY (old_exec_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_egw_absestimate_oldward FOREIGN KEY (old_ward) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_estimate_reappr_details
    ADD CONSTRAINT fk_egw_est_reappr_detail_cre FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_estimate_reappr_details
    ADD CONSTRAINT fk_egw_est_reappr_detail_modf FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_est_reappr_user_dept FOREIGN KEY (user_department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_estimate_photo
    ADD CONSTRAINT fk_egw_estimate_photo FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_estimate_reappr_bndary FOREIGN KEY (ward) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_estimate_reappr_created FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_estimate_reappr_dept FOREIGN KEY (department) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_estimate_reappr_modf FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_estimate_reappr_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_estimate_reappropriation
    ADD CONSTRAINT fk_egw_estimate_reappr_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT fk_egw_myea_createdby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT fk_egw_myea_dept FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT fk_egw_myea_financialyearid FOREIGN KEY (financialyear_id) REFERENCES financialyear(id);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT fk_egw_myea_modifiedby FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT fk_egw_myea_stateid FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_multiyearestimateappr
    ADD CONSTRAINT fk_egw_myea_statusid FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_absestimateid FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_budgetgroupid FOREIGN KEY (budgetgroup_id) REFERENCES egf_budgetgroup(id);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_creaetedby FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_functionid FOREIGN KEY (function_id) REFERENCES function(id);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_fundid FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_modfiedby FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_multiyearestimateapprdtls
    ADD CONSTRAINT fk_egw_myead_multiyearestappr FOREIGN KEY (multiyearestimateappr_id) REFERENCES egw_multiyearestimateappr(id);



ALTER TABLE ONLY egw_projectcodemis
    ADD CONSTRAINT fk_egw_projectcodemis_projcode FOREIGN KEY (projectcode_id) REFERENCES egw_projectcode(id);



ALTER TABLE ONLY egeis_leave_opening_balance
    ADD CONSTRAINT fk_eis_calendaryear FOREIGN KEY (calendaryear) REFERENCES calendaryear(id);



ALTER TABLE ONLY egpay_batchfailuredetails
    ADD CONSTRAINT fk_emp__id FOREIGN KEY (empid) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpay_payscale_employee
    ADD CONSTRAINT fk_emp_id FOREIGN KEY (id_employee) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpay_saladvances
    ADD CONSTRAINT fk_employee_id FOREIGN KEY (id_employee) REFERENCES eg_employee(id);



ALTER TABLE ONLY egdms_intnl_user
    ADD CONSTRAINT fk_employee_intnl_user FOREIGN KEY (empinfo) REFERENCES eg_employee(id);



ALTER TABLE ONLY egeis_cert_details
    ADD CONSTRAINT fk_employeeid FOREIGN KEY (id_emp) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpen_nominee
    ADD CONSTRAINT fk_empnomid_nominee FOREIGN KEY (emp_nominee_id) REFERENCES egeis_nominee_master(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_emppayroll_posid FOREIGN KEY (first_approver_pos) REFERENCES eg_position(id);



ALTER TABLE ONLY eg_history_attribs
    ADD CONSTRAINT fk_entityhist_attribs FOREIGN KEY (id_entity_history) REFERENCES eg_entity_history(id_history);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_est_application_request FOREIGN KEY (application_request_id) REFERENCES egw_dw_applicationrequest(id);



ALTER TABLE ONLY egw_est_apprn_history
    ADD CONSTRAINT fk_est_apprn_hist_bud FOREIGN KEY (budgetusage_id) REFERENCES egf_budget_usage(id);



ALTER TABLE ONLY egw_est_apprn_history
    ADD CONSTRAINT fk_est_apprn_hist_est FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_est_by FOREIGN KEY (preparedby) REFERENCES eg_employee(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_est_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_est_rate_contract FOREIGN KEY (rate_contract_id) REFERENCES egw_rate_contract(id);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_adminbry FOREIGN KEY (adminboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_assetid FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_egwstatus FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_landid FOREIGN KEY (landid) REFERENCES eglems_land(id);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_locbry FOREIGN KEY (locboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_usr1 FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglems_estate
    ADD CONSTRAINT fk_estate_usr2 FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_estimate_appropriation
    ADD CONSTRAINT fk_estimate_appropriation_bud FOREIGN KEY (budgetusage_id) REFERENCES egf_budget_usage(id);



ALTER TABLE ONLY egw_estimate_appropriation
    ADD CONSTRAINT fk_estimate_appropriation_dep FOREIGN KEY (depositworksusage_id) REFERENCES egw_depositworks_usage(id);



ALTER TABLE ONLY egw_estimate_appropriation
    ADD CONSTRAINT fk_estimate_appropriation_est FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_estimate_estimate FOREIGN KEY (parentid) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_estimate_projcode FOREIGN KEY (projectcode_id) REFERENCES egw_projectcode(id);



ALTER TABLE ONLY egw_estimate_reappr_details
    ADD CONSTRAINT fk_estimate_reappr_detail_est FOREIGN KEY (estimate) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY eg_abstract_estimate_status
    ADD CONSTRAINT fk_estimate_state FOREIGN KEY (status_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY jbpm_event
    ADD CONSTRAINT fk_event_node FOREIGN KEY (node_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY jbpm_event
    ADD CONSTRAINT fk_event_procdef FOREIGN KEY (processdefinition_) REFERENCES jbpm_processdefinition(id_);



ALTER TABLE ONLY jbpm_event
    ADD CONSTRAINT fk_event_task FOREIGN KEY (task_) REFERENCES jbpm_task(id_);



ALTER TABLE ONLY jbpm_event
    ADD CONSTRAINT fk_event_trans FOREIGN KEY (transition_) REFERENCES jbpm_transition(id_);



ALTER TABLE ONLY eg_event_actions
    ADD CONSTRAINT fk_eventactions_event FOREIGN KEY (eventid) REFERENCES eg_event(eventid);



ALTER TABLE ONLY egdms_outbound_file
    ADD CONSTRAINT fk_extnl_user_receiver FOREIGN KEY (receiver) REFERENCES egdms_extnl_user(id);



ALTER TABLE ONLY egdms_inbound_file
    ADD CONSTRAINT fk_extnl_user_sender FOREIGN KEY (sender) REFERENCES egdms_extnl_user(id);



ALTER TABLE ONLY egdms_extnl_user
    ADD CONSTRAINT fk_extnl_user_type_extnl_user FOREIGN KEY (user_type) REFERENCES egdms_extnl_user_type(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_family_pensioner FOREIGN KEY (id_family_pensioner_header) REFERENCES egpen_family_pensioner_header(id);



ALTER TABLE ONLY egpen_batchfailuredetails
    ADD CONSTRAINT fk_familypensioner_id FOREIGN KEY (familypensionerid) REFERENCES egpen_family_pensioner_header(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fampen_do_id FOREIGN KEY (drawingoffice_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fampen_func_id FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_category FOREIGN KEY (category) REFERENCES egdms_file_category(id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_category_sub_category FOREIGN KEY (sub_category) REFERENCES egdms_file_category(id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_created_by FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_modified_by FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_priority FOREIGN KEY (priority) REFERENCES egdms_file_priority(id);



ALTER TABLE ONLY egdms_file_category
    ADD CONSTRAINT fk_file_property_category FOREIGN KEY (id) REFERENCES egdms_file_property(id);



ALTER TABLE ONLY egdms_file_priority
    ADD CONSTRAINT fk_file_property_priority FOREIGN KEY (id) REFERENCES egdms_file_property(id);



ALTER TABLE ONLY egdms_file_source
    ADD CONSTRAINT fk_file_property_source FOREIGN KEY (id) REFERENCES egdms_file_property(id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_source FOREIGN KEY (source) REFERENCES egdms_file_source(id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_states FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egdms_generic_file
    ADD CONSTRAINT fk_file_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_fin_year_id FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egpay_batchfailuredetails
    ADD CONSTRAINT fk_financialyear_id FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_financialyearid FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT fk_findetail_abstractest FOREIGN KEY (abstractestimate_id) REFERENCES egw_abstractestimate(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT fk_findetail_function FOREIGN KEY (function_id) REFERENCES function(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT fk_findetail_functionary FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT fk_findetail_fund FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT fk_findetail_scheme FOREIGN KEY (scheme_id) REFERENCES scheme(id);



ALTER TABLE ONLY egw_financialdetail
    ADD CONSTRAINT fk_findetail_subscheme FOREIGN KEY (subscheme_id) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egpay_batchgendetails
    ADD CONSTRAINT fk_finyear_id FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egpay_exception
    ADD CONSTRAINT fk_finyearid FOREIGN KEY (financilayear_id) REFERENCES financialyear(id);



ALTER TABLE ONLY egp_firmactiveservcreg
    ADD CONSTRAINT fk_firm_actvservreg_firm FOREIGN KEY (portaluserid) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_firmactiveservcreg
    ADD CONSTRAINT fk_firm_actvservreg_servreg FOREIGN KEY (serviceregid) REFERENCES egp_firmserviceregistry(id);



ALTER TABLE ONLY egp_firmnotification
    ADD CONSTRAINT fk_firm_notifcation_firm FOREIGN KEY (portaluserid) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_firm
    ADD CONSTRAINT fk_firm_portaluser FOREIGN KEY (id) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_firmservicereqregistry
    ADD CONSTRAINT fk_firm_servreqreg_firm FOREIGN KEY (portaluserid) REFERENCES egp_portaluser(id);



ALTER TABLE ONLY egp_firmservicereqregistry
    ADD CONSTRAINT fk_firm_servreqreg_servreg FOREIGN KEY (serviceregid) REFERENCES egp_firmserviceregistry(id);



ALTER TABLE ONLY egp_firmserviceregistry
    ADD CONSTRAINT fk_firmservicereg_action FOREIGN KEY (actionid) REFERENCES eg_action(id);



ALTER TABLE ONLY egp_firmserviceregistry
    ADD CONSTRAINT fk_firmservicereg_module FOREIGN KEY (moduleid) REFERENCES eg_module(id_module);



ALTER TABLE ONLY egp_firmserviceregistry
    ADD CONSTRAINT fk_firmservicereg_parent FOREIGN KEY (parentid) REFERENCES egp_firmserviceregistry(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_bkaccountid FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_bkbranchid FOREIGN KEY (bankbranchid) REFERENCES bankbranch(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_challanvh FOREIGN KEY (inflowgjvid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_fdvh FOREIGN KEY (outflowgjvid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_id FOREIGN KEY (parentid) REFERENCES egf_fixeddeposit(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_instrumentid FOREIGN KEY (instrumentheaderid) REFERENCES egf_instrumentheader(id);



ALTER TABLE ONLY egf_fixeddeposit
    ADD CONSTRAINT fk_fixeddeposit_withdrawalvh FOREIGN KEY (challanreceiptvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egpt_floordemandcalc
    ADD CONSTRAINT fk_floordet FOREIGN KEY (id_floordet) REFERENCES egpt_floor_detail(id_floor_detail);



ALTER TABLE ONLY egf_budget_usage
    ADD CONSTRAINT fk_fp_bu FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY fiscalperiod
    ADD CONSTRAINT fk_fp_md FOREIGN KEY (moduleid) REFERENCES eg_module(id_module);



ALTER TABLE ONLY eg_numbers
    ADD CONSTRAINT fk_fp_num FOREIGN KEY (fiscialperiodid) REFERENCES fiscalperiod(id);



ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_fp_vh FOREIGN KEY (fiscalperiodid) REFERENCES fiscalperiod(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_addbasic_valmap FOREIGN KEY (additionbasic_valmap_id) REFERENCES egpen_additionbasic_valmap(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_address_id_user FOREIGN KEY (address_id) REFERENCES eg_address(addressid);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_comp_id FOREIGN KEY (emp_component_id) REFERENCES egpen_basic_components(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_created_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_dept_id FOREIGN KEY (emp_department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_desig_id FOREIGN KEY (emp_designation_id) REFERENCES eg_designation(designationid);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_disb_typ_id FOREIGN KEY (disbursement_type_id) REFERENCES eg_disbursement_mode(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_emp_id FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_fptype_id1 FOREIGN KEY (familypensioner_type) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_fund_id_user FOREIGN KEY (emp_fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_grade_id FOREIGN KEY (emp_grade_id) REFERENCES egeis_grade_mstr(grade_id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_modified_id_user FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_nominee_id FOREIGN KEY (nominee_id) REFERENCES egeis_nominee_master(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_pay_commission FOREIGN KEY (pay_commission_id) REFERENCES egpen_pay_commission(id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_fpen_status_id FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_family_pensioner_details
    ADD CONSTRAINT fk_fpendet_created_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_family_pensioner_details
    ADD CONSTRAINT fk_fpendet_mod_id_user FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_family_pensioner_details
    ADD CONSTRAINT fk_fpendet_pen_header FOREIGN KEY (id_family_pensioner_header) REFERENCES egpen_family_pensioner_header(id);



ALTER TABLE ONLY fundsource
    ADD CONSTRAINT fk_fs FOREIGN KEY (parentid) REFERENCES fundsource(id);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_fs_txn FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY generalledger
    ADD CONSTRAINT fk_fun_gl FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egpen_func_do_mapping
    ADD CONSTRAINT fk_funcdo_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egpen_func_do_mapping
    ADD CONSTRAINT fk_funcdo_funcid FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY function
    ADD CONSTRAINT fk_function FOREIGN KEY (parentid) REFERENCES function(id);



ALTER TABLE ONLY fund
    ADD CONSTRAINT fk_fund1 FOREIGN KEY (parentid) REFERENCES fund(id);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_fund_ts FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_fund_vh FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT fk_fundflow_bankaccount_id FOREIGN KEY (bankaccountid) REFERENCES bankaccount(id);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT fk_fundflow_create_eg_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_fundflow
    ADD CONSTRAINT fk_fundflow_modif_eg_user_id FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY voucherheader
    ADD CONSTRAINT fk_fundsource_vh FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY fiscalperiod
    ADD CONSTRAINT fk_fy_fp FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egf_openingbalance_jobdetail
    ADD CONSTRAINT fk_fy_storesopeningbalancejob FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egf_tax_account_mapping
    ADD CONSTRAINT fk_fy_tam FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY transactionsummary
    ADD CONSTRAINT fk_fy_ts FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY egdms_inbound_file
    ADD CONSTRAINT fk_generic_file_inbound FOREIGN KEY (id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY egdms_internal_file
    ADD CONSTRAINT fk_generic_file_internal FOREIGN KEY (id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY egdms_notification_file
    ADD CONSTRAINT fk_generic_file_notify FOREIGN KEY (id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY egdms_outbound_file
    ADD CONSTRAINT fk_generic_file_outbound FOREIGN KEY (id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY generalledgerdetail
    ADD CONSTRAINT fk_gl_gld FOREIGN KEY (generalledgerid) REFERENCES generalledger(id);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_govtdepartment FOREIGN KEY (id_respondentgovtdept) REFERENCES eglc_governmentdepartment(id);



ALTER TABLE ONLY gpf_openbal
    ADD CONSTRAINT fk_gpf_openbal_empid FOREIGN KEY (empid) REFERENCES eg_employee(id);



ALTER TABLE ONLY gpf_openbal
    ADD CONSTRAINT fk_gpf_openbal_finyear FOREIGN KEY (financialyearid) REFERENCES financialyear(id);



ALTER TABLE ONLY eg_designation
    ADD CONSTRAINT fk_grade_id_desig FOREIGN KEY (grade_id) REFERENCES egeis_grade_mstr(grade_id);



ALTER TABLE ONLY egeis_employee_grade
    ADD CONSTRAINT fk_grademaster FOREIGN KEY (grade_id) REFERENCES egeis_grade_mstr(grade_id);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_grat_creat_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_grat_mod_id_user FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_grat_pen_id FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_grat_status_id FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_grat_type_id FOREIGN KEY (type) REFERENCES egpen_type_master(id);



ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT fk_group_created_by FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdms_notification_user
    ADD CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES egdms_notification_group(id);



ALTER TABLE ONLY egdms_notification_group
    ADD CONSTRAINT fk_group_modified_by FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egdms_notification_user
    ADD CONSTRAINT fk_group_user FOREIGN KEY (position_id) REFERENCES eg_position(id);



ALTER TABLE ONLY eglc_employeehearing
    ADD CONSTRAINT fk_hearing_employee FOREIGN KEY (id_hearing) REFERENCES eglc_hearings(id);



ALTER TABLE ONLY egeis_movable_prop_details
    ADD CONSTRAINT fk_how_acquired FOREIGN KEY (how_acquired_id) REFERENCES egeis_how_acquired_mstr(how_acquired_id);



ALTER TABLE ONLY egeis_immovable_prop_details
    ADD CONSTRAINT fk_how_acquired_imm FOREIGN KEY (how_acquired_id) REFERENCES egeis_how_acquired_mstr(how_acquired_id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibt_userid FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_interbintransferdetail
    ADD CONSTRAINT fk_ibtd_ibt FOREIGN KEY (interbintransferid) REFERENCES egf_interbintransfer(id);



ALTER TABLE ONLY egf_interbintransferdetail
    ADD CONSTRAINT fk_ibtd_mrinh FOREIGN KEY (mrinheaderid) REFERENCES egf_mrinheader(id);



ALTER TABLE ONLY egf_interbintransferdetail
    ADD CONSTRAINT fk_ibtd_mrnh FOREIGN KEY (mrnheaderid) REFERENCES egf_mrnheader(id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibtfrombinid_bins FOREIGN KEY (frombinid) REFERENCES eg_bins(id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibtitem_item FOREIGN KEY (itemid) REFERENCES eg_item(id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibtstatus_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibtstoreid_stores FOREIGN KEY (storeid) REFERENCES eg_stores(id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibttobinid_bins FOREIGN KEY (tobinid) REFERENCES eg_bins(id);



ALTER TABLE ONLY egf_interbintransfer
    ADD CONSTRAINT fk_ibtuomid_uom FOREIGN KEY (uomid) REFERENCES eg_uom(id);



ALTER TABLE ONLY egpay_deductions
    ADD CONSTRAINT fk_id_accountcode FOREIGN KEY (id_accountcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpay_deductions
    ADD CONSTRAINT fk_id_adv_schedule FOREIGN KEY (id_advance_scheduler) REFERENCES egpay_advance_schedule(id);



ALTER TABLE ONLY egpen_earnings
    ADD CONSTRAINT fk_id_arrears_detail FOREIGN KEY (id_arrears_detail) REFERENCES egpen_arrears_detail(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_id_assignment FOREIGN KEY (id_emp_assignment) REFERENCES eg_emp_assignment(id);



ALTER TABLE ONLY egdiary_images
    ADD CONSTRAINT fk_id_attribute_values FOREIGN KEY (id_diary_attribute_values) REFERENCES egdiary_attribute_values(id);



ALTER TABLE ONLY egpt_bills
    ADD CONSTRAINT fk_id_basic_property_bill FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egpt_notice100
    ADD CONSTRAINT fk_id_basic_propertynotice100 FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egpt_notice72
    ADD CONSTRAINT fk_id_basic_propertynotice72 FOREIGN KEY (id_basic_property) REFERENCES egpt_basic_property(id_basic_property);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_id_billregister FOREIGN KEY (id_billregister) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egeis_rel_nom_cert_reqd
    ADD CONSTRAINT fk_id_cert_type FOREIGN KEY (id_cert_type) REFERENCES egeis_elig_cert_type(id);



ALTER TABLE ONLY egeis_type_master
    ADD CONSTRAINT fk_id_chartofaccount FOREIGN KEY (id_chartofaccount) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpay_batchgendetails
    ADD CONSTRAINT fk_id_dept FOREIGN KEY (id_dept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_position
    ADD CONSTRAINT fk_id_drawingofficer FOREIGN KEY (id_drawing_officer) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egpay_payscale_header
    ADD CONSTRAINT fk_id_eg_wf_action FOREIGN KEY (id_wf_action) REFERENCES eg_wf_actions(id);



ALTER TABLE ONLY egpay_exception
    ADD CONSTRAINT fk_id_emp FOREIGN KEY (id_employee) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpay_earnings
    ADD CONSTRAINT fk_id_emp_payroll FOREIGN KEY (id_emppayroll) REFERENCES egpay_emppayroll(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_id_employee FOREIGN KEY (id_employee) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpay_deductions
    ADD CONSTRAINT fk_id_emppayroll FOREIGN KEY (id_emppayroll) REFERENCES egpay_emppayroll(id);



ALTER TABLE ONLY egpay_exception
    ADD CONSTRAINT fk_id_exceptionmstr FOREIGN KEY (id_exceptionmstr) REFERENCES egpay_exception_mstr(id);



ALTER TABLE ONLY egpay_batchgendetails
    ADD CONSTRAINT fk_id_function FOREIGN KEY (id_function) REFERENCES function(id);



ALTER TABLE ONLY egpay_batchfailuredetails
    ADD CONSTRAINT fk_id_function_batchfail FOREIGN KEY (id_function) REFERENCES function(id);



ALTER TABLE ONLY egpay_batchgendetails
    ADD CONSTRAINT fk_id_functionary FOREIGN KEY (id_functionary) REFERENCES functionary(id);



ALTER TABLE ONLY egpay_batchfailuredetails
    ADD CONSTRAINT fk_id_functionary_fail FOREIGN KEY (id_functionary) REFERENCES functionary(id);



ALTER TABLE ONLY eg_intg_fin_aggregate
    ADD CONSTRAINT fk_id_module FOREIGN KEY (mode_of_collection) REFERENCES eg_module(id_module);



ALTER TABLE ONLY egpen_earnings
    ADD CONSTRAINT fk_id_monthly_pension FOREIGN KEY (id_monthly_pension) REFERENCES egpen_monthly_pension(id);



ALTER TABLE ONLY egpt_mutation_owner
    ADD CONSTRAINT fk_id_mutation FOREIGN KEY (id_prop_mutation) REFERENCES egpt_property_mutation(id_prop_mutation);



ALTER TABLE ONLY egeis_rel_nom_cert_reqd
    ADD CONSTRAINT fk_id_nominee_type FOREIGN KEY (id_relation_type) REFERENCES egeis_relation_type(id);



ALTER TABLE ONLY egpen_earnings
    ADD CONSTRAINT fk_id_payhead FOREIGN KEY (id_payhead) REFERENCES egpen_payheads(id);



ALTER TABLE ONLY egpay_payscale_incrdetails
    ADD CONSTRAINT fk_id_payheader FOREIGN KEY (id_payheader) REFERENCES egpay_payscale_header(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_id_payscale_emp FOREIGN KEY (id_payscale_employee) REFERENCES egpay_payscale_employee(id);



ALTER TABLE ONLY egpay_incrementdetails
    ADD CONSTRAINT fk_id_payscale_incr FOREIGN KEY (id_payscale_employee) REFERENCES egpay_payscale_employee(id);



ALTER TABLE ONLY egpen_deductions
    ADD CONSTRAINT fk_id_pen_advance FOREIGN KEY (id_advance) REFERENCES egpen_advance(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_id_pensioner_header FOREIGN KEY (id_pension_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egeis_regularisation
    ADD CONSTRAINT fk_id_personal_information FOREIGN KEY (id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egf_target_area
    ADD CONSTRAINT fk_id_position FOREIGN KEY (positionid) REFERENCES eg_position(id);



ALTER TABLE ONLY egpt_property_mutation_docs
    ADD CONSTRAINT fk_id_propmutationdoc FOREIGN KEY (id_prop_doc) REFERENCES egpt_property_master_docs(id_prop_doc);



ALTER TABLE ONLY egpen_payhead_rule
    ADD CONSTRAINT fk_id_rule_payhead FOREIGN KEY (id_payhead) REFERENCES egpen_payheads(id);



ALTER TABLE ONLY egpen_payhead_rule
    ADD CONSTRAINT fk_id_rule_wf_action FOREIGN KEY (id_wf_action) REFERENCES eg_wf_actions(id);



ALTER TABLE ONLY egpay_deductions
    ADD CONSTRAINT fk_id_saladvance FOREIGN KEY (id_sal_advance) REFERENCES egpay_saladvances(id);



ALTER TABLE ONLY egpay_payhead_rule
    ADD CONSTRAINT fk_id_salarycode FOREIGN KEY (id_salarycode) REFERENCES egpay_salarycodes(id);



ALTER TABLE ONLY eglc_legalcase_advocate
    ADD CONSTRAINT fk_id_stage_junior FOREIGN KEY (id_juniorstage) REFERENCES eglc_case_stage(id);



ALTER TABLE ONLY eglc_legalcase_advocate
    ADD CONSTRAINT fk_id_stage_senior FOREIGN KEY (id_seniorstage) REFERENCES eglc_case_stage(id);



ALTER TABLE ONLY egeis_cert_details
    ADD CONSTRAINT fk_id_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpt_property_status_values
    ADD CONSTRAINT fk_id_ststus FOREIGN KEY (id_status) REFERENCES egpt_status(id_status);



ALTER TABLE ONLY egpt_category
    ADD CONSTRAINT fk_id_usage FOREIGN KEY (id_usage) REFERENCES egpt_property_usage_master(id_usg_mstr);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_id_wf_action FOREIGN KEY (id_wf_action) REFERENCES eg_wf_actions(id);



ALTER TABLE ONLY egpt_wfmodifyprop
    ADD CONSTRAINT fk_id_wfmodifyprop_status FOREIGN KEY (status_id) REFERENCES egpt_status(id_status);



ALTER TABLE ONLY egpt_wfproperty
    ADD CONSTRAINT fk_id_wfprop_status FOREIGN KEY (status_id) REFERENCES egpt_status(id_status);



ALTER TABLE ONLY notice
    ADD CONSTRAINT fk_idmodule_notice FOREIGN KEY (id_module) REFERENCES eg_module(id_module);



ALTER TABLE ONLY egpay_advance_schedule
    ADD CONSTRAINT fk_idsaladvance FOREIGN KEY (id_saladvance) REFERENCES egpay_saladvances(id);



ALTER TABLE ONLY eglc_legalcase_advocate
    ADD CONSTRAINT fk_idsenioradvocate FOREIGN KEY (id_senioradvocate) REFERENCES eglc_advocate_master(id);



ALTER TABLE ONLY notice
    ADD CONSTRAINT fk_iduser_notice FOREIGN KEY (id_user) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT fk_ims_surbysouth FOREIGN KEY (surroundedbysouth) REFERENCES egbpaextnd_mstr_surnbldgdtls(id);



ALTER TABLE ONLY egbpaextnd_inspect_measuredtls
    ADD CONSTRAINT fk_ims_surndedbyeast FOREIGN KEY (surroundedbyeast) REFERENCES egbpaextnd_mstr_surnbldgdtls(id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT fk_ims_surroundedbyeast FOREIGN KEY (surroundedbyeast) REFERENCES egbpa_mstr_surroundedbldgdtls(id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT fk_ims_surroundedbynorth FOREIGN KEY (surroundedbynorth) REFERENCES egbpa_mstr_surroundedbldgdtls(id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT fk_ims_surroundedbysouth FOREIGN KEY (surroundedbysouth) REFERENCES egbpa_mstr_surroundedbldgdtls(id);



ALTER TABLE ONLY egbpa_inspect_measurementdtls
    ADD CONSTRAINT fk_ims_surroundedbywest FOREIGN KEY (surroundedbywest) REFERENCES egbpa_mstr_surroundedbldgdtls(id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_dept FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_fund FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egw_indent_jurisdiction
    ADD CONSTRAINT fk_indent_juris FOREIGN KEY (indent_id) REFERENCES egw_indent(id);



ALTER TABLE ONLY egw_indent_jurisdiction
    ADD CONSTRAINT fk_indent_juris_division FOREIGN KEY (division_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_indent_jurisdiction
    ADD CONSTRAINT fk_indent_juris_zone FOREIGN KEY (zone_id) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egw_indent_maintenance_detail
    ADD CONSTRAINT fk_indent_maintenance_dtl FOREIGN KEY (indent_id) REFERENCES egw_indent(id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_modifiedby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT fk_indent_tender_contractor FOREIGN KEY (contractor_id) REFERENCES egw_contractor(id);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT fk_indent_tender_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT fk_indent_tender_modifiedby FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT fk_indent_tender_rc FOREIGN KEY (rate_contract_id) REFERENCES egw_rate_contract(id);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT fk_indent_tender_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_wfstate FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_work_subtype FOREIGN KEY (work_subtype_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_indent
    ADD CONSTRAINT fk_indent_worktype FOREIGN KEY (work_type_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egf_instrumentheader
    ADD CONSTRAINT fk_inh_ecs FOREIGN KEY (ecstype) REFERENCES egf_ecstype(id);



ALTER TABLE ONLY egpt_property
    ADD CONSTRAINT fk_inst_prop FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egpt_property_detail
    ADD CONSTRAINT fk_installment FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY eg_installment_master
    ADD CONSTRAINT fk_instmstr_module FOREIGN KEY (id_module) REFERENCES eg_module(id_module);



ALTER TABLE ONLY eg_integration_data
    ADD CONSTRAINT fk_int_fd FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY eg_integration_data
    ADD CONSTRAINT fk_int_fn FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_interest_glcodeid FOREIGN KEY (interest_glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egdms_inbound_file
    ADD CONSTRAINT fk_intnl_user_receiver FOREIGN KEY (receiver) REFERENCES egdms_intnl_user(id);



ALTER TABLE ONLY egdms_internal_file
    ADD CONSTRAINT fk_intnl_user_recvr FOREIGN KEY (receiver) REFERENCES egdms_intnl_user(id);



ALTER TABLE ONLY egdms_outbound_file
    ADD CONSTRAINT fk_intnl_user_sender FOREIGN KEY (sender) REFERENCES egdms_intnl_user(id);



ALTER TABLE ONLY egdms_internal_file
    ADD CONSTRAINT fk_intnl_user_sndr FOREIGN KEY (sender) REFERENCES egdms_intnl_user(id);



ALTER TABLE ONLY egf_issuerepair
    ADD CONSTRAINT fk_isr_mrin FOREIGN KEY (mrinheaderid) REFERENCES egf_mrinheader(id);



ALTER TABLE ONLY egf_issuerepair
    ADD CONSTRAINT fk_isr_user FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egstores_issuewriteoffscrap
    ADD CONSTRAINT fk_issue_writeoff FOREIGN KEY (mrinlineid) REFERENCES egf_mrinline(id);



ALTER TABLE ONLY egf_issuedfrommrn
    ADD CONSTRAINT fk_issuefrommrn_mrnline FOREIGN KEY (mrnlineid) REFERENCES egf_mrnline(id);



ALTER TABLE ONLY egf_interstoretransfer
    ADD CONSTRAINT fk_ist_mrinl FOREIGN KEY (mrinlineid) REFERENCES egf_mrinline(id);



ALTER TABLE ONLY egw_indent_tender
    ADD CONSTRAINT fk_it_latest_offl_status FOREIGN KEY (latest_offline_status_id) REFERENCES egw_works_status(id);



ALTER TABLE ONLY eg_itemtype
    ADD CONSTRAINT fk_it_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_itemtype
    ADD CONSTRAINT fk_it_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT fk_item_coa FOREIGN KEY (expenseglcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT fk_item_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT fk_item_puom FOREIGN KEY (puchuomid) REFERENCES eg_uom(id);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT fk_item_suom FOREIGN KEY (stockinguomid) REFERENCES eg_uom(id);



ALTER TABLE ONLY eg_item
    ADD CONSTRAINT fk_item_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egstores_itemdeptdetails
    ADD CONSTRAINT fk_itemdept_dept FOREIGN KEY (deptid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egstores_itemdeptdetails
    ADD CONSTRAINT fk_itemdept_item FOREIGN KEY (itemid) REFERENCES eg_item(id);



ALTER TABLE ONLY jbpm_job
    ADD CONSTRAINT fk_job_action FOREIGN KEY (action_) REFERENCES jbpm_action(id_);



ALTER TABLE ONLY jbpm_job
    ADD CONSTRAINT fk_job_node FOREIGN KEY (node_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY jbpm_job
    ADD CONSTRAINT fk_job_prinst FOREIGN KEY (processinstance_) REFERENCES jbpm_processinstance(id_);



ALTER TABLE ONLY jbpm_job
    ADD CONSTRAINT fk_job_token FOREIGN KEY (token_) REFERENCES jbpm_token(id_);



ALTER TABLE ONLY jbpm_job
    ADD CONSTRAINT fk_job_tskinst FOREIGN KEY (taskinstance_) REFERENCES jbpm_taskinstance(id_);



ALTER TABLE ONLY eg_user_jurvalues
    ADD CONSTRAINT fk_jurlevel_jurvalues FOREIGN KEY (id_user_jurlevel) REFERENCES eg_user_jurlevel(id_user_jurlevel);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT fk_land_adminbry FOREIGN KEY (adminboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT fk_land_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT fk_land_landtype FOREIGN KEY (landtypeid) REFERENCES eglems_landtype(id);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT fk_land_locbry FOREIGN KEY (locboundaryid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT fk_land_modeofacq FOREIGN KEY (modeofacquisitionid) REFERENCES eglems_modeofacquisition(id);



ALTER TABLE ONLY eglems_land
    ADD CONSTRAINT fk_land_uom FOREIGN KEY (landuomid) REFERENCES eg_uom(id);



ALTER TABLE ONLY eglems_landtype
    ADD CONSTRAINT fk_landtype_usr2 FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_lastmodifidby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_lc_functionaryid FOREIGN KEY (id_functionary) REFERENCES functionary(id);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_lc_petition FOREIGN KEY (id_petitiontype) REFERENCES eglc_petitiontype_master(id);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_lc_reportstatus FOREIGN KEY (id_reportstatus) REFERENCES eglc_reportstatus(id);



ALTER TABLE ONLY eglc_appeal
    ADD CONSTRAINT fk_lcappeal_judimpl FOREIGN KEY (id_judgmentimpl) REFERENCES eglc_judgmentimpl(id);



ALTER TABLE ONLY eglc_appeal
    ADD CONSTRAINT fk_lcappeal_status FOREIGN KEY (id_status) REFERENCES egw_status(id);



ALTER TABLE ONLY eglc_contempt
    ADD CONSTRAINT fk_lccontempt_judimpl FOREIGN KEY (id_judgmentimpl) REFERENCES eglc_judgmentimpl(id);



ALTER TABLE ONLY eglc_vacatestay_petition
    ADD CONSTRAINT fk_lcinterimorder FOREIGN KEY (id_lcinterimorder) REFERENCES eglc_lcinterimorder(id);



ALTER TABLE ONLY eglc_legalcase_batchcase
    ADD CONSTRAINT fk_legalcase FOREIGN KEY (id_legalcase) REFERENCES eglc_legalcase(id);



ALTER TABLE ONLY eglc_legalcase
    ADD CONSTRAINT fk_legalcase_court FOREIGN KEY (id_court) REFERENCES eglc_court_master(id);



ALTER TABLE ONLY eglc_legalcase_advocate
    ADD CONSTRAINT fk_legalcaseid_pk FOREIGN KEY (id_legalcase) REFERENCES eglc_legalcase(id);



ALTER TABLE ONLY eglems_tender
    ADD CONSTRAINT fk_lemstender_egwstatus FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY eglems_tender
    ADD CONSTRAINT fk_lemstender_status FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY eglems_tenderdetail
    ADD CONSTRAINT fk_lemstender_tdrdetl FOREIGN KEY (tenderid) REFERENCES eglems_tender(id);



ALTER TABLE ONLY eg_location_ipmap
    ADD CONSTRAINT fk_location_id FOREIGN KEY (locationid) REFERENCES eg_location(id);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_action FOREIGN KEY (action_) REFERENCES jbpm_action(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_childtoken FOREIGN KEY (child_) REFERENCES jbpm_token(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_destnode FOREIGN KEY (destinationnode_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_newbytes FOREIGN KEY (newbytearray_) REFERENCES jbpm_bytearray(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_node FOREIGN KEY (node_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_oldbytes FOREIGN KEY (oldbytearray_) REFERENCES jbpm_bytearray(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_parent FOREIGN KEY (parent_) REFERENCES jbpm_log(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_sourcenode FOREIGN KEY (sourcenode_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_swiminst FOREIGN KEY (swimlaneinstance_) REFERENCES jbpm_swimlaneinstance(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_taskinst FOREIGN KEY (taskinstance_) REFERENCES jbpm_taskinstance(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_token FOREIGN KEY (token_) REFERENCES jbpm_token(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_transition FOREIGN KEY (transition_) REFERENCES jbpm_transition(id_);



ALTER TABLE ONLY jbpm_log
    ADD CONSTRAINT fk_log_varinst FOREIGN KEY (variableinstance_) REFERENCES jbpm_variableinstance(id_);



ALTER TABLE ONLY eg_login_log
    ADD CONSTRAINT fk_loginlocid FOREIGN KEY (locationid) REFERENCES eg_location(id);



ALTER TABLE ONLY eg_login_log
    ADD CONSTRAINT fk_loginuserid FOREIGN KEY (userid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_usercounter_map
    ADD CONSTRAINT fk_mapcounterid FOREIGN KEY (counterid) REFERENCES eg_location(id);



ALTER TABLE ONLY eg_usercounter_map
    ADD CONSTRAINT fk_mapuserid FOREIGN KEY (userid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_marketrate
    ADD CONSTRAINT fk_marketrate_sor FOREIGN KEY (scheduleofrate_id) REFERENCES egw_scheduleofrate(id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mb_billreg FOREIGN KEY (billregister_id) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mb_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mb_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_mb_details
    ADD CONSTRAINT fk_mbd_mbh FOREIGN KEY (mbheader_id) REFERENCES egw_mb_header(id);



ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT fk_mbd_pbi FOREIGN KEY (paidbyid) REFERENCES eg_user(id_user);



ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT fk_mbd_pvh FOREIGN KEY (payvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egw_mb_details
    ADD CONSTRAINT fk_mbd_usr1 FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_mb_details
    ADD CONSTRAINT fk_mbd_usr2 FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY miscbilldetail
    ADD CONSTRAINT fk_mbd_vh FOREIGN KEY (billvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egw_mb_details
    ADD CONSTRAINT fk_mbd_woact FOREIGN KEY (wo_activity_id) REFERENCES egw_work_order_activity(id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mbh_emp FOREIGN KEY (prepared_by) REFERENCES eg_employee(id);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mbh_usr1 FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mbh_usr2 FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_mb_header
    ADD CONSTRAINT fk_mbh_wr FOREIGN KEY (workorder_id) REFERENCES egw_work_order(id);



ALTER TABLE ONLY egw_milestone_template
    ADD CONSTRAINT fk_milestome_stateid FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_milestone
    ADD CONSTRAINT fk_milestome_status_id FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_milestone_template
    ADD CONSTRAINT fk_milestone_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_milestone
    ADD CONSTRAINT fk_milestone_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egw_milestone_template
    ADD CONSTRAINT fk_milestone_stw FOREIGN KEY (subtype_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_milestone_template
    ADD CONSTRAINT fk_milestone_tw FOREIGN KEY (worktype_id) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY egw_milestone
    ADD CONSTRAINT fk_milestone_wo_est_id FOREIGN KEY (workorder_est_id) REFERENCES egw_workorder_estimate(id);



ALTER TABLE ONLY eglems_modeofacquisition
    ADD CONSTRAINT fk_modaq_landtype FOREIGN KEY (landtype) REFERENCES eglems_landtype(id);



ALTER TABLE ONLY jbpm_moduledefinition
    ADD CONSTRAINT fk_moddef_procdef FOREIGN KEY (processdefinition_) REFERENCES jbpm_processdefinition(id_);



ALTER TABLE ONLY eglems_modeofacquisition
    ADD CONSTRAINT fk_modeofacq_usr1 FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eglems_modeofacquisition
    ADD CONSTRAINT fk_modeofacq_usr2 FOREIGN KEY (modifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY eg_object_history
    ADD CONSTRAINT fk_modified_by FOREIGN KEY (modifed_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY jbpm_moduleinstance
    ADD CONSTRAINT fk_modinst_prcinst FOREIGN KEY (processinstance_) REFERENCES jbpm_processinstance(id_);



ALTER TABLE ONLY eg_action
    ADD CONSTRAINT fk_module_id FOREIGN KEY (module_id) REFERENCES eg_module(id_module);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_monthlypen_depr_id FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_monthlypen_do_id FOREIGN KEY (drawingoffice_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_monthlypen_func_id FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_monthlypen_fund_id FOREIGN KEY (fund_id) REFERENCES fund(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_monthpen_bank FOREIGN KEY (id_bank) REFERENCES bank(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_monthpen_bankbranch FOREIGN KEY (id_bankbranch) REFERENCES bankbranch(id);



ALTER TABLE ONLY egtl_installed_motor
    ADD CONSTRAINT fk_motor_trade FOREIGN KEY (trade_id) REFERENCES egtl_trade(id);



ALTER TABLE ONLY egfms_marchout_header
    ADD CONSTRAINT fk_mout_created FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egfms_marchout_header
    ADD CONSTRAINT fk_mout_modified FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egfms_marchout_details
    ADD CONSTRAINT fk_moutdtl_dept FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egfms_marchout_details
    ADD CONSTRAINT fk_moutdtl_header FOREIGN KEY (marchout_headerid) REFERENCES egfms_marchout_header(id);



ALTER TABLE ONLY egfms_marchout_details
    ADD CONSTRAINT fk_moutdtl_modified FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egfms_marchout_details
    ADD CONSTRAINT fk_moutdtl_vehcategory FOREIGN KEY (veh_category) REFERENCES egfms_vehicle_category(id);



ALTER TABLE ONLY egfms_marchout_otherdetails
    ADD CONSTRAINT fk_moutodtl_mh FOREIGN KEY (marchout_headerid) REFERENCES egfms_marchout_header(id);



ALTER TABLE ONLY egfms_marchout_otherdetails
    ADD CONSTRAINT fk_moutodtl_veh FOREIGN KEY (vehicleid) REFERENCES egfms_vehicle_header(id);



ALTER TABLE ONLY egfms_marchout_vehicles
    ADD CONSTRAINT fk_moutvh_det FOREIGN KEY (marchout_detailid) REFERENCES egfms_marchout_details(id);



ALTER TABLE ONLY egfms_marchout_vehicles
    ADD CONSTRAINT fk_moutvh_modified FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egfms_marchout_vehicles
    ADD CONSTRAINT fk_moutvh_veh FOREIGN KEY (vehicleid) REFERENCES egfms_vehicle_header(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_accountcode FOREIGN KEY (accountcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_dept FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_fld FOREIGN KEY (fieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_function FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_fund FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_istat FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_luser FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_projectcode FOREIGN KEY (projectcodeid) REFERENCES egw_projectcode(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_purpose FOREIGN KEY (purpose) REFERENCES egstores_txntypes(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mr_user FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_accountcode FOREIGN KEY (accountcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_fld FOREIGN KEY (fieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_function FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_fund FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egf_mrinheader
    ADD CONSTRAINT fk_mrin_mr FOREIGN KEY (mrheaderid) REFERENCES egf_mrheader(id);



ALTER TABLE ONLY egf_mrinline
    ADD CONSTRAINT fk_mrinl_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_mrline
    ADD CONSTRAINT fk_mrl_item FOREIGN KEY (itemid) REFERENCES eg_item(id);



ALTER TABLE ONLY egf_mrline
    ADD CONSTRAINT fk_mrl_mr FOREIGN KEY (mrheaderid) REFERENCES egf_mrheader(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mrl_str FOREIGN KEY (fromstoreid) REFERENCES eg_stores(id);



ALTER TABLE ONLY egf_mrheader
    ADD CONSTRAINT fk_mrl_str1 FOREIGN KEY (tostoreid) REFERENCES eg_stores(id);



ALTER TABLE ONLY egf_mrline
    ADD CONSTRAINT fk_mrl_sup FOREIGN KEY (relationid) REFERENCES relation(id);



ALTER TABLE ONLY egf_mrline
    ADD CONSTRAINT fk_mrl_uom FOREIGN KEY (mruom) REFERENCES eg_uom(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_accountcode FOREIGN KEY (accountcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_br FOREIGN KEY (billid) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_fd FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_fld FOREIGN KEY (fieldid) REFERENCES eg_boundary(id_bndry);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_fs FOREIGN KEY (fundsourceid) REFERENCES fundsource(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_function FOREIGN KEY (functionid) REFERENCES function(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_functionary FOREIGN KEY (functionaryid) REFERENCES functionary(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_sh FOREIGN KEY (schemeid) REFERENCES scheme(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrn_ssh FOREIGN KEY (subschemeid) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egf_mrnline
    ADD CONSTRAINT fk_mrnl_poline FOREIGN KEY (polineid) REFERENCES egstores_poline(id);



ALTER TABLE ONLY egf_mrnline
    ADD CONSTRAINT fk_mrnl_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY egf_mrnline
    ADD CONSTRAINT fk_mrnline_asset FOREIGN KEY (assetid) REFERENCES eg_asset(id);



ALTER TABLE ONLY egf_issuedfrommrn
    ADD CONSTRAINT fk_mrnlineother FOREIGN KEY (mrnlineotherid) REFERENCES egf_mrnlineothers(id);



ALTER TABLE ONLY egf_mrnlineothers
    ADD CONSTRAINT fk_mrnlo_mrnl FOREIGN KEY (mrnlineid) REFERENCES egf_mrnline(id);



ALTER TABLE ONLY egf_mrnlineothers
    ADD CONSTRAINT fk_mrnlobin_binsid FOREIGN KEY (binid) REFERENCES eg_bins(id);



ALTER TABLE ONLY egstores_mrnpodetails
    ADD CONSTRAINT fk_mrnpo_mrnline FOREIGN KEY (mrnlineid) REFERENCES egf_mrnline(id);



ALTER TABLE ONLY egstores_mrnpodetails
    ADD CONSTRAINT fk_mrnpo_poline FOREIGN KEY (polineid) REFERENCES egstores_poline(id);



ALTER TABLE ONLY egf_mrnheader
    ADD CONSTRAINT fk_mrnpurchasetype_txntype FOREIGN KEY (purchasetype) REFERENCES egstores_txntypes(id);



ALTER TABLE ONLY egpt_property_detail
    ADD CONSTRAINT fk_mutaion_master FOREIGN KEY (id_mutation) REFERENCES egpt_mutation_master(id_mutation);



ALTER TABLE ONLY egpt_mutation_owner
    ADD CONSTRAINT fk_mutation_owner_ownerid FOREIGN KEY (ownerid) REFERENCES eg_citizen(citizenid);



ALTER TABLE ONLY egpt_property_effective_period
    ADD CONSTRAINT fk_new_effective_date FOREIGN KEY (new_effective_date) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY jbpm_node
    ADD CONSTRAINT fk_node_action FOREIGN KEY (action_) REFERENCES jbpm_action(id_);



ALTER TABLE ONLY jbpm_node
    ADD CONSTRAINT fk_node_procdef FOREIGN KEY (processdefinition_) REFERENCES jbpm_processdefinition(id_);



ALTER TABLE ONLY jbpm_node
    ADD CONSTRAINT fk_node_script FOREIGN KEY (script_) REFERENCES jbpm_action(id_);



ALTER TABLE ONLY jbpm_node
    ADD CONSTRAINT fk_node_superstate FOREIGN KEY (superstate_) REFERENCES jbpm_node(id_);



ALTER TABLE ONLY egpen_pensioner_nominee
    ADD CONSTRAINT fk_nom_pen_header FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egeis_nominee_master
    ADD CONSTRAINT fk_nominee_relation FOREIGN KEY (id_relation_type) REFERENCES egeis_relation_type(id);



ALTER TABLE ONLY egpt_notice100
    ADD CONSTRAINT fk_notice100_inst_master FOREIGN KEY (id_installment) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egdms_notify_file_grp_mapping
    ADD CONSTRAINT fk_notification_file FOREIGN KEY (file_id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY egdms_notify_file_grp_mapping
    ADD CONSTRAINT fk_notification_group FOREIGN KEY (group_id) REFERENCES egdms_notification_group(id);



ALTER TABLE ONLY egdms_notification
    ADD CONSTRAINT fk_notify_file_id FOREIGN KEY (file_id) REFERENCES egdms_generic_file(id);



ALTER TABLE ONLY egdms_notification_file
    ADD CONSTRAINT fk_notify_sender FOREIGN KEY (sender) REFERENCES egdms_extnl_user(id);



ALTER TABLE ONLY egdms_notification
    ADD CONSTRAINT fk_notify_user FOREIGN KEY (position_id) REFERENCES eg_position(id);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT fk_ns_pi FOREIGN KEY (approved_by) REFERENCES eg_employee(id);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT fk_ns_st FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT fk_ns_tr FOREIGN KEY (tender_response_id) REFERENCES egw_tender_response(id);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT fk_ns_usr1 FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT fk_ns_usr2 FOREIGN KEY (modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egw_negotiation_status
    ADD CONSTRAINT fk_ns_wfst FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY otherbilldetail
    ADD CONSTRAINT fk_obd_br FOREIGN KEY (billid) REFERENCES eg_billregister(id);



ALTER TABLE ONLY otherbilldetail
    ADD CONSTRAINT fk_obd_vh FOREIGN KEY (voucherheaderid) REFERENCES voucherheader(id);



ALTER TABLE ONLY otherbilldetail
    ADD CONSTRAINT fk_obd_vh1 FOREIGN KEY (payvhid) REFERENCES voucherheader(id);



ALTER TABLE ONLY eg_object_history
    ADD CONSTRAINT fk_object_type_id FOREIGN KEY (object_type_id) REFERENCES eg_object_type(id);



ALTER TABLE ONLY egw_overheadvalues
    ADD CONSTRAINT fk_oh_ohv FOREIGN KEY (overhead_id) REFERENCES egw_overhead(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_old_bdgt_grp FOREIGN KEY (old_budget_group) REFERENCES egf_budgetgroup(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_old_dep_coa FOREIGN KEY (old_dep_coa) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpt_property_effective_period
    ADD CONSTRAINT fk_old_effective_date FOREIGN KEY (old_effective_date) REFERENCES eg_installment_master(id_installment);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_old_function FOREIGN KEY (old_function) REFERENCES function(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_old_fund FOREIGN KEY (old_fund) REFERENCES fund(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_old_schm FOREIGN KEY (old_scheme) REFERENCES scheme(id);



ALTER TABLE ONLY egw_change_fd_estimate
    ADD CONSTRAINT fk_old_subschm FOREIGN KEY (old_sub_scheme) REFERENCES sub_scheme(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_old_user_dept FOREIGN KEY (old_user_dept_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eg_portal_organization
    ADD CONSTRAINT fk_orgzn_parent FOREIGN KEY (parentid) REFERENCES eg_portal_organization(id);



ALTER TABLE ONLY eglems_allottee
    ADD CONSTRAINT fk_owner_allotee FOREIGN KEY (ownerid) REFERENCES eg_citizen(citizenid);



ALTER TABLE ONLY eg_entity_history
    ADD CONSTRAINT fk_parent FOREIGN KEY (parent) REFERENCES eg_entity_history(id_history);



ALTER TABLE ONLY egdiary_activity_type
    ADD CONSTRAINT fk_parent_activity_type FOREIGN KEY (parent) REFERENCES egdiary_activity_type(id);



ALTER TABLE ONLY egw_abstractestimate
    ADD CONSTRAINT fk_parent_category FOREIGN KEY (parent_category) REFERENCES egw_typeofwork(id);



ALTER TABLE ONLY eg_location
    ADD CONSTRAINT fk_parentid FOREIGN KEY (locationid) REFERENCES eg_location(id);



ALTER TABLE ONLY egpay_payscale_header
    ADD CONSTRAINT fk_pay_fixed_id FOREIGN KEY (pay_fixed_id) REFERENCES egeis_pay_fixed_in_mstr(pay_fixed_in_id);



ALTER TABLE ONLY eg_employee
    ADD CONSTRAINT fk_pay_fixed_in_id FOREIGN KEY (pay_fixed_in_id) REFERENCES egeis_pay_fixed_in_mstr(pay_fixed_in_id);



ALTER TABLE ONLY egpay_payscale_employee
    ADD CONSTRAINT fk_pay_header_id FOREIGN KEY (id_payheader) REFERENCES egpay_payscale_header(id);



ALTER TABLE ONLY egpay_paygenrulessetup_mstr
    ADD CONSTRAINT fk_paygenrules_fin_id FOREIGN KEY (id_financialyear) REFERENCES financialyear(id);



ALTER TABLE ONLY egpay_paygenrulessetup_mstr
    ADD CONSTRAINT fk_paygenrulessetup_salary_id FOREIGN KEY (salarycode_id) REFERENCES egpay_salarycodes(id);



ALTER TABLE ONLY egpay_payhead_rule
    ADD CONSTRAINT fk_payhead_rule_id_action FOREIGN KEY (id_wf_action) REFERENCES eg_wf_actions(id);



ALTER TABLE ONLY egpay_payscale_details
    ADD CONSTRAINT fk_payheader_id FOREIGN KEY (id_payheader) REFERENCES egpay_payscale_header(id);



ALTER TABLE ONLY egpay_emppayroll
    ADD CONSTRAINT fk_paytypeid FOREIGN KEY (paytype) REFERENCES egpay_emppayrolltypes(id);



ALTER TABLE ONLY egpay_salarycodes
    ADD CONSTRAINT fk_pct_basis FOREIGN KEY (pct_basis) REFERENCES egpay_salarycodes(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_addbasic_valmap FOREIGN KEY (additionbasic_valmap_id) REFERENCES egpen_additionbasic_valmap(id);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT fk_pen_adv_penhead_id FOREIGN KEY (id_pensionerheader) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_deductions
    ADD CONSTRAINT fk_pen_adv_scheduler FOREIGN KEY (id_advance_scheduler) REFERENCES egpen_advance_schedule(id);



ALTER TABLE ONLY egpen_pensioner_bank_details
    ADD CONSTRAINT fk_pen_bank_id FOREIGN KEY (id_bank) REFERENCES bank(id);



ALTER TABLE ONLY egpen_pensioner_bank_details
    ADD CONSTRAINT fk_pen_branch_id FOREIGN KEY (id_branch) REFERENCES bankbranch(id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_catrgory_id FOREIGN KEY (categoryid) REFERENCES egpen_category_master(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_comp_id FOREIGN KEY (component_id) REFERENCES egpen_basic_components(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_created_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_deathgratuity_details
    ADD CONSTRAINT fk_pen_deathgrat_empnominee FOREIGN KEY (id_empnominee) REFERENCES egeis_nominee_master(id);



ALTER TABLE ONLY egpen_deathgratuity_details
    ADD CONSTRAINT fk_pen_deathgrat_grat_id FOREIGN KEY (id_gratuity) REFERENCES egpen_gratuity(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_dept_id FOREIGN KEY (department_id) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_desig_id FOREIGN KEY (designation_id) REFERENCES eg_designation(designationid);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_disb_typ_id FOREIGN KEY (disbursement_type_id) REFERENCES eg_disbursement_mode(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_do_id FOREIGN KEY (drawingoffice_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_emp_id FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_func_id FOREIGN KEY (functionary_id) REFERENCES functionary(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_grade_id FOREIGN KEY (grade_id) REFERENCES egeis_grade_mstr(grade_id);



ALTER TABLE ONLY egpen_family_pensioner_header
    ADD CONSTRAINT fk_pen_header_id FOREIGN KEY (pensionerheader_id) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_pension_change_history
    ADD CONSTRAINT fk_pen_history_created_by FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pension_change_history
    ADD CONSTRAINT fk_pen_history_modified_by FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pension_change_history
    ADD CONSTRAINT fk_pen_history_pen_id FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_deductions
    ADD CONSTRAINT fk_pen_id_accountcode FOREIGN KEY (id_accountcode) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_pen_id_billreg FOREIGN KEY (id_bill_register) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_pen_id_billregister FOREIGN KEY (id_billregister) REFERENCES eg_billregister(id);



ALTER TABLE ONLY egpen_gratuity
    ADD CONSTRAINT fk_pen_id_wf_state FOREIGN KEY (id_wf_state) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_modified_id_user FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pensioner_nominee
    ADD CONSTRAINT fk_pen_nom_id FOREIGN KEY (id_nominee) REFERENCES egeis_nominee_master(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_pay_commission FOREIGN KEY (pay_commission_id) REFERENCES egpen_pay_commission(id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_payheads_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_payheads_glcode FOREIGN KEY (glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_payheads_id_wf_action FOREIGN KEY (id_wf_action) REFERENCES eg_wf_actions(id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_payheads_lastmodifidby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_payheads_tds_id FOREIGN KEY (tds_id) REFERENCES tds(id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_pct_basis FOREIGN KEY (pct_basis) REFERENCES egpen_payheads(id);



ALTER TABLE ONLY egpen_payheads
    ADD CONSTRAINT fk_pen_sal_interest_glcodeid FOREIGN KEY (interest_glcodeid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_pen_state_id FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_pen_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_pensioner_header
    ADD CONSTRAINT fk_pen_status_id FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT fk_penadv_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT fk_penadv_lastmodifidby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT fk_penadv_payhead FOREIGN KEY (id_payhead) REFERENCES egpen_payheads(id);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT fk_penadv_status FOREIGN KEY (status) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_advance
    ADD CONSTRAINT fk_penadv_year FOREIGN KEY (year) REFERENCES financialyear(id);



ALTER TABLE ONLY egpen_advance_schedule
    ADD CONSTRAINT fk_penadvsch_createdby FOREIGN KEY (createdby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_advance_schedule
    ADD CONSTRAINT fk_penadvsch_lastmodifidby FOREIGN KEY (lastmodifiedby) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_advance_schedule
    ADD CONSTRAINT fk_penadvsch_penadv FOREIGN KEY (id_advance) REFERENCES egpen_advance(id);



ALTER TABLE ONLY egpen_pensioner_bank_details
    ADD CONSTRAINT fk_penbank_id_user FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pensioner_bank_details
    ADD CONSTRAINT fk_penbank_mod_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pensioner_details
    ADD CONSTRAINT fk_pendet_created_id_user FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pensioner_details
    ADD CONSTRAINT fk_pendet_mod_id_user FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pensioner_details
    ADD CONSTRAINT fk_pendet_pen_header FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_nominee
    ADD CONSTRAINT fk_penid_nominee FOREIGN KEY (pensioner_header_id) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT fk_pension_created_by FOREIGN KEY (created_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT fk_pension_modified_by FOREIGN KEY (last_modified_by) REFERENCES eg_user(id_user);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT fk_pension_pen_id FOREIGN KEY (id_pensioner_header) REFERENCES egpen_pensioner_header(id);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT fk_pension_state FOREIGN KEY (state_id) REFERENCES eg_wf_states(id);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT fk_pension_status FOREIGN KEY (status_id) REFERENCES egw_status(id);



ALTER TABLE ONLY egpen_pension
    ADD CONSTRAINT fk_pension_type FOREIGN KEY (type) REFERENCES egpen_type_master(id);



ALTER TABLE ONLY egpen_monthly_pension
    ADD CONSTRAINT fk_pensiontype FOREIGN KEY (pensiontype) REFERENCES egpen_pension_types(id);



ALTER TABLE ONLY egeis_person_address
    ADD CONSTRAINT fk_per_id FOREIGN KEY (id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egeis_probation
    ADD CONSTRAINT fk_personal_information FOREIGN KEY (id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egpay_pfheader
    ADD CONSTRAINT fk_pfhdr_accid FOREIGN KEY (pfaccountid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpay_pfheader
    ADD CONSTRAINT fk_pfhdr_expaccid FOREIGN KEY (pfintexpaccountid) REFERENCES chartofaccounts(id);



ALTER TABLE ONLY egpay_pfheader
    ADD CONSTRAINT fk_pfheader_id_action FOREIGN KEY (id_wf_action) REFERENCES eg_wf_actions(id);



ALTER TABLE ONLY egpay_pfheader
    ADD CONSTRAINT fk_pfheader_tds_id FOREIGN KEY (tds_id) REFERENCES tds(id);



ALTER TABLE ONLY egpay_pftrigger_detail
    ADD CONSTRAINT fk_pftriggerdtl_year_id FOREIGN KEY (financialyearid) REFERENCES financialyear(id);






ALTER TABLE ONLY eggr_roles_department
    ADD CONSTRAINT fk_pgr_deptid FOREIGN KEY (departmentid) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY eggr_roles_department
    ADD CONSTRAINT fk_pgr_roleid FOREIGN KEY (roleid) REFERENCES eg_roles(id_role);



ALTER TABLE ONLY paymentheader
    ADD CONSTRAINT fk_ph_doid FOREIGN KEY (drawingofficer_id) REFERENCES eg_drawingofficer(id);



ALTER TABLE ONLY egeis_employee_grade
    ADD CONSTRAINT fk_pimsemp FOREIGN KEY (emp_id) REFERENCES eg_employee(id);



ALTER TABLE ONLY egstores_poline
    ADD CONSTRAINT fk_po_asset_category FOREIGN KEY (assetcategoryid) REFERENCES eg_assetcategory(id);



ALTER TABLE ONLY egstores_poheader
    ADD CONSTRAINT fk_po_stat FOREIGN KEY (statusid) REFERENCES egw_status(id);



ALTER TABLE ONLY egstores_poheader
    ADD CONSTRAINT fk_poh_deliverydept FOREIGN KEY (deliverydept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egstores_poheader
    ADD CONSTRAINT fk_poh_purchasedpt FOREIGN KEY (purchasedept) REFERENCES eg_department(id_dept);



ALTER TABLE ONLY egstores_poheader
    ADD CONSTRAINT fk_poheader_fd FOREIGN KEY (fundid) REFERENCES fund(id);



ALTER TABLE ONLY egstores_poheader