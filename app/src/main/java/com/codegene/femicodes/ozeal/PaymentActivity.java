package com.codegene.femicodes.ozeal;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {
    EditText txEmail, txCardNumber, txAmount, txCvv;
    AppCompatSpinner spExpiryMonth, spExpiryYear;
    AppCompatButton btPay;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getSupportActionBar().setTitle("Payment");
        viewInit();
        btPay.setOnClickListener(this);
    }

    //View Initialization
    private void viewInit() {
        txEmail = findViewById(R.id.etEmail);
        txCardNumber = findViewById(R.id.etCardNumber);
        spExpiryMonth = findViewById(R.id.spExpiryMonth);
        spExpiryYear = findViewById(R.id.spExpiryYear);
        txAmount = findViewById(R.id.etAmount);
        txCvv = findViewById(R.id.etCvv);
        btPay = findViewById(R.id.btPay);
        //Set Array Of Months To Spinner
        ArrayList<String> monthsList = new ArrayList<String>();
        String[] months = new DateFormatSymbols().getMonths();
        for (int i = 0; i < months.length; i++) {
            monthsList.add(months[i]);
        }
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, monthsList);
        spExpiryMonth.setAdapter(monthAdapter);
        //Set Array Of Years To Spinner
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = thisYear; i <= 2050; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, years);
        spExpiryYear.setAdapter(yearAdapter);
    }

    private void payInit() {
        Util util = new Util();
        if (!util.isEmailValid(txEmail.getText().toString().trim())) {
            txEmail.setError("Please Input Valid Email Address");
        } else if (txCardNumber.getText().toString().trim().isEmpty()) {
            txCardNumber.setError("Please Input Card Number");
        } else if (txAmount.getText().toString().trim().isEmpty()) {
            txAmount.setError("Please Input Amount");
        } else if (txCvv.getText().toString().trim().length() < 3 || txCvv.getText().toString().trim().length() > 3) {
            txCvv.setError("Please Input Valid Card CVV");
        } else {
            //Check If user is connected to the internet or not
            if (AppStatus.getInstance(this).isOnline()) {
                String email = txEmail.getText().toString();
                String cardNumber = txCardNumber.getText().toString();
                int exMonth = util.getMonthInt(spExpiryMonth.getSelectedItem().toString().toLowerCase());//convert month(String) to Int
                int exYear = Integer.parseInt(spExpiryYear.getSelectedItem().toString());
                int amount = Integer.parseInt(txAmount.getText().toString());
                String cvc = txCvv.getText().toString();
                progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                progressDialog.show();
                Pay(email, cardNumber, exMonth, exYear, amount, cvc);
            } else {
                //Network Connection Error
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle("Network Error");
                builder.setMessage("Please Connect To The Internet And Try Again");

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }
        }
    }

    //Method To Process Payment
    private void Pay(String email, String cardNumber, int expiryMonth, int expiryYear, int amount, String cvc) {
        //create a charge
        Charge charge = new Charge();
        charge.setCard(new Card.Builder(cardNumber, expiryMonth, expiryYear, cvc).build());
        charge.setAmount(amount);
        charge.setEmail(email);
        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                // This is called only after transaction is deemed successful
                // retrieve the transaction, and send its reference to your server
                // for verification.
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle("Success");
                builder.setMessage("Payment Processed Successfully");

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
                // This is called only before requesting OTP
                // Save reference so you may send to server. If
                // error occurs with OTP, you should still verify on server
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
                progressDialog.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                builder.setTitle("Payment Error");
                builder.setMessage(error.getMessage());

                String positiveText = getString(android.R.string.ok);
                builder.setPositiveButton(positiveText,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog = builder.create();
                // display dialog
                dialog.show();
            }

        });
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btPay:
                payInit();//Start Payment Process
                break;
        }
    }
}