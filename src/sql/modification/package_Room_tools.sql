CREATE OR REPLACE PACKAGE Room_tools
IS
	TYPE row_object_id IS RECORD(object_id NUMBER(20));
	TYPE table_object_id IS TABLE OF row_object_id;
	TYPE rate_rec_type IS RECORD (	obj_id NUMBER(20),
									begin_date DATE,
									end_date DATE,
									rate_for_day NUMBER);

	FUNCTION get_free_rooms
		(in_room_type_obj_id IN NUMBER,
		 in_number_of_residents IN NUMBER,
		 in_date_start IN DATE,
	 	 in_date_finish IN DATE)
	RETURN table_object_id PIPELINED;


	FUNCTION get_free_room_types
		(in_number_of_residents IN NUMBER,
		 in_date_start IN DATE,
	 	 in_date_finish IN DATE)
	RETURN table_object_id PIPELINED;


	FUNCTION get_cost_living
		(in_room_type_obj_id IN NUMBER,
		 in_number_of_residents IN NUMBER,
		 in_date_start IN DATE,
	 	 in_date_finish IN DATE)
	RETURN NUMBER;

END Room_tools;
/

CREATE OR REPLACE PACKAGE BODY Room_tools
IS
	-- возвращает все свободные комнаты
	-- OBJECT_ID типа которой равен in_room_type_obj_id,
	-- с количеством человек равным in_number_of_residents,
	-- на период с in_date_start до in_date_finish
	FUNCTION get_free_rooms
		(in_room_type_obj_id IN NUMBER,
		 in_number_of_residents IN NUMBER,
		 in_date_start IN DATE,
	 	 in_date_finish IN DATE)
	RETURN table_object_id PIPELINED
	IS
		CURSOR Rooms IS
			SELECT Rooms.OBJECT_ID AS object_id
			FROM OBJECTS Rooms,
				 ATTRIBUTES Number_of_residents,
				 OBJREFERENCE Has_room_type
			WHERE 	Rooms.OBJECT_TYPE_ID = 1
				AND Number_of_residents.ATTR_ID = 2 AND Rooms.OBJECT_ID = Number_of_residents.OBJECT_ID AND Number_of_residents.VALUE = in_number_of_residents
				AND Has_room_type.ATTR_ID = 4 AND Rooms.OBJECT_ID = Has_room_type.OBJECT_ID AND Has_room_type.REFERENCE = in_room_type_obj_id;
		Orders SYS_REFCURSOR;
		Order_rec NUMBER(20);
		bad_orders NUMBER;
		bad_blocks NUMBER;
	BEGIN
		FOR Room_rec IN Rooms
		LOOP
			bad_orders := 0;
			bad_blocks := 0;

			SELECT COUNT(Orders.OBJECT_ID) INTO bad_orders
			FROM OBJECTS Orders,
				 ATTRIBUTES L_start, ATTRIBUTES L_finish,
				 OBJREFERENCE Booked
			WHERE	Orders.OBJECT_TYPE_ID = 2
				AND L_start.ATTR_ID = 11 AND Orders.OBJECT_ID = L_start.OBJECT_ID
				AND L_finish.ATTR_ID = 12 AND Orders.OBJECT_ID = L_finish.OBJECT_ID
				AND Booked.ATTR_ID = 6 AND Orders.OBJECT_ID = Booked.OBJECT_ID AND Booked.REFERENCE = Room_rec.OBJECT_ID
				AND NOT (L_start.DATE_VALUE > in_date_finish OR L_finish.DATE_VALUE < in_date_start);

			SELECT COUNT(Blocks.OBJECT_ID) INTO bad_blocks
			FROM OBJECTS Blocks,
				 ATTRIBUTES B_start, ATTRIBUTES B_finish,
				 OBJREFERENCE Blocked
			WHERE	Blocks.OBJECT_TYPE_ID = 8
				AND B_start.ATTR_ID = 35 AND Blocks.OBJECT_ID = B_start.OBJECT_ID
				AND B_finish.ATTR_ID = 36 AND Blocks.OBJECT_ID = B_finish.OBJECT_ID
				AND Blocked.ATTR_ID = 34 AND Blocks.OBJECT_ID = Blocked.OBJECT_ID AND Blocked.REFERENCE = Room_rec.OBJECT_ID
				AND NOT (B_start.DATE_VALUE > in_date_finish OR B_finish.DATE_VALUE < in_date_start);

			IF ((bad_orders + bad_blocks) = 0)
				THEN pipe row(Room_rec);
			END IF;
		END LOOP;
	END get_free_rooms;


	-- возвращает все типы комнат, для которых есть хоть одна свободная комната
	-- с количеством человек равным in_number_of_residents,
	-- на период с in_date_start до in_date_finish
	FUNCTION get_free_room_types
		(in_number_of_residents IN NUMBER,
		 in_date_start IN DATE,
	 	 in_date_finish IN DATE)
	RETURN table_object_id PIPELINED
	IS
		CURSOR Room_types IS
			SELECT Room_types.OBJECT_ID
			FROM OBJECTS Room_types
			WHERE Room_types.OBJECT_TYPE_ID = 5;
		number_of_free_rooms NUMBER;
	BEGIN
		FOR Room_types_rec IN Room_types LOOP
			number_of_free_rooms := 0;

			SELECT COUNT(*) INTO number_of_free_rooms
				FROM TABLE(ROOM_TOOLS.GET_FREE_ROOMS(Room_types_rec.OBJECT_ID, in_number_of_residents, in_date_start, in_date_finish));

			IF (number_of_free_rooms > 0)
				THEN pipe row(Room_types_rec);
			END IF;

		END LOOP;
	END get_free_room_types;

	-- возвращает стоимость за ПРОЖИВАНИЕ
	-- в комнате OBJECT_ID типа которой равен in_room_type_obj_id,
	-- с количеством человек равным in_number_of_residents,
	-- на период с in_date_start до in_date_finish
	-- учитывая все рейты, которые находятся внутри этого периода
	FUNCTION get_cost_living
		(in_room_type_obj_id IN NUMBER,
		 in_number_of_residents IN NUMBER,
		 in_date_start IN DATE,
	 	 in_date_finish IN DATE)
	RETURN NUMBER
	IS
		start_rate rate_rec_type;
		finish_rate rate_rec_type;
		cost_inner_rate NUMBER;
	BEGIN
		SELECT Rate.OBJECT_ID, begin_date.DATE_VALUE, end_date.DATE_VALUE, rate_for_day.VALUE INTO start_rate
		FROM OBJECTS Rate, OBJECTS Price,
			 ATTRIBUTES begin_date, ATTRIBUTES end_date, ATTRIBUTES number_of_people, ATTRIBUTES rate_for_day
		WHERE 	Rate.OBJECT_TYPE_ID = 6 AND Rate.PARENT_ID = in_room_type_obj_id
			AND Price.OBJECT_TYPE_ID = 7
			AND Price.PARENT_ID = Rate.OBJECT_ID
			AND begin_date.ATTR_ID = 30 AND Rate.OBJECT_ID = begin_date.OBJECT_ID
			AND end_date.ATTR_ID = 31 AND Rate.OBJECT_ID = end_date.OBJECT_ID
			AND number_of_people.ATTR_ID = 32 AND Price.OBJECT_ID = number_of_people.OBJECT_ID
			AND rate_for_day.ATTR_ID = 33 AND Price.OBJECT_ID = rate_for_day.OBJECT_ID
			AND begin_date.DATE_VALUE <= in_date_start AND end_date.DATE_VALUE >= in_date_start
			AND number_of_people.VALUE = in_number_of_residents;

		SELECT Rate.OBJECT_ID, begin_date.DATE_VALUE, end_date.DATE_VALUE, rate_for_day.VALUE INTO finish_rate
		FROM OBJECTS Rate, OBJECTS Price,
			 ATTRIBUTES begin_date, ATTRIBUTES end_date, ATTRIBUTES number_of_people, ATTRIBUTES rate_for_day
		WHERE 	Rate.OBJECT_TYPE_ID = 6 AND Rate.PARENT_ID = in_room_type_obj_id
			AND Price.OBJECT_TYPE_ID = 7
			AND Price.PARENT_ID = Rate.OBJECT_ID
			AND begin_date.ATTR_ID = 30 AND Rate.OBJECT_ID = begin_date.OBJECT_ID
			AND end_date.ATTR_ID = 31 AND Rate.OBJECT_ID = end_date.OBJECT_ID
			AND number_of_people.ATTR_ID = 32 AND Price.OBJECT_ID = number_of_people.OBJECT_ID
			AND rate_for_day.ATTR_ID = 33 AND Price.OBJECT_ID = rate_for_day.OBJECT_ID
			AND begin_date.DATE_VALUE <= in_date_finish AND end_date.DATE_VALUE >= in_date_finish
			AND number_of_people.VALUE = in_number_of_residents;

		IF (start_rate.obj_id = finish_rate.obj_id)
		THEN
			RETURN (	(TRUNC(in_date_finish) - TRUNC(in_date_start) + 1) * start_rate.rate_for_day 	);
		ELSE
			IF (TRUNC(start_rate.end_date) + 1 = TRUNC(finish_rate.begin_date))
			THEN
				RETURN
					(	(TRUNC(start_rate.end_date) - TRUNC(in_date_start) + 1) * start_rate.rate_for_day
						+
						(TRUNC(in_date_finish) - TRUNC(finish_rate.begin_date) + 1) * finish_rate.Rate_for_day 	);
			ELSE
				SELECT SUM((TRUNC(end_date.DATE_VALUE) - TRUNC(begin_date.DATE_VALUE) + 1) * rate_for_day.VALUE) INTO cost_inner_rate
				FROM OBJECTS Rate, OBJECTS Price,
					 ATTRIBUTES begin_date, ATTRIBUTES end_date, ATTRIBUTES number_of_people, ATTRIBUTES rate_for_day
				WHERE 	Rate.OBJECT_TYPE_ID = 6 AND Rate.PARENT_ID = in_room_type_obj_id
					AND Price.OBJECT_TYPE_ID = 7
					AND Price.PARENT_ID = Rate.OBJECT_ID
					AND begin_date.ATTR_ID = 30 AND Rate.OBJECT_ID = begin_date.OBJECT_ID
					AND end_date.ATTR_ID = 31 AND Rate.OBJECT_ID = end_date.OBJECT_ID
					AND number_of_people.ATTR_ID = 32 AND Price.OBJECT_ID = number_of_people.OBJECT_ID
					AND rate_for_day.ATTR_ID = 33 AND Price.OBJECT_ID = rate_for_day.OBJECT_ID
					AND number_of_people.VALUE = in_number_of_residents
					AND (in_date_start < begin_date.DATE_VALUE AND end_date.DATE_VALUE < in_date_finish);
				RETURN
					(	(TRUNC(start_rate.end_date) - TRUNC(in_date_start) + 1) * start_rate.rate_for_day
						+
						cost_inner_rate
						+
						(TRUNC(in_date_finish) - TRUNC(finish_rate.begin_date) + 1) * finish_rate.Rate_for_day 	);
			END IF;
		END IF;
	END get_cost_living;

END Room_tools;
/