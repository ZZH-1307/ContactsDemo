package contactsdemo.zhaozihao.com.contactsdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;


public class MainActivity extends ActionBarActivity {
    private static final String TAG ="MainActivity" ;
    Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
    Uri dataUri = Uri.parse("content://com.android.contacts/data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void printCursor(Cursor cursor){
        if (cursor != null && cursor.getCount() > 0){
            while (cursor.moveToNext()){
            int column = cursor.getColumnCount();
                for (int i = 0;i < column;i++){
                    String name = cursor.getColumnName(i);
                    String value = cursor.getString(i);
                    Log.i(TAG,"这是第"+cursor.getPosition()+"行的数据"+name+"-->"+value);

                }
            }
            cursor.close();
        }
    }
    public void queryContacts(View view) {
        Cursor cursor = getContentResolver().query(uri, new String[]{"_id"},null,null,null);
        //printCursor(cursor);
        if (cursor!=null&&cursor.getCount()>0){
            while(cursor.moveToNext()){
                int id = cursor.getInt(0);
                String selection = "raw_contact_id = ?";
                String[] selectionArgs = {String.valueOf(id)};
                Cursor c = getContentResolver().query(dataUri,new String[]{"data1","mimetype"}, selection,selectionArgs,null);
                if (c!=null&&c.getCount()>0){
                    while (c.moveToNext()){

                            String mimetype = c.getString(1);
                            String data1 = c.getString(0);
                            if("vnd.android.cursor.item/name".equals(mimetype)){
                                Log.i(TAG,"这是第"+c.getPosition()+"行的数据"+"姓名"+"-->"+data1);
                            }else  if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                                Log.i(TAG,"这是第"+c.getPosition()+"行的数据"+"电话"+"-->"+data1);
                            }



                    }
                    c.close();
                }
            }
        }
    }

    public void addContacts(View view) {
        Cursor cursor = getContentResolver().query(uri,new String[]{"contact_id"},null,null,"contact_id desc limit 1");
        if (cursor!=null&&cursor.moveToFirst()){
            int contact_id = cursor.getInt(0);
            contact_id++;
            cursor.close();

            ContentValues value = new ContentValues();
            value.put("contact_id",contact_id);
            getContentResolver().insert(uri,value);


            //
            value = new ContentValues();
            value.put("mimetype","vnd.android.cursor.item/name");
            value.put("data1","中国移动");
            value.put("raw_contact_id",contact_id);
            getContentResolver().insert(dataUri,value);

            value = new ContentValues();
            value.put("mimetype","vnd.android.cursor.item/phone_v2");
            value.put("data1","10086");
            value.put("raw_contact_id",contact_id);
            getContentResolver().insert(dataUri,value);



        }

    }
}
