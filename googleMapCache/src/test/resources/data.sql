
insert into tenant (tenantName, apiKey, activeTenantContractId, alertEmailAddress)
values('test', 'AIzaSyAG85u75B-uXYy_exa4jIW10wFMM4MpUzA', 1, 'test@pshealth.co.uk');

insert into tenantcontract(tenantId, startDate, endDate, dailyThreshold, status, yearlyThreshold, OutOfYearlyCredits) 
values(1,  date_sub(curdate(), interval 10 day) , date_add(curdate(), interval 10 day), 3, 1, 5, 0 );


insert into distancecache(originPostcode, destinationPostcode, travelDistanceMetres, travelTimeSeconds, timestamp)
values('Sn1 2ed','AB31 5TQ', 784694, 29955, now());
insert into distancecache(originPostcode, destinationPostcode, travelDistanceMetres, travelTimeSeconds, timestamp)
values('Sn1 2ed','AB45 1FQ', 875474, 34386, now());
insert into distancecache(originPostcode, destinationPostcode, travelDistanceMetres, travelTimeSeconds, timestamp)
values('Sn1 2ed','IP1 2QA', 271837, 10705, now());
insert into distancecache(originPostcode, destinationPostcode, travelDistanceMetres, travelTimeSeconds, timestamp)
values('Sn1 2ed','DE65 6YB', 198319, 7856, now());
insert into distancecache(originPostcode, destinationPostcode, travelDistanceMetres, travelTimeSeconds, timestamp)
values('Sn1 2ed','LA12 7BT', 389248, 15253, date_sub(curdate(), interval 370 day));
