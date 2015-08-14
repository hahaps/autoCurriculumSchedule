package com.algorithm.course.scheduling.automatic;

/***
 * Course specified
 * 
 * @version 0.1
 * @author Li Xipeng, lixipeng@hihuron.com
 * 
 */
public class Course {
	// course id
	private Integer id;
	// input course id
	private Integer course_id;
	// course name
	private String course_name;
	// who teaches this course
	private Teacher teacher;
	// course count each week
	private int times_each_week;
	// is main course, default is true
	private boolean is_main_course = true;
	// course count each time, default is 1
	private int length = 1;
	// priority of this course
	private int priority = 1000;
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public int getPriority() {
		return this.priority;
	}

	/***
	 * Instructor of Course
	 * 
	 * @param course_id
	 * @param course_name
	 * @param teacher
	 */
	public Course(Integer course_id, String course_name, Teacher teacher) {
		this.course_id = course_id;
		this.id = course_id;
		this.course_name = course_name;
		this.teacher = teacher;
	}

	/***
	 * Get course count each time
	 * 
	 * @return
	 */
	public int getLength() {
		return length;
	}

	/***
	 * Set course count each time
	 * 
	 * @param length
	 */
	public void setLength(int length) {
		this.length = length;
	}

	/***
	 * Set course whether is main course
	 * 
	 * @param is_main_course
	 */
	public void setIs_main_course(boolean is_main_course) {
		this.is_main_course = is_main_course;
	}

	/***
	 * Get course id
	 * 
	 * @return
	 */
	public Integer getId() {
		return id;
	}

	/***
	 * Set course id
	 * 
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/***
	 * Get course whether it is main course
	 * 
	 * @return
	 */
	public boolean getIs_main_course() {
		return this.is_main_course;
	}

	/***
	 * Set course count each week
	 * 
	 * @param times_each_week
	 */
	public void setTimes_each_week(int times_each_week) {
		this.times_each_week = times_each_week;
	}

	/***
	 * Get course id when input
	 * 
	 * @return course id
	 */
	public Integer getCourse_id() {
		return course_id;
	}

	/***
	 * Get course name
	 * 
	 * @return course_name
	 */
	public String getCourse_name() {
		return course_name;
	}

	/***
	 * Get course Teacher
	 * 
	 * @return teacher
	 */
	public Teacher getTeacher() {
		return teacher;
	}

	/***
	 * Get course count each week
	 * 
	 * @return times_each_week
	 */
	public int getTimes_each_week() {
		return times_each_week;
	}

}
