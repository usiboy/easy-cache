package com.github.usiboy.easycache;

/**
 * @author liumingjian
 * @date 2018/8/28
 **/
public abstract class AbstractEasyCache implements EasyCache{

    protected String name;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
