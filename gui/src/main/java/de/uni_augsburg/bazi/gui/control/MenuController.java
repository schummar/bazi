package de.uni_augsburg.bazi.gui.control;

import com.google.common.base.Charsets;
import de.schummar.castable.Castable;
import de.schummar.castable.Data;
import de.uni_augsburg.bazi.common.Plugin;
import de.uni_augsburg.bazi.common.PluginManager;
import de.uni_augsburg.bazi.common.database.Tree;
import de.uni_augsburg.bazi.common.format.Format;
import de.uni_augsburg.bazi.zipdatabase.ZipDatabase;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Optional;

public class MenuController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);


	@FXML public MenuItem menuFileOpen;
	@FXML public MenuItem menuFileReopen;
	@FXML public MenuItem menuFileRestart;
	@FXML public MenuItem menuFileSave;
	@FXML public MenuItem menuFileOptions;
	@FXML public MenuItem menuFileQuit;

	@FXML public Menu menuDatabase;

	@FXML public MenuItem menuHelpAbout;
	@FXML public MenuItem menuHelpChangelog;
	@FXML public MenuItem menuHelpUpdate;


	public MainController main;
	private Castable last = null;

	@FXML void initialize()
	{
		menuFileOpen.setOnAction(this::open);
		new Thread(this::init).start();
	}

	private void open(ActionEvent e)
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open");
		File file = fileChooser.showOpenDialog(main.title.getScene().getWindow());
		int i = file.getName().lastIndexOf(".");
		String ending = i == -1 ? file.getName() : file.getName().substring(i + 1);
		load(ending, () -> new FileInputStream(file));
	}

	private void init()
	{
		ZipDatabase db = new ZipDatabase(new File("data.zip"));
		db.load();
		db.get().children.forEach(c -> init(menuDatabase, c));
	}

	private void init(Menu menu, Tree tree)
	{
		MenuItem child;
		if (tree.isLeaf())
		{
			child = new MenuItem(tree.name);
			child.setOnAction(e -> load(tree.ending, tree.loader));
		}
		else
		{
			child = new Menu(tree.name);
			tree.children.forEach(c -> init((Menu) child, c));
		}
		menu.getItems().add(child);
	}

	private void load(String ending, Tree.Loader loader)
	{
		try (InputStream stream = loader.load())
		{
			Plugin.Params params = Data.create(Plugin.Params.class);
			params.name(ending.toLowerCase());
			Optional<Format> format = PluginManager.tryInstantiate(Format.class, params);

			if (!format.isPresent())
			{
				LOGGER.error("unknown format");
				return;
			}

			last = format.get().deserialize(new InputStreamReader(stream, Charsets.UTF_8));
			main.data.src().overwrite(last);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
