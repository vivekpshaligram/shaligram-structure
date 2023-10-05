package com.codestracture.ui.main.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.smstpdu.ApplicationPortAddressing16BitAddress;
import org.mobicents.protocols.ss7.map.smstpdu.ApplicationPortAddressing16BitAddressImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsDeliverTpduImpl;
import org.mobicents.protocols.ss7.map.smstpdu.SmsTpduImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class PingSmsReceiver extends BroadcastReceiver {
    public static final String TAG = "MyTag";

    public static final DateFormat sdf = SimpleDateFormat.getDateTimeInstance();

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "PingSmsReceiver:- onReceive", Toast.LENGTH_SHORT).show();
        if (!Telephony.Sms.Intents.DATA_SMS_RECEIVED_ACTION.equals(intent.getAction())) {
            Log.d(TAG, "Received intent, not DATA_SMS_RECEIVED_ACTION, but " + intent.getAction());
            return;
        }
        Log.d(TAG, "Received intent, DATA_SMS_RECEIVED_ACTION");
        try {
            Bundle bundle = intent.getExtras();
            if (bundle == null) {
                return;
            }

            Object[] PDUs = (Object[]) bundle.get("pdus");
            for (Object pdu : PDUs != null ? PDUs : new Object[0]) {
                StringBuilder sb = new StringBuilder();
                for (byte b : (byte[]) pdu) {
                    sb.append(String.format("%02x", b));
                }
                String pdusString = sb.toString();
                ArrayList<String> data = parseSmsPDUs(pdusString);
                Log.d(TAG,"MessageData:" + new Gson().toJson(data));

                Toast.makeText(context, "PingSmsReceiver:- onReceive : -> " + pdusString, Toast.LENGTH_SHORT).show();
                Toast.makeText(context, "PingSmsReceiver:- onReceive : -> " + new Gson().toJson(data), Toast.LENGTH_LONG).show();
            }

            /*Object[] PDUs = (Object[]) bundle.get("pdus");
            for (Object pdu : PDUs != null ? PDUs : new Object[0]) {
                StringBuilder sb = new StringBuilder();
                for (byte b : (byte[]) pdu) {
                    sb.append(String.format("%02x", b));
                }

                Toast.makeText(context, "PingSmsReceiver:- onReceive : -> " + sb.toString(), Toast.LENGTH_SHORT).show();
            }*/
        } catch (Exception e) {
            Toast.makeText(context, "PingSmsReceiver:- onReceive Exception : -> " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Notes: PDU will always be SMS
     */
    public ArrayList<String> parseSmsPDUs(String PDU) {
        ArrayList<String> parsedPDUs = new ArrayList<>();

        StringBuilder sb = new StringBuilder();

        ApplicationPortAddressing16BitAddress smsPort = null;
        SmsTpduImpl smsTpdu = null;

        try {
            // PDU should always be SMS-DELIVER prefixed by SMSC info (SCA - Service Centre Address)
            byte[] pduWithSCA = pduHexToByteArray(PDU);
            // Cut off the SCA
            byte[] smsPdu = Arrays.copyOfRange(pduWithSCA, pduWithSCA[0] + 1, pduWithSCA.length);

            smsTpdu = SmsTpduImpl.createInstance(smsPdu, false, null);
            switch (smsTpdu.getSmsTpduType()) {
                case SMS_DELIVER:
                    ((SmsDeliverTpduImpl) smsTpdu).getUserData().decode();

                    // see https://github.com/RestComm/jss7/issues/275
                    byte[] dtr = ((SmsDeliverTpduImpl) smsTpdu).getUserData().getDecodedUserDataHeader().getAllData().get(5);
                    smsPort = new ApplicationPortAddressing16BitAddressImpl(dtr);
                    break;
            }
        } catch (MAPException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            Log.d(TAG,"parseSmsPDUs -> Exception:" + e.getMessage());
            // ignore the exceptions
            e.printStackTrace();
        }


        SmsMessage sms = SmsMessage.createFromPdu(pduHexToByteArray(PDU));
        //sb.append("Raw PDU (hex): ").append(PDU);
        sb.append("Date/Time: ").append(sdf.format(sms.getTimestampMillis()));
        sb.append("\nSMSC: ").append(formatNumber(sms.getServiceCenterAddress()));
        sb.append("\nFrom: ").append(formatNumber(sms.getOriginatingAddress()));
        sb.append("\nFrom (display): ").append(sms.getDisplayOriginatingAddress());
        sb.append("\nFrom port: ").append(smsPort == null ? "N/A" : smsPort.getOriginatorPort());
        sb.append("\nTo port: ").append(smsPort == null ? "N/A" : smsPort.getDestinationPort());
        sb.append("\nData (hex): ");

        for (byte b : sms.getUserData()) {
            sb.append(String.format("%02x", b));
        }

        if (smsTpdu != null) {
            sb.append("\n\nAll info\n\n").append(smsTpdu);
        }

        //sb.append("\nUser Data (ascii):");
        //sb.append(new String(sms.getUserData()));

        parsedPDUs.add(sb.toString());
        parsedPDUs.add(PDU);

        return parsedPDUs;
    }

    public byte[] pduHexToByteArray(String PDU) {
        if (PDU.length() % 2 != 0) {
            Log.e(TAG, "wrong number of bytes to pduHexToByteArray");
            return new byte[0];
        }
        byte[] converted = new byte[PDU.length() / 2];
        for (int i = 0; i < (PDU.length() / 2); i++) {
            converted[i] = (byte) ((Character.digit(PDU.charAt(i * 2), 16) << 4)
                    + Character.digit(PDU.charAt((i * 2) + 1), 16));
        }
        return converted;
    }

    public static String formatNumber(String number) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return PhoneNumberUtils.formatNumber(number, Locale.getDefault().getCountry());
        } else {
            return PhoneNumberUtils.formatNumber(number);
        }
    }
}
