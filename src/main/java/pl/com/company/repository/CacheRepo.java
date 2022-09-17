package pl.com.company.repository;

import pl.com.company.model.Entity;

import java.util.List;

public interface CacheRepo<T extends Entity> {
    void loadAll(List<T> dataList);
    List<T> getAll();
}
