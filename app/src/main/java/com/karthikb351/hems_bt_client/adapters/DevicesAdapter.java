package com.karthikb351.hems_bt_client.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.karthikb351.hems_bt_client.R;
import com.karthikb351.hems_bt_client.objects.Device;
import com.karthikb351.hems_bt_client.objects.PlugHelper;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by karthikbalakrishnan on 28/03/15.
 */
public abstract class DevicesAdapter extends RecyclerView.Adapter<DevicesAdapter.ListItemViewHolder> {
    private final Context context;
    public List<Device> devices;

    public DevicesAdapter(Context context, List<Device> devices) {
        this.context = context;
        this.devices = devices;
    }

    public List<Device> getDevices() {
        return devices;
    }

    public void setDevices(List<Device> devices) {
        this.devices = devices;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.device_list_item,
                        viewGroup,
                        false);

        return new ListItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ListItemViewHolder viewHolder, int position) {
        final Device d = devices.get(position);
        viewHolder.name.setText(d.getName());
        viewHolder.tag.setText(d.getRfidTag());
        viewHolder.power.setText(d.getPowerRating());
        viewHolder.voltage.setText(d.getVoltageRating());
        viewHolder.current.setText(d.getCurrentRating());

        viewHolder.mListener = new ListItemViewHolder.ViewHolderClick() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.btnToggle:
                        // Is the toggle on?
                        boolean on = ((ToggleButton) v).isChecked();

                        if (on) {
                            sendBTCommand(PlugHelper.getOnCommandForPlug(d.getPlug()));
                            viewHolder.status.setText("Enabled");
                        } else {
                            sendBTCommand(PlugHelper.getOffCommandForPlug(d.getPlug()));
                            viewHolder.status.setText("Disabled");
                        }

                }
            }
        };
    }

    public abstract void sendBTCommand(String cmd);

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tag;
        public TextView name;
        public TextView power;
        public TextView voltage;
        public TextView current;
        public TextView status;

        public ToggleButton toggle;

        public ViewHolderClick mListener;


        public ListItemViewHolder(View itemView) {
            super(itemView);
            tag = (TextView)itemView.findViewById(R.id.dTag);
            name = (TextView)itemView.findViewById(R.id.dName);
            power = (TextView)itemView.findViewById(R.id.dPower);
            voltage = (TextView)itemView.findViewById(R.id.dVoltage);
            current = (TextView)itemView.findViewById(R.id.dCurrent);
            status = (TextView)itemView.findViewById(R.id.dStatus);
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        public static interface ViewHolderClick {
            public void onClick(View v);
        }
    }

}
