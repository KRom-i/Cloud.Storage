package DataTransferTools;

import java.io.Serializable;

public class RefreshPack implements Serializable {

    private String currentDirectory;

    private long currentSizeStorage;
    private long currentMaxSize;

    private long[] sizesStorage;

    private String[] arrayFileInfo;

    public RefreshPack (String currentDirectory, long currentSizeStorage, long currentMaxSize,
                        long[] sizesStorage, String[] arrayFileInfo) {

        this.currentDirectory = currentDirectory;
        this.currentSizeStorage = currentSizeStorage;
        this.currentMaxSize = currentMaxSize;
        this.sizesStorage = sizesStorage;
        this.arrayFileInfo = arrayFileInfo;

    }

    public String getCurrentDirectory () {
        return currentDirectory;
    }

    public long getCurrentSizeStorage () {
        return currentSizeStorage;
    }

    public long getCurrentMaxSize () {
        return currentMaxSize;
    }

    public String[] getArrayFileInfo () {
        return arrayFileInfo;
    }

    public long[] getSizesStorage () {
        return sizesStorage;
    }
}
