package cn.lyh.buckupapk;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BackupAdapter extends RecyclerView.Adapter<BackupAdapter.ViewHolder> {

    private List<Map<Integer, Object>> data = null;
    private MainActivity activity = null;
    private Map<Integer, Boolean> checkeds = new HashMap<>();

    public BackupAdapter(MainActivity activity, List<Map<Integer, Object>> data) {
        this.data = data;
        this.activity = activity;
        for (int i = 0; i < data.size(); i++) {
            checkeds.put(i, true);
        }
    }

    public Map<Integer, Boolean> getCheckeds() {
        return checkeds;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cb_checked.setChecked(checkeds.get(position));
        holder.iv_logo.setImageDrawable((Drawable) data.get(position).get(Buckup.LOGO));
        holder.tv_app_name.setText(data.get(position).get(Buckup.APP_NAME).toString());
        holder.tv_package_name.setText(data.get(position).get(Buckup.PACKAGE_NAME).toString());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked(position);
            }
        });
        holder.cb_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checked(position);
            }
        });
    }

    private void checked(int position) {
        if (checkeds.get(position)) {
            checkeds.put(position, false);
        } else {
            checkeds.put(position, true);
        }
        int count = 0;
        for (int i = 0; i < checkeds.size(); i++) {
            if (checkeds.get(i)) {
                count++;
            }
        }
        activity.getSupportActionBar().setTitle("BackupAPK(" + count + ")");
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cb_checked;
        private ImageView iv_logo;
        private TextView tv_app_name, tv_package_name;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.cb_checked = (CheckBox) itemView.findViewById(R.id.cb_checked);
            this.iv_logo = (ImageView) itemView.findViewById(R.id.iv_logo);
            this.tv_app_name = (TextView) itemView.findViewById(R.id.tv_app_name);
            this.tv_package_name = (TextView) itemView.findViewById(R.id.tv_package_name);
        }
    }
}
