package jc.cici.android.atom.bean;

/**
 * 面授(在线,直播)卡片内容
 * Created by atom on 2017/5/10.
 */

public class CardItem {

    private int StageId; // 阶段id
    private String StageName; // 阶段名称
    private String StageType; // 阶段类型(面授，在线，直播)
    private int StageStatus; // 班级状态(0:未开始,1:正常,2:已过期)
    private String StageStartTime; // 阶段开始时间
    private String StageEndTime; // 阶段结束时间
    private int StagePeriod; // 阶段学时
    private int StageProblem; // 问题状态(0:未启用,1:已启用)
    private int StageNote; // 笔记状态(0:未启用,1:已启用)
    private int StageInformation; // 资料状态(0:未启用,1:已启用)

    public int getStageId() {
        return StageId;
    }

    public void setStageId(int stageId) {
        StageId = stageId;
    }

    public String getStageName() {
        return StageName;
    }

    public void setStageName(String stageName) {
        StageName = stageName;
    }

    public String getStageType() {
        return StageType;
    }

    public void setStageType(String stageType) {
        StageType = stageType;
    }

    public int getStageStatus() {
        return StageStatus;
    }

    public void setStageStatus(int stageStatus) {
        StageStatus = stageStatus;
    }

    public String getStageStartTime() {
        return StageStartTime;
    }

    public void setStageStartTime(String stageStartTime) {
        StageStartTime = stageStartTime;
    }

    public String getStageEndTime() {
        return StageEndTime;
    }

    public void setStageEndTime(String stageEndTime) {
        StageEndTime = stageEndTime;
    }

    public int getStagePeriod() {
        return StagePeriod;
    }

    public void setStagePeriod(int stagePeriod) {
        StagePeriod = stagePeriod;
    }

    public int getStageProblem() {
        return StageProblem;
    }

    public void setStageProblem(int stageProblem) {
        StageProblem = stageProblem;
    }

    public int getStageNote() {
        return StageNote;
    }

    public void setStageNote(int stageNote) {
        StageNote = stageNote;
    }

    public int getStageInformation() {
        return StageInformation;
    }

    public void setStageInformation(int stageInformation) {
        StageInformation = stageInformation;
    }
}
