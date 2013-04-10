/**
 * 
 */
package org.iit.cshou.dp.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * @author cshou
 * 
 */
public class ObjectCoder {

	public static String code(Object object) throws IOException {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutput out = null;
		String result = null;

		try {
			out = new ObjectOutputStream(bos);
			out.writeObject(object);
			byte[] bytes = bos.toByteArray();
			
			result = Base64.encodeBase64String(bytes);

		} finally {
			out.close();
			bos.close();
		}

		return result;
	}

	public static Object decode(String string) throws IOException, ClassNotFoundException {
		
		byte[] bytes = Base64.decodeBase64(string);
		
		ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
		ObjectInput in = null;
		
		Object result = null;
		
		try {
			
		  in = new ObjectInputStream(bis);
		  result = in.readObject(); 
		  
		} finally {
		  bis.close();
		  in.close();
		}
		
		return result;
	}

}
