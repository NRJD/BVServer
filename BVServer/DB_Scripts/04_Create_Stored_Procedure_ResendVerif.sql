
DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_updateVerifCodes`(

				IN emailId varchar(50) 
				,IN emailVerifCode varchar(32) 
                ,IN mobVerifCode int(6) )
BEGIN

declare	emailInDB varchar(50);
declare	acctVerified int(1);
declare statusCode int(4);

set @statusCode = -1;
set @acctVerified = -1;

	SELECT ACCT_VERIFIED into @acctVerified FROM bv.user_login where email_id  = emailId;

    IF @acctVerified = -1 THEN
		SET @statusCode = 3035 ; -- Email not registered.
        
    ELSE IF @acctVerified = 0 THEN
    
		BEGIN
			
            UPDATE 
				user_login 
			SET 
				EMAIL_VERIF_CODE = emailVerifCode,
				MOBILE_VERIF_CODE = mobVerifCode  
			WHERE 
				EMAIL_ID=emailId;
                
			SET @statusCode = 3033 ; -- Reset Codes success
            
		END;
        
	ELSE
    
		SET @statusCode = 3011 ; -- Account Already Verified
	
    END IF;
    END IF;
    
    SELECT @statusCode as 'STATUS_FROM_DB';

END$$
DELIMITER ;


