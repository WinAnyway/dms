package pl.com.bottega.dms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class DmsApplication {
//    D:\hsqldb-2.3.4\hsqldb
//    java -cp lib/hsqldb.jar org.hsqldb.server.Server --database.0 file:D:/dmsResources/dmsDB --dbname.0 dms

    public static void main(String[] args) {
        ApplicationContext ctx = SpringApplication.run(DmsApplication.class, args);
    }

}
