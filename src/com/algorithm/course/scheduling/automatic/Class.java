package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;

/***
 * Class specified
 * 
 * @version 0.1
 * @author Li Xipeng, lixipeng@hihuron.com
 * 
 */
public class Class {

	// class id
	private Integer class_mode;
	// grade id
	private Integer grade;
	// courses map
	private ArrayList<Course> courses;
	private int courseCount = 0;

	/***
	 * Instructor of class
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
	 * Get class id
	 * 
	 * @return
	 */
	public Integer getClass_mode() {
		return class_mode;
	}

	/***
	 * Get grade id of this class
	 * 
	 * @return
	 */
	public Integer getGrade() {
		return grade;
	}

	/***
	 * Get curriculum of this class
	 * 
	 * @return
	 */
	public ArrayList<Course> getCourses() {
		return courses;
	}

	/***
	 * Get course count each week for this class
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
