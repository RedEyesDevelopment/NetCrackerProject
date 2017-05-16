@Override
    public List<Phone> getPhonesList() {
        String sql = "SELECT PHONESUSER.PARENT_ID \"userId\", PHONES.VALUE \"phoneNumber\"\n" +
                "FROM ATTRIBUTES PHONES, OBJECTS PHONESUSER\n" +
                "WHERE PHONES.ATTR_ID=38 AND\n" +
                "PHONESUSER.OBJECT_TYPE_ID=9 AND\n" +
                "PHONESUSER.OBJECT_ID = PHONES.OBJECT_ID";
        List<Phone> list = namedParameterJdbcTemplate.query(sql, new BeanPropertyRowMapper<Phone>(Phone.class));
        return list;
    }