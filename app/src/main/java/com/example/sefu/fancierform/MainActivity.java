package com.example.sefu.fancierform;

import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sefu.fancierform.model.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends TabActivity {
    List<Restaurant> model = new ArrayList<Restaurant>();
    RestaurantAdapter adapter = null;
    Restaurant current = null;

    EditText name;
    EditText address;
    EditText notes;
    RadioGroup types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button save = (Button) findViewById(R.id.save);

        name = (EditText) findViewById(R.id.name);
        address = (EditText) findViewById(R.id.addr);
        types = (RadioGroup) findViewById(R.id.types);

        save.setOnClickListener(onSave);
        ListView list = (ListView) findViewById(R.id.restaurants);
        adapter = new RestaurantAdapter();
        list.setAdapter(adapter);
        list.setOnItemClickListener(onListClick);

        TabHost.TabSpec spec = getTabHost().newTabSpec("tag1");
        spec.setContent(R.id.restaurants);
        spec.setIndicator("List", getResources().getDrawable(R.drawable.ic_apps_black_24dp));
        getTabHost().addTab(spec);


        spec = getTabHost().newTabSpec("tag2");
        spec.setContent(R.id.details);
        spec.setIndicator("Details", getResources().getDrawable(R.drawable.ic_assignment_black_24dp));
        getTabHost().addTab(spec);
        getTabHost().setCurrentTab(0);


    }

    private View.OnClickListener onSave = new View.OnClickListener() {
        public void onClick(View v) {
            current=new Restaurant();
            current.setName(name.getText().toString());
            current.setAddress(address.getText().toString());
            current.setNotes(notes.getText().toString());

            switch (types.getCheckedRadioButtonId()) {
                case R.id.sit_down:
                    current.setType("sit_down");
                    break;
                case R.id.take_out:
                    current.setType("take_out");
                    break;
                case R.id.delivery:
                    current.setType("delivery");
                    break;
            }
            adapter.add(current);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.option, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String message = "No restaurant selected";
        if (id == R.id.toast) {

            if (current != null) {
                message = current.getName();
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            return (true);
        }

        return super.onOptionsItemSelected(item);
    }

    class RestaurantAdapter extends ArrayAdapter<Restaurant> {
        RestaurantAdapter() {
            super(MainActivity.this, R.layout.row, model);
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;
            RestaurantHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.row, parent, false);
                holder = new RestaurantHolder(row);
                row.setTag(holder);
            } else {
                holder = (RestaurantHolder) row.getTag();
            }
            holder.populateFrom(model.get(position));
            return (row);
        }
    }

    static class RestaurantHolder {
        private TextView name = null;
        private TextView address = null;
        private ImageView icon = null;
        private View row = null;

        RestaurantHolder(View row) {
            this.row = row;
            name = (TextView) row.findViewById(R.id.title);
            address = (TextView) row.findViewById(R.id.address);
            icon = (ImageView) row.findViewById(R.id.icon);
        }

        void populateFrom(Restaurant r) {
            name.setText(r.getName());
            address.setText(r.getAddress());
            if (r.getType().equals("sit_down")) {
                icon.setImageResource(R.drawable.ic_local_hotel_black_24dp);
            } else if (r.getType().equals("take_out")) {
                icon.setImageResource(R.drawable.ic_computer_black_24dp);
            } else {
                icon.setImageResource(R.drawable.ic_attachment_black_24dp);
            }
        }
    }


    private AdapterView.OnItemClickListener onListClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            current=model.get(position);
            name.setText(current.getName());
            address.setText(current.getAddress());
            notes.setText(current.getNotes());

            if (current.getType().equals("sit_down")) {
                types.check(R.id.sit_down);
            } else if (current.getType().equals("take_out")) {
                types.check(R.id.take_out);
            } else {
                types.check(R.id.delivery);
            }

            getTabHost().setCurrentTab(1);
        }
    };
}