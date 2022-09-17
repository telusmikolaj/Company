package pl.com.company.repository;

import pl.com.company.model.Entity;
import java.util.List;

public interface Repository<T extends Entity> {

    List<T> get(String pesel);

    T create(T entity);

    boolean delete(String pesel);

    void deleteAll();

    int size();
}
