package com.karthikb351.hems_bt_client.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.karthikb351.hems_bt_client.R;
import com.karthikb351.hems_bt_client.objects.Device;
import com.karthikb351.hems_bt_client.objects.PlugHelper;
import com.karthikb351.hems_bt_client.objects.TariffHelper;

import org.w3c.dom.Text;

import java.util.Calendar;
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
        viewHolder.power.setText(d.getPowerRating());
        viewHolder.voltage.setText(d.getVoltageRating());
        viewHolder.current.setText(d.getCurrentRating());

        viewHolder.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                switch (v.getId()) {
                    case R.id.dToggle:
                        // Is the toggle on?
                        boolean on = ((ToggleButton) v).isChecked();

                        if (on) {
                            if(TariffHelper.isPeakTime())
                                new AlertDialog.Builder(context)
                                    .setTitle("Peak Usage Period")
                                    .setMessage("You will be charged higher rates during this time. Consider using this device later.")
                                    .setCancelable(false)
                                    .setPositiveButton("Turn on anyway", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            sendBTCommand(PlugHelper.getOnCommandForPlug(d.getPlug()));
                                            d.setStartTime(Calendar.getInstance().getTimeInMillis());
                                            viewHolder.status.setText("Enabled");
                                            d.setStatus(true);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            ((ToggleButton) v).setChecked(false);
                                        }
                                    })
                                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogInterface dialog) {
                                            ((ToggleButton) v).setChecked(false);
                                        }
                                    })
                                    .create().show();
                            else {
                                sendBTCommand(PlugHelper.getOnCommandForPlug(d.getPlug()));
                                d.setStartTime(Calendar.getInstance().getTimeInMillis());
                                viewHolder.status.setText("Enabled");
                                d.setStatus(true);
                            }
                        } else {
                            sendBTCommand(PlugHelper.getOffCommandForPlug(d.getPlug()));
                            d.setEndTime(Calendar.getInstance().getTimeInMillis());
                            viewHolder.status.setText("Disabled");
                            d.setStatus(false);
                        }
                }
            }
        });
    }

    public abstract void sendBTCommand(String cmd);

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public final static class ListItemViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView power;
        public TextView voltage;
        public TextView current;
        public TextView status;

        public ToggleButton toggle;


        public ListItemViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.dName);
            power = (TextView)itemView.findViewById(R.id.dPower);
            voltage = (TextView)itemView.findViewById(R.id.dVoltage);
            current = (TextView)itemView.findViewById(R.id.dCurrent);
            status = (TextView)itemView.findViewById(R.id.dStatus);
            toggle = (ToggleButton)itemView.findViewById(R.id.dToggle);
        }
    }

}
