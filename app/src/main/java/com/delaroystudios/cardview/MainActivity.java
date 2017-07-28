package com.delaroystudios.cardview;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AlbumsAdapter adapter;
    private List<Album> albumList;
    //Variables Bluetooth
    public static final String TAG = "LEDv0";
    public static final boolean D = true;
    // Tipos de mensaje enviados y recibidos desde el Handler de ConexionBT
    public static final int Mensaje_Estado_Cambiado = 1;
    public static final int Mensaje_Leido = 2;
    public static final int Mensaje_Escrito = 3;
    public static final int Mensaje_Nombre_Dispositivo = 4;
    public static final int Mensaje_TOAST = 5;
    public static final int MESSAGE_Desconectado = 6;
    public static final int REQUEST_ENABLE_BT = 7;

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";
    //Nombre del dispositivo conectado
    private String mConnectedDeviceName = null;
    // Adaptador local Bluetooth
    private BluetoothAdapter AdaptadorBT = null;
    //Objeto miembro para el servicio de ConexionBT
    public static ConexionBT Servicio_BT = null;
    //Vibrador
    private Vibrator vibrador;
    //variables para el Menu de conexi�n
    private boolean seleccionador=false;
    public int Opcion=R.menu.activity_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new AlbumsAdapter(this, albumList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(6), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();
        try {
            Glide.with(this).load(R.drawable.portada).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {
        int[] covers = new int[]{
                R.drawable.al2,
                R.drawable.jin,
                R.drawable.al3,
                R.drawable.al4,
                R.drawable.cerveza1,
                R.drawable.naranja,
                R.drawable.album7,
                R.drawable.album8,
                };

        Album a = new Album("Cuba Libre", 13, covers[0]);
        albumList.add(a);

        a = new Album("Destornillador", 8, covers[1]);
        albumList.add(a);

        a = new Album("Jim Tonic", 11, covers[2]);
        albumList.add(a);

        a = new Album("Tonic", 12, covers[3]);
        albumList.add(a);

        a = new Album("Cerveza", 14, covers[4]);
        albumList.add(a);

        a = new Album("Jugo de Naranja", 1, covers[5]);
        albumList.add(a);

        a = new Album("Black Eyed Peas", 11, covers[6]);
        albumList.add(a);

        adapter.notifyDataSetChanged();
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    public  void onStart() {
        super.onStart();
        ConfigBT();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (Servicio_BT != null) Servicio_BT.stop();//Detenemos servicio
    }

    public void ConfigBT(){
        // Obtenemos el adaptador de bluetooth
        AdaptadorBT = BluetoothAdapter.getDefaultAdapter();
        if (AdaptadorBT.isEnabled()) {//Si el BT esta encendido,
            if (Servicio_BT == null) {//y el Servicio_BT es nulo, invocamos el Servicio_BT
                Servicio_BT = new ConexionBT(this, mHandler);
            }
        }
        else{ if(D) Log.e("Setup", "Bluetooth apagado...");
            Intent enableBluetooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBluetooth, REQUEST_ENABLE_BT);
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Una vez que se ha realizado una actividad regresa un "resultado"...
        switch (requestCode) {
            case REQUEST_ENABLE_BT://Respuesta de intento de encendido de BT
                if (resultCode == Activity.RESULT_OK) {//BT esta activado,iniciamos servicio
                    ConfigBT();
                } else {//No se activo BT, salimos de la app
                    finish();}
        }//fin de switch case
    }//fin de onActivityResult

    @Override
    public boolean onPrepareOptionsMenu(Menu menux){
        //cada vez que se presiona la tecla menu  este metodo es llamado
        menux.clear();//limpiamos menu actual
        if (seleccionador==false)Opcion=R.menu.activity_main;//dependiendo las necesidades
        if (seleccionador==true)Opcion=R.menu.desconecta;  // crearemos un menu diferente
        getMenuInflater().inflate(Opcion, menux);
        return super.onPrepareOptionsMenu(menux);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Conexion:
                if(D) Log.e("conexion", "conectandonos");
                vibrador = (Vibrator) getSystemService(VIBRATOR_SERVICE);

                String address = "00:13:04:11:11:53";//Direccion Mac del  rn42
                BluetoothDevice device = AdaptadorBT.getRemoteDevice(address);
                Servicio_BT.connect(device);
                vibrador.vibrate(1000);
                //estado.setText("Conectado :)");
                return true;
            case R.id.Salir:
                finish();

            case R.id.desconexion:
                //if (Servicio_BT != null)
                   sendMessage(null,"2");
                    //Servicio_BT.stop();//Detenemos servicio
               // estado.setText("Desconectado :(");
                return true;


        }//fin de swtich de opciones
        return false;
    }//fin de metodo onOptionsItemSelected

    //Enviar mennsaje
    public static  void sendMessage(Context context,String message) {
        if (Servicio_BT.getState() == ConexionBT.STATE_CONNECTED) {//checa si estamos conectados a BT
            if (message.length() > 0) {   // checa si hay algo que enviar
                byte[] send = message.getBytes();//Obtenemos bytes del mensaje
                if(D) Log.e(TAG, "Mensaje enviado:"+ message);
                Servicio_BT.write(send);     //Mandamos a escribir el mensaje
            }
        } else Toast.makeText(context, "No conectado", Toast.LENGTH_SHORT).show();
    }//fin de sendMessage


    final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_Escrito:
                    byte[] writeBuf = (byte[]) msg.obj;//buffer de escritura...
                    // Construye un String del Buffer
                    String writeMessage = new String(writeBuf);
                    if(D) Log.e(TAG, "Message_write  =w= "+ writeMessage);
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_Leido:
                    byte[] readBuf = (byte[]) msg.obj;//buffer de lectura...
                    //Construye un String de los bytes validos en el buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    if(D) Log.e(TAG, "Message_read   =w= "+ readMessage);
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_Nombre_Dispositivo:
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME); //Guardamos nombre del dispositivo
                    Toast.makeText(getApplicationContext(), "Conectado con "+ mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    seleccionador=true;
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case Mensaje_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
                case MESSAGE_Desconectado:
                    if(D) Log.e("Conexion","Desconectados");
                    seleccionador=false;
                    break;
                //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
            }//FIN DE SWITCH CASE PRIMARIO DEL HANDLER
        }//FIN DE METODO INTERNO handleMessage
    };//Fin de Handler

}
