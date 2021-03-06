package com.shyam.rollcall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.*;

/**
 * Created by shyamsundar on 3/30/2014.
 */
public class ClassFragment extends android.app.Fragment {
    public ClassFragment(){}
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    database db;
    Toast toast;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.show_class, container, false);
        toast=Toast.makeText(getActivity(), "",Toast.LENGTH_SHORT);
        // get the listview
        expListView = (ExpandableListView) rootView.findViewById(R.id.lvExp);
        db= new database(getActivity());
        // preparing list data
        try {
            prepareListData();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),

                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                toast.setText(listDataHeader.get(groupPosition));
                toast.show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                // Toast.makeText(getApplicationContext(),
                //       listDataHeader.get(groupPosition) + " Collapsed",


            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                // TODO Auto-generated method stub
                if(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)=="Empty Class"){
                    toast.setText("Nothing To Show");
                    toast.show();
                }
                else{
                    try{
                        db.open();
                        long l=Long.parseLong(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition));
                        toast.setText(listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition)
                                + " : "
                                +db.getname(listDataHeader.get(groupPosition),l));
                        toast.show();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }}
                return false;
            }
        });
        return rootView;
    }
    /*
     * Preparing the list data
     */
    private void prepareListData() throws SQLException {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        String[] array=new String[8];
        array[0]="Class_1";
        array[1]="Class_2";
        array[2]="Class_3";
        array[3]="Class_4";
        array[4]="Class_5";
        array[5]="Class_6";
        array[6]="Class_7";
        array[7]="Class_8";
        for(int i=0;i<8;i++){
            listDataHeader.add(array[i]);
            List<String> students = new ArrayList<String>();
            db.open();
            students=db.getEnr1(array[i]);
            db.close();
            if(students.size()==0)
            {students.add("Empty Class");
                listDataChild.put(listDataHeader.get(i),students);}
            else
            { Collections.sort(students, new Comparator<String>() {
                @Override
                public int compare(String s, String s2) {

                    return s.compareTo(s2);
                }
            });
                listDataChild.put(listDataHeader.get(i),students);}
        }

    }
    @Override
    public void onPause() {
        super.onPause();
        toast.cancel();
    }
}
