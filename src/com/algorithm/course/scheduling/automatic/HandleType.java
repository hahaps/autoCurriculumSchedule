package com.algorithm.course.scheduling.automatic;

import java.util.HashMap;
import java.util.Map;

/***
 * Result type
 * 
 * @version 0.1
 * @author Li Xipeng, lixipeng@hihuron.com
 * 
 */
public enum HandleType {
	SUCCESS_DONE(0), // success done
	SYSTEM_ERROR(100), // system error
	CHECK_ERROR(300), // error when checking
	CHECK_ERROR_AT_CLASS_CONTINUE_COURSE_TOO_MANY(301),
	CHECK_ERROR_AT_TEACHER_COURSE_TOO_MANY(302), // too many course for one teacher
	CHECK_ERROR_AT_VICE_COURSE_TOO_MANY(303), // too less main courses
	CHECK_ERROR_AT_COURSE_A_WEEK_TOO_MANY(304), // No week day
	CHECK_ERROR_AT_COURSE_A_DAY_TOO_MANY(305), // too many course one day, more than 8
	CHECK_ERROR_AT_COURSE_OUT_OF_RANGE(306), // course count is more than setting
	CHECK_ERROR_AT_COURSE_CONTINUE_COURSE(307),
	RUN_ERROR(400), // Run time error
	RUN_ERROR_AT_NO_TEACHER(401), // can't get Teacher with teacher id
	RUN_ERROR_AT_NO_COURSE(402), // can't get course with course id
	RUN_ERROR_AT_NO_CLASS(403), // can't get class with class id
	RUN_ERROR_AT_NO_GRADE(404), // can't get grade with grade id
	RUN_ERROR_AT_COURSE_POSITION_OUT_OF_RANGE(405), // too many courses out of range
	RUN_ERROR_AT_TOO_MANY_CLASS(406), // one class get more than one course timetable
	RUN_ERROR_AT_INIT_TIME_TABLE(407), // error when initial course timetable
	DEFAULT_ERROR(500), // Default
	;
	
	// Result mapping
	@SuppressWarnings("serial")
	public final Map<Integer, String> CODE_MESSAGE_MAP = new HashMap<Integer, String>() {
		{
			put(0, "Rusult perfect");
			put(100, "System error");
			put(300, "Check error");
			put(301, "Check error that the number of three or more lessons exist");
			put(302, "Check error that one courses for a teacher in a week are too much, more than 1/3");
			put(303, "Check error that vice classes is too much, more than 1/2");
			put(304, "Check error that there is two many day at shcool, more than 6 a week");
			put(305, "Check error that there is two courses a day, more than 9");
			put(306, "Data error that courses for all teachers are more than courses count");
		}
	};

	private int code = 0;
	/***
	 * 
	 * @param code
	 */
	HandleType(int code) {
		this.code = code;
	}

	/***
	 * 
	 * @return
	 */
	public int getcode() {
		return this.code;
	}
}
