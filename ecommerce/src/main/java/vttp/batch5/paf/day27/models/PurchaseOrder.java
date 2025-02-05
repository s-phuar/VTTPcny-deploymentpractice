package vttp.batch5.paf.day27.models;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class PurchaseOrder {

    private String poId;
    private String name;
    private String address;
    private Date deliveryDate;
    private List<LineItem> lineItems = new LinkedList<>();

    public void setPoId(String poId) { this.poId = poId;}
    public String getPoId() { return this.poId; }

    public void setName(String name) { this.name = name;}
    public String getName() { return this.name; }

    public void setAddress(String address) { this.address = address;}
    public String getAddress() { return this.address; }

    public void setDeliveryDate(Date deliveryDate) { this.deliveryDate = deliveryDate;}
    public Date getDeliveryDate() { return this.deliveryDate; }

    public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems;}
    public List<LineItem> getLineItems() { return this.lineItems; }
    public void addLineItem(LineItem lineItem) { this.lineItems.add(lineItem); }

    @Override
    public String toString() {
        return "PurchaseOrder[poId=%s, name=%s, address=%s, deliveryDate=%s lineItems=%d]"
            .formatted(poId, name, address, deliveryDate, lineItems.size());
    }


    public static JsonObject toJson(PurchaseOrder po){
        List<LineItem> items = po.getLineItems();
        JsonArrayBuilder jArray = Json.createArrayBuilder();
        items.stream()
            .map(li ->(LineItem.toJson(li)))
            .forEach(j -> jArray.add(j));



        JsonObject jObj = Json.createObjectBuilder()
            .add("poId", po.getPoId())
            .add("name", po.getName())
            .add("address", po.getAddress())
            .add("deliveryDate", po.getDeliveryDate().toString())
            .add("lineItems", jArray)
            .build();

        return jObj;
    }


}

