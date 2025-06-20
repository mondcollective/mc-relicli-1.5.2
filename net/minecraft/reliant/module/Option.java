package net.minecraft.reliant.module;

public class Option {

	private String nameIdentifier;
	private Object value;
	private Object minAndMax[] = new Object[2];
	public static final int NA = -1,
							INTEGER = 0,
							FLOAT = 1,
							DOUBLE = 2,
							STRING = 3,
							BOOLEAN = 4;
	
	public Option(String name, Object curValue, Object[] minAndMax) {
		this.nameIdentifier = name;
		this.value = curValue;
		this.minAndMax[0] = minAndMax[0];
		this.minAndMax[1] = minAndMax[1];
	}
	
	public Option(String name, Object curValue) {
		this.nameIdentifier = name;
		this.value = curValue;
		this.minAndMax[0] = curValue;
		this.minAndMax[1] = curValue;
	}
	
	/**
	 * @return
	 * 		The name identifier of the Option
	 */
	public String getName() {
		return this.nameIdentifier;
	}
	
	/**
	 * @return
	 * 		The Object value of the Option
	 */
	public Object getValue() {
		return this.value;
	}
	
	/**
	 * Sets the Object value of the Option.
	 * 
	 * @param object
	 */
	public void setValue(Object object) {
		if (this.isType(FLOAT)) {
			if (((Float) object) < ((Float) this.getMinAndMax()[0])) {
				object = this.getMinAndMax()[0];
			}
			if (((Float) object) > ((Float) this.getMinAndMax()[1])) {
				object = this.getMinAndMax()[1];
			} 
		} else if (this.isType(DOUBLE)) {
			if (((Double) object) < ((Double) this.getMinAndMax()[0])) {
				object = this.getMinAndMax()[0];
			}
			if (((Double) object) > ((Double) this.getMinAndMax()[1])) {
				object = this.getMinAndMax()[1];
			}
		} else if (this.isType(INTEGER)) {
			if (((Integer) object) < ((Integer) this.getMinAndMax()[0])) {
				object = this.getMinAndMax()[0];
			}
			if (((Integer) object) > ((Integer) this.getMinAndMax()[1])) {
				object = this.getMinAndMax()[1];
			}
		}
		this.value = object;
	}
	
	public void toggleBoolean() {
		if (this.isType(this.BOOLEAN)) {
			this.value = !((Boolean) this.value);
		}
	}
	
	/**
	 * Returns true if the Option's Object value
	 * is of the type specified.
	 * 
	 * @param type
	 * 		
	 * @return
	 * 		
	 */
	public boolean isType(int type) {
		switch (type) {
			case 0:
				return this.value instanceof Integer;
				
			case 1:
				return this.value instanceof Float;
				
			case 2:
				return this.value instanceof Double;
				
			case 3: 
				return this.value instanceof String;
				
			case 4:
				return this.value instanceof Boolean;
				
			default:
				return false;
		}
	}
	
	/**
	 * @return
	 * 		The type of the Option's Object value.
	 */
	public int getType() {
		if (this.value instanceof Integer) {
			return this.INTEGER;
		} else if (this.value instanceof Float) {
			return this.FLOAT;
		} else if (this.value instanceof Double) {
			return this.DOUBLE;
		} else if (this.value instanceof String) {
			return this.STRING;
		} else if (this.value instanceof Boolean) {
			return this.BOOLEAN;
		}
		return this.NA;
	}
	
	/**
	 * @return
	 * 		The value as a float value.
	 */
	public float getFloat() {
		if (this.isType(this.FLOAT)) {
			return (Float) this.value;
		}
		return 0f;
	}
	
	/**
	 * @return
	 * 		The value as a integer value.
	 */
	public int getInteger() {
		if (this.isType(this.INTEGER)) {
			return (Integer) this.value;
		}
		return 0;
	}
	
	/**
	 * @return
	 * 		The value as a double value.
	 */
	public double getDouble() {
		if (this.isType(this.DOUBLE)) {
			return (Double) this.value;
		}
		return 0D;
	}
	
	/**
	 * @return
	 * 		The value as a String.
	 */
	public String getString() {
		if (this.isType(this.STRING)) {
			return (String) this.value;
		}
		return "";
	}
	
	/**
	 * @return
	 * 		The value as a boolean.
	 */
	public Boolean getBoolean() {
		if (this.isType(this.BOOLEAN)) {
			return (Boolean) this.value;
		}
		return false;
	}
	
	/**
	 * @return
	 * 		Object Array of [0] - the value's minimum, 
	 * 		and [1] - the value's maximum. If they are
	 * 		the same, the value is not meant to change.
	 */
	public Object[] getMinAndMax() {
		return this.minAndMax;
	}
	
}
