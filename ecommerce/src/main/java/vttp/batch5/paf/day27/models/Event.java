package vttp.batch5.paf.day27.models;

import java.util.Date;


public class Event {

    private String eventId;
    private String operation;
    private Date timeStamp;
    private PurchaseOrder po;

    public String getEventId() {return eventId;}
    public void setEventId(String eventId) {this.eventId = eventId;}
    public String getOperation() {return operation;}
    public void setOperation(String operation) {this.operation = operation;}
    public Date getTimeStamp() {return timeStamp;}
    public void setTimeStamp(Date timeStamp) {this.timeStamp = timeStamp;}
    public PurchaseOrder getPo() {return po;}
    public void setPo(PurchaseOrder po) {this.po = po;}


    
    
}
