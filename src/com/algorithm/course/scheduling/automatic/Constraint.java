package com.algorithm.course.scheduling.automatic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/***
 * 限制条件组集合
 * 
 * @version 0.1
 * @author 李细鹏
 */
public class Constraint {
	// 默认步长
	public static int PACE_DEFAULT = 2;
	
	// 最大允许某课程在同一时间点上的班级个数
	private Map<Integer, Integer> max_allow;
	// 固定课程，key=课程ID，value=固定的位置
	private Map<Integer, ArrayList<FixPosition>> static_position;
	// 合班课导致的固定课程，key=班级ID，value=FixPosition集合
	private Map<Integer, ArrayList<FixPosition>> fixed_position;
	// 由于同一教师导致的不期望安排课程时间集合，key=班级ID，value=UnfitPosition集合
	private Map<Integer, ArrayList<UnfitPosition>> none_position;
	// 相对较好的授课位置集合
	private ArrayList<Integer> best_position;
	// 课程步长集合，key=课程ID，value=步长值，步长默认为2
	private Map<Integer, Integer> paces;
	// 合班课集合
	private ArrayList<TogetherClass> together_course;
	
	// 无课时间，key=教师ID，value=无课时间
	private Map<Integer, ArrayList<Integer>> undesire_postion;
	
	/***
	 * 初始化限制条件集合
	 */
	public Constraint () {
		this.max_allow = new HashMap<Integer, Integer> ();
		this.none_position = new HashMap<Integer, ArrayList<UnfitPosition>>();
		this.undesire_postion = new HashMap<Integer, ArrayList<Integer>>();
		this.static_position = new HashMap<Integer, ArrayList<FixPosition>>();
		this.fixed_position = new HashMap<Integer, ArrayList<FixPosition>>();
		this.best_position = new ArrayList<Integer>();
		this.paces = new HashMap<Integer, Integer> ();
		this.together_course = new ArrayList<TogetherClass> ();
	}
	
	/***
	 * 获取最大允许某课程在同一时间点上的班级数
	 * @return
	 */
	public Map<Integer, Integer> getMax_allow() {
		return max_allow;
	}

	/***
	 * 获取固定课程集合
	 * 
	 * @return
	 */
	public Map<Integer, ArrayList<FixPosition>> getStatic_position() {
		return static_position;
	}

	/***
	 * 获取由于同一教师导致的不期望安排课程时间集合
	 * 
	 * @return
	 */
	public Map<Integer, ArrayList<UnfitPosition>> getNone_position() {
		return none_position;
	}

	public ArrayList<Integer> getBest_position() {
		return best_position;
	}

	/***
	 * 获取课程步长设置集合
	 * 
	 * @return
	 */
	public Map<Integer, Integer> getPaces() {
		return paces;
	}

	/***
	 * 获取和班课集合
	 * 
	 * @return
	 */
	public ArrayList<TogetherClass> getTogether_course() {
		return together_course;
	}

	/***
	 * 获取无课时间集合
	 * 
	 * @return
	 */
	public Map<Integer, ArrayList<Integer>> getUndesire_postion() {
		return undesire_postion;
	}

	/***
	 * 获取合班课导致的固定课程集
	 * 
	 * @return
	 */
	public Map<Integer, ArrayList<FixPosition>> getFixed_position() {
		return fixed_position;
	}
	
}

/***
 * 合班课
 * 
 * @version 0.1
 */
class TogetherClass {
	public Integer course_id;
	public Integer [] classGroup;
	public int courseCount = 0;
	public TogetherClass () {}
}

/***
 * 合班课导致的固定课程
 * 
 * @version 0.1
 */
class FixPosition {
	public Integer course_id;
	public Integer positons;
}

/***
 * 由于同一教师导致的不期望安排课程时间
 * 
 * @version 0.1
 */
class UnfitPosition {
	public Integer course_id;
	public Integer positons;
}
