package com.example.appventas2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;


import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    EditText etReferencia;
    EditText etDescripcion;
    EditText etCosto;
    EditText etExistencia;
    Button btnSave, btnSearch, btnUpdate, btnDelete, logoutButton ;
    TextView tvMessage, etValoriva;
    ClsVentas dbproducts = new ClsVentas(this, "dbproducts", null, 1);
    String oldReferencia;
    String newReferencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }



        etReferencia = findViewById(R.id.etreferencia);
        etDescripcion = findViewById(R.id.etdescripcion);
        etCosto = findViewById(R.id.etcosto);
        etExistencia = findViewById(R.id.etexistencia);
        etValoriva = findViewById(R.id.etvaloriva);
        btnSave = findViewById(R.id.btnsave);
        btnSearch = findViewById(R.id.btnsearch);
        btnUpdate = findViewById(R.id.btnupdate);
        btnDelete = findViewById(R.id.btndelete);
        logoutButton = findViewById(R.id.logoutButton);
        tvMessage = findViewById(R.id.tvmessage);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String referencia = etReferencia.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String costo = etCosto.getText().toString();
                String existencia = etExistencia.getText().toString();

                if (referencia.isEmpty() || descripcion.isEmpty() || costo.isEmpty() || existencia.isEmpty()) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("Todos los datos son obligatorios");
                    return;
                }

                int existenciaValue;
                try {
                    existenciaValue = Integer.parseInt(existencia);
                } catch (NumberFormatException e) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("La existencia debe ser un número entero");
                    return;
                }

                int costoValue;
                try {
                    costoValue = Integer.parseInt(costo);
                } catch (NumberFormatException e) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("El costo debe ser un número entero");
                    return;
                }

                if (existenciaValue < 5 || existenciaValue > 20) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("La existencia debe estar entre 5 y 20");
                    return;
                }

                if (costoValue <= 20000) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("El costo debe ser superior a 20 mil");
                    return;
                }

                saveProducts(referencia, descripcion, costo, existencia);
            }

            private void saveProducts(String sReferencia, String sDescripcion, String sCosto, String sExistencia) {
                SQLiteDatabase dbr = dbproducts.getReadableDatabase();
                String sql = "SELECT Referencia FROM Products WHERE Referencia ='" + etReferencia.getText().toString() + "'";
                Cursor cProductsr = dbr.rawQuery(sql, null);
                if (!cProductsr.moveToFirst()) {
                    double iva = 0.19;
                    SQLiteDatabase dbw = dbproducts.getWritableDatabase();
                    ContentValues cProducts = new ContentValues();
                    cProducts.put("Referencia", sReferencia);
                    cProducts.put("Descripcion", sDescripcion);
                    cProducts.put("Costo", sCosto);
                    cProducts.put("Existencia", sExistencia);
                    double costo = Double.parseDouble(sCosto);
                    int existencia = Integer.parseInt(sExistencia);
                    double costoTotal = costo * existencia;
                    double totalconiva = costoTotal + (costoTotal * iva);
                    DecimalFormat numberFormat = new DecimalFormat("###,###,###,###.##");
                    etValoriva.setText(numberFormat.format(totalconiva));

                    dbw.insert("Products", null, cProducts);
                    dbw.close();
                    tvMessage.setTextColor(Color.GREEN);
                    tvMessage.setText("Producto Agregado correctamente");
                } else {
                    tvMessage.setText("La Referencia ya existe. Intente otra vez");
                    tvMessage.setTextColor(Color.RED);
                }
            }
        });


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbr = dbproducts.getReadableDatabase();
                String query = "SELECT Referencia, Descripcion, Costo, Existencia, Valoriva FROM Products WHERE Referencia = '" + etReferencia.getText().toString() + "'";
                Cursor cursorProductosr = dbr.rawQuery(query, null);
                if (cursorProductosr.moveToFirst()) {
                    etDescripcion.setText(cursorProductosr.getString(1));
                    etCosto.setText(cursorProductosr.getString(2));
                    etExistencia.setText(cursorProductosr.getString(3));

                    double costo = Double.parseDouble(cursorProductosr.getString(2));
                    double iva = 0.19;
                    double costoTotal = costo * Double.parseDouble(cursorProductosr.getString(3));
                    double totalConIva = costoTotal + (costoTotal * iva);
                    DecimalFormat numberFormat = new DecimalFormat("###,###,###,###.##");
                    etValoriva.setText(numberFormat.format(totalConIva));

                    // Mostrar el valor de IVA en un Toast
                    //String mensajeIva = "Valor IVA: " + numberFormat.format(totalConIva);
                   // Toast.makeText(MainActivity.this, mensajeIva, Toast.LENGTH_SHORT).show();
                    tvMessage.setTextColor(Color.GREEN);
                    tvMessage.setText("Producto Encontrado");
                    oldReferencia = etReferencia.getText().toString();
                } else {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("La Referencia no existe. Intentalo nuevamente");
                }
            }
        });



        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!etReferencia.getText().toString().isEmpty()) {
                    SQLiteDatabase dbr = dbproducts.getReadableDatabase();
                    String query = "SELECT Referencia, Descripcion, Costo, Existencia, Valoriva FROM Products WHERE Referencia ='" + etReferencia.getText().toString() + "'";
                    Cursor cursorProductosr = dbr.rawQuery(query, null);
                    if (cursorProductosr.moveToFirst()) {
                        int existencia = cursorProductosr.getInt(3);
                        if (existencia == 0) {
                            AlertDialog.Builder adbConfirm = new AlertDialog.Builder(MainActivity.this);
                            adbConfirm.setMessage("Eliminar del servidor");
                            adbConfirm.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SQLiteDatabase dbdelete = dbproducts.getWritableDatabase();
                                    String referencia = etReferencia.getText().toString();

                                    // Verificar si la existencia es igual a 0
                                    if (existencia == 0) {
                                        // Eliminar el registro
                                        dbdelete.execSQL("DELETE FROM Products WHERE Referencia ='" + referencia + "'");
                                        tvMessage.setTextColor(Color.GREEN);
                                        tvMessage.setText("Referencia eliminada correctamente");

                                        // Limpiar el formulario
                                        etReferencia.setText("");
                                        etDescripcion.setText("");
                                        etCosto.setText("");
                                        etExistencia.setText("");
                                        etValoriva.setText("");
                                    } else {
                                        // Mostrar mensaje de error
                                        tvMessage.setTextColor(Color.RED);
                                        tvMessage.setText("No se puede eliminar la referencia. La existencia no es igual a 0");
                                    }

                                    dbdelete.close();
                                }
                            });
                            adbConfirm.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // Acciones adicionales si el usuario elige "No"
                                }
                            });
                            AlertDialog alertDialog = adbConfirm.create();
                            alertDialog.show();
                        } else {
                            tvMessage.setTextColor(Color.RED);
                            tvMessage.setText("No se puede eliminar un producto con existencia diferente de cero");
                        }
                    } else {
                        tvMessage.setTextColor(Color.RED);
                        tvMessage.setText("La Referencia no existe. Intente nuevamente");
                    }
                }
            }
        });


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase dbw = dbproducts.getWritableDatabase();
                String referencia = etReferencia.getText().toString();
                String descripcion = etDescripcion.getText().toString();
                String costo = etCosto.getText().toString();
                String existencia = etExistencia.getText().toString();

                // Validar campos obligatorios
                if (referencia.isEmpty() || descripcion.isEmpty() || costo.isEmpty() || existencia.isEmpty()) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("Todos los campos son obligatorios");
                    return;
                }

                // Validar costo superior a 20 mil
                int costoInt = Integer.parseInt(costo);
                if (costoInt <= 20000) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("El costo debe ser superior a 20,000");
                    return;
                }

                // Validar existencia entre 0 y 20
                int existenciaInt = Integer.parseInt(existencia);
                if (existenciaInt < 0 || existenciaInt > 20 || (existenciaInt != 0 && (existenciaInt < 5 || existenciaInt > 20))) {
                    tvMessage.setTextColor(Color.RED);
                    tvMessage.setText("La existencia debe ser 0 o estar entre 5 y 20");
                    return;
                }

                if (oldReferencia.equals(referencia)) {
                    dbw.execSQL("UPDATE Products SET Descripcion='" + descripcion + "', Costo='" + costo + "', Existencia='" + existencia + "', Valoriva='" + etValoriva.getText().toString() + "' WHERE Referencia='" + oldReferencia + "'");
                    double iva = 0.19;
                    int costoTotal = Integer.valueOf(costo) * existenciaInt;
                    double totalConIva = costoTotal + (costoTotal * iva);
                    DecimalFormat numberFormat = new DecimalFormat("###,###,###,###.##");
                    etValoriva.setText(numberFormat.format(totalConIva));
                    tvMessage.setText("Producto actualizado correctamente");
                    tvMessage.setTextColor(Color.GREEN);
                } else {
                    SQLiteDatabase dbr = dbproducts.getReadableDatabase();
                    String sql = "SELECT Referencia FROM Products WHERE Referencia ='" + referencia + "'";
                    Cursor cProductosl = dbr.rawQuery(sql, null);
                    if (!cProductosl.moveToFirst()) {
                        String newReferencia = referencia;
                        dbw.execSQL("UPDATE Products SET Referencia='" + newReferencia + "', Descripcion='" + descripcion + "', Costo='" + costo + "', Existencia='" + existencia + "', Valoriva='" + etValoriva.getText().toString() + "' WHERE Referencia='" + oldReferencia + "'");
                        double iva = 0.19;
                        int costoTotal = Integer.valueOf(costo) * existenciaInt;
                        double totalConIva = costoTotal + (costoTotal * iva);
                        DecimalFormat numberFormat = new DecimalFormat("###,###,###,###.##");
                        etValoriva.setText(numberFormat.format(totalConIva));
                        tvMessage.setText("Referencia actualizada correctamente");
                    } else {
                        tvMessage.setTextColor(Color.RED);
                        tvMessage.setText("La Referencia ya existe");
                    }
                    cProductosl.close(); // Cerrar el cursor
                    dbr.close();
                }
                dbw.close();
            }
        });

        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agrega el código para redirigir al usuario a LoginActivity
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Opcional: finaliza la actividad actual para que no se pueda volver atrás
            }
        });



    }
}
