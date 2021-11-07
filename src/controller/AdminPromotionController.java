package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import database.DBInitialize;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ProductItem;
import model.Promotion;

public class AdminPromotionController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private TableView<Promotion> tb_promo;

	private TableColumn<Promotion, String> col_item_id;

	private TableColumn<Promotion, String> col_item_name;

	private TableColumn<Promotion, String> col_item_product_id;

	private TableColumn<Promotion, String> col_item_percentage;

	private TableColumn<Promotion, String> col_item_product_name;

	private ObservableList<Promotion> promoData = FXCollections.observableArrayList();
	private ObservableList<String> productdata = FXCollections.observableArrayList();

	@FXML
	private JFXComboBox <String> cbo_product;
	
	@FXML
	private JFXTextField tf_id;

	@FXML
	private JFXTextField tf_name;

	@FXML
	private JFXTextField tf_product_id;

	@FXML
	private JFXTextField tf_percentage;

	@FXML
	private JFXButton bt_add;

	@FXML
	private JFXTextField tf_product_name;

	@FXML
	private JFXButton bt_new;

	@FXML
	void initialize() throws Exception {
		

		col_item_id = new TableColumn<Promotion, String>("ID");
		col_item_name = new TableColumn<Promotion, String>("Name");
		col_item_product_id = new TableColumn<Promotion, String>("Product ID");
		col_item_product_name = new TableColumn<Promotion, String>("Product Name");
		col_item_percentage = new TableColumn<Promotion, String>("Percentage");
		
		col_item_id.setMinWidth(100.0);
		col_item_name.setMinWidth(250.0);
		col_item_product_id.setMinWidth(100.0);
		col_item_product_name.setMinWidth(400.0);
		col_item_percentage.setMinWidth(120.0);
		
		col_item_id.setStyle("-fx-font-size: 18");
		col_item_name.setStyle("-fx-font-size: 18");
		col_item_product_id.setStyle("-fx-font-size: 18; -fx-alignment: CENTER");
		col_item_product_name.setStyle("-fx-font-size: 18");
		col_item_percentage.setStyle("-fx-font-size: 18; -fx-alignment: CENTER");
		
		col_item_id.setCellValueFactory(new PropertyValueFactory<Promotion, String>("id"));
		col_item_name.setCellValueFactory(new PropertyValueFactory<Promotion, String>("name"));
		col_item_product_id.setCellValueFactory(new PropertyValueFactory<Promotion, String>("productId"));
		col_item_product_name.setCellValueFactory(new PropertyValueFactory<Promotion, String>("productName"));
		col_item_percentage.setCellValueFactory(new PropertyValueFactory<Promotion, String>("percentage"));
		
		tb_promo.getColumns().addAll(col_item_id, col_item_name, col_item_product_id, col_item_product_name,
				col_item_percentage);

		// get data from db
		String query = "SELECT discount.id, discount.name, discount.productbarcode, productitems.name,discount.percentage, discount.description FROM discount, productitems WHERE discount.productbarcode = productitems.barcode";
						
		new DBInitialize().dBInitializer();
		new DBInitialize();
		ResultSet rs = DBInitialize.statement.executeQuery(query);
		while (rs.next()) {
			Promotion pro = new Promotion();
			pro.setId(rs.getString(1));
			pro.setName(rs.getString(2));
			pro.setProductId(rs.getString(3));
			pro.setProductName(rs.getString(4));
			pro.setPercentage(rs.getString(5));
		
			promoData.add(pro);
		}

		// set data to table
		tb_promo.setItems(promoData);

		// row action
		tb_promo.setRowFactory(t -> {
			TableRow<Promotion> row = new TableRow<>();
			row.setOnMouseClicked(e -> {
				// get data from selected row

				if (e.getClickCount() == 2 && (!row.isEmpty())) {
					Promotion pro = tb_promo.getSelectionModel().getSelectedItem();
					System.out.println("Double click is: " + pro.getName());

				
					// set data to tf
					tf_id.setText(pro.getId());
					tf_name.setText(pro.getName());
					tf_product_id.setText(pro.getProductId());
					tf_product_name.setText(pro.getProductName());
					
					tf_percentage.setText(pro.getPercentage());
					
				}
			});

			final ContextMenu rowMenu = new ContextMenu();

			MenuItem removeItem = new MenuItem("Delete");
			removeItem.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Promotion pro = tb_promo.getSelectionModel().getSelectedItem();

					Alert alert = new Alert(AlertType.CONFIRMATION, "Are U Sure To Delete " + pro.getName() + " ?",
							ButtonType.YES, ButtonType.NO);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.YES) {
						// do stuff
						String removequery = "DELETE FROM `discount` WHERE discount.id = '" + pro.getId() + "';";
						try {
							new DBInitialize().dBInitializer();
							new DBInitialize();
							DBInitialize.statement.executeUpdate(removequery);

							// update table
							// update table data
							new DBInitialize().dBInitializer();
							String queryupdatetable = "SELECT discount.id, discount.name, discount.productbarcode, discount.name,discount.percentage, discount.description FROM discount, productitems WHERE discount.productbarcode = productitems.barcode";

							new DBInitialize();
							ResultSet rsu = DBInitialize.statement.executeQuery(queryupdatetable);
							
							promoData.clear();

							while (rsu.next()) {
								Promotion p = new Promotion();
								p.setId(rsu.getString(1));
								p.setName(rsu.getString(2));
								p.setProductId(rsu.getString(3));
								p.setProductName(rsu.getString(4));
								p.setPercentage(rsu.getString(5));
							

								promoData.add(p);
							}
							// tb_product_item.getItems().clear();
							tb_promo.refresh();

							/*
							 * //show alert Alert al = new Alert(AlertType.INFORMATION, "Item removed!");
							 * al.showAndWait();
							 */

							// show alert
							Alert al = new Alert(AlertType.INFORMATION, "Item deleted!");
							al.showAndWait();

						} catch (ClassNotFoundException | SQLException | InstantiationException
								| IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			});
			rowMenu.getItems().addAll(removeItem);

			// only display context menu for non-null items:
			row.contextMenuProperty().bind(
					Bindings.when(Bindings.isNotNull(row.itemProperty())).then(rowMenu).otherwise((ContextMenu) null));

			return row;

		});

		/////////////////////////////////////////////////////////////////
		
		try {
			
			String queryProduct = "SELECT `barcode`, `name` FROM `productitems`";
		
			new DBInitialize().dBInitializer();
			new DBInitialize();
			ResultSet rsp = DBInitialize.statement.executeQuery(queryProduct);
			ProductItem item ;
			while (rsp.next()) {
				item =new ProductItem();
				item.setBarcode(rsp.getString(1));
				item.setName(rsp.getString(2));
				productdata.add(item.getBarcode()+" - "+item.getName());
				System.out.println(item.getBarcode()+" - "+item.getName());
			}
			cbo_product.setItems(productdata);
			
		
		}catch(Exception e) {
			
			System.out.println(e.getMessage());
			
		}
		
		
	}
	
	@FXML
	void oncbochanged(ActionEvent event)throws Exception
	{
	
		String cboproducttext =cbo_product.getValue();
		String[] parts = cboproducttext.split(" - ");
		tf_product_id.setText(parts[0]);
		tf_product_name.setText(parts[1]);
		
	}
	
	
	

	@FXML
	void onNewAction(ActionEvent event)
			throws Exception {

		tf_id.clear();
		tf_name.clear();
		tf_product_id.clear();
		tf_product_name.clear();
		tf_percentage.clear();
	
		String query = "SELECT discount.id FROM discount ORDER BY discount.id DESC LIMIT 1";

		String oldid = "";

		new DBInitialize().dBInitializer();
		new DBInitialize();
		ResultSet rs = DBInitialize.statement.executeQuery(query);
		while (rs.next()) {
			oldid = "" + rs.getInt(1);
		}

		// count +1 new Id
		String newId = "" + (Integer.parseInt(oldid) + 1);
		tf_id.setText(newId);

	}

	@FXML
	void onAddAction(ActionEvent event)
			throws Exception {
		
		if(tf_id.getText().equals("") ||tf_name.getText().equals("") || tf_product_id.getText().equals("") ||
				tf_percentage.getText().matches(".*[a-zA-Z]+.*")   
				) {
			Alert al = new Alert(AlertType.ERROR, "Invalid input or data missing!");
			al.showAndWait();
		}
		else {

		String id = tf_id.getText().toString();
		String name = tf_name.getText().toString();
		String productid = tf_product_id.getText().toString();
		String percentage = tf_percentage.getText().toString();
		

		if (percentage.equals("")) {
			percentage = "0";
		}

		
		try {
		String addquery = "INSERT INTO `discount`(`id`, `name`, `productbarcode`, `percentage`) "
				+ "VALUES ('" + id + "','" + name + "','" + productid + "','" + percentage +"')";

		new DBInitialize().dBInitializer();
		new DBInitialize();
		DBInitialize.statement.executeUpdate(addquery);

		// update table
		String query = "SELECT discount.id, discount.name, discount.productbarcode, productitems.name,discount.percentage FROM discount, productitems WHERE discount.productbarcode = productitems.barcode";
		new DBInitialize().dBInitializer();
		new DBInitialize();
		ResultSet rs = DBInitialize.statement.executeQuery(query);

		// clear promodata array
		promoData.clear();

		while (rs.next()) {
			Promotion pro = new Promotion();
			pro.setId(rs.getString(1));
			pro.setName(rs.getString(2));
			pro.setProductId(rs.getString(3));
			pro.setProductName(rs.getString(4));
			pro.setPercentage(rs.getString(5));
			

			promoData.add(pro);
		}

		// set data to table
		tb_promo.refresh();
		
		tf_id.clear();
		tf_name.clear();
		tf_product_id.clear();
		tf_product_name.clear();
		tf_percentage.clear();
		
		// show alert
		Alert al = new Alert(AlertType.INFORMATION, "Item added!");
		al.showAndWait();

		}//end of try
    	catch(Exception ex) {
    		Alert al = new Alert(AlertType.ERROR, ""+ex.getMessage());
    		al.showAndWait();
    	}
		
		}//end of else
	}

	@FXML
	void onUpdateAction(ActionEvent event)
			throws Exception {

		if(tf_id.getText().equals("") ||tf_name.getText().equals("") || tf_product_id.getText().equals("") ||
				tf_percentage.getText().matches(".*[a-zA-Z]+.*")   
				) {
			Alert al = new Alert(AlertType.ERROR, "Invalid input or data missing!");
			al.showAndWait();
		}
		else {
			
	
		String id = tf_id.getText().toString();
		String name = tf_name.getText().toString();
		String productid = tf_product_id.getText().toString();
		String percentage = tf_percentage.getText().toString();
		

	

		String updatequery = "UPDATE `discount` SET `name`='" + name + "',`productbarcode`='" + productid
				+ "',`percentage`='" + percentage +  "' WHERE discount.id = '" + id + "';";

		new DBInitialize().dBInitializer();
		new DBInitialize();
		DBInitialize.statement.executeUpdate(updatequery);

		// update table
		String query = "SELECT discount.id, discount.name, discount.productbarcode, productitems.name,discount.percentage, discount.description FROM discount, productitems WHERE discount.productbarcode = productitems.barcode";
		new DBInitialize().dBInitializer();
		new DBInitialize();
		ResultSet rs = DBInitialize.statement.executeQuery(query);

		// clear promodata array
		promoData.clear();

		while (rs.next()) {
			Promotion pro = new Promotion();
			pro.setId(rs.getString(1));
			pro.setName(rs.getString(2));
			pro.setProductId(rs.getString(3));
			pro.setProductName(rs.getString(4));
			pro.setPercentage(rs.getString(5));
			

			promoData.add(pro);
		}

		// set data to table
		tb_promo.refresh();

		
		tf_id.clear();
		tf_name.clear();
		tf_product_id.clear();
		tf_product_name.clear();
		tf_percentage.clear();
	
		
		// show alert
		Alert al = new Alert(AlertType.INFORMATION, "Item updated!");
		al.showAndWait();
	}
		
	}//end of else

}
