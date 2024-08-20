package com.structurizr.dsl;

import com.structurizr.model.ModelItem;

abstract class ModelItemDslContext extends DslContext {

    ModelItemDslContext() {
        super();
    }

    abstract ModelItem getModelItem();

}