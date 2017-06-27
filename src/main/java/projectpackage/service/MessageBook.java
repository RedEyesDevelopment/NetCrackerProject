package projectpackage.service;

/**
 * Created by Arizel on 24.06.2017.
 */
public interface MessageBook {
    String WRONG_UPDATE_ID = "Cannot update with wrong id! Updated entity id not equals original entity id, but id cannot be changed!";
    String WRONG_DELETED_ID = "Cannot delete entity with inadequate id!";
    String DISCREPANCY_PARENT_ID = "Discrepancy parent id with current id!";
    String WRONG_DATES = "Date cannot be set on 1 year forward! Start date cannot be later then finish date!";
    String DELETED_OBJECT_NOT_EXISTS = "Cannot execute delete operation because deleted entity does not exists! Check your request!";
    String WRONG_PHONE_NUMBER = "Cannot insert or update incorrect phone number!";
    String WRONG_FIELD = "The required field cannot be empty or null for insert and update operations!";
    String EMPTY_ROOM_NOT_FOUND = "The room of this type is not available on current date.";
    String ORDER_CREATED = "The order has been confirmed. The message with requisites was sent to your email.";
    String ORDER_CANCELED = "The order has been canceled.";
    String DUPLICATE_EMAIL = "This email is already exist!";
    String STAT_WRONG = "Unable to create statistics!";
    String FAILED_EMAIL_SENT = "Your message haven't been sent!";
    String ON_ENTITY_REFERENCE = "Cannot delete entity with reference on self!";
    String NULL_ID = "Id cannot be null!";
    String INCORRECT_NUMBER_OF_RESIDENTS = "Number of residents cannot be less then 1 and more then 3!";
    String WRONG_PASSWORD = "Password is null, can not encrypt null password!";
    String PDF_ERROR = "Error with creating pdf document!";
    String SAME_PASSWORDS = "Passwords cannot be same!";
    String NULL_ENTITY = "Entity cannot be null!";
}
