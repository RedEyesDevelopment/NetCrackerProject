CREATE OR REPLACE FUNCTION delete_object_by_id(obj_id IN NUMBER)
RETURN NUMBER
IS count_rows NUMBER;
BEGIN
    count_rows := 0;
    EXECUTE IMMEDIATE 'DELETE FROM OBJREFERENCE WHERE OBJECT_ID = ' || obj_id;
    count_rows := count_rows + SQL%ROWCOUNT;
    EXECUTE IMMEDIATE 'DELETE FROM ATTRIBUTES WHERE OBJECT_ID = ' || obj_id;
    count_rows := count_rows + SQL%ROWCOUNT;
    EXECUTE IMMEDIATE 'DELETE FROM OBJECTS WHERE OBJECT_ID = ' || obj_id;
    count_rows := count_rows + SQL%ROWCOUNT;
    RETURN count_rows;
END;