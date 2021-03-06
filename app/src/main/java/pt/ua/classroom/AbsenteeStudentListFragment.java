package pt.ua.classroom;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class AbsenteeStudentListFragment extends Fragment {

    private static final String TAG = "AbStudentListFragment";

    private ArrayAdapter<AbsenteeStudent> adapterItems;
    private ListView lvItems;
    private OnItemSelectedListener listener;

    interface OnItemSelectedListener {
        void onItemSelected(AbsenteeStudent i);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement AbStudentListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Create arraylist from item fixtures

        ArrayList<AbsenteeStudent> students = AbsenteeStudent.getStudents();
        adapterItems = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_list_item_activated_1, students);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view
        View view = inflater.inflate(R.layout.fragment_absentee_student_list, container, false);

        // Bind adapter to ListView
        lvItems = view.findViewById(R.id.lvAbsenteeStudents);
        lvItems.setAdapter(adapterItems);
        lvItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View item, int position, long rowId) {
                // Retrieve item based on position
                AbsenteeStudent i = adapterItems.getItem(position);
                // Fire selected event for item
                listener.onItemSelected(i);
            }
        });
        return view;
    }

    /**
     * Turns on activate-on-click mode. When this mode is on, list items will be
     * given the 'activated' state when touched.
     */
    public void setActivateOnItemClick(boolean activateOnItemClick) {
        // When setting CHOICE_MODE_SINGLE, ListView will automatically
        // give items the 'activated' state when touched.
        lvItems.setChoiceMode(
                activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
                        : ListView.CHOICE_MODE_NONE);
    }
}