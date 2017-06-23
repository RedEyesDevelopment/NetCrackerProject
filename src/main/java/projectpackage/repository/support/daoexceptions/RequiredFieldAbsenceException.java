package projectpackage.repository.support.daoexceptions;

/**
 * Created by Arizel on 21.06.2017.
 */
public class RequiredFieldAbsenceException extends RuntimeException {
    private static final String DEFAULT_MESSAGE = "Required field cannot be null!";

    public RequiredFieldAbsenceException() {
        super(DEFAULT_MESSAGE);
    }
}
