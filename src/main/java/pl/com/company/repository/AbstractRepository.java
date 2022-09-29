package pl.com.company.repository;

import pl.com.company.model.Entity;
import pl.com.company.visitor.Visitable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractRepository<T extends Entity> implements Repository<T>, Visitable<T> {

    private List<T> entites = new ArrayList<>();

    @Override
    public List<T> get(String pesel) {
        return this.entites.stream()
                .filter(entity -> entity.getPesel().equals(pesel))
                .collect(Collectors.toList());
    }

    @Override
    public T create(T entity) {
        this.entites.add(entity);
        return entity;
    }

    @Override
    public boolean delete(String pesel) {
        return this.entites.removeIf(entity -> entity.getPesel().equals(pesel));
    }

    @Override
    public void loadAll(List<T> dataList) {
        this.entites = dataList;
    }

    @Override
    public List<T> getAll() {
        return new ArrayList<>(this.entites);
    }

    @Override
    public void deleteAll() {
        this.entites.clear();
    }

    @Override
    public int size() {
        return this.entites.size();
    }
}
