package pl.edu.agh.kiro.latex_plagiat.server.database;

public class DaoFactory {

    static public Dao createDao() {
        return new DaoImp();
    }

    static public Dao createDaoToTest() {
        return new DaoImpToTests();
    }

}
