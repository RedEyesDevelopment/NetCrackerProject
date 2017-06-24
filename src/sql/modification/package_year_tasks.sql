BEGIN

	DBMS_SCHEDULER.CREATE_SCHEDULE (
		Schedule_name => 'YEAR_SCHEDULE',
		Start_date => to_timestamp_tz('31-12-16 1:00:00 AM Europe/Athens',
									  'DD-MM-RR HH:MI:SS PM TZR'),
		Repeat_interval => 'FREQ=YEARLY; INTERVAL=1'
	);

	DBMS_SCHEDULER.CREATE_PROGRAM (
		Program_name => 'YEAR_PROGRAM',
		Program_type => 'STORED_PROCEDURE',
		Program_action => 'HOTEL.Year_tasks.Execute_tasks',
		Enabled => TRUE
	);

	DBMS_SCHEDULER.CREATE_JOB (
		Job_name => 'YEAR_JOB',
		Program_name => 'YEAR_PROGRAM',
		Schedule_name => 'YEAR_SCHEDULE',
		Enabled => TRUE
	);

END;
/



CREATE OR REPLACE PACKAGE Year_tasks
IS

	PROCEDURE Execute_tasks;

	PROCEDURE Delete_old;

	PROCEDURE Copy_rates;

	PROCEDURE Delete_old_rates;

	PROCEDURE Delete_old_orders;

	PROCEDURE Delete_old_blocks;

	PROCEDURE Delete_old_notifs;

	PROCEDURE Delete_disabled_users;

	FUNCTION is_date_leap_year
		(d DATE)
	RETURN NUMBER;

END Year_tasks;



CREATE OR REPLACE PACKAGE BODY Year_tasks
IS

	PROCEDURE Execute_tasks
	IS
	BEGIN
		HOTEL.Year_tasks.Copy_rates();
		HOTEL.Year_tasks.Delete_old();
	END Execute_tasks;


	PROCEDURE Delete_old
	IS
	BEGIN
		HOTEL.Year_tasks.Delete_old_rates();
		HOTEL.Year_tasks.Delete_old_orders();
		HOTEL.Year_tasks.Delete_old_blocks();
		HOTEL.Year_tasks.Delete_old_notifs();
		HOTEL.Year_tasks.Delete_disabled_users();
	END Delete_old;


	PROCEDURE Copy_rates
	IS
		CURSOR rates IS
			SELECT 	Rates.OBJECT_ID AS obj_id, Rates.PARENT_ID AS room_type,
					begin_date.DATE_VALUE AS beginn, end_date.DATE_VALUE AS endd,
					price1.VALUE AS price1, price2.VALUE AS price2, price3.VALUE AS price3
			FROM 	OBJECTS Rates,
					ATTRIBUTES begin_date, ATTRIBUTES end_date,
					ATTRIBUTES price1, ATTRIBUTES price2, ATTRIBUTES price3
			WHERE	Rates.OBJECT_TYPE_ID = 6
				AND begin_date.ATTR_ID = 30 AND begin_date.OBJECT_ID = Rates.OBJECT_ID
				AND end_date.ATTR_ID = 31 AND end_date.OBJECT_ID = Rates.OBJECT_ID
				-- 	начало следующего года		add_months(trunc(SYSDATE,'YY'),12)
				-- 	конец следующего года		add_months(trunc(SYSDATE,'YY'),24)-1
				AND NOT (
							(end_date.DATE_VALUE < add_months(trunc(SYSDATE,'YY'),12))
						 OR
						 	(begin_date.DATE_VALUE > add_months(trunc(SYSDATE,'YY'),24)-1)
						)
				AND price1.ATTR_ID = 33
				AND price1.OBJECT_ID = (SELECT Price.OBJECT_ID
										FROM OBJECTS Price, ATTRIBUTES num_of_people
										WHERE 	Price.PARENT_ID = Rates.OBJECT_ID
											AND num_of_people.ATTR_ID = 32 AND num_of_people.OBJECT_ID = Price.OBJECT_ID
											AND num_of_people.VALUE = 1)
				AND price2.ATTR_ID = 33
				AND price2.OBJECT_ID = (SELECT Price.OBJECT_ID
										FROM OBJECTS Price, ATTRIBUTES num_of_people
										WHERE 	Price.PARENT_ID = Rates.OBJECT_ID
											AND num_of_people.ATTR_ID = 32 AND num_of_people.OBJECT_ID = Price.OBJECT_ID
											AND num_of_people.VALUE = 2)
				AND price3.ATTR_ID = 33
				AND price3.OBJECT_ID = (SELECT Price.OBJECT_ID
										FROM OBJECTS Price, ATTRIBUTES num_of_people
										WHERE 	Price.PARENT_ID = Rates.OBJECT_ID
											AND num_of_people.ATTR_ID = 32 AND num_of_people.OBJECT_ID = Price.OBJECT_ID
											AND num_of_people.VALUE = 3);
		idNewR NUMBER;
	BEGIN
		FOR rate_rec IN rates LOOP
			-- помнить про 29 февраля!!
			-- когда копируем в высокосный год - проверить на наличие рейта заканчив. 28.02, и если он есть, то растянуть его до 29-го
			-- когда копируем из высокосного года - проверить на наличие рейта заканчив. 29.02, и сжать его до 28.02
									 			 -- если есть рейт начинающийся 29.02, то его тоже сжать до 1.03

			-- Если тот год, в который копируем (через один от текущего) - високосный
			IF (HOTEL.Year_tasks.is_date_leap_year(add_months(trunc(SYSDATE,'YY'),24)) = 1)
			THEN
				-- если кто-то заканчивается 28-го ФЕВ, то растянуть его до 29-го ФЕВ
				IF (rate_rec.endd = trunc(SYSDATE,'YY') + 58)
				THEN
					rate_rec.endd := rate_rec.endd + 1;
				END IF;
			END IF;
			-- Если источник копирования (следующий год от текущего) - високосный год
			IF (HOTEL.Year_tasks.is_date_leap_year(add_months(trunc(SYSDATE,'YY'),12)) = 1)
			THEN
				-- если кто-то заканчивается 29-го ФЕВ, то сжать его до 28-го ФЕВ
				IF (rate_rec.endd = trunc(SYSDATE,'YY') + 59)
				THEN
					rate_rec.endd := rate_rec.endd - 1;
				END IF;
				-- если кто-то начинается 29-го ФЕВ, то сжать его до 1-го МАР
				IF (rate_rec.beginn = trunc(SYSDATE,'YY') + 59)
				THEN
					rate_rec.beginn := rate_rec.beginn + 1;
				END IF;
			END IF;

			-- сужаем до начала года, и до его конца
			IF (rate_rec.beginn < add_months(trunc(SYSDATE,'YY'),12))
			THEN
				rate_rec.beginn := add_months(trunc(SYSDATE,'YY'),12);
			END IF;

			IF (rate_rec.endd > add_months(trunc(SYSDATE,'YY'),24)-1)
			THEN
				rate_rec.endd := add_months(trunc(SYSDATE,'YY'),24)-1;
			END IF;

			-- прибавляем 1 год к датам
			rate_rec.beginn := add_months(rate_rec.beginn,12);
			rate_rec.endd := add_months(rate_rec.endd,12);

			-- инсертим нашим методом для безопасности + глубокое копирование прайсов
			idNewR := HOTEL.Rate_tools.new_rate(rate_rec.price1,
												rate_rec.price2,
												rate_rec.price3,
												rate_rec.room_type,
												rate_rec.beginn,
												rate_rec.endd);
		END LOOP;
	END Copy_rates;


	PROCEDURE Delete_old_rates
	IS
		CURSOR cur IS
			SELECT Rates.OBJECT_ID AS obj_id
			FROM OBJECTS Rates, ATTRIBUTES finish
			WHERE	 Rates.OBJECT_TYPE_ID = 6
				AND finish.ATTR_ID = 31 AND finish.OBJECT_ID = Rates.OBJECT_ID
				AND SYSDATE - finish.DATE_VALUE > 366;
		num_of_del_rows NUMBER;
	BEGIN
		FOR idRec IN cur LOOP
			num_of_del_rows := HOTEL.delete_object_by_id(idRec.obj_id);
		END LOOP;
	END Delete_old_rates;


	PROCEDURE Delete_old_orders
	IS
		CURSOR cur IS
			SELECT Orders.OBJECT_ID AS obj_id
			FROM OBJECTS Orders, ATTRIBUTES living_finish
			WHERE	 Orders.OBJECT_TYPE_ID = 2
				AND living_finish.ATTR_ID = 12 AND living_finish.OBJECT_ID = Orders.OBJECT_ID
				AND SYSDATE - living_finish.DATE_VALUE > 366;
		num_of_del_rows NUMBER;
	BEGIN
		FOR idRec IN cur LOOP
			num_of_del_rows := HOTEL.delete_object_by_id(idRec.obj_id);
		END LOOP;
	END Delete_old_orders;


	PROCEDURE Delete_old_blocks
	IS
		CURSOR cur IS
			SELECT Blocks.OBJECT_ID AS obj_id
			FROM OBJECTS Blocks, ATTRIBUTES block_finish
			WHERE	 Blocks.OBJECT_TYPE_ID = 8
				AND block_finish.ATTR_ID = 36 AND block_finish.OBJECT_ID = Blocks.OBJECT_ID
				AND SYSDATE - block_finish.DATE_VALUE > 366;
		num_of_del_rows NUMBER;
	BEGIN
		FOR idRec IN cur LOOP
			num_of_del_rows := HOTEL.delete_object_by_id(idRec.obj_id);
		END LOOP;
	END Delete_old_blocks;


	PROCEDURE Delete_old_notifs
	IS
		CURSOR cur IS
			SELECT Notif.OBJECT_ID AS obj_id
			FROM OBJECTS Notif, ATTRIBUTES exe_date
			WHERE	 Notif.OBJECT_TYPE_ID = 4
				AND exe_date.ATTR_ID = 25 AND exe_date.OBJECT_ID = Notif.OBJECT_ID
				AND SYSDATE - exe_date.DATE_VALUE > 366;
		num_of_del_rows NUMBER;
	BEGIN
		FOR idRec IN cur LOOP
			num_of_del_rows := HOTEL.delete_object_by_id(idRec.obj_id);
		END LOOP;
	END Delete_old_notifs;


	PROCEDURE Delete_disabled_users
	IS
		CURSOR cur IS
			SELECT Users.OBJECT_ID AS obj_id
			FROM OBJECTS Users, ATTRIBUTES enabled
			WHERE	 Users.OBJECT_TYPE_ID = 3
				AND enabled.ATTR_ID = 3 AND enabled.OBJECT_ID = Users.OBJECT_ID
				AND enabled.VALUE = 'false';
		ref_count NUMBER;
		num_of_del_rows NUMBER;
	BEGIN
		ref_count := 0;
		FOR idRec IN cur LOOP
			SELECT COUNT(*) INTO ref_count FROM OBJREFERENCE WHERE Reference = idRec.obj_id;
			IF (ref_count = 0)
				THEN num_of_del_rows := HOTEL.delete_object_by_id(idRec.obj_id);
			END IF;
		END LOOP;
	END Delete_disabled_users;


	FUNCTION is_date_leap_year
		(d DATE)
	RETURN NUMBER
	IS
		y NUMBER(4);
	BEGIN
		y := EXTRACT (YEAR FROM d);
		IF (MOD(y,400) = 0) THEN RETURN 1; END IF;
		IF (MOD(y,100) = 0) THEN RETURN 0; END IF;
		IF (MOD(y,4) = 0) THEN RETURN 1; END IF;
		RETURN 0;
	END is_date_leap_year;

END Year_tasks;