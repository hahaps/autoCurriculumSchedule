package com.algorithm.course.scheduling.automatic;

/***
 * 排课结果信息汇总
 * 
 * @version 0.1
 * @author 李细鹏
 * 
 */
public class SchedulExceptions extends Exception {

	private static final long serialVersionUID = 1L;

	// 排课结果类型
	private HandleType type = HandleType.SUCCESS_DONE;
	// 结果类型日志
	private String message = null;
	// 结果类型附加信息
	private String metadata = null;

	/***
	 * 
	 * @param type
	 */
	public SchedulExceptions(HandleType type) {
		super();
		this.type = type;
		this.message = type.CODE_MESSAGE_MAP.get(type.getcode());
	}

	/***
	 * 
	 * @param type
	 * @param metadata
	 */
	public SchedulExceptions(HandleType type, String metadata) {
		super();
		this.type = type;
		this.metadata = metadata;
		this.message = type.CODE_MESSAGE_MAP.get(type.getcode());
	}

	/***
	 * 
	 * @param type
	 * @param message
	 * @param metadata
	 */
	public SchedulExceptions(HandleType type, String message, String metadata) {
		super();
		this.type = type;
		this.metadata = metadata;
		this.message = message;
	}

	/***
	 * 覆盖toString方法
	 */
	public String toString() {
		return super.toString() + "<" + this.type.getcode() + ", "
				+ this.message() + ">";
	}

	/***
	 * 获取结果类型日志
	 * 
	 * @return
	 */
	public String message() {
		return this.message + ", metadata: " + this.metadata;
	}

	/***
	 * 获取结果类型附加信息
	 * 
	 * @return
	 */
	public String metadata() {
		return this.metadata;
	}

    /**
     * 排课结果类型
     * @return
     */
    public HandleType getType() {
        return type;
    }
}
