
CREATE TABLE tenant (
	id int unsigned auto_increment primary key,
	tenantName varchar(255) not null,
	apiKey varchar(255) not null,  // based on contract
	activeTenantContractId int,  // remove
	alertEmailAddress varchar(255) not null	
	
	
);


CREATE TABLE tenantContract (
	id int unsigned auto_increment primary key,
	tenantId int unsigned,
	startdate datetime,
	enddate   datetime,
	status    tinyint(1),
	dailyThreshold int,
	yearlyThreshold int,
	outOfYearlyCredits tinyint(1),

	foreign key fk_tenant(tenantId)
    REFERENCES tenant(id)
    on delete restrict
		
	
);

CREATE TABLE distanceCache (
	id int unsigned auto_increment primary key,
	originPostcode varchar(255) not null,  // origine and desitination address
	destinationPostcode varchar(255) not null,
	travelDistanceMetres bigint,
	travelTimeSeconds bigint,
	timestamp datetime
);


CREATE TABLE transaction (
	id int unsigned auto_increment primary key,
	tenantId int unsigned,
	contractId int unsigned,
	elementUsed int,
	timestamp datetime,
	
	foreign key fk_tenantId(tenantId)
    REFERENCES tenant(id)
    on delete restrict
);


CREATE TABLE creditHistory (   
	
	tenantId int unsigned,
	tenantContractId int unsigned,
    historyDate date,
	dailyElementsUsed int,
	outofDailyCredits tinyint,
	yearlyElementsUsed int unsigned,
	
	
	foreign key fk_tenantId(tenantId)
    REFERENCES tenant(id)
    on delete restrict,
	
	foreign key fk_tenantContractId(tenantContractId)
    REFERENCES tenantContract(id)
    on delete restrict,
	
	primary key(tenantId, tenantContractId, historyDate)
	
) 



