package projectpackage.repository;

import projectpackage.dto.IUDAnswer;

/**
 * Created by Arizel on 23.06.2017.
 */
public interface Rollbackable {
    IUDAnswer rollback(Integer id, String message, Exception e);
    IUDAnswer rollback(String message, Exception e);
}
