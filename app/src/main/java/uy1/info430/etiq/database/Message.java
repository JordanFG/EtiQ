package uy1.info430.etiq.database;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import uy1.info430.etiq.database.Application;

@DatabaseTable
public class Message {

    @DatabaseField(columnName = "IDMESSAGE",generatedId = true)
    private int idMessage;
    @DatabaseField(columnName = "USERMESSAGE",dataType = DataType.LONG_STRING)
    private String userMessage;
    @DatabaseField(columnName = "TEXTMESSAGE",dataType = DataType.LONG_STRING)
    private String textMessage;
    @DatabaseField(columnName = "ETIQUETTE",dataType = DataType.LONG_STRING)
    private String etiquette;
    @DatabaseField(columnName = "CHOIX")
    private int choix;
    @DatabaseField(columnName = "SEND", dataType = DataType.BOOLEAN)
    private boolean send;
    @DatabaseField(canBeNull = false,foreign = true,foreignColumnName = "IDAPPLICATION")
    private Application application;


    public Message() {
    }

    public Message(String textMessage, String userMessage, Application application, int choix, String etiquette, boolean send) {
        this.textMessage = textMessage;
        this.userMessage = userMessage;
        this.application = application;
        this.choix = choix;
        this.etiquette = etiquette;
        this.send = send;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }


    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public String getEtiquette() {
        return etiquette;
    }

    public void setEtiquette(String etiquette) {
        this.etiquette = etiquette;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public int getChoix() {
        return choix;
    }

    public void setChoix(int choix) {
        this.choix = choix;
    }

    public boolean isSend() {
        return send;
    }
    public void setSend(boolean send) {
        this.send = send;
    }
}
