package projectpackage.repository.reacdao.querying;

import projectpackage.repository.reacdao.ReacTask;

import java.util.Map;

/**
 * Created by Lenovo on 08.05.2017.
 */
public class ReactQueryTaskHolder {
    private ReacTask node;
    private String query;
    private Map<String,Object> source;

    public ReactQueryTaskHolder(ReacTask node, String query, Map<String, Object> source) {
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

    public Map<String, Object> getSource() {
        return source;
    }

}
