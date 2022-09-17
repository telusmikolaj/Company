package pl.com.company.visitor;

import pl.com.company.model.Entity;
import pl.com.company.repository.CacheRepo;
import pl.com.company.repository.Repository;

import java.io.IOException;

public interface Visitable<T extends Entity> extends CacheRepo<T>  {

    default void accept(Visitor visitor) throws IOException {
        visitor.visit(this);
    }


}
