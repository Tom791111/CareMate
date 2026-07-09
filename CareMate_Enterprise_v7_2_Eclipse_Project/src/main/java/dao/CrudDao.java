package dao;import model.CrudRecord;import java.util.*;
public interface CrudDao<T extends CrudRecord>{void insert(T m); List<T> findAll(); T findById(int id); void update(T m); void delete(int id);}
