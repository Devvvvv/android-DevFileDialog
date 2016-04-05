package inc.dev.nix.timetable.DevFileDialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Window;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


public class DevFileDialog {
    Context context = null;
    AlertDialog.Builder alertDialog;
    ArrayList<String> type_items_list = new ArrayList<String>();

    AlertDialog ad = null;

    private DevFileDialogListener listener;

    String nowPath, oldPath;

    public interface DevFileDialogListener {
        public void OnSelectedFile(String patch,String fileName);
    }

    public DevFileDialog (Context lContext) {
        this.context = lContext;

        alertDialog = new AlertDialog.Builder(lContext);

        nowPath = oldPath = getRootPath();

        alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ad.dismiss();
            }
        });
    }

    public void show() {
        setItems(getListFiles(nowPath));
        ad = alertDialog.show();
    }


    public void setTitle(String str) {
        alertDialog.setTitle(str);
    }

    public void setItems(final String[] items) {
        /*alertDialog.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //System.out.println("DevLogs "+type_items_list.get(which));

            }
        });*/


        alertDialog.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (type_items_list.get(which).equals("dir")) {
                    if (which == 0 && !nowPath.equals("/")) {
                        nowPath = getParentPath(nowPath);
                    } else {
                        nowPath += ((nowPath.equals("/")) ? "" : "/") + items[which];
                    }

                    setItems(getListFiles(nowPath));

                    alertDialog = new AlertDialog.Builder(context);

                    setItems(getListFiles(nowPath));

                    alertDialog.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ad.dismiss();
                        }
                    });

                    ad = alertDialog.show();
                } else {
                    listener.OnSelectedFile(nowPath, items[which]);
                }
            }
        });
    }

    public void setOpenDialogListener(DevFileDialogListener listener) {
        this.listener = listener;
    }

    public String[] getListFiles(String path) {
        File f = new File(path);
        File[] fileList = f.listFiles();
        ArrayList<String> listFileList = new ArrayList<String>();

        type_items_list.clear();

        if (!path.equals("/")) {
            listFileList.add("...");
        }
        type_items_list.add("dir");


        try{
            for (File file : fileList) {
                listFileList.add(file.getName());
                type_items_list.add(((file.isDirectory()) ? "dir" : "") + ((file.isFile()) ? "file" : ""));
            }
        }catch (Exception e) {
            Toast.makeText(context,"Ошибка чтения каталога", Toast.LENGTH_SHORT).show();
        }
        setTitle(path);
        return listFileList.toArray(new String[listFileList.size()]);
    }

    public String getRootPath() {
        String sdState = android.os.Environment.getExternalStorageState();
        if (sdState.equals(android.os.Environment.MEDIA_MOUNTED)) {
            return android.os.Environment.getExternalStorageDirectory().getPath();
        } else {
            return context.getCacheDir().getPath();
        }
    }

    public String getParentPath (String path) {
        File pathFile = new File(path);
        return pathFile.getParent();
    }

    public DevFileDialog setPath (String path) {
        File check_path = new File(path);
        System.out.println("setPath " + check_path.exists() + " " + path);
        if (check_path.exists()) {
            System.out.println("setPath ok");
            nowPath = oldPath = path;
        }
        return this;
    }
}