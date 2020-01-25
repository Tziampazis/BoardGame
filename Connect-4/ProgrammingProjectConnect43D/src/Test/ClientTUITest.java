package Test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import serverofficial.ClientTUI;

public class ClientTUITest {

	@Test
	public void getClientNameTest() {
		String data = "name With spaces";
		System.setIn(new ByteArrayInputStream(data.getBytes()));
		//client tui na setin aanroepen
		ClientTUI test = new ClientTUI();
		String name = test.getClientName();
		assertEquals(data, name);
		
		String data2 = "namewithoutspaces";
		System.setIn(new ByteArrayInputStream(data2.getBytes()));
		ClientTUI test2 = new ClientTUI();
		String name2 = test2.getClientName();
		assertEquals(data2, name2);
	}
	
	
	@Test
	public void getLevelTest() {
		String wrongh = "34";
		System.setIn(new ByteArrayInputStream(wrongh.getBytes()));
		String goodh = "h";
		System.setIn(new ByteArrayInputStream(goodh.getBytes()));
		
		ClientTUI test = new ClientTUI();
		String level = test.getLevel();
		assertEquals("human", level);
		
		String goodc = "c";
		System.setIn(new ByteArrayInputStream(goodc.getBytes()));
		String wronge = "ea";
		System.setIn(new ByteArrayInputStream(wronge.getBytes()));
		String goode = "easy";
		System.setIn(new ByteArrayInputStream(goode.getBytes()));
		
		ClientTUI test2 = new ClientTUI();
		String level2 = test2.getLevel();
		assertEquals("easy", level2);
			
		
	}
	
	

}
