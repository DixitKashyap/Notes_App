package com.dixitkumar.mynotesapp;

public class TODOItems {
    private int id;
    private String isChecked;
    private String todoItem;

    public TODOItems(int id,String isChecked, String todoItem) {
        this.id = id;
        this.isChecked = isChecked;
        this.todoItem = todoItem;
    }
    public TODOItems(String isChecked,String todoItem){
        this.isChecked = isChecked;
        this.todoItem = todoItem;
    }
    public TODOItems(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setChecked(String checked) {
        isChecked = checked;
    }

    public String getTodoItem() {
        return todoItem;
    }

    public void setTodoItem(String todoItem) {
        this.todoItem = todoItem;
    }
}
