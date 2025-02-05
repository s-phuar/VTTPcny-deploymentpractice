package vttp.batch5.paf.day27.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import vttp.batch5.paf.day27.models.LineItem;
import vttp.batch5.paf.day27.models.PurchaseOrder;
import vttp.batch5.paf.day27.sql.sql;

@Repository
public class LineItemRepository {
    @Autowired
    private JdbcTemplate template;

    //batch update
    public void insertLineItem(PurchaseOrder po){
        List<LineItem> lineItemList= po.getLineItems();
        String poId = po.getPoId();

        List<Object[]> data = lineItemList.stream()
            .map( li ->{
                Object[] obj = new Object[4];
                obj[0] = li.getName();
                obj[1] = li.getQuantity();
                obj[2] = li.getUnitPrice();
                obj[3] = poId;
                return obj;
            }).toList();

            template.batchUpdate(sql.sql_InsertLineItem, data);
    }

}
