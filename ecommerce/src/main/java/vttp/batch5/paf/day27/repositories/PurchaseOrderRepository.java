package vttp.batch5.paf.day27.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.day27.models.PurchaseOrder;
import vttp.batch5.paf.day27.sql.sql;

@Repository
public class PurchaseOrderRepository {
    @Autowired
    private JdbcTemplate template;

    public void insertPurchaseOrder(PurchaseOrder po){    
        template.update(sql.sql_InsertPurchaseOrder, po.getPoId(), po.getName(), po.getAddress(), po.getDeliveryDate());
    }



}
