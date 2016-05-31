package com.zpi.plagiarism_detector.server.database;

public class DaoFactory {

    static public Dao createDao() {
        return new DaoImp();
    }

    static public Dao createDaoToTest() {
        return new DaoImpToTests();
    }

}
