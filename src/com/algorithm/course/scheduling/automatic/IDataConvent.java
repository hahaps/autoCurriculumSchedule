package com.algorithm.course.scheduling.automatic;

import java.util.Map;

/**
 * 通过数字ID获取实际的基础数据
 * Created by Hyberbin on 2014/5/22.
 */
public interface IDataConvent {
    public Map getClassInfo(int classID);
    public Map getGradeInfo(int gradeID);
    public Map getTeacherInfo(int teacherID);
    public Map getCourseInfo(int courseID);
}
