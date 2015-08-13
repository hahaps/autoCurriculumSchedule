package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;

/***
 * 班级类
 * 
 * @version 0.1
 * @author 李细鹏
 * 
 */
public class Class {

	// 班级id
	private Integer class_mode;
	// 年级id
	private Integer grade;
	// 该班级课程集合
	private ArrayList<Course> courses;
	private int courseCount = 0;

	/***
	 * 初始化班级的构造函数
	 * 
	 * @param class_mode
	 * @param grade
	 */
	public Class(Integer class_mode, Integer grade) {
		this.class_mode = class_mode;
		this.grade = grade;
		this.courses = new ArrayList<Course>();
	}

	/***
	 * 获取班级ID
	 * 
	 * @return
	 */
	public Integer getClass_mode() {
		return class_mode;
	}

	/***
	 * 获取该班级的年级ID
	 * 
	 * @return
	 */
	public Integer getGrade() {
		return grade;
	}

	/***
	 * 获取该班级的课程列表
	 * 
	 * @return
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}

	/***
	 * 获取该班级每周课程数量
	 * 
	 * @return
	 */
	public int getCourseCount() {
		return courseCount;
	}

	/***
	 * 
	 * @param courseCount
	 */
	public void setCourseCount(int courseCount) {
		this.courseCount = courseCount;
	}
}
