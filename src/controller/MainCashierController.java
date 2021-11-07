package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;


import database.DBInitialize;
import functs.DailySale;
import functs.EditingCell;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.input.KeyEvent;

import javafx.stage.Stage;
import javafx.util.Callback;
import livedata.cahierdata;
import model.ProductItem;
import model.Sale;

import reports.P;



public class MainCashierController {

	@FXML
	private Label lb_cashier_name;
	
	@FXML
	private Label lbldailySale;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private JFXButton bt_logout;

	@FXML
	private JFXTextField tf_barcode_search;

	@FXML
	private JFXButton bt_new;

	@FXML
	private TableView<ProductItem> tb_total_item;

	private TableColumn<ProductItem, String> col_item_name;

	private TableColumn<ProductItem, String> col_item_category;

	private TableColumn<ProductItem, String> col_item_price;

	private TableColumn<ProductItem, String> col_item_barcode;

	private TableColumn<ProductItem, String> col_item_stock;

	private ObservableList<ProductItem> data = FXCollections.observableArrayList();

	private static ObservableList<Sale> purchasedata = FXCollections.observableArrayList();;

	@FXML
	private TableView<Sale> tb_sale;

	private TableColumn<Sale, String> col_purchase_barcode;

	private TableColumn<Sale, String> col_purchase_name;

	private TableColumn<Sale, String> col_purchase_price;

	private TableColumn<Sale, Double> col_purchase_quantity;

	private TableColumn<Sale, String> col_purchase_discount;

	private TableColumn<Sale, String> col_purchase_totalamount;

	@FXML
	private JFXButton bt_pay;

	@FXML
	private JFXTextField tf_total;

	@FXML
	private JFXTextField tf_pay_amount;

	@FXML
	private JFXTextField tf_change;

	@FXML
	private JFXTextField tf_name_search;

	@FXML
	private JFXButton btPrint;

	@FXML
	private Label lb_slip_no;

	

	private Socket s;
	DataInputStream inputFromClient;
	DataOutputStream outputToClient;
	ServerSocket ss;

	private Thread th;
	/*
	 * private Thread th1; private Thread th2;
	 */

	// public static Thread thcashier;
	
	
	
	
	//logout button click action
	//logout and shaow login screen
	
	@FXML
	void onLogoutClick(ActionEvent event) {

		// scene transaction
		try {
			new LoginPg().start((Stage) bt_logout.getScene().getWindow());
			th.interrupt();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	//initialize  before start
	@FXML
	void initialize() throws Exception {
	
		
		
		// set slip number
		new DBInitialize().dBInitializer();
		String previousgetpurchaseid = " SELECT `id` FROM `purchase` ORDER BY `id` DESC LIMIT 1 ";
				
		new DBInitialize();
		ResultSet rsslip = DBInitialize.statement.executeQuery(previousgetpurchaseid);
		String previousid = "0";
		while (rsslip.next()) {
			previousid = rsslip.getString("id");
		}
		int nowid = Integer.parseInt(previousid) + 1;

		lb_slip_no.setText("" + nowid);   // set slip number of the bill
		cahierdata.slipno = "" + nowid;			// set slip number to livedata object

		
		bt_new.fire();//ready for accept new customer when stat
		
		//daily Sale
		DailySale ds=new DailySale();
		lbldailySale.setText(ds.empDailySale());
		
		
		//set alignment to total pay amount and balance text fields

		tf_total.setAlignment(Pos.BOTTOM_RIGHT);
		tf_pay_amount.setAlignment(Pos.BOTTOM_RIGHT);
		tf_change.setAlignment(Pos.BOTTOM_RIGHT);
		
		//----------------------------------------------------------------------------------------------------------
		
		
		
		//total item table design begins 
		
		//initialize clolumns total item table

		col_item_name = new TableColumn<ProductItem, String>("Name");
		col_item_category = new TableColumn<ProductItem, String>("Category");
		col_item_price = new TableColumn<ProductItem, String>("Price");
		col_item_barcode = new TableColumn<ProductItem, String>("Barcode");
		col_item_stock = new TableColumn<ProductItem, String>("Stock");

		
		//set total table min width
		col_item_name.setMinWidth(290.0);
		col_item_category.setMinWidth(160.0);
		col_item_price.setMinWidth(100.0);
		col_item_barcode.setMinWidth(80.0);
		col_item_stock.setMinWidth(90.0);

		
		//set total table font size
		col_item_name.setStyle("-fx-font-size: 18");
		col_item_category.setStyle("-fx-font-size: 18");
		col_item_price.setStyle("-fx-font-size: 18; -fx-alignment: CENTER");
		col_item_barcode.setStyle("-fx-font-size: 18");
		col_item_stock.setStyle("-fx-font-size: 18; -fx-alignment: CENTER");

		
		
		
		//give cell names to extracted data row to access easially
		
		col_item_name.setCellValueFactory(new PropertyValueFactory<ProductItem, String>("name"));
		col_item_category.setCellValueFactory(new PropertyValueFactory<ProductItem, String>("categoryname"));
		col_item_price.setCellValueFactory(new PropertyValueFactory<ProductItem, String>("price"));
		col_item_barcode.setCellValueFactory(new PropertyValueFactory<ProductItem, String>("barcode"));
		col_item_stock.setCellValueFactory(new PropertyValueFactory<ProductItem, String>("stockamount"));

		
		
		//add column items to the total table
		
		tb_total_item.getColumns().addAll(col_item_barcode, col_item_name, col_item_category, col_item_price,
				col_item_stock);
		
		
		//
		
		
		// End total item table design
		
		
		//
		
		
		
		//---------------------------------------------------------------------------------------------------------------------------------
		
			

		// purchase table design begins 
		
		
		// initialize column variables for sale table

		col_purchase_barcode = new TableColumn<Sale, String>("Barcode");
		col_purchase_name = new TableColumn<Sale, String>("Name");
		col_purchase_price = new TableColumn<Sale, String>("Price");
		col_purchase_quantity = new TableColumn<Sale, Double>("Quantity");
		col_purchase_discount = new TableColumn<Sale, String>("Discount");
		col_purchase_totalamount = new TableColumn<Sale, String>("TotalAmount");
		
			
		
		//set sale table min width
		col_purchase_barcode.setMinWidth(70.0);
		col_purchase_name.setMinWidth(120.0);
		col_purchase_price.setMinWidth(50.0);
		col_purchase_quantity.setMinWidth(20.0);
		col_purchase_discount.setMinWidth(25.0);
		col_purchase_totalamount.setMinWidth(60.0);

		
		
		
		//set sale table font size
		col_purchase_barcode.setStyle("-fx-font-size: 15");
		col_purchase_name.setStyle("-fx-font-size: 15");
		col_purchase_price.setStyle("-fx-font-size: 15; -fx-alignment: CENTER");
		col_purchase_quantity.setStyle("-fx-font-size: 15; -fx-alignment: CENTER");
		col_purchase_discount.setStyle("-fx-font-size: 15; -fx-alignment: CENTER");
		col_purchase_totalamount.setStyle("-fx-font-size: 15; -fx-alignment: CENTER");
		
		
		
		
		//update table cell when changes

		Callback<TableColumn<Sale, Double>, TableCell<Sale, Double>> cellFactory = (
				TableColumn<Sale, Double> param) -> new EditingCell();
				
				
				
				
		//give cell names to extracted data row to access easially
				
		col_purchase_barcode.setCellValueFactory(new PropertyValueFactory<Sale, String>("barcode"));
		col_purchase_name.setCellValueFactory(new PropertyValueFactory<Sale, String>("name"));
		col_purchase_price.setCellValueFactory(new PropertyValueFactory<Sale, String>("unitamount"));

		col_purchase_quantity.setCellValueFactory(new PropertyValueFactory<Sale, Double>("quantity"));
		
		
		// update quentity column changes 
		col_purchase_quantity.setCellFactory(cellFactory);
		
		
		//
		//Add Action commit other changes when edit occured
		//-------------------------------------------------------------------------------------------------------------------------
		
		
		col_purchase_quantity.setOnEditCommit(new EventHandler<CellEditEvent<Sale, Double>>() {
			@Override
			public void handle(CellEditEvent<Sale, Double> t) {
				((Sale) t.getTableView().getItems().get(t.getTablePosition().getRow())).setQuantity((t.getNewValue()));

				System.out.println("Qty edit Working");

				t.getRowValue().setQuantity(t.getNewValue());
				double qty = ((Sale) t.getTableView().getItems().get(t.getTablePosition().getRow())).getQuantity(); // get updated quentity

				String barc = ((Sale) t.getTableView().getItems().get(t.getTablePosition().getRow()))	// get discount rate
						.getBarcode();
				
				double discountpercent=0d;
				///to be edit
				try {
				new DBInitialize().dBInitializer();
				String dispercent = " SELECT `percentage` FROM `discount` WHERE `productbarcode`='"+barc+"'";
						
				new DBInitialize();
				ResultSet percentage = DBInitialize.statement.executeQuery(dispercent);
				
				if (percentage.next()) {
					discountpercent = percentage.getDouble(1);
				}
				
				
				}catch(Exception e) {
					
					System.out.println("Errror getting Discount");
				}
				
				
				///
				
				
				
				
				
				
				String itemmId = ((Sale) t.getTableView().getItems().get(t.getTablePosition().getRow())).getBarcode(); // get barcode

				double unitprice = t.getRowValue().getUnitamount();    //get unit amount
				
				double total1 = unitprice * qty;		//calculate new total after user change amount		total1 has normal value for total bought quantity

				
				Double discount=0d;
				
				double total = 0;
				
				
				// check discounts
				
				if (discountpercent>0d) {

					discount	=(total1 * discountpercent )/ 100;
					
					//if availble discounts calculate new price after discount
				
					total = total1 - discount;									//total has the price after discounted
				}

				

				t.getRowValue().setTotalamount(total);				//handler replace total after discount to the table cell
				t.getRowValue().setDiscount(discount);
				
				tb_sale.refresh();									//refresh table and cells				
				tb_sale.getColumns().get(0).setVisible(false);
				tb_sale.getColumns().get(0).setVisible(true);		

				
				
				double totalall = 0;
				for (Sale i : tb_sale.getItems()) {
					totalall += i.getTotalamount();					//using sell model class count get price
				}
				//tf_total.setText("" + totalall);				//update new total
				
				
				tf_total.setText(new DecimalFormat("0.00").format(totalall));
				
				cahierdata.paybleAmount = Double.parseDouble(tf_total.getText()); //update livedata object too tf_total.setText(new DecimalFormat("0.00").format(totalall));
				System.out.println("Total amount is : " + cahierdata.paybleAmount);
				// tb_sale.refresh();
			}
		});
		
		//End commit other changes after edit 
		//--------------------------------------------------------------------------------------------------------------------------------------------
		
		// continous after commit qty changes  
		//give cell names to extracted data row to access easially cont...

		col_purchase_discount.setCellValueFactory(new PropertyValueFactory<Sale, String>("discount"));

		/*
		 * col_purchase_totalamount.setCellValueFactory(new
		 * Callback<CellDataFeatures<Sale, String>, ObservableValue<String>>() {
		 * 
		 * 
		 * 
		 * public ObservableValue<String> call(CellDataFeatures<Sale, String> param) {
		 * 
		 * 
		 * double total = param.getValue().getQuantity() *
		 * Double.parseDouble(param.getValue().getUnitamount());
		 * 
		 * 
		 * return new SimpleStringProperty(""+total);
		 * 
		 * }
		 * 
		 * });
		 */
		col_purchase_totalamount.setCellValueFactory(new PropertyValueFactory<Sale, String>("totalamount"));

		/*
		 * col_purchase_totalamount.setCellValueFactory(cellData -> { Sale data =
		 * cellData.getValue(); return Bindings.createDoubleBinding( () -> { try {
		 * double price = data.getUnitamount(); double quantity = data.getQuantity();
		 * return price * quantity ; } catch (NumberFormatException nfe) { return 0 ; }
		 * }, data.totalamountProperty(), data.quantityProperty() ); });
		 */
		
		
		tb_sale.setEditable(true); // make sale table editable
		tb_sale.setItems(purchasedata); // serve list of data source to the sell table
		
		
		// set sell table columns
		tb_sale.getColumns().addAll(col_purchase_barcode, col_purchase_name, col_purchase_price, col_purchase_quantity,
				col_purchase_discount, col_purchase_totalamount);
		
		//refresh the table
		tb_sale.refresh();

		/*
		 * col_item_id.setCellValueFactory(new Callback<CellDataFeatures<ProductItem,
		 * String>, ObservableValue<String>>() {
		 * 
		 * public ObservableValue<String> call(CellDataFeatures<ProductItem, String>
		 * param) {
		 * 
		 * return new SimpleStringProperty(""); } });
		 */

		
		
		// set cashier name
		lb_cashier_name.setText(cahierdata.cashierrec.getName());

		
		
		
		
		//---------------------------------------------------------------------------------------------------------------------------------------
		//database quering to table begins
		
		
		// get data from db and set it to table
		new DBInitialize().dBInitializer();

		String tablequery = "SELECT productitems.barcode, productitems.name, productcategory.name, productitems.price, supplier.companyname, productitems.dateadded, productitems.stockamount, productitems.expireddate FROM productitems, supplier,productcategory WHERE productitems.categoryid = productcategory.id AND productitems.supplierid = supplier.id ORDER BY productitems.barcode DESC;";
		ResultSet rs = DBInitialize.statement.executeQuery(tablequery);
		while (rs.next()) {

			ProductItem p = new ProductItem();
			p.setBarcode(rs.getString(1));
			p.setName(rs.getString(2));
			p.setCategoryname(rs.getString(3));
			p.setPrice(new DecimalFormat("0.00").format(Double.parseDouble(rs.getString(4))));
			p.setSuppliername(rs.getString(5));
			p.setDateadded(rs.getString(6));
			p.setStockamount(rs.getString(7));
			p.setExpiredate(rs.getString(8));

			data.add(p);  // add product items model object to list (data)
		}

		tb_total_item.setItems(data);      // fill data to the total item table

		
		
		
		
		//Add Action when double click on a row in total table 
		tb_total_item.setRowFactory(t -> {
			TableRow<ProductItem> row = new TableRow<>();
			row.setOnMouseClicked(e -> {
				// get data from selected row
				// ProductItem productItem =
				// tb_total_item.getSelectionModel().getSelectedItem();
				// System.out.println("Select row is : "+productItem.getName());

				
				//if double clicked and not a empt row
				if (e.getClickCount() == 2 && (!row.isEmpty())) {
					
					
					
					String dispercentage = "0";
					double discount=0d;

					//get selected row data to the product item model
					ProductItem product = tb_total_item.getSelectionModel().getSelectedItem();
					
					
					System.out.println("Double click is: " + product.getName());

					// get discount form db using selected item barcode
					
					String discountQuery = "SELECT ((discount.percentage* productitems.price*1)/100)AS percentage FROM `discount`,productitems WHERE discount.productbarcode ='"+ product.getBarcode()+"' AND productitems.barcode=discount.productbarcode";
							
					
					try {
						new DBInitialize().dBInitializer();
						new DBInitialize();
						ResultSet rsd = DBInitialize.statement.executeQuery(discountQuery);

						if (rsd.next()) {
							dispercentage = rsd.getString(1);  //percentage
							
						} else {
							System.out.println("no discount");
						}

						System.out
								.println("percentage from db is ::::" + dispercentage );
						
						//selected item of product item model data updated with discounts
						product.setDiscount(dispercentage); 
												

					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					
					//-------------------------------------------------------------------------
								//End deal with total item table and product model
					//---------------------------------------------------------------------
					
					
					
					
					
					//-------------------------------------------------------------------------
					//Begin to deal with sale item table
					//---------------------------------------------------------------------
					
					// create virtual sale item
					Sale sa = new Sale();

					
					
					double total = 0;
					// discounts handling
					
					
					if (dispercentage.equals("0")) {
						//if not exist discounts
						sa.setTotalamount(Double.parseDouble(product.getPrice()));            //get unit price   
						System.out.println("dispercent 0 is working");
						
					//	new DecimalFormat("0.00").format(totalall)
					} else {
						
						//if discount exist
						System.out.println("unit price is :::" + product.getPrice());
						System.out.println("discount percnet is :::" + dispercentage);   //dispercentage get from database
						
						
						double tominuspromotion = 	//get amount of discount
								(Double.parseDouble(dispercentage) );//removed /100
						
						
						total = Double.parseDouble(product.getPrice()) - tominuspromotion;		//count total after deduct discount= total
						System.out.println("unit amount after discount is : " + total);

						sa.setTotalamount(Double.parseDouble(total + ""));					//set total of sale object
						System.out.println("discount percent compute is working");
					}
					
					
					// set sale data
					
					sa.setBarcode("" + product.getBarcode());
					sa.setName(product.getName());
					sa.setQuantity(1);						//set quantity as 1 without asking     can develop later
					sa.setUnitamount(Double.parseDouble(product.getPrice()));
					sa.setDiscount(Double.parseDouble(product.getDiscount()));
					

					// double paybleAmount = count * Double.parseDouble(sale.getUnitamount());
					// sa.setTotalamount(Double.parseDouble(""+product.getPrice()));

					
					
					//sales model objects add to list call - purchase data
					
					purchasedata.add(sa);
					tb_sale.refresh();
					
					//start counting purchased items total
					double totalall = 0;
					for (Sale i : tb_sale.getItems()) {
						totalall += i.getTotalamount();
					}
					
					
					tf_total.setText(new DecimalFormat("0.00").format(totalall));   // display current total
					
					
					cahierdata.paybleAmount = Double.parseDouble(tf_total.getText()); // set livedata object with current total amount
					System.out.println("Total amount is : " + cahierdata.paybleAmount);
				}
			});

			return row;
		});
		
		//-------------------------------------------------------------
		//end double click action on a total item table
		//-------------------------------------------------------------
		
		
		
		//-------------------------------------------------------------
		//start  double click action on a sale item table
		//-------------------------------------------------------------
		
		

		tb_sale.setRowFactory(t -> {
			TableRow<Sale> r = new TableRow<>();
			r.setOnMouseClicked(e -> {
				// get data from selected row
				// ProductItem productItem =
				// tb_total_item.getSelectionModel().getSelectedItem();
				// System.out.println("Select row is : "+productItem.getName());

				if (e.getClickCount() == 2 && (!r.isEmpty())) {
					Sale sale = tb_sale.getSelectionModel().getSelectedItem();
					System.out.println("sale Double click is: " + sale.getName());

				}

				tb_sale.refresh();
			});
			
			
			
			//-------------------------------------------------------------
			//End  double click action on a sale item table
			//-------------------------------------------------------------
			
			
			
			//add delete menu to the sale table
			
			final ContextMenu rowMenu = new ContextMenu();

			MenuItem removeItem = new MenuItem("Delete");
			
			//set action to delete menu item 
			removeItem.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					Sale s = tb_sale.getSelectionModel().getSelectedItem();

					Alert alert = new Alert(AlertType.CONFIRMATION, "Are U Sure To Delete " + s.getName() + " ?",
							ButtonType.YES, ButtonType.NO);
					alert.showAndWait();

					if (alert.getResult() == ButtonType.YES) {
						
						// reduce all total ammount and clear
						Sale se = purchasedata.get(tb_sale.getSelectionModel().getFocusedIndex());
						cahierdata.paybleAmount = cahierdata.paybleAmount - se.getTotalamount();
						tf_total.setText(new DecimalFormat("0.00").format(cahierdata.paybleAmount));
						tf_pay_amount.clear();
						tf_change.clear();
						purchasedata.remove(tb_sale.getSelectionModel().getFocusedIndex());

						tb_sale.refresh(); // recall and refill data

					}
				}
			});
			//end action rown menu
			
			//add menu item
			rowMenu.getItems().addAll(removeItem);
			
			

			// only display context menu for non-null items:
			r.contextMenuProperty().bind(
					Bindings.when(Bindings.isNotNull(r.itemProperty())).then(rowMenu).otherwise((ContextMenu) null));

			return r;
		});
		// display total
		// tf_total.textProperty().setValue();

		// tf_total.setText(""+total);

	}

	
	
	

	// for screen transaction from login to admin panel
	public class LoginPg extends Application {

		@Override
		public void start(Stage primaryStage) throws Exception {
			Parent root = FXMLLoader.load(getClass().getResource("/views/Page_login.fxml"));

			Scene scene = new Scene(root, 1320, 700);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Cashier");
			// primaryStage.sizeToScene();
			primaryStage.setResizable(false);
			primaryStage.setMaximized(false);
			primaryStage.show();
		}
	}
	
	

	// search product item table by barcode 
	@FXML
	void tfTypeSearchAction(KeyEvent event) throws ClassNotFoundException, SQLException, InterruptedException {


		th.sleep(1);   // because got a exception. this will let to run other thread

		tf_name_search.clear();

		/*
		 * if(!th.isInterrupted()) { th.start();
		 * System.out.println("------------------------------- xD"); }else {
		 * th.interrupt(); System.out.println("------------------------------- xP"); }
		 */

		String searchKey = tf_barcode_search.getText().toString();  		//get user typed barcode
		System.out.println("key entered is : " + searchKey);	
		
		//query to filter total item according the barcode. used like parameter
		String query = "SELECT productitems.barcode, productitems.name, productcategory.name, productitems.price, supplier.companyname, productitems.dateadded, productitems.stockamount, productitems.expireddate FROM productitems, supplier,productcategory WHERE productitems.categoryid = productcategory.id AND productitems.supplierid = supplier.id AND productitems.barcode LIKE '"
				+ "%"	+ searchKey + "%'";

		// new DBInitialize().DBInitialize();

		System.out.println("working");
		try {
			// ResultSet rs = st.executeQuery("SELECT * FROM USER");
			ResultSet rs = DBInitialize.statement.executeQuery(query);			//execute query
			ObservableList<ProductItem> row = FXCollections.observableArrayList();
			
			//get data to the list
			while (rs.next()) {

				ProductItem p = new ProductItem();
				p.setBarcode(rs.getString(1));
				p.setName(rs.getString(2));
				p.setCategoryname(rs.getString(3));
				p.setPrice(rs.getString(4));
				p.setSuppliername(rs.getString(5));
				p.setDateadded(rs.getString(6));
				p.setStockamount(rs.getString(7));
				p.setExpiredate(rs.getString(8));

				row.add(p); ////add to list 

			}
			tb_total_item.setItems(row);   //list serve data to the total item table
			// System.out.println("working1"+data);

			// tb_total_item.getItems().clear();
			// tb_total_item.setItems(data);

			System.out.println("working2");
			// data.getItems().addAll(row);
		} catch (SQLException ex) {

		}

	}
	
	
	
//serch product by name
	@FXML
	void tfNameSearchAction(KeyEvent event) throws InterruptedException {

		th.sleep(1);
		tf_barcode_search.clear();

		/*
		 * if(!th.isInterrupted()) { th.start();
		 * System.out.println("------------------------------- xD"); }else {
		 * th.interrupt(); System.out.println("------------------------------- xP"); }
		 */

		String searchKey = tf_name_search.getText().toString();
		System.out.println("key entered is : " + searchKey);
		
		//query to filter accoding to user typed name- like parameter used
		String query = "SELECT productitems.barcode, productitems.name, productcategory.name, productitems.price, supplier.companyname, productitems.dateadded, productitems.stockamount, productitems.expireddate FROM productitems, supplier,productcategory WHERE productitems.categoryid = productcategory.id AND productitems.supplierid = supplier.id AND productitems.name LIKE '"
						
				
				+ "%"	+ searchKey + "%'";

		// new DBInitialize().DBInitialize();

		System.out.println("working");
		try {
			// ResultSet rs = st.executeQuery("SELECT * FROM USER");
			
			//execute query
			ResultSet rs = DBInitialize.statement.executeQuery(query);
			ObservableList<ProductItem> row = FXCollections.observableArrayList();
			
			//add model objects to list
			while (rs.next()) {
				ProductItem p = new ProductItem();
				p.setBarcode(rs.getString(1));
				p.setName(rs.getString(2));
				p.setCategoryname(rs.getString(3));
				p.setPrice(rs.getString(4));
				p.setSuppliername(rs.getString(5));
				p.setDateadded(rs.getString(6));
				p.setStockamount(rs.getString(7));
				p.setExpiredate(rs.getString(8));

				row.add(p);
			}
			tb_total_item.setItems(row);
			// System.out.println("working1"+data);

			// tb_total_item.getItems().clear();
			// tb_total_item.setItems(data);

			System.out.println("working2");
			// data.getItems().addAll(row);
		} catch (SQLException ex) {

		}
		
		//------------------------------------------------------------------------------------------------------------------------------
		//end filtering
		//--------------------------------------------------------------------------------------------------------------------------------

	}
	
	
	//handle action when press enter after the input amount
	@FXML
	void onEnterButtonClick(ActionEvent event) {

		if (tf_pay_amount.getText().equals("")) {
			Alert al = new Alert(AlertType.ERROR, "No Input!");
			al.showAndWait();
		} else if (tf_pay_amount.getText().matches(".*[a-zA-Z]+.*")) {
			Alert al = new Alert(AlertType.ERROR, "Please input the right amount in number!");
			al.showAndWait();
		} else if (Double.parseDouble(tf_pay_amount.getText()) < cahierdata.paybleAmount) {
			// do nothing
			Alert al = new Alert(AlertType.ERROR, "Invalid amount!");
			al.showAndWait();
			tf_pay_amount.clear();
		} else {
			try {
				cahierdata.payamount = Double.parseDouble(tf_pay_amount.getText());
				cahierdata.change = cahierdata.payamount - cahierdata.paybleAmount;
				
				
			
				tf_change.setText(new DecimalFormat("0.00").format(cahierdata.change));
				
				
				System.out.println("Total Amount is: " + cahierdata.paybleAmount);
				System.out.println("Pay Amount is: " + cahierdata.payamount);
				System.out.println("Change is: " + cahierdata.change);
			} catch (Exception ex) {
				System.out.println("Error in payamount: " + ex.getMessage());
			}

		} // end of else
	}

	// customer number textfield  -may be can remove
	public class NumberTextField extends TextField {

		@Override
		public void replaceText(int start, int end, String text) {
			if (validate(text)) {
				super.replaceText(start, end, text);
			}
		}

		@Override
		public void replaceSelection(String text) {
			if (validate(text)) {
				super.replaceSelection(text);
			}
		}

		private boolean validate(String text) {
			return text.matches("[0-9]*");
		}
	}
	
	
	

	
	
	//pay button action
	@FXML
	void onBtPayClick(ActionEvent event) {
		// get all data from table and add to oberable list
		
		
		//if havent amount on total -do nothing
		if (tf_total.getText().equals("")) {
			System.out.println("Do nothing");
			Alert al = new Alert(AlertType.ERROR, "No item to sale");
			al.showAndWait();
		} else {
			
			//if has any purchased item
			//Insert Bill record
			
			try {
			
			new DBInitialize().dBInitializer();
			new DBInitialize();
			
			Timestamp t=new Timestamp(new Date().getTime());
			
			
			String billQuery ="INSERT INTO `bill`(`id`, `cashierid`, `date`, `type`, `amount`) VALUES "
					+ 		"("+ cahierdata.slipno +","+cahierdata.cashierrec.getId()+",'"+t.toString()+"','cash',"+cahierdata.paybleAmount+")";
			System.out.println("Statement Created \n"+ billQuery );
			
			DBInitialize.statement.executeUpdate(billQuery);

			
			System.out.println("Bill inserted" );

			ObservableList<Sale> saledata = FXCollections.observableArrayList();
			cahierdata.saleitemsdatafromsaletable = saledata;// add sale data to livedata list for generating report in pay action 

			}catch(Exception e) {
				
				System.out.println("Error on bill print");
			}
			
			//create list of sale items to send one row 
			
			ObservableList<Sale> saledata = FXCollections.observableArrayList();
			saledata = tb_sale.getItems();
			cahierdata.saleitemsdatafromsaletable = saledata;// for generating report in pay action
			cahierdata.billTotal=0d;
			try {
			
		
			String solditemInsetQuery=" INSERT INTO `purchase`(`billid`, `id`, `barcode`, `quantity`, `eachprice`, `totalamount`, `discount`) VALUES ";
			
			//insert values annd append its to query
			for (int i = 0 , itemid=1; i < saledata.size(); i++,itemid++) {
			
				
				String append="("+cahierdata.slipno+", "+itemid+", "+saledata.get(i).getBarcode() +", "+saledata.get(i).getQuantity()+", "+saledata.get(i).getUnitamount()+", "+saledata.get(i).getTotalamount()+", "+saledata.get(i).getDiscount()+"),";
				
				solditemInsetQuery+=append;
				System.out.println(cahierdata.slipno);
				cahierdata.billTotal+=saledata.get(i).getQuantity()*saledata.get(i).getUnitamount();
				cahierdata.discountTotal+=saledata.get(i).getDiscount();
				

			}
			
		
			solditemInsetQuery=solditemInsetQuery.substring(0,solditemInsetQuery.length()-1); //remove last comma
			System.out.println("Sold Items Query Created \n"+ solditemInsetQuery );
			
			//Insert sold product items to purchase table
			
			DBInitialize.statement.executeUpdate(solditemInsetQuery);

			
			System.out.println("Purchased Data inserted " );
			
			
			}catch(Exception e) {System.out.println("Error on sold item insert");}
			
		
			//Update Daily Sale
			DailySale ds=new DailySale();
			lbldailySale.setText(ds.empDailySale());
			
			
			
			
			
			
			
			
			
			
			
			//Update Inventory
			
			
			
			try {
			
				for (int i = 0 ; i < saledata.size(); i++) {
				
				
				}
			
			}catch(Exception e) {System.out.println("Error on Update Inventory");}
			
			
			
			
			
			
			}
		
	}
	
	//end pay action

	@FXML
	void onbtPrintClick(ActionEvent event) throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
		System.out.println("Printing Started");

		
		
		//String cashier=cahierdata.
		P p=new P();
		p.m();
		
        System.out.println("Printing Done");
	}

	@FXML
	void onbtNewClick(ActionEvent event)
			throws Exception {

		
	
		cahierdata.saleitemsdatafromsaletable.clear();
		// set slip number
		new DBInitialize().dBInitializer();
		String previousgetpurchaseid = " SELECT `id` FROM `bill` ORDER BY `id` DESC LIMIT 1 ";
		                             //    SELECT DISTINCT `billid` FROM `purchase` ORDER BY `billid` DESC LIMIT 1
		new DBInitialize();
		ResultSet rsslip = DBInitialize.statement.executeQuery(previousgetpurchaseid);
		String previousid = "0";
		while (rsslip.next()) {
			previousid = rsslip.getString("id");
		}
		int nowid = Integer.parseInt(previousid) + 1;

		lb_slip_no.setText("" + nowid);
		cahierdata.slipno = "" + nowid;

		
		// clear sale data
		purchasedata.clear();
		tb_sale.refresh();
		cahierdata.paybleAmount = 0;
		tf_total.clear();
		tf_pay_amount.clear();
		tf_change.clear();

		// update instock table
		// get data from db and set it to table
		new DBInitialize().dBInitializer();
		data.clear();

		String tablequery = "SELECT productitems.barcode, productitems.name, productcategory.name, productitems.price, supplier.companyname, productitems.dateadded, productitems.stockamount, productitems.expireddate FROM productitems, supplier,productcategory WHERE productitems.categoryid = productcategory.id AND productitems.supplierid = supplier.id ORDER BY productitems.barcode DESC;";
		ResultSet rs = DBInitialize.statement.executeQuery(tablequery);
		while (rs.next()) {

			ProductItem p = new ProductItem();
			p.setBarcode(rs.getString(1));
			p.setName(rs.getString(2));
			p.setCategoryname(rs.getString(3));
			p.setPrice(rs.getString(4));
			p.setSuppliername(rs.getString(5));
			p.setDateadded(rs.getString(6));
			p.setStockamount(rs.getString(7));
			p.setExpiredate(rs.getString(8));

			data.addAll(p);
		}

		tb_total_item.refresh();
		
		//tf_name_search.clear();
		//tf_barcode_search.clear();
		
	}
	


}
