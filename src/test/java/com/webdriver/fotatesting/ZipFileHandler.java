package com.webdriver.fotatesting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by hongyuechi on 3/10/16.
 */

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipFileHandler {

    public static void main(String[] args) throws IOException {
//        String inZip = "/Users/hongyuechi/Documents/lecar/fota_update/sample-rom-version-1s2.0.zip";
//        String outZip = "/Users/hongyuechi/Documents/lecar/fota_update/sample-rom-version-1s2.0auto.zip";
////        String destinationFolder = "/Users/hongyuechi/Documents/lecar/fota_update/";
//        String infoPath = "sample-rom-version-1s2.0/info.properties";
//        Map<String, String> updates = new HashMap<String, String>();
//        updates.put("model", "auto test1");
//        updates.put("sku", "126");
//        updateZipPropEntry(inZip, outZip, infoPath, updates);
//        unzipFunction(destinationFolder,zipFile);
//        try {
//            modifyFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
    public static void modifyFile() throws IOException{
        ZipFile zipFile = new ZipFile("/Users/hongyuechi/Documents/lecar/fota_update/sample-rom-version-1s2.0.zip");
        final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream("/Users/hongyuechi/Documents/lecar/fota_update/sample-rom-version-1s2.0auto.zip"));
        for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
            ZipEntry entryIn = (ZipEntry) e.nextElement();
            if (!entryIn.getName().equalsIgnoreCase("sample-rom-version-1s2.0/info.properties")) {
                zos.putNextEntry(entryIn);
                InputStream is = zipFile.getInputStream(entryIn);
                byte[] buf = new byte[1024];
                int len;
                while ((len = (is.read(buf))) > 0) {
                    zos.write(buf, 0, len);
                }
            } else {
                zos.putNextEntry(new ZipEntry("sample-rom-version-1s2.0/info.properties"));

                InputStream is = zipFile.getInputStream(entryIn);
                BufferedReader br=new BufferedReader(new InputStreamReader(is));
                String s=br.readLine();
                while (s!=null) {

                    if (s.contains("model=auto test org") || s.contains("sku=123")) {
                        s = s.replaceAll("model=auto test org", "model=auto test1");
                        s = s.replaceAll("sku=123", "sku=126");
                    }
                    zos.write(s.getBytes());
                    zos.write(new byte[]{10});
                    System.out.println(s);
                    s=br.readLine();

                }
            }
            zos.closeEntry();
        }
        zos.close();
    }

    public static void updateZipPropEntry(String inZip, String outZip, String entryPath,
                                          Map<String, String> updates) throws IOException {
        ZipFile zipFile = new ZipFile(inZip);
        final ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(outZip));
        try {
            Enumeration e = zipFile.entries();
            while (e.hasMoreElements()) {
                ZipEntry entryIn = (ZipEntry) e.nextElement();
                InputStream in = zipFile.getInputStream(entryIn);
                try {
                    byte[] buff = new byte[10240];
                    int len;
                    if (!entryIn.getName().equals(entryPath)) {
                        zos.putNextEntry(entryIn);
                        while ((len = (in.read(buff))) > 0) {
                            zos.write(buff, 0, len);
                        }
                    } else {
                        zos.putNextEntry(new ZipEntry(entryPath));
                        StringBuilder sb = new StringBuilder();
                        BufferedReader br = new BufferedReader(new InputStreamReader(in));
                        String s;
                        while ((s = br.readLine()) != null) {
                            for (String key : updates.keySet()) {
                                if (s.contains(key + "=")) {
                                    s = key + "=" + updates.get(key);
                                    break;
                                }
                            }
                            sb.append(s).append('\n');
                        }
//                        System.out.println(sb);
                        zos.write(sb.toString().getBytes());
                    }
                    zos.closeEntry();
                } finally {
                    in.close();
                }
            }
        } finally {
            zos.close();
        }
    }

    private static void unzipFunction(String destinationFolder, String zipFile) {
        File directory = new File(destinationFolder);

        // if the output directory doesn't exist, create it
        if(!directory.exists())
            directory.mkdirs();

        // buffer for read and write data to file
        byte[] buffer = new byte[2048];

        try {
            FileInputStream fInput = new FileInputStream(zipFile);
            ZipInputStream zipInput = new ZipInputStream(fInput);

            ZipEntry entry = zipInput.getNextEntry();

            while(entry != null){
                String entryName = entry.getName();
                File file = new File(destinationFolder + File.separator + entryName);

                System.out.println("Unzip file " + entryName + " to " + file.getAbsolutePath());

                // create the directories of the zip directory
                if(entry.isDirectory()) {
                    File newDir = new File(file.getAbsolutePath());
                    if(!newDir.exists()) {
                        boolean success = newDir.mkdirs();
                        if(success == false) {
                            System.out.println("Problem creating Folder");
                        }
                    }
                }
                else {
                    FileOutputStream fOutput = new FileOutputStream(file);
                    int count = 0;
                    while ((count = zipInput.read(buffer)) > 0) {
                        // write 'count' bytes to the file output stream
                        fOutput.write(buffer, 0, count);
                    }
                    fOutput.close();
                }
                // close ZipEntry and take the next one
                zipInput.closeEntry();
                entry = zipInput.getNextEntry();
            }

            // close the last ZipEntry
            zipInput.closeEntry();

            zipInput.close();
            fInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

