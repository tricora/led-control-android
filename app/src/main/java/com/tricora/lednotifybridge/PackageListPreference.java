package com.tricora.lednotifybridge;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by Tille on 03.01.2016.
 */
public class PackageListPreference extends DialogPreference implements View.OnClickListener, AdapterView.OnItemLongClickListener {

    private SharedPreferences prefs;
    private ListView packagesListView;
    final ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.package_list_item, R.id.packageTitle);
    Button newPackageButton;
    EditText newPackageEditText;

    public PackageListPreference(Context context, AttributeSet attrs) {
        super(context, attrs);

        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        setPersistent(false);
        setDialogLayoutResource(R.layout.package_preference);
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        newPackageButton = (Button) view.findViewById(R.id.buttonNewPackage);
        newPackageButton.setOnClickListener(this);

        newPackageEditText = (EditText) view.findViewById(R.id.editTextNewPackage);

        packagesListView = (ListView) view.findViewById(R.id.packagesListView);
        packagesListView.setAdapter(adapter);

        packagesListView.setOnItemLongClickListener(this);

        loadListItems();
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {
            saveListItems();
        }
    }

    private void loadListItems() {

        adapter.clear();
        Set<String> packages = prefs.getStringSet(getContext().getResources().getString(R.string.pref_key_allowed_packages), new HashSet<String>(Arrays.asList("com.whatsapp")));
        Iterator<String> iter = packages.iterator();

        ArrayList<String> values = new ArrayList<String>();
        while (iter.hasNext()) {
            adapter.add(iter.next());
        }
    }

    private void saveListItems() {
        Set<String> vals = new HashSet<String>();
        for (int i = 0; i < adapter.getCount(); i++) {
            vals.add((String)adapter.getItem(i));
        }

        SharedPreferences.Editor editor = prefs.edit();
        editor.putStringSet(getContext().getResources().getString(R.string.pref_key_allowed_packages), vals);
        editor.commit();
    }

    private int c = 0;
    @Override
    public void onClick(View v) {
        String val = newPackageEditText.getText().toString();
        if (val.equals("")) {
            return;
        }
        int pos = adapter.getPosition(val);
        if (pos == -1) {
            adapter.add(val);
            newPackageEditText.setText("");
        }

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        adapter.remove(adapter.getItem(position));
        return true;
    }
}
