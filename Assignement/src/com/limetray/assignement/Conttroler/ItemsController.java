package com.limetray.assignement.Conttroler;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.TableViewSelectionModel;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import com.limetray.assignement.Beans.ItemsBeans;
import com.limetray.assignement.Util.Utilities;

/**
 * FXML Controller class
 *
 * @author ADMIN
 */
public class ItemsController implements Initializable {
    @FXML
    private Color x2;
    @FXML
    private Font x1;
    @FXML
    private ComboBox<String> itemsList;
    @FXML
    private Label pricePerItems;
    @FXML
    private ComboBox<Integer> numberOfItems;
    @FXML
    private Label totalPrice;
    @FXML
    private Button addDetail;
    @FXML
    private TableView<ItemsBeans> itemsDetailsList = new TableView<ItemsBeans>();
    @FXML
    private Button savedItems;
    @FXML
    private Color x4;
    @FXML
    private Insets x5;
    @FXML
    private Button cancelOrder;
    @FXML
    private Label successLabel;
    @FXML
    private ProgressIndicator indicator;
    @FXML
    private Label totalVatAmount;
    @FXML
    private Label vatLabel;
    @FXML
    private Label vatVal;
    @FXML
    private Label calculateVat;
    @FXML
    private Label totalPriceAfterVat;
    @FXML
    private TableColumn<ItemsBeans, Integer> seqNo;
    @FXML
    private TableColumn<ItemsBeans, String> itemNameCo;
    @FXML
    private TableColumn<ItemsBeans, Integer> noOfItemCo;
    @FXML
    private TableColumn<ItemsBeans, Double> pricePerItemCo;
    @FXML
    private TableColumn<ItemsBeans, Double> totalPriceCo;
    @FXML
    private TableColumn<ItemsBeans, String> dateTime;
    @FXML
    private ContextMenu deleteARowFromTable;
    /**
     * Initializes the controller class.
     */
    MyLRUCache lruCache;
    HashMap<String,String> itemList;
    HashMap<String,String> itemPrice;
    HashMap<String,String> numberOfItem;
    ObservableList<ItemsBeans> tableData = FXCollections.observableArrayList();
    private int count = 0;
    private Utilities utilities;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	try{
    		init();
    		initComponentValue();
    		initTableView();
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }    

   
	@FXML
    private void addDetailAction(ActionEvent event) {
    	String itemName = itemsList.getSelectionModel().getSelectedItem();
    	Integer noOfItems = numberOfItems.getSelectionModel().getSelectedItem(); 
    	if((itemName != null && !itemName.equals("null")) && (noOfItems != null && noOfItems != 0)){
    		count++;
    		ItemsBeans itemDetail = new ItemsBeans(count, itemName, (double)Double.parseDouble(pricePerItems.getText()), noOfItems, (double)Double.parseDouble(totalPriceAfterVat.getText()),utilities.dateToStringUtil(new Date()));
    		tableData.add(itemDetail);
    		itemsDetailsList.setItems(tableData);
    		
    	}
    	
    }

    @FXML
    private void savedItemsAction(ActionEvent event) {
    	
    }

    @FXML
    private void cancelOrderAction(ActionEvent event) {
    	
    	count = 0;
    }
    @FXML
    private void selectItemName(ActionEvent event) {
    	try{
    		String itemName = itemsList.getSelectionModel().getSelectedItem();
    		if(itemName != null && !itemName.equals("null")){
    			numberOfItems.setValue(null);
    			pricePerItems.setText(null);
    			ObservableList<Integer> numOfItemList = FXCollections.observableArrayList();
    			for(int count = 1; count <= Integer.parseInt(getNumberOfItem().get("NoOfItem"+itemName));count++){
    				numOfItemList.add(count);
    			}
    			numberOfItems.setItems(numOfItemList);
    			pricePerItems.setText(floatToStringConversion((float)Double.parseDouble((String)getItemPrice().get(itemName+"ItemPrice"))));
    			numOfItemList = null;
    		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }

    @FXML
    private void selectItemPrice(ActionEvent event) {
    	try{
    		String itemName = itemsList.getSelectionModel().getSelectedItem();
    		Integer noOfItems = numberOfItems.getSelectionModel().getSelectedItem(); 
    		if(itemName != null && !itemName.equals("null") && noOfItems != null && noOfItems != 0){
    			totalPrice.setText(null);
    			totalPrice.setText(floatToStringConversion(((float)Double.parseDouble(pricePerItems.getText())*noOfItems)));
    			calculateVat.setText("VAT Amount : "+floatToStringConversion((((float)Double.parseDouble(totalPrice.getText())*
    														((float)Double.parseDouble(vatVal.getText().substring(vatVal.getText().indexOf(':')+1).trim())))/100)));
    			totalVatAmount.setText("Total Amount : ");
    			totalPriceAfterVat.setText(floatToStringConversion(((float)Double.parseDouble(totalPrice.getText())+
    																((float)Double.parseDouble(calculateVat.getText().substring(calculateVat.getText().indexOf(':')+1).trim()))))); 
    		}
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    public void init(){
    	try{
    		lruCache = MyLRUCache.getMyLRUCache();
    		utilities = Utilities.getUtilClass();
    		itemList = new HashMap<String, String>();
    		numberOfItem = new HashMap<String, String>();
    		itemList = lruCache.getItemCache();
    		numberOfItem = lruCache.getNumberOfItems();
    		itemPrice = lruCache.getItemPrice();
    		System.out.print(numberOfItem.keySet()); 
    		setItemList(itemList);
    		setNumberOfItem(numberOfItem);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }

    
    public void initComponentValue(){
    	try{
    		lruCache = MyLRUCache.getMyLRUCache();
    		ObservableList<String> listOfItems = FXCollections.observableArrayList();
    		for(Map.Entry<String, String> itemMap : getItemList().entrySet()){
    			String val = (String)itemMap.getValue();
    			listOfItems.add(val);
    		}
    		itemsList.setItems(listOfItems);
    		vatVal.setText("VAT (%) : "+floatToStringConversion((float)lruCache.getVatValue().get()));
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    private String floatToStringConversion(double totalPrice){
    	try{
    		DecimalFormat df2 = new DecimalFormat("00.00");
    		float totalPriceInFloat = (float) totalPrice;
    		String totalPriceFromFloatToString = df2.format(totalPriceInFloat);
    		totalPriceFromFloatToString = totalPriceFromFloatToString.substring(totalPriceFromFloatToString.indexOf('.')+1).length() == 1?
    									  (totalPriceFromFloatToString.substring(0,totalPriceFromFloatToString.indexOf('.'))+"."+totalPriceFromFloatToString.substring(totalPriceFromFloatToString.indexOf('.')+1)+String .valueOf('0')):
    										  totalPriceFromFloatToString;
    		return totalPriceFromFloatToString;
    		
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	return null;
    }
    
    
    public void initTableView(){
    	try{
    		seqNo.setCellValueFactory(new PropertyValueFactory<ItemsBeans,Integer>("seqNo"));
    		itemNameCo.setCellValueFactory(new PropertyValueFactory<ItemsBeans,String>("itemNameCo"));
    		noOfItemCo.setCellValueFactory(new PropertyValueFactory<ItemsBeans,Integer>("noOfItemCo"));
    		pricePerItemCo.setCellValueFactory(new PropertyValueFactory<ItemsBeans,Double>("pricePerItemCo"));
    		totalPriceCo.setCellValueFactory(new PropertyValueFactory<ItemsBeans,Double>("totalPriceCo"));
    		dateTime.setCellValueFactory(new PropertyValueFactory<ItemsBeans,String>("dateTime"));
    		itemsDetailsList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    		//itemsDetailsList.setItems(null);
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    
    @FXML
    private void deleteARowFromTableEvent(ActionEvent event) {
    	try{
    		
    		itemsDetailsList.getItems().removeAll(itemsDetailsList.getSelectionModel().getSelectedItem());
    		
//    		int leftItemCount = 0;
    		
    		//System.out.println(itemsDetailsList.getSelectionModel().getSelectionMode().);
//    		ItemsBeans deleteSelectedItem = itemsDetailsList.getSelectionModel().getSelectedItem();
//    		//tableData = null;
//    		for(ItemsBeans item : itemList){
//    			if(deleteSelectedItem.getSeqNo() != item.getSeqNo()){
//    				leftItemCount++;
//    				item.setSeqNo(leftItemCount);
//    				tableData.add(item);
//    				System.out.println(item.getSeqNo()+" > "+item.getItemNameCo());
//    			}
//    		}
//    		//itemsDetailsList.setItems(tableData);
//    		//System.out.println(deleteSelectedItem.getSeqNo()+" > "+deleteSelectedItem.getItemNameCo());
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    
	public HashMap<String, String> getItemList() {
		return itemList;
	}

	public void setItemList(HashMap<String, String> itemList) {
		this.itemList = itemList;
	}

	public HashMap<String, String> getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(HashMap<String, String> itemPrice) {
		this.itemPrice = itemPrice;
	}

	public HashMap<String, String> getNumberOfItem() {
		return numberOfItem;
	}

	public void setNumberOfItem(HashMap<String, String> numberOfItem) {
		this.numberOfItem = numberOfItem;
	}
    
    
}
