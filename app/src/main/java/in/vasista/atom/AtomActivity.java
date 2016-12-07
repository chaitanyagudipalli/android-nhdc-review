package in.vasista.atom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import in.vasista.nhdcapp.R;
import in.vasista.vsales.DashboardAppCompatActivity;
import in.vasista.vsales.PaymentActivity;
import in.vasista.vsales.SalesDashboardActivity;
import in.vasista.vsales.sync.ServerSync;

import com.atom.mobilepaymentsdk.PayActivity;
import com.payu.india.Payu.PayuConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by upendra on 18/11/16.
 */
public class AtomActivity extends DashboardAppCompatActivity{

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentChildView(R.layout.atom_payment);
        actionBarHomeEnabled();

        intent=getIntent();



    }

    Button payMerchantNB;
    Button payMerchantDC;
    Spinner Bank;
    Spinner cardType;
    Spinner PaymentType;

    EditText et_nb_amt, et_card_amt;

    String orderId;
    String prodId;
    boolean env_live;
    String customerName;
    String partyId;
    @Override
    public void onResume()
    {
        super.onResume();
        System.out.println("In On Resume");
       // setContentView(R.layout.mainpage);

        et_nb_amt = (EditText) findViewById(R.id.et_nb_amt);

        //et_card_amt = (EditText) findViewById(R.id.et_nb_amt);

        if(""+intent.getFloatExtra("amount",60.000f) != null) {
            et_nb_amt.setText(""+intent.getFloatExtra("amount",60.000f));
            //et_card_amt.setText(""+intent.getFloatExtra("amount",60.000f));
            et_nb_amt.setEnabled(true);
            //et_card_amt.setEnabled(true);
            orderId = intent.getStringExtra("orderId");
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        env_live = prefs.getBoolean("payuLive",false)? true:false;
        prodId = prefs .getString("productStoreId","");
        //cardType = (Spinner) findViewById(R.id.sp_cardType);
        //PaymentType = (Spinner)findViewById(R.id.sp_paymentType);
        //Bank = (Spinner)findViewById(R.id.sp_bank);
        customerName = prefs.getString("customerName","");
        partyId = prefs.getString("storeId","");
        payMerchantNB = (Button) findViewById(R.id.btn_payMerchantNB);
        payMerchantNB.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                String amt = et_nb_amt.getText().toString();

                if(true)
                {

                    Double doubleAmt = Double.valueOf(amt);
                    amt = doubleAmt.toString();
//					String bankId = "2001";



//					Intent newPayIntent = new Intent(MPSActivity.this,	PayActivity.class);
//
//			        newPayIntent.putExtra("merchantId", "18718");
//			        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
//			        newPayIntent.putExtra("loginid", "18718");
//			        newPayIntent.putExtra("password", "ed1b76ba");
//			        newPayIntent.putExtra("prodid", "SANTOSH");
//			        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
//			        newPayIntent.putExtra("clientcode", "001");
//			        newPayIntent.putExtra("custacc", "100000036600");
//			        newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
//			        newPayIntent.putExtra("txnid", "662016053501");
//			        newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
//			        newPayIntent.putExtra("bankid", ""); //Should be valid bank id
//
//			        //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
//			        newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
////			        newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production
//
//			        //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
////			        newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)
//
//			        //Optinal Parameters
////			        newPayIntent.putExtra("customerName", "JKL PQR"); //Only for Name
////			        newPayIntent.putExtra("customerEmailID", "jkl.pqr@atomtech.in");//Only for Email ID
////			        newPayIntent.putExtra("customerMobileNo", "9876543210");//Only for Mobile Number
////			        newPayIntent.putExtra("billingAddress", "Mumbai");//Only for Address
////			        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 1");// Can pass any data
//
//			        startActivityForResult(newPayIntent, 1);



                    Intent newPayIntent = new Intent(AtomActivity.this,	PayActivity.class);

                    newPayIntent.putExtra("merchantId", "21089");
                    newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
                    newPayIntent.putExtra("loginid", "21089");
                    newPayIntent.putExtra("password", "NHDC@1234");
                    newPayIntent.putExtra("prodid", prodId);
                    newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
                    byte[] encodePartyId = Base64.encode(partyId.getBytes(), Base64.DEFAULT);
                    newPayIntent.putExtra("clientcode", new String(encodePartyId));
                    newPayIntent.putExtra("custacc", "100000036600");
                    newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
                    String tempStr = partyId+customerName;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("ddmmyyyyHHmmss");
                    String tempDate = sdf1.format(new Date());
                    tempStr = tempStr+tempDate;
                    String txnId = "";
                    try{
                        txnId = makeSHA1Hash(tempStr);
                    } catch(NoSuchAlgorithmException e)
                    {
                        e.printStackTrace();
                    }
                    catch(UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    newPayIntent.putExtra("txnid", txnId);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                    String date  = dateFormat.format(new Date());

                    newPayIntent.putExtra("date",date);//Should be in same format
                    newPayIntent.putExtra("bankid", ""); //Should be valid bank id

                    //if(env_live) {
                        //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
                        newPayIntent.putExtra("ru", "https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
                    //}else {
                        //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
                        //newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)
                    //}

                    //Optinal Parameters
                    newPayIntent.putExtra("udf1", customerName); //Only for Name
                    newPayIntent.putExtra("udf9", orderId);
                    //newPayIntent.putExtra("customerEmailID", "raviteja@vasita.in");//Only for Email ID
                    //newPayIntent.putExtra("customerMobileNo", "965211525");//Only for Mobile Number
                    //newPayIntent.putExtra("billingAddress", "Hyderabadd");//Only for Address
                    //newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 1");// Can pass any data

                    startActivityForResult(newPayIntent, 1);
                }
            }
        });

        /*payMerchantDC = (Button) findViewById(R.id.btn_payMerchantDC);
        payMerchantDC.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String amt = et_nb_amt.getText().toString();
                String strPaymentMode = null, strCardType = null;
                int PaymentTypePos = PaymentType.getSelectedItemPosition();
                int cardTypePos = cardType.getSelectedItemPosition();

                if(amt.equalsIgnoreCase(""))
                {
                    Toast.makeText(AtomActivity.this, "Please enter valid amount", Toast.LENGTH_LONG).show();
                }
                else if(PaymentTypePos==0)
                {
                    Toast.makeText(AtomActivity.this, "Please select valid Payment Mode", Toast.LENGTH_LONG).show();
                }
                else if(cardTypePos==0)
                {
                    Toast.makeText(AtomActivity.this, "Please select valid Card Type", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Double doubleAmt = Double.valueOf(amt);
                    amt = doubleAmt.toString();

                    switch(PaymentTypePos)
                    {
                        case 1:
                            strPaymentMode = "CC";
                            break;
                        case 2:
                            strPaymentMode = "DC";
                            break;
                    }


                    switch(cardTypePos)
                    {
                        case 1:
                            strCardType = "VISA";
                            break;
                        case 2:
                            strCardType = "MAESTRO";
                            break;
                        case 3:
                            strCardType = "MASTER";
                            break;
                    }


//					Intent newPayIntent = new Intent(MPSActivity.this,	PayActivity.class);
//				 	newPayIntent.putExtra("merchantId", "18718");
//			        newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
//			        newPayIntent.putExtra("loginid", "18718");
//			        newPayIntent.putExtra("password", "ed1b76ba");
//			        newPayIntent.putExtra("prodid", "SANTOSH");
//			        newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
//			        newPayIntent.putExtra("clientcode", "001");
//			        newPayIntent.putExtra("custacc", "100000036600");
//			        newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000
//			        newPayIntent.putExtra("txnid", "662016053501");
//			        newPayIntent.putExtra("date", "25/08/2015 18:31:00");//Should be in same format
//			        newPayIntent.putExtra("cardtype", strPaymentMode);// CC or DC ONLY (value should be same as commented)
//					newPayIntent.putExtra("cardAssociate", strCardType);// VISA or MASTER or MAESTRO ONLY (value should be same as commented)
//
//					//use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
//			        newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production
////			        newPayIntent.putExtra("ru","https://sslpayment.atomtech.in/mobilesdk/param"); //ru FOR Production
//
//			        //Optinal Parameters
//					newPayIntent.putExtra("customerName", "LMN PQR");//Only for Name
//			        newPayIntent.putExtra("customerEmailID", "pqr.lmn@atomtech.in");//Only for Email ID
//			        newPayIntent.putExtra("customerMobileNo", "9978868666");//Only for Mobile Number
//			        newPayIntent.putExtra("billingAddress", "Pune");//Only for Address
//			        newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data
//
//					startActivityForResult(newPayIntent, 1);



                    Intent newPayIntent = new Intent(AtomActivity.this,	PayActivity.class);
                    newPayIntent.putExtra("merchantId", "21089");
                    newPayIntent.putExtra("txnscamt", "0"); //Fixed. Must be �0�
                    newPayIntent.putExtra("loginid", "21089");
                    newPayIntent.putExtra("password", "NHDC@1234");
                    newPayIntent.putExtra("prodid", prodId);
                    newPayIntent.putExtra("txncurr", "INR"); //Fixed. Must be �INR�
                    byte[] encodePartyId = Base64.encode(partyId.getBytes(), Base64.DEFAULT);
                    newPayIntent.putExtra("clientcode", new String(encodePartyId));
                    newPayIntent.putExtra("custacc", "100000036600");
                    newPayIntent.putExtra("channelid", "INT");
                    newPayIntent.putExtra("amt", amt);//Should be 3 decimal number i.e 1.000

                    String tempStr = partyId+customerName;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("ddmmyyyyHHmmss");
                    String tempDate = sdf1.format(new Date());
                    tempStr = tempStr+tempDate;
                    String txnId = "";
                    try{
                        txnId = makeSHA1Hash(tempStr);
                    } catch(NoSuchAlgorithmException e)
                    {
                        e.printStackTrace();
                    }
                    catch(UnsupportedEncodingException e)
                    {
                        e.printStackTrace();
                    }
                    newPayIntent.putExtra("txnid", txnId);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
                    String date  = dateFormat.format(new Date());

                    newPayIntent.putExtra("date",date);//Should be in same format
                    newPayIntent.putExtra("cardtype", strPaymentMode);// CC or DC ONLY (value should be same as commented)
                    newPayIntent.putExtra("cardAssociate", strCardType);// VISA or MASTER or MAESTRO ONLY (value should be same as commented)

                    //use below Production url only with Production "Library-MobilePaymentSDK", Located inside PROD folder
                    newPayIntent.putExtra("ru","https://payment.atomtech.in/mobilesdk/param"); //ru FOR Production

                    //use below UAT url only with UAT "Library-MobilePaymentSDK", Located inside UAT folder
                    //newPayIntent.putExtra("ru", "https://paynetzuat.atomtech.in/mobilesdk/param"); // FOR UAT (Testing)

                    //Optinal Parameters
                    newPayIntent.putExtra("udf1", customerName); //Only for Name
                    newPayIntent.putExtra("udf9", orderId);
                    //newPayIntent.putExtra("customerEmailID", "raviteja@vasista.in   ");//Only for Email ID
                    //newPayIntent.putExtra("customerMobileNo", "9652115255");//Only for Mobile Number
                    //newPayIntent.putExtra("billingAddress", "HYDERABAD");//Only for Address
                    //newPayIntent.putExtra("optionalUdf9", "OPTIONAL DATA 2");// Can pass any data

                    startActivityForResult(newPayIntent, 1);


                }
            }
        });*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed here it is 2
        System.out.println("RESULTCODE--->" + resultCode);
        System.out.println("REQUESTCODE--->" + requestCode);
        System.out.println("RESULT_OK--->" + RESULT_OK);

        if (requestCode == 1) {
            System.out.println("---------INSIDE-------");

            if (data != null) {
                String message = data.getStringExtra("status");
                String[] resKey = data.getStringArrayExtra("responseKeyArray");
                String[] resValue = data.getStringArrayExtra("responseValueArray");

                if(resKey!=null && resValue!=null)
                {
                    for(int i=0; i<resKey.length; i++)
                        System.out.println("  "+i+" resKey : "+resKey[i]+" resValue : "+resValue[i]);

                    try {

                        if(resValue[Arrays.asList(resKey).indexOf("f_code")].equalsIgnoreCase("success_00")){
                            HashMap<String,String> transaction= new HashMap<>();
                            transaction.put("txnid", resValue[Arrays.asList(resKey).indexOf("bank_txn")]);
                            transaction.put("amount",resValue[Arrays.asList(resKey).indexOf("amt")]);
                            transaction.put("addedon",resValue[Arrays.asList(resKey).indexOf("date")]);
                            transaction.put("orderId",orderId);
                            ServerSync serverSync = new ServerSync(this);
                            serverSync.uploadAtomPayment(null,transaction,this);
                        }else{
                            new AlertDialog.Builder(this)
                                    .setCancelable(false)
                                    .setMessage("Transaction failed.")
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            onBackPressed();
                                        }
                                    }).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(this, message, Toast.LENGTH_LONG).show();
                System.out.println("RECEIVED BACK--->" + message);
            }

        }

    }
    public void paymentDone(){
        startActivity(new Intent(this, SalesDashboardActivity.class));
    }

    public static String makeSHA1Hash(String input)
            throws NoSuchAlgorithmException, UnsupportedEncodingException
    {
        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.reset();
        byte[] buffer = input.getBytes("UTF-8");
        md.update(buffer);
        byte[] digest = md.digest();

        String hexStr = "";
        for (int i = 0; i < digest.length; i++) {
            hexStr +=  Integer.toString( ( digest[i] & 0xff ) + 0x100, 16).substring( 1 );
        }
        return hexStr;
    }

}
