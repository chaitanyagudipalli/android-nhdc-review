package in.vasista.vsales.sync;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.location.DetectedActivity;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import in.vasista.atom.AtomActivity;
import in.vasista.hr.attendance.AttendanceListFragment;
import in.vasista.location.MapsActivity;
import in.vasista.nhdcapp.R;
import in.vasista.payumoney.PayumoneyActivity;
import in.vasista.vsales.EmployeeDetailsActivity;
import in.vasista.vsales.HRDashboardActivity;
import in.vasista.vsales.IndentDetailed;
import in.vasista.vsales.LeaveActivity;
import in.vasista.vsales.MainActivity;
import in.vasista.vsales.PaymentActivity;
import in.vasista.vsales.SalesDashboardActivity;
import in.vasista.vsales.catalog.CatalogListFragment;
import in.vasista.vsales.catalog.Product;
import in.vasista.vsales.db.AttendanceDataSource;
import in.vasista.vsales.db.EmployeeDataSource;
import in.vasista.vsales.db.FacilityDataSource;
import in.vasista.vsales.db.IndentsDataSource;
import in.vasista.vsales.db.LeavesDataSource;
import in.vasista.vsales.db.LocationsDataSource;
import in.vasista.vsales.db.OrdersDataSource;
import in.vasista.vsales.db.PODataSource;
import in.vasista.vsales.db.PaymentsDataSource;
import in.vasista.vsales.db.PayslipDataSource;
import in.vasista.vsales.db.ProductsDataSource;
import in.vasista.vsales.db.SupplierDataSource;
import in.vasista.vsales.db.TransporterDataSource;
import in.vasista.vsales.employee.Employee;
import in.vasista.vsales.employee.EmployeeListFragment;
import in.vasista.vsales.facility.Facility;
import in.vasista.vsales.facility.FacilityListFragment;
import in.vasista.vsales.indent.Indent;
import in.vasista.vsales.indent.IndentItem;
import in.vasista.vsales.indent.IndentItemNHDC;
import in.vasista.vsales.indent.IndentItemsListFragment;
import in.vasista.vsales.indent.IndentListFragment;
import in.vasista.vsales.order.OrderListFragment;
import in.vasista.vsales.payment.PaymentListFragment;
import in.vasista.vsales.supplier.Supplier;
import in.vasista.vsales.supplier.SupplierListFragment;
import in.vasista.vsales.supplier.SupplierPO;
import in.vasista.vsales.supplier.SupplierPOItem;
import in.vasista.vsales.supplier.SupplierPOItemListFragment;
import in.vasista.vsales.supplier.SupplierPOListFragment;
import in.vasista.vsales.supplier.SupplierPOShip;
import in.vasista.vsales.sync.xmlrpc.XMLRPCApacheAdapter;
import in.vasista.vsales.sync.xmlrpc.XMLRPCMethodCallback;
import in.vasista.vsales.transporter.Transporter;
import in.vasista.vsales.transporter.TransporterListFragment;
import in.vasista.vsales.util.DateUtil;

public class ServerSync {

	public static final String module = ServerSync.class.getName();	

	
	private Context context;
	//private MySQLiteHelper dbHelper;
	
	public ServerSync(Context context) {
		this.context = context; 
	    //dbHelper = new MySQLiteHelper(context); 		
	}

	public void uploadPayment(ProgressBar progressBar, HashMap<String, String> list, final PayumoneyActivity payumoneyActivity){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");
		Map paramMap = new HashMap();
		paramMap.put("partyId", storeId);
		paramMap.put("transactionId", list.get("txnid"));
		paramMap.put("amount", list.get("amount"));
		paramMap.put("paymentDate", list.get("addedon"));
		paramMap.put("orderId", list.get("orderId"));

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("makeWeaverPayment", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {

					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}

					Toast.makeText( context, "Successfully Payment Has Been Created", Toast.LENGTH_LONG ).show();
					payumoneyActivity.paymentDone();
				}
			});
		}
		catch (Exception e) {

			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}

			Toast.makeText( context, "Payment failed: " + e, Toast.LENGTH_LONG ).show();
			payumoneyActivity.paymentDone();
		}

	}

	public void uploadAtomPayment(ProgressBar progressBar, HashMap<String, String> list, final AtomActivity payumoneyActivity){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");
		Map paramMap = new HashMap();
		paramMap.put("partyId", storeId);
		paramMap.put("transactionId", list.get("txnid"));
		paramMap.put("amount", list.get("amount"));
		paramMap.put("paymentDate", list.get("addedon"));
		paramMap.put("orderId", list.get("orderId"));

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("makeWeaverPayment", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {

					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}

					Toast.makeText( context, "Successfully Payment Has Been Created", Toast.LENGTH_LONG ).show();
					payumoneyActivity.paymentDone();
				}
			});
		}
		catch (Exception e) {

			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}

			Toast.makeText( context, "Payment failed: " + e, Toast.LENGTH_LONG ).show();
			payumoneyActivity.paymentDone();
		}

	}



	public  void uploadNHDCIndent(final MenuItem menuItem, ProgressBar progressBar, List<HashMap> list, String supplierPartyId, String transporterId,String schemeCategory, final long indent_id, String prodStoreId){
				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");
		Map paramMap = new HashMap();
		paramMap.put("partyId", storeId);
		Date supplyDate = new Date();
		paramMap.put("effectiveDate", ""+supplyDate.getTime());
		paramMap.put("salesChannel", "MOBILE_SALES_CHANNEL");
		paramMap.put("supplierPartyId", supplierPartyId);
		paramMap.put("transporterId", transporterId);
		paramMap.put("schemeCategory", schemeCategory);
	    paramMap.put("indentItems", list);
		paramMap.put("productStoreId",prodStoreId);

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("createBranchSalesIndent", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						IndentsDataSource datasource = new IndentsDataSource(context);
						datasource.open();
						datasource.updateIndentStatus(indent_id,"Created");
						if (result != null) {
							Map indentResults = (Map)((Map)result).get("indentResults");

							// Intent i = new Intent(context,PayumoneyActivity.class);
							Intent i = new Intent(context,AtomActivity.class);
							//i.putExtra("numIndentItems",(int)indentResults.get("numIndentItems"));
							i.putExtra("orderId",(String)indentResults.get("orderId"));
							i.putExtra("amount",((BigDecimal) indentResults.get("amount")).floatValue());
							Log.v("amnt",""+((BigDecimal) indentResults.get("amount")).floatValue());
							context.startActivity(i);

						}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Indent upload succeeded!", Toast.LENGTH_LONG ).show();

				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "Upload failed: " + e, Toast.LENGTH_LONG ).show();
		}
	}
	
	public void uploadIndent(final MenuItem menuItem, final Indent indent, ProgressBar progressBar, final IndentItemsListFragment listFragment) {
		IndentsDataSource datasource = new IndentsDataSource(context);
		datasource.open();
//		if (indent == null || !indent.getStatus().equals("Created")) {
//			progressBar.setVisibility(View.GONE);
//			return;
//		}
//		Map[] indentItems = datasource.getXMLRPCSerializedIndentItems(indent.getId());
//		datasource.close();
//		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
//		String storeId = prefs.getString("storeId", "");
//		Map paramMap = new HashMap();
//		paramMap.put("boothId", storeId);
//		Date supplyDate = indent.getSupplyDate();
//		paramMap.put("supplyDate", supplyDate.getTime());
//		paramMap.put("subscriptionTypeId", indent.getSubscriptionType());
//	    paramMap.put("indentItems", indentItems);
//		try {
//			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
//			adapter.call("processChangeIndentApi", paramMap, progressBar, new XMLRPCMethodCallback() {
//				public void callFinished(Object result, ProgressBar progressBar) {
//					if (result != null) {
//				    	Map indentResults = (Map)((Map)result).get("indentResults");
//				    	Log.d(module, "numIndentItems = " + indentResults.get("numIndentItems"));
//						IndentsDataSource ds = new IndentsDataSource(context);
//						ds.open();
//				    	ds.setIndentSyncStatus(indent.getId(), true);
//				    	ds.close();
//				    	if (listFragment != null) {
//				    		listFragment.notifyChange();
//				    	}
//					}
//					if (progressBar != null) {
//						progressBar.setVisibility(View.INVISIBLE);
//					}
//					if(menuItem !=null){
//						if (progressBar != null) {
//							progressBar.setVisibility(View.VISIBLE);
//						}
//						menuItem.setActionView(null);
//					}
//					Toast.makeText( context, "Indent upload succeeded!", Toast.LENGTH_LONG ).show();
//				}
//			});
//		}
//		catch (Exception e) {
//			Log.e(module, "Exception: ", e);
//			if (progressBar != null) {
//				progressBar.setVisibility(View.INVISIBLE);
//			}
//			if(menuItem !=null){
//				if (progressBar != null) {
//					progressBar.setVisibility(View.VISIBLE);
//				}
//				menuItem.setActionView(null);
//			}
//			Toast.makeText( context, "Upload failed: " + e, Toast.LENGTH_LONG ).show();
//		}
	}
	public void uploadShipments(final MenuItem menuItem, final List<SupplierPOItem> suppitems, Map po, ProgressBar progressBar, final SupplierPOItemListFragment listFragment){


				SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");

		PODataSource poDataSource = new PODataSource(context);
		poDataSource.open();
		final Map paramMap = new HashMap();

		SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");

		Date suppInvoiceDate = null, lrDate = null;
		try {
			suppInvoiceDate = format.parse((String)po.get("suppInvoiceDate"));
			lrDate = format.parse((String)po.get("lrDate"));
		} catch (ParseException e) {
			e.printStackTrace();
		}

		paramMap.put("partyId", storeId);
		paramMap.put("boothId", storeId);
		paramMap.put("orderId", po.get("orderId"));
		try {
			paramMap.put("suppInvoiceDate", suppInvoiceDate.getTime());
			paramMap.put("lrDate", lrDate.getTime());
		}catch (NullPointerException e){

		}
		paramMap.put("suppInvoiceId", po.get("suppInvoiceId"));
		paramMap.put("lrNumber", po.get("lrNumber"));

		paramMap.put("carrierName", po.get("carrierName"));
		paramMap.put("vehicleId", po.get("vehicleId"));
		paramMap.put("freightCharges", po.get("freightCharges"));
		paramMap.put("remarks", po.get("remarks"));
	    paramMap.put("shipmentItems", poDataSource.convertToXMLRPC(suppitems));
		poDataSource.close();
		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("createSupplierDispatch", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					Log.v("Service call","createSupplierDispatch :: "+paramMap.size());
					if (result != null) {
				    	Map indentResults = (Map)((Map)result).get("shipmentResult");
				    	//Log.d(module, "numIndentItems = " + indentResults.get("numIndentItems"));
				    	if (listFragment != null) {
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Goods dispatched successfully", Toast.LENGTH_LONG ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "Upload failed: " + e, Toast.LENGTH_LONG ).show();
		}

	}
	public void updateProducts(final MenuItem menuItem, ProgressBar progressBar, final CatalogListFragment listFragment) {
		Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");			
		paramMap.put("boothId", storeId);
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getProducts", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						final ProductsDataSource datasource = new ProductsDataSource(context);
				    	Map priceResults = (Map)((Map)result).get("productsMap");
				    	Log.d(module, "priceResults.size() = " + priceResults.size());
				    	datasource.open();
				    	datasource.deleteAllProducts();
				    	Iterator entries = priceResults.entrySet().iterator();
						List<Product> ls = new ArrayList<Product>();
				    	while (entries.hasNext()) {
				    	  Entry thisEntry = (Entry) entries.next();
				    	  String productId = (String)thisEntry.getKey();
				    	  Map value = (Map)thisEntry.getValue();


							// Display all the Key's from the service.
//							for ( Object key : value.keySet() ) {
//								Log.v("Upendra","Key - "+(String)key);
//							}
				    	  String name = (String)value.get("productName");
				    	  String description = (String)value.get("description");
							String primaryProductCategoryId = (String)value.get("primaryProductCategoryId");
							String internalName = (String)value.get("internalName");
							String brandName = (String)value.get("brandName");
							String quantityUomId = (String)value.get("quantityUomId");
							String productTypeId = (String)value.get("productTypeId");
							String productParentTypeId = (String)value.get("primaryParentCategoryId");

							Map schemeMap = (Map)((Map) value).get("schemeCategoryMap");
							Iterator schemes = schemeMap.entrySet().iterator();
							String scheme = "";
							while (schemes.hasNext()){
								Entry schemes_e = (Entry) schemes.next();
								scheme = (String)schemes_e.getKey();
							}

				    	  float price = 0.0f;
				    	  float quantityIncluded = ((BigDecimal)value.get("quantityIncluded")).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();
				    	  Product product = new Product(productId, name, internalName, brandName,description,productTypeId,quantityUomId , price, quantityIncluded, primaryProductCategoryId,productParentTypeId,scheme);
				    	  //Log.d(module, "product = " + product);
							ls.add(product);
				    	  //datasource.insertProduct(product);
				    	}
						datasource.insertProducts(ls);
				    	datasource.close();
				    	if (listFragment != null) {
					    	Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated product catalog!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "Update product prices failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}	
	}

	public void cancelIndent(final MenuItem menuItem, ProgressBar progressBar, String orderId, final IndentDetailed indentDetailed){
		Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");
		paramMap.put("partyId", storeId);
		paramMap.put("orderId", orderId);

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("cancelIndent", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {

					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);

					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					indentDetailed.navtolistOfIndents();

				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "getFacilityIndent failed: " + e, Toast.LENGTH_SHORT ).show();
		}

	}

	public void updateSupplierPOs(final MenuItem menuItem, ProgressBar progressBar, final SupplierPOListFragment listFragment) {
		Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");
		paramMap.put("partyId", storeId);
		Date now = new Date();
		final Date supplyDate = DateUtil.addDays(now, 1);
		paramMap.put("supplyDate", supplyDate.getTime());
		Log.d(module, "PO supplyDate = " + supplyDate);

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getSupplierPO", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
					if (result != null) {
						PODataSource indentDataSource = new PODataSource(context);
						indentDataSource.open();
						indentDataSource.deleteAllPOs();

						Map indentResults = (Map)((Map)result).get("supplierPOList");
						Log.d(module, "indentResults.size() = " + indentResults.size());
						if (indentResults.size() > 0) {
							List <SupplierPO> suppliers = new ArrayList<SupplierPO>();
							List<SupplierPOShip> supplierPOShips = new ArrayList<SupplierPOShip>();

							for ( Object key : indentResults.keySet() ) {
								Map boothMap = (Map) indentResults.get(key);
								String id = (String)boothMap.get("orderId");
								String name = (String)boothMap.get("orderNo");
								Date suppDate = null;
								try {
									suppDate = format.parse((String)boothMap.get("orderDate"));
								} catch (ParseException e) {
									e.printStackTrace();
								}
								String partyTypeId = (String)boothMap.get("statusId");
								SupplierPO supplier = new SupplierPO(key.toString(), (String)boothMap.get("orderDate"),id, name, partyTypeId);
								suppliers.add(supplier);
								//Log.d(module, "supplier = " + supplier);
								Object[] orderItemsList = (Object[])boothMap.get("orderItemsList");
								for (int i=0;i<orderItemsList.length;i++){
									Map orderItem = (Map) orderItemsList[i];
									SupplierPOItem supplierPOItem = new SupplierPOItem(key.toString(),(String)orderItem.get("productId"),
											(String)orderItem.get("itemName"),
											(String)orderItem.get("specification"),
											(String)orderItem.get("orderItemSeqId"),
											((BigDecimal)orderItem.get("unitPrice")).floatValue(),
											((BigDecimal)orderItem.get("indentQuantity")).floatValue(),
											((BigDecimal)orderItem.get("dispatchedQty")).floatValue(),
											((BigDecimal)orderItem.get("balanceQty")).floatValue());
									indentDataSource.insertSuppPOItem(supplierPOItem);
								}

								Map shipmentHistory = (Map)(boothMap).get("shipmentItemHistory");
								if (shipmentHistory != null) {
									for (Object ship_key : shipmentHistory.keySet()) {
										Log.v("ShipKey", "" + ship_key.toString() +" for "+key.toString());

										Map shipment_details = (Map) shipmentHistory.get(ship_key);

										Object[] shipment = (Object[]) shipment_details.get("shipmentItems");
										if (shipment.length > 0) {

											for (int i=0;i<shipment.length;i++){
												Map shipitem = (Map)shipment[i];
												Log.v("sh",""+ship_key.toString()+" for "+i);

												SupplierPOShip supplierPOShip = new SupplierPOShip(ship_key.toString(), key.toString(),
													(String) shipitem.get("productId"),
													(String) shipitem.get("itemName"), (String) shipitem.get("orderItemSeqId"),
													((BigDecimal) shipitem.get("quantity")).floatValue(), ((BigDecimal) shipitem.get("unitPrice")).floatValue(),
													((BigDecimal) shipitem.get("itemAmount")).floatValue(),
														(String) shipment_details.get("customer"),(String) shipment_details.get("destination"));
													supplierPOShips.add(supplierPOShip);
											}
//
										}
									}
								}


							}
							indentDataSource.insertSuppPO(suppliers);
							indentDataSource.insertSuppPOShips(supplierPOShips);

						}
						indentDataSource.close();
						if (listFragment != null) {
							//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
							listFragment.notifyChange();
						}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);

					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "getFacilityIndent failed: " + e, Toast.LENGTH_SHORT ).show();
		}
		if (listFragment != null) {
			//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
			listFragment.notifyChange();
		}
	}

	public void fetchActiveIndents(final MenuItem menuItem, ProgressBar progressBar, final IndentListFragment listFragment) {
		Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");			
		paramMap.put("partyId", storeId);
	  	Date now = new Date();
		final Date supplyDate = DateUtil.addDays(now, 1);
		paramMap.put("supplyDate", supplyDate.getTime());	
    	Log.d(module, "supplyDate = " + supplyDate);	
		final Map productsMap = new HashMap();
		ProductsDataSource prodDataSource = new ProductsDataSource (context);
		prodDataSource.open();
		List<Product> products = prodDataSource.getAllProducts();
		for (int i =0; i < products.size(); ++i) {
			Product product = products.get(i);
			productsMap.put(product.getId(), product);
		}
		prodDataSource.close();
		if (productsMap.isEmpty()) {
			// don't do anything if we don't have any products
			Toast.makeText( context, "No products available, first fetch products.", Toast.LENGTH_SHORT ).show();	    		    						
			return;
		}    	
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getWeaverIndents", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");  		    	  
					if (result != null) {						
						IndentsDataSource indentDataSource = new IndentsDataSource(context);
						indentDataSource.open();  
						indentDataSource.deleteAllIndents();
						indentDataSource.deleteAllIndentItems();
				    	Map indentResults = (Map)((Map)result).get("indentSearchResults");
				    	Log.d(module, "indentResults.size() = " + indentResults.size());
				    	if (indentResults.size() > 0) {
							if (indentResults.get("orderList") != null) {
								Object[] indentResultsList = (Object[])indentResults.get("orderList");
								Log.d(module, "indentResultsList.size() = " + indentResultsList.length);
								List<Indent> indents = new ArrayList<Indent>();
								List<IndentItemNHDC> indentItemNHDCs;
								for (int i=0; i < indentResultsList.length; ++i) {
									//indentItemNHDCs = new ArrayList<IndentItemNHDC>();
									Map indentMap = (Map)indentResultsList[i];
									try {
										Log.v("indent disc",""+((BigDecimal)indentMap.get("totDiscountAmt")));
										float totDiscountAmt = ((BigDecimal)indentMap.get("totDiscountAmt")).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue();

										Indent indent = new Indent(0,(String)indentMap.get("tallyRefNo"),(String)indentMap.get("POorder"),(String)indentMap.get("poSquenceNo"),((String)(indentMap.get("isgeneratedPO"))).equalsIgnoreCase("Y"),
												(String)indentMap.get("supplierPartyId"),(String)indentMap.get("transporterId"),(String)indentMap.get("storeName"),(String)indentMap.get("supplierPartyName"),(String)indentMap.get("orderNo"),(String)indentMap.get("orderId"),
												format.parse(String.valueOf(indentMap.get("orderDate"))),(String)indentMap.get("statusId"),
												((BigDecimal)indentMap.get("orderTotal")).floatValue(),((BigDecimal)indentMap.get("paidAmt")).floatValue(),((BigDecimal)indentMap.get("balance")).floatValue(),(String)indentMap.get("scheme"),"",
												totDiscountAmt);
										indents.add(indent);

										long indent_id = indentDataSource.insertIndent(indent);
										Object[] productsMap = (Object[])indentMap.get("orderItemsList");
										for (int j=0;j<productsMap.length;j++){
											Map productMap = (Map)productsMap[j];
											IndentItemNHDC indentItemNHDC = new IndentItemNHDC(0,indent.getId(),(String)productMap.get("productId"),
													""+productMap.get("quantity"),(String)productMap.get("specification"),
													""+productMap.get("baleQty"),""+productMap.get("bundleWeight"),""+productMap.get("unitPrice"),
													""+productMap.get("uom"),"","",
													"",""+productMap.get("totalAmount"),
													((BigDecimal)productMap.get("vatPercent")).floatValue(),
													((BigDecimal)productMap.get("vatAmount")).floatValue(),
													((BigDecimal)productMap.get("cstPercent")).floatValue(),
													((BigDecimal)productMap.get("cstAmount")).floatValue(),
													((BigDecimal)productMap.get("shippedQty")).floatValue(),
													((BigDecimal)productMap.get("discountAmount")).floatValue(),
													(productMap.get("otherCharges")!=null)?(((BigDecimal)productMap.get("otherCharges")).floatValue()):0,
													(String)productMap.get("specification")
													);
											indentDataSource.insertIndentItem(indent_id,indentItemNHDC);
										}


									} catch (ParseException e) {
										e.printStackTrace();
									}

								}

								//indentDataSource.insertIndents(indents);
							}
				    	}			
		    		  	indentDataSource.close();
				    	if (listFragment != null) {
					    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);

					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
				}
			});
		}
		catch (Exception e) { 
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "getFacilityIndent failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
    	if (listFragment != null) {
	    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
    		listFragment.notifyChange();
    	}
	}	

	public void fetchPayments(final MenuItem menuItem,ProgressBar progressBar, final PaymentListFragment listFragment) {
		final Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");			
		paramMap.put("partyId", storeId);
	  	final Date thruDate = new Date();
		final Date fromDate = DateUtil.addDays(thruDate, -31);
		paramMap.put("fromDate", fromDate.getTime());					
		paramMap.put("thruDate", thruDate.getTime());
		String PaymentMethodName = "getWeaverPayments";
		if(prefs.getBoolean(MainActivity.IS_SUP_PORTAL,false))
			PaymentMethodName = "getSupplierPayments";
		//::TODO:: add logic to first fetch active indents from server
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call(PaymentMethodName, paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						final PaymentsDataSource paymentsDataSource = new PaymentsDataSource (context);
						paymentsDataSource.open();  
				    	Map paymentsResult = (Map)((Map)result).get("paymentSearchResults");
				    	Log.d(module, "paymentsResult.size() = " + paymentsResult.size());	
				    	paymentsDataSource.deleteAllPayments();
				    	if (paymentsResult.size() > 0) {
				    		if (paymentsResult.get("paymentSearchResultsList") != null) {
					    		Object[] boothPaymentsList = (Object[])paymentsResult.get("paymentSearchResultsList");
						    	Log.d(module, "paymentsList.size() = " + boothPaymentsList.length);	
				    		  	for (int i=0; i < boothPaymentsList.length; ++i) {
				    		  		Map paymentMap = (Map)boothPaymentsList[i];				    		  		
							    	String paymentId = (String)paymentMap.get("paymentId");
							    	Date paymentDate = (Date)paymentMap.get("paymentDate");
							    	String paymentMethodTypeId = (String)paymentMap.get("paymentMethodTypeId");							    	
							    	double amount_paid = ((BigDecimal)paymentMap.get("paidAmount")).doubleValue();
									double amount_balance = ((BigDecimal)paymentMap.get("balanceAmount")).doubleValue();
									String from = (String)paymentMap.get("partyIdFrom"),to = (String)paymentMap.get("partyIdTo"),
											status = (String)paymentMap.get("statusId");
							    	Log.d(module, "paymentId = " + paymentId + "; paymentMethod =" + paymentMethodTypeId 
							    			+ "; paymentDate=" + paymentDate + "; amount = " + amount_paid);
							    	paymentsDataSource.insertPayment(paymentId, paymentDate, paymentMethodTypeId,status,from,to, amount_paid,amount_balance);
				    		  	}   
				    		}					    		
				    	}				    		  	
		    		  	paymentsDataSource.close();
				    	if (listFragment != null) {
					    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated payments!", Toast.LENGTH_SHORT ).show();	    		    			
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "getFacilityPayments failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
    	if (listFragment != null) {
	    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
    		listFragment.notifyChange();
    	}
	}
	public void getWeaverDetails(ProgressBar progressBar, final SalesDashboardActivity activity) {
		Map paramMap = new HashMap();
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String partyId = prefs.getString("storeId", "");
		paramMap.put("partyId", partyId);
		paramMap.put("effectiveDate", (new Date()).getTime());

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getWeaverDetails", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						Map weaverDetails = (Map)((Map)result).get("weaverDetails");
						//Log.d(module, "boothDues.size() = " + boothDues.size() + ";boothDues=" + boothDues);
						if (activity != null) {
							SharedPreferences.Editor prefEditor = prefs.edit();
							prefEditor.putString("weaverDetailsMap", ""+ weaverDetails);
							prefEditor.putString(MainActivity.USER_FULLNAME, ""+ weaverDetails.get("partyName"));
							prefEditor.putString(MainActivity.USER_PASSBOOK, ""+ weaverDetails.get("passBookNo"));
							prefEditor.apply();
							//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
							//activity.updateDues(boothDues, boothTotalDues);
						}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					//Toast.makeText( context, "got facility dues!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			Toast.makeText( context, "getFacilityDues failed: " + e, Toast.LENGTH_SHORT ).show();
		}
	}

	public void getWeaverDetails(ProgressBar progressBar) {
		Map paramMap = new HashMap();
		final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String partyId = prefs.getString("storeId", "");
		paramMap.put("partyId", partyId);
		paramMap.put("effectiveDate", (new Date()).getTime());

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getWeaverDetails", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						Map weaverDetails = (Map)((Map)result).get("weaverDetails");
							SharedPreferences.Editor prefEditor = prefs.edit();
							prefEditor.putString("weaverDetailsMap", ""+ weaverDetails);
							prefEditor.putString(MainActivity.USER_FULLNAME, ""+ weaverDetails.get("partyName"));
							prefEditor.putString(MainActivity.USER_PASSBOOK, ""+ weaverDetails.get("passBookNo"));
							prefEditor.apply();
							//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
							//activity.updateDues(boothDues, boothTotalDues);
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					//Toast.makeText( context, "got facility dues!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			Toast.makeText( context, "getFacilityDues failed: " + e, Toast.LENGTH_SHORT ).show();
		}
	}

	public void getFacilityDues(ProgressBar progressBar, final SalesDashboardActivity activity) {
		Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");			
		paramMap.put("boothId", storeId);
			
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getFacilityDues", paramMap, progressBar, new XMLRPCMethodCallback() {	
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) { 
				    	Map boothDues = (Map)((Map)result).get("boothDues");
				    	Map boothTotalDues = (Map)((Map)result).get("boothTotalDues");				    	
				    	Log.d(module, "boothDues.size() = " + boothDues.size() + ";boothDues=" + boothDues);
				    	Log.d(module, "boothTotalDues.size() = " + boothTotalDues.size() + ";boothTotalDues=" + boothTotalDues);					    	
				    	if (activity != null) {
					    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
				    		//activity.updateDues(boothDues, boothTotalDues);
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					//Toast.makeText( context, "got facility dues!", Toast.LENGTH_SHORT ).show();	    		    			
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			Toast.makeText( context, "getFacilityDues failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
	}	
	
	public void fetchOrders(final MenuItem menuItem,ProgressBar progressBar, final OrderListFragment listFragment) {
		Map paramMap = new HashMap();
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("storeId", "");			
		paramMap.put("facilityId", storeId);
	  	final Date thruDate = new Date();
		final Date fromDate = DateUtil.addDays(thruDate, -31);
		paramMap.put("fromDate", fromDate.getTime());	 				
		paramMap.put("thruDate", thruDate.getTime());			
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getFacilityOrders", paramMap, progressBar, new XMLRPCMethodCallback() {	
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						final OrdersDataSource ordersDataSource = new OrdersDataSource (context);
						ordersDataSource.open();  
				    	Map ordersResult = (Map)((Map)result).get("ordersResult");
				    	Log.d(module, "ordersResult.size() = " + ordersResult.size());	  
				    	ordersDataSource.deleteAllOrders();	 
				    	if (ordersResult.size() > 0) {
			    		  	ordersDataSource.insertOrderAndItems(ordersResult);				    		
				    	}						    	
		    		  	ordersDataSource.close();
				    	if (listFragment != null) {
					    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated orders!", Toast.LENGTH_SHORT ).show();	    		    			
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
					}
			Toast.makeText( context, "fetchOrders failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
    	if (listFragment != null) {
	    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
    		listFragment.notifyChange();
    	}
	}
	public void updateSuppliers(final MenuItem menuItem, ProgressBar progressBar, final SupplierListFragment listFragment) {
		final SupplierDataSource datasource = new SupplierDataSource(context);
		datasource.open();
		Map paramMap = new HashMap();
		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getSuppliers", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						Map facilitiesResult = (Map)((Map)result).get("suppliersMap");
						Log.d(module, "facilitiesResult.size() = " + facilitiesResult.size());
						datasource.open();
						//datasource.deleteAllSuppliers();
						if (facilitiesResult.size() > 0) {
								List <Supplier> suppliers = new ArrayList();

							for ( Object key : facilitiesResult.keySet() ) {
								Map boothMap = (Map) facilitiesResult.get(key);
								String id = (String)boothMap.get("partyId");
									String name = (String)boothMap.get("groupName");
									String roleTypeId = (String)boothMap.get("roleTypeId");
									String partyTypeId = (String)boothMap.get("partyTypeId");
									Supplier supplier = new Supplier(id, name, roleTypeId, partyTypeId);
									suppliers.add(supplier);
								//Log.d(module, "supplier = " + supplier);

							}
								datasource.insertSuppliers(suppliers);
//							}
						}
						datasource.close();
						if (listFragment != null) {
							//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
							listFragment.notifyChange();
						}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated suppliers!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "Update suppliers failed: " + e, Toast.LENGTH_SHORT ).show();
		}
		if (listFragment != null) {
			//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
			listFragment.notifyChange();
		}
	}

	public void updateTransporters(final MenuItem menuItem, ProgressBar progressBar, final TransporterListFragment listFragment) {
		final TransporterDataSource datasource = new TransporterDataSource(context);
		datasource.open();
		Map paramMap = new HashMap();
		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getTransporters", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						Map facilitiesResult = (Map)((Map)result).get("transportersMap");
						Log.d(module, "facilitiesResult.size() = " + facilitiesResult.size());
						datasource.open();
						//datasource.deleteAllSuppliers();
						if (facilitiesResult.size() > 0) {
							List <Transporter> suppliers = new ArrayList();

							for ( Object key : facilitiesResult.keySet() ) {
								Map boothMap = (Map) facilitiesResult.get(key);
								String id = (String)boothMap.get("partyId");
								String name = (String)boothMap.get("partyName");
								String contactNumber = (String)boothMap.get("contactNumber");
								String address = (String)((Map)boothMap.get("addressMap")).get("address1");
								Transporter supplier = new Transporter(id, name, contactNumber, address);
								suppliers.add(supplier);
								//Log.d(module, "supplier = " + supplier);

							}
							datasource.insertTransporters(suppliers);
//							}
						}
						datasource.close();
						if (listFragment != null) {
							//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
							listFragment.notifyChange();
						}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated Transporters!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "Update Transporters failed: " + e, Toast.LENGTH_SHORT ).show();
		}
		if (listFragment != null) {
			//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());
			listFragment.notifyChange();
		}
	}

	public void updateFacilities(final MenuItem menuItem, ProgressBar progressBar, final FacilityListFragment listFragment) {
		final FacilityDataSource datasource = new FacilityDataSource(context);
		datasource.open();  
		Map paramMap = new HashMap();
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getAllRMFacilities", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
				    	Map facilitiesResult = (Map)((Map)result).get("facilitiesResult");
				    	Log.d(module, "facilitiesResult.size() = " + facilitiesResult.size());
				    	datasource.open();
				    	datasource.deleteAllFacilities();
				    	if (facilitiesResult.size() > 0) {
				    		if (facilitiesResult.get("boothsDetailsList") != null) {
					    		Object[] boothsDetailsList = (Object[])facilitiesResult.get("boothsDetailsList");
						    	Log.d(module, "boothsDetailsList.size() = " + boothsDetailsList.length);
						    	List <Facility> facilities = new ArrayList();
				    		  	for (int i=0; i < boothsDetailsList.length; ++i) {
				    		  		Map boothMap = (Map)boothsDetailsList[i];				    		  		
							    	String id = (String)boothMap.get("facilityId");
							    	String name = (String)boothMap.get("facilityName");
							    	String category = (String)boothMap.get("category");	
							    	String ownerPhone = (String)boothMap.get("ownerPhone");	
							    	String salesRep = (String)boothMap.get("salesRep");							    								    	
							    	String amRouteId = (String)boothMap.get("amRouteId");							    	
							    	String pmRouteId = (String)boothMap.get("pmRouteId");
							    	String latitude = (String)boothMap.get("latitude");
							    	String longitude = (String)boothMap.get("longitude");							    	
							    	Facility facility = new Facility(id, name, category, ownerPhone, salesRep, amRouteId, pmRouteId, latitude, longitude);
							    	facilities.add(facility);
							    	//Log.d(module, "facility = " + facility);	  
				    		  	}  
						    	datasource.insertFacilities(facilities);
				    		}	
				    	}
				    	datasource.close();
				    	if (listFragment != null) {
					    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated outlets!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
					}
			Toast.makeText( context, "Update outlets failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}	
    	if (listFragment != null) {
	    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
    		listFragment.notifyChange();
    	}
	}

	public void updateEmployees(final MenuItem menuItem, ProgressBar progressBar, final EmployeeListFragment listFragment) {
		Map paramMap = new HashMap();
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("getActiveEmployees", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");  		    	  
				    	Map employeesResult = (Map)((Map)result).get("employeesResult");
				    	Log.d(module, "employeesResult.size() = " + employeesResult.size());
						final EmployeeDataSource datasource = new EmployeeDataSource(context);
				    	datasource.open();
				    	datasource.deleteAllEmployees();
				    	if (employeesResult.size() > 0) {
				    		if (employeesResult.get("employeeList") != null) {
					    		Object[] employeesList = (Object[])employeesResult.get("employeeList");
						    	Log.d(module, "employeesList.size() = " + employeesList.length);
						    	List <Employee> employees = new ArrayList();
				    		  	for (int i=0; i < employeesList.length; ++i) {
				    		  		Map employeeMap = (Map)employeesList[i];				    		  		
							    	String id = (String)employeeMap.get("employeeId");
							    	String name = (String)employeeMap.get("name");
							    	String department = (String)employeeMap.get("department");	
							    	String position = (String)employeeMap.get("position");	
							    	String phoneNumber = (String)employeeMap.get("phoneNumber");
									Date joinDate = new Date();
									String joinDateStr = (String)employeeMap.get("joinDate");
									try {
										joinDate = format.parse(joinDateStr);
									} catch (ParseException e) {
										// just go with default date for now
									}	
							    	String weeklyOff = (String)employeeMap.get("weeklyOff");
									
							    	Employee employee = new Employee(id, name, department, 
							    			position, phoneNumber, joinDate, weeklyOff);
					    			if (employeeMap.get("leaveBalanceDate") != null) {
					    				Date leaveBalanceDate = (Date)employeeMap.get("leaveBalanceDate");
					    				//Log.d(module, "leaveBalanceDate=" + leaveBalanceDate);	
					    				BigDecimal earnedLeaveBalance = BigDecimal.ZERO;
					    				BigDecimal casualLeaveBalance = BigDecimal.ZERO;
					    				BigDecimal halfPayLeaveBalance = BigDecimal.ZERO;				    					
					    				if (employeeMap.get("earnedLeaveBalance") != null) {
					    					earnedLeaveBalance = (BigDecimal)employeeMap.get("earnedLeaveBalance");				    						
					    				}				    					
					    				if (employeeMap.get("casualLeaveBalance") != null) {
					    					casualLeaveBalance = (BigDecimal)employeeMap.get("casualLeaveBalance");				    						
					    				}
					    				if (employeeMap.get("halfPayLeaveBalance") != null) {
					    					halfPayLeaveBalance = (BigDecimal)employeeMap.get("halfPayLeaveBalance");				    						
					    				}	
					    				employee.setLeaveBalanceDate(leaveBalanceDate);
				    					employee.setEarnedLeave(earnedLeaveBalance.floatValue());
				    					employee.setCasualLeave(casualLeaveBalance.floatValue());
				    					employee.setHalfPayLeave(halfPayLeaveBalance.floatValue());	
					    			}
			    					employees.add(employee);					    			
				    		  	}   
				    			datasource.insertEmployees(employees);
				    		}	
				    	}
				    	datasource.close();
				    	if (listFragment != null) {
					    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
				    		listFragment.notifyChange();
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					Toast.makeText( context, "Updated employees!", Toast.LENGTH_SHORT ).show();
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "Update employees failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}	
    	if (listFragment != null) {
	    	//Log.d(module, "calling listFragment notifyChange..." + listFragment.getClass().getName());						    		
    		listFragment.notifyChange();
    	}
	}	
	
	public void fetchMyEmployeeDetails(ProgressBar progressBar, final HRDashboardActivity activity) {
		Map paramMap = new HashMap();		
		final EmployeeDataSource datasource = new EmployeeDataSource(context);
		datasource.open();
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("fetchEmployeeDetails", paramMap, progressBar, new XMLRPCMethodCallback() {	
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) { 
						SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");  		    	  						
				    	Map employeeDetailsResult = (Map)((Map)result).get("employeeDetailsResult");
				    	Map employeeMap = (Map) employeeDetailsResult.get("employeeProfile");
				    	if (employeeMap != null) {
				    		String id = (String)employeeMap.get("employeeId");
				    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				    		SharedPreferences.Editor prefEditor = prefs.edit();
				    		prefEditor.putString("employeeId", id);
				    		prefEditor.apply();
				    		String name = (String)employeeMap.get("name");
				    		String department = (String)employeeMap.get("department");	
				    		String position = (String)employeeMap.get("position");	
				    		String phoneNumber = (String)employeeMap.get("phoneNumber");
				    		Date joinDate = new Date();
				    		String joinDateStr = (String)employeeMap.get("joinDate");
				    		try {
				    			joinDate = format.parse(joinDateStr);
				    		} catch (ParseException e) {
				    			// just go with default date for now
				    		}
					    	String weeklyOff = (String)employeeMap.get("weeklyOff");				    		
				    		Employee employee = new Employee(id, name, department, 
				    				position, phoneNumber, joinDate, weeklyOff);
				    		if (employeeMap.get("leaveBalanceDate") != null) {
				    			Date leaveBalanceDate = (Date)employeeMap.get("leaveBalanceDate");
				    			//Log.d(module, "leaveBalanceDate=" + leaveBalanceDate);	
				    			BigDecimal earnedLeaveBalance = BigDecimal.ZERO;
				    			BigDecimal casualLeaveBalance = BigDecimal.ZERO;
				    			BigDecimal halfPayLeaveBalance = BigDecimal.ZERO;				    					
				    			if (employeeMap.get("earnedLeaveBalance") != null) {
				    				earnedLeaveBalance = (BigDecimal)employeeMap.get("earnedLeaveBalance");				    						
				    			}				    					
				    			if (employeeMap.get("casualLeaveBalance") != null) {
				    				casualLeaveBalance = (BigDecimal)employeeMap.get("casualLeaveBalance");				    						
				    			}
				    			if (employeeMap.get("halfPayLeaveBalance") != null) {
				    				halfPayLeaveBalance = (BigDecimal)employeeMap.get("halfPayLeaveBalance");				    						
				    			}	
				    			employee.setLeaveBalanceDate(leaveBalanceDate);
				    			employee.setEarnedLeave(earnedLeaveBalance.floatValue());
				    			employee.setCasualLeave(casualLeaveBalance.floatValue());
				    			employee.setHalfPayLeave(halfPayLeaveBalance.floatValue());
			    				//Log.d(module, "employeeId=" + employee.getId());	
			    				//Log.d(module, "leaveBalanceDate=" + employee.getLeaveBalanceDate());	
			    				//Log.d(module, "earnedLeaveBalance=" + employee.getEarnedLeave());				    				
				    		}
				    		datasource.updateEmployee(employee); 
				    		Object[] payslips = (Object[]) employeeDetailsResult.get("payslips");
					    	if (payslips != null) {
					    		final PayslipDataSource payslipDS = new PayslipDataSource(context);
					    		payslipDS.open();
					    		payslipDS.deleteAllPayslips();
					    		payslipDS.insertPayslips(id, payslips);
					    		payslipDS.close();
					    	}
//					    	if (activity != null) {
//					    		activity.updateEmployeeDetails(employee);
//					    	}				    		
				    	}

				    	datasource.close();

					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					//Toast.makeText( context, "got facility dues!", Toast.LENGTH_SHORT ).show();	    		    			
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			Toast.makeText( context, "fetchMyEmployeeDetails failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
	}		
	
	public void fetchEmployeeRecentLeaves(final MenuItem menuItem, ProgressBar progressBar, final LeaveActivity leaveActivity) {
		Map paramMap = new HashMap();		
		final EmployeeDataSource emplDatasource = new EmployeeDataSource(context);
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("fetchEmployeeRecentLeaves", paramMap, progressBar, new XMLRPCMethodCallback() {	
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) { 
						//SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd");
				    	Map employeeLeavesResult = (Map)((Map)result).get("employeeLeavesResult");
			    		Object[] recentLeaves = (Object[]) employeeLeavesResult.get("recentLeaves");
				    	if (recentLeaves != null) {
				    		String id = (String)employeeLeavesResult.get("employeeId");
				    		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
				    		SharedPreferences.Editor prefEditor = prefs.edit();
				    		prefEditor.putString("employeeId", id);
				    		prefEditor.apply();
				    		emplDatasource.open();
				    		Employee employee = emplDatasource.getEmployeeDetails(id);		
				    		if (employee != null) {        
					    		if (employeeLeavesResult.get("leaveBalanceDate") != null) {
					    			Date leaveBalanceDate = (Date)employeeLeavesResult.get("leaveBalanceDate");
					    			//Log.d(module, "leaveBalanceDate=" + leaveBalanceDate);	
					    			BigDecimal earnedLeaveBalance = BigDecimal.ZERO;
					    			BigDecimal casualLeaveBalance = BigDecimal.ZERO;
					    			BigDecimal halfPayLeaveBalance = BigDecimal.ZERO;				    					
					    			if (employeeLeavesResult.get("earnedLeaveBalance") != null) {
					    				earnedLeaveBalance = (BigDecimal)employeeLeavesResult.get("earnedLeaveBalance");				    						
					    			}				    					
					    			if (employeeLeavesResult.get("casualLeaveBalance") != null) {
					    				casualLeaveBalance = (BigDecimal)employeeLeavesResult.get("casualLeaveBalance");				    						
					    			}
					    			if (employeeLeavesResult.get("halfPayLeaveBalance") != null) {
					    				halfPayLeaveBalance = (BigDecimal)employeeLeavesResult.get("halfPayLeaveBalance");				    						
					    			}	
					    			employee.setLeaveBalanceDate(leaveBalanceDate);
					    			employee.setEarnedLeave(earnedLeaveBalance.floatValue());
					    			employee.setCasualLeave(casualLeaveBalance.floatValue());
					    			employee.setHalfPayLeave(halfPayLeaveBalance.floatValue());
					    		}
						    	Object[] leaves = (Object[]) employeeLeavesResult.get("recentLeaves");
						    	if (leaves != null) {
						    		final LeavesDataSource leavesDS = new LeavesDataSource(context);
						    		leavesDS.open();
						    		leavesDS.deleteAllLeaves();
						    		leavesDS.insertLeaves(id, leaves);
						    		leavesDS.close();		
						    	}
				    				//Log.d(module, "employeeId=" + employee.getId());	
				    				//Log.d(module, "leaveBalanceDate=" + employee.getLeaveBalanceDate());	
				    				//Log.d(module, "earnedLeaveBalance=" + employee.getEarnedLeave());				    				
					    		emplDatasource.updateEmployee(employee); 
					    		emplDatasource.close();
						    	if (leaveActivity != null) {
						    		leaveActivity.updateLeaveDetails(employee);
						    	}						    		
				    		}
			    		
				    	}
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
					//Toast.makeText( context, "got facility dues!", Toast.LENGTH_SHORT ).show();	    		    			
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "fetchEmployeeRecentLeaves failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
	}	

	
	public void fetchEmployeeAttendance(final MenuItem menuItem,ProgressBar progressBar, final AttendanceListFragment listFragment) {
		Map paramMap = new HashMap();		
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("fetchEmployeeAttendance", paramMap, progressBar, new XMLRPCMethodCallback() {	
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
				    	Map employeeAttendanceResult = (Map)((Map)result).get("employeeAttendanceResult");
			    		Object[] recentPunches = (Object[]) employeeAttendanceResult.get("recentPunches");
				    	if (recentPunches != null) {
					    	Object[] punches = (Object[]) employeeAttendanceResult.get("recentPunches");
					    	if (punches != null) {
					    		String id = (String)employeeAttendanceResult.get("employeeId");
					    		final AttendanceDataSource attendanceDS = new AttendanceDataSource(context);
					    		attendanceDS.open();
					    		attendanceDS.deleteAllPunches();
					    		attendanceDS.insertPunches(id, punches);
					    		attendanceDS.close();		
					    	} 
					    	if (listFragment != null) {
					    		listFragment.notifyChange();
					    	}
				    	}				    		  	
					} 
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			Toast.makeText( context, "fetchEmployeeAttendance failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
	}	
	
	public void fetchEmployeeLastPunch(String employeeId, final EmployeeDetailsActivity activity) {
		Map paramMap = new HashMap();		
		paramMap.put("partyId", employeeId); 
//Log.d( module, "paramMap =" + paramMap); 
		
		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("fetchEmployeeLastPunch", paramMap, null, new XMLRPCMethodCallback() {	
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
				    	Map employeeLastPunchResult = (Map)((Map)result).get("employeeLastPunchResult");
				    	Map punchMap = (Map) employeeLastPunchResult.get("punch");
				    	if (punchMap != null) {
				    		String punchTime = (String)punchMap.get("punchTime");
				    		String inOut = (String)punchMap.get("inOut");
					    	if (activity != null && punchTime != null) {
					    		activity.updateLastPunchTime(punchTime, inOut);
					    	}
				    	}				    		  	
					} 
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			//Toast.makeText( context, "fetchEmployeeLastPunch failed: " + e, Toast.LENGTH_SHORT ).show();	    		    			
		}		
	}		

	public void syncLocations(final MenuItem menuItem, ProgressBar progressBar, final MapsActivity locationActivity) {
		final LocationsDataSource datasource = new LocationsDataSource(context);
		datasource.open();  
		final Map [] locations = datasource.getXMLRPCSerializedUnsyncedLocations();
		if (locations ==null || locations.length == 0) {
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null) {
				menuItem.setActionView(null);
			}

			//locationActivity.showSnackBar(locationActivity.getString(R.string.synced_location));

			return;
		}
		Map paramMap = new HashMap();
		//SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    paramMap.put("locations",locations);	 

		try {   
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("uploadPartyLocations", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						if (result != null) {
					    	Map uploadLocationsResults = (Map)((Map)result).get("uploadLocationsResults");
					    	Log.d(module, "uploadLocationsResults = " + uploadLocationsResults);
					    	datasource.changeLocationsSyncStatus();
				    	}
				    	datasource.close();
					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);

					}
					locationActivity.showSnackBar(locationActivity.getString(R.string.sync_success_location));
					locationActivity.markLocations(null);
					locationActivity.recyclerViewFragment.markLocations(null);
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			locationActivity.showSnackBar(locationActivity.getString(R.string.sync_failed_location));
//			Toast.makeText( context, "Sync locations failed: " + e, Toast.LENGTH_SHORT ).show();
		}	
		
	}	
	
	public static boolean isNetworkAvailable(Context context) 
	{
	    return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
	}

	public void fetchPrevLocations(final MenuItem menuItem, ProgressBar progressBar, final MapsActivity mapsActivity){
		Map paramMap = new HashMap();



		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String storeId = prefs.getString("partyId", "");
		paramMap.put("partyId", storeId);
		final Date thruDate = new Date();
		final Date fromDate = DateUtil.addDays(thruDate, -31);
		paramMap.put("fromDate", fromDate.getTime());
		paramMap.put("thruDate", thruDate.getTime());

		try {
			XMLRPCApacheAdapter adapter = new XMLRPCApacheAdapter(context);
			adapter.call("fetchPartyVisitedLocations", paramMap, progressBar, new XMLRPCMethodCallback() {
				public void callFinished(Object result, ProgressBar progressBar) {
					if (result != null) {
						final LocationsDataSource datasource = new LocationsDataSource(context);
						datasource.open();
						Map paymentsResult = (Map)((Map)result).get("locationsMap");
						Log.d("Upendra", "locations.size() = " + paymentsResult);


						datasource.updatePrevLocations();
						if (paymentsResult.size() > 0) {

							Map dayWise = (Map)paymentsResult.get("dayWise");
							if (!dayWise.isEmpty()){
								for ( Object key : dayWise.keySet() ) {
									Log.v("Key",""+key.toString());
									Object[] locations = (Object[])dayWise.get(key);

									for (int i=0;i<locations.length;i++){
										Map locationMap = (Map)locations[i];

										datasource.insertPrevLocation(((BigDecimal)locationMap.get("latitude")).doubleValue(),
												((BigDecimal)locationMap.get("longitude")).doubleValue(),
												((Date) locationMap.get("createdDate")).getTime(),(String)locationMap.get("noteName"),
												(String)locationMap.get("noteInfo"));


									}
								}
							}
						}
						datasource.close();

					}
					if (progressBar != null) {
						progressBar.setVisibility(View.INVISIBLE);
					}
					if(menuItem !=null){
						if (progressBar != null) {
							progressBar.setVisibility(View.VISIBLE);
						}
						menuItem.setActionView(null);
					}

					mapsActivity.showSnackBar(mapsActivity.getString(R.string.sync_map_locations));
					mapsActivity.markLocations(null);
					mapsActivity.recyclerViewFragment.markLocations(null);
				}
			});
		}
		catch (Exception e) {
			Log.e(module, "Exception: ", e);
			if (progressBar != null) {
				progressBar.setVisibility(View.INVISIBLE);
			}
			if(menuItem !=null){
				if (progressBar != null) {
					progressBar.setVisibility(View.VISIBLE);
				}
				menuItem.setActionView(null);
			}
			//mapsActivity.showSnackBar(mapsActivity.getString(R.string.sync_failed_location));
		}

	}
}
