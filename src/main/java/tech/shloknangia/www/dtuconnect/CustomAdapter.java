package tech.shloknangia.www.dtuconnect;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SHLOK on 16-04-2017.
 */
public class CustomAdapter extends ArrayAdapter<String> {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private final Context context;
    private ArrayList nameList,bloodGroupList,phoneList;
    private SparseBooleanArray mSelectedItemsIds;



    public CustomAdapter(Context context, ArrayList nameList,ArrayList bloodGroupList,ArrayList phoneList) {
        super(context,android.R.layout.simple_list_item_multiple_choice,nameList);
        this.context = context;
        this.nameList = nameList;
        this.bloodGroupList = bloodGroupList;
        this.phoneList = phoneList;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rowitem, parent, false);
        TextView name = (TextView) rowView.findViewById(R.id.name);
        TextView blood = (TextView) rowView.findViewById(R.id.bloodgroup);

        TextView number = (TextView) rowView.findViewById(R.id.phone);


        name.setText(nameList.get(position).toString());
        blood.setText(bloodGroupList.get(position).toString());
        number.setText(phoneList.get(position).toString());

        // Change the icon for Windows and iPhone




        return rowView;
    }


}

