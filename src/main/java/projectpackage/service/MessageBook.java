package projectpackage.service;

public interface MessageBook {
    String WRONG_UPDATE_ID = "Cannot update with wrong id!";
    String WRONG_DELETED_ID = "Cannot delete entity with inadequate id!";
    String WRONG_RESTORED_ID = "Cannot restore user with inadequate id!";
    String DISCREPANCY_PARENT_ID = "Discrepancy parent id with current id!";
    String WRONG_DATES = "Date cannot be set on 1 year forward! Start date cannot be later then finish date!";
    String DELETED_OBJECT_NOT_EXISTS = "Cannot execute delete operation because deleted entity does not exists! Check your request!";
    String RESTORED_USER_NOT_EXISTS = "Cannot execute restore operation because restored user does not exists! Check your request!";
    String WRONG_PHONE_NUMBER = "Cannot insert or update incorrect phone number!";
    String WRONG_FIELD = "The required field cannot be empty or null for insert and update operations!";
    String EMPTY_ROOM_NOT_FOUND = "The room of this type is not available on current date.";
    String ORDER_CREATED = "The order has been confirmed. The message with requisites was sent to your email.";
    String ORDER_CANCELED = "The order has been canceled.";
    String DUPLICATE_EMAIL = "This email is already exist!";
    String STAT_WRONG = "Unable to create statistics!";
    String FAILED_EMAIL_SENT = "Your message haven't been sent!";
    String ON_ENTITY_REFERENCE = "Cannot delete entity because someone have reference on this entity!";
    String NULL_ID = "Id cannot be null!";
    String INCORRECT_NUMBER_OF_RESIDENTS = "Number of residents cannot be less then 1 and more then 3!";
    String INVALID_PASSWORD = "Password is null, can not encrypt null password!";
    String PDF_ERROR = "Error with creating pdf document!";
    String SAME_PASSWORDS = "Passwords cannot be same!";
    String NULL_ENTITY = "Entity cannot be null!";
    String NOT_ADMIN = "This operation available only for Admin!";
    String PHONE_NOT_BELONG_TO_USER = "This phone not belong to user!";
    String TRY_TO_DELETE_LAST_PHONE = "User must have at least one phone!";
    String NEED_TO_AUTH = "For this operation user must be authorized!";
    String WRONG_PASSWORD = "Wrong password!";
    String FAIL_CRYPT_PASSWORD = "Fail crypt password!";
    String NOT_RECEPTION_OR_ADMIN = "This operation available for reception or admin!";
    String ROOM_NOT_AVAILABLE = "Current room not available for reservation!";
    String EMPTY_DTO_IN_SESSION = "Current room not available for reservation!";
    String ORDER_STARTED = "Cannot delete order which was started!";
    String WRONG_RATE_DATES = "Rate dates cannot be set more then 1 year forward or before! Start date cannot be later then finish date!";
    String ORDER_DOESNT_BELONG_USER = "This order doesn't belong user!";
    String CANNOT_HAVE_DUPLICATE_COMPLIMENTARY = "This category already has this complimentary!";
    String INVALID_USER_ID = "User id and session user id not equals!";
    String INVALID_USER = "This user does not exists in DB!";
    String EMPTY_ENTITY_STRING = "String cannot be empty!";
}


