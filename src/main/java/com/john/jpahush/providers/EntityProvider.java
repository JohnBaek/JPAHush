package com.john.jpahush.providers;

import com.john.jpahush.utils.MoreExceptionHandler;

import javax.persistence.EntityManager;

public class EntityProvider implements AutoCloseable {
    /**
     * 엔티티 매니저 
     */
    public final EntityManager entityManager;

    /**
     * 생성자
     * @param entityManager 엔티티 매니저
     */
    public EntityProvider(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /**
     * AutoCloseable 구현
     */
    @Override
    public void close()  {
        try {
          this.entityManager.close();
        } catch (Exception ex) {
            MoreExceptionHandler.Log(ex);
        }
    }
}
