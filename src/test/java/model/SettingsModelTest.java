package model;


import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import controller.Controller;
import model.SettingsModel;


public class SettingsModelTest {

	SettingsModel settings;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		(new File(Controller.getPath("settings.esc"))).delete();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		(new File(Controller.getPath("settings.esc"))).delete();
	}

	@Before
	public void setUp() throws Exception {
		settings = SettingsModel.load();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveLoadValue() {
		int test;
		
		test = settings.getValue("test", 1);
		assertEquals(test, 1);
		settings.setValue("test", 2);
		test = settings.getValue("test", 1);
		assertEquals(test, 2);

		settings = SettingsModel.load();
		test = settings.getValue("test", 3);
		assertEquals(test, 3);
		settings.save();
		settings = SettingsModel.load();
		test = settings.getValue("test", 1);
		assertEquals(test, 3);

		Double d = settings.getValue("test", 1.3);
		assertNull(d);
	}

}