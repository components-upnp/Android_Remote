package com.components.createch.androidremote.upnp;

import android.os.Build;
import android.os.Environment;

import org.fourthline.cling.model.types.UDN;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.UUID;

import xdroid.toaster.Toaster;

/**
 * Created by mkostiuk on 03/05/2017.
 */

public class SaveUDN {

    public UDN getUdn(String name) throws IOException {

        UDN ret;
        File fi;

        if (Build.BRAND.toString().equals("htc_europe"))
            fi = new File("/mnt/emmc/"+ name+"/udn.txt");
        else
            fi = new File(Environment.getExternalStorageDirectory().getPath() + "/"+name+"/udn.txt");

        if (!fi.exists()) {
            fi.createNewFile();
            fi.setWritable(true);
        }

        InputStream is = new FileInputStream(fi);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = br.readLine();


        if ((line != null) || (line == "")) {
            ret =  UDN.valueOf(line);
        }
        else {
            ret = new UDN(UUID.randomUUID());
            OutputStream o = new FileOutputStream(fi);
            o.write(ret.toString().getBytes());
        }
        Toaster.toast("UDN: " + ret.toString());
        return ret;
    }
}
