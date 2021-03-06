package com.teste.devjr.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class DownAndUpFile {


    private static final Logger log = LoggerFactory.getLogger(DownAndUpFile.class);


    @Scheduled(fixedRate = 10000)
    public void ftpConnect() {
        String server = "3.86.89.252";
        int port = 21;
        String user = "tainan";
        String pass = "890iop";

        FTPClient ftpClient = new FTPClient();

        try {
            // connect and login to the server
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            System.out.println("Connected");

            String remoteDirPath = "pending";
            String saveDirPath = "";


            File file = new File("pending");
            File[] files = file.listFiles();

            FTPFile[] subFiles = ftpClient.listFiles(remoteDirPath);


            if (files == null || files.length < subFiles.length) {
                DownAndUpFile.downloadDirectory(ftpClient, remoteDirPath, "", saveDirPath);
            } else {
                log.info("Files already downloaded.");
            }

            remoteDirPath = "processed";
            saveDirPath = "";

            File processedFolder = new File("processed");
            File[] processedFile = processedFolder.listFiles();

            FTPFile[] processedFiles = ftpClient.listFiles(remoteDirPath);

            assert processedFile != null;
            if (processedFiles == null || processedFiles.length < processedFile.length) {
                DownAndUpFile.uploadProcessedFiles(processedFile, ftpClient);
            }

            // log out and disconnect from the server
            ftpClient.logout();
            ftpClient.disconnect();

            System.out.println("Disconnected");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static boolean downloadSingleFile(FTPClient ftpClient,
                                             String remoteFilePath,
                                             String savePath) throws IOException {
        File downloadFile = new File(savePath);

        File parentDir = downloadFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        OutputStream outputStream = new BufferedOutputStream(
                new FileOutputStream(downloadFile));
        try {
            ftpClient.setFileType(FTP.ASCII_FILE_TYPE);

            return ftpClient.retrieveFile(remoteFilePath, outputStream);
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (outputStream != null) {
                outputStream.close();
            }
        }
    }

    public static void downloadDirectory(FTPClient ftpClient,
                                         String parentDir,
                                         String currentDir,
                                         String saveDir) throws IOException {

        String dirToList = parentDir;
        if (!currentDir.equals("")) {
            dirToList += "/" + currentDir;
        }

        FTPFile[] subFiles = ftpClient.listFiles(dirToList);

        File file = new File("pending");
        File[] files = file.listFiles();


        if (subFiles != null && subFiles.length > 0) {
            for (FTPFile aFile : subFiles) {
                String currentFileName = aFile.getName();
                if (currentFileName.equals(".") || currentFileName.equals("..")) {
                    // skip parent directory and the directory itself
                    continue;
                }
                String filePath = parentDir + "/" + currentDir + "/"
                        + currentFileName;
                if (currentDir.equals("")) {
                    filePath = parentDir + "/" + currentFileName;
                }

                String newDirPath = saveDir + parentDir + File.separator
                        + currentDir + File.separator + currentFileName;
                if (currentDir.equals("")) {
                    newDirPath = saveDir + parentDir + File.separator
                            + currentFileName;
                }

                if (aFile.isDirectory()) {
                    // create the directory in saveDir
                    File newDir = new File(newDirPath);
                    boolean created = newDir.mkdirs();
                    if (created) {
                        log.info("CREATED the directory: " + newDirPath);
                    } else {
                        log.info("COULD NOT create the directory: " + newDirPath);
                    }

                    // download the sub directory
                    downloadDirectory(ftpClient, dirToList, currentFileName,
                            saveDir);
                } else {
                    // download the file

                    boolean success = downloadSingleFile(ftpClient, filePath,
                            newDirPath);
                    if (success) {
                        log.info("DOWNLOADED the file: " + filePath);
                    } else {
                        log.info("COULD NOT download the file: "
                                + filePath);
                    }
                }
            }
        }
    }

    public static void uploadProcessedFiles(File[] processedFile, FTPClient ftpClient) throws IOException {

        for (File f : processedFile) {

            FileInputStream fis = new FileInputStream(f);

            boolean success = ftpClient.storeFile("processed/" + f.getName(), fis);

            if (success) {
                log.info("Sending file " + f.getName() + " to server.");
            } else {
                log.info("Error sending file " + f.getName() + " to server.");
            }
        }
    }
}
