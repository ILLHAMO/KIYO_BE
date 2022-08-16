package project.kiyobackend.common;

import com.google.common.base.CaseFormat;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DatabaseCleanup implements InitializingBean {


    @PersistenceContext
    private EntityManager em;



    private List<String> tableNames;

    @Override
    public void afterPropertiesSet() throws Exception {
//       tableNames = em.getMetamodel().getEntities().stream()
//                .filter(e -> e.getJavaType().getAnnotation(Entity.class) != null)
//                .map(e -> CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE,e.getName()))
//                .collect(Collectors.toList());
    }

    @Transactional
    public void truncateAllEntity() {
        em.flush();
        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();

            em.createNativeQuery("TRUNCATE TABLE " + "STORE").executeUpdate();

        em.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
    }
}
