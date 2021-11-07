package controller;


import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;

import database.DBInitialize;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import livedata.cahierdata;

public class LoginController{


	    @FXML // ResourceBundle that was given to the FXMLLoader
	    private ResourceBundle resources;

	    @FXML // URL location of the FXML file that was given to the FXMLLoader
	    private URL location;

	    @FXML // fx:id="tf_id"
	    private JFXTextField tf_id; // Value injected by FXMLLoader

	    @FXML // fx:id="tf_pass"
	    private JFXPasswordField tf_pass; // Value injected by FXMLLoader

	    @FXML // fx:id="bt_login"
	    private JFXButton bt_login; // Value injected by FXMLLoader
	    
	    @FXML
	    private JFXRadioButton bt_rdo_admin;

	    @FXML
	    private ToggleGroup usertype;

	    @FXML
	    private JFXRadioButton bt_rdo_cashier;
	    
	    //db
	   // private Statement statement;
	    private ResultSet resultSet;
	    private String dbQuery;
    	
	    private String realId ;
	    private String realPw ;
	    
		//strings for data from cashier db
	    private String name;
	    private int age;
	    private String gender;
	    private String addr;
	    private String ph;
	    private String mail;
	    private String date;

	    @FXML // This method is called by the FXMLLoader when initialization is complete
	    void initialize() throws Exception {
	       
	        //database
	        new DBInitialize().dBInitializer();;
	        
	        bt_rdo_cashier.setSelected(true);
	    }
	    



		@FXML
	    void bt_login_action(ActionEvent event) throws SQLException {
	    	//get dat from 2 text field
			try {
	    	String id = tf_id.getText().toString();
	    	String pw = tf_pass.getText().toString();

	    	
	    	
	    	if(bt_rdo_admin.isSelected()) {
	    		System.out.println("Admin is seleted");
		    	
		    	realId = "admin";             //ID 
		    	realPw = "admin";				//Password

        	if(id.equals(realId) && pw.equals(realPw)) {
        		
	    		System.out.println("Success!");
	    		
	    		//scene transaction
	    		try {
					new MainAdmin().start((Stage)bt_login.getScene().getWindow());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    		
	    	}
	    	else{
	    		//System.out.println("Login Fail! User name or password incorrect! because realid is "+ realId+" and id is "+id+" real password is "+ realPw+ " password is "+pw);
	    		Alert alert = new Alert(AlertType.ERROR, " Login Fail !");
				alert.showAndWait();
				tf_id.clear();
				tf_pass.clear();

				System.out.println("login fail Error showed");
	    	}
        	
	    	}//end of admin selected
	    	
	    	
	    	
	    	else {
	    		
		    		System.out.println("Cashier is seleted");
		    		if(!isInteger(id+"")) {
		    			tf_id.clear();
		    			tf_pass.clear();
		    			
		    			System.out.println("cashier id enter is String...");
		    			Alert all = new Alert(AlertType.ERROR, "Invalid input!");
		    			all.showAndWait();
		    			
		    			
		    		}
		    		else {

		    		//if(id.equals(int))
			    	//get user name and password from db
			    	dbQuery = "SELECT `id`, `name`, `dob`, `gender`, `address`, `phone`, `email`, `password`, `date created` FROM `cashier` WHERE `id`='"+id+"'";
			    	resultSet = DBInitialize.statement.executeQuery(dbQuery);
			    	
			    	if(resultSet.next()) {
			    	realId = ""+resultSet.getInt("id");
			    	realPw = resultSet.getString("password");
			    	
			    	name = resultSet.getString("name");
			    	
			    	System.out.println("name");
			    	
		
			    	Date dob =resultSet.getDate("dob");  	
			    	Date today= new Date();
			    	Date years=new Date (today.getTime()-dob.getTime());
			    	int age=years.getYear();
			    	System.out.println("pass");
			    	
		   			gender = resultSet.getString("gender");
			    	addr = resultSet.getString("address");
			    	ph = resultSet.getString("phone");
			    	mail = resultSet.getString("email");
			    	date = resultSet.getDate("date created").toString();
			    	
			    	System.out.println("Id is "+id);
			    	System.out.println("Password is ********");

		        	if(id.equals(realId) && pw.equals(realPw)) {
		        		
		        		//add cashier info to temp
		        		cahierdata.cashierrec.setId(""+realId);
		        		cahierdata.cashierrec.setPassword(realPw);
		        		cahierdata.cashierrec.setName(name);
		        		cahierdata.cashierrec.setGender(gender);
		        		cahierdata.cashierrec.setAddress(addr);
		        		cahierdata.cashierrec.setAge(""+age);
		        		cahierdata.cashierrec.setAge("24");
		        		cahierdata.cashierrec.setPhone(ph);
		        		cahierdata.cashierrec.setEmail(mail);
		        		cahierdata.cashierrec.setDateCreated(date);
			    		System.out.println("Success!");
			    		System.out.println("cashier name is : "+cahierdata.cashierrec.getName());
			    	
			    		//scene transaction
			    		try {
							new MainCashier().start((Stage)bt_login.getScene().getWindow());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    	}
			    	else{
			    		//System.out.println("Login Fail! User name or password incorrect! because realid is "+ realId+" and id is "+id+" real password is "+ realPw+ " password is "+pw);
			    		Alert alert = new Alert(AlertType.ERROR, "Login fail! Incorrect Password");
						alert.showAndWait();
						tf_id.clear();
						tf_pass.clear();

						System.out.println("login fail Error showed");
			    	}
			    	 
			    	}//end of rs.next()
			    	else {
			    		Alert alert = new Alert(AlertType.ERROR, "No such user");
						alert.showAndWait();
						tf_id.clear();
						tf_pass.clear();

						System.out.println("no user Error showed");
			    		//System.out.println("No such user!!");
			    	}
		    	
		    	
	    	}
		    		
		    		
		    		
		    		
	    	}//end of check is Integer
			}
			catch(NumberFormatException nfe) {
				Alert alert = new Alert(AlertType.ERROR, "Invalid input!");
				alert.showAndWait();

				System.out.println("input Error showed"+nfe.getMessage());
			}
			
			
			
	    }
	    	
		    		
		    		
		    		//for screen transaction from login to cashier panel
		public class MainCashier extends Application{

		    @Override
		    public void start(Stage primaryStage) throws Exception {
		        Parent root = FXMLLoader.load(getClass().getResource("/views/cashier_main.fxml"));

	
		    
		        
		        Scene scene = new Scene(root, 1320, 700);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Cashier Panel");
				//primaryStage.sizeToScene();
				primaryStage.setResizable(false);
				primaryStage.getIcons().add(new Image("images/poslogorect.png"));
				primaryStage.setMaximized(false);
				primaryStage.show();
		    }
		}
	    	
		
		
		
		
		
		//for screen transaction from login to admin panel
		public class MainAdmin extends Application{

		    @Override
		    public void start(Stage primaryStage) throws Exception {
		        Parent root = FXMLLoader.load(getClass().getResource("/views/Admin_panel.fxml"));

	
		    
		        
		        Scene scene = new Scene(root,1320,700);
				primaryStage.setScene(scene);
				primaryStage.setTitle("Admin Panel");
				//primaryStage.sizeToScene();
				primaryStage.setResizable(false);
				primaryStage.getIcons().add(new Image("images/poslogorect.png"));
				primaryStage.setMaximized(false);
				primaryStage.show();
		    }
		}
		
		public static boolean isInteger(String s) {
		    try { 
		        Integer.parseInt(s); 
		    } catch(NumberFormatException e) { 
		        return false; 
		    } catch(NullPointerException e) {
		        return false;
		    }
		    // only got here if we didn't return false
		    return true;
		}
		
	}
