package service.impl;
import service.CallLogService;
import dao.impl.CallLogDaoImpl;
public class CallLogServiceImpl extends GenericCrudService implements CallLogService {
    public CallLogServiceImpl(){ super(new CallLogDaoImpl()); }
}
