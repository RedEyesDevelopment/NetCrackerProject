package projectpackage.repository.support.daoexceptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sergey on 21.05.2017.
 */
public class ReferenceBreakException extends RuntimeException {
    private String message;

    private List<String> referringEntities = new ArrayList<String>();

    public ReferenceBreakException(String[] arrayReferringEntities) {
        for (int i = 0; i < arrayReferringEntities.length; i++) {
            this.referringEntities.add(arrayReferringEntities[i]);
        }
        setDefaultMessage();
    }

    public List<String> getReferringEntities() {
        return referringEntities;
    }

    private void setDefaultMessage() {
        String string = "";
        for (String referringEntity : referringEntities) {
            string += referringEntity + " and ";
        }
        this.message = string.substring(0, string.length() - 5);
    }

    @Override
    public String getMessage() {
        return message;
    }
}
