CREATE DEFINER=`root`@`localhost` PROCEDURE `cronTrackQuota`(in inDate date, out emailList varchar(100) )
BEGIN
declare done            int default 0;
declare tenantId        int;
declare contractId      int;
declare elementsUsed    int default 0;
declare dailyThreshold  int;
declare yearlyThreshold bigint;
declare yearlyElesUsed  bigint;
declare dailyStart   	datetime;
declare contractStart   date;


declare curs cursor for (select tenant.id as tenantId, tenant.activeTenantContractId, tenantcontract.dailyThreshold, tenantcontract.yearlyThreshold, tenantcontract.startDate, tenant.alertEmailAddress
    from tenant, tenantcontract where tenant.activeTenantContractId = tenantcontract.id and tenantcontract.status=1);
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1; 	
    
	set dailyStart = TIMESTAMP(inDate,'05:00:00');
        
	open curs;    
    loop_label: loop
    
		if  done = 1 then
            leave loop_label;
        end if;
        
		fetch curs into tenantId, contractId, dailyThreshold, yearlyThreshold, contractStart, emailAddress;
        
        insert ignore into credithistory
		set credithistory.tenantId = tenantId, credithistory.tenantContractId=contractId, historyDate=inDate, dailyElementsUsed=0, outofDailyCredits=false, yearlyElementsUsed=0;
		
        select sum(elementUsed) into elementsUsed from transaction
        where transaction.tenantId = tenantId and transaction.contractId = contractId and timestamp between dailyStart and date_add(dailyStart, interval 1 day);
        
        SELECT concat('elementsUsed is ', elementsUsed);
        
        update credithistory set dailyElementsUsed = elementsUsed
        where credithistory.tenantId = tenantId and credithistory.tenantContractId = contractId and credithistory.historyDate = inDate;
        
        if (elementsUsed >= dailyThreshold) then
			
            update credithistory set outofDailyCredits = 1
			where credithistory.tenantId = tenantId and credithistory.tenantContractId = contractId and credithistory.historyDate = inDate;
            
			update credithistory set yearlyElementsUsed = elementsUsed - dailyThreshold
			where credithistory.tenantId = tenantId and credithistory.tenantContractId = contractId and credithistory.historyDate = inDate;
            			
        end if; 
        
		select sum(yearlyElementsUsed) into yearlyElesUsed from credithistory 
        where credithistory.tenantId = tenantId and credithistory.tenantContractId = contractId and historyDate between contractStart and inDate; 

		if (yearlyElesUsed >=yearlyThreshold) then
         
			concat(emailList, " ", emailAddress);
			update tenantContract set OutofYearlyCredits = 1
            where tenantcontract.tenantId = tenantId and tenantcontract.id = contractId;
            
        end if;
        
		 
	end loop loop_label;
    
    close curs;


end