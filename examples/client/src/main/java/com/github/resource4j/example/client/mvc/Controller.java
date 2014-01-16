package com.github.resource4j.example.client.mvc;

import javax.swing.JComponent;

public interface Controller<V extends JComponent> {
	
	V getView();
	
}
