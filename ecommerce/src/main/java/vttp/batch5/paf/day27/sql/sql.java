package vttp.batch5.paf.day27.sql;

public class sql {

    public static final String sql_InsertPurchaseOrder = """
        insert into purchase_orders(po_id, name, address, delivery_date)
        values (?, ?, ?, ?)
            """;


    public static final String sql_InsertLineItem = """
        insert into line_items(name, quantity, unit_price, po_id)
        values(?, ?, ?, ?)
            """;


}
