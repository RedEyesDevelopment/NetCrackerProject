@Override
    public Map<Integer,User> getAllUsers(String orderParameter) {
        String sqlQuery = "SELECT USERS.OBJECT_ID \"objectId\", USERLOG.VALUE \"email\", USERPASS.VALUE \"password\",\n" +
                "USERFIN.VALUE \"firstName\", USERLAN.VALUE \"lastName\", USERAD.VALUE \"additionalInfo\",\n" +
                "USEROLE.VALUE \"role\"\n" +
                "FROM OBJECTS USERS, ATTRIBUTES USERLOG, ATTRIBUTES USERPASS,\n" +
                "ATTRIBUTES USERFIN, ATTRIBUTES USERLAN, ATTRIBUTES USERAD, ATTRIBUTES USEROLE,\n" +
                "OBJREFERENCE ROLEREF\n" +
                "WHERE USERS.OBJECT_TYPE_ID=3 AND\n" +
                "    USERLOG.ATTR_ID=15 AND\n" +
                "    USERPASS.ATTR_ID=16 AND\n" +
                "    USERFIN.ATTR_ID=17 AND\n" +
                "    USERLAN.ATTR_ID=18 AND\n" +
                "    USERAD.ATTR_ID=19 AND\n" +
                "    USEROLE.ATTR_ID=39 AND\n" +
                "    ROLEREF.ATTR_ID=20 AND\n" +
                "    ROLEREF.OBJECT_ID=USERS.OBJECT_ID AND\n" +
                "    ROLEREF.REFERENCE=USEROLE.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERLOG.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERPASS.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERFIN.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERLAN.OBJECT_ID AND\n" +
                "    USERS.OBJECT_ID=USERAD.OBJECT_ID\n" +
                "    ORDER BY :orderParameter";
        SqlParameterSource namedParameters = new MapSqlParameterSource("orderParameter", orderParameter);
        List<User> list = namedParameterJdbcTemplate.query(sqlQuery,namedParameters, new BeanPropertyRowMapper<User>(User.class));
        TreeMap<Integer,User> map = new TreeMap<>();
        for (User user: list){
            map.put(user.getObjectId(), user);
        }
    return map;
    }