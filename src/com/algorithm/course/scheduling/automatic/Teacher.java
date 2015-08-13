package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 * 教师
 * 
 * @version 0.1
 * @author 李细鹏
 * 
 */
public class Teacher {
	// 教师id
	private Integer teacher_id;
	// 教师名称
	private String teacher_name;

	/***
	 * 教师信息构造函数
	 * 
	 * @param teacher_id
	 * @param teacher_name
	 */
	public Teacher(Integer teacher_id, String teacher_name) {
		this.teacher_id = teacher_id;
		this.teacher_name = teacher_name;
	}

	/***
	 * 获取教师id
	 * 
	 * @return teacher id
	 */
	public Integer getTeacher_id() {
		return teacher_id;
	}

	/***
	 * 获取教师名称
	 * 
	 * @return teacher name
	 */
	public String getTeacher_name() {
		return teacher_name;
	}
}

class TeacherWithCourse {
	
	public Map<Integer, ArrayList<ClassWithCourse>> map = new HashMap<Integer, ArrayList<ClassWithCourse>>();
}

class ClassWithCourse {
	public int classId;
	public int len;
	public int len_each_time;
	public int priority;
}
