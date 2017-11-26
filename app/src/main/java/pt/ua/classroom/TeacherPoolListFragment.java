package pt.ua.classroom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class TeacherPoolListFragment extends Fragment {

    private static final String TAG = "TeacherPoolListFragment";

    private ArrayAdapter<TeacherPool> adapterItems;
    private OnItemSelectedListener listener;

    interface OnItemSelectedListener {
        void onItemSelected(TeacherPool i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            Log.d(TAG, String.valueOf(activity));
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement TeacherPoolListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create arraylist from item fixtures
        ArrayList<TeacherPool> pools = TeacherPool.getPools();
        adapterItems = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1, pools);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_teacher_pool_list, container, false);

        // Bind adapter to ListView
        ListView lvItems = view.findViewById(R.id.lvPools);
        lvItems.setAdapter(adapterItems);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                // Retrieve item based on position
                TeacherPool i = adapterItems.getItem(position);
                // Fire selected event for item
                listener.onItemSelected(i);
            }
        });
        return view;
    }
}