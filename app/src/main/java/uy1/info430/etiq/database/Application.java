package uy1.info430.etiq.database;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "T_Application")
public class Application {

    @DatabaseField(columnName = "IDAPPLICATION",generatedId = true)
    private int idApplication;
    @DatabaseField(columnName = "NAMEAPP",dataType = DataType.LONG_STRING)
    private String nameApp;
    @DatabaseField(columnName = "PACK",dataType = DataType.LONG_STRING)
    private String pack;

    @DatabaseField(columnName = "PRINT",dataType = DataType.BOOLEAN)
    private boolean print;

    @ForeignCollectionField(eager = false)
    private ForeignCollection<Message> messages;

    public Application() {
    }

    public Application(String nameApp, String pack, Boolean print) {
        this.nameApp = nameApp;
        this.pack = pack;
        this.print = print;
    }

    public int getIdApplication() {
        return idApplication;
    }

    public void setIdApplication(int idApplication) {
        this.idApplication = idApplication;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public ForeignCollection<Message> getMessages() {
        return messages;
    }

    public void setMessages(ForeignCollection<Message> messages) {
        this.messages = messages;
    }

    public boolean isPrint() { return print; }
    public void setPrint(boolean print) {this.print = print;}
}