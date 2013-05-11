package com.example.europeana_crawler;

import com.vaadin.Application;
import com.vaadin.ui.*;

/**
 * Main application class.
 */
public class Europeana_crawlerApplication extends Application {
 
    @Override
    public void init() {
        Window mainWindow = new Window("Europeana_crawler Application");
        Label label = new Label("Hello Vaadin user");
        mainWindow.addComponent(label);
        setMainWindow(mainWindow);
    }

}

