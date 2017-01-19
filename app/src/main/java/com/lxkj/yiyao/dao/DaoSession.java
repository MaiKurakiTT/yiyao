package com.lxkj.yiyao.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.lxkj.yiyao.db.QuestionBean;

import com.lxkj.yiyao.dao.QuestionBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig questionBeanDaoConfig;

    private final QuestionBeanDao questionBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        questionBeanDaoConfig = daoConfigMap.get(QuestionBeanDao.class).clone();
        questionBeanDaoConfig.initIdentityScope(type);

        questionBeanDao = new QuestionBeanDao(questionBeanDaoConfig, this);

        registerDao(QuestionBean.class, questionBeanDao);
    }
    
    public void clear() {
        questionBeanDaoConfig.clearIdentityScope();
    }

    public QuestionBeanDao getQuestionBeanDao() {
        return questionBeanDao;
    }

}