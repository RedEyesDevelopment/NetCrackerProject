package projectpackage.repository.reacteav.querying;

import projectpackage.repository.reacteav.ReacTask;

import java.util.Map;

public class ReactQueryTaskHolder {
    private ReacTask node;
    private StringBuilder query;
    private Map<String, Object> source;

    public ReactQueryTaskHolder(ReacTask node, StringBuilder query, Map<String, Object> source) {
        this.node = node;
        this.query = query;
        this.source = source;
    }

    public ReacTask getNode() {
        return node;
    }

    public StringBuilder getQuery() {
        return query;
    }

    public Map<String, Object> getSource() {
        return source;
    }

}
