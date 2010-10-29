package com.flickberry.main;

import com.flickberry.view.TestScreen;

import net.rim.device.api.ui.UiApplication;

public class Main extends UiApplication {
	public static void main(String[] args) {
		Main main = new Main();
		main.enterEventDispatcher();
	}

	public Main() {
		TestScreen test = new TestScreen();
		pushScreen(test);
	}
}
