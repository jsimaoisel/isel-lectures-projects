package service;

import java.util.List;

public class Document {
    public String docID;
    public String blobName;
    public String contentType;
    public String hashID;
    public int nBlocks;
    public long size;
    public List<String> labels;
    public List<String> labelsTranslated;
}
