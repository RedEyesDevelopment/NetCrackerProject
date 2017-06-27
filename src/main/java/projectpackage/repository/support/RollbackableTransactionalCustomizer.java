package projectpackage.repository.support;

import com.mchange.v2.c3p0.ConnectionCustomizer;

import java.sql.Connection;

/**
 * Created by Arizel on 23.06.2017.
 */

public class RollbackableTransactionalCustomizer implements ConnectionCustomizer{
    @Override
    public void onAcquire(Connection connection, String s) throws Exception {
    }

    @Override
    public void onDestroy(Connection connection, String s) throws Exception {
    }

    @Override
    public void onCheckOut(Connection connection, String s) throws Exception {
        connection.setAutoCommit(false);
    }

    @Override
    public void onCheckIn(Connection connection, String s) throws Exception {
    }
}
