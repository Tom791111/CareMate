package dao.impl;
import dao.CallLogDao;
import model.CrudRecord;
public class CallLogDaoImpl extends GenericCrudDao<CrudRecord> implements CallLogDao {
    public CallLogDaoImpl(){ super("call_log", "call_id", CrudRecord.class); }
}
