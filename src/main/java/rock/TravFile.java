package rock;


import dao.Producer_Dao;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class TravFile {
    static {
        PropertyConfigurator.configure(System.getProperty("user.dir") + File.separator
                + "log4j.properties");
    }
    public static Logger logger1 = Logger.getLogger("ReadData");

    public static List<String> listAll(String dir, String tablename, String datadate,String HA,String path) throws IOException {

        if (StringUtils.isBlank(dir)) {
            return new ArrayList<String>();
        }
        dir=path.replace("${TX_DT}",datadate);
        dir=HA+dir;
        System.out.println(dir);
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(URI.create(dir), conf);
        if (fs.exists(new Path(dir))) {
        } else {
            logger1.info("hdfs目录不存在");
            System.exit(-1);
        }
        FileStatus[] stats = fs.listStatus(new Path(dir));
        List<String> names = new ArrayList<String>();
        for (int i = 0; i < stats.length; ++i) {
            if (stats[i].isFile()) {
                // regular file
                names.add(stats[i].getPath().toString());
            } else if (stats[i].isDirectory()) {
                // dir
                names.add(stats[i].getPath().toString());
            } else if (stats[i].isSymlink()) {
                // is s symlink in linux
                names.add(stats[i].getPath().toString());
            }
        }

        fs.close();
        return names;
    }

}
