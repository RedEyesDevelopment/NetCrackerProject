package projectpackage.repository.reacdao.querying;

import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import projectpackage.repository.reacdao.ReacTask;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class ReactQueryTaskHolder {
    private ReacTask node;
    private String query;
    private SqlParameterSource source;

    public ReactQueryTaskHolder(ReacTask node, String query, SqlParameterSource source) {
        this.node = node;
        this.query = query;
        this.source = source;
    }

    public ReacTask getNode() {
        return node;
    }

    public String getQuery() {
        return query;
    }

    public SqlParameterSource getSource() {
        return source;
    }

}
