package com.spidertechnosoft.app.xeniamobi_v2.model.service;

import java.util.List;

/**
 * Created by sandheepgr on 12/1/16.
 */
public interface IEntityService<T> {

    public Long save(T t) ;
    public T findById(Long id);
    public List<T> findByQuery(String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit);
    public boolean delete(Long id);
    public boolean delete(T t);

}
