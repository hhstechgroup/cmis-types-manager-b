package com.engagepoint.view;

import org.primefaces.event.RowEditEvent;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.ArrayList;

@ManagedBean(name = "item")
@ViewScoped
public class Item implements Serializable {
    private String item;
    private Integer qty;
    private Double price;
    private static final ArrayList<OrderBean> orderList = new ArrayList<OrderBean>();

    OrderBean order;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public OrderBean getOrder() {
        return order;
    }
    public void setOrder(OrderBean order) {
        this.order = order;
    }

    public ArrayList<OrderBean> getOrderList() {
        return orderList;
    }

    public String addAction() {
        OrderBean orderitem = new OrderBean(this.item, this.qty, this.price);
        orderList.add(orderitem);
        item = "";
        qty = 0;
        price = 0.0;
        return null;
    }
    public void onEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Edited",((OrderBean) event.getObject()).getItem());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Item Cancelled");
        FacesContext.getCurrentInstance().addMessage(null, msg);
        orderList.remove((OrderBean) event.getObject());
    }
}
