package projectpackage.repository.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import projectpackage.repository.support.rowmappers.ParentIdRowMapper;

@Repository
public class ParentDAOImpl implements ParentsDAO {
    private static final String QUERY = "SELECT OB.PARENT_ID \"PARENT\" FROM OBJECTS OB WHERE OB.OBJECT_ID=";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    @Transactional(readOnly = true)
    public Integer getParentId(Integer targetId) {
        return (Integer) jdbcTemplate.query(QUERY+targetId,new ParentIdRowMapper()).get(0);
    }
}
