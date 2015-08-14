package com.algorithm.course.scheduling.automatic;

import java.util.Map;

/**
 * Get id number
 */
public interface IDataConvent {
    public Map getClassInfo(int classID);
    public Map getGradeInfo(int gradeID);
    public Map getTeacherInfo(int teacherID);
    public Map getCourseInfo(int courseID);
}
