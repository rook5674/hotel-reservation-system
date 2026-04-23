package interfaces;

public interface Manageable<T> {
    void create(T item) throws Exception;
    T read(int id);
    void delete(int id) throws Exception;
}
