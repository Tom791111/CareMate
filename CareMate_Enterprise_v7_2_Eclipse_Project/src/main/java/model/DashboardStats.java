package model;

import java.util.ArrayList;
import java.util.List;

/** 首頁 Dashboard 動態統計資料：由 MySQL 即時計算，不再使用固定假資料。 */
public class DashboardStats {
    private int completionRate;
    private int healthScore;
    private int completedItems;
    private int totalItems;
    private String summaryText;
    private String latestBloodPressure;
    private String latestTemperature;
    private String latestMedicineStatus;
    private String latestMealStatus;
    private String latestMoodStatus;
    private List<Integer> sevenDayScores = new ArrayList<>();

    public int getCompletionRate() { return completionRate; }
    public void setCompletionRate(int completionRate) { this.completionRate = completionRate; }
    public int getHealthScore() { return healthScore; }
    public void setHealthScore(int healthScore) { this.healthScore = healthScore; }
    public int getCompletedItems() { return completedItems; }
    public void setCompletedItems(int completedItems) { this.completedItems = completedItems; }
    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }
    public String getSummaryText() { return summaryText; }
    public void setSummaryText(String summaryText) { this.summaryText = summaryText; }
    public String getLatestBloodPressure() { return latestBloodPressure; }
    public void setLatestBloodPressure(String latestBloodPressure) { this.latestBloodPressure = latestBloodPressure; }
    public String getLatestTemperature() { return latestTemperature; }
    public void setLatestTemperature(String latestTemperature) { this.latestTemperature = latestTemperature; }
    public String getLatestMedicineStatus() { return latestMedicineStatus; }
    public void setLatestMedicineStatus(String latestMedicineStatus) { this.latestMedicineStatus = latestMedicineStatus; }
    public String getLatestMealStatus() { return latestMealStatus; }
    public void setLatestMealStatus(String latestMealStatus) { this.latestMealStatus = latestMealStatus; }
    public String getLatestMoodStatus() { return latestMoodStatus; }
    public void setLatestMoodStatus(String latestMoodStatus) { this.latestMoodStatus = latestMoodStatus; }
    public List<Integer> getSevenDayScores() { return sevenDayScores; }
    public void setSevenDayScores(List<Integer> sevenDayScores) { this.sevenDayScores = sevenDayScores; }
}
