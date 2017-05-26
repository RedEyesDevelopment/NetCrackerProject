//package projectpackage.components;
//
//import org.springframework.dao.DataAccessException;
//import org.springframework.stereotype.Component;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.Locale;
//
//@Component
//public class VerticalDatabaseMessageSource extends CustomDatabaseMessageSource {
//    private static final String CODETABLEFROMDATABASE = "IM_CODE";
//    private static final String LABELTABLEFROMDATABASE = "IM_LOCALE";
//    private static final String MESSAGETABLEFROMDATABASE = "IM_DATA";
//    private static final String TABLENAMEFROMDATABASE = "INTERNATIONAL_MESSAGES";
//
//    private static final String I18N_QUERY = "select "+CODETABLEFROMDATABASE+", "+LABELTABLEFROMDATABASE+", "+MESSAGETABLEFROMDATABASE+" from "+TABLENAMEFROMDATABASE;
//
//    @Override
//    protected String getI18NSqlQuery() {
//        return I18N_QUERY;
//    }
//
//    @Override
//    protected Messages extractI18NData(ResultSet rs) throws SQLException, DataAccessException {
//
//        Messages messages = new Messages();
//        while (rs.next()) {
//            String localeString = rs.getString(LABELTABLEFROMDATABASE);
//            Locale locale = new Locale.Builder().setLanguage(localeString).setRegion(localeString.toUpperCase()).build();
//            messages.addMessage(rs.getString(CODETABLEFROMDATABASE), locale, rs.getString(MESSAGETABLEFROMDATABASE));
//        }
//        return messages;
//    }
//}