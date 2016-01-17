package cn.lyh.buckupapk;

import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private List<Map<Integer, Object>> datas = null;
    private Handler getDataHandler = null;
    private Handler backupHandler = null;
    private BackupAdapter adapter = null;
    private String path = null;
    private RecyclerView recyclerView = null;
    private Boolean isVersion5 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        path = Environment.getExternalStorageDirectory().getPath()
                + File.separator + "backup_apk";
        if (Build.VERSION.SDK_INT >= 21) {
            isVersion5 = true;
        }
        initData();
    }

    private void initData() {
        getDataHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == 0x111) {
                    adapter = new BackupAdapter(MainActivity.this, datas);
                    recyclerView.setAdapter(adapter);
                    DividerItemDecoration dividerItemDecoration =
                            new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL_LIST);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    findViewById(R.id.textView).setVisibility(View.GONE);
                    getSupportActionBar().setTitle("BackupAPK("+datas.size() + ")");
                }
                return false;
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                datas = Buckup.getAllApps(MainActivity.this);
                getDataHandler.sendEmptyMessage(0x111);
            }
        }).start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_backup:
                backup();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void backup() {
        //创建文件夹
        File file = new File(path);
        if (!file.exists()) file.mkdirs();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_layout, null);
        final AlertDialog dialog = builder.setView(view).create();
        dialog.show();

        final int count = datas.size();

        backupHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == count) {
                    dialog.hide();
                    Toast.makeText(MainActivity.this, "备份完成！", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < datas.size(); i++) {
                    if (adapter.getCheckeds().get(i)) {
                        Map<Integer, Object> map = datas.get(i);
                        String package_name = map.get(Buckup.PACKAGE_NAME).toString();
                        String file_name = map.get(Buckup.APP_NAME) + ".apk";
                        if (isVersion5) {
                            Buckup.backupApp5(package_name, path, file_name);
                        } else {
                            Buckup.backupApp(package_name, path, file_name);
                        }
                    }
                    backupHandler.sendEmptyMessage(i + 1);
                }
            }
        }).start();
    }


}
