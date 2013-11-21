package dag;

import javax.ejb.Local;
import java.util.Collection;
import java.util.Map;

@Local
public interface BaseDao<T> {
    Object findById(long id);

    Collection<T> findByName(String name);

    Collection<T> findByNameAndAge(String name, int age);

    Collection<T> findAll();

    Collection<T> findAll(int first, int max);

    Collection<T> namedQuery(String name, Map<String, ?> params, int first, int max);

    Collection<T> namedQuery(String name, int first, int max, Map<String, ?> params);

    Collection<T> namedQuery(String name, Map<String, ?> params);

    Collection<T> namedQuery(String name);

    Collection<T> query(String value, Map<String, ?> params);

    Collection<T> getRootContent();

    void save(T u);

    void delete(T u);

    T update(T u);
}