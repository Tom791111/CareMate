package service;

import model.DashboardStats;

public interface DashboardService {
    DashboardStats getTodayStats(int elderId);
}
