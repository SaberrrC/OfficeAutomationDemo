package com.shanlinjinrong.oa.ui.activity.home.workreport.bean;

import javax.inject.Named;

/**
 * Created by 丁 on 2017/8/21.
 * 发起日报实体类
 */

public class WorkReportBean {

    private String checkManId;// 检查人Id
    private String selfAppearance;// 仪容仪表
    private String selfBehavior;// 个人言行
    private String selfCommunication;// 沟通协调能力
    private String selfCooperation;// 合作性
    private String selfDedicated;//	 敬业精神
    private String selfDiscipline;// 工作纪律

    private String selfEnvironmental;//	环境卫生
    private String selfEnvironMental;//	环境卫生
    private String selfJobInitiative;//	工作主动性
    private String selfOrganization;//服从组织安排

    public String getSelfEnvironMental() {
        return selfEnvironMental;
    }

    public void setSelfEnvironMental(String selfEnvironMental) {
        this.selfEnvironMental = selfEnvironMental;
    }

    //自评
    private String selfRatingEigth;
    private String selfRatingFive;
    private String selfRatingFour;
    private String selfRatingOne;
    private String selfRatingSeven;
    private String selfRatingSix;
    private String selfRatingThree;
    private String selfRatingTwo;
    private String selfSave; // 节约

    private int supervisorId;//监督人id
    private String therecipientId;

    private long time;//提交时间
    private String tomorrowPlan;//明日计划

    //实际工作
    private String workEigth;
    private String workFive;
    private String workFour;
    private String workOne;
    private String workSeven;
    private String workSix;
    private String workThree;
    private String workTwo;

    //计划工作
    private String workPlanEigth;
    private String workPlanFive;
    private String workPlanFour;
    private String workPlanOne;
    private String workPlanSeven;
    private String workPlanSix;
    private String workPlanThree;
    private String workPlanTwo;

    //数据量化
    private String workResultOne;
    private String workResultTwo;
    private String workResultThree;
    private String workResultFour;
    private String workResultFive;
    private String workResultSix;
    private String workResultSeven;
    private String workResultEigth;

    //检查人评价
    private String checkAppearance;
    private String checkBehavior;
    private String checkCommunication;
    private String checkCooperation;
    private String checkDedicated;
    private String checkDiscipline;
    private String checkEnvironMental;
    private String checkJobInitiative;
    private String checkOrganization;
    private String checkRatingEigth;
    private String checkRatingFive;
    private String checkRatingFour;
    private String checkRatingOne;
    private String checkRatingSeven;
    private String checkRatingSix;
    private String checkRatingThree;
    private String checkRatingTwo;
    private String checkSave;

    //监督人评价
    private String supervisorAppearance;
    private String supervisorBehavior;
    private String supervisorCommunication;
    private String supervisorCooperation;
    private String supervisorDedicated;
    private String supervisorDiscipline;
    private String supervisorEnvironMental;
    private String supervisorJobInitiative;
    private String supervisorOrganization;
    private String supervisorRatingEigth;
    private String supervisorRatingFive;
    private String supervisorRatingFour;
    private String supervisorRatingOne;
    private String supervisorRatingSeven;
    private String supervisorRatingSix;
    private String supervisorRatingThree;
    private String supervisorRatingTwo;
    private String supervisorSave;

    //打分
    private String teamScore;
    private String totalScore;
    private String worklogScore;
    private String professionalismScore;


    private String deptName;//部门
    private String checkMan;//检查人
    private String postName;//职位
    private int rateState;//评价状态


    public String getCheckManId() {
        return checkManId;
    }

    public WorkReportBean setCheckManId(String checkManId) {
        this.checkManId = checkManId;
        return this;
    }

    public String getSelfAppearance() {
        return selfAppearance;
    }

    public WorkReportBean setSelfAppearance(String selfAppearance) {
        this.selfAppearance = selfAppearance;
        return this;
    }

    public String getSelfBehavior() {
        return selfBehavior;
    }

    public WorkReportBean setSelfBehavior(String selfBehavior) {
        this.selfBehavior = selfBehavior;
        return this;
    }

    public String getSelfCommunication() {
        return selfCommunication;
    }

    public WorkReportBean setSelfCommunication(String selfCommunication) {
        this.selfCommunication = selfCommunication;
        return this;
    }

    public String getSelfCooperation() {
        return selfCooperation;
    }

    public WorkReportBean setSelfCooperation(String selfCooperation) {
        this.selfCooperation = selfCooperation;
        return this;
    }

    public String getSelfDedicated() {
        return selfDedicated;
    }

    public WorkReportBean setSelfDedicated(String selfDedicated) {
        this.selfDedicated = selfDedicated;
        return this;
    }

    public String getSelfDiscipline() {
        return selfDiscipline;
    }

    public WorkReportBean setSelfDiscipline(String selfDiscipline) {
        this.selfDiscipline = selfDiscipline;
        return this;
    }

    public String getSelfEnvironmental() {
        return selfEnvironmental;
    }

    public WorkReportBean setSelfEnvironmental(String selfEnvironmental) {
        this.selfEnvironmental = selfEnvironmental;
        return this;
    }

    public String getSelfJobInitiative() {
        return selfJobInitiative;
    }

    public WorkReportBean setSelfJobInitiative(String selfJobInitiative) {
        this.selfJobInitiative = selfJobInitiative;
        return this;
    }

    public String getSelfOrganization() {
        return selfOrganization;
    }

    public WorkReportBean setSelfOrganization(String selfOrganization) {
        this.selfOrganization = selfOrganization;
        return this;
    }

    public String getSelfRatingEigth() {
        return selfRatingEigth;
    }

    public WorkReportBean setSelfRatingEigth(String selfRatingEigth) {
        this.selfRatingEigth = selfRatingEigth;
        return this;
    }

    public String getSelfRatingFive() {
        return selfRatingFive;
    }

    public WorkReportBean setSelfRatingFive(String selfRatingFive) {
        this.selfRatingFive = selfRatingFive;
        return this;
    }

    public String getSelfRatingFour() {
        return selfRatingFour;
    }

    public WorkReportBean setSelfRatingFour(String selfRatingFour) {
        this.selfRatingFour = selfRatingFour;
        return this;
    }

    public String getSelfRatingOne() {
        return selfRatingOne;
    }

    public WorkReportBean setSelfRatingOne(String selfRatingOne) {
        this.selfRatingOne = selfRatingOne;
        return this;
    }

    public String getSelfRatingSeven() {
        return selfRatingSeven;
    }

    public WorkReportBean setSelfRatingSeven(String selfRatingSeven) {
        this.selfRatingSeven = selfRatingSeven;
        return this;
    }

    public String getSelfRatingSix() {
        return selfRatingSix;
    }

    public WorkReportBean setSelfRatingSix(String selfRatingSix) {
        this.selfRatingSix = selfRatingSix;
        return this;
    }

    public String getSelfRatingThree() {
        return selfRatingThree;
    }

    public WorkReportBean setSelfRatingThree(String selfRatingThree) {
        this.selfRatingThree = selfRatingThree;
        return this;
    }

    public String getSelfRatingTwo() {
        return selfRatingTwo;
    }

    public WorkReportBean setSelfRatingTwo(String selfRatingTwo) {
        this.selfRatingTwo = selfRatingTwo;
        return this;
    }

    public String getSelfSave() {
        return selfSave;
    }

    public WorkReportBean setSelfSave(String selfSave) {
        this.selfSave = selfSave;
        return this;
    }

    public int getSupervisorId() {
        return supervisorId;
    }

    public WorkReportBean setSupervisorId(int supervisorId) {
        this.supervisorId = supervisorId;
        return this;
    }

    public String getTherecipientId() {
        return therecipientId;
    }

    public WorkReportBean setTherecipientId(String therecipientId) {
        this.therecipientId = therecipientId;
        return this;
    }

    public long getTime() {
        return time;
    }

    public WorkReportBean setTime(long time) {
        this.time = time;
        return this;
    }

    public String getTomorrowPlan() {
        return tomorrowPlan;
    }

    public WorkReportBean setTomorrowPlan(String tomorrowPlan) {
        this.tomorrowPlan = tomorrowPlan;
        return this;
    }

    public String getWorkEigth() {
        return workEigth;
    }

    public WorkReportBean setWorkEigth(String workEigth) {
        this.workEigth = workEigth;
        return this;
    }

    public String getWorkFive() {
        return workFive;
    }

    public WorkReportBean setWorkFive(String workFive) {
        this.workFive = workFive;
        return this;
    }

    public String getWorkFour() {
        return workFour;
    }

    public WorkReportBean setWorkFour(String workFour) {
        this.workFour = workFour;
        return this;
    }

    public String getWorkOne() {
        return workOne;
    }

    public WorkReportBean setWorkOne(String workOne) {
        this.workOne = workOne;
        return this;
    }

    public String getWorkSeven() {
        return workSeven;
    }

    public WorkReportBean setWorkSeven(String workSeven) {
        this.workSeven = workSeven;
        return this;
    }

    public String getWorkSix() {
        return workSix;
    }

    public WorkReportBean setWorkSix(String workSix) {
        this.workSix = workSix;
        return this;
    }

    public String getWorkThree() {
        return workThree;
    }

    public WorkReportBean setWorkThree(String workThree) {
        this.workThree = workThree;
        return this;
    }

    public String getWorkTwo() {
        return workTwo;
    }

    public WorkReportBean setWorkTwo(String workTwo) {
        this.workTwo = workTwo;
        return this;
    }

    public String getWorkPlanEigth() {
        return workPlanEigth;
    }

    public WorkReportBean setWorkPlanEigth(String workPlanEigth) {
        this.workPlanEigth = workPlanEigth;
        return this;
    }

    public String getWorkPlanFive() {
        return workPlanFive;
    }

    public WorkReportBean setWorkPlanFive(String workPlanFive) {
        this.workPlanFive = workPlanFive;
        return this;
    }

    public String getWorkPlanFour() {
        return workPlanFour;
    }

    public WorkReportBean setWorkPlanFour(String workPlanFour) {
        this.workPlanFour = workPlanFour;
        return this;
    }

    public String getWorkPlanOne() {
        return workPlanOne;
    }

    public WorkReportBean setWorkPlanOne(String workPlanOne) {
        this.workPlanOne = workPlanOne;
        return this;
    }

    public String getWorkPlanSeven() {
        return workPlanSeven;
    }

    public WorkReportBean setWorkPlanSeven(String workPlanSeven) {
        this.workPlanSeven = workPlanSeven;
        return this;
    }

    public String getWorkPlanSix() {
        return workPlanSix;
    }

    public WorkReportBean setWorkPlanSix(String workPlanSix) {
        this.workPlanSix = workPlanSix;
        return this;
    }

    public String getWorkPlanThree() {
        return workPlanThree;
    }

    public WorkReportBean setWorkPlanThree(String workPlanThree) {
        this.workPlanThree = workPlanThree;
        return this;
    }

    public String getWorkPlanTwo() {
        return workPlanTwo;
    }

    public WorkReportBean setWorkPlanTwo(String workPlanTwo) {
        this.workPlanTwo = workPlanTwo;
        return this;
    }

    public String getWorkResultOne() {
        return workResultOne;
    }

    public WorkReportBean setWorkResultOne(String workResultOne) {
        this.workResultOne = workResultOne;
        return this;
    }

    public String getWorkResultTwo() {
        return workResultTwo;
    }

    public WorkReportBean setWorkResultTwo(String workResultTwo) {
        this.workResultTwo = workResultTwo;
        return this;
    }

    public String getWorkResultThree() {
        return workResultThree;
    }

    public WorkReportBean setWorkResultThree(String workResultThree) {
        this.workResultThree = workResultThree;
        return this;
    }

    public String getWorkResultFour() {
        return workResultFour;
    }

    public WorkReportBean setWorkResultFour(String workResultFour) {
        this.workResultFour = workResultFour;
        return this;
    }

    public String getWorkResultFive() {
        return workResultFive;
    }

    public WorkReportBean setWorkResultFive(String workResultFive) {
        this.workResultFive = workResultFive;
        return this;
    }

    public String getWorkResultSix() {
        return workResultSix;
    }

    public WorkReportBean setWorkResultSix(String workResultSix) {
        this.workResultSix = workResultSix;
        return this;
    }

    public String getWorkResultSeven() {
        return workResultSeven;
    }

    public WorkReportBean setWorkResultSeven(String workResultSeven) {
        this.workResultSeven = workResultSeven;
        return this;
    }

    public String getWorkResultEigth() {
        return workResultEigth;
    }

    public WorkReportBean setWorkResultEigth(String workResultEigth) {
        this.workResultEigth = workResultEigth;
        return this;
    }

    public String getCheckAppearance() {
        return checkAppearance;
    }

    public WorkReportBean setCheckAppearance(String checkAppearance) {
        this.checkAppearance = checkAppearance;
        return this;
    }

    public String getCheckBehavior() {
        return checkBehavior;
    }

    public WorkReportBean setCheckBehavior(String checkBehavior) {
        this.checkBehavior = checkBehavior;
        return this;
    }

    public String getCheckCommunication() {
        return checkCommunication;
    }

    public WorkReportBean setCheckCommunication(String checkCommunication) {
        this.checkCommunication = checkCommunication;
        return this;
    }

    public String getCheckCooperation() {
        return checkCooperation;
    }

    public WorkReportBean setCheckCooperation(String checkCooperation) {
        this.checkCooperation = checkCooperation;
        return this;
    }

    public String getCheckDedicated() {
        return checkDedicated;
    }

    public WorkReportBean setCheckDedicated(String checkDedicated) {
        this.checkDedicated = checkDedicated;
        return this;
    }

    public String getCheckDiscipline() {
        return checkDiscipline;
    }

    public WorkReportBean setCheckDiscipline(String checkDiscipline) {
        this.checkDiscipline = checkDiscipline;
        return this;
    }

    public String getCheckEnvironMental() {
        return checkEnvironMental;
    }

    public WorkReportBean setCheckEnvironMental(String checkEnvironMental) {
        this.checkEnvironMental = checkEnvironMental;
        return this;
    }

    public String getCheckJobInitiative() {
        return checkJobInitiative;
    }

    public WorkReportBean setCheckJobInitiative(String checkJobInitiative) {
        this.checkJobInitiative = checkJobInitiative;
        return this;
    }

    public String getCheckOrganization() {
        return checkOrganization;
    }

    public WorkReportBean setCheckOrganization(String checkOrganization) {
        this.checkOrganization = checkOrganization;
        return this;
    }

    public String getCheckRatingEigth() {
        return checkRatingEigth;
    }

    public WorkReportBean setCheckRatingEigth(String checkRatingEigth) {
        this.checkRatingEigth = checkRatingEigth;
        return this;
    }

    public String getCheckRatingFive() {
        return checkRatingFive;
    }

    public WorkReportBean setCheckRatingFive(String checkRatingFive) {
        this.checkRatingFive = checkRatingFive;
        return this;
    }

    public String getCheckRatingFour() {
        return checkRatingFour;
    }

    public WorkReportBean setCheckRatingFour(String checkRatingFour) {
        this.checkRatingFour = checkRatingFour;
        return this;
    }

    public String getCheckRatingOne() {
        return checkRatingOne;
    }

    public WorkReportBean setCheckRatingOne(String checkRatingOne) {
        this.checkRatingOne = checkRatingOne;
        return this;
    }

    public String getCheckRatingSeven() {
        return checkRatingSeven;
    }

    public WorkReportBean setCheckRatingSeven(String checkRatingSeven) {
        this.checkRatingSeven = checkRatingSeven;
        return this;
    }

    public String getCheckRatingSix() {
        return checkRatingSix;
    }

    public WorkReportBean setCheckRatingSix(String checkRatingSix) {
        this.checkRatingSix = checkRatingSix;
        return this;
    }

    public String getCheckRatingThree() {
        return checkRatingThree;
    }

    public WorkReportBean setCheckRatingThree(String checkRatingThree) {
        this.checkRatingThree = checkRatingThree;
        return this;
    }

    public String getCheckRatingTwo() {
        return checkRatingTwo;
    }

    public WorkReportBean setCheckRatingTwo(String checkRatingTwo) {
        this.checkRatingTwo = checkRatingTwo;
        return this;
    }

    public String getCheckSave() {
        return checkSave;
    }

    public WorkReportBean setCheckSave(String checkSave) {
        this.checkSave = checkSave;
        return this;
    }

    public String getSupervisorAppearance() {
        return supervisorAppearance;
    }

    public WorkReportBean setSupervisorAppearance(String supervisorAppearance) {
        this.supervisorAppearance = supervisorAppearance;
        return this;
    }

    public String getSupervisorBehavior() {
        return supervisorBehavior;
    }

    public WorkReportBean setSupervisorBehavior(String supervisorBehavior) {
        this.supervisorBehavior = supervisorBehavior;
        return this;
    }

    public String getSupervisorCommunication() {
        return supervisorCommunication;
    }

    public WorkReportBean setSupervisorCommunication(String supervisorCommunication) {
        this.supervisorCommunication = supervisorCommunication;
        return this;
    }

    public String getSupervisorCooperation() {
        return supervisorCooperation;
    }

    public WorkReportBean setSupervisorCooperation(String supervisorCooperation) {
        this.supervisorCooperation = supervisorCooperation;
        return this;
    }

    public String getSupervisorDedicated() {
        return supervisorDedicated;
    }

    public WorkReportBean setSupervisorDedicated(String supervisorDedicated) {
        this.supervisorDedicated = supervisorDedicated;
        return this;
    }

    public String getSupervisorDiscipline() {
        return supervisorDiscipline;
    }

    public WorkReportBean setSupervisorDiscipline(String supervisorDiscipline) {
        this.supervisorDiscipline = supervisorDiscipline;
        return this;
    }

    public String getSupervisorEnvironMental() {
        return supervisorEnvironMental;
    }

    public WorkReportBean setSupervisorEnvironMental(String supervisorEnvironMental) {
        this.supervisorEnvironMental = supervisorEnvironMental;
        return this;
    }

    public String getSupervisorJobInitiative() {
        return supervisorJobInitiative;
    }

    public WorkReportBean setSupervisorJobInitiative(String supervisorJobInitiative) {
        this.supervisorJobInitiative = supervisorJobInitiative;
        return this;
    }

    public String getSupervisorOrganization() {
        return supervisorOrganization;
    }

    public WorkReportBean setSupervisorOrganization(String supervisorOrganization) {
        this.supervisorOrganization = supervisorOrganization;
        return this;
    }

    public String getSupervisorRatingEigth() {
        return supervisorRatingEigth;
    }

    public WorkReportBean setSupervisorRatingEigth(String supervisorRatingEigth) {
        this.supervisorRatingEigth = supervisorRatingEigth;
        return this;
    }

    public String getSupervisorRatingFive() {
        return supervisorRatingFive;
    }

    public WorkReportBean setSupervisorRatingFive(String supervisorRatingFive) {
        this.supervisorRatingFive = supervisorRatingFive;
        return this;
    }

    public String getSupervisorRatingFour() {
        return supervisorRatingFour;
    }

    public WorkReportBean setSupervisorRatingFour(String supervisorRatingFour) {
        this.supervisorRatingFour = supervisorRatingFour;
        return this;
    }

    public String getSupervisorRatingOne() {
        return supervisorRatingOne;
    }

    public WorkReportBean setSupervisorRatingOne(String supervisorRatingOne) {
        this.supervisorRatingOne = supervisorRatingOne;
        return this;
    }

    public String getSupervisorRatingSeven() {
        return supervisorRatingSeven;
    }

    public WorkReportBean setSupervisorRatingSeven(String supervisorRatingSeven) {
        this.supervisorRatingSeven = supervisorRatingSeven;
        return this;
    }

    public String getSupervisorRatingSix() {
        return supervisorRatingSix;
    }

    public WorkReportBean setSupervisorRatingSix(String supervisorRatingSix) {
        this.supervisorRatingSix = supervisorRatingSix;
        return this;
    }

    public String getSupervisorRatingThree() {
        return supervisorRatingThree;
    }

    public WorkReportBean setSupervisorRatingThree(String supervisorRatingThree) {
        this.supervisorRatingThree = supervisorRatingThree;
        return this;
    }

    public String getSupervisorRatingTwo() {
        return supervisorRatingTwo;
    }

    public WorkReportBean setSupervisorRatingTwo(String supervisorRatingTwo) {
        this.supervisorRatingTwo = supervisorRatingTwo;
        return this;
    }

    public String getSupervisorSave() {
        return supervisorSave;
    }

    public WorkReportBean setSupervisorSave(String supervisorSave) {
        this.supervisorSave = supervisorSave;
        return this;
    }

    public String getTeamScore() {
        return teamScore;
    }

    public WorkReportBean setTeamScore(String teamScore) {
        this.teamScore = teamScore;
        return this;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public WorkReportBean setTotalScore(String totalScore) {
        this.totalScore = totalScore;
        return this;
    }

    public String getWorklogScore() {
        return worklogScore;
    }

    public WorkReportBean setWorklogScore(String worklogScore) {
        this.worklogScore = worklogScore;
        return this;
    }

    public String getProfessionalismScore() {
        return professionalismScore;
    }

    public WorkReportBean setProfessionalismScore(String professionalismScore) {
        this.professionalismScore = professionalismScore;
        return this;
    }

    public String getDeptName() {
        return deptName;
    }

    public WorkReportBean setDeptName(String deptName) {
        this.deptName = deptName;
        return this;
    }

    public String getCheckMan() {
        return checkMan;
    }

    public WorkReportBean setCheckMan(String checkMan) {
        this.checkMan = checkMan;
        return this;
    }

    public String getPostName() {
        return postName;
    }

    public WorkReportBean setPostName(String postName) {
        this.postName = postName;
        return this;
    }

    public int getRateState() {
        return rateState;
    }

    public WorkReportBean setRateState(int rateState) {
        this.rateState = rateState;
        return this;
    }
}
