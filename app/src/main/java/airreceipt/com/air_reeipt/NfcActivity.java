package airreceipt.com.air_reeipt;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Locale;

public class NfcActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    EditText txtTagContent;

    ToggleButton toggleBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        txtTagContent = (EditText) findViewById(R.id.txtTagContent);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        toggleBtn = (ToggleButton)findViewById(R.id.toggleBtn);

        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show();

        }
        else {
            Toast.makeText(this, "NFC Available", Toast.LENGTH_LONG).show();
//            finish();

        }


    }

    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        if(intent.hasExtra(NfcAdapter.EXTRA_TAG)) {
            Toast.makeText(this,"NFC Intent", Toast.LENGTH_LONG).show();

            if (toggleBtn.isChecked()) {
                Parcelable[] parcelable = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

                if (parcelable != null && parcelable.length > 0 ) {

                    readTextFromMessage ((NdefMessage) parcelable[0]);

                } else {
                    Toast.makeText(this, "No NDEF MESSAGE FOUND!", Toast.LENGTH_LONG).show();
                }
            }

            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            NdefMessage ndefMessage = createNdefMessage(txtTagContent.getText() + "");

            writeNdefMessage(tag, ndefMessage);
        }
    }

    private void readTextFromMessage(NdefMessage ndefMessage) {

        NdefRecord[] ndefRecords = ndefMessage.getRecords();

        if(ndefRecords != null && ndefRecords.length > 0) {

            NdefRecord ndefRecord = ndefRecords[0];

            String tagContent = getTextFromNdefRecord(ndefRecord);

            txtTagContent.setText(tagContent);

        } else {
            Toast.makeText(this, "No NDEF RECORDS FOUND!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {

        enableForegroundDispatchSystem();

        super.onResume();
    }

    @Override
    protected void onPause() {

        disableForegroundDispatchSystem();
        super.onPause();
    }
    private void enableForegroundDispatchSystem() {

        Intent intent = new Intent (this, NfcActivity.class).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        nfcAdapter.enableForegroundDispatch(this, pendingIntent,intentFilters, null);

    }

    private void disableForegroundDispatchSystem() {

        nfcAdapter.disableForegroundDispatch(this);

    }
//
    private void formatTag(Tag tag, NdefMessage ndefMessage ) {
        try {

            NdefFormatable ndefFormatable = NdefFormatable.get(tag);

            if(ndefFormatable == null) {
                Toast.makeText(this, "Tag is not formatable", Toast.LENGTH_LONG).show();
                return;
            }

                ndefFormatable.connect();
                ndefFormatable.format(ndefMessage);
                ndefFormatable.close();

            Toast.makeText(this, "Tag written", Toast.LENGTH_LONG).show();


        }catch (Exception e) {
            Log.e("formatTag", e.getMessage());

        }
    }

    private void writeNdefMessage(Tag tag, NdefMessage ndefMessage) {

        try {

            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null", Toast.LENGTH_LONG).show();
                return;
            }

            Ndef ndef = Ndef.get(tag);

            if (ndef == null) {
                //format tag with the ndef
                formatTag(tag, ndefMessage);

            }else {
                ndef.connect();
                if(!ndef.isWritable()) {

                    Toast.makeText(this, "Tag is not writable", Toast.LENGTH_LONG).show();
                    ndef.close();
                    return;
                }

                ndef.writeNdefMessage(ndefMessage);
                ndef.close();

                Toast.makeText(this, "Tag written!", Toast.LENGTH_LONG).show();
            }


        }catch (Exception e) {
            Log.e("formatTag", e.getMessage());

        }
    }
//
    private NdefRecord createTextRecord(String content) {
        try {
            byte[] language;
            language = Locale.getDefault().getLanguage().getBytes("UTF-8");

            final byte[] text = content.getBytes("UTF-8");
            final int languageSize = language.length;
            final int textLength = text.length;
            final ByteArrayOutputStream payload = new ByteArrayOutputStream(1 + languageSize + textLength);

            payload.write ((byte) (languageSize & 0x1F));
            payload.write(language, 0, languageSize);
            payload.write(text, 0, textLength);

            return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], payload.toByteArray());

        } catch (UnsupportedEncodingException e ) {

            Log.e("Create TextRecord", e.getMessage());

        }
        return null;
    }
//
    private NdefMessage createNdefMessage(String content) {

        NdefRecord ndefRecord = createTextRecord(content);

        NdefMessage ndefMessage = new NdefMessage(new NdefRecord[]{ndefRecord});

        return ndefMessage;
    }

    private void tglReadWriteOnclick(View view) {
        txtTagContent.setText("");
    }

    public String getTextFromNdefRecord(NdefRecord ndefRecord) {
        String text = null;
        try {
            byte[] payload = ndefRecord.getPayload();
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
            int languageSize = payload[0] & 0063;
            text = new String (payload, languageSize + 1, payload.length - languageSize - 1, textEncoding);
        } catch (UnsupportedEncodingException e ){
            Log.e("getTextFromNdefRecord", e.getMessage(), e);

        }
        return text;
    }



}

