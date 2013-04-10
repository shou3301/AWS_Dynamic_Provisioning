package org.iit.cshou.dp.util;

import static org.junit.Assert.*;

import java.io.IOException;

import org.iit.cshou.dp.client.SleepRequest;
import org.iit.cshou.dp.intl.Request;
import org.junit.Test;

public class ObjectCoderTest {

	@Test
	public void testCode() throws Exception {
		
		Request req = new SleepRequest("locahost", 1000, "id", 10);
		String tmp = ObjectCoder.code(req);
		System.out.println((Request) ObjectCoder.decode(tmp));
	}

}
