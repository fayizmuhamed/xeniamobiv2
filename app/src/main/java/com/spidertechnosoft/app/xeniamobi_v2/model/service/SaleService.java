package com.spidertechnosoft.app.xeniamobi_v2.model.service;

import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.CategorySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.DailySummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.IndicatorStatus;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.ItemSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.PaymentType;
import com.spidertechnosoft.app.xeniamobi_v2.model.dictionary.StaffSummary;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.Sale;
import com.spidertechnosoft.app.xeniamobi_v2.model.domain.SaleLineItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaleService implements IEntityService<Sale> {


    @Override
    public Long save(Sale sale) {

        // check if the sale is null
        if ( sale == null ) return null;

        Sale existingSale=findByUid(sale.getSaleUid());

        if(existingSale!=null){

            sale.setId(existingSale.getId());

            SaleLineItem.deleteAll(SaleLineItem.class,"sale_Uid = ?",existingSale.getSaleUid());

        }

        List<SaleLineItem> saleLineItemList=sale.getItems();

        for(SaleLineItem saleLineItem:saleLineItemList){

            SaleLineItem.save(saleLineItem);
        }

        return sale.save();

    }

    @Override
    public Sale findById(Long id) {


        // the user by id
        return  Sale.findById(Sale.class, id);
    }

    @Override
    public List<Sale> findByQuery(String whereClause, String[] whereArgs, String groupBy, String orderBy, String limit) {


        // Return the list
        return Sale.find(Sale.class, whereClause, whereArgs, groupBy, orderBy, limit);
    }

    @Override
    public boolean delete(Long id) {

        // Create the Product object
        Sale sale = Sale.findById(Sale.class, id);

        // Return the response after delete
        return sale.delete();
    }

    @Override
    public boolean delete(Sale sale) {


        // Return the response after delete
        return sale.delete();
    }

    public Long deleteSale(Sale sale) {

        Sale existingSale=findByUid(sale.getSaleUid());

        if(existingSale!=null){

            existingSale.setDeleted(IndicatorStatus.YES);

            return existingSale.save();

        }

        // Return the response after delete
        return -1L;
    }

    public Sale findByUid(String uid) {

        // Create the Category object
        List<Sale> sales = new ArrayList<>();

        // the user by username
        sales=  Sale.find(Sale.class, "sale_Uid = ?", uid);

        return sales.size()>0?sales.get(0):null;

    }

    public List<Sale> findSaleSummaryList(String fromDatetime,String toDateTime) {


        String query="deleted=? AND sales_Date between ? AND ? ";

        String[] params=new String[]{String.valueOf(IndicatorStatus.NO),fromDatetime,toDateTime};

        // Return the list
        List<Sale> sales=Sale.find(Sale.class, query,params, null, "sales_Date ASC", null);

        if(sales==null||sales.isEmpty())
            return new ArrayList<>();

        return sales;
    }

    public List<ItemSummary> findItemSummaryList(String fromDatetime,String toDateTime) {

        HashMap<String,ItemSummary> itemSummaryHashMap=new HashMap<>();

        String query="deleted=? AND sales_Date between ? AND ? ";

        String[] params=new String[]{String.valueOf(IndicatorStatus.NO),fromDatetime,toDateTime};

        // Return the list
        List<Sale> sales=Sale.find(Sale.class, query,params, null, "sales_Date ASC", null);

        if(sales==null||sales.isEmpty())
            return new ArrayList<>();

        for(Sale sale:sales){

            List<SaleLineItem> saleLineItems=sale.getSaleLineItems();

            if(saleLineItems==null||saleLineItems.isEmpty())
                continue;

            for(SaleLineItem saleLineItem:saleLineItems){

                String key=saleLineItem.getItemUid()+"#"+saleLineItem.getItemRate();

                ItemSummary itemSummary=null;

                if(itemSummaryHashMap.containsKey(key)){

                    itemSummary=itemSummaryHashMap.get(key);
                }else{
                    itemSummary=new ItemSummary();
                    itemSummary.setItemUid(saleLineItem.getItemUid());
                    itemSummary.setItemName(saleLineItem.getItemName());
                    itemSummary.setItemPrice(saleLineItem.getItemRate());
                    itemSummary.setItemQty(0);
                    itemSummary.setItemAmount(0.0);
                }

                itemSummary.setItemQty(itemSummary.getItemQty()+saleLineItem.getQty());
                itemSummary.setItemAmount(itemSummary.getItemAmount()+saleLineItem.getNetAmount());

                itemSummaryHashMap.put(key,itemSummary);
            }
        }

        return new ArrayList<>(itemSummaryHashMap.values());
    }

    public List<CategorySummary> findCategorySummaryList(String fromDatetime, String toDateTime) {

        HashMap<String,CategorySummary> categorySummaryHashMap=new HashMap<>();

        String query="deleted=? AND sales_Date between ? AND ? ";

        String[] params=new String[]{String.valueOf(IndicatorStatus.NO),fromDatetime,toDateTime};

        // Return the list
        List<Sale> sales=Sale.find(Sale.class, query,params, null, "sales_Date ASC", null);

        if(sales==null||sales.isEmpty())
            return new ArrayList<>();

        for(Sale sale:sales){

            List<SaleLineItem> saleLineItems=sale.getSaleLineItems();

            if(saleLineItems==null||saleLineItems.isEmpty())
                continue;

            for(SaleLineItem saleLineItem:saleLineItems){

                String key=saleLineItem.getCategoryUid();

                CategorySummary categorySummary=null;

                if(categorySummaryHashMap.containsKey(key)){

                    categorySummary=categorySummaryHashMap.get(key);
                }else{
                    categorySummary=new CategorySummary();
                    categorySummary.setCategoryUid(saleLineItem.getCategoryUid());
                    categorySummary.setCategoryName(saleLineItem.getCategoryName());
                    categorySummary.setCategoryQty(0);
                    categorySummary.setCategoryAmount(0.0);
                }

                categorySummary.setCategoryQty(categorySummary.getCategoryQty()+saleLineItem.getQty());
                categorySummary.setCategoryAmount(categorySummary.getCategoryAmount()+saleLineItem.getNetAmount());

                categorySummaryHashMap.put(key,categorySummary);
            }
        }

        return new ArrayList<>(categorySummaryHashMap.values());
    }

    public List<StaffSummary> findWaiterSummaryList(String fromDatetime, String toDateTime) {

        HashMap<String,StaffSummary> staffSummaryHashMap=new HashMap<>();

        String query="deleted=? AND sales_Date between ? AND ? ";

        String[] params=new String[]{String.valueOf(IndicatorStatus.NO),fromDatetime,toDateTime};

        // Return the list
        List<Sale> sales=Sale.find(Sale.class, query,params, null, "sales_Date ASC", null);

        if(sales==null||sales.isEmpty())
            return new ArrayList<>();

        for(Sale sale:sales){

            String key=sale.getUserId();

            StaffSummary staffSummary =null;

            if(staffSummaryHashMap.containsKey(key)){

                staffSummary =staffSummaryHashMap.get(key);
            }else{
                staffSummary =new StaffSummary();
                staffSummary.setUserUid(sale.getUserId());
                staffSummary.setUserName(sale.getUserName());
                staffSummary.setAmount(0.0);
            }

            staffSummary.setAmount(staffSummary.getAmount()+sale.getNetAmount());

            staffSummaryHashMap.put(key,staffSummary);
        }

        return new ArrayList<>(staffSummaryHashMap.values());
    }

    public DailySummary findDailySummary(String fromDatetime, String toDateTime) {

        DailySummary dailySummary=new DailySummary();

        String query="deleted=? AND sales_Date between ? AND ? ";

        String[] params=new String[]{String.valueOf(IndicatorStatus.NO),fromDatetime,toDateTime};

        // Return the list
        List<Sale> sales=Sale.find(Sale.class, query,params, null, "sales_Date ASC", null);

        if(sales==null||sales.isEmpty())
            return dailySummary;

        for(Sale sale:sales){

            dailySummary.setNoOfSales(dailySummary.getNoOfSales()+1);

            if(sale.getPaymentType().equals(PaymentType.CASH))
                dailySummary.setCashTransaction(dailySummary.getCashTransaction()+sale.getTotalAmount());
            else if(sale.getPaymentType().equals(PaymentType.CARD))
                dailySummary.setCardTransaction(dailySummary.getCardTransaction()+sale.getTotalAmount());
            else if(sale.getPaymentType().equals(PaymentType.CREDIT))
                dailySummary.setCreditTransaction(dailySummary.getCreditTransaction()+sale.getTotalAmount());
            else if(sale.getPaymentType().equals(PaymentType.BOTH))
                dailySummary.setBothTransaction(dailySummary.getBothTransaction()+sale.getTotalAmount());

            dailySummary.setTotalAmount(dailySummary.getTotalAmount()+sale.getTotalAmount());

            dailySummary.setTaxAmount(dailySummary.getTaxAmount()+sale.getTaxAmount());

            dailySummary.setGrossAmount(dailySummary.getGrossAmount()+sale.getGrossAmount());

            dailySummary.setBillDiscount(dailySummary.getBillDiscount()+sale.getDiscount());

            dailySummary.setRoundOff(dailySummary.getRoundOff()+sale.getRoundOff());

            dailySummary.setNetAmount(dailySummary.getNetAmount()+sale.getNetAmount());

            dailySummary.setCashReceived(dailySummary.getCashReceived()+sale.getCashReceived());

            dailySummary.setCardReceived(dailySummary.getCardReceived()+sale.getCardReceived());

        }

        return dailySummary;
    }

    public  Boolean isItemDeleteEnabled(String productUid) {

        // the user by username
        List<SaleLineItem> saleLineItems = SaleLineItem.find(SaleLineItem.class, "item_Uid = ?",new String[]{productUid},null, "item_Uid ASC",null);

        return (saleLineItems==null||saleLineItems.isEmpty())?true:false;
    }
}
