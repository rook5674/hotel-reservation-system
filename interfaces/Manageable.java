package interfaces;

public interface Manageable<T> {
    boolean  create(T item) throws Exception;
    T read(int id);
    boolean delete(int id) throws Exception;
}
