package com.pp.tdl.tdl;

/**
 * Created by mles on 15.04.2017.
 * Platformy programistyczne
 */

class Item {
    private Deadline deadline;
    private String description;
    private int id;
    public static int globalId = 0;

    Item(Deadline p_deadline, String p_description, int p_id)
    {
        deadline = p_deadline;
        description = p_description;
        id = p_id;
    }

    Item(Deadline p_deadline, String p_description)
    {
        deadline = p_deadline;
        description = p_description;
        id = globalId;
        globalId++;
    }

    int getId() {
        return id;
    }
}
