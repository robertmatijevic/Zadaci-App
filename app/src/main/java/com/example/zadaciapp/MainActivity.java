package com.example.zadaciapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.example.zadaciapp.bazapodataka.DatabaseHandler;
import com.example.zadaciapp.modeli.Task;

public class MainActivity extends AppCompatActivity {
    private ListView lvTask;
    private TaskListAdapter adapter;
    private List<Task> mTaskList;
    private static DatabaseHandler dbHandler;
    static final int EDIT_TASK_CODE = 1;
    static final int CREATE_TASK_CODE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvTask = (ListView)findViewById(R.id.listview_tasks);

        mTaskList = new ArrayList<>();

        dbHandler = new DatabaseHandler(getApplicationContext());

        mTaskList = dbHandler.getAllTasks();


        Collections.sort(mTaskList,new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                return o1.getCalendar().compareTo(o2.getCalendar());

            }
        });

        adapter = new TaskListAdapter(getApplicationContext(), mTaskList);
        lvTask.setAdapter(adapter);
        lvTask.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int taskId = (int)view.getTag();
                Toast.makeText(getApplicationContext(), "Clicked product id = " + id, Toast.LENGTH_SHORT).show();

            }
        });
        lvTask.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            }
        });

        Log.i("Pocetni zapisi", mTaskList.toString());
        registerForContextMenu(lvTask);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId()==R.id.listview_tasks) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu, menu);
        }
    }



    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch(item.getItemId()) {
            case R.id.edit:
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                intent.putExtra("taskForEdit", mTaskList.get(info.position));
                startActivityForResult(intent, EDIT_TASK_CODE);
                return true;
            case R.id.delete:
                dbHandler.deleteTask(mTaskList.get(info.position));
                mTaskList.remove((info.position));
                adapter.notifyDataSetChanged();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TASK_CODE || requestCode == CREATE_TASK_CODE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                Task task = (Task) getIntent().getSerializableExtra("newTask");

                mTaskList = dbHandler.getAllTasks();

                Collections.sort(mTaskList, new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        return o1.getCalendar().compareTo(o2.getCalendar());

                    }
                });
                Log.i("Novi zapisi", mTaskList.toString());
                adapter = new TaskListAdapter(getApplicationContext(), mTaskList);
                lvTask.setAdapter(adapter);

                //adapter.notifyDataSetChanged();
                Log.i("DOŠO", "na kraju");
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_add:
                Intent intent = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivityForResult(intent, CREATE_TASK_CODE);
                return true;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    public static DatabaseHandler getDbHandler() {
        return dbHandler;
    }
}
