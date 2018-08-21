
CREATE TABLE tenant (
	id int unsigned auto_increment primary key,
	tenantName varchar(255) not null,
	activeTenantContractId int,
	alertEmailAddress varchar(255) not null
	
)


CREATE TABLE tenantContract (
	id int unsigned auto_increment primary key,
	tenantId int unsigned,
	apiKey varchar(255) not null,
	startdate datetime,
	enddate   datetime,
	status    tinyint(1),
	dailyThreshold int,
	yearlyThreshold int,
	outOfYearlyCredits tinyint(1),
	
	foreign key fk_tenant(tenantId)
    REFERENCES tenant(id)
    on delete restrict
			
)

CREATE TABLE distanceCache (
	id int unsigned auto_increment primary key,
	originPostcode varchar(255) not null,
	destinationPostcode varchar(255) not null,
	travelDistanceMetres bigint,
	travelTimeSeconds bigint,
	timestamp datetime
)


CREATE TABLE transaction (
	id int unsigned auto_increment primary key,
	tenantId int unsigned,
	contractId int unsigned,
	elementUsed int,
	timestamp datetime,
	
	foreign key fk_tenantId(tenantId)
    REFERENCES tenant(id)
    on delete restrict
)


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



insert into tenant (tenantName, apiKey, activeTenantContractId, alertEmailAddress)
values('test', 'AIzaSyAG85u75B-uXYy_exa4jIW10wFMM4MpUzA', 1, 'lichao.shen@pshealth.co.uk');

insert into tenantcontract(tenantId, startDate, endDate, dailyThreshold, yearlyThreshold, OutOfYearlyCredits) 
values(1, now(), now(), 10000, 10000, 0 );




update distancecache set timestamp='2011-12-26 14:48:36'
where id=4
 



