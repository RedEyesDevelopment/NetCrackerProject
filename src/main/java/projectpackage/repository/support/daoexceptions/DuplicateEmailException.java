package projectpackage.repository.support.daoexceptions;

/**
 * Created by Sergey on 04.06.2017.
 */
public class DuplicateEmailException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "This email is already exist - ";

    public DuplicateEmailException(String email) {
        super(DEFAULT_MESSAGE + email);
    }
}
