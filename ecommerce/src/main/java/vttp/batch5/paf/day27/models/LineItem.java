package vttp.batch5.paf.day27.models;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public class LineItem {
    private int id;
    private String name;
    private int quantity;
    private float unitPrice;
    private int poId;

    public void setId(int id) { this.id = id; }
    public int getId() { return this.id; }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    public void setQuantity(int quantity) { this.quantity = quantity; }
    public int getQuantity() { return this.quantity; }

    public void setUnitPrice(float unitPrice) { this.unitPrice = unitPrice; }
    public float getUnitPrice() { return this.unitPrice; }

    public void setPoId(int poId) { this.poId = poId; }
    public int getPoId() { return this.poId; }

    @Override
    public String toString() {
        return "LineItem[id=%d, name=%s, quantity=%d, unitPrice=%f]"
                .formatted(id, name, quantity, unitPrice);

    }

    
    public static JsonObject toJson(LineItem lineItem){
        JsonObject jObj = Json.createObjectBuilder()
            .add("id", lineItem.getId())
            .add("name", lineItem.getName())
            .add("quantity", lineItem.getQuantity())
            .add("unitPrice", lineItem.getUnitPrice())
            .add("poId", lineItem.getPoId())
            .build();
        return jObj;
    }


}
