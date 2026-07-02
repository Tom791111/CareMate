package service.impl;

import dao.DashboardDao;
import dao.impl.DashboardDaoImpl;
import model.DashboardStats;
import service.DashboardService;

public class DashboardServiceImpl implements DashboardService {
    private final DashboardDao dao = new DashboardDaoImpl();

    @Override
    public DashboardStats getTodayStats(int elderId) {
        return dao.getTodayStats(elderId);
    }
}
