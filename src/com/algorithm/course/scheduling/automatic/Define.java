package com.algorithm.course.scheduling.automatic;

public class Define {
    /**最多同时上某课的限制条件*/
    public static final int Constraint_MaxAllow=0;
    /**黄金时间段集合*/
    public static final int Constraint_BestPosition=1;
    /**加载步长设置集合*/
    public static final int Constraint_Step=2;
    /**合班课限制条件*/
    public static final int Constraint_GetClassTogether=3;
    /**固定课程限制条件*/
    public static final int Constraint_StaticCourse=4;
    /**无课时间限制条件*/
    public static final int Constraint_UndesireCourseTime=5;
    /**连课限制条件*/
    public static final int Constraint_TogetherCourse=6;

	// group size, group size must be a even integer.
	public static final int GROUP_SIZE = 20;//20;
	// max iterate times.
	public static final int MAX_UNM = 2000;//5000;
	public static final double ALLOW_COUNT = .5;
	// crossover probability of selection
	public static final double PXOVER = .618;
	public static final double PXOVER_MIN = .9;
	// mutation probability of selection
	public static final double PMULTATION = .382;
	public  static final double PMULTATION_MIN = .1;
	// group threshold
	public static final double THRESHOLD = 1;

	public static double[] CONSTRAINT_VALUE = {
		.6, // 课程分布不均冲突
		.2, // 课程步长冲突
		1, // 无课时间冲突
		.3, // 副课在黄金时间上冲突
		1,   // 教师课程冲突
		1    // 教室或场地冲突
		};
}
