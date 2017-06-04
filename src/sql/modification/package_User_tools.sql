CREATE OR REPLACE PACKAGE User_tools
IS

	PROCEDURE check_email_for_dupl
		(in_email VARCHAR2);

END User_tools;



CREATE OR REPLACE PACKAGE BODY User_tools
IS

	PROCEDURE check_email_for_dupl
		(in_email VARCHAR2)
	IS
		num_of_email NUMBER(1);
	BEGIN
		SELECT COUNT(email.OBJECT_ID) INTO num_of_email
		FROM ATTRIBUTES email
		WHERE email.VALUE = in_email;

		IF (num_of_email > 0)
		THEN
			RAISE_APPLICATION_ERROR(-20002, 'Duplicate email!');
		END IF;
	END check_email_for_dupl;

END User_tools;