package vttp.batch5.paf.day27.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vttp.batch5.paf.day27.Utils;
import vttp.batch5.paf.day27.models.PurchaseOrder;
import vttp.batch5.paf.day27.repositories.EventRepository;
import vttp.batch5.paf.day27.repositories.LineItemRepository;
import vttp.batch5.paf.day27.repositories.PurchaseOrderRepository;

@Service
public class PurchaseOrderService {

  @Autowired
  private PurchaseOrderRepository purchaseOrderRepository;

  @Autowired
  private LineItemRepository lineItemRepository;

  @Autowired
  private EventRepository eventRepository;

  public String createPurchaseOrder(PurchaseOrder po) {
    String poId = UUID.randomUUID().toString().substring(0, 8);

    //set poId inside po object
    po.setPoId(poId);

    return poId;
  }

  @Transactional
  public void insertPurchaseOrder(PurchaseOrder po){
    purchaseOrderRepository.insertPurchaseOrder(po);
    lineItemRepository.insertLineItem(po);

    //event storage
    //push event object into redis
    eventRepository.pushRedisMessage(Utils.poToEvent(po));
  }








}
