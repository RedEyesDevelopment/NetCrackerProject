package projectpackage.repository.daoexceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 21.05.2017.
 */
public class ReferenceBreakException extends Exception {

    private List<String> referringEntities = new ArrayList<String>();

    public ReferenceBreakException(String[] arrayReferringEntities) {
        for (int i = 0; i < arrayReferringEntities.length; i++) {
            this.referringEntities.add(arrayReferringEntities[i]);
        }
    }

    public List<String> getReferringEntities() {
        return referringEntities;
    }

    public String printReferencesEntities() {
        String string = "";
        for (String referringEntity : referringEntities) {
            string += referringEntity + " and ";
        }
        return string.substring(0, string.length() - 5);
    }
}
