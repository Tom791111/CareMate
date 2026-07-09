package dao;

import model.DashboardStats;

public interface DashboardDao {
    DashboardStats getTodayStats(int elderId);
}
