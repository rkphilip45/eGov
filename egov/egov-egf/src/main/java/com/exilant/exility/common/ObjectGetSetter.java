package com.exilant.exility.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;

/**
 * Static utility Class to set/get field of/from an object
 * we will not worry about any exception. We only promised that we will TRY :-)
 */

public class ObjectGetSetter {
	
	private static final Logger logger = Logger.getLogger(ObjectGetSetter.class);

	private ObjectGetSetter() {
	}
	
/****************************************************
 * Gets the value of a field of an object as an object.
 * Caller can cast this to appropraite class or inspect its type before using it.
 * 
 * @param object : object whose field  value is to be returned
 * @param fieldName : Name of the field
 * @return String representing the String value of the field
 */	
	public static Object get(Object object, String fieldName){

		try {
			Field field = ObjectGetSetter.getField(object, fieldName);
			field.setAccessible(true);
			return field.get(object).toString();
		} catch (Exception e) {
			//logger.error("Problem in get()"+object+"for Fiels"+fieldName);
		} // let us not worry about any exception. We only promised that we will TRY :-)
		return null;
	}

/*********************************************
 * Returns a HashMap of name/value pairs for all fields declared by the class or super classes
 * Note: fields with 'private' modifiers are not picked up.
 * 
 * @param object : object from which the fields are to be extracted 
 * @param primitivesonly : Data types, int, Integer, float, Float, ..... and String are considered to be primitive
 * 		If this field is set to true, fields of only these type are picked up. Else all fields are picked up.
 * @return HashMap containing name/value pairs of all field values 
 */	
	public static HashMap getAll(Object object, boolean primitivesonly){

		HashMap values = new HashMap();
		Field[] fields;
		Field field;
		/*
		 * Get fields declared in this as well as super classes, but ones that are not private
		 */
		 Class cls = object.getClass();
		 while (!cls.equals(Object.class)){ //go up the super class till you reach Object.
		 	fields = cls.getDeclaredFields();
			for (int i=0; i<fields.length; i++){
					field = fields[i];
					if (field.getModifiers() == 2)continue; // let us keep private fields private !!
					
					// int, char ets return isPrimitive() = true. Classes like Integer, String are defined in java.lang. These are also primitives
					if (primitivesonly && !field.getType().isPrimitive() && (field.getType().getName().indexOf("java.lang.") != 0)) continue;

					field.setAccessible(true); //ensures that the field is accessible
					try{
						values.put(field.getName(), field.get(object));
					}catch (Exception e){
						//logger.error("Error while putting values to HashMap");
					}
			}
		 	cls = cls.getSuperclass(); //loop back for the declared fields in the superclass
		 }
		return values;
	}
/**********************
 * Get a field from the class or the super classes of the object
 * @param object
 * @param fieldName name of teh field to return
 * @return Field object if found
 * @throws NoSuchFieldException
 */	
	public static Field getField(Object object, String fieldName)throws NoSuchFieldException {
		Field field = null;
		Class cls = object.getClass();
		while (field == null && !cls.equals(Object.class)){ //keep going up till you reach object, or you find field
			try {
				field = cls.getDeclaredField(fieldName);
			}catch (NoSuchFieldException e){
				//logger.error("Error while getting declared field"+object+"    "+fieldName);
				cls = cls.getSuperclass();
			}
		}
		if (field == null)throw new NoSuchFieldException("NoSuchFieldException for field " +fieldName + " for class" +object.getClass().getName());
		return field;

	}
/**********************************
 * Sets the field to the specified value in the suppled object
 * Either the field name shoudl exist, or a generic HashMap named 'atributes' should exist
 * Else, teh suppled value is not set.
 * @param object
 * @param fieldName
 * @param fieldValue
 */	
	public static void set(Object object, String fieldName, String fieldValue){
		Class[] clsarr = {String.class};
		String[] strarr = {fieldValue};
		Field field; 
		try {
			field = ObjectGetSetter.getField(object, fieldName);
			field.setAccessible(true);
			Class cls = field.getType();
			if (cls.isPrimitive() || cls.equals(String.class)){
				if(cls.equals(int.class)){
					field.setInt(object, Integer.parseInt(fieldValue));
				}else if(cls.equals(long.class)){
					field.setLong(object, Long.parseLong(fieldValue));
				}else if(cls.equals(short.class)){
					field.setShort(object, Short.parseShort(fieldValue));
				}else if(cls.equals(byte.class)){
					field.setByte(object, Byte.parseByte(fieldValue));
				}else if(cls.equals(float.class)){
					field.setFloat(object, Float.parseFloat(fieldValue));
				}else if(cls.equals(double.class)){
					field.setDouble(object, Double.parseDouble(fieldValue));
				}else if(cls.equals(boolean.class)){
					if(fieldValue.equalsIgnoreCase("true") 
						||fieldValue.equalsIgnoreCase("yes") 
						|| fieldValue.equals("1"))
							field.setBoolean(object, true);
					else field.setBoolean(object, false);
					
				}else if(cls.equals(char.class)){
					field.setChar(object, fieldValue.charAt(0));
				}else if(cls.equals(String.class)){
					field.set(object, fieldValue);
				}else{ // let us try to create that object with a constructur that accepts String
					Object o = cls.getConstructor(clsarr).newInstance(strarr);
					field.set(object, o);
				}
			}
			return;
		} catch (Exception e) {
		/*
		 * Let us try for a generic field named attributes with a put(object, object) method 
		 */
			
			//logger.error(e.getMessage());
			try{ // to invoke put (fieldName, fieldValue) for 
				Class[] objectClass = {Object.class, Object.class};	
				field = ObjectGetSetter.getField(object,"attributes");
				field.setAccessible(true);
				Object atts = field.get(object);
				Class fldclass = field.getType();
				// instantiate and assign the instance if required
				if (null == atts){
					atts = fldclass.newInstance();
					field.setAccessible(true);
					field.set(object,atts);
				}
				Method met = fldclass.getMethod("put",objectClass);
				Object[] objarr = {fieldName, fieldValue};
				met.invoke(atts,objarr);
			}catch (Exception e1){
				//logger.error(e.getMessage());  
				
			}
		}
	}
/**********************
 * Sets values from a HashMap containing bname/value pairs to a corresponding fields
 * of the supplied object. Any mismatch is ignored, and no exception is raised
 * @param object
 * @param values
 */
	public static void setAll(Object object, HashMap values){
		Object o;
		Map.Entry entry;
		Iterator iter = values.entrySet().iterator();
		while (iter.hasNext()){
			entry = (Map.Entry)iter.next();
			o = entry.getKey();
			if (!o.getClass().equals(String.class)) continue; //key has to be a string
			ObjectGetSetter.set(object, (String)o, entry.getValue().toString());
		}
	}
/**********************
 * Sets values from an Attribute containing bname/value pairs to a corresponding fields
 * of the supplied object. Any mismatch is ignored, and no exception is raised
 * 
 * @param object
 * @param values
 */
	public static void setAll(Object object, Attributes values){
		for(int i=0; i<values.getLength(); i++){
			ObjectGetSetter.set(object, values.getQName(i), values.getValue(i));
		}
	}
	/*
	public static String unescapeXMLChars(String value){
			return value.replaceAll("&gt;",">").replaceAll("&lt;","<").replaceAll("&quot;","\"").replaceAll("&amp;", "&");
	}
	*/
}