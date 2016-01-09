DELIMITER $$
CREATE DEFINER=`root`@`localhost` PROCEDURE `usp_user_register`(
				  	
				  IN userName varchar(50) 
                  ,IN emailId varchar(50) 
                  ,IN pwd varchar(512) 
                  ,IN mobNumber varchar(10) 
                  ,IN lang varchar(15) 
                  ,IN emailVerifCode varchar(32) 
                  ,IN mobVerifCode int(6)
                  ,IN cCode varchar(3)
                  
                )
BEGIN

declare	emailInDB varchar(50);
declare	acctVerified int(1);
declare statusCode int(4);


set @emailInDB = null;
set @acctVerified = 0;

	SELECT email_id, ACCT_VERIFIED into @emailInDB, @acctVerified FROM bv.user_login where email_id  = emailId;
    
    -- select emailId, @emailInDB,  @acctVerified;
    
    IF @emailInDB IS NOT NULL THEN
    
		IF @acctVerified = 1 THEN
			SET @statusCode = 3001;
		ELSE
			SET @statusCode = 3006;
		END IF;
        
	ELSE 
    
		INSERT INTO BV.USER_LOGIN (USER_LOGIN_ID,NAME, EMAIL_ID, PASSWORD, MOBILE_NUMBER
        , LANGUAGE, ACCT_VERIFIED, EMAIL_VERIF_CODE, MOBILE_VERIF_CODE, COUNTRY_CODE)
        VALUES (0,userName,emailId ,pwd ,mobNumber,lang ,0, emailVerifCode ,mobVerifCode,cCode  );
        
        SET @statusCode = 3000;
        
    END IF;
    
    SELECT @statusCode as 'STATUS_FROM_DB';
    
END$$
DELIMITER ;
