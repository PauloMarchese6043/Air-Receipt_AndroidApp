package airreceipt.com.air_reeipt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ListViewAdapter extends BaseAdapter{

    //variables
    Context mContext;
    LayoutInflater inflater;
    List<Model> modelList;
    ArrayList<Model> arrayList;

    //constructor
    public ListViewAdapter(Context mContext, List<Model> modelList) {
        mContext = mContext;
        this.modelList = modelList;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<Model>();
        this.arrayList.addAll(modelList);

    }

    public class ViewHolder {
        TextView mStore, mPrice;
        ImageView mIcon;
    }


    @Override
    public int getCount() {
        return modelList.size();
    }

    @Override
    public Object getItem(int i) {
        return modelList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view==null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_receipt_list_row, null);

            //locate the views in activity_receipt_list_row.xml
            holder.mStore = view.findViewById(R.id.storeName);
            holder.mPrice = view.findViewById(R.id.price);
            holder.mIcon = view.findViewById(R.id.receiptIcon);

            view.setTag(holder);
        }
        else {
            holder = (ViewHolder) view.getTag();

        }

        //set results into textviews
        holder.mStore.setText(modelList.get(position).getStore());
        holder.mPrice.setText(modelList.get(position).getPrice());
        holder.mIcon.setImageResource(modelList.get(position).getIcon());

        //listview item clicks
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //code later
            }
        });


        return view;
    }

    //filter
    public void filter(String charText) {
         charText = charText.toLowerCase(Locale.getDefault());
         modelList.clear();
         if (charText.length() ==0) {
             modelList.addAll(arrayList);
         }
         else {
             for (Model model : arrayList) {
                 if (model.getStore().toLowerCase(Locale.getDefault())
                     .contains(charText)) {
                     modelList.add(model);
                 }
             }
         }
         notifyDataSetChanged();
    }
}
