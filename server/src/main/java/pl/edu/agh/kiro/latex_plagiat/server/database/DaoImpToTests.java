package pl.edu.agh.kiro.latex_plagiat.server.database;

class DaoImpToTests extends DaoImp {

    @Override
    void createHibernateAccess() {
        hibernateAccess = new HibernateAccess("META-INF/test.hibernate.cfg.xml");
    }
}
