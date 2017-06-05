CREATE OR REPLACE PACKAGE Rate_tools
IS
	TYPE rate_rec_type IS RECORD (	obj_id NUMBER(20),
									begin_date DATE,
									end_date DATE);

	TYPE price_rec_type IS RECORD (	obj_id NUMBER(20),
									price NUMBER);

	TYPE rates IS TABLE OF rate_rec_type;


	FUNCTION new_rate (in_price1_price IN NUMBER,
						in_price2_price IN NUMBER,
						in_price3_price IN NUMBER,

						in_room_type_id IN NUMBER,
						in_new_rate_start IN DATE,
						in_new_rate_finish IN DATE)
	RETURN NUMBER;


	PROCEDURE clone_rate_on_another_date (	in_rate_obj_id NUMBER,
											in_rate_parent NUMBER,
											in_date_start IN DATE,
											in_date_finish IN DATE);


	FUNCTION insert_rate_with_prices (	in_price1_price IN NUMBER,
									   	in_price2_price IN NUMBER,
									   	in_price3_price IN NUMBER,

										in_room_type_id IN NUMBER,
										in_new_rate_start IN DATE,
										in_new_rate_finish IN DATE)
	RETURN NUMBER;


	PROCEDURE update_rate (	obj_id IN NUMBER,
							in_room_type_id IN NUMBER,
							in_new_rate_start IN DATE,
							in_new_rate_finish IN DATE);


	PROCEDURE delete_rate (obj_id IN NUMBER);


END Rate_tools;
/

CREATE OR REPLACE PACKAGE BODY Rate_tools
IS
	FUNCTION new_rate
		(in_price1_price IN NUMBER,
		 in_price2_price IN NUMBER,
		 in_price3_price IN NUMBER,

		 in_room_type_id IN NUMBER,
		 in_new_rate_start IN DATE,
		 in_new_rate_finish IN DATE)
	RETURN NUMBER
	IS
		cursor_rates SYS_REFCURSOR;
		rate rate_rec_type;
		stmt VARCHAR(600) := '
			SELECT Rates.OBJECT_ID, r_start.DATE_VALUE, r_finish.DATE_VALUE
			FROM OBJECTS Rates,
				ATTRIBUTES r_start, ATTRIBUTES r_finish
			WHERE 	Rates.OBJECT_TYPE_ID = 6
				AND Rates.PARENT_ID = :1
				AND r_start.ATTR_ID = 30 AND r_start.OBJECT_ID = Rates.OBJECT_ID
				AND r_finish.ATTR_ID = 31 AND r_finish.OBJECT_ID = Rates.OBJECT_ID
				AND NOT (r_start.DATE_VALUE > :2 OR r_finish.DATE_VALUE < :3)';
		inserted_rate_id NUMBER(20);
	BEGIN
		OPEN cursor_rates FOR stmt USING in_room_type_id, in_new_rate_finish, in_new_rate_start;
		FETCH cursor_rates INTO rate;

		IF cursor_rates%FOUND
		THEN
			WHILE cursor_rates%FOUND LOOP
				-- 2		3		4		5		6
				-- ____|_______________________|_____

							    ---------
				IF (rate.begin_date < in_new_rate_start AND in_new_rate_finish < rate.end_date)
				THEN
					update_rate(rate.obj_id, in_room_type_id, rate.begin_date, (in_new_rate_start - 1));
					clone_rate_on_another_date(rate.obj_id, in_room_type_id, (in_new_rate_finish + 1), rate.end_date);
				END IF;

							--------------------
							--------------------------
				IF (rate.begin_date < in_new_rate_start AND rate.end_date <= in_new_rate_finish)
				THEN
					update_rate(rate.obj_id, in_room_type_id, rate.begin_date, (in_new_rate_start - 1));
				END IF;

						--------------------
					------------------------
				IF (in_new_rate_start <= rate.begin_date AND in_new_rate_finish < rate.end_date)
				THEN
					update_rate(rate.obj_id, in_room_type_id, (in_new_rate_finish + 1), rate.end_date);
				END IF;

						-------------------------
						--------------------------------
				---------------------------------
				----------------------------------------
				IF (in_new_rate_start <= rate.begin_date AND rate.end_date <= in_new_rate_finish)
				THEN
					delete_rate(rate.obj_id);
				END IF;

				FETCH cursor_rates INTO rate;
			END LOOP;
		END IF;

		inserted_rate_id := Rate_tools.insert_rate_with_prices(	in_price1_price, in_price2_price, in_price3_price,
																in_room_type_id,
																in_new_rate_start,
																in_new_rate_finish);
		CLOSE cursor_rates;
		RETURN inserted_rate_id;
	END new_rate;

	-- клонирует рейс с его прайсами на другие даты
	PROCEDURE clone_rate_on_another_date (	in_rate_obj_id NUMBER,
											in_rate_parent NUMBER,
											in_date_start IN DATE,
											in_date_finish IN DATE)
	IS
		rate_id NUMBER;
		price price_rec_type;
	BEGIN
		SELECT SEQ_OBJ_ID.NEXTVAL INTO rate_id FROM DUAL;
		INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID) VALUES (rate_id, in_rate_parent, 6);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,DATE_VALUE) VALUES (30, rate_id, in_date_start);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,DATE_VALUE) VALUES (31, rate_id, in_date_finish);

		FOR i IN 1..3 LOOP
			SELECT SEQ_OBJ_ID.NEXTVAL, Rate.VALUE INTO price
			FROM OBJECTS Price, ATTRIBUTES Rate, ATTRIBUTES People
			WHERE 	Price.OBJECT_TYPE_ID = 7
				AND Price.PARENT_ID = in_rate_obj_id
				AND Rate.ATTR_ID = 33 AND Rate.OBJECT_ID = Price.OBJECT_ID
				AND People.ATTR_ID = 32 AND Price.OBJECT_ID = People.OBJECT_ID AND People.VALUE = i;

			INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID) VALUES (price.obj_id, rate_id, 7);
			INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (32, price.obj_id, i);
			INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (33, price.obj_id, price.price);
		END LOOP;

	END clone_rate_on_another_date;

	-- инсёртит рейт вместе с прайсами
	FUNCTION insert_rate_with_prices ( in_price1_price IN NUMBER,
									   	in_price2_price IN NUMBER,
									   	in_price3_price IN NUMBER,

										in_room_type_id IN NUMBER,
										in_new_rate_start IN DATE,
										in_new_rate_finish IN DATE)
	RETURN NUMBER
	IS
		rate_id NUMBER;
		price_id NUMBER;
	BEGIN
		SELECT SEQ_OBJ_ID.NEXTVAL INTO rate_id FROM DUAL;
		INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID) VALUES (rate_id, in_room_type_id, 6);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,DATE_VALUE) VALUES (30, rate_id, in_new_rate_start);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,DATE_VALUE) VALUES (31, rate_id, in_new_rate_finish);

		SELECT SEQ_OBJ_ID.NEXTVAL INTO price_id FROM DUAL;
		INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID) VALUES (price_id, rate_id, 7);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (32, price_id, 1);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (33, price_id, in_price1_price);

		SELECT SEQ_OBJ_ID.NEXTVAL INTO price_id FROM DUAL;
		INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID) VALUES (price_id, rate_id, 7);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (32, price_id, 2);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (33, price_id, in_price2_price);

		SELECT SEQ_OBJ_ID.NEXTVAL INTO price_id FROM DUAL;
		INSERT INTO OBJECTS (OBJECT_ID, PARENT_ID, OBJECT_TYPE_ID) VALUES (price_id, rate_id, 7);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (32, price_id, 3);
		INSERT INTO ATTRIBUTES (ATTR_ID,OBJECT_ID,VALUE) VALUES (33, price_id, in_price3_price);

		RETURN rate_id;
	END insert_rate_with_prices;

	-- изменяет существующий рейт
	PROCEDURE update_rate (	obj_id IN NUMBER,
							in_room_type_id IN NUMBER,
							in_new_rate_start IN DATE,
							in_new_rate_finish IN DATE)
	IS
	BEGIN
		UPDATE OBJECTS SET PARENT_ID = in_room_type_id WHERE OBJECT_ID = obj_id;
		UPDATE ATTRIBUTES SET DATE_VALUE = in_new_rate_start WHERE OBJECT_ID = obj_id AND ATTR_ID = 30;
		UPDATE ATTRIBUTES SET DATE_VALUE = in_new_rate_finish WHERE OBJECT_ID = obj_id AND ATTR_ID = 31;
	END update_rate;

	-- удаляет рейт
	PROCEDURE delete_rate (obj_id IN NUMBER)
	IS
		deleted_rows NUMBER;
	BEGIN
		deleted_rows := delete_object_by_id(obj_id);
	END delete_rate;

END Rate_tools;
/