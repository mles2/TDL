package com.pp.tdl.tdl;


import java.util.Vector;

/**
 * Created by mles on 15.04.2017.
 * Platformy Programistyczne
 */

public class TodoList {
    private Vector<Item> m_items;

    public void loadItemsFromNetwork(){

    }

    public void saveItemsToNetwork(){

    }

    public void addItem(Item p_item){
        m_items.add(p_item);
    }

    public void removeItem(int p_id){
        for(int i = 0; i < m_items.size(); ++i)
            if(m_items.get(i).getId() == p_id)
                m_items.remove(i);
    }

}