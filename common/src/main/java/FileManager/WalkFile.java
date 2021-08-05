package FileManager;

import java.io.File;

public class WalkFile{

        private String absolutePathWalk;
        private String absolutePathVisitFile;
        private String relativePath;
        private String hideRootServer;


        public WalkFile (String absolutePathWalk, String absolutePathVisitFile) {
            this.absolutePathWalk = absolutePathWalk;
            this.absolutePathVisitFile = absolutePathVisitFile;
            this.relativePath = getRelativePath();
            this.hideRootServer = getHideRootServer();
        }


        public String getAbsolutePathVisitFile () {
            return absolutePathVisitFile;
        }

        public String getHideRootServer () {

        hideRootServer = "";

        int rootLength = absolutePathWalk.split("\\\\").length;

        String[] p = absolutePathVisitFile.split("\\\\");

        for (int i = rootLength; i < p.length - 1; i++) {

            if (hideRootServer.length() > 1){
                hideRootServer  += "\\";
            }

            hideRootServer += p[i];
        }

        return hideRootServer;
    }

    public String getRelativePath () {

            relativePath = "";

            int rootLength = absolutePathWalk.split("\\\\").length;

            String[] p = absolutePathVisitFile.split("\\\\");
            for (int i = rootLength - 1; i < p.length; i++) {
                if (relativePath.length() > 1){
                    relativePath  += "\\";
                }
                relativePath += p[i];
            }

            return relativePath;
        }

        public File getFile(){
            return new File(getAbsolutePathVisitFile());
        }

        public String getFullName(){
            return getFile().getName();
        }

        public String getPathDirs(){
            return new File(getRelativePath()).getParent();
        }

         public boolean isDirectory () {
            return getFile().isDirectory();
         }

    @Override
    public String toString () {
        return "WalkFile{" +
                "absolutePathWalk='" + absolutePathWalk + '\'' +
                ", absolutePathVisitFile='" + absolutePathVisitFile + '\'' +
                ", relativePath='" + relativePath + '\'' +
                ", hideRootServer='" + hideRootServer + '\'' +
                '}';
    }
}