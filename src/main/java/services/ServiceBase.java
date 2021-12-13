package services;

import javax.persistence.EntityManager;

import utils.DBUtil;

public class ServiceBase {

    protected EntityManager em = DBUtil.createEntityManager();

    public void close() {
        if (em.isOpen()) {
            em.close();
        }
    }

}
