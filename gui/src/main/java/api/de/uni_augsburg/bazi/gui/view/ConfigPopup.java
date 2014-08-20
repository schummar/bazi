package de.uni_augsburg.bazi.gui.view;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.PopupControl;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.util.ResourceBundle;

public class ConfigPopup extends PopupControl
{
	private final Node owner;
	private Object controller = null;

	public ConfigPopup(Node owner, String fxml)
	{
		this.owner = owner;

		StackPane root = new StackPane();
		root.getStylesheets().add("/de/uni_augsburg/bazi/gui/view/config_popup.css");
		root.getStyleClass().add("popup");
		getScene().setRoot(root);

		StackPane connector = new StackPane();
		connector.getStyleClass().add("connector");
		StackPane wrapper = new StackPane();
		wrapper.getStyleClass().add("wrapper");
		root.getChildren().addAll(connector, wrapper);


		try
		{
			FXMLLoader loader = new FXMLLoader(
				ConfigPopup.class.getResource(fxml),
				ResourceBundle.getBundle("de.uni_augsburg.bazi.common.bazi")
			);
			Parent content = loader.load();
			wrapper.getChildren().add(content);
			controller = loader.getController();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		setAutoHide(true);
		root.setOnKeyPressed(e -> hide());
	}

	public void open()
	{
		Bounds pos = owner.localToScreen(owner.getBoundsInLocal());
		double x = pos.getMaxX();
		double y = (pos.getMinY() + pos.getMaxY()) / 2;
		show(owner, x, y);
	}


	public static void open(Node node, String fxml)
	{
		new ConfigPopup(node, fxml).open();
	}


	public Object contentController()
	{
		return controller;
	}
}
