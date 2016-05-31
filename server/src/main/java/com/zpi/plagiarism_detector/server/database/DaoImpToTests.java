package com.zpi.plagiarism_detector.server.database;

class DaoImpToTests extends DaoImp {

    @Override
    void createHibernateAccess() {
        hibernateAccess = new HibernateAccess("META-INF/test.hibernate.cfg.xml");
    }
}
