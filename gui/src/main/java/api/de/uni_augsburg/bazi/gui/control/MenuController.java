package de.uni_augsburg.bazi.gui.control;

import javafx.beans.binding.Binding;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

/**
 * Created by Marco on 07.07.2014.
 */
public class MenuController
{
	private final Binding<Window> window;

	private final MenuItem menuFileOpen;
	private final MenuItem menuFileReopen;
	private final MenuItem menuFileRestart;
	private final MenuItem menuFileSave;
	private final MenuItem menuFileOptions;
	private final MenuItem menuFileQuit;

	private final MenuItem menuDatabase;

	private final MenuItem menuHelpAbout;
	private final MenuItem menuHelpChangelog;
	private final MenuItem menuHelpUpdate;

	public MenuController(
		Binding<Window> window,
		MenuItem menuFileOpen,
		MenuItem menuFileReopen,
		MenuItem menuFileRestart,
		MenuItem menuFileSave,
		MenuItem menuFileOptions,
		MenuItem menuFileQuit,
		MenuItem menuDatabase,
		MenuItem menuHelpAbout,
		MenuItem menuHelpChangelog,
		MenuItem menuHelpUpdate)
	{
		this.window = window;
		this.menuFileOpen = menuFileOpen;
		this.menuFileReopen = menuFileReopen;
		this.menuFileRestart = menuFileRestart;
		this.menuFileSave = menuFileSave;
		this.menuFileOptions = menuFileOptions;
		this.menuFileQuit = menuFileQuit;
		this.menuDatabase = menuDatabase;
		this.menuHelpAbout = menuHelpAbout;
		this.menuHelpChangelog = menuHelpChangelog;
		this.menuHelpUpdate = menuHelpUpdate;

		menuFileOpen.setOnAction(
			e -> {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("sdfkjsdgfdjkfg");
				File file = fileChooser.showOpenDialog(window.getValue());
			}
		);
	}
}
