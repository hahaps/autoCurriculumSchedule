package com.algorithm.course.scheduling.automatic;

import java.util.HashMap;
import java.util.Map;

/***
 * 排课结果类型
 * 
 * @version 0.1
 * @author 李细鹏
 * 
 */
public enum HandleType {
	SUCCESS_DONE(0), // 处理正确
	SYSTEM_ERROR(100), // 系统错误
	CHECK_ERROR(300), // 检验时出错 
	CHECK_ERROR_AT_CLASS_CONTINUE_COURSE_TOO_MANY(301), // 连课节数超过2
	CHECK_ERROR_AT_TEACHER_COURSE_TOO_MANY(302), // 教师一周的课节数太多，超过了每周总课数的3/4
	CHECK_ERROR_AT_VICE_COURSE_TOO_MANY(303), // 副课太多，超过3/5，超过3/5,无法完成初始化任务
	CHECK_ERROR_AT_COURSE_A_WEEK_TOO_MANY(304), // 没有休息日
	CHECK_ERROR_AT_COURSE_A_DAY_TOO_MANY(305), // 每天的课程量过多，超过了8
	CHECK_ERROR_AT_COURSE_OUT_OF_RANGE(306), // 安排的课节种数超过了设定的待排课节数，
	CHECK_ERROR_AT_COURSE_CONTINUE_COURSE(307), // 安排的连课节数不合理，
	RUN_ERROR(400), // 程序运行时出错
	RUN_ERROR_AT_NO_TEACHER(401), // 无法根据教师id获取教师
	RUN_ERROR_AT_NO_COURSE(402), // 无法根据课程id获取课程
	RUN_ERROR_AT_NO_CLASS(403), // 无法根据课程id获取课程
	RUN_ERROR_AT_NO_GRADE(404), // 无法根据年级id获取年级
	RUN_ERROR_AT_COURSE_POSITION_OUT_OF_RANGE(405), // 课程安排的位置超过排课范围
	RUN_ERROR_AT_TOO_MANY_CLASS(406), // 同class_id所找到的课表有多个
	RUN_ERROR_AT_INIT_TIME_TABLE(407), // 初始化课程表时出错
	DEFAULT_ERROR(500), // Default
	;
	
	// 处理结果与返回信息对照表
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
