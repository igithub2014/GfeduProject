package jc.cici.android.atom.bean;

/**
 * 下载信息
 * Created by atom on 2017/5/19.
 */

public class DownloadInfo {

    // 资料id
    private int FileID;
    // 资料名称
    private String FileName;
    // 资料时长
    private String FileSize;
    // 资料类型
    private String FileType;
    // 下载地址
    private String FileUrl;

    public int getFileID() {
        return FileID;
    }

    public void setFileID(int fileID) {
        FileID = fileID;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileSize() {
        return FileSize;
    }

    public void setFileSize(String fileSize) {
        FileSize = fileSize;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
    }

    public String getFileUrl() {
        return FileUrl;
    }

    public void setFileUrl(String fileUrl) {
        FileUrl = fileUrl;
    }
}
