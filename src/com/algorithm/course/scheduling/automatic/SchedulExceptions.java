package com.algorithm.course.scheduling.automatic;

/***
 * Exception
 * 
 * @version 0.1
 * @author Li Xipeng, lixipeng@hihuron.com
 * 
 */
public class SchedulExceptions extends Exception {

	private static final long serialVersionUID = 1L;

	private HandleType type = HandleType.SUCCESS_DONE;
	private String message = null;
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
	 * Overwrite toString
	 */
	public String toString() {
		return super.toString() + "<" + this.type.getcode() + ", "
				+ this.message() + ">";
	}

	/***
	 * Get message
	 * 
	 * @return
	 */
	public String message() {
		return this.message + ", metadata: " + this.metadata;
	}

	/***
	 * Get metadata message
	 * 
	 * @return
	 */
	public String metadata() {
		return this.metadata;
	}

    /**
     * result type
     * @return
     */
    public HandleType getType() {
        return type;
    }
}
