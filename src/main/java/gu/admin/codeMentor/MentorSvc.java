package gu.admin.codeMentor;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import gu.common.SearchVO;

@Service
public class MentorSvc {

    @Autowired
    private SqlSessionTemplate sqlSession;    
    @Autowired
    private DataSourceTransactionManager txManager;

    static final Logger LOGGER = LoggerFactory.getLogger(MentorSvc.class);
    
    public Integer selectCodeCount(SearchVO param) {
        return sqlSession.selectOne("selectCodeCount", param);
    }
    
    public List<?> selectCodeList(SearchVO param) {
        return sqlSession.selectList("selectCodeList", param);
    }
    
    /**
     * 글 저장.
     */
    public void insertCode(String codeFormType, MentorVO param) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = txManager.getTransaction(def);
        
        try {
            if ("U".equals(codeFormType)) {
                sqlSession.update("updateCode", param);
            } else {
                sqlSession.insert("insertCode", param);
            }
            txManager.commit(status);
        } catch (TransactionException ex) {
            txManager.rollback(status);
            LOGGER.error("insertCode");
        }            
    }
 
    public MentorVO selectCodeOne(MentorVO param) {
        return sqlSession.selectOne("selectCodeOne", param);
    }

    public void deleteCodeOne(MentorVO param) {
        sqlSession.delete("deleteCodeOne", param);
    }

}
