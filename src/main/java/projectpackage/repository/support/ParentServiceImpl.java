package projectpackage.repository.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import projectpackage.repository.support.rowmappers.ParentIdRowMapper;

@Service
public class ParentServiceImpl implements ParentsService{
    private static final String QUERY = "SELECT OB.PARENT_ID \"PARENT\" FROM OBJECTS OB WHERE OB.OBJECT_ID=";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer getParentId(Integer targetId) {
        return (Integer) jdbcTemplate.query(QUERY+targetId,new ParentIdRowMapper()).get(0);
    }
}
