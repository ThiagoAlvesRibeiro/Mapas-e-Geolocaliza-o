package com.example.maps;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissoes {
    public static Boolean validarPermissoes(String[] Permissoes, Activity activity, int requestcode){
        if(Build.VERSION.SDK_INT >= 23){
            List<String> listadePermissoes = new ArrayList<>();
            for (String permissão:Permissoes) {
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity,permissão) ==
                        PackageManager.PERMISSION_GRANTED;
                        if( !temPermissao ) listadePermissoes.add(permissão);

                        if ( listadePermissoes.isEmpty() ) return true;
                        String[] novasPermissoes = new String[listadePermissoes.size()];
                        listadePermissoes.toArray(novasPermissoes);



                        ActivityCompat.requestPermissions(activity, novasPermissoes, requestcode);
            }
        }
        return true;
    }
}
